package org.zerock.b52.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
public class MemberDTO extends User implements OAuth2User{

    private String mname;
    private String email;
    private String pw;

    // 생성자가 있으면 문제가 된다. Mybatis는 기본으로 Getter Setter동작
    public MemberDTO(String email, String mpw, String mname, List<String> roleNames){
        super(email,mpw, roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str))
        .collect(Collectors.toList()));
        this.mname = mname;
        this.email = email;
        this.pw = mpw;
    }


    // social 로그인 
    @Override
    public Map<String, Object> getAttributes() {


        return null;
    }

    @Override
    public String getName() {

    //    return this.getUsername();
       return this.email;
    }

    // public MemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    //     super(username, password, authorities);

    // }

    }
