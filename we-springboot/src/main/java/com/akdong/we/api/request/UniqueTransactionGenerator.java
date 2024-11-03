package com.akdong.we.api.request;

import java.security.SecureRandom;

public class UniqueTransactionGenerator {
    // 난수를 생성할 수 있는 객체
    private static final SecureRandom random = new SecureRandom();

    // 20자리 숫자 랜덤 문자열 생성
    public static String generateRandomTransactionNo() {
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            sb.append(random.nextInt(10));  // 0부터 9까지의 숫자를 생성
        }
        return sb.toString();
    }
}
