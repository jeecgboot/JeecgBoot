<template>
  <a-modal
    :title="title"
    :width="280"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :bodyStyle ="bodyStyle"
    :mask = "false"
    destroyOnClose
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <div style="height: 300px;overflow: hidden;overflow-y: auto;overflow-x: auto;">
        <template v-for="(item, key, index) in nodeInfos">
          <table class="gridtable">
            <tbody>
            <tr>
              <th width="90">任务名称</th>
              <td width="150">{{ item.taskName}}</td>
            </tr>
            <tr>
              <th width="90">执行人</th>
              <td width="150">{{ item.taskAssigneeId}}</td>
            </tr>
            <tr>
              <th width="90">开始时间</th>
              <td width="150">{{ item.taskBeginTime }}</td>
            </tr>
            <tr>
              <th width="90">结束时间</th>
              <td width="150">{{ item.taskEndTime }}</td>
            </tr>
            <tr>
              <th width="90">耗时</th>
              <td width="150">{{ item.durationStr }}</td>
            </tr>
            <tr>
              <th width="90">意见</th>
              <td width="150">{{ item.remarks }}</td>
            </tr>
            </tbody>
          </table>
        </template>
      </div>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "ProcNodeInfoModel",
    data () {
      return {
        title:"任务审批详情",
        visible: false,
        bodyStyle:{
          padding: "0",
        },
        confirmLoading: false,
        validatorRules:{
        },
        nodeInfos:[],
      }
    },
    created () {
    },
    methods: {
      showInfo(record,taskId) {
        this.nodeInfos = [];
        for (var item of record) {
          if(item.taskId == taskId){
            this.nodeInfos.push(item);
          }
        }
        this.visible = true;
      },
      close() {
        this.nodeInfos = [];
        this.visible = false;
      },
      handleCancel () {
        this.nodeInfos = [];
        this.visible = false;
      },

    }
  }
</script>

<style scoped>
  table.gridtable {
    margin: 0 auto;
    margin-top: 10px;
    font-family: verdana,arial,sans-serif;
    font-size:12px;
    color:#333333;
    border-width: 1px;
    border-color: #ddd;
    border-collapse: collapse;
  }
  table.gridtable th {
    border-width: 1px;
    padding: 8px;
    border-style: solid;
    border-color: #ddd;
    background-color: #eee;
  }
  table.gridtable td {
    border-width: 1px;
    padding: 8px;
    border-style: solid;
    border-color: #ddd;
    background-color: #ffffff;
  }
</style>