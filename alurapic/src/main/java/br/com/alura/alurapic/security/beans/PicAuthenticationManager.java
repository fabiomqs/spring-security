package br.com.alura.alurapic.security.beans;

import br.com.alura.alurapic.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PicAuthenticationManager {

    //TODO
    //ADD pic owner check
    public boolean usernameMatches(Authentication authentication, String username) {
        //TODO
        //User or UserPrincipal
        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Auth User username: " + authenticatedUser.getUsername() + " username:" + username);

        return authenticatedUser.getUsername().equals(username);
    }
}
