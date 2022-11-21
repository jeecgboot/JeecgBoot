/* 请确认以下SQL符合您的变更需求，务必确认无误后再提交执行 */

CREATE TABLE `t_opus` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `name` varchar(64) NOT NULL COMMENT '模型名称',
                           `address` varchar(256) NOT NULL COMMENT '接口地址',
                           `status` varchar(10) NOT NULL COMMENT '接口状态',
                           `deleted` bit(1)  NOT NULL DEFAULT b'0' COMMENT '是否标记删除',
                           `creator` varchar(64)  NOT NULL COMMENT '添加人员',
                           `modifier` varchar(64)  NULL COMMENT '修改人员',
                           `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                           `gmt_modify` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           Unique KEY `idx_name`(`name`) USING BTREE
)
COMMENT='作品表';


CREATE TABLE `t_banner` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `opus_id` varchar(64)  NULL COMMENT '作品表',
                                 `url` varchar(256) NULL COMMENT '接口地址',
                                 `status` varchar(10) NOT NULL COMMENT '接口状态',
                                 `time` int(10) NOT NULL DEFAULT 0 COMMENT '耗时，毫秒',
                                 `deleted` bit(1)  NOT NULL DEFAULT b'0' COMMENT '是否标记删除',
                                 `creator` varchar(64)  NULL COMMENT '添加人员',
                                 `modifier` varchar(64)  NULL COMMENT '修改人员',
                                 `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                 `gmt_modify` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_time`(`gmt_create`) USING BTREE,
                                 KEY `idx_model`(`model_id`) USING BTREE,
                                 KEY `idx_url`(`url`) USING BTREE
)
  COMMENT='模型接口检测表';


/* 请确认以下SQL符合您的变更需求，务必确认无误后再提交执行 */

CREATE TABLE `task_material` (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT,
                               `model_id` bigint(11) NOT NULL COMMENT '选择的模型id',
                               `material_name` varchar(100)  NOT NULL COMMENT '物料名称',
                               `model` varchar(100)  NOT NULL COMMENT '型号',
                               `spec` varchar(100)  NOT NULL COMMENT '规格',
                               `quantity` int(11) NOT NULL COMMENT '数量',
                               `review` int(1)  NULL  COMMENT '复核数',
                               `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否标记删除',
                               `image_url` varchar(256)  NOT NULL COMMENT '图片地址',
                               `file_name` varchar(256) NULL COMMENT '文件名',
                               `file_size` int(10) default 0 not NULL COMMENT '文件大小',
                               `modifier` varchar(32)  NULL COMMENT '修改人',
                               `creator_id` int(11) NULL COMMENT '创建人id',
                               `creator` varchar(32)  NOT NULL COMMENT '创建人',
                               `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modify` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_name`(`model`,`spec`,`material_name`) USING BTREE

)
COMMENT='物料盘点任务表';


/* 请确认以下SQL符合您的变更需求，务必确认无误后再提交执行 */




/* 请确认以下SQL符合您的变更需求，务必确认无误后再提交执行 */

CREATE TABLE `task_nameplate` (
                                `id` bigint(11) NOT NULL AUTO_INCREMENT,
                                `model_id` bigint(11) NOT NULL COMMENT '选择的模型id',
                                `nameplate_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '铭牌类型',
                                `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                                `review_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '复核内容',
                                `review` int(1) NOT NULL DEFAULT 0 COMMENT '是否复核',
                                `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否标记删除',
                                `image_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
                                `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '修改人',
                                `creator_id` bigint(32) NOT NULL COMMENT '创建人id',
                                `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `gmt_modify` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                PRIMARY KEY (`id`)
)
COMMENT='铭牌检查任务表';

