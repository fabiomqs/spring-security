package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
