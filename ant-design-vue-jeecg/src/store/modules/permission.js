import { asyncRouterMap, constantRouterMap } from "@/config/router.config"

/**
 * 过滤账户是否拥有某一个权限，并将菜单从加载列表移除
 *
 * @param permission
 * @param route
 * @returns {boolean}
 */
function hasPermission(permission, route) {
  if (route.meta && route.meta.permission) {
    let flag = -1
    for (let i = 0, len = permission.length; i < len; i++) {
      flag = route.meta.permission.indexOf(permission[i])
      if (flag >= 0) {
        return true
      }
    }
    return false
  }
  return true
}

/**
 * 单账户多角色时，使用该方法可过滤角色不存在的菜单
 *
 * @param roles
 * @param route
 * @returns {*}
 */
// eslint-disable-next-line
function hasRole(roles, route) {
  if (route.meta && route.meta.roles) {
    return route.meta.roles.indexOf(roles.id)
  } else {
    return true
  }
}

function filterAsyncRouter(routerMap, roles) {
  const accessedRouters = routerMap.filter(route => {
    if (hasPermission(roles.permissionList, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, roles)
      }
      return true
    }
    return false
  })
  return accessedRouters
}


const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, data) => {
      state.addRouters = data
      state.routers = constantRouterMap.concat(data)
      //console.log('-----mutations---SET_ROUTERS----', data)
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { roles } = data
        console.log('-----mutations---data----', data)
        let accessedRouters
        accessedRouters = filterAsyncRouter(asyncRouterMap, roles)
        console.log('-----mutations---accessedRouters----', accessedRouters)
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    },
    // 动态添加主界面路由，需要缓存
    UpdateAppRouter({ commit }, routes) {
      return new Promise(resolve => {
        //const [ roles ] = routes.constRoutes
        let routelist = routes.constRoutes;
        commit('SET_ROUTERS', routelist)
        resolve()
      })
    }
  }
}

export default permission