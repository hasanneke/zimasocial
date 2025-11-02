-- Alter song duration to BIGINT to save in format of milisecond
ALTER TABLE public.chat_room
ADD COLUMN last_message_id UUID REFERENCES public.chat_message(id);