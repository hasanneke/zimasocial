CREATE TABLE public.followers (
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (follower_id, following_id),

    FOREIGN KEY (follower_id) REFERENCES public.users(id)
        ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES public.users(id)
        ON DELETE CASCADE,

     CONSTRAINT prevent_self_follow
        CHECK (follower_id != following_id)
);
-- In PostgreSQL indexes are created outside of CREATE statement
CREATE INDEX idx_follower ON public.followers(follower_id);
CREATE INDEX idx_following ON public.followers(following_id);