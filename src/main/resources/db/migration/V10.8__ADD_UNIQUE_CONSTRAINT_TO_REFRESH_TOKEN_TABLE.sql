ALTER TABLE refresh_token
ADD CONSTRAINT unique_refresh_token UNIQUE (token_hash);