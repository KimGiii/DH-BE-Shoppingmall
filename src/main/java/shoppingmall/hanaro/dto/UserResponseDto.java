package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.domain.UserRole;

@Getter
public class UserResponseDto {
    private Long userId;
    private String loginId;
    private String name;
    private Address address;
    private UserRole role;

    @Builder
    public UserResponseDto(Long userId, String loginId, String name, Address address, UserRole role) {
        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }
}
