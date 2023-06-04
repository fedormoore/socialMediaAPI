package ru.moore.social_media_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "post")
@Where(clause = "deleted = false")
public class Post extends BaseEntity {

    @Column(name = "header_post")
    private String headerPost;

    @Column(name="text_post")
    private String textPost;

    @Column(name = "image_post")
    private String imagePost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Account author;

}