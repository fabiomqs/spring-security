package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean locked, Timestamp timestamp);
}
