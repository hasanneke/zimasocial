CREATE INDEX IF NOT EXISTS idx_user_received_relation
    ON public.user_relation (receiver_id, relation);

CREATE INDEX IF NOT EXISTS idx_user_follow_request
    ON public.follow_request(followed_id, follower_id);

CREATE INDEX IF NOT EXISTS idx_post_like_search
    ON public.likes(user_id, post_id, like_type);

CREATE INDEX IF NOT EXISTS idx_comment_like_search
    ON public.likes(user_id, comment_id);

CREATE INDEX IF NOT EXISTS idx_user_relation
    ON public.user_relation (initiated_id, receiver_id, relation);

CREATE INDEX IF NOT EXISTS idx_user_initiated_relation
    ON public.user_relation (initiated_id, relation);