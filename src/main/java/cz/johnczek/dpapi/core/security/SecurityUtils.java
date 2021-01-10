package cz.johnczek.dpapi.core.security;

import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    /**
     * @return {@code Logged person object} if there is any logged person, {@code Optional.empy()} otherwise
     */
    public static Optional<LoggedUserDetails> getLoggedUser() {
        return getLoggedUserAuthentication()
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (LoggedUserDetails) authentication.getPrincipal());
    }

    private static Optional<Authentication> getLoggedUserAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication);
    }
}
