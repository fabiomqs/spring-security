package br.com.alura.alurapic.security.listener;

import br.com.alura.alurapic.domain.security.LoginFailure;
import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.repository.LoginFailureRepository;
import br.com.alura.alurapic.repository.UserRepository;
import br.com.alura.alurapic.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFailureListener {

    private final LoginAttemptService loginAttemptService;
    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    public AuthenticationFailureListener(
            LoginAttemptService loginAttemptService,
            LoginFailureRepository loginFailureRepository,
            UserRepository userRepository) {
        this.loginAttemptService = loginAttemptService;
        this.loginFailureRepository = loginFailureRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void onAuthenticationFailure(
            AuthenticationFailureBadCredentialsEvent event) {
        log.debug("Login failure");
        if(event.getAuthentication().getPrincipal() instanceof String) {
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(username);

            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
            builder.username(username);
            User user = userRepository.findUserByUsername(username);
            if(user != null) {
                builder.user(user);
            }

            LoginFailure failure = loginFailureRepository.save(builder.build());
            log.debug("Failure Event: " + failure.getId());


        }
    }
}
