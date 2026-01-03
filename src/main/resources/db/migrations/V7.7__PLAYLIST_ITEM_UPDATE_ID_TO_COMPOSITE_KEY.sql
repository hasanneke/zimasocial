ALTER TABLE playlist_item
RENAME COLUMN media_item_id TO media_id;

ALTER TABLE playlist_item
DROP COLUMN id;

ALTER TABLE playlist_item
ADD CONSTRAINT playlist_item_pk PRIMARY KEY (playlist_id, media_id)