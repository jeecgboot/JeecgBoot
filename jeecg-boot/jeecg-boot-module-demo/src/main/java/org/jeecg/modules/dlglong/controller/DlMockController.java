package org.jeecg.modules.dlglong.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.MatchTypeEnum;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.constant.VXESocketConst;
import org.jeecg.modules.demo.mock.vxe.websocket.VXESocket;
import org.jeecg.modules.dlglong.entity.MockEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/mock/dlglong")
public class DlMockController {

    /**
     * 模拟更改状态
     *
     * @param id
     * @param status
     * @return
     */
    @GetMapping("/change1")
    public Result mockChange1(@RequestParam("id") String id, @RequestParam("status") String status) {
        /* id 为 行的id（rowId），只要获取到rowId，那么只需要调用 VXESocket.sendMessageToAll() 即可 */

        // 封装行数据
        JSONObject rowData = new JSONObject();
        // 这个字段就是要更改的行数据ID
        rowData.put("id", id);
        // 这个字段就是要更改的列的key和具体的值
        rowData.put("status", status);
        // 模拟更改数据
        this.mockChange(rowData);

        return Result.ok();
    }

    /**
     * 模拟更改拖轮状态
     *
     * @param id
     * @param tug_status
     * @return
     */
    @GetMapping("/change2")
    public Result mockChange2(@RequestParam("id") String id, @RequestParam("tug_status") String tug_status) {
        /* id 为 行的id（rowId），只要获取到rowId，那么只需要调用 VXESocket.sendMessageToAll() 即可 */

        // 封装行数据
        JSONObject rowData = new JSONObject();
        // 这个字段就是要更改的行数据ID
        rowData.put("id", id);
        // 这个字段就是要更改的列的key和具体的值
        JSONObject tugStatus = JSON.parseObject(tug_status);
        rowData.put("tug_status", tugStatus);
        // 模拟更改数据
        this.mockChange(rowData);

        return Result.ok();
    }

    /**
     * 模拟更改进度条状态
     *
     * @param id
     * @param progress
     * @return
     */
    @GetMapping("/change3")
    public Result mockChange3(@RequestParam("id") String id, @RequestParam("progress") String progress) {
        /* id 为 行的id（rowId），只要获取到rowId，那么只需要调用 VXESocket.sendMessageToAll() 即可 */

        // 封装行数据
        JSONObject rowData = new JSONObject();
        // 这个字段就是要更改的行数据ID
        rowData.put("id", id);
        // 这个字段就是要更改的列的key和具体的值
        rowData.put("progress", progress);
        // 模拟更改数据
        this.mockChange(rowData);

        return Result.ok();
    }

    private void mockChange(JSONObject rowData) {
        // 封装socket数据
        JSONObject socketData = new JSONObject();
        // 这里的 socketKey 必须要和调度计划页面上写的 socketKey 属性保持一致
        socketData.put("socketKey", "page-dispatch");
        // 这里的 args 必须得是一个数组，下标0是行数据，下标1是caseId，一般不用传
        socketData.put("args", new Object[]{rowData, ""});
        // 封装消息字符串，这里的 type 必须是 VXESocketConst.TYPE_UVT
        String message = VXESocket.packageMessage(VXESocketConst.TYPE_UVT, socketData);
        // 调用 sendMessageToAll 发送给所有在线的用户
        VXESocket.sendMessageToAll(message);
    }

    /**
     * 模拟更改【大船待审】状态
     *
     * @param status
     * @return
     */
    @GetMapping("/change4")
    public Result mockChange4(@RequestParam("status") String status) {
        // 封装socket数据
        JSONObject socketData = new JSONObject();
        // 这里的 key 是前端注册时使用的key，必须保持一致
        socketData.put("key", "dispatch-dcds-status");
        // 这里的 args 必须得是一个数组，每一位都是注册方法的参数，按顺序传递
        socketData.put("args", new Object[]{status});

        // 封装消息字符串，这里的 type 必须是 VXESocketConst.TYPE_UVT
        String message = VXESocket.packageMessage(VXESocketConst.TYPE_CSD, socketData);
        // 调用 sendMessageToAll 发送给所有在线的用户
        VXESocket.sendMessageToAll(message);

        return Result.ok();
    }

    /**
     * 【模拟】即时保存单行数据
     *
     * @param rowData 行数据，实际使用时可以替换成一个实体类
     */
    @PutMapping("/immediateSaveRow")
    public Result mockImmediateSaveRow(@RequestBody JSONObject rowData) throws Exception {
        System.out.println("即时保存.rowData：" + rowData.toJSONString());
        // 延时1.5秒，模拟网慢堵塞真实感
        Thread.sleep(500);
        return Result.ok();
    }

    /**
     * 【模拟】即时保存整个表格的数据
     *
     * @param tableData 表格数据（实际使用时可以替换成一个List实体类）
     */
    @PostMapping("/immediateSaveAll")
    public Result mockImmediateSaveAll(@RequestBody JSONArray tableData) throws Exception {
        // 【注】：
        // 1、tableData里包含该页所有的数据
        // 2、如果你实现了“即时保存”，那么除了新增的数据，其他的都是已经保存过的了，
        //    不需要再进行一次update操作了，所以可以在前端传数据的时候就遍历判断一下，
        //    只传新增的数据给后台insert即可，否者将会造成性能上的浪费。
        // 3、新增的行是没有id的，通过这一点，就可以判断是否是新增的数据

        System.out.println("即时保存.tableData：" + tableData.toJSONString());
        // 延时1.5秒，模拟网慢堵塞真实感
        Thread.sleep(1000);
        return Result.ok();
    }

    /**
     * 获取模拟数据
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param parentId 父ID，不传则查询顶级
     * @return
     */
    @GetMapping("/getData")
    public Result getMockData(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            // 父级id，根据父级id查询子级，如果为空则查询顶级
            @RequestParam(name = "parentId", required = false) String parentId
    ) {
        // 模拟JSON数据路径
        String path = "classpath:org/jeecg/modules/dlglong/json/dlglong.json";
        // 读取JSON数据
        JSONArray dataList = readJsonData(path);
        if (dataList == null) {
            return Result.error("读取数据失败！");
        }
        IPage<JSONObject> page = this.queryDataPage(dataList, parentId, pageNo, pageSize);
        return Result.ok(page);
    }

    /**
     * 获取模拟“调度计划”页面的数据
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param parentId 父ID，不传则查询顶级
     * @return
     */
    @GetMapping("/getDdjhData")
    public Result getMockDdjhData(
            // SpringMVC 会自动将参数注入到实体里
            MockEntity mockEntity,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            // 父级id，根据父级id查询子级，如果为空则查询顶级
            @RequestParam(name = "parentId", required = false) String parentId,
            @RequestParam(name = "status", required = false) String status,
            // 高级查询条件
            @RequestParam(name = "superQueryParams", required = false) String superQueryParams,
            // 高级查询模式
            @RequestParam(name = "superQueryMatchType", required = false) String superQueryMatchType,
            HttpServletRequest request
    ) {
        // 获取查询条件（前台传递的查询参数）
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 遍历输出到控制台
        System.out.println("\ngetDdjhData - 普通查询条件：");
        for (String key : parameterMap.keySet()) {
            System.out.println("-- " + key + ": " + JSON.toJSONString(parameterMap.get(key)));
        }
        // 输出高级查询
        try {
            System.out.println("\ngetDdjhData - 高级查询条件：");
            // 高级查询模式
            MatchTypeEnum matchType = MatchTypeEnum.getByValue(superQueryMatchType);
            if (matchType == null) {
                System.out.println("-- 高级查询模式：不识别（" + superQueryMatchType + "）");
            } else {
                System.out.println("-- 高级查询模式：" + matchType.getValue());
            }
            superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
            List<QueryCondition> conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
            if (conditions != null) {
                for (QueryCondition condition : conditions) {
                    System.out.println("-- " + JSON.toJSONString(condition));
                }
            } else {
                System.out.println("-- 没有传递任何高级查询条件");
            }
            System.out.println();
        } catch (Exception e) {
            log.error("-- 高级查询操作失败：" + superQueryParams, e);
            e.printStackTrace();
        }

        /* 注：实际使用中不用写上面那种繁琐的代码，这里只是为了直观的输出到控制台里而写的示例，
              使用下面这种写法更简洁方便 */

        // 封装成 MyBatisPlus 能识别的 QueryWrapper，可以直接使用这个对象进行SQL筛选条件拼接
        // 这个方法也会自动封装高级查询条件，但是高级查询参数名必须是superQueryParams和superQueryMatchType
        QueryWrapper<MockEntity> queryWrapper = QueryGenerator.initQueryWrapper(mockEntity, parameterMap);
        System.out.println("queryWrapper： " + queryWrapper.getCustomSqlSegment());

        // 模拟JSON数据路径
        String path = "classpath:org/jeecg/modules/dlglong/json/ddjh.json";
        if ("8".equals(status)) {
            path = "classpath:org/jeecg/modules/dlglong/json/ddjh_s8.json";
        }
        // 读取JSON数据
        JSONArray dataList = readJsonData(path);
        if (dataList == null) {
            return Result.error("读取数据失败！");
        }

        IPage<JSONObject> page = this.queryDataPage(dataList, parentId, pageNo, pageSize);
        // 逐行查询子表数据，用于计算拖轮状态
        List<JSONObject> records = page.getRecords();
        for (JSONObject record : records) {
            Map<String, Integer> tugStatusMap = new HashMap<>();
            String id = record.getString("id");
            // 查询出主表的拖轮
            String tugMain = record.getString("tug");
            // 判断是否有值
            if (StringUtils.isNotBlank(tugMain)) {
                // 拖轮根据分号分割
                String[] tugs = tugMain.split(";");
                // 查询子表数据
                List<JSONObject> subRecords = this.queryDataPage(dataList, id, null, null).getRecords();
                // 遍历子表和拖轮数据，找出进行计算反推拖轮状态
                for (JSONObject subData : subRecords) {
                    String subTug = subData.getString("tug");
                    if (StringUtils.isNotBlank(subTug)) {
                        for (String tug : tugs) {
                            if (tug.equals(subTug)) {
                                // 计算拖轮状态逻辑
                                int statusCode = 0;

                                /* 如果有发船时间、作业开始时间、作业结束时间、回船时间，则主表中的拖轮列中的每个拖轮背景色要即时变色 */

                                // 有发船时间，状态 +1
                                String departureTime = subData.getString("departure_time");
                                if (StringUtils.isNotBlank(departureTime)) {
                                    statusCode += 1;
                                }
                                // 有作业开始时间，状态 +1
                                String workBeginTime = subData.getString("work_begin_time");
                                if (StringUtils.isNotBlank(workBeginTime)) {
                                    statusCode += 1;
                                }
                                // 有作业结束时间，状态 +1
                                String workEndTime = subData.getString("work_end_time");
                                if (StringUtils.isNotBlank(workEndTime)) {
                                    statusCode += 1;
                                }
                                // 有回船时间，状态 +1
                                String returnTime = subData.getString("return_time");
                                if (StringUtils.isNotBlank(returnTime)) {
                                    statusCode += 1;
                                }
                                // 保存拖轮状态，key是拖轮的值，value是状态，前端根据不同的状态码，显示不同的颜色，这个颜色也可以后台计算完之后返回给前端直接使用
                                tugStatusMap.put(tug, statusCode);
                                break;
                            }
                        }
                    }
                }
            }
            // 新加一个字段用于保存拖轮状态，不要直接覆盖原来的，这个字段可以不保存到数据库里
            record.put("tug_status", tugStatusMap);
        }
        page.setRecords(records);
        return Result.ok(page);
    }

    /**
     * 模拟查询数据，可以根据父ID查询，可以分页
     *
     * @param dataList 数据列表
     * @param parentId 父ID
     * @param pageNo   页码
     * @param pageSize 页大小
     * @return
     */
    private IPage<JSONObject> queryDataPage(JSONArray dataList, String parentId, Integer pageNo, Integer pageSize) {
        // 根据父级id查询子级
        JSONArray dataDB = dataList;
        if (StringUtils.isNotBlank(parentId)) {
            JSONArray results = new JSONArray();
            List<String> parentIds = Arrays.asList(parentId.split(","));
            this.queryByParentId(dataDB, parentIds, results);
            dataDB = results;
        }
        // 模拟分页（实际中应用SQL自带的分页）
        List<JSONObject> records = new ArrayList<>();
        IPage<JSONObject> page;
        long beginIndex, endIndex;
        // 如果任意一个参数为null，则不分页
        if (pageNo == null || pageSize == null) {
            page = new Page<>(0, dataDB.size());
            beginIndex = 0;
            endIndex = dataDB.size();
        } else {
            page = new Page<>(pageNo, pageSize);
            beginIndex = page.offset();
            endIndex = page.offset() + page.getSize();
        }
        for (long i = beginIndex; (i < endIndex && i < dataDB.size()); i++) {
            JSONObject data = dataDB.getJSONObject((int) i);
            data = JSON.parseObject(data.toJSONString());
            // 不返回 children
            data.remove("children");
            records.add(data);
        }
        page.setRecords(records);
        page.setTotal(dataDB.size());
        return page;
    }

    private void queryByParentId(JSONArray dataList, List<String> parentIds, JSONArray results) {
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject data = dataList.getJSONObject(i);
            JSONArray children = data.getJSONArray("children");
            // 找到了该父级
            if (parentIds.contains(data.getString("id"))) {
                if (children != null) {
                    // addAll 的目的是将多个子表的数据合并在一起
                    results.addAll(children);
                }
            } else {
                if (children != null) {
                    queryByParentId(children, parentIds, results);
                }
            }
        }
        results.addAll(new JSONArray());
    }

    private JSONArray readJsonData(String path) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(path.replace("classpath:", ""));
            if (stream != null) {
                String json = IOUtils.toString(stream, "UTF-8");
                return JSON.parseArray(json);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
