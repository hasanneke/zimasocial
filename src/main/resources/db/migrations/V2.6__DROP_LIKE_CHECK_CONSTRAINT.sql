-- Dropped the check_post_and_like constraint to manager the business logic inside the domain.
ALTER TABLE public.likes
DROP CONSTRAINT check_post_and_like;