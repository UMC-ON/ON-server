package com.on.server.domain.chat.domain.repository;

import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.marketPost.domain.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialChatRepository extends JpaRepository<SpecialChat, Long> {

    SpecialChat findByChattingRoom(ChattingRoom chattingRoom);

    List<SpecialChat> findByCompanyPost(CompanyPost companyPost);

    List<SpecialChat> findByMarketPost(MarketPost marketPost);
}
