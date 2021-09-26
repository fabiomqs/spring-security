package br.com.alura.alurapic.security.perms.user.manager;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('user.manager.delete') OR " +
        "hasAuthority('user.admin.delete')")
public @interface ManagerDeletePermission {
}
