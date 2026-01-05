ALTER TABLE chat_room
ADD CONSTRAINT uq_par1_par2 UNIQUE (participant_1, participant_2);