package br.com.alura.alurapic.security.perms.user.user;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('user.manager.update') OR " +
        "hasAuthority('user.admin.update') OR " +
        "hasAuthority('user.update') AND " +
        "@picAuthenticationManager.usernameMatches(authentication, #username)")
public @interface UserUpdatePermission {
}
