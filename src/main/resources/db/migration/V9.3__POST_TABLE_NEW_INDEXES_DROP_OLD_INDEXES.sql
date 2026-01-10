DROP INDEX idx_search_all_index;

DROP INDEX idx_search_by_type_index;

CREATE INDEX idx_id_score
ON public.post (id, score);

CREATE INDEX idx_id_score_type
    ON public.post (id, score, type);