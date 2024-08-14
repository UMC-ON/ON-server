package com.on.server.domain.dispatchCertify.domain;

public enum PermitStatus {
    AWAIT, // UserStatus.AWAIT 와 동일하게 가야 함 (교환/파견교 관리자 인증 승인 대기)
    ACTIVE, // UserStatus.ACTIVE 와 동일하게 가야 함 (교환/파견교 관리자 인증 승인 완료)
    DENIED, // UserStatus.DENIED 와 동일하게 가야 함 (교환/파견교 관리자 인증 승인 거절)
    CLOSED // 관리자의 승인/거절에 상관없이 종료된 상태(비활성화)
}
