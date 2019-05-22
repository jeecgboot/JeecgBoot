# 报表组件文档

## 柱状图

##### 引用方式

```js 
import Bar from '@/components/chart/Bar'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| title      | string |    | 报表标题       |
| dataSource | array  | ✔️ | 报表数据源      |
| height     | number |    | 报表高度，默认254 |

##### dataSource 示例

```json
[
    {
        "x": "1月",
        "y": 320
    },
    {
        "x": "2月",
        "y": 457
    },
    {
        "x": "3月",
        "y": 182
    }
]
```

##### 代码示例

```html
<template>
    <bar title="柱状图" :dataSource="dataSource" :height="420"/>
</template>

<script>
    import Bar from '@/components/chart/Bar'

    export default {
        name: 'ChartDemo',
        components: {
            Bar
        },
        data() {
            return {
                dataSource: [
                    {
                        "x": "1月",
                        "y": 320
                    },
                    {
                        "x": "2月",
                        "y": 457
                    },
                    {
                        "x": "3月",
                        "y": 182
                    }
                ]
            }
        }
    }
</script>

<style></style>
```

## 多列柱状图

##### 引用方式

```js 
import BarMultid from '@/components/chart/BarMultid'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| title      | string |    | 报表标题       |
| fields     | array  |    | 主列字段列表     |
| dataSource | array  |    | 报表数据源      |
| height     | number |    | 报表高度，默认254 |

##### fields 示例

```json
["Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug."]
```

##### dataSource 示例

```json
[
    {
        "type": "Jeecg", // 列名
        "Jan.": 18.9,
        "Feb.": 28.8,
        "Mar.": 39.3,
        "Apr.": 81.4,
        "May": 47,
        "Jun.": 20.3,
        "Jul.": 24,
        "Aug.": 35.6
    },
    {
        "type": "Jeebt",
        "Jan.": 12.4,
        "Feb.": 23.2,
        "Mar.": 34.5,
        "Apr.": 99.7,
        "May": 52.6,
        "Jun.": 35.5,
        "Jul.": 37.4,
        "Aug.": 42.4
    }
]
```

## 迷你柱状图

不带标题和数据轴的柱状图

##### 引用方式

```js 
import MiniBar from '@/components/chart/MiniBar'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明            |
|------------|--------|----|---------------|
| width      | number |    | 报表宽度度，默认自适应宽度 |
| height     | number |    | 报表高度，默认200    |
| dataSource | array  |    | 报表数据源         |

##### dataSource 示例

```json
[
    {
        "x": "1月",
        "y": 320
    },
    {
        "x": "2月",
        "y": 457
    },
    {
        "x": "3月",
        "y": 182
    }
]
```

## 面积图

##### 引用方式

```js 
import AreaChartTy from '@/components/chart/AreaChartTy'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| title      | string |    | 报表标题       |
| dataSource | array  | ✔️ | 报表数据源      |
| height     | number |    | 报表高度，默认254 |
| lineSize   | number |    | 线的粗细，默认2   |

##### dataSource 示例

```json
[
    {
        "x": "1月",
        "y": 320
    },
    {
        "x": "2月",
        "y": 457
    },
    {
        "x": "3月",
        "y": 182
    }
]
```

## 多行折线图

##### 引用方式

```js 
import LineChartMultid from '@/components/chart/LineChartMultid'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| title      | string |    | 报表标题       |
| fields     | array  |    | 主列字段列表     |
| dataSource | array  |    | 报表数据源      |
| height     | number |    | 报表高度，默认254 |

##### fields 示例

```json
["jeecg", "jeebt"]
```

##### dataSource 示例

```json
[
    {
        "type": "Jan", // 列名
        "jeecg": 7,
        "jeebt": 3.9
    },
    { "type": "Feb", "jeecg": 6.9, "jeebt": 4.2 },
    { "type": "Mar", "jeecg": 9.5, "jeebt": 5.7 },
    { "type": "Apr", "jeecg": 14.5, "jeebt": 8.5 },
    { "type": "May", "jeecg": 18.4, "jeebt": 11.9 },
    { "type": "Jun", "jeecg": 21.5, "jeebt": 15.2 },
    { "type": "Jul", "jeecg": 25.2, "jeebt": 17 },
    { "type": "Aug", "jeecg": 26.5, "jeebt": 16.6 },
    { "type": "Sep", "jeecg": 23.3, "jeebt": 14.2 },
    { "type": "Oct", "jeecg": 18.3, "jeebt": 10.3 },
    { "type": "Nov", "jeecg": 13.9, "jeebt": 6.6 },
    { "type": "Dec", "jeecg": 9.6, "jeebt": 4.8 }
]
```

## 饼状图

##### 引用方式

```js 
import Pie from '@/components/chart/Pie'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| dataSource | array  |    | 报表数据源      |
| height     | number |    | 报表高度，默认254 |

##### dataSource 示例

```json
[
    // 所有的 percent 相加等于 100
    { "item": "一月", "percent": 40 },
    { "item": "二月", "percent": 21 },
    { "item": "三月", "percent": 17 },
    { "item": "四月", "percent": 13 },
    { "item": "五月", "percent": 9 }
]
```

## 雷达图

##### 引用方式

```js 
import Radar from '@/components/chart/Radar'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明         |
|------------|--------|----|------------|
| dataSource | array  |    | 报表数据源      |
| height     | number |    | 报表高度，默认254 |

##### dataSource 示例

```json
[
    // score 最小值为 0，最大值为 100
    { "item": "一月", "score": 40 },
    { "item": "二月", "score": 20 },
    { "item": "三月", "score": 67 },
    { "item": "四月", "score": 43 },
    { "item": "五月", "score": 90 }
]
```

## 进度条

##### 引用方式

```js 
import MiniProgress from '@/components/chart/MiniProgress'
```

##### 参数列表

| 参数名        | 类型     | 必填 | 说明                |
|------------|--------|----|-------------------|
| percentage | number |    | 当前进度百分比，默认0，最高100 |
| target     | number |    | 目标值，默认10          |
| height     | number |    | 进度条高度，默认10        |
| color      | string |    | 进度条颜色，默认 #13C2C2  |

## 仪表盘

##### 引用方式

```js 
import DashChartDemo from '@/components/chart/DashChartDemo'
```

##### 参数列表

| 参数名    | 类型     | 必填 | 说明             |
|--------|--------|----|----------------|
| title  | string |    | 报表标题           |
| value  | number |    | 当前值，默认6.7，最大为9 |
| height | number |    | 报表高度，默认254     |

## 排名列表

##### 引用方式

```js 
import RankList from '@/components/chart/RankList'
```

##### 参数列表

| 参数名    | 类型     | 必填 | 说明           |
|--------|--------|----|--------------|
| title  | string |    | 报表标题         |
| list   | array  |    | 排名列表数据       |
| height | number |    | 报表高度，默认自适应高度 |

##### list 示例

```json
[
    {
        "name": "北京朝阳 1 号店",
        "total": 1981
    },
    { "name": "北京朝阳 2 号店", "total": 1359 },
    { "name": "北京朝阳 3 号店", "total": 1354 },
    { "name": "北京朝阳 4 号店", "total": 263 },
    { "name": "北京朝阳 5 号店", "total": 446 },
    { "name": "北京朝阳 6 号店", "total": 796 }
]
```