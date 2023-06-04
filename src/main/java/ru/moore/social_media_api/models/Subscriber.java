package ru.moore.social_media_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "SUBSCRIBERS")
@Where(clause = "deleted = false")
public class Subscriber extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_from_id")
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_to_id")
    private Account accountTo;

}