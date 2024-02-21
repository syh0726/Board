package com.board.config;

import com.board.config.data.MemberSession;
import com.board.domain.auth.Session;
import com.board.exception.auth.UnAuthException;
import com.board.repository.auth.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    @Override   //어떤 클레스가 parameter로 들어왔을때 인가요청을 할 것인지.(이 경우 parameter에 memebersession들어오면 인가요청)
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberSession.class);
    }
    @Override //인가
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest=webRequest.getNativeRequest(HttpServletRequest.class);

        if(httpServletRequest==null){
            log.error("serveletRequest is null");
            throw new UnAuthException();
        }

        Cookie[] cookies= httpServletRequest.getCookies();

        if(cookies.length==0){
            log.error("쿠키가 없음");
            throw new UnAuthException();
        }

        String accessToken=cookies[0].getValue();
        Session session=sessionRepository.findByAccessToken(accessToken);

        if(session==null){
            log.error("DB에 세션이 존재하지 않음");
            throw new UnAuthException();
        }

        return new MemberSession(session.getMember().getId(),session.getAccessToken());
    }
}
