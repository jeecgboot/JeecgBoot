<template>
  <div>
      <a-input
        v-show="!departIds"
        @click="openSelect"
        placeholder="请点击选择部门"
        v-model="departNames"
        readOnly
        :disabled="componentDisabled"
        class="jvxe-select-input">
        <a-icon slot="prefix" type="cluster" title="部门选择控件"/>
      </a-input>
      <j-select-depart-modal
        ref="innerDepartSelectModal"
        :modal-width="modalWidth"
        :multi="multi"
        :rootOpened="rootOpened"
        :depart-id="departIds"
        :store="storeField()"
        :text="textField()"
        @ok="handleOK"
        @initComp="initComp"/>
    <span style="display: inline-block;height:100%;padding-left:14px" v-if="departIds" >
      <span @click="openSelect" style="display: inline-block;vertical-align: middle">{{ departNames }}</span>
      <a-icon v-if="!componentDisabled" style="margin-left:5px;vertical-align: middle" type="close-circle" @click="handleEmpty" title="清空"/>
    </span>
  </div>
</template>

<script>
  import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'
  import JSelectDepartModal from '@/components/jeecgbiz/modal/JSelectDepartModal'

  export default {
    name: 'JVxeDepartSelectCell',
    mixins: [JVxeCellMixins],
    components:{
      JSelectDepartModal
    },
    data() {
      return {
        departNames: '',
        departIds: '',
        selectedOptions: [],
        customReturnField: 'id'
      }
    },
    computed: {
      custProps() {
        const {departIds, originColumn: col, caseId, cellProps} = this
        return {
          ...cellProps,
          value: departIds,
          field: col.field || col.key,
          groupId: caseId,
          class: 'jvxe-select'
        }
      },
      componentDisabled(){
        if(this.cellProps.disabled==true){
          return true
        }
        return false
      },
      modalWidth(){
        if(this.cellProps.modalWidth){
          return this.cellProps.modalWidth
        }else{
          return 500
        }
      },
      multi(){
        if(this.cellProps.multi==false || this.originColumn.multi===false){
          return false
        }else{
          return true
        }
      },
      rootOpened(){
        if(this.cellProps.open==false){
          return false
        }else{
          return true
        }
      }
    },
    watch: {
      innerValue: {
        immediate: true,
        handler(val) {
          if (val == null || val === '') {
            this.departIds = ''
          } else {
            this.departIds = val
          }
        }
      }
    },
    methods: {
      openSelect(){
        // disabled 不弹窗
        if (this.componentDisabled) {
          return
        }
        this.$refs.innerDepartSelectModal.show()
      },
      handleEmpty(){
        this.handleOK('')
      },
      handleOK(rows) {
        let value = ''
        if (!rows && rows.length <= 0) {
          this.departNames = ''
          this.departIds = ''
        } else {
          let storeField = this.storeField();
          let textField = this.textField();
          value = rows.map(row => row[storeField]).join(',')
          this.departNames = rows.map(row => row[textField]).join(',')
          this.departIds = value
        }
        this.handleChangeCommon(this.departIds)
      },
      initComp(departNames){
        this.departNames = departNames
      },
      handleChange(value) {
        this.handleChangeCommon(value)
      },
      storeField(){
        if(this.originColumn){
          const str = this.originColumn.fieldExtendJson
          if(str){
            let json = JSON.parse(str)
            if(json && json.store){
              return json.store
            }
          }else if(this.originColumn.store){
            return this.originColumn.store
          }
        }
        return 'id'
      },
      textField(){
        if(this.originColumn){
          const str = this.originColumn.fieldExtendJson
          if(str){
            let json = JSON.parse(str)
            if(json && json.text){
              return json.text
            }
          }else if(this.originColumn.text){
            return this.originColumn.text
          }
        }
        return 'departName'
      }
    },
    enhanced: {
      switches: {
        visible: true
      },
      translate: {
        enabled: false
      }
    }
  }
</script>

<style scoped>
  /deep/ .jvxe-select-input .ant-input{
    border: none !important;
  }
</style>