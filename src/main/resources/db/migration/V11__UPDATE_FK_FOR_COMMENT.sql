ALTER TABLE likes
DROP CONSTRAINT fk_like_comment;

ALTER TABLE likes
ADD CONSTRAINT fk_like_comment
    FOREIGN KEY (comment_id)
REFERENCES public.comment(id) ON DELETE CASCADE;