-- CHANGES FOR: grammar mistake
-- RENAME initiater_id to initiated_id due to grammar mistake
ALTER TABLE user_relations
RENAME TO user_relation;

ALTER TABLE public.user_relation
RENAME initiater_id TO initiated_id;