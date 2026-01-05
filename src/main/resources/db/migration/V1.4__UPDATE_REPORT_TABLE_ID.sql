-- SEARCH PRIMARY KEY WITH THE COMMAND BELOW
-- SELECT conname FROM pg_constraint WHERE conrelid = 'report'::regclass AND contype = 'p';

ALTER TABLE report DROP CONSTRAINT post_report_pkey;

ALTER TABLE report
ADD PRIMARY KEY (reporter_id, resource_id, resource_type);
