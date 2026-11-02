<template>
  <JVxeTable
    ref="tableRef"
    row-class-name="online-config-page"
    rowNumber
    keyboardEdit
    :maxHeight="tableHeight.noToolbar"
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :disabledRows="{ dbFieldName: ['id', 'has_child'] }"
    @valueChange="handleValueChange"
    v-bind="tableProps" >
    <!-- 定义插槽开始了 -->
    <template #dbFieldNameSlot="props">
      <span class="field-name-cell">
        <span>{{ props.value }}</span>
        <a-tooltip v-if="isTitleFieldRow(props.row)" title="已设置为标题字段">
          <a-tag color="blue" class="title-field-tag">T</a-tag>
        </a-tooltip>
      </span>
    </template>
    <template #fieldConfig="props">
      <a-space v-if="showConfigButton(props)">
        <a-button type="primary" size="small" ghost @click="openConfig(props)">配置</a-button>
        <a-button size="small" @click="openJsonConfig(props)">JSON</a-button>
      </a-space>
    </template>
    <!--update-begin-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列-->
    <template #dictConfig="props">
      <div class="dict-config-cell">
        <a-input
          v-if="!props.row.dictTable"
          class="dict-config-input"
          :value="props.value"
          :placeholder="getDictInputPlaceholder(props.row)"
          allowClear
          @change="(e) => props.triggerChange(e.target.value)"
        />
        <a-tooltip v-else :title="getDictSummary(props.row)" placement="topLeft">
          <div class="dict-config-summary" @click="openDictConfig(props)">
            {{ getDictSummary(props.row) }}
          </div>
        </a-tooltip>
        <a-tooltip :title="props.row.dictTable ? '编辑字典配置' : '高级配置（表字典/Popup/自定义树/联动）'">
          <a-button class="dict-config-btn" size="small" type="text" @click="openDictConfig(props)">
            <Icon :icon="props.row.dictTable ? 'ant-design:edit-outlined' : 'mdi:book-cog-outline'" />
          </a-button>
        </a-tooltip>
      </div>
    </template>
    <!--update-end-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列-->
  </JVxeTable>
  
  <!-- 关联记录弹窗配置 -->
  <link-table-config-modal @register="registerModal" @success="handleConfigData"/>
  
  <!-- 他表字段弹窗配置 -->
  <link-table-field-config-modal :isSubTableOneToMany="isSubTableOneToMany" @register="registerFieldModal" @success="handleFieldConfigData"/>

  <!-- 扩展参数弹窗配置 -->
  <FieldExtendJsonModal @register="registerExtJsonModal" @success="handleExtJson"/>

  <!-- 扩展参数JSON直接编辑弹窗 -->
  <a-modal v-model:open="jsonModalVisible" title="扩展参数" okText="确认" cancelText="取消" :width="600" @ok="handleJsonSubmit">
    <div class="p-4">
      <a-textarea v-model:value="currentJsonStr" :rows="12" placeholder="请输入扩展参数JSON" />
    </div>
  </a-modal>
  <!-- 字典配置弹窗 -->
  <DictConfigModal @register="registerDictConfigModal" @success="handleDictConfig" />
</template>

<script lang="ts">
  import { ref, defineComponent } from 'vue';
  import { JVxeTypes, JVxeColumn } from '/@/components/jeecg/JVxeTable/types';
  import { useTableSync } from '../../hooks/useTableSync';
  import LinkTableConfigModal from '../../extend/linkTable/LinkTableConfigModal.vue'
  import LinkTableFieldConfigModal from '../../extend/linkTable/LinkTableFieldConfigModal.vue'
  import FieldExtendJsonModal from '../../extend/FieldExtendJsonModal.vue'
  import DictConfigModal from '../../extend/DictConfigModal.vue'
  import { Icon } from '/@/components/Icon';
  import { useModal } from '/@/components/Modal';

  const commonPageOptions = [
    { title: '文本框', value: 'text', group: '基础输入' },
    { title: '下拉框', value: 'list', group: '选择类' },
    { title: '下拉多选框', value: 'list_multi', group: '选择类' },
    { title: '下拉搜索框', value: 'sel_search', group: '选择类' },
    { title: '单选框', value: 'radio', group: '选择类' },
    { title: '多选框', value: 'checkbox', group: '选择类' },
    { title: '开关', value: 'switch', group: '选择类' },
    { title: '日期(年月日)', value: 'date', group: '日期时间' },
    { title: '日期(年月日时分秒)', value: 'datetime', group: '日期时间' },
    { title: '时间(时分秒)', value: 'time', group: '日期时间' },
    { title: '文件', value: 'file', group: '文件/图片' },
    { title: '图片', value: 'image', group: '文件/图片' },
    { title: 'Popup弹框', value: 'popup', group: '弹框/Popup' },
    // update-begin--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
    { title: 'Popup字典', value: 'popup_dict', group: '弹框/Popup' },
    // update-end--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
    { title: '分类字典树', value: 'cat_tree', group: '树形/联动/地区' },
    { title: '自定义树控件', value: 'sel_tree', group: '树形/联动/地区' },
    { title: '省市区组件', value: 'pca', group: '树形/联动/地区' },
    { title: '联动组件', value: 'link_down', group: '树形/联动/地区' },
    { title: '用户选择', value: 'sel_user', group: '人员/部门' },
    { title: '部门选择', value: 'sel_depart', group: '人员/部门' },
    { title: '关联记录', value: 'link_table', group: '关联记录' },
    { title: '他表字段', value: 'link_table_field', group: '关联记录' },
    { title: '密码', value: 'password', group: '不常用' },
    { title: '多行文本', value: 'textarea', group: '不常用' },
    { title: '富文本', value: 'umeditor', group: '不常用' },
    { title: 'MarkDown', value: 'markdown', group: '不常用' },
  ];
  const subTablePageOptions = [
    { title: '文本框', value: 'text', group: '基础输入' },
    { title: '下拉框', value: 'list', group: '选择类' },
    { title: '下拉多选框', value: 'list_multi', group: '选择类' },
    { title: '下拉搜索框', value: 'sel_search', group: '选择类' },
    { title: '单选框', value: 'radio', group: '选择类' },
    { title: '开关', value: 'switch', group: '选择类' },
    { title: '日期(年月日)', value: 'date', group: '日期时间' },
    { title: '日期(年月日时分秒)', value: 'datetime', group: '日期时间' },
    { title: '时间(时分秒)', value: 'time', group: '日期时间' },
    { title: '文件', value: 'file', group: '文件/图片' },
    { title: '图片', value: 'image', group: '文件/图片' },
    { title: 'Popup弹框', value: 'popup', group: '弹框/Popup' },
    // update-begin--author:liaozhiyang---date:20260507---for：【issues/7617】vxetable子表支持popup字典
    { title: 'Popup字典', value: 'popup_dict', group: '弹框/Popup' },
    // update-end--author:liaozhiyang---date:20260507---for：【issues/7617】vxetable子表支持popup字典
    { title: '分类字典树', value: 'cat_tree', group: '树形/地区' },
    { title: '自定义树控件', value: 'sel_tree', group: '树形/地区' },
    { title: '省市区组件', value: 'pca', group: '树形/地区' },
    { title: '用户选择', value: 'sel_user', group: '人员/部门' },
    { title: '部门选择', value: 'sel_depart', group: '人员/部门' },
    { title: '关联记录', value: 'link_table', group: '关联记录' },
    { title: '他表字段', value: 'link_table_field', group: '关联记录' },
    { title: '多行文本', value: 'textarea', group: '不常用' },
  ];

  export default defineComponent({
    name: 'PageAttributeTable',
    components: {
      LinkTableConfigModal,
      LinkTableFieldConfigModal,
      FieldExtendJsonModal,
      DictConfigModal,
      Icon,
    },
    setup() {
      const columns = ref<JVxeColumn[]>([
        { title: '字段名称', key: 'dbFieldName', width: 120, fixed: 'left', type: JVxeTypes.slot, slotName: 'dbFieldNameSlot' },
        { title: '字段备注', key: 'dbFieldTxt', width: 120, fixed: 'left' },
        {
          title: '表单显示',
          key: 'isShowForm',
          width: 68,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: true,
        },
        {
          title: '列显示',
          key: 'isShowList',
          width: 55,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: true,
        },
        {
          title: '排序',
          key: 'sortFlag',
          width: 60,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: false,
          // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
          props: {
            isDisabledCell({ row, column }) {
              let { dbTable } = tables;
              // 获取到 dbTable（数据库属性） 中的数据
              const dbRowData = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              return dbRowData?.dbIsPersist == '0' ? true : false;
            },
          },
          // update-end--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
        },
        {
          title: '只读',
          key: 'isReadOnly',
          width: 60,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '控件类型',
          key: 'fieldShowType',
          width: 170,
          type: JVxeTypes.select,
          options: commonPageOptions,
          allowSearch: true,
          defaultValue: 'text',
          placeholder: '请选择${title}',
          validateRules: [{ required: true, message: '请选择${title}' }, { handler: validateFieldShowType }],
        },
        {
          title: '控件默认值',
          key: 'fieldDefaultValue',
          width: 140,
          type: JVxeTypes.textarea,
          props: {
            allowClear: true,
          },
          defaultValue: '',
        },
        {
          title: '长度',
          key: 'fieldLength',
          width: 90,
          titleHelp: { message: '此控件长度只对子表列字段宽度有效！'},
          type: JVxeTypes.inputNumber,
          defaultValue: 120,
          placeholder: '请输入控件长度',
        },
        {
          title: '查询',
          key: 'isQuery',
          width: 60,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: false,
          // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-423】未勾选同步数据库禁用查询
          props: {
            isDisabledCell({ row, column }) {
              let { dbTable } = tables;
              // 获取到 dbTable（数据库属性） 中的数据
              const dbRowData = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
              if (dbRowData?.dbType === 'Blob') {
                return true;
              }
              // update-end--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
              // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-489】他表字段禁用查询
              if (row.fieldShowType === 'link_table_field') {
                return true;
              }
              // update-end--author:liaozhiyang---date:20260714---for：【LHZP-489】他表字段禁用查询
              return dbRowData?.dbIsPersist == '0' ? true : false;
            },
          },
          // update-end--author:liaozhiyang---date:20240528---for：【TV360X-423】未勾选同步数据库禁用查询
        },
        {
          title: '查询类型',
          key: 'queryMode',
          width: 120,
          type: JVxeTypes.select,
          options: [
            { title: '普通查询', value: 'single' },
            { title: '模糊查询', value: 'like' },
            { title: '范围查询', value: 'group' },
          ],
          defaultValue: 'single',
          placeholder: '请选择${title}',
          validateRules: [{ handler: validateQueryMode }],
          props: {
            isDisabledCell({ row, column }) {
              // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-222】范围查询只支持数字和日期类型
              // let { dbTable } = tables;
              // // 获取到 dbTable（数据库属性） 中的数据
              // const dbRowData = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              // if (['double', 'int', 'BigDecimal', 'Date', 'Datetime'].includes(dbRowData?.dbType)) {
              //   return false;
              // }
              // return true;
              // update-end--author:liaozhiyang---date:20240717---for：【TV360X-222】范围查询只支持数字和日期类型
            },
          },
        },
        // update-begin-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列
        {
          title: '字典编码',
          key: 'dictField',
          width: 210,
          type: JVxeTypes.slot,
          slotName: 'dictConfig',
          allowInput: true,
          defaultValue: '',
          validateRules: [{ handler: validateDictFieldOrDictText }],
        },
        // 隐藏列：dictTable 与 dictText 仍作为底层数据存储，通过弹窗或同步逻辑写入
        {
          title: '字典Table',
          key: 'dictTable',
          type: JVxeTypes.hidden,
          defaultValue: '',
        },
        {
          title: '字典Text',
          key: 'dictText',
          type: JVxeTypes.hidden,
          defaultValue: '',
        },
        // update-end-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列
        {
          title: '扩展参数',
          key: 'fieldExtendJson',
          type: JVxeTypes.hidden,
          defaultValue: '',
        },
        {
          title: '更多配置',
          minWidth: 150,
          key: 'fieldConfig',
          type: JVxeTypes.slot,
          slotName: 'fieldConfig',
        },
      ]);
      const setup = useTableSync(columns);
      const { tableRef, tables } = setup;

      // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（新增页面属性行时设置默认查询类型）
      /** 同步数据库字段时，仅为新增的 String 文本框设置模糊查询默认值。 */
      async function syncTable(dbTable) {
        const existingIds = new Set(tableRef.value!.getTableData().map((row) => row.id));
        await setup.syncTable(dbTable);
        const values = tableRef.value!
          .getTableData()
          .filter((row) => !existingIds.has(row.id) && row.dbType === 'string' && row.fieldShowType === 'text')
          .map((row) => ({ rowKey: row.id, values: { queryMode: 'like' } }));
        if (values.length > 0) {
          tableRef.value!.setValues(values);
        }
      }
      // update-end--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（新增页面属性行时设置默认查询类型）

      function validateFieldShowType({ cellValue, row }, callback) {
        let { dbTable } = tables;
        // 获取到 dbTable（数据库属性） 中的数据
        let dbType = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0].dbType;
        //当控件类型为时间时，数据库类型必须是String类型
        if (cellValue === 'time' && dbType !== 'string') {
          callback(false, '当控件类型为时间时,数据库属性里的字段类型必须是String！');
        }
        // 当控件类型为日期时，数据库类型必须是Date类型
        else if (cellValue === 'date' && dbType !== 'Date' && dbType !== 'Datetime') {
          callback(false, '当控件类型为日期时，数据库属性里的字段类型必须是Date或Datetime！');
        }
        // 当控件类型为 yyyy-MM-dd HH:mm:ss 时，数据库类型必须是Datetime类型
        else if (cellValue === 'datetime' && dbType !== 'Datetime') {
          callback(false, '当控件类型为datetime时，数据库属性里的字段类型必须是Datetime！');
        } else {
          callback(true);
        }
      }

      /** 同步控件类型 */
      function syncFieldShowType(row) {
        // update-begin--author:liaozhiyang---date:20240801---for：【TV360X-1963】当字段类型旧值是时间类型时再次选择只要不是时间类型，控件类型就重置到文本框，都重置成输入框
        let showType = 'text';
        if (row.dbType === 'Datetime') {
          showType = 'datetime';
        } else if (row.dbType === 'Date') {
          showType = 'date';
        }
        // update-end--author:liaozhiyang---date:20240801---for：【TV360X-1963】当字段类型旧值是时间类型时再次选择只要不是时间类型，控件类型就重置到文本框，都重置成输入框
        // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
        // Blob类型不支持查询，去掉查询和排序勾选
        const values: Recordable = { fieldShowType: showType };
        if (row.dbType === 'Blob') {
          values.isQuery = '0';
          values.sortFlag = '0';
        }
        tableRef.value!.setValues([
          {
            rowKey: row.id,
            values,
          },
        ]);
        // update-end--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
      }
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选
      function syncIsQuery(row) {
        if (row.dbIsPersist === '0') {
          tableRef.value!.setValues([
            {
              rowKey: row.id,
              values: { isQuery: '0', sortFlag: '0' },
            },
          ]);
        }
      }
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选

      const isSubTableOneToMany = ref(false);
      const isSubTable = ref(false);
      function changePageType(flag, relationType = 0) {
        isSubTableOneToMany.value = flag && relationType == 0;
        isSubTable.value = !!flag;
        for (let col of columns.value) {
          if (col.key == 'fieldShowType') {
            // update-begin--author:liaozhiyang---date:20260715---for：【LHZP-580】一对一子表控件类型支持同主表控件类型
            col.options = !flag ? commonPageOptions : relationType == 1 ? commonPageOptions : subTablePageOptions;
            // update-end--author:liaozhiyang---date:20260715---for：【LHZP-580】一对一子表控件类型支持同主表控件类型
            break;
          }
        }
      }

      function enableQuery(id) {
        tableRef.value!.setValues([
          {
            rowKey: id,
            values: { isQuery: '1' },
          },
        ]);
      }

      //update-begin-author:taoyan date:2022-8-11 for: VUEN-1940【online】关联记录和他表字段，配置智能化
      //注册modal
      const [registerModal, { openModal }] = useModal();
      const [registerFieldModal, { openModal: openFieldModal }] = useModal();
      const [registerExtJsonModal, { openModal: openExtJsonModal }] = useModal();
      const [registerDictConfigModal, { openModal: openDictConfigModal }] = useModal();
      //是否展示个性化配置按钮
      function showConfigButton(props){
        if(props.row.dbFieldName=='id'){
          return false
        }else{
          return true;
        }
      }

      /**
       * 根据不同类型打开不同配置
       */
      function openConfig(props){
        if(props.row.fieldShowType.indexOf('link_table')>=0){
          showFieldConfig(props);
        }else{
          showExtJsonConfig(props);
        }
      }
      
      //点击按钮触发弹窗
      function showFieldConfig(props){
        let { row } = props;
        // update-begin--author:liaozhiyang---date:20260413---for:【QQYUN-9776】字典放在页面属性tab中
        if(props.row.fieldShowType == 'link_table'){
          let record = getLinkTableModalData(row);
          openModal(true, {
            record,
            fieldName: row.dbFieldName,
            isSubTableOneToMany: isSubTableOneToMany.value,
          })
        }else if(props.row.fieldShowType == 'link_table_field'){
          let data = getLinkTableFieldData(row);
          openFieldModal(true, data)
        }
        // update-end--author:liaozhiyang---date:20260413---for:【QQYUN-9776】字典放在页面属性tab中
      }
      
      // 获取他表字段的配置信息
      function getLinkTableFieldData(row){
        let tableData = tableRef.value!.getTableData();
        let pageTableData = tableData.filter(item=>item.fieldShowType=='link_table');
        // 获取关联记录的表名和相关字段信息
        let tableAndFieldsMap = {}
        if(pageTableData && pageTableData.length>0){
          for(let pageTableItem of pageTableData){
            tableAndFieldsMap[pageTableItem.dbFieldName] = {
              title: pageTableItem.dbFieldTxt,
              table: pageTableItem.dictTable,
              fields: pageTableItem.dictText
            }
          }
        }
        const { dictTable, dictText, id, dbFieldTxt} = row;
        let record = {
          rowKey: id,
          dbFieldTxt,
          dictText,
          dictTable,
        };
        return {
          record,
          tableAndFieldsMap
        }
      }
      
      //获取关联记录的配置数据
      function getLinkTableModalData(row){
        const { id, dbFieldTxt, fieldExtendJson, dictTable, dictText} = row;
        //dbFieldTxt  dictTable titleField otherFields multiSelect showType
        let result = {
          rowKey: id,
          dbFieldTxt,
          dictTable
        };
        if(dictText){
          let arr = dictText.split(',');
          result['titleField'] = arr[0];
          if(arr.length>1){
            result['otherFields'] = dictText.substring(dictText.indexOf(',')+1)
          }
        }else{
          result['titleField'] = ''
          result['otherFields'] = ''
        }
        if(fieldExtendJson){
          try{
            let json = JSON.parse(fieldExtendJson);
            if(json.multiSelect){
              result['multiSelect'] = json.multiSelect
            }else{
              result['multiSelect'] = false
            }
            // update-begin--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
            if (json.isListReadOnly) {
              result['isListReadOnly'] = json.isListReadOnly;
            } else {
              result['isListReadOnly'] = false;
            }
            // update-end--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
            if(json.showType){
              result['showType'] = json.showType
            }else{
              result['showType'] = 'card'
            }
            if(json.imageField){
              result['imageField'] = json.imageField
            }else{
              result['imageField'] = ''
            }
          }catch (e) {
            console.error('解析扩展参数出错', fieldExtendJson)
          }
        }
        return result;
      }
      
      // 配置完成将数据填回vxe table
      function handleConfigData(record){
        //dbFieldTxt  dictTable titleField otherFields multiSelect showType
        //1.设置扩展配置和字典信息
        const { multiSelect, showType, imageField, isListReadOnly } = record;
        let extJson = {showType, multiSelect, imageField, isListReadOnly};
        // update-begin--author:liaozhiyang---date:20260413---for:【QQYUN-9776】字典放在页面属性tab中
        let dictText = record.titleField;
        if(record.otherFields){
          dictText+=','+record.otherFields;
        }
        const {dictTable, dictField} = record;
        let values = [{ rowKey: record.rowKey, values: { fieldExtendJson: JSON.stringify(extJson), dbFieldTxt: record.dbFieldTxt, dictTable, dictField, dictText } }];
        // update-end--author:liaozhiyang---date:20260413---for:【QQYUN-9776】字典放在页面属性tab中
        tableRef.value!.setValues(values);
        
        let { dbTable } = tables;
        //2.设置字段描述
        if(dbTable){
          let dbTableValues = [{ rowKey: record.rowKey, values: { dbFieldTxt: record.dbFieldTxt } }];
          dbTable.value!.tableRef!.setValues(dbTableValues)
        }
      }

      // 配置完成将数据填回vxe table
      function handleFieldConfigData(record){
        const {dbFieldTxt, dictTable, dictText, rowKey } = record;
        //1.设置字段描述和字典信息
        let values = [{ rowKey, values: { dbFieldTxt, dictTable, dictText } }];
        tableRef.value!.setValues(values);

        let { dbTable } = tables;
        //2.设置字段描述，取消同步数据库勾选状态
        if(dbTable){
          let tableValues = [{ rowKey, values: { dbFieldTxt, dbIsPersist:'0' } }];
          dbTable.value!.tableRef!.setValues(tableValues)
        }
      }

      /**
       * 扩展参数的配置弹窗
       * @param props
       */
      function showExtJsonConfig(props) {
        let jsonStr = props.row.fieldExtendJson || '';
        let id = props.rowId;
        let fieldShowType = props.row.fieldShowType || '';
        let sortFlag = props.row.sortFlag || '0';
        const dbType = props.row.dbType;
        openExtJsonModal(true, {
          jsonStr,
          fieldShowType,
          sortFlag,
          id,
          dbType,
          isSubTable: isSubTable.value,
        });
      }

      /**
       * 扩展参数的 弹窗回调
       * @param props
       */
      function handleExtJson(data, rowKey){
        let values;
        if(data && Object.keys(data).length>0){
          //update-begin---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
          // 若设置了标题字段，清除其他行的 isTitleField
          if (data.isTitleField === 1) {
            const allRows = tableRef.value!.getTableData();
            const clearValues = allRows
              .filter((row) => row.id !== rowKey && row.fieldExtendJson)
              .reduce((acc: any[], row) => {
                try {
                  const json = JSON.parse(row.fieldExtendJson);
                  if (json.isTitleField === 1) {
                    delete json.isTitleField;
                    acc.push({ rowKey: row.id, values: { fieldExtendJson: Object.keys(json).length > 0 ? JSON.stringify(json) : '' } });
                  }
                } catch (e) {}
                return acc;
              }, []);
            if (clearValues.length > 0) {
              tableRef.value!.setValues(clearValues);
            }
          }
          //update-end---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
          values = [{ rowKey, values: { fieldExtendJson: JSON.stringify(data) } }];
        }else{
          values = [{ rowKey, values: { fieldExtendJson: '' } }];
        }
        tableRef.value!.setValues(values);
      }

      // 判断某行是否为标题字段
      function isTitleFieldRow(row) {
        if (!row?.fieldExtendJson) return false;
        try {
          const json = JSON.parse(row.fieldExtendJson);
          return json.isTitleField === 1;
        } catch (e) {
          return false;
        }
      }

      // 扩展参数JSON直接编辑
      const jsonModalVisible = ref(false);
      const currentJsonStr = ref('');
      const currentJsonRowKey = ref('');

      function openJsonConfig(props) {
        currentJsonStr.value = props.row.fieldExtendJson || '';
        currentJsonRowKey.value = props.rowId;
        jsonModalVisible.value = true;
      }

      function handleJsonSubmit() {
        let values = [{ rowKey: currentJsonRowKey.value, values: { fieldExtendJson: currentJsonStr.value } }];
        tableRef.value!.setValues(values);
        jsonModalVisible.value = false;
      }
      
      const handleValueChange = (event) => {
        console.log(event);
        // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-489】他表字段禁用查询
        const { col, value, row } = event;
        if (col?.key === 'fieldShowType' && value === 'link_table_field') {
          tableRef.value!.setValues([
            {
              rowKey: row.id,
              values: { isQuery: '0', sortFlag: '0' },
            },
          ]);
        }
        // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（控件类型变化时同步默认查询类型）
        if (col?.key === 'fieldShowType') {
          syncQueryMode({ row });
        }
        // update-end--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（控件类型变化时同步默认查询类型）
        // update-end--author:liaozhiyang---date:20260714---for【LHZP-489】他表字段禁用查询
      }
      /**
       * 2024-07-25
       * liaozhiyang
       * 【TV360X-1857】online查询增加模糊查询
       * */
      function validateQueryMode({ cellValue, row }, callback) {
        let { dbTable } = tables;
        // 获取到 dbTable（数据库属性） 中的数据
        const dbType = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0].dbType;
        if (cellValue === 'group') {
          // 【TV360X-1967】范围查询只支持时间和数字类型且得支持控件类型是时间(HH:mm:ss)
          if (['double', 'int', 'long', 'BigDecimal', 'Date', 'Datetime'].includes(dbType) || row.fieldShowType === 'time') {
            callback(true);
          } else {
            callback(false, '范围查询，只支持数据库属里的字段类型为：Integer、Long、Double、BigDecimal及Date、Datetime 或 控件类型是时间(HH:mm:ss)');
          }
        } else if (cellValue === 'like') {
          if (['string'].includes(dbType) && row.fieldShowType === 'text') {
            callback(true);
          } else {
            callback(false, '模糊查询，只支持控件类型是文本框且数据库属里的字段类型是String');
          }
        } else {
          callback(true);
        }
      }
      // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（按字段类型和控件类型设置模糊或普通查询）
      const syncQueryMode = ({ row }) => {
        const curTableRow = tableRef.value!.getTableData({ rowIds: [row.id] })[0];
        if (!curTableRow) {
          return;
        }
        const dbRow = tables.dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
        const queryMode = dbRow?.dbType === 'string' && curTableRow.fieldShowType === 'text' ? 'like' : 'single';
        if ((queryMode === 'like' && curTableRow.queryMode !== 'like') || (queryMode === 'single' && curTableRow.queryMode === 'like')) {
          tableRef.value!.setValues([{ rowKey: row.id, values: { queryMode } }]);
        }
      };
      // update-end--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（按字段类型和控件类型设置模糊或普通查询）

      /**
       * 2024-04-17
       * liaozhiyang
       * 【QQYUN-8972】popup字典组件字典code和字典text只允许输入一个字段
       * */
      function validateDictFieldOrDictText({ cellValue, row }, callback) {
        if (row?.fieldShowType === 'popup_dict') {
          if (cellValue.indexOf(',') == -1) {
            callback(true);
          } else {
            callback(false, 'popup字典组件只允许填写一个字段');
          }
        } else {
          callback(true);
        }
      }

      // update-begin-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列
      /** 字典配置摘要：表字典/Popup 已配置时显示 table.code → text */
      function getDictSummary(row) {
        const table = row?.dictTable || '';
        const code = row?.dictField || '';
        const text = row?.dictText || '';
        if (!table) return '';
        const left = code ? `${table}.${code}` : table;
        return text ? `${left} → ${text}` : left;
      }

      /** 内联输入框 placeholder：popup_dict 时强调单字段 */
      function getDictInputPlaceholder(row) {
        if (row?.fieldShowType === 'popup_dict') {
          return '点击右侧配置表字典';
        }
        return '字典编码';
      }

      /** 打开字典配置弹窗 */
      function openDictConfig(props) {
        const { row, rowId } = props;
        openDictConfigModal(true, {
          rowKey: rowId,
          fieldShowType: row.fieldShowType,
          dictTable: row.dictTable || '',
          dictField: row.dictField || '',
          dictText: row.dictText || '',
        });
      }

      /** 弹窗回填三字段 */
      function handleDictConfig(record) {
        const { rowKey, dictTable, dictField, dictText } = record;
        tableRef.value!.setValues([{ rowKey, values: { dictTable, dictField, dictText } }]);
      }
      // update-end-author:liaozhiyang---date:2026-05-20---for:【QQYUN-15464】字典Table/Code/Text 合成一列

      return { ...setup, syncTable, columns, enableQuery, syncFieldShowType, changePageType,
        showConfigButton, showFieldConfig,
        registerExtJsonModal, handleExtJson, openConfig,
        registerModal, handleConfigData,
        registerFieldModal, handleFieldConfigData, syncIsQuery, handleValueChange, syncQueryMode, isSubTableOneToMany,
        jsonModalVisible, currentJsonStr, openJsonConfig, handleJsonSubmit, isSubTable,
        registerDictConfigModal, openDictConfig, handleDictConfig, getDictSummary, getDictInputPlaceholder,
        isTitleFieldRow,
      };
      //update-end-author:taoyan date:2022-8-11 for: VUEN-1940【online】关联记录和他表字段，配置智能化
      
    },
  });
</script>

<style lang="less" scoped>
  :deep(.online-config-page td:last-child.col--selected){
    box-shadow:none !important;
  }
  .field-name-cell {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    width: 100%;
  }
  .title-field-tag {
    font-size: 10px;
    line-height: 14px;
    padding: 0 4px;
    margin: 0;
    flex-shrink: 0;
  }
  //【QQYUN-8972】online隐藏控件长度前面的可编辑icon
  :deep(.vxe-header--row) {
    .vxe-header--column:nth-child(9) {
      .vxe-cell--edit-icon {display:none;}
    }
  }
  .dict-config-cell {
    display: flex;
    align-items: center;
    width: 100%;
    gap: 4px;
    .dict-config-input {
      flex: 1;
      min-width: 0;
    }
    // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-157】字典编码默认不出现边框
    > :deep(.dict-config-input.ant-input-affix-wrapper) {
      border-color: transparent;
      background-color: transparent;
      box-shadow: none;
      transition-property: border-color, box-shadow;
      &:hover {
        border-color: transparent;
        background-color: transparent;
      }
      .ant-input,
      .ant-input:hover {
        background-color: transparent;
      }
      &:focus-within {
        border-color: @primary-color;
        background-color: @component-background;
        box-shadow: 0 0 0 2px fade(@primary-color, 20%);
        .ant-input {
          background-color: @component-background;
        }
        .ant-input-clear-icon {
          visibility: visible;
        }
      }
      .ant-input-clear-icon {
        visibility: hidden;
      }
    }
    // update-end--author:liaozhiyang---date:20260831---for:【LHZP-157】字典编码默认不出现边框
    .dict-config-summary {
      flex: 1;
      min-width: 0;
      padding: 0 8px;
      height: 28px;
      line-height: 28px;
      border: 1px dashed var(--ant-color-border, #d9d9d9);
      border-radius: 4px;
      background-color: var(--ant-color-fill-quaternary, #fafafa);
      color: var(--ant-color-text, rgba(0, 0, 0, 0.85));
      cursor: pointer;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      &:hover {
        border-color: var(--ant-color-primary, #1677ff);
        color: var(--ant-color-primary, #1677ff);
      }
    }
    .dict-config-btn {
      flex: 0 0 auto;
      padding: 0 6px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      color: rgba(0, 0, 0, 0.45);
      .app-iconify {
        color: inherit;
      }
      &:hover {
        color: @primary-color;
      }
    }
  }
</style>
