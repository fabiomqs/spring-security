package guru.sfg.brewery.config.perms.brewery;

import guru.sfg.brewery.domain.security.Authority;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('brewery.read')")
public @interface BreweryReadPermission {
}