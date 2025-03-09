CREATE TABLE user
(
    idx           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(255)                                NOT NULL,                  -- 이메일
    password      VARCHAR(255),                                                          -- 비밀번호 (NULL 가능)
    profile_image VARCHAR(255),                                                          -- 프로필 이미지 URL
    join_type     ENUM ('normal', 'google', 'kakao', 'naver') NOT NULL DEFAULT 'normal', -- 가입 유형 (예: 이메일, 소셜 로그인)
    created_at    TIMESTAMP                                            DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE travel
(
    idx         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255)                             NOT NULL, -- 여행 제목
    description TEXT,                                              -- 여행 설명 (NULL 가능)
    start_date  DATE                                     NOT NULL, -- 여행 시작일
    end_date    DATE                                     NOT NULL, -- 여행 종료일
    status      ENUM ('planned', 'ongoing', 'completed') NOT NULL  -- 여행 상태
);

CREATE TABLE member
(
    idx        BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_idx BIGINT                   NOT NULL,                  -- 여행 ID
    user_idx   BIGINT                   NOT NULL,                  -- 사용자 ID
    role       ENUM ('admin', 'member') NOT NULL DEFAULT 'member', -- 역할
    created_at TIMESTAMP                         DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (travel_idx) REFERENCES travel (idx),
    FOREIGN KEY (user_idx) REFERENCES user (idx)
);

CREATE TABLE currency
(
    idx          BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_name VARCHAR(100) NOT NULL, -- 여행 국가명
    symbol       VARCHAR(10)  NOT NULL,
    code         VARCHAR(10)  NOT NULL,
    flag_image   VARCHAR(255)
);

CREATE TABLE category
(
    idx   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    icon  VARCHAR(10)  NOT NULL,
    color VARCHAR(10)  NOT NULL
);


CREATE TABLE budget
(
    idx            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100)                  NOT NULL,
    planned_amount DECIMAL(15, 2)                NOT NULL,                -- 계획된 예산
    spent_amount   DECIMAL(15, 2)                NOT NULL DEFAULT 0.00,   -- 실제 사용 금액
    travel_idx     BIGINT                        NOT NULL,
    currency_idx   BIGINT                        NOT NULL,                -- 화폐 단위
    created_at     TIMESTAMP                              DEFAULT CURRENT_TIMESTAMP,
    pay_type       ENUM ('card', 'cash', 'both') NOT NULL DEFAULT 'both', -- 결제 유형
    FOREIGN KEY (travel_idx) REFERENCES travel (idx),
    FOREIGN KEY (currency_idx) REFERENCES currency (idx)
);

CREATE TABLE expense
(
    idx              BIGINT AUTO_INCREMENT PRIMARY KEY,
    item             VARCHAR(100)                               NOT NULL,
    amount           DECIMAL(15, 2)                             NOT NULL, -- 지출 금액
    payment_type     ENUM ('cash', 'card'),                               -- 결제 유형
    payment_category ENUM ('personal', 'shared', 'group_funds') NOT NULL,
    description      TEXT,                                                -- 지출 설명
    image_url        VARCHAR(255),
    is_prepared      TINYINT(1)                                 NOT NULL DEFAULT 0,
    category_idx     BIGINT                                     NOT NULL, -- 카테고리 ID
    budget_idx       BIGINT                                     NOT NULL, -- 예산 ID (어떤 예산의 지출인지)
    member_idx       BIGINT                                     NOT NULL, -- 멤버 ID
    created_at       TIMESTAMP                                           DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_idx) REFERENCES category (idx),
    FOREIGN KEY (budget_idx) REFERENCES budget (idx),
    FOREIGN KEY (member_idx) REFERENCES member (idx)
);


CREATE TABLE income
(
    idx        BIGINT AUTO_INCREMENT PRIMARY KEY,
    item       VARCHAR(100)   NOT NULL,
    amount     DECIMAL(15, 2) NOT NULL, -- 지출 금액
    budget_idx BIGINT         NOT NULL, -- 예산 ID (어떤 예산의 수입인지)
    member_idx BIGINT         NOT NULL, -- 멤버 ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (budget_idx) REFERENCES budget (idx),
    FOREIGN KEY (member_idx) REFERENCES member (idx)
);

