package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.security.LoginFailure;
import br.com.alura.alurapic.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {

    List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
}
