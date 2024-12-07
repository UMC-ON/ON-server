package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChatType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query("SELECT cr FROM ChattingRoom cr WHERE (cr.chatUserOne = :user OR cr.chatUserTwo = :user) AND cr.chattingRoomType = :chattingRoomType")
    Page<ChattingRoom> findByChatUserOneOrChatUserTwoAndChattingRoomType(
            @Param("user") User user,
            @Param("chattingRoomType") ChatType chattingRoomType,
            Pageable pageable
    );

    List<ChattingRoom> findChattingRoomByChatUserOneAndChatUserTwoAndChattingRoomType(User user, User chatUserTwo, ChatType chatType);

}
