package com.supportportal.resources;

import com.supportportal.exception.ExceptionHandling;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserResource {//don't need to extend the controller advice
    //
    @GetMapping("/home")
    public String showUser() throws Exception {
        //throw new EmailExistException("This email address is already taken!");
        //throw new UserNotFoundException("This user was not found!");
        throw new RuntimeException("Some other Runtime error!");
        //return "Application works!";

    }
}
