CREATE TABLE IF NOT EXISTS screenplays (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           title VARCHAR(255) NOT NULL,
    content TEXT,
    client_name VARCHAR(255),
    client_email VARCHAR(255),
    client_phone VARCHAR(50),
    stage VARCHAR(50),
    created_at DATETIME,
    analyst_note TEXT,
    approvals_count INT DEFAULT 0,
    rejections_count INT DEFAULT 0
    );
