package com.on.server.domain.home.application;

import com.on.server.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

//    public Object getInfoBoardList(User user) {
//    }

//    public Object getFreeBoardList(User user) {
//    }

//    public Object getCompanyBoardList(User user) {
//    }
}
