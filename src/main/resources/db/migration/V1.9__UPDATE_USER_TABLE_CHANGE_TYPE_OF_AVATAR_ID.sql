ALTER TABLE public.users
DROP COLUMN avatar_id,
ADD COLUMN avatar_file_name TEXT;