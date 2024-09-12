CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       description TEXT,
                       title TEXT,
                       created TIMESTAMP,
                       done BOOLEAN
);