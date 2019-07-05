<template>
  <div>
    <a-modal
      :width="modalWidth"
      :visible="visible"
      title="数据对比窗口"
      :confirmLoading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="取消">

      <a-spin :spinning="confirmLoading">
        <a-form @submit="handleSubmit" :form="form" class="form">
          <a-row class="form-row" :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="数据库表名"  :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
                <a-input placeholder="请输入数据库表名" v-decorator="[ 'dataTale', {}]" @blur="handleTableBlur" disabled/>
              </a-form-item>
            </a-col>

            <a-col :md="12" :sm="8">
              <a-form-item label="数据ID"  :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }">
                <a-input placeholder="请输入数据ID" v-decorator="[ 'dataId', {}]" @blur="handleIdBlur" disabled/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row class="form-row" :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="版本号1"  :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
                <a-select placeholder="请选择版本号" v-decorator="[ 'dataVersion1', {}]" @change="handleChange1">
                  <a-select-option v-for="(log,logindex) in DataVersionList" :key="logindex.toString()" :value="log.id">
                    {{ log.dataVersion }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="版本号2"  :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }">
                <a-select placeholder="请选择版本号" v-decorator="[ 'dataVersion2', {}]" @change="handleChange2">
                  <a-select-option v-for="(log,logindex) in DataVersionList" :key="logindex.toString()" :value="log.id">
                    {{ log.dataVersion }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
      <data-log-compare-modal ref="modal" @ok="modalFormOk" ></data-log-compare-modal>
    </a-modal>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  import DataLogCompareModal from './DataLogCompareModal'
  export default {
    name: 'DataLogModal',
    components: { DataLogCompareModal },
    dataId1:'',
    dataId2: '',
    dataTable1:'',
    dataID3:'',

    data () {
      return {
        modalWidth:700,
        modaltoggleFlag:true,
        confirmDirty: false,
        title:"操作",
        visible: false,
        model: {},
        confirmLoading: false,
        headers:{},
        form:this.$form.createForm(this),
        url: {
          queryDataVerListUrl:"/sys/dataLog/queryDataVerList",
        },
        DataVersionList:[],
      }
    },
    created () {
    },
    methods: {
      addModal(records){
        const dataTable = records[0].dataTable
        const dataId = records[0].dataId;
        const dataVersion1 = records[0].dataVersion;
        const dataVersion2 = records[1].dataVersion;
        this.dataId1 = records[0].id;
        this.dataId2 = records[1].id;
        this.dataTable1 = records[0].dataTable
        this.dataID3 =  records[0].dataId
        this.initDataVersionList();
        this.form.resetFields();
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue({dataTale:dataTable,dataId:dataId,dataVersion1:dataVersion1,dataVersion2:dataVersion2});
        });
      },
      handleOk () {
        this.close();
        this.$refs.modal.compareModal(this.dataId1  ,this.dataId2);
        this.$refs.modal.title="数据比较";
      },
      handleCancel(){
        this.close()
      },
      handleSubmit(){
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.disableSubmit = false;
      },
      modalFormOk () {
      },
      initDataVersionList(){
        let that = this;
        getAction(that.url.queryDataVerListUrl,{dataTable:this.dataTable1,dataId:this.dataID3}).then((res)=>{
          if(res.success){
            this.DataVersionList = res.result;
          }else{
            this.DataVersionList=[];
            this.dataId1 = '',
            this.dataId2='',
            console.log(res.message);

          }
        });
      },
      handleChange1(value) {
        this.dataId1 = value;
      },
      handleChange2(value) {
        this.dataId2 = value;
      },
      handleTableBlur(e){
        this.dataTable1 = e.target.value;
        this.initDataVersionList();
      },
      handleIdBlur(e){
        this.dataID3 = e.target.value;
        this.initDataVersionList();
      }
    }
  }
</script>

<style scoped>

</style>