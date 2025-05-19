-- check_receiver_sender_not_equal
ALTER TABLE public.notification
ADD CONSTRAINT check_receiver_sender_not_equal
CHECK (receiver_user_id != sender_user_id);