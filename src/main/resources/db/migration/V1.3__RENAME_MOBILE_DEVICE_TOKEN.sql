-- RENAME MOBILE TOKEN TO DEVICE TOKEN
ALTER TABLE public.user_mobile_token
RENAME TO user_device_token;