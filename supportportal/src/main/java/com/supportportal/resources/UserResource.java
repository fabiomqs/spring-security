package com.supportportal.resources;

import com.supportportal.domain.HttpResponse;
import com.supportportal.domain.User;
import com.supportportal.exception.domain.*;
import com.supportportal.security.perms.user.UserDeletePermission;
import com.supportportal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    public static final String EMAIL_SENT = "An email with new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User Deleted Successfully!";
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    //
    @ResponseStatus(OK)
    @PostMapping("/register")
    public User register(@RequestBody User user)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User newUser = userService
                .register(user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail());
        return newUser;
    }

    @ResponseStatus(OK)
    @PostMapping("/add")
    public User addUser(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                        @RequestBody User user) throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User newUser = userService.addNewUser(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail(), user.getRole(), user.isNotLocked(),
                user.isActive(), profileImage);
        return newUser;
    }

    @ResponseStatus(OK)
    @PostMapping("/update")
    public User updateUser(@RequestParam("currentUsername") String currentUsername,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestBody User user) throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User updatedUser = userService.updateUser(currentUsername, user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail(), user.getRole(), user.isNotLocked(),
                user.isActive(), profileImage);
        return updatedUser;
    }

    @ResponseStatus(OK)
    @GetMapping("/find/{username}")
    public User findUser(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @ResponseStatus(OK)
    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.getUsers();
    }

    @ResponseStatus(OK)
    @GetMapping("/resetPassword/{email}")
    public HttpResponse resetPassword(@PathVariable String email) throws EmailNotFoundException, MessagingException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{id}")
    @UserDeletePermission
    public HttpResponse deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return response(NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

    @ResponseStatus(OK)
    @PostMapping("/updateProfileImage")
    public User updateUser(@RequestParam("username") String username,
                           @RequestParam("profileImage") MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {
        return userService.updateProfileImage(username, profileImage);
    }

    private HttpResponse response(HttpStatus httpStatus, String message) {
        return HttpResponse.builder()
                .httpStatusCode(httpStatus.value())
                .httpStatus(httpStatus)
                .reason(httpStatus.getReasonPhrase().toUpperCase())
                .message(message.toUpperCase())
                .build();
    }
}
