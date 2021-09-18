package com.supportportal.resources;

import com.supportportal.domain.User;
import com.supportportal.exception.ExceptionHandling;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UserNotFoundException;
import com.supportportal.exception.domain.UsernameExistException;
import com.supportportal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserResource extends ExceptionHandling {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    //
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public User register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User savedUser = userService
                .register(user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail());
        return savedUser;
    }
}
