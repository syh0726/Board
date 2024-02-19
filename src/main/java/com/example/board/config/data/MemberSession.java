package com.example.board.config.data;

import lombok.Getter;

public class MemberSession {
    public final Long id;
    public final String accessToken;

    public MemberSession(Long id, String accessToken){
        this.id=id;
        this.accessToken = accessToken;
    }

}
