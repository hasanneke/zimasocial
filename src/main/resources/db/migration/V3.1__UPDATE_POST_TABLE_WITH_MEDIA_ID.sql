ALTER TABLE public.post
ADD COLUMN media_id UUID REFERENCES public.media(id);
