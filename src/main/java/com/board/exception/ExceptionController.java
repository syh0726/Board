package com.board.exception;

import com.board.exception.BoardException;
import com.board.responseDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    //@valid 검증 실패 Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ErrorResponseDto errorResponseDto=ErrorResponseDto.of(ErrorCode.INVALID_INPUT_TYPE,e.getBindingResult());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    //@bind 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException e){
        ErrorResponseDto errorResponseDto=ErrorResponseDto.of(ErrorCode.INVALID_INPUT_TYPE,e.getBindingResult());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    //커스텀 Exception
    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ErrorResponseDto> boardException(BoardException e){
        final ErrorCode errorCode=e.getErrorCode();
        ErrorResponseDto errorResponseDto= ErrorResponseDto.of(errorCode);

        return ResponseEntity.status(errorCode.getStatus())
                .body(errorResponseDto);
    }
}
