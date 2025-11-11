-- Force created_at at not null db level
ALTER TABLE post
ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE comment
ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE likes
ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE notification
ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE users
ALTER COLUMN created_at SET NOT NULL;