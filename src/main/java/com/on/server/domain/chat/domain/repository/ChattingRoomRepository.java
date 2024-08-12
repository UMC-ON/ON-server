package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
