package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long> {

}
