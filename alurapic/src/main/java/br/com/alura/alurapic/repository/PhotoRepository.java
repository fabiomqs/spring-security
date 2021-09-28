package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query("SELECT p FROM Photo p LEFT JOIN FETCH p.comments c " +
            "JOIN FETCH p.user JOIN FETCH c.user WHERE p.id = ?1")
    Photo fetchById(Integer id);

    List<Photo> findAllByUser(User user);

    List<Photo> findAllByUser(User user, Pageable pageable);

}
