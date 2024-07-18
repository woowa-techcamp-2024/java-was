-- 유저 테이블 삭제 (이미 존재하는 경우)
DROP TABLE IF EXISTS users CASCADE;

-- 유저 테이블 생성
CREATE TABLE users (
                       user_id VARCHAR(255) PRIMARY KEY,
                       password VARCHAR(255),
                       name VARCHAR(255),
                       email VARCHAR(255)
);

-- 초기 유저 데이터 삽입
-- INSERT INTO users (user_id, password, name, email) VALUES
--                                                        ('a', 'a', 'a', 'a');

-- 아티클 테이블 삭제 (이미 존재하는 경우)
DROP TABLE IF EXISTS articles CASCADE;

-- 아티클 테이블 생성
CREATE TABLE articles (
                          article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          content CLOB NOT NULL,
                          user_id VARCHAR(255) NOT NULL,
                          photo_path VARCHAR(255),
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);