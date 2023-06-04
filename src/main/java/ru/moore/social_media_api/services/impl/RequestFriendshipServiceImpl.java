package ru.moore.social_media_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moore.social_media_api.DTO.RequestFriendshipDTO;
import ru.moore.social_media_api.enums.StatusRequestFriendshipEnum;
import ru.moore.social_media_api.exeptions.ErrorTemplate;
import ru.moore.social_media_api.models.Account;
import ru.moore.social_media_api.models.RequestFriendship;
import ru.moore.social_media_api.repositories.RequestFriendshipRepository;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.services.FriendService;
import ru.moore.social_media_api.services.RequestFriendshipService;
import ru.moore.social_media_api.services.SubscriberService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestFriendshipServiceImpl implements RequestFriendshipService {

    private final AuthService authService;

    private final MapperUtils mapperUtils;

    private final FriendService friendService;

    private final SubscriberService subscriberService;

    private final RequestFriendshipRepository requestFriendshipRepository;

    /**
     * Метод позволяет получить список запросов в друзья
     *
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public List<RequestFriendshipDTO> getAllRequest(Authentication authentication) {
        return mapperUtils.mapAll(requestFriendshipRepository.findAllByAccountFromId(authService.getUserPrincipal(authentication).getId()), RequestFriendshipDTO.class);
    }

    /**
     * Метод позволяет создать новый запрос в друзья
     *
     * @param friendToId id пользователя которого добавляем в друзья
     * @param authentication       пользователь авторизованной сессии
     */
    @Override
    @Transactional
    public RequestFriendshipDTO newRequest(UUID friendToId, Authentication authentication) {
        Account accountTo = authService.findById(friendToId).get();
        Account accountFrom = authService.getUserPrincipal(authentication);

        if (accountTo.getId().equals(accountFrom.getId())) {
            throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Попытка подписаться на самого себя");
        }

        RequestFriendship requestFriendship = new RequestFriendship();
        requestFriendship.setStatus(StatusRequestFriendshipEnum.NEW);
        requestFriendship.setAccountFrom(accountFrom);
        requestFriendship.setAccountTo(accountTo);

        requestFriendshipRepository.save(requestFriendship);

        subscriberService.newSubscribers(requestFriendship.getAccountTo().getId(), authentication);

        return mapperUtils.map(requestFriendship, RequestFriendshipDTO.class);
    }

    /**
     * Метод позволяет подтвердить запрос в друзья
     *
     * @param requestId id запроса
     * @param authentication       пользователь авторизованной сессии
     */
    @Override
    @Transactional
    public ResponseEntity<String> confirmRequest(UUID requestId, Authentication authentication) {
        RequestFriendship requestFriendship = findById(requestId).get();

        checkRequestToAccount(requestFriendship, authentication);

        requestFriendship.setStatus(StatusRequestFriendshipEnum.CONFIRM);
        requestFriendshipRepository.save(requestFriendship);

        friendService.newFriend(requestFriendship.getAccountFrom().getId(), authService.getUserPrincipal(authentication).getId());
        friendService.newFriend(authService.getUserPrincipal(authentication).getId(), requestFriendship.getAccountFrom().getId());

        subscriberService.newSubscribers(requestFriendship.getAccountFrom().getId(), authentication);

        return new ResponseEntity<>("Запрос подтвержден", HttpStatus.OK);
    }

    /**
     * Метод позволяет отклонить запрос в друзья
     *
     * @param requestId id запроса
     * @param authentication       пользователь авторизованной сессии
     */
    @Override
    public ResponseEntity<String> rejectedRequest(UUID requestId, Authentication authentication) {
        RequestFriendship requestFriendship = findById(requestId).get();

        checkRequestToAccount(requestFriendship, authentication);

        requestFriendship.setStatus(StatusRequestFriendshipEnum.REJECTED);

        requestFriendshipRepository.save(requestFriendship);
        return new ResponseEntity<>("Запрос отклонен", HttpStatus.OK);
    }

    /**
     * Метод позволяет проверить существует ли запрос на добавления в друзья в базе данных
     *
     * @param id id запроса в друзья
     */
    @Override
    public Optional<RequestFriendship> findById(UUID id) {
        if (id != null) {
            Optional<RequestFriendship> requestFriendshipFind = requestFriendshipRepository.findById(id);
            if (requestFriendshipFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Запрос с ID " + id + " не найден!");
            }
            return requestFriendshipFind;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод позволяет проверить что запрос принадлежит пользователю
     *
     * @param @param         requestFriendshipDTO        принимает в качестве параметра RequestFriendshipDTO
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public void checkRequestToAccount(RequestFriendship requestFriendship, Authentication authentication) {
        Account accountFrom = authService.getUserPrincipal(authentication);
        if (!requestFriendship.getAccountTo().getId().equals(accountFrom.getId())) {
            throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Попытка изменить чужой запрос");
        }
    }
}
