export const getData = (() => {
  let dottedBase = +new Date();
  const barDataSource: any[] = [];
  const barMultiData: any[] = [];
  const barLineData: any[] = [];

  for (let i = 0; i < 20; i++) {
    let obj = { name: '', value: 0 };
    const date = new Date((dottedBase += 1000 * 3600 * 24));
    obj.name = [date.getFullYear(), date.getMonth() + 1, date.getDate()].join('-');
    obj.value = Math.random() * 200;
    barDataSource.push(obj);
  }

  for (let j = 0; j < 2; j++) {
    for (let i = 0; i < 20; i++) {
      let obj = { name: '', value: 0, type: 2010 + j + '' };
      const date = new Date(dottedBase + 1000 * 3600 * 24 * i);
      obj.name = [date.getFullYear(), date.getMonth() + 1, date.getDate()].join('-');
      obj.value = Math.random() * 200;
      barMultiData.push(obj);
    }
  }
  const pieData = [
    { value: 335, name: '客服电话' },
    { value: 310, name: '奥迪官网' },
    { value: 234, name: '媒体曝光' },
    { value: 135, name: '质检总局' },
    { value: 105, name: '其他' },
  ];
  const radarData = [
    { value: 75, name: '政治', type: '文综', max: 100 },
    { value: 65, name: '历史', type: '文综', max: 100 },
    { value: 55, name: '地理', type: '文综', max: 100 },
    { value: 74, name: '化学', type: '文综', max: 100 },
    { value: 38, name: '物理', type: '文综', max: 100 },
    { value: 88, name: '生物', type: '文综', max: 100 },
  ];
  for (let j = 0; j < 2; j++) {
    for (let i = 0; i < 15; i++) {
      let obj = { name: '', value: 0, type: 2010 + j + '', seriesType: j >= 1 ? 'line' : 'bar' };
      const date = new Date(dottedBase + 1000 * 3600 * 24 * i);
      obj.name = [date.getFullYear(), date.getMonth() + 1, date.getDate()].join('-');
      obj.value = Math.random() * 200;
      barLineData.push(obj);
    }
  }
  return { barDataSource, barMultiData, pieData, barLineData, radarData };
})();
