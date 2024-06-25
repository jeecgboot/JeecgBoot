import type { App } from 'vue';
import { Icon } from './Icon';
import AIcon from '/@/components/jeecg/AIcon.vue';
//Tinymce富文本
 import Editor from '/@/components/Tinymce/src/Editor.vue'

import { Button, JUploadButton } from './Button';

// 按需注册antd的组件
import {
  // Need
  Button as AntButton,
  Select,
  Alert,
  Checkbox,
  DatePicker,
  TimePicker,
  Calendar,
  Radio,
  Switch,
  Card,
  List,
  Tabs,
  Descriptions,
  Tree,
  Table,
  Divider,
  Modal,
  Drawer,
  TreeSelect,
  Dropdown,
  Tag,
  Tooltip,
  Badge,
  Popover,
  Upload,
  Transfer,
  Steps,
  PageHeader,
  Result,
  Empty,
  Avatar,
  Menu,
  Breadcrumb,
  Form,
  Input,
  Row,
  Col,
  Spin,
  Space,
  Layout,
  Collapse,
  Slider,
  InputNumber,
  Carousel,
  Popconfirm,
  Skeleton,
  Cascader,
  Rate,
  Progress
} from 'ant-design-vue';
const compList = [AntButton.Group, Icon, AIcon, JUploadButton];


export function registerGlobComp(app: App) {
  compList.forEach((comp) => {
    app.component(comp.name || comp.displayName, comp);
  });
  
  //仪表盘依赖Tinymce，需要提前加载（没办法按需加载了）
  app.component(Editor.name, Editor);
  // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8241】Tinymce异步加载
  // app.component(
  //   'Tinymce',
  //   createAsyncComponent(() => import('./Tinymce/src/Editor.vue'), {
  //     loading: true,
  //   })
  // );
  // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8241】Tinymce异步加载
  app.use(Select)
    .use(Alert)
    .use(Button)
    .use(Breadcrumb)
    .use(Checkbox)
    .use(DatePicker)
    .use(TimePicker)
    .use(Calendar)
    .use(Radio)
    .use(Switch)
    .use(Card)
    .use(List)
    .use(Descriptions)
    .use(Tree)
    .use(TreeSelect)
    .use(Table)
    .use(Divider)
    .use(Modal)
    .use(Drawer)
    .use(Dropdown)
    .use(Tag)
    .use(Tooltip)
    .use(Badge)
    .use(Popover)
    .use(Upload)
    .use(Transfer)
    .use(Steps)
    .use(PageHeader)
    .use(Result)
    .use(Empty)
    .use(Avatar)
    .use(Menu)
    .use(Tabs)
    .use(Form)
    .use(Input)
    .use(Row)
    .use(Col)
    .use(Spin)
    .use(Space)
    .use(Layout)
    .use(Collapse)
    .use(Slider)
    .use(InputNumber)
    .use(Carousel)
    .use(Popconfirm)
    .use(Skeleton)
    .use(Cascader)
    .use(Rate)
    .use(Progress);
    console.log("---初始化---， 全局注册Antd、仪表盘、流程设计器、online、流程等组件--------------")
}
