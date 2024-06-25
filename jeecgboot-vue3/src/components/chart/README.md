# 报表组件文档

## 柱状图

##### 引用方式

```js
import Bar from '/@/components/chart/Bar.vue';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |

##### chartData 示例

```json
[
  {
    "name": "1月",
    "value": 320
  },
  {
    "name": "2月",
    "value": 457
  },
  {
    "name": "3月",
    "value": 182
  }
]
```

##### 代码示例

```html
<template>
  <Bar :chartData="chartData"></Bar>
</template>

<script lang="ts" setup>
  import Bar from '/@/components/chart/Bar.vue';
  const chartData = [
    {
      name: '1月',
      value: 320,
    },
    {
      name: '2月',
      value: 457,
    },
    {
      name: '3月',
      value: 182,
    },
  ];
</script>

<style></style>
```

## 多列柱状图

##### 引用方式

```js
import BarMulti from '/@/components/chart/BarMulti.vue';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |

##### chartData 示例

```json
[
  {
    "name": "1月",
    "value": 320,
    "type": "2021"
  },
  {
    "name": "2月",
    "value": 457,
    "type": "2021"
  },
  {
    "name": "3月",
    "value": 182,
    "type": "2021"
  },
  {
    "name": "1月",
    "value": 240,
    "type": "2022"
  },
  {
    "name": "2月",
    "value": 357,
    "type": "2022"
  },
  {
    "name": "3月",
    "value": 456,
    "type": "2022"
  }
]
```

## 迷你柱状图

同柱形图，修改配置即可

## 面积图

##### 引用方式

```js
import Line from '/@/components/chart/Line.vue';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |
| option    | object |      | 配置项     |

##### chartData 示例

```json
[
  {
    "name": "1月",
    "value": 320
  },
  {
    "name": "2月",
    "value": 457
  },
  {
    "name": "3月",
    "value": 182
  }
]
```

## 多行折线图

##### 引用方式

```js
import LineMulti from '/@/components/chart/LineMulti.vue';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |
| option    | object |      | 配置项     |

##### chartData 示例

同柱形图

## 饼状图

##### 引用方式

```js
import Pie from '/@/components/chart/Pie';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |
| option    | object |      | 配置项     |

##### chartData 示例

```json
[
  { "name": "一月", "value": 40 },
  { "name": "二月", "value": 21 },
  { "name": "三月", "value": 17 },
  { "name": "四月", "value": 13 },
  { "name": "五月", "value": 9 }
]
```

## 雷达图

##### 引用方式

```js
import Radar from '/@/components/chart/Radar';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |
| option    | object |      | 配置项     |

##### chartData 示例

```json
[
  { "item": "一月", "score": 40 },
  { "item": "二月", "score": 20 },
  { "item": "三月", "score": 67 },
  { "item": "四月", "score": 43 },
  { "item": "五月", "score": 90 }
]
```

## 仪表盘

##### 引用方式

```js
import Gauge from '/@/components/chart/Gauge';
```

##### 参数列表

| 参数名    | 类型   | 必填 | 说明       |
| --------- | ------ | ---- | ---------- |
| chartData | array  | ✔️   | 报表数据源 |
| width     | number |      | 报表宽度   |
| height    | number |      | 报表高度   |
| option    | object |      | 配置项     |

## 排名列表

##### 引用方式

```js
import RankList from '@/components/chart/RankList';
```

##### 参数列表

| 参数名 | 类型   | 必填 | 说明                     |
| ------ | ------ | ---- | ------------------------ |
| title  | string |      | 报表标题                 |
| list   | array  |      | 排名列表数据             |
| height | number |      | 报表高度，默认自适应高度 |

##### list 示例

```json
[
  { "name": "北京朝阳 1 号店", "total": 1981 },
  { "name": "北京朝阳 2 号店", "total": 1359 },
  { "name": "北京朝阳 3 号店", "total": 1354 },
  { "name": "北京朝阳 4 号店", "total": 263 },
  { "name": "北京朝阳 5 号店", "total": 446 },
  { "name": "北京朝阳 6 号店", "total": 796 }
]
```
