package com.on.server.domain.user.application;

import java.security.SecureRandom;

public class TempPasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom random = new SecureRandom();

    public static String generateTempPassword(int minLength, int maxLength) {
        // 임시 비밀번호의 길이를 8~20자리 중 랜덤하게 선택
        int passwordLength = random.nextInt(maxLength - minLength + 1) + minLength;

        // 비밀번호 생성
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

}
