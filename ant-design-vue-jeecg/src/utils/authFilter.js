
import { USER_AUTH,SYS_BUTTON_AUTH } from "@/store/mutation-types"

export function disabledAuthFilter(code,formData) {
  if(nodeDisabledAuth(code,formData)){
    return true;
  }else{
    return globalDisabledAuth(code);
  }
}

function nodeDisabledAuth(code,formData){
  console.log("页面权限禁用--NODE--开始");
  var permissionList = [];
  try {
    var obj = formData;
    //console.log("页面权限禁用--NODE--开始",obj);
    if (obj) {
      let bpmList = obj.permissionList;
      for (var bpm of bpmList) {
        if(bpm.type == '2') {
          permissionList.push(bpm);
        }
      }
    }
  } catch (e) {
    //console.log("页面权限异常----", e);
  }
  if (permissionList === null || permissionList === "" || permissionList === undefined||permissionList.length<=0) {
    return false;
  }
  let permissions = [];
  for (var item of permissionList) {
    if(item.type == '2') {
      permissions.push(item.action);
    }
  }
  //console.log("页面权限----"+code);
  if (!permissions.includes(code)) {
    return false;
  }else{
    for (var item2 of permissionList) {
      if(code === item2.action){
        console.log("页面权限禁用--NODE--生效");
        return true;
      }
    }
  }
  return false;
}

function globalDisabledAuth(code){
  console.log("页面禁用权限--Global--开始");

  var permissionList = [];
  var allPermissionList = [];

  //let authList = Vue.ls.get(USER_AUTH);
  let authList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
  for (var auth of authList) {
    if(auth.type == '2') {
      permissionList.push(auth);
    }
  }
  //console.log("页面禁用权限--Global--",sessionStorage.getItem(SYS_BUTTON_AUTH));
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");
  for (var gauth of allAuthList) {
    if(gauth.type == '2') {
      allPermissionList.push(gauth);
    }
  }
  //设置全局配置是否有命中
  var  gFlag = false;//禁用命中
  var invalidFlag = false;//无效命中
  if(allPermissionList != null && allPermissionList != "" && allPermissionList != undefined && allPermissionList.length > 0){
    for (var itemG of allPermissionList) {
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
  for (var item of permissionList) {
    if(item.type == '2') {
      permissions.push(item.action);
    }
  }
  //console.log("页面禁用权限----"+code);
  if (!permissions.includes(code)) {
    return gFlag;
  }else{
    for (var item2 of permissionList) {
      if(code === item2.action){
        console.log("页面权限解除禁用--Global--生效");
        gFlag = false;
      }
    }
    return gFlag;
  }
}



export function colAuthFilter(columns,pre) {
  var authList = getNoAuthCols(pre);
  const cols = columns.filter(item => {
    if (hasColoum(item,authList)) {
      return true
    }
    return false
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
function getNoAuthCols(pre){
  var permissionList = [];
  var allPermissionList = [];

  //let authList = Vue.ls.get(USER_AUTH);
  let authList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]");
  for (var auth of authList) {
    //显示策略，有效状态
    if(auth.type == '1'&&startWith(auth.action,pre)) {
      permissionList.push(substrPre(auth.action,pre));
    }
  }
  //console.log("页面禁用权限--Global--",sessionStorage.getItem(SYS_BUTTON_AUTH));
  let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH) || "[]");
  for (var gauth of allAuthList) {
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