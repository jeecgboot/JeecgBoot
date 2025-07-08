<template>
  <div ref="tableLinkCardRef">
    <div class="table-link-card">
      <div style="width: 100%; height: 100%">
        <div class="card-button" v-if="showButton">
          <a-button @click="handleAddRecord"><PlusOutlined />记 录</a-button>
        </div>

        <a-row>
          <a-col :span="fixedSpan ? fixedSpan : itemSpan" v-for="(record, index) in selectRecords" :key="index">
            <div class="card-item" :class="{ 'disabled-chunk': detail == true }">
              <!-- -->
              <div class="card-item-left" :class="{ 'show-right-image': getImageSrc(record) }">
                <span class="card-delete" v-if="disabled == false">
                  <minus-circle-filled @click="(e) => handleDeleteRecord(e, index)" />
                </span>
                <div class="card-inner">
                  <div class="card-main-content">{{ getMainContent(record) }}</div>
                  <div class="other-content">
                    <a-row>
                      <a-col :span="columnSpan" v-for="(col, cIndex) in realShowColumns" :key="cIndex">
                        <span class="label ellipsis">{{ col.title }}</span>
                        <span class="text ellipsis">{{ record[col.dataIndex] }}</span>
                      </a-col>
                    </a-row>
                  </div>
                </div>
              </div>

              <div class="card-item-image" v-if="getImageSrc(record)">
                <img v-if="getImageSrc(record)" :src="getImageSrc(record)" @error="handleImageError" />
              </div>
            </div>
          </a-col>
        </a-row>
      </div>
    </div>
    <LinkTableListModal @register="registerListModal" :multi="multi" :id="popTableName" @success="addCard" />
  </div>
</template>

<script>
  import { propTypes } from '/@/utils/propTypes';
  import { PlusOutlined, MinusCircleFilled } from '@ant-design/icons-vue';
  import { computed, ref, watch, onMounted } from 'vue';
  import { useLinkTable } from './hooks/useLinkTable';
  import { useModal } from '/@/components/Modal';
  import placeholderImage from '/@/assets/images/placeholderImage.png';
  import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';

  export default {
    name: 'JLinkTableCard',
    inheritAttrs: false,
    props: {
      // value字段
      valueField: propTypes.string.def(''),
      // 文本字段
      textField: propTypes.string.def(''),
      // 关联表名
      tableName: propTypes.string.def(''),
      // 是否多选
      multi: propTypes.bool.def(false),
      value: propTypes.oneOfType([propTypes.string, propTypes.number]),
      // ["表单字段,表字典字段","表单字段,表字典字段"]
      linkFields: propTypes.array.def([]),
      //是否是禁用页面
      disabled: propTypes.bool.def(false),
      // 是否是detail页面
      detail: propTypes.bool.def(false),
      // 图片字段
      imageField: propTypes.string.def(''),
    },
    components: {
      PlusOutlined,
      MinusCircleFilled,
      LinkTableListModal: createAsyncComponent(() => import('./components/LinkTableListModal.vue'), { loading: true }),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit }) {
      const popTableName = computed(() => {
        return props.tableName;
      });
      //注册model
      const [registerListModal, { openModal: openListModal }] = useModal();
      const selectValue = ref([]);
      const selectRecords = ref([]);
      const tableLinkCardRef = ref(null);
      const fixedSpan = ref(0);

      const showButton = computed(() => {
        if (props.disabled == true) {
          return false;
        }
        if (props.multi === false) {
          //单选且有值
          if (selectRecords.value.length > 0) {
            return false;
          }
        }
        return true;
      });

      const {
        auths,
        otherColumns,
        realShowColumns,
        tableColumns,
        textFieldArray,
        transData,
        loadOne,
        compareData,
        formatData,
        initFormData,
        getImageSrc,
        showImage,
      } = useLinkTable(props);

      const itemSpan = computed(() => {
        if (props.multi === true) {
          return 12;
        }
        return 24;
      });

      const columnSpan = computed(() => {
        if (props.multi === true) {
          return 24;
        }
        return 12;
      });

      function getMainContent(record) {
        if (record) {
          if (textFieldArray.value.length > 0) {
            let field = textFieldArray.value[0];
            return record[field];
          }
        }
      }

      function prevent(e) {
        e?.stopPropagation();
        e?.preventDefault();
      }


      function handleAddRecord(e) {
        prevent(e);
        openListModal(true, {
          // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
          selectedRowKeys: selectRecords.value.map((item) => item.id),
          selectedRows: [...selectRecords.value],
          // update-end--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
        });
      }

      function addCard(data) {
        // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
        let arr = [];
        // update-end--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
        for (let item of data) {
          let temp = { ...item };
          transData(temp);
          arr.push(temp);
        }
        selectRecords.value = arr;
        emitValue();
      }

      function updateCardData(formData) {
        let arr = selectRecords.value;
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].id === formData.id) {
            let temp = { ...formData };
            transData(temp);
            arr.splice(i, 1, temp);
          }
        }
        selectRecords.value = arr;
        emitValue();
      }

      function handleDeleteRecord(e, index) {
        prevent(e);
        let temp = selectRecords.value;
        if (temp && temp.length > index) {
          temp.splice(index, 1);
          selectRecords.value = temp;
        }
        emitValue();
      }

      function emitValue() {
        let arr = selectRecords.value;
        let values = [];
        let formData = {};
        let linkFieldArray = props.linkFields;
        if (arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            values.push(arr[i][props.valueField]);
            initFormData(formData, linkFieldArray, arr[i]);
          }
        } else {
          initFormData(formData, linkFieldArray);
        }
        let text = values.join(',');
        formatData(formData);
        emit('change', text, formData);
        emit('update:value', text);
      }

      watch(
        () => props.value,
        async (val) => {
          if (val) {
            let flag = compareData(selectRecords.value, val);
            if (flag === false) {
              let arr = await loadOne(val);
              selectRecords.value = arr;
            }
            //保证表单其他值回显成功
            if (props.linkFields && props.linkFields.length > 0) {
              emitValue();
            }
          } else {
            selectRecords.value = [];
          }
        },
        { immediate: true }
      );

      onMounted(() => {
        // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-281】分辨率小时关联记录文字被图片挤没了
        if (tableLinkCardRef.value.offsetWidth < 250) {
          fixedSpan.value = 24;
        }
        // update-end--author:liaozhiyang---date:20240522---for：【TV360X-281】分辨率小时关联记录文字被图片挤没了
      });
      // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-389】下拉和卡片关联记录图裂开给个默认图片
      const handleImageError = (event) => {
        event.target.src = placeholderImage;
      };
      // update-end--author:liaozhiyang---date:20240529---for：【TV360X-389】下拉和卡片关联记录图裂开给个默认图片

      return {
        popTableName,
        selectRecords,
        otherColumns,
        realShowColumns,
        showButton,
        selectValue,
        handleAddRecord,
        handleDeleteRecord,
        getMainContent,
        itemSpan,
        columnSpan,
        tableColumns,
        addCard,
        registerListModal,
        updateCardData,
        getImageSrc,
        showImage,
        auths,
        tableLinkCardRef,
        fixedSpan,
        placeholderImage,
        handleImageError,
      };
    },
  };
</script>

<style scoped lang="less">
  .table-link-card {
    box-sizing: border-box;
    position: relative;
    width: 100%;
    .card-button {
      margin-bottom: 10px !important;
    }
    .card-item {
      width: calc(100% - 10px);
      display: inline-flex;
      flex-direction: row;
      margin: 0px 10px 10px 0px;
      position: relative;
      border-radius: 3px;
      background-color: rgb(255, 255, 255);
      box-shadow:
        rgb(0 0 0 / 12%) 0px 1px 4px 0px,
        rgb(0 0 0 / 12%) 0px 0px 2px 0px;
      cursor: pointer;
      &:hover {
        /*  box-shadow: rgb(0 0 0 / 12%) 0px 4px 12px 0px, rgb(0 0 0 / 12%) 0px 0px 2px 0px;*/
        .card-item-left {
          .card-delete {
            display: inline-block;
          }
        }
      }

      &.disabled-chunk {
        background: none;
        box-shadow: none;
      }

      .card-item-image {
        width: 100px;
        padding-right: 8px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        overflow: hidden;
        img {
          width: 100%;
        }
      }

      .card-item-left {
        width: 100%;
        display: inline-block;
        &.show-right-image {
          width: calc(100% - 100px);
        }
        .card-delete {
          position: absolute;
          top: -8px;
          right: -8px;
          font-size: 16px;
          color: #757575;
          line-height: 1em;
          overflow: hidden;
          display: none;
        }

        .card-inner {
          flex: 1 1 0%;
          padding: 12px 16px;
          overflow: hidden;
          padding-bottom: 10px;

          .card-main-content {
            overflow: hidden;
            text-overflow: ellipsis;
            vertical-align: top;
            white-space: nowrap;
            margin-bottom: 8px;
            font-weight: 500;
            font-size: 14px;
            line-height: 20px;
            color: rgb(51, 51, 51);
          }

          .other-content {
            .text {
              font-size: 12px !important;
              display: inline-block;
              width: 80%;
            }
            .label {
              max-width: 160px;
              color: rgb(158, 158, 158);
              padding-right: 0.7em;
              display: inline-block;
            }
            .ellipsis {
              overflow: hidden;
              height: 22px;
              line-height: 22px;
              text-overflow: ellipsis;
              /*   vertical-align: top;*/
              white-space: nowrap;
            }
          }
        }
      }
    }
  }
</style>
