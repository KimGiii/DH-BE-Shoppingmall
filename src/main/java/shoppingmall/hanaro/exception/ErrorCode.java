package shoppingmall.hanaro.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 요청입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "C003", "엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "잘못된 타입의 값입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "U002", "로그인 정보가 올바르지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "U003", "리프레시 토큰을 찾을 수 없습니다."),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다."),

    // Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "장바구니를 찾을 수 없습니다."),
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "CP001", "장바구니에서 해당 상품을 찾을 수 없습니다."),

    // 500
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "파일 업로드에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
