ALTER TABLE refresh_token
DROP CONSTRAINT fk_refresh_token_user;

ALTER TABLE refresh_token
ADD CONSTRAINT fk_user_refresh_token
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;