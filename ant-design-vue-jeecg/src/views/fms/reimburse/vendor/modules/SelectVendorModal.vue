<template>
  <a-modal
    title="选择供应商"
    width="70%"
    :visible="modalShow"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="close"
    cancelText="关闭"
  >
    <a-row :gutter="18">
      <a-col :span="16">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :span="14">
                <a-form-item :label="(queryParamText||name)">
                  <a-input v-model="queryParam[queryParamCode||valueKey]" :placeholder="'请输入' + (queryParamText||name)" @pressEnter="searchQuery"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                  <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                    <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                  </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
      </a-col>
    </a-row>
    <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ y: 240 }"
          :rowSelection="{selectedRowKeys, onChange: onSelectChange, type: 'radio', columnTitle:'选择',columnWidth:'5%'}"
          :customRow="clickThenSelect"
          @change="handleTableChange">
        </a-table>

  </a-modal>
</template>

<script>
  import { httpAction,getAction } from '@/api/manage';
  //import pick from 'lodash.pick';
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import Options from "ant-design-vue/es/vc-pagination/Options";
  import AForm from "ant-design-vue/es/form/Form";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import { JeecgListMixin } from '@/mixins/JeecgListMixin';

  export default {
    name: 'SelectVendorModal',
    mixins:[JeecgListMixin],
    components: { AForm,
      AFormItem,
      ARow,
      ACol,
      Options,
      ATextarea},
    props: {
      value:{type:String}//,
      //modalShow: {type: Boolean }
    },
    data() {
      return {
        name: '供应商',
        displayKey: 'vendorName',
        returnKeys: ['id,','mdCode','vendorName','vendorType'],
        listUrl: '/base/mdmVendorInfo/listVendorForBiz',
        queryParamCode: 'mdCode',
        queryParamText: '供应商编号',
        multiple:false,
        modalShow:false,
        confirmLoading: false,
        columns: [
            { title: '供应商编号', dataIndex: 'mdCode', align: 'center', width: '20%' },
            { title: '供应商名称', dataIndex: 'vendorName', align: 'center', width: '35%' },
            { title: '供应商类型', dataIndex: 'vendorType', align: 'center', width: '20%' },
            { title: '供应商纳税人类型', dataIndex: 'vatFlag', align: 'center', width: '25%'}
        ]
        ,
        url: {
          list: "/base/mdmVendorInfo/listVendorForBiz"
        },
        dictOptions:{
        },
        ipagination:{ /* 分页参数 */
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '10', '50'],
          showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      selectedMainId:'',
      model: {}
     }
    },
    computed: {
      clearSelected(){
        this.onClearSelected();
      }
    },
    watch:{

    },
    methods: {
      /** 完成选择 */
      handleOk() {
        //let value = this.selectedTable.dataSource.map(data => data[this.valueKey])
        let record =  this.selectionRows;
        console.log("供应商编号=="+record[0].mdCode);
        try{
          this.$emit('setVendorInfo', record[0]);
        }catch (ex) {
          console.log(ex);
        }
        this.close();
      },
      /** 关闭弹窗 */
      close() {
        this.modalShow=false;
        this.$emit('update:visible', false)
      },
      clickThenSelect(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.split(","), [record]);
            }
          }
        }
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.selectedMainId=''
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedMainId=selectedRowKeys[0];
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.onClearSelected()
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
    setVendor(){
      try {
        this.$emit('setVendorName','名称是什么=='+this.value);
      }catch (ex) {
        console.log(ex);
      }
    },
      show(value) {
        console.log("请设置uvalue=="+value);
        this.onClearSelected();
        this.modalShow= value;
      }
    }
  }
</script>

<style lang="scss" scoped></style>