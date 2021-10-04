package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.NotAnImageFileException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.security.perms.photos.PhotoCreatePermission;
import br.com.alura.alurapic.security.perms.photos.PhotoDeletePermission;
import br.com.alura.alurapic.service.PhotosService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/photos")
public class PhotoRestController {

    private final PhotosService photosService;

    public PhotoRestController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @ResponseStatus(OK)
    @GetMapping("/user/{username}")
    public List<Photo> getPhotos(@PathVariable String username) throws UserNotFoundException {
        return photosService.getPhotos(username);
    }

    @ResponseStatus(OK)
    @GetMapping(path = "/user/{username}", params = { "page" })
    public List<Photo> getPhotosPaginated(
            @PathVariable String username,
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue= "12") int size) throws UserNotFoundException {
        return photosService.getPhotos(username, page, size);
    }

    @ResponseStatus(OK)
    @GetMapping(path = "/user/photo/{idPhoto}")
    public Photo getPhoto(@PathVariable String idPhoto) throws PhotoNotFounException {
        return photosService.getPhoto(Integer.parseInt(idPhoto));
    }

    @PhotoCreatePermission
    @ResponseStatus(OK)
    @PostMapping("/photo/upload")
    public Photo uploadPhoto(
            @RequestParam("username")  String username,
                @RequestParam("description") String description,
                @RequestParam("allowComments") String allowComments,
                @RequestParam("photo") MultipartFile photo)
            throws UserNotFoundException, IOException, NotAnImageFileException {

        return photosService.uploadPhotos(username, description,
                Boolean.parseBoolean(allowComments), photo);
    }

    @PhotoCreatePermission
    @ResponseStatus(OK)
    @PostMapping("/photo/upload/base64")
    public Photo uploadPhotoBase64(
            @RequestParam("username")  String username,
            @RequestParam("description") String description,
            @RequestParam("allowComments") String allowComments,
            @RequestParam("photo") String photo)
            throws UserNotFoundException, IOException, NotAnImageFileException {

        return photosService.uploadPhotoBase64(username, description,
                Boolean.parseBoolean(allowComments), photo);
    }

    @PhotoDeletePermission
    @ResponseStatus(OK)
    @DeleteMapping("/photo/{username}/{idPhoto}")
    public void deletePhoto(@PathVariable String username,
                            @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        photosService.deletePhoto(username, Integer.parseInt(idPhoto));
    }

    @PhotoCreatePermission
    @ResponseStatus(OK)
    @PostMapping("/photo/like/{idPhoto}/{username}")
    public boolean likePhoto(@PathVariable String username,
                          @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        return photosService.likePhoto(username, Integer.parseInt(idPhoto));
    }


}
