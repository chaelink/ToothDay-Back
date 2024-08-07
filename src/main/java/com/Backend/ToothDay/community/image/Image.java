package com.Backend.ToothDay.community.image;

import com.Backend.ToothDay.community.post.model.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @JsonBackReference //순환참조 방지
    private Post post;

    private String imageUrl;

    public Image() {}

    public Image(Post post, String imageUrl) {
        this.post = post;
        this.imageUrl = imageUrl;
    }
}
