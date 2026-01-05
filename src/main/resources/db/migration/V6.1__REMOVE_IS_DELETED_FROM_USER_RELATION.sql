-- IS_DELETED is reorganized. We no longer want to track every deleted item for the use of indexes in ease.
ALTER TABLE user_relation
DROP COLUMN is_deleted;
