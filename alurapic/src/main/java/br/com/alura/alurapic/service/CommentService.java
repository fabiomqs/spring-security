package br.com.alura.alurapic.service;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotAllowedException;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;

import java.util.List;

public interface CommentService {

    List<Comment> getComments(Integer idPhoto);

    List<Comment> getCommentsPage(Integer idPhoto, int page, int size);

    void addComment(String username, Integer idPhoto, String comment)
            throws UserNotFoundException, PhotoNotFounException, CommentNotAllowedException;

    void deleteComment(String username, Integer idPhoto, Integer idComment)
            throws CommentNotFoundException, PhotoNotFounException;
}
