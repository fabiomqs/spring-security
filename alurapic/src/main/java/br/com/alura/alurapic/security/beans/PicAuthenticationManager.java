package br.com.alura.alurapic.security.beans;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.repository.CommentRepository;
import br.com.alura.alurapic.repository.PhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PicAuthenticationManager {

    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;

    public PicAuthenticationManager(
            PhotoRepository photoRepository,
            CommentRepository commentRepository) {
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
    }

    public boolean usernameMatches(Authentication authentication, String username) {
        String userPrincipal = (String) authentication.getPrincipal();
        if(!userPrincipal.equals(username))
            return false;
        return true;
    }

    public boolean usernameMatchesPhoto(Authentication authentication, String username, String idPhoto) {
        String userPrincipal = (String) authentication.getPrincipal();
        if(!userPrincipal.equals(username))
            return false;
        Photo photo = photoRepository.fetchByIdAndUser(Integer.parseInt(idPhoto), username);
        if(photo == null)
            return false;
        return true;
    }

    public boolean usernameMatchesComment(Authentication authentication, String username, String idPhoto, String idComment) {
        String userPrincipal = (String) authentication.getPrincipal();
        if(!userPrincipal.equals(username))
            return false;
        Comment comment = commentRepository.fetchByIdAndPhotoAndUser(Integer.parseInt(idComment), Integer.parseInt(idPhoto), username);
        if(comment == null)
            return false;
        return true;
    }
}
