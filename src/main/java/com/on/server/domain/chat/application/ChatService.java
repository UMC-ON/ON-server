package com.on.server.domain.chat.application;

import com.on.server.global.common.exceptions.BaseRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    // ** 추후 삭제 예정 **
    public String getApiTest() throws BaseRuntimeException {
        return "API 응답 통일 테스트입니다.";
    }
    // ******************

}
