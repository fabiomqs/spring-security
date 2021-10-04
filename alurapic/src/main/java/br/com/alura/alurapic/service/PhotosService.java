package br.com.alura.alurapic.service;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.exception.domain.NotAnImageFileException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotosService {

    List<Photo> getPhotos(String username) throws UserNotFoundException;

    List<Photo> getPhotos(String username, int page, int size) throws UserNotFoundException;

    Photo uploadPhotos(String username, String description,
                       boolean allowComments, MultipartFile photo) throws UserNotFoundException, IOException, NotAnImageFileException;

    Photo uploadPhotoBase64(String username, String description,
                       boolean allowComments, String photo) throws UserNotFoundException, IOException, NotAnImageFileException;

    Photo getPhoto(Integer idPhoto) throws PhotoNotFounException;

    void deletePhoto(String username, Integer idPhoto) throws PhotoNotFounException, UserNotFoundException, IOException;

    boolean likePhoto(String username, Integer idPhoto) throws UserNotFoundException, PhotoNotFounException;
}
