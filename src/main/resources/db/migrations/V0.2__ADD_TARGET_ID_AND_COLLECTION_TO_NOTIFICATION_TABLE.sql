ALTER TABLE public.notification
ADD COLUMN target_collection VARCHAR(64),
ADD COLUMN target_id BIGINT,
ADD COLUMN post_id BIGINT;
