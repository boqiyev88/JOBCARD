package uz.uat.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Get the current authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is null or not authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Check if the principal is "anonymousUser"
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        // Get the principal (could be a String or UserDetails)
        Object principal = authentication.getPrincipal();

        // If the principal is a UserDetails, extract the username
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return Optional.of(((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
        }

        // If the principal is a String (e.g., username), return it directly
        if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        // Fallback: Return empty if the principal type is unknown
        return Optional.empty();
    }
}