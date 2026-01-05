CREATE INDEX IF NOT EXISTS idx_post_indexes
    ON public.post (score, created_at)