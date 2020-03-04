/**
 * online 自定义按钮表达式处理类
 */
export default class ButtonExpHandler {
  /**
   * 构造器
   * @param express
   */
  constructor(express,record) {
    this._express = express;
    this._record = record;
  }

  get show() {
    if(!this._express || this._express==''){
      return true;
    }
    let arr = this._express.split('#');
    //获取字段值
    let fieldValue = this._record[arr[0]];
    //获取表达式
    let exp = arr[1].toLowerCase();
    //判断表达式
    if(exp === 'eq'){
      return fieldValue == arr[2];
    }else if(exp === 'ne'){
      return !(fieldValue == arr[2]);
    }else if(exp === 'empty'){
      if(arr[2]==='true' || arr[2]===true){
        return !fieldValue || fieldValue=='';
      }else{
        return fieldValue && fieldValue.length>0
      }
    }else if(exp === 'in'){
      let arr2 = arr[2].split(',');
      return arr2.indexOf(String(fieldValue))>=0;
    }
    return false;
  }

}