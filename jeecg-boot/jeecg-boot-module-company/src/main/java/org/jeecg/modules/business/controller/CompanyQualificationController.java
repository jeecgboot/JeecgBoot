package org.jeecg.modules.business.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.modules.business.entity.CompanyQualification;
import org.jeecg.modules.business.service.ICompanyQualificationService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Description: 企业资质
* @Author: jeecg-boot
* @Date:   2020-06-01
* @Version: V1.0
*/
@Api(tags="企业资质")
@RestController
@RequestMapping("/companyQualification")
@Slf4j
public class CompanyQualificationController extends JeecgController<CompanyQualification, ICompanyQualificationService> {
   @Autowired
   private ICompanyQualificationService companyQualificationService;

//   /**
//    * 分页列表查询
//    *
//    * @param companyQualification
//    * @param pageNo
//    * @param pageSize
//    * @param req
//    * @return
//    */
//   @AutoLog(value = "企业资质-分页列表查询")
//   @ApiOperation(value="企业资质-分页列表查询", notes="企业资质-分页列表查询")
//   @GetMapping(value = "/list")
//   public Result<?> queryPageList(CompanyQualification companyQualification,
//                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//                                  HttpServletRequest req) {
//       QueryWrapper<CompanyQualification> queryWrapper = QueryGenerator.initQueryWrapper(companyQualification, req.getParameterMap());
//       Page<CompanyQualification> page = new Page<CompanyQualification>(pageNo, pageSize);
//       IPage<CompanyQualification> pageList = companyQualificationService.page(page, queryWrapper);
//       return Result.ok(pageList);
//   }

   /**
    *   添加
    *
    * @param companyQualification
    * @return
    */
//   @AutoLog(value = "企业资质-添加")
//   @ApiOperation(value="企业资质-添加", notes="企业资质-添加")
//   @PostMapping(value = "/add")
//   public Result<?> add(@RequestBody CompanyQualification companyQualification) {
//       companyQualificationService.save(companyQualification);
//       return Result.ok("添加成功！");
//   }
//
//   /**
//    *  编辑
//    *
//    * @param companyQualification
//    * @return
//    */
//   @AutoLog(value = "企业资质-编辑")
//   @ApiOperation(value="企业资质-编辑", notes="企业资质-编辑")
//   @PutMapping(value = "/edit")
//   public Result<?> edit(@RequestBody CompanyQualification companyQualification) {
//       companyQualificationService.updateById(companyQualification);
//       return Result.ok("编辑成功!");
//   }
//
//   /**
//    *   通过id删除
//    *
//    * @param id
//    * @return
//    */
//   @AutoLog(value = "企业资质-通过id删除")
//   @ApiOperation(value="企业资质-通过id删除", notes="企业资质-通过id删除")
//   @DeleteMapping(value = "/delete")
//   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//       companyQualificationService.removeById(id);
//       return Result.ok("删除成功!");
//   }
//
//   /**
//    *  批量删除
//    *
//    * @param ids
//    * @return
//    */
//   @AutoLog(value = "企业资质-批量删除")
//   @ApiOperation(value="企业资质-批量删除", notes="企业资质-批量删除")
//   @DeleteMapping(value = "/deleteBatch")
//   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//       this.companyQualificationService.removeByIds(Arrays.asList(ids.split(",")));
//       return Result.ok("批量删除成功!");
//   }

   /**
    * 通过id查询
    *
    * @param jsonObject 企业id
    * @return
    */
   @AutoLog(value = "企业资质-通过企业id查询")
   @ApiOperation(value="企业资质-通过企业id查询", notes="企业资质-通过企业id查询")
   @PostMapping(value = "/queryByCompanyId")
   public Result<?> queryById(@RequestBody JSONObject jsonObject) {
       Map<String, List<Map<String,String>>> result = companyQualificationService.getQualificationFiles(jsonObject.getString("companyId"));
       return Result.ok(result);
   }



    /**
   * 导出excel
   *
   * @param request
   * @param companyQualification
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, CompanyQualification companyQualification) {
       return super.exportXls(request, companyQualification, CompanyQualification.class, "企业资质");
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
       return super.importExcel(request, response, CompanyQualification.class);
   }

}
