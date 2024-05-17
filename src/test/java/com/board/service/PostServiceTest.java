package com.board.service;

import com.board.domain.image.PostImage;
import com.board.domain.member.Member;
import com.board.domain.member.Role;
import com.board.domain.post.Post;
import com.board.exception.post.like.PostAlreadyLikesException;
import com.board.repository.image.PostImageRepository;
import com.board.repository.member.MemberRepository;
import com.board.requestServiceDto.Post.*;
import com.board.responseDto.Post.PostResponseDto;
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
import com.board.responseDto.member.GetActivictyResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
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

    @Autowired
    S3service s3service;

    @Autowired
    PostImageRepository postImageRepository;

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
        postLikeRepository.deleteAll();
        memberRepository.deleteAll();
        postRepository.deleteAll();
        postImageRepository.deleteAll();
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

        List<String> list=new ArrayList<>();
        GetActivictyResponseDto getActivictyResponseDto=postService.newPost(newPostServiceDto,list);
        Long id=getActivictyResponseDto.getPostList().get(0).getPostId();

        return id;
    }
    public Long newImgPost() throws IOException {
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

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("image", "불광천.jpg", "image/jpg",
                new FileInputStream(new File("C:/test/불광천.jpg"))));

        List<String> list=s3service.saveFile(multipartFiles,"raw");

        GetActivictyResponseDto getActivictyResponseDto=postService.newPost(newPostServiceDto,list);
        Long postId=getActivictyResponseDto.getPostList().get(0).getPostId();

        return postId;
    }


    @Test
    @DisplayName("이미지 글 작성 서비스")
    public void test1() throws IOException {
        //given
        Long postId=newImgPost();
        //then
        Post resultPost=postRepository.getPostById(postId);

        Assertions.assertEquals(1L,postRepository.count());
        Assertions.assertEquals("test내용2",resultPost.getContent());
        Assertions.assertEquals("자유",resultPost.getCategory().getCategoryName().getCategory());
        Assertions.assertEquals(1L,resultPost.getImgUrls().size());
        System.out.println(resultPost.getImgUrls().get(0));

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
    @DisplayName("글 수정 서비스 이미지 x")
    public void test3(){
        //given
        Long postId=newPost();
        Long id=getId();

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();


        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(id)
                .build();
        //when
        List<String> list=new ArrayList<>();
        postService.editPost(editPostServiceDto,list);
        //then
        Post resultPost=postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
    }


    @Test
    @DisplayName("글 좋아요 실패 중복 추천!")
    public void test4(){
        //given
        Long postId=newPost();
        Long id=getId();

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(postId)
                .id(id)
                .isLike(true)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        Assertions.assertThrows(PostAlreadyLikesException.class,()->{postService.likesPost(likesPostServiceDto);});
        //then
        Assertions.assertEquals(1L,postLikeRepository.count());
    }

    @Test
    @DisplayName("글 좋아요 다른글 성공!!")
    public void test5() throws IOException {
        //given
        Long postId=newPost();

        Long postId2=newImgPost();
        Long id=getId();


        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(postId)
                .id(id)
                .isLike(true)
                .build();
        LikesPostServiceDto likesPostServiceDto2=LikesPostServiceDto.builder()
                .postId(postId2)
                .isLike(true)
                .id(id)
                .build();

        //when
        postService.likesPost(likesPostServiceDto);
        postService.likesPost(likesPostServiceDto2);
        //then
        int likesNum=postRepository.getLikesNum(postId);
        Assertions.assertEquals(1,likesNum);
    }


    @Test
    @DisplayName("글 좋아요 다른 아이디 서비스 성공")
    public void test6(){
        //given
        Long postId=newPost();

        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .postId(postId)
                .id(id)
                .isLike(true)
                .build();

        LikesPostServiceDto likesPostServiceDto2=LikesPostServiceDto.builder()
                .postId(postId)
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
    @DisplayName("글 수정 서비스 :관리자  이미지 x")
    public void test7(){
        //given
        Long postId=newPost();
        Long id=getId();
        testSignIn2();

        Member member=memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.setRole(Role.ADMIN);
        memberRepository.save(member);

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();

        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(id)
                .build();
        List<String> list =new ArrayList<>();
        //when
        postService.editPost(editPostServiceDto,list);
        //then
        Post resultPost=postRepository.getPostById(postId);

        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
        Assertions.assertEquals("거래",resultPost.getCategory().getCategoryName().getCategory());
    }

    @Test
    @DisplayName("글 삭제 서비스  ")
    public void test8(){
        //given
        Long postId=newPost();
        Long id=getId();

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(postId)
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
        Long postId=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(postId)
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
        Long postId=newPost();
        Long id=getId();
        testSignIn2();
        Long id2=getId2();

        Member member=memberRepository.findById(id2).orElseThrow(MemberNotFoundException::new);
        member.setRole(Role.ADMIN);

        memberRepository.save(member);

        DeletePostServiceDto deletePostServiceDto= DeletePostServiceDto.builder()
                .postId(postId)
                .id(id2)
                .build();

        postService.deletePost(deletePostServiceDto);

        //when
        Assertions.assertEquals(0,postRepository.count());
        //then
    }

    @Test
    @DisplayName("글 1개 조회수 늘어나는지 확인")
    public void test11() throws IOException {
        //given
        Long postId=newImgPost();
        Long id=getId();

        //when
        PostResponseDto postResponseDto=postService.getPost(postId);
        System.out.println(postResponseDto.getPostResponseData().getImgUrls().get(0));
        //then

    }


    @Test
    @DisplayName("이미지 글 삭제 서비스")
    public void test12() throws IOException {
        //given
        Long id=getId();
        Long postId=newImgPost();

        DeletePostServiceDto deletePostServiceDto=DeletePostServiceDto.builder()
                .postId(postId)
                .id(id)
                .build();
        //when
        postService.deletePost(deletePostServiceDto);
        //then
        Assertions.assertEquals(0L,postRepository.count());
        Assertions.assertEquals(0L,postImageRepository.count());

    }

    @Test
    @DisplayName("글 작성 이미지 x 서비스")
    public void test13() throws IOException {
        //given
        Long postId=newPost();
        //then
        Post resultPost=postRepository.getPostById(postId);

        Assertions.assertEquals(1L,postRepository.count());
        Assertions.assertEquals("테스트테스트테스트",resultPost.getContent());
        Assertions.assertEquals("자유",resultPost.getCategory().getCategoryName().getCategory());
        Assertions.assertEquals(0L,resultPost.getImgUrls().size());
    }

    @Test
    @DisplayName("글 수정 (삭제) 서비스")
    public void test14() throws IOException {
        //given
        Long postId=newImgPost();
        Long id=getId();

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();


        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(id)
                .build();
        List<String> deleteFiles = new ArrayList<>();
        Post post=postRepository.getPostById(postId);

        deleteFiles.add(post.getImgUrls().get(0).getImgFileName());

        List<String> list=new ArrayList<>();

        s3service.deleteFiles(deleteFiles,"raw");

        //when
        postService.editPost(editPostServiceDto,list);
        //then
        Post resultPost=postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
        Assertions.assertEquals(0L,postImageRepository.count());
    }
    @Test
    @DisplayName("글 수정 (이미지 추가,삭제) 서비스")
    public void test15() throws IOException {
        //given
        Long postId=newImgPost();
        Long id=getId();

        EditPostDto editPostDto=EditPostDto.builder()
                .category("TRADE")
                .title("제목 수정")
                .content("내용 수정")
                .build();


        EditPostServiceDto editPostServiceDto= EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(id)
                .build();

        //추가할 파일
        List<MultipartFile> upLoadFiles = new ArrayList<>();
        upLoadFiles.add(new MockMultipartFile("image", "한강.jpg", "image/jpg",
                new FileInputStream(new File("C:/test/한강.jpg"))));

        List<String> list=new ArrayList<>();
        //삭제할 파일
        List<String> deleteFiles = new ArrayList<>();
        Post post=postRepository.getPostById(postId);

        // 첫 번째 파일이라고 가정... 이름을 정확히 알 수 없으니까.....
        deleteFiles.add(post.getImgUrls().get(0).getImgFileName());

        //when
        list=s3service.saveFile(upLoadFiles,"raw");
        postService.editPost(editPostServiceDto,list);
        s3service.deleteFiles(deleteFiles,"raw");
        //then
        Post resultPost=postRepository.getPostById(postId);
        Assertions.assertEquals("제목 수정",resultPost.getTitle());
        Assertions.assertEquals("내용 수정",resultPost.getContent());
        Assertions.assertEquals(1L,postImageRepository.count());
        for(PostImage postImage:resultPost.getImgUrls()){
            System.out.println(postImage.getImgFileName());
        }


    }

}
