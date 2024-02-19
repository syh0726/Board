package com.example.board.controller;


import com.example.board.config.data.MemberSession;
import com.example.board.domain.member.Member;
import com.example.board.repository.member.MemberRepository;
import com.example.board.requestDto.member.FindPasswordDto;
import com.example.board.requestDto.member.SignUpDto;
import com.example.board.requestDto.member.SignInDto;
import com.example.board.requestDto.member.ChangeNicknameDto;
import com.example.board.requestDto.member.ChangePasswordDto;
import com.example.board.requestDto.member.ChangePhoneDto;
import com.example.board.requestServiceDto.member.ChangeNicknameServiceDto;
import com.example.board.requestServiceDto.member.ChangePasswordServiceDto;
import com.example.board.requestServiceDto.member.ChangePhoneServiceDto;
import com.example.board.responseDto.member.GetActivictyResponseDto;
import com.example.board.responseDto.member.GetProfileResponseDto;
import com.example.board.responseDto.member.SignInResponseDto;
import com.example.board.service.AuthService;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/auth/signup")
    public void signUp(@RequestBody @Valid SignUpDto signUpDto){
        memberService.signup(signUpDto);
    }

    @PostMapping("/auth/signin")
    public SignInResponseDto signIn(@RequestBody @Valid SignInDto signInDto, HttpServletResponse response){

        String accessToken=memberService.signin(signInDto);
        ResponseCookie cookie= authService.AcceessTokenToCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());

        return memberRepository.findBAccessToken(accessToken).toSignInResponseDto();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Object> logOut(MemberSession memberSession){
       memberService.logout(memberSession);
       ResponseCookie cookie=authService.removeCooke();
       return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).build();
    }

    //계정 정보 불러오기
    @GetMapping("/auth/profile")
    public GetProfileResponseDto getProfile(MemberSession memberSession){
        Member member=memberRepository.getMemberbyId(memberSession.id);
        return member.toGetProfileResponseDto();
    }

    //비밀번호 변경
    @PutMapping("/auth/setting/password")
    public void changePassword(MemberSession memberSession,@RequestBody @Valid ChangePasswordDto changePasswordDto){
        ChangePasswordServiceDto changePasswordServiceDto=ChangePasswordServiceDto.builder()
                .originPassword(changePasswordDto.getOriginPassword())
                .newPassWord(changePasswordDto.getNewPassword())
                .id(memberSession.id)
                .build();

        memberService.changePassword(changePasswordServiceDto);
    }

    //휴대폰 번호 변경
    @PutMapping("/auth/setting/phone")
    public void changePhoneNumber(MemberSession memberSession,@RequestBody @Valid ChangePhoneDto changePhoneDto){
        ChangePhoneServiceDto changePhoneServiceDto=ChangePhoneServiceDto.builder()
                .id(memberSession.id)
                .phoneNumber(changePhoneDto.getPhoneNumber())
                .build();

        memberService.changePhoneNumber(changePhoneServiceDto);
    }

    //닉네임 변경
    @PutMapping("/auth/setting/nickname")
    public void changeNickName(MemberSession memberSession,@RequestBody @Valid ChangeNicknameDto changeNicknameDto){
        ChangeNicknameServiceDto changeNicknameServiceDto= ChangeNicknameServiceDto.builder()
                .id(memberSession.id)
                .nickName(changeNicknameDto.getNickName())
                .build();

        memberService.changeNickName(changeNicknameServiceDto);
    }

    //사용자의 게시글 목록 불러오기
    @GetMapping("/member/{id}")
    public GetActivictyResponseDto getPosts(@PathVariable(name = "id") long id){
        return memberService.getActivity(id);
    }



    @PostMapping("/auth/password")
    public boolean findPassword(@RequestBody @Valid FindPasswordDto findPasswordDto){
        return memberService.findpassword(findPasswordDto);
    }

}
