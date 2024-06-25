<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" :title="getTitle" width="30%" @ok="handleSubmit" destroyOnClose showFooter>
    <a-form ref="formRef" :label-col="labelCol" :wrapper-col="wrapperCol" :model="router" :rules="validatorRules">
      <a-form-item label="路由ID" name="routerId">
        <a-input v-model:value="router.routerId" placeholder="路由唯一ID" />
      </a-form-item>
      <a-form-item label="路由名称" name="name">
        <a-input v-model:value="router.name" placeholder="路由名称" />
      </a-form-item>
      <a-form-item label="路由URI" name="uri">
        <a-input v-model:value="router.uri" placeholder="路由URL" />
      </a-form-item>
      <a-form-item label="路由状态" name="status">
        <a-switch default-checked :checked-value="1" :un-checked-value="0" v-model:checked="router.status" />
      </a-form-item>

      <a-form-item name="predicates" label="路由条件">
        <div v-for="(item, index) in router.predicates">
          <!--当name在noKeyRouter时不需要指定key-->
          <template v-if="noKeyRouter.includes(item.name)">
            <a-divider
              >{{ item.name }}
              <DeleteOutlined size="22" @click="removePredicate(router, index)" />
            </a-divider>
            <div>
              <template v-for="(tag, tagIndx) in item.args">
                <a-input
                  ref="inputRef2"
                  v-if="tagIndx == currentTagIndex && index == currentNameIndex"
                  type="text"
                  size="small"
                  :style="{ width: '190px' }"
                  v-model:value="state.inputValue"
                  @change="handleInputChange"
                  @blur="handleInputEditConfirm(item, tag, tagIndx)"
                  @keyup.enter="handleInputEditConfirm(item, tag, tagIndx)"
                />
                <a-tag
                  v-else
                  :key="tag"
                  style="margin-bottom: 2px"
                  :closable="true"
                  @close="() => removeTag(item, tag)"
                  @click="editTag(item, tag, tagIndx, index)"
                >
                  {{ tag }}
                </a-tag>
              </template>
              <a-input
                ref="inputRef"
                v-if="state.inputVisible && index == currentNameIndex"
                type="text"
                size="small"
                :style="{ width: '100px' }"
                v-model:value="state.inputValue"
                @change="handleInputChange"
                @blur="handleInputConfirm(item)"
                @keyup.enter="handleInputConfirm(item)"
              />
              <a-tag v-else style="background: #fff; borderstyle: dashed; margin-bottom: 2px" @click="showInput(item, index)">
                <PlusOutlined size="22" />
                新建{{ item.name }}
              </a-tag>
            </div>
          </template>
          <!--当name不在noKeyRouter时需要指定key-->
          <template v-if="!noKeyRouter.includes(item.name)">
            <a-divider
              >{{ item.name }}
              <DeleteOutlined size="22" @click="removePredicate(router, index)" />
            </a-divider>
            <div>
              <template v-for="(value, key) in item.args">
                <a-row>
                  <a-col :span="5" style="margin-top: 8px">
                    <span v-if="key == 'header'">Header名称</span>
                    <span v-if="key == 'regexp'">参数值</span>
                    <span v-if="key == 'param'">参数名</span>
                    <span v-if="key == 'name'">参数名</span>
                  </a-col>
                  <a-col :span="18">
                    <a-input
                      :defaultValue="value"
                      placeholder="参数值"
                      style="width: 70%; margin-right: 8px; margin-top: 3px"
                      @change="(e) => valueChange(e, item.args, key)"
                    />
                  </a-col>
                </a-row>
              </template>
            </div>
          </template>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown trigger="click">
            <template #overlay>
              <a-menu>
                <a-menu-item :key="item.name" v-for="item in tagArray" @click="predicatesHandleMenuClick(item)">{{ item.name }}</a-menu-item>
              </a-menu>
            </template>
            <a-button type="dashed" style="margin-left: 8px; width: 100%">
              添加路由条件
              <DownOutlined :size="22" />
            </a-button>
          </a-dropdown>
        </p>
      </a-form-item>
      <a-form-item name="predicates" label="过滤器">
        <div v-for="(item, index) in router.filters">
          <a-divider
            >{{ item.name }}
            <DeleteOutlined size="22" @click="removeFilter(router, index)" />
          </a-divider>
          <div v-for="(tag, index) in item.args" :key="tag.key">
            <!-- update-begin---author:wangshuai ---date: 20230829 for：vue3.0后自定义表单重复组件要用a-form-item-rest,否则会警告提醒------------  -->
            <a-form-item-rest>
              <a-input v-model:value="tag.key" placeholder="参数键" style="width: 45%; margin-right: 8px" />
              <a-input v-model:value="tag.value" placeholder="参数值" style="width: 40%; margin-right: 8px; margin-top: 3px" />
            </a-form-item-rest>
            <!-- update-end---author:wangshuai ---date: 20230829 for：vue3.0后自定义表单重复组件要用a-form-item-rest,否则会警告提醒------------  -->
            <CloseOutlined :size="22" @click="removeFilterParams(item, index)" />
          </div>
          <a-button type="dashed" style="margin-left: 28%; width: 37%; margin-top: 5px" size="small" @click="addFilterParams(item)">
            <DownOutlined :size="22" />
            添加参数
          </a-button>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown trigger="click">
            <template #overlay>
              <a-menu @click="filterHandleMenuClick">
                <a-menu-item :key="item.key" :name="item.name" v-for="item in filterArray">{{ item.name }}</a-menu-item>
              </a-menu>
            </template>
            <a-button type="dashed" style="margin-left: 8px; width: 100%">
              添加过滤器
              <DownOutlined />
            </a-button>
          </a-dropdown>
        </p>
      </a-form-item>
    </a-form>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, useAttrs, reactive, nextTick } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { saveOrUpdateRoute } from './route.api';
  import { DeleteOutlined } from '@ant-design/icons-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { CloseOutlined } from '@ant-design/icons-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  // 声明Emits
  const emit = defineEmits(['register', 'success']);
  const labelCol = reactive({
    xs: { span: 24 },
    sm: { span: 5 },
  });
  const wrapperCol = reactive({
    xs: { span: 24 },
    sm: { span: 16 },
  });
  const attrs = useAttrs();
  const isUpdate = ref(true);
  const inputRef = ref();
  const inputRef2 = ref();
  let state = reactive({
    inputVisible: false,
    inputValue: '',
  });
  const currentNameIndex = ref(0);
  const currentTagIndex = ref(-1);
  const validatorRules = {
    routerId: [{ required: true, message: 'routerId不能为空', trigger: 'blur' }],
    name: [{ required: true, message: '路由名称不能为空', trigger: 'blur' }],
    uri: [{ required: true, message: 'uri不能为空', trigger: 'blur' }],
  };
  const noKeyRouter = ['Path', 'Host', 'Method', 'After', 'Before', 'Between', 'RemoteAddr'];
  const filterArray = [/*{ key: 0, name: '熔断器' },*/ { key: 1, name: '限流过滤器' }];
  const tagArray = ref([
    {
      name: 'Path',
      args: [],
    },
    {
      name: 'Header',
      args: {
        header: '',
        regexp: '',
      },
    },
    {
      name: 'Query',
      args: {
        param: '',
        regexp: '',
      },
    },
    {
      name: 'Method',
      args: [],
    },
    {
      name: 'Host',
      args: [],
    },
    {
      name: 'Cookie',
      args: {
        name: '',
        regexp: '',
      },
    },
    {
      name: 'After',
      args: [],
    },
    {
      name: 'Before',
      args: [],
    },
    {
      name: 'Between',
      args: [],
    },
    {
      name: 'RemoteAddr',
      args: [],
    },
  ]);
  const formRef = ref();
  let router = reactive({});

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    isUpdate.value = !!data?.isUpdate;
    setDrawerProps({ confirmLoading: false });
    initRouter();
    if (unref(isUpdate)) {
      router = Object.assign(router, data.record);
    }
  });
  /**
   * 标题
   */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增路由' : '编辑路由'));

  //删除路由条件配置项
  function removeTag(item, removedTag) {
    let tags = item.args.filter((tag) => tag !== removedTag);
    item.args = tags;
  }

  //初始化参数
  function initRouter() {
    router = Object.assign(router, {
      id: '',
      routerId: '',
      name: '',
      uri: '',
      status: 1,
      predicates: [],
      filters: [],
    });
  }

  //添加路由选项
  function predicatesHandleMenuClick(e) {
    router.predicates.push({
      args: e.args,
      name: e.name,
    });
  }

  /**
   * 值修改事件
   * @param e
   * @param item
   * @param key
   */
  function valueChange(e, item, key) {
    item[key] = e.target.value;
  }

  function editTag(item, tag, tagIndex, index) {
    currentNameIndex.value = index;
    currentTagIndex.value = tagIndex;
    state.inputValue = tag;
    nextTick(() => {
      inputRef2.value[0].focus();
    });
  }

  //显示输入框
  function showInput(item, index) {
    state.inputValue = '';
    state.inputVisible = true;
    currentNameIndex.value = index;
    nextTick(() => {
      inputRef.value[0].focus();
    });
  }

  //路由选项输入框改变事件
  function handleInputChange(e) {
    console.info('change', e);
    console.info('change', e.target.value);
    //state.value = e.target.value;
    //state.tag=true;
  }

  //删除路由条件
  function removePredicate(item, index) {
    item.predicates.splice(index, 1);
  }

  //删除过滤器参数
  function removeFilterParams(item, index) {
    item.args.splice(index, 1);
  }

  //删除过滤器
  function removeFilter(item, index) {
    item.filters.splice(index, 1);
  }

  //添加过滤器参数
  function addFilterParams(item) {
    item.args.push({
      key: 'key' + item.args.length + 1,
      value: '',
    });
  }

  //过滤器添加事件
  function filterHandleMenuClick(e) {
    if (e.key == 0) {
      router.filters.push({
        args: [
          {
            key: 'name',
            value: 'default',
          },
          {
            key: 'fallbackUri',
            value: 'forward:/fallback',
          },
        ],
        name: 'Hystrix',
        title: filterArray[0].name,
      });
    }
    console.info('test', router);
    if (e.key == 1) {
      router.filters.push({
        args: [
          {
            key: 'key-resolver',
            value: '#{@ipKeyResolver}',
          },
          {
            key: 'redis-rate-limiter.replenishRate',
            value: 20,
          },
          {
            key: 'redis-rate-limiter.burstCapacity',
            value: 20,
          },
        ],
        name: 'RequestRateLimiter',
        title: filterArray[0].name,
      });
    }
  }

  //输入框确认
  function handleInputConfirm(item) {
    let tags = item.args;
    const inputValue = state.inputValue;
    if (inputValue && tags.indexOf(inputValue) === -1) {
      item.args = [...tags, state.inputValue];
    }
    state.inputVisible = false;
    state.inputValue = '';
    currentTagIndex.value = -1;
    currentNameIndex.value = -1;
  }

  //输入框确认
  function handleInputEditConfirm(item, tag, index) {
    const inputValue = state.inputValue;
    if (inputValue) {
      item.args[index] = state.inputValue;
    }
    currentTagIndex.value = -1;
    currentNameIndex.value = -1;
  }

  //关闭弹窗
  function handleCancel() {}

  /**
   * 提交
   */
  async function handleSubmit() {
    await formRef.value.validate().then(() => {
      try {
        setDrawerProps({ confirmLoading: true });
        //重新构造表单提交对象,切记不可修改router对象，数组修改为字符串容易造成界面混乱
        let params = Object.assign({}, router, {
          predicates: JSON.stringify(router.predicates),
          filters: JSON.stringify(router.filters),
        });
        //提交表单
        saveOrUpdateRoute({ router: params }).then(() => {
          closeDrawer();
          //刷新列表
          emit('success');
        });
      } finally {
        setDrawerProps({ confirmLoading: false });
      }
    });
  }
</script>
