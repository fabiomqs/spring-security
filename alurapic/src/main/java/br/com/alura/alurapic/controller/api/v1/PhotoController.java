package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.NotAnImageFileException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.service.PhotosService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static br.com.alura.alurapic.util.constant.FileConstant.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/api/v1/photos")
public class PhotoController {

    private final PhotosService photosService;

    public PhotoController(PhotosService photosService) {
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
    @GetMapping(path = "/user/{username}/{idPhoto}", produces = IMAGE_JPEG_VALUE)
    public byte[] getPhoto(@PathVariable String username, @PathVariable String idPhoto)
            throws PhotoNotFounException, IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username +
                PHOTOS_FOLDER + FORWARD_SLASH + idPhoto));
    }

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

    @ResponseStatus(OK)
    @DeleteMapping("/photo/{username}/{idPhoto}")
    public void deletePhoto(@PathVariable String username,
                               @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        photosService.deletePhoto(username, Integer.parseInt(idPhoto));
    }

    @ResponseStatus(OK)
    @PostMapping("/photo/like/{idPhoto}")
    public void likePhoto(@RequestParam("username") String username,
                            @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        photosService.likePhoto(username, Integer.parseInt(idPhoto));
    }


}
