package org.zerock.b52.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b52.dto.MemberDTO;
import org.zerock.b52.dto.MemberReadDTO;
import org.zerock.b52.mappers.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final MemberMapper memberMapper;

    // 안좋은 방식
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     /* 리턴타입이 UserDetails 이므로 Mapper를 통해 MemberDTO로 반환해야한다.  */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUserName : " + username);

        MemberReadDTO readDTO = memberMapper.selectOne(username);

        log.info(readDTO);

        MemberDTO memberDTO = 
        new MemberDTO(username, 
        readDTO.getMpw(), 
        readDTO.getMname(),
        // 권한은 SimpleGranteAuthority객체이므로 나중에 Map을 써서 타입을 바꿔줘야 함 
        readDTO.getRolenames()
        );

        // 화면단 접근시 principal.mname
        // memberDTO.setMname("키보드워리어");

        return memberDTO;
    }
    
}
