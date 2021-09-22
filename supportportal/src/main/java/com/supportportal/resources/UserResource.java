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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.supportportal.constant.FileConstant.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(UserResource.BASE_URL)
public class UserResource {

    public static final String BASE_URL = "/api/v1/user";
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
    public User addUser(@RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName,
                        @RequestParam("username") String username,
                        @RequestParam("email") String email,
                        @RequestParam("role") String role,
                        @RequestParam("isNonLocked") String isNonLocked,
                        @RequestParam("isActive") String isActive,
                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User newUser = userService.addNewUser(firstName, lastName,
                username, email, role, Boolean.parseBoolean(isNonLocked),
                Boolean.parseBoolean(isActive), profileImage);
        return newUser;
    }

    @ResponseStatus(OK)
    @PostMapping("/update")
    public User updateUser(@RequestParam("currentUsername") String currentUsername,
                           @RequestParam("firstName") String firstName,
                           @RequestParam("lastName") String lastName,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("role") String role,
                           @RequestParam("isNonLocked") String isNonLocked,
                           @RequestParam("isActive") String isActive,
                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User updatedUser = userService.updateUser(currentUsername, firstName, lastName,
                username, email, role, Boolean.parseBoolean(isNonLocked),
                Boolean.parseBoolean(isActive), profileImage);
        return updatedUser;
    }

    @ResponseStatus(OK)
    @GetMapping("/find/{username}")
    public User findUser(@PathVariable String username) throws UserNotFoundException {
        return userService.findUserByUsername(username);
    }

    @ResponseStatus(OK)
    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.getUsers();
    }

    @ResponseStatus(OK)
    @GetMapping("/reset-password/{email}")
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
    @PostMapping("/update-profile-image")
    public User updateUser(@RequestParam("username") String username,
                           @RequestParam("profileImage") MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {
        return userService.updateProfileImage(username, profileImage);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable String username, @PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
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
