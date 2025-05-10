ALTER TABLE post_report
DROP CONSTRAINT fk_reported_post,
ADD COLUMN reported_post_type VARCHAR(64);

-- RENAME POST_REPORT TABLE TO REPORT FOR GENERAL USE
ALTER TABLE public.post_report RENAME TO report;
