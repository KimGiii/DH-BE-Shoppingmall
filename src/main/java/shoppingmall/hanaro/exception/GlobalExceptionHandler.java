package shoppingmall.hanaro.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingmall.hanaro.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.builder()
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus().value())
                .error(ErrorCode.INVALID_INPUT_VALUE.getStatus().name())
                .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
        return new ResponseEntity<>(response, ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    /**
     * 비즈니스 로직 실행 중 발생하는 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().name())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 위에 정의되지 않은 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Internal Server Error: ", e); // 에러 로그 추가
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().name())
                .code(errorCode.getCode())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}
