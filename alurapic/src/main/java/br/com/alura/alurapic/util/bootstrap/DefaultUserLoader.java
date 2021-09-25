package br.com.alura.alurapic.util.bootstrap;

import br.com.alura.alurapic.domain.security.Authority;
import br.com.alura.alurapic.domain.security.Role;
import br.com.alura.alurapic.domain.security.User;
import br.com.alura.alurapic.repository.AuthorityRepository;
import br.com.alura.alurapic.repository.RoleRepository;
import br.com.alura.alurapic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultUserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        loadSecurityData();
    }

    private void loadSecurityData() {
        //user auths
        Authority createUser = authorityRepository.save(
                Authority.builder().permission("user.create").build());
        Authority readUser = authorityRepository.save(
                Authority.builder().permission("user.read").build());
        Authority updateUser = authorityRepository.save(
                Authority.builder().permission("user.update").build());
        Authority deleteUser = authorityRepository.save(
                Authority.builder().permission("user.delete").build());

        Authority banUser = authorityRepository.save(
                Authority.builder().permission("user.ban").build());
        Authority suspendUser = authorityRepository.save(
                Authority.builder().permission("user.suspend").build());

        Authority createUserAdmin = authorityRepository.save(
                Authority.builder().permission("user.admin.create").build());
        Authority readUserAdmin = authorityRepository.save(
                Authority.builder().permission("user.admin.read").build());
        Authority updateUserAdmin = authorityRepository.save(
                Authority.builder().permission("user.admin.update").build());
        Authority deleteUserAdmin = authorityRepository.save(
                Authority.builder().permission("user.admin.delete").build());

        //user auths
        Authority createPic = authorityRepository.save(
                Authority.builder().permission("pic.create").build());
        Authority updatePic = authorityRepository.save(
                Authority.builder().permission("pic.update").build());
        Authority deletePic = authorityRepository.save(
                Authority.builder().permission("pic.delete").build());

        Authority blockPic = authorityRepository.save(
                Authority.builder().permission("pic.block").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role managerRole = roleRepository.save(Role.builder().name("MANAGER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(createUser,readUser,updateUser, deleteUser,
                createUserAdmin,readUserAdmin,updateUserAdmin,deleteUserAdmin,banUser, suspendUser,
                createPic, updatePic, deletePic, blockPic)));

        managerRole.setAuthorities(new HashSet<>(Set.of(createUser,readUser,updateUser, deleteUser,
                readUserAdmin,banUser, suspendUser, createPic, updatePic, deletePic, blockPic)));

        userRole.setAuthorities(new HashSet<>(Set.of(readUser,updateUser, deleteUser,
                createPic, updatePic, deletePic)));

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole));

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(managerRole)
                .build());

        log.debug("Users Loaded: " + userRepository.count());
    }


}
