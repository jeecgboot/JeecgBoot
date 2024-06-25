import { genMessage } from '../helper';
import antdLocale from 'ant-design-vue/es/locale/en_US';
//import momentLocale from 'moment/dist/locale/eu';

const modules = import.meta.glob('./en/**/*.ts', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'en'),
    antdLocale,
  },
  dateLocale: null,
  dateLocaleName: 'en',
};
