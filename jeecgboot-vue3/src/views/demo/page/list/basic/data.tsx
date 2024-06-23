export const cardList = (() => {
  const result: any[] = [];
  for (let i = 0; i < 6; i++) {
    result.push({
      id: i,
      title: 'Jeecg Admin',
      description: '基于Vue Next, TypeScript, Ant Design Vue实现的一套完整的企业级后台管理系统',
      datetime: '2020-11-26 17:39',
      extra: '编辑',
      icon: 'logos:vue',
      color: '#1890ff',
      author: 'Jeecg',
      percent: 20 * (i + 1),
    });
  }
  return result;
})();
