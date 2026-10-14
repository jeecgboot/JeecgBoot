-- ---author:scott---date:20261014-----for:OpenAPI授权管理内置的演示AK/SK为测试数据，清空其SK（代码已拒绝空SK）并标记名称，需在授权管理重新生成AK/SK后才能调用；仅处理SK仍为出厂值的记录，已重新生成的不受影响
UPDATE `open_api_auth`
   SET `sk` = '',
       `name` = '【测试数据】请重新生成AK/SK后使用'
 WHERE `id` = '1922164194775056386'
   AND `sk` = '4hV5dBrZtmGAtPdbA5yseaeKRYNpzGsS';
