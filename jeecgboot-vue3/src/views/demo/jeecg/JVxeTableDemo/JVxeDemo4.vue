<template>
  <JVxeTable
    ref="vTable"
    toolbar
    rowNumber
    rowSelection
    :maxHeight="580"
    :dataSource="dataSource"
    :columns="columns"
    :linkageConfig="linkageConfig"
  />
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { JVxeTypes, JVxeColumn, JVxeLinkageConfig } from '/@/components/jeecg/JVxeTable/types';

  // 联动配置
  const linkageConfig = ref<JVxeLinkageConfig[]>([
    { requestData: requestMockData, key: 's1' },
    // 可配置多个联动
    { requestData: requestMenu, key: 'menu1' },
  ]);

  const columns = ref<JVxeColumn[]>([
    {
      title: '性别',
      key: 'sex',
      type: JVxeTypes.select,
      dictCode: 'sex',
      width: '180px',
      placeholder: '请选择${title}',
    },
    {
      title: '省/直辖市/自治区',
      key: 's1',
      type: JVxeTypes.select,
      width: '180px',
      placeholder: '请选择${title}',
      // 联动字段（即下一级的字段）
      linkageKey: 's2',
    },
    {
      title: '市',
      key: 's2',
      type: JVxeTypes.select,
      width: '180px',
      placeholder: '请选择${title}',
      // 联动字段（即下一级的字段）
      linkageKey: 's3',
    },
    {
      title: '县/区',
      key: 's3',
      type: JVxeTypes.select,
      width: '180px',
      options: [],
      placeholder: '请选择${title}',
    },
    {
      title: '一级菜单',
      key: 'menu1',
      type: JVxeTypes.select,
      width: '180px',
      placeholder: '请选择${title}',
      // 联动字段（即下一级的字段）
      linkageKey: 'menu2',
    },
    {
      title: '二级菜单',
      key: 'menu2',
      type: JVxeTypes.select,
      width: '180px',
      placeholder: '请选择${title}',
      // 联动字段（即下一级的字段）
      linkageKey: 'menu3',
    },
    {
      title: '三级菜单',
      key: 'menu3',
      type: JVxeTypes.select,
      width: '180px',
      placeholder: '请选择${title}',
    },
  ]);

  const dataSource = ref([
    { sex: '1', s1: '110000', s2: '110100', s3: '110101' },
    { sex: '2', s1: '130000', s2: '130300', s3: '130303' },
  ]);

  // 模拟数据
  const mockData = [
    { text: '北京市', value: '110000', parent: '' },
    { text: '天津市', value: '120000', parent: '' },
    { text: '河北省', value: '130000', parent: '' },
    { text: '上海市', value: '310000', parent: '' },

    { text: '北京市', value: '110100', parent: '110000' },
    { text: '天津市市', value: '120100', parent: '120000' },
    { text: '石家庄市', value: '130100', parent: '130000' },
    { text: '唐山市', value: '130200', parent: '130000' },
    { text: '秦皇岛市', value: '130300', parent: '130000' },
    { text: '上海市', value: '310100', parent: '310000' },

    { text: '东城区', value: '110101', parent: '110100' },
    { text: '西城区', value: '110102', parent: '110100' },
    { text: '朝阳区', value: '110105', parent: '110100' },
    { text: '和平区', value: '120101', parent: '120100' },
    { text: '河东区', value: '120102', parent: '120100' },
    { text: '河西区', value: '120103', parent: '120100' },
    { text: '黄浦区', value: '310101', parent: '310100' },
    { text: '徐汇区', value: '310104', parent: '310100' },
    { text: '长宁区', value: '310105', parent: '310100' },
    { text: '长安区', value: '130102', parent: '130100' },
    { text: '桥西区', value: '130104', parent: '130100' },
    { text: '新华区', value: '130105', parent: '130100' },
    { text: '路南区', value: '130202', parent: '130200' },
    { text: '路北区', value: '130203', parent: '130200' },
    { text: '古冶区', value: '130204', parent: '130200' },
    { text: '海港区', value: '130302', parent: '130300' },
    { text: '山海关区', value: '130303', parent: '130300' },
    { text: '北戴河区', value: '130304', parent: '130300' },
  ];

  /** 模拟从后台查询数据 */
  function requestMockData(parent) {
    return new Promise((resolve) => {
      let data = mockData.filter((i) => i.parent === parent);
      setTimeout(() => resolve(data), 500);
    });
  }

  /** 查询后台真实数据 */
  async function requestMenu(parent) {
    let result;
    // 如果parent为空，则查询第一级菜单
    if (parent === '') {
      result = await defHttp.get({
        url: '/sys/permission/getSystemMenuList',
        params: {},
      });
    } else {
      result = await defHttp.get({
        url: '/sys/permission/getSystemSubmenu',
        params: { parentId: parent },
      });
    }
    // 返回的数据里必须包含 value 和 text 字段
    return result.map((item) => ({ value: item.id, text: item.name }));
  }
</script>
