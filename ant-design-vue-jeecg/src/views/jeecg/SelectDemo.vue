<template>
  <a-card :bordered="false" style="height:100%">

    <div class="table-page-search-wrapper">
      <a-form layout="inline" :form="form">


        <!--  字典下拉 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="性别">
              <j-dict-select-tag v-model="formData.sex" title="性别" dictCode="sex"/>
            <!--  <j-dict-select-tag title="性别" dictCode="sex" disabled/>-->
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.sex}}</a-col>
        </a-row>

        <!--  字典表下拉 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="字典表下拉">
              <j-dict-select-tag v-model="formData.user" placeholder="请选择用户" dictCode="sys_user,realname,id"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.user}}</a-col>
        </a-row>

        <!--  带条件字典表下拉 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="字典表下拉(带条件)">
              <j-dict-select-tag v-model="formData.user2" placeholder="请选择用户" dictCode="sys_user,realname,id,username!='admin' order by create_time"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.user2}}</a-col>
        </a-row>

        <!--  部门选择控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择部门">
              <j-select-depart v-decorator="['departId']" :trigger-change="true"></j-select-depart>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的部门ID(v-decorator):{{ getDepartIdValue() }}</a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择部门">
              <j-select-depart v-model="departId" :multi="true"></j-select-depart>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的部门ID(v-model):{{ departId }}</a-col>
        </a-row>

        <!--  用户选择控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择用户">
              <j-select-user-by-dep v-model="userRealName"></j-select-user-by-dep>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的用户(v-model):{{ userRealName }}</a-col>
        </a-row>

        <!--  用户选择控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择用户">
              <j-select-multi-user v-model="multiUser"></j-select-multi-user>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的用户(v-model):{{ multiUser }}</a-col>
        </a-row>

        <!--  JCheckbox -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="多选组合">
              <j-checkbox
                v-model="jcheckbox.values"
                :options="jcheckbox.options"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">多选组合(v-model)：{{ jcheckbox.values }}</a-col>
        </a-row>

        <!--  JCodeEditor -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="代码输入框" style="min-height: 120px">
              <j-code-editor
                language="javascript"
                v-model="jcodedditor.value"
                :fullScreen="true"
                style="min-height: 100px"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">代码输入框(v-model)：{{ jcodedditor.value }}</a-col>
        </a-row>

        <!--  JDate -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="日期选择框">
              <j-date v-model="jdate.value" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">日期选择框(v-model)：{{ jdate.value }}</a-col>
        </a-row>

        <!-- JEditor -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="富文本编辑器" style="min-height: 300px">
              <j-editor v-model="jeditor.value"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">富文本编辑器(v-model)：{{ jeditor.value }}</a-col>
        </a-row>

        <!-- JEllipsis -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="过长剪切">
              <j-ellipsis :value="jellipsis.value" :length="30"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">过长剪切：{{ jellipsis.value }}</a-col>
        </a-row>

        <!-- JGraphicCode -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="验证码">
              <j-graphic-code @success="generateCode"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">验证码：{{ jgraphicCode.value }}</a-col>
        </a-row>

        <!-- JSlider -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="滑块验证码">
              <j-slider @onSuccess="handleJSliderSuccess"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">滑块验证码验证通过：{{ jslider.value }}</a-col>
        </a-row>

        <!-- JSelectMultiple -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="多选下拉框">
              <j-select-multiple v-model="jselectMultiple.value" :options="jselectMultiple.options"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">多选下拉框(v-model)：{{ jselectMultiple.value }}</a-col>
        </a-row>

        <!-- JSelectMultiple -->
        <a-row :gutter="24">
          <a-col>

            <a-form-item label="最大化弹窗">
              <a-button @click="()=>modal.visible=true">最大化弹窗</a-button>
            </a-form-item>

            <a-modal
              :visible="modal.visible"
              :width="modal.width"
              :style="modal.style"
              @ok="()=>modal.visible=false"
              @cancel="()=>modal.visible=false">

              <template slot="title">
                <div style="width: 100%;height:20px;padding-right:32px;">
                  <div style="float: left;">{{ modal.title }}</div>
                  <div style="float: right;">
                    <a-button
                      icon="fullscreen"
                      style="width:56px;height:100%;border:0"
                      @click="handleClickToggleFullScreen"/>
                  </div>
                </div>
              </template>

              <template v-for="(i,k) of 30">
                <p :key="k">这是主体内容，高度是自适应的</p>
              </template>

            </a-modal>

          </a-col>
        </a-row>

        <!-- JSuperQuery 高级查询 -->
        <!-- JTreeSelect 树组件 -->
        <!-- JTreeTable 树列表 -->
        <!-- JUpload.上传组件 -->
        <!-- JImportModal 导入组件 -->

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="树字典">
              <j-tree-dict parentCode="B01" />
            </a-form-item>
          </a-col>
          <a-col :span="12"></a-col>
        </a-row>

        <!-- VueCron -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="cron表达式">
              <j-cron ref="innerVueCron" v-decorator="['cronExpression', {'initialValue':'0/1 * * * * ?'}]"  @change="setCorn"></j-cron>
              <!--              <j-cron ref="innerVueCron" v-model="cron" @change="setCorn"></j-cron>-->
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>

  </a-card>
</template>

<script>

  import JDictSelectTag from '../../components/dict/JDictSelectTag.vue'
  import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'
  import JCheckbox from '@/components/jeecg/JCheckbox'
  import JCodeEditor from '@/components/jeecg/JCodeEditor'
  import JDate from '@/components/jeecg/JDate'
  import JEditor from '@/components/jeecg/JEditor'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JGraphicCode from '@/components/jeecg/JGraphicCode'
  import JSlider from '@/components/jeecg/JSlider'
  import JSelectMultiple from '@/components/jeecg/JSelectMultiple'
  import JTreeDict from "../../components/jeecg/JTreeDict.vue";
  import JCron from "@/components/jeecg/JCron.vue";
  export default {
    name: 'SelectDemo',
    components: {
      JTreeDict,
      JDictSelectTag,
      JSelectDepart,
      JSelectUserByDep,
      JSelectMultiUser,
      JCheckbox,
      JCodeEditor,
      JDate, JEditor, JEllipsis, JGraphicCode, JSlider, JSelectMultiple,
      JCron
    },
    data() {
      return {
        selectList: [],
        selectedDepUsers: '',
        formData:{},
        form: this.$form.createForm(this),
        departId: '4f1765520d6346f9bd9c79e2479e5b12,57197590443c44f083d42ae24ef26a2c',
        userRealName: '',
        multiUser: '',
        jcheckbox: {
          values: 'spring,jeecgboot',
          options: [
            { label: 'Jeecg', value: 'jeecg' },
            { label: 'Jeecg-Boot', value: 'jeecgboot' },
            { label: 'Spring', value: 'spring', disabled: true },
            { label: 'MyBaits', value: 'mybatis' }
          ]
        },
        jcodedditor: {
          value: `function sayHi(word) {
  alert(word)
}
sayHi('hello, world!')`
        },
        jdate: {
          value: '2019-5-10 15:33:06'
        },
        jeditor: {
          value: '<h2 style="text-align: center;">富文本编辑器</h2> <p>这里是富文本编辑器。</p>'
        },
        jellipsis: {
          value: '这是一串很长很长的文字段落。这是一串很长很长的文字段落。这是一串很长很长的文字段落。这是一串很长很长的文字段落。'
        },
        jgraphicCode: {
          value: ''
        },
        jslider: {
          value: false
        },
        jselectMultiple: {
          options: [
            { text: '字符串', value: 'String' },
            { text: '整数型', value: 'Integer' },
            { text: '浮点型', value: 'Double' },
            { text: '布尔型', value: 'Boolean' }
          ],
          value: 'Integer,Boolean'
        },
        modal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
        cron: '',
      }
    },
    computed: {
      nameList: function() {

        var names = []
        for (var a = 0; a < this.selectList.length; a++) {
          names.push(this.selectList[a].name)
        }
        return names
      }
    },
    methods: {
      handleChange() {
      },
      getDepartIdValue() {
        return this.form.getFieldValue('departId')
      },
      selectOK: function(data) {
        this.selectList = data
      },
      handleSelect: function() {
        this.$refs.selectDemoModal.add()
      },
      selectReset() {
        this.selectList = []
      },
      //通过组织机构筛选选择用户
      onSearchDepUser() {
        this.$refs.JSearchUserByDep.showModal()
        this.selectedDepUsers = ''
        this.$refs.JSearchUserByDep.title = '根据部门查询用户'
      },
      onSearchDepUserCallBack(selectedDepUsers) {
        this.selectedDepUsers = selectedDepUsers
      },
      generateCode(value) {
        this.jgraphicCode.value = value.toLowerCase()
      },
      handleJSliderSuccess(value) {
        this.jslider.value = value
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.modal.fullScreen
        if (mode) {
          this.modal.width = '100%'
          this.modal.style.top = '20px'
        } else {
          this.modal.width = '1200px'
          this.modal.style.top = '50px'
        }
        this.modal.fullScreen = mode
      },
      setCorn(data){

        this.$nextTick(() => {
          this.form.cronExpression = data;
        })
      }
    }
  }
</script>
<style lang="less" scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>