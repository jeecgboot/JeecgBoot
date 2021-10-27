<template>
  <j-vxe-table
    ref="vTable"
    toolbar
    row-number
    row-selection
    keep-source
    :height="484"
    :dataSource="dataSource"
    :columns="columns"
    :linkage-config="linkageConfig"
  />
</template>

<script>
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getAction } from '@api/manage'

export default {
  name: 'JVxeDemo2',
  data() {
    return {
      // 联动配置
      linkageConfig: [
        {requestData: this.requestData, key: 's1'},
        // 可配置多个联动
        {requestData: this.loadMenu, key: 'menu1',},
      ],
      columns: [
        {
          title: '性别',
          key: 'sex',
          type: JVXETypes.select,
          dictCode: 'sex',
          width: '180px',
          placeholder: '请选择${title}',
        },
        {
          title: '省/直辖市/自治区',
          key: 's1',
          type: JVXETypes.select,
          width: '180px',
          placeholder: '请选择${title}',
          // 联动字段（即下一级的字段）
          linkageKey: 's2',
        },
        {
          title: '市',
          key: 's2',
          type: JVXETypes.select,
          width: '180px',
          placeholder: '请选择${title}',
          // 联动字段（即下一级的字段）
          linkageKey: 's3',
        },
        {
          title: '县/区',
          key: 's3',
          type: JVXETypes.select,
          width: '180px',
          options: [],
          placeholder: '请选择${title}',
        },
        {
          title: '一级菜单',
          key: 'menu1',
          type: JVXETypes.select,
          width: '180px',
          placeholder: '请选择${title}',
          // 联动字段（即下一级的字段）
          linkageKey: 'menu2',
        },
        {
          title: '二级菜单',
          key: 'menu2',
          type: JVXETypes.select,
          width: '180px',
          placeholder: '请选择${title}',
          // 联动字段（即下一级的字段）
          linkageKey: 'menu3',
        },
        {
          title: '三级菜单',
          key: 'menu3',
          type: JVXETypes.select,
          width: '180px',
          placeholder: '请选择${title}',
        }
      ],
      dataSource: [
        {sex: '1', s1: '110000', s2: '110100', s3: '110101'},
        {sex: '2', s1: '130000', s2: '130300', s3: '130303'},
      ],
      // 模拟数据
      mockData: [
        {text: '北京市', value: '110000', parent: ''},
        {text: '天津市', value: '120000', parent: ''},
        {text: '河北省', value: '130000', parent: ''},
        {text: '上海市', value: '310000', parent: ''},

        {text: '北京市', value: '110100', parent: '110000'},
        {text: '天津市市', value: '120100', parent: '120000'},
        {text: '石家庄市', value: '130100', parent: '130000'},
        {text: '唐山市', value: '130200', parent: '130000'},
        {text: '秦皇岛市', value: '130300', parent: '130000'},
        {text: '上海市', value: '310100', parent: '310000'},

        {text: '东城区', value: '110101', parent: '110100'},
        {text: '西城区', value: '110102', parent: '110100'},
        {text: '朝阳区', value: '110105', parent: '110100'},
        {text: '和平区', value: '120101', parent: '120100'},
        {text: '河东区', value: '120102', parent: '120100'},
        {text: '河西区', value: '120103', parent: '120100'},
        {text: '黄浦区', value: '310101', parent: '310100'},
        {text: '徐汇区', value: '310104', parent: '310100'},
        {text: '长宁区', value: '310105', parent: '310100'},
        {text: '长安区', value: '130102', parent: '130100'},
        {text: '桥西区', value: '130104', parent: '130100'},
        {text: '新华区', value: '130105', parent: '130100'},
        {text: '路南区', value: '130202', parent: '130200'},
        {text: '路北区', value: '130203', parent: '130200'},
        {text: '古冶区', value: '130204', parent: '130200'},
        {text: '海港区', value: '130302', parent: '130300'},
        {text: '山海关区', value: '130303', parent: '130300'},
        {text: '北戴河区', value: '130304', parent: '130300'},
      ],
    }
  },
  methods: {
    /**
     * 模拟从后台查询数据
     */
    requestData(parent) {
      return new Promise((resolve, reject) => {
        let data = this.mockData.filter(i => i.parent === parent)
        setTimeout(() => {
          resolve(data)
        }, 500)
      })
    },

    async loadMenu(parent) {
      let res
      // 如果parent为空，则查询第一级菜单
      if (parent === '') {
        res = await getAction('/sys/permission/getSystemMenuList')
      } else {
        res = await getAction('/sys/permission/getSystemSubmenu', {parentId: parent})
      }
      if (res.success) {
        // 返回的数据里必须包含 value 和 text 字段
        return res.result.map(item => ({value: item.id, text: item.name}))
      }
      this.$message.warning('loadMenu失败：' + res.message)
      return []
    },

  }
}
</script>

<style scoped>

</style>