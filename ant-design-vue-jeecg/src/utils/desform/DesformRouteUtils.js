import router from '@/router'
import { getAction } from '@api/manage'

/** 表单设计器路由类型 */
export const DESFORM_ROUTE_TYPE = {
  /** 跳转到表单 */
  form: '1',
  /** 跳转到菜单 */
  menu: '2',
  /** 跳转到外部链接 */
  href: '3',
}
/** 表单设计器路由跳转带过来的ID名字 */
export const DESFORM_ROUTE_DATA_ID = 'routeDataId'

/**
 * 获取当前路由或指定路由的 routeDataId
 *
 * @param $route 路由对象，默认为当前路由
 */
export function getDesformRouteDataId($route) {
  if (arguments.length === 0) {
    $route = router.currentRoute
  }
  if ($route) {
    return $route.query[DESFORM_ROUTE_DATA_ID]
  }
  return null
}

/**
 * 根据路由跳转带过来的ID来获取表单设计器的数据
 * @param param 表单设计器路由跳转带过来的ID（可以直接传id字符串，也可以传 this.$route，自动从$route里获取id，如果不传就获取当前路由的参数）
 * @returns {Promise<void>}
 */
export async function getDesformDataByRouteDataId(param) {
  if (!param) {
    param = router.currentRoute
  }
  let id
  if (typeof param == 'string') {
    id = param
  } else if (typeof param.query === 'object') {
    id = getDesformRouteDataId(param)
    if (!id) {
      // 当前$route.query里没有带表单设计器路由ID，直接返回null
      return null
    }
  } else {
    throw new Error('传递的参数不能识别，可以直接传id字符串，也可以传 this.$route，自动从$route里获取id，如果不传就获取当前路由的参数')
  }
  let url = `/desform/data/queryById?id=${id}`
  let { success, result, message } = await getAction(url)
  if (success) {
    result.desformDataJson = JSON.parse(result.desformDataJson)
    return result
  } else {
    throw new Error('表单设计器路由数据获取失败：' + message)
  }
}