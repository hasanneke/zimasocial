-- Populate users table with trust_score
ALTER TABLE public.users
ADD COLUMN trust_score DOUBLE PRECISION NOT NULL DEFAULT 0;