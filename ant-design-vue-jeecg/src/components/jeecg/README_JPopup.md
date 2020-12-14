# JPopup 弹窗选择组件

## 参数配置
| 参数           | 类型   | 必填 |说明|
|--------------|---------|----|---------|
| placeholder      |string   | | placeholder |
| code      |string   | | online报表编码 |
| orgFields      |string   | | online报表中显示的列,多个以逗号隔开 |
| destFields      |string   | | 回调对象的属性,多个以逗号隔开,其顺序和orgFields一一对应 |
| field      |string   | | v-model模式专用,表示从destFields中选择一个属性的值返回给当前组件 |
| triggerChange      |Boolean   | | v-decorator模式下需设置成true |
| callback(事件)      |function   | | 回调事件,v-decorator模式下用到,用于设置form控件的值 |

使用示例
----
```vue
<template>
  <a-form :form="form">
    <a-form-item label="v-model模式指定一个值返回至当前组件" style="width: 300px">
      <j-popup
        v-model="selectValue"
        code="user_msg"
        org-fields="username,realname"
        dest-fields="popup,other"
        field="popup"/>
      {{ selectValue }}
    </a-form-item>

    <a-form-item label="v-decorator模式支持回调多个值至当前表单" style="width: 300px">
      <j-popup
        v-decorator="['one']"
        :trigger-change="true"
        code="user_msg"
        org-fields="username,realname"
        dest-fields="one,two"
        @callback="popupCallback"/>
      {{ getFormFieldValue('one') }}
    </a-form-item>

    <a-form-item label="v-decorator模式被回调的值" style="width: 300px">
      <a-input v-decorator="['two']"></a-input>
    </a-form-item>


  </a-form >
</template>

<script>
  export default {
    data() {
      return {
        form: this.$form.createForm(this),
        selectValue:"",
      }
    },
    methods:{
      getFormFieldValue(field){
        return this.form.getFieldValue(field)
      },
      popupCallback(row){
        this.form.setFieldsValue(row)
      }
    }
  }
</script>
