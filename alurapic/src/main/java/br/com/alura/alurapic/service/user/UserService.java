package br.com.alura.alurapic.service.user;

import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.exception.domain.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    UserPrincipal login(User user);

    User register(String firstName, String lastName, String username, String email)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username) throws UserNotFoundException;

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String[] roles,
                    MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                    String newEmail, String[] roles, boolean isNonLocked, boolean isActive,
                    boolean NotExpired, boolean credentialsNotExpired,
                    boolean suspended, boolean banned,
                    MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;

    void deleteUser(String username) throws UserNotFoundException, IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;

    User suspendUser(String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    User banUser(String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    boolean userExists(String username);
}
