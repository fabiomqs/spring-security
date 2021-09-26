package br.com.alura.alurapic.repository;

import br.com.alura.alurapic.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    @Query("SELECT l FROM Like l WHERE l.user.username = ?1 AND l.photo.id = ?2")
    Like getByUsernameAndPhoto(String username, Integer idPhoto);
}
