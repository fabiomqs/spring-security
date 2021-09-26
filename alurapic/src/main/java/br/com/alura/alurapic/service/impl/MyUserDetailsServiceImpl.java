package br.com.alura.alurapic.service.impl;

import br.com.alura.alurapic.domain.security.User;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.repository.UserRepository;
import br.com.alura.alurapic.service.LoginAttemptService;
import br.com.alura.alurapic.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static br.com.alura.alurapic.util.constant.UserConstant.FOUND_USER_BY_USERNAME;
import static br.com.alura.alurapic.util.constant.UserConstant.NO_USER_FOUND_BY_USERNAME;

@Slf4j
@Qualifier("userDetailsService")
@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public MyUserDetailsServiceImpl(UserRepository userRepository,
                                    LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            log.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user) {
        if(user.isAccountNonLocked()) {
            if(loginAttemptService.hasExceededMaXAttempts(user.getUsername())) {
                user.setAccountNonLocked(false);
            } else {
                user.setAccountNonLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
