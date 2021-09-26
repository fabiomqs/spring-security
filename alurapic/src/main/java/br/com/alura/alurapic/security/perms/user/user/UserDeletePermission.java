package br.com.alura.alurapic.security.perms.user.user;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('user.manager.delete') OR " +
        "hasAuthority('user.admin.delete') OR " +
        "hasAuthority('user.delete') AND " +
        "@picAuthenticationManager.usernameMatches(authentication, #username)")
public @interface UserDeletePermission {
}
