<template>
  <a-card :bordered="false">
    <a-row :gutter="8">
      <a-col :span="6">
        <!-- 加上 show-line 属性后，展开收起图标自动变成 +- 样式 -->
        <a-tree
          class="template-5-tree"
          :tree-data="treeData"
          show-icon
          show-line
          :expandedKeys="treeExpandedKeys"
          :selectedKeys="[pagination.current]"
          @expand="handleTreeExpand"
          @select="handleTreeSelect"
        >
          <!-- 自定义子节点图标 -->
          <a-icon slot="myIcon" type="unordered-list" style="color: #0c8fcf" />
        </a-tree>
      </a-col>
      <a-col :span="18">
        <JVxeTable
          rowNumber
          rowSelection
          :height="750"
          :loading="loading"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="pagination"
          @pageChange="handleTablePageChange"
        />
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
  import { defHttp } from '/@/utils/http/axios';
  import { JVxeTypes } from '/@/components/jeecg/JVxeTable/types';

  // 【多种布局模板】左侧为树，右侧为行编辑
  export default {
    name: 'Template5',
    data() {
      return {
        // 是否正在加载
        loading: false,
        // 分页器参数
        pagination: {
          // 当前页码
          current: 1,
          // 每页的条数
          pageSize: 50,
          // 可切换的条数
          pageSizeOptions: ['50'],
          // 数据总数（目前并不知道真实的总数，所以先填写0，在后台查出来后再赋值）
          total: 0,
        },
        // 选择的行
        selectedRows: [],
        // 数据源，控制表格的数据
        dataSource: [],
        // 列配置，控制表格显示的列
        columns: [
          { key: 'num', title: '序号', width: '80px' },
          {
            // 字段key，跟后台数据的字段名匹配
            key: 'ship_name',
            // 列的标题
            title: '船名',
            // 列的宽度
            width: '180px',
            // 如果加上了该属性，就代表当前单元格是可编辑的，type就是表单的类型，input就是简单的输入框
            type: JVxeTypes.input,
          },
          { key: 'call', title: '呼叫', width: '80px', type: JVxeTypes.input },
          { key: 'len', title: '长', width: '80px', type: JVxeTypes.input },
          { key: 'ton', title: '吨', width: '120px', type: JVxeTypes.input },
          { key: 'payer', title: '付款方', width: '120px', type: JVxeTypes.input },
          { key: 'count', title: '数', width: '40px' },
          {
            key: 'company',
            title: '公司',
            // 最小宽度，与宽度不同的是，这个不是固定的宽度，如果表格有多余的空间，会平均分配给设置了 minWidth 的列
            // 如果要做占满表格的列可以这么写
            minWidth: '180px',
            type: JVxeTypes.input,
          },
          { key: 'trend', title: '动向', width: '120px', type: JVxeTypes.input },
        ],
        // 树的数据，这里模拟分页固定数据，实际情况应该是后台查出来的数据
        treeData: [
          // 第1级数据
          {
            title: '1-10页',
            key: '1-10',
            // 第2级数据
            children: [
              { title: '第 1 页', key: 1, slots: { icon: 'myIcon' } },
              { title: '第 2 页', key: 2, slots: { icon: 'myIcon' } },
              {
                title: '第 3 页',
                key: 3,
                slots: { icon: 'myIcon' },
                // 第3级数据
                children: [
                  { title: '第 333 页', key: 333, slots: { icon: 'myIcon' } },
                  { title: '第 444 页', key: 444, slots: { icon: 'myIcon' } },
                  { title: '第 555 页', key: 555, slots: { icon: 'myIcon' } },
                  // 第4第5级以此类推，加上 children 属性即可
                ],
              },
              { title: '第 4 页', key: 4, slots: { icon: 'myIcon' } },
              { title: '第 5 页', key: 5, slots: { icon: 'myIcon' } },
              { title: '第 6 页', key: 6, slots: { icon: 'myIcon' } },
              { title: '第 7 页', key: 7, slots: { icon: 'myIcon' } },
              { title: '第 8 页', key: 8, slots: { icon: 'myIcon' } },
              { title: '第 9 页', key: 9, slots: { icon: 'myIcon' } },
              { title: '第 10 页', key: 10, slots: { icon: 'myIcon' } },
            ],
            slots: { icon: 'myIcon' },
          },
          {
            title: '11-20页',
            key: '11-20',
            children: [
              { title: '第 11 页', key: 11, slots: { icon: 'myIcon' } },
              { title: '第 12 页', key: 12, slots: { icon: 'myIcon' } },
              { title: '第 13 页', key: 13, slots: { icon: 'myIcon' } },
              { title: '第 14 页', key: 14, slots: { icon: 'myIcon' } },
              { title: '第 15 页', key: 15, slots: { icon: 'myIcon' } },
              { title: '第 16 页', key: 16, slots: { icon: 'myIcon' } },
              { title: '第 17 页', key: 17, slots: { icon: 'myIcon' } },
              { title: '第 18 页', key: 18, slots: { icon: 'myIcon' } },
              { title: '第 19 页', key: 19, slots: { icon: 'myIcon' } },
              { title: '第 20 页', key: 20, slots: { icon: 'myIcon' } },
            ],
            slots: { icon: 'myIcon' },
          },
        ],
        // 树展开的列，默认 1-10
        treeExpandedKeys: ['1-10'],
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
        },
      };
    },
    created() {
      this.loadData();
    },
    methods: {
      // 加载行编辑的数据
      loadData() {
        // 封装查询条件
        let formData = {
          pageNo: this.pagination.current,
          pageSize: this.pagination.pageSize,
        };
        // 调用查询数据接口
        this.loading = true;
        defHttp
          .get({
            url: this.url.getData,
            params: formData,
          })
          .then((result) => {
            // 后台查询回来的 total，数据总数量
            this.pagination.total = result.total;
            // 将查询的数据赋值给 dataSource
            this.dataSource = result.records;
            // 重置选择
            this.selectedRows = [];
          })
          .finally(() => {
            // 这里是无论成功或失败都会执行的方法，在这里关闭loading
            this.loading = false;
          });
      },

      handleTablePageChange(event) {
        // 重新赋值
        this.pagination.current = event.current;
        this.pagination.pageSize = event.pageSize;
        // 查询数据
        this.loadData();
        // 判断树展开的key
        if (event.current <= 10) {
          this.treeExpandedKeys = ['1-10'];
        } else {
          this.treeExpandedKeys = ['11-20'];
        }
      },

      // 树被选择触发的事件
      handleTreeSelect(selectedKeys) {
        let key = selectedKeys[0];
        if (typeof key === 'string') {
          // 控制树展开为当前选择的列
          this.treeExpandedKeys = selectedKeys;
        } else {
          this.pagination.current = key;
          this.loadData();
        }
      },

      // 树被选择触发的事件
      handleTreeExpand(expandedKeys) {
        this.treeExpandedKeys = expandedKeys;
      },
    },
  };
</script>

<style lang="less">
  /** 隐藏文件小图标 */
  .template-5-tree.ant-tree {
    li span.ant-tree-switcher.ant-tree-switcher-noop {
      display: none;
    }
  }
</style>
