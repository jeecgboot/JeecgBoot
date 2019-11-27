<template>
  <a-modal
    title="高级查询构造器"
    :width="1000"
    :visible="visible"
    @cancel="handleCancel"
    :mask="false"
    wrapClassName="ant-modal-cust-warp"
    class="j-super-query-modal"
    style="top:5%;max-height: 95%;">

    <template slot="footer">
      <div style="float: left">
        <a-button :loading="loading" @click="handleReset">重置</a-button>
        <a-button :loading="loading" @click="handleSave">保存查询条件</a-button>
      </div>
      <a-button :loading="loading" @click="handleCancel">关闭</a-button>
      <a-button :loading="loading" type="primary" @click="handleOk">查询</a-button>
    </template>

    <a-spin :spinning="loading">
      <a-row>
        <a-col :sm="24" :md="24-5">

          <a-empty v-if="queryParamsModel.length === 0">
            <div slot="description">
              <span>没有任何查询条件</span>
              <a-divider type="vertical"/>
              <a @click="handleAdd">点击新增</a>
            </div>
          </a-empty>

          <a-form v-else layout="inline">

            <a-form-item label="过滤条件匹配" style="margin-bottom: 12px;">
              <a-select v-model="selectValue">
                <a-select-option value="and">AND（所有条件都要求匹配）</a-select-option>
                <a-select-option value="or">OR（条件中的任意一个匹配）</a-select-option>
              </a-select>
            </a-form-item>

            <a-row type="flex" style="margin-bottom:10px" :gutter="16" v-for="(item, index) in queryParamsModel" :key="index">

              <a-col :span="8">
                <a-select placeholder="选择查询字段" v-model="item.field" @select="(val,option)=>handleSelected(option,item)">
                  <a-select-option v-for="(f,fIndex) in fieldList" :key=" 'field'+fIndex" :value="f.value" :data-idx="fIndex">{{ f.text }}</a-select-option>
                </a-select>
              </a-col>

              <a-col :span="4">
                <a-select placeholder="匹配规则" v-model="item.rule">
                  <a-select-option value="eq">等于</a-select-option>
                  <a-select-option value="ne">不等于</a-select-option>
                  <a-select-option value="gt">大于</a-select-option>
                  <a-select-option value="ge">大于等于</a-select-option>
                  <a-select-option value="lt">小于</a-select-option>
                  <a-select-option value="le">小于等于</a-select-option>
                  <a-select-option value="right_like">以..开始</a-select-option>
                  <a-select-option value="left_like">以..结尾</a-select-option>
                  <a-select-option value="like">包含</a-select-option>
                  <a-select-option value="in">在...中</a-select-option>
                </a-select>
              </a-col>

              <a-col :span="8">
                <template v-if="item.dictCode">
                  <template v-if="item.type === 'table-dict'">
                    <j-popup
                      v-model="item.val"
                      :code="item.dictTable"
                      :field="item.dictCode"
                      :orgFields="item.dictCode"
                      :destFields="item.dictCode"
                    ></j-popup>
                  </template>
                  <j-dict-select-tag v-else v-model="item.val" :dictCode="item.dictCode" placeholder="请选择"/>
                </template>
                <j-select-multi-user
                  v-else-if="item.type === 'select-user'"
                  v-model="item.val"
                  :buttons="false"
                  :multiple="false"
                  placeholder="请选择用户"
                  :returnKeys="['id', item.customReturnField || 'username']"
                />
                <j-select-depart
                  v-else-if="item.type === 'select-depart'"
                  v-model="item.val"
                  :multi="false"
                  placeholder="请选择部门"
                  :customReturnField="item.customReturnField || 'id'"
                />
                <j-date v-else-if=" item.type=='date' " v-model="item.val" placeholder="请选择日期" style="width: 100%"></j-date>
                <j-date v-else-if=" item.type=='datetime' " v-model="item.val" placeholder="请选择时间" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"></j-date>
                <a-input-number v-else-if=" item.type=='int'||item.type=='number' " style="width: 100%" placeholder="请输入数值" v-model="item.val"/>
                <a-input v-else v-model="item.val" placeholder="请输入值"/>
              </a-col>

              <a-col :span="4">
                <a-button @click="handleAdd" icon="plus"></a-button>&nbsp;
                <a-button @click="handleDel( index )" icon="minus"></a-button>
              </a-col>

            </a-row>

          </a-form>
        </a-col>
        <a-col :sm="24" :md="5">
          <!-- 查询记录 -->

          <a-card class="j-super-query-history-card" :bordered="true">
            <div slot="title">
              保存的查询
            </div>

            <a-empty v-if="treeData.length === 0" class="j-super-query-history-empty" description="没有保存任何查询"/>
            <a-tree
              v-else
              class="j-super-query-history-tree"
              showIcon
              :treeData="treeData"
              @select="handleTreeSelect"
              @rightClick="handleTreeRightClick"
            >
            </a-tree>
          </a-card>


        </a-col>
      </a-row>


    </a-spin>

    <a-modal title="请输入保存的名称" :visible="prompt.visible" @cancel="prompt.visible=false" @ok="handlePromptOk">
      <a-input v-model="prompt.value"></a-input>
    </a-modal>

  </a-modal>
</template>

<script>
  import * as utils from '@/utils/util'
  import JDate from '@/components/jeecg/JDate.vue'
  import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
  import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'

  export default {
    name: 'JSuperQuery',
    components: { JDate, JSelectDepart, JSelectMultiUser },
    props: {
      /*
       fieldList: [{
          value:'',
          text:'',
          type:'',
          dictCode:'' // 只要 dictCode 有值，无论 type 是什么，都显示为字典下拉框
       }]
       type:date datetime int number string
      * */
      fieldList: {
        type: Array,
        required: true
      },
      /*
      * 这个回调函数接收一个数组参数 即查询条件
      * */
      callback: {
        type: String,
        required: false,
        default: 'handleSuperQuery'
      },

      // 当前是否在加载中
      loading: {
        type: Boolean,
        default: false
      },

      // 保存查询条件的唯一 code，通过该 code 区分
      saveCode: {
        type: String,
        default: 'testSaveCode'
      }

    },
    data() {
      return {

        prompt: {
          visible: false,
          value: ''
        },

        visible: false,
        queryParamsModel: [{}],
        treeIcon: <a-icon type="file-text"/>,
        treeData: [],
        // 保存查询条件的前缀名
        saveCodeBefore: 'JSuperQuerySaved_',
        selectValue: 'and',
      }
    },
    watch: {
      // 当 saveCode 变化时，重新查询已保存的条件
      saveCode: {
        immediate: true,
        handler(val) {
          let list = this.$ls.get(this.saveCodeBefore + val)
          if (list instanceof Array) {
            this.treeData = list.map(item => {
              item.icon = this.treeIcon
              return item
            })
          }
        }
      }
    },

    methods: {
      show() {
        if (!this.queryParamsModel || this.queryParamsModel.length == 0) {
          this.queryParamsModel = [{}]
        }
        this.visible = true
      },
      handleOk() {
        console.log('---高级查询参数--->', this.queryParamsModel)
        if (!this.isNullArray(this.queryParamsModel)) {
          let event = {
            matchType: this.selectValue,
            params: this.removeEmptyObject(utils.cloneObject(this.queryParamsModel))
          }
          this.$emit(this.callback, event.params, event.matchType)
        } else {
          this.$emit(this.callback)
        }
      },
      handleCancel() {
        this.close()
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleAdd() {
        this.queryParamsModel.push({})
      },
      handleDel(index) {
        this.queryParamsModel.splice(index, 1)
      },
      handleSelected(option, item) {
        let index = option.data.attrs['data-idx']

        let { type, dictCode, dictTable, customReturnField } = this.fieldList[index]
        item['type'] = type
        item['dictCode'] = dictCode
        item['dictTable'] = dictTable
        item['customReturnField'] = customReturnField
        this.$set(item, 'val', '')
      },
      handleReset() {
        this.queryParamsModel = [{}]
        this.$emit(this.callback)
      },
      handleSave() {
        let queryParams = this.removeEmptyObject(utils.cloneObject(this.queryParamsModel))
        if (this.isNullArray(queryParams)) {
          this.$message.warning('空条件不能保存')
        } else {
          this.prompt.value = ''
          this.prompt.visible = true
        }
      },
      handlePromptOk() {

        let { value } = this.prompt
        // 判断有没有重名

        let filterList = this.treeData.filter(i => i.title === value)
        if (filterList.length > 0) {
          this.$confirm({
            content: `${value} 已存在，是否覆盖？`,
            onOk: () => {
              this.prompt.visible = false
              filterList[0].records = this.removeEmptyObject(utils.cloneObject(this.queryParamsModel))
              this.saveToLocalStore()
              this.$message.success('保存成功')
            }
          })
        } else {
          this.prompt.visible = false
          this.treeData.push({
            title: value,
            icon: this.treeIcon,
            records: this.removeEmptyObject(utils.cloneObject(this.queryParamsModel))
          })
          this.saveToLocalStore()
          this.$message.success('保存成功')
        }


      },
      handleTreeSelect(idx, event) {
        if (event.selectedNodes[0]) {
          this.queryParamsModel = utils.cloneObject(event.selectedNodes[0].data.props.records)
        }
      },
      handleTreeRightClick(args) {
        this.$confirm({
          content: '是否删除当前查询？',
          onOk: () => {
            let { node: { eventKey } } = args
            this.treeData.splice(Number.parseInt(eventKey.substring(2)), 1)
            this.saveToLocalStore()
            this.$message.success('删除成功')
          },
        })
      },

      // 将查询保存到 LocalStore 里
      saveToLocalStore() {
        this.$ls.set(this.saveCodeBefore + this.saveCode, this.treeData.map(item => {
          return { title: item.title, records: item.records }
        }))
      },

      isNullArray(array) {
        //判断是不是空数组对象
        if (!array || array.length === 0) {
          return true
        }
        if (array.length === 1) {
          let obj = array[0]
          if (!obj.field || !obj.val || !obj.rule) {
            return true
          }
        }
        return false
      },
      // 去掉数组中的空对象
      removeEmptyObject(array) {
        for (let i = 0; i < array.length; i++) {
          let item = array[i]
          if (item == null || Object.keys(item).length <= 0) {
            array.splice(i--, 1)
          }
        }
        return array
      }
    }
  }
</script>

<style lang="scss" scoped>

  .j-super-query-modal {

    /deep/ {
    }

    .j-super-query-history-card /deep/ {
      .ant-card-body,
      .ant-card-head-title {
        padding: 0;
      }

      .ant-card-head {
        padding: 4px 8px;
        min-height: initial;
      }
    }

    .j-super-query-history-empty /deep/ {
      .ant-empty-image {
        height: 80px;
        line-height: 80px;
        margin-bottom: 0;
      }

      img {
        width: 80px;
        height: 65px;
      }

      .ant-empty-description {
        color: #afafaf;
        margin: 8px 0;
      }
    }

    .j-super-query-history-tree /deep/ {
      .ant-tree-switcher {
        display: none;
      }

      .ant-tree-node-content-wrapper {
        width: 100%;
      }
    }

  }

</style>