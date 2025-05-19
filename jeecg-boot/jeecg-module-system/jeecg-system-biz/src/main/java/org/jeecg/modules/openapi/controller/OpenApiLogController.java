package org.jeecg.modules.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.openapi.entity.OpenApiLog;
import org.jeecg.modules.openapi.service.OpenApiLogService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @date 2024/12/10 9:57
 */
@RestController
@RequestMapping("/openapi/record")
public class OpenApiLogController extends JeecgController<OpenApiLog, OpenApiLogService> {

    /**
     * 分页列表查询
     *
     * @param OpenApiLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenApiLog OpenApiLog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<OpenApiLog> queryWrapper = QueryGenerator.initQueryWrapper(OpenApiLog, req.getParameterMap());
        Page<OpenApiLog> page = new Page<>(pageNo, pageSize);
        IPage<OpenApiLog> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param OpenApiLog
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenApiLog OpenApiLog) {
        service.save(OpenApiLog);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param OpenApiLog
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenApiLog OpenApiLog) {
        service.updateById(OpenApiLog);
        return Result.ok("修改成功!");

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        service.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OpenApiLog OpenApiLog = service.getById(id);
        return Result.ok(OpenApiLog);
    }
}
