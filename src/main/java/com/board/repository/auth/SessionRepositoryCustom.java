package com.board.repository.auth;

import com.board.domain.auth.Session;

public interface SessionRepositoryCustom {
    Session findByAccessToken(String accessToken);
}
