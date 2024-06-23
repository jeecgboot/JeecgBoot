<template>
  <div class="slide-wrap">
    <div class="createArea">
      <a-button type="dashed" @click="handleCreate">新建聊天</a-button>
    </div>
    <div class="historyArea">
      <ul>
        <li
          v-for="item in dataSource.history"
          :key="item.uuid"
          class="list"
          :class="[item.uuid == dataSource.active ? 'active' : 'normal', dataSource.history.length == 1 ? 'last' : '']"
          @click="handleToggleChat(item)"
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
            @blur="inputBlur(item)"
          />
          <span class="title" v-else>{{ item.title }}</span>
          <span class="icon edit" @click="handleEdit(item)" v-if="!item.isEdit">
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
                d="M6.414 15.89L16.556 5.748l-1.414-1.414L5 14.476v1.414zm.829 2H3v-4.243L14.435 2.212a1 1 0 0 1 1.414 0l2.829 2.829a1 1 0 0 1 0 1.414zM3 19.89h18v2H3z"
              ></path>
            </svg>
          </span>
          <span class="icon del" v-if="!item.isEdit">
            <a-popconfirm title="确定删除此记录？" placement="bottom" ok-text="确定" cancel-text="取消" @confirm="handleDel(item)">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                aria-hidden="true"
                role="img"
                class="iconify iconify--ri"
                width="1em"
                height="1em"
                viewBox="0 0 24 24"
                @click.stop=""
              >
                <path
                  fill="currentColor"
                  d="M17 6h5v2h-2v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V8H2V6h5V3a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1zm1 2H6v12h12zm-9 3h2v6H9zm4 0h2v6h-2zM9 4v2h6V4z"
                ></path>
              </svg>
            </a-popconfirm>
          </span>
          <span class="icon save" v-if="item.isEdit" @click="handleSave(item)">
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
                d="M7 19v-6h10v6h2V7.828L16.172 5H5v14zM4 3h13l4 4v13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1m5 12v4h6v-4z"
              ></path>
            </svg>
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  const props = defineProps(['dataSource']);
  const inputRef = ref(null);
  let inputValue = '';
  //新建聊天
  const handleCreate = () => {
    const uuid = getUuid();
    props.dataSource.history.unshift({ title: '新建聊天', uuid, isEdit: false });
    props.dataSource.chat.unshift({ uuid, data: [] });
    // 新建第一个(需要高亮选中)
    if (props.dataSource.history.length == 1) {
      props.dataSource.active = uuid;
    }
  };
  // 切换聊天
  const handleToggleChat = (item) => {
    if (item.uuid != props.dataSource.active) {
      props.dataSource.active = item.uuid;
      const findItem = props.dataSource.history.find((item) => item.isEdit);
      if (findItem) {
        handleSave(findItem);
      }
    }
  };
  const handleInputChange = (e) => {
    inputValue = e.target.value.trim();
  };
  // 失去焦点
  const inputBlur = (item) => {
    item.isEdit = false;
    item.title = inputValue;
  };
  // 编辑
  const handleEdit = (item) => {
    item.isEdit = true;
    inputValue = item.title;
  };
  // 保存
  const handleSave = (item) => {
    item.isEdit = false;
    item.title = inputValue;
  };
  // 删除
  const handleDel = (data) => {
    const findIndex = props.dataSource.history.findIndex((item) => item.uuid == data.uuid);
    if (findIndex != -1) {
      props.dataSource.history.splice(findIndex, 1);
      props.dataSource.chat.splice(findIndex, 1);
      // 删除的是当前active的，active往前移，前面没了往后移。
      if (props.dataSource.history.length) {
        if (props.dataSource.active == data.uuid) {
          if (findIndex > 0) {
            props.dataSource.active = props.dataSource.history[findIndex - 1].uuid;
          } else {
            props.dataSource.active = props.dataSource.history[0].uuid;
          }
        }
      } else {
        //  删没了（删除了最后一个）
        props.dataSource.active = null;
      }
    }
  };
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
    .historyArea ul li:hover{
      .del{
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
</style>
