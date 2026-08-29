--update-begin---author:kriptoburak---date:20260824---for:【issues/9846】增加Xquik推文搜索插件---
INSERT INTO `airag_mcp`
    (`id`, `icon`, `name`, `descr`, `category`, `type`, `endpoint`, `headers`, `tools`, `status`, `synced`, `metadata`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`, `tenant_id`)
SELECT
    '2080119897947770881',
    NULL,
    'Xquik推文搜索',
    '搜索公开X（Twitter）推文。支持关键词、用户和X搜索语法。设置JEECG_PLUGIN_XQUIK_API_KEY后使用。Not affiliated with X Corp.',
    'plugin',
    'api',
    'https://xquik.com/api/v1',
    CONCAT('{"x-api-key":"$', '{JEECG_PLUGIN_XQUIK_API_KEY}","xquik-api-contract":"2026-04-29"}'),
    '[{"name":"search_x_tweets","description":"按关键词、用户或X搜索语法搜索公开推文","path":"/x/tweets/search","method":"GET","enabled":true,"parameters":[{"name":"q","description":"关键词、用户名或X搜索表达式","type":"String","location":"Query","required":true,"defaultValue":""},{"name":"queryType","description":"排序方式：Latest或Top，默认Latest","type":"String","location":"Query","required":false,"defaultValue":"Latest"},{"name":"limit","description":"结果上限，建议1至100","type":"Number","location":"Query","required":false,"defaultValue":"20"},{"name":"cursor","description":"上一页返回的next_cursor，分页时原样传递","type":"String","location":"Query","required":false,"defaultValue":""}],"responses":[{"name":"tweets","description":"公开推文列表","type":"Array"},{"name":"has_next_page","description":"是否还有下一页","type":"Boolean"},{"name":"next_cursor","description":"下一页游标","type":"String"}]}]',
    'enable',
    1,
    '{"tool_count":1}',
    'admin',
    NOW(),
    'admin',
    NOW(),
    '',
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM `airag_mcp`
    WHERE `name` = 'Xquik推文搜索'
      AND `category` = 'plugin'
);
--update-end---author:kriptoburak---date:20260824---for:【issues/9846】增加Xquik推文搜索插件---
