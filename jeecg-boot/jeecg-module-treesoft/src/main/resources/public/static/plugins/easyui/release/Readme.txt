
插件引用方式：

于项目页面中，依次按照如下顺序引入各个文件即可

一、引入 jquery 基础库
    1.  jquery.js

二、引入 jquery-easyui 基础库
    2.  easyui.css
    3.  icon.css
    4.  jquery.easyui.min.js
    5.  easyui-lang-zh_CN.js (可选)

三、引入 jquery.jdirk 基础扩展库
    6.  jquery.jdirk.min.js

四、引入 jquery-easyui 扩展图标库 (可选)
    7.  icons/icon-all.min.css (可选)
    8.  jeasyui.icons.all.min.js (可选)

五、引入 jquery-easyui-extensions 插件扩展库
    9. jeasyui.extensions.min.css
    10.  可选，如果需要用到 easyui-my97 插件，则需要引入如下文件
            plugins/My97DatePicker/WdatePicker.js
    11.  可选，如果需要用到 easyui-ueditor 插件，则需要引入如下文件(根据服务器语言环境的不同，以下路径中 "ue1.4.3-utf8-net" 请更换成其对应的服务语言环境版本)
            plugins/ueditor/ueditor1_4_3-utf8-net/ueditor.config.js
            plugins/ueditor/ueditor1_4_3-utf8-net/ueditor.all.js
            plugins/ueditor/ueditor1_4_3-utf8-net/langh/zh-cn/zh-cn.js (可选，ueditor 的中文语言包)
    12. jeasyui.extensions.all.min.js
