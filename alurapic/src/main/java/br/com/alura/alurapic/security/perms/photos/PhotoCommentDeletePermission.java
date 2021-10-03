package br.com.alura.alurapic.security.perms.photos;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('pic.delete') AND " +
        "(@picAuthenticationManager.usernameMatchesPhoto(authentication, #username, #idPhoto) OR " +
        "@picAuthenticationManager.usernameMatchesComment(authentication, #username, #idPhoto, idComment))")
public @interface PhotoCommentDeletePermission {
}
