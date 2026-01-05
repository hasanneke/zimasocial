CREATE TABLE public.media_item (
    id UUID PRIMARY KEY,
    resource_id VARCHAR(64) NOT NULL,
    resource_url VARCHAR(512) NOT NULL,
    content JSONB,
    provider VARCHAR(32)
);

CREATE TABLE public.play_list (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES public.users(id)  ON DELETE CASCADE,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE public.play_list_item (
    id UUID PRIMARY KEY,
    media_item_id UUID NOT NULL REFERENCES public.media_item(id) ON DELETE CASCADE
);
