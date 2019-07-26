package org.jeecg.modules.bjcl.product.controller;

import java.io.File;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.bjcl.news.entity.News;
import org.jeecg.modules.bjcl.product.entity.Product;
import org.jeecg.modules.bjcl.product.service.IProductService;
import org.jeecg.modules.bjcl.util.FreeMarkerUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 产品
 * @Author: jeecg-boot
 * @Date:   2019-07-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="产品")
@RestController
@RequestMapping("/bjcl/product")
public class ProductController {
	@Autowired
	private IProductService productService;
	 @Autowired
	 private FreeMarkerUtil freeMarkerUtil;

	 @Value("${jeecg.path.url}")
	 private String url;
	/**
	  * 分页列表查询
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "产品-分页列表查询")
	@ApiOperation(value="产品-分页列表查询", notes="产品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Product>> queryPageList(Product product,
												@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												HttpServletRequest req) {
		Result<IPage<Product>> result = new Result<IPage<Product>>();
		QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, req.getParameterMap());
		Page<Product> page = new Page<Product>(pageNo, pageSize);
		IPage<Product> pageList = productService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param product* @return
	 */
	@AutoLog(value = "产品-添加")
	@ApiOperation(value="产品-添加", notes="产品-添加")
	@PostMapping(value = "/add")
	public Result<Product> add(@RequestBody Product product) {
		Result<Product> result = new Result<Product>();
		try {
			productService.save(product);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		buiderPages();
		return result;
	}
	
	/**
	  *  编辑
	 * @param product
	 * @return
	 */
	@AutoLog(value = "产品-编辑")
	@ApiOperation(value="产品-编辑", notes="产品-编辑")
	@PutMapping(value = "/edit")
	public Result<Product> edit(@RequestBody Product product) {
		Result<Product> result = new Result<Product>();
		Product productEntity = productService.getById(product.getId());
		if(productEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = productService.updateById(product);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		buiderPages();
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品-通过id删除")
	@ApiOperation(value="产品-通过id删除", notes="产品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			productService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品-批量删除")
	@ApiOperation(value="产品-批量删除", notes="产品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Product> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Product> result = new Result<Product>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.productService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品-通过id查询")
	@ApiOperation(value="产品-通过id查询", notes="产品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Product> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Product> result = new Result<Product>();
		Product product = productService.getById(id);
		if(product==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(product);
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
      QueryWrapper<Product> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Product product = JSON.parseObject(deString, Product.class);
              queryWrapper = QueryGenerator.initQueryWrapper(product, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Product> pageList = productService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "产品列表");
      mv.addObject(NormalExcelConstants.CLASS, Product.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("产品列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Product> listProducts = ExcelImportUtil.importExcel(file.getInputStream(), Product.class, params);
              productService.saveBatch(listProducts);
              return Result.ok("文件导入成功！数据行数:" + listProducts.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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

  public void buiderPages(){
	  QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
	  queryWrapper.orderByAsc("sort");
	  int count = productService.count();
	  int pages = count / 8;
	  if (count % 8 != 0) {
		  pages++;
	  }
	  for (int i = 1;i<=pages;i++){
	  	Map<String,Object> data = new HashMap<>();
	  	Page<Product> page = new Page<>(i, 8);
	  	IPage<Product> pageList = productService.page(page, queryWrapper);
	  	List<Product> list = pageList.getRecords();
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
			  data.put(url+"products/"+"upPage",(i-1)+".html");
		  }
		  if(i<pages){
			  data.put(url+"products/"+"downPage",(i+1)+"html");
		  }

		  data.put("home",url+"products/page_1.html");
		  data.put("pages",url+"products/page_"+pages+".html");
		  data.put("current",i);
		  //页面list内容
		  data.put("productList",list);
		  //页面名称
		  data.put("page","page_"+i);
		  data.put("url",url);
		  //生成页面
		  File file = new File("E:/static/products/"+data.get("page")+".html");
		  freeMarkerUtil.freeMarkerContent(data, "bjcl/products/page.ftl",file);
	  }
  }
}
