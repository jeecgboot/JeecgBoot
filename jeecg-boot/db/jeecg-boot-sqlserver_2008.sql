/*
Navicat SQL Server Data Transfer

Source Server         : vmmac_sqlserver2008
Source Server Version : 105000
Source Host           : 192.168.1.200:1433
Source Database       : jeecg-boot2
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 105000
File Encoding         : 65001

Date: 2019-05-20 15:57:15
*/


-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE [dbo].[demo]
GO
CREATE TABLE [dbo].[demo] (
[id] nvarchar(50) NOT NULL ,
[name] nvarchar(30) NULL ,
[key_word] nvarchar(255) NULL ,
[punch_time] datetime2(7) NULL ,
[salary_money] decimal(10,3) NULL ,
[bonus_money] real NULL ,
[sex] nvarchar(2) NULL ,
[age] int NULL ,
[birthday] date NULL ,
[email] nvarchar(50) NULL ,
[content] nvarchar(1000) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[sys_org_code] nvarchar(64) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'key_word')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'关键词'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'key_word'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'关键词'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'key_word'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'punch_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'打卡时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'punch_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'打卡时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'punch_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'salary_money')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'工资'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'salary_money'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'工资'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'salary_money'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'bonus_money')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'奖金'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'bonus_money'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'奖金'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'bonus_money'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'sex')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'性别 {男:1,女:2}'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'sex'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'性别 {男:1,女:2}'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'sex'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'age')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'年龄'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'age'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'年龄'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'age'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'birthday')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'生日'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'birthday'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'生日'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'birthday'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'email')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'邮箱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'email'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'邮箱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'email'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'个人简介'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'个人简介'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'demo', 
'COLUMN', N'sys_org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'所属部门编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'sys_org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'所属部门编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'demo'
, @level2type = 'COLUMN', @level2name = N'sys_org_code'
GO

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'08375a2dff80e821d5a158dd98302b23', N'导入小虎', null, null, null, null, N'2', N'28', null, null, null, N'jeecg-boot', N'2019-04-10 11:42:57.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'1c2ba51b29a42d9de02bbd708ea8121a', N'777777', N'777', N'2018-12-07 19:43:17.0000000', null, null, null, N'7', N'2018-12-07', null, null, null, null, N'admin', N'2019-02-21 18:26:04.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'1dc29e80be14d1400f165b5c6b30c707', N'zhang daihao', null, null, null, null, N'2', null, null, N'zhangdaiscott@163.com', null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'304e651dc769d5c9b6e08fb30457a602', N'小白兔', null, null, null, null, N'2', N'28', null, null, null, N'scott', N'2019-01-19 13:12:53.0000000', N'qinfeng', N'2019-01-19 13:13:12.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'4', N'Sandy', N'开源，很好', N'2018-12-15 00:00:00.0000000', null, null, N'2', N'21', N'2018-12-15', N'test4@baomidou.com', N'聪明00', null, null, N'admin', N'2019-02-25 16:29:27.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'42c08b1a2e5b2a96ffa4cc88383d4b11', N'秦50090', null, N'2019-01-05 20:33:31.0000000', null, null, null, N'28', N'2019-01-05', null, null, N'admin', N'2019-01-19 20:33:54.0000000', N'admin', N'2019-01-19 20:34:29.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'4436302a0de50bb83025286bc414d6a9', N'zhang daihao', null, null, null, null, null, null, null, N'zhangdaiscott@163.com', null, N'admin', N'2019-01-19 15:39:04.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'4981637bf71b0c1ed1365241dfcfa0ea', N'小虎', null, null, null, null, N'2', N'28', null, null, null, N'scott', N'2019-01-19 13:12:53.0000000', N'qinfeng', N'2019-01-19 13:13:12.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'5c16e6a5c31296bcd3f1053d5d118815', N'导入zhangdaiscott', null, null, null, null, N'1', null, N'2019-01-03', null, null, N'jeecg-boot', N'2019-04-10 11:42:57.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'7', N'zhangdaiscott', null, null, null, null, N'1', null, N'2019-01-03', null, null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'73bc58611012617ca446d8999379e4ac', N'郭靖11a', N'777', N'2018-12-07 00:00:00.0000000', null, null, null, null, null, null, null, N'jeecg-boot', N'2019-03-28 18:16:39.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'917e240eaa0b1b2d198ae869b64a81c3', N'zhang daihao', null, null, null, null, N'2', N'0', N'2018-11-29', N'zhangdaiscott@163.com', null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'94420c5d8fc4420dde1e7196154b3a24', N'秦111', null, null, null, null, null, null, null, null, null, N'scott', N'2019-01-19 12:54:58.0000000', N'qinfeng', N'2019-01-19 13:12:10.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'95740656751c5f22e5932ab0ae33b1e4', N'杨康22a', N'奸臣', null, null, null, null, null, null, null, null, N'jeecg-boot', N'2019-03-28 18:16:39.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'b86897900c770503771c7bb88e5d1e9b', N'scott1', N'开源、很好、hello', null, null, null, N'1', null, null, N'zhangdaiscott@163.com', null, N'scott', N'2019-01-19 12:22:34.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'c0b7c3de7c62a295ab715943de8a315d', N'秦风555', null, null, null, null, null, null, null, null, null, N'admin', N'2019-01-19 13:18:30.0000000', N'admin', N'2019-01-19 13:18:50.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'c28fa8391ef81d6fabd8bd894a7615aa', N'小麦', null, null, null, null, N'2', null, null, N'zhangdaiscott@163.com', null, N'jeecg-boot', N'2019-04-04 17:18:09.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'c2c0d49e3c01913067cf8d1fb3c971d2', N'zhang daihao', null, null, null, null, N'2', null, null, N'zhangdaiscott@163.com', null, N'admin', N'2019-01-19 23:37:18.0000000', N'admin', N'2019-01-21 16:49:06.0000000', null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'c96279c666b4b82e3ef1e4e2978701ce', N'报名时间', null, null, null, null, null, null, null, null, null, N'jeecg-boot', N'2019-03-28 18:00:52.0000000', null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'd24668721446e8478eeeafe4db66dcff', N'zhang daihao999', null, null, null, null, N'1', null, null, N'zhangdaiscott@163.com', null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'eaa6c1116b41dc10a94eae34cf990133', N'zhang daihao', null, null, null, null, null, null, null, N'zhangdaiscott@163.com', null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[demo] ([id], [name], [key_word], [punch_time], [salary_money], [bonus_money], [sex], [age], [birthday], [email], [content], [create_by], [create_time], [update_by], [update_time], [sys_org_code]) VALUES (N'ffa9da1ad40632dfcabac51d766865bd', N'秦999', null, null, null, null, null, null, null, null, null, N'admin', N'2019-01-19 23:36:34.0000000', N'admin', N'2019-02-14 17:30:43.0000000', null)
GO
GO

-- ----------------------------
-- Table structure for jeecg_monthly_growth_analysis
-- ----------------------------
DROP TABLE [dbo].[jeecg_monthly_growth_analysis]
GO
CREATE TABLE [dbo].[jeecg_monthly_growth_analysis] (
[id] int NOT NULL ,
[year] nvarchar(50) NULL ,
[month] nvarchar(50) NULL ,
[main_income] decimal(18,2) NULL ,
[other_income] decimal(18,2) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_monthly_growth_analysis', 
'COLUMN', N'month')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'月份'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'month'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'月份'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'month'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_monthly_growth_analysis', 
'COLUMN', N'main_income')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'佣金/主营收入'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'main_income'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'佣金/主营收入'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'main_income'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_monthly_growth_analysis', 
'COLUMN', N'other_income')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'其他收入'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'other_income'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'其他收入'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_monthly_growth_analysis'
, @level2type = 'COLUMN', @level2name = N'other_income'
GO

-- ----------------------------
-- Records of jeecg_monthly_growth_analysis
-- ----------------------------
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'1', N'2018', N'1月', N'114758.90', N'4426054.19')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'2', N'2018', N'2月', N'8970734.12', N'1230188.67')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'3', N'2018', N'3月', N'26755421.23', N'2048836.84')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'4', N'2018', N'4月', N'2404990.63', N'374171.44')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'5', N'2018', N'5月', N'5450793.02', N'502306.10')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'6', N'2018', N'6月', N'17186212.11', N'1375154.97')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'7', N'2018', N'7月', N'579975.67', N'461483.99')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'8', N'2018', N'8月', N'1393590.06', N'330403.76')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'9', N'2018', N'9月', N'735761.21', N'1647474.92')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'10', N'2018', N'10月', N'1670442.44', N'3423368.33')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'11', N'2018', N'11月', N'2993130.34', N'3552024.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'12', N'2018', N'12月', N'4206227.26', N'3645614.92')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'13', N'2019', N'1月', N'483834.66', N'418046.77')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'14', N'2019', N'2月', N'11666578.65', N'731352.20')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'15', N'2019', N'3月', N'27080982.08', N'1878538.81')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'16', N'2019', N'4月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'17', N'2019', N'5月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'18', N'2019', N'6月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'19', N'2019', N'7月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'20', N'2019', N'8月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'21', N'2019', N'9月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'22', N'2019', N'10月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'23', N'2019', N'11月', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_monthly_growth_analysis] ([id], [year], [month], [main_income], [other_income]) VALUES (N'24', N'2019', N'12月', N'.00', N'.00')
GO
GO

-- ----------------------------
-- Table structure for jeecg_order_customer
-- ----------------------------
DROP TABLE [dbo].[jeecg_order_customer]
GO
CREATE TABLE [dbo].[jeecg_order_customer] (
[id] nvarchar(32) NOT NULL ,
[name] nvarchar(100) NOT NULL ,
[sex] nvarchar(4) NULL ,
[idcard] nvarchar(18) NULL ,
[idcard_pic] nvarchar(500) NULL ,
[telphone] nvarchar(32) NULL ,
[order_id] nvarchar(32) NOT NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'客户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'客户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'sex')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'性别'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'sex'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'性别'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'sex'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'idcard')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'身份证号码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'idcard'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'身份证号码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'idcard'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'idcard_pic')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'身份证扫描件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'idcard_pic'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'身份证扫描件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'idcard_pic'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'telphone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'电话1'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'telphone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'电话1'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'telphone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'外键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'外键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_customer', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_customer'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of jeecg_order_customer
-- ----------------------------
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15538561502720', N'3333', N'1', N'', null, N'', N'0d4a2e67b538ee1bc881e5ed34f670f0', N'jeecg-boot', N'2019-03-29 18:42:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15538561512681', N'3332333', N'2', N'', null, N'', N'0d4a2e67b538ee1bc881e5ed34f670f0', N'jeecg-boot', N'2019-03-29 18:42:55.0000000', N'admin', N'2019-03-29 18:43:12.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15538561550142', N'4442', N'2', N'', null, N'', N'0d4a2e67b538ee1bc881e5ed34f670f0', N'jeecg-boot', N'2019-03-29 18:42:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541168497342', N'444', N'', N'', N'', N'', N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541168499553', N'5555', N'', N'', N'', N'', N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169272690', N'小王1', N'1', N'', N'', N'18611788525', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169288141', N'效力1', N'1', N'', N'', N'18611788525', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169441372', N'小红1', N'1', N'', N'', N'18611788525', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695362380', N'1111', N'', N'', N'', N'', N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695397221', N'222', N'', N'', N'', N'', N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695398992', N'333', N'', N'', N'', N'', N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'18dc5eb1068ccdfe90e358951ca1a3d6', N'dr2', N'', N'', N'', N'', N'8ab1186410a65118c4d746eb085d3bed', N'admin', N'2019-04-04 17:25:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'195d280490fe88ca1475512ddcaf2af9', N'12', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'217a2bf83709775d2cd85bf598392327', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'22bc052ae53ed09913b946abba93fa89', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'23bafeae88126c3bf3322a29a04f0d5e', N'x秦风', null, null, null, null, N'163e2efcbc6d7d54eb3f8a137da8a75a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'25c4a552c6843f36fad6303bfa99a382', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2d32144e2bee63264f3f16215c258381', N'33333', N'2', null, null, null, N'd908bfee3377e946e59220c4a4eb414a', N'admin', N'2019-04-01 16:27:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2d43170d6327f941bd1a017999495e25', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2e5f62a8b6e0a0ce19b52a6feae23d48', N'3', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'313abf99558ac5f13ecca3b87e562ad1', N'scott', N'2', null, null, null, N'b190737bd04cca8360e6f87c9ef9ec4e', N'admin', N'2019-02-25 16:29:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'34a1c5cf6cee360ed610ed0bed70e0f9', N'导入秦风', null, null, null, null, N'a2cce75872cc8fcc47f78de9ffd378c2', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3c87400f8109b4cf43c5598f0d40e34d', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'40964bcbbecb38e5ac15e6d08cf3cd43', N'233', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'41e3dee0b0b6e6530eccb7fbb22fd7a3', N'4555', N'1', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4808ae8344c7679a4a2f461db5dc3a70', N'44', N'1', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4b6cef12f195fad94d57279b2241770d', N'dr12', N'', N'', N'', N'', N'8ab1186410a65118c4d746eb085d3bed', N'admin', N'2019-04-04 17:25:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'524e695283f8e8c256cc24f39d6d8542', N'小王', N'2', N'370285198604033222', null, N'18611788674', N'eb13ab35d2946a2b0cfe3452bca1e73f', N'admin', N'2019-02-25 16:29:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'57c2a8367db34016114cbc9fa368dba0', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5df36a1608b8c7ac99ad9bc408fe54bf', N'4', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6b694e9ba54bb289ae9cc499e40031e7', N'x秦风', N'1', null, null, null, N'b190737bd04cca8360e6f87c9ef9ec4e', N'admin', N'2019-02-25 16:29:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6c6fd2716c2dcd044ed03c2c95d261f8', N'李四', N'2', N'370285198602058833', N'', N'18611788676', N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'742d008214dee0afff2145555692973e', N'秦风', N'1', N'370285198602058822', null, N'18611788676', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7469c3e5d371767ff90a739d297689b5', N'导入秦风', N'2', null, null, null, N'3a867ebf2cebce9bae3f79676d8d86f3', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', N'admin', N'2019-04-08 17:35:02.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7a96e2c7b24847d4a29940dbc0eda6e5', N'drscott', null, null, null, null, N'e73434dad84ebdce2d4e0c2a2f06d8ea', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7f5a40818e225ee18bda6da7932ac5f9', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8011575abfd7c8085e71ff66df1124b9', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8404f31d7196221a573c9bd6c8f15003', N'小张', N'1', N'370285198602058211', null, N'18611788676', N'eb13ab35d2946a2b0cfe3452bca1e73f', N'admin', N'2019-02-25 16:29:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'859020e10a2f721f201cdbff78cf7b9f', N'scott', null, null, null, null, N'163e2efcbc6d7d54eb3f8a137da8a75a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8cc3c4d26e3060975df3a2adb781eeb4', N'dr33', null, null, null, null, N'b2feb454e43c46b2038768899061e464', N'jeecg-boot', N'2019-04-04 17:23:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8d1725c23a6a50685ff0dedfd437030d', N'4', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'933cae3a79f60a93922d59aace5346ce', N'小王', null, N'370285198604033222', null, N'18611788674', N'6a719071a29927a14f19482f8693d69a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9bdb5400b709ba4eaf3444de475880d7', N'dr22', null, null, null, null, N'22c17790dcd04b296c4a2a089f71895f', N'jeecg-boot', N'2019-04-04 17:23:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9f87677f70e5f864679314389443a3eb', N'33', N'2', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2c2b7101f75c02deb328ba777137897', N'44', N'2', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ab4d002dc552c326147e318c87d3bed4', N'ddddd', N'1', N'370285198604033222', null, N'18611755848', N'9a57c850e4f68cf94ef7d8585dbaf7e6', N'admin', N'2019-04-04 17:30:47.0000000', N'admin', N'2019-04-04 17:31:17.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ad116f722a438e5f23095a0b5fcc8e89', N'dr秦风', null, null, null, null, N'e73434dad84ebdce2d4e0c2a2f06d8ea', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b1ba147b75f5eaa48212586097fc3fd1', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b43bf432c251f0e6b206e403b8ec29bc', N'lisi', null, null, null, null, N'f8889aaef6d1bccffd98d2889c0aafb5', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bcdd300a7d44c45a66bdaac14903c801', N'33', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'beb983293e47e2dc1a9b3d649aa3eb34', N'ddd3', null, null, null, null, N'd908bfee3377e946e59220c4a4eb414a', N'admin', N'2019-04-01 16:27:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c219808196406f1b8c7f1062589de4b5', N'44', N'1', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c8ed061d4b27c0c7a64e100f2b1c8ab5', N'张经理', N'2', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cc5de4af7f06cd6d250965ebe92a0395', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cf8817bd703bf7c7c77a2118edc26cc7', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd72b26fae42e71270fce2097a88da58a', N'导入scott', null, N'www', null, null, N'3a867ebf2cebce9bae3f79676d8d86f3', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', N'admin', N'2019-04-08 17:35:05.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dbdc60a6ac1a8c43f24afee384039b68', N'xiaowang', null, null, null, null, N'f8889aaef6d1bccffd98d2889c0aafb5', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dc5883b50466de94d900919ed96d97af', N'33', N'1', N'370285198602058823', null, N'18611788674', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'deeb73e553ad8dc0a0b3cfd5a338de8e', N'3333', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e2570278bf189ac05df3673231326f47', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e39cb23bb950b2bdedfc284686c6128a', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e46fe9111a9100844af582a18a2aa402', N'1', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ee7af0acb9beb9bf8d8b3819a8a7fdc3', N'2', null, null, null, null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f5d2605e844192d9e548f9bd240ac908', N'小张', null, N'370285198602058211', null, N'18611788676', N'6a719071a29927a14f19482f8693d69a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_customer] ([id], [name], [sex], [idcard], [idcard_pic], [telphone], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f6db6547382126613a3e46e7cd58a5f2', N'导入scott', null, null, null, null, N'a2cce75872cc8fcc47f78de9ffd378c2', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO

-- ----------------------------
-- Table structure for jeecg_order_main
-- ----------------------------
DROP TABLE [dbo].[jeecg_order_main]
GO
CREATE TABLE [dbo].[jeecg_order_main] (
[id] nvarchar(32) NOT NULL ,
[order_code] nvarchar(50) NULL ,
[ctype] nvarchar(500) NULL ,
[order_date] datetime2(7) NULL ,
[order_money] real NULL ,
[content] nvarchar(500) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'order_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'订单号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'订单号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'ctype')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'订单类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'ctype'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'订单类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'ctype'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'order_date')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'订单日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_date'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'订单日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_date'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'order_money')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'订单金额'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_money'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'订单金额'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'order_money'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'订单备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'订单备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_main', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_main'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of jeecg_order_main
-- ----------------------------
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'163e2efcbc6d7d54eb3f8a137da8a75a', N'B100', null, null, N'3000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3a867ebf2cebce9bae3f79676d8d86f3', N'导入B100', N'2222', null, N'3000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', N'admin', N'2019-04-08 17:35:13.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4bca3ea6881d39dbf67ef1e42c649766', N'1212', null, null, null, null, N'admin', N'2019-04-03 10:55:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4cba137333127e8e31df7ad168cc3732', N'青岛订单A0001', N'2', N'2019-04-03 10:56:07.0000000', null, null, N'admin', N'2019-04-03 10:56:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'54e739bef5b67569c963c38da52581ec', N'NC911', N'1', N'2019-02-18 09:58:51.0000000', N'40', null, N'admin', N'2019-02-18 09:58:47.0000000', N'admin', N'2019-02-18 09:58:59.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5d6e2b9e44037526270b6206196f6689', N'N333', null, N'2019-04-04 17:19:11.0000000', null, N'聪明00', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6a719071a29927a14f19482f8693d69a', N'c100', null, null, N'5000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8ab1186410a65118c4d746eb085d3bed', N'导入400', N'1', N'2019-02-18 09:58:51.0000000', N'40', null, N'admin', N'2019-02-18 09:58:47.0000000', N'admin', N'2019-02-18 09:58:59.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9a57c850e4f68cf94ef7d8585dbaf7e6', N'halou100dd', null, N'2019-04-04 17:30:32.0000000', null, null, N'admin', N'2019-04-04 17:30:41.0000000', N'admin', N'2019-04-04 17:31:08.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2cce75872cc8fcc47f78de9ffd378c2', N'导入B100', null, null, N'3000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b190737bd04cca8360e6f87c9ef9ec4e', N'B0018888', N'1', null, null, null, N'admin', N'2019-02-15 18:39:29.0000000', N'admin', N'2019-02-15 18:39:37.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd908bfee3377e946e59220c4a4eb414a', N'SSSS001', null, null, N'599', null, N'admin', N'2019-04-01 15:43:03.0000000', N'admin', N'2019-04-01 16:26:52.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e73434dad84ebdce2d4e0c2a2f06d8ea', N'导入200', null, null, N'3000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eb13ab35d2946a2b0cfe3452bca1e73f', N'BJ9980', N'1', null, N'90', null, N'admin', N'2019-02-16 17:36:42.0000000', N'admin', N'2019-02-16 17:46:16.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f618a85b17e2c4dd58d268220c8dd9a1', N'N001', null, N'2019-04-01 19:09:02.0000000', N'2222', null, N'admin', N'2019-04-01 19:09:47.0000000', N'admin', N'2019-04-01 19:10:00.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f71f7f8930b5b6b1703d9948d189982b', N'BY911', null, N'2019-04-06 19:08:39.0000000', null, null, N'admin', N'2019-04-01 16:36:02.0000000', N'admin', N'2019-04-01 16:36:08.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f8889aaef6d1bccffd98d2889c0aafb5', N'A100', null, N'2018-10-10 00:00:00.0000000', N'6000', null, N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_main] ([id], [order_code], [ctype], [order_date], [order_money], [content], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fe81ee5d19bbf9eef2066d4f29dfbe0f', N'uuuu', null, null, null, null, N'jeecg-boot', N'2019-04-03 11:00:39.0000000', null, null)
GO
GO

-- ----------------------------
-- Table structure for jeecg_order_ticket
-- ----------------------------
DROP TABLE [dbo].[jeecg_order_ticket]
GO
CREATE TABLE [dbo].[jeecg_order_ticket] (
[id] nvarchar(32) NOT NULL ,
[ticket_code] nvarchar(100) NOT NULL ,
[tickect_date] datetime2(7) NULL ,
[order_id] nvarchar(32) NOT NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'ticket_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'航班号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'ticket_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'航班号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'ticket_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'tickect_date')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'航班时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'tickect_date'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'航班时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'tickect_date'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'外键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'外键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_order_ticket', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_order_ticket'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of jeecg_order_ticket
-- ----------------------------
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0f0e3a40a215958f807eea08a6e1ac0a', N'88', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0fa3bd0bbcf53650c0bb3c0cac6d8cb7', N'ffff', N'2019-02-21 00:00:00.0000000', N'eb13ab35d2946a2b0cfe3452bca1e73f', N'admin', N'2019-02-25 16:29:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'14221afb4f5f749c1deef26ac56fdac3', N'33', N'2019-03-09 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15538561502730', N'222', null, N'0d4a2e67b538ee1bc881e5ed34f670f0', N'jeecg-boot', N'2019-03-29 18:42:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15538561526461', N'2244', N'2019-03-29 00:00:00.0000000', N'0d4a2e67b538ee1bc881e5ed34f670f0', N'jeecg-boot', N'2019-03-29 18:42:55.0000000', N'admin', N'2019-03-29 18:43:26.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541168478913', N'hhhhh', null, N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169272810', N'22211', N'2019-04-01 19:09:40.0000000', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169302331', N'333311', N'2019-04-01 19:09:40.0000000', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15541169713092', N'333311', N'2019-04-01 19:09:47.0000000', N'f618a85b17e2c4dd58d268220c8dd9a1', N'admin', N'2019-04-01 19:10:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15542604293170', N'c', null, N'fe81ee5d19bbf9eef2066d4f29dfbe0f', N'jeecg-boot', N'2019-04-03 11:00:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15542604374431', N'd', null, N'fe81ee5d19bbf9eef2066d4f29dfbe0f', N'jeecg-boot', N'2019-04-03 11:00:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695362380', N'ccc2', null, N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695381291', N'cccc1', null, N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15543695740352', N'dddd', null, N'5d6e2b9e44037526270b6206196f6689', N'admin', N'2019-04-04 17:19:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'18905bc89ee3851805aab38ed3b505ec', N'44', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1f809cbd26f4e574697e1c10de575d72', N'A100', null, N'e73434dad84ebdce2d4e0c2a2f06d8ea', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21051adb51529bdaa8798b5a3dd7f7f7', N'C10029', N'2019-02-20 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'269576e766b917f8b6509a2bb0c4d4bd', N'A100', null, N'163e2efcbc6d7d54eb3f8a137da8a75a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2d473ffc79e5b38a17919e15f8b7078e', N'66', N'2019-03-29 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3655b66fca5fef9c6aac6d70182ffda2', N'AA123', N'2019-04-01 00:00:00.0000000', N'd908bfee3377e946e59220c4a4eb414a', N'admin', N'2019-04-01 16:27:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'365d5919155473ade45840fd626c51a9', N'dddd', N'2019-04-04 17:25:29.0000000', N'8ab1186410a65118c4d746eb085d3bed', N'admin', N'2019-04-04 17:25:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4889a782e78706ab4306a925cfb163a5', N'C34', N'2019-04-01 00:00:00.0000000', N'd908bfee3377e946e59220c4a4eb414a', N'admin', N'2019-04-01 16:35:00.0000000', N'admin', N'2019-04-01 16:35:07.0000000')
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'48d385796382cf87fa4bdf13b42d9a28', N'导入A100', null, N'3a867ebf2cebce9bae3f79676d8d86f3', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'541faed56efbeb4be9df581bd8264d3a', N'88', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'57a27a7dfd6a48e7d981f300c181b355', N'6', N'2019-03-30 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5ce4dc439c874266e42e6c0ff8dc8b5c', N'导入A100', null, N'a2cce75872cc8fcc47f78de9ffd378c2', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5f16e6a64ab22a161bd94cc205f2c662', N'222', N'2019-02-23 00:00:00.0000000', N'b190737bd04cca8360e6f87c9ef9ec4e', N'admin', N'2019-02-25 16:29:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'645a06152998a576c051474157625c41', N'88', N'2019-04-04 17:25:31.0000000', N'8ab1186410a65118c4d746eb085d3bed', N'admin', N'2019-04-04 17:25:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6e3562f2571ea9e96b2d24497b5f5eec', N'55', N'2019-03-23 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8fd2b389151568738b1cc4d8e27a6110', N'导入A100', null, N'a2cce75872cc8fcc47f78de9ffd378c2', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'93f1a84053e546f59137432ff5564cac', N'55', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'969ddc5d2e198d50903686917f996470', N'A10029', N'2019-04-01 00:00:00.0000000', N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'96e7303a8d22a5c384e08d7bcf7ac2bf', N'A100', null, N'e73434dad84ebdce2d4e0c2a2f06d8ea', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9e8a3336f6c63f558f2b68ce2e1e666e', N'dddd', null, N'9a57c850e4f68cf94ef7d8585dbaf7e6', N'admin', N'2019-04-04 17:30:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a28db02c810c65660015095cb81ed434', N'A100', null, N'f8889aaef6d1bccffd98d2889c0aafb5', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b217bb0e4ec6a45b6cbf6db880060c0f', N'A100', null, N'6a719071a29927a14f19482f8693d69a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ba708df70bb2652ed1051a394cfa0bb3', N'333', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'beabbfcb195d39bedeeafe8318794562', N'A1345', N'2019-04-01 00:00:00.0000000', N'd908bfee3377e946e59220c4a4eb414a', N'admin', N'2019-04-01 16:27:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bf450223cb505f89078a311ef7b6ed16', N'777', N'2019-03-30 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c06165b6603e3e1335db187b3c841eef', N'fff', null, N'9a57c850e4f68cf94ef7d8585dbaf7e6', N'admin', N'2019-04-04 17:30:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c113136abc26ace3a6da4e41d7dc1c7e', N'44', N'2019-03-15 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c1abdc2e30aeb25de13ad6ee3488ac24', N'77', N'2019-03-22 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c23751a7deb44f553ce50a94948c042a', N'33', N'2019-03-09 00:00:00.0000000', N'8ab1186410a65118c4d746eb085d3bed', N'admin', N'2019-04-04 17:25:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c64547666b634b3d6a0feedcf05f25ce', N'C10019', N'2019-04-01 00:00:00.0000000', N'f71f7f8930b5b6b1703d9948d189982b', N'admin', N'2019-04-01 19:08:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c8b8d3217f37da78dddf711a1f7da485', N'A100', null, N'163e2efcbc6d7d54eb3f8a137da8a75a', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cab691c1c1ff7a6dfd7248421917fd3c', N'A100', null, N'f8889aaef6d1bccffd98d2889c0aafb5', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cca10a9a850b456d9b72be87da7b0883', N'77', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd2fbba11f4814d9b1d3cb1a3f342234a', N'C10019', N'2019-02-18 00:00:00.0000000', N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd746c1ed956a562e97eef9c6faf94efa', N'111', N'2019-02-01 00:00:00.0000000', N'b190737bd04cca8360e6f87c9ef9ec4e', N'admin', N'2019-02-25 16:29:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dbdb07a16826808e4276e84b2aa4731a', N'导入A100', null, N'3a867ebf2cebce9bae3f79676d8d86f3', N'jeecg-boot', N'2019-03-29 18:43:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e7075639c37513afc0bbc4bf7b5d98b9', N'88', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa759dc104d0371f8aa28665b323dab6', N'888', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[jeecg_order_ticket] ([id], [ticket_code], [tickect_date], [order_id], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ff197da84a9a3af53878eddc91afbb2e', N'33', null, N'54e739bef5b67569c963c38da52581ec', N'admin', N'2019-03-15 16:50:15.0000000', null, null)
GO
GO

-- ----------------------------
-- Table structure for jeecg_project_nature_income
-- ----------------------------
DROP TABLE [dbo].[jeecg_project_nature_income]
GO
CREATE TABLE [dbo].[jeecg_project_nature_income] (
[id] int NOT NULL ,
[nature] nvarchar(50) NOT NULL ,
[insurance_fee] decimal(18,2) NULL ,
[risk_consulting_fee] decimal(18,2) NULL ,
[evaluation_fee] decimal(18,2) NULL ,
[insurance_evaluation_fee] decimal(18,2) NULL ,
[bidding_consulting_fee] decimal(18,2) NULL ,
[interol_consulting_fee] decimal(18,2) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'nature')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'项目性质'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'nature'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'项目性质'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'nature'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'insurance_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'保险经纪佣金费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'insurance_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'保险经纪佣金费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'insurance_fee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'risk_consulting_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'风险咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'risk_consulting_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'风险咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'risk_consulting_fee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'evaluation_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'承保公估评估费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'evaluation_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'承保公估评估费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'evaluation_fee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'insurance_evaluation_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'保险公估费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'insurance_evaluation_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'保险公估费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'insurance_evaluation_fee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'bidding_consulting_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'投标咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'bidding_consulting_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'投标咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'bidding_consulting_fee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'jeecg_project_nature_income', 
'COLUMN', N'interol_consulting_fee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'内控咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'interol_consulting_fee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'内控咨询费'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'jeecg_project_nature_income'
, @level2type = 'COLUMN', @level2name = N'interol_consulting_fee'
GO

-- ----------------------------
-- Records of jeecg_project_nature_income
-- ----------------------------
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'1', N'市场化-电商业务', N'4865.41', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'2', N'统筹型', N'35767081.88', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'3', N'市场化-非股东', N'1487045.35', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'4', N'市场化-参控股', N'382690.56', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'5', N'市场化-员工福利', N'256684.91', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'6', N'市场化-再保险', N'563451.03', N'.00', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'7', N'市场化-海外业务', N'760576.25', N'770458.75', N'.00', N'.00', N'.00', N'.00')
GO
GO
INSERT INTO [dbo].[jeecg_project_nature_income] ([id], [nature], [insurance_fee], [risk_consulting_fee], [evaluation_fee], [insurance_evaluation_fee], [bidding_consulting_fee], [interol_consulting_fee]) VALUES (N'8', N'市场化-风险咨询', N'910183.93', N'.00', N'.00', N'.00', N'.00', N'226415.09')
GO
GO

-- ----------------------------
-- Table structure for joa_demo
-- ----------------------------
DROP TABLE [dbo].[joa_demo]
GO
CREATE TABLE [dbo].[joa_demo] (
[id] nvarchar(32) NULL ,
[name] nvarchar(100) NULL ,
[days] int NULL ,
[begin_date] datetime2(7) NULL ,
[end_date] datetime2(7) NULL ,
[reason] nvarchar(500) NULL ,
[bpm_status] nvarchar(50) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'流程测试'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'流程测试'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'days')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假天数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'days'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假天数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'days'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'begin_date')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'begin_date'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'begin_date'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'end_date')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'end_date'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'end_date'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'reason')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'reason'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'reason'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'bpm_status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'流程状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'bpm_status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'流程状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'bpm_status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'joa_demo', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'joa_demo'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO

-- ----------------------------
-- Records of joa_demo
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_BLOB_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_BLOB_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[BLOB_DATA] image NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_CALENDARS]
GO
CREATE TABLE [dbo].[QRTZ_CALENDARS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[CALENDAR_NAME] varchar(200) NOT NULL ,
[CALENDAR] image NOT NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_CRON_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_CRON_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[CRON_EXPRESSION] varchar(120) NOT NULL ,
[TIME_ZONE_ID] varchar(80) NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_FIRED_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_FIRED_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[ENTRY_ID] varchar(95) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[INSTANCE_NAME] varchar(200) NOT NULL ,
[FIRED_TIME] bigint NOT NULL ,
[SCHED_TIME] bigint NOT NULL ,
[PRIORITY] int NOT NULL ,
[STATE] varchar(16) NOT NULL ,
[JOB_NAME] varchar(200) NULL ,
[JOB_GROUP] varchar(200) NULL ,
[IS_NONCONCURRENT] varchar(1) NULL ,
[REQUESTS_RECOVERY] varchar(1) NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_JOB_DETAILS]
GO
CREATE TABLE [dbo].[QRTZ_JOB_DETAILS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[JOB_NAME] varchar(200) NOT NULL ,
[JOB_GROUP] varchar(200) NOT NULL ,
[DESCRIPTION] varchar(250) NULL ,
[JOB_CLASS_NAME] varchar(250) NOT NULL ,
[IS_DURABLE] varchar(1) NOT NULL ,
[IS_NONCONCURRENT] varchar(1) NOT NULL ,
[IS_UPDATE_DATA] varchar(1) NOT NULL ,
[REQUESTS_RECOVERY] varchar(1) NOT NULL ,
[JOB_DATA] image NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_LOCKS]
GO
CREATE TABLE [dbo].[QRTZ_LOCKS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[LOCK_NAME] varchar(40) NOT NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO [dbo].[QRTZ_LOCKS] ([SCHED_NAME], [LOCK_NAME]) VALUES (N'quartzScheduler', N'TRIGGER_ACCESS')
GO
GO

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS]
GO
CREATE TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE [dbo].[QRTZ_SCHEDULER_STATE]
GO
CREATE TABLE [dbo].[QRTZ_SCHEDULER_STATE] (
[SCHED_NAME] varchar(120) NOT NULL ,
[INSTANCE_NAME] varchar(200) NOT NULL ,
[LAST_CHECKIN_TIME] bigint NOT NULL ,
[CHECKIN_INTERVAL] bigint NOT NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[REPEAT_COUNT] bigint NOT NULL ,
[REPEAT_INTERVAL] bigint NOT NULL ,
[TIMES_TRIGGERED] bigint NOT NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[STR_PROP_1] varchar(512) NULL ,
[STR_PROP_2] varchar(512) NULL ,
[STR_PROP_3] varchar(512) NULL ,
[INT_PROP_1] int NULL ,
[INT_PROP_2] int NULL ,
[LONG_PROP_1] bigint NULL ,
[LONG_PROP_2] bigint NULL ,
[DEC_PROP_1] numeric(13,4) NULL ,
[DEC_PROP_2] numeric(13,4) NULL ,
[BOOL_PROP_1] varchar(1) NULL ,
[BOOL_PROP_2] varchar(1) NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE [dbo].[QRTZ_TRIGGERS]
GO
CREATE TABLE [dbo].[QRTZ_TRIGGERS] (
[SCHED_NAME] varchar(120) NOT NULL ,
[TRIGGER_NAME] varchar(200) NOT NULL ,
[TRIGGER_GROUP] varchar(200) NOT NULL ,
[JOB_NAME] varchar(200) NOT NULL ,
[JOB_GROUP] varchar(200) NOT NULL ,
[DESCRIPTION] varchar(250) NULL ,
[NEXT_FIRE_TIME] bigint NULL ,
[PREV_FIRE_TIME] bigint NULL ,
[PRIORITY] int NULL ,
[TRIGGER_STATE] varchar(16) NOT NULL ,
[TRIGGER_TYPE] varchar(8) NOT NULL ,
[START_TIME] bigint NOT NULL ,
[END_TIME] bigint NULL ,
[CALENDAR_NAME] varchar(200) NULL ,
[MISFIRE_INSTR] smallint NULL ,
[JOB_DATA] image NULL 
)


GO

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE [dbo].[sys_announcement]
GO
CREATE TABLE [dbo].[sys_announcement] (
[id] nvarchar(32) NOT NULL ,
[titile] nvarchar(100) NULL ,
[msg_content] nvarchar(MAX) NULL ,
[start_time] datetime2(7) NULL ,
[end_time] datetime2(7) NULL ,
[sender] nvarchar(100) NULL ,
[priority] nvarchar(255) NULL ,
[msg_category] nvarchar(10) NOT NULL ,
[msg_type] nvarchar(10) NULL ,
[send_status] nvarchar(10) NULL ,
[send_time] datetime2(7) NULL ,
[cancel_time] datetime2(7) NULL ,
[del_flag] nvarchar(1) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[user_ids] nvarchar(MAX) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'系统通告表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'系统通告表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'titile')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'titile'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'titile'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'msg_content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'start_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'start_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'start_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'end_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'end_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'end_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'sender')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发布人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'sender'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发布人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'sender'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'priority')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'优先级（L低，M中，H高）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'priority'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'优先级（L低，M中，H高）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'priority'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'msg_category')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'消息类型1:通知公告2:系统消息'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_category'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'消息类型1:通知公告2:系统消息'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_category'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'msg_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'通告对象类型（USER:指定用户，ALL:全体用户）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'通告对象类型（USER:指定用户，ALL:全体用户）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'msg_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'send_status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发布状态（0未发布，1已发布，2已撤销）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'send_status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发布状态（0未发布，1已发布，2已撤销）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'send_status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'send_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发布时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'send_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发布时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'send_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'cancel_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'撤销时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'cancel_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'撤销时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'cancel_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement', 
'COLUMN', N'user_ids')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'指定用户'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'user_ids'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'指定用户'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement'
, @level2type = 'COLUMN', @level2name = N'user_ids'
GO

-- ----------------------------
-- Records of sys_announcement
-- ----------------------------
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'1b714f8ebc3cc33f8b4f906103b6a18d', N'5467567', null, null, null, N'admin', null, N'2', null, N'1', N'2019-03-30 12:40:38.0000000', null, N'0', N'admin', N'2019-02-26 17:23:26.0000000', N'admin', N'2019-02-26 17:35:10.0000000', null)
GO
GO
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'3d11237ccdf62450d20bb8abdb331178', N'111222', null, null, null, null, null, N'2', null, N'0', null, null, N'1', N'admin', N'2019-03-29 17:19:47.0000000', N'admin', N'2019-03-29 17:19:50.0000000', null)
GO
GO
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'7ef04e95f8de030b1d5f7a9144090dc6', N'111', null, N'2019-02-06 17:28:10.0000000', N'2019-03-08 17:28:11.0000000', null, null, N'2', null, N'0', null, null, N'1', N'admin', N'2019-02-26 17:28:17.0000000', N'admin', N'2019-03-26 19:59:49.0000000', null)
GO
GO
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'93a9060a1c20e4bf98b3f768a02c2ff9', N'111', N'111', N'2019-02-06 17:20:17.0000000', N'2019-02-21 17:20:20.0000000', N'admin', N'M', N'2', N'ALL', N'1', N'2019-02-26 17:24:29.0000000', null, N'0', N'admin', N'2019-02-26 17:16:26.0000000', N'admin', N'2019-02-26 17:19:35.0000000', null)
GO
GO
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'de1dc57f31037079e1e55c8347fe6ef7', N'222', N'2222', N'2019-02-06 17:28:26.0000000', N'2019-02-23 17:28:28.0000000', N'admin', N'M', N'2', N'ALL', N'1', N'2019-03-29 17:19:56.0000000', null, N'1', N'admin', N'2019-02-26 17:28:36.0000000', N'admin', N'2019-02-26 17:28:40.0000000', null)
GO
GO
INSERT INTO [dbo].[sys_announcement] ([id], [titile], [msg_content], [start_time], [end_time], [sender], [priority], [msg_category], [msg_type], [send_status], [send_time], [cancel_time], [del_flag], [create_by], [create_time], [update_by], [update_time], [user_ids]) VALUES (N'e52f3eb6215f139cb2224c52517af3bd', N'334', N'334', null, null, null, null, N'2', null, N'0', null, null, N'1', N'admin', N'2019-03-30 12:40:28.0000000', N'admin', N'2019-03-30 12:40:32.0000000', null)
GO
GO

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE [dbo].[sys_announcement_send]
GO
CREATE TABLE [dbo].[sys_announcement_send] (
[id] nvarchar(32) NULL ,
[annt_id] nvarchar(32) NULL ,
[user_id] nvarchar(32) NULL ,
[read_flag] nvarchar(10) NULL ,
[read_time] datetime2(7) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户通告阅读标记表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户通告阅读标记表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'annt_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'通告ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'annt_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'通告ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'annt_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'user_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'user_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'user_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'read_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'阅读状态（0未读，1已读）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'read_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'阅读状态（0未读，1已读）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'read_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'read_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'阅读时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'read_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'阅读时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'read_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_announcement_send', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_announcement_send'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_announcement_send
-- ----------------------------
INSERT INTO [dbo].[sys_announcement_send] ([id], [annt_id], [user_id], [read_flag], [read_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'646c0c405ec643d4dc4160db2446f8ff', N'93a9060a1c20e4bf98b3f768a02c2ff9', N'e9ca23d68d884d4ebb19d07889727dae', N'0', null, N'admin', N'2019-05-17 11:50:56.0000000', null, null)
GO
GO

-- ----------------------------
-- Table structure for sys_data_log
-- ----------------------------
DROP TABLE [dbo].[sys_data_log]
GO
CREATE TABLE [dbo].[sys_data_log] (
[id] nvarchar(32) NOT NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[data_table] nvarchar(32) NULL ,
[data_id] nvarchar(32) NULL ,
[data_content] nvarchar(MAX) NULL ,
[data_version] int NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'data_table')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'表名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_table'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'表名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_table'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'data_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'数据ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'数据ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'data_content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'数据内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'数据内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_data_log', 
'COLUMN', N'data_version')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'版本号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_version'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'版本号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_data_log'
, @level2type = 'COLUMN', @level2name = N'data_version'
GO

-- ----------------------------
-- Records of sys_data_log
-- ----------------------------
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab0d198015ab12274bf0006', N'admin', N'2017-03-09 11:35:09.0000000', null, null, N'jeecg_demo', N'4028ef81550c1a7901550c1cd6e70001', N'{"mobilePhone":"","officePhone":"","email":"","createDate":"Jun 23, 2016 12:00:00 PM","sex":"1","depId":"402880e447e99cf10147e9a03b320003","userName":"9001","status":"1","content":"111","id":"4028ef81550c1a7901550c1cd6e70001"}', N'3')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab700bead0009', N'admin', N'2017-03-10 14:56:03.0000000', null, null, N'jeecg_demo', N'402880f05ab6d12b015ab700be8d0008', N'{"mobilePhone":"","officePhone":"","email":"","createDate":"Mar 10, 2017 2:56:03 PM","sex":"0","depId":"402880e447e99cf10147e9a03b320003","userName":"111","status":"0","id":"402880f05ab6d12b015ab700be8d0008"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab705a23f000d', N'admin', N'2017-03-10 15:01:24.0000000', null, null, N'jeecg_demo', N'402880f05ab6d12b015ab705a233000c', N'{"mobilePhone":"","officePhone":"11","email":"","createDate":"Mar 10, 2017 3:01:24 PM","sex":"0","depId":"402880e447e99cf10147e9a03b320003","userName":"11","status":"0","id":"402880f05ab6d12b015ab705a233000c"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab712a6420013', N'admin', N'2017-03-10 15:15:37.0000000', null, null, N'jeecg_demo', N'402880f05ab6d12b015ab712a6360012', N'{"mobilePhone":"","officePhone":"","email":"","createDate":"Mar 10, 2017 3:15:37 PM","sex":"0","depId":"402880e447e99cf10147e9a03b320003","userName":"小王","status":"0","id":"402880f05ab6d12b015ab712a6360012"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab712d0510015', N'admin', N'2017-03-10 15:15:47.0000000', null, null, N'jeecg_demo', N'402880f05ab6d12b015ab712a6360012', N'{"mobilePhone":"18611788525","officePhone":"","email":"","createDate":"Mar 10, 2017 3:15:37 AM","sex":"0","depId":"402880e447e99cf10147e9a03b320003","userName":"小王","status":"0","id":"402880f05ab6d12b015ab712a6360012"}', N'2')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab71308240018', N'admin', N'2017-03-10 15:16:02.0000000', null, null, N'jeecg_demo', N'8a8ab0b246dc81120146dc81860f016f', N'{"mobilePhone":"13111111111","officePhone":"66666666","email":"demo@jeecg.com","age":12,"salary":10.00,"birthday":"Feb 14, 2014 12:00:00 AM","sex":"1","depId":"402880e447e99cf10147e9a03b320003","userName":"小明","status":"","content":"","id":"8a8ab0b246dc81120146dc81860f016f"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'402880f05ab6d12b015ab72806c3001b', N'admin', N'2017-03-10 15:38:58.0000000', null, null, N'jeecg_demo', N'8a8ab0b246dc81120146dc81860f016f', N'{"mobilePhone":"18611788888","officePhone":"66666666","email":"demo@jeecg.com","age":12,"salary":10.00,"birthday":"Feb 14, 2014 12:00:00 AM","sex":"1","depId":"402880e447e99cf10147e9a03b320003","userName":"小明","status":"","content":"","id":"8a8ab0b246dc81120146dc81860f016f"}', N'2')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef815318148a0153181567690001', N'admin', N'2016-02-25 18:59:29.0000000', null, null, N'jeecg_demo', N'4028ef815318148a0153181566270000', N'{"mobilePhone":"13423423423","officePhone":"1","email":"","age":1,"salary":1,"birthday":"Feb 25, 2016 12:00:00 AM","createDate":"Feb 25, 2016 6:59:24 PM","depId":"402880e447e9a9570147e9b6a3be0005","userName":"1","status":"0","id":"4028ef815318148a0153181566270000"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef815318148a01531815ec5c0003', N'admin', N'2016-02-25 19:00:03.0000000', null, null, N'jeecg_demo', N'4028ef815318148a0153181566270000', N'{"mobilePhone":"13426498659","officePhone":"1","email":"","age":1,"salary":1.00,"birthday":"Feb 25, 2016 12:00:00 AM","createDate":"Feb 25, 2016 6:59:24 AM","depId":"402880e447e9a9570147e9b6a3be0005","userName":"1","status":"0","id":"4028ef815318148a0153181566270000"}', N'2')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c0502e6b0003', N'admin', N'2016-03-29 10:59:53.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0502d420002', N'{"mobilePhone":"18455477548","officePhone":"123","email":"","createDate":"Mar 29, 2016 10:59:53 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"123","status":"0","id":"4028ef8153c028db0153c0502d420002"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c0509aa40006', N'admin', N'2016-03-29 11:00:21.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0509a3e0005', N'{"mobilePhone":"13565486458","officePhone":"","email":"","createDate":"Mar 29, 2016 11:00:21 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"22","status":"0","id":"4028ef8153c028db0153c0509a3e0005"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c051c4a70008', N'admin', N'2016-03-29 11:01:37.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0509a3e0005', N'{"mobilePhone":"13565486458","officePhone":"","email":"","createDate":"Mar 29, 2016 11:00:21 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"22","status":"0","id":"4028ef8153c028db0153c0509a3e0005"}', N'2')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c051d4b5000a', N'admin', N'2016-03-29 11:01:41.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0502d420002', N'{"mobilePhone":"13565486458","officePhone":"123","email":"","createDate":"Mar 29, 2016 10:59:53 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"123","status":"0","id":"4028ef8153c028db0153c0502d420002"}', N'2')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c07033d8000d', N'admin', N'2016-03-29 11:34:52.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0502d420002', N'{"mobilePhone":"13565486458","officePhone":"123","email":"","age":23,"createDate":"Mar 29, 2016 10:59:53 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"123","status":"0","id":"4028ef8153c028db0153c0502d420002"}', N'3')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef8153c028db0153c070492e000f', N'admin', N'2016-03-29 11:34:57.0000000', null, null, N'jeecg_demo', N'4028ef8153c028db0153c0509a3e0005', N'{"mobilePhone":"13565486458","officePhone":"","email":"","age":22,"createDate":"Mar 29, 2016 11:00:21 AM","depId":"402880e447e99cf10147e9a03b320003","userName":"22","status":"0","id":"4028ef8153c028db0153c0509a3e0005"}', N'3')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef81550c1a7901550c1cd7850002', N'admin', N'2016-06-01 21:17:44.0000000', null, null, N'jeecg_demo', N'4028ef81550c1a7901550c1cd6e70001', N'{"mobilePhone":"","officePhone":"","email":"","createDate":"Jun 1, 2016 9:17:44 PM","sex":"1","depId":"402880e447e99cf10147e9a03b320003","userName":"121221","status":"0","id":"4028ef81550c1a7901550c1cd6e70001"}', N'1')
GO
GO
INSERT INTO [dbo].[sys_data_log] ([id], [create_by], [create_time], [update_by], [update_time], [data_table], [data_id], [data_content], [data_version]) VALUES (N'4028ef81568c31ec01568c3307080004', N'admin', N'2016-08-15 11:16:09.0000000', null, null, N'jeecg_demo', N'4028ef81550c1a7901550c1cd6e70001', N'{"mobilePhone":"","officePhone":"","email":"","createDate":"Jun 23, 2016 12:00:00 PM","sex":"1","depId":"402880e447e99cf10147e9a03b320003","userName":"9001","status":"1","content":"111","id":"4028ef81550c1a7901550c1cd6e70001"}', N'2')
GO
GO

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE [dbo].[sys_depart]
GO
CREATE TABLE [dbo].[sys_depart] (
[id] nvarchar(32) NOT NULL ,
[parent_id] nvarchar(32) NULL ,
[depart_name] nvarchar(100) NOT NULL ,
[depart_name_en] nvarchar(500) NULL ,
[depart_name_abbr] nvarchar(500) NULL ,
[depart_order] int NULL ,
[description] nvarchar(MAX) NULL ,
[org_type] nvarchar(10) NULL ,
[org_code] nvarchar(64) NOT NULL ,
[mobile] nvarchar(32) NULL ,
[fax] nvarchar(32) NULL ,
[address] nvarchar(100) NULL ,
[memo] nvarchar(500) NULL ,
[status] nvarchar(1) NULL ,
[del_flag] nvarchar(1) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'组织机构表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'组织机构表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'parent_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'父机构ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'parent_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'父机构ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'parent_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'depart_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'机构/部门名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'机构/部门名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'depart_name_en')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'英文名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name_en'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'英文名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name_en'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'depart_name_abbr')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name_abbr'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_name_abbr'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'depart_order')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_order'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'depart_order'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'org_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'机构类型 1一级部门 2子部门'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'org_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'机构类型 1一级部门 2子部门'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'org_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'机构编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'机构编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'org_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'mobile')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'手机号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'mobile'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'手机号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'mobile'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'fax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'传真'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'fax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'传真'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'fax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'address')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'address'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'address'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'memo')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'memo'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'memo'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'状态（1启用，0不启用）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'状态（1启用，0不启用）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_depart', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_depart'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4f1765520d6346f9bd9c79e2479e5b12', N'c6d7cb4deeac411cb3384b1b31278596', N'市场部', null, null, N'0', null, N'2', N'A01A03', null, null, null, null, null, N'0', N'admin', N'2019-02-20 17:15:34.0000000', N'admin', N'2019-02-26 16:36:18.0000000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5159cde220114246b045e574adceafe9', N'6d35e179cd814e3299bd588ea7daed3f', N'研发部', null, null, N'0', null, N'2', N'A02A02', null, null, null, N'', null, N'0', N'admin', N'2019-02-26 16:44:38.0000000', N'admin', N'2019-05-17 15:40:38.4260000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'57197590443c44f083d42ae24ef26a2c', N'c6d7cb4deeac411cb3384b1b31278596', N'研发部', null, null, N'0', null, N'2', N'A01A05', null, null, null, null, null, N'0', N'admin', N'2019-02-21 16:14:41.0000000', N'admin', N'2019-03-27 19:05:49.0000000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'67fc001af12a4f9b8458005d3f19934a', N'c6d7cb4deeac411cb3384b1b31278596', N'财务部', null, null, N'0', null, N'2', N'A01A04', null, null, null, null, null, N'0', N'admin', N'2019-02-21 16:14:35.0000000', N'admin', N'2019-02-25 12:49:41.0000000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6d35e179cd814e3299bd588ea7daed3f', N'', N'卓尔互动公司', null, null, N'0', null, N'1', N'A02', null, null, null, null, null, N'0', N'admin', N'2019-02-26 16:36:39.0000000', N'admin', N'2019-03-22 16:47:25.0000000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'743ba9dbdc114af8953a11022ef3096a', N'f28c6f53abd841ac87ead43afc483433', N'财务部', null, null, N'0', null, N'2', N'A03A01', null, null, null, null, null, N'0', N'admin', N'2019-03-22 16:45:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a7d7e77e06c84325a40932163adcdaa6', N'6d35e179cd814e3299bd588ea7daed3f', N'财务部', null, null, N'0', null, N'2', N'A02A01', null, null, null, null, null, N'0', N'admin', N'2019-02-26 16:36:47.0000000', N'admin', N'2019-02-26 16:37:25.0000000')
GO
GO
INSERT INTO [dbo].[sys_depart] ([id], [parent_id], [depart_name], [depart_name_en], [depart_name_abbr], [depart_order], [description], [org_type], [org_code], [mobile], [fax], [address], [memo], [status], [del_flag], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c6d7cb4deeac411cb3384b1b31278596', N'', N'北京国炬公司', null, null, N'0', null, N'1', N'A01', null, null, null, null, null, N'0', N'admin', N'2019-02-11 14:21:51.0000000', N'admin', N'2019-03-22 16:47:19.0000000')
GO
GO

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE [dbo].[sys_dict]
GO
CREATE TABLE [dbo].[sys_dict] (
[id] nvarchar(32) NOT NULL ,
[dict_name] nvarchar(100) NULL ,
[dict_code] nvarchar(100) NULL ,
[description] nvarchar(255) NULL ,
[del_flag] int NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[type] int NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'dict_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'dict_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'dict_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'dict_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'dict_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'dict_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict', 
'COLUMN', N'type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典类型0为string,1为number'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典类型0为string,1为number'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict'
, @level2type = 'COLUMN', @level2name = N'type'
GO

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'0b5d19e1fce4b2e6647e6b4a17760c14', N'通告类型', N'msg_category', N'消息类型1:通知公告2:系统消息', N'1', N'admin', N'2019-04-22 18:01:35.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'236e8a4baff0db8c62c00dd95632834f', N'同步工作流引擎', N'activiti_sync', N'同步工作流引擎', N'1', N'admin', N'2019-05-15 15:27:33.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'2e02df51611a4b9632828ab7e5338f00', N'权限策略', N'perms_type', N'权限策略', N'1', N'admin', N'2019-04-26 18:26:55.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'2f0320997ade5dd147c90130f7218c3e', N'推送类别', N'msg_type', N'', N'1', N'admin', N'2019-03-17 21:21:32.0000000', N'admin', N'2019-03-26 19:57:45.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'3486f32803bb953e7155dab3513dc68b', N'删除状态', N'del_flag', null, N'1', N'admin', N'2019-01-18 21:46:26.0000000', N'admin', N'2019-03-30 11:17:11.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'3d9a351be3436fbefb1307d4cfb49bf2', N'性别', N'sex', null, N'1', null, N'2019-01-04 14:56:32.0000000', N'admin', N'2019-03-30 11:28:27.0000000', N'1')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'404a04a15f371566c658ee9ef9fc392a', N'cehis2', N'22', null, N'2', N'admin', N'2019-01-30 11:17:21.0000000', N'admin', N'2019-03-30 11:18:12.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4274efc2292239b6f000b153f50823ff', N'全局权限策略', N'global_perms_type', N'全局权限策略', N'1', N'admin', N'2019-05-10 17:54:05.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4c03fca6bf1f0299c381213961566349', N'Online图表展示模板', N'online_graph_display_template', N'Online图表展示模板', N'1', N'admin', N'2019-04-12 17:28:50.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4c753b5293304e7a445fd2741b46529d', N'字典状态', N'dict_item_status', null, N'1', N'admin', N'2020-06-18 23:18:42.0000000', N'admin', N'2019-03-30 19:33:52.0000000', N'1')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4d7fec1a7799a436d26d02325eff295e', N'优先级', N'priority', N'优先级', N'1', N'admin', N'2019-03-16 17:03:34.0000000', N'admin', N'2019-04-16 17:39:23.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4e4602b3e3686f0911384e188dc7efb4', N'条件规则', N'rule_conditions', N'', N'1', N'admin', N'2019-04-01 10:15:03.0000000', N'admin', N'2019-04-01 10:30:47.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'4f69be5f507accea8d5df5f11346181a', N'发送消息类型', N'msgType', null, N'1', N'admin', N'2019-04-11 14:27:09.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'68168534ff5065a152bfab275c2136f8', N'有效无效状态', N'valid_status', N'有效无效状态', N'1', N'admin', N'2020-09-26 19:21:14.0000000', N'admin', N'2019-04-26 19:21:23.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'6b78e3f59faec1a4750acff08030a79b', N'用户类型', N'user_type', null, N'2', null, N'2019-01-04 14:59:01.0000000', N'admin', N'2019-03-18 23:28:18.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'72cce0989df68887546746d8f09811aa', N'Online表单类型', N'cgform_table_type', N'', N'1', N'admin', N'2019-01-27 10:13:02.0000000', N'admin', N'2019-03-30 11:37:36.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'78bda155fe380b1b3f175f1e88c284c6', N'流程状态', N'bpm_status', N'流程状态', N'1', N'admin', N'2019-05-09 16:31:52.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'83bfb33147013cc81640d5fd9eda030c', N'日志类型', N'log_type', null, N'1', N'admin', N'2019-03-18 23:22:19.0000000', null, null, N'1')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'845da5006c97754728bf48b6a10f79cc', N'状态', N'status', null, N'2', N'admin', N'2019-03-18 21:45:25.0000000', N'admin', N'2019-03-18 21:58:25.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'8dfe32e2d29ea9430a988b3b558bf233', N'发布状态', N'send_status', N'发布状态', N'1', N'admin', N'2019-04-16 17:40:42.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'a9d9942bd0eccb6e89de92d130ec4c4a', N'消息发送状态', N'msgSendStatus', null, N'1', N'admin', N'2019-04-12 18:18:17.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'ac2f7c0c5c5775fcea7e2387bcb22f01', N'菜单类型', N'menu_type', null, N'1', N'admin', N'2020-12-18 23:24:32.0000000', N'admin', N'2019-04-01 15:27:06.0000000', N'1')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'ad7c65ba97c20a6805d5dcdf13cdaf36', N'onlineT类型', N'ceshi_online', null, N'2', N'admin', N'2019-03-22 16:31:49.0000000', N'admin', N'2019-03-22 16:34:16.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'bd1b8bc28e65d6feefefb6f3c79f42fd', N'Online图表数据类型', N'online_graph_data_type', N'Online图表数据类型', N'1', N'admin', N'2019-04-12 17:24:24.0000000', N'admin', N'2019-04-12 17:24:57.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'c36169beb12de8a71c8683ee7c28a503', N'部门状态', N'depart_status', null, N'1', N'admin', N'2019-03-18 21:59:51.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'c5a14c75172783d72cbee6ee7f5df5d1', N'Online图表类型', N'online_graph_type', N'Online图表类型', N'1', N'admin', N'2019-04-12 17:04:06.0000000', null, null, N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'd6e1152968b02d69ff358c75b48a6ee1', N'流程类型', N'bpm_process_type', null, N'1', N'admin', N'2021-02-22 19:26:54.0000000', N'admin', N'2019-03-30 18:14:44.0000000', N'0')
GO
GO
INSERT INTO [dbo].[sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [update_by], [update_time], [type]) VALUES (N'fc6cd58fde2e8481db10d3a1e68ce70c', N'用户状态', N'user_status', null, N'1', N'admin', N'2019-03-18 21:57:25.0000000', N'admin', N'2019-03-18 23:11:58.0000000', N'1')
GO
GO

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE [dbo].[sys_dict_item]
GO
CREATE TABLE [dbo].[sys_dict_item] (
[id] nvarchar(32) NOT NULL ,
[dict_id] nvarchar(32) NULL ,
[item_text] nvarchar(100) NULL ,
[item_value] nvarchar(100) NULL ,
[description] nvarchar(255) NULL ,
[sort_order] int NULL ,
[status] int NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'dict_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'dict_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'dict_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'item_text')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典项文本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'item_text'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典项文本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'item_text'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'item_value')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字典项值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'item_value'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字典项值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'item_value'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'sort_order')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'sort_order'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'sort_order'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_dict_item', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'状态（1启用 0不启用）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'状态（1启用 0不启用）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_dict_item'
, @level2type = 'COLUMN', @level2name = N'status'
GO

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0072d115e07c875d76c9b022e2179128', N'4d7fec1a7799a436d26d02325eff295e', N'低', N'L', N'低', N'3', N'1', N'admin', N'2019-04-16 17:04:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'05a2e732ce7b00aa52141ecc3e330b4e', N'3486f32803bb953e7155dab3513dc68b', N'已删除', N'1', null, null, N'1', N'admin', N'2025-10-18 21:46:56.0000000', N'admin', N'2019-03-28 22:23:20.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'096c2e758d823def3855f6376bc736fb', N'bd1b8bc28e65d6feefefb6f3c79f42fd', N'SQL', N'sql', null, N'1', N'1', N'admin', N'2019-04-12 17:26:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0c9532916f5cd722017b46bc4d953e41', N'2f0320997ade5dd147c90130f7218c3e', N'指定用户', N'USER', null, null, N'1', N'admin', N'2019-03-17 21:22:19.0000000', N'admin', N'2019-03-17 21:22:28.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0ca4beba9efc4f9dd54af0911a946d5c', N'72cce0989df68887546746d8f09811aa', N'附表', N'3', null, N'3', N'1', N'admin', N'2019-03-27 10:13:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1030a2652608f5eac3b49d70458b8532', N'2e02df51611a4b9632828ab7e5338f00', N'禁用', N'2', N'禁用', N'2', N'1', N'admin', N'2021-03-26 18:27:28.0000000', N'admin', N'2019-04-26 18:39:11.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'147c48ff4b51545032a9119d13f3222a', N'd6e1152968b02d69ff358c75b48a6ee1', N'测试流程', N'test', null, N'1', N'1', N'admin', N'2019-03-22 19:27:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1543fe7e5e26fb97cdafe4981bedc0c8', N'4c03fca6bf1f0299c381213961566349', N'单排布局', N'single', null, N'2', N'1', N'admin', N'2022-07-12 17:43:39.0000000', N'admin', N'2019-04-12 17:43:57.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1b8a6341163062dad8cb2fddd34e0c3b', N'404a04a15f371566c658ee9ef9fc392a', N'22', N'222', null, N'1', N'1', N'admin', N'2019-03-30 11:17:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1ce390c52453891f93514c1bd2795d44', N'ad7c65ba97c20a6805d5dcdf13cdaf36', N'000', N'00', null, N'1', N'1', N'admin', N'2019-03-22 16:34:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1db531bcff19649fa82a644c8a939dc4', N'4c03fca6bf1f0299c381213961566349', N'组合布局', N'combination', N'', N'4', N'1', N'admin', N'2019-05-11 16:07:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'222705e11ef0264d4214affff1fb4ff9', N'4f69be5f507accea8d5df5f11346181a', N'短信', N'1', N'', N'1', N'1', N'admin', N'2023-02-28 10:50:36.0000000', N'admin', N'2019-04-28 10:58:11.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'23a5bb76004ed0e39414e928c4cde155', N'4e4602b3e3686f0911384e188dc7efb4', N'不等于', N'!=', N'不等于', N'3', N'1', N'admin', N'2019-04-01 16:46:15.0000000', N'admin', N'2019-04-01 17:48:40.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'25847e9cb661a7c711f9998452dc09e6', N'4e4602b3e3686f0911384e188dc7efb4', N'小于等于', N'<=', N'小于等于', N'6', N'1', N'admin', N'2019-04-01 16:44:34.0000000', N'admin', N'2019-04-01 17:49:10.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2d51376643f220afdeb6d216a8ac2c01', N'68168534ff5065a152bfab275c2136f8', N'有效', N'1', N'有效', N'2', N'1', N'admin', N'2019-04-26 19:22:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'308c8aadf0c37ecdde188b97ca9833f5', N'8dfe32e2d29ea9430a988b3b558bf233', N'已发布', N'1', N'已发布', N'2', N'1', N'admin', N'2019-04-16 17:41:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'333e6b2196e01ef9a5f76d74e86a6e33', N'8dfe32e2d29ea9430a988b3b558bf233', N'未发布', N'0', N'未发布', N'1', N'1', N'admin', N'2019-04-16 17:41:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'337ea1e401bda7233f6258c284ce4f50', N'bd1b8bc28e65d6feefefb6f3c79f42fd', N'JSON', N'json', null, N'1', N'1', N'admin', N'2019-04-12 17:26:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'33bc9d9f753cf7dc40e70461e50fdc54', N'a9d9942bd0eccb6e89de92d130ec4c4a', N'发送失败', N'2', null, N'3', N'1', N'admin', N'2019-04-12 18:20:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3fbc03d6c994ae06d083751248037c0e', N'78bda155fe380b1b3f175f1e88c284c6', N'已完成', N'3', N'已完成', N'3', N'1', N'admin', N'2019-05-09 16:33:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'41d7aaa40c9b61756ffb1f28da5ead8e', N'0b5d19e1fce4b2e6647e6b4a17760c14', N'通知公告', N'1', null, N'1', N'1', N'admin', N'2019-04-22 18:01:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'41fa1e9571505d643aea87aeb83d4d76', N'4e4602b3e3686f0911384e188dc7efb4', N'等于', N'=', N'等于', N'4', N'1', N'admin', N'2019-04-01 16:45:24.0000000', N'admin', N'2019-04-01 17:49:00.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'43d2295b8610adce9510ff196a49c6e9', N'845da5006c97754728bf48b6a10f79cc', N'正常', N'1', null, null, N'1', N'admin', N'2019-03-18 21:45:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4f05fb5376f4c61502c5105f52e4dd2b', N'83bfb33147013cc81640d5fd9eda030c', N'操作日志', N'2', null, null, N'1', N'admin', N'2019-03-18 23:22:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'50223341bfb5ba30bf6319789d8d17fe', N'd6e1152968b02d69ff358c75b48a6ee1', N'业务办理', N'business', null, N'3', N'1', N'admin', N'2023-04-22 19:28:05.0000000', N'admin', N'2019-03-22 23:24:39.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'538fca35afe004972c5f3947c039e766', N'2e02df51611a4b9632828ab7e5338f00', N'显示', N'1', N'显示', N'1', N'1', N'admin', N'2025-03-26 18:27:13.0000000', N'admin', N'2019-04-26 18:39:07.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5584c21993bde231bbde2b966f2633ac', N'4e4602b3e3686f0911384e188dc7efb4', N'自定义SQL表达式', N'USE_SQL_RULES', N'自定义SQL表达式', N'9', N'1', N'admin', N'2019-04-01 10:45:24.0000000', N'admin', N'2019-04-01 17:49:27.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'58b73b344305c99b9d8db0fc056bbc0a', N'72cce0989df68887546746d8f09811aa', N'主表', N'2', null, N'2', N'1', N'admin', N'2019-03-27 10:13:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5b65a88f076b32e8e69d19bbaadb52d5', N'2f0320997ade5dd147c90130f7218c3e', N'全体用户', N'ALL', null, null, N'1', N'admin', N'2020-10-17 21:22:43.0000000', N'admin', N'2019-03-28 22:17:09.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5d84a8634c8fdfe96275385075b105c9', N'3d9a351be3436fbefb1307d4cfb49bf2', N'女', N'2', null, N'2', N'1', null, N'2019-01-04 14:56:56.0000000', null, N'2019-01-04 17:38:12.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'66c952ae2c3701a993e7db58f3baf55e', N'4e4602b3e3686f0911384e188dc7efb4', N'大于', N'>', N'大于', N'1', N'1', N'admin', N'2019-04-01 10:45:46.0000000', N'admin', N'2019-04-01 17:48:29.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6937c5dde8f92e9a00d4e2ded9198694', N'ad7c65ba97c20a6805d5dcdf13cdaf36', N'easyui', N'3', null, N'1', N'1', N'admin', N'2019-03-22 16:32:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69cacf64e244100289ddd4aa9fa3b915', N'a9d9942bd0eccb6e89de92d130ec4c4a', N'未发送', N'0', null, N'1', N'1', N'admin', N'2019-04-12 18:19:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6a7a9e1403a7943aba69e54ebeff9762', N'4f69be5f507accea8d5df5f11346181a', N'邮件', N'2', N'', N'2', N'1', N'admin', N'2031-02-28 10:50:44.0000000', N'admin', N'2019-04-28 10:59:03.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6c682d78ddf1715baf79a1d52d2aa8c2', N'72cce0989df68887546746d8f09811aa', N'单表', N'1', null, N'1', N'1', N'admin', N'2019-03-27 10:13:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6d404fd2d82311fbc87722cd302a28bc', N'4e4602b3e3686f0911384e188dc7efb4', N'模糊', N'LIKE', N'模糊', N'7', N'1', N'admin', N'2019-04-01 16:46:02.0000000', N'admin', N'2019-04-01 17:49:20.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6d4e26e78e1a09699182e08516c49fc4', N'4d7fec1a7799a436d26d02325eff295e', N'高', N'H', N'高', N'1', N'1', N'admin', N'2019-04-16 17:04:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'700e9f030654f3f90e9ba76ab0713551', N'6b78e3f59faec1a4750acff08030a79b', N'333', N'333', null, null, N'1', N'admin', N'2019-02-21 19:59:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7050c1522702bac3be40e3b7d2e1dfd8', N'c5a14c75172783d72cbee6ee7f5df5d1', N'柱状图', N'bar', null, N'1', N'1', N'admin', N'2019-04-12 17:05:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'71b924faa93805c5c1579f12e001c809', N'd6e1152968b02d69ff358c75b48a6ee1', N'OA办公', N'oa', null, N'2', N'1', N'admin', N'2021-03-22 19:27:17.0000000', N'admin', N'2019-03-22 23:24:36.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'75b260d7db45a39fc7f21badeabdb0ed', N'c36169beb12de8a71c8683ee7c28a503', N'不启用', N'0', null, null, N'1', N'admin', N'2019-03-18 23:29:41.0000000', N'admin', N'2019-03-18 23:29:54.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7688469db4a3eba61e6e35578dc7c2e5', N'c36169beb12de8a71c8683ee7c28a503', N'启用', N'1', null, null, N'1', N'admin', N'2019-03-18 23:29:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'78ea6cadac457967a4b1c4eb7aaa418c', N'fc6cd58fde2e8481db10d3a1e68ce70c', N'正常', N'1', null, null, N'1', N'admin', N'2019-03-18 23:30:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7ccf7b80c70ee002eceb3116854b75cb', N'ac2f7c0c5c5775fcea7e2387bcb22f01', N'按钮权限', N'2', null, null, N'1', N'admin', N'2019-03-18 23:25:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'81fb2bb0e838dc68b43f96cc309f8257', N'fc6cd58fde2e8481db10d3a1e68ce70c', N'冻结', N'2', null, null, N'1', N'admin', N'2019-03-18 23:30:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'83250269359855501ec4e9c0b7e21596', N'4274efc2292239b6f000b153f50823ff', N'显示/访问(授权后显示/可访问)', N'1', N'', N'1', N'1', N'admin', N'2019-05-10 17:54:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'84778d7e928bc843ad4756db1322301f', N'4e4602b3e3686f0911384e188dc7efb4', N'大于等于', N'>=', N'大于等于', N'5', N'1', N'admin', N'2019-04-01 10:46:02.0000000', N'admin', N'2019-04-01 17:49:05.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'848d4da35ebd93782029c57b103e5b36', N'c5a14c75172783d72cbee6ee7f5df5d1', N'饼图', N'pie', null, N'3', N'1', N'admin', N'2019-04-12 17:05:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'84dfc178dd61b95a72900fcdd624c471', N'78bda155fe380b1b3f175f1e88c284c6', N'处理中', N'2', N'处理中', N'2', N'1', N'admin', N'2019-05-09 16:33:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'86f19c7e0a73a0bae451021ac05b99dd', N'ac2f7c0c5c5775fcea7e2387bcb22f01', N'子菜单', N'1', null, null, N'1', N'admin', N'2019-03-18 23:25:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8bccb963e1cd9e8d42482c54cc609ca2', N'4f69be5f507accea8d5df5f11346181a', N'微信', N'3', null, N'3', N'1', N'admin', N'2021-05-11 14:29:12.0000000', N'admin', N'2019-04-11 14:29:31.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8c618902365ca681ebbbe1e28f11a548', N'4c753b5293304e7a445fd2741b46529d', N'启用', N'1', N'', N'0', N'1', N'admin', N'2020-07-18 23:19:27.0000000', N'admin', N'2019-05-17 14:51:18.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8cdf08045056671efd10677b8456c999', N'4274efc2292239b6f000b153f50823ff', N'可编辑(未授权时禁用)', N'2', N'', N'2', N'1', N'admin', N'2019-05-10 17:55:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8ff48e657a7c5090d4f2a59b37d1b878', N'4d7fec1a7799a436d26d02325eff295e', N'中', N'M', N'中', N'2', N'1', N'admin', N'2019-04-16 17:04:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', N'4c753b5293304e7a445fd2741b46529d', N'不启用', N'0', null, N'1', N'1', N'admin', N'2019-03-18 23:19:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2321496db6febc956a6c70fab94cb0c', N'404a04a15f371566c658ee9ef9fc392a', N'3', N'3', null, N'1', N'1', N'admin', N'2019-03-30 11:18:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2be752dd4ec980afaec1efd1fb589af', N'8dfe32e2d29ea9430a988b3b558bf233', N'已撤销', N'2', N'已撤销', N'3', N'1', N'admin', N'2019-04-16 17:41:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aa0d8a8042a18715a17f0a888d360aa4', N'ac2f7c0c5c5775fcea7e2387bcb22f01', N'一级菜单', N'0', null, null, N'1', N'admin', N'2019-03-18 23:24:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'adcf2a1fe93bb99a84833043f475fe0b', N'4e4602b3e3686f0911384e188dc7efb4', N'包含', N'IN', N'包含', N'8', N'1', N'admin', N'2019-04-01 16:45:47.0000000', N'admin', N'2019-04-01 17:49:24.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b029a41a851465332ee4ee69dcf0a4c2', N'0b5d19e1fce4b2e6647e6b4a17760c14', N'系统消息', N'2', null, N'1', N'1', N'admin', N'2019-02-22 18:02:08.0000000', N'admin', N'2019-04-22 18:02:13.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b2a8b4bb2c8e66c2c4b1bb086337f393', N'3486f32803bb953e7155dab3513dc68b', N'正常', N'0', null, null, N'1', N'admin', N'2022-10-18 21:46:48.0000000', N'admin', N'2019-03-28 22:22:20.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b57f98b88363188daf38d42f25991956', N'6b78e3f59faec1a4750acff08030a79b', N'22', N'222', null, null, N'0', N'admin', N'2019-02-21 19:59:43.0000000', N'admin', N'2019-03-11 21:23:27.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b5f3bd5f66bb9a83fecd89228c0d93d1', N'68168534ff5065a152bfab275c2136f8', N'无效', N'0', N'无效', N'1', N'1', N'admin', N'2019-04-26 19:21:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b9fbe2a3602d4a27b45c100ac5328484', N'78bda155fe380b1b3f175f1e88c284c6', N'待提交', N'1', N'待提交', N'1', N'1', N'admin', N'2019-05-09 16:32:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ba27737829c6e0e582e334832703d75e', N'236e8a4baff0db8c62c00dd95632834f', N'同步', N'1', N'同步', N'1', N'1', N'admin', N'2021-04-15 15:28:15.0000000', N'admin', N'2019-05-17 15:17:32.6570000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cbfcc5b88fc3a90975df23ffc8cbe29c', N'c5a14c75172783d72cbee6ee7f5df5d1', N'曲线图', N'line', null, N'2', N'1', N'admin', N'2019-05-12 17:05:30.0000000', N'admin', N'2019-04-12 17:06:06.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd217592908ea3e00ff986ce97f24fb98', N'c5a14c75172783d72cbee6ee7f5df5d1', N'数据列表', N'table', null, N'4', N'1', N'admin', N'2019-04-12 17:05:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'df168368dcef46cade2aadd80100d8aa', N'3d9a351be3436fbefb1307d4cfb49bf2', N'男', N'1', null, N'1', N'1', null, N'2027-08-04 14:56:49.0000000', N'admin', N'2019-03-23 22:44:44.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e6329e3a66a003819e2eb830b0ca2ea0', N'4e4602b3e3686f0911384e188dc7efb4', N'小于', N'<', N'小于', N'2', N'1', N'admin', N'2019-04-01 16:44:15.0000000', N'admin', N'2019-04-01 17:48:34.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e94eb7af89f1dbfa0d823580a7a6e66a', N'236e8a4baff0db8c62c00dd95632834f', N'不同步', N'0', N'不同步', N'2', N'1', N'admin', N'2019-05-15 15:28:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f0162f4cc572c9273f3e26b2b4d8c082', N'ad7c65ba97c20a6805d5dcdf13cdaf36', N'booostrap', N'1', null, N'1', N'1', N'admin', N'2021-08-22 16:32:04.0000000', N'admin', N'2019-03-22 16:33:57.0000000')
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f16c5706f3ae05c57a53850c64ce7c45', N'a9d9942bd0eccb6e89de92d130ec4c4a', N'发送成功', N'1', null, N'2', N'1', N'admin', N'2019-04-12 18:19:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f2a7920421f3335afdf6ad2b342f6b5d', N'845da5006c97754728bf48b6a10f79cc', N'冻结', N'2', null, null, N'1', N'admin', N'2019-03-18 21:46:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f37f90c496ec9841c4c326b065e00bb2', N'83bfb33147013cc81640d5fd9eda030c', N'登录日志', N'1', null, null, N'1', N'admin', N'2019-03-18 23:22:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f753aff60ff3931c0ecb4812d8b5e643', N'4c03fca6bf1f0299c381213961566349', N'双排布局', N'double', null, N'3', N'1', N'admin', N'2019-04-12 17:43:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fcec03570f68a175e1964808dc3f1c91', N'4c03fca6bf1f0299c381213961566349', N'Tab风格', N'tab', null, N'1', N'1', N'admin', N'2019-04-12 17:43:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_dict_item] ([id], [dict_id], [item_text], [item_value], [description], [sort_order], [status], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fe50b23ae5e68434def76f67cef35d2d', N'78bda155fe380b1b3f175f1e88c284c6', N'已作废', N'4', N'已作废', N'4', N'1', N'admin', N'2021-09-09 16:33:43.0000000', N'admin', N'2019-05-09 16:34:40.0000000')
GO
GO

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE [dbo].[sys_log]
GO
CREATE TABLE [dbo].[sys_log] (
[id] nvarchar(32) NOT NULL ,
[log_type] int NULL ,
[log_content] nvarchar(1000) NULL ,
[operate_type] int NULL ,
[userid] nvarchar(32) NULL ,
[username] nvarchar(100) NULL ,
[ip] nvarchar(100) NULL ,
[method] nvarchar(500) NULL ,
[request_url] nvarchar(255) NULL ,
[request_param] nvarchar(255) NULL ,
[request_type] nvarchar(10) NULL ,
[cost_time] bigint NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'系统日志表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'系统日志表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'log_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'日志类型（1登录日志，2操作日志）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'log_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'日志类型（1登录日志，2操作日志）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'log_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'log_content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'日志内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'log_content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'日志内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'log_content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'operate_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'操作类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'operate_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'操作类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'operate_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'userid')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'操作用户账号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'userid'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'操作用户账号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'userid'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'username')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'操作用户名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'username'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'操作用户名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'username'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'ip')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'IP'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'ip'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'IP'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'ip'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'method')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请求java方法'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'method'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请求java方法'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'method'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'request_url')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请求路径'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_url'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请求路径'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_url'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'request_param')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请求参数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_param'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请求参数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_param'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'request_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请求类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请求类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'request_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'cost_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'耗时'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'cost_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'耗时'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'cost_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_log', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_log'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'002b7112a147edeb6149a891494577d0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:52:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'00f763e007e5a6bddf4cb8e562a53005', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 15:41:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'01075aa535274735e0df0a8bc44f62f9', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-09 16:56:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'016510fc662d9bb24d0186c5478df268', N'1', N'用户名: admin,登录成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-02-26 20:27:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0169622dcd4e89b177a0917778ac7f9c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 11:17:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'017e9596f489951f1cc7d978085adc00', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 10:58:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'018fe8d3f049a32fb8b541c893058713', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 15:17:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'01ebe1cbeae916a9228770f63130fdac', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-09 16:56:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'02026841bf8a9204db2c500c86a4a9be', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:44:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'024a4c5ba78538d05373dac650b227d1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 15:59:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0251bbee51c28f83459f4a57eeb61777', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 22:14:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'02d4447c9d97ac4fc1c3a9a4c789c2a8', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-28 18:24:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'03c0ab177bd7d840b778713b37daf86f', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 10:04:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'03ec66b6b6d17c007ec2f918efe5b898', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:16:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0473dedf4aa653b253b008dacff2937c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 13:04:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0475b4445d5f58f29212258c1644f339', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-01 20:20:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'04f97d7f906c1e97384a94f3728606a4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 12:08:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'055cf35c8865761b479c7f289dc36616', N'2', N'添加测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"admin","createTime":1547912238787,"email":"zhangdaiscott@163.com","id":"c2c0d49e3c01913067cf8d1fb3c971d2","name":"zhang daihao"}]', null, N'16', N'admin', N'2019-01-19 23:37:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'056dd4466f4ed51de26c535fd9864828', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 19:47:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0571e5730ee624d0dd1b095ad7101738', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 16:10:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'059bac84373e9dae94363ea18802d70f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 10:06:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'060d541a9571ca2b0d24790a98d170a6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 19:28:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'063baad688535096d2ed906ae6f3a128', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-18 22:09:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0643f3ad4394de9fb3c491080c6a7a03', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 10:18:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'06fbb85b34f518cd211b948552de72f8', N'1', N'登录失败，用户名:null不存在！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:08:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'07132c1228b1c165f62ea35f4ff1cbe9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 18:15:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'07a0b3f8b4140a7a586305c2f40a2310', N'2', N'删除测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', null, N'["7eac655877842eb39dc2f0469f3964ec"]', null, N'14', N'admin', N'2019-01-19 15:38:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0819ea9729ddf70f64ace59370a62cf1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 18:59:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0913bb0e92715892c470cf538726dfbc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 16:17:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'097be3e8fdf77a245f5c85884e97b88c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-05 22:52:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0a24b1f04f79a2bcb83c4cd12d077cbc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 17:34:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0a634ed086442afa7a5fc9aa000b898a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:10:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0a6eb1fa998b749012216542a2447ae7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 11:29:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0aa792eadeae39a1ed2a98ea5d2f6d27', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-16 09:11:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0aa9272c0581e1d7f62b1293375b4574', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-16 17:26:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0ad51ba59da2c8763a4e6ed6e0a292b2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 17:37:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0b42292a532c796495a34d8d9c633afa', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 12:58:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0b9940fc5487026a3f16cade73efead5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:28:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0ba24c5f61ff53f93134cf932dd486db', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-31 21:06:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0bc44e2d682c9f28525d203589a90b43', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 20:31:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0d4582c6b7719b0bfc0260939d97274f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 21:48:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0d6cd835072c83f46d1d2a3cf13225d3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-01 12:04:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0d85728028ed67da696137c0e82ab2f6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-16 12:58:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0dc22e52c9173e4e880728bc7734ff65', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 11:14:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0dc6d04b99e76ad400eef1ded2d3d97c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:59:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0e365a21c60e4460813bdc4e3cb320a3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 23:01:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0e41fe3a34d5715bf4c88e220663583a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 17:04:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0e754ee377033067f7b2f10b56b8784c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 17:17:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0e9c0d0d26ddc652a7277912e0784d11', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 12:27:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0ef3e7ae8c073a7e3bdd736068f86c84', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:02:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0efc9df0d52c65ec318e7b46db21655f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:42:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'0f173ec7e8819358819aa14aafc724c0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:15:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'10922e0d030960e6b026c67c6179247b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 20:07:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'10a434c326e39b1d046defddc8c57f4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 21:18:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'111156480d4d18ebf40427083f25830f', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 19:48:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'11802c7a3644af411bc4e085553cfd4f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 14:46:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'11f511eeeb2e91af86b9d5e05132fc89', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 15:13:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'122edcafd54dd06e12838f41123d9d5d', N'2', N'添加测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"age":28,"birthday":1546617600000,"createBy":"admin","createTime":1547901234989,"id":"42c08b1a2e5b2a96ffa4cc88383d4b11","name":"秦500","punchTime":1546691611000}]', null, N'21387', N'admin', N'2019-01-19 20:34:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1241cf8e9fd0e28478a07bf755f528c5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'12709e62742056aa4a57fa8c2c82d84a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 09:13:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'130de55edac71aab730786307cc65936', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 20:22:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'131ccd390401b6e3894a37e4d1d195d3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:26:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'13c1e763e9d624a69727a38b85411352', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 18:39:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'13c83c56a0de8a702aeb2aa0c330e42c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 14:53:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1487d69ff97888f3a899e2ababb5ae48', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 14:21:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'14f447d9b60725cc86b3100a5cb20b75', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 19:46:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'151a9f1b01e4e749124d274313cd138c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:59:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1534f0c50e67c5682e91af5160a67a80', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 22:47:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'155d2991204d541388d837d1457e56ab', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 11:32:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'15b9599cb02b49a62fb4a1a71ccebc18', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:02:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'189842bf681338dc99dfa66d366a0e6f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 15:55:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'189e3428e35e27dfe92ece2848b10ba8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 15:52:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'18a51a5f04eeaad6530665f6b0883f0c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:34:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'18b16a451fec0fe7bf491ab348c65e30', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-16 11:55:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'18eafaeec588403245269a41732d1a74', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:45:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'190eb7b4d493eb01b13c5b97916eeb13', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:09:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1912a44dd4a6ffa1636d2dde9c2f1ab7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 11:01:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1a570aac0c30ac2955b59e2dc7a6204c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 20:58:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1a5b71c9458c17f9bcb19a5747fd47dd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 11:56:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1aa593c64062f0137c0691eabac07521', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 10:45:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1ab7c74d217152081f4fa59e4a56cc7b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 12:03:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1b05434820cbcb038028da9f5cda31bb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 17:45:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1d970c0e396ffc869e3a723d51f88b46', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 13:01:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1db82f78233c120c6ec7648ca1177986', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 23:07:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1e4533a02fb9c739a3555fa7be6e7899', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:04:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1f0b36f7e021aa5d059ffb0a74ef6de4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 23:11:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1f33d11e1833ae497e3ef65a3f02dd5b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-21 19:51:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'1f8f46118336b2cacf854c1abf8ae144', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 11:02:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'202344b08b69ad70754e6adaa777eae0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:54:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'20751803c1e5b2d758b981ba22f61fcd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 18:11:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'20e5887d0c9c7981159fe91a51961141', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 20:12:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'20fc3263762c80ab9268ddd3d4b06500', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:36:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'20fea778f4e1ac5c01b5a5a58e3805be', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 19:01:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'210a01dcb34302eaed0d1e95820655d0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:30:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21510ebaa4eca640852420ed6f6cbe01', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 11:41:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'217aa2f713b0903e6be699136e374012', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 20:07:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2186244ae450e83d1487aa01fbeae664', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 14:47:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21910e350c9083e107d39ff4278f51d6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 18:14:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21b8493a05050584d9bb06cfc2a05a6b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-05 17:29:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21bad1470a40da8336294ca7330f443d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 17:35:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'21fed0f2d080e04cf0901436721a77a6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 21:53:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'22735c059b01949a87cb918f5ef3be76', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 22:41:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'22ad9f87788506456c774801389d6a01', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:20:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'22d8a2fbd53eafb21f6f62ae073c0fc1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-17 22:28:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2312c2693d6b50ca06799fee0ad2554a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 12:11:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'23176e4b29c3d2f3abadd99ebeffa347', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 16:37:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'233e39d8b7aa90459ebef23587c25448', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 17:38:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'25f8b1b345b1c8a070fe81d715540c85', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 15:39:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'260bb025d91b59d0135d635ef85eeb82', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 11:40:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'26529d5753ceebbd0d774542ec83a43e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 20:17:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2659c59136fb1a284ab0642361b10cdd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 18:40:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2676be4ffc66f83221fd95e23d494827', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 21:31:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'26975f09c66025d1c8d87a6894a3c262', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 18:29:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'273081678d85acebaa6615973bff31db', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:02:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2746af3dd0309cdeeff7d27999fbcda1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:52:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'279e519d647f1a4e1f85f9b90ab370b9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 17:01:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'27d23027dc320175d22391d06f50082f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 15:50:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'27e8812c9a16889f14935eecacf188eb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 18:52:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2811e224e4e8d70f2946c815988b9b7c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:08:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'286af82485388bfcd3bb9821ff1a4727', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 18:34:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'28dbc8d16f98fb4b1f481462fcaba48b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 20:56:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'28e8a7ed786eaced3182c70f68c7ea78', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:18:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2919d2f18db064978a619707bde4d613', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 09:58:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2942a12521ac8e3d441429e6c4b04207', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 14:12:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2966ed2bdf67c9f3306b058d13bef301', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 21:25:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'29fb5d4297748af3cd1c7f2611b7a2d6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:38:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2a383edf5445dc8493f5240144ca72f5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:56:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2ab9cf95ac35fdbb8fe976e13c404c41', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:49:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2b3be3da6ba9d1ee49f378d729d69c50', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 13:24:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2b433e88db411bef115bc9357ba6a78b', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.105', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 12:09:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2b4d33d9be98e1e4cdd408a55f731050', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 10:32:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2b5a76869a7d1900487cd220de378dba', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-05 16:32:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2b801129457c05d23653ecaca88f1711', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 21:44:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2c6822927334eb0810b71465fd9c4945', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:02:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2c6ede513b83fbc23aaedb89dbfa868a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 18:03:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2d5af41d2df82b316ba31fcdf6168d6a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-17 14:43:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2de252a92b59ebfbf16860cc563e3865', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 22:04:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2e44c368eda5a7f7a23305b61d82cddb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 18:14:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2e63fd1b3b6a6145bc04b2a1df18d2f5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 19:01:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2eb75cb6ca5bc60241e01fa7471c0ccf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 18:34:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2eb964935df6f3a4d2f3af6ac5f2ded1', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.200', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 13:27:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2ebe7f0432f01788d69d39bc6df04a1a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 18:05:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'2fecb508d344c5b3a40f471d7b110f14', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:36:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3087ac4988a961fa1ec0b4713615c719', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 22:54:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'30da94dd068a5a57f3cece2ca5ac1a25', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 18:01:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'30ec2dc50347240f131c1004ee9b3a40', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 10:19:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'310bb368795f4985ed4eada030a435a0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 23:22:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'317e3ae1b6ccdfb5db6940789e12d300', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-03 21:44:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'32464c6f7f772ddda0a963b19ad2fd70', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 11:30:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3267222d9387284b864792531b450bfe', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 10:33:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'326b2df4ab05a8dbb03a0a0087e82a25', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-10 11:53:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'335956cbad23d1974138752199bf1d84', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 10:05:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'337b647a4085e48f61c7832e6527517d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:45:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'34a6b86424857a63159f0e8254e238c2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 18:22:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3569ada5c43a4022d3d13ac801aff40e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 14:50:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'35fdedc363d9fe514b44095da40f170b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3612f8d40add5a7754ea3d54de0b5f20', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 19:59:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'36358cacfc5eb3ba7e85cfe156218b71', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 19:14:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3683743d1936d06f3aaa03d6470e5178', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 22:40:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'36fd54ce8bc1ee4aac9e3ea4bfdcd5a8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-29 18:49:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'375aadb2833e57a0d5a2ce0546a65ca4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 20:38:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3767186b722b7fefd465e147d3170ad1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-26 21:57:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'378b44af9c1042c1438450b11c707fcf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 16:07:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'37ca8ff7098b9d118adb0a586bdc0d13', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:46:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3836dc3f91d072e838092bc8d3143906', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 12:50:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'39caf3d5d308001aeb0a18e15ae480b9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 10:31:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3a0330033a8d3b51ffbfb2e0a7db9bba', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:54:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3a290289b4b30a1caaac2d03ad3161cd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:58:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3a4a0e27d77aa8b624180e5fd5e4004e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 09:51:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3a76114e431912ff9a19a4b6eb795112', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:19:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3b07fda32423a5696b2097e1c23c00d4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 16:04:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3ba1e54aa9aa760b59dfe1d1259459bc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 09:44:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3bc73699a9fd3245b87336787422729b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:41:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3d25a4cdd75b9c4c137394ce68e67154', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 09:59:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3d9874f248a984608ca98c36c21c5a7a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 13:05:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3e2574b7b723fbc9c712b8e200ea0c84', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 14:24:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3e6116220fa8d4808175738c6de51b12', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 21:04:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3e64011b4bea7cdb76953bfbf57135ce', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 23:09:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3e69108be63179550afe424330a8a9e4', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 18:38:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3ec2023daa4a7d6a542bf28b11acf586', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 16:18:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3f47afcdce94596494746ac34eebf13b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 13:59:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'3fd0d771bbdd34fae8b48690ddd57799', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 17:17:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'404d5fb6cce1001c3553a69089a618c8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 12:29:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'406e79995e3340d052d85a74a5d40d1b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:23:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4084f184160940a96e47d7be1fab4ea3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'40b3a9bee45b23548250936310b273f4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 14:42:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4114145795da30b34545e9e39b7822d9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:39:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4218b30015501ee966548c139c14f43f', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 10:11:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4234117751af62ac87343cbf8a6f1e0f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:17:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4261867172d0fd5c04c993638661ac0b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 11:24:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4263de16a41a9ffd083a727e1e2b3cf0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-17 15:19:34.5860000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'42aef93749cc6222d5debe3fb31ba41b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 15:51:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'42bf42af90d4df949ad0a6cd1b39805e', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.200', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 17:39:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4303dbb3e502f11a3c4078f899bb3070', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 17:28:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'43079866b75ee6a031835795bb681e16', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-04 22:44:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'432067d777447423f1ce3db11a273f6f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:47:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'43536edd8aa99f9b120872e2c768206c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 10:53:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'43848099c1e70910ba1572868ee40415', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 11:28:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'445436e800d306ec1d7763c0fe28ad38', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:43:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'446724ea6dd41f4a03111c42e00d80cd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 16:56:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'45819fe1b96af820575a12e9f973014e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 09:19:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'45f0309632984f5f7c70b3d40dbafe8b', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 09:59:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'472c34745b8f86a46efa28f408465a63', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:56:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'477592ab95cd219a2ccad79de2f69f51', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-05 10:38:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4778fe2992fd5efd65f86cb0e00e338e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:53:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'47c5a5b799e10255c96ccd65286541ef', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:50:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4816854636129e31c2a5f9d38af842ef', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:45:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'484cdb8db40e3f76ef686552f57d8099', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 18:14:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'48929ec94226d9ccff9fae4ff48e95e3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:32:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'48e4e10ac7e583050fd85734f0676a7c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 19:58:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'48e5faf2d21ead650422dc2eaf1bb6c5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 22:08:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'48eac0dd1c11fe8f0cb49f1bd14529c2', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 13:01:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4922f2f1173a1edc11dfd11cb2a100ae', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:08:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4930e32672465979adbc592e116226a6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 16:53:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'49d48fda33126595f6936a5d64e47af0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 13:17:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'49f1ec54eb16af2001ff6809a089e940', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:59:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4a0020835a71fc6dcaefd01968d21f81', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 18:46:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4aa770f37a7de0039ba0f720c5246486', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:26:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4ab79469ba556fa890258a532623d1dc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:54:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4acfbc327681d89dab861c77401f8992', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 10:54:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4b1341863a8fffeccda8bbe413bd815f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 15:59:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4ba055970859a6f1afcc01227cb82a2d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 09:43:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4d1be4b4991a5c2d4d17d0275e4209cf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-05 20:47:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4d9299e2daac1f49eac0cec75a90c32e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 11:28:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4de1ed55165f7086f1a425a26a2f56ec', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:26:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4f237cdf7860dc71ffabff620fdd3ab3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-17 15:16:56.1230000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4f31f3ebaf5d1a159d2bb11dd9984909', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-15 11:14:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'4f7f587bec68ed5bf9f68b0ccd76d62b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 21:01:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5034aec34f0b79da510e66008dbf2fcc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 16:18:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'506ce2d73a038b6e491a35a6c74a7343', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 13:44:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'507b55d3b5ddc487fb40ca1f716a1253', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:43:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'50e8de3e6b45f8625b8fd5590c9fd834', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 18:23:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'515c28df59f07478339b61ca5b1b54a8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 10:34:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'51aeabed335ab4e238640a4d17dd51a3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-12 10:12:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'523a54948d5edaf421566014b66f9465', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 19:50:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'52673feae24ea5bc3ca111f19c9a85d4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:16:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'528baecc596a66eaadc8887bff911f55', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 10:08:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'52e36d72cd04bea2604747e006b038ec', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 19:47:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'52fde989fb8bb78d03fb9c14242f5613', N'1', N'用户名: admin,登录成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-20 17:04:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5323f848cddbb80ba4f0d19c0580eba9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 22:58:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5358b182eab53a79eec236a9cee1e0fc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 13:01:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'543970eba4d1c522e3cb597b0fd4ad13', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 22:53:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'54c2bad38dafd9e636ce992aa93b26af', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 11:57:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5554869b3475770046602061775e0e57', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 14:38:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'557b3c346d9bc8f7a83fac9f5b12dc1b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:20:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'55d649432efa7eaecd750b4b6b883f83', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 22:44:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'55e906361eeabb6ec16d66c7196a06f0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 12:50:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'562092eb81561ee0f63be5dd9367d298', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:20:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'580256f7c7ea658786dba919009451b6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:39:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'583d3aa445d408f4ecd19ee0a85514af', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:18:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5858f2f8436460a94a517904c0bfcacb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 23:42:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'586002e1fb4e60902735070bab48afe3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 16:18:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'586e8244eff6d6761077ef15ab9a82d9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 23:03:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5902fb4ba61ccf7ff4d2dd97072b7e5b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:48:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'59558082e1b1d754fa3def125ed4db3c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 18:24:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5c04e3d9429e3bcff4d55f6205c4aa83', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 18:14:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5c35117cbeb39428fcc2ddd90ce96a2b', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:18:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5c48703e3a2d4f81ee5227f0e2245990', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-04 23:12:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5c675eeb69795180eee2c1069efc114b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 12:59:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5c7e834e089ef86555d8c2627b1b29b5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:25:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5cf2431447eab30fd3623e831033eea0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 19:17:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5d8ed15778aa7d99224ee62c606589fb', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-30 15:51:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5dee273feb8dd12989b40c2c92ce8c4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 13:42:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5e8bac7831de49146d568c9a8477ddad', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:16:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5ea258e1f478d27e0879e2f4bcb89ecd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 14:01:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5ee6d5fe1e6adcc4ad441b230fae802d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 15:56:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5f00b5514a11cd2fe240c131e9ddd136', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 16:30:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'5f314fc45492d7f90b74d1ca74d1d392', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 17:45:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'606cb4f81f9bb412e2b2bdaa0f3e5dda', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 16:27:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'60886d5de8a18935824faf8b0bed489e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:11:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'60a975067f02cf05e74fa7b71e8e862a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-31 14:31:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'60d4f59974170c67826e64480533d793', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 13:25:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'611fa74c70bd5a7a8af376464a2133e8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 17:48:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'61445cc950f5d04d91339089b18edef9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:13:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'615625178b01fc20c60184cd28e64a70', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:47:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'61aac4cfe67ec6437cd901f95fbd6f45', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:40:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'61d2d2fd3e9e23f67c23b893a1ae1e72', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 22:44:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'623e4bc7c098f368abcc368227235caf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-01 09:48:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'62d197757e2cb40f9e8cb57fa6a207f7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:54:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'62e208389a400e37250cfa51c204bdc8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:44:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'636309eec5e750bc94ce06fb98526fb2', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-30 18:15:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'636d37d423199e15b4030f35c60859fe', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 10:07:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'63ccf8dda5d9bf825ecdbfb9ff9f456c', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.105', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 12:14:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'642e48f2e5ac8fe64f1bfacf4d234dc8', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:51:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'64711edfb8c4eb24517d86baca005c96', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:41:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'64c00f27ddc93fda22f91b38d2b828b5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 17:34:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'65771bce3f5786dfb4d84570df61a47a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 22:07:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'65ace1ae98891f48ab4121d9258e4f1e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 10:45:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'65be8e015c9f2c493bd0a4e405dd8221', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:40:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'661c4792f00b0814e486c3d623d7259f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 18:06:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'66493cd0347eeb6ee2ef5ee923604683', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:29:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6664dc299f547f6702f93e2358810cc1', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.3.22', null, null, null, null, null, N'jeecg-boot', N'2019-04-05 21:04:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'67181c36b55b06047a16a031fd1262c1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-17 13:55:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'671a44fd91bf267549d407e0c2a680ee', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 22:45:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'672b527c49dc349689288ebf2c43ed4d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 11:26:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6737424e01b38f2273e9728bf39f3e37', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 19:43:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'675153568c479d8b7c6fe63327066c9f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 15:29:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'679e12ba247575749e03aa8f67347ac6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:14:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'680b3e6e4768d80d6ea0ce8ba71bdd0e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 09:14:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6836a652dc96246c028577e510695c6f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-15 20:47:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'687810e7fea7e480962c58db515a5e1c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 18:42:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'689b8f2110f99c52e18268cbaf05bbb6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:58:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'68df65639e82cc6a889282fbef53afbb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-08 15:01:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'68e90e08a866de748e9901e923406959', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 12:37:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'68f7394ca53c59438b2b41e7bb9f3094', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 14:09:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69a7a5b960d6aedda5c4bd8b877be0a8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:50:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69a9dfb2fb02e4537b86c9c5c05184ae', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 15:22:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69baa4f883fe881f401ea063ddfd0079', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-12 20:51:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69e3164d007be2b9834e4fb398186f39', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 23:38:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69e6fd7891d4b42b0cccdc0874a43752', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:45:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69ea2322f72b41bcdc7f235889132703', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:54:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'69fc2407b46abad64fa44482c0dca59f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-01 12:04:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6a4231540c73ad67128d5a24e6a877ff', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 12:54:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6a67bf2ff924548dee04aa97e1d64d38', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:52:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6b4cdd499885ccba43b40f10abf64a78', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 13:04:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6b876be6e384337b36ad28a4a5868be8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:22:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6baccd034e970c6f109791cff43bc327', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:46:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6bc98b7dc91a3924f794202867367aca', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:50:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6c558d70dc5794f9f473d8826485727a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 18:38:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6cbd2a9257fae1cb7ff7bc2eb264b3ab', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 19:08:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6cf638853ef5384bf81ed84572a6445d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 19:25:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6cfeaf6a6be5bb993b9578667999c354', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-24 11:43:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6d45672f99bbfd01d6385153e9c3ad91', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 13:49:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6d93d5667245ef8e5d6eafdbc9113f51', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:34:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'6fb7db45b11bc22347b234fda07700c8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 12:00:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'703fbcb7e198e8e64978ec0518971420', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 17:53:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'70849167f54fd50d8906647176d90fdf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 23:12:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'709b0f2bf8cb8f785f883509e54ace28', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:37:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7126b35521cd0dba932e6f04b0dac88f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:52:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7148b3d58f121ef04bcbea5dd2e5fe3b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:35:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'716f9f5f066a6f75a58b7b05f2f7f861', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:59:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7225200c3cec4789af4f1da2c46b129d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:19:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7268539fbe77c5cc572fb46d71d838f1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 13:22:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7285730e2644f49def0937dc99bfbe3d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:07:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7289cf420ac87ea0538bde81435b1aaa', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:03:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'72ee87d0637fb3365fdff9ccbf286c4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 17:36:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7313b43ff53015d79a58b4dc7c660721', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:03:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'732a1015057fde25d81ee12a7fbf66b2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-01 10:05:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7351132f4f5f65e5bf157dd7ad5344a4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:51:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7374f3a2ccb20216cf8eecb26037ce0a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 18:08:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'74209dfc97285eb7919868545fc2c649', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 11:23:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'74c991568d8bcb2049a0dbff53f72875', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 22:12:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'75c7fa1a7d3639be1b112e263561e43a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 17:07:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'76bea561f662ec0ccf05bc370f1ffe35', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 11:08:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'772f238d46531a75fff31bae5841057c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 11:31:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'77579d78a903635cc4942882f568e9e5', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:13:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'775e987a2ca37edc4f21e022b265a84a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 13:36:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'776c2e546c9ab0375d97590b048b8a9d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:13:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'77a329e5eb85754075165b06b7d877fd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 13:25:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'78caf9e97aedfb8c7feef0fc8fdb4fb5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-10 17:04:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'78f519b618f82a39adad391fbf6b9c7a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 13:49:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'790b722fa99a8f3a0bc38f61e13c1cf4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 18:34:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'79a1737fcc199c8262f344e48afb000d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 23:25:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'79e76353faffd0beb0544c0aede8564f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-08 17:28:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7a511b225189342b778647db3db385cd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 20:50:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7a99cf653439ca82ac3b0d189ddaad4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 10:41:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7a9d307d22fb2301d6a9396094afc82f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 18:45:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7ae9cad197aee3d50e93bc3a242d68ec', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-10 13:12:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7b2b322a47e1ce131d71c50b46d7d29e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-06 15:55:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7b44138c1b80b67da13b89db756a22b0', N'2', N'添加测试DEMO', null, null, null, N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"jeecg-boot","createTime":1553824172062,"id":"5fce01cb7f0457746c97d8ca05628f81","name":"1212"}]', null, N'25', N'jeecg-boot', N'2019-03-29 09:49:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7b85fba62bc001773fff1a54e1609aef', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 16:28:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7ba3df5d2612ac3dd724e07a55411386', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:35:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7bc7b1ff923dbb19fb0ecd800cd690bd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-18 09:34:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7c310b99a84411798a2aaf4074a28e7e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:42:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7c88e9cf6018a1b97b420b8cb6122815', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-28 19:46:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7ce1934fb542a406e92867aec5b7254d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 14:53:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7d11535270734de80bd52ec0daa4fc1f', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.105', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 12:20:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7d8539ff876aad698fba235a1c467fb8', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 09:47:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7da063020a42db99e0f3bb9500498828', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 13:45:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7dc448f04edf4b9655362ad1a1c58753', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 23:10:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7dc498b45fbf25c59686d9dda0d3eb66', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:12:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7e2edea80050d2e46aa2e8faef8e29ce', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:01:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7e41208e29d412d586fc39375628b0d0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-21 15:34:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7e92abdc0c1f54596df499a5a2d11683', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:59:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7f31435ca2f5a4ef998a4152b2433dec', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 18:36:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7f9c3d539030049a39756208670be394', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:44:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'7feae2fb5001ca0095c05a8b08270317', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:17:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'802cec0efbe9d862b7cea29fefc5448b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 18:58:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'810deb9fd39fa2f0a8e30e3db42f7c2b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 18:51:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8143ce0b35bfe6e7b8113e1ecc066acd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-17 14:48:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'81b06ae2cd24ac0daeb213722e620f72', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-05-18 11:46:15.4990000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'81c9056ac38e6f881d60f3d41df1845e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-17 11:44:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'81c95e1c8805fa191753fc99ba54c3e9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:01:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'81f7a606359aff9f97f95c15ce8e7c69', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 11:33:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'82cee1c403025fc1db514c60fc7d8d29', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 16:41:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8300e85a2c2f16c2358d31e8b364edf7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 11:55:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8317a81bce60a10afeb44af6ef6c807a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 11:27:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8327cced60486bad4009276e14403502', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 09:56:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8447099784da63b3b2cd2fbbc5eabcea', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 16:04:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'845f732f6a0f0d575debc4103e92bea2', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 10:17:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'85949de2d54078e6b8f3df0a3c79c43d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 17:08:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'85b3106d757d136b48172a9ab1f35bb6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 18:34:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'862aa0e6e101a794715174eef96f7847', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:41:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'873f425879ef9ca7ced982acda19ea58', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 16:35:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8742a458bf166fd5f134ac65fa8903f9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 13:09:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'87885bc889d23c7c208614da8e021fb0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 10:23:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8802209912ca66d56f2ea241ffd0cc13', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:52:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'88bab180edf685549c7344ec8db7d954', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 19:07:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'88bfc5b77b4be0d6d0f7c8661cf24853', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:25:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'88d7136ed5c7630057451816dbaff183', N'1', N'用户名: jeecg,退出成功！', null, N'jeecg', N'jeecg', N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-03-25 13:01:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'88d8b4b50bdab58c52fe25fa711fbbef', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:21:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'89bfd8b9d6fa57a8e7017a2345ec1534', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-12 09:27:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'89d2bc84e056f327291c53821d421034', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 16:57:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'89fbc93e77defb34c609c84a7fe83039', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 09:47:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8a13971104d70e35111d10dd99de392e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:34:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8ab131214232450ca202103ef81f0a2d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 15:46:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8b2ad448021fbb5509ea04c9a780b165', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 14:35:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8b66ec251e3107765768dbd0590eeb29', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 14:25:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8d105ea6c89691bc8ee7d4fd568aa690', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:39:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8d9ce65020320d46882be43b22b12a62', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 10:56:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8e03def9e0283005161d062d4c0a5a80', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:46:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8ec3a287a37d155047e80a80769d5226', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 18:37:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8f616500d666a5a67bc98e7ccd73c2e2', N'2', N'添加测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"admin","createTime":1547912194199,"id":"ffa9da1ad40632dfcabac51d766865bd","name":"秦999"}]', null, N'386', N'admin', N'2019-01-19 23:36:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8fde5f89e8ad30cf3811b8683a9a77b1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 18:02:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8fe913a5b037943c6667ee4908f88bea', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-16 11:18:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'8ff27392165c8c707ee10ec0010c7bb8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:44:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'90555a39c0b02180df74752e4d33f253', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 18:26:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'905d2cf4308f70a3a2121a3476e38ed0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 14:00:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'90711ddb861e28bd8774631c98f3edb9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:57:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'90b4bad7939233a1e0d7935f079ea0fa', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:45:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9174fe77fe8ba69243f72d5577b391d3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:48:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'917dbb5db85d1a6f142135827e259bbf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 20:21:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'92e514fee917a1a459c4ffdb0ca42516', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 10:20:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'939b3ff4733247a47efe1352157b1f27', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 19:01:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'93b4d26f60d7fb45a60524760bf053e4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 22:20:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'93bb98ba996dacebfb4f61503067352e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 22:47:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9410b7974fbc9df415867095b210e572', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-16 11:18:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'94fe4465d779e0438cfe6f0cb1a1aa7e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:57:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'95063e0bdfa5c9817cc0f66e96baad93', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:59:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'952947331f8f3379494c4742be797fc3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 18:42:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'954f1ccb8b230d2d7d4858eec3aba0a4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 17:08:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'95d906e6f048c3e71ddbcc0c9448cf49', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-26 19:23:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'961992e05772bc7ad2ca927cf7649440', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:55:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'965c74ffe09d8a06bb817efa6d62254b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 10:01:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'968d434c45aae64c9ad0e86d18238065', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-01 10:02:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'96ada57ac17c4477f4e4c8d596d4cc1a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 10:54:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'96d7fe922f46123e0497e22dedf89328', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 23:10:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'98b7fc431e4654f403e27ec9af845c7b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 17:31:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'98d4b573769af6d9c10cd5c509bfb7af', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-16 11:21:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'993010965223b8e3a7a784409f7e377e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 15:50:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'99357d793f2507cfb7b270677b4fe56c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:46:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'994efef0ebca19292e14a39b385b0e21', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 16:22:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'997bb4cb1ad24439b6f7656222af0710', N'2', N'添加测试DEMO', null, null, null, N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"jeecg-boot","createTime":1553824768819,"id":"ee84471f0dff5ae88c45e852bfa0280f","keyWord":"22","name":"222"}]', null, N'5', N'jeecg-boot', N'2019-03-29 09:59:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9a1456ef58a2b1fb63cdc54b723f2539', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 17:26:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9a5c1fbf3543880af6461182e24b75db', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-15 13:51:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9acebd2d37c9078f9568125fb9696976', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 16:07:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9b23981621d5265a55681883ec19fa91', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:46:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9b568a868e57f24c5ba146848061613f', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:09:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9b7a830914668881335da1b0ce2274b1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 17:19:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9bd6e11c5a2f0bb70215cfa097a4b29c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:57:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9be945480d69038865279f02df5cee45', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:49:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9bfe7312f2951503082a28c2cc966ce4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 10:24:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9c32ec437d8f8d407b1bd1165fc0305d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-08 15:01:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9c64406daa2b6e7ad1f6776789d61e43', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 10:56:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9cea908c7a78dc77fdaed975819983bd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:20:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9d0416e09fae7aeeeefc8511a61650c2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 18:15:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9db7e7d214dbe9fe8fff5ff20634e282', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 11:19:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9df97c1b3213aa64eda81c6bf818b02b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 22:42:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9e9d01c430b72703ce3a94589be54bbe', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 18:26:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9eb3fb6d9d45e3847a88f65ed47da935', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'192.168.3.22', null, null, null, null, null, N'jeecg-boot', N'2019-04-05 20:52:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9ed114408a130e69c0de4c91b2d6bf7e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 13:03:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9ef3f1ed07003e3abec3445920b062f1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 11:17:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9f2db1ffaf89518a25cc6701da0c5858', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 17:05:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9f31eedbe3f3c5c431b490d5fec0094c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-01 09:56:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'9f4960f89a10d7fdcf22d1ea46143fff', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a052befb699ee69b3197b139fd9263f0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-15 17:34:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a1b870eee811cfa4960f577b667b0973', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 18:23:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a22ddd4b5b0b84bd7794edd24b25fc64', N'2', N'添加测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"admin","createTime":1547883299809,"email":"zhangdaiscott@163.com","id":"7eac655877842eb39dc2f0469f3964ec","name":"zhang daihao"}]', null, N'25', N'admin', N'2019-01-19 15:34:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a28de45f52c027a3348a557efab6f430', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 10:34:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2950ae3b86f786a6a6c1ce996823b53', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 09:47:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a2e0435673b17f4fb848eecdf8ecacd6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 21:32:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a34ed4c6fef2b9f07a20e54ef4501b99', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 10:48:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a35a476c303983701045507c9af3fa03', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:44:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a42e5cd05566ea226c2e2fc201860f2c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 11:15:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a521d9f2a0087daa37923fa704dea85b', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:45:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a56661bbc72b8586778513c71f4764f5', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:53:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a5848ab4e8d0fb6ecf71ee1d99165468', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 22:14:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a5daa58b078cb8b3653af869aeecebd0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 17:14:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a61d9db83888d42b0d24621de48a880d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-05 22:49:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a6209166e1e9b224cca09de1e9ea1ed7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:41:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a6261bbbf8e964324935722ea1384a5d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 19:46:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a63147887c6ca54ce31f6c9e6279a714', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:19:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a68160f37cace166fedd299c4ca0be10', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 15:40:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a6971e63e3d9158020e0186cda81467d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 14:59:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a69f4ff4e48754de96ae6fa4fabc1579', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 09:18:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a6c3b28530416dace21371abe8cae00b', N'2', N'删除测试DEMO', null, null, null, N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', null, N'["ee84471f0dff5ae88c45e852bfa0280f"]', null, N'9', N'jeecg-boot', N'2019-03-29 09:59:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a6e323785535592ee208aa7e53554644', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 09:15:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a710ed2de7e31fd72b1efb1b54ba5a87', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 15:30:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a77d29673cfe97c9e03cfb879b934f62', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 12:41:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a7d1f4a774eb8644e2b1d37ca5f93641', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 10:16:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a7ee4b4c236bc0e8f56db5fdf1e5ac38', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 13:21:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a83e37b55a07fe48272b0005a193dee6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 09:17:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a867c282a8d97f7758235f881804bb48', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-13 18:28:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a8c7ba2d11315b171940def2cbeb0e8f', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 13:01:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a95192071de908f37f4998af4c269bcb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 14:26:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a9b34565c6460dc9cede00ad150393f9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 14:17:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a9bd713f975bfbff87638432a104b715', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:04:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aa47c8cf2a4f2de16f415b9d9d3dbf05', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 16:14:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aa49341b29865b45588ad2f9b89c47ea', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-11 19:42:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aaf10eab9c2b6ed6af1d7a9ce844d146', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 13:08:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ab1d707bbfdf44aa17307d30ca872403', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 15:50:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ab550d09101a88bc999ea57cbb05aa5a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 17:59:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ab8a71b7565d356d12e12c6730b0ceb0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ac8cf22c2f10a38c7a631fc590551c40', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 12:04:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ad97829fe7fefcd38c80d1eb1328e40f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 09:28:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'add13f513772a63f8ca8bf85634bb72c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 13:09:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ae61be664d2f30d4f2248347c5998a45', N'1', N'用户名: jeecg,退出成功！', null, N'jeecg', N'jeecg', N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-03-25 12:53:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aeb738ab880c262772453d35fc98f2f2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 18:50:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aec0817ecc0063bde76c1f6b6889d117', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:47:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'aeca30df24ce26f008a7e2101f7c513c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:27:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'af5869701738a6f4c2c58fe8dfe02726', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 16:42:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'af8fe96a9f0b325e4833fc0d9c4721bf', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-30 18:14:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b01c3f89bcfd263de7cb1a9b0210a7af', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 17:53:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b09ccd219a1ce5c7270bb659748b8330', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 15:34:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b0cebd174565a88bb850a2475ce14625', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 18:19:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b0d11dfec52e02f504c63e2f8224b00d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 19:27:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b0e6b3a0ec5d8c73166fb8129d21a834', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-09 16:56:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b1e9797721dbfcc51bbe7182142cbdcd', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:52:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b20ff98a10af3c25c1991741fd59ea64', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 16:07:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b23293288a84ba965509f466ed0e7e2f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:43:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b26369680b41d581649cf865e88331e9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:03:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b3127e34f395e1f1790450da5689a4a1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 16:28:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b3474fc5aad9ec2f36ccbbf7bf864a69', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 11:17:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b38f42f4e15ee72e494bdf6f6feb0ae7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:49:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b3adf055f54878657611ef430f85803e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:33:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b3cceb535fa5577cc21b12502535ad29', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:29:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b428718441be738cf8b5ce92109068c3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 16:21:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b4c3c7af9899b9af3f42f730cfabc9b2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:17:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b4ccdfc1280e73439eb1ad183076675b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 14:10:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b53c9e8ce1e129a09a3cda8c01fe644c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:38:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b55cc05e8dd4279c0fa145833db19ba8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 11:37:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b5df1807f08af5db640da11affec24d3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:49:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b5f6636c6e24e559ddf1feb3e1a77fd5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:14:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b605a83a9b5f3cdaaa1b3f4f41a5f12d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 17:04:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b6ee157afd006ceddc8c7558c251192e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 14:20:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b7085f003b4336af4d4ba18147f8e5ae', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 22:29:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b7478d917ab6f663e03d458f0bb022a3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:50:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b7f33b5a514045878447fc64636ac3e6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 22:00:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b86958d773b2c2bd79baa2e8c3c84050', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-15 16:49:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b87ce09bc7c7c32b7fa8b93779c90963', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-05-20 15:53:35.2200000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b8bd2a9de3fb917dfb6b435e58389901', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 20:13:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b945fe8b63e0fc26d02c85466f36ebd9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-01 09:57:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b954f7c34dfbe9f6a1fc12244e0a7d59', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:41:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b972484d206b36420efac466fae1c53f', N'1', N'用户名: jeecg,退出成功！', null, N'jeecg', N'jeecg', N'127.0.0.1', null, null, null, null, null, N'jeecg', N'2019-03-25 12:54:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b98b7ac9e890657aa86a900763afbe2a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 11:49:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b99fc7c53d4e3edc0c618edc11d3a073', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 15:58:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'b9bf472a12fc25a9d4b500421b08b025', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:53:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'baa53d6a534e669f6150ea47565fa5b9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-09 17:27:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'baaf37e5937f938ac92856bc74cc2b86', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 13:48:53.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bbd3e1f27e025502a67cf54945b0b269', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-31 22:13:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bbe2e637bafa0d7f465dc9e1266cff3d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 11:16:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bbf4fb593d6918cc767bb50c9b6c16c5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 10:44:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bc28d4275c7c7fcd067e1aef40ec1dd4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 10:53:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bc594b8921a0bcdb26d4a93916316092', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:42:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bd6d7d720b9dd803f8ad26e2d40870f3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 11:04:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bd9167a87aee4574a30d67825acaad0a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:51:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bdeae62057ae9858b6a546c1bdb6efc7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:49:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bdfd95b4d4c271d7d8d38f89f4a55da9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:59:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'beb9ef68b586f05bd7cf43058e01ad4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 22:29:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'befbcf5a27ef8d2ca8e6234077f9413d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 16:01:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bfa0766f53dbd3a0fe4043f57bd9bbee', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:35:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bfe758860662ae07a15598396a12cfaa', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:50:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'bfec8c8c88868391041667d924e3af7f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 14:38:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c03985d6e038b5d8ebdeec27fce249ba', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:43:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c12e3d7655a5a8b192bb9964a2a66946', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:35:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c1842fc83cdf0b0cc0264bf093e9c55d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:56:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c18db091677ec01d55e913662b9028a9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:19:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c1a68605bee6b3d1264390c1cfe7a9fa', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:49:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c21422fa08f8480a53367fda7ddddf12', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 10:02:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c2bfe3b92e6bfb7016cc82e95419a602', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:54:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c33b4e0bbf998330e44fad65e9d0029e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 15:54:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c434dc5172dc993ee7cd96187ca58653', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-28 19:46:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c5954beca75d6a0c014e2de3b621275a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 22:41:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c5d4597b38275dcb890c6568a7c113f2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-24 12:18:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c5e541648bab341230c93377b4d4e262', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:05:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c665d704539483630cc9ed5715ed57a8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:10:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c66e22782dd3916d1361c76b0cc4ec8a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 22:44:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c6c0316b6989bf1eea0a3803f593bf69', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 15:47:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c6cbe54fcb194d025a081e5f91a7e3f0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 10:26:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c72bb25acd132303788699834ae039b4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:06:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c7b6156c4f42b70c562b507766f4546c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:14:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c98a6367b152cf5311d0eec98fab390c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 22:13:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'c9be887c9292153e39861c91243b7432', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 15:12:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ca737885d9034f71f70c4ae7986fafa8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 22:47:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'caee69e55ec929f7ba904280cac971e6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:49:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cb6b52fbbdd4c5698c17edaf9960e11e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:22:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cb7c6178101ef049d3f1820ee41df539', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-28 19:59:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cbd720f20fc090c7350a98be0738816a', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 20:55:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cbf83d11486a8d57814ae38c9822b022', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:05:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cc39057ae0a8a996fb0b3a8ad5b8f341', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:20:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cc7fa5567e7833a3475b29b7441a2976', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 14:21:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cc8ab347f332c55570830c5fc39bbf9f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 13:08:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ccad29843623a6c3ca59548b1d533b15', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:56:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cd064a2f6cb6c640cb97a74aaa6041d7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:17:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cd5af66a87bb40026c72a748155b47e8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:47:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cd7a7c49e02ca9613b6879fda4e563cf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:29:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ce6aa822166b97a78b0bbea62366f8e0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 20:14:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ce9893f4d0dd163e900fcd537f2c292d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:55:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cedf399271592c27dcb6f6ce3312e77d', N'2', N'删除测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', null, N'["7501"]', null, N'24', N'admin', N'2019-03-06 16:03:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cf590576a5f6a42b347e6b5bf5ebf5bd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 12:43:31.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'cfba34db2d7fbb15a2971212f09b59ec', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:51:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd00964eee24c6f9a8609a42eeebef957', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-10 17:04:48.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd01d658731dac4b580a879d986b03456', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 15:00:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd03aaee882d13b796db860cb95f27724', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:59:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd0ce9bfc790a573d48d49d3bbbf1a1cb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 22:09:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd1111594fef195980370c5f91ccf9212', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:48:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd1746c5c937fcb650bd835ff74dabdff', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 18:06:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd18bff297a5c2fa54d708f25a7d790d6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:13:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd19b6e77ab1b6d6aa58996a93918754c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 11:33:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd1eb2a8ebed28d34199c5fc4a1579c4c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 12:55:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd23e6766cecf911fb2e593eeee354e18', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 18:42:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd2910961a0ff046cc3ef6cf8d33a8094', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 15:38:47.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd29cf7aae44523bf2f3d187e91356fe8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 12:20:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd2ac19a709ea08f7259286df28efd635', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:35:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd2b516c5d834bd0fca91cda416fe499e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:46:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd2fe98d661f1651b639bf74499f124db', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 10:16:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd3b54be0510db6a6da27bf30becb5335', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 19:42:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd3c4f120d8a23b62ec9e24b431a58496', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-07 14:17:24.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd3df1a4057b6d7fb4dab073a727ba21f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 23:14:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd3f08843a9b2b3284711e376fb785beb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 10:58:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd4ef00700436645680657f72445d38db', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-11 18:05:29.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd5b9e5d9bfbbd8e6d651087ead76d9f7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 20:17:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd68957c067fb27e80a23babebdb1591f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:55:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd6aaf0f8e2428bf3c957becbf4bcedb4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:38:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd70329497664391dabc25effe7406c50', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:25:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd70c2847d8d0936a2a761f745a84aa48', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 16:39:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd762a1cba3dc23068f352323d98909e0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 22:26:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd7902eeab2c34611fad046a79bff1c1b', N'2', N'添加测试DEMO', null, N'admin', N'管理员', N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"admin","createTime":1547883544104,"email":"zhangdaiscott@163.com","id":"4436302a0de50bb83025286bc414d6a9","name":"zhang daihao"}]', null, N'1682', N'admin', N'2019-01-19 15:39:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd7e7cb4c21372e48b8e0ec7e679466e3', N'1', N'用户名: null,退出成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:02:34.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd7e8a7f14967c70d68f5569cb4d11d0a', N'2', N'删除测试DEMO', null, null, null, N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', null, N'["5fce01cb7f0457746c97d8ca05628f81"]', null, N'9', N'jeecg-boot', N'2019-03-29 09:49:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd82b170459d99fc05eb8aa1774e1a1c9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-26 18:45:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd869534109332e770c70fad65ef37998', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:02:30.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd8c43edd685431ab3ef7b867efc29214', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 17:37:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd8eed69045aae6cedbff402b4e35f495', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 18:22:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd8fd478e6ceb03a575719e1a54342333', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:43:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd916bd1d956418e569549ee1c7220576', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 19:18:42.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd92d9e003666c6b020f079eaee721f9f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 12:08:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd98115c02c0ac478a16d6c35de35053d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 12:50:09.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd9a0bb9fe6d2c675aa84f9441c0bd8bb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-11 10:56:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'd9e0150666b69cced93eb4defb19788b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 23:11:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'da3fda67aea2e565574ec2bcfab5b750', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-08 22:36:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'da9a15efcf4e1e4f24647db7e2143238', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 11:19:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dabdcb8e15ea9215a1af22f7567ff73d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 16:48:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dae0658783324c81fa6909b6e4a25a65', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-22 11:46:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'db2b518e7086a0561f936d327a0ab522', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-18 22:39:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'db8adca4aa7972fdc283be96d877efe0', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:04:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'db8c89112bf4706fb558664dd741aa46', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 09:33:23.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dbbcfb7f59311637a613ec9a6c63f04a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 10:53:57.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dcec1957987abbe6658f1f2c96980366', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:05:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dcfe23b155d5c6fa9a302c063b19451e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 18:47:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dd4e1ab492e59719173d8ae0f5dbc9a2', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-11 19:47:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dd6fbb9b6224c927c0923c16b9285525', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 13:37:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'de37620b6921abcfe642606a0358d30f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-09 15:42:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'de938485a45097d1bf3fa311d0216ed4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-02 10:15:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'de978382f59685babf3684d1c090d136', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 12:55:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dee4d42c439b51b228ab5db5d0723fc0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 20:02:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'dfacaa7c01ccf0bade680044cced3f11', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 15:25:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e01ed1516e8ae3a2180acbd4e4508fa5', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 20:28:12.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e088a2607864d3e6aadf239874d51756', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-26 18:46:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e09bb0a74c268a9aaf1f94edcc2eb65a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-08 18:26:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e0da357be27d66de1c9e9b8ecb22f9f9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:51:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e14cd21cf5eaad9ea3689730a824a50c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 16:12:32.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e169938510c9320cb1495ddb9aabb9d1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:47:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e1d0b1fd3be59e465b740e32346e85b0', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-02 10:16:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e1d1fc464cf48ec26b7412585bdded1a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:49:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e1fa52ecbcc0970622cc5a0c06de9317', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-03 18:33:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e232f89df26cc9e5eced10476c4e4a2b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-10 10:05:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e234abc35a52f0dd2512b0ce2ea0e4f2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 20:05:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e261674e2640fe6d0a3cd86df631537d', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:05:51.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e2af7674bb716a7c0b703c7c7e20b906', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 11:38:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e2b6d0e751f130d35c0c3b8c6bd2a77e', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-27 16:18:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e2f703771f64b1bcd709204669ae3d93', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-05-17 14:48:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e3031f999984909f9048d8ec15543ad0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 18:43:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e37cce529d0c98c47b4977d7ddf963c0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:17:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e39f051ba6fdb7447f975421f3b090a7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-05 12:49:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e3b531fa12e47ac19a2ab0c883dee595', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 10:40:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e40823376fa8c0e74a4e760de695e824', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-30 15:36:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e42a38382fce916909d6d09f66147006', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 14:28:44.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e48a6bd82c92a8005c80c5ef36746117', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 19:32:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e4a166fcd0fc4037cb26e35cc1fb87b2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 18:32:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e4afd66ac249dde9c3bd9da50f9c2469', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 17:41:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e4c06405615399d6b1ebea45c8112b4d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:07:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e4c330b381e2fbfde49f1d4dd43e68b7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-14 22:22:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e4e40e21437b23b74324e0402cceb71a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 11:34:40.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e540ca989819c54baefffbc3d05e8b58', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 10:10:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e5a9b045449136719d4c19c429c2dd56', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 13:08:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e713a89e753cbecf1e10247b2112c3f8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 23:14:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e720278084b0d4316448ec59d4e3399d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 15:52:45.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e78f8832d61c1603c17767ee2b78ef07', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-01 19:50:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e7f2b0a7493e7858c5db1f1595fa54b1', N'2', N'添加测试DEMO', null, null, null, N'127.0.0.1', N'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, N'[{"createBy":"jeecg-boot","createTime":1553824376817,"id":"e771211b77cd3b326d3e61edfd9a5a19","keyWord":"222","name":"222"}]', null, N'7', N'jeecg-boot', N'2019-03-29 09:52:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e864c0007983211026d6987bd0cd4dc8', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.114', null, null, null, null, null, N'jeecg-boot', N'2019-03-11 13:37:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e8b37ad67ef15925352a4ac3342cef07', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:38:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e8cde8dcd6253b249d67a05aaf10f968', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-22 12:30:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e92544c6102243e7908e0cbb217f5198', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:48:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e93f1a170e3cd33f90dd132540c7a39b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:12:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e9a31bfc128b3f5ae01656916c605ddb', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:44:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e9ce2b3f7ac1fa3f5f7fd247207ca5c0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 22:53:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e9d3202c14f7f2812346fb4c2b781c67', N'1', N'用户名: admin,登录成功！', null, null, null, N'192.168.1.104', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 21:38:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ea268ad02db29012b2f1bd3d4aea1419', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:10:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ea5f9191b0f593a1d6cb585538caa815', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:46:08.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ea66ed22fde49640cee5d73c6ef69718', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 11:50:04.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eaf74cd1489b02d39c470eed131fc918', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 18:37:39.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eb0b8a7cdf77df133566d7bd5a5f1fc0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-19 11:02:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eb4536aa50a58985baf0a763a1ce2ebf', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-17 19:48:49.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eb6f5196de91dd2e8316696bddd61345', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 22:26:36.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eb9a522fd947c7a706c5a106ca32b8c9', N'1', N'用户名: jeecg,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-17 17:50:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ecfee5b948602a274093b8890e5e7f3f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:05:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ed0bbe9047a7471ae1cdc1c2941eccb1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-29 17:52:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ed2740de487c684be9fa3cf72113ae30', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:51:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ed50b1fbc80c3b953f4551081b10335e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 16:19:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ed9b4ffc8afab10732aac2d0f84c567b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 19:10:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ee2bb63c47c868d59a45503b3d2f34ea', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 19:16:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'eeb1f2e2c1b480e0bb62533848cbb176', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 22:55:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ef54197116da89bf091c0ed58321eea4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-09 19:22:06.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ef7219725c4b84cc71f56f97a8eab01a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:08:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ef7669ac0350730d198f59b8411b19d1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:28:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'efa591832b375b4609a5890b0c6f3250', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 16:00:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'efe77038e00cfff98d6931c3e7a4c3d6', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-06 16:20:19.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f0409312093beb563ac4016f2b2c6dfd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-03 13:24:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f04910792a74c563d057c4fcb345f963', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-20 18:30:00.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f06048c147c5bcdbed672e32b2c86b1c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-30 14:07:28.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f06e8fa83b408be905b4dc7caeaf9a80', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 19:40:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f0748a25728348591c7b73a66f273457', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-28 19:46:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f1186792c6584729a0f6da4432d951f9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-14 21:45:52.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f20416373c8fbae120991fa2b46534ee', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-05-17 15:19:27.4620000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f20cf3fe228ba6196a48015b98d0d354', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-01 19:25:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f21e30d73c337ea913849ed65808525c', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-27 10:23:22.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f21f9f700bf4f5bd9edda7a16ed338f8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:30:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f29f3b7b7e14b1389a0c53d263c0b26b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-29 17:44:25.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f2ce8024e62740f63c134c3cfb3cae23', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:07:41.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f3aab8f9dff7bf705aa29c6dcce49011', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-13 15:18:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f3cafb545e5693e446f641fa0b5ac8cd', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-04-04 17:07:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f3d371d6f71409ea2fe52405b725db4a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-12 13:38:15.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f3e1f7fb81004ccd64df12d94ef1e695', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-22 21:30:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f43e38800d779422c75075448af738d1', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 18:47:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f540eff3f6e86c1e0beccd300efd357f', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 15:15:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f543c25bdd741055aeb4f77c5b5acf58', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 14:40:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f58e160e97d13a851f59b70bf54e0d06', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-23 20:11:58.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f5c08b45885d248c422a5d406cd5f223', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:41:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f6646950c8465da1d1219b7a7a209fc2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-25 19:14:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f74f759b43afa639fd1c4f215c984ae0', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 22:08:18.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f78e24f5e841acac2a720f46f6c554bc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:47:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f79af48e6aeb150432640483f3bb7f2a', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:09:11.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f84e86c9a21149134b1f2599a424164b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 20:12:27.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f8960d64e93606fa52220cc9c4ae35a2', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-23 11:21:02.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f9062582881b42f6b139c313d8ab0463', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-17 20:47:26.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f93279c6899dc5e6cec975906f8bf811', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 13:47:20.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f95d517f43ba2229c80c14c1883a4ee9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 12:11:59.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f99912c5ff252594f14d31b768f8ad15', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-21 15:03:10.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f9abb524e0dc3571571dc6e50ec6db75', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 11:47:13.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa0612372b332b6c3ce787d9ca6dd2cc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-24 11:48:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa0ce422c12a565461eca56006052891', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 10:13:21.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa427f74dc6bd9cca3db478b5842f7f7', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-28 14:19:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa9b4d7d42bc9d1ba058455b4afedbfb', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-25 12:59:46.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fa9cebbb6af23d2830584b3aacd51e46', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-31 13:59:17.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'faad055dd212ed9506b444f8f1a920b9', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-26 12:00:38.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fadb32d678346ee4bab02997988ff3bc', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 22:55:16.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'faea0dbfb7f86b571fed0dd270623831', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-18 14:12:14.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fb2871cda1421b766f8e68cb36a22bf3', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-25 17:35:01.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fb73d58bf6503270025972f99e50335d', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 11:57:56.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fbb8834e9736bdd4b6d3baee895c4ca4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-31 18:05:03.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fc22aaf9660e66558689a58dfa443074', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-15 16:30:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fc69a1640a4772c8edf2548d053fa6de', N'1', N'用户名: admin,登录成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-03-23 14:55:33.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fce1553149aea9bfd93e089f387199c8', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-02-24 23:11:35.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fded8eb5d78d13791baec769019fee54', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-25 12:15:07.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fe0dc06eaef69047131f39052fcce5c4', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-19 13:56:05.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'fea8e1e2d229557185be0d9a10ebce17', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-01-19 21:55:55.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'feaf7c377abc5824c1757d280dd3c164', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-20 11:58:54.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ff3f7dbda20cd2734b1238fa5ba17fcf', N'1', N'用户名: 管理员,退出成功！', null, N'admin', N'管理员', N'127.0.0.1', null, null, null, null, null, N'admin', N'2019-04-10 11:26:43.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ffac84fff3c65bb17aa1bda3a0d2029e', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-06 20:10:50.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_log] ([id], [log_type], [log_content], [operate_type], [userid], [username], [ip], [method], [request_url], [request_param], [request_type], [cost_time], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ffc6178ffa099bb90b9a4d0a64dae42b', N'1', N'用户名: admin,登录成功！', null, null, null, N'127.0.0.1', null, null, null, null, null, N'jeecg-boot', N'2019-03-21 18:28:32.0000000', null, null)
GO
GO

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE [dbo].[sys_permission]
GO
CREATE TABLE [dbo].[sys_permission] (
[id] nvarchar(32) NOT NULL ,
[parent_id] nvarchar(32) NULL ,
[name] nvarchar(100) NULL ,
[url] nvarchar(255) NULL ,
[component] nvarchar(255) NULL ,
[component_name] nvarchar(100) NULL ,
[redirect] nvarchar(255) NULL ,
[menu_type] int NULL ,
[perms] nvarchar(255) NULL ,
[perms_type] nvarchar(10) NULL ,
[sort_no] int NULL ,
[always_show] tinyint NULL ,
[icon] nvarchar(100) NULL ,
[is_route] tinyint NULL ,
[is_leaf] tinyint NULL ,
[hidden] int NULL ,
[description] nvarchar(255) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[del_flag] int NULL ,
[rule_flag] int NULL ,
[status] nvarchar(2) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单权限表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单权限表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'parent_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'父id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'parent_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'父id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'parent_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'url')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'路径'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'url'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'路径'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'url'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'component')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'组件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'component'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'组件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'component'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'component_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'组件名字'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'component_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'组件名字'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'component_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'redirect')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'一级菜单跳转地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'redirect'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'一级菜单跳转地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'redirect'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'menu_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'menu_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'menu_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'perms')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单权限编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'perms'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单权限编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'perms'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'perms_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'权限策略1显示2禁用'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'perms_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'权限策略1显示2禁用'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'perms_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'sort_no')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'sort_no'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单排序'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'sort_no'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'always_show')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'聚合子路由: 1是0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'always_show'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'聚合子路由: 1是0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'always_show'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'icon')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单图标'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'icon'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单图标'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'icon'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'is_route')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否路由菜单: 0:不是  1:是（默认值1）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'is_route'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否路由菜单: 0:不是  1:是（默认值1）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'is_route'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'is_leaf')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否叶子节点:    1:是   0:不是'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'is_leaf'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否叶子节点:    1:是   0:不是'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'is_leaf'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'hidden')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否隐藏路由: 0否,1是'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'hidden'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否隐藏路由: 0否,1是'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'hidden'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态 0正常 1已删除'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态 0正常 1已删除'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'rule_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否添加数据权限1是0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'rule_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否添加数据权限1是0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'rule_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'按钮权限状态(0无效1有效)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'按钮权限状态(0无效1有效)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission'
, @level2type = 'COLUMN', @level2name = N'status'
GO

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'00a2a0ae65cdca5e93209cdbde97cbe6', N'2e42e3835c2b44ec9f7bc26c146ee531', N'成功', N'/result/success', N'result/Success', null, null, N'1', null, null, N'1', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'020b06793e4de2eee0007f603000c769', N'f0675b52d89100ee88472b6800754a08', N'ViserChartDemo', N'/report/ViserChartDemo', N'jeecg/report/ViserChartDemo', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-03 19:08:53.0000000', N'admin', N'2019-04-03 19:08:53.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'024f1fd1283dc632458976463d8984e1', N'700b7f95165c46cc7a78bf227aa8fed3', N'Tomcat信息', N'/monitor/TomcatInfo', N'modules/monitor/TomcatInfo', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-02 09:44:29.0000000', N'admin', N'2019-05-07 15:19:10.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'05b3c82ddb2536a4a5ee1a4c46b5abef', N'540a2936940846cb98114ffb0d145cb8', N'用户列表', N'/list/user-list', N'list/UserList', null, null, N'1', null, null, N'3', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'0620e402857b8c5b605e1ad9f4b89350', N'2a470fc0c3954d9dbb61de6d80846549', N'异步树列表Demo', N'/jeecg/JeecgTreeTable', N'jeecg/JeecgTreeTable', null, null, N'1', null, N'0', N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-05-13 17:30:30.0000000', N'admin', N'2019-05-13 17:32:17.0000000', N'0', N'0', N'1')
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'078f9558cdeab239aecb2bda1a8ed0d1', N'fb07ca05a3e13674dbf6d3245956da2e', N'搜索列表（文章）', N'/list/search/article', N'list/TableList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-12 14:00:34.0000000', N'admin', N'2019-02-12 14:17:54.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'', N'系统监控', N'/dashboard3', N'layouts/RouteView', null, null, N'0', null, null, N'6', N'0', N'dashboard', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-31 22:19:58.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'13212d3416eb690c2e1d5033166ff47a', N'2e42e3835c2b44ec9f7bc26c146ee531', N'失败', N'/result/fail', N'result/Error', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'1367a93f2c410b169faa7abcbad2f77c', N'6e73eb3c26099c191bf03852ee1310a1', N'基本设置', N'/account/settings/base', N'account/settings/BaseSetting', null, null, N'1', N'BaseSettings', null, null, N'0', null, N'1', N'1', N'1', null, null, N'2018-12-26 18:58:35.0000000', N'admin', N'2019-03-20 12:57:31.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'190c2b43bec6a5f7a4194a85db67d96a', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'角色维护', N'/system/roleUserList', N'system/RoleUserList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-17 15:13:56.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'1a0811914300741f4e11838ff37a1d3a', N'3f915b2769fc80648e92d04e84ca059d', N'手机号禁用', null, null, null, null, N'2', N'user:form:phone', N'2', N'1', N'0', null, N'0', N'1', N'0', null, N'admin', N'2019-05-11 17:19:30.0000000', N'admin', N'2019-05-11 18:00:22.0000000', N'0', N'0', N'1')
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'200006f0edf145a2b50eacca07585451', N'fb07ca05a3e13674dbf6d3245956da2e', N'搜索列表（应用）', N'/list/search/application', N'list/TableList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-12 14:02:51.0000000', N'admin', N'2019-02-12 14:14:01.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'265de841c58907954b8877fb85212622', N'2a470fc0c3954d9dbb61de6d80846549', N'图片拖拽排序', N'/jeecg/imgDragSort', N'jeecg/ImgDragSort', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-25 10:43:08.0000000', N'admin', N'2019-04-25 10:46:26.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'277bfabef7d76e89b33062b16a9a5020', N'e3c13679c73a4f829bcff2aba8fd68b1', N'基础表单', N'/form/base-form', N'form/BasicForm', null, null, N'1', null, null, N'1', N'0', null, N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-02-26 17:02:08.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'2a470fc0c3954d9dbb61de6d80846549', N'', N'常见案例', N'/jeecg', N'layouts/RouteView', null, null, N'0', null, null, N'7', N'0', N'qrcode', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-04-02 11:46:42.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'2aeddae571695cd6380f6d6d334d6e7d', N'f0675b52d89100ee88472b6800754a08', N'布局统计报表', N'/report/ArchivesStatisticst', N'jeecg/report/ArchivesStatisticst', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-03 18:32:48.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'2dbbafa22cda07fa5d169d741b81fe12', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'在线文档', N'{{ window._CONFIG[''domianURL''] }}/swagger-ui.html#/', N'layouts/IframePageView', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-01-30 10:00:01.0000000', N'admin', N'2019-03-23 19:44:43.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'2e42e3835c2b44ec9f7bc26c146ee531', N'', N'结果页', N'/result', N'layouts/PageView', null, null, N'0', null, null, N'8', N'0', N'check-circle-o', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-04-02 11:46:56.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'339329ed54cf255e1f9392e84f136901', N'2a470fc0c3954d9dbb61de6d80846549', N'helloworld', N'/jeecg/helloworld', N'jeecg/helloworld', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-02-15 16:24:56.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'3f915b2769fc80648e92d04e84ca059d', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'用户管理', N'/isystem/user', N'system/UserList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-16 11:20:33.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'3fac0d3c9cd40fa53ab70d4c583821f8', N'2a470fc0c3954d9dbb61de6d80846549', N'分屏', N'/jeecg/splitPanel', N'jeecg/SplitPanel', null, null, N'1', null, null, N'6', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-25 16:27:06.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'4148ec82b6acd69f470bea75fe41c357', N'2a470fc0c3954d9dbb61de6d80846549', N'单表模型示例', N'/jeecg/jeecgDemoList', N'jeecg/JeecgDemoList', N'DemoList', null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, null, N'2018-12-28 15:57:30.0000000', N'admin', N'2019-02-15 16:24:37.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'418964ba087b90a84897b62474496b93', N'540a2936940846cb98114ffb0d145cb8', N'查询表格', N'/list/query-list', N'list/TableList', null, null, N'1', null, null, N'1', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'4356a1a67b564f0988a484f5531fd4d9', N'2a470fc0c3954d9dbb61de6d80846549', N'内嵌Table', N'/jeecg/TableExpandeSub', N'jeecg/TableExpandeSub', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-04 22:48:13.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'45c966826eeff4c99b8f8ebfe74511fc', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'部门管理', N'/isystem/depart', N'system/DepartList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-01-29 18:47:40.0000000', N'admin', N'2019-03-07 19:23:16.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'4875ebe289344e14844d8e3ea1edd73f', N'', N'详情页', N'/profile', N'layouts/RouteView', null, null, N'0', null, null, N'8', N'0', N'profile', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-04-02 11:46:48.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'4f66409ef3bbd69c1d80469d6e2a885e', N'6e73eb3c26099c191bf03852ee1310a1', N'账户绑定', N'/account/settings/binding', N'account/settings/Binding', null, null, N'1', N'BindingSettings', null, null, null, null, N'1', N'1', null, null, null, N'2018-12-26 19:01:20.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'4f84f9400e5e92c95f05b554724c2b58', N'540a2936940846cb98114ffb0d145cb8', N'角色列表', N'/list/role-list', N'list/RoleList', null, null, N'1', null, null, N'4', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'53a9230444d33de28aa11cc108fb1dba', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'我的消息', N'/isps/userAnnouncement', N'system/UserAnnouncementList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-19 10:16:00.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'540a2936940846cb98114ffb0d145cb8', N'', N'列表页', N'/list', N'layouts/PageView', null, N'/list/query-list', N'0', null, null, N'9', N'0', N'table', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-31 22:20:20.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'54dd5457a3190740005c1bfec55b1c34', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'菜单管理', N'/isystem/permission', N'system/PermissionList', null, null, N'1', null, null, N'3', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'58857ff846e61794c69208e9d3a85466', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'日志管理', N'/isystem/log', N'system/LogList', null, null, N'1', null, null, N'1', N'0', N'', N'1', N'1', N'0', null, null, N'2018-12-26 10:11:18.0000000', N'admin', N'2019-04-02 11:38:17.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'58b9204feaf07e47284ddb36cd2d8468', N'2a470fc0c3954d9dbb61de6d80846549', N'图片翻页', N'/jeecg/imgTurnPage', N'jeecg/ImgTurnPage', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-25 11:36:42.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'5c2f42277948043026b7a14692456828', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'我的部门', N'/system/departUserList', N'system/DepartUserList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-17 15:12:24.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'5c8042bd6c601270b2bbd9b20bccc68b', N'', N'消息中心', N'/message', N'layouts/RouteView', null, null, N'0', null, null, N'6', N'0', N'message', N'1', N'0', N'0', null, N'admin', N'2019-04-09 11:05:04.0000000', N'admin', N'2019-04-11 19:47:54.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'6531cf3421b1265aeeeabaab5e176e6d', N'e3c13679c73a4f829bcff2aba8fd68b1', N'分步表单', N'/form/step-form', N'form/stepForm/StepForm', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'655563cd64b75dcf52ef7bcdd4836953', N'2a470fc0c3954d9dbb61de6d80846549', N'图片预览', N'/jeecg/ImagPreview', N'jeecg/ImagPreview', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-17 11:18:45.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'65a8f489f25a345836b7f44b1181197a', N'c65321e57b7949b7a975313220de0422', N'403', N'/exception/403', N'exception/403', null, null, N'1', null, null, N'1', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'6ad53fd1b220989a8b71ff482d683a5a', N'2a470fc0c3954d9dbb61de6d80846549', N'一对多Tab示例', N'/jeecg/tablist/JeecgOrderDMainList', N'jeecg/tablist/JeecgOrderDMainList', null, null, N'1', null, null, N'2', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-20 14:45:09.0000000', N'admin', N'2019-02-21 16:26:21.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'6e73eb3c26099c191bf03852ee1310a1', N'717f6bee46f44a3897eca9abd6e2ec44', N'个人设置', N'/account/settings/base', N'account/settings/Index', null, null, N'1', null, null, N'2', N'1', null, N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-04-19 09:41:05.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'700b7f95165c46cc7a78bf227aa8fed3', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'性能监控', N'/monitor', N'layouts/RouteView', null, null, N'1', null, null, N'0', N'0', null, N'1', N'0', N'0', null, N'admin', N'2019-04-02 11:34:34.0000000', N'admin', N'2019-05-05 17:49:47.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'717f6bee46f44a3897eca9abd6e2ec44', null, N'个人页', N'/account', N'layouts/RouteView', null, null, N'0', null, null, N'9', N'0', N'user', N'1', N'0', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'73678f9daa45ed17a3674131b03432fb', N'540a2936940846cb98114ffb0d145cb8', N'权限列表', N'/list/permission-list', N'list/PermissionList', null, null, N'1', null, null, N'5', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'7593c9e3523a17bca83b8d7fe8a34e58', N'3f915b2769fc80648e92d04e84ca059d', N'添加用户按钮', N'', null, null, null, N'2', N'user:add', N'1', N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-16 11:20:33.0000000', N'admin', N'2019-05-17 15:19:05.4570000', N'0', N'0', N'1')
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'7960961b0063228937da5fa8dd73d371', N'2a470fc0c3954d9dbb61de6d80846549', N'JEditableTable示例', N'/jeecg/JEditableTable', N'jeecg/JeecgEditableTableExample', null, null, N'1', null, null, N'7', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-22 15:22:18.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'7ac9eb9ccbde2f7a033cd4944272bf1e', N'540a2936940846cb98114ffb0d145cb8', N'卡片列表', N'/list/card', N'list/CardList', null, null, N'1', null, null, N'7', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'841057b8a1bef8f6b4b20f9a618a7fa6', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'数据日志', N'/sys/dataLog-list', N'system/DataLogList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-11 19:26:49.0000000', N'admin', N'2019-03-12 11:40:47.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'882a73768cfd7f78f3a37584f7299656', N'6e73eb3c26099c191bf03852ee1310a1', N'个性化设置', N'/account/settings/custom', N'account/settings/Custom', null, null, N'1', N'CustomSettings', null, null, null, null, N'1', N'1', null, null, null, N'2018-12-26 19:00:46.0000000', null, N'2018-12-26 21:13:25.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'8b3bff2eee6f1939147f5c68292a1642', N'700b7f95165c46cc7a78bf227aa8fed3', N'服务器信息', N'/monitor/SystemInfo', N'modules/monitor/SystemInfo', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-02 11:39:19.0000000', N'admin', N'2019-04-02 15:40:02.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'8d1ebd663688965f1fd86a2f0ead3416', N'700b7f95165c46cc7a78bf227aa8fed3', N'Redis监控', N'/monitor/redis/info', N'modules/monitor/RedisInfo', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-02 13:11:33.0000000', N'admin', N'2019-05-07 15:18:54.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'8fb8172747a78756c11916216b8b8066', N'717f6bee46f44a3897eca9abd6e2ec44', N'工作台', N'/dashboard/workplace', N'dashboard/Workplace', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-04-02 11:45:02.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'944abf0a8fc22fe1f1154a389a574154', N'5c8042bd6c601270b2bbd9b20bccc68b', N'消息管理', N'/modules/message/sysMessageList', N'modules/message/SysMessageList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-09 11:27:53.0000000', N'admin', N'2019-04-09 19:31:23.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'9502685863ab87f0ad1134142788a385', N'', N'首页', N'/dashboard/analysis', N'dashboard/Analysis', null, null, N'0', null, null, N'0', N'0', N'home', N'1', N'1', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-29 11:04:13.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'97c8629abc7848eccdb6d77c24bb3ebb', N'700b7f95165c46cc7a78bf227aa8fed3', N'磁盘监控', N'/monitor/Disk', N'modules/monitor/DiskMonitoring', null, null, N'1', null, null, N'6', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-25 14:30:06.0000000', N'admin', N'2019-05-05 14:37:14.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'9a90363f216a6a08f32eecb3f0bf12a3', N'2a470fc0c3954d9dbb61de6d80846549', N'常用选择组件', N'/jeecg/SelectDemo', N'jeecg/SelectDemo', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-19 11:19:05.0000000', N'admin', N'2019-04-10 15:36:50.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'9cb91b8851db0cf7b19d7ecc2a8193dd', N'1939e035e803a99ceecb6f5563570fb2', N'我的任务表单', N'/modules/bpm/task/form/FormModule', N'modules/bpm/task/form/FormModule', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-08 16:49:05.0000000', N'admin', N'2019-03-08 18:37:56.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'a400e4f4d54f79bf5ce160ae432231af', N'2a470fc0c3954d9dbb61de6d80846549', N'百度', N'http://www.baidu.com', N'layouts/IframePageView', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-01-29 19:44:06.0000000', N'admin', N'2019-02-15 16:25:02.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'ae4fed059f67086fd52a73d913cf473d', N'540a2936940846cb98114ffb0d145cb8', N'内联编辑表格', N'/list/edit-table', N'list/TableInnerEditList', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'aedbf679b5773c1f25e9f7b10111da73', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'SQL监控', N'{{ window._CONFIG[''domianURL''] }}/druid/', N'layouts/IframePageView', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-01-30 09:43:22.0000000', N'admin', N'2019-03-23 19:00:46.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'b1cb0a3fedf7ed0e4653cb5a229837ee', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', N'定时任务', N'/isystem/QuartzJobList', N'system/QuartzJobList', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, null, N'2019-01-03 09:38:52.0000000', N'admin', N'2019-04-02 10:24:13.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'b3c824fc22bd953e2eb16ae6914ac8f9', N'4875ebe289344e14844d8e3ea1edd73f', N'高级详情页', N'/profile/advanced', N'profile/advanced/Advanced', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', N'c65321e57b7949b7a975313220de0422', N'500', N'/exception/500', N'exception/500', null, null, N'1', null, null, N'3', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'b6bcee2ccc854052d3cc3e9c96d90197', N'71102b3b87fb07e5527bbd2c530dd90a', N'加班申请', N'/modules/extbpm/joa/JoaOvertimeList', N'modules/extbpm/joa/JoaOvertimeList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-03 15:33:10.0000000', N'admin', N'2019-04-03 15:34:48.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'c431130c0bc0ec71b0a5be37747bb36a', N'2a470fc0c3954d9dbb61de6d80846549', N'一对多JEditable', N'/jeecg/JeecgOrderMainListForJEditableTable', N'jeecg/JeecgOrderMainListForJEditableTable', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-03-29 10:51:59.0000000', N'admin', N'2019-04-04 20:09:39.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'c65321e57b7949b7a975313220de0422', null, N'异常页', N'/exception', N'layouts/RouteView', null, null, N'0', null, null, N'8', null, N'warning', N'1', N'0', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'c6cf95444d80435eb37b2f9db3971ae6', N'2a470fc0c3954d9dbb61de6d80846549', N'数据回执模拟', N'/jeecg/InterfaceTest', N'jeecg/InterfaceTest', null, null, N'1', null, null, N'6', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-19 16:02:23.0000000', N'admin', N'2019-02-21 16:25:45.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'cc50656cf9ca528e6f2150eba4714ad2', N'4875ebe289344e14844d8e3ea1edd73f', N'基础详情页', N'/profile/basic', N'profile/basic/Index', null, null, N'1', null, null, N'1', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'd07a2c87a451434c99ab06296727ec4f', N'700b7f95165c46cc7a78bf227aa8fed3', N'JVM信息', N'/monitor/JvmInfo', N'modules/monitor/JvmInfo', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-01 23:07:48.0000000', N'admin', N'2019-04-02 11:37:16.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'd2bbf9ebca5a8fa2e227af97d2da7548', N'c65321e57b7949b7a975313220de0422', N'404', N'/exception/404', N'exception/404', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'', N'系统管理', N'/isystem', N'layouts/RouteView', null, null, N'1', null, null, N'4', N'0', N'setting', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-31 22:19:52.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'd86f58e7ab516d3bc6bfb1fe10585f97', N'717f6bee46f44a3897eca9abd6e2ec44', N'个人中心', N'/account/center', N'account/center/Index', null, null, N'1', null, null, N'1', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'de13e0f6328c069748de7399fcc1dbbd', N'fb07ca05a3e13674dbf6d3245956da2e', N'搜索列表（项目）', N'/list/search/project', N'list/TableList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-12 14:01:40.0000000', N'admin', N'2019-02-12 14:14:18.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e08cb190ef230d5d4f03824198773950', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'系统通告', N'/isystem/annountCement', N'system/SysAnnouncementList', null, null, N'1', N'annountCement', null, N'6', null, N'', N'1', N'1', null, null, null, N'2019-01-02 17:23:01.0000000', null, N'2019-01-02 17:31:23.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e1979bb53e9ea51cecc74d86fd9d2f64', N'2a470fc0c3954d9dbb61de6d80846549', N'PDF预览', N'/jeecg/jeecgPdfView', N'jeecg/JeecgPdfView', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-25 10:39:35.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e3c13679c73a4f829bcff2aba8fd68b1', N'', N'表单页', N'/form', N'layouts/PageView', null, null, N'0', null, null, N'9', N'0', N'form', N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-03-31 22:20:14.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e5973686ed495c379d829ea8b2881fc6', N'e3c13679c73a4f829bcff2aba8fd68b1', N'高级表单', N'/form/advanced-form', N'form/advancedForm/AdvancedForm', null, null, N'1', null, null, N'3', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e6bfd1fcabfd7942fdd05f076d1dad38', N'2a470fc0c3954d9dbb61de6d80846549', N'打印测试', N'/jeecg/PrintDemo', N'jeecg/PrintDemo', null, null, N'1', null, null, N'3', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-19 15:58:48.0000000', N'admin', N'2019-05-07 20:14:39.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'e8af452d8948ea49d37c934f5100ae6a', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'角色管理', N'/isystem/role', N'system/RoleList', null, null, N'1', null, null, N'2', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'ec8d607d0156e198b11853760319c646', N'6e73eb3c26099c191bf03852ee1310a1', N'安全设置', N'/account/settings/security', N'account/settings/Security', null, null, N'1', N'SecuritySettings', null, null, null, null, N'1', N'1', null, null, null, N'2018-12-26 18:59:52.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'f0675b52d89100ee88472b6800754a08', N'', N'统计报表', N'/report', N'layouts/RouteView', null, null, N'0', null, null, N'1', N'0', N'bar-chart', N'1', N'0', N'0', null, N'admin', N'2019-04-03 18:32:02.0000000', N'admin', N'2019-05-20 15:51:38.7800000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'f1cb187abf927c88b89470d08615f5ac', N'd7d6e2e4e2934f2c9385a623fd98c6f3', N'数据字典', N'/isystem/dict', N'system/DictList', null, null, N'1', null, null, N'5', null, null, N'1', N'1', null, null, null, N'2018-12-28 13:54:43.0000000', null, N'2018-12-28 15:37:54.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'f23d9bfff4d9aa6b68569ba2cff38415', N'540a2936940846cb98114ffb0d145cb8', N'标准列表', N'/list/basic-list', N'list/StandardList', null, null, N'1', null, null, N'6', null, null, N'1', N'1', null, null, null, N'2018-12-25 20:34:38.0000000', null, null, N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'f780d0d3083d849ccbdb1b1baee4911d', N'5c8042bd6c601270b2bbd9b20bccc68b', N'模板管理', N'/modules/message/sysMessageTemplateList', N'modules/message/SysMessageTemplateList', null, null, N'1', null, null, N'1', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-09 11:50:31.0000000', N'admin', N'2019-04-12 10:16:34.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'fb07ca05a3e13674dbf6d3245956da2e', N'540a2936940846cb98114ffb0d145cb8', N'搜索列表', N'/list/search', N'list/search/SearchLayout', null, N'/list/search/article', N'1', null, null, N'8', N'0', null, N'1', N'0', N'0', null, null, N'2018-12-25 20:34:38.0000000', N'admin', N'2019-02-12 15:09:13.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'fb367426764077dcf94640c843733985', N'2a470fc0c3954d9dbb61de6d80846549', N'一对多示例', N'/jeecg/JeecgOrderMainList', N'jeecg/JeecgOrderMainList', null, null, N'1', null, null, N'2', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-02-15 16:24:11.0000000', N'admin', N'2019-02-18 10:50:14.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'fc810a2267dd183e4ef7c71cc60f4670', N'700b7f95165c46cc7a78bf227aa8fed3', N'请求追踪', N'/monitor/HttpTrace', N'modules/monitor/HttpTrace', null, null, N'1', null, null, N'4', N'0', null, N'1', N'1', N'0', null, N'admin', N'2019-04-02 09:46:19.0000000', N'admin', N'2019-04-02 11:37:27.0000000', N'0', N'0', null)
GO
GO
INSERT INTO [dbo].[sys_permission] ([id], [parent_id], [name], [url], [component], [component_name], [redirect], [menu_type], [perms], [perms_type], [sort_no], [always_show], [icon], [is_route], [is_leaf], [hidden], [description], [create_by], [create_time], [update_by], [update_time], [del_flag], [rule_flag], [status]) VALUES (N'fedfbf4420536cacc0218557d263dfea', N'6e73eb3c26099c191bf03852ee1310a1', N'新消息通知', N'/account/settings/notification', N'account/settings/Notification', null, null, N'1', N'NotificationSettings', null, null, null, N'', N'1', N'1', null, null, null, N'2018-12-26 19:02:05.0000000', null, null, N'0', N'0', null)
GO
GO

-- ----------------------------
-- Table structure for sys_permission_data_rule
-- ----------------------------
DROP TABLE [dbo].[sys_permission_data_rule]
GO
CREATE TABLE [dbo].[sys_permission_data_rule] (
[id] nvarchar(32) NOT NULL ,
[permission_id] nvarchar(32) NULL ,
[rule_name] nvarchar(50) NULL ,
[rule_column] nvarchar(50) NULL ,
[rule_conditions] nvarchar(50) NULL ,
[rule_value] nvarchar(300) NULL ,
[status] nvarchar(3) NULL ,
[create_time] datetime2(7) NULL ,
[create_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'permission_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'菜单ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'permission_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'菜单ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'permission_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'rule_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'规则名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'规则名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'rule_column')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'字段'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_column'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'字段'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_column'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'rule_conditions')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'条件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_conditions'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'条件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_conditions'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'rule_value')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'规则值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_value'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'规则值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'rule_value'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'权限有效状态1有0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'权限有效状态1有0否'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_permission_data_rule', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_permission_data_rule'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO

-- ----------------------------
-- Records of sys_permission_data_rule
-- ----------------------------
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'32b62cb04d6c788d9d92e3ff5e66854e', N'8d4683aacaa997ab86b966b464360338', N'000', N'00', N'!=', N'00', N'1', N'2019-04-02 18:36:08.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'40283181614231d401614234fe670003', N'40283181614231d401614232cd1c0001', N'createBy', N'createBy', N'=', N'#{sys_user_code}', N'1', N'2018-01-29 21:57:04.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'4028318161424e730161424fca6f0004', N'4028318161424e730161424f61510002', N'createBy', N'createBy', N'=', N'#{sys_user_code}', N'1', N'2018-01-29 22:26:20.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402880e6487e661a01487e732c020005', N'402889fb486e848101486e93a7c80014', N'SYS_ORG_CODE', N'SYS_ORG_CODE', N'LIKE', N'010201%', N'1', N'2014-09-16 20:32:30.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402880e6487e661a01487e8153ee0007', N'402889fb486e848101486e93a7c80014', N'create_by', N'create_by', N'', N'#{SYS_USER_CODE}', N'1', N'2014-09-16 20:47:57.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402880ec5ddec439015ddf9225060038', N'40288088481d019401481d2fcebf000d', N'复杂关系', N'', N'USE_SQL_RULES', N'name like ''%张%'' or age > 10', N'1', null, null, N'2017-08-14 15:10:25.0000000', N'demo')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402880ec5ddfdd26015ddfe3e0570011', N'4028ab775dca0d1b015dca3fccb60016', N'复杂sql配置', N'', N'USE_SQL_RULES', N'table_name like ''%test%'' or is_tree = ''Y''', N'1', null, null, N'2017-08-14 16:38:55.0000000', N'demo')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402880f25b1e2ac7015b1e5fdebc0012', N'402880f25b1e2ac7015b1e5cdc340010', N'只能看自己数据', N'create_by', N'=', N'#{sys_user_code}', N'1', N'2017-03-30 16:40:51.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881875b19f141015b19f8125e0014', N'40288088481d019401481d2fcebf000d', N'可看下属业务数据', N'sys_org_code', N'LIKE', N'#{sys_org_code}', N'1', null, null, N'2017-08-14 15:04:32.0000000', N'demo')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881e45394d66901539500a4450001', N'402881e54df73c73014df75ab670000f', N'sysCompanyCode', N'sysCompanyCode', N'=', N'#{SYS_COMPANY_CODE}', N'1', N'2016-03-21 01:09:21.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881e45394d6690153950177cb0003', N'402881e54df73c73014df75ab670000f', N'sysOrgCode', N'sysOrgCode', N'=', N'#{SYS_ORG_CODE}', N'1', N'2016-03-21 01:10:15.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881e56266f43101626727aff60067', N'402881e56266f43101626724eb730065', N'销售自己看自己的数据', N'createBy', N'=', N'#{sys_user_code}', N'1', N'2018-03-27 19:11:16.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881e56266f4310162672fb1a70082', N'402881e56266f43101626724eb730065', N'销售经理看所有下级数据', N'sysOrgCode', N'LIKE', N'#{sys_org_code}', N'1', N'2018-03-27 19:20:01.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881e56266f431016267387c9f0088', N'402881e56266f43101626724eb730065', N'只看金额大于1000的数据', N'money', N'>=', N'1000', N'1', N'2018-03-27 19:29:37.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402881f3650de25101650dfb5a3a0010', N'402881e56266f4310162671d62050044', N'22', N'', N'USE_SQL_RULES', N'22', N'1', N'2018-08-06 14:45:01.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402889fb486e848101486e913cd6000b', N'402889fb486e848101486e8e2e8b0007', N'userName', N'userName', N'=', N'admin', N'1', N'2014-09-13 18:31:25.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402889fb486e848101486e98d20d0016', N'402889fb486e848101486e93a7c80014', N'title', N'title', N'=', N'12', N'1', null, null, N'2014-09-13 22:18:22.0000000', N'scott')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'402889fe47fcb29c0147fcb6b6220001', N'8a8ab0b246dc81120146dc8180fe002b', N'12', N'12', N'>', N'12', N'1', N'2014-08-22 15:55:38.0000000', N'8a8ab0b246dc81120146dc8181950052', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'4028ab775dca0d1b015dca4183530018', N'4028ab775dca0d1b015dca3fccb60016', N'表名限制', N'isDbSynch', N'=', N'Y', N'1', null, null, N'2017-08-14 16:43:45.0000000', N'demo')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'4028ef815595a881015595b0ccb60001', N'40288088481d019401481d2fcebf000d', N'限只能看自己', N'create_by', N'=', N'#{sys_user_code}', N'1', null, null, N'2017-08-14 15:03:56.0000000', N'demo')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'4028ef81574ae99701574aed26530005', N'4028ef81574ae99701574aeb97bd0003', N'用户名', N'userName', N'!=', N'admin', N'1', N'2016-09-21 12:07:18.0000000', N'admin', null, null)
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'53609e1854f4a87eb23ed23a18a1042c', N'4148ec82b6acd69f470bea75fe41c357', N'只看当前部门数据', N'sysOrgCode', N'=', N'#{sys_org_code}', N'1', N'2019-05-11 19:40:39.0000000', N'admin', N'2019-05-11 19:40:50.0000000', N'admin')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'a7d661ef5ac168b2b162420c6804dac5', N'4148ec82b6acd69f470bea75fe41c357', N'只看自己的数据', N'createBy', N'=', N'#{sys_user_code}', N'1', N'2019-05-11 19:19:05.0000000', N'admin', N'2019-05-11 19:24:58.0000000', N'admin')
GO
GO
INSERT INTO [dbo].[sys_permission_data_rule] ([id], [permission_id], [rule_name], [rule_column], [rule_conditions], [rule_value], [status], [create_time], [create_by], [update_time], [update_by]) VALUES (N'f852d85d47f224990147f2284c0c0005', null, N'小于', N'test', N'<=', N'11', N'1', N'2014-08-20 14:43:52.0000000', N'8a8ab0b246dc81120146dc8181950052', null, null)
GO
GO

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE [dbo].[sys_quartz_job]
GO
CREATE TABLE [dbo].[sys_quartz_job] (
[id] nvarchar(32) NOT NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[del_flag] int NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[job_class_name] nvarchar(255) NULL ,
[cron_expression] nvarchar(255) NULL ,
[parameter] nvarchar(255) NULL ,
[description] nvarchar(255) NULL ,
[status] int NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'修改时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'job_class_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'任务类名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'job_class_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'任务类名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'job_class_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'cron_expression')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'cron表达式'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'cron_expression'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'cron表达式'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'cron_expression'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'parameter')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'参数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'parameter'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'参数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'parameter'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_quartz_job', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'状态 0正常 -1停止'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'状态 0正常 -1停止'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_quartz_job'
, @level2type = 'COLUMN', @level2name = N'status'
GO

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO [dbo].[sys_quartz_job] ([id], [create_by], [create_time], [del_flag], [update_by], [update_time], [job_class_name], [cron_expression], [parameter], [description], [status]) VALUES (N'5b3d2c087ad41aa755fc4f89697b01e7', N'admin', N'2019-04-11 19:04:21.0000000', N'0', N'admin', N'2019-04-11 19:49:49.0000000', N'org.jeecg.modules.message.job.SendMsgJob', N'0/60 * * * * ?', null, null, N'-1')
GO
GO
INSERT INTO [dbo].[sys_quartz_job] ([id], [create_by], [create_time], [del_flag], [update_by], [update_time], [job_class_name], [cron_expression], [parameter], [description], [status]) VALUES (N'a253cdfc811d69fa0efc70d052bc8128', N'admin', N'2019-03-30 12:44:48.0000000', N'0', N'admin', N'2019-03-30 12:44:52.0000000', N'org.jeecg.modules.quartz.job.SampleJob', N'0/1 * * * * ?', null, null, N'-1')
GO
GO
INSERT INTO [dbo].[sys_quartz_job] ([id], [create_by], [create_time], [del_flag], [update_by], [update_time], [job_class_name], [cron_expression], [parameter], [description], [status]) VALUES (N'df26ecacf0f75d219d746750fe84bbee', null, null, N'0', N'admin', N'2019-01-19 15:09:41.0000000', N'org.jeecg.modules.quartz.job.SampleParamJob', N'0/1 * * * * ?', N'scott', N'带参测试 后台将每隔1秒执行输出日志', N'-1')
GO
GO

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE [dbo].[sys_role]
GO
CREATE TABLE [dbo].[sys_role] (
[id] nvarchar(32) NOT NULL ,
[role_name] nvarchar(200) NULL ,
[role_code] nvarchar(100) NOT NULL ,
[description] nvarchar(255) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'role_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'role_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'role_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'role_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'role_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色编码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'role_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'description')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'description'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'description'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO [dbo].[sys_role] ([id], [role_name], [role_code], [description], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e51758fa916c881624b046d26bd09230', N'人力资源部', N'hr', N'人力资源部', N'admin', N'2019-01-21 18:07:24.0000000', N'admin', N'2019-05-17 15:37:13.3010000')
GO
GO
INSERT INTO [dbo].[sys_role] ([id], [role_name], [role_code], [description], [create_by], [create_time], [update_by], [update_time]) VALUES (N'ee8626f80f7c2619917b6236f3a7f02b', N'临时角色', N'test', N'这是新建的临时角色123', null, N'2018-12-20 10:59:04.0000000', N'admin', N'2019-02-19 15:08:37.0000000')
GO
GO
INSERT INTO [dbo].[sys_role] ([id], [role_name], [role_code], [description], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f6817f48af4fb3af11b9e8bf182f618b', N'管理员', N'admin', N'管理员', null, N'2018-12-21 18:03:39.0000000', N'admin', N'2019-05-17 15:39:59.6000000')
GO
GO

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE [dbo].[sys_role_permission]
GO
CREATE TABLE [dbo].[sys_role_permission] (
[id] nvarchar(32) NOT NULL ,
[role_id] nvarchar(32) NULL ,
[permission_id] nvarchar(32) NULL ,
[data_rule_ids] nvarchar(1000) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role_permission', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色权限表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色权限表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role_permission', 
'COLUMN', N'role_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
, @level2type = 'COLUMN', @level2name = N'role_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
, @level2type = 'COLUMN', @level2name = N'role_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_role_permission', 
'COLUMN', N'permission_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'权限id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
, @level2type = 'COLUMN', @level2name = N'permission_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'权限id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_role_permission'
, @level2type = 'COLUMN', @level2name = N'permission_id'
GO

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'00b0748f04d3ea52c8cfa179c1c9d384', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'd7d6e2e4e2934f2c9385a623fd98c6f3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'00b82058779cca5106fbb84783534c9b', N'f6817f48af4fb3af11b9e8bf182f618b', N'4148ec82b6acd69f470bea75fe41c357', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0254c0b25694ad5479e6d6935bbc176e', N'f6817f48af4fb3af11b9e8bf182f618b', N'944abf0a8fc22fe1f1154a389a574154', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'09bd4fc30ffe88c4a44ed3868f442719', N'f6817f48af4fb3af11b9e8bf182f618b', N'e6bfd1fcabfd7942fdd05f076d1dad38', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0c2d2db76ee3aa81a4fe0925b0f31365', N'f6817f48af4fb3af11b9e8bf182f618b', N'024f1fd1283dc632458976463d8984e1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0c6b8facbb1cc874964c87a8cf01e4b1', N'f6817f48af4fb3af11b9e8bf182f618b', N'841057b8a1bef8f6b4b20f9a618a7fa6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0c6e1075e422972083c3e854d9af7851', N'f6817f48af4fb3af11b9e8bf182f618b', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0d9d14bc66e9d5e99b0280095fdc8587', N'ee8626f80f7c2619917b6236f3a7f02b', N'277bfabef7d76e89b33062b16a9a5020', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0dec36b68c234767cd35466efef3b941', N'ee8626f80f7c2619917b6236f3a7f02b', N'54dd5457a3190740005c1bfec55b1c34', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0e139e6c1b5b73eee81381ddf0b5a9f3', N'f6817f48af4fb3af11b9e8bf182f618b', N'277bfabef7d76e89b33062b16a9a5020', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'0f861cb988fdc639bb1ab943471f3a72', N'f6817f48af4fb3af11b9e8bf182f618b', N'97c8629abc7848eccdb6d77c24bb3ebb', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'105c2ac10741e56a618a82cd58c461d7', N'e51758fa916c881624b046d26bd09230', N'1663f3faba244d16c94552f849627d84', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'115a6673ae6c0816d3f60de221520274', N'21c5a3187763729408b40afb0d0fdfa8', N'63b551e81c5956d5c861593d366d8c57', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'126ea9faebeec2b914d6d9bef957afb6', N'f6817f48af4fb3af11b9e8bf182f618b', N'f1cb187abf927c88b89470d08615f5ac', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'145eac8dd88eddbd4ce0a800ab40a92c', N'e51758fa916c881624b046d26bd09230', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'154edd0599bd1dc2c7de220b489cd1e2', N'f6817f48af4fb3af11b9e8bf182f618b', N'7ac9eb9ccbde2f7a033cd4944272bf1e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'165acd6046a0eaf975099f46a3c898ea', N'f6817f48af4fb3af11b9e8bf182f618b', N'4f66409ef3bbd69c1d80469d6e2a885e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1664b92dff13e1575e3a929caa2fa14d', N'f6817f48af4fb3af11b9e8bf182f618b', N'd2bbf9ebca5a8fa2e227af97d2da7548', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'16ef8ed3865ccc6f6306200760896c50', N'ee8626f80f7c2619917b6236f3a7f02b', N'e8af452d8948ea49d37c934f5100ae6a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'17ead5b7d97ed365398ab20009a69ea3', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'e08cb190ef230d5d4f03824198773950', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1ac1688ef8456f384091a03d88a89ab1', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'693ce69af3432bd00be13c3971a57961', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1af4babaa4227c3cbb830bc5eb513abb', N'ee8626f80f7c2619917b6236f3a7f02b', N'e08cb190ef230d5d4f03824198773950', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1ba162bbc2076c25561f8622f610d5bf', N'ee8626f80f7c2619917b6236f3a7f02b', N'aedbf679b5773c1f25e9f7b10111da73', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1c1dbba68ef1817e7fb19c822d2854e8', N'f6817f48af4fb3af11b9e8bf182f618b', N'fb367426764077dcf94640c843733985', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1c55c4ced20765b8ebab383caa60f0b6', N'e51758fa916c881624b046d26bd09230', N'fb367426764077dcf94640c843733985', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1e099baeae01b747d67aca06bdfc34d1', N'e51758fa916c881624b046d26bd09230', N'6ad53fd1b220989a8b71ff482d683a5a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1e47db875601fd97723254046b5bba90', N'f6817f48af4fb3af11b9e8bf182f618b', N'baf16b7174bd821b6bab23fa9abb200d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'1fe4d408b85f19618c15bcb768f0ec22', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'9502685863ab87f0ad1134142788a385', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'20e53c87a785688bdc0a5bb6de394ef1', N'f6817f48af4fb3af11b9e8bf182f618b', N'540a2936940846cb98114ffb0d145cb8', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'248d288586c6ff3bd14381565df84163', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'3f915b2769fc80648e92d04e84ca059d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'25491ecbd5a9b34f09c8bc447a10ede1', N'f6817f48af4fb3af11b9e8bf182f618b', N'd07a2c87a451434c99ab06296727ec4f', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'25f5443f19c34d99718a016d5f54112e', N'ee8626f80f7c2619917b6236f3a7f02b', N'6e73eb3c26099c191bf03852ee1310a1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'27489816708b18859768dfed5945c405', N'a799c3b1b12dd3ed4bd046bfaef5fe6e', N'9502685863ab87f0ad1134142788a385', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2779cdea8367fff37db26a42c1a1f531', N'f6817f48af4fb3af11b9e8bf182f618b', N'fef097f3903caf3a3c3a6efa8de43fbb', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'296f9c75ca0e172ae5ce4c1022c996df', N'646c628b2b8295fbdab2d34044de0354', N'732d48f8e0abe99fe6a23d18a3171cd1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'29fb4d37aa29b9fa400f389237cf9fe7', N'ee8626f80f7c2619917b6236f3a7f02b', N'05b3c82ddb2536a4a5ee1a4c46b5abef', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'29fb6b0ad59a7e911c8d27e0bdc42d23', N'f6817f48af4fb3af11b9e8bf182f618b', N'9a90363f216a6a08f32eecb3f0bf12a3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2ad37346c1b83ddeebc008f6987b2227', N'f6817f48af4fb3af11b9e8bf182f618b', N'8d1ebd663688965f1fd86a2f0ead3416', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2c462293cbb0eab7e8ae0a3600361b5f', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'9502685863ab87f0ad1134142788a385', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2dc1a0eb5e39aaa131ddd0082a492d76', N'ee8626f80f7c2619917b6236f3a7f02b', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2ea2382af618ba7d1e80491a0185fb8a', N'ee8626f80f7c2619917b6236f3a7f02b', N'f23d9bfff4d9aa6b68569ba2cff38415', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2fcfa2ac3dcfadc7c67107dae9a0de6d', N'ee8626f80f7c2619917b6236f3a7f02b', N'73678f9daa45ed17a3674131b03432fb', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'2fdaed22dfa4c8d4629e44ef81688c6a', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'aedbf679b5773c1f25e9f7b10111da73', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'300c462b7fec09e2ff32574ef8b3f0bd', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'2a470fc0c3954d9dbb61de6d80846549', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'326181da3248a62a05e872119a462be1', N'ee8626f80f7c2619917b6236f3a7f02b', N'3f915b2769fc80648e92d04e84ca059d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3369650f5072b330543f8caa556b1b33', N'e51758fa916c881624b046d26bd09230', N'a400e4f4d54f79bf5ce160ae432231af', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'35a7e156c20e93aa7e825fe514bf9787', N'e51758fa916c881624b046d26bd09230', N'c6cf95444d80435eb37b2f9db3971ae6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'35ac7cae648de39eb56213ca1b649713', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'b1cb0a3fedf7ed0e4653cb5a229837ee', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'37112f4d372541e105473f18da3dc50d', N'ee8626f80f7c2619917b6236f3a7f02b', N'a400e4f4d54f79bf5ce160ae432231af', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'37789f70cd8bd802c4a69e9e1f633eaa', N'ee8626f80f7c2619917b6236f3a7f02b', N'ae4fed059f67086fd52a73d913cf473d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'381504a717cb3ce77dcd4070c9689a7e', N'ee8626f80f7c2619917b6236f3a7f02b', N'4f84f9400e5e92c95f05b554724c2b58', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'38a2e55db0960262800576e34b3af44c', N'f6817f48af4fb3af11b9e8bf182f618b', N'5c2f42277948043026b7a14692456828', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'38dd7a19711e7ffe864232954c06fae9', N'e51758fa916c881624b046d26bd09230', N'd2bbf9ebca5a8fa2e227af97d2da7548', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3b1886f727ac503c93fecdd06dcb9622', N'f6817f48af4fb3af11b9e8bf182f618b', N'c431130c0bc0ec71b0a5be37747bb36a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3dd8c364504484efe3499ed080548016', N'f6817f48af4fb3af11b9e8bf182f618b', N'1a0811914300741f4e11838ff37a1d3a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3de2a60c7e42a521fecf6fcc5cb54978', N'f6817f48af4fb3af11b9e8bf182f618b', N'2d83d62bd2544b8994c8f38cf17b0ddf', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3e4e38f748b8d87178dd62082e5b7b60', N'f6817f48af4fb3af11b9e8bf182f618b', N'7960961b0063228937da5fa8dd73d371', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3e563751942b0879c88ca4de19757b50', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'58857ff846e61794c69208e9d3a85466', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'3f1d04075e3c3254666a4138106a4e51', N'f6817f48af4fb3af11b9e8bf182f618b', N'3fac0d3c9cd40fa53ab70d4c583821f8', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'412e2de37a35b3442d68db8dd2f3c190', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'f1cb187abf927c88b89470d08615f5ac', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4204f91fb61911ba8ce40afa7c02369f', N'f6817f48af4fb3af11b9e8bf182f618b', N'3f915b2769fc80648e92d04e84ca059d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'439568ff7db6f329bf6dd45b3dfc9456', N'f6817f48af4fb3af11b9e8bf182f618b', N'7593c9e3523a17bca83b8d7fe8a34e58', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'444126230885d5d38b8fa6072c9f43f8', N'f6817f48af4fb3af11b9e8bf182f618b', N'f780d0d3083d849ccbdb1b1baee4911d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'445656dd187bd8a71605f4bbab1938a3', N'f6817f48af4fb3af11b9e8bf182f618b', N'020b06793e4de2eee0007f603000c769', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'44b5a73541bcb854dd5d38c6d1fb93a1', N'ee8626f80f7c2619917b6236f3a7f02b', N'418964ba087b90a84897b62474496b93', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'455cdb482457f529b79b479a2ff74427', N'f6817f48af4fb3af11b9e8bf182f618b', N'e1979bb53e9ea51cecc74d86fd9d2f64', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'459aa2e7021b435b4d65414cfbc71c66', N'e51758fa916c881624b046d26bd09230', N'4148ec82b6acd69f470bea75fe41c357', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4c0940badae3ef9231ee9d042338f2a4', N'e51758fa916c881624b046d26bd09230', N'2a470fc0c3954d9dbb61de6d80846549', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4d56ce2f67c94b74a1d3abdbea340e42', N'ee8626f80f7c2619917b6236f3a7f02b', N'd86f58e7ab516d3bc6bfb1fe10585f97', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4dab5a06acc8ef3297889872caa74747', N'f6817f48af4fb3af11b9e8bf182f618b', N'ffb423d25cc59dcd0532213c4a518261', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4e0a37ed49524df5f08fc6593aee875c', N'f6817f48af4fb3af11b9e8bf182f618b', N'f23d9bfff4d9aa6b68569ba2cff38415', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4ea403fc1d19feb871c8bdd9f94a4ecc', N'f6817f48af4fb3af11b9e8bf182f618b', N'2e42e3835c2b44ec9f7bc26c146ee531', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4f254549d9498f06f4cc9b23f3e2c070', N'f6817f48af4fb3af11b9e8bf182f618b', N'93d5cfb4448f11e9916698e7f462b4b6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'4faad8ff93cb2b5607cd3d07c1b624ee', N'a799c3b1b12dd3ed4bd046bfaef5fe6e', N'70b8f33da5f39de1981bf89cf6c99792', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'504e326de3f03562cdd186748b48a8c7', N'f6817f48af4fb3af11b9e8bf182f618b', N'027aee69baee98a0ed2e01806e89c891', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'51b11ce979730f8ce8606da16e4d69bb', N'f6817f48af4fb3af11b9e8bf182f618b', N'e8af452d8948ea49d37c934f5100ae6a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'520b5989e6fe4a302a573d4fee12a40a', N'f6817f48af4fb3af11b9e8bf182f618b', N'6531cf3421b1265aeeeabaab5e176e6d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'54fdf85e52807bdb32ce450814abc256', N'f6817f48af4fb3af11b9e8bf182f618b', N'cc50656cf9ca528e6f2150eba4714ad2', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'57c0b3a547b815ea3ec8e509b08948b3', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'3f915b2769fc80648e92d04e84ca059d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'593ee05c4fe4645c7826b7d5e14f23ec', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'8fb8172747a78756c11916216b8b8066', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5affc85021fcba07d81c09a6fdfa8dc6', N'ee8626f80f7c2619917b6236f3a7f02b', N'078f9558cdeab239aecb2bda1a8ed0d1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5d230e6cd2935c4117f6cb9a7a749e39', N'f6817f48af4fb3af11b9e8bf182f618b', N'fc810a2267dd183e4ef7c71cc60f4670', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5de6871fadb4fe1cdd28989da0126b07', N'f6817f48af4fb3af11b9e8bf182f618b', N'a400e4f4d54f79bf5ce160a3432231af', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5e4015a9a641cbf3fb5d28d9f885d81a', N'f6817f48af4fb3af11b9e8bf182f618b', N'2dbbafa22cda07fa5d169d741b81fe12', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5e634a89f75b7a421c02aecfd520325f', N'e51758fa916c881624b046d26bd09230', N'339329ed54cf255e1f9392e84f136901', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5e74637c4bec048d1880ad0bd1b00552', N'e51758fa916c881624b046d26bd09230', N'a400e4f4d54f79bf5ce160a3432231af', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'5fc194b709336d354640fe29fefd65a3', N'a799c3b1b12dd3ed4bd046bfaef5fe6e', N'9ba60e626bf2882c31c488aba62b89f0', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'60eda4b4db138bdb47edbe8e10e71675', N'f6817f48af4fb3af11b9e8bf182f618b', N'fb07ca05a3e13674dbf6d3245956da2e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'61835e48f3e675f7d3f5c9dd3a10dcf3', N'f6817f48af4fb3af11b9e8bf182f618b', N'f0675b52d89100ee88472b6800754a08', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'6451dac67ba4acafb570fd6a03f47460', N'ee8626f80f7c2619917b6236f3a7f02b', N'e3c13679c73a4f829bcff2aba8fd68b1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'660fbc40bcb1044738f7cabdf1708c28', N'f6817f48af4fb3af11b9e8bf182f618b', N'b3c824fc22bd953e2eb16ae6914ac8f9', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'66b202f8f84fe766176b3f51071836ef', N'f6817f48af4fb3af11b9e8bf182f618b', N'1367a93f2c410b169faa7abcbad2f77c', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'6c43fd3f10fdaf2a0646434ae68709b5', N'ee8626f80f7c2619917b6236f3a7f02b', N'540a2936940846cb98114ffb0d145cb8', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'6c74518eb6bb9a353f6a6c459c77e64b', N'f6817f48af4fb3af11b9e8bf182f618b', N'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'6daddafacd7eccb91309530c17c5855d', N'f6817f48af4fb3af11b9e8bf182f618b', N'edfa74d66e8ea63ea432c2910837b150', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'6fb4c2142498dd6d5b6c014ef985cb66', N'f6817f48af4fb3af11b9e8bf182f618b', N'6e73eb3c26099c191bf03852ee1310a1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'71a5f54a90aa8c7a250a38b7dba39f6f', N'ee8626f80f7c2619917b6236f3a7f02b', N'8fb8172747a78756c11916216b8b8066', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'737d35f582036cd18bfd4c8e5748eaa4', N'e51758fa916c881624b046d26bd09230', N'693ce69af3432bd00be13c3971a57961', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7413acf23b56c906aedb5a36fb75bd3a', N'f6817f48af4fb3af11b9e8bf182f618b', N'a4fc7b64b01a224da066bb16230f9c5a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588591820806', N'16457350655250432', N'5129710648430592', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588604403712', N'16457350655250432', N'5129710648430593', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588612792320', N'16457350655250432', N'40238597734928384', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588625375232', N'16457350655250432', N'57009744761589760', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588633763840', N'16457350655250432', N'16392452747300864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588637958144', N'16457350655250432', N'16392767785668608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'75002588650541056', N'16457350655250432', N'16439068543946752', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'76a54a8cc609754360bf9f57e7dbb2db', N'f6817f48af4fb3af11b9e8bf182f618b', N'c65321e57b7949b7a975313220de0422', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277779875336192', N'496138616573952', N'5129710648430592', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780043108352', N'496138616573952', N'5129710648430593', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780055691264', N'496138616573952', N'15701400130424832', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780064079872', N'496138616573952', N'16678126574637056', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780072468480', N'496138616573952', N'15701915807518720', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780076662784', N'496138616573952', N'15708892205944832', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780085051392', N'496138616573952', N'16678447719911424', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780089245696', N'496138616573952', N'25014528525733888', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780097634304', N'496138616573952', N'56898976661639168', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780135383040', N'496138616573952', N'40238597734928384', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780139577344', N'496138616573952', N'45235621697949696', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780147965952', N'496138616573952', N'45235787867885568', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780156354560', N'496138616573952', N'45235939278065664', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780164743168', N'496138616573952', N'43117268627886080', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780168937472', N'496138616573952', N'45236734832676864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780181520384', N'496138616573952', N'45237010692050944', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780189908992', N'496138616573952', N'45237170029465600', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780198297600', N'496138616573952', N'57009544286441472', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780206686208', N'496138616573952', N'57009744761589760', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780215074816', N'496138616573952', N'57009981228060672', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780219269120', N'496138616573952', N'56309618086776832', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780227657728', N'496138616573952', N'57212882168844288', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780236046336', N'496138616573952', N'61560041605435392', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780244434944', N'496138616573952', N'61560275261722624', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780257017856', N'496138616573952', N'61560480518377472', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780265406464', N'496138616573952', N'44986029924421632', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780324126720', N'496138616573952', N'45235228800716800', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780332515328', N'496138616573952', N'45069342940860416', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780340903937', N'496138616573952', N'5129710648430594', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780349292544', N'496138616573952', N'16687383932047360', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780357681152', N'496138616573952', N'16689632049631232', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780366069760', N'496138616573952', N'16689745006432256', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780370264064', N'496138616573952', N'16689883993083904', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780374458369', N'496138616573952', N'16690313745666048', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780387041280', N'496138616573952', N'5129710648430595', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780395429888', N'496138616573952', N'16694861252005888', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780403818496', N'496138616573952', N'16695107491205120', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780412207104', N'496138616573952', N'16695243126607872', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780420595712', N'496138616573952', N'75002207560273920', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780428984320', N'496138616573952', N'76215889006956544', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780433178624', N'496138616573952', N'76216071333351424', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780441567232', N'496138616573952', N'76216264070008832', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780449955840', N'496138616573952', N'76216459709124608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780458344448', N'496138616573952', N'76216594207870976', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780466733056', N'496138616573952', N'76216702639017984', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780475121664', N'496138616573952', N'58480609315524608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780483510272', N'496138616573952', N'61394706252173312', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780491898880', N'496138616573952', N'61417744146370560', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780496093184', N'496138616573952', N'76606430504816640', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780504481792', N'496138616573952', N'76914082455752704', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780508676097', N'496138616573952', N'76607201262702592', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780517064704', N'496138616573952', N'39915540965232640', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780525453312', N'496138616573952', N'41370251991977984', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780538036224', N'496138616573952', N'45264987354042368', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780546424832', N'496138616573952', N'45265487029866496', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780554813440', N'496138616573952', N'45265762415284224', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780559007744', N'496138616573952', N'45265886315024384', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780567396352', N'496138616573952', N'45266070000373760', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780571590656', N'496138616573952', N'41363147411427328', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780579979264', N'496138616573952', N'41363537456533504', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780588367872', N'496138616573952', N'41364927394353152', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780596756480', N'496138616573952', N'41371711400054784', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780605145088', N'496138616573952', N'41469219249852416', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780613533696', N'496138616573952', N'39916171171991552', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780621922304', N'496138616573952', N'39918482854252544', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780630310912', N'496138616573952', N'41373430515240960', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780718391296', N'496138616573952', N'41375330996326400', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780722585600', N'496138616573952', N'63741744973352960', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780730974208', N'496138616573952', N'42082442672082944', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780739362816', N'496138616573952', N'41376192166629376', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780747751424', N'496138616573952', N'41377034236071936', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780756140032', N'496138616573952', N'56911328312299520', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780764528640', N'496138616573952', N'41378916912336896', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780768722944', N'496138616573952', N'63482475359244288', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780772917249', N'496138616573952', N'64290663792906240', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780785500160', N'496138616573952', N'66790433014943744', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780789694464', N'496138616573952', N'42087054753927168', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780798083072', N'496138616573952', N'67027338952445952', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780806471680', N'496138616573952', N'67027909637836800', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780810665985', N'496138616573952', N'67042515441684480', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780823248896', N'496138616573952', N'67082402312228864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780827443200', N'496138616573952', N'16392452747300864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780835831808', N'496138616573952', N'16392767785668608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780840026112', N'496138616573952', N'16438800255291392', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780844220417', N'496138616573952', N'16438962738434048', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277780852609024', N'496138616573952', N'16439068543946752', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860062040064', N'496138616573953', N'5129710648430592', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860070428672', N'496138616573953', N'5129710648430593', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860078817280', N'496138616573953', N'40238597734928384', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860091400192', N'496138616573953', N'43117268627886080', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860099788800', N'496138616573953', N'57009744761589760', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860112371712', N'496138616573953', N'56309618086776832', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860120760320', N'496138616573953', N'44986029924421632', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860129148928', N'496138616573953', N'5129710648430594', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860141731840', N'496138616573953', N'5129710648430595', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860150120448', N'496138616573953', N'75002207560273920', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860158509056', N'496138616573953', N'58480609315524608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860162703360', N'496138616573953', N'76606430504816640', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860171091968', N'496138616573953', N'76914082455752704', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860179480576', N'496138616573953', N'76607201262702592', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860187869184', N'496138616573953', N'39915540965232640', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860196257792', N'496138616573953', N'41370251991977984', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860204646400', N'496138616573953', N'41363147411427328', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860208840704', N'496138616573953', N'41371711400054784', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860213035009', N'496138616573953', N'39916171171991552', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860221423616', N'496138616573953', N'39918482854252544', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860225617920', N'496138616573953', N'41373430515240960', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860234006528', N'496138616573953', N'41375330996326400', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860242395136', N'496138616573953', N'63741744973352960', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860250783744', N'496138616573953', N'42082442672082944', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860254978048', N'496138616573953', N'41376192166629376', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860263366656', N'496138616573953', N'41377034236071936', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860271755264', N'496138616573953', N'56911328312299520', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860313698304', N'496138616573953', N'41378916912336896', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860322086912', N'496138616573953', N'63482475359244288', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860326281216', N'496138616573953', N'64290663792906240', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860334669824', N'496138616573953', N'66790433014943744', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860343058432', N'496138616573953', N'42087054753927168', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860347252736', N'496138616573953', N'67027338952445952', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860351447041', N'496138616573953', N'67027909637836800', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860359835648', N'496138616573953', N'67042515441684480', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860364029952', N'496138616573953', N'67082402312228864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860368224256', N'496138616573953', N'16392452747300864', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860372418560', N'496138616573953', N'16392767785668608', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860376612865', N'496138616573953', N'16438800255291392', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860385001472', N'496138616573953', N'16438962738434048', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'77277860389195776', N'496138616573953', N'16439068543946752', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7750f9be48ee09cd561fce718219a3e2', N'ee8626f80f7c2619917b6236f3a7f02b', N'882a73768cfd7f78f3a37584f7299656', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7a5d31ba48fe3fb1266bf186dc5f7ba7', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'58857ff846e61794c69208e9d3a85466', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7ca833caa5eac837b7200d8b6de8b2e3', N'f6817f48af4fb3af11b9e8bf182f618b', N'fedfbf4420536cacc0218557d263dfea', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7d2ea745950be3357747ec7750c31c57', N'ee8626f80f7c2619917b6236f3a7f02b', N'2a470fc0c3954d9dbb61de6d80846549', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7de42bdc0b8c5446b7d428c66a7abc12', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'54dd5457a3190740005c1bfec55b1c34', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7e19d90cec0dd87aaef351b9ff8f4902', N'646c628b2b8295fbdab2d34044de0354', N'f9d3f4f27653a71c52faa9fb8070fbe7', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'7f862c47003eb20e8bad05f506371f92', N'ee8626f80f7c2619917b6236f3a7f02b', N'd7d6e2e4e2934f2c9385a623fd98c6f3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'812ed54661b1a24b81b58974691a73f5', N'e51758fa916c881624b046d26bd09230', N'e6bfd1fcabfd7942fdd05f076d1dad38', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'83f704524b21b6a3ae324b8736c65333', N'ee8626f80f7c2619917b6236f3a7f02b', N'7ac9eb9ccbde2f7a033cd4944272bf1e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'84d32474316a43b01256d6644e6e7751', N'ee8626f80f7c2619917b6236f3a7f02b', N'ec8d607d0156e198b11853760319c646', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'84eac2f113c23737128fb099d1d1da89', N'f6817f48af4fb3af11b9e8bf182f618b', N'03dc3d93261dda19fc86dd7ca486c6cf', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'85755a6c0bdff78b3860b52d35310c7f', N'e51758fa916c881624b046d26bd09230', N'c65321e57b7949b7a975313220de0422', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'86060e2867a5049d8a80d9fe5d8bc28b', N'f6817f48af4fb3af11b9e8bf182f618b', N'765dd244f37b804e3d00f475fd56149b', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8703a2410cddb713c33232ce16ec04b9', N'ee8626f80f7c2619917b6236f3a7f02b', N'1367a93f2c410b169faa7abcbad2f77c', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'884f147c20e003cc80ed5b7efa598cbe', N'f6817f48af4fb3af11b9e8bf182f618b', N'e5973686ed495c379d829ea8b2881fc6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'885c1a827383e5b2c6c4f8ca72a7b493', N'ee8626f80f7c2619917b6236f3a7f02b', N'4148ec82b6acd69f470bea75fe41c357', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8a60df8d8b4c9ee5fa63f48aeee3ec00', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'd7d6e2e4e2934f2c9385a623fd98c6f3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8b1e326791375f325d3e6b797753b65e', N'ee8626f80f7c2619917b6236f3a7f02b', N'2dbbafa22cda07fa5d169d741b81fe12', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8ce1022dac4e558ff9694600515cf510', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'08e6b9dc3c04489c8e1ff2ce6f105aa4', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8d154c2382a8ae5c8d1b84bd38df2a93', N'f6817f48af4fb3af11b9e8bf182f618b', N'd86f58e7ab516d3bc6bfb1fe10585f97', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8d848ca7feec5b7ebb3ecb32b2c8857a', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'4148ec82b6acd69f470bea75fe41c357', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8dd64f65a1014196078d0882f767cd85', N'f6817f48af4fb3af11b9e8bf182f618b', N'e3c13679c73a4f829bcff2aba8fd68b1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8e3dc1671abad4f3c83883b194d2e05a', N'f6817f48af4fb3af11b9e8bf182f618b', N'b1cb0a3fedf7ed0e4653cb5a229837ee', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8eec2c510f1ac9c5eee26c041b1f00ca', N'ee8626f80f7c2619917b6236f3a7f02b', N'58857ff846e61794c69208e9d3a85466', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'8f762ff80253f634b08cf59a77742ba4', N'ee8626f80f7c2619917b6236f3a7f02b', N'9502685863ab87f0ad1134142788a385', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'903b790e6090414343502c6dc393b7c9', N'ee8626f80f7c2619917b6236f3a7f02b', N'de13e0f6328c069748de7399fcc1dbbd', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'905bf419332ebcb83863603b3ebe30f0', N'f6817f48af4fb3af11b9e8bf182f618b', N'8fb8172747a78756c11916216b8b8066', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'90996d56357730e173e636b99fc48bea', N'ee8626f80f7c2619917b6236f3a7f02b', N'fb07ca05a3e13674dbf6d3245956da2e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'90e1c607a0631364eec310f3cc4acebd', N'ee8626f80f7c2619917b6236f3a7f02b', N'4f66409ef3bbd69c1d80469d6e2a885e', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9264104cee9b10c96241d527b2d0346d', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'54dd5457a3190740005c1bfec55b1c34', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9380121ca9cfee4b372194630fce150e', N'f6817f48af4fb3af11b9e8bf182f618b', N'65a8f489f25a345836b7f44b1181197a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'94911fef73a590f6824105ebf9b6cab3', N'f6817f48af4fb3af11b9e8bf182f618b', N'8b3bff2eee6f1939147f5c68292a1642', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9700d20dbc1ae3cbf7de1c810b521fe6', N'f6817f48af4fb3af11b9e8bf182f618b', N'ec8d607d0156e198b11853760319c646', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'980171fda43adfe24840959b1d048d4d', N'f6817f48af4fb3af11b9e8bf182f618b', N'd7d6e2e4e2934f2c9385a623fd98c6f3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'987c23b70873bd1d6dca52f30aafd8c2', N'f6817f48af4fb3af11b9e8bf182f618b', N'00a2a0ae65cdca5e93209cdbde97cbe6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'98f02353f91dd569e3c6b8fd6b4f4034', N'ee8626f80f7c2619917b6236f3a7f02b', N'6531cf3421b1265aeeeabaab5e176e6d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9b2ad767f9861e64a20b097538feafd3', N'f6817f48af4fb3af11b9e8bf182f618b', N'73678f9daa45ed17a3674131b03432fb', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9d8772c310b675ae43eacdbc6c7fa04a', N'a799c3b1b12dd3ed4bd046bfaef5fe6e', N'1663f3faba244d16c94552f849627d84', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9d980ec0489040e631a9c24a6af42934', N'f6817f48af4fb3af11b9e8bf182f618b', N'05b3c82ddb2536a4a5ee1a4c46b5abef', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'9f8311ecccd44e079723098cf2ffe1cc', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'693ce69af3432bd00be13c3971a57961', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a034ed7c38c996b880d3e78f586fe0ae', N'f6817f48af4fb3af11b9e8bf182f618b', N'c89018ea6286e852b424466fd92a2ffc', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a098e2acc3f90316f161f6648d085640', N'ee8626f80f7c2619917b6236f3a7f02b', N'e6bfd1fcabfd7942fdd05f076d1dad38', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a307a9349ad64a2eff8ab69582fa9be4', N'f6817f48af4fb3af11b9e8bf182f618b', N'0620e402857b8c5b605e1ad9f4b89350', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a5d25fdb3c62904a8474182706ce11a0', N'f6817f48af4fb3af11b9e8bf182f618b', N'418964ba087b90a84897b62474496b93', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a66feaaf128417ad762e946abccf27ec', N'ee8626f80f7c2619917b6236f3a7f02b', N'c6cf95444d80435eb37b2f9db3971ae6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a72c31a3913c736d4eca11d13be99183', N'e51758fa916c881624b046d26bd09230', N'a44c30db536349e91106223957e684eb', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'a7ab87eac0f8fafa2efa4b1f9351923f', N'ee8626f80f7c2619917b6236f3a7f02b', N'fedfbf4420536cacc0218557d263dfea', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'abdc324a2df9f13ee6e73d44c6e62bc8', N'ee8626f80f7c2619917b6236f3a7f02b', N'f1cb187abf927c88b89470d08615f5ac', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'acacce4417e5d7f96a9c3be2ded5b4be', N'f6817f48af4fb3af11b9e8bf182f618b', N'f9d3f4f27653a71c52faa9fb8070fbe7', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'aefc8c22e061171806e59cd222f6b7e1', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'e8af452d8948ea49d37c934f5100ae6a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'af60ac8fafd807ed6b6b354613b9ccbc', N'f6817f48af4fb3af11b9e8bf182f618b', N'58857ff846e61794c69208e9d3a85466', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b0c8a20800b8bf1ebdd7be473bceb44f', N'f6817f48af4fb3af11b9e8bf182f618b', N'58b9204feaf07e47284ddb36cd2d8468', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b128ebe78fa5abb54a3a82c6689bdca3', N'f6817f48af4fb3af11b9e8bf182f618b', N'aedbf679b5773c1f25e9f7b10111da73', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b131ebeafcfd059f3c7e542606ea9ff5', N'ee8626f80f7c2619917b6236f3a7f02b', N'e5973686ed495c379d829ea8b2881fc6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b21b07951bb547b09cc85624a841aea0', N'f6817f48af4fb3af11b9e8bf182f618b', N'4356a1a67b564f0988a484f5531fd4d9', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b2b2dcfff6986d3d7f890ea62d474651', N'ee8626f80f7c2619917b6236f3a7f02b', N'200006f0edf145a2b50eacca07585451', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b495a46fa0e0d4637abe0db7fd12fe1a', N'ee8626f80f7c2619917b6236f3a7f02b', N'717f6bee46f44a3897eca9abd6e2ec44', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'b64c4ab9cd9a2ea8ac1e9db5fb7cf522', N'f6817f48af4fb3af11b9e8bf182f618b', N'2aeddae571695cd6380f6d6d334d6e7d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'bbec16ad016efec9ea2def38f4d3d9dc', N'f6817f48af4fb3af11b9e8bf182f618b', N'13212d3416eb690c2e1d5033166ff47a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'bd30561f141f07827b836878137fddd8', N'e51758fa916c881624b046d26bd09230', N'65a8f489f25a345836b7f44b1181197a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'bea2986432079d89203da888d99b3f16', N'f6817f48af4fb3af11b9e8bf182f618b', N'54dd5457a3190740005c1bfec55b1c34', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c09373ebfc73fb5740db5ff02cba4f91', N'f6817f48af4fb3af11b9e8bf182f618b', N'339329ed54cf255e1f9392e84f136901', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c56fb1658ee5f7476380786bf5905399', N'f6817f48af4fb3af11b9e8bf182f618b', N'de13e0f6328c069748de7399fcc1dbbd', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c689539d20a445b0896270290c58d01f', N'e51758fa916c881624b046d26bd09230', N'13212d3416eb690c2e1d5033166ff47a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c6fee38d293b9d0596436a0cbd205070', N'f6817f48af4fb3af11b9e8bf182f618b', N'4f84f9400e5e92c95f05b554724c2b58', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c8571839e6b14796e661f3e2843b80b6', N'ee8626f80f7c2619917b6236f3a7f02b', N'45c966826eeff4c99b8f8ebfe74511fc', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c90b0b01c7ca454d2a1cb7408563e696', N'f6817f48af4fb3af11b9e8bf182f618b', N'882a73768cfd7f78f3a37584f7299656', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'c9d35261cccd67ab2932107a0967a7d7', N'e51758fa916c881624b046d26bd09230', N'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ced80e43584ce15e97bb07298e93020d', N'e51758fa916c881624b046d26bd09230', N'45c966826eeff4c99b8f8ebfe74511fc', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'cf1feb1bf69eafc982295ad6c9c8d698', N'f6817f48af4fb3af11b9e8bf182f618b', N'a2b11669e98c5fe54a53c3e3c4f35d14', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'cf2ef620217673e4042f695743294f01', N'f6817f48af4fb3af11b9e8bf182f618b', N'717f6bee46f44a3897eca9abd6e2ec44', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'cf43895aef7fc684669483ab00ef2257', N'f6817f48af4fb3af11b9e8bf182f618b', N'700b7f95165c46cc7a78bf227aa8fed3', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd03d792b0f312e7b490afc5cec3dd6c5', N'e51758fa916c881624b046d26bd09230', N'8fb8172747a78756c11916216b8b8066', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd281a95b8f293d0fa2a136f46c4e0b10', N'f6817f48af4fb3af11b9e8bf182f618b', N'5c8042bd6c601270b2bbd9b20bccc68b', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd37ad568e26f46ed0feca227aa9c2ffa', N'f6817f48af4fb3af11b9e8bf182f618b', N'9502685863ab87f0ad1134142788a385', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd3ddcacee1acdfaa0810618b74e38ef2', N'f6817f48af4fb3af11b9e8bf182f618b', N'c6cf95444d80435eb37b2f9db3971ae6', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd3fe195d59811531c05d31d8436f5c8b', N'1750a8fb3e6d90cb7957c02de1dc8e59', N'e8af452d8948ea49d37c934f5100ae6a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd5267597a4450f06d49d2fb63859641a', N'e51758fa916c881624b046d26bd09230', N'2dbbafa22cda07fa5d169d741b81fe12', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd83282192a69514cfe6161b3087ff962', N'f6817f48af4fb3af11b9e8bf182f618b', N'53a9230444d33de28aa11cc108fb1dba', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'd8a5c9079df12090e108e21be94b4fd7', N'f6817f48af4fb3af11b9e8bf182f618b', N'078f9558cdeab239aecb2bda1a8ed0d1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'dbc5dd836d45c5bc7bc94b22596ab956', N'f6817f48af4fb3af11b9e8bf182f618b', N'1939e035e803a99ceecb6f5563570fb2', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'dc83bb13c0e8c930e79d28b2db26f01f', N'f6817f48af4fb3af11b9e8bf182f618b', N'63b551e81c5956d5c861593d366d8c57', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'dc8fd3f79bd85bd832608b42167a1c71', N'f6817f48af4fb3af11b9e8bf182f618b', N'91c23960fab49335831cf43d820b0a61', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'de82e89b8b60a3ea99be5348f565c240', N'f6817f48af4fb3af11b9e8bf182f618b', N'56ca78fe0f22d815fabc793461af67b8', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'de8f43229e351d34af3c95b1b9f0a15d', N'f6817f48af4fb3af11b9e8bf182f618b', N'a400e4f4d54f79bf5ce160ae432231af', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'e258ca8bf7ee168b93bfee739668eb15', N'ee8626f80f7c2619917b6236f3a7f02b', N'fb367426764077dcf94640c843733985', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'e339f7db7418a4fd2bd2c113f1182186', N'ee8626f80f7c2619917b6236f3a7f02b', N'b1cb0a3fedf7ed0e4653cb5a229837ee', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'e3e922673f4289b18366bb51b6200f17', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'45c966826eeff4c99b8f8ebfe74511fc', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'e7467726ee72235baaeb47df04a35e73', N'f6817f48af4fb3af11b9e8bf182f618b', N'e08cb190ef230d5d4f03824198773950', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'eaef4486f1c9b0408580bbfa2037eb66', N'f6817f48af4fb3af11b9e8bf182f618b', N'2a470fc0c3954d9dbb61de6d80846549', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ec4bc97829ab56afd83f428b6dc37ff6', N'f6817f48af4fb3af11b9e8bf182f618b', N'200006f0edf145a2b50eacca07585451', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ec846a3f85fdb6813e515be71f11b331', N'f6817f48af4fb3af11b9e8bf182f618b', N'732d48f8e0abe99fe6a23d18a3171cd1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ec93bb06f5be4c1f19522ca78180e2ef', N'f6817f48af4fb3af11b9e8bf182f618b', N'265de841c58907954b8877fb85212622', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ecdd72fe694e6bba9c1d9fc925ee79de', N'f6817f48af4fb3af11b9e8bf182f618b', N'45c966826eeff4c99b8f8ebfe74511fc', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'edefd8d468f5727db465cf1b860af474', N'f6817f48af4fb3af11b9e8bf182f618b', N'6ad53fd1b220989a8b71ff482d683a5a', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'ef8bdd20d29447681ec91d3603e80c7b', N'f6817f48af4fb3af11b9e8bf182f618b', N'ae4fed059f67086fd52a73d913cf473d', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'f12b6c90e8913183d7ca547c66600891', N'e51758fa916c881624b046d26bd09230', N'aedbf679b5773c1f25e9f7b10111da73', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'f177acac0276329dc66af0c9ad30558a', N'f6817f48af4fb3af11b9e8bf182f618b', N'c2c356bf4ddd29975347a7047a062440', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'f17ab8ad1e71341140857ef4914ef297', N'21c5a3187763729408b40afb0d0fdfa8', N'732d48f8e0abe99fe6a23d18a3171cd1', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'f99f99cc3bc27220cdd4f5aced33b7d7', N'f6817f48af4fb3af11b9e8bf182f618b', N'655563cd64b75dcf52ef7bcdd4836953', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'fafe73c4448b977fe42880a6750c3ee8', N'f6817f48af4fb3af11b9e8bf182f618b', N'9cb91b8851db0cf7b19d7ecc2a8193dd', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'fced905c7598973b970d42d833f73474', N'f6817f48af4fb3af11b9e8bf182f618b', N'4875ebe289344e14844d8e3ea1edd73f', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'fd86f6b08eb683720ba499f9d9421726', N'ee8626f80f7c2619917b6236f3a7f02b', N'693ce69af3432bd00be13c3971a57961', null)
GO
GO
INSERT INTO [dbo].[sys_role_permission] ([id], [role_id], [permission_id], [data_rule_ids]) VALUES (N'fed41a4671285efb266cd404f24dd378', N'52b0cf022ac4187b2a70dfa4f8b2d940', N'00a2a0ae65cdca5e93209cdbde97cbe6', null)
GO
GO

-- ----------------------------
-- Table structure for sys_sms
-- ----------------------------
DROP TABLE [dbo].[sys_sms]
GO
CREATE TABLE [dbo].[sys_sms] (
[id] nvarchar(32) NOT NULL ,
[es_title] nvarchar(100) NULL ,
[es_type] nvarchar(1) NULL ,
[es_receiver] nvarchar(50) NULL ,
[es_param] nvarchar(1000) NULL ,
[es_content] nvarchar(MAX) NULL ,
[es_send_time] datetime2(7) NULL ,
[es_send_status] nvarchar(1) NULL ,
[es_send_num] int NULL ,
[es_result] nvarchar(255) NULL ,
[remark] nvarchar(500) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_title')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'消息标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_title'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'消息标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_title'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发送方式：1短信 2邮件 3微信'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发送方式：1短信 2邮件 3微信'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_receiver')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'接收人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_receiver'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'接收人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_receiver'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_param')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发送所需参数Json格式'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_param'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发送所需参数Json格式'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_param'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'推送内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'推送内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_send_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'推送时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'推送时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_send_status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'推送状态 0未推送 1推送成功 2推送失败 -1失败不再发送'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'推送状态 0未推送 1推送成功 2推送失败 -1失败不再发送'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_send_num')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'发送次数 超过5次不再发送'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_num'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'发送次数 超过5次不再发送'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_send_num'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'es_result')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'推送失败原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_result'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'推送失败原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'es_result'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'remark')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'remark'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'备注'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'remark'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_sms
-- ----------------------------
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402880e74dc2f361014dc2f8411e0001', N'消息推送测试333', N'2', N'411944058@qq.com', null, N'张三你好，你的订单4028d881436d514601436d521ae80165已付款!', N'2015-06-05 17:06:01.0000000', N'3', null, null, N'认证失败错误的用户名或者密码', N'admin', N'2015-06-05 17:05:59.0000000', N'admin', N'2015-11-19 22:30:39.0000000')
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402880ea533647b00153364e74770001', N'发个问候', N'3', N'admin', null, N'你好', N'2016-03-02 00:00:00.0000000', N'2', null, null, null, N'admin', N'2016-03-02 15:50:24.0000000', N'admin', N'2018-07-05 19:53:01.0000000')
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402880ee5a17e711015a17f3188e013f', N'消息推送测试333', N'2', N'411944058@qq.com', null, N'张三你好，你的订单4028d881436d514601436d521ae80165已付款!', null, N'2', null, null, null, N'admin', N'2017-02-07 17:41:31.0000000', N'admin', N'2017-03-10 11:37:05.0000000')
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402880f05ab649b4015ab64b9cd80012', N'消息推送测试333', N'2', N'411944058@qq.com', null, N'张三你好，你的订单4028d881436d514601436d521ae80165已付款!', N'2017-11-16 15:58:15.0000000', N'3', null, null, null, N'admin', N'2017-03-10 11:38:13.0000000', N'admin', N'2017-07-31 17:24:54.0000000')
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402880f05ab7b035015ab7c4462c0004', N'消息推送测试333', N'2', N'411944058@qq.com', null, N'张三你好，你的订单4028d881436d514601436d521ae80165已付款!', N'2017-11-16 15:58:15.0000000', N'3', null, null, null, N'admin', N'2017-03-10 18:29:37.0000000', null, null)
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402881f3646a472b01646a4a5af00001', N'催办：HR审批', N'3', N'admin', null, N'admin，您好！
请前待办任务办理事项！HR审批


===========================
此消息由系统发出', N'2018-07-05 19:53:35.0000000', N'2', null, null, null, N'admin', N'2018-07-05 19:53:35.0000000', N'admin', N'2018-07-07 13:45:24.0000000')
GO
GO
INSERT INTO [dbo].[sys_sms] ([id], [es_title], [es_type], [es_receiver], [es_param], [es_content], [es_send_time], [es_send_status], [es_send_num], [es_result], [remark], [create_by], [create_time], [update_by], [update_time]) VALUES (N'402881f3647da06c01647da43a940014', N'催办：HR审批', N'3', N'admin', null, N'admin，您好！
请前待办任务办理事项！HR审批


===========================
此消息由系统发出', N'2018-07-09 14:04:32.0000000', N'2', null, null, null, N'admin', N'2018-07-09 14:04:32.0000000', N'admin', N'2018-07-09 18:51:30.0000000')
GO
GO

-- ----------------------------
-- Table structure for sys_sms_template
-- ----------------------------
DROP TABLE [dbo].[sys_sms_template]
GO
CREATE TABLE [dbo].[sys_sms_template] (
[id] nvarchar(32) NOT NULL ,
[template_name] nvarchar(50) NULL ,
[template_code] nvarchar(32) NOT NULL ,
[template_type] nvarchar(1) NOT NULL ,
[template_content] nvarchar(1000) NOT NULL ,
[template_test_json] nvarchar(1000) NULL ,
[create_time] datetime2(7) NULL ,
[create_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'template_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'模板标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'模板标题'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'template_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'模板CODE'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'模板CODE'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'template_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'模板类型：1短信 2邮件 3微信'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'模板类型：1短信 2邮件 3微信'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'template_content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'模板内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'模板内容'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'template_test_json')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'模板测试json'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_test_json'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'模板测试json'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'template_test_json'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_sms_template', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_sms_template'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO

-- ----------------------------
-- Records of sys_sms_template
-- ----------------------------
INSERT INTO [dbo].[sys_sms_template] ([id], [template_name], [template_code], [template_type], [template_content], [template_test_json], [create_time], [create_by], [update_time], [update_by]) VALUES (N'4028608164691b000164693108140003', N'催办：${taskName}', N'SYS001', N'3', N'${userName}，您好！
请前待办任务办理事项！${taskName}


===========================
此消息由系统发出', N'{
"taskName":"HR审批",
"userName":"admin"
}', N'2018-07-05 14:46:18.0000000', N'admin', N'2018-07-05 18:31:34.0000000', N'admin')
GO
GO

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE [dbo].[sys_user]
GO
CREATE TABLE [dbo].[sys_user] (
[id] nvarchar(32) NOT NULL ,
[username] nvarchar(100) NULL ,
[realname] nvarchar(100) NULL ,
[password] nvarchar(255) NULL ,
[salt] nvarchar(45) NULL ,
[avatar] nvarchar(255) NULL ,
[birthday] datetime2(7) NULL ,
[sex] int NULL ,
[email] nvarchar(45) NULL ,
[phone] nvarchar(45) NULL ,
[org_code] nvarchar(100) NULL ,
[status] int NULL ,
[del_flag] nvarchar(1) NULL ,
[activiti_sync] nvarchar(6) NULL ,
[create_by] nvarchar(32) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(32) NULL ,
[update_time] datetime2(7) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'username')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'登录账号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'username'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'登录账号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'username'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'realname')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'真实姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'realname'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'真实姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'realname'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'password')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'密码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'password'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'密码'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'password'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'salt')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'md5密码盐'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'salt'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'md5密码盐'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'salt'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'avatar')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'头像'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'avatar'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'头像'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'avatar'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'birthday')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'生日'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'birthday'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'生日'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'birthday'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'sex')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'性别（1：男 2：女）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'sex'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'性别（1：男 2：女）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'sex'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'email')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'电子邮件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'email'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'电子邮件'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'email'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'phone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'电话'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'phone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'电话'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'phone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门code'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门code'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'org_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'状态(1：正常  2：冻结 ）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'状态(1：正常  2：冻结 ）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'del_flag')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'del_flag'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'删除状态（0，正常，1已删除）'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'del_flag'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'activiti_sync')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'同步工作流引擎1同步0不同步'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'activiti_sync'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'同步工作流引擎1同步0不同步'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'activiti_sync'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO [dbo].[sys_user] ([id], [username], [realname], [password], [salt], [avatar], [birthday], [sex], [email], [phone], [org_code], [status], [del_flag], [activiti_sync], [create_by], [create_time], [update_by], [update_time]) VALUES (N'42d153bffeea74f72a9c1697874fa4a7', N'test22', N'23232', N'ac52e15671a377cf', N'5FMD48RM', N'user/20190314/ly-plate-e_1552531617500.png', N'2019-02-09 00:00:00.0000000', N'1', N'zhangdaiscott@163.com', N'18611782222', null, N'1', N'0', N'1', N'admin', N'2019-01-26 18:01:10.0000000', N'admin', N'2019-03-23 15:05:50.0000000')
GO
GO
INSERT INTO [dbo].[sys_user] ([id], [username], [realname], [password], [salt], [avatar], [birthday], [sex], [email], [phone], [org_code], [status], [del_flag], [activiti_sync], [create_by], [create_time], [update_by], [update_time]) VALUES (N'a75d45a015c44384a04449ee80dc3503', N'jeecg', N'jeecg', N'3dd8371f3cf8240e', N'vDDkDzrK', N'user/20190220/e1fe9925bc315c60addea1b98eb1cb1349547719_1550656892940.jpg', null, N'2', null, null, null, N'1', N'0', N'1', N'admin', N'2019-02-13 16:02:36.0000000', N'admin', N'2019-04-09 15:47:36.0000000')
GO
GO
INSERT INTO [dbo].[sys_user] ([id], [username], [realname], [password], [salt], [avatar], [birthday], [sex], [email], [phone], [org_code], [status], [del_flag], [activiti_sync], [create_by], [create_time], [update_by], [update_time]) VALUES (N'e9ca23d68d884d4ebb19d07889727dae', N'admin', N'管理员', N'cb362cfeefbf3d8d', N'RCGTeGiH', N'user/20190119/logo-2_1547868176839.png', N'2018-12-05 00:00:00.0000000', N'1', N'11@qq.com', N'18566666666', N'A01', N'1', N'0', N'1', null, N'2022-06-21 17:54:10.0000000', N'admin', N'2019-05-20 15:52:06.5620000')
GO
GO
INSERT INTO [dbo].[sys_user] ([id], [username], [realname], [password], [salt], [avatar], [birthday], [sex], [email], [phone], [org_code], [status], [del_flag], [activiti_sync], [create_by], [create_time], [update_by], [update_time]) VALUES (N'f0019fdebedb443c98dcb17d88222c38', N'zhagnxiao', N'张小红', N'f898134e5e52ae11a2ffb2c3b57a4e90', N'go3jJ4zX', N'user/20190401/20180607175028Fn1Lq7zw_1554118444672.png', N'2019-04-01 00:00:00.0000000', null, null, null, null, N'1', N'0', N'1', N'admin', N'2023-10-01 19:34:10.0000000', N'admin', N'2019-04-10 22:00:22.0000000')
GO
GO

-- ----------------------------
-- Table structure for sys_user_agent
-- ----------------------------
DROP TABLE [dbo].[sys_user_agent]
GO
CREATE TABLE [dbo].[sys_user_agent] (
[id] nvarchar(32) NOT NULL ,
[user_name] nvarchar(100) NULL ,
[agent_user_name] nvarchar(100) NULL ,
[start_time] datetime2(7) NULL ,
[end_time] datetime2(7) NULL ,
[status] nvarchar(2) NULL ,
[create_name] nvarchar(50) NULL ,
[create_by] nvarchar(50) NULL ,
[create_time] datetime2(7) NULL ,
[update_name] nvarchar(50) NULL ,
[update_by] nvarchar(50) NULL ,
[update_time] datetime2(7) NULL ,
[sys_org_code] nvarchar(50) NULL ,
[sys_company_code] nvarchar(50) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户代理人设置'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户代理人设置'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'序号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'序号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'user_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'user_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'user_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'agent_user_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'代理人用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'agent_user_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'代理人用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'agent_user_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'start_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'代理开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'start_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'代理开始时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'start_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'end_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'代理结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'end_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'代理结束时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'end_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'状态0无效1有效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'状态0无效1有效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'create_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'update_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人登录名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'sys_org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'所属部门'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'sys_org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'所属部门'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'sys_org_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_agent', 
'COLUMN', N'sys_company_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'所属公司'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'sys_company_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'所属公司'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_agent'
, @level2type = 'COLUMN', @level2name = N'sys_company_code'
GO

-- ----------------------------
-- Records of sys_user_agent
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE [dbo].[sys_user_depart]
GO
CREATE TABLE [dbo].[sys_user_depart] (
[ID] nvarchar(32) NOT NULL ,
[user_id] nvarchar(32) NULL ,
[dep_id] nvarchar(32) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_depart', 
'COLUMN', N'ID')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'ID'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'ID'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_depart', 
'COLUMN', N'user_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'user_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'user_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_depart', 
'COLUMN', N'dep_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'dep_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_depart'
, @level2type = 'COLUMN', @level2name = N'dep_id'
GO

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'0c42ba309c2c4cad35836ec2336676fa', N'42d153bffeea74f72a9c1697874fa4a7', N'6d35e179cd814e3299bd588ea7daed3f')
GO
GO
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'2835834d133f9118ee87a666e0f5501e', N'a75d45a015c44384a04449ee80dc3503', N'a7d7e77e06c84325a40932163adcdaa6')
GO
GO
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'1f3a0267811327b9eca86b0cc2b956f3', N'bcbe1290783a469a83ae3bd8effe15d4', N'5159cde220114246b045e574adceafe9')
GO
GO
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'ea8232c4554b0b19c96a7e94a2eb5aab', N'e9ca23d68d884d4ebb19d07889727dae', N'c6d7cb4deeac411cb3384b1b31278596')
GO
GO
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'ac52f23ae625eb6560c9227170b88166', N'f0019fdebedb443c98dcb17d88222c38', N'57197590443c44f083d42ae24ef26a2c')
GO
GO
INSERT INTO [dbo].[sys_user_depart] ([ID], [user_id], [dep_id]) VALUES (N'179660a8b9a122f66b73603799a10924', N'f0019fdebedb443c98dcb17d88222c38', N'67fc001af12a4f9b8458005d3f19934a')
GO
GO

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE [dbo].[sys_user_role]
GO
CREATE TABLE [dbo].[sys_user_role] (
[id] nvarchar(32) NOT NULL ,
[user_id] nvarchar(32) NULL ,
[role_id] nvarchar(32) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_role', 
NULL, NULL)) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户角色表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户角色表'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_role', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_role', 
'COLUMN', N'user_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'user_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'user_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sys_user_role', 
'COLUMN', N'role_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'角色id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'role_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'角色id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sys_user_role'
, @level2type = 'COLUMN', @level2name = N'role_id'
GO

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'b3ffd9311a1ca296c44e2409b547384f', N'01b802058ea94b978a2c96f4807f6b48', N'1')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'0ede6d23d53bc7dc990346ff14faabee', N'3db4cf42353f4e868b7ccfeef90505d2', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'e78d210d24aaff48e0a736e2ddff4cdc', N'3e177fede453430387a8279ced685679', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'f2de3ae7b5efd8345581aa802a6675d6', N'41b1be8d4c52023b0798f51164ca682d', N'e51758fa916c881624b046d26bd09230')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'6f9da7310489bac1e5f95e0efe92b4ce', N'42d153bffeea74f72a9c1697874fa4a7', N'e51758fa916c881624b046d26bd09230')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'f2922a38ba24fb53749e45a0c459adb3', N'439ae3e9bcf7418583fcd429cadb1d72', N'1')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'f72c6190b0722e798147e73c776c6ac9', N'439ae3e9bcf7418583fcd429cadb1d72', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'ee45d0343ecec894b6886effc92cb0b7', N'4d8fef4667574b24a9ccfedaf257810c', N'f6817f48af4fb3af11b9e8bf182f618b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'be2639167ede09379937daca7fc3bb73', N'526f300ab35e44faaed54a9fb0742845', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'31af310584bd5795f76b1fe8c38294a0', N'70f5dcf03f36471dabba81381919291f', N'e51758fa916c881624b046d26bd09230')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'8d7846ec783e157174e4ce2949231a65', N'7ee6630e89d17afbf6d12150197b578d', N'e51758fa916c881624b046d26bd09230')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'79d66ef7aa137cfa9957081a1483009d', N'9a668858c4c74cf5a2b25ad9608ba095', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'f3a4ca33848daba3e43490707ae859e7', N'a75d45a015c44384a04449ee80dc3503', N'e51758fa916c881624b046d26bd09230')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'fe38580871c5061ba59d5c03a0840b0e', N'a75d45a015c44384a04449ee80dc3503', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'094426fcaf5d2ddde4305d3e32044809', N'e9ca23d68d884d4ebb19d07889727dae', N'f6817f48af4fb3af11b9e8bf182f618b')
GO
GO
INSERT INTO [dbo].[sys_user_role] ([id], [user_id], [role_id]) VALUES (N'd2233e5be091d39da5abb0073c766224', N'f0019fdebedb443c98dcb17d88222c38', N'ee8626f80f7c2619917b6236f3a7f02b')
GO
GO

-- ----------------------------
-- Table structure for test_person
-- ----------------------------
DROP TABLE [dbo].[test_person]
GO
CREATE TABLE [dbo].[test_person] (
[id] nvarchar(36) NOT NULL ,
[create_by] nvarchar(50) NULL ,
[create_time] datetime2(7) NULL ,
[update_by] nvarchar(50) NULL ,
[update_time] datetime2(7) NULL ,
[sex] nvarchar(32) NULL ,
[name] nvarchar(200) NULL ,
[content] nvarchar(MAX) NULL ,
[be_date] datetime2(7) NULL ,
[qj_days] int NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'update_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'update_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新人'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'update_by'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'update_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'update_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'update_time'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'sex')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'性别'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'sex'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'性别'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'sex'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'用户名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'content')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'content'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假原因'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'content'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'be_date')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'be_date'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'be_date'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'test_person', 
'COLUMN', N'qj_days')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'请假天数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'qj_days'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'请假天数'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'test_person'
, @level2type = 'COLUMN', @level2name = N'qj_days'
GO

-- ----------------------------
-- Records of test_person
-- ----------------------------
INSERT INTO [dbo].[test_person] ([id], [create_by], [create_time], [update_by], [update_time], [sex], [name], [content], [be_date], [qj_days]) VALUES (N'8ca668defdae47df8649a5477ae08b05', N'admin', N'2019-04-12 09:51:37.0000000', null, null, N'1', N'zhangdaiscott', N'dsdsd', N'2019-04-12 00:00:00.0000000', N'2')
GO
GO

-- ----------------------------
-- Indexes structure for table demo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table demo
-- ----------------------------
ALTER TABLE [dbo].[demo] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table jeecg_monthly_growth_analysis
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table jeecg_monthly_growth_analysis
-- ----------------------------
ALTER TABLE [dbo].[jeecg_monthly_growth_analysis] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table jeecg_order_customer
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table jeecg_order_customer
-- ----------------------------
ALTER TABLE [dbo].[jeecg_order_customer] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table jeecg_order_main
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table jeecg_order_main
-- ----------------------------
ALTER TABLE [dbo].[jeecg_order_main] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table jeecg_order_ticket
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table jeecg_order_ticket
-- ----------------------------
ALTER TABLE [dbo].[jeecg_order_ticket] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table jeecg_project_nature_income
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table jeecg_project_nature_income
-- ----------------------------
ALTER TABLE [dbo].[jeecg_project_nature_income] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_CALENDARS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_CALENDARS] ADD PRIMARY KEY ([SCHED_NAME], [CALENDAR_NAME])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_CRON_TRIGGERS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] ADD PRIMARY KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_FIRED_TRIGGERS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_FIRED_TRIGGERS] ADD PRIMARY KEY ([SCHED_NAME], [ENTRY_ID])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_JOB_DETAILS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_JOB_DETAILS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_JOB_DETAILS] ADD PRIMARY KEY ([SCHED_NAME], [JOB_NAME], [JOB_GROUP])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_LOCKS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_LOCKS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_LOCKS] ADD PRIMARY KEY ([SCHED_NAME], [LOCK_NAME])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] ADD PRIMARY KEY ([SCHED_NAME], [TRIGGER_GROUP])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_SCHEDULER_STATE
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_SCHEDULER_STATE
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_SCHEDULER_STATE] ADD PRIMARY KEY ([SCHED_NAME], [INSTANCE_NAME])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] ADD PRIMARY KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] ADD PRIMARY KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP])
GO

-- ----------------------------
-- Indexes structure for table QRTZ_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table QRTZ_TRIGGERS
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_TRIGGERS] ADD PRIMARY KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP])
GO

-- ----------------------------
-- Indexes structure for table sys_announcement
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sys_announcement
-- ----------------------------
ALTER TABLE [dbo].[sys_announcement] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_data_log
-- ----------------------------
CREATE INDEX [sindex] ON [dbo].[sys_data_log]
([data_table] ASC, [data_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_data_log
-- ----------------------------
ALTER TABLE [dbo].[sys_data_log] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_depart
-- ----------------------------
CREATE INDEX [index_depart_depart_order] ON [dbo].[sys_depart]
([depart_order] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_depart_org_code] ON [dbo].[sys_depart]
([org_code] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_depart_parent_id] ON [dbo].[sys_depart]
([parent_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_depart
-- ----------------------------
ALTER TABLE [dbo].[sys_depart] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_dict
-- ----------------------------
CREATE UNIQUE INDEX [indextable_dict_code] ON [dbo].[sys_dict]
([dict_code] ASC) 
WITH (IGNORE_DUP_KEY = ON, STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_dict
-- ----------------------------
ALTER TABLE [dbo].[sys_dict] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_dict_item
-- ----------------------------
CREATE INDEX [index_table_dict_id] ON [dbo].[sys_dict_item]
([dict_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_table_dict_status] ON [dbo].[sys_dict_item]
([status] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_table_sort_order] ON [dbo].[sys_dict_item]
([sort_order] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_dict_item
-- ----------------------------
ALTER TABLE [dbo].[sys_dict_item] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_log
-- ----------------------------
CREATE INDEX [index_log_type] ON [dbo].[sys_log]
([log_type] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_logt_ype] ON [dbo].[sys_log]
([log_type] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_operate_type] ON [dbo].[sys_log]
([operate_type] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_table_userid] ON [dbo].[sys_log]
([userid] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_log
-- ----------------------------
ALTER TABLE [dbo].[sys_log] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_permission
-- ----------------------------
CREATE INDEX [index_prem_del_flag] ON [dbo].[sys_permission]
([del_flag] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_prem_is_leaf] ON [dbo].[sys_permission]
([is_leaf] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_prem_is_route] ON [dbo].[sys_permission]
([is_route] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_prem_pid] ON [dbo].[sys_permission]
([parent_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_prem_sort_no] ON [dbo].[sys_permission]
([sort_no] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_permission
-- ----------------------------
ALTER TABLE [dbo].[sys_permission] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_permission_data_rule
-- ----------------------------
CREATE INDEX [index_fucntionid] ON [dbo].[sys_permission_data_rule]
([permission_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_permission_data_rule
-- ----------------------------
ALTER TABLE [dbo].[sys_permission_data_rule] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_quartz_job
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sys_quartz_job
-- ----------------------------
ALTER TABLE [dbo].[sys_quartz_job] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_role
-- ----------------------------
CREATE UNIQUE INDEX [index_role_code] ON [dbo].[sys_role]
([role_code] ASC) 
WITH (IGNORE_DUP_KEY = ON, STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE [dbo].[sys_role] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_role_permission
-- ----------------------------
CREATE INDEX [index_group_per_id] ON [dbo].[sys_role_permission]
([permission_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_group_role_id] ON [dbo].[sys_role_permission]
([role_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_group_role_per_id] ON [dbo].[sys_role_permission]
([role_id] ASC, [permission_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_role_permission
-- ----------------------------
ALTER TABLE [dbo].[sys_role_permission] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_sms
-- ----------------------------
CREATE INDEX [index_receiver] ON [dbo].[sys_sms]
([es_receiver] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_sendtime] ON [dbo].[sys_sms]
([es_send_time] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_status] ON [dbo].[sys_sms]
([es_send_status] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_type] ON [dbo].[sys_sms]
([es_type] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_sms
-- ----------------------------
ALTER TABLE [dbo].[sys_sms] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_sms_template
-- ----------------------------
CREATE UNIQUE INDEX [uniq_templatecode] ON [dbo].[sys_sms_template]
([template_code] ASC) 
WITH (IGNORE_DUP_KEY = ON, STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_sms_template
-- ----------------------------
ALTER TABLE [dbo].[sys_sms_template] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_user
-- ----------------------------
CREATE INDEX [index_user_del_flag] ON [dbo].[sys_user]
([del_flag] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE UNIQUE INDEX [index_user_name] ON [dbo].[sys_user]
([username] ASC) 
WITH (IGNORE_DUP_KEY = ON, STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_user_status] ON [dbo].[sys_user]
([status] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE [dbo].[sys_user] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_user_agent
-- ----------------------------
CREATE INDEX [begintime_index] ON [dbo].[sys_user_agent]
([start_time] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [endtime_index] ON [dbo].[sys_user_agent]
([end_time] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [statux_index] ON [dbo].[sys_user_agent]
([status] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE UNIQUE INDEX [uniq_username] ON [dbo].[sys_user_agent]
([user_name] ASC) 
WITH (IGNORE_DUP_KEY = ON, STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_user_agent
-- ----------------------------
ALTER TABLE [dbo].[sys_user_agent] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table sys_user_depart
-- ----------------------------
CREATE INDEX [index_depart_groupk_uidanddid] ON [dbo].[sys_user_depart]
([user_id] ASC, [dep_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_depart_groupk_userid] ON [dbo].[sys_user_depart]
([user_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index_depart_groupkorgid] ON [dbo].[sys_user_depart]
([dep_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_user_depart
-- ----------------------------
ALTER TABLE [dbo].[sys_user_depart] ADD PRIMARY KEY ([ID])
GO

-- ----------------------------
-- Indexes structure for table sys_user_role
-- ----------------------------
CREATE INDEX [index2_groupuu_ole_id] ON [dbo].[sys_user_role]
([role_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index2_groupuu_user_id] ON [dbo].[sys_user_role]
([user_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO
CREATE INDEX [index2_groupuu_useridandroleid] ON [dbo].[sys_user_role]
([user_id] ASC, [role_id] ASC) 
WITH (STATISTICS_NORECOMPUTE = ON)
GO

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE [dbo].[sys_user_role] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table test_person
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table test_person
-- ----------------------------
ALTER TABLE [dbo].[test_person] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[QRTZ_BLOB_TRIGGERS]
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_BLOB_TRIGGERS] ADD FOREIGN KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) REFERENCES [dbo].[QRTZ_TRIGGERS] ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) ON DELETE CASCADE ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[QRTZ_CRON_TRIGGERS]
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] ADD FOREIGN KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) REFERENCES [dbo].[QRTZ_TRIGGERS] ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) ON DELETE CASCADE ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[QRTZ_SIMPLE_TRIGGERS]
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] ADD FOREIGN KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) REFERENCES [dbo].[QRTZ_TRIGGERS] ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) ON DELETE CASCADE ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[QRTZ_SIMPROP_TRIGGERS]
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] ADD FOREIGN KEY ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) REFERENCES [dbo].[QRTZ_TRIGGERS] ([SCHED_NAME], [TRIGGER_NAME], [TRIGGER_GROUP]) ON DELETE CASCADE ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[QRTZ_TRIGGERS]
-- ----------------------------
ALTER TABLE [dbo].[QRTZ_TRIGGERS] ADD FOREIGN KEY ([SCHED_NAME], [JOB_NAME], [JOB_GROUP]) REFERENCES [dbo].[QRTZ_JOB_DETAILS] ([SCHED_NAME], [JOB_NAME], [JOB_GROUP]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
