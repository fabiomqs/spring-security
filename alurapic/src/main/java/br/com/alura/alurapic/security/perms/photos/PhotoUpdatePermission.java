package br.com.alura.alurapic.security.perms.photos;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
//TODO
//check pic owner
@PreAuthorize("hasAuthority('pic.update') AND " +
        "@picAuthenticationManager.usernameMatchesPhoto(authentication, #username, #idPhoto)")
public @interface PhotoUpdatePermission {
}
