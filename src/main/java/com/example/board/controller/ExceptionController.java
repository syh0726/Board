package com.example.board.controller;

import com.example.board.exception.BoardException;
import com.example.board.exception.member.InvalidSigninInformationException;
import com.example.board.responseDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ErrorResponseDto> boardException(BoardException e){
    int statuscode=e.getStatusCode();

    ErrorResponseDto errorResponseDto =ErrorResponseDto.builder()
            .code(String.valueOf(statuscode))
            .message(e.getMessage())
            .build();

    ResponseEntity<ErrorResponseDto> response=ResponseEntity.status(statuscode)
            .body(errorResponseDto);

    return response;
    }
}
