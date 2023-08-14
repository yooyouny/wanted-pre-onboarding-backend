DROP TABLE IF EXISTS member;
CREATE TABLE member(
    created_datetime datetime(6) DEFAULT NULL,
    id bigint NOT NULL AUTO_INCREMENT,
    modified_date_time datetime(6) DEFAULT NULL,
    email varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_email (email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS post;
CREATE TABLE post (
    is_deleted bit(1) NOT NULL,
    created_datetime datetime(6) DEFAULT NULL,
    deleted_at datetime(6) DEFAULT NULL,
    id bigint NOT NULL AUTO_INCREMENT,
    member_id bigint DEFAULT NULL,
    modified_date_time datetime(6) DEFAULT NULL,
    body text,
    title varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_member_id (member_id),
    CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;