package com.on.server.domain.chat.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "chatting_room")
public class ChattingRoom extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "chatting_room_type", nullable = false)
    private ChatType chattingRoomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_one")
    private User chatUserOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_two")
    private User chatUserTwo;
}
