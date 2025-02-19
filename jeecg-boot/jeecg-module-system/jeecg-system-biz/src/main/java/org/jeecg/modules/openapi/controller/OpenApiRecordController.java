package org.jeecg.modules.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.openapi.entity.OpenApiRecord;
import org.jeecg.modules.openapi.service.OpenApiRecordService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @date 2024/12/10 9:57
 */
@RestController
@RequestMapping("/openapi/record")
public class OpenApiRecordController extends JeecgController<OpenApiRecord, OpenApiRecordService> {

    /**
     * 分页列表查询
     *
     * @param openApiRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenApiRecord openApiRecord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<OpenApiRecord> queryWrapper = QueryGenerator.initQueryWrapper(openApiRecord, req.getParameterMap());
        Page<OpenApiRecord> page = new Page<>(pageNo, pageSize);
        IPage<OpenApiRecord> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openApiRecord
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenApiRecord openApiRecord) {
        service.save(openApiRecord);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openApiRecord
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenApiRecord openApiRecord) {
        service.updateById(openApiRecord);
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
        OpenApiRecord openApiRecord = service.getById(id);
        return Result.ok(openApiRecord);
    }
}
