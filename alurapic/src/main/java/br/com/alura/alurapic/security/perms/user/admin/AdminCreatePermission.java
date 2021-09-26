package br.com.alura.alurapic.security.perms.user.admin;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('user.admin.create')")
public @interface AdminCreatePermission {
}
