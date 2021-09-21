package com.supportportal.service;

import com.supportportal.domain.User;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.exception.domain.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username) throws UserNotFoundException;

    User findUserByEmail(String email);

    UserPrincipal login(User user);

    User addNewUser(String firstName, String lastName, String username, String email, String role,
                    boolean isNonLocked, boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                    String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;

    void deleteUser(Long id);
    //void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException;
}
