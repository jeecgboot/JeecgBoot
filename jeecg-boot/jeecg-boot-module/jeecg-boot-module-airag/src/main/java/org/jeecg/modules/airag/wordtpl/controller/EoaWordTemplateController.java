package org.jeecg.modules.airag.wordtpl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.wordtpl.dto.WordTplGenDTO;
import org.jeecg.modules.airag.wordtpl.entity.EoaWordTemplate;
import org.jeecg.modules.airag.wordtpl.service.IEoaWordTemplateService;
import org.jeecg.modules.airag.wordtpl.utils.WordTplUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @Description: word模版管理
 * @Author: jeecg-boot
 * @Date: 2025-07-04
 * @Version: V1.0
 */
@Tag(name = "word模版管理")
@RestController("eoaWordTemplateController")
@RequestMapping("/airag/word")
@Slf4j
public class EoaWordTemplateController extends JeecgController<EoaWordTemplate, IEoaWordTemplateService> {
    @Autowired
    private IEoaWordTemplateService eoaWordTemplateService;

    @Autowired
    WordTplUtils wordTplUtils;

    /**
     * 分页列表查询
     *
     * @param eoaWordTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @Operation(summary = "word模版管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<EoaWordTemplate>> queryPageList(EoaWordTemplate eoaWordTemplate,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        QueryWrapper<EoaWordTemplate> queryWrapper = QueryGenerator.initQueryWrapper(eoaWordTemplate, req.getParameterMap());
        Page<EoaWordTemplate> page = new Page<EoaWordTemplate>(pageNo, pageSize);
        IPage<EoaWordTemplate> pageList = eoaWordTemplateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param eoaWordTemplate
     * @return
     */
    @AutoLog(value = "word模版管理-添加")
    @Operation(summary = "word模版管理-添加")
//	@RequiresPermissions("wordtpl:template:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody EoaWordTemplate eoaWordTemplate) {
        AssertUtils.assertNotEmpty("参数异常", eoaWordTemplate);
        AssertUtils.assertNotEmpty("模版名称不能为空", eoaWordTemplate.getName());
        boolean isCodeExists = eoaWordTemplateService.exists(Wrappers.lambdaQuery(EoaWordTemplate.class).eq(EoaWordTemplate::getCode, eoaWordTemplate.getCode()));
        AssertUtils.assertFalse("模版编码已存在", isCodeExists);
        eoaWordTemplateService.save(eoaWordTemplate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param eoaWordTemplate
     * @return
     */
    @AutoLog(value = "word模版管理-编辑")
    @Operation(summary = "word模版管理-编辑")
//	@RequiresPermissions("wordtpl:template:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody EoaWordTemplate eoaWordTemplate) {
        AssertUtils.assertNotEmpty("参数异常", eoaWordTemplate);
        AssertUtils.assertNotEmpty("模版名称不能为空", eoaWordTemplate.getName());
        // 避免编辑时修改编码
        eoaWordTemplate.setCode(null);
        eoaWordTemplateService.updateById(eoaWordTemplate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "word模版管理-通过id删除")
    @Operation(summary = "word模版管理-通过id删除")
//	@RequiresPermissions("wordtpl:template:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        eoaWordTemplateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "word模版管理-批量删除")
    @Operation(summary = "word模版管理-批量删除")
//	@RequiresPermissions("wordtpl:template:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.eoaWordTemplateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "word模版管理-通过id查询")
    @Operation(summary = "word模版管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<EoaWordTemplate> queryById(@RequestParam(name = "id", required = true) String id) {
        EoaWordTemplate eoaWordTemplate = eoaWordTemplateService.getById(id);
        if (eoaWordTemplate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(eoaWordTemplate);
    }

    /**
     * 下载word模版
     * @param id
     * @param response
     * @return
     * @author chenrui
     * @date 2025/7/9 14:38
     */
    @GetMapping(value = "/download")
    public void downloadTemplate(@RequestParam(name = "id", required = true) String id, HttpServletResponse response) {
        AssertUtils.assertNotEmpty("请先选择模版", id);
        EoaWordTemplate template = eoaWordTemplateService.getById(id);
        try (ByteArrayOutputStream wordTemplateOut = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
            wordTplUtils.generateWordTemplate(template, wordTemplateOut);
            String fileName = template.getName();
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.addHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".docx");
            response.addHeader("filename", encodedFileName + ".docx");
            byte[] bytes = wordTemplateOut.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JeecgBootException("下载word模版失败: " + e.getMessage(), e);
        }
    }


    /**
     * 解析word模版文件
     * @param file
     * @param id
     * @return
     * @author chenrui
     * @date 2025/7/9 14:38
     */
    @PostMapping(value = "/parse/file")
    public Result<?> parseWOrdFile(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EoaWordTemplate eoaWordTemplate = wordTplUtils.parseWordFile(inputStream);
            log.info("解析的模版信息: {}", eoaWordTemplate);
            return Result.OK("解析成功", eoaWordTemplate);
        } catch (Exception e) {
            throw new RuntimeException("解析word模版失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成word文档
     *
     * @param wordTplGenDTO
     * @param response
     * @author chenrui
     * @date 2025/7/10 15:39
     */
    @PostMapping(value = "/generate/word")
    public void generateWord(@RequestBody WordTplGenDTO wordTplGenDTO, HttpServletResponse response) {
        AssertUtils.assertNotEmpty("参数异常", wordTplGenDTO);
        EoaWordTemplate template ;
        if (oConvertUtils.isNotEmpty(wordTplGenDTO.getTemplateId())) {
            template = eoaWordTemplateService.getById(wordTplGenDTO.getTemplateId());
        }else{
            AssertUtils.assertNotEmpty("请先选择模版", wordTplGenDTO.getTemplateCode());
            template = eoaWordTemplateService.getOne(Wrappers.lambdaQuery(EoaWordTemplate.class)
                    .eq(EoaWordTemplate::getCode, wordTplGenDTO.getTemplateCode()));
        }
        AssertUtils.assertNotEmpty("未找到对应的模版", template);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
            eoaWordTemplateService.generateWordFromTpl(wordTplGenDTO, outputStream);
            String fileName = template.getName();
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.addHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".docx");
            response.addHeader("filename", encodedFileName + ".docx");
            byte[] bytes = outputStream.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JeecgBootException("生成word文档失败: " + e.getMessage(), e);
        }
    }

}
