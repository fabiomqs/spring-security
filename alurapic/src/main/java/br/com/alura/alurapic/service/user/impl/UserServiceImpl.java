package br.com.alura.alurapic.service.user.impl;

import br.com.alura.alurapic.domain.security.Role;
import br.com.alura.alurapic.domain.security.User;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.exception.domain.*;
import br.com.alura.alurapic.repository.RoleRepository;
import br.com.alura.alurapic.repository.UserRepository;
import br.com.alura.alurapic.security.util.JwtTokenProvider;
import br.com.alura.alurapic.service.EmailService;
import br.com.alura.alurapic.service.LoginAttemptService;
import br.com.alura.alurapic.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.alura.alurapic.util.constant.FileConstant.*;
import static br.com.alura.alurapic.util.constant.UserConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           LoginAttemptService loginAttemptService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserPrincipal login(User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = null;
        try {
            loginUser = findUserByUsername(user.getUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected Error on Authenticate, Shouldn't get here.");
        }
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        return userPrincipal;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(String firstName, String lastName, String username, String email)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = buildUser(firstName, lastName,username, email, Arrays.asList("USER"));
        User savedUser = userRepository.save(user);
        log.info(username + " new password: " + user.getTransientPassword());
        //
        //emailService.sendNewPasswordEmail(firstName, user.getTransientPassword(), email);
        return savedUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User addNewUser(String firstName, String lastName, String username, String email,
                           String[] roles, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = buildUser(firstName, lastName,username, email,Arrays.asList(roles));
        User savedUser = userRepository.save(user);
        saveProfileImage(user, profileImage);
        log.info(username + " new password: " + user.getTransientPassword());
        //
        //emailService.sendNewPasswordEmail(firstName, user.getTransientPassword(), email);

        return savedUser;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null)
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName,
                           String newUsername, String newEmail, String[] roles, boolean isNonLocked,
                           boolean isActive,
                           boolean NotExpired, boolean credentialsNotExpired,
                           boolean suspended, boolean banned,
                           MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException {


        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setAccountNonLocked(isNonLocked);
        currentUser.setRoles(getRoles(Arrays.asList(roles)));

        currentUser.setNotExpired(NotExpired);
        currentUser.setCredentialsNotExpired(credentialsNotExpired);
        currentUser.setSuspended(suspended);
        currentUser.setBanned(banned);

        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(String username) throws UserNotFoundException, IOException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
        user.setActive(false);
        user.setAccountNonLocked(false);

        user.setNotExpired(false);
        user.setCredentialsNotExpired(false);

        user.setProfileImageUrl(getTemporaryProfileImageUrl(user.getUsername()));

        Path userFolder =
                Paths.get(USER_FOLDER + user.getUsername())
                        .toAbsolutePath().normalize();
        Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));

        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        log.info("New user password: " + password);
        //emailService.sendEmail(user.getFirstName(), password, user.getEmail());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public User suspendUser(String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User user = validateNewUsernameAndEmail(username, null, null);
        user.setAccountNonLocked(false);
        user.setSuspended(true);
        User savedUser = userRepository.save(user);
        String msg = "Hello " + user.getFirstName() +
                ", \n \n Your new account(" + user.getUsername() + ") has been suspended!"+
                "\n \n The Support Team";

        //
        //emailService.sendEmail("Alura Pic - Account Suspended", msg, user.getEmail());
        return savedUser;
    }

    @Override
    public User banUser(String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User user = validateNewUsernameAndEmail(username, null, null);
        user.setAccountNonLocked(false);
        user.setActive(false);
        user.setBanned(true);
        User savedUser = userRepository.save(user);
        String msg = "Hello " + user.getFirstName() +
                ", \n \n Your new account(" + user.getUsername() + ") has been banned!"+
                "\n \n The Support Team";
        //emailService.sendEmail("Alura Pic - Account Banned", msg, user.getEmail());
        return savedUser;
    }

    private void authenticate(String username, String password) {
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username, password));
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private User buildUser(String firstName, String lastName, String username, String email,
                           List<String> roleNames) {
        String password = generatePassword();
        User user = User.builder().firstName(firstName)
                .lastName(lastName).username(username).email(email)
                .password(encodePassword(password)).transientPassword(password)
                .roles(getRoles(roleNames))
                .profileImageUrl(getTemporaryProfileImageUrl(username))
                .build();
        return user;
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private Set<Role> getRoles(List<String> roleNames) {
        return roleNames.stream().map(roleName -> {
            Role role = roleRepository.findRoleByName(roleName);
            if(role == null)
                throw new RuntimeException("Role not found");
            return role;
        }).collect(Collectors.toSet());
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(USER_IMAGE_PATH + username + FORWARD_SLASH
                        + username + DOT + JPG_EXTENSION).toUriString();
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException,
            NotAnImageFileException {

        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername,
                                             String newUsername,
                                             String newEmail)
            throws UserNotFoundException, UsernameExistException, EmailExistException {
        if(newUsername == null && newEmail == null) {
            User user = findUserByUsername(currentUsername);
            if (user == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            return user;
        } else {
            User userByNewUsername = null;
            try {
                userByNewUsername = findUserByUsername(newUsername);
            } catch (UserNotFoundException e) {
            }
            User userByNewEmail = findUserByEmail(newEmail);

            if (StringUtils.isNotBlank(currentUsername)) {
                User currentUser = findUserByUsername(currentUsername);
                if (currentUser == null) {
                    throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
                }
                if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
                }
                if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                    throw new EmailExistException(EMAIL_ALREADY_EXISTS);
                }
                return currentUser;
            } else {
                if (userByNewUsername != null) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
                }
                if (userByNewEmail != null) {
                    throw new EmailExistException(EMAIL_ALREADY_EXISTS);
                }
                return null;
            }
        }
    }
}
