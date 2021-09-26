package br.com.alura.alurapic.service.photos;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;

import java.util.List;

public interface CommentService {

    List<Comment> getComments(Integer idPhoto);

    Photo addComment(String username, Integer idPhoto, String comment)
            throws UserNotFoundException, PhotoNotFounException;

    Photo deleteComment(String username, Integer idPhoto, Integer idComment)
            throws CommentNotFoundException, PhotoNotFounException;
}
