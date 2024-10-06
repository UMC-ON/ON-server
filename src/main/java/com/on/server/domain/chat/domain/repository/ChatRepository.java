package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChattingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findAllByChattingRoom(ChattingRoom currentChattingRoom, Pageable pageable);

    Chat findFirstByChattingRoomOrderByCreatedAtDesc(ChattingRoom chattingRoom);
}
