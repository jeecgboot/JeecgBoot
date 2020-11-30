<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    width="45%"
    placement="right"
    v-if="visible"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="overflow: auto;padding-bottom: 53px;">
    <a-form-model ref="form" :layout="layout" :label-col="labelCol" :wrapper-col="wrapperCol" :model="router">
      <a-form-model-item label="路由ID">
        <a-input v-model="router.routerId" placeholder="路由唯一ID"/>
      </a-form-model-item>
      <a-form-model-item label="路由名称">
        <a-input v-model="router.name" placeholder="路由名称"/>
      </a-form-model-item>
      <a-form-model-item label="路由URI">
        <a-input v-model="router.uri" placeholder="路由URL"/>
      </a-form-model-item>
      <a-form-model-item label="路由状态" prop="status">
        <a-switch default-checked v-model="router.status"/>
      </a-form-model-item>

      <a-form-model-item prop="predicates" label="路由条件">
        <div v-for="(item,index) in router.predicates">
          <a-divider>{{item.name}}
            <a-icon type="delete" size="22" @click="removePredicate(router,index)"/>
          </a-divider>
          <div>
            <template v-for="(tag, index) in item.args">
              <a-input v-if="index==currentTagIndex" ref="input" type="text" size="small"
                       :style="{ width: '190px' }"
                       :value="tag"
                       @change="handleInputChange" @blur="handleInputEditConfirm(item,tag,index)"
                       @keyup.enter="handleInputEditConfirm(item,tag,index)"/>
              <a-tag v-else :key="tag" :closable="true" @close="() => removeTag(item,tag)" @click="editTag(tag,index)">
                {{ tag }}
              </a-tag>
            </template>
            <a-input v-if="inputVisible&&index==currentNameIndex" ref="input" type="text" size="small"
                     :style="{ width: '100px' }"
                     :value="inputValue"
                     @change="handleInputChange" @blur="handleInputConfirm(item)"
                     @keyup.enter="handleInputConfirm(item)"/>
            <a-tag v-else style="background: #fff; borderStyle: dashed;" @click="showInput(item,index)">
              <a-icon type="plus"/>
              新建{{item.name}}
            </a-tag>
          </div>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown>
            <a-menu slot="overlay" @click="predicatesHandleMenuClick">
              <a-menu-item :key="item" v-for="item in tagArray">{{item}}</a-menu-item>
            </a-menu>
            <a-button type="dashed" style="margin-left: 8px;width:100%"> 添加路由条件
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>
        </p>
      </a-form-model-item>
      <a-form-model-item prop="predicates" label="过滤器">
        <div v-for="(item,index) in router.filters">
          <a-divider>{{item.name}}
            <a-icon type="delete" size="22" @click="removeFilter(router,index)"/>
          </a-divider>
          <div v-for="(tag, index) in item.args" :key="tag.key">
            <a-input v-model="tag.key" placeholder="参数键" style="width: 45%; margin-right: 8px"/>
            <a-input v-model="tag.value" placeholder="参数值" style="width: 40%; margin-right: 8px"/>
            <a-icon class="dynamic-delete-button" type="minus-circle-o" @click="removeFilterParams(item,index)"/>
          </div>
          <a-button type="dashed" style="margin-left:28%;width: 30%" size="small" @click="addFilterParams(item)">
            <a-icon type="plus"/>
            添加参数
          </a-button>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown>
            <a-menu slot="overlay" @click="filterHandleMenuClick">
              <a-menu-item :key="item.key" :name="item.name" v-for="item in filterArray">{{item.name}}</a-menu-item>
            </a-menu>
            <a-button type="dashed" style="margin-left: 8px;width:100%"> 添加过滤器
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>
        </p>
      </a-form-model-item>
      <a-row :style="{textAlign:'right'}" class="drawer-bootom-button">
        <a-button :style="{marginRight: '8px'}" @click="handleCancel">
          关闭
        </a-button>
        <a-button @click="handleSubmit" type="primary">确定</a-button>
      </a-row>
    </a-form-model>

  </a-drawer>

</template>
<script>

  import { postAction } from '@/api/manage'

  export default {
    name: 'GateWayRouteModal',
    components: {},
    data() {
      return {
        layout: 'horizontal',
        labelCol: { span: 3 },
        wrapperCol: { span: 14 },
        currentNameIndex: 0,
        currentTagIndex:-1,
        predicates: {},
        filterArray: [{ key: 0, name: '熔断器' }, { key: 1, name: '限流过滤器' }],
        tagArray: ['Path', 'Host', 'Cookie', 'Header', 'Method', 'Query', 'After', 'Before', 'Between', 'RemoteAddr'],
        inputVisible: false,
        inputValue: '',
        url: {
          update: '/sys/gatewayRoute/updateAll',
          clear: '/sys/gatewayRoute/clearRedis'
        },
        router: this.getRouter(),
        title: '路由编辑',
        visible: false,
        loading: false
      }
    },
    methods: {
      getRouter() {
        return {
          routerId: '',
          name: '',
          uri: '',
          predicates: [],
          filters: []
        }
      },
      show(router) {
        if (router) {
          router.status=Boolean(router.status)
          this.router = router
        } else {
          this.router = this.getRouter()
          this.inputValue=''
        }
        this.visible = true
        this.currentTagIndex=-1
        this.currentNameIndex=-1
      },
      close() {
        this.reset()
        this.$emit('close')
        this.$refs['form'].resetFields()
        this.visible = false
      },
      //删除路由条件配置项
      removeTag(item, removedTag) {
        const tags = item.args.filter(tag => tag !== removedTag)
        item.args = tags
      },
      //添加路由选项
      predicatesHandleMenuClick(e) {
        this.router.predicates.push({
          args: [],
          name: e.key
        })
      },
      editTag(tag,index){
        this.currentTagIndex=index
      },
      //显示输入框
      showInput(item, index) {
        this.inputVisible = true
        this.currentNameIndex = index
      },
      //路由选项输入框失去焦点事件
      handleInputChange(e) {
        this.inputValue = e.target.value
      },
      //删除路由条件
      removePredicate(item, index) {
        item.predicates.splice(index, 1)
      },
      //删除过滤器参数
      removeFilterParams(item, index) {
        item.args.splice(index, 1)
      },
      //删除过滤器
      removeFilter(item, index) {
        item.filters.splice(index, 1)
      },
      //添加过滤器参数
      addFilterParams(item) {
        item.args.push({
          key: 'key' + item.args.length + 1,
          value: ''
        })
      },
      //过滤器添加事件
      filterHandleMenuClick(e) {
        if (e.key == 0) {
          this.router.filters.push({
            args: [  {
              key: 'name',
              value: 'default'
            },{
              key: 'fallbackUri',
              value: 'forward:/fallback'
            }],
            name:'Hystrix',
            title: this.filterArray[0].name
          })
        }
        if (e.key == 1) {
          this.router.filters.push({
            args: [ {
              key: 'key-resolver',
              value: '#{@ipKeyResolver}'
            }, {
              key: 'redis-rate-limiter.replenishRate',
              value: 20
            }, {
              key: 'redis-rate-limiter.burstCapacity',
              value: 20
            }],
            name:"RequestRateLimiter",
            title: this.filterArray[1].name
          })
        }
      },
      //输入框确认
      handleInputConfirm(item) {
        const inputValue = this.inputValue
        let tags = item.args
        if (inputValue && tags.indexOf(inputValue) === -1) {
          item.args = [...tags, inputValue]
        }
        console.log(tags)
        Object.assign(this, {
          tags,
          inputVisible: false,
          inputValue: ''
        })
        this.currentTagIndex=-1
      },
      //输入框确认
      handleInputEditConfirm(item,tag,index) {
        if(this.inputValue)
        {
          const inputValue = this.inputValue
          item.args[index]=inputValue
        }
        this.currentTagIndex=-1

      },
      reset() {
        this.expandedKeysss = []
        this.checkedKeys = []
        this.defaultCheckedKeys = []
        this.loading = false
      },
      //关闭弹窗
      handleCancel() {
        this.close()
      },
      //提交路由
      handleSubmit() {
        let { predicates, filters, ...other } = this.router
        let router = other
        router.predicates = JSON.stringify(this.router.predicates)
        router.filters = JSON.stringify(this.router.filters)
        postAction(this.url.update, {
          router
        }).then(res => {
          if (res.success) {
            this.close()
            this.$emit('ok')
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
        })

      }
    }
  }

</script>
<style lang="less" scoped>
  .drawer-bootom-button {
    position: absolute;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>