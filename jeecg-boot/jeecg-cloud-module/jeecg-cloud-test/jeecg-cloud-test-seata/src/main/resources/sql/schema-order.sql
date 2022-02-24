DROP TABLE IF EXISTS p_order;
CREATE TABLE p_order
(
    id               INT(11) NOT NULL AUTO_INCREMENT,
    user_id          INT(11) DEFAULT NULL,
    product_id       INT(11) DEFAULT NULL,
    amount           INT(11) DEFAULT NULL,
    total_price      DOUBLE       DEFAULT NULL,
    status           VARCHAR(100) DEFAULT NULL,
    add_time         DATETIME     DEFAULT CURRENT_TIMESTAMP,
    last_update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS undo_log;
CREATE TABLE undo_log
(
    id            BIGINT(20) NOT NULL AUTO_INCREMENT,
    branch_id     BIGINT(20) NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT(11) NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
