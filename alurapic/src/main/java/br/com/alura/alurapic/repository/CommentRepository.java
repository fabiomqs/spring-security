package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.photo p " +
            "JOIN FETCH c.user u WHERE c.id = ?1 and p.id = ?2 and u.username = ?3")
    Comment fetchByIdAndPhotoAndUser(Integer id, Integer idPhoto, String username);
}
