package com.sns.yourconnection.model.entity.post;

import com.sns.yourconnection.model.entity.common.StorageType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"post_storage\"")
public class PostStorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originFilename;

    private String storeFilename;

    private String pathUrl;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private PostStorageEntity(String originFilename, String storeFilename, String pathUrl,
        StorageType storageType, PostEntity post) {
        this.originFilename = originFilename;
        this.storeFilename = storeFilename;
        this.pathUrl = pathUrl;
        this.storageType = storageType;
        this.post = post;
    }

    public static PostStorageEntity of(String originFilename, String storeFilename,
        String pathUrl, StorageType storageType, PostEntity post) {

        return new PostStorageEntity(originFilename, storeFilename, pathUrl, storageType, post);
    }

    public void setPost(PostEntity post) {
        if (this.post != null) {
            post.getPostStorage().remove(this);
        }
        this.post = post;
        post.getPostStorage().add(this);
    }
}
