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

| 参数名                | 类型    | 必填 | 默认值       | 备注                                                                                 |
|-----------------------|---------|------|--------------|--------------------------------------------------------------------------------------|
| rowKey                | String  |      | "id"         | 唯一标识的字段名                                                                     |
| value(v-model)        | String  |      | ""           | 默认选择的数据，多个用半角逗号分割                                                   |
| name                  | String  |      | ""           | 显示名字，例如选择用户就填写"用户"                                                   |
| listUrl               | String  | 是   |              | 数据请求地址，必须是封装了分页的地址                                                 |
| valueUrl              | String  |      | ""           | 获取显示文本的地址，例如存的是 username，可以通过该地址获取到 realname               |
| displayKey            | String  |      | null         | 显示在标签上的字段 key    ，不传则直接显示数据                                       |
| returnKeys            | Array   |      | ['id', 'id'] | v-model 绑定的 keys，是个数组，默认使用第二项，当配置了 `returnId=true` 就返回第一项 |
| returnId              | Boolean |      | false        | 返回ID，设为true后将返回配置的 `returnKeys` 中的第一项                               |
| selectButtonText      | String  |      | "选择"       | 选择按钮的文字                                                                       |
| queryParamText        | String  |      | null         | 查询条件显示文字，不传则使用 `name`                                                  |
| columns               | Array   | 是   |              | 列配置项，与antd的table的配置完全一致。列的第一项会被配置成右侧已选择的列表上        |
| columns[0].widthRight | Array   |      | null         | 仅列的第一项可以应用此配置，表示右侧已选择列表的宽度，建议 `70%`，不传则应用`width`  |
| placeholder           | String  |      | "请选择"     | 占位符                                                                               |
| disabled              | Boolean |      | false        | 是否禁用                                                                             |
| multiple              | Boolean |      | false        | 是否可多选                                                                           |
| buttons               | Boolean |      | true         | 是否显示"选择"按钮，如果不显示，可以直接点击文本框打开选择界面                       |
