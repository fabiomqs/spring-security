package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.security.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Integer> {
}
