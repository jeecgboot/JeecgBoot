package org.jeecg.modules.airag.llm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
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

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    // 解压文件:单个文件最大150MB
    private static final long MAX_FILE_SIZE = 150 * 1024 * 1024;
    // 解压文件:总解压大小1024MB
    private static final long MAX_TOTAL_SIZE = 1024 * 1024 * 1024;
    // 解压文件:最多解压10000个Entry
    private static final int MAX_ENTRY_COUNT = 10000;

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
                        this.handleDocBuildFailed(doc, "向量化失败");
                        log.info("重建文档失败, 知识库id: {}, 文档id: {}", knowId, doc.getId());
                    }
                }catch (Throwable t){
                    this.handleDocBuildFailed(doc, t.getMessage());
                    log.error("重建文档失败:" + t.getMessage() + ", 知识库id: " + knowId + ", 文档id: " + doc.getId(), t);
                }
                //update-end---author:chenrui ---date:20250410  for：[QQYUN-11943]【ai】ai知识库 上传完文档 一直显示构建中？------------
            }, buildDocExecutorService);
        });
        log.info("返回操作成功");
        return Result.ok("操作成功");
    }

    /**
     * 处理文档构建失败
     */
    private void handleDocBuildFailed(AiragKnowledgeDoc doc, String failedReason) {
        doc.setStatus(KNOWLEDGE_DOC_STATUS_FAILED);

        String metadataStr = doc.getMetadata();
        JSONObject metadata;
        if (oConvertUtils.isEmpty(metadataStr)) {
            metadata = new JSONObject();
        } else {
            metadata = JSONObject.parseObject(metadataStr);
        }
        metadata.put("failedReason", failedReason);
        doc.setMetadata(metadata.toJSONString());

        this.updateById(doc);
    }

    @Override
    public Result<?> removeByKnowIds(List<String> knowIds) {
        AssertUtils.assertNotEmpty("选择知识库", knowIds);
        for (String knowId : knowIds) {
            AiragKnowledge airagKnowledge = airagKnowledgeMapper.selectById(knowId);
            AssertUtils.assertNotEmpty("知识库不存在", airagKnowledge);
            AssertUtils.assertNotEmpty("请先为知识库配置向量模型库", airagKnowledge.getEmbedId());
            // 异步删除向量数据
            final String embedId = airagKnowledge.getEmbedId();
            final String finalKnowId = knowId;
            CompletableFuture.runAsync(() -> {
                try {
                    embeddingHandler.deleteEmbedDocsByKnowId(finalKnowId, embedId);
                } catch (Throwable ignore) {
                }
            });
            // 删除数据
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
            // 异步删除向量数据
            final String embedId = airagKnowledge.getEmbedId();
            final List<String> docIdsToDelete = new ArrayList<>(groupedDocIds);
            CompletableFuture.runAsync(() -> {
                try {
                    embeddingHandler.deleteEmbedDocsByDocIds(docIdsToDelete, embedId);
                } catch (Throwable ignore) {
                }
            });
            // 删除数据
            airagKnowledgeDocMapper.deleteBatchIds(groupedDocIds);
        });
        return Result.ok("success");
    }

    @Override
    public Result<?> deleteAllByKnowId(String knowId) {
        if (oConvertUtils.isEmpty(knowId)) {
            return Result.error("知识库id不能为空");
        }
        LambdaQueryWrapper<AiragKnowledgeDoc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiragKnowledgeDoc::getKnowledgeId, knowId);
        //noinspection unchecked
        wrapper.select(AiragKnowledgeDoc::getId);
        List<AiragKnowledgeDoc> docList = airagKnowledgeDocMapper.selectList(wrapper);
        if (docList.isEmpty()) {
            return Result.ok("暂无文档");
        }
        List<String> docIds = docList.stream().map(AiragKnowledgeDoc::getId).collect(Collectors.toList());
        this.removeDocByIds(docIds);
        return Result.ok("清空完成");
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
            AssertUtils.assertNotEmpty("压缩包中没有符合要求的文档", docList);
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
     * @param zipFilePath 压缩文件路径
     * @param destDir    目标文件夹
     * @param afterExtract 解压完成后回调
     * @throws IOException
     * @author chenrui
     * @date 2025/3/20 14:37
     */
    public static void unzipFile(String zipFilePath, String destDir, Consumer<File> afterExtract) throws IOException {
        unzipFile(Paths.get(zipFilePath), Paths.get(destDir), afterExtract);
    }


    /**
     * 解压缩文件
     *
     * @param zipFilePath  压缩文件路径
     * @param targetDir    目标文件夹
     * @param afterExtract 解压完成后回调
     * @throws IOException
     * @author chenrui
     * @date 2025/4/28 17:02
     */
    private static void unzipFile(Path zipFilePath, Path targetDir, Consumer<File> afterExtract) throws IOException {
        long totalUnzippedSize = 0;
        int entryCount = 0;

        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                entryCount++;
                if (entryCount > MAX_ENTRY_COUNT) {
                    throw new IOException("解压文件数量超限，可能是zip bomb攻击");
                }

                Path newPath = safeResolve(targetDir, entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    try (InputStream is = zipFile.getInputStream(entry);
                         OutputStream os = Files.newOutputStream(newPath)) {

                        long bytesCopied = copyLimited(is, os, MAX_FILE_SIZE);
                        totalUnzippedSize += bytesCopied;

                        if (totalUnzippedSize > MAX_TOTAL_SIZE) {
                            throw new IOException("解压总大小超限，可能是zip bomb攻击");
                        }
                    }

                    // 解压完成后回调
                    if (afterExtract != null) {
                        afterExtract.accept(newPath.toFile());
                    }
                }
            }
        }
    }

    /**
     * 安全解析路径，防止Zip Slip攻击
     *
     * @param targetDir
     * @param entryName
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/4/28 16:46
     */
    private static Path safeResolve(Path targetDir, String entryName) throws IOException {
        Path resolvedPath = targetDir.resolve(entryName).normalize();
        if (!resolvedPath.startsWith(targetDir)) {
            throw new IOException("ZIP 路径穿越攻击被阻止:" + entryName);
        }
        return resolvedPath;
    }

    /**
     * 复制输入流到输出流，并限制最大字节数
     *
     * @param in
     * @param out
     * @param maxBytes
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/4/28 17:03
     */
    private static long copyLimited(InputStream in, OutputStream out, long maxBytes) throws IOException {
        byte[] buffer = new byte[8192];
        long totalCopied = 0;
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            totalCopied += bytesRead;
            if (totalCopied > maxBytes) {
                throw new IOException("单个文件解压超限，可能是zip bomb攻击");
            }
            out.write(buffer, 0, bytesRead);
        }
        return totalCopied;
    }

}
