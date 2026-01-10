ALTER TABLE user_device_token
DROP CONSTRAINT user_mobile_token_user_id_fkey;

ALTER TABLE user_device_token
ADD CONSTRAINT user_mobile_token_user_id_fkey
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;