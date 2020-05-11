import { pcaa } from 'area-data'

/**
 * 省市区
 */
export default class Area {
  /**
   * 构造器
   * @param express
   */
  constructor() {
    let arr = []
    const province = pcaa['86']
    Object.keys(province).map(key=>{
      arr.push({id:key, text:province[key], pid:'86'});
      const city = pcaa[key];
      Object.keys(city).map(key2=>{
        arr.push({id:key2, text:city[key2], pid:key});
        const qu = pcaa[key2];
        Object.keys(qu).map(key3=>{
          arr.push({id:key3, text:qu[key3], pid:key2});
        })
      })
    })
    this.all = arr;
  }

  get pca(){
    return this.all;
  }

  getCode(text){
    if(!text || text.length==0){
      return ''
    }
    for(let item of this.all){
      if(item.text === text){
        return item.id;
      }
    }
  }

  getText(code){
    if(!code || code.length==0){
      return ''
    }
    let arr = []
    this.getAreaBycode(code,arr);
    return arr.join('/')
  }

  getRealCode(code){
    let arr = []
    this.getPcode(code, arr)
    return arr;
  }

  getPcode(id, arr){
    for(let item of this.all){
      if(item.id === id){
        arr.unshift(id)
        if(item.pid != '86'){
          this.getPcode(item.pid,arr)
        }
      }
    }
  }

  getAreaBycode(code,arr){
    //console.log("this.all.length",this.all)
    for(let item of this.all){
      if(item.id === code){
        arr.unshift(item.text);
        this.getAreaBycode(item.pid,arr)
      }
    }
  }

}