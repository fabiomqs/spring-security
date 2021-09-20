package com.supportportal.resources;

import com.supportportal.domain.User;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.exception.ExceptionHandling;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UserNotFoundException;
import com.supportportal.exception.domain.UsernameExistException;
import com.supportportal.security.util.JwtTokenProvider;
import com.supportportal.service.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static org.springframework.http.HttpStatus.*;
import static com.supportportal.constant.SecurityConstant.*;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserResource extends ExceptionHandling {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResource(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user)  {
        UserPrincipal userPrincipal = userService.login(user);
        HttpHeaders jwtHeader = getJwtHeaders(userPrincipal);
        return new ResponseEntity<>(userPrincipal.getUser(), jwtHeader, OK);
    }

    private HttpHeaders getJwtHeaders(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }


    //
    @ResponseStatus(OK)
    @PostMapping("/register")
    public User register(@RequestBody User user)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User savedUser = userService
                .register(user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail());
        return savedUser;
    }
}
