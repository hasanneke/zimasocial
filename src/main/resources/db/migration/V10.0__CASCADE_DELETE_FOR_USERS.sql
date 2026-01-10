ALTER TABLE chat_room
DROP CONSTRAINT chat_room_participant_2_fkey;

ALTER TABLE chat_room
ADD CONSTRAINT chat_room_participant_2_fkey
FOREIGN KEY (participant_2)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE chat_room
DROP CONSTRAINT chat_room_participant_1_fkey;

ALTER TABLE chat_room
ADD CONSTRAINT chat_room_participant_1_fkey
FOREIGN KEY (participant_1)
REFERENCES users(id)
ON DELETE CASCADE;