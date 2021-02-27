package cz.johnczek.dpapi.user.service;

import com.google.common.collect.Maps;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.RoleNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyExistsRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundOrIncorrectPasswordRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.core.security.jwt.JwtUtils;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.file.service.FileService;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.entity.UserRoleEntity;
import cz.johnczek.dpapi.user.enums.RoleEnum;
import cz.johnczek.dpapi.user.mapper.UserMapper;
import cz.johnczek.dpapi.user.repository.UserRepository;
import cz.johnczek.dpapi.user.repository.UserRoleRepository;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.request.UserChangeAvatarRequest;
import cz.johnczek.dpapi.user.request.UserChangePasswordRequest;
import cz.johnczek.dpapi.user.request.UserChangeRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final FileService fileService;

    private final UserRoleRepository userRoleRepository;

    private final BankAccountService bankAccountService;

    private final AddressService addressService;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(@NonNull String email, @NonNull String password) {

        Authentication authentication;

        try {
         authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            log.error("Authentication for user with login {} failed, bad credentials", email, e);

            throw new UserNotFoundOrIncorrectPasswordRestException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        LoggedUserDetails user = (LoggedUserDetails) authentication.getPrincipal();

        return getJwtResponse(jwtToken, user);
    }

    @Override
    public JwtResponse loggedUser() {

        LoggedUserDetails user = getLoggedPerson();
        String jwtToken = jwtUtils.generateJwtToken(getLoggedPerson());

        return getJwtResponse(jwtToken, user);
    }

    @Override
    @Transactional
    public void register(@NonNull RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed. User with email {} alreadz exists", registerRequest.getEmail());
            throw new UserAlreadyExistsRestException();
        }

        UserEntity user = userMapper.registerRequestToEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRegistered(LocalDateTime.now());
        userRepository.save(user);

        RoleEntity role = roleService.findByCode(RoleEnum.USER)
                .orElseThrow(() -> {
                    log.error("Registration failed. Role {} not found", RoleEnum.USER);

                    return new RoleNotFoundRestException(RoleEnum.USER.name());
                });

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public void patch(long id, @NonNull UserChangeRequest userChangeRequest) {

        UserEntity user = checkUserPermissionEditability(id);

        user.setFirstName(userChangeRequest.getFirstName());
        user.setLastName(userChangeRequest.getLastName());
        user.setDescription(userChangeRequest.getDescription());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, UserDto> findByItemIds(@NonNull Set<Long> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyMap();
        }

        List<UserDto> users = userRepository.findByItemIdsWithAvatarFetched(itemIds).stream()
                .map(userMapper::entityToDto).collect(Collectors.toList());

        return Maps.uniqueIndex(users, UserDto::getId);
    }

    @Override
    @Transactional
    public void updateUserAvatar(long id, @NonNull UserChangeAvatarRequest request) {

        // TODO delete previous avatar

        String pictureUUID = request.getAvatarUUID();
        FileEntity file = fileService.findByFileIdentifier(pictureUUID).orElseThrow(() -> {

            log.error("Update of user avatar with id {} failed. Item not with identifier {} found", id, pictureUUID);

            return new FileNotFoundRestException(pictureUUID);
        });

        UserEntity user = checkUserPermissionEditability(id);
        user.setAvatar(file);
    }

    @Override
    @Transactional
    public void updateUserPassword(long id, @NonNull UserChangePasswordRequest request) {

        UserEntity user = checkUserPermissionEditability(id);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    @Override
    @Transactional
    public Optional<BankAccountDto> addBankAccount(long userId, @NonNull BankAccountCreationRequest request) {
        UserEntity user = checkUserPermissionEditability(userId);

        return bankAccountService.addBankAccount(user, request);
    }

    @Override
    @Transactional
    public void deleteBankAccount(long bankAccountId, long userId) {
        checkUserPermissionEditability(userId);

        bankAccountService.deleteBankAccount(bankAccountId, userId);
    }

    @Override
    @Transactional
    public Optional<AddressDto> addAddress(long userId, @NonNull AddressCreationRequest request) {
        UserEntity user = checkUserPermissionEditability(userId);

        return addressService.addAddress(user, request);
    }

    @Override
    @Transactional
    public void deleteAddress(long addressId, long userId) {
        checkUserPermissionEditability(userId);

        addressService.deleteAddress(addressId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(long userId) {

        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("User retrieval failed. User with id {} not found", userId);

            return new UserNotFoundRestException(userId);
        });

        List<AddressDto> addresses = addressService.findByUserId(userId);
        List<BankAccountDto> bankAccounts = bankAccountService.findByUserId(userId);

        return Optional.of(userMapper.entityToDto(user, addresses, bankAccounts));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, UserDto> findByUserIds(@NonNull List<Long> userIds) {

        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }

        List<UserEntity> users = userRepository.findByUserIds(userIds);
        Map<Long, List<AddressDto>> addressesMap = addressService.findByUserIds(userIds);
        Map<Long, List<BankAccountDto>> bankAccountsMap = bankAccountService.findByUserIds(userIds);

        return users.stream()
                .map(u -> userMapper.entityToDto(u, addressesMap.get(u.getId()), bankAccountsMap.get(u.getId())))
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findEntityById(long userId) {

        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("User retrieval failed. User with id {} not found", userId);

            return new UserNotFoundRestException(userId);
        });

        return Optional.of(user);
    }

    /**
     * Method checks if currently logged person has right to edit user with given id
     *
     * @param id id if user to be checked
     * @return desired user entity
     * @throws UserNotFoundRestException if user could not be found
     * @throws BaseForbiddenRestException if logged user is not the same as user with given id
     */
    private UserEntity checkUserPermissionEditability(long id) {

        LoggedUserDetails loggedUser = getLoggedPerson();

        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("Update of user with id {} failed. User not found", id);

            return new UserNotFoundRestException(id);
        });

        if (!user.getId().equals(loggedUser.getId())) {
            log.error("Update of user with id {} failed. Currently logged person with id {} does not match",
                    id, loggedUser.getId());

            throw new BaseForbiddenRestException();
        }

        return user;
    }

    /**
     * @return currently logged person
     * @throws BaseForbiddenRestException in case that there is no logged user
     */
    private LoggedUserDetails getLoggedPerson() {
        return SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Getting logged person failed. No user logged");

            return new BaseForbiddenRestException();
        });
    }

    /**
     * Method generated response for successfull user login request
     *
     * @param jwtToken user jwt token
     * @param user user data
     * @return filled response object
     */
    private JwtResponse getJwtResponse(@NonNull String jwtToken, LoggedUserDetails user) {
        return JwtResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .token(jwtToken)
                .roles(user.getUserRoles())
                .avatarUUID(user.getAvatarUUID())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
