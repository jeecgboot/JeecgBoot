-- v3.8.0版本归档了历史增量SQL，启动报错！请手工执行下面SQL，清空flyway_schema历史
CREATE TABLE flyway_schema_history_1 AS SELECT * FROM flyway_schema_history;
delete from flyway_schema_history where installed_rank > 1;