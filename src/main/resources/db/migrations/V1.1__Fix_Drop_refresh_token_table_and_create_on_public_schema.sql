DROP TABLE public.refresh_token;

CREATE TABLE public.refresh_token (
    id UUID primary key,
    user_id BIGINT not null,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_info TEXT,
    ip_address VARCHAR(45)
);

ALTER TABLE public.refresh_token
ADD CONSTRAINT fk_refresh_token_user
FOREIGN KEY (user_id) REFERENCES users(id);
