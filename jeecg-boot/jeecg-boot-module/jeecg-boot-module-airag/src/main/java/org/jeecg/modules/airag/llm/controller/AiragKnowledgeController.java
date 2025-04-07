package org.jeecg.modules.airag.llm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.modules.airag.common.vo.knowledge.KnowledgeSearchResult;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.entity.AiragKnowledge;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.jeecg.modules.airag.llm.handler.EmbeddingHandler;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeDocService;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @Description: AIRag知识库
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/airag/knowledge")
@Slf4j
public class AiragKnowledgeController {
    @Autowired
    private IAiragKnowledgeService airagKnowledgeService;

    @Autowired
    private IAiragKnowledgeDocService airagKnowledgeDocService;

    @Autowired
    EmbeddingHandler embeddingHandler;

    /**
     * 分页列表查询知识库
     *
     * @param airagKnowledge
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<AiragKnowledge>> queryPageList(AiragKnowledge airagKnowledge,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        QueryWrapper<AiragKnowledge> queryWrapper = QueryGenerator.initQueryWrapper(airagKnowledge, req.getParameterMap());
        Page<AiragKnowledge> page = new Page<AiragKnowledge>(pageNo, pageSize);
        IPage<AiragKnowledge> pageList = airagKnowledgeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加知识库
     *
     * @param airagKnowledge 知识库
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody AiragKnowledge airagKnowledge) {
        airagKnowledge.setStatus(LLMConsts.STATUS_ENABLE);
        airagKnowledgeService.save(airagKnowledge);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑知识库
     *
     * @param airagKnowledge 知识库
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody AiragKnowledge airagKnowledge) {
        AiragKnowledge airagKnowledgeEntity = airagKnowledgeService.getById(airagKnowledge.getId());
        if (airagKnowledgeEntity == null) {
            return Result.error("未找到对应数据");
        }
        String oldEmbedId = airagKnowledgeEntity.getEmbedId();
        airagKnowledgeService.updateById(airagKnowledge);
        if (!oldEmbedId.equalsIgnoreCase(airagKnowledge.getEmbedId())) {
            // 更新了模型,重建文档
            airagKnowledgeDocService.rebuildDocumentByKnowId(airagKnowledge.getId());
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 重建知识库
     *
     * @param knowIds
     * @return
     * @author chenrui
     * @date 2025/3/12 17:05
     */
    @PutMapping(value = "/rebuild")
    public Result<?> rebuild(@RequestParam("knowIds") String knowIds) {
        String[] knowIdArr = knowIds.split(",");
        for (String knowId : knowIdArr) {
            airagKnowledgeDocService.rebuildDocumentByKnowId(knowId);
        }
        return Result.OK("");
    }

    /**
     * 通过id删除知识库
     *
     * @param id
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        airagKnowledgeDocService.removeByKnowIds(Collections.singletonList(id));
        airagKnowledgeService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除知识库
     *
     * @param ids
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idsList = Arrays.asList(ids.split(","));
        airagKnowledgeDocService.removeByKnowIds(idsList);
        airagKnowledgeService.removeByIds(idsList);
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询知识库
     *
     * @param id
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @GetMapping(value = "/queryById")
    public Result<AiragKnowledge> queryById(@RequestParam(name = "id", required = true) String id) {
        AiragKnowledge airagKnowledge = airagKnowledgeService.getById(id);
        if (airagKnowledge == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(airagKnowledge);
    }

    /**
     * 文档分页查询
     *
     * @param airagKnowledgeDoc
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @author chenrui
     * @date 2025/2/18 18:37
     */
    @GetMapping(value = "/doc/list")
    public Result<IPage<AiragKnowledgeDoc>> queryDocumentPageList(AiragKnowledgeDoc airagKnowledgeDoc,
                                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  HttpServletRequest req) {
        AssertUtils.assertNotEmpty("请先选择知识库", airagKnowledgeDoc.getKnowledgeId());
        QueryWrapper<AiragKnowledgeDoc> queryWrapper = QueryGenerator.initQueryWrapper(airagKnowledgeDoc, req.getParameterMap());
        Page<AiragKnowledgeDoc> page = new Page<>(pageNo, pageSize);
        IPage<AiragKnowledgeDoc> pageList = airagKnowledgeDocService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 新增或编辑文档
     *
     * @param airagKnowledgeDoc 知识库文档
     * @return
     * @author chenrui
     * @date 2025/2/18 15:47
     */
    @PostMapping(value = "/doc/edit")
    public Result<?> addDocument(@RequestBody AiragKnowledgeDoc airagKnowledgeDoc) {
        return airagKnowledgeDocService.editDocument(airagKnowledgeDoc);
    }


    /**
     * 从压缩包导入文档
     * @return
     * @author chenrui
     * @date 2025/3/20 11:29
     */
    @PostMapping(value = "/doc/import/zip")
    public Result<?> importDocumentFromZip(@RequestParam(name = "knowId", required = true) String knowId,
                                           @RequestParam(name = "file", required = true) MultipartFile file) {
        return airagKnowledgeDocService.importDocumentFromZip(knowId,file);
    }

    /**
     * 通过文档库查询导入任务列表
     * @param knowId
     * @return
     * @author chenrui
     * @date 2025/3/20 11:37
     */
    @GetMapping(value = "/doc/import/task/list")
    public Result<?> importDocumentTaskList(@RequestParam(name = "knowId", required = true) String knowId) {
        return Result.OK(Collections.emptyList());
    }

    /**
     * 重新向量化文档
     *
     * @param docIds 文档id集合
     * @return
     * @author chenrui
     * @date 2025/2/18 15:47
     */
    @PutMapping(value = "/doc/rebuild")
    public Result<?> rebuildDocument(@RequestParam("docIds") String docIds) {
        return airagKnowledgeDocService.rebuildDocument(docIds);
    }

    /**
     * 批量删除文档
     *
     * @param ids
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = "/doc/deleteBatch")
    public Result<String> deleteDocumentBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idsList = Arrays.asList(ids.split(","));
        airagKnowledgeDocService.removeDocByIds(idsList);
        return Result.OK("批量删除成功！");
    }


    /**
     * 命中测试
     *
     * @param knowId     知识库id
     * @param queryText  查询内容
     * @param topNumber  最多返回条数
     * @param similarity 最小分数
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @GetMapping(value = "/embedding/hitTest/{knowId}")
    public Result<?> hitTest(@PathVariable("knowId") String knowId,
                             @RequestParam(name = "queryText") String queryText,
                             @RequestParam(name = "topNumber") Integer topNumber,
                             @RequestParam(name = "similarity") Double similarity) {
        List<Map<String, Object>> searchResp = embeddingHandler.searchEmbedding(knowId, queryText, topNumber, similarity);
        return Result.ok(searchResp);
    }

    /**
     * 向量查询
     *
     * @param knowIds    知识库ids
     * @param queryText  查询内容
     * @param topNumber  最多返回条数
     * @param similarity 最小分数
     * @return
     * @author chenrui
     * @date 2025/2/18 17:09
     */
    @GetMapping(value = "/embedding/search")
    public Result<?> embeddingSearch(@RequestParam("knowIds") List<String> knowIds,
                                     @RequestParam(name = "queryText") String queryText,
                                     @RequestParam(name = "topNumber", required = false) Integer topNumber,
                                     @RequestParam(name = "similarity", required = false) Double similarity) {
        KnowledgeSearchResult searchResp = embeddingHandler.embeddingSearch(knowIds, queryText, topNumber, similarity);
        return Result.ok(searchResp);
    }

    /**
     * 通过ids批量查询知识库
     *
     * @param ids
     * @return
     * @author chenrui
     * @date 2025/2/27 16:44
     */
    @GetMapping(value = "/query/batch/byId")
    public Result<?> queryBatchByIds(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<AiragKnowledge> airagKnowledges = airagKnowledgeService.listByIds(idList);
        return Result.OK(airagKnowledges);
    }

}
