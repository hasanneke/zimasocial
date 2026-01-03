ALTER TABLE post
DROP CONSTRAINT post_media_id_fkey;

UPDATE media SET post_id = null;
DROP TABLE media;