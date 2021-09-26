package br.com.alura.alurapic.service.photos.impl;

import br.com.alura.alurapic.domain.Like;
import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.exception.domain.NotAnImageFileException;
import br.com.alura.alurapic.exception.domain.PhotoNotFounException;
import br.com.alura.alurapic.exception.domain.UserNotFoundException;
import br.com.alura.alurapic.repository.CommentRepository;
import br.com.alura.alurapic.repository.LikeRepository;
import br.com.alura.alurapic.repository.PhotoRepository;
import br.com.alura.alurapic.repository.UserRepository;
import br.com.alura.alurapic.service.photos.PhotosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.alura.alurapic.util.constant.FileConstant.*;
import static br.com.alura.alurapic.util.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static br.com.alura.alurapic.util.constant.PhotoConstant.NO_PHOTO_FOUND_BY_ID;
import static br.com.alura.alurapic.util.constant.UserConstant.NO_USER_FOUND_BY_USERNAME;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Slf4j
@Service
public class PhotosServiceImpl implements PhotosService {

    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public PhotosServiceImpl(PhotoRepository photoRepository,
                             CommentRepository commentRepository,
                             LikeRepository likeRepository,
                             UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Photo> getPhotos(String username) {
        List<Photo> photos = photoRepository.findAll();
        return photos.stream().map(photo -> {
            return prepareEntity(photo, false);
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Photo uploadPhotos(String username, String description, boolean allowComments,
            MultipartFile profileImage) throws UserNotFoundException, IOException, NotAnImageFileException {
        User user = findUser(username);

        return savePhoto(user, description, allowComments, profileImage);
    }

    @Override
    public Photo getPhoto(Integer idPhoto) throws PhotoNotFounException {
        Photo photo = photoRepository.fetchById(idPhoto);
        if(photo == null)
            throw new PhotoNotFounException(NO_PHOTO_FOUND_BY_ID + idPhoto);
        return prepareEntity(photo, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePhoto(String username, Integer idPhoto)
            throws PhotoNotFounException, UserNotFoundException, IOException {
        User user = findUser(username);
        Photo photo = findPhoto(idPhoto);

        deletePhoto(photo, user);
        //TODO Delete file

        photoRepository.deleteById(photo.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likePhoto(String username, Integer idPhoto)
            throws UserNotFoundException, PhotoNotFounException {
        Like like = likeRepository.getByUsernameAndPhoto(username, idPhoto);
        if(like == null) {
            likeRepository.deleteById(like.getId());
        } else {
            User user = findUser(username);
            Photo photo = findPhoto(idPhoto);
            likeRepository.save(Like.builder().user(user).photo(photo).build());
        }
    }

    private void deletePhoto(Photo photo, User user) throws IOException {
        Path photoPath = Paths.get(USER_FOLDER + user.getUsername() +
                PHOTOS_FOLDER + FORWARD_SLASH + photo.getId() + DOT + JPG_EXTENSION)
                .toAbsolutePath().normalize();

        Files.deleteIfExists(photoPath);

    }

    private Photo savePhoto(User user, String description, boolean allowComments, MultipartFile photoImage)
            throws IOException, NotAnImageFileException {

        if (photoImage == null) {
            throw new NotAnImageFileException(IMAGE_NULL);
        }
        Photo photo = Photo.builder().allowComments(allowComments).description(description)
                .user(user).build();
        Photo savedPhoto = photoRepository.save(photo);
        if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE)
                .contains(photoImage.getContentType())) {
            throw new NotAnImageFileException(photoImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
        }
        Path userPhotosFolder = Paths.get(USER_FOLDER + user.getUsername() +
                PHOTOS_FOLDER).toAbsolutePath().normalize();
        if(!Files.exists(userPhotosFolder)) {
            Files.createDirectories(userPhotosFolder);
            log.info(DIRECTORY_CREATED + userPhotosFolder);
        }

        Files.copy(photoImage.getInputStream(),
                userPhotosFolder.resolve(savedPhoto.getId() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
        savedPhoto.setUrl(setPhotoUrl(user.getUsername(), savedPhoto.getId()));
        savedPhoto = photoRepository.save(savedPhoto);
        return savedPhoto;

    }

    private String setPhotoUrl(String username, Integer id) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(PHOTO_PATH + username + FORWARD_SLASH + id +
                         DOT + JPG_EXTENSION).toUriString();
    }

    private Photo findPhoto(Integer id) throws PhotoNotFounException {
        return photoRepository.findById(id)
                .orElseThrow(
                        () -> new PhotoNotFounException(NO_PHOTO_FOUND_BY_ID + id));
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
                comment.setPhotoId(comment.getPhoto().getId());
                comment.setUsername(comment.getUser().getUsername());
                return comment;
            }).collect(Collectors.toSet());
        } else {
            photo.setComments(null);
        }
        return photo;
    }
}
