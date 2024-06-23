<!--下拉树-->
<template>
  <a-tree-select
    allowClear
    labelInValue
    style="width: 100%"
    :disabled="disabled"
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    :placeholder="placeholder"
    :loadData="asyncLoadTreeData"
    :value="treeValue"
    :treeData="treeData"
    :multiple="multiple"
    @change="onChange"
  >
  </a-tree-select>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, watch, nextTick } from 'vue';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { loadDictItem, loadTreeData } from '/@/api/common/api';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage, createErrorModal } = useMessage();
  export default defineComponent({
    name: 'JCategorySelect',
    components: {},
    inheritAttrs: false,
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
      placeholder: {
        type: String,
        default: '请选择',
        required: false,
      },
      disabled: {
        type: Boolean,
        default: false,
        required: false,
      },
      condition: {
        type: String,
        default: '',
        required: false,
      },
      // 是否支持多选
      multiple: {
        type: [Boolean, String],
        default: false,
      },
      loadTriggleChange: {
        type: Boolean,
        default: false,
        required: false,
      },
      pid: {
        type: String,
        default: '',
        required: false,
      },
      pcode: {
        type: String,
        default: '',
        required: false,
      },
      back: {
        type: String,
        default: '',
        required: false,
      },
    },
    emits: ['options-change', 'change', 'update:value'],
    setup(props, { emit, refs }) {
      console.info(props);
      const emitData = ref<any[]>([]);
      const treeData = ref<any[]>([]);
      const treeValue = ref();
      const attrs = useAttrs();
      const [state, , , formItemContext] = useRuleFormItem(props, 'value', 'change', emitData);
      watch(
        () => props.value,
        () => {
          loadItemByCode();
        },
        { deep: true }
      );
      watch(
        () => props.pcode,
        () => {
          loadRoot();
        },
        { deep: true, immediate: true }
      );

      function loadRoot() {
        let param = {
          pid: props.pid,
          pcode: !props.pcode ? '0' : props.pcode,
          condition: props.condition,
        };
        console.info(param);
        loadTreeData(param).then((res) => {
            if(res && res.length>0){
                for (let i of res) {
                    i.value = i.key;
                    if (i.leaf == false) {
                        i.isLeaf = false;
                    } else if (i.leaf == true) {
                        i.isLeaf = true;
                    }
                }
                treeData.value = res;
						}
        });
      }

      function loadItemByCode() {
        if (!props.value || props.value == '0') {
          if(props.multiple){
            treeValue.value = [];
          }else{
            treeValue.value = { value: null, label: null };
          }
        } else {
          loadDictItem({ ids: props.value }).then((res) => {
            let values = props.value.split(',');
            treeValue.value = res.map((item, index) => ({
              key: values[index],
              value: values[index],
              label: item,
            }));
            if(!props.multiple){
              treeValue.value = treeValue.value[0];
            }
            onLoadTriggleChange(res[0]);
          });
        }
      }

      function onLoadTriggleChange(text) {
        //只有单选才会触发
        if (!props.multiple && props.loadTriggleChange) {
          backValue(props.value, text);
        }
      }

      function backValue(value, label) {
        let obj = {};
        if (props.back) {
          obj[props.back] = label;
        }
        emit('change', value, obj);
        emit("update:value",value)
      }

      function asyncLoadTreeData(treeNode) {
        let dataRef = treeNode.dataRef;
        return new Promise<void>((resolve) => {
          if (treeNode.children && treeNode.children.length > 0) {
            resolve();
            return;
          }
          let pid = dataRef.key;
          let param = {
            pid: pid,
            condition: props.condition,
          };
          loadTreeData(param).then((res) => {
            if (res) {
              for (let i of res) {
                i.value = i.key;
                if (i.leaf == false) {
                  i.isLeaf = false;
                } else if (i.leaf == true) {
                  i.isLeaf = true;
                }
              }
              addChildren(pid, res, treeData.value);
              resolve();
            }
          });
        });
      }

      function addChildren(pid, children, treeArray) {
        if (treeArray && treeArray.length > 0) {
          for (let item of treeArray) {
            if (item.key == pid) {
              if (!children || children.length == 0) {
                item.isLeaf = true;
              } else {
                item.children = children;
              }
              break;
            } else {
              addChildren(pid, children, item.children);
            }
          }
        }
      }

      function onChange(value) {
        if (!value) {
          emit('change', '');
          treeValue.value = '';
          emit("update:value",'')
        } else if (Array.isArray(value)) {
          let labels = [];
          let values = value.map((item) => {
            labels.push(item.label);
            return item.value;
          });
          backValue(values.join(','), labels.join(','));
          treeValue.value = value;
        } else {
          backValue(value.value, value.label);
          treeValue.value = value;
        }
        // update-begin--author:liaozhiyang---date:20240429---for：【QQYUN-9110】组件有值校验没消失
        nextTick(() => {
          formItemContext?.onFieldChange();
        });
        // update-end--author:liaozhiyang---date:20240429---for：【QQYUN-9110】组件有值校验没消失
      }

      function getCurrTreeData() {
        return treeData;
      }

      function validateProp() {
        let mycondition = props.condition;
        return new Promise((resolve, reject) => {
          if (!mycondition) {
            resolve();
          } else {
            try {
              let test = JSON.parse(mycondition);
              if (typeof test == 'object' && test) {
                resolve();
              } else {
                createMessage.error('组件JTreeSelect-condition传值有误，需要一个json字符串!');
                reject();
              }
            } catch (e) {
              createMessage.error('组件JTreeSelect-condition传值有误，需要一个json字符串!');
              reject();
            }
          }
        });
      }

      return {
        state,
        attrs,
        onChange,
        treeData,
        treeValue,
        asyncLoadTreeData,
      };
    },
  });
</script>
