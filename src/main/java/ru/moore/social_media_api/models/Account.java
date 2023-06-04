package ru.moore.social_media_api.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ACCOUNTS")
@Where(clause = "deleted = false")
public class Account extends BaseEntity {

    @Column(name = "email", updatable = false)
    @Email()
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> postList;

}