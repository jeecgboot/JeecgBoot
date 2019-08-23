import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"
import store from '@/store'
/**
 * 单点登录
 */
const init = (callback) => {
  console.log("-------单点登录开始-------");
  let token = Vue.ls.get(ACCESS_TOKEN);
  let st = getUrlParam("ticket");
  var sevice = "http://"+window.location.host+"/";
  if(token){
    loginSuccess(callback);
  }else{
    if(st){
      validateSt(st,sevice,callback);
    }else{
      var serviceUrl = encodeURIComponent(sevice);
      window.location.href = window._CONFIG['casPrefixUrl']+"/login?service="+serviceUrl;
    }
  }
  console.log("-------单点登录结束-------");
};
const SSO = {
  init: init
};

function getUrlParam(paraName) {
  var url = document.location.toString();
  var arrObj = url.split("?");

  if (arrObj.length > 1) {
    var arrPara = arrObj[1].split("&");
    var arr;

    for (var i = 0; i < arrPara.length; i++) {
      arr = arrPara[i].split("=");

      if (arr != null && arr[0] == paraName) {
        return arr[1];
      }
    }
    return "";
  }
  else {
    return "";
  }
}

function validateSt(ticket,service,callback){
  let params = {
    ticket: ticket,
    service:service
  };
  store.dispatch('ValidateLogin',params).then(res => {
    //this.departConfirm(res)
    if(res.success){
      loginSuccess(callback);
    }else{
      var sevice = "http://"+window.location.host+"/";
      var serviceUrl = encodeURIComponent(sevice);
      window.location.href = window._CONFIG['casPrefixUrl']+"/login?service="+serviceUrl;
    }
  }).catch((err) => {
    console.log(err);
    //that.requestFailed(err);
  });
}

function loginSuccess (callback) {
  callback();
}
export default SSO;