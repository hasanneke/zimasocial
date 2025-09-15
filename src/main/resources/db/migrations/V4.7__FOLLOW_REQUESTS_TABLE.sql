CREATE TABLE follow_request (
    id UUID PRIMARY KEY,
    follower_id BIGINT references users(id),
    followed_id BIGINT references users(id),
    accepted BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);