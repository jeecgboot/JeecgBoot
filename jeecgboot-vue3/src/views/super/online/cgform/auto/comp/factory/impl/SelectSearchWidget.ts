import { FormSchema } from '/@/components/Form';
import IFormSchema from '../IFormSchema';

/**
 * 下拉搜索
 */
export default class SelectSearchWidget extends IFormSchema {
  dict: string;
  type: number;
  constructor(key, data) {
    super(key, data);
    if (data.dictTable && data.dictText && data.dictCode) {
      // 字典表
      this.dict = this.genDictTableCode(data.dictTable, data.dictText, data.dictCode);
      this.type = 1;
    } else {
      // 数据字典
      this.dict = encodeURI(`${data.dictCode}`);
      this.type = 0;
    }
  }

  setFormRef(ref) {
    super.setFormRef(ref);
    this.handleDictTableParams();
  }

  updateDictTable(dictTable: string) {
    this.formRef.value.updateSchema(({
      field: this.field,
      componentProps: {
        dict: this.genDictTableCode(dictTable, this._data.dictText, this._data.dictCode),
      }
    }))
  }

  getItem(): FormSchema {
    let item = super.getItem();
    let popContainer = this.getPopContainer();
    // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
    const extendData = this.getExtendData();
    // update-end--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
    return Object.assign({}, item, {
      component: 'JSearchSelect',
      componentProps: {
        dict: this.dict,
        pageSize: 10,
        // update-begin--author:liaozhiyang---date:20240628---for：【issues/6336】online下拉搜索框设置数据字典编辑弹窗报错
        async: this.type ? true : false,
        // update-end--author:liaozhiyang---date:20240628---for：【issues/6336】online下拉搜索框设置数据字典编辑弹窗报错
        useDicColor: true,
        popContainer: popContainer,
        // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
        multiple: extendData.multiple ?? false,
        // update-end--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
      },
    });
  }
}
