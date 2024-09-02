CREATE TABLE IF NOT EXISTS member
(
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    account                  VARCHAR(50) UNIQUE NOT NULL,
    password                 VARCHAR(255)       NOT NULL,
    x                        DECIMAL            NULL,
    y                        DECIMAL            NULL,
    is_recommendation_active BOOLEAN            NOT NULL DEFAULT true,
    joined_at                TIMESTAMP(6)       NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurant
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(50)    NOT NULL,
    zip_code     VARCHAR(10)    NOT NULL,
    address      VARCHAR(255)   NOT NULL,
    cuisine      VARCHAR(50)    NOT NULL,
    x            DECIMAL(6, 10) NOT NULL,
    y            DECIMAL(6, 10) NOT NULL,
    phone_number VARCHAR(20)    NULL,
    homepage_url VARCHAR(255)   NULL,
    avg_score    DECIMAL(1, 2)  NOT NULL,
    view_count   INT            NOT NULL,
    updated_at   TIMESTAMP(6)   NOT NULL
);

CREATE TABLE IF NOT EXISTS review
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    content       VARCHAR(255) NULL,
    score         INT          NOT NULL DEFAULT 5,
    created_at    TIMESTAMP(6) NOT NULL,
    member_id     BIGINT       NOT NULL,
    restaurant_id BIGINT       NOT NULL,
    CONSTRAINT fk_member_review FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_restaurant_review FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
);

CREATE TABLE IF NOT EXISTS region
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    si_do      VARCHAR(255) NOT NULL,
    sgg        VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);