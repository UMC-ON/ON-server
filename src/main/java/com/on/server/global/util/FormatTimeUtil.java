package com.on.server.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatTimeUtil {

    /**
     * localDateTime2FormattedString() : LocalDateTime을 yyyy-MM-dd HH:mm 형태로 변환
     *
     * @param createdAt
     * @return yyyy-MM-dd HH:mm 형태 문자열
     */
    public static String localDateTime2FormattedString(LocalDateTime createdAt) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return createdAt.format(timeFormatter);
    }
}
