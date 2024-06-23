package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysPositionService;
import org.jeecg.modules.system.service.ISysUserPositionService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "职务表")
@RestController
@RequestMapping("/sys/position")
public class SysPositionController {

    @Autowired
    private ISysPositionService sysPositionService;

    @Autowired
    private ISysUserPositionService userPositionService;

    @Autowired
    private ISysUserService userService;

    /**
     * 分页列表查询
     *
     * @param sysPosition
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "职务表-分页列表查询")
    @ApiOperation(value = "职务表-分页列表查询", notes = "职务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysPosition>> queryPageList(SysPosition sysPosition,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<SysPosition>> result = new Result<IPage<SysPosition>>();
        //------------------------------------------------------------------------------------------------
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            sysPosition.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
        }
        //------------------------------------------------------------------------------------------------
        QueryWrapper<SysPosition> queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, req.getParameterMap());
        Page<SysPosition> page = new Page<SysPosition>(pageNo, pageSize);
        IPage<SysPosition> pageList = sysPositionService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-添加")
    @ApiOperation(value = "职务表-添加", notes = "职务表-添加")
    @PostMapping(value = "/add")
    public Result<SysPosition> add(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<SysPosition>();
        try {
            //update-begin---author:wangshuai ---date:20230313  for：【QQYUN-4558】vue3职位功能调整，去掉编码和级别，可以先隐藏------------
            //编号是空的，不需要判断多租户隔离了
            if(oConvertUtils.isEmpty(sysPosition.getCode())){
                //生成职位编码10位
                sysPosition.setCode(RandomUtil.randomString(10));
            }
            //update-end---author:wangshuai ---date:20230313  for：【QQYUN-4558】vue3职位功能调整，去掉编码和级别，可以先隐藏-------------
            sysPositionService.save(sysPosition);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-编辑")
    @ApiOperation(value = "职务表-编辑", notes = "职务表-编辑")
    @RequestMapping(value = "/edit", method ={RequestMethod.PUT, RequestMethod.POST})
    public Result<SysPosition> edit(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPositionEntity = sysPositionService.getById(sysPosition.getId());
        if (sysPositionEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysPositionService.updateById(sysPosition);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id删除")
    @ApiOperation(value = "职务表-通过id删除", notes = "职务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysPositionService.removeById(id);
            //删除用户职位关系表
            userPositionService.removeByPositionId(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "职务表-批量删除")
    @ApiOperation(value = "职务表-批量删除", notes = "职务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysPosition> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysPosition> result = new Result<SysPosition>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysPositionService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id查询")
    @ApiOperation(value = "职务表-通过id查询", notes = "职务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysPosition> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPosition = sysPositionService.getById(id);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<SysPosition> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                SysPosition sysPosition = JSON.parseObject(deString, SysPosition.class);
                //------------------------------------------------------------------------------------------------
                //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
                if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
                    sysPosition.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
                }
                //------------------------------------------------------------------------------------------------
                queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysPosition> pageList = sysPositionService.list(queryWrapper);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "职务表列表");
        mv.addObject(NormalExcelConstants.CLASS, SysPosition.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("职务表列表数据", "导出人:"+user.getRealname(),"导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Object>  listSysPositions = ExcelImportUtil.importExcel(file.getInputStream(), SysPosition.class, params);
                List<String> list = ImportExcelUtil.importDateSave(listSysPositions, ISysPositionService.class, errorMessage,CommonConstant.SQL_INDEX_UNIQ_CODE);
                errorLines+=list.size();
                successLines+=(listSysPositions.size()-errorLines);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
    }

    /**
     * 通过code查询
     *
     * @param code
     * @return
     */
    @AutoLog(value = "职务表-通过code查询")
    @ApiOperation(value = "职务表-通过code查询", notes = "职务表-通过code查询")
    @GetMapping(value = "/queryByCode")
    public Result<SysPosition> queryByCode(@RequestParam(name = "code", required = true) String code) {
        Result<SysPosition> result = new Result<SysPosition>();
        QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<SysPosition>();
        queryWrapper.eq("code",code);
        SysPosition sysPosition = sysPositionService.getOne(queryWrapper);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }


    /**
     * 通过多个ID查询
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "职务表-通过多个查询")
    @ApiOperation(value = "职务表-通过多个id查询", notes = "职务表-通过多个id查询")
    @GetMapping(value = "/queryByIds")
    public Result<List<SysPosition>> queryByIds(@RequestParam(name = "ids") String ids) {
        Result<List<SysPosition>> result = new Result<>();
        QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(true,"id",ids.split(","));
        List<SysPosition> list = sysPositionService.list(queryWrapper);
        if (list == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }



    /**
     * 获取职位用户列表
     *
     * @param pageNo
     * @param pageSize
     * @param positionId
     * @return
     */
    @GetMapping("/getPositionUserList")
    public Result<IPage<SysUser>> getPositionUserList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      @RequestParam(name = "positionId") String positionId) {

        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> pageList = userPositionService.getPositionUserList(page, positionId);
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if (null != userIds && userIds.size() > 0) {
            Map<String, String> useDepNames = userService.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item -> {
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        return Result.ok(pageList);
    }

    /**
     * 添加成员到用户职位关系表
     *
     * @param userIds
     * @param positionId
     * @return
     */
    @PostMapping("/savePositionUser")
    public Result<String> saveUserPosition(@RequestParam(name = "userIds") String userIds,
                                           @RequestParam(name = "positionId") String positionId) {
        userPositionService.saveUserPosition(userIds, positionId);
        return Result.ok("添加成功");
    }

    /**
     * 职位列表移除成员
     *
     * @param userIds
     * @param positionId
     * @return
     */
    @DeleteMapping("/removePositionUser")
    public Result<String> removeUserPosition(@RequestParam(name = "userIds") String userIds,
                                             @RequestParam(name = "positionId") String positionId) {
        userPositionService.removePositionUser(userIds, positionId);
        return Result.OK("移除成员成功");
    }
}
