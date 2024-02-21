package com.board.responseDto;

import com.board.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {
    private String code;
    private String message;
    private int status;
    private List<FieldError> errors;

    private ErrorResponseDto(final ErrorCode code){
        this.message=code.getMessage();
        this.code=code.getCode();
        this.status=code.getStatus();
        this.errors=new ArrayList<>();
    }

    private ErrorResponseDto(final ErrorCode code,List<FieldError> errors){
        this.message=code.getMessage();
        this.code=code.getCode();
        this.status=code.getStatus();
        this.errors=errors;
    }

    public static ErrorResponseDto of(final ErrorCode code,final BindingResult bindingResult){
        List<FieldError> fieldErrors=FieldError.getFieldError(bindingResult);
        return new ErrorResponseDto(code,fieldErrors);
    }

    public static ErrorResponseDto of(final ErrorCode code){
        return new ErrorResponseDto(code);
    }



    @Getter
    public static class FieldError{
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field,String value,String reason){
            this.field=field;
            this.value=value;
            this.reason=reason;
        }

        private static List<FieldError> getFieldError(final BindingResult bindingResult){
            final List<org.springframework.validation.FieldError> fieldErrors=bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error->FieldError.builder()
                            .reason(error.getDefaultMessage())
                            .field(error.getField())
                            .value((String)error.getRejectedValue())
                            .build())
                    .collect(Collectors.toList());
        }
    }
}
