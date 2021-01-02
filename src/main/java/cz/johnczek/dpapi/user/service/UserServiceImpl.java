package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.mapper.UserMapper;
import cz.johnczek.dpapi.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public LoggedUserDetails findByEmailOrFail(@NonNull String email) {
        return userRepository.findByEmailWithRolesFetched(email)
                .map(userMapper::entityToLoggedUserDetails)
                .orElseThrow(UserNotFoundRestException::new);
    }
}
