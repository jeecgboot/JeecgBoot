<template>
  <a-card :bordered="false">
    <a-form @submit="handleSubmit" :form="form">
      <a-col :md="24" :sm="24">
      <a-form-item label="Note" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
        <a-input v-decorator="['note',{rules: [{ required: true, message: 'Please input your note!' }]}]"/>
      </a-form-item>
      </a-col>
      <a-col :md="24" :sm="24">
      <a-form-item label="Gender" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
        <a-select v-decorator="['gender',{rules: [{ required: true, message: 'Please select your gender!' }]}]" placeholder="Select a option and change input text above" @change="this.handleSelectChange">
          <a-select-option value="male">male</a-select-option>
          <a-select-option value="female">female</a-select-option>
        </a-select>
      </a-form-item>
      </a-col>
      <a-col :md="24" :sm="24">
      <a-form-item label="Gender" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
        <a-cascader :options="areaOptions" @change="onChange" :showSearch="{filter}" placeholder="Please select" />
      </a-form-item>
      </a-col>
      <a-form-item :wrapperCol="{ span: 12, offset: 5 }">
        <a-col :md="24" :sm="24">
          <a-form-item :wrapperCol="{ span: 12, offset: 5 }">
            <a-button type="primary" htmlType="submit">Submit</a-button>
          </a-form-item>
        </a-col>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'
  export default {
    props: ['sex','name'],
    data () {
      return {
        formLayout: 'horizontal',
        form: this.$form.createForm(this),
        areaOptions:[]
      }
    },
    methods: {
      handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values)
          }
        })
      },
      handleSelectChange (value) {
        console.log(value)
        this.form.setFieldsValue({
          note: `Hi, ${value === 'male' ? 'man' : 'lady'}!`,
        })
      },
      onChange(value, selectedOptions) {
        console.log(value, selectedOptions);
      },
      filter(inputValue, path) {
        return (path.some(option => (option.label).toLowerCase().indexOf(inputValue.toLowerCase()) > -1));
      },
    },
    created (){
      console.log('============= online href common props ============= ');
      console.log('props sex: ',this.sex);
      console.log('props name: ',this.name);

      getAction('/api/area').then((res) => {
          console.log("------------")
          console.log(res)
          this.areaOptions = res;
      })
    },
    watch: {
      $route: {
        immediate: true,
        handler() {
          console.log('============= online href  $route props ============= ');
          let sex = this.$route.query.sex
          console.log('$route sex: ', sex);
        }
      }
    },
  }
</script>