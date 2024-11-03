package com.akdong.we.config.security;


import com.akdong.we.common.auth.MemberDetailService;
import com.akdong.we.common.jwt.JwtAuthFilter;
import com.akdong.we.common.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig  {
    private final MemberDetailService memberDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    //여기에 있는 애만 프리패스
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/api-docs/**", "/swagger-ui.html", "/v1/auth/login", "/v1/auth/email-code", "/v1/auth/find-password", "/v1/auth/reset-password",
            "/v1/no-auth/**", "/v1/email/**", "/v1/images/**", "/v1/images", "/v1/need-auth",
            "/v1/members/**", "/error", "/v1/conferences/**", "/room/**", "/v1/moderators/**",
            "/v1/media/**", "v1/articles/groups/**", "/v1/notifications/**", "/v1/groups/**", "v1/articles/**",
            "v1/articles", "/v1/statistics/members/**", "/v1/groups","**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);


        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(memberDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/groups/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/articles/**").permitAll()
                        //@PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
                        .anyRequest().authenticated()
//                        .anyRequest().authenticated()
        );



        return http.build();
    }




}
