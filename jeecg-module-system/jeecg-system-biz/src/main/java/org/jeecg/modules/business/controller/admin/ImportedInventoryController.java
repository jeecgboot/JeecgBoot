package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.ImportedInventory;
import org.jeecg.modules.business.service.IImportedInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 导入采购清单
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Api(tags = "导入采购清单")
@RestController
@RequestMapping("/business/importedInventory")
@Slf4j
public class ImportedInventoryController extends JeecgController<ImportedInventory, IImportedInventoryService> {
    @Autowired
    private IImportedInventoryService importedInventoryService;

    /**
     * 分页列表查询
     *
     * @param importedInventory
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "导入采购清单-分页列表查询")
    @ApiOperation(value = "导入采购清单-分页列表查询", notes = "导入采购清单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ImportedInventory importedInventory,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ImportedInventory> queryWrapper = QueryGenerator.initQueryWrapper(importedInventory, req.getParameterMap());
        Page<ImportedInventory> page = new Page<>(pageNo, pageSize);
        IPage<ImportedInventory> pageList = importedInventoryService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param importedInventory
     * @return
     */
    @AutoLog(value = "导入采购清单-添加")
    @ApiOperation(value = "导入采购清单-添加", notes = "导入采购清单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ImportedInventory importedInventory) {
        importedInventoryService.save(importedInventory);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param importedInventory
     * @return
     */
    @AutoLog(value = "导入采购清单-编辑")
    @ApiOperation(value = "导入采购清单-编辑", notes = "导入采购清单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ImportedInventory importedInventory) {
        importedInventoryService.updateById(importedInventory);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "导入采购清单-通过id删除")
    @ApiOperation(value = "导入采购清单-通过id删除", notes = "导入采购清单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        importedInventoryService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "导入采购清单-批量删除")
    @ApiOperation(value = "导入采购清单-批量删除", notes = "导入采购清单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.importedInventoryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "导入采购清单-通过id查询")
    @ApiOperation(value = "导入采购清单-通过id查询", notes = "导入采购清单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ImportedInventory importedInventory = importedInventoryService.getById(id);
        if (importedInventory == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(importedInventory);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param importedInventory
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ImportedInventory importedInventory) {
        return super.exportXls(request, importedInventory, ImportedInventory.class, "导入采购清单");
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
        return super.importExcel(request, response, ImportedInventory.class);
    }

}
