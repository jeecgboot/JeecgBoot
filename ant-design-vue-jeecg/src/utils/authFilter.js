import { USER_AUTH,SYS_BUTTON_AUTH } from "@/store/mutation-types"

export function disabledAuthFilter(code,formData) {
  if(nodeDisabledAuth(code,formData)){
    return true;
  }else{
    return globalDisabledAuth(code);
  }
}

function nodeDisabledAuth(code,formData){
  //console.log("页面权限禁用--NODE--开始");
  let permissionList = [];
  try {
    //console.log("页面权限禁用--NODE--开始",formData);
    if (formData) {
      let bpmList = formData.permissionList;
      permissionList = bpmList.filter(item=>item.type=='2')
      // for (let bpm of bpmList) {
      //   if(bpm.type == '2') {
      //     permissionList.push(bpm);
      //   }
      // }
    }else{
      return false;
    }
  } catch (e) {
    //console.log("页面权限异常----", e);
  }
  if (permissionList.length ==  0) {
    return false;
  }

  console.log("流程节点页面权限禁用--NODE--开始");
  let permissions = [];
  for (let item of permissionList) {
    if(item.type == '2') {
      permissions.push(item.action);
    }
  }
  //console.log("页面权限----"+code);
  if (!permissions.includes(code)) {
    return false;
  }else{
    for (let item2 of permissionList) {
      if(code === item2.action){
        console.log("流程节点页面权限禁用--NODE--生效");
        return true;
      }
    }
  }
  return false;
}

function globalDisabledAuth(code){
  //console.log("全局页面禁用权限--Global--开始");

  let permissionList = [];
  let allPermissionList = [];

  //let authList = Vue.ls.get(USER_AUTH);
  let authList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
  for (let auth of authList) {
    if(auth.type == '2') {
      permissionList.push(auth);
    }
  }
  //console.log("页面禁用权限--Global--",sessionStorage.getItem(SYS_BUTTON_AUTH));
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");
  for (let gauth of allAuthList) {
    if(gauth.type == '2') {
      allPermissionList.push(gauth);
    }
  }
  //设置全局配置是否有命中
  let  gFlag = false;//禁用命中
  let invalidFlag = false;//无效命中
  if(allPermissionList != null && allPermissionList != "" && allPermissionList != undefined && allPermissionList.length > 0){
    for (let itemG of allPermissionList) {
      if(code === itemG.action){
        if(itemG.status == '0'){
          invalidFlag = true;
          break;
        }else{
          gFlag = true;
          break;
        }
      }
    }
  }
  if(invalidFlag){
    return false;
  }
  if (permissionList === null || permissionList === "" || permissionList === undefined||permissionList.length<=0) {
    return gFlag;
  }
  let permissions = [];
  for (let item of permissionList) {
    if(item.type == '2') {
      permissions.push(item.action);
    }
  }
  //console.log("页面禁用权限----"+code);
  if (!permissions.includes(code)) {
    return gFlag;
  }else{
    for (let item2 of permissionList) {
      if(code === item2.action){
        //console.log("全局页面权限解除禁用--Global--生效");
        gFlag = false;
      }
    }
    return gFlag;
  }
}



export function colAuthFilter(columns,pre) {
  let authList = getNoAuthCols(pre);
  const cols = columns.filter(item => {
    if (hasColoum(item,authList)) {
      return true
    }
    return false
  })
  return cols
}

/**
 * 【子表行编辑】实现两个功能：
 * 1、隐藏JEditableTable无权限的字段
 * 2、禁用JEditableTable无权限的字段
 * @param columns
 * @param pre
 * @returns {*}
 */
export function colAuthFilterJEditableTable(columns,pre) {
  let authList = getAllShowAndDisabledAuthCols(pre);
  const cols = columns.filter(item => {
    let oneAuth = authList.find(auth => {
      return auth.action === pre + item.key;
    });
    if(!oneAuth){
      return true
    }

    //代码严谨处理，防止一个授权标识，配置多次
    if(oneAuth instanceof Array){
      oneAuth = oneAuth[0]
    }

    //禁用逻辑
    if (oneAuth.type == '2' && !oneAuth.isAuth) {
      item["disabled"] = true
      return true
    }
    //隐藏逻辑逻辑
    if (oneAuth.type == '1' && !oneAuth.isAuth) {
      return false
    }
    return true
  })
  return cols
}


function hasColoum(item,authList){
  if (authList.includes(item.dataIndex)) {
    return false
  }
  return true
}

//权限无效时不做控制，有效时控制，只能控制 显示不显示
//根据授权码前缀获取未授权的列信息
export function getNoAuthCols(pre){
  if(!pre || pre.length==0){
    return []
  }
  let permissionList = [];
  let allPermissionList = [];

  //let authList = Vue.ls.get(USER_AUTH);
  let authList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
  for (let auth of authList) {
    //显示策略，有效状态
    if(auth.type == '1'&&startWith(auth.action,pre)) {
      permissionList.push(substrPre(auth.action,pre));
    }
  }
  //console.log("页面禁用权限--Global--",sessionStorage.getItem(SYS_BUTTON_AUTH));
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");
  for (let gauth of allAuthList) {
    //显示策略，有效状态
    if(gauth.type == '1'&&gauth.status == '1'&&startWith(gauth.action,pre)) {
      allPermissionList.push(substrPre(gauth.action,pre));
    }
  }
  const cols = allPermissionList.filter(item => {
    if (permissionList.includes(item)) {
      return false;
    }
    return true;
  })
  return cols;
}

/**
 * 将Online的行编辑按钮权限，添加至本地存储
 */
export function addOnlineBtAuth2Storage(pre, authList){
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");
  let newAuthList = allAuthList.filter(item=>{
    if(!item.action){
      return true
    }
    return item.action.indexOf(pre)<0
  })
  if(authList && authList.length>0){
    for(let item of authList){
      newAuthList.push({
        action: pre+item,
        type:1,
        status:1
      })
    }
    let temp = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
    let newArr = temp.filter(item=>{
      if(!item.action){
        return true
      }
      return item.action.indexOf(pre)<0 || authList.indexOf(item.action.replace(pre, ''))<0
    })
    sessionStorage.setItem(USER_AUTH, JSON.stringify(newArr))
  }
  sessionStorage.setItem(SYS_BUTTON_AUTH, JSON.stringify(newAuthList))
}



/**
 * 额外增加方法【用于行编辑组件】
 * date: 2020-04-05
 * author: scott
 * @param pre
 * @returns {*[]}
 */
function getAllShowAndDisabledAuthCols(pre){
  //用户拥有的权限
  let userAuthList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
  //全部权限配置
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");

  let newAllAuthList = allAuthList.map(function (item, index) {
    let hasAuthArray = userAuthList.filter(u => u.action===item.action );
    if (hasAuthArray && hasAuthArray.length>0) {
      item["isAuth"] = true
    }
    return item;
  })

  return newAllAuthList;
}

function startWith(str,pre) {
  if (pre == null || pre == "" || str==null|| str==""|| str.length == 0 || pre.length > str.length)
    return false;
  if (str.substr(0, pre.length) == pre)
    return true;
  else
    return false;
}

function substrPre(str,pre) {
  return str.substr(pre.length);
}