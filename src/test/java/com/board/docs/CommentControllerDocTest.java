package com.board.docs;

import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.repository.member.MemberRepository;
import com.board.service.CommentService;
import com.board.service.PostService;
import com.board.domain.comment.Comment;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerDocTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void SignIn(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
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
    }


    public Cookie getCookie(){
        Member member=memberRepository.getMemberbyId(1L);
        String accessToken= member.getSessions().get(0).getAccessToken();
        return new Cookie("SESSION",accessToken);
    }

    public long getId(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }

    public Post newPost(){
        NewPostDto testPost= NewPostDto.builder()
                .category("FREE")
                .title("안녕하세요")
                .content("테스트테스트테스트")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(testPost)
                .id(getId())
                .build();

        return postRepository.getPostById(1L);
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
    public void test1() throws Exception {
        Post post =newPost();
        Cookie cookie =getCookie();
        Long postId= post.getId();

        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글 쓰기")
                .build();

        String json=objectMapper.writeValueAsString(newCommentDto);

        mockMvc.perform(post("/posts/{postId}/comments",postId)
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-create",
                        pathParameters(
                                parameterWithName("postId").description("게시글 번호")
                        )
                       ,
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                        ),responseFields(
                                fieldWithPath("commentListItems[]").description("댓글 목록"),
                                fieldWithPath("commentsNum").description("댓글 갯수")
                        ).andWithPrefix("commentListItems[].",
                                fieldWithPath("id").description("댓글 번호"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("nickName").description("댓글 작성자"),
                                fieldWithPath("createdAt").description("댓글 수정시간")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 수정")
    public void test2() throws Exception {
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

        mockMvc.perform(patch("/posts/{postId}/comments/{commentId}",postId,commentId)
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-edit",
                        pathParameters(
                                parameterWithName("postId").description("게시글 번호"),
                                parameterWithName("commentId").description("댓글 번호")
                        ),
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정 내용")
                        ),responseFields(
                                fieldWithPath("commentListItems[]").description("댓글 목록"),
                                fieldWithPath("commentsNum").description("댓글 갯수")
                        ).andWithPrefix("commentListItems[].",
                                fieldWithPath("id").description("댓글 번호"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("nickName").description("댓글 작성자"),
                                fieldWithPath("createdAt").description("댓글 수정시간")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제")
    public void test3() throws Exception {
        Post post =newPost();
        Cookie cookie =getCookie();
        Long postId=post.getId();
        Long id=getId();
        Long commentId=newComment(id,postId).getId();
        newComment(id,postId);

        mockMvc.perform(delete("/posts/{postId}/comments/{commentId}",postId,commentId)
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-delete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 번호"),
                                parameterWithName("commentId").description("댓글 번호")
                        ),
                        requestCookies(
                                cookieWithName("SESSION").description("사용자 인증 토큰")
                        ),responseFields(
                                fieldWithPath("commentListItems[]").description("댓글 목록"),
                                fieldWithPath("commentsNum").description("댓글 갯수")
                        ).andWithPrefix("commentListItems[].",
                                fieldWithPath("id").description("댓글 번호"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("nickName").description("댓글 작성자"),
                                fieldWithPath("createdAt").description("댓글 수정시간")
                        )
                ));

    }
}
