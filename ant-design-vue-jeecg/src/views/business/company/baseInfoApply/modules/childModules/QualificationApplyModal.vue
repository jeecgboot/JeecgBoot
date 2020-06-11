<template>
  <business-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"

    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-descriptions title="申报详情" >
        <a-descriptions-item label="申报时间">
          {{model.createTime}}
        </a-descriptions-item>
        <a-descriptions-item label="申报人" :span="2">
          {{model.createBy}}
        </a-descriptions-item>

        <a-descriptions-item label="申报详情" :span="3">
        </a-descriptions-item>
        <a-descriptions-item label="   新增资质" :span="3" v-if="data.add!==undefined">
          <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 6 }" :data-source="data.add">
            <a-list-item slot="renderItem" slot-scope="item, index">

                <div style="float: left;width:104px;height:104px;margin-right: 10px;margin: 0 8px 8px 0;">
                  <div
                    style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
                    <img style="width: 100%;" :src="item.url" preview="add">
                  </div>
                </div>

            </a-list-item>
          </a-list>
        </a-descriptions-item>
        <a-descriptions-item label="   删除资质" :span="3" v-if="data.delete!==undefined">
          <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }" :data-source="data.delete">
            <a-list-item slot="renderItem" slot-scope="item, index">

              <div style="float: left;width:104px;height:104px;margin-right: 10px;margin: 0 8px 8px 0;">
                <div
                  style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
                  <img style="width: 100%;object-fit:cover;" :src="item.url" preview="add">
                </div>
              </div>

            </a-list-item>
          </a-list>
        </a-descriptions-item>

        <a-descriptions-item label="审核人">
          {{model.updateBy}}
        </a-descriptions-item>
        <a-descriptions-item label="申报状态">
          {{geStatusValue(model.status)}}
        </a-descriptions-item>
        <a-descriptions-item label="生效时间">
         {{model.updateTime}}
        </a-descriptions-item>

      </a-descriptions>
    </a-spin>
  </business-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import BusinessModal from "../../../../component/BusinessModal";
  import {comparisonQualification} from "../../../../requestAction/request"
  import {getFileAccessHttpUrl} from '@/api/manage';
  import {ajaxGetDictItems,getDictItemsFromCache} from '@/api/api'

  export default {
    name: "QualicationApplyModal",
    components: {
      BusinessModal,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        width:800,
        visible: false,
        model: {},
        confirmLoading: false,
        data : {},
        spinning:false,
        dictOptions:[]

      }
    },
    created () {
      this.initDictData("statue");
    },
    methods: {
      initDictData(dictCode) {

        //优先从缓存中读取字典配置
        if(getDictItemsFromCache(dictCode)){
          this.dictOptions = getDictItemsFromCache(dictCode);
          return
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(dictCode, null).then((res) => {
          if (res.success) {
            this.dictOptions = res.result;
          }
        })
      },
      detail (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.compareDetail(record.id);
      },
      compareDetail(index){
        let that = this;
        //查询前后明细
        comparisonQualification({applyId:index}).then((res)=>{
          if(res.success) {
            that.data = res.result;
            console.log( res.result)
            if(that.data.add!==undefined)
              that.data.add.forEach(e=>{
                e.url = getFileAccessHttpUrl(e.url);
              });
            if(that.data.delete!==undefined)
              that.data.delete.forEach(e=>{
                e.url = getFileAccessHttpUrl(e.url);
              });
          }else{
            this.$message.error(res.message);
          }
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },

      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'updateTime','companyId','status','content'))
      },
      handleOk(){

      },
      geStatusValue(status){
        if(status===null)
          return status;
        let result =
         this.dictOptions.find(e=>{
          return  e.value===status;
        });
        if(result==null)
          return status;
        return result.text;

      }
      
    }
  }
</script>
<style scoped>
  .table-operator {
    margin-bottom: 10px
  }

  .clName .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle {
    width: 10px !important;
  }

  .clName .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
    background-color: #1890FF !important;
  }
</style>