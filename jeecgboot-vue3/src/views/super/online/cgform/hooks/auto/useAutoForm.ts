import { ref, watch, reactive, toRaw, nextTick, computed } from 'vue';
import { FormSchema } from '/@/components/Form';
import FormSchemaFactory from '../../auto/comp/factory/FormSchemaFactory';
import IFormSchema from '../../auto/comp/factory/IFormSchema';
import { JVxeTableInstance } from '/@/components/jeecg/JVxeTable/types';
import { duplicateCheck } from '/@/views/system/user/user.api';
import { initDefValueConfig, initSubTableDefValueConfig } from '../../util/FieldDefVal';
import { usePermissionStore } from '/@/store/modules/permission';
import { ONL_AUTH_PRE } from '../../types/onlineRender';
import { pick } from 'lodash-es';
import { DetailFormSchema } from '../../extend/form/useDetailForm';
import {useExtendComponent} from './useExtendComponent'
import componentSetting from '/@/settings/componentSetting';
import { LABELLENGTH } from '../../util/constant';
import { useAppInject } from '/@/hooks/web/useAppInject';

export const LINK_DOWN = 'link_down';
export const LINK_TABLE_FIELD = 'link_table_field';
export const LINK_TABLE = 'link_table';

export interface OnlSubTab {
  key: string;
  formTemplate: number;
  properties?: any[];
  columns?: any[];
  foreignKey: string;
  describe: string;
  relationType: number;
  requiredFields?: string[];
  order: number;
  id?:string;
}
/**
 * 获取实际表单的配置信息
 */
export function useFormItems(props, onlineFormRef) {
  // 添加表单组件-专门给 online 表单用
  useExtendComponent();
  
  // 下拉框等组件需要通过此class作为父级container
  const modalClass = props.modalClass;
  // 表单渲染用到的配置
  const formSchemas = ref<any>([]);
  // 表名
  const tableName = ref('');
  // 编辑页面 数据库原数据
  const dbData = ref<any>({});
  // 字段展示状态
  const fieldDisplayStatus = reactive<any>({});
  const hasSubTable = ref(false);

  const subTabInfo = ref<OnlSubTab[]>([]);
  const subDataSource = ref({});
  const refMap = {};
  // 联动组件列表
  const linkDownList = ref<any>([]);
  /**
   * 有表单默认值的字段
   * 表名: [{field, value, type}]
   */
  const defaultValueFields = reactive<any>({});
  // 表单栅格
  const baseColProps = ref<any>('');
  baseColProps.value = { sm: 24, xs: 24, md: 12, lg: 12, xl: 12, xxl: 12 };
  // update-begin--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } });
  // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
  const wrapperCol = ref<any>(null);
  // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
  // update-end--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
  // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
  const labelWidth = ref<any>(6 * 14 + 10);
  // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化

  function createFormSchemas(properties: any[], required, checkOnlyFieldValue, onlineExtConfigJson = {}) {
    //let properties:any[] = result.schema.properties
    clearObj(defaultValueFields);
    defaultValueFields[tableName.value] = [];
    let subInfo: OnlSubTab[] = [];
    let arr: IFormSchema[] = [];
    let hideFields: string[] = [];
    let dataSourceObj = {};
    let tableLinkInfo:any = {}
    Object.keys(properties).map((key) => {
      const item = properties[key];
      // uiSchema 无用
      //const uiItem = this.uiSchema[key];// method、formTemplate、url
      if (item.view == 'tab') {
        hasSubTable.value = true;
        defaultValueFields[key] = [];
        let temp: OnlSubTab = {
          key,
          formTemplate: Number(item.formTemplate || 1),
          // 这个foreignKey是主表的字段
          foreignKey: item['foreignKey'],
          describe: item.describe,
          relationType: item.relationType,
          requiredFields: item.required || [],
          order: item.order,
          id: item.id
        };
        if (item.relationType == 1) {
          refMap[key] = ref(null);
          temp['properties'] = item.properties;
        } else {
          dealSubProerties(item);
          refMap[key] = ref<JVxeTableInstance>();
          temp['columns'] = item.columns;
          dataSourceObj[key] = [];
          // TODO 处理子表的新增删除按钮权限
          //this.handleSubTableButtonAuth(item)
        }
        subInfo.push(temp);
        // 记录子表按钮权限
        handleSubTableButtonAuth(key, item);
      } else {
        initDefValueConfig(key, item, defaultValueFields[tableName.value]);
        if (item.view === LINK_DOWN) {
          let array = handleLinkDown(item, key);
          for (let linkDownItem of array) {
            // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-314】联动组件添加组件默认值
            const fItem = linkDownItem.key == key ? item : item.others?.find((item) => item.field === linkDownItem.key);
            fItem && initDefValueConfig(linkDownItem.key, fItem, defaultValueFields[tableName.value]);
            // update-end--author:liaozhiyang---date:20240522---for：【TV360X-314】联动组件添加组件默认值
            fieldDisplayStatus[linkDownItem.key] = true;
            fieldDisplayStatus[linkDownItem.key + '_load'] = true;
            // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-75】表单label长度设置，联动组件没生效
            setFieldExtend(onlineExtConfigJson, linkDownItem);
            // update-end--author:liaozhiyang---date:20240521---for：【TV360X-75】表单label长度设置，联动组件没生效
            let temp = FormSchemaFactory.createFormSchema(linkDownItem.key, linkDownItem);
            // update-begin--author:liaozhiyang---date:20251230---for：【issues/9223】js增强，用loaded方法里加入某个字段隐藏，导致打开窗口时其他已经设置只读字段，恢复成可写
            fieldDisplayStatus[linkDownItem.key + '_disabled'] = temp.disabled ?? false;
            // update-end--author:liaozhiyang---date:20251230---for：【issues/9223】js增强，用loaded方法里加入某个字段隐藏，导致打开窗口时其他已经设置只读字段，恢复成可写
            if (checkOnlyFieldValue) {
              temp.setOnlyValidateFun(checkOnlyFieldValue);
            }
            temp.isRequired(required);
            temp.setFormRef(onlineFormRef);
            // 联动控件 只读由第一个控件的只读状态决定
            temp.handleWidgetAttr(item);
            let tempIndex = getFieldIndex(arr, linkDownItem.key);
            if (tempIndex == -1) {
              arr.push(temp);
            } else {
              arr[tempIndex] = temp;
            }
          }
        } else {
          // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-314】联动组件添加组件默认值
          initDefValueConfig(key, item, defaultValueFields[tableName.value]);
          // update-end--author:liaozhiyang---date:20240522---for：【TV360X-314】联动组件添加组件默认值
          fieldDisplayStatus[key] = true;
          fieldDisplayStatus[key + '_load'] = true;
          let tempIndex = getFieldIndex(arr, key);
          if (tempIndex == -1) {
            // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
            setFieldExtend(onlineExtConfigJson, item);
            // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
            let temp = FormSchemaFactory.createFormSchema(key, item);
            // update-begin--author:liaozhiyang---date:20251230---for：【issues/9223】js增强，用loaded方法里加入某个字段隐藏，导致打开窗口时其他已经设置只读字段，恢复成可写
            fieldDisplayStatus[key + '_disabled'] = temp.disabled ?? false;
            // update-end--author:liaozhiyang---date:20251230---for：【issues/9223】js增强，用loaded方法里加入某个字段隐藏，导致打开窗口时其他已经设置只读字段，恢复成可写
            if (checkOnlyFieldValue) {
              temp.setOnlyValidateFun(checkOnlyFieldValue);
            }
            temp.isRequired(required);
            temp.setFormRef(onlineFormRef);
            arr.push(temp);
            hideFields.push(...temp.getRelatedHideFields());
            
            //update-begin-author:taoyan date:2022-8-5 for: 获取他表字段信息
            //如果是他表字段获取关联信息
            if(item.view === LINK_TABLE_FIELD){
              let tempInfo = temp.getLinkFieldInfo();
              if(tempInfo){
                if(tableLinkInfo[tempInfo[0]]){
                  let tableLinkInfoEle:string[] = tableLinkInfo[tempInfo[0]];
                  tableLinkInfoEle.push(tempInfo[1]);
                }else{
                  tableLinkInfo[tempInfo[0]] = [tempInfo[1]]
                }
              }
            }
            //update-end-author:taoyan date:2022-8-5 for: 获取他表字段信息
            
          }
        }
        //fp.checkOnlyMethod = this.$Jdebounce(this.checkOnlyFieldValue, 1000);
      }
    });
    // 1.对arr排序
    arr.sort(function (a, b) {
      return a.order - b.order;
    });
    // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    const oneRowData: any = [];
    (() => {
      for (let i = 0, len = arr.length; i < len; i++) {
        const item = arr[i];
        if (getFieldExtend(item?._data, 'isOneRow')) {
          oneRowData.push(arr.splice(i, 1)[0]);
          i--;
          len--;
        }
      }
    })();
    arr = [...arr,...oneRowData];
    // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    // 2.获取真实表单配置
    let formSchemaArray: FormSchema[] = [];
    formSchemaArray.push(FormSchemaFactory.createIdField());
    let longestLabelComponet: any = null;
    let isComponetRequired = false;
    for (let a of arr) {
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      const curLabelLen = a.label.length;
      if (longestLabelComponet) {
        if (longestLabelComponet.label.length < curLabelLen) {
          longestLabelComponet = a;
        } else if(longestLabelComponet.label.length === curLabelLen) {
          // 文字labael相同，则判断是否必填。（必填*占13像素）
          if(!longestLabelComponet.required && a.required) {
            longestLabelComponet = a;
          }
        }
      } else {
        longestLabelComponet = a;
      }
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // update-begin--author:liaozhiyang---date:20230105---for：【TV360X-209】表单label长度设置了且字段有必填宽度计算不正确
      if (a.required) {
        isComponetRequired = true;
      }
      // update-end--author:liaozhiyang---date:20230105---for：【TV360X-209】表单label长度设置了且字段有必填宽度计算不正确
      //update-begin-author:taoyan date:2022-8-5 for: 将他表字段的配置信息 添加至 关联字段上
      //关联记录字段设置新的配置
      if(a['view'] && a['view']==LINK_TABLE){
        if(tableLinkInfo[a.field]){
          a.setOtherInfo(tableLinkInfo[a.field])
        }
      }
      //update-end-author:taoyan date:2022-8-5 for: 将他表字段的配置信息 添加至 关联字段上
      //设置hidden的字段
      if (hideFields.indexOf(a.field) >= 0) {
        a.isHidden();
      }
      // popModal-下拉框的父级容器需要自定义，否则会被遮挡
      if(modalClass){
        a.setCustomPopContainer(modalClass)
      }
      // update-begin--author:liaozhiyang---date:20231222---for：【QQYUN-7515】online 下拉字典、单选组件options跟随数据库类型
      const result = a.getFormItemSchema();
      if (result.component === 'JDictSelectTag' && ['number', 'integer'].includes(a?._data?.type)) {
        result.componentProps.stringToNumber = true;
      }
      // update-end-author:liaozhiyang---date:20231222---for：【QQYUN-7515】online 下拉字典、单选组件options跟随数据库类型
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      if (props.formTemplate > 1 && getFieldExtend(a?._data, 'isOneRow')) {
        result.colProps = { span: 24 };
        const colGrid = getFormItemColProps();
        const { labelCol = {} } = colGrid;
        const itemLabelCol = {};
        const itemWrapperCol = {};
        Object.keys(labelCol).forEach((key) => {
          if (['xs', 'sm', 'md', 'lg', 'xl', 'xxl'].includes(key)) {
            const span = labelCol[key].span;
            const value = Math.round(span / props.formTemplate);
            itemLabelCol[key] = { span: value };
            itemWrapperCol[key] = { span: 24 - value - 1 };
          }
        });
        result.itemProps = { labelCol: itemLabelCol, wrapperCol: itemWrapperCol };
      }
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      // update-begin--author:liaozhiyang---date:20251011---for：【issues/8791】js增强popup弹框的onlChange()没生效
      if (result.component === 'JPopup') {
         result.changeEvent = 'popUpChange';
      }
      // update-end--author:liaozhiyang---date:20251011---for：【issues/8791】js增强popup弹框的onlChange()没生效
      formSchemaArray.push(result);
    }
    formSchemas.value = formSchemaArray;
    //return formSchemaArray;
    //update-begin-author:taoyan date:2022-5-31 for: VUEN-1147 主子表 子表顺序并不是按照设置顺序排列
    subInfo.sort(function (a, b) {
      return a.order - b.order;
    });
    //update-end-author:taoyan date:2022-5-31 for: VUEN-1147 主子表 子表顺序并不是按照设置顺序排列
    // update-begin--author:liaozhiyang---date:20231009---for：【issues/5371】一对多子表popup增加多选
    subInfo.forEach((sItem) => {
      const columns: any = sItem.columns;
      if(sItem.columns){
        columns.forEach((cItem) => {
          // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-452】一对多子表popup默认多选没生效
          if (sItem.relationType == 0) {
            if (['popup', 'popup_dict'].includes(cItem.type)) {
              // 只有1对多才需要处理，1对1或者单表直接组件中处理了。
              let popupMulti = true;
              if (cItem.fieldExtendJson) {
                const fieldExtendJson = JSON.parse(cItem.fieldExtendJson);
                popupMulti = fieldExtendJson.popupMulti;
              }
              const props = cItem.props ?? {};
              cItem.props = { ...props, multi: popupMulti };
            }
          }
          // update-end--author:liaozhiyang---date:20240529---for：【TV360X-452】一对多子表popup默认多选没生效
          // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
          if (cItem.type === 'date' && cItem.fieldExtendJson) {
            const fieldExtendJson = JSON.parse(cItem.fieldExtendJson);
            if (fieldExtendJson.picker && fieldExtendJson.picker != 'default') {
              Object.assign(cItem, { picker: fieldExtendJson.picker });
            }
          }
          // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
        });
      }
    });
    // update-end--author:liaozhiyang---date:20231009---for：【issues/5371】一对多子表popup增加多选
    subTabInfo.value = subInfo;
    subDataSource.value = dataSourceObj;
    // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    if (onlineExtConfigJson.formLabelLengthShow && onlineExtConfigJson.formLabelLength) {
      // 14是文字size，24是间隙
      labelWidth.value = onlineExtConfigJson.formLabelLength * 14 + 10 + (+`${isComponetRequired ? 13 : 0}`);
      wrapperCol.value = null;
    } else {
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // num这个值是label没截取之前真实长度，如果大于初始值则还是得使用初始值
      if (longestLabelComponet) {
        let realLabelLen = longestLabelComponet.label.length;
        realLabelLen = realLabelLen > LABELLENGTH ? LABELLENGTH : realLabelLen;
        const required = longestLabelComponet.required;
        const num = realLabelLen * 14 + 10 + (+`${required ? 13 : 0}`);
        labelWidth.value = num;
      }
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    }
    // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
  }
  
  watch(
    fieldDisplayStatus,
    (val) => {
      let ref = onlineFormRef.value;
      let arr: any[] = [];
      let map = toRaw(val);
      Object.keys(map).map((k) => {
        if (k.endsWith('_load')) {
        } else {
          let item = {
            field: k,
            show: map[k],
          };
          let loadKey = k + '_load';
          if (map.hasOwnProperty(loadKey)) {
            item['ifShow'] = map[loadKey];
          }
          // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-8537】js增强，控制表单字段的禁用
          let disabledKey = k + '_disabled';
          if (map.hasOwnProperty(disabledKey)) {
            item['dynamicDisabled'] = () => {
              return map[disabledKey];
            };
          }
          // update-end--author:liaozhiyang---date:20240321---for：【QQYUN-8537】js增强，控制表单字段的禁用
          arr.push(item);
        }
      });
      if (ref) {
        ref.updateSchema(arr);
      }
    },
    { immediate: false }
  );

  function dealSubProerties(subInfo) {
    useOnlineVxeTableColumns(subInfo, (column)=>{
      initSubTableDefValueConfig(column, defaultValueFields[subInfo.key]);
    })
  }
  /*
   2024-03-06
   liaozhiyang
   表单中的扩展参数的labelLength设置为onlineExtConfigJson.formLabelLength
  */
  function setFieldExtend(onlineExtConfigJson, data, key = 'labelLength') {
    const { formLabelLengthShow, formLabelLength } = onlineExtConfigJson;
    if (formLabelLengthShow && formLabelLength) {
      let fieldExtendJson = data?.fieldExtendJson;
      if (fieldExtendJson) {
        fieldExtendJson = JSON.parse(fieldExtendJson);
        fieldExtendJson[key] = formLabelLength;
      } else {
        fieldExtendJson = { [key]: formLabelLength };
      }
      data.fieldExtendJson = JSON.stringify(fieldExtendJson);
    } else if (data?.fieldExtendJson) {
      // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-34】查询label设置的长度应该只对查询区域的字符长度生效，不应该对表单长度生效
      const fieldExtendJson = JSON.parse(data?.fieldExtendJson);
      delete fieldExtendJson.labelLength;
      data.fieldExtendJson = JSON.stringify(fieldExtendJson);
      // update-end--author:liaozhiyang---date:20260714---for：【LHZP-34】查询label设置的长度应该只对查询区域的字符长度生效，不应该对表单长度生效
    }
  }

  /*
   2024-01-05
   liaozhiyang
   获取扩展参数
  */
  function getFieldExtend(data: any = {}, key: string) {
    let fieldExtendJson = data?.fieldExtendJson;
    if (fieldExtendJson) {
      fieldExtendJson = JSON.parse(fieldExtendJson);
      return fieldExtendJson[key];
    }
  }

  //监听主表表单的formTemplate
  watch(
    () => props.formTemplate,
    () => {
      //重新渲染表单
      const result = getFormItemColProps()
      baseColProps.value = result.baseColProps;
      labelCol.value = result.labelCol;
      wrapperCol.value = result.wrapperCol;
    },
    { immediate: true }
  );

  function getFormItemColProps() {
    let temp = props.formTemplate;
    // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    // update-begin--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
    // const form: any = componentSetting.form || {};
    // const { labelCol = {} } = form;
    // const { wrapperCol = {} } = form;
    // update-end--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
    if (temp == 2) {
      // update-begin--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
      return {
        baseColProps: { sm: 24, xs: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
        // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
        // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
        // wrapperCol: { xs: { span: 24 }, sm: { span: 19 }, md: { span: 19 }, lg: { span: 19 }, xl: { span: 19 }, xxl: { span: 19 } },
        // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      };
      // update-end--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
    } else if (temp == 3) {
      return {
        baseColProps: { sm: 24, xs: 24, md: 8, lg: 8, xl: 8, xxl: 8 },
        // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
        // labelCol: { xs: { span: 24 }, sm: { span: 6 }, md: { span: 6 }, lg: { span: 6 }, xl: { span: 6 }, xxl: { span: 6 } },
        // wrapperCol: { xs: { span: 24 }, sm: { span: 17 }, md: { span: 17 }, lg: { span: 17 }, xxl: { span: 17 } },
        // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      };
    } else if (temp == 4) {
      return {
        baseColProps: { sm: 24, xs: 24, md: 6, lg: 6, xl: 6, xxl: 6 },
        // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
        // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
        // wrapperCol: { xs: { span: 24 }, sm: { span: 18 }, md: { span: 18 }, lg: { span: 18 }, xl: { span: 18 }, xxl: { span: 18 } },
        // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      };
    } else {
      // update-begin--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
      return {
        baseColProps: { sm: 24, xs: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
        // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
        // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
        // wrapperCol:{ xs: { span: 24 }, sm: { span: 18 }, md: { span: 18 }, lg: { span: 18 }, xl: { span: 18 }, xxl: { span: 18 } },
        // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      };
      // update-end--author:liaozhiyang---date:20240311---for：【QQYUN-8440】小屏幕居中(跟vue2栅格同步)
    }
    // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
  }
  // 唯一校验
  function checkOnlyFieldValue(rule, value) {
    return new Promise((resolve) => {
      if (!value) {
        resolve('');
      }
      //对于视图 需要将表名后的$+数字 取掉
      let realTableName = tableName.value.replace(/\$\d+/, '');
      let param = {
        tableName: realTableName,
        fieldName: rule.field,
        fieldVal: value,
      };
      let formData: any = dbData.value;
      if (formData.id) {
        param['dataId'] = formData.id;
      }
      //console.log("唯一校验---》",param)
      duplicateCheck(param)
        .then((res) => {
          if (res.success) {
            resolve('');
          } else {
            resolve(res.message);
          }
        })
        .catch((msg) => {
          resolve(msg);
        });
    });
  }

  /**
   * 有些数据是数组格式的 强转成字符串
   */
  function changeDataIfArray2String(data) {
    Object.keys(data).map((k) => {
      if (data[k]) {
        if (data[k] instanceof Array) {
          data[k] = data[k].join(',');
        }
      }
    });
    return data;
  }

  return {
    formSchemas,
    defaultValueFields,
    tableName,
    dbData,
    checkOnlyFieldValue,
    createFormSchemas,
    fieldDisplayStatus,
    subTabInfo,
    hasSubTable,
    subDataSource,
    baseColProps,
    changeDataIfArray2String,
    linkDownList,
    refMap: refMap,
    labelCol,
    wrapperCol,
    labelWidth,
  };
}

/**
 * 处理online子表 jvxeTable的列配置信息
 */
export function useOnlineVxeTableColumns(subInfo, callback?){
  const uploadColumnMinWidth = 180;
  const uploadColumnTypes = ['image', 'file'];
  // 新旧jvxetable 列的类型不一致
  const vxeTypeMap = {
    inputNumber: 'input-number',
    sel_depart: 'depart-select',
    sel_user: 'user-select',
    list_multi: 'select-multiple',
    input_pop: 'textarea',
    sel_search: 'select-search',
    'select-dict-search': 'selectDictSearch',
  };
  
  //一对多子表如果为单选按钮改为下拉框
  subInfo.columns.forEach((column) => {
    if (column.type === 'radio') {
      column.type = 'select';
    } else if (vxeTypeMap[column.type]) {
      const originalType = column.type;
      column.type = vxeTypeMap[originalType];
      // Online数字字段的小数位数传递给JVxeTable，确保行编辑与弹窗编辑的精度处理一致
      if (originalType === 'inputNumber' && typeof column.dbPointLength === 'number') {
        column.props = {
          ...(column.props ?? {}),
          precision: column.dbPointLength,
        };
      }
    } else if (column.type === 'popup') {
      handleSubPopup(column);
    } else if (column.type === 'popup_dict') {
      // update-begin--author:liaozhiyang---date:20260507---for：【issues/7617】vxetable子表支持popup字典
      const { dictTable, dictCode, dictText } = column;
      column.fieldConfig = `${dictTable},${dictText ?? ''},${dictCode ?? ''}`;
      column.type = 'popupDict';
      column.multi = true;
      const str = column.fieldExtendJson;
      if (str) {
        try {
          const json = JSON.parse(str);
          if (json.multiSelect === false) {
            column.multi = false;
          }
        } catch (e) {
          console.log('子表获取popupdict的扩展配置出现错误', e);
        }
      }
      // update-end--author:liaozhiyang---date:20260507---for：【issues/7617】vxetable子表支持popup字典
    } else if (column.type === 'link_table') {
      handleSubLinkTable(column, subInfo.columns);
    } else if (column.type === 'link_table_field') {
      // update-begin--author:liaozhiyang---date:20260317---for:【QQYUN-9441】online一对多加上关联记录和他表字段
      column.type = 'input';
      column.flag = 'link-table-field';
      column.props = {
        ...(column.props ?? {}),
        disabled: true,
      };
      // update-end--author:liaozhiyang---date:20260317---for:【QQYUN-9441】online一对多加上关联记录和他表字段
    }
    // 部门树选择组件需要设置 父子节点不关联
    if (column.type === 'depart-select') {
      column['checkStrictly'] = true;
    }
    // 子表用户选择 控制是否多选
    if (column.type === 'user-select') {
      handleSubUserSelect(column);
    }
    if (column.type === 'pca') {
      column.width = '230px';
    }
    // update-begin--author:liaozhiyang---date:20260413---for:【issues/7633】online子表支持分类字典树，自定义树
    // 自定义树
    if (column.type === 'sel_tree') {
      const { dictTable, dictCode, dictText } = column;
      const [id, pid, name, child] = dictText.split(',');
      column.type = 'sel-tree';
      column.dict = `${dictTable},${name},${id}`;
      column.pidField = pid;
      column.pidValue = dictCode ?? '0';
      column.hasChildField = child;
      delete column.dictText;
      delete column.dictCode;
      delete column.dictTable;
    }
    // 分类字典书
    if (column.type === 'cat_tree') {
       const { dictCode } = column;
      column.type = 'cat-tree';
      column.pcode = dictCode ?? '0';
      delete column.dictCode;
    }
    // update-end--author:liaozhiyang---date:20260413---for:【issues/7633】online子表支持分类字典树，自定义树
    //update-begin-author:taoyan date:2022-4-24 for: VUEN-855 对多子表 文件、图片右侧少个边框
    // 图片、附件列需要为上传图标、按钮文字和单元格内边距预留空间，避免内容被裁剪
    const columnWidth = Number.parseFloat(column.width);
    if (uploadColumnTypes.includes(column.type) && columnWidth > 0 && columnWidth < uploadColumnMinWidth) {
      column.width = `${uploadColumnMinWidth}px`;
    }
    //如果没有宽度 默认设置一个宽度
    if (!column.width) {
      column.width = '200px';
    }
    if(callback){
      callback(column)
    }
    //update-end-author:taoyan date:2022-4-24 for: VUEN-855 对多子表 文件、图片右侧少个边框
  });

  // 子表popup特殊处理
  function handleSubPopup(column) {
    let { destFields, orgFields } = column;
    let fieldConfig: any[] = [];
    if (!destFields || destFields.length == 0) {
    } else {
      let arr1 = destFields.split(',');
      let arr2 = orgFields.split(',');
      for (let i = 0; i < arr1.length; i++) {
        fieldConfig.push({
          target: arr1[i],
          source: arr2[i],
        });
      }
    }
    column.fieldConfig = fieldConfig;
  }

  // 子表 用户选择特殊处理
  function handleSubUserSelect(column) {
    let str = column.fieldExtendJson;
    let isRadioSelection = false;
    if (str) {
      try {
        let json = JSON.parse(str);
        if (json.multiSelect === false) {
          isRadioSelection = true;
        }
      } catch (e) {
        console.log('子表获取用户组件的扩展配置出现错误', e);
      }
    }
    column.isRadioSelection = isRadioSelection;
  }
  /**
   * 20260317
   * 【QQYUN-9441】online一对多加上关联记录和他表字段
   * liaozhiang
   * */
  function handleSubLinkTable(column, columns) {
    column.type = 'link-table';
    column.tableName = column.dictTable || '';
    column.valueField = column.dictCode || 'id';
    column.textField = column.dictText || '';
    column.multi = false;
    column.linkFields = [];
    columns.forEach(item => {
      if (item.type === 'link_table_field' && item.dictTable === column.key) {
        column.linkFields.push(`${item.key},${item.dictText}`);
      }
    });
    delete column.dictTable;
    delete column.dictCode;
    delete column.dictText;
    let str = column.fieldExtendJson;
    if (str) {
      try {
        let json = JSON.parse(str);
        if (json.multiSelect === true) {
          column.multi = true;
        }
      } catch (e) {
        console.log('子表获取关联记录组件的扩展配置出现错误', e);
      }
    }
    if (!column.width || column.width === '200px') {
      column.width = '240px';
    }
  }
  
}

/***
 * 表单上下文
 */
export function useOnlineFormContext(props) {
  let that = {};
  const CONTEXT_DESCRIPTION = {
      addSubRows: '<m> 一对多子表，新增自定义行',
      changeOptions: '<m> 改变下拉框选项',
      clearSubRows: '<m> 清空一对多子表行',
      clearThenAddRows: '<m> 清空一对多子表行，然后新增自定义行',
      executeMainFillRule: '<m> 刷新主表的增值规制值',
      executeSubFillRule: '<m> 刷新子表的增值规制值',
      getFieldsValue: '<m> 获取表单控件的值',
      getSubTableInstance: '<m> 获取子表实例',
      isUpdate: '<p> 判断是否为编辑模式',
      loading: '<p> 页面加载状态',
      onlineFormRef: '<p> 当前表单ref对象',
      refMap: '<p> 子表ref对象map',
      setFieldsValue: '<m> 设置表单控件的值',
      sh: '<p> 表单控件的显示隐藏状态',
      subActiveKey: '<p> 子表激活tab，对应子表表名',
      subFormHeight: '<p> 一对一子表表单高度',
      submitFlowFlag: '<p> 是否提交流程状态',
      subTableHeight: '<p> 一对多子表表格高度',
      tableName: '<p> 当前表名',
      triggleChangeValues: '<m> 修改多个表单值',
      triggleChangeValue: '<m> 修改表单值',
      updateSchema: '<m> 修改表单控件配置',
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      changeSubTableOptions: '<m> 改变一对多子表下拉框选项',
      changeSubFormbleOptions: '<m> 改变一对一子表下拉框选项',
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options
      changeRemoteOptions:'<m> 改变远程下拉框选项',
     // update-end--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options
     // update-begin--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
     submitFormAndFlow: '<m> 提交表单且发起流程',
    // update-end--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
  };
  const onlineFormContext = new Proxy(CONTEXT_DESCRIPTION, {
    get(_target: any, prop: string): any {
      return Reflect.get(that, prop);
    },
  });

  function addObject2Context(prop, object) {
    that[prop] = object;
  }

  function resetContext(context) {
    Object.keys(context).map((k) => {
      that[k] = context[k];
    });
  }
  addObject2Context('$nextTick', nextTick);
  addObject2Context('addObject2Context', addObject2Context);

  // 自定义按钮
  const createBIButtonCfg = (btnKey, defBtn) => computed(() => {
    const {buttonSwitch} = props
    const cfg = {
      enabled: true,
      buttonIcon: defBtn[0],
      buttonName: defBtn[1],
    }
    if (buttonSwitch?.[btnKey] === false) {
      cfg.enabled = false
      return cfg
    }
    const {cgBIBtnMap} = props
    return cgBIBtnMap?.[btnKey] ? cgBIBtnMap[btnKey] : cfg
  })

  const getSubAddBtnCfg = createBIButtonCfg('form_sub_add', ['ant-design:plus-outlined', '新增'])
  const getSubRemoveBtnCfg = createBIButtonCfg('form_sub_batch_delete', ['ant-design:minus-outlined', '删除'])
  const getSubOpenAddBtnCfg = createBIButtonCfg('form_sub_open_add', ['ant-design:form-outlined', '新增'])
  const getSubOpenEditBtnCfg = createBIButtonCfg('form_sub_open_edit', ['ant-design:form-outlined', '弹窗编辑'])

  return {
    onlineFormContext,
    addObject2Context,
    resetContext,

    getSubAddBtnCfg,
    getSubRemoveBtnCfg,
    getSubOpenAddBtnCfg,
    getSubOpenEditBtnCfg,
  };
}

/**
 * 找联动组件
 * @param properties
 */
export function handleLinkDown(item, field) {
  const {
    config: { table, key, txt, linkField, idField, pidField, condition },
    others,
    order,
    title,
  } = item;
  let commonProp = {
    dictTable: table,
    dictText: txt,
    dictCode: key,
    pidField: pidField,
    idField: idField,
    view: LINK_DOWN,
    type: item.type,
  };
  let array: any = [];
  let main = {
    key: field,
    title,
    order,
    condition,
    origin: true,
    ...commonProp,
  };

  if (linkField && linkField.length > 0) {
    let fields = linkField.split(',');
    main['next'] = fields[0];
    for (let i = 0; i < fields.length; i++) {
      for (let o of others) {
        if (o.field == fields[i]) {
          let temp = {
            key: o.field,
            title: o.title,
            order: o.order,
            origin: false,
            ...commonProp,
          };
          if (i + 1 < fields.length) {
            temp['next'] = fields[i + 1];
          }
          array.push(temp);
        }
      }
    }
  }
  array.push(main);
  //let ls = linkDownList.value
  // ls.push(...array)
  // linkDownList.value = ls;
  return array;
}

/**
 * 获取 字段的索引
 * @param arr
 * @param key
 */
export function getFieldIndex(arr: IFormSchema[], key: string) {
  let index = -1;
  for (let i = 0; i < arr.length; i++) {
    let item = arr[i];
    if (item.field === key) {
      index = i;
      break;
    }
  }
  return index;
}

/**
 * 轮询获取 ref 对象的值，获取为true或是真实存在 就执行下一步逻辑，可用于判断状态或组件的加载是否完成
 * @param componentRef
 */
export function getRefPromise(componentRef) {
  return new Promise((resolve) => {
    (function next() {
      let ref = componentRef.value;
      if (ref) {
        resolve(ref);
      } else {
        setTimeout(() => {
          next();
        }, 100);
      }
    })();
  });
}

function clearObj(obj) {
  Object.keys(obj).map((k) => {
    delete obj[k];
  });
}

//update-begin-author:taoyan date:2022-6-1 for:  VUEN-1162 子表按钮没控制
/**
 * 记录子表按钮权限-隐藏的按钮编码
 */
const permissionStore = usePermissionStore();
function handleSubTableButtonAuth(tableName, item) {
  let arr = item.hideButtons;
  let code = ONL_AUTH_PRE + tableName + ':';
  if (!arr) {
    arr = [];
  }
  permissionStore.setOnlineSubTableAuth(code, arr);
}
//update-end-author:taoyan date:2022-6-1 for:  VUEN-1162 子表按钮没控制



/**
 * 获取 DetailFormSchema[online用]
 */
export function getDetailFormSchemas(props) {
  const detailFormSchemas = ref<DetailFormSchema[]>([]);
  const refMap = {};
  const showStatus = reactive({
    
  });
  const hasSubTable = ref(false);
  const subTabInfo = ref<OnlSubTab[]>([]);
  const subDataSource = ref({});
  const { getIsMobile } = useAppInject();
  const formSpan = computed(() => {
    let temp = props.formTemplate;
    // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-82】详情页移动端只显示一列
    if (getIsMobile.value) {
      return 24;
    }
    // update-end--author:liaozhiyang---date:20240522---for：【TV360X-82】详情页移动端只显示一列
    if (temp == '2') {
      return 12;
    } else if (temp == '3') {
      return 8;
    } else if (temp == '4') {
      return 6;
    } else {
      return 24;
    }
  });

  function createFormSchemas(properties: any[]) {
    //let properties:any[] = result.schema.properties
    let subInfo: OnlSubTab[] = [];
    console.log('111', properties);
    let arr: DetailFormSchema[] = [];
    let dataSourceObj = {};
    // update-begin--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
    const tableLinkInfo: any = {};
    // update-end--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
    Object.keys(properties).map((key) => {
      const item = properties[key];
      // uiSchema 无用
      //const uiItem = this.uiSchema[key];// method、formTemplate、url
      if (item.view == 'tab') {
        hasSubTable.value = true;
        let temp: OnlSubTab = {
          key,
          formTemplate: Number(item.formTemplate || 1),
          // 这个foreignKey是主表的字段
          foreignKey: item['foreignKey'],
          describe: item.describe,
          relationType: item.relationType,
          requiredFields: item.required || [],
          order: item.order,
        };
        if (item.relationType == 1) {
          refMap[key] = ref(null);
          temp['properties'] = item.properties;
        } else {
          dealSubProerties(item);
          refMap[key] = ref<JVxeTableInstance>();
          temp['columns'] = item.columns;
          dataSourceObj[key] = [];
          showStatus[key] = false;
        }
        subInfo.push(temp);
      } else {
        if (item.view === LINK_DOWN) {
          let array = handleLinkDown(item, key);
          for (let linkDownItem of array) {
            let tempIndex = getFieldIndex(arr, linkDownItem.key);
            let temp = {
              field: linkDownItem.key,
              label: linkDownItem.title,
              view: linkDownItem.view,
              order: linkDownItem.order,
              dictTable: linkDownItem.dictTable,
              linkField: linkDownItem.linkField||'',
            };
            if (tempIndex == -1) {
              arr.push(temp);
            } else {
              arr[tempIndex] = temp;
            }
          }
        } else if (item.view == 'hidden') {
          //隐藏的不处理
        } else {
          let tempIndex = getFieldIndex(arr, key);
          if (tempIndex == -1) {
            // update-begin--author:liusq---date:20261102---for：online树表详情顶级节点的父节点显示0，带上pidComponent/pidValue用于识别树表父节点字段
            let temp = Object.assign(
              {
                field: key,
                label: item.title,
              },
              pick(item, ['view', 'order', 'fieldExtendJson', 'dictTable', 'dictText', 'dictCode', 'dict', 'pidComponent', 'pidValue'])
            );
            // update-end--author:liusq---date:20261102---for：online树表详情顶级节点的父节点显示0，带上pidComponent/pidValue用于识别树表父节点字段
            if (item.view == 'file') {
              temp['span'] = 24;
              temp['isFile'] = true;
            }
            if (item.view == 'image') {
              temp['span'] = 24;
              temp['isImage'] = true;
            }
            if (item.view == 'link_table') {
              // 判断是不是卡片
              if(item.fieldExtendJson){
                try{
                  let json = JSON.parse(item.fieldExtendJson);
                  if(json.showType!='select'){
                   // temp['span'] = 24;
                    temp['isCard'] = true;
                  }
                  if(json.multiSelect==true){
                    temp['multi'] = true;
                  }
                  // update-begin--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
                  if(json.imageField){
                    temp['imageField'] = json.imageField;
                  }
                  // update-end--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
                }catch (e) {
                  console.error('解析json字符串出错', item.fieldExtendJson)
                }
              }
            }
            if (item.view == 'umeditor' || item.view == 'markdown') {
              temp['isHtml'] = true;
              temp['span'] = 24;
            }
            arr.push(temp);
            // update-begin--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
            if (item.view === 'link_table_field') {
              if (!tableLinkInfo[item.dictTable]) {
                tableLinkInfo[item.dictTable] = [];
              }
              tableLinkInfo[item.dictTable].push(`${key},${item.dictText}`);
            }
            // update-end--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
          }
        }
      }
    });
    // 1.对arr排序
    arr.sort(function (a, b) {
      return a.order - b.order;
    });
    // update-begin--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
    arr.forEach((item) => {
      if (item.view === 'link_table' && tableLinkInfo[item.field]) {
        item['linkFields'] = tableLinkInfo[item.field];
      }
    });
    // update-end--author:liaozhiyang---date:20260413---for：【QQYUN-14951】一对一他表字段详情没值
    // 2.对子表排序
    subInfo.sort(function (a, b) {
      return a.order - b.order;
    });
    subTabInfo.value = subInfo;
    for (let i = 0; i < arr.length; i++) {
      let temp = arr[i];
      if (temp.isFile === true || temp.isImage === true || temp.isHtml === true) {
        if (i > 0) {
          let last = arr[i - 1];
          let span = last.span || formSpan.value;
          last.span = span;
        }
      }
    }
    detailFormSchemas.value = arr;
    subDataSource.value = dataSourceObj;
    console.log('adadad', arr);
  }

  function dealSubProerties(subInfo) {
    useOnlineVxeTableColumns(subInfo);
  }

  function getFieldIndex(arr: DetailFormSchema[], key: string) {
    let index = -1;
    for (let i = 0; i < arr.length; i++) {
      let item = arr[i];
      if (item.field === key) {
        index = i;
        break;
      }
    }
    return index;
  }

  function handleLinkDown(item, field){
    let all:any[] = []
    const {
      config: { table, key, txt, linkField },
      order,
      title,
      others,
    } = item;
    let obj = {
      table, key, txt
    }
    let temp = {
      view: 'link_down',
      order,
      title,
      dictTable: JSON.stringify(obj)
    };
    all.push(Object.assign({}, {linkField, key: field}, temp));
    if(linkField){
      let arr = linkField.split(',');
      for(let a of arr){
        let title = ''
        for(let o of others){
          if(o.field==a){
            title = o.title
          }
        }
        all.push(Object.assign({}, {key: a}, temp, {title}));
      }
    }
    return all;
  }
  return {
    detailFormSchemas,
    hasSubTable,
    subTabInfo,
    refMap,
    showStatus,
    createFormSchemas,
    formSpan,
    subDataSource,
  };
}
