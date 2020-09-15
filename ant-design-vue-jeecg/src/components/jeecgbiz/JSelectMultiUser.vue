<template>
  <!-- 定义在这里的参数都是不可在外部覆盖的，防止出现问题 -->
  <j-select-biz-component
    :value="value"
    :ellipsisLength="25"
    :listUrl="url.list"
    :columns="columns"
    v-on="$listeners"
    v-bind="attrs"
  />
</template>

<script>
  import JDate from '@comp/jeecg/JDate'
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectMultiUser',
    components: {JDate, JSelectBizComponent},
    props: {
      value: null, // any type
      queryConfig: {
        type: Array,
        default: () => []
      },
    },
    data() {
      return {
        url: { list: '/sys/user/list' },
        columns: [
          { title: '姓名', align: 'center', width: '25%', widthRight: '70%', dataIndex: 'realname' },
          { title: '账号', align: 'center', width: '25%', dataIndex: 'username' },
          { title: '电话', align: 'center', width: '20%', dataIndex: 'phone' },
          { title: '出生日期', align: 'center', width: '20%', dataIndex: 'birthday' }
        ],
        // 定义在这里的参数都是可以在外部传递覆盖的，可以更灵活的定制化使用的组件
        default: {
          name: '用户',
          width: 1200,
          displayKey: 'realname',
          returnKeys: ['id', 'username'],
          queryParamText: '账号',
        },
        // 多条件查询配置
        queryConfigDefault: [
          {
            key: 'sex',
            label: '性别',
            // 如果包含 dictCode，那么就会显示成下拉框
            dictCode: 'sex',
          },
          {
            key: 'birthday',
            label: '生日',
            placeholder: '请选择出生日期',
            // 如果想要使用局部注册的组件，就必须要使用箭头函数
            customRender: ({key, queryParam, options}) => {
              return <j-date {...options} vModel={queryParam[key]} style="width:180px;"/>
            },
          },
        ],
      }
    },
    computed: {
      attrs() {
        return Object.assign(this.default, this.$attrs, {
          queryConfig: this.queryConfigDefault.concat(this.queryConfig)
        })
      }
    }
  }
</script>

<style lang="less" scoped></style>