package ru.moore.social_media_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.moore.social_media_api.DTO.SubscriberDTO;
import ru.moore.social_media_api.exeptions.ErrorTemplate;
import ru.moore.social_media_api.models.Account;
import ru.moore.social_media_api.models.Subscriber;
import ru.moore.social_media_api.repositories.SubscriberRepository;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.services.SubscriberService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final AuthService authService;

    private final MapperUtils mapperUtils;

    private final SubscriberRepository subscriberRepository;

    /**
     * Метод позволяет получить список подписок на пользователей
     *
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public List<SubscriberDTO> getAllSubscribers(Authentication authentication) {
        return mapperUtils.mapAll(subscriberRepository.findAllByAccountFromId(authService.getUserPrincipal(authentication).getId()), SubscriberDTO.class);
    }

    /**
     * Метод позволяет создать новую подписку на пользователя
     *
     * @param newFriendId    id пользователя на которого подписываемся
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public ResponseEntity<String> newSubscribers(UUID newFriendId, Authentication authentication) {
        Account accountTo = authService.findById(newFriendId).get();
        Account accountFrom = authService.getUserPrincipal(authentication);

        Subscriber subscriber = new Subscriber();
        subscriber.setAccountFrom(accountFrom);
        subscriber.setAccountTo(accountTo);

        subscriberRepository.save(subscriber);

        return new ResponseEntity<>("Добавлена новая подписка", HttpStatus.OK);
    }

    /**
     * Метод позволяет удалить подписку на пользователя
     *
     * @param subscriberId id подписки
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public ResponseEntity<String> deleteSubscribers(UUID subscriberId, Authentication authentication) {
        Subscriber subscriber = findByIdAndAccountFromId(subscriberId, authService.getUserPrincipal(authentication).getId()).get();
        subscriber.setDeleted(true);
        subscriberRepository.save(subscriber);

        return new ResponseEntity<>("Подписка удалена", HttpStatus.OK);
    }

    /**
     * Метод позволяет проверить действительно ли подписка принадлежит пользователю
     *
     * @param id            id подписки
     * @param accountFromId id пользователь
     */
    @Override
    public Optional<Subscriber> findByIdAndAccountFromId(UUID id, UUID accountFromId) {
        if (id != null) {
            Optional<Subscriber> subscribersFind = subscriberRepository.findByIdAndAccountFromId(id, accountFromId);
            if (subscribersFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Подписка с ID " + accountFromId + " не найдена!");
            }
            return subscribersFind;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод позволяет проверить действительно ли есть подписка на пользователя
     *
     * @param accountFromId id пользователь кто подписан
     * @param accountToId   id пользователь на кого подписан
     */
    @Override
    public Optional<Subscriber> findByAccountFromIdAndAccountToId(UUID accountFromId, UUID accountToId) {
        if (accountFromId != null) {
            Optional<Subscriber> subscribersFind = subscriberRepository.findByAccountFromIdAndAccountToId(accountFromId, accountToId);
            if (subscribersFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Подписка с ID " + accountFromId + " не найдена!");
            }
            return subscribersFind;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод позволяет получить список подписок пользователя
     *
     * @param id id пользователя
     */
    @Override
    public List<Subscriber> findAllByAccountFromId(UUID id) {
        return subscriberRepository.findAllByAccountFromId(id);
    }
}
