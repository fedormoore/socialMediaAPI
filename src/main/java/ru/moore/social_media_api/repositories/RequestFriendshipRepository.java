package ru.moore.social_media_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moore.social_media_api.models.RequestFriendship;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestFriendshipRepository extends JpaRepository<RequestFriendship, UUID> {

    List<RequestFriendship> findAllByAccountFromId(UUID id);
}
