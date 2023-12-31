package org.zerock.b52.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Log4j2
public class MemberController {

    @PreAuthorize("permitAll")
    @GetMapping("/signup")
    public void signup() {

        log.info("GET | /member/signup");

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/mypage")
    public void mypage() {

        log.info("GET | /member/mypage");

    }

    @PreAuthorize("permitAll")
    @GetMapping("/signin")
    public void signin() {

        log.info("GET | /member/signin");
    }

}
