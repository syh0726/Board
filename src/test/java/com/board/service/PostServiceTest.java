package com.board.service;

import com.board.domain.member.Member;
import com.board.domain.member.Role;
import com.board.domain.post.Post;
import com.board.repository.member.MemberRepository;
import com.board.requestServiceDto.Post.*;
import com.board.responseDto.Post.PostsResponseDto;
import com.board.domain.category.Category;
import com.board.domain.category.CategoryName;
import com.board.exception.auth.AuthInvalidMemberException;
import com.board.exception.member.MemberNotFoundException;
import com.board.exception.post.PostNotFoundException;
import com.board.repository.auth.SessionRepository;
import com.board.repository.category.CategoryRepository;
import com.board.repository.post.PostRepository;
import com.board.repository.post.like.PostLikeRepository;
import com.board.requestDto.member.SignInDto;
import com.board.requestDto.member.SignUpDto;
import com.board.requestDto.post.EditPostDto;
import com.board.requestDto.post.GetPostsDto;
import com.board.requestDto.post.NewPostDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
public class PostServiceTest {

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

    @Autowired
    PostLikeRepository postLikeRepository;

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



    @AfterEach
    public void clean(){
        postLikeRepository.deleteAll();
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    public void dummyArticles(){
        Member member=memberRepository.findById(getId()).orElseThrow(MemberNotFoundException::new);
        Category test=Category.builder()
                .category("FREE")
                .build();
        categoryRepository.save(test);
        CategoryName categoryName=CategoryName.FREE;
        Category category=categoryRepository.findByCategoryName(categoryName);

        for(int i=0;i<20;i++) {
            Post post = Post.builder()
                    .member(member)
                    .category(category)
                    .content("내용" + i)
                    .title("제목" + i)
                    .build();

            postRepository.save(post);
        }

    }
    public NewPostDto newArticle(){
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
    public Long getId2(){
        Member member=memberRepository.findByEmail("test2@gmail.com");
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

        return postRepository.getPostById(2L);
    }


    @Test
    @DisplayName("글 작성 서비스")
    public void test1(){
        //given
        NewPostDto newPostDto =newArticle();
        Long id =getId();
        NewPostServiceDto newPostServiceDto=NewPostServiceDto.builder()
                .id(id)
                .newPostDto(newPostDto)
                .build();
        //when
        Post post =postRepository.getPostById(1L);

        //then
        Post resultPost=postRepository.getPostById(post.getId());

        Assertions.assertEquals(1L,postRepository.count());
        Assertions.assertEquals("테스트테스트테스트",post.getContent());
        Assertions.assertEquals("자유",resultPost.getCategory().getCategoryName().getCategory());
    }

    @Test
    @DisplayName("글 리스트 서비스")
    public void test2(){
        //given
        dummyArticles();

        GetPostsDto getPostsDto=GetPostsDto.builder()
                .page(1)
                .condition("TITLE")
                .keyword("1")
                .build();

        GetPostsServiceDto getPostsServiceDto= GetPostsServiceDto.builder()
                .getPostsDto(getPostsDto)
                .category("FREE")
                .build();

        //when
        PostsResponseDto postsResponseDto =postService.getList(getPostsServiceDto);
        //then
        Assertions.assertEquals("제목18",postsResponseDto.getPostListItems().get(1).getTitle());
        Assertions.assertEquals(20L,postRepository.count());;
    }

    @Test
    @DisplayName("글 수정 서비스")
    public void test3(){
        //given
        Post post=newPost();
        Long id=getId();

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();


        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(post.getId())
                .id(id)
                .build();
        //when
        postService.editPost(editPostServiceDto);
        //then
        Post resultPost=postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
    }


    @Test
    @DisplayName("글 좋아요 실패 중복 추천!")
    public void test4(){
        //given
        Post post=newPost();
        Long id=getId();

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id)
                .isLike(true)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        postService.likesPost(likesPostServiceDto);
        //then
        Assertions.assertEquals(2L,postLikeRepository.count());
    }

    @Test
    @DisplayName("글 좋아요 다른글 성공!!")
    public void test5(){
        //given
        Post post=newPost();
        Post post2=newPost2();
        Long id=getId();


        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id)
                .isLike(true)
                .build();
        LikesPostServiceDto likesPostServiceDto2=LikesPostServiceDto.builder()
                .postId(post2.getId())
                .isLike(true)
                .id(id)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        postService.likesPost(likesPostServiceDto2);
        //then
        int likesNum=postRepository.getLikesNum(post.getId());
        Assertions.assertEquals(1,likesNum);
    }


    @Test
    @DisplayName("글 좋아요 다른 아이디 서비스 성공")
    public void test6(){
        //given
        Post post=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id)
                .isLike(true)
                .build();

        LikesPostServiceDto likesPostServiceDto2=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id2)
                .isLike(false)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        postService.likesPost(likesPostServiceDto2);
        //then
        Assertions.assertEquals(2L,postLikeRepository.count());
    }


    @Test
    @DisplayName("글 수정 서비스 :관리자 ")
    public void test7(){
        //given
        Post post=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();
        Post post2=newPost2();

        Member member=memberRepository.findById(id2).orElseThrow(MemberNotFoundException::new);
        member.setRole(Role.ADMIN);
        memberRepository.save(member);

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();

        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(post.getId())
                .id(id2)
                .build();
        //when
        postService.editPost(editPostServiceDto);
        //then
        Post resultPost=postRepository.getPostById(post.getId());
        Post resultPost2=postRepository.getPostById(post2.getId());
        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
        Assertions.assertEquals("거래",resultPost.getCategory().getCategoryName().getCategory());
        Assertions.assertEquals("자유",resultPost2.getCategory().getCategoryName().getCategory());
    }

    @Test
    @DisplayName("글 삭제 서비스  ")
    public void test8(){
        //given
        Post post=newPost();
        Long id=getId();

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(post.getId())
                .id(id)
                .build();

        //when
        postService.deletePost(deletePostServiceDto);
        //then
        Assertions.assertEquals(0,postRepository.count());
    }
    @Test
    @DisplayName("글 삭제 서비스 권한없음 exception  ")
    public void test9(){
        //given
        Post post=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(post.getId())
                .id(id2)
                .build();

        //when
        Assertions.assertThrows(AuthInvalidMemberException.class,()->postService.deletePost(deletePostServiceDto));
        //then
    }

    @Test
    @DisplayName("글 삭제 서비스 관리자 성공~  ")
    public void test10(){
        //given
        Post post=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        Member member=memberRepository.findById(id2).orElseThrow(MemberNotFoundException::new);
        member.setRole(Role.ADMIN);

        memberRepository.save(member);

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(post.getId())
                .id(id2)
                .build();

        postService.deletePost(deletePostServiceDto);

        //when
        Assertions.assertEquals(0,postRepository.count());
        //then
    }

    @Test
    @DisplayName("글 1개 조회수 늘어나는지 확인")
    public void test11(){
        //given
        Post post=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();


        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id)
                .isLike(true)
                .build();

        LikesPostServiceDto likesPostServiceDto2=LikesPostServiceDto.builder()
                .postId(post.getId())
                .id(id2)
                .isLike(false)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        postService.likesPost(likesPostServiceDto2);
        postService.getPost(post.getId());
        postService.getPost(post.getId());
        //then
        Assertions.assertEquals(2,postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new).getViewCnt());
    }




    @Test
    @DisplayName("간단테스트")
    public void test111() {
        Post post=newPost();
        Post post2=newPost();
        CategoryName categoryName = CategoryName.valueOf("FREE");
        Category category=categoryRepository.findByCategoryName(categoryName);
        System.out.println("2 "+category.toString());
        System.out.println("3 "+categoryRepository.count());

    }
}
