package com.on.server.domain.alarm.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.on.server.domain.alarm.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {
    @Value("${firebase.api-url}")
    private String API_URL;

    @Value("${firebase.key-path}")
    private String FIREBASE_KEY_PATH;

    private final ObjectMapper objectMapper;

    public Response sendMessage(String deviceToken, String title, String body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FcmMessage message = FcmMessage.makeMessage(deviceToken, title, body);
        RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(message),
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        return client.newCall(request).execute();
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
