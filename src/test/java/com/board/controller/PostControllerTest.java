package com.board.controller;

import com.board.domain.member.Role;
import com.board.domain.post.Post;
import com.board.responseDto.member.GetActivictyResponseDto;
import com.board.service.PostService;
import com.board.crypto.PasswordEncoder;
import com.board.domain.auth.Session;
import com.board.domain.category.Category;
import com.board.domain.category.CategoryName;
import com.board.domain.comment.Comment;
import com.board.domain.member.Member;
import com.board.exception.member.MemberNotFoundException;
import com.board.exception.post.PostNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.category.CategoryRepository;
import com.board.repository.comment.CommentRepository;
import com.board.repository.member.MemberRepository;
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
import com.board.responseDto.comment.CommentListItem;
import com.board.service.CommentService;
import com.board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
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

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {
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

    public NewPostDto newPostDto(){
        NewPostDto testPost= NewPostDto.builder()
                .category("FREE")
                .title("안녕하세요")
                .content("테스트테스트테스트")
                .build();
        return testPost;
    }
    public Long getId(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        return member.getId();
    }
    public Long getAdminId(){
        Member member=memberRepository.findByEmail("admin@gmail.com");
        return member.getId();
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
    public void adminSignIn(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("admin@gmail.com")
                .phoneNumber("01013341234")
                .nickName("admin")
                .password("1234")
                .build();

        memberService.signup(signUpDto);

        SignInDto signInDto= SignInDto.builder()
                .email("admin@gmail.com")
                .password("1234")
                .build();

        memberService.signin(signInDto);
    }
    public Post adminPost(){
        NewPostDto testPost= NewPostDto.builder()
                .category("FREE")
                .title("안녕하세요 관리자입니다.")
                .content("테스트테스트테스트")
                .build();

        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .newPostDto(testPost)
                .id(getAdminId())
                .build();

        return postRepository.getPostById(1L);
    }

    public Member getAdmin(){
        Member member=memberRepository.findByEmail("admin@gmail.com");
        member.setRole(Role.ADMIN);
        memberRepository.save(member);

        return member;
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
        Post post=postRepository.getPostById(newPost());

        for (int i = 1; i < 11; i++) {
            NewCommentDto newCommentDto= NewCommentDto.builder()
                    .content("댓글 달기"+i)
                    .build();
            NewCommentServiceDto newCommentServiceDto= NewCommentServiceDto.builder()
                    .newCommentDto(newCommentDto)
                    .id(getId())
                    .postId(post.getId())
                    .build();
            commentService.newComment(newCommentServiceDto);
        }

        return post;
    }

    public Cookie getCookie(){
        Member member=memberRepository.findByEmail("test@gmail.com");
        Session session=sessionRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
        System.out.println(session.getAccessToken());
        String accessToken = session.getAccessToken();
        Cookie cookie =new Cookie("SESSION",accessToken);

        return cookie;
    }

    public void testSignIn2(){
        SignUpDto signUpDto=SignUpDto.builder()
                .email("test2@gmail.com")
                .phoneNumber("0101231234")
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

    public Cookie getAdminCookie(){
        Member member=memberRepository.findByEmail("admin@gmail.com");
        Session session=sessionRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
        System.out.println(session.getAccessToken());
        String accessToken = session.getAccessToken();
        Cookie cookie =new Cookie("SESSION",accessToken);

        return cookie;
    }



    @Test
    @DisplayName("글 작성 접근 ")
    public void test() throws Exception {
        //given
        Cookie cookie=getCookie();
        NewPostDto newPostDto =newPostDto();


        String json= objectMapper.writeValueAsString(newPostDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/new")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
        //then
    }

    @Test
    @DisplayName("글 작성 실패 401 코드")
    public void test2() throws Exception {
        //given
        Cookie cookie=new Cookie("SESSION","124");
        NewPostDto newPostDto =newPostDto();


        String json= objectMapper.writeValueAsString(newPostDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/new")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
        //then
    }

    @Test
    @DisplayName("글 작성 실패 400code ")
    public void test3() throws Exception {
        //given
        Cookie cookie=new Cookie("SESSION","1234");
        NewPostDto newPostDto = NewPostDto.builder()
                .category("FREE")
                .title("")
                .content("")
                .build();

        String json= objectMapper.writeValueAsString(newPostDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/new")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        //then
    }

    @Test
    @DisplayName("글 삭제 ")
    public void test4() throws Exception {
        //given
        Long postId=newPost();
        Cookie cookie=getCookie();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Assertions.assertEquals(0,postRepository.count());

    }

    @Test
    @DisplayName("글 삭제 관리자 ID ")
    public void test5() throws Exception {
        //given
        Long postId=newPost();
        adminSignIn();
        Member member=getAdmin();


        Cookie cookie=getAdminCookie();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Assertions.assertEquals(0,postRepository.count());

    }

    @Test
    @DisplayName("글 수정 관리자 ID ")
    public void test6() throws Exception {
        //given
        Long postId=newPost();
        adminSignIn();

        Member member=getAdmin();
        Cookie cookie=getAdminCookie();

        EditPostDto editPostDto=EditPostDto.builder()
                .content("내용 수정")
                .title("제목 수정")
                .category("TRADE")
                .build();

        String json=objectMapper.writeValueAsString(editPostDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Post post=postRepository.getPostById(postId);
        Assertions.assertEquals("내용 수정",post.getContent());
        Assertions.assertEquals("제목 수정",post.getTitle());
        Assertions.assertEquals("거래",post.getCategory().getCategoryName().getCategory());

    }

    @Test
    @DisplayName("글 1개 확인 ")
    public void test7() throws Exception {
        //given
        Long postId= newPost();

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}",postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcResultHandlers.print());
        //then

    }

    @Test
    @DisplayName("글 추천 ")
    public void test8() throws Exception {
        //given
        long postId=newPost();

        Cookie cookie=getCookie();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/like",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        int likes= postRepository.getLikesNum(postId);
        Assertions.assertEquals(1L,likes);
        //then
    }

    @Test
    @DisplayName("글 비추천 ")
    public void test9() throws Exception {
        //given
        long postId=newPost();

        Cookie cookie=getCookie();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/dislike",postId)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        int likes= postRepository.getLikesNum(postId);
        Assertions.assertEquals(-1L,likes);
        //then
    }

    @Test
    @DisplayName("글 작성 리스트 불러오기 ")
    public void test10() throws Exception {
        //given
        dummyArticles();
        Post post=dummyComments();
        Long postId =post.getId();
        adminSignIn();
        adminPost();

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .isLike(true)
                .postId(postId)
                .id(getId())
                .build();

        postService.likesPost(likesPostServiceDto);

        String category="INFORMATION";
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/?page=1&condition=NICKNAME&keyword=admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
    }


  /*  @Test
    @DisplayName("데이터 변환 속도 비교")
    public void CompareData() throws Exception {
        //given
        Post post=dummyComments();
        Post post2=dummyComments2();
        List<Comment> comments=commentRepository.findByPost(post);
        List<Comment> comments2=commentRepository.findByPost(post2);
        List<CommentListItem> commentListItems=new ArrayList<>();
        List<CommentListItem> commentListItemsForTest=new ArrayList<>();
        //when

        for(Comment comment : comments){
            commentListItemsForTest.add(CommentListItem.builder()
                    .comment(comment)
                    .build());
        }

        List<CommentListItem> commentListItemsTest=comments.stream()
                .map(CommentListItem::new)
                .toList();


        long startTime2 = System.nanoTime();

        long startTime = System.nanoTime();
        for(Comment comment : comments){
            commentListItems.add(CommentListItem.builder()
                    .comment(comment)
                    .build());
        }
        long estimatedTime = System.nanoTime() - startTime;

        List<CommentListItem> commentListItems2=comments.stream()
                .map(CommentListItem::new)
                .toList();

        long estimatedTime2 = System.nanoTime() - startTime2;//10억분의 1초 ==나노초


        System.out.println("for 문 걸린시간: "+(double)estimatedTime/1000000000+" 초");
        System.out.println("Stream 걸린시간: "+(double)estimatedTime2/1000000000+" 초");
        System.out.println("Stream이 "+(double)estimatedTime/estimatedTime2+"배 빠릅니다.");

        //then
    }

    @Test
    @DisplayName("스트림 데이터 변환 ")
    public void testData() throws Exception {
        //given
        Post post=dummyComments();
        List<Comment> comments=commentRepository.findByPost(post);
        //when
        Member member=memberRepository.findByEmail("test@gmail.com");
        long startTime = System.nanoTime();

        List<CommentListItem> commentListItems2=comments.stream()
                .map(CommentListItem::new)
                .toList();

        long estimatedTime = System.nanoTime() - startTime;//10억분의 1초 ==나노초
        System.out.println("Stream 걸린시간: "+(double)estimatedTime/1000000000+" 초");

        //then
    }
*/

}
