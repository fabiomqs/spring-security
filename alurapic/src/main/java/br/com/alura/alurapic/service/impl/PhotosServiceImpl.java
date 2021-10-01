package br.com.alura.alurapic.service.impl;

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
import br.com.alura.alurapic.service.PhotosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.alura.alurapic.util.constant.FileConstant.*;
import static br.com.alura.alurapic.util.constant.PhotoConstant.NO_PHOTO_FOUND_BY_ID;
import static br.com.alura.alurapic.util.constant.UserConstant.NO_USER_FOUND_BY_USERNAME;
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
    public List<Photo> getPhotos(String username) throws UserNotFoundException {
        List<Photo> photos = photoRepository.findAllByUser(findUser(username));
        return photos.stream().map(photo -> {
            return prepareEntity(photo, false);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Photo> getPhotos(String username, int page, int size) throws UserNotFoundException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "postDate"));
        List<Photo> photosPage = photoRepository.findAllByUser(findUser(username), pageable);
        return photosPage.stream().map(photo -> {
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

        photoRepository.deleteById(photo.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likePhoto(String username, Integer idPhoto)
            throws UserNotFoundException, PhotoNotFounException {
        // getPhoto(idPhoto);
        Like like = likeRepository.getByUsernameAndPhoto(username, idPhoto);
        if(like != null) {
            Photo photo = like.getPhoto();
            photo.lessLike();
            likeRepository.deleteById(like.getId());
            photoRepository.save(photo);
        } else {
            Photo photo = findPhoto(idPhoto);
            User user = findUser(username);
            photo.plusLike();
            likeRepository.save(Like.builder().user(user).photo(photo).build());
            photoRepository.save(photo);
        }

    }

//    private void deletePhoto(Photo photo, User user) throws IOException {
//        Path photoPath = Paths.get(USER_FOLDER + user.getUsername() +
//                PHOTOS_FOLDER + FORWARD_SLASH + photo.getId() + DOT + JPG_EXTENSION)
//                .toAbsolutePath().normalize();

//        Files.deleteIfExists(photoPath);

//    }

    private Photo savePhoto(User user, String description, boolean allowComments, MultipartFile photoImage)
            throws IOException, NotAnImageFileException {
        if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE)
                .contains(photoImage.getContentType())) {
            throw new NotAnImageFileException(photoImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
        }


        Photo savedPhoto = photoRepository.save(Photo.builder().allowComments(allowComments).description(description)
                .user(user).file64(photoImage.getBytes()).build());
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
            photo.setNumberOfcomments(photo.getComments().size());
        } else {
            photo.setComments(null);
            //TODO - fix to get real count
            photo.setNumberOfcomments(0);
            photo.setUrl(generateBase64Image(photo.getFile64()));
        }
        return photo;
    }

    private String generateBase64Image(byte[] file64) {
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_64_PREFIX);
        builder.append(Base64.getEncoder().encodeToString(file64));
        return builder.toString();
    }
}
