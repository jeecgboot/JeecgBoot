import { ComponentInternalInstance, ExtractPropTypes } from 'vue';
import { useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';

export namespace JVxeComponent {
  export type Props = ExtractPropTypes<ReturnType<typeof useJVxeCompProps>>;

  interface EnhancedCtx {
    props?: JVxeComponent.Props;
    context?: any;
  }

  /** 组件增强类型 */
  export interface Enhanced {
    // 注册参数（详见：https://xuliangzhan_admin.gitee.io/vxe-table/v4/table/renderer/edit）
    installOptions: {
      // 自动聚焦的 class 类名
      autofocus?: string;
    } & Recordable;
    // 事件拦截器（用于兼容）
    interceptor: {
      // 已实现：event.clearActived
      // 说明：比如点击了某个组件的弹出层面板之后，此时被激活单元格不应该被自动关闭，通过返回 false 可以阻止默认的行为。
      'event.clearActived'?: (params, event, target, ctx?: EnhancedCtx) => boolean;
      // 自定义：event.clearActived.className
      // 说明：比原生的多了一个参数：className，用于判断点击的元素的样式名（递归到顶层）
      'event.clearActived.className'?: (params, event, target, ctx?: EnhancedCtx) => boolean;
    };
    // 【功能开关】
    switches: {
      // 是否使用 editRender 模式（仅当前组件，并非全局）
      // 如果设为true，则表头上方会出现一个可编辑的图标
      editRender?: boolean;
      // false = 组件触发后可视）；true = 组件一直可视
      visible?: boolean;
    };
    // 【切面增强】切面事件处理，一般在某些方法执行后同步执行
    aopEvents: {
      // 单元格被激活编辑时会触发该事件
      editActived?: (this: ComponentInternalInstance, ...args) => any;
      // 单元格编辑状态下被关闭时会触发该事件
      editClosed?: (this: ComponentInternalInstance, ...args) => any;
      // 返回值决定单元格是否可以编辑
      activeMethod?: (this: ComponentInternalInstance, ...args) => boolean;
    };
    // 【翻译增强】可以实现例如select组件保存的value，但是span模式下需要显示成text
    translate: {
      // 是否启用翻译
      enabled?: boolean;
      /**
       * 【翻译处理方法】如果handler留空，则使用默认的翻译方法
       *
       * @param value 需要翻译的值
       * @returns{*} 返回翻译后的数据
       */
      handler?: (value, ctx?: EnhancedCtx) => any;
    };
    /**
     * 【获取值增强】组件抛出的值
     *
     * @param value 保存到数据库里的值
     * @returns{*} 返回处理后的值
     */
    getValue: (value, ctx?: EnhancedCtx) => any;
    /**
     * 【设置值增强】设置给组件的值
     *
     * @param value 组件触发的值
     * @returns{*} 返回处理后的值
     */
    setValue: (value, ctx?: EnhancedCtx) => any;
    /**
     * 【新增行增强】在用户点击新增时触发的事件，返回新行的默认值
     *
     * @param defaultValue 默认值
     * @param row 行数据
     * @param column 列配置，.params 是用户配置的参数
     * @param $table vxe 实例
     * @param renderOptions 渲染选项
     * @param params 可以在这里获取 $table
     *
     * @returns 返回新值
     */
    createValue: (defaultValue: any, ctx?: EnhancedCtx) => any;
  }

  export type EnhancedPartial = Partial<Enhanced>;
}
