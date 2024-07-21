package com.bomaos.common.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.utils.CoreUtil;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.common.core.web.JsonResult;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.Dictionary;
import com.bomaos.common.system.service.DictionaryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典管理
 * Created by Panyoujie on 2020-03-14 11:29:03
 */
@Controller
@RequestMapping("/sys/dict")
public class DictionaryController extends BaseController {
    @Autowired
    private DictionaryService dictionaryService;

    @RequiresPermissions("sys:dict:view")
    @RequestMapping()
    public String view() {
        return "system/dictionary.html";
    }

    /**
     * 分页查询字典
     */
    @OperLog(value = "字典管理", desc = "分页查询")
    @RequiresPermissions("sys:dict:list")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Dictionary> page(HttpServletRequest request) {
        PageParam<Dictionary> pageParam = new PageParam<>(request);
        return new PageResult<>(dictionaryService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部字典
     */
    @OperLog(value = "字典管理", desc = "查询全部")
    @RequiresPermissions("sys:dict:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Dictionary> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(dictionaryService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询字典
     */
    @OperLog(value = "字典管理", desc = "根据id查询")
    @RequiresPermissions("sys:dict:list")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(dictionaryService.getById(id));
    }

    /**
     * 添加字典
     */
    @OperLog(value = "字典管理", desc = "添加", param = false, result = true)
    @RequiresPermissions("sys:dict:save")
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Dictionary dictionary) {
        if (dictionaryService.count(new QueryWrapper<Dictionary>().eq("dict_code", dictionary.getDictCode())) > 0) {
            return JsonResult.error("字典标识已存在");
        }
        if (dictionaryService.count(new QueryWrapper<Dictionary>().eq("dict_name", dictionary.getDictName())) > 0) {
            return JsonResult.error("字典名称已存在");
        }
        if (dictionaryService.save(dictionary)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改字典
     */
    @OperLog(value = "字典管理", desc = "修改", param = false, result = true)
    @RequiresPermissions("sys:dict:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Dictionary dictionary) {
        if (dictionaryService.count(new QueryWrapper<Dictionary>().eq("dict_code", dictionary.getDictCode())
                .ne("dict_id", dictionary.getDictId())) > 0) {
            return JsonResult.error("字典代码已存在");
        }
        if (dictionaryService.count(new QueryWrapper<Dictionary>().eq("dict_name", dictionary.getDictName())
                .ne("dict_id", dictionary.getDictId())) > 0) {
            return JsonResult.error("字典名称已存在");
        }
        if (dictionaryService.updateById(dictionary)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除字典
     */
    @OperLog(value = "字典管理", desc = "删除", result = true)
    @RequiresPermissions("sys:dict:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (dictionaryService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加字典
     */
    @OperLog(value = "字典管理", desc = "批量添加", param = false, result = true)
    @RequiresPermissions("sys:dict:save")
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Dictionary> list) {
        // 对集合本身进行非空和重复校验
        StringBuilder sb = new StringBuilder();
        sb.append(CoreUtil.listCheckBlank(list, "dictCode", "字典标识"));
        sb.append(CoreUtil.listCheckBlank(list, "dictName", "字典名称"));
        sb.append(CoreUtil.listCheckRepeat(list, "dictCode", "字典标识"));
        sb.append(CoreUtil.listCheckRepeat(list, "dictName", "字典名称"));
        if (sb.length() != 0) return JsonResult.error(sb.toString());
        // 数据库层面校验
        if (dictionaryService.count(new QueryWrapper<Dictionary>().in("dict_code",
                list.stream().map(Dictionary::getDictCode).collect(Collectors.toList()))) > 0) {
            return JsonResult.error("字典标识已存在");
        }
        if (dictionaryService.count(new QueryWrapper<Dictionary>().in("dict_name",
                list.stream().map(Dictionary::getDictName).collect(Collectors.toList()))) > 0) {
            return JsonResult.error("字典名称已存在");
        }
        if (dictionaryService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量删除字典
     */
    @OperLog(value = "字典管理", desc = "批量删除", result = true)
    @RequiresPermissions("sys:dict:remove")
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (dictionaryService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
