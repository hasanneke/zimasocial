CREATE INDEX IF NOT EXISTS idx_search_by_type_index
    ON public.post (score, created_at, id, type);