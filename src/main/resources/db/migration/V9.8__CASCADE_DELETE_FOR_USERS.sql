ALTER TABLE notification
DROP CONSTRAINT fk_notification_receiver_user_id;

ALTER TABLE notification
ADD CONSTRAINT fk_notification_receiver_user_id
FOREIGN KEY (receiver_user_id)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE notification
DROP CONSTRAINT fk_notification_sender_user_id;

ALTER TABLE notification
ADD CONSTRAINT fk_notification_sender_user_id
FOREIGN KEY (sender_user_id)
REFERENCES users(id)
ON DELETE CASCADE;