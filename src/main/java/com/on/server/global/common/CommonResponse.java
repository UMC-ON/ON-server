package com.on.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommonResponse<T> {

    private int code;
    private boolean inSuccess;
    private String message;
    private T result;

    /**
     * 해당 Builder들은 코드 가독성을 위해 최대한 사용을 자제/마이그레이션 해주시고
     * 아래 정적 팩토리 메서드 사용 부탁드립니다.
     */
    // 요청에 성공한 경우
    @Builder
    public CommonResponse(ResponseCode status, T result) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();

        this.result = result;
    }

    // 요청에 실패한 경우
    @Builder
    public CommonResponse(ResponseCode status) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();
    }

    // 따로 Response가 필요없을 때 사용하는 정적 팩토리 메서드입니다.
    public static <T> CommonResponse<T> success() {
        return CommonResponse.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .inSuccess(true)
                .message(ResponseCode.SUCCESS.getMessage())
                .result(null)
                .build();
    }

    // Response가 필요할 때 사용하는 정적 팩토리 메서드 입니다.
    public static <T> CommonResponse<T> ok(T result) {
        return CommonResponse.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .inSuccess(true)
                .message(ResponseCode.SUCCESS.getMessage())
                .result(result)
                .build();
    }

    // 요청 실패 시
    public static <T> CommonResponse<T> error(BaseRuntimeException baseRuntimeException) {
        return CommonResponse.<T>builder()
                .code(baseRuntimeException.getResponseCode().getCode())
                .inSuccess(false)
                .message(baseRuntimeException.getMessage())
                .result(null)
                .build();
    }

}
