CREATE INDEX IF NOT EXISTS idx_user_deleted_private
    ON public.users (is_deleted, is_private);