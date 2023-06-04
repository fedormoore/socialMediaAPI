package ru.moore.social_media_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moore.social_media_api.DTO.FriendDTO;
import ru.moore.social_media_api.exeptions.ErrorTemplate;
import ru.moore.social_media_api.models.Account;
import ru.moore.social_media_api.models.Friend;
import ru.moore.social_media_api.repositories.FriendRepository;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.services.FriendService;
import ru.moore.social_media_api.services.SubscriberService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final AuthService authService;

    private final MapperUtils mapperUtils;

    private final FriendRepository friendRepository;

    private final SubscriberService subscriberService;

    /**
     * Метод позволяет получить список друзей
     *
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public List<FriendDTO> getAllFriends(Authentication authentication) {
        return mapperUtils.mapAll(friendRepository.findAllByAccountFromId(authService.getUserPrincipal(authentication).getId()), FriendDTO.class);
    }

    /**
     * Метод позволяет добавить пользователя в друзья
     *
     * @param friendFromId   id первого пользователя
     * @param friendToId id второго пользователя
     */
    @Override
    public ResponseEntity<String> newFriend(UUID friendFromId, UUID friendToId){
        Account accountFrom = authService.findById(friendFromId).get();
        Account accountTo = authService.findById(friendToId).get();

        Friend friend = new Friend();
        friend.setAccountFrom(accountFrom);
        friend.setAccountTo(accountTo);

        friendRepository.save(friend);

        return new ResponseEntity<>("Добавлен новый друг", HttpStatus.OK);
    }

    /**
     * Метод позволяет удалить пользователя из друзей
     *
     * @param friendToId id друга с кем дружим
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    @Transactional
    public ResponseEntity<String> deleteFriend(UUID friendToId, Authentication authentication) {
        Friend friend = findByAccountFromIdAndAccountToId(authService.getUserPrincipal(authentication).getId(), friendToId).get();
        friend.setDeleted(true);

        friendRepository.save(friend);

        UUID subscriberId = subscriberService.findByAccountFromIdAndAccountToId(authService.getUserPrincipal(authentication).getId(), friend.getAccountTo().getId()).get().getId();
        subscriberService.deleteSubscribers(subscriberId, authentication);

        return new ResponseEntity<>("Удалили из друзей", HttpStatus.OK);
    }

    /**
     * Метод позволяет получить запись о дружбе с пользователем
     *
     * @param friendFromId id пользователя кто дружит
     * @param friendToId     id записи
     */
    @Override
    public Optional<Friend> findByAccountFromIdAndAccountToId(UUID friendFromId, UUID friendToId) {
        if (friendToId != null) {
            Optional<Friend> friendsFind = friendRepository.findByAccountFromIdAndAccountToId(friendFromId, friendToId);
            if (friendsFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Аккаунт с ID " + friendToId + " не найден!");
            }
            return friendsFind;
        } else {
            return Optional.empty();
        }
    }
}
