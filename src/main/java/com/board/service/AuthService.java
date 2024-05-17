package com.board.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthService {

    //운영땐 secure true 개발떈 false
    public ResponseCookie AcceessTokenToCookie(String accessToken){
        return ResponseCookie.from("SESSION",accessToken)
                .sameSite("None")
                .maxAge(Duration.ofDays(30))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain("community-board.store")
                .build();
    }

    public ResponseCookie removeCooke(){
        return ResponseCookie.from("SESSION","")
                .sameSite("None")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain("community-board.store")
                .build();
    }


}
