package com.board.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CacheType {
    CACHE_STORE("posts",3*60*60);

    private final String cacheName; //캐시 이름
    private final int expireAfterAccess;  //만료시간
}
