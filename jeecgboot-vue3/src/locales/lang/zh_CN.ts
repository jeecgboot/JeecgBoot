import { genMessage } from '../helper';
import antdLocale from 'ant-design-vue/es/locale/zh_CN';

const modules = import.meta.glob('./zh-CN/**/*.ts', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'zh-CN'),
    antdLocale,
  },
};
