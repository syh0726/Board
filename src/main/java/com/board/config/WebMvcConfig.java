package com.board.config;

import com.board.repository.auth.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final SessionRepository sessionRepository;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(sessionRepository));
    }



    //cors설정
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**") //모든 경로
                .allowedOrigins("https://community-board.store","http://localhost:5173") //로컬 환경과 aws 환경
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS") //허용할 메서드
                .allowedHeaders("*")    //모든 헤더
                .allowCredentials(true)     //credentials 허용
                .maxAge(3600);//프리플라이트 캐싱시간
    }

}
