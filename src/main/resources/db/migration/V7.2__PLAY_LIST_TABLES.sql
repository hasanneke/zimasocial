CREATE TABLE playlist (
    id uuid primary key,
    name varchar(64) not null,
    user_id bigint references public.users(id) on delete cascade not null,
    type varchar(16) not null
);

CREATE TABLE playlist_item (
    id uuid primary key,
    playlist_id uuid references public.playlist(id) on delete cascade not null,
    media_item_id uuid references public.media_item(id) on delete cascade not null
);