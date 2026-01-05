-- When user disables their account or any action that implies they don't want to be seen, update it to true
ALTER TABLE users
ADD COLUMN is_visible BOOLEAN DEFAULT FALSE;