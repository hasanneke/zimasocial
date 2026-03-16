ALTER TABLE notification
DROP CONSTRAINT fk_notification_post;

ALTER TABLE notification
ADD CONSTRAINT fk_notification_post FOREIGN KEY (post_id) REFERENCES public.post(id) ON DELETE CASCADE;