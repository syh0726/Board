package com.example.board.repository.auth;

import com.example.board.domain.auth.Session;
import com.example.board.domain.member.Member;

public interface SessionRepositoryCustom {
    Session findByAccessToken(String accessToken);
}
