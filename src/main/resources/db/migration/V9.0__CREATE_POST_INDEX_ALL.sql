CREATE INDEX IF NOT EXISTS idx_search_all_index
    ON public.post (score, created_at, id);