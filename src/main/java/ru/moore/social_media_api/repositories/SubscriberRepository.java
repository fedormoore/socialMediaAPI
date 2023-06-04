package ru.moore.social_media_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moore.social_media_api.models.Subscriber;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    Optional<Subscriber> findByAccountFromIdAndAccountToId(UUID accountFromId, UUID accountToId);

    List<Subscriber> findAllByAccountFromId(UUID id);

    Optional<Subscriber> findByIdAndAccountFromId(UUID id, UUID accountFromId);
}
