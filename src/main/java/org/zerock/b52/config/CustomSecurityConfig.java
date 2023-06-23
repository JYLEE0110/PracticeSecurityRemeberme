package org.zerock.b52.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
// MapperScan 같이 Application.java에 걸어줘도 되지만 Security설정한 곳에 선언
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final DataSource dataSource;

    // TokenRepository에 토큰 값 저장 함수
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // FilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("filter chain----------------------------");

        // 로그인 페이지 경로 지정
        http.formLogin(config -> {

            config.loginPage("/member/signin");

        });

        http.rememberMe(config -> {
            // 발행한 토큰 값 repository에 저장
            config.tokenRepository(persistentTokenRepository());
            config.tokenValiditySeconds(60 * 60 * 24 * 7);
        });

        // form 안에있는 Hidden으로 포함된 csrf input tag를 없애겠다.
        http.csrf(config -> {

            config.disable();

        });

        return http.build();
    }

}
