# JDictSelectTag 组件用法
----
- 从字典表获取数据,dictCode格式说明: 字典code
```html
<j-dict-select-tag  v-model="queryParam.sex" placeholder="请输入用户性别"
                  dictCode="sex"/>
```

v-decorator用法：
```html
<j-dict-select-tag  v-decorator="['sex', {}]" :triggerChange="true" placeholder="请输入用户性别"
                  dictCode="sex"/>
```

- 从数据库表获取字典数据，dictCode格式说明: 表名,文本字段,取值字段
```html
<j-dict-select-tag v-model="queryParam.username" placeholder="请选择用户名称" 
                   dictCode="sys_user,realname,id"/>
```



# JDictSelectUtil.js 列表字典函数用法
----

- 第一步: 引入依赖方法
```html
       import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
```

- 第二步: 在created()初始化方法执行字典配置方法
```html
      //初始化字典配置
      this.initDictConfig();
```
      
- 第三步: 实现initDictConfig方法，加载列表所需要的字典(列表上有多个字典项，就执行多次initDictOptions方法)
      
```html
      initDictConfig() {
        //初始化字典 - 性别
        initDictOptions('sex').then((res) => {
          if (res.success) {
            this.sexDictOptions = res.result;
          }
        });
      },
```
      
- 第四步: 实现字段的customRender方法
```html
     customRender: (text, record, index) => {
       //字典值替换通用方法
       return filterDictText(this.sexDictOptions, text);
     }
```