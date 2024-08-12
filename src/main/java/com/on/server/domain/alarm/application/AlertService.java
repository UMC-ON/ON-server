package com.on.server.domain.alarm.application;

import com.on.server.domain.alarm.domain.Alert;
import com.on.server.domain.alarm.domain.repository.AlertRepository;
import com.on.server.domain.alarm.dto.AlertListResponseDto;
import com.on.server.domain.alarm.dto.DeviceTokenRequestDto;
import com.on.server.domain.alarm.dto.FcmRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertService {

    private final AlertRepository alertRepository;

    private final UserRepository userRepository;

    // 1. 사용자 디바이스 토큰 저장하기
    @Transactional
    public void saveDeviceToken(User user, DeviceTokenRequestDto deviceTokenRequestDto) {

        user.setDeviceToken(deviceTokenRequestDto.getDeviceToken());

        userRepository.save(user);
    }

}
