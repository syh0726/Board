package com.example.board.service;

import com.example.board.crypto.PasswordEncoder;
import com.example.board.domain.member.Member;
import com.example.board.domain.post.Post;
import com.example.board.exception.member.AlreadyExistEmailException;
import com.example.board.exception.member.InvalidSigninInformationException;
import com.example.board.repository.member.MemberRepository;
import com.example.board.repository.post.PostRepository;
import com.example.board.requestDto.member.FindPasswordDto;
import com.example.board.requestDto.member.SignInDto;
import com.example.board.requestDto.member.SignUpDto;
import com.example.board.requestDto.post.NewPostDto;
import com.example.board.requestServiceDto.Post.NewPostServiceDto;
import com.example.board.requestServiceDto.member.ChangeNicknameServiceDto;
import com.example.board.requestServiceDto.member.ChangePasswordServiceDto;
import com.example.board.requestServiceDto.member.ChangePhoneServiceDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        return postRepository.getPostById(1L);
    }

    @Test
    @DisplayName("회원가입 성공 비밀번호 암호화 하기 때문에 그냥 비밀번호를 넣으면 같지않음!!")
    public void test1(){
        //given
        SignUpDto signUpDto= SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("010-1234-5678")
                .password("1234")
                .nickName("test")
                .build();

        //when
        memberService.signup(signUpDto);

        //then
        Assertions.assertEquals(1,memberRepository.count());

        Member member=memberRepository.findById(1L)
                .orElseThrow(()->new AlreadyExistEmailException());

        Assertions.assertEquals("test",member.getNickName());
        Assertions.assertNotEquals("1234",member.getPassword());
    }

    @Test
    @DisplayName("회원가입 성공 비밀번호 암호화 해독")
    public void test2(){
        //given
        SignUpDto signUpDto= SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("010-1234-5678")
                .password("1234")
                .nickName("test")
                .build();

        //when
        Member member=memberService.signup(signUpDto);

        //then
        Assertions.assertEquals(1,memberRepository.count());

        Member loginMember=memberRepository.findById(member.getId())
                .orElseThrow(()->new AlreadyExistEmailException());

        Assertions.assertEquals("test",member.getNickName());
        Assertions.assertTrue(passwordEncoder.matches("1234",loginMember.getPassword()));
    }

    @Test
    @DisplayName("로그인 성공")
    public void test3(){
        //given

        Member member=Member.builder()
                .nickName("test")
                .password(passwordEncoder.encrypt("1234"))
                .phoneNumber("010-1234-5678")
                .email("test@gmail.com")
                .build();

        memberRepository.save(member);

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
        Member member=Member.builder()
                .nickName("test")
                .password(passwordEncoder.encrypt("1234"))
                .phoneNumber("010-1234-5678")
                .email("test@gmail.com")
                .build();

        memberRepository.save(member);

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
        Member member=testSignIn();

        ChangePasswordServiceDto changePasswordServiceDto= ChangePasswordServiceDto.builder()
                .id(member.getId())
                .originPassword("test1234")
                .newPassWord("test0000")
                .build();

        //when
        memberService.changePassword(changePasswordServiceDto);

        //then
        Member member2=memberRepository.findByEmail("test@gmail.com");
        if(passwordEncoder.matches("test0000",member2.getPassword())){
            System.out.println("변경 성공");
        }
    }

    @Test
    @DisplayName("닉네임 변경")
    public void test6(){
        //given
        Member member=testSignIn();

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
        Member member=testSignIn();

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

        memberService.getActivity(id);
    }

}