ALTER TABLE public.users
ADD COLUMN terms_of_use_accepted BOOLEAN DEFAULT FALSE;

ALTER TABLE public.post
    DROP COLUMN terms_of_use_accepted;