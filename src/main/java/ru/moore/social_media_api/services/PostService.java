package ru.moore.social_media_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.moore.social_media_api.DTO.PostDTO;
import ru.moore.social_media_api.models.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    /**
     * Метод позволяет получить список всех постов друзей на которых подписан пользователе
     *
     * @param sorted         сортировка
     * @param page           пагинация, страница
     * @param pageSize       Пагинация, количество записей на странице
     * @param authentication пользователь авторизованной сессии
     */
    Page<PostDTO> getPostSubscriptions(String sorted, int page, int pageSize, Authentication authentication);

    /**
     * Метод позволяет новый пост
     *
     * @param postDTO        пост
     * @param file           возможность прикрепить изображение к посту
     * @param authentication пользователь авторизованной сессии
     */
    PostDTO newPost(PostDTO postDTO, MultipartFile file, Authentication authentication);

    /**
     * Метод позволяет обновить пост
     *
     * @param postDTO        пост который необходимо обновить
     * @param authentication пользователь авторизованной сессии
     */
    PostDTO updatePost(PostDTO postDTO, Authentication authentication);

    /**
     * Метод позволяет удалить пост
     *
     * @param postDTO        пост который необходимо удалить
     * @param authentication пользователь авторизованной сессии
     */
    ResponseEntity<String> deletePost(PostDTO postDTO, Authentication authentication);

    /**
     * Метод позволяет найти пост по id и автору
     *
     * @param postId   id поста
     * @param authorId id автора
     */
    Optional<Post> findByIdAndAuthor(UUID postId, UUID authorId);

}
