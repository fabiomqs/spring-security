package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query("SELECT p FROM Photo p JOIN FETCH p.comments c " +
            "JOIN FETCH p.user JOIN FETCH c.user WHERE p.id = ?1")
    Photo fetchById(Integer id);
}
