-- Allow users to disable their account
ALTER TABLE public.users
ADD COLUMN is_disabled BOOLEAN DEFAULT FALSE,
ADD COLUMN disable_date DATE,
ADD COLUMN disable_reason VARCHAR(64),
ADD COLUMN delete_date DATE,
ADD COLUMN delete_reason VARCHAR(64);