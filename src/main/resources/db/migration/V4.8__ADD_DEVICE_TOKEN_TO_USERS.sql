-- Added FCM token for push notifications
ALTER TABLE public.users
ADD COLUMN device_token VARCHAR(512);