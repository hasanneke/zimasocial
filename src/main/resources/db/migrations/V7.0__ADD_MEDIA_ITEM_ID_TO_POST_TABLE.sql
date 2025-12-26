ALTER TABLE public.post
ADD COLUMN media_item_id UUID REFERENCES public.media_item(id);