<template>
  <a-card :bordered="false" style="height:100%;padding-bottom:200px; ">

    <div class="table-page-search-wrapper">
      <a-form layout="inline" :form="form">


        <!--  字典下拉 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="性别">
              <j-dict-select-tag v-model="formData.sex" title="性别" dictCode="sex" placeholder="请选择性别"/>
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


        <!-- 字典搜索  -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="字典搜索(同步)">
              <j-search-select-tag placeholder="请做出你的选择" v-model="formData.searchValue" :dictOptions="searchOptions">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.searchValue}}</a-col>
        </a-row>

        <!--  字典搜索 异步加载 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="字典搜索(异步)">
              <j-search-select-tag
                placeholder="请做出你的选择"
                v-model="formData.asyncSelectValue"
                dict="sys_depart,depart_name,id"
                :async="true">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.asyncSelectValue}}</a-col>
        </a-row>

        <!--  JMultiSelectTag -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="字典下拉(多选)">
              <j-multi-select-tag
                v-model="formData.selMuti"
                dictCode="sex"
                placeholder="请选择">
              </j-multi-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span="12">多选组合(v-model)：{{ formData.selMuti }}</a-col>
        </a-row>

        <!--  部门选择控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择部门 自定义返回值">
              <j-select-depart v-model="orgCodes" :trigger-change="true" customReturnField="orgCode" :multi="true"></j-select-depart>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的部门Code(v-decorator):{{ orgCodes }}</a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择部门">
              <j-select-depart v-model="departId" :multi="true"></j-select-depart>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的部门ID(v-model):{{ departId }}</a-col>
        </a-row>

        <!--  通过部门选择用户控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择用户">
              <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的用户(v-model):{{ userIds }}</a-col>
        </a-row>

        <!--  用户选择控件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择用户">
              <j-select-multi-user v-model="multiUser" :query-config="selectUserQueryConfig"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的用户(v-model):{{ multiUser }}</a-col>
        </a-row>

        <!-- 角色选择 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择角色">
              <j-select-role v-model="formData.selectRole" @change="changeMe"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.selectRole}}</a-col>
        </a-row>

        <!-- 职务选择 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择职务">
              <j-select-position  :buttons="false" :disabled="true" v-model="formData.selectPosition" />
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData.selectPosition}}</a-col>
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

            <a-form-item label="JModal弹窗">
              <a-button style="margin-right: 8px;" @click="()=>modal.visible=true">点击弹出JModal</a-button>
              <span style="margin-right: 8px;">全屏化：<a-switch v-model="modal.fullscreen"/></span>
              <span style="margin-right: 8px;">允许切换全屏：<a-switch v-model="modal.switchFullscreen"/></span>

            </a-form-item>

            <j-modal
              :visible.sync="modal.visible"
              :width="1200"
              :title="modal.title"
              :fullscreen.sync="modal.fullscreen"
              :switchFullscreen="modal.switchFullscreen"
            >

              <template v-for="(i,k) of 30">
                <p :key="k">这是主体内容，高度是自适应的</p>
              </template>

            </j-modal>

          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="树字典">
              <j-tree-dict v-model="formData.treeDict" placeholder="请选择树字典" parentCode="B01" />
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的值(v-model)：{{ formData.treeDict }}</a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="下拉树选择">
              <j-tree-select
                v-model="formData.treeSelect"
                placeholder="请选择菜单"
                dict="sys_permission,name,id"
                pidField="parent_id"
                pidValue=""
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的值(v-model)：{{ formData.treeSelect }}</a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="下拉树多选">
              <j-tree-select
                v-model="formData.treeSelectMultiple"
                placeholder="请选择菜单"
                dict="sys_permission,name,id"
                pidField="parent_id"
                pidValue=""
                multiple
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的值(v-model)：{{ formData.treeSelectMultiple }}</a-col>
        </a-row>

        <!-- 分类字典树 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="分类字典树">
              <j-category-select v-model="formData.selectCategory" pcode="B01" :multiple="true"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的值(v-model)：{{ formData.selectCategory }}</a-col>
        </a-row>

        <!-- VueCron -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="cron表达式">
              <j-cron ref="innerVueCron" v-decorator="['cronExpression', { initialValue: '* * * * * ? *' }]" @change="setCorn"></j-cron>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="高级查询">
              <j-super-query :fieldList="superQuery.fieldList" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="高级查询（自定义按钮）">
              <j-super-query :fieldList="superQuery.fieldList">
                <!--
                    v-slot:button 可以更高自由的定制按钮
                    参数介绍：
                      isActive: 是否是激活状态（已有高级查询条件生效）
                      isMobile: 当前是否是移动端，可针对移动端展示不同的样式
                          open: 打开弹窗，一个方法，可绑定点击事件
                         reset: 重置所有查询条件，一个方法，可绑定点击事件
                -->
                <template v-slot:button="{isActive,isMobile,open,reset}">
                  <!-- 定义的是普通状态下的按钮 -->
                  <a-button v-if="!isActive" type="primary" ghost icon="clock-circle" @click="open()">高级查询</a-button>
                  <!-- 定义的当有高级查询条件生效状态下的按钮 -->
                  <a-button-group v-else>
                    <a-button type="primary" ghost @click="open()">
                      <a-icon type="plus-circle" spin/>
                      <span>高级查询</span>
                    </a-button>
                    <a-button v-if="isMobile" type="primary" ghost icon="delete" @click="reset()"/>
                  </a-button-group>
                </template>
              </j-super-query>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="图片上传">
              <j-image-upload v-model="imgList"></j-image-upload>
            </a-form-item>
          </a-col>
          <a-col :span="12">选中的值(v-model)：{{ imgList }}</a-col>
        </a-row>
        <a-row :gutter="24" style="margin-top: 65px;margin-bottom:50px;">
          <a-col :span="12">
            <a-form-item label="文件上传">
              <j-upload v-model="fileList"></j-upload>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            选中的值(v-model)：
            <j-ellipsis :value="fileList" :length="30" v-if="fileList.length>0"/>
          </a-col>
        </a-row>

        <!-- 特殊查询组件 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="特殊查询组件">
              <a-row>
                <a-col :span="16">
                  <j-input v-model="formData.jInput" :type="jInput.type"/>
                </a-col>
                <a-col :span="3" style="text-align: right;" >查询类型：</a-col>
                <a-col :span="5">
                  <a-select v-model="jInput.type" :options="jInput.options"></a-select>
                </a-col>
              </a-row>
            </a-form-item>
          </a-col>
          <a-col :span="12">输入的值(v-model)：{{ formData.jInput }}</a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="15">
            <a-form-item label="MarkdownEditor" style="min-height: 300px">
              <j-markdown-editor v-model="content"></j-markdown-editor>
            </a-form-item>
          </a-col>
          <a-col :span="9">
            输入的值(v-model)：{{ content }}
          </a-col>
        </a-row>

        <!-- 省市县级联 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="省市县级联">
              <j-area-linkage v-model="formData.areaLinkage1" type="cascader"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">输入的值(v-model)：{{ formData.areaLinkage1 }}</a-col>
        </a-row>


        <!-- 省市县级联 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="省市县级联">
              <j-area-linkage v-model="formData.areaLinkage2" type="select"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">输入的值(v-model)：{{ formData.areaLinkage2 }}</a-col>
        </a-row>

        <!-- 功能示例：关闭当前页面 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="功能示例：关闭当前页面">
              <a-button type="primary" @click="handleCloseCurrentPage">点击关闭当前页面</a-button>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- JPopup示例 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="JPopup示例">
              <j-popup v-model="formData.jPopup" code="demo" field="name" orgFields="name" destFields="name" :multi="true"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">选择的值(v-model)：{{ formData.jPopup }}</a-col>
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
  import JSelectRole from '@/components/jeecgbiz/JSelectRole'
  import JCheckbox from '@/components/jeecg/JCheckbox'
  import JCodeEditor from '@/components/jeecg/JCodeEditor'
  import JDate from '@/components/jeecg/JDate'
  import JEditor from '@/components/jeecg/JEditor'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JSlider from '@/components/jeecg/JSlider'
  import JSelectMultiple from '@/components/jeecg/JSelectMultiple'
  import JTreeDict from "../../components/jeecg/JTreeDict.vue";
  import JCron from "@/components/jeecg/JCron.vue";
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  import JSuperQuery from '@/components/jeecg/JSuperQuery'
  import JUpload from '@/components/jeecg/JUpload'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JSelectPosition from '@comp/jeecgbiz/JSelectPosition'
  import JCategorySelect from '@comp/jeecg/JCategorySelect'
  import JMultiSelectTag from '@comp/dict/JMultiSelectTag'
  import JInput from '@comp/jeecg/JInput'
  import JAreaLinkage from '@comp/jeecg/JAreaLinkage'
  import JMarkdownEditor from '@/components/jeecg/JMarkdownEditor/index'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: 'SelectDemo',
    inject:['closeCurrent'],
    components: {
      JMarkdownEditor,
      JAreaLinkage,
      JInput,
      JCategorySelect,
      JSelectPosition,
      JImageUpload,
      JUpload,
      JTreeDict,
      JDictSelectTag,
      JSelectDepart,
      JSelectUserByDep,
      JSelectMultiUser,
      JSelectRole,
      JCheckbox,
      JCodeEditor,
      JDate, JEditor, JEllipsis, JSlider, JSelectMultiple,
      JCron, JTreeSelect, JSuperQuery, JMultiSelectTag,
      JSearchSelectTag
    },
    data() {
      return {
        selectList: [],
        selectedDepUsers: '',
        formData: {
          areaLinkage1: '110105',
          areaLinkage2: '140221',
          sex: 1
        },
        form: this.$form.createForm(this),
        departId: '57197590443c44f083d42ae24ef26a2c,a7d7e77e06c84325a40932163adcdaa6',
        orgCodes: 'A02A01,A02A02',
        userIds: 'admin',
        multiUser: 'admin,jeecg',
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
          fullscreen: true,
          switchFullscreen: true,
        },
        cron: '',
        superQuery: {
          fieldList: [
            { type: 'input', value: 'name', text: '姓名', },
            { type: 'select', value: 'sex', text: '性别', dictCode: 'sex' },
            { type: 'number', value: 'age', text: '年龄', },
            {
              type: 'select', value: 'hobby', text: '爱好',
              options: [
                { label: '音乐', value: '1' },
                { label: '游戏', value: '2' },
                { label: '电影', value: '3' },
                { label: '读书', value: '4' },
              ]
            },
          ]
        },
        fileList:[],
        imgList:[],
        jInput: {
          type: 'like',
          options: [
            { value: 'like', label: '模糊（like）' },
            { value: 'ne', label: '不等于（ne）' },
            { value: 'ge', label: '大于等于（ge）' },
            { value: 'le', label: '小于等于（le)' },
          ],
        },
        content: '',
        searchOptions:[{
          text:"选项一",
          value:"1"
        },{
          text:"选项二",
          value:"2"
        },{
          text:"选项三",
          value:"3"
        }],

        // 选择用户查询条件配置
        selectUserQueryConfig: [
          {key: 'phone', label: '电话'},
        ],
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
      getOrgCodesValue() {
        return this.form.getFieldValue('orgCodes')
      },
      changeMe() {
        console.log('you so ...  , change Me')
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
      handleJSliderSuccess(value) {
        this.jslider.value = value
      },
      setCorn(data){
        this.$nextTick(() => {
          this.form.cronExpression = data;
        })
      },

      handleCloseCurrentPage() {
        // 注意：以下代码必须存在
        // inject:['closeCurrent'],
        this.closeCurrent()
      },

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