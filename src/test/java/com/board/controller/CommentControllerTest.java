package com.board.controller;

import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.repository.member.MemberRepository;
import com.board.service.CommentService;
import com.board.service.PostService;
import com.board.domain.auth.Session;
import com.board.domain.comment.Comment;
import com.board.exception.comment.CommentNotFoundException;
import com.board.exception.member.MemberNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.comment.CommentRepository;
import com.board.repository.post.PostRepository;
import com.board.requestDto.comment.EditCommentDto;
import com.board.requestDto.comment.NewCommentDto;
import com.board.requestDto.member.SignInDto;
import com.board.requestDto.member.SignUpDto;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.requestServiceDto.comment.NewCommentServiceDto;
import com.board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /*@BeforeEach
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

    @AfterEach
    public void clean(){
        memberRepository.deleteAll();
        sessionRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }*/

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

    public Cookie getCookie(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        Session session=sessionRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
        String accessToken = session.getAccessToken();
        Cookie cookie =new Cookie("SESSION",accessToken);

        return cookie;
    }

    public Comment newComment(Long id, Long postId){
        NewCommentDto newCommentDto=NewCommentDto.builder()
                .content("댓글 작성")
                .build();

        NewCommentServiceDto newCommentServiceDto=NewCommentServiceDto.builder()
                .id(id)
                .postId(postId)
                .newCommentDto(newCommentDto)
                .build();

        return commentService.newComment(newCommentServiceDto);
    }


    @Test
    @DisplayName("댓글 작성")
    public void test1() throws Exception{
        Post post =newPost();
        Cookie cookie =getCookie();
        Long postId= post.getId();

        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글 쓰기")
                .build();

        String json=objectMapper.writeValueAsString(newCommentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comments",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


        Assertions.assertEquals(1L,commentRepository.count());
    }

    @Test
    @DisplayName("댓글 수정")
    public void test2() throws Exception{
        Post post =newPost();
        Long id=getId();
        Cookie cookie =getCookie();
        Long postId= post.getId();

        Comment comment=newComment(id,postId);

        Long commentId=comment.getId();

        EditCommentDto editCommentDto = EditCommentDto.builder()
                .content("댓글 수정")
                .build();

        String json= objectMapper.writeValueAsString(editCommentDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}/comments/{commentId}",postId,commentId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Comment editComment=commentRepository.findById(comment.getId()).orElseThrow(CommentNotFoundException::new);

        Assertions.assertEquals("댓글 수정",editComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    public void test3() throws Exception{
        Post post =newPost();
        Cookie cookie =getCookie();
        Long postId=post.getId();
        Long id=getId();
        Long commentId=newComment(id,postId).getId();


        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}/comments/{commentId}",postId,commentId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


        Assertions.assertEquals(0,commentRepository.count());
    }

}
