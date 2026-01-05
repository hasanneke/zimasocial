CREATE INDEX IF NOT EXISTS idx_post_indexes_type
    ON public.post (score, type, created_at)