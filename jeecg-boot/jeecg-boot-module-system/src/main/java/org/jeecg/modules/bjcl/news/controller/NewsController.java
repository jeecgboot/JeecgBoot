package org.jeecg.modules.bjcl.news.controller;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bjcl.news.entity.News;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.bjcl.news.service.INewsService;
import org.jeecg.modules.bjcl.news.service.impl.NewsServiceImpl;
import org.jeecg.modules.bjcl.util.FreeMarkerUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date: 2019-07-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "新闻")
@RestController
@RequestMapping("/bjcl/news")
public class NewsController {
    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private FreeMarkerUtil freeMarkerUtil;

    @Value("${jeecg.path.url}")
    private String url;

    /**
     * 分页列表查询
     *
     * @param news
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "新闻-分页列表查询")
    @ApiOperation(value = "新闻-分页列表查询", notes = "新闻-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<News>> queryPageList(News news,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             HttpServletRequest req) {
        Result<IPage<News>> result = new Result<IPage<News>>();
        QueryWrapper<News> queryWrapper = QueryGenerator.initQueryWrapper(news, req.getParameterMap());
        queryWrapper.orderByAsc("sort");
        Page<News> page = new Page<News>(pageNo, pageSize);
        IPage<News> pageList = newsService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param news
     * @return
     */
    @AutoLog(value = "新闻-添加")
    @ApiOperation(value = "新闻-添加", notes = "新闻-添加")
    @PostMapping(value = "/add")
    public Result<News> add(@RequestBody News news) {
        Result<News> result = new Result<News>();
        try {
            news.setPage(getPage());
            news.setUpdateTime(new Date());
            news.setUrl(url + "news/" + news.getPage() + ".html");
            newsService.save(news);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        buiderPage();
        buiderPages();
        return result;
    }

    /**
     * 编辑
     *
     * @param news
     * @return
     */
    @AutoLog(value = "新闻-编辑")
    @ApiOperation(value = "新闻-编辑", notes = "新闻-编辑")
    @PutMapping(value = "/edit")
    public Result<News> edit(@RequestBody News news) {
        Result<News> result = new Result<News>();
        News newsEntity = newsService.getById(news.getId());
        if (newsEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = newsService.updateById(news);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }
        buiderPage();
        buiderPages();
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "新闻-通过id删除")
    @ApiOperation(value = "新闻-通过id删除", notes = "新闻-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            newsService.removeById(id);
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
    @AutoLog(value = "新闻-批量删除")
    @ApiOperation(value = "新闻-批量删除", notes = "新闻-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<News> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<News> result = new Result<News>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.newsService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "新闻-通过id查询")
    @ApiOperation(value = "新闻-通过id查询", notes = "新闻-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<News> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<News> result = new Result<News>();
        News news = newsService.getById(id);
        if (news == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(news);
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
        QueryWrapper<News> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                News news = JSON.parseObject(deString, News.class);
                queryWrapper = QueryGenerator.initQueryWrapper(news, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<News> pageList = newsService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "新闻列表");
        mv.addObject(NormalExcelConstants.CLASS, News.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("新闻列表数据", "导出人:Jeecg", "导出信息"));
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
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<News> listNewss = ExcelImportUtil.importExcel(file.getInputStream(), News.class, params);
                newsService.saveBatch(listNewss);
                return Result.ok("文件导入成功！数据行数:" + listNewss.size());
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
        return Result.ok("文件导入失败！");
    }

    public void buiderPage() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<News> list = newsService.list(queryWrapper);
        int page = 10000;
        for (int i = 0; i < list.size(); i++) {
            News news = list.get(i);
            page++;
            if (i == 0) {
                news.setPage(page);
                news.setUrl(url+"/news/"+news.getPage() + ".html");
                news.setUpTitle("");
                news.setUpUrl("");
                news.setDownTitle("");
                news.setDownUrl("");
                if (list.size() > 1) {
                    news.setDownTitle(list.get(i + 1).getTitle());
                    news.setDownUrl(url+"/news/"+page + 1 + ".html");
                }
            } else if (i == list.size() - 1) {
                news.setPage(page);
                news.setUrl(url+"/news/"+news.getPage() + ".html");
                news.setUpTitle("");
                news.setUpUrl("");
                news.setDownTitle("");
                news.setDownUrl("");
                if (list.size() > 1) {
                    news.setUpTitle(list.get(i - 1).getTitle());
                    news.setUpUrl(url+"/news/"+(page - 1) + ".html");
                }
            } else {
                news.setPage(page);
                news.setUrl(url+"/news/"+news.getPage() + ".html");
                news.setUpTitle(list.get(i - 1).getTitle());
                news.setUpUrl(url+"/news/"+(page - 1) + ".html");
                news.setDownTitle(list.get(i + 1).getTitle());
                news.setDownUrl(url+"/news/"+(page + 1) + ".html");
            }
            newsService.updateById(news);
            Map<String, Object> data = convertBeanToMap(list.get(i));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String updateTime = formatter.format(data.get("updateTime"));
            data.put("updateTime", updateTime);
            //生成页面
            File file = new File("E:/static/news/"+data.get("page")+".html");
            freeMarkerUtil.freeMarkerContent(data, "bjcl/news/news.ftl",file);
        }
    }

    public void buiderPages() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        int count = newsService.count();
        int pages = count / 6;
        if (count % 6 != 0) {
            pages++;
        }
        for (int i = 1; i <= pages; i++) {
            Map<String, Object> data = new HashMap<>();
            Page<News> page1 = new Page<News>(i, 6);
            IPage<News> pageList = newsService.page(page1, queryWrapper);
            List<News> list = pageList.getRecords();
            for (int j = 0; j < list.size();j++) {
                String updateTime = formatter.format(list.get(j).getUpdateTime());
                String content = list.get(j).getContent();
                if (content.length() > 100) {
                    content = content.substring(0, 100) + "...";
                }
                list.get(j).setContent(content);
                list.get(j).setUpdateTimeStr(updateTime);
            }
            List<Integer> pagesList = new ArrayList<>();
            if(pages<=5){
                for(int k =1;k<=pages;k++){
                    pagesList.add(k);
                }
            }else {
                for (int j = i; j < i + 5; j++) {
                    if (i == 1) {
                        pagesList.add(j);
                    } else if (i == 2) {
                        pagesList.add(j - 1);
                    } else if (i > 2 && pages <= 5) {
                        pagesList.add(j - 2);
                    } else if (i > 2 && pages >= 5 && i != pages && i + 1 != pages) {
                        pagesList.add(j - 2);
                    } else if (i == pages && pages >= 5) {
                        pagesList.add(j - 4);
                    } else if (i + 1 == pages && pages >= 5) {
                        pagesList.add(j - 3);
                    }
                }
            }
            //分页显示
            data.put("pagesList",pagesList);
            //上一页下一页
            if(i>1){
                data.put(url+"news/"+"upPage",(i-1)+".html");
            }
            if(i<pages){
                data.put(url+"news/"+"downPage",(i+1)+"html");
            }

            data.put("home",url+"news/page_1.html");
            data.put("pages",url+"news/page_"+pages+".html");
            data.put("current",i);
            //页面list内容
            data.put("newList",list);
            //页面名称
            data.put("page","page_"+i);
            data.put("url",url);
            //生成页面
            File file = new File("E:/static/news/"+data.get("page")+".html");
            freeMarkerUtil.freeMarkerContent(data, "bjcl/news/page.ftl",file);
        }
    }

    public Integer getPage() {
        Integer page = newsService.getPage();
        if (page != null) {
            return page + 1;
        } else {
            return getPage();
        }
    }

    /**
     *  * 实体类转map
     *  * @param obj
     *  * @return
     *  
     */
    public static Map<String, Object> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        map.put(key, "");
                    } else {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
        }
        return map;
    }
}
