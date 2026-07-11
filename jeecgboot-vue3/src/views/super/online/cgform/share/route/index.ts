import type {RouteRecordRaw} from 'vue-router';
import {PageEnum} from "@/enums/pageEnum";

// 外部链接路由名称（新增）
export const SHARE_ADD_ROUTER_NAME = 'online-cgform-@formId-share-add'

// 外部链接路由名称（编辑）
export const SHARE_UPDATE_ROUTER_NAME = 'online-cgform-@formId-share-u-@dataId'

// 外部链接路由名称（详情）
export const SHARE_DETAIL_ROUTER_NAME = 'online-cgform-@formId-share-d-@dataId'

export const SHARE_ROUTE: RouteRecordRaw = {
  path: "/online/cgform/share",
  name: "online-cgform-share",
  component: () => import("../layouts/default/index.vue"),
  meta: {
    title: 'Online表单外部页面',
    ignoreAuth: true,
  },
  children: [
    {
      path: ':id/add',
      name: SHARE_ADD_ROUTER_NAME,
      component: () => import("../components/add/ShareAddView.vue"),
      meta: {
        title: 'Online表单外部新增页面',
      },
    },
    {
      path: ':id/u/:dataId',
      name: SHARE_UPDATE_ROUTER_NAME,
      component: () => import("../components/edit/ShareEditView.vue"),
      meta: {
        title: 'Online表单外部编辑页面',
      },
    },
    {
      path: ':id/d/:dataId',
      name: SHARE_DETAIL_ROUTER_NAME,
      component: () => import("../components/edit/ShareEditView.vue"),
      meta: {
        title: 'Online表单外部详情页面',
      },
    },
  ]
}


// 外部链接路由名称（详情）
export const SHARE_LOGIN__ROUTER_NAME = 'online-cgform-share-login'

export const SHARE_LOGIN_ROUTE: RouteRecordRaw = {
  path: "/online/cgform/share/login",
  name: SHARE_LOGIN__ROUTER_NAME,
  // component: () => import("../layouts/login/index.vue"),
  component: () => import("../layouts/login/AppLogin.vue"),
  meta: {
    title: '登录 · Online表单',
    ignoreAuth: true,
  },
}

//
// Online表单外部链接页面
const ONLINE_CGFORM_SHARE = '/online/cgform/share';

export async function routerBeforeEach(to: any, _from: any) {
  // 如果是登录路由
  if (to.path === PageEnum.BASE_LOGIN) {
    // 获取 redirect
    let redirect = ((redirect: string) => {
      if (!redirect) {
        return '';
      }
      // 判断 redirect 是否是 online表单 外部页面
      if (redirect.startsWith(ONLINE_CGFORM_SHARE)) {
        return redirect;
      }
      redirect = decodeURIComponent(redirect)
      if (redirect.startsWith(ONLINE_CGFORM_SHARE)) {
        return redirect;
      }
      return '';
    })(to.query.redirect as string)
    // 如果是则跳转到 online表单 外部专属登录页面
    if (redirect) {
      redirect = redirect.split('?')[0];
      return {
        name: SHARE_LOGIN__ROUTER_NAME,
        query: {
          redirect: encodeURIComponent(redirect),
        }
      };
    }
  }
}
