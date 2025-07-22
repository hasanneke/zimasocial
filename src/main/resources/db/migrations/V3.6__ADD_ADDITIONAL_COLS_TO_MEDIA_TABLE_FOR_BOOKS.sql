-- Purpose of this script is to add additional columns to enrich media table for books
ALTER TABLE public.media
ADD COLUMN print_type VARCHAR(64),
ADD COLUMN publish_date DATE,
ADD COLUMN book_language VARCHAR(64),
ADD COLUMN book_small_cover_url TEXT,
ADD COLUMN book_publisher VARCHAR(128);