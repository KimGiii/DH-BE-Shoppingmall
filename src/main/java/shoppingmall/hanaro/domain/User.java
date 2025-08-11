package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    private String loginId;

    private String password;

    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static User createUser(String loginId, String password, String name, Address address) {
        User user = new User();
        user.loginId = loginId;
        user.password = password;
        user.name = name;
        user.address = address;
        user.role = UserRole.ROLE_USER; // 기본값은 USER

        //== 연관관계 설정 ==//
        Cart cart = Cart.createCart(user);
        user.cart = cart;

        return user;
    }
}
