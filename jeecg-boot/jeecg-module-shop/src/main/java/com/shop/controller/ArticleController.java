package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.entity.Article;
import com.shop.service.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章表管理
 * Created by Panyoujie on 2021-11-08 04:44:45
 */
@Controller
@RequestMapping("/content/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @RequiresPermissions("content:article:view")
    @RequestMapping()
    public String view() {
        return "content/article.html";
    }

    @RequiresPermissions("content:article:view")
    @RequestMapping("/addArticle")
    public String addView() {
        return "content/add-article.html";
    }

    /**
     * 分页查询文章表
     */
    @RequiresPermissions("content:article:list")
    @OperLog(value = "文章表管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Article> page(HttpServletRequest request) {
        PageParam<Article> pageParam = new PageParam<>(request);
        return new PageResult<>(articleService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部文章表
     */
    @RequiresPermissions("content:article:list")
    @OperLog(value = "文章表管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Article> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(articleService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询文章表
     */
    @RequiresPermissions("content:article:list")
    @OperLog(value = "文章表管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(articleService.getById(id));
    }

    /**
     * 添加文章表
     */
    @RequiresPermissions("content:article:save")
    @OperLog(value = "文章表管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Article article) {
        article.setUsername(getLoginUsername());
        if (articleService.save(article)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改文章表
     */
    @RequiresPermissions("content:article:update")
    @OperLog(value = "文章表管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Article article) {
        if (articleService.updateById(article)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 修改文章表
     */
    @RequiresPermissions("content:article:view")
    @RequestMapping("/editArticle/{articleId}")
    public String editArticle(Model model, @PathVariable("articleId") Integer articleId) {
        Article article = articleService.getById(articleId);
        model.addAttribute("article", JSON.toJSONString(article));
        return "content/edit-article.html";
    }

    /**
     * 修改文章表
     */
    @OperLog(value = "文章表管理", desc = "点赞文章", param = false, result = true)
    @ResponseBody
    @RequestMapping("/updateLike")
    public JsonResult updateLike(Integer articleId) {
        Article articleEntity = articleService.getById(articleId);
        Article article = new Article();
        article.setId(articleId);
        article.setLikes(articleEntity.getLikes() + 1);
        if (articleService.updateById(article)) {
            return JsonResult.ok("点赞成功！");
        }
        return JsonResult.error("哎呀！点赞失败了。");
    }

    /**
     * 删除文章表
     */
    @RequiresPermissions("content:article:remove")
    @OperLog(value = "文章表管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (articleService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加文章表
     */
    @RequiresPermissions("content:article:save")
    @OperLog(value = "文章表管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Article> list) {
        for (Article article : list) {
            article.setUsername(getLoginUsername());
        }
        if (articleService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改文章表
     */
    @RequiresPermissions("content:article:update")
    @OperLog(value = "文章表管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Article> batchParam) {
        if (batchParam.update(articleService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除文章表
     */
    @RequiresPermissions("content:article:remove")
    @OperLog(value = "文章表管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (articleService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改状态
     */
    @OperLog(value = "文章表管理", desc = "修改状态", result = true)
    @RequiresPermissions("content:article:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Article article = new Article();
        article.setId(id);
        article.setEnabled(status);
        if (articleService.updateById(article)) {
            return JsonResult.ok("启用成功");
        }
        return JsonResult.error("启用失败");
    }


}
