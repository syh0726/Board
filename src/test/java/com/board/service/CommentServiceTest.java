package com.board.service;


import com.board.config.data.MemberSession;
import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.repository.member.MemberRepository;
import com.board.domain.comment.Comment;
import com.board.exception.comment.CommentNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.category.CategoryRepository;
import com.board.repository.comment.CommentRepository;
import com.board.repository.post.PostRepository;
import com.board.requestDto.comment.EditCommentDto;
import com.board.requestDto.comment.NewCommentDto;
import com.board.requestDto.member.SignInDto;
import com.board.requestDto.member.SignUpDto;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.requestServiceDto.comment.DeleteCommentServiceDto;
import com.board.requestServiceDto.comment.EditCommentServiceDto;
import com.board.requestServiceDto.comment.NewCommentServiceDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MemberService memberService;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    MemberSession memberSession=new MemberSession(1L,"aa");


    @BeforeEach
    public void testSignIn(){
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
    @AfterEach
    public void clean(){
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }


    public Long getId(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }

    public Long getId2(){
        Member member=memberRepository.findByEmail("test2@gmail.com");
        return member.getId();
    }

    public void testSignIn2(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test2@gmail.com")
                .phoneNumber("01012341254")
                .nickName("test2")
                .password("1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("test2@gmail.com")
                .password("1234")
                .build();

        memberService.signin(signInDto);

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

    @Test
    @DisplayName("댓글 달기 서비스")
    public void test1(){
        Long Id=getId();
        Post post=newPost();

        NewCommentDto newCommentDto=NewCommentDto.builder()
                        .content("댓글달기1")
                        .build();

        NewCommentServiceDto newCommentServiceDto= NewCommentServiceDto.builder()
                .newCommentDto(newCommentDto)
                .id(memberSession.id)
                .postId(post.getId())
                .build();

        Comment comment=commentService.newComment(newCommentServiceDto);

        Assertions.assertEquals(1,commentRepository.count());
        Assertions.assertEquals("댓글달기1",comment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 서비스")
    public void test2(){
        Long id=getId();
        Post post=newPost();

        NewCommentDto newCommentDto=NewCommentDto.builder()
                .content("댓글달기1")
                .build();

        NewCommentServiceDto newCommentServiceDto= NewCommentServiceDto.builder()
                .newCommentDto(newCommentDto)
                .id(id)
                .postId(post.getId())
                .build();

        Comment comment=commentService.newComment(newCommentServiceDto);

        EditCommentDto editCommentDto=EditCommentDto.builder()
                .content("댓글 변경 !!")
                .build();

        EditCommentServiceDto editCommentServiceDto= EditCommentServiceDto.builder()
                .commentId(comment.getId())
                .editCommentDto(editCommentDto)
                .id(id)
                .build();

        commentService.editComment(editCommentServiceDto);

        Comment editComment =commentRepository.findById(comment.getId()).orElseThrow(CommentNotFoundException::new);

        Assertions.assertEquals(1,commentRepository.count());
        Assertions.assertEquals("댓글 변경 !!",editComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 서비스")
    public void test3(){
        Long id=getId();
        Post post=newPost();
        testSignIn2();
        Long id2=getId2();

        NewCommentDto newCommentDto=NewCommentDto.builder()
                .content("댓글달기1")
                .build();

        NewCommentServiceDto newCommentServiceDto= NewCommentServiceDto.builder()
                .newCommentDto(newCommentDto)
                .id(id)
                .postId(post.getId())
                .build();

        Comment comment=commentService.newComment(newCommentServiceDto);

        DeleteCommentServiceDto deleteCommentServiceDto= DeleteCommentServiceDto.builder()
                .commentId(comment.getId())
                .id(id)
                .build();

        commentService.deleteComment(deleteCommentServiceDto);


        Assertions.assertEquals(0,commentRepository.count());
    }

}
