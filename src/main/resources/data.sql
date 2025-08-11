-- 관리자 계정 (비밀번호: 12345678)
INSERT INTO users (user_id, login_id, password, name, role, city, street, zipcode) VALUES
(1, 'admin', '$2a$10$y.DW2qjKiU7z.d.5v9t6i..1.p3.S.5.m.u.W.5.k.Z.i', '관리자', 'ROLE_ADMIN', '서울', '강남대로', '06123');

-- 일반 사용자 계정 (비밀번호: password)
INSERT INTO users (user_id, login_id, password, name, role, city, street, zipcode) VALUES
(2, 'user', '$2a$10$NEJ25eJd2F.4.V.D.5s2a.jB6X3Z6y4..j.k.Z.b.1k.Q.z.q.g', '일반사용자', 'ROLE_USER', '부산', '해운대로', '48094');

-- 샘플 상품 데이터
INSERT INTO product (product_id, name, price, stock_quantity, description, image_url) VALUES
(1, '신선한 사과', 2500, 100, '아삭하고 달콤한 가을 사과', '/upload/sample/apple.jpg'),
(2, '유기농 당근', 3500, 50, '깨끗한 땅에서 자란 건강한 당근', '/upload/sample/carrot.jpg'),
(3, '국산 블루베리 100g', 8000, 30, '진한 맛과 향을 자랑하는 블루베리', '/upload/sample/blueberry.jpg'),
(4, '제주 감귤 1kg', 12000, 80, '제주도의 햇살을 듬뿍 받은 달콤한 감귤', '/upload/sample/tangerine.jpg');

-- 사용자(user_id=2)의 장바구니 생성
INSERT INTO cart (cart_id, user_id) VALUES (1, 2);

-- 사용자(user_id=2)의 장바구니에 상품 추가
INSERT INTO cart_product (cart_id, product_id, quantity) VALUES
(1, 1, 2), -- 사과 2개
(1, 3, 1); -- 블루베리 1개

-- 주문 데이터 생성 (다양한 상태 및 시간을 가진 주문)
-- 주문 1: 배송 완료 (어제 주문)
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (1, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (1, 2, 1, NOW() - INTERVAL 1 DAY, 'DELIVERED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES
(1, 1, 2500, 2),
(1, 2, 3500, 1);

-- 주문 2: 배송 중 (1시간 30분 전 주문) -> 스케줄러에 의해 '배송완료'로 변경될 예정
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (2, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (2, 2, 2, NOW() - INTERVAL 90 MINUTE, 'SHIPPED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (2, 3, 8000, 1);

-- 주문 3: 배송 준비 중 (20분 전 주문) -> 스케줄러에 의해 '배송중'으로 변경될 예정
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (3, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (3, 2, 3, NOW() - INTERVAL 20 MINUTE, 'PREPARING_FOR_SHIPMENT');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (3, 4, 12000, 1);

-- 주문 4: 결제 완료 (방금 주문) -> 스케줄러에 의해 '배송준비중'으로 변경될 예정
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (4, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (4, 2, 4, NOW(), 'PAYMENT_COMPLETED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (4, 1, 2500, 5);

-- 주문 5: 취소된 주문
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (5, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (5, 2, 5, NOW() - INTERVAL 2 DAY, 'CANCEL');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (5, 2, 3500, 3);

-- 일별 매출 통계 데이터 (어제, 그저께)
-- 어제 매출 (주문 1의 합계: 2500*2 + 3500*1 = 8500)
INSERT INTO daily_sales (sales_date, total_sales) VALUES (CURDATE() - INTERVAL 1 DAY, 8500);
-- 그저께 매출 (주문 5는 취소되었으므로 0)
INSERT INTO daily_sales (sales_date, total_sales) VALUES (CURDATE() - INTERVAL 2 DAY, 0);

-- MySQL에서 auto_increment 값 초기화
ALTER TABLE users AUTO_INCREMENT = 3;
ALTER TABLE product AUTO_INCREMENT = 5;
ALTER TABLE orders AUTO_INCREMENT = 6;
ALTER TABLE delivery AUTO_INCREMENT = 6;
ALTER TABLE cart AUTO_INCREMENT = 2;
ALTER TABLE daily_sales AUTO_INCREMENT = 3;
ALTER TABLE cart_product AUTO_INCREMENT = 3;
ALTER TABLE order_item AUTO_INCREMENT = 6;