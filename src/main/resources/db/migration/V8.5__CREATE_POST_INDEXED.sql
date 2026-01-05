CREATE INDEX IF NOT EXISTS idx_post_indexes
    ON public.post (score, type, created_at)