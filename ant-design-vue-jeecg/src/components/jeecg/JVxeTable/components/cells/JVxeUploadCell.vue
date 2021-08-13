<template>
  <div>
    <template v-if="hasFile" v-for="(file, fileKey) of [innerFile || {}]">
      <a-input
        :key="fileKey"
        :readOnly="true"
        :value="file.name"
      >

        <template slot="addonBefore" style="width: 30px">
          <a-tooltip v-if="file.status === 'uploading'" :title="`上传中(${Math.floor(file.percent)}%)`">
            <a-icon type="loading"/>
          </a-tooltip>
          <a-tooltip v-else-if="file.status === 'done'" title="上传完成">
            <a-icon type="check-circle" style="color:#00DB00;"/>
          </a-tooltip>
          <a-tooltip v-else :title="file.message||'上传失败'">
            <a-icon type="exclamation-circle" style="color:red;"/>
          </a-tooltip>
        </template>

        <span v-if="file.status === 'uploading'" slot="addonAfter">{{ Math.floor(file.percent) }}%</span>
        <template v-else-if="originColumn.allowDownload !== false || originColumn.allowRemove !== false" slot="addonAfter">
          <a-dropdown :trigger="['click']" placement="bottomRight">
            <a-tooltip title="操作">
              <a-icon
                type="setting"
                style="cursor: pointer;"/>
            </a-tooltip>

            <a-menu slot="overlay">
              <!-- <a-menu-item @click="handleClickPreviewFile">-->
              <!--  <span><a-icon type="eye"/>&nbsp;预览</span>-->
              <!-- </a-menu-item>-->
              <a-menu-item v-if="originColumn.allowDownload !== false" @click="handleClickDownloadFile">
                <span><a-icon type="download"/>&nbsp;下载</span>
              </a-menu-item>
              <a-menu-item v-if="originColumn.allowRemove !== false" @click="handleClickDeleteFile">
                <span><a-icon type="delete"/>&nbsp;删除</span>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </template>

      </a-input>
    </template>
    <a-upload
      v-show="!hasFile"
      name="file"
      :data="{'isup': 1}"
      :multiple="false"
      :action="originColumn.action"
      :headers="uploadHeaders"
      :showUploadList="false"
      v-bind="cellProps"
      @change="handleChangeUpload"
    >
      <a-button icon="upload">{{originColumn.btnText || '点击上传'}}</a-button>
    </a-upload>
  </div>
</template>

<script>
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import { getFileAccessHttpUrl } from '@api/manage'

  export default {
    name: 'JVxeUploadCell',
    mixins: [JVxeCellMixins],
    props: {},
    data() {
      return {
        innerFile: null,
      }
    },
    computed: {
      /** upload headers */
      uploadHeaders() {
        let {originColumn: col} = this
        let headers = {}
        if (col.token === true) {
          headers['X-Access-Token'] = this.$ls.get(ACCESS_TOKEN)
        }
        return headers
      },

      hasFile() {
        return this.innerFile != null
      },

    },
    watch: {
      innerValue: {
        immediate: true,
        handler() {
          if (this.innerValue) {
            this.innerFile = this.innerValue
          } else {
            this.innerFile = null
          }
        },
      },
    },
    methods: {

      handleChangeUpload(info) {
        let {row, originColumn: col} = this
        let {file} = info
        let value = {
          name: file.name,
          type: file.type,
          size: file.size,
          status: file.status,
          percent: file.percent
        }
        if (col.responseName && file.response) {
          value['responseName'] = file.response[col.responseName]
        }
        if (file.status === 'done') {
          if (typeof file.response.success === 'boolean') {
            if (file.response.success) {
              value['path'] = file.response[col.responseName]
            } else {
              value['status'] = 'error'
              value['message'] = file.response.message || '未知错误'
            }
          } else {
            // 考虑到如果设置action上传路径为非jeecg-boot后台，可能不会返回 success 属性的情况，就默认为成功
            value['path'] = file.response[col.responseName]
          }
        } else if (file.status === 'error') {
          value['message'] = file.response.message || '未知错误'
        }
        this.innerFile = value
      },

      // handleClickPreviewFile(id) {
      //   this.$message.info('尚未实现')
      // },

      handleClickDownloadFile(id) {
        let {path} = this.value || {}
        if (path) {
          let url = getFileAccessHttpUrl(path)
          window.open(url)
        }
      },

      handleClickDeleteFile() {
        this.handleChangeCommon(null)
      },

    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      switches: {visible: true},
      getValue: value => fileGetValue(value),
      setValue: value => fileSetValue(value),
    }
  }

  function fileGetValue(value) {
    if (value && value.path) {
      return value.path
    }
    return value
  }

  function fileSetValue(value) {
    if (value) {
      let first = value.split(',')[0]
      let name = first.substring(first.lastIndexOf('/') + 1)
      return {
        name: name,
        path: value,
        status: 'done',
      }
    }
    return value
  }

</script>

<style scoped>

</style>