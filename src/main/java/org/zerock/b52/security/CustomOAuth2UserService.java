package org.zerock.b52.security;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b52.dto.MemberDTO;
import org.zerock.b52.dto.MemberReadDTO;
import org.zerock.b52.mappers.MemberMapper;
import org.zerock.b52.security.handler.CustomOAuthSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// 소셜로그인 하기위해서 Service 생성
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("--------load User-----------");
        log.info(userRequest);
        log.info("============================");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("===============================");
        log.info(email);
        log.info("===============================");

        // DB에 해당 사용자가있으면
        MemberReadDTO memberReadDTO = memberMapper.selectOne(email);
        if(memberReadDTO != null){

        return new MemberDTO(email, 
        memberReadDTO.getMpw(), 
        memberReadDTO.getMname(),
        // 권한은 SimpleGranteAuthority객체이므로 나중에 Map을 써서 타입을 바꿔줘야 함 
        memberReadDTO.getRolenames()
        );

        }

        // 
        // PW를 사용하지않는 자원 이미 카카오에서 인증이되었기 때문에 비워둔다.
        MemberDTO memberDTO = new MemberDTO(email, "", "카카오사용자", List.of("USER"));

        return memberDTO;
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {

        log.info("KAKAO-----------------------------------------");

        Object value = paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");

        log.info("email..." + email);

        return email;
    }

}
