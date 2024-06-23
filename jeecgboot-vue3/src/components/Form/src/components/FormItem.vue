<script lang="tsx">
  import { NamePath, ValidateOptions } from 'ant-design-vue/lib/form/interface';
  import type { PropType, Ref } from 'vue';
  import type { FormActionType, FormProps } from '../types/form';
  import type { FormSchema } from '../types/form';
  import type { ValidationRule } from 'ant-design-vue/lib/form/Form';
  import type { TableActionType } from '/@/components/Table';
  import { defineComponent, computed, unref, toRefs } from 'vue';
  import { Form, Col, Divider } from 'ant-design-vue';
  import { componentMap } from '../componentMap';
  import { BasicHelp } from '/@/components/Basic';
  import { isBoolean, isFunction, isNull } from '/@/utils/is';
  import { getSlot } from '/@/utils/helper/tsxHelper';
  import { createPlaceholderMessage, setComponentRuleType } from '../helper';
  import { upperFirst, cloneDeep } from 'lodash-es';
  import { useItemLabelWidth } from '../hooks/useLabelWidth';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { usePermission } from '/@/hooks/web/usePermission';
  import Middleware from './Middleware.vue';
  export default defineComponent({
    name: 'BasicFormItem',
    inheritAttrs: false,
    props: {
      schema: {
        type: Object as PropType<FormSchema>,
        default: () => ({}),
      },
      formProps: {
        type: Object as PropType<FormProps>,
        default: () => ({}),
      },
      allDefaultValues: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      formModel: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      setFormModel: {
        type: Function as PropType<(key: string, value: any) => void>,
        default: null,
      },
      validateFields: {
        type: Function as PropType<(nameList?: NamePath[] | undefined, options?: ValidateOptions) => Promise<any>>,
        default: null,
      },
      tableAction: {
        type: Object as PropType<TableActionType>,
      },
      formActionType: {
        type: Object as PropType<FormActionType>,
      },
      // update-begin--author:liaozhiyang---date:20240605---for：【TV360X-857】解决禁用状态下触发校验
      clearValidate: {
        type: Function,
        default: null,
      },
      // update-end-author:liaozhiyang---date:20240605---for：【TV360X-857】解决禁用状态下触发校验
    },
    setup(props, { slots }) {
      const { t } = useI18n();

      const { schema, formProps } = toRefs(props) as {
        schema: Ref<FormSchema>;
        formProps: Ref<FormProps>;
      };

      const itemLabelWidthProp = useItemLabelWidth(schema, formProps);

      const getValues = computed(() => {
        const { allDefaultValues, formModel, schema } = props;
        const { mergeDynamicData } = props.formProps;
        return {
          field: schema.field,
          model: formModel,
          values: {
            ...mergeDynamicData,
            ...allDefaultValues,
            ...formModel,
          } as Recordable,
          schema: schema,
        };
      });

      const getComponentsProps = computed(() => {
        const { schema, tableAction, formModel, formActionType } = props;
        let { componentProps = {} } = schema;
        if (isFunction(componentProps)) {
          componentProps = componentProps({ schema, tableAction, formModel, formActionType }) ?? {};
        }
        if (schema.component === 'Divider') {
          //update-begin---author:wangshuai---date:2023-09-22---for:【QQYUN-6603】分割线标题位置显示不正确---
          componentProps = Object.assign({ type: 'horizontal',orientation:'left', plain: true, }, componentProps);
          //update-end---author:wangshuai---date:2023-09-22---for:【QQYUN-6603】分割线标题位置显示不正确---
        }
        return componentProps as Recordable;
      });

      const getDisable = computed(() => {
        const { disabled: globDisabled } = props.formProps;
        // update-begin--author:liaozhiyang---date:20240530---for：【TV360X-594】表单全局禁用则dynamicDisabled不生效
        if (!!globDisabled) {
          return globDisabled;
        }
        // update-end--author:liaozhiyang---date:20240530---for：【TV360X-594】表单全局禁用则dynamicDisabled不生效
        const { dynamicDisabled } = props.schema;
        const { disabled: itemDisabled = false } = unref(getComponentsProps);
        let disabled = !!globDisabled || itemDisabled;
        if (isBoolean(dynamicDisabled)) {
          disabled = dynamicDisabled;
        }
        if (isFunction(dynamicDisabled)) {
          disabled = dynamicDisabled(unref(getValues));
        }
        return disabled;
      });

      // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改
      const getDynamicPropsValue = computed(() => {
        const { dynamicPropsVal, dynamicPropskey } = props.schema;
        if (dynamicPropskey == null) {
          return null;
        } else {
          const { [dynamicPropskey]: itemValue } = unref(getComponentsProps);
          let value = itemValue;
          if (isFunction(dynamicPropsVal)) {
            value = dynamicPropsVal(unref(getValues));
            return value;
          }
        }
      });
      // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改

      function getShow(): { isShow: boolean; isIfShow: boolean } {
        const { show, ifShow } = props.schema;
        const { showAdvancedButton } = props.formProps;
        const itemIsAdvanced = showAdvancedButton ? (isBoolean(props.schema.isAdvanced) ? props.schema.isAdvanced : true) : true;

        let isShow = true;
        let isIfShow = true;

        if (isBoolean(show)) {
          isShow = show;
        }
        if (isBoolean(ifShow)) {
          isIfShow = ifShow;
        }
        if (isFunction(show)) {
          isShow = show(unref(getValues));
        }
        if (isFunction(ifShow)) {
          isIfShow = ifShow(unref(getValues));
        }
        isShow = isShow && itemIsAdvanced;
        return { isShow, isIfShow };
      }
      // update-begin--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
      let vSwitchArr: any = [],
        prevValidatorArr: any = [];
      const hijackValidator = (rules) => {
        vSwitchArr = [];
        prevValidatorArr = [];
        rules.forEach((item, index) => {
          const fn = item.validator;
          vSwitchArr.push(true);
          prevValidatorArr.push(null);
          if (isFunction(fn)) {
            item.validator = (rule, value, callback) => {
              if (vSwitchArr[index]) {
                vSwitchArr[index] = false;
                setTimeout(() => {
                  vSwitchArr[index] = true;
                }, 100);
                const result = fn(rule, value, callback);
                prevValidatorArr[index] = result;
                return result;
              } else {
                return prevValidatorArr[index];
              }
            };
          }
        });
      };
      // update-end--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
      function handleRules(): ValidationRule[] {
        const { rules: defRules = [], component, rulesMessageJoinLabel, label, dynamicRules, required, auth, field } = props.schema;
        // update-begin--author:liaozhiyang---date:20240605---for：【TV360X-857】解决禁用状态下触发校验
        const { disabled: globDisabled } = props.formProps;
        const { disabled: itemDisabled = false } = unref(getComponentsProps);
        if (!!globDisabled || !!itemDisabled) {
          props.clearValidate(field);
          return [];
        }
        // update-end--author:liaozhiyang---date:20240605---for：【TV360X-857】解决禁用状态下触发校验
        // update-begin--author:liaozhiyang---date:20240531---for：【TV360X-842】必填项v-auth、show隐藏的情况下表单无法提交
        const { hasPermission } = usePermission();
        const { isShow } = getShow();
        if ((auth && !hasPermission(auth)) || !isShow) {
          return [];
        }
        // update-end--author:liaozhiyang---date:20240531---for：【TV360X-842】必填项v-auth、show隐藏的情况下表单无法提交
        if (isFunction(dynamicRules)) {
          // update-begin--author:liaozhiyang---date:20240514---for：【issues/1244】标识了必填，但是必填标识没显示
          const ruleArr = dynamicRules(unref(getValues)) as ValidationRule[];
          if (required) {
            ruleArr.unshift({ required: true });
          }
          // update-begin--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
          hijackValidator(ruleArr);
          // update-end--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
          return ruleArr;
          // update-end--author:liaozhiyang---date:20240514---for：【issues/1244】标识了必填，但是必填标识没显示
        }

        let rules: ValidationRule[] = cloneDeep(defRules) as ValidationRule[];
        const { rulesMessageJoinLabel: globalRulesMessageJoinLabel } = props.formProps;

        const joinLabel = Reflect.has(props.schema, 'rulesMessageJoinLabel') ? rulesMessageJoinLabel : globalRulesMessageJoinLabel;
        const defaultMsg = createPlaceholderMessage(component) + `${joinLabel ? label : ''}`;

        function validator(rule: any, value: any) {
          const msg = rule.message || defaultMsg;
          if (value === undefined || isNull(value)) {
            // 空值
            return Promise.reject(msg);
          } else if (Array.isArray(value) && value.length === 0) {
            // 数组类型
            return Promise.reject(msg);
          } else if (typeof value === 'string' && value.trim() === '') {
            // 空字符串
            return Promise.reject(msg);
          } else if (
            typeof value === 'object' &&
            Reflect.has(value, 'checked') &&
            Reflect.has(value, 'halfChecked') &&
            Array.isArray(value.checked) &&
            Array.isArray(value.halfChecked) &&
            value.checked.length === 0 &&
            value.halfChecked.length === 0
          ) {
            // 非关联选择的tree组件
            return Promise.reject(msg);
          }
          return Promise.resolve();
        }

        const getRequired = isFunction(required) ? required(unref(getValues)) : required;

        if ((!rules || rules.length === 0) && getRequired) {
          rules = [{ required: getRequired, validator }];
        }

        const requiredRuleIndex: number = rules.findIndex((rule) => Reflect.has(rule, 'required') && !Reflect.has(rule, 'validator'));

        if (requiredRuleIndex !== -1) {
          const rule = rules[requiredRuleIndex];
          const { isShow } = getShow();
          if (!isShow) {
            rule.required = false;
          }
          if (component) {
            //update-begin---author:wangshuai---date:2024-02-01---for:【QQYUN-8176】编辑表单中,校验必填时,如果组件是ApiSelect,打开编辑页面时,即使该字段有值,也会提示请选择---
            //https://github.com/vbenjs/vue-vben-admin/pull/3082 github修复原文
            /*if (!Reflect.has(rule, 'type')) {
              rule.type = component === 'InputNumber' ? 'number' : 'string';
            }*/
            //update-end---author:wangshuai---date:2024-02-01---for:【QQYUN-8176】编辑表单中,校验必填时,如果组件是ApiSelect,打开编辑页面时,即使该字段有值,也会提示请选择---

            rule.message = rule.message || defaultMsg;

            if (component.includes('Input') || component.includes('Textarea')) {
              rule.whitespace = true;
            }
            const valueFormat = unref(getComponentsProps)?.valueFormat;
            setComponentRuleType(rule, component, valueFormat);
          }
        }

        // Maximum input length rule check
        const characterInx = rules.findIndex((val) => val.max);
        if (characterInx !== -1 && !rules[characterInx].validator) {
          rules[characterInx].message = rules[characterInx].message || t('component.form.maxTip', [rules[characterInx].max] as Recordable);
        }
        // update-begin--author:liaozhiyang---date:20241226---for：【QQYUN-7495】pattern由字符串改成正则传递给antd（因使用InputNumber时发现正则无效）
        rules.forEach((item) => {
          if (typeof item.pattern === 'string') {
            try {
              const reg = new Function('item', `return ${item.pattern}`)(item);
              if (Object.prototype.toString.call(reg) === '[object RegExp]') {
                item.pattern = reg;
              } else {
                item.pattern = new RegExp(item.pattern);
              }
            } catch (error) {
              item.pattern = new RegExp(item.pattern);
            }
          }
        });
        // update-end--author:liaozhiyang---date:20231226---for：【QQYUN-7495】pattern由字符串改成正则传递给antd（因使用InputNumber时发现正则无效）
        // update-begin--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
        hijackValidator(rules);
        // update-end--author:liaozhiyang---date:20240530---for：【TV360X-434】validator校验执行两次
        return rules;
      }

      function renderComponent() {
        const { renderComponentContent, component, field, changeEvent = 'change', valueField, componentProps, dynamicRules } = props.schema;

        const isCheck = component && ['Switch', 'Checkbox'].includes(component);
        // update-begin--author:liaozhiyang---date:20231013---for：【QQYUN-6679】input去空格
        let isTrim = false;
        if (component === 'Input' && componentProps && componentProps.trim) {
          isTrim = true;
        }
        // update-end--author:liaozhiyang---date:20231013---for：【QQYUN-6679】input去空格
        const eventKey = `on${upperFirst(changeEvent)}`;
        // update-begin--author:liaozhiyang---date:20230922---for：【issues/752】表单校验dynamicRules 无法 使用失去焦点后校验 trigger: 'blur'
        const on = {
          [eventKey]: (...args: Nullable<Recordable>[]) => {
            const [e] = args;
            if (propsData[eventKey]) {
              propsData[eventKey](...args);
            }
            const target = e ? e.target : null;
            // update-begin--author:liaozhiyang---date:20231013---for：【QQYUN-6679】input去空格
            let value;
            if (target) {
              if (isCheck) {
                value = target.checked;
              } else {
                value = isTrim ? target.value.trim() : target.value;
              }
            } else {
              value = e;
            }
            // update-end--author:liaozhiyang---date:20231013---for：【QQYUN-6679】input去空格
            props.setFormModel(field, value);
            // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-341】有值之后必填校验不消失
            props.validateFields([field]).catch((_) => {});
            // update-end--author:liaozhiyang---date:20240522--for：【TV360X-341】有值之后必填校验不消失
          },
          // onBlur: () => {
          //   props.validateFields([field], { triggerName: 'blur' }).catch((_) => {});
          // },
        };
        // update-end--author:liaozhiyang---date:20230922---for：【issues/752】表单校验dynamicRules 无法 使用失去焦点后校验 trigger: 'blur'
        const Comp = componentMap.get(component) as ReturnType<typeof defineComponent>;

        const { autoSetPlaceHolder, size } = props.formProps;
        const propsData: Recordable = {
          allowClear: true,
          getPopupContainer: (trigger: Element) => {

            return trigger?.parentNode;
          },
          size,
          ...unref(getComponentsProps),
          disabled: unref(getDisable),
        };
        // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改
        const dynamicPropskey = props.schema.dynamicPropskey;
        if (dynamicPropskey) {
          propsData[dynamicPropskey] = unref(getDynamicPropsValue);
        }
        // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改

        const isCreatePlaceholder = !propsData.disabled && autoSetPlaceHolder;
        // RangePicker place是一个数组
        if (isCreatePlaceholder && component !== 'RangePicker' && component) {
          //自动设置placeholder
          propsData.placeholder = unref(getComponentsProps)?.placeholder || createPlaceholderMessage(component) + props.schema.label;
        }
        propsData.codeField = field;
        propsData.formValues = unref(getValues);

        const bindValue: Recordable = {
          [valueField || (isCheck ? 'checked' : 'value')]: props.formModel[field],
        };

        const compAttr: Recordable = {
          ...propsData,
          ...on,
          ...bindValue,
        };

        if (!renderComponentContent) {
          return <Comp {...compAttr} />;
        }
        const compSlot = isFunction(renderComponentContent)
          ? { ...renderComponentContent(unref(getValues)) }
          : {
              default: () => renderComponentContent,
            };
        return <Comp {...compAttr}>{compSlot}</Comp>;
      }

      /**
       *渲染Label
       * @updateBy:zyf
       */
      function renderLabelHelpMessage() {
        //update-begin-author:taoyan date:2022-9-7 for: VUEN-2061【样式】online表单超出4个 .. 省略显示
        //label宽度支持自定义
        const { label, helpMessage, helpComponentProps, subLabel, labelLength } = props.schema;
        let showLabel: string = label + '';
        // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-98】label展示的文字必须和labelLength配置一致
        if (labelLength) {
          showLabel = showLabel.substr(0, labelLength);
        }
        // update-end--author:liaozhiyang---date:20240517---for：【TV360X-98】label展示的文字必须和labelLength配置一致
        const titleObj = { title: label };
        const renderLabel = subLabel ? (
          <span>
            {label} <span class="text-secondary">{subLabel}</span>
          </span>
        ) : labelLength ? (
          <label {...titleObj}>{showLabel}</label>
        ) : (
          label
        );
        //update-end-author:taoyan date:2022-9-7 for: VUEN-2061【样式】online表单超出4个 .. 省略显示
        const getHelpMessage = isFunction(helpMessage) ? helpMessage(unref(getValues)) : helpMessage;
        if (!getHelpMessage || (Array.isArray(getHelpMessage) && getHelpMessage.length === 0)) {
          return renderLabel;
        }
        return (
          <span>
            {renderLabel}
            <BasicHelp placement="top" class="mx-1" text={getHelpMessage} {...helpComponentProps} />
          </span>
        );
      }

      function renderItem() {
        const { itemProps, slot, render, field, suffix, component } = props.schema;
        const { labelCol, wrapperCol } = unref(itemLabelWidthProp);
        const { colon } = props.formProps;

        if (component === 'Divider') {
          return (
            <Col span={24}>
              <Divider {...unref(getComponentsProps)}>{renderLabelHelpMessage()}</Divider>
            </Col>
          );
        } else {
          const getContent = () => {
            return slot ? getSlot(slots, slot, unref(getValues)) : render ? render(unref(getValues)) : renderComponent();
          };

          const showSuffix = !!suffix;
          const getSuffix = isFunction(suffix) ? suffix(unref(getValues)) : suffix;

          return (
            <Form.Item
              name={field}
              colon={colon}
              class={{ 'suffix-item': showSuffix }}
              {...(itemProps as Recordable)}
              label={renderLabelHelpMessage()}
              rules={handleRules()}
              // update-begin--author:liaozhiyang---date:20240514---for：【issues/1244】标识了必填，但是必填标识没显示
              validateFirst = { true }
              // update-end--author:liaozhiyang---date:20240514---for：【issues/1244】标识了必填，但是必填标识没显示
              labelCol={labelCol}
              wrapperCol={wrapperCol}
            >
              <div style="display:flex">
                {/* author: sunjianlei for: 【VUEN-744】此处加上 width: 100%; 因为要防止组件宽度超出 FormItem */}
                {/* update-begin--author:liaozhiyang---date:20240510---for：【TV360X-719】表单校验不通过项滚动到可视区内 */}
                <Middleware>{getContent()}</Middleware>
                {/* update-end--author:liaozhiyang---date:20240510---for：【TV360X-719】表单校验不通过项滚动到可视区内 */}
                {showSuffix && <span class="suffix">{getSuffix}</span>}
              </div>
            </Form.Item>
          );
        }
      }

      return () => {
        const { colProps = {}, colSlot, renderColContent, component } = props.schema;
        if (!componentMap.has(component)) {
          return null;
        }

        const { baseColProps = {} } = props.formProps;
        // update-begin--author:liaozhiyang---date:20230803---for：【issues-641】调整表格搜索表单的span配置无效
        const { getIsMobile } = useAppInject();
        let realColProps;
        realColProps = { ...baseColProps, ...colProps };
        if (colProps['span'] && !unref(getIsMobile)) {
          ['xs', 'sm', 'md', 'lg', 'xl', 'xxl'].forEach((name) => delete realColProps[name]);
        }
        // update-end--author:liaozhiyang---date:20230803---for：【issues-641】调整表格搜索表单的span配置无效
        const { isIfShow, isShow } = getShow();
        const values = unref(getValues);

        const getContent = () => {
          return colSlot ? getSlot(slots, colSlot, values) : renderColContent ? renderColContent(values) : renderItem();
        };

        return (
          isIfShow && (
            <Col {...realColProps} v-show={isShow}>
              {getContent()}
            </Col>
          )
        );
      };
    },
  });
</script>
