package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
