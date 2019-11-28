# JSelectBizComponent

Jeecg 选择组件的公共可复用组件

## 引用方式

```js
import JSelectBizComponent from '@/src/components/jeecgbiz/JSelectBizComponent'

export default {
  components: { JSelectBizComponent }
}
```

## 参数

### 配置参数

- `name`:`String` 显示名字，例如选择部门就填写'部门'
- `listUrl`:`String` 数据请求地址，必须是封装了分页的地址
- `displayKey`:`String` 显示在标签上的字段 key
- `returnKeys`:`Array` v-model 绑定的 keys，是个数组，默认使用第二项，当配置了 `returnId=true` 就返回第一项
- `returnId`:`Boolean` 返回ID，设为true后将返回配置的 `returnKeys` 中的第一项
- `selectButtonText`:`String` 选择按钮的文字
- `queryParamText`:`String` 查询条件显示文字
- `columns`:`Array` 列配置项，与a-table的列配置项相同，会将第一项配置成已选择的列表