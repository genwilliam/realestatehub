package com.example.realestatehub.util;

import com.example.realestatehub.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static UserPrincipal currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal;
        }
        return null;
    }

    public static Long currentUserId() {
        UserPrincipal principal = currentUser();
        if (principal == null) {
            return null;
        }
        return principal.getUser().getId();
    }
}

