ALTER TABLE public.notification
ADD CONSTRAINT fk_notification_post
FOREIGN KEY (post_id) REFERENCES public.post(id);