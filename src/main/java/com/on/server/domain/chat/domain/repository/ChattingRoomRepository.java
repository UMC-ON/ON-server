package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChatType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    Page<ChattingRoom> findChattingRoomByChatUserOneOrChatUserTwo(User user, User user1, Pageable pageable);

    ChattingRoom findChattingRoomByChatUserOneAndChatUserTwoAndChattingRoomType(User user, User chatUserTwo, ChatType chatType);

}
