package br.com.alura.alurapic.domain.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isNotExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked() && !this.user.isSuspended() && !this.user.isBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNotExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }

    public User getUser() {
        return user;
    }
}
