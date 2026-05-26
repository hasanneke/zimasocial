CREATE SEQUENCE IF NOT EXISTS comment_id_seq
    START WITH 1
    INCREMENT BY 1;

SELECT setval(
       'comment_id_seq',
       COALESCE((SELECT MAX(ID) FROM comment), 0) + 1,
       false
);