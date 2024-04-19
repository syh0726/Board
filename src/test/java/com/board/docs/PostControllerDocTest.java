package com.board.docs;

import com.board.crypto.PasswordEncoder;
import com.board.domain.auth.Session;
import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.exception.member.MemberNotFoundException;
import com.board.repository.member.MemberRepository;
import com.board.service.CommentService;
import com.board.service.PostService;
import com.board.domain.category.Category;
import com.board.domain.category.CategoryName;
import com.board.exception.post.PostNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.category.CategoryRepository;
import com.board.repository.comment.CommentRepository;
import com.board.repository.post.PostRepository;
import com.board.repository.post.like.PostLikeRepository;
import com.board.requestDto.comment.NewCommentDto;
import com.board.requestDto.member.SignInDto;
import com.board.requestDto.member.SignUpDto;
import com.board.requestDto.post.EditPostDto;
import com.board.requestDto.post.NewPostDto;
import com.board.requestServiceDto.Post.LikesPostServiceDto;
import com.board.requestServiceDto.Post.NewPostServiceDto;
import com.board.requestServiceDto.comment.NewCommentServiceDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostService postService;

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

    @AfterEach
    public void clean(){
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    public Long newPost(){
        NewPostDto testPost= NewPostDto.builder()
                .category("FREE")
                .title("안녕하세요")
                .content("테스트테스트테스트")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(testPost)
                .id(getId())
                .build();

        GetActivictyResponseDto getActivictyResponseDto=postService.newPost(newPostServiceDto);
        Long id=getActivictyResponseDto.getPostList().get(0).getPostId();


        return id;
    }
    public long getId() {
        Member member = memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }
    public Cookie getCookie(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        Session session=sessionRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
        String accessToken = session.getAccessToken();
        Cookie cookie =new Cookie("SESSION",accessToken);

        return cookie;
    }

public void dummyArticles() {

    Member member = memberRepository.findByEmail("test@gmail.com");
    Category tradeCategory = Category.builder()
            .category("TRADE")
            .build();

    Category freeCategory = Category.builder()
            .category("FREE")
            .build();

    Category informationCategory = Category.builder()
            .category("INFORMATION")
            .build();

    categoryRepository.save(tradeCategory);
    categoryRepository.save(freeCategory);
    categoryRepository.save(informationCategory);


    CategoryName categoryFree=CategoryName.FREE;
    CategoryName categoryTrade=CategoryName.TRADE;
    CategoryName categoryInformation=CategoryName.INFORMATION;


    Category trade = categoryRepository.findByCategoryName(categoryFree);
    Category free = categoryRepository.findByCategoryName(categoryTrade);
    Category infromation= categoryRepository.findByCategoryName(categoryInformation);

    for (int i = 0; i < 21; i++) {
        Post freePost = Post.builder()
                .member(member)
                .category(trade)
                .content("내용" + i)
                .title("제목" + i)
                .build();

        Post tradePost = Post.builder()
                .member(member)
                .category(free)
                .content("내용" + i)
                .title("제목" + i)
                .build();

        Post informationPost = Post.builder()
                .member(member)
                .category(infromation)
                .content("내용" + i)
                .title("제목" + i)
                .build();

        postRepository.save(freePost);
        postRepository.save(tradePost);
        postRepository.save(informationPost);
    }
}

public Post dummyComments() {
    Long postId=newPost();

    for (int i = 1; i < 11; i++) {
        NewCommentDto newCommentDto= NewCommentDto.builder()
                .content("댓글 달기"+i)
                .build();
        NewCommentServiceDto newCommentServiceDto= NewCommentServiceDto.builder()
                .newCommentDto(newCommentDto)
                .id(getId())
                .postId(postId)
                .build();
        commentService.newComment(newCommentServiceDto);
    }

    return postRepository.getPostById(postId);
}




@Test
@DisplayName("게시글 작성")
public void test1() throws Exception {
    Cookie cookie=getCookie();
    NewPostDto newPostDto= NewPostDto.builder()
            .category("FREE")
            .title("안녕하세요")
            .content("테스트테스트테스트")
            .build();


    String json= objectMapper.writeValueAsString(newPostDto);
    //when
    mockMvc.perform(post("/posts/new")
                    .cookie(cookie)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-create",
                    requestCookies(
                            cookieWithName("SESSION").description("사용자 인증 토큰")
                    ),
                    requestFields(
                            fieldWithPath("category").description("게시글 카테고리"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("content").description("게시글 내용")
                    )
            ));
}

@Test
@DisplayName("게시글 수정")
public void test2() throws Exception {
    Long postId=newPost();
    Cookie cookie=getCookie();

    EditPostDto editPostDto=EditPostDto.builder()
            .content("내용 수정")
            .title("제목 수정")
            .category("TRADE")
            .build();

    String json=objectMapper.writeValueAsString(editPostDto);
    //when
    mockMvc.perform(patch("/posts/{postId}",postId)
                    .cookie(cookie)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-edit",
                    pathParameters(
                            parameterWithName("postId").description("게시글 번호")
                    )
                    ,
                    requestCookies(
                            cookieWithName("SESSION").description("사용자 인증 토큰")
                    ),
                    requestFields(
                            fieldWithPath("title").description("게시글 수정 제목"),
                            fieldWithPath("content").description("게시글 수정 내용"),
                            fieldWithPath("category").description("게시글 수정 카테고리")
                    )

            ));
}

@Test
@DisplayName("게시글 삭제")
public void test3() throws Exception {
    Long postId=newPost();
    Cookie cookie=getCookie();
    //when
    mockMvc.perform(delete("/posts/{postId}",postId)
                    .cookie(cookie)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-delete",
                    pathParameters(
                            parameterWithName("postId").description("게시글 번호")
                    ),
                    requestCookies(
                            cookieWithName("SESSION").description("사용자 인증 토큰")
                    )
            ));

}

@Test
@DisplayName("게시글 추천")
public void test4() throws Exception {
    long postId=newPost();

    Cookie cookie=getCookie();

    //when
    mockMvc.perform(post("/posts/{postId}/like",postId)
                    .cookie(cookie)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-like",
                    pathParameters(
                            parameterWithName("postId").description("게시글 번호")
                    ),
                    requestCookies(
                            cookieWithName("SESSION").description("사용자 인증 토큰")
                    ),
                    responseFields(
                            fieldWithPath("likeCnt").description("추천 갯수")
                    )
            ));

}

@Test
@DisplayName("게시글 비추천")
public void test5() throws Exception{
    long postId=newPost();

    Cookie cookie=getCookie();

    //when
    mockMvc.perform(post("/posts/{postId}/dislike",postId)
                    .cookie(cookie)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-dislike",
                    pathParameters(
                            parameterWithName("postId").description("게시글 번호")
                    ),
                    requestCookies(
                            cookieWithName("SESSION").description("사용자 인증 토큰")
                    ),
                    responseFields(
                            fieldWithPath("likeCnt").description("추천 갯수")
                    )
            ));

}

@Test
@DisplayName("게시글 확인")
public void test6() throws Exception{
    long id=getId();
    Long postId=newPost();

    LikesPostServiceDto likesPostServiceDto= LikesPostServiceDto
            .builder()
            .id(id)
            .isLike(true)
            .postId(postId)
            .build();

    postService.likesPost(likesPostServiceDto);

    NewCommentDto newCommentDto=NewCommentDto.builder()
            .content("댓글 작성")
            .build();

    NewCommentServiceDto newCommentServiceDto=NewCommentServiceDto.builder()
            .postId(postId)
            .id(id)
            .newCommentDto(newCommentDto)
            .build();

    commentService.newComment(newCommentServiceDto);

    Post post=postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

    //when
    mockMvc.perform(get("/posts/{postId}",postId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("post-get",
                    pathParameters(
                            parameterWithName("postId").description("게시글 번호")
                    ),
                    responseFields(
                            fieldWithPath("postResponseData.id").description("게시글 번호"),
                            fieldWithPath("postResponseData.title").description("게시글 제목"),
                            fieldWithPath("postResponseData.content").description("게시글 내용"),
                            fieldWithPath("postResponseData.category").description("게시글 카테고리"),
                            fieldWithPath("postResponseData.nickName").description("게시글 작성자"),
                            fieldWithPath("postResponseData.createdAt").description("게시글 작성시간"),
                            fieldWithPath("postResponseData.viewCnt").description("게시글 조회수"),
                            fieldWithPath("postResponseData.likeCnt").description("게시글 좋아요수"),
                            fieldWithPath("postResponseData.authorId").description("게시글 작성자 ID"),
                            fieldWithPath("commentListResponseDto.commentListItems[]").description("댓글 목록"),
                            fieldWithPath("commentListResponseDto.commentsNum").description("댓글 갯수")
                    ).andWithPrefix("commentListResponseDto.commentListItems[].",
                            fieldWithPath("id").description("댓글 번호"),
                            fieldWithPath("content").description("댓글 내용"),
                            fieldWithPath("nickName").description("댓글 작성자"),
                            fieldWithPath("createdAt").description("댓글 작성시간")
                    )
            ));
}

@Test
@DisplayName("게시글 리스트")
public void test7() throws Exception {
    dummyArticles();
    Post post=dummyComments();
    Long postId =post.getId();

    LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
            .isLike(true)
            .postId(postId)
            .id(getId())
            .build();

    postService.likesPost(likesPostServiceDto);

    String category="FREE";
    //when
    mockMvc.perform(get("/{category}",category)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("posts-getbyCategory",
                    pathParameters(
                            parameterWithName("category").optional().description("게시글 카테고리(필수값아님)")
                    ),
                    responseFields(
                            fieldWithPath("postListItems[]").description("게시글 목록"),
                            fieldWithPath("maxPage").description("게시글 최대 페이지 수")
                    ).andWithPrefix("postListItems[].",
                            fieldWithPath("id").description("게시글 번호"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("createdAt").description("게시글 작성시간"),
                            fieldWithPath("nickName").description("게시글 작성자"),
                            fieldWithPath("viewCnt").description("게시글 조회 수"),
                            fieldWithPath("commentNum").description("게시글 댓글 수"),
                            fieldWithPath("likeCnt").description("게시글 좋아요 수")
                    )
            ));

    mockMvc.perform(get("/")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("posts-get",
                    responseFields(
                            fieldWithPath("postListItems[]").description("게시글 목록"),
                            fieldWithPath("maxPage").description("게시글 최대 페이지 수")
                    ).andWithPrefix("postListItems[].",
                            fieldWithPath("id").description("게시글 번호"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("createdAt").description("게시글 작성시간"),
                            fieldWithPath("nickName").description("게시글 작성자"),
                            fieldWithPath("viewCnt").description("게시글 조회 수"),
                            fieldWithPath("commentNum").description("게시글 댓글 수"),
                            fieldWithPath("likeCnt").description("게시글 좋아요 수")
                    )
            ));

    mockMvc.perform(get("/?page=1&condition=TITLE&keyword=1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("posts-search",
                    queryParameters(
                            parameterWithName("page").description("게시글 페이지"),
                            parameterWithName("condition").description("게시글 검색 조건"),
                            parameterWithName("keyword").description("게시글 검색 키워드")
                    ),
                    responseFields(
                            fieldWithPath("postListItems[]").description("게시글 목록"),
                            fieldWithPath("maxPage").description("게시글 최대 페이지 수")
                    ).andWithPrefix("postListItems[].",
                            fieldWithPath("id").description("게시글 번호"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("createdAt").description("게시글 작성시간"),
                            fieldWithPath("nickName").description("게시글 작성자"),
                            fieldWithPath("viewCnt").description("게시글 조회 수"),
                            fieldWithPath("commentNum").description("게시글 댓글 수"),
                            fieldWithPath("likeCnt").description("게시글 좋아요 수")
                    )
            ));
}

}
