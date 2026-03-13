drop table if exists tenant_asset;
create table tenant_asset (
    `id` bigint primary key auto_increment,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '资产名称',
    `barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '条码',
    `serial_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '序列号',
    `fa_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '固定资产ID',
    
    `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '品牌',
    `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '分类',
    `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '位置',
    `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default 1 comment '状态',
    `tenant_id` bigint not null comment '租户ID',
    `is_deleted` tinyint(1) not null default 0 comment '是否删除',
    `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci comment '备注',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci comment '描述',
    `create_time` datetime default null comment '创建时间',
    `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '创建人',
    `update_time` datetime default null comment '更新时间',
    `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '更新人'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci comment = '租户资产表';