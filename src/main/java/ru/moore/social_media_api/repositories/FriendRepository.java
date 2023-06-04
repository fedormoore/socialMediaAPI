package ru.moore.social_media_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moore.social_media_api.models.Friend;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {

    Optional<Friend> findByAccountFromIdAndAccountToId(UUID friendFromId, UUID friendToId);

    List<Friend> findAllByAccountFromId(UUID friendFromId);
}
