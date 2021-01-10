package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.RoleNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyExistsRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.core.security.jwt.JwtUtils;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.entity.UserRoleEntity;
import cz.johnczek.dpapi.user.enums.RoleEnum;
import cz.johnczek.dpapi.user.mapper.UserMapper;
import cz.johnczek.dpapi.user.repository.UserRepository;
import cz.johnczek.dpapi.user.repository.UserRoleRepository;
import cz.johnczek.dpapi.user.request.PatchRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(@NonNull String email, @NonNull String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        LoggedUserDetails user = (LoggedUserDetails) authentication.getPrincipal();

        return JwtResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .token(jwtToken)
                .roles(user.getUserRoles())
                .build();
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
    public void patch(long id, @NonNull PatchRequest patchRequest) {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Update of user with id {} failed. Logged person not found", id);

            return new BaseForbiddenRestException();
        });

        if (!loggedUser.getId().equals(id)) {
            log.error("Update of user with id {} failed. Currently logged persons id {} does not match",
                    id, loggedUser.getId());

            throw new BaseForbiddenRestException();
        }

        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("Update of user with id {} failed. User with given id not found", id);

            return new UserNotFoundRestException(id);
        });

        user.setFirstName(patchRequest.getFirstName());
        user.setLastName(patchRequest.getLastName());
        user.setDescription(patchRequest.getDescription());
    }
}
