package com.akdong.we.notification.service;

import com.akdong.we.notification.domain.FcmMessage;
import com.akdong.we.notification.domain.FcmMessage.*;
import com.akdong.we.notification.domain.TokenDto;
import com.akdong.we.notification.domain.TokenEntity;
import com.akdong.we.notification.repository.TokenRepository;
import com.akdong.we.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Service
public class FirebaseService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/wewe-a3725/messages:send";
    public final ObjectMapperProvider objectMapperProvider;
    private String accessToken;
    private final ResourceLoader resourceLoader;

    private final TokenRepository tokenRepository;


    @PostConstruct
    public void init() throws IOException {
        try {
            getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
            // 로그 출력 또는 적절한 예외 처리
        }
    }

    @Scheduled(fixedRate = 3000000)  // 50분(3000초)마다 갱신
    public void getAccessToken() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:firebase/firebase_service_key.json");
        InputStream inputStream = resource.getInputStream();

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        googleCredentials.refreshIfExpired();
        accessToken = googleCredentials.getAccessToken().getTokenValue();
    }

    public String getCurrentAccessToken() {
        return accessToken;
    }

    public Response sendMessageTo(long user_id, String title, String body) throws IOException
    {
        String targetToken = Optional
                .of(tokenRepository.findTokenByUserId(user_id))
                .orElseThrow()
                .asDto()
                .getToken();

        if(targetToken == null) return null;
        String message = makeMessage(targetToken,title,body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + getCurrentAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE,"application/json;UTF-8")
                .build();
        return client.newCall(request).execute();
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        Data data = new Data(title,body);
        Message message = new Message(data,targetToken);
        FcmMessage fcmMessage = new FcmMessage(false,message);
        return objectMapperProvider.jsonMapper().writeValueAsString(fcmMessage);
    }

    public TokenDto registToken(String token) {
        TokenEntity tokenEntity = tokenRepository
                .findTokenByUserId(Util.getUserIdFromJwt());

        tokenEntity = tokenEntity == null ? TokenEntity.builder()
                .user_id(Util.getUserIdFromJwt()).build() : tokenEntity;

        tokenEntity.setToken(token);
        return tokenRepository.save(tokenEntity).asDto();
    }
}