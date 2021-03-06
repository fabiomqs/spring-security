package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.Comment;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotAllowedException;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.security.perms.photos.PhotoCommentDeletePermission;
import br.com.alura.alurapic.security.perms.photos.PhotoCreatePermission;
import br.com.alura.alurapic.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/photos/comment")
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseStatus(OK)
    @GetMapping("/public/all/{idPhoto}")
    public List<Comment> getAllComments(@PathVariable String idPhoto) {
        return commentService.getComments(Integer.parseInt(idPhoto));
    }

    @ResponseStatus(OK)
    @GetMapping(path = "/public/{idPhoto}", params = { "page" })
    public List<Comment> getAllCommentsPage(
            @PathVariable String idPhoto,
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue= "10")  int size) {
        return commentService.getCommentsPage(Integer.parseInt(idPhoto), page, size);
    }

    //TODO - Modificar retorno
    @PhotoCreatePermission
    @ResponseStatus(OK)
    @PostMapping("/{idPhoto}")
    public void addComment(@RequestParam("username") String username,
                            @PathVariable String idPhoto,
                            @RequestParam("comment") String comment)
            throws UserNotFoundException, PhotoNotFounException, CommentNotAllowedException {
        commentService.addComment(username, Integer.parseInt(idPhoto), comment);
    }

    @PhotoCommentDeletePermission
    @ResponseStatus(OK)
    @DeleteMapping("/{username}/{idPhoto}/{idComment}")
    public void deleteComment(@PathVariable("username") String username,
                               @PathVariable String idPhoto,
                               @PathVariable String idComment)
            throws PhotoNotFounException, CommentNotFoundException {
        commentService.deleteComment(username,
                Integer.parseInt(idPhoto), Integer.parseInt(idComment));
    }
}
