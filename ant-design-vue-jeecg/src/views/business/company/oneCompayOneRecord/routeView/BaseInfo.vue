<template>

    <a-spin :spinning="confirmLoading">
      <a-form :form="form"   >
      <a-row >
        <a-col span = '12'>
        <a-form-item label="企业名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['shortName',{}]" placeholder="请输入企业名称":disabled="disable"  ></a-input>
        </a-form-item>
        </a-col>
        <a-col span = '12'>
          <a-form-item label="企业简称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['shortName', validatorRules.shortName]" placeholder="请输入企业简称":disabled="disable"  ></a-input>
          </a-form-item>
        </a-col>
      </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="统一社会信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['socialCreditCode', validatorRules.socialCreditCode]" placeholder="请输入统一社会信用代码":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="企业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['companyType', validatorRules.companyType]" placeholder="请输入企业类型":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="所属行政区" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['administrativeRegion', validatorRules.administrativeRegion]" placeholder="请输入所属行政区":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="所属行业" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['industry', validatorRules.industry]" placeholder="请输入所属行业":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >

          <a-col span = '24'>
          <a-form-item label="企业地址" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <a-input v-decorator="['address', validatorRules.address]" placeholder="请输入企业地址":disabled="disable"  ></a-input>
          </a-form-item>
          </a-col>

        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['longitude', validatorRules.longitude]" placeholder="请输入经度":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dimension', validatorRules.dimension]" placeholder="请输入纬度":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="企业法人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['corporate', {}]" placeholder="请输入企业法人":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="法人联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['principalPhone', {}]" placeholder="请输入企业法人联系电话":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="经济类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['economicType',{}]" placeholder="请输入经济类型":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="企业行政隶属关系" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['affiliation', {}]" placeholder="请输入企业行政隶属关系":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="环保负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['envProtectPrincipal', {}]" placeholder="请输入环保负责人":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="环保负责人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['principalPhone', {}]" placeholder="请输入环保负责人电话":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="环保联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['envProtectContact', {}]" placeholder="请输入环保联系人":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="环保联系人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['contactPhone', {}]" placeholder="请输入环保联系人电话":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row >
          <a-col span = '12'>
            <a-form-item label="应急负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['emergencyLeader', {}]" placeholder="请输入应急负责人":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="应急负责人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['leaderPhone', {}]" placeholder="请输入应急负责人电话":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="所属流域" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['drainageArea', {}]" placeholder="请输入所属流域":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="邮政编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['postalCode', {}]" placeholder="请输入邮政编码":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="传真" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fax', {}]" placeholder="请输入传真":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="电子邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['email', {}]" placeholder="请输入电子邮箱":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="工业总产值（万元）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['industrialOutput', {}]" placeholder="请输入工业总产值":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="员工人数（人）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['staffCount', {}]" placeholder="请输入员工人数（人）":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="企业规模" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['enterpriseSize', {}]" placeholder="请输入企业规模":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="厂区面积（平方米）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['factoryArea', {}]" placeholder="请输入厂区面积（平方米）":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="是否位于化工集中区" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ischemicals', {}]" placeholder="请输入是否位于化工集中区":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="厂区面积（平方米）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['factoryArea', {}]" placeholder="请输入厂区面积（平方米）":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="母公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['parentCompany', {}]" placeholder="请输入母公司名称":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="集团公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['groupCompany', {}]" placeholder="请输入集团公司名称":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="注册资金（万元）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['registeCapital', {}]" placeholder="请输入注册资金（万元）":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="年销售收入(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['annualSalesIncome', {}]" placeholder="请输入年销售收入(万元)":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col span = '12'>
            <a-form-item label="年利润(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['annualProfit',{}]" placeholder="请输入年利润(万元)":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col span = '12'>
            <a-form-item label="资产总额（万元）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['totalAssets', {}]" placeholder="请输入资产总额（万元）":disabled="disable"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="工商注册地址" :labelCol="labelCols" :wrapperCol="wrapperCols">
          <a-input v-decorator="['registeAddress', {}]" placeholder="请输入工商注册地址":disabled="disable"  ></a-input>
        </a-form-item>
        <a-form-item label="经营范围" :labelCol="labelCols" :wrapperCol="wrapperCols">
          <a-input v-decorator="['operateScope', {}]" placeholder="请输入经营范围":disabled="disable"  ></a-input>
        </a-form-item>

        <a-form-item label="企业简介" :labelCol="labelCols" :wrapperCol="wrapperCols">
          <a-textarea
            :auto-size="{ minRows: 3, maxRows: 5 }"  v-decorator="['profile', {}]" placeholder="请输入企业简介" :disabled="disable" />
        </a-form-item>

      </a-form>
    </a-spin>

</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import {loadCompanyBaseInfo} from '../../../requestAction/request'

  export default {
    name: "BaseInfo",
    components: { 
    },
    props:{
      companyId:'',

    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        datecolums:['shortName','socialCreditCode','companyType','administrativeRegion','industry','address','longitude','dimension','corporate','economicType','affiliation','envProtectPrincipal','principalPhone','envProtectContact','contactPhone','emergencyLeader','leaderPhone','drainageArea','postalCode','fax','email','industrialOutput','staffCount','enterpriseSize','factoryArea','ischemicals','attachedPark','parentCompany','groupCompany','registeCapital','annualSalesIncome','annualProfit','totalAssets','registeAddress','operateScope','profile'],
        model: {},
        disable:true,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        labelCols: {
          xs: { span: 24},
          sm: { span: 3},
        },
        wrapperCols: {
          xs: {
            span: 24
          },
          sm: {
            span: 20

          }
        },
        confirmLoading: false,
        validatorRules: {

          companyName: {
            rules: [
              { required: true, message: '请输入企业名称'},
            ]
          },
          shortName: {
            rules: [
              { required: true, message: '请输入企业简称!'},
            ]
          },
          socialCreditCode: {
            rules: [
              { required: true, message: '请输入统一社会信用代码!'},
            ]
          },
          companyType: {
            rules: [
              { required: true, message: '请输入企业类型!'},
            ]
          },
          administrativeRegion: {
            rules: [
              { required: true, message: '请输入所属行政区!'},
            ]
          },
          industry: {
            rules: [
              { required: true, message: '请输入所属行业!'},
            ]
          },
          address: {
            rules: [
              { required: true, message: '请输入企业地址!'},
            ]
          },
          longitude: {
            rules: [
              { required: true, message: '请输入经度!'},
            ]
          },
          dimension: {
            rules: [
              { required: true, message: '请输入纬度!'},
            ]
          }
        },
        url: {
          // add: "/testOneDemo/companyBaseinfo/add",
          // edit: "/testOneDemo/companyBaseinfo/edit",
        }
      }
    },
    created () {
      console.log("baseinfo create")
      let that = this;
      let record = {};
      loadCompanyBaseInfo({companyId: this.companyId}).then((res)=>{
        if(res.success){
          record = res.result;
          that.edit(record);
        }else{
          console.log(res.message);
        }
      });





    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,...this.datecolums))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,...this.datecolums))
      },

      
    }
  }
</script>