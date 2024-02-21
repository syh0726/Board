package com.board.domain.member;

import com.board.domain.post.Post;
import com.board.domain.auth.Session;
import com.board.responseDto.member.GetProfileResponseDto;
import com.board.responseDto.member.SignInResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String nickName;
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private String phoneNumber;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "member",fetch = FetchType.LAZY)
    List<Session> sessions;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "member",fetch = FetchType.LAZY)
    List<Post> posts;

    @Builder
    public Member(String email, String nickName, String password, String phoneNumber) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role=Role.USER;
    }

    public SignInResponseDto toSignInResponseDto(){
        return SignInResponseDto.builder()
                .id(this.getId())
                .role(this.getRole().getRole())
                .nickName(this.getNickName())
                .build();
    }

    public GetProfileResponseDto toGetProfileResponseDto(){
        return GetProfileResponseDto.builder()
                .email(this.getEmail())
                .nickName(this.getNickName())
                .phoneNumber(this.getPhoneNumber())
                .build();
    }

    public void changePassword(String password){
        this.password=password;
    }

    public void changePhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public void changeNickname(String nickName){
        this.nickName=nickName;
    }

}
