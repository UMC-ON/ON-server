package com.on.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /*
    1000 : Request 성공
    */
    SUCCESS(ResponseType.SUCCESS, 1000, true, "요청에 성공하였습니다."),

    /*
        2000~ : Request 오류
    */


    // =====================================
    /*
        3000~ : Response 오류
    */



    /**
     * 400 Bad Request
     */
    ROW_DOES_NOT_EXIST(ResponseType.BAD_REQUEST, 4000, false, "해당하는 데이터가 존재하지 않습니다."),
    ROW_ALREADY_EXIST(ResponseType.BAD_REQUEST, 4001, false, "해당하는 데이터가 이미 존재합니다."),
    INVALID_PARAMETER(ResponseType.BAD_REQUEST, 4002, false, "유효하지 않은 파라미터입니다."),            // Parameter format error
    INVALID_USER_DATA_REQUEST(ResponseType.BAD_REQUEST, 4003, false, "유효하지 않은 사용자 데이터 요청입니다."),    // User signup & update validation error
    TARGET_DELETED(ResponseType.BAD_REQUEST, 4004, false, "해당 데이터가 삭제되었습니다."),
    INVALID_HTTP_METHOD(ResponseType.BAD_REQUEST, 4005, false, "유효하지 않은 HTTP Method입니다."),
    APPLY_NOT_EXIST(ResponseType.BAD_REQUEST, 4006, false, "해당하는 신청이 존재하지 않습니다."),
    CANNOT_PERFORMED(ResponseType.BAD_REQUEST, 4007, false, "수행할 수 없는 작업입니다."),
    AWAITING_STATUS(ResponseType.BAD_REQUEST, 4008, false, "대기 상태입니다."),
    INVALID_STUDENT_ID(ResponseType.BAD_REQUEST, 4009, false, "유효하지 않은 학번입니다."),
    TIME_NOT_PASSED(ResponseType.BAD_REQUEST, 4010, false, "시간이 지나지 않았습니다."),
    NO_APPLICATION(ResponseType.BAD_REQUEST, 4011, false, "신청이 없습니다."),
    NEED_SIGN_IN(ResponseType.BAD_REQUEST, 4012, false, "로그인이 필요합니다."),
    INVALID_REQUEST_ROLE(ResponseType.BAD_REQUEST, 4013, false, "유효하지 않은 요청 권한입니다."),
    INVALID_REQUEST_USER_STATE(ResponseType.BAD_REQUEST, 4014, false, "유효하지 않은 사용자 상태입니다."),
    INVALID_FILE_EXTENSION(ResponseType.BAD_REQUEST, 4015, false, "유효하지 않은 파일 확장자입니다."),
    INVALID_EXPIRE_DATE(ResponseType.BAD_REQUEST, 4016, false, "유효하지 않은 만료 날짜입니다."),
    FILE_NEEDED(ResponseType.BAD_REQUEST, 4017, false, "파일이 필요합니다."),
    INVALID_CHATTING_ROOM(ResponseType.BAD_REQUEST, 4018, false, "유효하지 않은 채팅방입니다."),

    /**
     * 401 Unauthorized
     */
    API_NOT_ACCESSIBLE(ResponseType.UNAUTHORIZED, 4100, false, "API 접근이 불가능합니다."),
    INVALID_SIGNIN(ResponseType.UNAUTHORIZED, 4101, false, "유효하지 않은 로그인입니다."),
    BLOCKED_USER(ResponseType.UNAUTHORIZED, 4102, false, "차단된 사용자입니다."),
    INACTIVE_USER(ResponseType.UNAUTHORIZED, 4103, false, "비활성화된 사용자입니다."),
    AWAITING_USER(ResponseType.UNAUTHORIZED, 4104, false, "대기 중인 사용자입니다."),
    INVALID_JWT(ResponseType.UNAUTHORIZED, 4105, false, "유효하지 않은 JWT입니다."),
    GRANT_ROLE_NOT_ALLOWED(ResponseType.UNAUTHORIZED, 4106, false, "부여된 권한이 허용되지 않습니다."),
    API_NOT_ALLOWED(ResponseType.UNAUTHORIZED, 4107, false, "API 접근이 허용되지 않습니다."),
    NOT_MEMBER(ResponseType.UNAUTHORIZED, 4108, false, "회원이 아닙니다."),
    REJECT_USER(ResponseType.UNAUTHORIZED, 4109, false, "거부된 사용자입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER(ResponseType.INTERNAL_SERVER_ERROR, 5000, false, "서버 오류가 발생했습니다."),

    /**
     * 503 Service Unavailable Error
     */
    SERVICE_UNAVAILABLE(ResponseType.SERVICE_UNAVAILABLE, 5000, false, "서비스 이용이 불가능합니다."),
    DATA_NOT_EXIEST(ResponseType.SERVICE_UNAVAILABLE, 5005, false, "데이터가 존재하지 않습니다."),
    FILE_UPLOAD_FAIL(ResponseType.SERVICE_UNAVAILABLE, 5006, false, "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAIL(ResponseType.SERVICE_UNAVAILABLE, 5007, false, "파일 삭제에 실패했습니다."),
    FAILED_TO_SEND_ALERT(ResponseType.SERVICE_UNAVAILABLE, 5008, false, "알림 전송에 실패하였습니다.");


    // =====================================
    private final ResponseType responseType;
    private final Integer code;
    private final Boolean  inSuccess;
    private final String message;

}
