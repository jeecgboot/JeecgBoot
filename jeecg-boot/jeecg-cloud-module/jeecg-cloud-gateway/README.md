#  服务化改造手册

~~~
注意 ：启动服务跨域问题处理 
1、需要将common 模块的 WebMvcConfiguration corsFilter 注掉 后面做成注解切换
2、org.jeecg.config.shiro.filters.JwtFilter类的 65行，跨域代码注释掉
~~~