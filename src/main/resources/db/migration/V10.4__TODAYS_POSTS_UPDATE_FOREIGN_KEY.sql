ALTER TABLE todays_post
DROP CONSTRAINT fk_todays_post_post;

ALTER TABLE todays_post
ADD CONSTRAINT  fk_todays_post_post
FOREIGN KEY (post_id)
REFERENCES public.post(id)
ON DELETE CASCADE;