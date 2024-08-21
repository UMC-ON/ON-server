package com.on.server.domain.post.domain;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "is_anonymous_univ", nullable = false)
    private Boolean isAnonymousUniv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<UuidFile> images = new ArrayList<>();

}