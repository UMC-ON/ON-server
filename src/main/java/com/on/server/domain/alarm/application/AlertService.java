package com.on.server.domain.alarm.application;

import com.on.server.domain.alarm.domain.Alert;
import com.on.server.domain.alarm.domain.AlertType;
import com.on.server.domain.alarm.domain.repository.AlertRepository;
import com.on.server.domain.alarm.dto.*;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertService {

    private final AlertRepository alertRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final FcmService fcmService;

    // 1. 사용자 디바이스 토큰 저장하기
    @Transactional
    public void saveDeviceToken(User user, DeviceTokenRequestDto deviceTokenRequestDto) {

        user.setDeviceToken(deviceTokenRequestDto.getDeviceToken());

        userRepository.save(user);
    }

    // 2. 알림 리스트 조회하기
    public List<AlertListResponseDto> getAlertList(User user, Pageable pageable) {

        Page<Alert> alertList = alertRepository.findByUser(user, pageable);

        List<AlertListResponseDto> alertDtoList = getAlertListDto(alertList);

        return alertDtoList;
    }

    private static List<AlertListResponseDto> getAlertListDto(Page<Alert> alertList) {
        return alertList.stream()
                .map(alert -> new AlertListResponseDto(
                                alert.getTitle(),
                                alert.getContents(),
                                alert.getAlertType(),
                                alert.getAlertConnectId()
                        )
                ).toList();
    }

    // 3. 알림 저장하기
    @Transactional
    public void saveAlert(User user, FcmRequestDto fcmRequestDto) {
        Alert alert = Alert.builder()
                .title(fcmRequestDto.getTitle())
                .contents(fcmRequestDto.getBody())
                .alertConnectId(fcmRequestDto.getAlertConnectId())
                .alertType(fcmRequestDto.getAlertType())
                .user(user)
                .build();

        alertRepository.save(alert);
    }

    // 4. 알림 리다이렉트 url 찾기 및 읽음 표시
    @Transactional
    public AlertUrlDto markAsReadAndRedirect(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        alert.updateIsRead(alert.isRead());

        AlertType alertType = alert.getAlertType();
        Long connectId = alert.getAlertConnectId();

        // 동행은 동행글로 이동
        // /company-post/companyPostId
        if(alertType == AlertType.COMPANY) {
            String apiUrl = "/api/v1/company-post/";
            return new AlertUrlDto(connectId, apiUrl);
        }
        // 물품거래글 해당글로 이동
        // /market-post/{marketPostId}
        else if(alertType == AlertType.MARKET) {
            String apiUrl = "/api/v1/market-post/";
            return new AlertUrlDto(connectId, apiUrl);
        }
        // 정보, 자유는 댓글 알림 -> 해당글로 이동할 때 postId 이용
        else {
            Post post = postRepository.findById(connectId)
                    .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

            if(post.getBoard().getType() == BoardType.INFO) {
                String apiUrl = "/api/v1/post/INFO/";
                return new AlertUrlDto(connectId, apiUrl);
            }
            else {
                String apiUrl = "/api/v1/post/FREE/";
                return new AlertUrlDto(connectId, apiUrl);
            }
        }
    }

    // 4. 안 읽은 알림 개수
    public AlertCountDto getIsNotReadAlert(User user) {

        return new AlertCountDto(alertRepository.countByUserAndIsReadFalse(user));
    }

    // 5. 알림 보내기 및 저장
    @Transactional
    public void sendAndSaveAlert(User user, AlertType alertType, String title, String body, Long connectId) {
        try {
            // FCM 알림 전송
            fcmService.sendMessage(user.getDeviceToken(), alertType, title, body);

            FcmRequestDto fcmRequestDto = FcmRequestDto.builder()
                    .title(title)
                    .body(body)
                    .alertType(alertType)
                    .alertConnectId(connectId)
                    .build();

            // 알림 저장
            saveAlert(user, fcmRequestDto);

        } catch (IOException e) {
            throw new InternalServerException(ResponseCode.FAILED_TO_SEND_ALERT, "알림 전송에 실패했습니다: " + e.getMessage());
        }
    }

}
