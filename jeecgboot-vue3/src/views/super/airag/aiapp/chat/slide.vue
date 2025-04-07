<template>
  <div class="slide-wrap">
    <div class="header">
      <img class="header-image" :src="getImage()" />
      <div class="header-name">{{ appData.name || 'AI助手' }}</div>
    </div>
    <div class="createArea">
      <a-button type="dashed" @click="handleCreate">新建聊天</a-button>
    </div>
    <div class="historyArea">
      <ul>
        <li
          v-for="(item, index) in dataSource.history"
          :key="item.id"
          class="list"
          :class="[item.id == dataSource.active ? 'active' : 'normal', dataSource.history.length == 1 ? 'last' : '']"
          @click="handleToggleChat(item, index)"
        >
          <i class="icon message">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              aria-hidden="true"
              role="img"
              class="iconify iconify--ri"
              width="1em"
              height="1em"
              viewBox="0 0 24 24"
            >
              <path
                fill="currentColor"
                d="M2 8.994A5.99 5.99 0 0 1 8 3h8c3.313 0 6 2.695 6 5.994V21H8c-3.313 0-6-2.695-6-5.994zM20 19V8.994A4.004 4.004 0 0 0 16 5H8a3.99 3.99 0 0 0-4 3.994v6.012A4.004 4.004 0 0 0 8 19zm-6-8h2v2h-2zm-6 0h2v2H8z"
              ></path>
            </svg>
          </i>
          <a-input
            class="title"
            ref="inputRef"
            v-if="item.isEdit"
            :defaultValue="item.title"
            placeholder="请输入标题"
            @change="handleInputChange"
            @keyup.enter="inputBlur(item)"
          />
          <span class="title" v-else>{{ item.title }}</span>
          <span class="icon edit" @click.stop="handleEdit(item)" v-if="!item.isEdit && !item.disabled">
            <svg xmlns="http://www.w3.org/2000/svg" role="img" class="iconify iconify--ri" width="1em" height="1em" viewBox="0 0 24 24">
              <path
                fill="currentColor"
                d="M6.414 15.89L16.556 5.748l-1.414-1.414L5 14.476v1.414zm.829 2H3v-4.243L14.435 2.212a1 1 0 0 1 1.414 0l2.829 2.829a1 1 0 0 1 0 1.414zM3 19.89h18v2H3z"
              ></path>
            </svg>
          </span>
          <span class="icon del">
            <a-popconfirm
              :overlayStyle="{ 'z-index': 9999 }"
              title="确定删除此记录？"
              placement="bottom"
              ok-text="确定"
              cancel-text="取消"
              @confirm.stop="handleDel(item)"
            >
              <svg xmlns="http://www.w3.org/2000/svg" role="img" class="iconify iconify--ri" width="1em" height="1em" viewBox="0 0 24 24">
                <path
                  fill="currentColor"
                  d="M17 6h5v2h-2v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V8H2V6h5V3a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1zm1 2H6v12h12zm-9 3h2v6H9zm4 0h2v6h-2zM9 4v2h6V4z"
                ></path>
              </svg>
            </a-popconfirm>
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { defHttp } from '@/utils/http/axios';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import defaultImg from '../img/ailogo.png';
  const props = defineProps(['dataSource', 'appData']);
  const emit = defineEmits(['save', 'click', 'reloadRight', 'prologue']);
  const inputRef = ref(null);
  const router = useRouter();
  let inputValue = '';
  //新建聊天
  const handleCreate = () => {
    const uuid = getUuid();
    props.dataSource.history.unshift({ title: '新建聊天', id: uuid, isEdit: false, disabled: true });
    // 新建第一个(需要高亮选中)
    props.dataSource.active = uuid;
    emit('click', "新建聊天", 0);
  };
  // 切换聊天
  const handleToggleChat = (item, index) => {
    if (item.id != props.dataSource.active) {
      props.dataSource.active = item.id;
      emit('click', item.title, index);
    }
  };
  const handleInputChange = (e) => {
    inputValue = e.target.value.trim();
  };
  // 失去焦点
  const inputBlur = (item) => {
    item.isEdit = false;
    item.title = inputValue;
    defHttp
      .put(
        {
          url: '/airag/chat/conversation/update/title',
          params: { id: item.id, title: inputValue },
        },
        { joinParamsToUrl: true }
      )
      .then((res) => {});
  };
  // 编辑
  const handleEdit = (item) => {
    console.log(item);
    item.isEdit = true;
    inputValue = item.title;
  };
  // 保存
  const handleSave = (item) => {
    item.isEdit = false;
    item.title = inputValue;
  };

  /**
   * 删除
   * @param data
   */
  function handleDel(data) {
    const findIndex = props.dataSource.history.findIndex((item) => item.id == data.id);
    if (findIndex != -1) {
      props.dataSource.history.splice(findIndex, 1);
      // 删除的是当前active的，active往前移，前面没了往后移。
      if (props.dataSource.history.length) {
        if (props.dataSource.active == data.id) {
          if (findIndex > 0) {
            props.dataSource.active = props.dataSource.history[findIndex - 1].id;
          } else {
            props.dataSource.active = props.dataSource.history[0].id;
          }
        }
        emit('click', props.dataSource.history[0].title, findIndex);
      } else {
        //  删没了（删除了最后一个）
        props.dataSource.active = null;
        emit('click', "", -1);
      }
    }
    //update-begin---author:wangshuai---date:2025-03-12---for:【QQYUN-11560】新建聊天内容为空，无法删除---
    if(data.disabled){
      return;
    }
    //update-end---author:wangshuai---date:2025-03-12---for:【QQYUN-11560】新建聊天内容为空，无法删除---
    defHttp.delete({
      url: '/airag/chat/conversation/' + data.id,
    },{ isTransformResponse: false });
  }

  /**
   * 获取图片
   */
  function getImage() {
    return props.appData.icon ? getFileAccessHttpUrl(props.appData.icon) : defaultImg;
  }

  watch(
    () => inputRef.value,
    (newVal: any) => {
      if (newVal?.length) {
        newVal[0].focus();
      }
    },
    { deep: true }
  );

  // 指定长度和基数
  const getUuid = (len = 10, radix = 16) => {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid: any = [],
      i;
    radix = radix || chars.length;

    if (len) {
      for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
    } else {
      var r;
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
      uuid[14] = '4';
      for (i = 0; i < 36; i++) {
        if (!uuid[i]) {
          r = 0 | (Math.random() * 16);
          uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
        }
      }
    }
    return uuid.join('');
  };
</script>

<style scoped lang="less">
  .slide-wrap {
    border-right: 1px solid #e5e7eb;
    height: 100%;
    display: flex;
    flex-direction: column;
    .historyArea {
      padding: 20px;
      padding-top: 0;
      flex: 1;
      min-height: 0;
      overflow: auto;
      &::-webkit-scrollbar {
        width: 8px;
        height: 8px;
      }
    }
    .historyArea ul li:hover {
      .del {
        display: block;
      }
    }
    .createArea {
      padding: 20px;
      padding-bottom: 0;
    }
    .ant-btn {
      width: 100%;
      margin-bottom: 10px;
    }
  }
  ul {
    margin-bottom: 0;
  }
  .list {
    width: 100%;
    padding-top: 0.75rem;
    padding-bottom: 0.75rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
    border-radius: 0.375rem;
    border-width: 1px;
    cursor: pointer;
    margin-bottom: 10px;
    color: #333;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    &:hover,
    &.active {
      border-color: @primary-color;
      color: @primary-color;
    }
    .edit,
    .save,
    .del {
      display: none;
    }
    &.active {
      .edit,
      .save,
      .del {
        display: block;
      }
      &.last {
        .del {
          display: none;
        }
      }
    }
    .message {
      margin-right: 8px;
    }
    .edit {
      margin-right: 8px;
    }
    .title {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      &.ant-input {
        margin-right: 20px;
      }
    }
    svg {
      vertical-align: middle;
    }
  }
  :deep(.ant-popover) {
    z-index: 9999 !important;
  }
  :deep(.ant-popconfirm) {
    z-index: 9999 !important;
  }
  .header {
    display: flex;
    padding: 20px 4px 0 4px;
    margin-left: 16px;
    .header-image {
      height: 35px;
      width: 35px;
      border-radius: 4px;
      margin-right: 10px;
    }
    .header-name {
      align-self: center;
      color: #1d2939;
      font-weight: 600;
      font-size: 16px;
    }
  }
</style>
