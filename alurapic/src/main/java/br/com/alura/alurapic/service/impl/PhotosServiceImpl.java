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

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.alura.alurapic.util.constant.FileConstant.BASE_64_PREFIX;
import static br.com.alura.alurapic.util.constant.FileConstant.NOT_AN_IMAGE_FILE;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Photo uploadPhotoBase64(String username, String description,
                                   boolean allowComments, String photo) throws UserNotFoundException, IOException, NotAnImageFileException {
        User user = findUser(username);

        return savePhotoBase64(user, description, allowComments, photo);
    }

    @Override
    public Photo getPhoto(Integer idPhoto) throws PhotoNotFounException {
        Photo photo = photoRepository.fetchByIdWithUser(idPhoto);
        if(photo == null)
            throw new PhotoNotFounException(NO_PHOTO_FOUND_BY_ID + idPhoto);
        return prepareEntity(photo, false);
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
    public boolean likePhoto(String username, Integer idPhoto)
            throws UserNotFoundException, PhotoNotFounException {
        // getPhoto(idPhoto);
        Like like = likeRepository.getByUsernameAndPhoto(username, idPhoto);
        if(like != null) {
            Photo photo = like.getPhoto();
            photo.lessLike();
            likeRepository.deleteById(like.getId());
            photoRepository.save(photo);
            return false;
        } else {
            Photo photo = findPhoto(idPhoto);
            User user = findUser(username);
            photo.plusLike();
            likeRepository.save(Like.builder().user(user).photo(photo).build());
            photoRepository.save(photo);
            return true;
        }

    }

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

    private Photo savePhotoBase64(User user, String description, boolean allowComments, String photoImage)
            throws IOException, NotAnImageFileException {

        if(!photoImage.startsWith(BASE_64_PREFIX)) {
            throw new NotAnImageFileException("This" + NOT_AN_IMAGE_FILE);
        }

        String onlyPhoto = photoImage.substring(BASE_64_PREFIX.length());
        byte[] file64 = Base64.getDecoder().decode(onlyPhoto);

        Photo savedPhoto = photoRepository.save(Photo.builder().allowComments(allowComments).description(description)
                .user(user).file64(file64).build());
        return savedPhoto;
    }

//    private String setPhotoUrl(String username, Integer id) {
//        return ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .path(PHOTO_PATH + username + FORWARD_SLASH + id +
//                         DOT + JPG_EXTENSION).toUriString();
//    }

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
        if(withComments) {
            photo.getComments().stream().map(comment -> {
                comment.setPhotoId(comment.getPhoto().getId());
                comment.setUsername(comment.getUser().getUsername());
                return comment;
            }).collect(Collectors.toSet());
            photo.setNumberOfcomments(photo.getComments().size());
        } else {
            photo.setComments(null);
            //TODO - has a better way?
            photo.setNumberOfcomments(commentRepository.countByPhoto(photo));
        }
        photo.setUsername(photo.getUser().getUsername());
        photo.setUrl(generateBase64Image(photo.getFile64()));
        return photo;
    }

    private String generateBase64Image(byte[] file64) {
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_64_PREFIX);
        builder.append(Base64.getEncoder().encodeToString(file64));
        return builder.toString();
    }
}
