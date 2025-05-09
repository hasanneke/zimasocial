ALTER TABLE post_report
DROP CONSTRAINT fk_post_report_reason,
DROP TABLE REPORT_REASON;

CREATE TABLE REPORT_REASON (
    name VARCHAR(64),
    CONSTRAINT id_report_name_key PRIMARY KEY (name)
);

ALTER TABLE public.post_report
DROP COLUMN report_type,
ADD COLUMN report_type VARCHAR(64),
ADD CONSTRAINT fk_post_report_reason FOREIGN KEY (report_type) REFERENCES public.report_reason(name);