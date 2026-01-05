CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table media (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id BIGINT references public.post(id),
    comment_id BIGINT references public.comment(id),
    media_type VARCHAR(32) NOT NULL,
    -- Music
    song_resource_id VARCHAR(128),
    singer_name VARCHAR(64),
    song_name VARCHAR(64),
    song_duration INTEGER,
    album_cover_url VARCHAR(512),
    spotify_short_play_url VARCHAR(512),
    song_provider VARCHAR(32),
    -- Movies section
    movie_source_id VARCHAR(64),
    movie_poster_image_url VARCHAR(512),
    movie_name VARCHAR(256),
    movie_description TEXT, -- Can contain HTML code
    movie_summary VARCHAR(256),
    imdb_score DOUBLE PRECISION,
    vote_count INTEGER,
    movie_release_date DATE,
    movie_genres VARCHAR(256),
    movie_original_language VARCHAR(32),
    movie_provider VARCHAR(32),
    -- Books
    book_title VARCHAR(64),
    book_summary VARCHAR(256),
    book_description TEXT,
    author_name VARCHAR(64),
    page_count INTEGER,
    book_cover_url VARCHAR(256),
    book_provider VARCHAR(32),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);