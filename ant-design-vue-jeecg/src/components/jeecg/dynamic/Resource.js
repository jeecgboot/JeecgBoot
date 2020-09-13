
/**js编辑器关键词用于提示*/
const js_keyword = [
  'that',
  'getAction','postAction','deleteAction',
  'beforeAdd','beforeEdit','beforeDelete','mounted','created','show'
]

/**js编辑器 方法名用于提示*/
const js_method = [
  '.getSelectOptions','.changeOptions','.triggleChangeValues','.immediateEnhance ','.simpleDateFormat','.lodash'
]

/**sql编辑器 表名字段名用于提示*/
const sql_keyword = {
  sys_user: ['USERNAME', 'REALNAME', 'ID','BIRTHDAY','AGE'],
  demo: ['name', 'age', 'id', 'sex']
}

export {
  js_keyword,
  js_method,
  sql_keyword
}