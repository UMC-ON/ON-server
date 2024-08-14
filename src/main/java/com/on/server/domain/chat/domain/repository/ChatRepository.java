package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChattingRoom(ChattingRoom currentChattingRoom);

    Chat findFirstByChattingRoomOrderByCreatedAtDesc(ChattingRoom chattingRoom);
}
