package br.com.alura.alurapic.service.impl;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.repository.CommentRepository;
import br.com.alura.alurapic.repository.PhotoRepository;
import br.com.alura.alurapic.repository.UserRepository;
import br.com.alura.alurapic.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.alura.alurapic.util.constant.PhotoConstant.NO_COMMENT_FOUND_BY_ID;
import static br.com.alura.alurapic.util.constant.PhotoConstant.NO_PHOTO_FOUND_BY_ID;
import static br.com.alura.alurapic.util.constant.UserConstant.NO_USER_FOUND_BY_USERNAME;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(PhotoRepository photoRepository,
                              CommentRepository commentRepository,
                              UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> getComments(Integer idPhoto) {
        List<Comment> comments = commentRepository.findAllByIdPhoto(idPhoto, Sort.by(Sort.Direction.DESC, "postDate"));
        return comments.stream().map(comment -> {
            return prepareComment(comment);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Comment> getCommentsPage(Integer idPhoto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postDate"));

        List<Comment> comments = commentRepository.findAllByIdPhotoPage(idPhoto, pageable);
        return comments.stream().map(comment -> {
            return prepareComment(comment);
        }).collect(Collectors.toList());
    }

    @Override
    public Photo addComment(String username, Integer idPhoto, String comment)
            throws UserNotFoundException, PhotoNotFounException {
        User user = findUser(username);
        Photo photo = findPhoto(idPhoto);
        commentRepository.save(Comment.builder().user(user).photo(photo).comment(comment).build());
        return prepareEntity(findPhoto(idPhoto), true);
    }

    @Override
    public Photo deleteComment(String username, Integer idPhoto, Integer idComment)
            throws CommentNotFoundException, PhotoNotFounException {
        Comment comment = findComment(idComment);
        commentRepository.deleteById(comment.getId());
        return prepareEntity(findPhoto(idPhoto), true);
    }

    private Photo findPhoto(Integer id) throws PhotoNotFounException {
        return photoRepository.findById(id)
                .orElseThrow(() -> new PhotoNotFounException(NO_PHOTO_FOUND_BY_ID + id));
    }

    private Comment findComment(Integer id) throws CommentNotFoundException {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(NO_COMMENT_FOUND_BY_ID + id));
    }

    private User findUser(String username) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
        return user;
    }

    private Photo prepareEntity(Photo photo, boolean withComments) {
        photo.setUsername(photo.getUser().getUsername());
        if(withComments) {
            photo.getComments().stream().map(comment -> {
                return prepareComment(comment);
            }).collect(Collectors.toSet());
        } else {
            photo.setComments(null);
        }
        return photo;
    }

    private Comment prepareComment(Comment comment) {
        comment.setPhotoId(comment.getPhoto().getId());
        comment.setUsername(comment.getUser().getUsername());
        return comment;
    }
}
