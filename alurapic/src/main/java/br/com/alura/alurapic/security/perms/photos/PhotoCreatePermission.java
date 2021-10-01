package br.com.alura.alurapic.security.perms.photos;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('pic.create')")
public @interface PhotoCreatePermission {
}
