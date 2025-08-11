package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingmall.hanaro.dto.UserResponseDto;
import shoppingmall.hanaro.service.UserService;

import java.util.List;

@Tag(name = "관리자 회원 관리", description = "관리자 회원 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "전체 회원 목록 조회", description = "모든 회원 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "특정 회원 삭제", description = "ID로 특정 회원을 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
