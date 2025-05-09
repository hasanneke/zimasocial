CREATE TABLE post_report (
    reporter_id BIGINT NOT NULL,
    reported_post_id BIGINT NOT NULL,
    reported_post_owner_id BIGINT NOT NULL,
    report_reason VARCHAR(64) NOT NULL,
    PRIMARY KEY(reporter_id, reported_post_id)
);

ALTER TABLE public.post_report
ADD CONSTRAINT fk_reporter_user
FOREIGN KEY (reporter_id) REFERENCES public.users(id);

ALTER TABLE public.post_report
ADD CONSTRAINT fk_reported_post
FOREIGN KEY (reported_post_id) REFERENCES public.post(id);

ALTER TABLE public.post_report
ADD CONSTRAINT fk_reported_user
FOREIGN KEY (reported_post_owner_id) REFERENCES public.users(id);