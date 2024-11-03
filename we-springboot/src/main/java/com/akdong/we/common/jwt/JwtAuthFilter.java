package com.akdong.we.common.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.akdong.we.common.auth.MemberDetailService;
import com.akdong.we.common.dto.ErrorResponse;
import com.akdong.we.member.exception.auth.AuthErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private final MemberDetailService memberDetailService;
    private final JwtUtil jwtUtil;

    @Override
    /**
     * JWT 토큰 검증 필터 수행
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        //JWT가 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            //JWT 유효성 검증
            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserId(token);
                //유저와 토큰 일치 시 userDetails 생성
                UserDetails memberDetails = memberDetailService.loadUserByUsername(userId.toString());

                if (memberDetails != null) {
                    //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

                    //현재 Request의 Security Context에 접근권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } else{
                log.info("----------------- jwt error -------------------");
                jwtExceptionHandler(response, AuthErrorCode.AUTH_INVALID_ACCESS_TOKEN);
                return;
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }


    private void jwtExceptionHandler(HttpServletResponse response, AuthErrorCode errorCode){
        response.setStatus(403);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ErrorResponse<?> errorResponse = new ErrorResponse<>("다시 로그인해주세요", null);

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonErrorResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
