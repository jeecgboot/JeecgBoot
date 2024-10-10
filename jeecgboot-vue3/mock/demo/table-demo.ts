import { MockMethod } from 'vite-plugin-mock';
import { Random } from 'mockjs';
import { resultPageSuccess, baseUrl } from '../_util';

function getRandomPics(count = 10): string[] {
  const arr: string[] = [];
  for (let i = 0; i < count; i++) {
    arr.push(Random.image('800x600', Random.color(), Random.color(), Random.title()));
  }
  return arr;
}

const demoList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 200; index++) {
    result.push({
      id: `${index}`,
      beginTime: '@datetime',
      endTime: '@datetime',
      address: '@city()',
      name: '@cname()',
      name1: '@cname()',
      name2: '@cname()',
      name3: '@cname()',
      name4: '@cname()',
      name5: '@cname()',
      name6: '@cname()',
      name7: '@cname()',
      name8: '@cname()',
      avatar: Random.image('400x400', Random.color(), Random.color(), Random.first()),
      imgArr: getRandomPics(Math.ceil(Math.random() * 3) + 1),
      imgs: getRandomPics(Math.ceil(Math.random() * 3) + 1),
      age: Math.ceil(Math.random() * 30) + 1,
      score: Math.ceil(Math.random() * 80) + 1,
      date: `@date('yyyy-MM-dd')`,
      time: `@time('HH:mm')`,
      'no|100000-10000000': 100000,
      'status|1': ['normal', 'enable', 'disable'],
    });
  }
  return result;
})();

export default [
  {
    url: `${baseUrl}/table/getDemoList`,
    timeout: 100,
    method: 'get',
    response: ({ query }) => {
      const { page = 1, pageSize = 20 } = query;
      // update-begin--author:liaozhiyang---date:20240730---for：【issues/6943】mock翻页之后数据id和图片没自动刷新
      const pageNo = +(query.pageNo ?? page);
      return resultPageSuccess(pageNo, +pageSize, demoList);
      // update-end--author:liaozhiyang---date:20240730---for：【issues/6943】mock翻页之后数据id和图片没自动刷新
    },
  },
] as MockMethod[];
