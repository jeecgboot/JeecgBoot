package org.jeecg.modules.demo.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 单表示例
 * @Author: jeecg-boot
 * @Date:2018-12-29
 * @Version:V2.0
 */
@Slf4j
@Api(tags = "单表DEMO")
@RestController
@RequestMapping("/test/jeecgDemo")
public class JeecgDemoController extends JeecgController<JeecgDemo, IJeecgDemoService> {
    @Autowired
    private IJeecgDemoService jeecgDemoService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param jeecgDemo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取Demo数据列表", notes = "获取所有Demo数据列表")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "jeecg/JeecgDemoList")
    public Result<?> list(JeecgDemo jeecgDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<JeecgDemo> queryWrapper = QueryGenerator.initQueryWrapper(jeecgDemo, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<JeecgDemo> page = new Page<JeecgDemo>(pageNo, pageSize);

        IPage<JeecgDemo> pageList = jeecgDemoService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param jeecgDemo
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加测试DEMO")
    @ApiOperation(value = "添加DEMO", notes = "添加DEMO")
    public Result<?> add(@RequestBody JeecgDemo jeecgDemo) {
        jeecgDemoService.save(jeecgDemo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param jeecgDemo
     * @return
     */
    @AutoLog(value = "编辑DEMO", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "编辑DEMO", notes = "编辑DEMO")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<?> edit(@RequestBody JeecgDemo jeecgDemo) {
        jeecgDemoService.updateById(jeecgDemo);
        return Result.OK("更新成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除测试DEMO")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除DEMO", notes = "通过ID删除DEMO")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        jeecgDemoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除DEMO", notes = "批量删除DEMO")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.jeecgDemoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "通过ID查询DEMO", notes = "通过ID查询DEMO")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        JeecgDemo jeecgDemo = jeecgDemoService.getById(id);
        return Result.OK(jeecgDemo);
    }

    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    @PermissionData(pageComponent = "jeecg/JeecgDemoList")
    public ModelAndView exportXls(HttpServletRequest request, JeecgDemo jeecgDemo) {
        //获取导出表格字段
        String exportFields = jeecgDemoService.getExportFields();
        //分sheet导出表格字段
        return super.exportXlsSheet(request, jeecgDemo, JeecgDemo.class, "单表模型",exportFields,500);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, JeecgDemo.class);
    }

    // =====Redis 示例===============================================================================================

    /**
     * redis操作 -- set
     */
    @GetMapping(value = "/redisSet")
    public void redisSet() {
        redisUtil.set("name", "张三" + DateUtils.now());
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redisGet")
    public String redisGet() {
        return (String) redisUtil.get("name");
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisSetObj")
    public void redisSetObj() {
        JeecgDemo p = new JeecgDemo();
        p.setAge(10);
        p.setBirthday(new Date());
        p.setContent("hello");
        p.setName("张三");
        p.setSex("男");
        redisUtil.set("user-zdh", p);
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisGetObj")
    public Object redisGetObj() {
        return redisUtil.get("user-zdh");
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redis/{id}")
    public JeecgDemo redisGetJeecgDemo(@PathVariable("id") String id) {
        JeecgDemo t = jeecgDemoService.getByIdCacheable(id);
        log.info(t.toString());
        return t;
    }

    // ===Freemaker示例================================================================================

    /**
     * freemaker方式 【页面路径： src/main/resources/templates】
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping("/html")
    public ModelAndView ftl(ModelAndView modelAndView) {
        modelAndView.setViewName("demo3");
        List<String> userList = new ArrayList<String>();
        userList.add("admin");
        userList.add("user1");
        userList.add("user2");
        log.info("--------------test--------------");
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }


    // ==========================================动态表单 JSON接收测试===========================================
    /**
     * online新增数据
     */
    @PostMapping(value = "/testOnlineAdd")
    public Result<?> testOnlineAdd(@RequestBody JSONObject json) {
        log.info(json.toJSONString());
        return Result.OK("添加成功！");
    }

    /*----------------------------------------外部获取权限示例------------------------------------*/

    /**
     * 【数据权限示例 - 编程】mybatisPlus java类方式加载权限
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/mpList")
    @PermissionData(pageComponent = "jeecg/JeecgDemoList")
    public Result<?> loadMpPermissonList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        QueryWrapper<JeecgDemo> queryWrapper = new QueryWrapper<JeecgDemo>();
        //编程方式，给queryWrapper装载数据权限规则
        QueryGenerator.installAuthMplus(queryWrapper, JeecgDemo.class);
        Page<JeecgDemo> page = new Page<JeecgDemo>(pageNo, pageSize);
        IPage<JeecgDemo> pageList = jeecgDemoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 【数据权限示例 - 编程】mybatis xml方式加载权限
     *
     * @param jeecgDemo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/sqlList")
    @PermissionData(pageComponent = "jeecg/JeecgDemoList")
    public Result<?> loadSqlPermissonList(JeecgDemo jeecgDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        IPage<JeecgDemo> pageList = jeecgDemoService.queryListWithPermission(pageSize, pageNo);
        return Result.OK(pageList);
    }
    /*----------------------------------------外部获取权限示例------------------------------------*/

    /**
     * online api增强 列表
     * @param params
     * @return
     */
    @PostMapping("/enhanceJavaListHttp")
    public Result enhanceJavaListHttp(@RequestBody JSONObject params) {
        log.info(" =========================================================== ");
        log.info("params: " + params.toJSONString());
        log.info("params.tableName: " + params.getString("tableName"));
        log.info("params.json: " + params.getJSONObject("json").toJSONString());
        JSONArray dataList = params.getJSONArray("dataList");
        log.info("params.dataList: " + dataList.toJSONString());
        log.info(" =========================================================== ");
        return Result.OK(dataList);
    }

    /**
     * online api增强 表单
     * @param params
     * @return
     */
    @PostMapping("/enhanceJavaFormHttp")
    public Result enhanceJavaFormHttp(@RequestBody JSONObject params) {
        log.info(" =========================================================== ");
        log.info("params: " + params.toJSONString());
        log.info("params.tableName: " + params.getString("tableName"));
        log.info("params.json: " + params.getJSONObject("json").toJSONString());
        log.info(" =========================================================== ");
        return Result.OK("1");
    }

    @GetMapping(value = "/hello")
    public String hello(HttpServletRequest req) {
        return "hello world!";
    }

    // =====Vue3 Native  原生页面示例===============================================================================================
    @GetMapping(value = "/oneNative/list")
    public Result oneNativeList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Object oneNative = redisUtil.get("one-native");
        JSONArray data = new JSONArray();
        if(null != oneNative){
            JSONObject nativeObject = (JSONObject) oneNative;
            data = nativeObject.getJSONArray("data");
        }
        IPage<JSONObject> objectPage = queryDataPage(data, pageNo, pageSize);
        return Result.OK(objectPage);
    }
    
    @PostMapping("/oneNative/add")
    public Result<String> oneNativeAdd(@RequestBody JSONObject jsonObject){
        Object oneNative = redisUtil.get("one-native");
        JSONObject nativeObject = new JSONObject();
        JSONArray data = new JSONArray();
        if(null != oneNative){
            nativeObject = (JSONObject) oneNative;
            data = nativeObject.getJSONArray("data");
        }
        jsonObject.put("id", UUIDGenerator.generate());
        data.add(jsonObject);
        nativeObject.put("data",data);
        redisUtil.set("one-native",nativeObject);
        return Result.OK("添加成功");
    }
    
    @PutMapping("/oneNative/edit")
    public Result<String> oneNativeEdit(@RequestBody JSONObject jsonObject){
        JSONObject oneNative = (JSONObject)redisUtil.get("one-native");
        JSONArray data = oneNative.getJSONArray("data");
        data = getNativeById(data,jsonObject);
        oneNative.put("data", data);
        redisUtil.set("one-native", oneNative);
        return Result.OK("修改成功");
    }

    @DeleteMapping("/oneNative/delete")
    public Result<String> oneNativeDelete(@RequestParam(name = "ids") String ids){
        Object oneNative = redisUtil.get("one-native");
        if(null != oneNative){
            JSONObject nativeObject = (JSONObject) oneNative;
            JSONArray data = nativeObject.getJSONArray("data");
            data = deleteNativeById(data,ids);
            nativeObject.put("data",data);
            redisUtil.set("one-native",nativeObject);
        }
        return Result.OK("删除成功");
    }
    
    /**
     * 获取redis对应id的数据
     * @param data
     * @param jsonObject
     * @return
     */
    public JSONArray getNativeById(JSONArray data,JSONObject jsonObject){
        String dbId = "id";
        String id = jsonObject.getString(dbId);
        for (int i = 0; i < data.size(); i++) {
            if(id.equals(data.getJSONObject(i).getString(dbId))){
                data.set(i,jsonObject);
                break;
            }
        }
        return data;
    }

    /**
     * 删除redis中包含的id数据
     * @param data
     * @param ids
     * @return
     */
    public JSONArray deleteNativeById(JSONArray data,String ids){
        String dbId = "id";
        for (int i = 0; i < data.size(); i++) {
            //如果id包含直接清除data中的数据
            if(ids.contains(data.getJSONObject(i).getString(dbId))){
                data.fluentRemove(i);
            }
            //判断data的长度是否还剩1位
            if(data.size() == 1 && ids.contains(data.getJSONObject(0).getString(dbId))){
                data.fluentRemove(0);
            }
        }
        return data;
    }

    /**
     * 模拟查询数据，可以根据父ID查询，可以分页
     *
     * @param dataList 数据列表
     * @param pageNo   页码
     * @param pageSize 页大小
     * @return
     */
    private IPage<JSONObject> queryDataPage(JSONArray dataList, Integer pageNo, Integer pageSize) {
        // 根据父级id查询子级
        JSONArray dataDb = dataList;
        // 模拟分页（实际中应用SQL自带的分页）
        List<JSONObject> records = new ArrayList<>();
        IPage<JSONObject> page;
        long beginIndex, endIndex;
        // 如果任意一个参数为null，则不分页
        if (pageNo == null || pageSize == null) {
            page = new Page<>(0, dataDb.size());
            beginIndex = 0;
            endIndex = dataDb.size();
        } else {
            page = new Page<>(pageNo, pageSize);
            beginIndex = page.offset();
            endIndex = page.offset() + page.getSize();
        }
        for (long i = beginIndex; (i < endIndex && i < dataDb.size()); i++) {
            JSONObject data = dataDb.getJSONObject((int) i);
            data = JSON.parseObject(data.toJSONString());
            // 不返回 children
            data.remove("children");
            records.add(data);
        }
        page.setRecords(records);
        page.setTotal(dataDb.size());
        return page;
    }
    // =====Vue3 Native  原生页面示例===============================================================================================


    /**
     * 获取创建人
     * @return
     */
    @GetMapping(value = "/groupList")
    public Result<?> groupList() {
        return Result.ok(jeecgDemoService.getCreateByList());
    }

}
