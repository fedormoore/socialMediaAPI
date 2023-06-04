package ru.moore.social_media_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moore.social_media_api.models.Chat;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByAccountFromIdInAndAccountToIdIn(List<UUID> asList, List<UUID> asList1);

}
