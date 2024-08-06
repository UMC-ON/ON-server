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
@Table(name = "special_chat")
public class SpecialChat extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "special_chat_type", nullable = false)
    private ChatType specialChatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_post_id")
    // ** 추가 예정

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "market_post_id")
    // ** 추가 예정

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_participant_id")
    // ** 추가 예정

}
