-- Alter song duration to BIGINT to save in format of milisecond
ALTER TABLE public.media
ALTER COLUMN song_duration TYPE BIGINT;