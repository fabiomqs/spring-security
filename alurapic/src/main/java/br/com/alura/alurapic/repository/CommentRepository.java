package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
