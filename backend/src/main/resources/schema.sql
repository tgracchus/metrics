----------------------------------------
-- Hypertable to store weather metrics
----------------------------------------
-- Step 1: Define regular table
CREATE TABLE IF NOT EXISTS metrics (
                                               time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                               key text NULL,
                                               value float8 NULL,
                                               PRIMARY KEY(key, time)
);

-- Step 2: Turn into hypertable
SELECT create_hypertable('metrics','time', if_not_exists => TRUE);
