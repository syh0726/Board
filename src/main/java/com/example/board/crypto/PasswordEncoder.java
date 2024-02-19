package com.example.board.crypto;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Component
public class PasswordEncoder {
    private static final SCryptPasswordEncoder encoder=new SCryptPasswordEncoder(
            16,
            8,
            1,
            32,
            64
    );

    //암호화
    public String encrypt(String rawPassword){
        return encoder.encode(rawPassword);
    }

    //원본 비밀번호와 암호화한 비밀번호가 일치하면 true 반환
    public boolean matches(String rawPassword,String encryptedPassword){
        return encoder.matches(rawPassword,encryptedPassword);
    }

}
