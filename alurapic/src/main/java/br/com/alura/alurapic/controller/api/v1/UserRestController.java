package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.http.HttpResponse;
import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.exception.domain.*;
import br.com.alura.alurapic.security.perms.user.manager.ManagerCreatePermission;
import br.com.alura.alurapic.security.perms.user.user.UserDeletePermission;
import br.com.alura.alurapic.security.perms.user.user.UserUpdatePermission;
import br.com.alura.alurapic.service.UserService;
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

import static br.com.alura.alurapic.util.constant.FileConstant.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController  {

    public static final String EMAIL_SENT = "An email with new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User Deleted Successfully!";
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    //
    @ResponseStatus(OK)
    @PostMapping("/register")
    public User register(@RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {

        User newUser = userService.register(firstName, lastName, username, password, email);
        return newUser;
    }

    @ManagerCreatePermission
    @ResponseStatus(OK)
    @PostMapping("/add")
    public User addUser(@RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName,
                        @RequestParam("username") String username,
                        @RequestParam("email") String email,
                        @RequestParam("roles") String[] roles,
                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User newUser = userService.addNewUser(firstName, lastName,
                username, email, roles, profileImage);
        return newUser;
    }

    @UserUpdatePermission
    @ResponseStatus(OK)
    @PostMapping("/update")
    public User updateUser(@RequestParam("currentUsername") String currentUsername,
                           @RequestParam("firstName") String firstName,
                           @RequestParam("lastName") String lastName,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("roles") String[] roles,
                           @RequestParam("isNonLocked") String isNonLocked,
                           @RequestParam("isActive") String isActive,
                           @RequestParam("NotExpired") String NotExpired,
                           @RequestParam("credentialsNotExpired") String credentialsNotExpired,
                           @RequestParam("suspended") String suspended,
                           @RequestParam("banned") String banned,
                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailExistException, IOException,
            UsernameExistException, NotAnImageFileException {

        User updatedUser = userService.updateUser(currentUsername, firstName, lastName,
                username, email, roles, Boolean.parseBoolean(isNonLocked),
                Boolean.parseBoolean(isActive), Boolean.parseBoolean(NotExpired),
                Boolean.parseBoolean(credentialsNotExpired),
                Boolean.parseBoolean(suspended), Boolean.parseBoolean(banned),
                profileImage);

        return updatedUser;
    }

    @ResponseStatus(OK)
    @GetMapping("/find/{username}")
    public User findUser(@PathVariable String username) throws UserNotFoundException {
        return userService.findUserByUsername(username);
    }

    @ResponseStatus(OK)
    @GetMapping("/exists/{username}")
    public boolean userExists(@PathVariable String username) {
        return userService.userExists(username);
    }

    @ManagerCreatePermission
    @ResponseStatus(OK)
    @GetMapping("/suspend/{username}")
    public User suspendUser(@PathVariable String username) throws UserNotFoundException,
            EmailExistException, MessagingException, UsernameExistException {
        return userService.suspendUser(username);
    }

    @ManagerCreatePermission
    @ResponseStatus(OK)
    @GetMapping("/ban/{username}")
    public User banUser(@PathVariable String username) throws UserNotFoundException,
            EmailExistException, MessagingException, UsernameExistException {
        return userService.banUser(username);
    }

    @ResponseStatus(OK)
    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.getUsers();
    }

    @GetMapping("/reset-password/{email}")
    public HttpResponse resetPassword(@PathVariable String email) throws EmailNotFoundException, MessagingException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{username}")
    @UserDeletePermission
    public HttpResponse deleteUser(@PathVariable String username) throws UserNotFoundException, IOException {
        userService.deleteUser(username);
        return response(NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

    @UserUpdatePermission
    @ResponseStatus(OK)
    @PostMapping("/update-profile-image")
    public User updateProfileImage(@RequestParam("username") String username,
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
                .reason(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }
}
