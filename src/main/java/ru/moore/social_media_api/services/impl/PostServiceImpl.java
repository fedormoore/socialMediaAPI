package ru.moore.social_media_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.moore.social_media_api.DTO.PostDTO;
import ru.moore.social_media_api.exeptions.ErrorTemplate;
import ru.moore.social_media_api.models.Post;
import ru.moore.social_media_api.models.Subscriber;
import ru.moore.social_media_api.repositories.PostRepository;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.services.PostService;
import ru.moore.social_media_api.services.SubscriberService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final AuthService authService;

    private final MapperUtils mapperUtils;
    private final PostRepository postRepository;

    private final SubscriberService subscriberService;

    private final static String UPLOADED_FOLDER = "c:/2/";

    /**
     * Метод позволяет получить список всех постов друзей на которых подписан пользователе
     *
     * @param sorted         сортировка
     * @param page           пагинация, страница
     * @param pageSize       Пагинация, количество записей на странице
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public Page<PostDTO> getPostSubscriptions(String sorted, int page, int pageSize, Authentication authentication) {
        List<Subscriber> subscriberList = subscriberService.findAllByAccountFromId(authService.getUserPrincipal(authentication).getId());
        List<UUID> authorIdList = new ArrayList<>();
        for (Subscriber subscriber : subscriberList) {
            authorIdList.add(subscriber.getAccountTo().getId());
        }

        Specification<Post> whereInventoryId = (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("author").get("id")).value(authorIdList);

        return postRepository.findAll(whereInventoryId, PageRequest.of(page - 1, pageSize, Sort.by(sorted.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt"))).map(this::toDtoBudgetAccount);
    }

    private PostDTO toDtoBudgetAccount(Post budgetAccount) {
        return mapperUtils.map(budgetAccount, PostDTO.class);
    }

    /**
     * Метод позволяет новый пост
     *
     * @param postDTO        пост
     * @param file           возможность прикрепить изображение к посту
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public PostDTO newPost(PostDTO postDTO, MultipartFile file, Authentication authentication) {
        String filePatch = saveUploadedFile(file);
        Post post = new Post();
        post = mapperUtils.map(postDTO, Post.class);
        post.setImagePost(filePatch);
        post.setAuthor(authService.getUserPrincipal(authentication));

        postRepository.save(post);
        return mapperUtils.map(post, PostDTO.class);
    }

    /**
     * Метод позволяет обновить пост
     *
     * @param postDTO        пост который необходимо обновить
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public PostDTO updatePost(PostDTO postDTO, Authentication authentication) {
        Post post = findByIdAndAuthor(postDTO.getId(), authService.getUserPrincipal(authentication).getId()).get();
        post.setHeaderPost(postDTO.getHeaderPost());
        post.setTextPost(postDTO.getTextPost());
        postRepository.save(post);
        return mapperUtils.map(post, PostDTO.class);
    }

    /**
     * Метод позволяет удалить пост
     *
     * @param postDTO        пост который необходимо удалить
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public ResponseEntity<String> deletePost(PostDTO postDTO, Authentication authentication) {
        Post post = findByIdAndAuthor(postDTO.getId(), authService.getUserPrincipal(authentication).getId()).get();
        post.setDeleted(true);
        postRepository.save(post);
        return new ResponseEntity<>("Пост удален", HttpStatus.OK);
    }

    /**
     * Метод позволяет найти пост по id и автору
     *
     * @param postId   id поста
     * @param authorId id автора
     */
    @Override
    public Optional<Post> findByIdAndAuthor(UUID postId, UUID authorId) {
        if (postId != null) {
            Optional<Post> postFind = postRepository.findById(postId);
            if (postFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Пост с ID " + postId + " не найден!");
            }
            if (!postFind.get().getAuthor().getId().equals(authorId)) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Пост с ID " + postId + " принадлежит другому пользователю!");
            }
            return postFind;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод позволяет сохранить прикрепленное изображение
     *
     * @param file прикрепленное изображение
     */
    private String saveUploadedFile(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                byte[] bytes = new byte[0];

                bytes = file.getBytes();

                Path path = Paths.get(UPLOADED_FOLDER + UUID.randomUUID() + ".jpg");
                Files.write(path, bytes);
                return path.toString();
            }
            return "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
