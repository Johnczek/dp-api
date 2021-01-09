package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundOrIncorrectPasswordRestException;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.mapper.UserMapper;
import cz.johnczek.dpapi.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public LoggedUserDetails loadUserByUsername(@NonNull String email) {
        return userRepository.findByEmailWithRolesFetched(email)
                .map(userMapper::entityToLoggedUserDetails)
                .orElseThrow(UserNotFoundOrIncorrectPasswordRestException::new);
    }
}
