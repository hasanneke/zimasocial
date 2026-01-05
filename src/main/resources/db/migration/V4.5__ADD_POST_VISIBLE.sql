-- Purpose for this script to set posts are either visible or not
ALTER TABLE post
ADD COLUMN is_visible BOOLEAN DEFAULT true;

