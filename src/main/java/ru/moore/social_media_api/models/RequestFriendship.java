package ru.moore.social_media_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import ru.moore.social_media_api.enums.StatusRequestFriendshipEnum;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "REQUEST_FRIENDSHIP")
@Where(clause = "deleted = false")
public class RequestFriendship extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from_id")
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to_id")
    private Account accountTo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusRequestFriendshipEnum status = StatusRequestFriendshipEnum.NEW;

}