/**
 * 同步列表，可以同步新增、修改、删除
 * @author sunjianlei
 * */
export function syncAllTable(vm, table1) {
  vm.$refs.editableTable.resetScrollTop()
  let deleteIds = table1.$refs.editableTable.getDeleteIds()
  let table1Value
  table1.$refs.editableTable.getValuesPromise(false).then((values) => {
    table1Value = values
    return vm.$refs.editableTable.getValuesPromise(false)
  }).then((values) => {

    table1Value.forEach(value => {
      let flag = false
      values.forEach((thisValue) => {
        if (value.id === thisValue.id) {

          // 判断是否修改了值
          let dbFieldName = thisValue['dbFieldName']
          let dbFieldTxt = thisValue['dbFieldTxt']

          // return

          if (value.dbFieldName !== dbFieldName
            || value.dbFieldTxt !== dbFieldTxt) {

            // 修改了
            vm.$refs.editableTable.setValues([{
              rowKey: thisValue.id,
              values: {
                dbFieldName: value.dbFieldName,
                dbFieldTxt: value.dbFieldTxt
              }
            }])

          }
          flag = true
        } else {
          // id不匹配则有可能是新增也有可能是删除了的
          // 遍历传进来的 deleteIds 进行对比
          deleteIds.forEach(delId => {
            // 对比成功，则删除该条数据
            if (delId === thisValue.id) {
              vm.$refs.editableTable.removeRows(vm.$refs.editableTable.caseId + delId)
              flag = true
            }
          })
        }
      })
      // return
      // 判断是否操作了该条数据，若没有操作则代表要执行新增操作
      if (!flag) {
        let record = Object.assign({}, value)
        vm.columns.forEach(column => {
          if (
            column.dataIndex !== 'dbFieldName' &&
            column.dataIndex !== 'dbFieldTxt'
          ) {
            record[column.dataIndex] = column.defaultValue
          }
        })
        vm.$refs.editableTable.push(record)
      }
    })
  })

}

/**
 * 将数据分类并Set进dataSource
 * @author sunjianlei
 **/
export function setDataSource(vm, queryData) {
  let dataSource = []
  // 遍历查询出来的数据
  queryData.forEach(value => {

    let data = { id: value['id'] }
    vm.columns.forEach(column => {
      let key = column.key
      if (key) {
        data[key] = value[key]

        // 由于多选下拉框返回的是一个数组，所以需要改成 [1,2,3] 数组的形式，否则组件不识别
        // if (key === 'indexField') {
        //   data[key] = value[key].split(',')
        // }

      }
    })
    dataSource.push(data)
  })
  vm.dataSource = dataSource
}

/** 获取主表的初始化数据 */
export function getMasterTableInitialData() {
  return [
    {
      dbFieldName: 'id',
      dbFieldTxt: '主键',
      dbLength: 36,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'string',
      dbIsKey: '1',
      dbIsNull: '0',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'text',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 1
    },
    {
      dbFieldName: 'create_by',
      dbFieldTxt: '创建人',
      dbLength: 50,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'string',
      dbIsKey: '0',
      dbIsNull: '1',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'text',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 2
    },
    {
      dbFieldName: 'create_time',
      dbFieldTxt: '创建日期',
      dbLength: 20,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'Date',
      dbIsKey: '0',
      dbIsNull: '1',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'datetime',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 3
    },
    {
      dbFieldName: 'update_by',
      dbFieldTxt: '更新人',
      dbLength: 50,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'string',
      dbIsKey: '0',
      dbIsNull: '1',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'text',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 4
    },
    {
      dbFieldName: 'update_time',
      dbFieldTxt: '更新日期',
      dbLength: 20,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'Date',
      dbIsKey: '0',
      dbIsNull: '1',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'datetime',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 5
    },{
      dbFieldName: 'sys_org_code',
      dbFieldTxt: '所属部门',
      dbLength: 64,
      dbPointLength: 0,
      dbDefaultVal: '',
      dbType: 'string',
      dbIsKey: '0',
      dbIsNull: '1',
      // table2
      isShowForm: '0',
      isShowList: '0',
      fieldShowType: 'text',
      fieldLength: '120',
      queryMode: 'single',
      orderNum: 6
    }
    // {
    //   dbFieldName: 'sys_org_code',
    //   dbFieldTxt: '所属部门',
    //   dbLength: 50,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // }, {
    //   dbFieldName: 'sys_company_code',
    //   dbFieldTxt: '所属公司',
    //   dbLength: 50,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // }, {
    //   dbFieldName: 'bpm_status',
    //   dbFieldTxt: '流程状态',
    //   dbLength: 32,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // }
  ]
}
/** 获取树的初始化数据 */
export function getTreeNeedFields() {
  return [{
    dbFieldName: 'pid',
    dbFieldTxt: '父级节点',
    dbLength: 32,
    dbPointLength: 0,
    dbDefaultVal: '',
    dbType: 'string',
    dbIsKey: '0',
    dbIsNull: '1',
    // table2
    isShowForm: '1',
    isShowList: '0',
    fieldShowType: 'text',
    fieldLength: '120',
    queryMode: 'single',
    orderNum: 7
  },{
    dbFieldName: 'has_child',
    dbFieldTxt: '是否有子节点',
    dbLength: 3,
    dbPointLength: 0,
    dbDefaultVal: '',
    dbType: 'string',
    dbIsKey: '0',
    dbIsNull: '1',
    // table2
    isShowForm: '0',
    isShowList: '0',
    fieldShowType: 'list',
    fieldLength: '120',
    queryMode: 'single',
    orderNum: 8,
    // table3
    dictField:"yn"
  }]
}