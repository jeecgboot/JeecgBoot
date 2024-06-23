/**
 * JInput组件类型
 */
export enum JInputTypeEnum {
  //模糊
  JINPUT_QUERY_LIKE = 'like',
  //非
  JINPUT_QUERY_NE = 'ne',
  //大于等于
  JINPUT_QUERY_GE = 'ge',
  //小于等于
  JINPUT_QUERY_LE = 'le',
}

/**
 * 面板设计器需要的常量定义
 */
export enum JDragConfigEnum {
  //baseURL
  DRAG_BASE_URL = 'drag-base-url',
  //拖拽缓存前缀
  DRAG_CACHE_PREFIX = 'drag-cache:',
}
