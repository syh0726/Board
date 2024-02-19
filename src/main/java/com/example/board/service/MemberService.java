package com.example.board.service;

import com.example.board.config.data.MemberSession;
import com.example.board.crypto.PasswordEncoder;
import com.example.board.domain.auth.Session;
import com.example.board.domain.member.Member;
import com.example.board.exception.member.*;
import com.example.board.repository.auth.SessionRepository;
import com.example.board.repository.member.MemberRepository;
import com.example.board.requestDto.member.FindPasswordDto;
import com.example.board.requestDto.member.SignUpDto;
import com.example.board.requestDto.member.SignInDto;
import com.example.board.requestServiceDto.member.ChangeNicknameServiceDto;
import com.example.board.requestServiceDto.member.ChangePasswordServiceDto;
import com.example.board.requestServiceDto.member.ChangePhoneServiceDto;
import com.example.board.responseDto.member.GetActivictyResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(SignUpDto signupdto) {

        if(memberRepository.findByEmail(signupdto.getEmail())!=null){
           throw new AlreadyExistEmailException();
       }else if(memberRepository.findByPhoneNumber(signupdto.getPhoneNumber())!=null){
           throw new AlreadyExistPhoneNumberException();
       }else if(memberRepository.findByNickname(signupdto.getNickName())!=null){
           throw new AlreadyExistNicknameException();
       }

       Member member=Member.builder()
               .email(signupdto.getEmail())
               .phoneNumber(signupdto.getPhoneNumber())
               .password(passwordEncoder.encrypt(signupdto.getPassword()))
               .nickName(signupdto.getNickName())
               .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public String signin(SignInDto signInDto) {
        Member member=memberRepository.findByEmail(signInDto.getEmail());

        if(member==null){
            throw new InvalidSigninInformationException();
        }

        boolean matches= passwordEncoder.matches(signInDto.getPassword(), member.getPassword());

        if(!matches){
            throw new InvalidSigninInformationException();
        }

        Session session =Session.builder()
                .member(member)
                .build();

        return session.getAccessToken();
    }

    @Transactional
    public void logout(MemberSession memberSession) {
        Session session=sessionRepository.findByAccessToken(memberSession.accessToken);
        sessionRepository.delete(session);

    }

    @Transactional
    public boolean findpassword(FindPasswordDto findPasswordDto) {
        return memberRepository.findByEmailAndPhoneNumber(findPasswordDto);
    }

    @Transactional
    public void changePassword(ChangePasswordServiceDto changePasswordServiceDto) {
        Member member=memberRepository.findById(changePasswordServiceDto.getId()).orElseThrow(MemberNotFoundException::new);

        boolean matches=passwordEncoder.matches(changePasswordServiceDto.getOriginPassword(),member.getPassword());
        if(!matches){
            throw new InvalidSigninInformationException();
        }

        member.changePassword(passwordEncoder.encrypt(changePasswordServiceDto.getNewPassWord()));
    }

    @Transactional
    public void changePhoneNumber(ChangePhoneServiceDto changePhoneServiceDto) {
        Member member=memberRepository.findById(changePhoneServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        member.changePhoneNumber(changePhoneServiceDto.getPhoneNumber());
    }

    @Transactional
    public void changeNickName(ChangeNicknameServiceDto changeNicknameServiceDto) {
        Member member=memberRepository.findById(changeNicknameServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        member.changeNickname(changeNicknameServiceDto.getNickName());
    }


    //유저의 게시글 목록 불러오기 캐싱사용!!!(중요)
    @Transactional
    @Cacheable("posts")
    public GetActivictyResponseDto getActivity(long id) {
        return memberRepository.getActivityPosts(id);
    }
}
