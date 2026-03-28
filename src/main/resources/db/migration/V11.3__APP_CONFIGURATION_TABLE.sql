CREATE TABLE app_config (
    code VARCHAR(32) PRIMARY KEY,
    ads_enabled BOOLEAN DEFAULT FALSE
);

INSERT INTO app_config VALUES ('prod', true);