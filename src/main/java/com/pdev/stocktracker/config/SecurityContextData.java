package com.pdev.stocktracker.config;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityContextData {

    public static JWTUserData getUserData() {
        return (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
