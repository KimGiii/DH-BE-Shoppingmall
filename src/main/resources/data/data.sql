-- 관리자 계정 (비밀번호: 12345678)
INSERT INTO users (login_id, password, name, role, city, street, zipcode) VALUES ('admin', '$2a$10$6e13/QDFcZTmuVvTyBGt2uIOqkKkbDKIULeI55LOpj3wN.OF5v4bi', '관리자', 'ROLE_ADMIN', '서울', '강남대로', '06123');
-- 일반 사용자 계정 (비밀번호: 12345678)
INSERT INTO users (login_id, password, name, role, city, street, zipcode) VALUES ('user', '$2a$10$6e13/QDFcZTmuVvTyBGt2uIOqkKkbDKIULeI55LOpj3wN.OF5v4bi', '일반사용자', 'ROLE_USER', '부산', '해운대로', '48094');

-- 샘플 상품 데이터
INSERT INTO product (name, price, stock_quantity, description, image_url) VALUES
('신선한 사과', 2500, 100, '아삭하고 달콤한 가을 사과', '/upload/sample/apple.jpg'),
('유기농 당근', 3500, 50, '깨끗한 땅에서 자란 건강한 당근', '/upload/sample/carrot.jpg'),
('국산 블루베리 100g', 8000, 30, '진한 맛과 향을 자랑하는 블루베리', '/upload/sample/blueberry.jpg'),
('제주 감귤 1kg', 12000, 80, '제주도의 햇살을 듬뿍 받은 달콤한 감귤', '/upload/sample/tangerine.jpg');


-- 주문 데이터 생성 (고정 날짜 기준)
-- 주문 1: 2025-08-10
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (1, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (1, 2, 1, '2025-08-10 10:30:00', 'DELIVERED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES
(1, 1, 2500, 2),
(1, 2, 3500, 1);

-- 주문 2: 2025-08-11
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (2, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (2, 2, 2, '2025-08-11 14:00:00', 'DELIVERED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES
(2, 3, 8000, 1),
(2, 4, 12000, 1);

-- 주문 3: 2025-08-12
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (3, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (3, 2, 3, '2025-08-12 18:00:00', 'SHIPPED');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (3, 1, 2500, 5);

-- 주문 4: 2025-08-12 (취소된 주문)
INSERT INTO delivery (delivery_id, city, street, zipcode) VALUES (4, '부산', '해운대로', '48094');
INSERT INTO orders (order_id, user_id, delivery_id, order_date, status) VALUES (4, 2, 4, '2025-08-12 09:00:00', 'CANCEL');
INSERT INTO order_item (order_id, product_id, order_price, quantity) VALUES (4, 2, 3500, 3);


-- 일별 매출 통계 데이터
-- 2025-08-10 매출 (주문 1 합계: 2500*2 + 3500*1 = 8500)
INSERT INTO daily_sales (sales_date, total_sales) VALUES ('2025-08-10', 8500);
-- 2025-08-11 매출 (주문 2 합계: 8000*1 + 12000*1 = 20000)
INSERT INTO daily_sales (sales_date, total_sales) VALUES ('2025-08-11', 20000);
-- 2025-08-12 매출 (주문 3 합계: 2500*5 = 12500, 주문 4는 취소되어 제외)
INSERT INTO daily_sales (sales_date, total_sales) VALUES ('2025-08-12', 12500);

-- MySQL에서 auto_increment 값 초기화
ALTER TABLE users AUTO_INCREMENT = 3;
ALTER TABLE product AUTO_INCREMENT = 5;
ALTER TABLE orders AUTO_INCREMENT = 5;
ALTER TABLE delivery AUTO_INCREMENT = 5;
ALTER TABLE cart AUTO_INCREMENT = 2;
ALTER TABLE daily_sales AUTO_INCREMENT = 4;
ALTER TABLE cart_product AUTO_INCREMENT = 3;
ALTER TABLE order_item AUTO_INCREMENT = 7;