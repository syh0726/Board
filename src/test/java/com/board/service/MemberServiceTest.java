package com.board.service;

import com.board.crypto.PasswordEncoder;
import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.repository.member.MemberRepository;
import com.board.exception.member.AlreadyExistEmailException;
import com.board.exception.member.InvalidSigninInformationException;
import com.board.repository.post.PostRepository;
import com.board.requestDto.member.FindPasswordDto;
import com.board.requestDto.member.SignInDto;
import com.board.requestDto.member.SignUpDto;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.requestServiceDto.member.ChangeNicknameServiceDto;
import com.board.requestServiceDto.member.ChangePasswordServiceDto;
import com.board.requestServiceDto.member.ChangePhoneServiceDto;
import com.board.responseDto.Post.ActivityPostList;
import com.board.responseDto.member.GetActivictyResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    static Member member;
    @AfterEach
    void clean(){memberRepository.deleteAll();}

    @BeforeEach
    public void SignIn(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("01012341234")
                .nickName("test")
                .password("1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        memberService.signin(signInDto);

        member=memberRepository.findByEmail("test@gmail.com");
    }


    public Member testSignIn(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("01012341234")
                .nickName("test")
                .password("test1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("test@gmail.com")
                .password("test1234")
                .build();

        memberService.signin(signInDto);

        return memberRepository.findByEmail("test@gmail.com");
    }

    public Long getId(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }
    public Post newPost(){
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

        return postRepository.getPostById(1L);
    }
    public Post newPost2(){
        Long id=getId();

        NewPostDto postDto=NewPostDto.builder()
                .title("test제목2")
                .content("test내용2")
                .category("FREE")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(postDto)
                .id(id)
                .build();


        postService.newPost(newPostServiceDto);


        return postRepository.getPostById(1L);
    }

    @Test
    @DisplayName("회원가입 성공 비밀번호 암호화 하기 때문에 그냥 비밀번호를 넣으면 같지않음!!")
    public void test1(){
        //given

        //then
        Assertions.assertEquals(1,memberRepository.count());
        Assertions.assertEquals("test",member.getNickName());
        Assertions.assertNotEquals("1234",member.getPassword());
    }

    @Test
    @DisplayName("회원가입 성공 비밀번호 암호화 해독")
    public void test2(){


        Assertions.assertEquals(1,memberRepository.count());


        Assertions.assertEquals("test",member.getNickName());
        Assertions.assertTrue(passwordEncoder.matches("1234",member.getPassword()));
    }

    @Test
    @DisplayName("로그인 성공")
    public void test3(){
        //given

        SignInDto signInDto = SignInDto.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        //when
        memberService.signin(signInDto);
        //then
    }

    @Test
    @DisplayName("로그인 성공 실패기 때문에 exception 발생")
    public void test4(){
        //given

        SignInDto signInDto = SignInDto.builder()
                .email("test@gmail.com")
                .password("12234")
                .build();

        //when
        Assertions.assertThrows(InvalidSigninInformationException.class,()->{memberService.signin(signInDto);
        });
        //then
    }

    @Test
    @DisplayName("비밀번호 변경")
    public void test5(){
        //given

        ChangePasswordServiceDto changePasswordServiceDto= ChangePasswordServiceDto.builder()
                .id(member.getId())
                .originPassword("1234")
                .newPassWord("test0000")
                .build();

        //when
        memberService.changePassword(changePasswordServiceDto);

        //then
        Member member2=memberRepository.findByEmail("test@gmail.com");
        Assertions.assertTrue(passwordEncoder.matches("test0000",member2.getPassword()));

    }

    @Test
    @DisplayName("닉네임 변경")
    public void test6(){
        //given

        ChangeNicknameServiceDto changeNicknameServiceDto = ChangeNicknameServiceDto.builder()
                .id(member.getId())
                .nickName("끼돌이")
                .build();

        //when
        memberService.changeNickName(changeNicknameServiceDto);
        //then
        Member member2=memberRepository.findByEmail("test@gmail.com");
        Assertions.assertEquals(member2.getNickName(),"끼돌이");
    }

    @Test
    @DisplayName("휴대폰번호 변경")
    public void test7(){
        //given

        ChangePhoneServiceDto changePhoneServiceDto=ChangePhoneServiceDto.builder()
                .id(member.getId())
                .phoneNumber("010-1577-1577")
                .build();

        //when
        memberService.changePhoneNumber(changePhoneServiceDto);

        //then
        Member member2=memberRepository.findByEmail("test@gmail.com");
        Assertions.assertEquals("010-1577-1577",member2.getPhoneNumber());

    }

    @Test
    @DisplayName("패스워드 찾기")
    public void test10(){
        //given

        FindPasswordDto findPasswordDto=FindPasswordDto.builder()
                .email("test@gmail.com")
                .phoneNumber("010-1234-5678")
                .build();

        memberService.findpassword(findPasswordDto);
        //when

        //then
    }

    @Test
    @DisplayName("유저 게시글 리스트 불러오기")
    public void test12(){
        Post post=newPost();
        Post post2=newPost2();

        Long id=getId();

        GetActivictyResponseDto activityPostList=memberService.getActivity(id);
        Assertions.assertEquals("test제목2",activityPostList.getPostList().get(0).getTitle());
        Assertions.assertEquals("test제목",activityPostList.getPostList().get(1).getTitle());
    }

}