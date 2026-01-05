CREATE INDEX IF NOT EXISTS idx_report
    ON public.report (resource_id, reporter_id, resource_type);