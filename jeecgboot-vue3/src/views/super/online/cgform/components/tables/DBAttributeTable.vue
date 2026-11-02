<template>
  <JVxeTable
    class="dBAttributeTable"
    ref="tableRef"
    rowNumber
    rowSelection
    dragSort
    :notAllowDrag="[{'key':'dbFieldName','value':'id'}]"
    keyboardEdit
    sortKey="orderNum"
    addButtonSettings
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :toolbar="actionButton"
    :insertRow="actionButton"
    :maxHeight="tableHeight.normal"
    :disabledRows="{ dbFieldName: handleDisabledDbFieldName }"
    v-bind="tableProps"
    @added="handleAdded"
    @removed="handleRemoved"
    @dragged="handleDragged"
    @inserted="handleInserted"
    @valueChange="handleValueChange"
  >
    <template #toolbarSuffix>
      <a-button type="primary" preIcon="ant-design:robot-outlined" @click="openAiModal">AI字段建议</a-button>
      <!-- update-begin--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（在工具栏末尾按字段存在状态显示快捷按钮） -->
      <a-tooltip title="新增流程状态字段">
        <a-button v-if="actionButton && !hasBpmStatusField" type="primary" preIcon="ant-design:branches-outlined" @click="addBpmStatusField">流程状态字段</a-button>
      </a-tooltip>
      <!-- update-end--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（在工具栏末尾按字段存在状态显示快捷按钮） -->
    </template>
  </JVxeTable>
  <AiModal
    :tableName="tableName"
    :isUpdate="isUpdate"
    @register="registerAiModal"
    v-if="aiModalShow"
    @close="() => (aiModalShow = false)"
    :DBtableRef="tableRef"
    @generate="handleGenerate"
  />
</template>

<script lang="ts">
  import { ref, getCurrentInstance, defineComponent, computed } from 'vue';
  import { JVxeTypes, JVxeColumn } from '/@/components/jeecg/JVxeTable/types';
  import { useTableSync } from '../../hooks/useTableSync';
  import AiModal from './components/aiModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';

  const MySQLKeywords = [
    'ADD',
    'ALL',
    'ALTER',
    'ANALYZE',
    'AND',
    'AS',
    'ASC',
    'ASENSITIVE',
    'BEFORE',
    'BETWEEN',
    'BIGINT',
    'BINARY',
    'BLOB',
    'BOTH',
    'BY',
    'CALL',
    'CASCADE',
    'CASE',
    'CHANGE',
    'CHAR',
    'CHARACTER',
    'CHECK',
    'COLLATE',
    'COLUMN',
    'CONDITION',
    'CONNECTION',
    'CONSTRAINT',
    'CONTINUE',
    'CONVERT',
    'CREATE',
    'CROSS',
    'CURRENT_DATE',
    'CURRENT_TIME',
    'CURRENT_TIMESTAMP',
    'CURRENT_USER',
    'CURSOR',
    'DATABASE',
    'DATABASES',
    'DAY_HOUR',
    'DAY_MICROSECOND',
    'DAY_MINUTE',
    'DAY_SECOND',
    'DEC',
    'DECIMAL',
    'DECLARE',
    'DEFAULT',
    'DELAYED',
    'DELETE',
    'DESC',
    'DESCRIBE',
    'DETERMINISTIC',
    'DISTINCT',
    'DISTINCTROW',
    'DIV',
    'DOUBLE',
    'DROP',
    'DUAL',
    'EACH',
    'ELSE',
    'ELSEIF',
    'ENCLOSED',
    'ESCAPED',
    'EXISTS',
    'EXIT',
    'EXPLAIN',
    'FALSE',
    'FETCH',
    'FLOAT',
    'FLOAT4',
    'FLOAT8',
    'FOR',
    'FORCE',
    'FOREIGN',
    'FROM',
    'FULLTEXT',
    'GOTO',
    'GRANT',
    'GROUP',
    'HAVING',
    'HIGH_PRIORITY',
    'HOUR_MICROSECOND',
    'HOUR_MINUTE',
    'HOUR_SECOND',
    'IF',
    'IGNORE',
    'IN',
    'INDEX',
    'INFILE',
    'INNER',
    'INOUT',
    'INSENSITIVE',
    'INSERT',
    'INT',
    'INT1',
    'INT2',
    'INT3',
    'INT4',
    'INT8',
    'INTEGER',
    'INTERVAL',
    'INTO',
    'IS',
    'ITERATE',
    'JOIN',
    'KEY',
    'KEYS',
    'KILL',
    'LABEL',
    'LEADING',
    'LEAVE',
    'LEFT',
    'LIKE',
    'LIMIT',
    'LINEAR',
    'LINES',
    'LOAD',
    'LOCALTIME',
    'LOCALTIMESTAMP',
    'LOCK',
    'LONG',
    'LONGBLOB',
    'LONGTEXT',
    'LOOP',
    'LOW_PRIORITY',
    'MATCH',
    'MEDIUMBLOB',
    'MEDIUMINT',
    'MEDIUMTEXT',
    'MIDDLEINT',
    'MINUTE_MICROSECOND',
    'MINUTE_SECOND',
    'MOD',
    'MODIFIES',
    'NATURAL',
    'NOT',
    'NO_WRITE_TO_BINLOG',
    'NULL',
    'NUMERIC',
    'ON',
    'OPTIMIZE',
    'OPTION',
    'OPTIONALLY',
    'OR',
    'ORDER',
    'OUT',
    'OUTER',
    'OUTFILE',
    'PRECISION',
    'PRIMARY',
    'PROCEDURE',
    'PURGE',
    'RAID0',
    'RANGE',
    'READ',
    'READS',
    'REAL',
    'REFERENCES',
    'REGEXP',
    'RELEASE',
    'RENAME',
    'REPEAT',
    'REPLACE',
    'REQUIRE',
    'RESTRICT',
    'RETURN',
    'REVOKE',
    'RIGHT',
    'RLIKE',
    'SCHEMA',
    'SCHEMAS',
    'SECOND_MICROSECOND',
    'SELECT',
    'SENSITIVE',
    'SEPARATOR',
    'SET',
    'SHOW',
    'SMALLINT',
    'SPATIAL',
    'SPECIFIC',
    'SQL',
    'SQLEXCEPTION',
    'SQLSTATE',
    'SQLWARNING',
    'SQL_BIG_RESULT',
    'SQL_CALC_FOUND_ROWS',
    'SQL_SMALL_RESULT',
    'SSL',
    'STARTING',
    'STRAIGHT_JOIN',
    'TABLE',
    'TERMINATED',
    'THEN',
    'TINYBLOB',
    'TINYINT',
    'TINYTEXT',
    'TO',
    'TRAILING',
    'TRIGGER',
    'TRUE',
    'UNDO',
    'UNION',
    'UNIQUE',
    'UNLOCK',
    'UNSIGNED',
    'UPDATE',
    'USAGE',
    'USE',
    'USING',
    'UTC_DATE',
    'UTC_TIME',
    'UTC_TIMESTAMP',
    'VALUES',
    'VARBINARY',
    'VARCHAR',
    'VARCHARACTER',
    'VARYING',
    'WHEN',
    'WHERE',
    'WHILE',
    'WITH',
    'WRITE',
    'X509',
    'XOR',
    'YEAR_MONTH',
    'ZEROFILL',
  ];

  export default defineComponent({
    name: 'DBAttributeTable',
    props: {
      actionButton: { type: Boolean, default: true },
      tableName: { type: String, default: '' },
      isUpdate: { type: Boolean, default: false },
      isTree: { type: String, default: 'N' },
    },
    components: {
      AiModal,
    },
    emits: ['added', 'removed', 'inserted', 'dragged', 'syncDbType', 'syncQueryMode', 'syncDbIsPersist', 'syncDbIsNull'],
    setup(props, { emit }) {
      const instance = getCurrentInstance();
      const { createMessage: $message } = useMessage();
      const addBatching = ref(false);
      const columns = ref<JVxeColumn[]>([
        {
          title: '字段名称',
          key: 'dbFieldName',
          width: 140,
          type: JVxeTypes.input,
          defaultValue: '',
          placeholder: '请输入${title}',
          fixed: 'left',
          validateRules: [
            { required: true, message: '${title}不能为空' },
            {
              pattern: /^[a-zA-Z]{1}(?!_)[a-zA-Z0-9_\\$]+$/,
              message: '命名规则：只能由字母、数字、下划线、$符号组成；必须以字母开头；不能以单个字母加下滑线开头',
            },
            { unique: true, message: '${title}不能重复' },
            {
              handler({ cellValue }, callback) {
                /* 判断是否是关键字 */
                if (MySQLKeywords.includes(cellValue.toUpperCase())) {
                  callback(false, cellValue + '是关键字，不能作为字段名称使用！');
                } else {
                  callback(true);
                }
              },
            },
            { handler: validateExistIndex },
            // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
            {
              handler({ cellValue }, callback) {
                if (cellValue.length > 32) {
                  callback(false, '字段名最长32个字符');
                } else {
                  callback(true);
                }
              },
            },
            // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          ],
          disabled: !props.actionButton,
        },
        {
          title: '字段备注',
          key: 'dbFieldTxt',
          width: 140,
          type: JVxeTypes.input,
          defaultValue: '',
          placeholder: '请输入${title}',
          fixed: 'left',
          validateRules: [
            { required: true, message: '${title}不能为空' },
            // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
            {
              handler({ cellValue }, callback) {
                if (cellValue.length > 200) {
                  callback(false, '字段名最长200个字');
                } else {
                  callback(true);
                }
              },
            },
            // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          ],
        },
        {
          title: '字段长度',
          key: 'dbLength',
          width: 120,
          type: JVxeTypes.inputNumber,
          defaultValue: 32,
          placeholder: '请输入${title}',
          validateRules: [{ required: true, message: '${title}不能为空' }],
          disabled: !props.actionButton,
        },
        {
          title: '小数点',
          key: 'dbPointLength',
          width: 100,
          type: JVxeTypes.inputNumber,
          defaultValue: 0,
          placeholder: '请输入${title}',
          validateRules: [{ required: true, message: '${title}不能为空' }],
          disabled: !props.actionButton,
        },
        {
          title: '默认值',
          key: 'dbDefaultVal',
          width: 140,
          type: JVxeTypes.input,
          defaultValue: '',
          disabled: !props.actionButton,
        },
        {
          title: '字段类型',
          key: 'dbType',
          width: 140,
          type: JVxeTypes.select,
          options: [
            { title: 'String', value: 'string' },
            { title: 'Integer', value: 'int' },
            { title: 'Double', value: 'double' },
            { title: 'Long', value: 'long' },
            { title: 'Date', value: 'Date' },
            { title: 'Datetime', value: 'Datetime' },
            { title: 'BigDecimal', value: 'BigDecimal' },
            { title: 'Text', value: 'Text' },
            { title: 'LongText', value: 'LongText' },
            { title: 'Blob', value: 'Blob' },
          ],
          defaultValue: 'string',
          placeholder: '请选择${title}',
          disabled: !props.actionButton,
          validateRules: [{ required: true, message: '请选择${title}' }, { handler: validateFieldDbType }],
        },
        {
          title: '主键',
          key: 'dbIsKey',
          width: 80,
          type: JVxeTypes.checkbox,
          align: 'center',
          customValue: ['1', '0'],
          defaultChecked: false,
          disabled: !props.actionButton,
        },
        {
          title: '允许空值',
          key: 'dbIsNull',
          width: 80,
          type: JVxeTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: true,
          disabled: !props.actionButton,
        },
        {
          title: '同步数据库',
          key: 'dbIsPersist',
          minWidth: 80,
          type: JVxeTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: true,
          disabled: !props.actionButton,
        },
        { title: 'orderNum', key: 'orderNum', type: JVxeTypes.hidden },
      ]);
      let removeIds = [];

      const setup = useTableSync(columns);
      const { tableRef, loading, dataSource, tableHeight, tableProps, setDataSource, validateData } = setup;
      // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（监听表格增删并控制快捷按钮显隐）
      const tableDataVersion = ref(0);
      const hasBpmStatusField = computed(() => {
        tableDataVersion.value;
        const tableData = tableRef.value?.getTableData() ?? dataSource.value;
        return tableData.some((row) => row.dbFieldName === 'bpm_status');
      });
      // update-end--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（监听表格增删并控制快捷按钮显隐）
      // update-begin--author:liaozhiyang---date:20250407---for：【QQYUN-11801】ai建表字段
      const aiModalShow = ref(false);
      const [registerAiModal, { openModal }] = useModal();
      const handleGenerate = (data) => {
        tableRef.value!.addRows(data);
      };
      // update-end--author:liaozhiyang---date:20250407---for：【QQYUN-11801】ai建表字段

      // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（复用普通新增默认值并配置流程状态页面属性）
      /** 按普通新增行的默认配置添加流程状态字段，仅覆盖流程字段要求的属性。 */
      function addBpmStatusField() {
        if (hasBpmStatusField.value) {
          $message.warning('流程状态字段已存在');
          return;
        }
        tableRef.value!.addRows({
          dbFieldName: 'bpm_status',
          dbFieldTxt: '流程状态',
          isShowList: '1',
          fieldShowType: 'list',
          dictField: 'bpm_status',
          isShowForm: '0',
          sortFlag: '0',
        });
      }
      // update-end--author:liaozhiyang---date:20260824---for:【LHZP-1951】新增流程状态字段（复用普通新增默认值并配置流程状态页面属性）

      function handleAdded() {
        tableDataVersion.value++;
        emit('added', instance);
      }

      function handleRemoved(event) {
        tableDataVersion.value++;
        removeIds = removeIds.concat(event.deleteRows.map((r) => r.id));
        emit('removed', { ...event, removeIds, target: instance });
      }

      function handleDragged(event) {
        emit('dragged', {
          oldIndex: event.oldIndex,
          newIndex: event.newIndex,
          target: instance,
        });
      }

      function handleInserted(event) {
        emit('inserted', { ...event, target: instance });
      }

      function getRemoveIds() {
        return removeIds;
      }

      function handleValueChange(event) {
        let { type, row, col, value, target, oldValue } = event;
        if (type === JVxeTypes.select && col.key === 'dbType') {
          // 当字段类型改为Date时触发同步事件，同步控件类型为日期选择器
          if (value === 'Date' || value === 'Datetime') {
            emit('syncDbType', { row, value, target: instance });
          }

          // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7530】字段类型从Date和DateTime改成string，控件也变成文本框
          if ((value !== 'Date' || value !== 'Datetime') && (oldValue == 'Date' || oldValue == 'Datetime')) {
            emit('syncDbType', { row, value, target: instance });
          }
          // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7530】字段类型从Date和DateTime改成string，控件也变成文本框
          // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
          // Blob类型不支持查询，触发同步去掉查询勾选
          if (value === 'Blob') {
            emit('syncDbType', { row, value, target: instance });
          }
          // update-end--author:liaozhiyang---date:20260713---for：【LHZP-516】blob类型字段的不让勾选查询
          // 字段类型Blob、Date、Text 将字段长度改为0
          if (value === 'Blob' || value === 'Text' || value === 'Date') {
            target.setValues([{ rowKey: row.id, values: { dbLength: '0' } }]);
          } else if (value === 'string') {
            // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-56】字段类型修改，改成string时给默认长度
            target.setValues([{ rowKey: row.id, values: { dbLength: '32' } }]);
            // update-end--author:liaozhiyang---date:20240517---for：【TV360X-56】字段类型修改，改成string时给默认长度
          } else if (value === 'int' || value === 'double' || value === 'BigDecimal') {
            target.setValues([{ rowKey: row.id, values: { dbLength: '10' } }]);
          //update-begin---author:wangshuai---date:2025-10-24---for:【QQYUN-13901】增加Long类型---
          } else if (value === 'long') {
            target.setValues([{ rowKey: row.id, values: { dbLength: '19' } }]);
          //update-end---author:wangshuai---date:2025-10-24---for:【QQYUN-13901】增加Long类型---
          } else if (row['dbLength'] === '0') {
            target.setValues([{ rowKey: row.id, values: { dbLength: '32' } }]);
          }
          // update-begin--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（字段类型改变后通知页面属性更新查询类型）
          emit('syncQueryMode', { oldValue, row });
          // update-end--author:liaozhiyang---date:20260824---for:【LHZP-160】字段类型是String且控件是文本框默认模糊查询（字段类型改变后通知页面属性更新查询类型）
        } else if (col.key === 'dbIsPersist') {
          // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选
          emit('syncDbIsPersist', { row, value, target: instance });
          // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选
        } else if (col.key === 'dbIsNull') {
          // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】数据库不允许为空，校验默认相应勾上
          emit('syncDbIsNull', { row, value, target: instance });
          // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】数据库不允许为空，校验默认相应勾上
        }
      }

      // dbFieldName字段禁用
      function handleDisabledDbFieldName(value, _row, rowIndex) {
        // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-368】online视图插入一行禁用
        if (value === 'has_child' && props.isTree === 'Y') {
          return true;
        }
        // update-end--author:liaozhiyang---date:20260713---for：【LHZP-368】online视图插入一行禁用
        if (value === 'id') {
          // -update-begin--author:liaozhiyang---date:20240614---for：【TV360X-357】数据库属性其他行拖到id行之上时id可编辑了
          const { tables } = setup;
          const dataSource = tables.dbTable.value!.tableRef.getTableData() ?? [];
          const findIndex = dataSource.findIndex((item) => item.dbFieldName === 'id');
          const idIndex = findIndex === -1 ? 0 : findIndex;
          // -update-end--author:liaozhiyang---date:20240614---for：【TV360X-357】数据库属性其他行拖到id行之上时id可编辑了
          if (idIndex === rowIndex) {
            return true;
          }
        }
        return false;
      }

      /** 新增一行，并反馈给父组件 */
      function tableAddLine(newLine) {
        tableRef.value!.pushRows(newLine);
        // 正在批量添加数据，不单独反馈
        if (!addBatching.value) {
          emit('added', instance);
        }
      }

      function tableDeleteLines(ids: string[]) {
        return tableRef.value!.removeRowsById(ids);
      }

      /** 开始批量添加数据，在此期间添加的数据不会反馈给父组件进行同步 */
      function addBatchBegin() {
        addBatching.value = true;
        loading.value = true;
      }

      /** 结束批量添加数据，会立即反馈给父组件进行同步数据 */
      function addBatchEnd() {
        addBatching.value = false;
        loading.value = false;
        emit('added', instance);
      }

      //update-begin-author:taoyan date:2022-5-26 for: issues/I56ATQ Online代码功能，跨库数据库同步时报错（单库正常）：org.jeecg.modules.online.cgform.c.a:492
      function validateExistIndex({ cellValue, row }, callback) {
        const { tables } = setup;
        if (tables) {
          let dataSource = tables.dbTable.value!.tableRef.dataSource;
          //获取 table的原始数据
          let temp = dataSource.filter((item) => item.id === row.id);
          if (!temp || temp.length <= 0) {
            callback(true);
          }
          let dbFieldName = temp[0]['dbFieldName'];
          if (dbFieldName == cellValue) {
            callback(true);
          }
          //获取索引数据
          let arr = tables.idxTable.value!.tableRef.getTableData();
          for (let item of arr) {
            let indexField = item.indexField;
            //索引字段可能是多个字段
            let indexFieldArray = indexField.split(',');
            if (indexFieldArray.indexOf(dbFieldName) >= 0) {
              callback(false, '当前字段存在索引配置，请先删除索引再修改字段');
            }
          }
        }
        callback(true);
      }
      //update-end-author:taoyan date:2022-5-26 for: issues/I56ATQ Online代码功能，跨库数据库同步时报错（单库正常）：org.jeecg.modules.online.cgform.c.a:492
      // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-278】设置了小数点不可设置integer类型
      function validateFieldDbType({ cellValue, row }, callback) {
        if (row.dbType == 'int' && row.dbPointLength > 0) {
          callback(false, '设置了小数点不可设置integer类型');
        } else if (row.dbType == 'long' && row.dbPointLength > 0 ) {
          // update-begin--author:liaozhiyang---date:20240522---for：【LHZP-280】设置了小数点不可设置Long类型
          callback(false, '设置了小数点不可设置Long类型');
          // update-end--author:liaozhiyang---date:20240522---for：【LHZP-280】设置了小数点不可设置Long类型
        }
        callback(true);
      }
      // update-end--author:liaozhiyang---date:20240522---for：【TV360X-278】设置了小数点不可设置integer类型
      // update-begin--author:liaozhiyang---date:20250407---for：【QQYUN-11801】ai建表字段
      const openAiModal = () => {
        aiModalShow.value = true;
        setTimeout(() => {
          openModal(true);
        }, 0);
      };
      // update-end--author:liaozhiyang---date:20250407---for：【QQYUN-11801】ai建表字段
      return {
        tableRef,
        loading,
        columns,
        dataSource,
        setDataSource,
        addBatchBegin,
        addBatchEnd,
        tableAddLine,
        tableHeight,
        tableProps,
        tableDeleteLines,
        handleAdded,
        handleRemoved,
        handleDragged,
        handleInserted,
        handleValueChange,
        handleDisabledDbFieldName,
        validateData,
        getRemoveIds,
        validateExistIndex,
        openAiModal,
        registerAiModal,
        aiModalShow,
        handleGenerate,
        addBpmStatusField,
        hasBpmStatusField,
      };
    },
  });
</script>
<style lang="less" scoped>
  // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-212】字段名称校验样式调整
  .dBAttributeTable {
    :deep(table) {
      tr {
        td:nth-child(4) {
          .vxe-cell--valid-error-msg {
            white-space: nowrap;
          }
        }
      }
    }
  }
  // update-end--author:liaozhiyang---date:20240521---for：【TV360X-212】字段名称校验样式调整
</style>
