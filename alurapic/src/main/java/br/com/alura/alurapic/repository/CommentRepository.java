package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.photo p " +
            "JOIN FETCH c.user u WHERE c.id = ?1 and p.id = ?2 and u.username = ?3")
    Comment fetchByIdAndPhotoAndUser(Integer id, Integer idPhoto, String username);

    @Query("SELECT c FROM Comment c JOIN FETCH c.photo p JOIN FETCH c.user u WHERE p.id = ?1")
    List<Comment> findAllByIdPhotoPage(Integer id, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.photo p JOIN FETCH c.user u WHERE p.id = ?1")
    List<Comment> findAllByIdPhoto(Integer id, Sort sort);

    Integer countByPhoto(Photo photo);
}
