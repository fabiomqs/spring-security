package br.com.alura.alurapic.security.listener;

import br.com.alura.alurapic.domain.security.LoginSuccess;
import br.com.alura.alurapic.domain.security.User;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.repository.LoginSuccessRepository;
import br.com.alura.alurapic.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;
    private final LoginSuccessRepository loginSuccessRepository;

    public AuthenticationSuccessListener(
            LoginAttemptService loginAttemptService,
            LoginSuccessRepository loginSuccessRepository) {
        this.loginAttemptService = loginAttemptService;
        this.loginSuccessRepository = loginSuccessRepository;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.debug("User Logged In Okay");
        if(event.getAuthentication().getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(userPrincipal.getUsername());

            LoginSuccess.LoginSuccessBuilder builder = LoginSuccess.builder();

            User user = userPrincipal.getUser();
            builder.user(user);

            log.debug("User name logged in: " + user.getUsername() );
            LoginSuccess loginSuccess = loginSuccessRepository.save(builder.build());

            log.debug("Login Success saved. Id: " + loginSuccess.getId());
        }

    }
}
