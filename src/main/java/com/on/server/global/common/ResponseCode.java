package com.on.server.global.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    /*
    1000 : Request 성공
    */
    SUCCESS(1000, true, "요청에 성공하였습니다."),

    /*
        2000~ : Request 오류
    */


    // =====================================
    /*
        3000~ : Response 오류
    */

    // 그 외 오류
    INTERNAL_SERVER_ERROR(9000, false, "서버 오류가 발생했습니다.");


    // =====================================
    private int code;
    private boolean inSuccess;
    private String message;


    /*
        해당되는 코드 매핑
        @param code
        @param inSuccess
        @param message

     */
    ResponseCode(int code, boolean inSuccess, String message) {
        this.inSuccess = inSuccess;
        this.code = code;
        this.message = message;
    }
}
