package com.board.controller;

import com.board.crypto.PasswordEncoder;
import com.board.requestDto.member.*;
import com.board.service.PostService;
import com.board.domain.member.Member;
import com.board.repository.auth.SessionRepository;
import com.board.repository.member.MemberRepository;
import com.board.repository.post.PostRepository;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @AfterEach
    public void clean(){memberRepository.deleteAll();}


    public Cookie getCookie(){
        Member test=memberRepository.findByEmail("test@gmail.com");
        Member member=memberRepository.getMemberbyId(test.getId());
        String accessToken= member.getSessions().get(0).getAccessToken();
        return new Cookie("SESSION",accessToken);
    }

    public void SignIn(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .nickName("test")
                .password("test1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("test@gmail.com")
                .password("test1234")
                .build();

        memberService.signin(signInDto);
    }


    @Test
    @DisplayName("로그인 성공후 세션 1개 생성 응답값을 보면 쿠키에 세션값이 담긴것도 확인 가능")
    public void test1() throws Exception {
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .nickName("test")
                .password("test1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("test@gmail.com")
                .password("test1234")
                .build();

        String json=objectMapper.writeValueAsString(signInDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L,sessionRepository.count());
    }

    @Test
    @DisplayName("회원 가입 성공 1개 ")
    public void test2() throws Exception {
        SignUpDto signupDto= SignUpDto.builder()
                .email("test@gmail.com")
                .password("test1234")
                .phoneNumber("01012345678")
                .nickName("test")
                .build();

        String json=objectMapper.writeValueAsString(signupDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L,memberRepository.count());

    }

    @Test
    @DisplayName("로그아웃 ")
    public void test3() throws Exception {
        SignIn();
        Cookie cookie=getCookie();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());


        Assertions.assertEquals(0L,sessionRepository.count());
    }


    @Test
    @DisplayName("비번찾기 ")
    public void test4() throws Exception {
        SignIn();
        FindPasswordDto findPasswordDto=FindPasswordDto.builder()
                .email("test@gmail.com")
                .phoneNumber("010-1234-5678")
                .build();

        String json=objectMapper.writeValueAsString(findPasswordDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/findPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("비밀번호 변경")
    public void test5() throws Exception{
        SignIn();
        Cookie cookie=getCookie();

        ChangePasswordDto changePasswordDto= ChangePasswordDto.builder()
                .originPassword("test1234")
                .newPassword("test0000")
                .build();

        String json=objectMapper.writeValueAsString(changePasswordDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/auth/setting/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .content(json))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("휴대폰번호 변경")
    public void test6() throws Exception{
        SignIn();
        Cookie cookie=getCookie();

        ChangePhoneDto changePhoneDto =ChangePhoneDto.builder()
                .phoneNumber("010-1111-2222")
                .build();

        String json=objectMapper.writeValueAsString(changePhoneDto);

        mockMvc.perform(put("/auth/setting/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원 정보 불러오기")
    public void test7() throws Exception{
        SignIn();
        Cookie cookie=getCookie();

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("유저 게시글 목록 불러오기")
    public void test8() throws Exception{
        SignIn();
        Member member=memberRepository.findByEmail("test@gmail.com");

        NewPostDto postDto=NewPostDto.builder()
                .title("test제목")
                .content("test내용")
                .category("FREE")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(postDto)
                .id(member.getId())
                .build();

        List<String> list =new ArrayList<>();
        postService.newPost(newPostServiceDto,list);
        postService.newPost(newPostServiceDto,list);

        mockMvc.perform(MockMvcRequestBuilders.get("/member/{id}",member.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원 가입 실패 ")
    public void test9() throws Exception {
        SignUpDto signupDto= SignUpDto.builder()
                .email("@gmail.com")
                .password("1234")
                .phoneNumber("5678")
                .nickName("test")
                .build();

        String json=objectMapper.writeValueAsString(signupDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(0L,memberRepository.count());

    }

}