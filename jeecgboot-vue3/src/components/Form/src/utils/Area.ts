import REGION_DATA from 'china-area-data';

/**
 * Area 属性all的类型
 */
interface PlainPca {
  id: string;
  text: string;
  pid: string;
  index: Number;
}

/**
 * 省市区工具类 -解决列表省市区组件的翻译问题
 */
class Area {
  all: PlainPca[];

  /**
   * 构造器
   * @param express
   */
  constructor(pcaa?) {
    if (!pcaa) {
      pcaa = REGION_DATA;
    }
    let arr: PlainPca[] = [];
    const province = pcaa['86'];
    Object.keys(province).map((key) => {
      arr.push({ id: key, text: province[key], pid: '86', index: 1 });
      const city = pcaa[key];
      Object.keys(city).map((key2) => {
        arr.push({ id: key2, text: city[key2], pid: key, index: 2 });
        const qu = pcaa[key2];
        if (qu) {
          Object.keys(qu).map((key3) => {
            arr.push({ id: key3, text: qu[key3], pid: key2, index: 3 });
          });
        }
      });
    });
    this.all = arr;
  }

  get pca() {
    return this.all;
  }

  getCode(text) {
    if (!text || text.length == 0) {
      return '';
    }
    for (let item of this.all) {
      if (item.text === text) {
        return item.id;
      }
    }
  }
  
//update-begin-author:liusq---date:20230404--for: [issue/382]省市区组件JAreaLinkage数据不回显---
  getText(code,index=3) {
    if (!code || code.length == 0) {
      return '';
    }
    let arr = [];
    this.getAreaBycode(code, arr, index);
    return arr.join('/');
  }
//update-end-author:liusq---date:20230404--for: [issue/382]省市区组件JAreaLinkage数据不回显---

  getRealCode(code) {
    let arr = [];
    this.getPcode(code, arr, 3);
    return arr;
  }

  getPcode(id, arr, index) {
    for (let item of this.all) {
      if (item.id === id && item.index == index) {
        arr.unshift(id);
        if (item.pid != '86') {
          this.getPcode(item.pid, arr, --index);
        }
      }
    }
  }

  getAreaBycode(code, arr, index) {
    for (let item of this.all) {
      if (item.id === code && item.index == index) {
        arr.unshift(item.text);
        if (item.pid != '86') {
          this.getAreaBycode(item.pid, arr, --index);
        }
      }
    }
  }
}
const jeecgAreaData = new Area();

// 根据code找文本
const getAreaTextByCode = function (code) {
  let index = 3;
  //update-begin-author:liusq---date:20220531--for: 判断code是否是多code逗号分割的字符串，是的话，获取最后一位的code ---
  if (code && code.includes(',')) {
    index = code.split(",").length;
    code = code.substr(code.lastIndexOf(',') + 1);
  }
  //update-end-author:liusq---date:20220531--for: 判断code是否是多code逗号分割的字符串，是的话，获取最后一位的code ---
  return jeecgAreaData.getText(code,index);
};

export { getAreaTextByCode };
