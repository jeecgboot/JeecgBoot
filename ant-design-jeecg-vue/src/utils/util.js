import { isURL } from '@/utils/validate'

export function timeFix() {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour < 20 ? '下午好' : '晚上好')))
}

export function welcome() {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  let index = Math.floor((Math.random()*arr.length))
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent() {
  let event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

/**
 * 过滤对象中为空的属性
 * @param obj
 * @returns {*}
 */
export function filterObj(obj) {
  if (!(typeof obj == 'object')) {
    return;
  }

  for ( var key in obj) {
    if (obj.hasOwnProperty(key)
      && (obj[key] == null || obj[key] == undefined || obj[key] === '')) {
      delete obj[key];
    }
  }
  return obj;
}

/**
 * 时间格式化
 * @param value
 * @param fmt
 * @returns {*}
 */
export function formatDate(value, fmt) {
  var regPos = /^\d+(\.\d+)?$/;
  if(regPos.test(value)){
    //如果是数字
    let getDate = new Date(value);
    let o = {
      'M+': getDate.getMonth() + 1,
      'd+': getDate.getDate(),
      'h+': getDate.getHours(),
      'm+': getDate.getMinutes(),
      's+': getDate.getSeconds(),
      'q+': Math.floor((getDate.getMonth() + 3) / 3),
      'S': getDate.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (getDate.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    for (let k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
    }
    return fmt;
  }else{
    //TODO
    value = value.trim();
    return value.substr(0,fmt.length);
  }
}

// 生成首页路由
export function generateIndexRouter(data) {
let indexRouter = [{
          path: '/',
          name: 'dashboard',
          //component: () => import('@/components/layouts/BasicLayout'),
          component: resolve => require(['@/components/layouts/TabLayout'], resolve),
          meta: { title: '首页' },
          redirect: '/dashboard/analysis',
          children: [
            ...generateChildRouters(data)
          ]
        },{
          "path": "*", "redirect": "/404", "hidden": true
        }]
  return indexRouter;
}

// 生成嵌套路由（子路由）

function  generateChildRouters (data) {
  const routers = [];
  for (var item of data) {
    let component = "";
    if(item.component.indexOf("layouts")>=0){
       component = "components/"+item.component;
    }else{
       component = "views/"+item.component;
    }

    // eslint-disable-next-line
    let URL = (item.meta.url|| '').replace(/{{([^}}]+)?}}/g, (s1, s2) => eval(s2)) // URL支持{{ window.xxx }}占位符变量
    if (isURL(URL)) {
      item.meta.url = URL;
    }

    let menu =  {
      path: item.path,
      name: item.name,
      redirect:item.redirect,
      component: resolve => require(['@/' + component+'.vue'], resolve),
      hidden:item.hidden,
      //component:()=> import(`@/views/${item.component}.vue`),
      meta: {
        title:item.meta.title ,
        icon: item.meta.icon,
        url:item.meta.url ,
        permissionList:item.meta.permissionList
      }
    }
    if(item.alwaysShow){
      menu.alwaysShow = true;
    }
    if (item.children && item.children.length > 0) {
      menu.children = [...generateChildRouters( item.children)];
    }
    //--update-begin----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
    //判断是否生成路由
    if(item.route && item.route === '0'){
      console.log(' 不生成路由 item.route：  '+item.route);
      console.log(' 不生成路由 item.path：  '+item.path);
    }else{
      routers.push(menu);
    }
    //--update-end----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
  }
  return routers
}

/**
 * 深度克隆对象、数组
 * @param obj 被克隆的对象
 * @return 克隆后的对象
 */
export function cloneObject(obj) {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * 随机生成数字
 * @param min 最小值
 * @param max 最大值
 * @return int 生成后的数字
 */
export function randomNumber(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min)
}

/**
 * 随机生成字符串
 * @param length 字符串的长度
 * @param chats 可选字符串区间（只会生成传入的字符串中的字符）
 * @return string 生成的字符串
 */
export function randomString(length, chats) {
  if (!length) length = 1
  if (!chats) chats = '0123456789qwertyuioplkjhgfdsazxcvbnm'
  let str = ''
  for (let i = 0; i < length; i++) {
    let num = randomNumber(0, chats.length - 1)
    str += chats[num]
  }
  return str
}

/**
 * 随机生成uuid
 * @return string 生成的uuid
 */
export function randomUUID() {
  let chats = '0123456789abcdef'
  return randomString(32, chats)
}

/**
 * 【顶部导航栏模式】
 *  @date 2019-04-08
 *  顶部导航栏滚动条位置滚动到选中的菜单处
 * @param doc document 对象
 */
export function topNavScrollToSelectItem(doc) {
  let scrollWidth = doc.getElementById('top-nav-scroll-width')
  if (scrollWidth == null) return
  let menu = scrollWidth.getElementsByClassName('ant-menu')[0]
  if (menu) {
    let menuItems = menu.getElementsByTagName('li')
    for (let item of menuItems) {
      let index1 = item.className.indexOf('ant-menu-item-selected') !== -1
      let index2 = item.className.indexOf('ant-menu-submenu-selected') !== -1
      if (index1 || index2) {
        // scrollLeft = 选中项left - 选中项width - (第一个隐藏的div的宽度)
        let scrollLeft = (item.offsetLeft - item.offsetWidth - (index1 ? 100 : 60))
        let scrollView = doc.getElementById('top-nav-scroll-view')
        // scrollTo() 方法存在兼容性问题
        if (typeof scrollView.scrollTo === 'function') {
          scrollView.scrollTo({
            left: scrollLeft,
            behavior: 'smooth'
          })
        } else {
          scrollView.scrollLeft = scrollLeft
        }
        break
      }
    }
  }
}