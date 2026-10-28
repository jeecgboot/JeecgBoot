<template>
  <a-card title="SQL注入拦截验证（GHSA-jgwm / GHSA-xmw4 / GHSA-g87f）" size="small" style="margin-bottom: 16px">
    <a-alert
      message="以下按钮会用漏洞 payload 调用字典相关接口，修复生效时应全部被拦截（success=false）。"
      type="warning"
      show-icon
    />
    <a-space direction="vertical" style="width: 100%; margin-top: 12px">
      <div v-for="(c, i) in cases" :key="i">
        <a-space>
          <a-button danger size="small" :loading="c.loading" @click="runCase(c)">{{ c.title }}</a-button>
          <a-tag v-if="c.status === 'blocked'" color="success">已拦截</a-tag>
          <a-tag v-else-if="c.status === 'leak'" color="error">未拦截，存在风险</a-tag>
        </a-space>
        <div style="margin: 4px 0 0 4px; color: #999; font-size: 12px">{{ c.desc }}</div>
        <div v-if="c.msg" style="margin: 2px 0 0 4px; font-family: monospace; font-size: 12px; color: #d4380d">{{ c.msg }}</div>
      </div>
    </a-space>
  </a-card>

  <a-alert type="info" show-icon style="margin-bottom: 16px">
    <template #message>表字典过滤条件写法（合法用法）</template>
    <template #description>
      <div>1. 下拉 / 搜索：dictCode 写成「表名,文本字段,编码字段,过滤条件」</div>
      <div>2. 下拉树：dict 写成「表名 where 过滤条件,文本字段,编码字段」，过滤条件里不能有逗号（dict 按逗号拆分）</div>
      <div>3. 过滤条件只支持「字段 比较 常量」组合（=、!=、&gt;、&lt;、like、in、between、is null、and/or、order by），不支持注释、反斜杠、子查询和函数</div>
      <div>4. 关联记录按 id 回显、关键词搜索均为参数绑定，不需要也不允许传条件 SQL</div>
    </template>
  </a-alert>
  <BasicForm
    :labelCol="{ span: 6 }"
    :wrapperCol="{ span: 14 }"
    :showResetButton="false"
    :showSubmitButton="false"
    :schemas="schemas"
    :actionColOptions="{ span: 24 }"
  />
</template>
<script lang="ts" setup>
  /**
   * 表字典过滤条件 / 关联记录 示例 + SQL注入拦截验证
   *
   * @author scott
   * @since 2026-10-27 GHSA-jgwm/GHSA-xmw4/GHSA-g87f 字典条件校验加固后补充用法示例与回归验证
   */
  import { reactive } from 'vue';
  import { BasicForm, FormSchema } from '/@/components/Form';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();

  interface InjectCase {
    title: string;
    desc: string;
    request: () => Promise<any>;
    // 参数化类注入(jgwm)不会报错而是查不到数据，需按返回数据量判断是否被当作纯值处理
    leakWhenData?: boolean;
    loading: boolean;
    status: '' | 'blocked' | 'leak';
    msg: string;
  }

  // 注入 payload 仅用于验证拦截，经前端发出时会自动带上 X-Sign 签名
  const cases = reactive<InjectCase[]>([
    {
      title: 'jgwm：关联记录 keyValues 反斜杠闭合引号',
      desc: "/sys/dict/queryTableDataForLinkRecord  keyValues=a\\,) or 1=1#（参数化后应查出0条）",
      leakWhenData: true,
      loading: false,
      status: '',
      msg: '',
      request: () =>
        defHttp.get(
          {
            url: '/sys/dict/queryTableDataForLinkRecord',
            params: { tableName: 'sys_user', showFields: 'realname,username', valueField: 'id', keyValues: 'a\\,) or 1=1#', pageNo: 1, pageSize: -1 },
          },
          { isTransformResponse: false },
        ),
    },
    {
      title: 'xmw4：表字典 # 注释 + password# 字段',
      desc: "/sys/dict/loadDict  条件=username='admin' and password# like '%'",
      loading: false,
      status: '',
      msg: '',
      request: () =>
        defHttp.get(
          { url: "/sys/dict/loadDict/" + encodeURIComponent("sys_user,realname,username,username='admin' and password# like '%'"), params: { pageNo: 1, pageSize: 10 } },
          { isTransformResponse: false },
        ),
    },
    {
      title: 'xmw4：下拉树表名注入子查询',
      desc: '/sys/dict/loadTreeData  tableName=sys_permission where 1=1 or (select 1 from sys_user)>0#',
      loading: false,
      status: '',
      msg: '',
      request: () =>
        defHttp.get(
          {
            url: '/sys/dict/loadTreeData',
            params: { pid: '', condition: '', pidField: 'parent_id', hasChildField: 'is_leaf', converIsLeafVal: 0, text: 'name', code: 'id', tableName: 'sys_permission where 1=1 or (select 1 from sys_user)>0#' },
          },
          { isTransformResponse: false },
        ),
    },
    {
      title: 'g87f：queryTableData 字面量 null 旁路',
      desc: '/sys/dict/queryTableData  table=null&text=sys_user&code=password',
      loading: false,
      status: '',
      msg: '',
      request: () =>
        defHttp.get(
          { url: '/sys/dict/queryTableData', params: { table: 'null', text: 'sys_user', code: 'password', keyword: '%', pageSize: 10 } },
          { isTransformResponse: false },
        ),
    },
    {
      title: '敏感字段直取 sys_user.password',
      desc: '/sys/dict/loadDict/sys_user,password,id',
      loading: false,
      status: '',
      msg: '',
      request: () => defHttp.get({ url: '/sys/dict/loadDict/sys_user,password,id', params: { pageNo: 1, pageSize: 10 } }, { isTransformResponse: false }),
    },
  ]);

  async function runCase(c: InjectCase) {
    c.loading = true;
    c.status = '';
    c.msg = '';
    try {
      const res = await c.request();
      if (c.leakWhenData) {
        // 参数化类：payload 被当纯值处理时查不到数据(total=0)为安全；若 or 1=1 生效则查出数据
        const r = res?.result || {};
        const total = r.total ?? (r.records ? r.records.length : 0);
        if (res?.success && total === 0) {
          c.status = 'blocked';
          c.msg = '注入字符串被当作参数值，查出 0 条，注入未生效';
          createMessage.success('已防护');
        } else {
          c.status = 'leak';
          c.msg = `注入生效，查出 ${total} 条数据！`;
          createMessage.error(c.title + ' 未拦截');
        }
      } else if (res?.success) {
        c.status = 'leak';
        c.msg = '响应 success=true，注入未被拦截！';
        createMessage.error(c.title + ' 未拦截');
      } else {
        c.status = 'blocked';
        c.msg = res?.message || '后端已拒绝';
        createMessage.success('已拦截');
      }
    } catch (e: any) {
      c.status = 'blocked';
      c.msg = e?.message || e?.response?.data?.message || '请求被拒绝';
      createMessage.success('已拦截');
    } finally {
      c.loading = false;
    }
  }

  const schemas: FormSchema[] = [
    {
      field: 'condDictSelect',
      component: 'JDictSelectTag',
      label: '字典表下拉(带条件)',
      helpMessage: ["sys_user,realname,username,status = '1' and del_flag = 0"],
      componentProps: {
        dictCode: "sys_user,realname,username,status = '1' and del_flag = 0",
      },
      colProps: { span: 12 },
    },
    {
      field: 'condDictSelect',
      component: 'JEllipsis',
      label: '选中值',
      colProps: { span: 12 },
    },
    {
      field: 'condDictOrder',
      component: 'JDictSelectTag',
      label: '字典表下拉(条件+排序)',
      helpMessage: ['sys_user,realname,username,del_flag = 0 order by create_time desc'],
      componentProps: {
        dictCode: 'sys_user,realname,username,del_flag = 0 order by create_time desc',
      },
      colProps: { span: 12 },
    },
    {
      field: 'condDictOrder',
      component: 'JEllipsis',
      label: '选中值',
      colProps: { span: 12 },
    },
    {
      field: 'condSearchSelect',
      component: 'JSearchSelect',
      label: '字典搜索(异步带条件)',
      helpMessage: ['输入关键字后，在过滤条件范围内分页搜索'],
      componentProps: {
        dict: "sys_user,realname,username,status = '1' and del_flag = 0",
        pageSize: 6,
        async: true,
      },
      colProps: { span: 12 },
    },
    {
      field: 'condSearchSelect',
      component: 'JEllipsis',
      label: '选择值',
      colProps: { span: 12 },
    },
    {
      field: 'condTreeSelect',
      component: 'JTreeSelect',
      label: '下拉树(表名带条件)',
      helpMessage: ['sys_permission where menu_type != 2,name,id', '只显示菜单，不显示按钮'],
      componentProps: {
        dict: 'sys_permission where menu_type != 2,name,id',
        pidField: 'parent_id',
        hasChildField: 'is_leaf',
        converIsLeafVal: 0,
      },
      colProps: { span: 12 },
    },
    {
      field: 'condTreeSelect',
      component: 'JEllipsis',
      label: '选中值',
      colProps: { span: 12 },
    },
    {
      field: 'linkRecord',
      component: 'JLinkTableCard',
      label: '关联记录(按id回显)',
      helpMessage: ['调用 /sys/dict/queryTableDataForLinkRecord', '默认值为 admin 的 id，打开页面即按 keyValues 回显', '弹窗内关键词搜索走 keyword 参数'],
      defaultValue: 'e9ca23d68d884d4ebb19d07889727dae',
      componentProps: {
        queryMode: 'table',
        tableName: 'sys_user',
        valueField: 'id',
        textField: 'realname,username,sex',
        imageField: 'avatar',
        multi: true,
      },
      colProps: { span: 12 },
    },
    {
      field: 'linkRecord',
      component: 'JEllipsis',
      label: '选中值(id逗号分隔)',
      colProps: { span: 12 },
    },
  ];
</script>
