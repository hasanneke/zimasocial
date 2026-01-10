DROP INDEX idx_user_deleted_private;

CREATE INDEX idx_user_disabled_deleted_private
ON public.users(is_deleted, is_private, is_disabled);