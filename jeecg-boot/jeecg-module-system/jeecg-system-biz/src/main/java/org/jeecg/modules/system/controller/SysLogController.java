package org.jeecg.modules.system.controller;


import java.util.Arrays;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecgframework.poi.handler.inter.IExcelExportServerEnhanced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController extends JeecgController<SysLog, ISysLogService> {
	
	@Autowired
	private ISysLogService sysLogService;

    /**
     * for [issues/8699]AutoPoi在使用@ExcelEntity当设置show=true并且该项为null时报错
     */
    @Resource
    private JeecgBaseConfig jeecgBaseConfig;

    /**
     * 全部清除
     */
	private static final String ALL_ClEAR = "allclear";

	/**
	 * @功能：查询日志记录
	 * @param syslog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//@RequiresPermissions("system:log:list")
	public Result<IPage<SysLog>> queryPageList(SysLog syslog,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysLog>> result = new Result<IPage<SysLog>>();
		QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, req.getParameterMap());
		Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
		//日志关键词
		String keyWord = req.getParameter("keyWord");
		if(oConvertUtils.isNotEmpty(keyWord)) {
			queryWrapper.like("log_content",keyWord);
		}
		//TODO 过滤逻辑处理
		//TODO begin、end逻辑处理
		//TODO 一个强大的功能，前端传一个字段字符串，后台只返回这些字符串对应的字段
		//创建时间/创建人的赋值
		IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
//		log.info("查询当前页："+pageList.getCurrent());
//		log.info("查询当前页数量："+pageList.getSize());
//		log.info("查询结果数量："+pageList.getRecords().size());
//		log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	 * @功能：删除单个日志记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	//@RequiresPermissions("system:log:delete")
	public Result<SysLog> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysLog> result = new Result<SysLog>();
		SysLog sysLog = sysLogService.getById(id);
		if(sysLog==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysLogService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}
	
	/**
	 * @功能：批量，全部清空日志记录
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	//@RequiresPermissions("system:log:deleteBatch")
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysRole> result = new Result<SysRole>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			if(ALL_ClEAR.equals(ids)) {
				this.sysLogService.removeAll();
				result.success("清除成功!");
			}
			this.sysLogService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 导出excel
	 * for [QQYUN-13431]【jeecg】日志管理中添加大数据导出功能
	 * @param request
	 * @param syslog
	 */
	@RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysLog syslog) {
        // 复制参数，移除排序相关键（column/order 等）防止前端传入排序影响导出顺序
        java.util.Map<String, String[]> rawMap = request.getParameterMap();
        java.util.Map<String, String[]> paramMap = new java.util.HashMap<>(rawMap);
        // 剔除自定义排序参数
        paramMap.remove("column");
        paramMap.remove("order");
        // 组装查询条件（已剔除排序参数）
        QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, paramMap);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        // 定义IExcelExportServer
        IExcelExportServerEnhanced<SysLog> excelExportServer = new IExcelExportServerEnhanced<>() {

            @Override
            public List<SysLog> selectListForExcelExport(Object queryParams, SysLog lastRecord, int pageSize) {
                QueryWrapper<SysLog> originalWrapper = (QueryWrapper<SysLog>) queryParams;
                // 克隆原始条件，避免多次迭代污染
                QueryWrapper<SysLog> batchWrapper = null;
                try {
                    batchWrapper = (QueryWrapper<SysLog>) originalWrapper.clone();
                } catch (Exception e) {
                    batchWrapper = originalWrapper;
                }

                String lastId = null;
                if (lastRecord != null) {
                    lastId = lastRecord.getId();
                    final String cursorLastId = lastId;
                    // 仅基于雪花ID（全局唯一，数值递增）作为游标，提升索引利用与性能
                    // 条件：id < 上一批最后一条的ID，实现“从大到小”倒序分页
                    batchWrapper.lt("id", cursorLastId);
                }

                // 排序：按 id DESC（雪花ID递增，倒序可获取最新数据）
                batchWrapper.orderByDesc("id");
                Page<SysLog> cursorPage = new Page<>(1, pageSize);
                List<SysLog> list = service.page(cursorPage, batchWrapper).getRecords();

                log.info("系统日志游标导出(ID游标) - lastId: {} batchSize: {} 返回: {}", lastId, pageSize, list.size());
                if (!list.isEmpty()) {
                    SysLog endRecord = list.get(list.size() - 1);
                    log.debug("本批次最后一条记录游标ID -> id: {}", endRecord.getId());
                }
                return list;
            }

            @Override
            public int getPageSize() {
                return 10000;
            }
        };

        String title = "系统日志";
        // AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, SysLog.class);
        ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title, jeecgBaseConfig.getPath().getUpload());
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.EXPORT_SERVER, excelExportServer);
        mv.addObject(NormalExcelConstants.QUERY_PARAMS, queryWrapper);
        return mv;
    }
}
