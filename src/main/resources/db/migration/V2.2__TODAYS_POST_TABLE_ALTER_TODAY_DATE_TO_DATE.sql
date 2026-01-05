-- Mistakely created todays_date as timestamp
-- Taken action: Altered todays_date column to date
ALTER TABLE PUBLIC.todays_post
ALTER COLUMN todays_date TYPE DATE;