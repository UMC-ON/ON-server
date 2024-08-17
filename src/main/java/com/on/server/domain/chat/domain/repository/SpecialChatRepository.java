package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialChatRepository extends JpaRepository<SpecialChat, Long> {

    SpecialChat findByChattingRoom(ChattingRoom chattingRoom);
}
