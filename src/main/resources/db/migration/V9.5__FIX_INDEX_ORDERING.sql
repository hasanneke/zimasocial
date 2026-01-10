DROP INDEX idx_id_score;

DROP INDEX idx_id_score_type;

DROP INDEX idx_type;

CREATE INDEX idx_score_id
    ON public.post (score, id);

CREATE INDEX idx_score_type_id
    ON public.post (score, type, id);

CREATE INDEX idx_type
    ON public.post (type);