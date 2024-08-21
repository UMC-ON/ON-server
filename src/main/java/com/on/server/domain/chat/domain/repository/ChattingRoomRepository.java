package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    Optional<ChattingRoom> findById(Long roomId);

    ChattingRoom findChattingRoomByChatUserOneAndChatUserTwo(User user, User user2);

    List<ChattingRoom> findChattingRoomByChatUserOneOrChatUserTwo(User user, User user1);
}
