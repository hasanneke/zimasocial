ALTER TABLE public.chat_room
ADD COLUMN participant_1 BIGINT NOT NULL references public.users(id),
ADD COLUMN participant_2 BIGINT NOT NULL references public.users(id);