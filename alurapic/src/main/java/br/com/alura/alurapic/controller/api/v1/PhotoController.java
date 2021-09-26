package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.CommentNotFoundException;
import br.com.alura.alurapic.exception.domain.NotAnImageFileException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.service.photos.PhotosService;
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
@RequestMapping("/api/v1/photo")
public class PhotoController {

    private final PhotosService photosService;

    public PhotoController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @ResponseStatus(OK)
    @GetMapping("/{username}")
    public List<Photo> getPhotos(@PathVariable String username) {
        return photosService.getPhotos(username);
    }

    @ResponseStatus(OK)
    @GetMapping("/{username}/{idPhoto}")
    public byte[] getPhoto(@PathVariable String username, @PathVariable String idPhoto)
            throws PhotoNotFounException, IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username +
                PHOTOS_FOLDER + idPhoto));
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable String username, @PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @ResponseStatus(OK)
    @PostMapping("/upload")
    public Photo getPhotos(
            @RequestParam("username")  String username,
                @RequestParam("description") String description,
                @RequestParam("allowComments") String allowComments,
                @RequestParam("photo") MultipartFile photo)
            throws UserNotFoundException, IOException, NotAnImageFileException {

        return photosService.uploadPhotos(username, description,
                Boolean.parseBoolean(allowComments), photo);
    }

    @ResponseStatus(OK)
    @DeleteMapping("/{username}/{idPhoto}")
    public void deletePhoto(@PathVariable String username,
                               @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        photosService.deletePhoto(username, Integer.parseInt(idPhoto));
    }

    @ResponseStatus(OK)
    @PostMapping("/like/{idPhoto}")
    public void likePhoto(@RequestParam("username") String username,
                            @PathVariable String idPhoto)
            throws PhotoNotFounException, CommentNotFoundException, UserNotFoundException, IOException {
        photosService.likePhoto(username, Integer.parseInt(idPhoto));
    }


}
