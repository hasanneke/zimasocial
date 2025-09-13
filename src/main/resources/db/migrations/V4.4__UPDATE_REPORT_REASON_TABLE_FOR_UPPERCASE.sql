-- For enum matching and to avoid unnecessary logical complication, make report reason names uppercase
UPDATE report_reason SET name = UPPER(name);