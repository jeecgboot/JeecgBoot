package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeecg.dingtalk.api.core.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.constant.enums.NoticeTypeEnum;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.message.enums.RangeDateEnum;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.jeecg.modules.system.service.impl.ThirdAppDingtalkServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppWechatEnterpriseServiceImpl;
import org.jeecg.modules.system.util.XssUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.jeecg.common.constant.CommonConstant.ANNOUNCEMENT_SEND_STATUS_1;

/**
 * @Title: Controller
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/annountCement")
@Slf4j
public class SysAnnouncementController {
	@Autowired
	private ISysAnnouncementService sysAnnouncementService;
	@Autowired
	private ISysAnnouncementSendService sysAnnouncementSendService;
	@Resource
    private WebSocket webSocket;
	@Autowired
    ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
	@Autowired
    ThirdAppDingtalkServiceImpl dingtalkService;
	@Autowired
	private SysBaseApiImpl sysBaseApi;
	@Autowired
	@Lazy
	private RedisUtil redisUtil;
	@Autowired
	public RedisTemplate redisTemplate;
	//常规报错定义
	private static final String SPECIAL_CHAR_ERROR = "保存失败:消息内容包含数据库不支持的特殊字符，请检查并修改内容!";
	private static final String CONTENT_TOO_LONG_ERROR = "保存失败:消息内容超过最大长度限制，请缩减内容长度!";
	private static final String DEFAULT_ERROR = "操作失败，请稍后重试或联系管理员!";
	/**
	 * 通告缓存
	 */
	String ANNO_CACHE_KEY = "sys:cache:announcement";
	/**
	 * QQYUN-5072【性能优化】线上通知消息打开有点慢
	 */
	public static ExecutorService cachedThreadPool = new ThreadPoolExecutor(0, 1024,60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	public static ExecutorService completeNoteThreadPool = new ThreadPoolExecutor(0, 1024,60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

	/**
	  * 分页列表查询
	 * @param sysAnnouncement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysAnnouncement>> queryPageList(SysAnnouncement sysAnnouncement,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			sysAnnouncement.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		Result<IPage<SysAnnouncement>> result = new Result<IPage<SysAnnouncement>>();
		sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
		QueryWrapper<SysAnnouncement> queryWrapper = QueryGenerator.initQueryWrapper(sysAnnouncement, req.getParameterMap());
		Page<SysAnnouncement> page = new Page<SysAnnouncement>(pageNo,pageSize);
		IPage<SysAnnouncement> pageList = sysAnnouncementService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param sysAnnouncement
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysAnnouncement> add(@RequestBody SysAnnouncement sysAnnouncement) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		try {
			// update-begin-author:liusq date:20210804 for:标题处理xss攻击的问题
			String title = XssUtils.scriptXss(sysAnnouncement.getTitile());
			sysAnnouncement.setTitile(title);
			// update-end-author:liusq date:20210804 for:标题处理xss攻击的问题
			sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            //未发布
			sysAnnouncement.setSendStatus(CommonSendStatus.UNPUBLISHED_STATUS_0);
            //流程状态
			sysAnnouncement.setBpmStatus("1");
            sysAnnouncement.setNoticeType(NoticeTypeEnum.NOTICE_TYPE_SYSTEM.getValue());
			sysAnnouncementService.saveAnnouncement(sysAnnouncement);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500(determineErrorMessage(e));
		}
		return result;
	}

	/**
	  *  编辑
	 * @param sysAnnouncement
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<SysAnnouncement> eidt(@RequestBody SysAnnouncement sysAnnouncement) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncementEntity = sysAnnouncementService.getById(sysAnnouncement.getId());
		try{
			if(sysAnnouncementEntity==null) {
				result.error500("未找到对应实体");
			}else {
				// update-begin-author:liusq date:20210804 for:标题处理xss攻击的问题
				String title = XssUtils.scriptXss(sysAnnouncement.getTitile());
				sysAnnouncement.setTitile(title);
				// update-end-author:liusq date:20210804 for:标题处理xss攻击的问题
				sysAnnouncement.setNoticeType(NoticeTypeEnum.NOTICE_TYPE_SYSTEM.getValue());
				boolean ok = sysAnnouncementService.upDateAnnouncement(sysAnnouncement);
				//TODO 返回false说明什么？
				if(ok) {
					result.success("修改成功!");
				}
			}
		} catch (Exception e) {
			result.error500(determineErrorMessage(e));
		}

		return result;
	}
	/**
	  *  简单编辑
	 * @param sysAnnouncement
	 * @return
	 */
	@RequestMapping(value = "/editIzTop", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<SysAnnouncement> editIzTop(@RequestBody SysAnnouncement sysAnnouncement) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncementEntity = sysAnnouncementService.getById(sysAnnouncement.getId());
		if(sysAnnouncementEntity==null) {
			result.error500("未找到对应实体");
		}else {
			Integer izTop = sysAnnouncement.getIzTop();
			sysAnnouncementEntity.setIzTop(oConvertUtils.getInt(izTop,CommonConstant.IZ_TOP_0));
			sysAnnouncementService.updateById(sysAnnouncementEntity);
			result.success("修改成功!");
		}
		return result;
	}

	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysAnnouncement> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
		if(sysAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
			sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_1.toString());
			boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
			if(ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysAnnouncement> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			String[] id = ids.split(",");
			for(int i=0;i<id.length;i++) {
				SysAnnouncement announcement = sysAnnouncementService.getById(id[i]);
				announcement.setDelFlag(CommonConstant.DEL_FLAG_1.toString());
				sysAnnouncementService.updateById(announcement);
			}
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<SysAnnouncement> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
		if(sysAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysAnnouncement);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 *	 更新发布操作
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/doReleaseData", method = RequestMethod.GET)
	public Result<SysAnnouncement> doReleaseData(@RequestParam(name="id",required=true) String id, HttpServletRequest request) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
		if(sysAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
            //发布中
			sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);
			sysAnnouncement.setSendTime(new Date());
			String currentUserName = JwtUtil.getUserNameByToken(request);
			sysAnnouncement.setSender(currentUserName);
			boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
            if(oConvertUtils.isEmpty(sysAnnouncement.getNoticeType())){
                sysAnnouncement.setNoticeType(NoticeTypeEnum.NOTICE_TYPE_SYSTEM.getValue());
            }
			if(ok) {
				result.success("系统通知推送成功");
				if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
					// 补全公告和用户之前的关系
					sysAnnouncementService.batchInsertSysAnnouncementSend(sysAnnouncement.getId(), sysAnnouncement.getTenantId());
					
					// 推送websocket通知
					JSONObject obj = new JSONObject();
			    	obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
					obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
					obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
					obj.put(CommonConstant.NOTICE_TYPE, sysAnnouncement.getNoticeType());
			    	webSocket.sendMessage(obj.toJSONString());
				}else {
					// 2.插入用户通告阅读标记表记录
					String userId = sysAnnouncement.getUserIds();
					String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
					String anntId = sysAnnouncement.getId();
					Date refDate = new Date();
					JSONObject obj = new JSONObject();
			    	obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
					obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
					obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
                    obj.put(CommonConstant.NOTICE_TYPE, sysAnnouncement.getNoticeType());
			    	webSocket.sendMessage(userIds, obj.toJSONString());
				}
				try {
					// 同步企业微信、钉钉的消息通知
					Response<String> dtResponse = dingtalkService.sendActionCardMessage(sysAnnouncement, null, true);
					wechatEnterpriseService.sendTextCardMessage(sysAnnouncement, true);

					if (dtResponse != null && dtResponse.isSuccess()) {
						String taskId = dtResponse.getResult();
						sysAnnouncement.setDtTaskId(taskId);
						sysAnnouncementService.updateById(sysAnnouncement);
					}
				} catch (Exception e) {
					log.error("同步发送第三方APP消息失败：", e);
				}
			}
		}

		return result;
	}

	/**
	 *	 更新撤销操作
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/doReovkeData", method = RequestMethod.GET)
	public Result<SysAnnouncement> doReovkeData(@RequestParam(name="id",required=true) String id, HttpServletRequest request) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
		if(sysAnnouncement==null) {
			result.error500("未找到对应实体");
		}else {
            //撤销发布
			sysAnnouncement.setSendStatus(CommonSendStatus.REVOKE_STATUS_2);
			sysAnnouncement.setCancelTime(new Date());
			boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
			if(ok) {
				result.success("该系统通知撤销成功");
				if (oConvertUtils.isNotEmpty(sysAnnouncement.getDtTaskId())) {
					try {
						dingtalkService.recallMessage(sysAnnouncement.getDtTaskId());
					} catch (Exception e) {
						log.error("第三方APP撤回消息失败：", e);
					}
				}
			}
		}

		return result;
	}

	/**
	 * @功能：补充用户数据，并返回系统消息
	 * @return
	 */
	@RequestMapping(value = "/listByUser", method = RequestMethod.GET)
	public Result<Map<String, Object>> listByUser(@RequestParam(required = false, defaultValue = "5") Integer pageSize, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String,Object> sysMsgMap = new HashMap(5);
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();


		//update-begin---author:scott ---date:2024-05-11  for：【性能优化】优化系统通知，只查近2个月的通知---
		// 获取上个月的第一天（只查近两个月的通知）
		Date lastMonthStartDay = DateRangeUtils.getLastMonthStartDay();
		log.info("-----查询近两个月收到的未读通知-----，近2月的第一天：{}", lastMonthStartDay);
		//update-end---author:scott ---date::2024-05-11  for：【性能优化】优化系统通知，只查近2个月的通知---
		
//		//补推送数据（用户和通知的关系表）
//		completeNoteThreadPool.execute(()->{
//			sysAnnouncementService.completeAnnouncementSendInfo();
//		});
		
		// 2.查询用户未读的系统消息
		Page<SysAnnouncement> anntMsgList = new Page<SysAnnouncement>(0, pageSize);
        //通知公告消息
		anntMsgList = sysAnnouncementService.querySysCementPageByUserId(anntMsgList,userId,"1",null, lastMonthStartDay);
		sysMsgMap.put("anntMsgList", anntMsgList.getRecords());
		sysMsgMap.put("anntMsgTotal", anntMsgList.getTotal());

		log.info("begin 获取用户近2个月的系统公告 (通知)" + (System.currentTimeMillis() - start) + "毫秒");
		
        //系统消息
		Page<SysAnnouncement> sysMsgList = new Page<SysAnnouncement>(0, pageSize);
		sysMsgList = sysAnnouncementService.querySysCementPageByUserId(sysMsgList,userId,"2",null, lastMonthStartDay);
		sysMsgMap.put("sysMsgList", sysMsgList.getRecords());
		sysMsgMap.put("sysMsgTotal", sysMsgList.getTotal());

		log.info("end 获取用户2个月的系统公告 (系统消息)" + (System.currentTimeMillis() - start) + "毫秒");
		
		result.setSuccess(true);
		result.setResult(sysMsgMap);
		return result;
	}


	/**
	 * 获取未读消息通知数量
	 *
	 * @return
	 */
	@RequestMapping(value = "/getUnreadMessageCount", method = RequestMethod.GET)
	public Result<Map<String, Integer>> getUnreadMessageCount(@RequestParam(required = false, defaultValue = "5") Integer pageSize, HttpServletRequest request) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();

		// 获取上个月的第一天（只查近两个月的通知）
		Date lastMonthStartDay = DateRangeUtils.getLastMonthStartDay();
		log.info(" ------查询近两个月收到的未读通知消息数量------，近2月的第一天：{}", lastMonthStartDay);
        //update-begin---author:wangshuai---date:2025-06-26---for:【QQYUN-12162】OA项目改造，系统重消息拆分，目前消息都在一起 需按分类进行拆分---
        Map<String,Integer> unreadMessageCount = new HashMap<>();
        //系统消息数量
        Integer systemCount = sysAnnouncementService.getUnreadMessageCountByUserId(userId, lastMonthStartDay, NoticeTypeEnum.NOTICE_TYPE_SYSTEM.getValue());
        unreadMessageCount.put("systemCount",systemCount);
        //流程数量
        Integer flowCount = sysAnnouncementService.getUnreadMessageCountByUserId(userId, lastMonthStartDay, NoticeTypeEnum.NOTICE_TYPE_FLOW.getValue());
        unreadMessageCount.put("flowCount",flowCount);
        //文件数量
        Integer fileCount = sysAnnouncementService.getUnreadMessageCountByUserId(userId, lastMonthStartDay,  NoticeTypeEnum.NOTICE_TYPE_FILE.getValue());
        unreadMessageCount.put("fileCount",fileCount);
        //日程计划数量
        Integer planCount = sysAnnouncementService.getUnreadMessageCountByUserId(userId, lastMonthStartDay,  NoticeTypeEnum.NOTICE_TYPE_PLAN.getValue());
        unreadMessageCount.put("planCount",planCount);
        Integer count = systemCount + flowCount + fileCount + planCount;
        unreadMessageCount.put("count",count);
        //update-end---author:wangshuai---date:2025-06-26---for:【QQYUN-12162】OA项目改造，系统重消息拆分，目前消息都在一起 需按分类进行拆分---
        return Result.ok(unreadMessageCount);
	}


    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysAnnouncement sysAnnouncement,HttpServletRequest request) {
        // Step.1 组装查询条件
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<SysAnnouncement>(sysAnnouncement);
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		queryWrapper.eq(SysAnnouncement::getDelFlag,CommonConstant.DEL_FLAG_0.toString());
        List<SysAnnouncement> pageList = sysAnnouncementService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "系统通告列表");
        mv.addObject(NormalExcelConstants.CLASS, SysAnnouncement.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("系统通告列表数据", "导出人:"+user.getRealname(), "导出信息"));
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
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SysAnnouncement> listSysAnnouncements = ExcelImportUtil.importExcel(file.getInputStream(), SysAnnouncement.class, params);
                for (SysAnnouncement sysAnnouncementExcel : listSysAnnouncements) {
                	if(sysAnnouncementExcel.getDelFlag()==null){
                		sysAnnouncementExcel.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
					}
                	if(oConvertUtils.isEmpty(sysAnnouncementExcel.getIzTop())){
                		sysAnnouncementExcel.setIzTop(CommonConstant.IZ_TOP_0);
					}
                    sysAnnouncementService.save(sysAnnouncementExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysAnnouncements.size());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败！");
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }
	/**
	 *同步消息
	 * @param anntId
	 * @return
	 */
	@RequestMapping(value = "/syncNotic", method = RequestMethod.GET)
	public Result<SysAnnouncement> syncNotic(@RequestParam(name="anntId",required=false) String anntId, HttpServletRequest request) {
		Result<SysAnnouncement> result = new Result<SysAnnouncement>();
		JSONObject obj = new JSONObject();
		if(StringUtils.isNotBlank(anntId)){
			SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(anntId);
			if(sysAnnouncement==null) {
				result.error500("未找到对应实体");
			}else {
				if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
					obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
					obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
					obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
					webSocket.sendMessage(obj.toJSONString());
				}else {
					// 2.插入用户通告阅读标记表记录
					String userId = sysAnnouncement.getUserIds();
					if(oConvertUtils.isNotEmpty(userId)){
						String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
						obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
						obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
						obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
						webSocket.sendMessage(userIds, obj.toJSONString());
					}
				}
			}
		}else{
			obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
			obj.put(WebsocketConst.MSG_TXT, "批量设置已读");
			webSocket.sendMessage(obj.toJSONString());
		}
		return result;
	}

	/**
	 * 通告查看详情页面（用于第三方APP）
	 * @param modelAndView
	 * @param id
	 * @return
	 */
    @GetMapping("/show/{id}")
    public ModelAndView showContent(ModelAndView modelAndView, @PathVariable("id") String id, HttpServletRequest request) {
        SysAnnouncement announcement = sysAnnouncementService.getById(id);
        if (announcement != null) {
            boolean tokenOk = false;
            try {
                // 验证Token有效性
                tokenOk = TokenUtils.verifyToken(request, sysBaseApi, redisUtil);
            } catch (Exception ignored) {
            }
            // 判断是否传递了Token，并且Token有效，如果传了就不做查看限制，直接返回
            // 如果Token无效，就做查看限制：只能查看已发布的
            if (tokenOk || ANNOUNCEMENT_SEND_STATUS_1.equals(announcement.getSendStatus())) {
                modelAndView.addObject("data", announcement);
                modelAndView.setViewName("announcement/showContent");
                return modelAndView;
            }
        }
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

	/**
	 * 【vue3用】 消息列表查询
	 * @param fromUser
	 * @param busType
	 * @param starFlag
	 * @param msgCategory
	 * @param beginDate
	 * @param endDate
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/vue3List", method = RequestMethod.GET)
	public Result<List<SysAnnouncement>> vue3List(@RequestParam(name="fromUser", required = false) String fromUser,
												  @RequestParam(name="busType", required = false) String busType,
												  @RequestParam(name="starFlag", required = false) String starFlag,
												  @RequestParam(name="msgCategory", required = false) String msgCategory,
                                                  @RequestParam(name="rangeDateKey", required = false) String rangeDateKey,
                                                  @RequestParam(name="beginDate", required = false) String beginDate, 
												  @RequestParam(name="endDate", required = false) String endDate,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, 
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                  @RequestParam(name= "noticeType", required = false) String noticeType) {
		long calStartTime = System.currentTimeMillis(); // 记录开始时间
		
		// 1、获取日期查询条件，开始时间和结束时间
		Date beginTime = null, endTime = null;
		if (RangeDateEnum.ZDY.getKey().equals(rangeDateKey)) {
			// 自定义日期范围查询
			if (oConvertUtils.isNotEmpty(beginDate)) {
				beginTime = DateUtils.parseDatetime(beginDate);
			}
			if (oConvertUtils.isNotEmpty(endDate)) {
				endTime = DateUtils.parseDatetime(endDate);
			}
		} else {
			// 日期段落查询
			Date[] arr = RangeDateEnum.getRangeArray(rangeDateKey);
			if (arr != null) {
				beginTime = arr[0];
				endTime = arr[1];	
			}
		}
		
		// 2、根据条件查询用户的通知消息
		List<SysAnnouncement> ls = this.sysAnnouncementService.querySysMessageList(pageSize, pageNo, fromUser, starFlag,busType, msgCategory, beginTime, endTime, noticeType);

		// 3、设置当前页的消息为已读
		if (!CollectionUtils.isEmpty(ls)) {
			// 设置已读
			String readed = "1";
			List<String> annoceIdList = ls.stream().filter(item -> !readed.equals(item.getReadFlag())).map(item -> item.getId()).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(annoceIdList)) {
				cachedThreadPool.execute(() -> {
					sysAnnouncementService.updateReaded(annoceIdList);
				});
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		webSocket.sendMessage(sysUser.getId(), obj.toJSONString());

		// 4、性能统计耗时
		long calEndTime = System.currentTimeMillis(); // 记录结束时间
		long duration = calEndTime - calStartTime; // 计算耗时
		log.info("耗时：" + duration + " 毫秒");
		
		return Result.ok(ls);
	}


    /**
     * 根据用户id获取最新一条消息发送时间(创建时间)
     * @param userId
     * @return
     */
	@GetMapping("/getLastAnnountTime")
	public Result<Page<SysAnnouncementSend>> getLastAnnountTime(@RequestParam(name = "userId") String userId,@RequestParam(name="noticeType",required = false) String noticeType){
		Result<Page<SysAnnouncementSend>> result = new Result<>();
		//----------------------------------------------------------------------------------------
		// step.1 此接口过慢，可以采用缓存一小时方案
		String keyString = String.format(CommonConstant.CACHE_KEY_USER_LAST_ANNOUNT_TIME_1HOUR + "_" + noticeType, userId);
		if (redisTemplate.hasKey(keyString)) {
			log.info("[SysAnnouncementSend Redis] 通过Redis缓存查询用户最后一次收到系统通知时间，userId={}", userId);
			Page<SysAnnouncementSend> pageList = (Page<SysAnnouncementSend>) redisTemplate.opsForValue().get(keyString);
			result.setSuccess(true);
			result.setResult(pageList);
			return result;
		}
		//----------------------------------------------------------------------------------------

		Page<SysAnnouncementSend> page = new Page<>(1,1);
        LambdaQueryWrapper<SysAnnouncementSend> query = new LambdaQueryWrapper<>();
        query.eq(SysAnnouncementSend::getUserId,userId);
        //只查询上个月和本月，的通知的数据
		query.ne(SysAnnouncementSend::getCreateTime, DateRangeUtils.getLastMonthStartDay());
        query.select(SysAnnouncementSend::getCreateTime); // 提高查询效率
        query.orderByDesc(SysAnnouncementSend::getCreateTime);
        Page<SysAnnouncementSend> pageList = sysAnnouncementSendService.page(page, query);

		//----------------------------------------------------------------------------------------
		if (pageList != null && pageList.getSize() > 0) {
			// step.3 保留1小时redis缓存
			redisTemplate.opsForValue().set(keyString, pageList, 3600, TimeUnit.SECONDS);
		}
		//----------------------------------------------------------------------------------------

		result.setSuccess(true);
		result.setResult(pageList);
        return result;
    }

	/**
	 * 清除当前用户所有未读消息
	 * @return
	 */
	@PostMapping("/clearAllUnReadMessage")
    public Result<String> clearAllUnReadMessage(){
		sysAnnouncementService.clearAllUnReadMessage();
		return Result.ok("清除未读消息成功");
	}

	/**
	 * 添加访问次数
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/addVisitsNumber", method = RequestMethod.GET)
	public Result<?> addVisitsNumber(@RequestParam(name="id",required=true) String id) {
		int count = oConvertUtils.getInt(redisUtil.get(ANNO_CACHE_KEY+id),0) + 1;
		redisUtil.set(ANNO_CACHE_KEY+id, count);

		if (count % 5 == 0) {
			cachedThreadPool.execute(() -> {
				sysAnnouncementService.updateVisitsNum(id, count);
			});
			// 重置访问次数
			redisUtil.del(ANNO_CACHE_KEY+id);
		}
		return Result.ok("公告消息访问次数+1次");
	}

	/**
	 * 根据异常信息确定友好的错误提示
	 */
	private String determineErrorMessage(Exception e) {
		String errorMsg = e.getMessage();
		if (isSpecialCharacterError(errorMsg)) {
			return SPECIAL_CHAR_ERROR;
		} else if (isContentTooLongError(errorMsg)) {
			return CONTENT_TOO_LONG_ERROR;
		} else {
			return DEFAULT_ERROR;
		}
	}
	/**
	 * 判断是否为特殊字符错误
	 */
	private boolean isSpecialCharacterError(String errorMsg) {
		return errorMsg != null
				&& errorMsg.contains("Incorrect string value")
				&& errorMsg.contains("column 'msg_content'");
	}

	/**
	 * 判断是否为内容过长错误
	 */
	private boolean isContentTooLongError(String errorMsg) {
		return errorMsg != null
				&& errorMsg.contains("Data too long for column 'msg_content'");
	}
}
