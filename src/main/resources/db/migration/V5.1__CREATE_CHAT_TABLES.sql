CREATE TABLE chat_room (
    id UUID PRIMARY KEY
);

CREATE TABLE chat_room_participant (
    chat_room_id UUID NOT NULL REFERENCES public.chat_room(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (chat_room_id, user_id)
);

CREATE TABLE chat_message (
    id UUID PRIMARY KEY,
    content VARCHAR(1024),
    chat_room_id UUID NOT NULL REFERENCES public.chat_room(id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES public.users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);