package com.bomaos.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.web.*;
import com.bomaos.common.system.entity.Organization;
import com.bomaos.common.system.service.DictionaryDataService;
import com.bomaos.common.system.service.OrganizationService;
import com.bomaos.common.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 组织机构管理
 * Created by AutoGenerator on 2020-03-14 11:29:04
 */
@Controller
@RequestMapping("/sys/organization")
public class OrganizationController extends BaseController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private DictionaryDataService dictionaryDataService;
    @Autowired
    private RoleService roleService;

    @RequiresPermissions("sys:org:view")
    @RequestMapping()
    public String view(Model model) {
        model.addAttribute("sexList", dictionaryDataService.listByDictCode("sex"));
        model.addAttribute("organizationTypeList", dictionaryDataService.listByDictCode("organization_type"));
        model.addAttribute("rolesJson", JSON.toJSONString(roleService.list()));
        return "system/organization.html";
    }

    /**
     * 分页查询组织机构
     */
    @OperLog(value = "机构管理", desc = "分页查询")
    @RequiresPermissions("sys:org:list")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Organization> page(HttpServletRequest request) {
        PageParam<Organization> pageParam = new PageParam<>(request);
        return organizationService.listPage(pageParam);
    }

    /**
     * 查询全部组织机构
     */
    @OperLog(value = "机构管理", desc = "查询全部")
    @RequiresPermissions("sys:org:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Organization> pageParam = new PageParam<>(request);
        List<Organization> records = organizationService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.sortRecords(records));
    }

    /**
     * 根据id查询组织机构
     */
    @OperLog(value = "机构管理", desc = "根据id查询")
    @RequiresPermissions("sys:org:list")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        PageParam<Organization> pageParam = new PageParam<>();
        pageParam.put("organizationId", id);
        List<Organization> records = organizationService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.getOne(records));
    }

    /**
     * 添加组织机构
     */
    @OperLog(value = "机构管理", desc = "添加", param = false, result = true)
    @RequiresPermissions("sys:org:save")
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult add(Organization organization) {
        if (organization.getParentId() == null) organization.setParentId(0);
        if (organizationService.count(new QueryWrapper<Organization>()
                .eq("organization_name", organization.getOrganizationName())
                .eq("parent_id", organization.getParentId())) > 0) {
            return JsonResult.error("机构名称已存在");
        }
        if (organizationService.save(organization)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改组织机构
     */
    @OperLog(value = "机构管理", desc = "修改", param = false, result = true)
    @RequiresPermissions("sys:org:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Organization organization) {
        if (organization.getOrganizationName() != null) {
            if (organization.getParentId() == null) organization.setParentId(0);
            if (organizationService.count(new QueryWrapper<Organization>()
                    .eq("organization_name", organization.getOrganizationName())
                    .eq("parent_id", organization.getParentId())
                    .ne("organization_id", organization.getOrganizationId())) > 0) {
                return JsonResult.error("机构名称已存在");
            }
        }
        if (organizationService.updateById(organization)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除组织机构
     */
    @OperLog(value = "机构管理", desc = "删除", result = true)
    @RequiresPermissions("sys:org:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (organizationService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加组织机构
     */
    @OperLog(value = "机构管理", desc = "批量添加", param = false, result = true)
    @RequiresPermissions("sys:org:save")
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Organization> organizationList) {
        if (organizationService.saveBatch(organizationList)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改组织机构
     */
    @OperLog(value = "机构管理", desc = "批量修改", result = true)
    @RequiresPermissions("sys:org:update")
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Organization> batchParam) {
        if (batchParam.update(organizationService, "organization_id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除组织机构
     */
    @OperLog(value = "机构管理", desc = "批量删除", result = true)
    @RequiresPermissions("sys:org:remove")
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (organizationService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
