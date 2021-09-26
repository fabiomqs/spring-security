package br.com.alura.alurapic.security.perms.role.authority;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('authority.update')")
public @interface AuthorityUpdatePermission {
}
