package com.board.docs;

import com.board.crypto.PasswordEncoder;
import com.board.domain.auth.Session;
import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.exception.member.MemberNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.member.MemberRepository;
import com.board.requestDto.member.*;
import com.board.service.PostService;
import com.board.repository.post.PostRepository;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.responseDto.member.GetActivictyResponseDto;
import com.board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerDocTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    SessionRepository sessionRepository;

    @AfterEach
    public void clean(){
        memberRepository.deleteAll();
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


    public Long newPost(){
        Long id=getId();

        NewPostDto postDto=NewPostDto.builder()
                .title("test제목")
                .content("test내용")
                .category("FREE")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(postDto)
                .id(id)
                .build();


        postService.newPost(newPostServiceDto);

        GetActivictyResponseDto getActivictyResponseDto=postService.newPost(newPostServiceDto);
        Long postId=getActivictyResponseDto.getPostList().get(0).getPostId();

        return postId;
    }

    public long getId(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }


    public Cookie getCookie(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        Session session=sessionRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
        String accessToken = session.getAccessToken();
        Cookie cookie =new Cookie("SESSION",accessToken);

        return cookie;
    }



    @Test
    @DisplayName("회원 가입")
    public void test1() throws Exception {
        SignUpDto signupDto= SignUpDto.builder()
                .email("test@gmail.com")
                .password("test1234")
                .phoneNumber("01012345678")
                .nickName("test")
                .build();

        String json=objectMapper.writeValueAsString(signupDto);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-create",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호"),
                                fieldWithPath("phoneNumber").description("사용자 휴대폰 번호"),
                                fieldWithPath("nickName").description("사용자 닉네임")
                        )
                ));

    }

    @Test
    @DisplayName("로그인")
    public void test2() throws Exception{
        String encryptedpass= passwordEncoder.encrypt("1234");

        Member member=Member.builder()
                .email("test@gmail.com")
                .phoneNumber("01012341234")
                .nickName("test")
                .password(encryptedpass)
                .build();

        memberRepository.save(member);

        SignInDto signInDto= SignInDto.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        String json=objectMapper.writeValueAsString(signInDto);

        mockMvc.perform(post("/auth/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-signin",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 ID(키값)"),
                                fieldWithPath("nickName").description("사용자 닉네임"),
                                fieldWithPath("role").description("사용자 등급")
                        )
                        ,
                        responseCookies(
                                cookieWithName("SESSION").description("사용자 토큰값")
                        )
                ));
    }

    @Test
    @DisplayName("로그 아웃")
    public void test3() throws Exception {
        SignIn();
        Cookie cookie=getCookie();

        mockMvc.perform(post("/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-logout",
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        responseCookies(
                                cookieWithName("SESSION").description("사용자 토큰을 만료시간0으로 응답")
                        )
                ));

    }

    @Test
    @DisplayName("비밀번호 변경")
    public void test4() throws Exception {
        SignIn();
        Cookie cookie=getCookie();

        ChangePasswordDto changePasswordDto= ChangePasswordDto.builder()
                .originPassword("test1234")
                .newPassword("test0000")
                .build();

        String json=objectMapper.writeValueAsString(changePasswordDto);

        mockMvc.perform(put("/auth/setting/password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-setPassword",
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("originPassword").description("원래 비밀번호"),
                                fieldWithPath("newPassword").description("변경할 비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("휴대폰 번호 변경")
    public void test5() throws Exception {
        SignIn();
        Cookie cookie=getCookie();

        ChangePhoneDto changePhoneDto =ChangePhoneDto.builder()
                .phoneNumber("010-1111-2222")
                .build();

        String json=objectMapper.writeValueAsString(changePhoneDto);

        mockMvc.perform(put("/auth/setting/phone")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-setPhoneNumber",
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("phoneNumber").description("변경할 휴대폰번호")
                        )
                ));

    }

    @Test
    @DisplayName("닉네임 변경")
    public void test6() throws Exception {
        SignIn();
        Cookie cookie=getCookie();

        ChangeNicknameDto changeNicknameDto=ChangeNicknameDto.builder()
                .nickName("끼돌이")
                .build();

        String json=objectMapper.writeValueAsString(changeNicknameDto);

        mockMvc.perform(put("/auth/setting/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-setNickName",
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickName").description("변경할 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("계정정보 불러오기")
    public void test7() throws Exception {
        SignIn();
        Cookie cookie=getCookie();

        mockMvc.perform(get("/auth/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-getProfile",
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("phoneNumber").description("사용자 전화번호"),
                                fieldWithPath("nickName").description("사용자 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("특정 유저 게시글 불러오기")
    public void test8() throws Exception{
        SignIn();

        long memberId=memberRepository.findByEmail("test@gmail.com").getId();
        newPost();
        newPost();
        newPost();


        mockMvc.perform(RestDocumentationRequestBuilders.get("/member/{id}",memberId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-getActivity",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").description("유저의 ID값(key)")
                        ),
                        responseFields(
                                fieldWithPath("nickName").description("유저 닉네임"),
                                fieldWithPath("postList[]").description("유저 활동목록")
                        ).andWithPrefix("postList[].",
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("createdAt").description("게시글 작성일자")
                        )
                ));
    }




}
