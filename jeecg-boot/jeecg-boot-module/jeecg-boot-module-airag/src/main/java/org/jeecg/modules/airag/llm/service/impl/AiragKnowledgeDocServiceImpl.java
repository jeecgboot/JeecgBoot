package org.jeecg.modules.airag.llm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.config.mqtoken.UserTokenContext;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.*;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.entity.AiragKnowledge;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.jeecg.modules.airag.llm.handler.EmbeddingHandler;
import org.jeecg.modules.airag.llm.mapper.AiragKnowledgeDocMapper;
import org.jeecg.modules.airag.llm.mapper.AiragKnowledgeMapper;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.jeecg.modules.airag.llm.consts.LLMConsts.*;

/**
 * @Description: airag知识库文档
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
@Slf4j
@Service
public class AiragKnowledgeDocServiceImpl extends ServiceImpl<AiragKnowledgeDocMapper, AiragKnowledgeDoc> implements IAiragKnowledgeDocService {

    @Autowired
    private AiragKnowledgeDocMapper airagKnowledgeDocMapper;

    @Autowired
    private AiragKnowledgeMapper airagKnowledgeMapper;

    @Autowired
    EmbeddingHandler embeddingHandler;


    @Value(value = "${jeecg.path.upload:}")
    private String uploadpath;

    /**
     * 支持的文档类型
     */
    private static final List<String> SUPPORT_DOC_TYPE = Arrays.asList("txt", "pdf", "docx", "doc", "pptx", "ppt", "xlsx", "xls", "md");

    /**
     * 向量化线程池大小
     */
    private static final int THREAD_POOL_SIZE = 10;

    /**
     * 向量化文档线程池
     */
    private static final ExecutorService buildDocExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Result<?> editDocument(AiragKnowledgeDoc airagKnowledgeDoc) {
        AssertUtils.assertNotEmpty("文档不能未空", airagKnowledgeDoc);
        AssertUtils.assertNotEmpty("知识库不能未空", airagKnowledgeDoc.getKnowledgeId());
        AssertUtils.assertNotEmpty("文档标题不能未空", airagKnowledgeDoc.getTitle());
        AssertUtils.assertNotEmpty("文档类型不能未空", airagKnowledgeDoc.getType());
        if (KNOWLEDGE_DOC_TYPE_TEXT.equals(airagKnowledgeDoc.getType())) {
            AssertUtils.assertNotEmpty("文档内容不能为空", airagKnowledgeDoc.getContent());
        }

        airagKnowledgeDoc.setStatus(KNOWLEDGE_DOC_STATUS_DRAFT);
        // 保存到数据库
        if (this.saveOrUpdate(airagKnowledgeDoc)) {
            // 重建向量
            return this.rebuildDocument(airagKnowledgeDoc.getId());
        } else {
            return Result.error("保存失败");
        }
    }

    @Override
    public Result<?> rebuildDocumentByKnowId(String knowId) {
        AssertUtils.assertNotEmpty("知识库id不能为空", knowId);
        List<AiragKnowledgeDoc> docList = airagKnowledgeDocMapper.selectList(Wrappers.lambdaQuery(AiragKnowledgeDoc.class).eq(AiragKnowledgeDoc::getKnowledgeId, knowId));
        if (oConvertUtils.isObjectEmpty(docList)) {
            return Result.OK();
        }
        String docIds = docList.stream().map(AiragKnowledgeDoc::getId).collect(Collectors.joining(","));
        return rebuildDocument(docIds);
    }

    @Transactional(rollbackFor = {java.lang.Exception.class})
    @Override
    public Result<?> rebuildDocument(String docIds) {
        AssertUtils.assertNotEmpty("请选择要重建的文档", docIds);
        List<String> docIdList = Arrays.asList(docIds.split(","));
        // 查询数据
        List<AiragKnowledgeDoc> docList = airagKnowledgeDocMapper.selectBatchIds(docIdList);
        AssertUtils.assertNotEmpty("文档不存在", docList);

        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        String baseUrl = CommonUtils.getBaseUrl(request);
        // 检查状态
        List<AiragKnowledgeDoc> knowledgeDocs = docList.stream()
                .filter(doc -> {
                    //update-begin---author:chenrui ---date:20250410  for：[QQYUN-11943]【ai】ai知识库 上传完文档 一直显示构建中？------------
                    if(KNOWLEDGE_DOC_STATUS_BUILDING.equalsIgnoreCase(doc.getStatus())){
                        Date updateTime = doc.getUpdateTime();
                        if (updateTime != null) {
                            // 向量化超过了5分钟,重新向量化
                            long timeDifference = System.currentTimeMillis() - updateTime.getTime();
                            return timeDifference > 5 * 60 * 1000;
                        }else{
                            return true;
                        }
                    } else {
                        return true;
                    }
                    //update-end---author:chenrui ---date:20250410  for：[QQYUN-11943]【ai】ai知识库 上传完文档 一直显示构建中？------------
                })
                .peek(doc -> {
                    doc.setStatus(KNOWLEDGE_DOC_STATUS_BUILDING);
                    doc.setBaseUrl(baseUrl);
                })
                .collect(Collectors.toList());
        if (oConvertUtils.isObjectEmpty(knowledgeDocs)) {
            return Result.ok("操作成功");
        }
        if (oConvertUtils.isObjectEmpty(knowledgeDocs)) {
            return Result.ok("操作成功");
        }
        // 更新状态
        this.updateBatchById(knowledgeDocs);
        // 异步重建文档
        String tenantId = TenantContext.getTenant();
        String token = TokenUtils.getTokenByRequest();
        knowledgeDocs.forEach((doc) -> {
            CompletableFuture.runAsync(() -> {
                UserTokenContext.setToken(token);
                TenantContext.setTenant(tenantId);
                String knowId = doc.getKnowledgeId();
                log.info("开始重建文档, 知识库id: {}, 文档id: {}", knowId, doc.getId());
                doc.setStatus(KNOWLEDGE_DOC_STATUS_BUILDING);
                this.updateById(doc);
                //update-begin---author:chenrui ---date:20250410  for：[QQYUN-11943]【ai】ai知识库 上传完文档 一直显示构建中？------------
                try {
                    Map<String, Object> metadata = embeddingHandler.embeddingDocument(knowId, doc);
                    // 更新数据 date:2025/2/18
                    if (null != metadata) {
                        doc.setStatus(KNOWLEDGE_DOC_STATUS_COMPLETE);
                        this.updateById(doc);
                        log.info("重建文档成功, 知识库id: {}, 文档id: {}", knowId, doc.getId());
                    } else {
                        doc.setStatus(KNOWLEDGE_DOC_STATUS_DRAFT);
                        this.updateById(doc);
                        log.info("重建文档失败, 知识库id: {}, 文档id: {}", knowId, doc.getId());
                    }
                }catch (Throwable t){
                    doc.setStatus(KNOWLEDGE_DOC_STATUS_DRAFT);
                    this.updateById(doc);
                    log.error("重建文档失败:" + t.getMessage() + ", 知识库id: " + knowId + ", 文档id: " + doc.getId(), t);
                }
                //update-end---author:chenrui ---date:20250410  for：[QQYUN-11943]【ai】ai知识库 上传完文档 一直显示构建中？------------
            }, buildDocExecutorService);
        });
        log.info("返回操作成功");
        return Result.ok("操作成功");
    }


    @Override
    public Result<?> removeByKnowIds(List<String> knowIds) {
        AssertUtils.assertNotEmpty("选择知识库", knowIds);
        for (String knowId : knowIds) {
            AiragKnowledge airagKnowledge = airagKnowledgeMapper.selectById(knowId);
            AssertUtils.assertNotEmpty("知识库不存在", airagKnowledge);
            AssertUtils.assertNotEmpty("请先为知识库配置向量模型库", airagKnowledge.getEmbedId());
            // 删除数据
            embeddingHandler.deleteEmbedDocsByKnowId(knowId, airagKnowledge.getEmbedId());
            airagKnowledgeDocMapper.deleteByMainId(knowId);
        }
        return Result.OK();
    }

    @Override
    public Result<?> removeDocByIds(List<String> docIds) {
        AssertUtils.assertNotEmpty("请选择要删除的文档", docIds);
        // 查询数据
        List<AiragKnowledgeDoc> docList = airagKnowledgeDocMapper.selectBatchIds(docIds);
        AssertUtils.assertNotEmpty("文档不存在", docList);
        // 整理数据
        Map<String, List<String>> knowledgeDocs = docList.stream().collect(Collectors.groupingBy(
                AiragKnowledgeDoc::getKnowledgeId,
                Collectors.mapping(AiragKnowledgeDoc::getId, Collectors.toList())
        ));
        if (oConvertUtils.isObjectEmpty(knowledgeDocs)) {
            return Result.ok("success");
        }
        knowledgeDocs.forEach((knowId, groupedDocIds) -> {
            AiragKnowledge airagKnowledge = airagKnowledgeMapper.selectById(knowId);
            AssertUtils.assertNotEmpty("知识库不存在", airagKnowledge);
            AssertUtils.assertNotEmpty("请先为知识库配置向量模型库", airagKnowledge.getEmbedId());
            // 删除数据
            embeddingHandler.deleteEmbedDocsByDocIds(groupedDocIds, airagKnowledge.getEmbedId());
            airagKnowledgeDocMapper.deleteBatchIds(groupedDocIds);
        });
        return Result.ok("success");
    }

    @Transactional(rollbackFor = {java.lang.Exception.class})
    @Override
    public Result<?> importDocumentFromZip(String knowId, MultipartFile zipFile) {
        AssertUtils.assertNotEmpty("请先选择知识库", knowId);
        AssertUtils.assertNotEmpty("请上传文件", zipFile);
        long startTime = System.currentTimeMillis();
        log.info("开始上传知识库文档(zip), 知识库id: {}, 文件名: {}", knowId, zipFile.getOriginalFilename());

        try {
            String bizPath = knowId + File.separator + UUIDGenerator.generate();
            String workDir = uploadpath + File.separator + bizPath + File.separator;
            String sourcesPath = workDir + "files";

            SsrfFileTypeFilter.checkUploadFileType(zipFile);
            // 通过filePath 检查文件是不是压缩包(zip)
            String zipFileName = FilenameUtils.getBaseName(zipFile.getOriginalFilename());
            String fileExt = FilenameUtils.getExtension(zipFile.getOriginalFilename());
            if (null == fileExt || !fileExt.equalsIgnoreCase("zip")) {
                throw new JeecgBootException("请上传zip压缩包");
            }
            String uploadedZipPath = CommonUtils.uploadLocal(zipFile, bizPath, uploadpath);
            // 解压缩文件
            List<AiragKnowledgeDoc> docList = new ArrayList<>();
            AtomicInteger fileCount = new AtomicInteger(0);
            unzipFile(uploadpath + File.separator + uploadedZipPath, sourcesPath, uploadedFile -> {
                // 仅支持txt、pdf、docx、pptx、html、md文件
                String fileName = uploadedFile.getName();
                if (!SUPPORT_DOC_TYPE.contains(FilenameUtils.getExtension(fileName).toLowerCase())) {
                    log.warn("不支持的文件类型: {}", fileName);
                    return;
                }
                String baseName = FilenameUtils.getBaseName(fileName);
                AiragKnowledgeDoc doc = new AiragKnowledgeDoc();
                doc.setKnowledgeId(knowId);
                doc.setTitle(baseName);
                doc.setType(LLMConsts.KNOWLEDGE_DOC_TYPE_FILE);
                doc.setStatus(LLMConsts.KNOWLEDGE_DOC_STATUS_DRAFT);

                String relativePath;
                if (File.separator.equals("\\")) {
                    // Windows path handling
                    String escapedPath = uploadpath.replace("//", "\\\\");
                    relativePath = uploadedFile.getPath().replaceFirst("^" + escapedPath, "");
                } else {
                    // Unix path handling
                    relativePath = uploadedFile.getPath().replaceFirst("^" + uploadpath, "");
                }
                JSONObject metadata = new JSONObject();
                metadata.put(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH, relativePath);
                metadata.put(LLMConsts.KNOWLEDGE_DOC_METADATA_SOURCES_PATH, sourcesPath);
                doc.setMetadata(metadata.toJSONString());
                docList.add(doc);
            });
            // 保存数据
            this.saveBatch(docList);
            // 重建文档
            String docIds = docList.stream().map(AiragKnowledgeDoc::getId).filter(oConvertUtils::isObjectNotEmpty).collect(Collectors.joining(","));
            rebuildDocument(docIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        log.info("上传知识库文档(zip)成功, 知识库id: {}, 文件名: {}, 耗时: {}ms", knowId, zipFile.getOriginalFilename(), (System.currentTimeMillis() - startTime));
        return Result.ok("上传成功");
    }

    /**
     * 解压缩文件
     *
     * @param zipFilePath
     * @param destDir
     * @param afterExtract
     * @throws IOException
     * @author chenrui
     * @date 2025/3/20 14:37
     */
    public static void unzipFile(String zipFilePath, String destDir, Consumer<File> afterExtract) throws
            IOException {
        // 创建目标目录
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            byte[] buffer = new byte[1024];

            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                File newFile = new File(destDir, ze.getName());

                // 预防 ZIP 路径穿越攻击
                String canonicalDestDirPath = dir.getCanonicalPath();
                String canonicalFilePath = newFile.getCanonicalPath();
                if (!canonicalFilePath.startsWith(canonicalDestDirPath + File.separator)) {
                    throw new IOException("ZIP 路径穿越攻击被阻止: " + ze.getName());
                }

                if (ze.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    // 创建父目录
                    new File(newFile.getParent()).mkdirs();

                    // 读取 ZIP 文件并写入新文件
                    try (InputStream zis = zipFile.getInputStream(ze);
                         FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

                    if (afterExtract != null) {
                        afterExtract.accept(newFile);
                    }
                }
            }
        }
    }

}
