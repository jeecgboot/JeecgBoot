package org.jeecg.modules.airag.llm.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.parser.AutoDetectParser;
import org.jeecg.ai.factory.AiModelFactory;
import org.jeecg.ai.factory.AiModelOptions;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.*;
import org.jeecg.modules.airag.common.handler.IEmbeddingHandler;
import org.jeecg.modules.airag.common.vo.knowledge.KnowledgeSearchResult;
import org.jeecg.modules.airag.llm.config.EmbedStoreConfigBean;
import org.jeecg.modules.airag.llm.config.KnowConfigBean;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.document.TikaDocumentParser;
import org.jeecg.modules.airag.llm.entity.AiragKnowledge;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeService;
import org.jeecg.modules.airag.llm.service.IAiragModelService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;
import static org.jeecg.modules.airag.llm.consts.LLMConsts.KNOWLEDGE_DOC_TYPE_FILE;
import static org.jeecg.modules.airag.llm.consts.LLMConsts.KNOWLEDGE_DOC_TYPE_WEB;

/**
 * 向量工具类
 *
 * @Author: chenrui
 * @Date: 2025/2/18 14:31
 */
@Slf4j
@Component
public class EmbeddingHandler implements IEmbeddingHandler {

    @Autowired
    EmbedStoreConfigBean embedStoreConfigBean;

    @Autowired
    @Lazy
    private IAiragModelService airagModelService;

    @Autowired
    @Lazy
    private IAiragKnowledgeService airagKnowledgeService;

    @Value(value = "${jeecg.path.upload:}")
    private String uploadpath;

    @Autowired
    KnowConfigBean knowConfigBean;

    /**
     * 默认分段长度
     */
    private static final int DEFAULT_SEGMENT_SIZE = 1000;

    /**
     * 默认分段重叠长度
     */
    private static final int DEFAULT_OVERLAP_SIZE = 50;

    /**
     * 向量存储元数据:knowledgeId
     */
    public static final String EMBED_STORE_METADATA_KNOWLEDGEID = "knowledgeId";

    /**
     * 向量存储元数据:docId
     */
    public static final String EMBED_STORE_METADATA_DOCID = "docId";

    /**
     * 向量存储元数据:docName
     */
    public static final String EMBED_STORE_METADATA_DOCNAME = "docName";

    /**
     * 向量存储缓存
     */
    private static final ConcurrentHashMap<String, EmbeddingStore<TextSegment>> EMBED_STORE_CACHE = new ConcurrentHashMap<>();

    /**
     * 向量化文档
     *
     * @param knowId
     * @param doc
     * @return
     * @author chenrui
     * @date 2025/2/18 11:52
     */
    public Map<String, Object> embeddingDocument(String knowId, AiragKnowledgeDoc doc) {
        AiragKnowledge airagKnowledge = airagKnowledgeService.getById(knowId);
        AssertUtils.assertNotEmpty("知识库不存在", airagKnowledge);
        AssertUtils.assertNotEmpty("请先为知识库配置向量模型库", airagKnowledge.getEmbedId());
        AssertUtils.assertNotEmpty("文档不能为空", doc);
        // 读取文档
        String content = doc.getContent();
        // 向量化并存储
        if (oConvertUtils.isEmpty(content)) {
            switch (doc.getType()) {
                case KNOWLEDGE_DOC_TYPE_FILE:
                    //解析文件
                    if (knowConfigBean.isEnableMinerU()) {
                        parseFileByMinerU(doc);
                    }
                    content = parseFile(doc);
                    break;
                case KNOWLEDGE_DOC_TYPE_WEB:
                    // TODO author: chenrui for:读取网站内容 date:2025/2/18
                    break;
            }
        }
        //update-begin---author:chenrui ---date:20250307  for：[QQYUN-11443]【AI】是不是应该把标题也生成到向量库里，标题一般是有意义的------------
        if (oConvertUtils.isNotEmpty(doc.getTitle())) {
            content = doc.getTitle() + "\n\n" + content;
        }
        //update-end---author:chenrui ---date:20250307  for：[QQYUN-11443]【AI】是不是应该把标题也生成到向量库里，标题一般是有意义的------------

        // 向量化 date:2025/2/18
        AiragModel model = getEmbedModelData(airagKnowledge.getEmbedId());
        AiModelOptions modelOp = buildModelOptions(model);
        EmbeddingModel embeddingModel = AiModelFactory.createEmbeddingModel(modelOp);
        EmbeddingStore<TextSegment> embeddingStore = getEmbedStore(model);
        // 删除旧数据
        embeddingStore.removeAll(metadataKey(EMBED_STORE_METADATA_DOCID).isEqualTo(doc.getId()));
        // 分段器
        DocumentSplitter splitter = DocumentSplitters.recursive(DEFAULT_SEGMENT_SIZE, DEFAULT_OVERLAP_SIZE, new OpenAiTokenizer());
        // 分段并存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        Metadata metadata = Metadata.metadata(EMBED_STORE_METADATA_DOCID, doc.getId())
                .put(EMBED_STORE_METADATA_KNOWLEDGEID, doc.getKnowledgeId())
                .put(EMBED_STORE_METADATA_DOCNAME, FilenameUtils.getName(doc.getTitle()));
        Document from = Document.from(content, metadata);
        ingestor.ingest(from);
        return metadata.toMap();
    }

    /**
     * 向量查询(多知识库)
     *
     * @param knowIds
     * @param queryText
     * @param topNumber
     * @param similarity
     * @return
     * @author chenrui
     * @date 2025/2/18 16:52
     */
    public KnowledgeSearchResult embeddingSearch(List<String> knowIds, String queryText, Integer topNumber, Double similarity) {
        AssertUtils.assertNotEmpty("请选择知识库", knowIds);
        AssertUtils.assertNotEmpty("请填写查询内容", queryText);

        topNumber = oConvertUtils.getInteger(topNumber, 5);

        //命中的文档列表
        List<Map<String, Object>> documents = new ArrayList<>(16);
        for (String knowId : knowIds) {
            List<Map<String, Object>> searchResp = searchEmbedding(knowId, queryText, topNumber, similarity);
            if (oConvertUtils.isObjectNotEmpty(searchResp)) {
                documents.addAll(searchResp);
            }
        }

        //命中的文档内容
        StringBuilder data = new StringBuilder();
        // 对documents按score降序排序并取前topNumber个
        List<Map<String, Object>> sortedDocuments = documents.stream()
                .sorted(Comparator.comparingDouble((Map<String, Object> doc) -> (Double) doc.get("score")).reversed())
                .limit(topNumber)
                .peek(doc -> data.append(doc.get("content")).append("\n"))
                .collect(Collectors.toList());

        return new KnowledgeSearchResult(data.toString(), sortedDocuments);
    }

    /**
     * 向量查询
     *
     * @param knowId
     * @param queryText
     * @param topNumber
     * @param similarity
     * @return
     * @author chenrui
     * @date 2025/2/18 16:52
     */
    public List<Map<String, Object>> searchEmbedding(String knowId, String queryText, Integer topNumber, Double similarity) {
        AssertUtils.assertNotEmpty("请选择知识库", knowId);
        AiragKnowledge knowledge = airagKnowledgeService.getById(knowId);
        AssertUtils.assertNotEmpty("知识库不存在", knowledge);
        AssertUtils.assertNotEmpty("请填写查询内容", queryText);
        AiragModel model = getEmbedModelData(knowledge.getEmbedId());

        AiModelOptions modelOp = buildModelOptions(model);
        EmbeddingModel embeddingModel = AiModelFactory.createEmbeddingModel(modelOp);
        Embedding queryEmbedding = embeddingModel.embed(queryText).content();

        topNumber = oConvertUtils.getInteger(topNumber, modelOp.getTopNumber());
        similarity = oConvertUtils.getDou(similarity, modelOp.getSimilarity());
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topNumber)
                .minScore(similarity)
                .filter(metadataKey(EMBED_STORE_METADATA_KNOWLEDGEID).isEqualTo(knowId))
                .build();

        EmbeddingStore<TextSegment> embeddingStore = getEmbedStore(model);
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.search(embeddingSearchRequest).matches();
        List<Map<String, Object>> result = new ArrayList<>();
        if (oConvertUtils.isObjectNotEmpty(relevant)) {
            result = relevant.stream().map(matchRes -> {
                Map<String, Object> data = new HashMap<>();
                data.put("score", matchRes.score());
                data.put("content", matchRes.embedded().text());
                Metadata metadata = matchRes.embedded().metadata();
                data.put("chunk", metadata.getInteger("index"));
                data.put(EMBED_STORE_METADATA_DOCNAME, metadata.getString(EMBED_STORE_METADATA_DOCNAME));
                return data;
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取向量查询路由
     *
     * @param knowIds
     * @param topNumber
     * @param similarity
     * @return
     * @author chenrui
     * @date 2025/2/20 21:03
     */
    public QueryRouter getQueryRouter(List<String> knowIds, Integer topNumber, Double similarity) {
        AssertUtils.assertNotEmpty("请选择知识库", knowIds);
        List<ContentRetriever> retrievers = Lists.newArrayList();
        for (String knowId : knowIds) {
            if (oConvertUtils.isEmpty(knowId)) {
                continue;
            }
            AiragKnowledge knowledge = airagKnowledgeService.getById(knowId);
            AssertUtils.assertNotEmpty("知识库不存在", knowledge);
            AiragModel model = getEmbedModelData(knowledge.getEmbedId());
            AiModelOptions modelOptions = buildModelOptions(model);
            EmbeddingModel embeddingModel = AiModelFactory.createEmbeddingModel(modelOptions);

            EmbeddingStore<TextSegment> embeddingStore = getEmbedStore(model);
            topNumber = oConvertUtils.getInteger(topNumber, 5);
            similarity = oConvertUtils.getDou(similarity, 0.75);
            // 构建一个嵌入存储内容检索器，用于从嵌入存储中检索内容
            EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(topNumber)
                    .minScore(similarity)
                    .filter(metadataKey(EMBED_STORE_METADATA_KNOWLEDGEID).isEqualTo(knowId))
                    .build();
            retrievers.add(contentRetriever);
        }
        if (retrievers.isEmpty()) {
            return null;
        } else {
            return new DefaultQueryRouter(retrievers);
        }
    }

    /**
     * 删除向量化文档
     *
     * @param knowId
     * @param modelId
     * @author chenrui
     * @date 2025/2/18 19:07
     */
    public void deleteEmbedDocsByKnowId(String knowId, String modelId) {
        AssertUtils.assertNotEmpty("选择知识库", knowId);
        AiragModel model = getEmbedModelData(modelId);

        EmbeddingStore<TextSegment> embeddingStore = getEmbedStore(model);
        // 删除数据
        embeddingStore.removeAll(metadataKey(EMBED_STORE_METADATA_KNOWLEDGEID).isEqualTo(knowId));
    }

    /**
     * 删除向量化文档
     *
     * @param docIds
     * @param modelId
     * @author chenrui
     * @date 2025/2/18 19:07
     */
    public void deleteEmbedDocsByDocIds(List<String> docIds, String modelId) {
        AssertUtils.assertNotEmpty("选择文档", docIds);
        AiragModel model = getEmbedModelData(modelId);

        EmbeddingStore<TextSegment> embeddingStore = getEmbedStore(model);
        // 删除数据
        embeddingStore.removeAll(metadataKey(EMBED_STORE_METADATA_DOCID).isIn(docIds));
    }

    /**
     * 查询向量模型数据
     *
     * @param modelId
     * @return
     * @author chenrui
     * @date 2025/2/20 20:08
     */
    private AiragModel getEmbedModelData(String modelId) {
        AssertUtils.assertNotEmpty("向量模型不能为空", modelId);
        AiragModel model = airagModelService.getById(modelId);
        AssertUtils.assertNotEmpty("向量模型不存在", model);
        AssertUtils.assertEquals("仅支持向量模型", LLMConsts.MODEL_TYPE_EMBED, model.getModelType());
        return model;
    }

    /**
     * 获取向量存储
     *
     * @param model
     * @return
     * @author chenrui
     * @date 2025/2/18 14:56
     */
    private EmbeddingStore<TextSegment> getEmbedStore(AiragModel model) {
        AssertUtils.assertNotEmpty("未配置模型", model);
        String modelId = model.getId();
        String connectionInfo = embedStoreConfigBean.getHost() + embedStoreConfigBean.getPort() + embedStoreConfigBean.getDatabase();
        String key = modelId + connectionInfo;
        if (EMBED_STORE_CACHE.containsKey(key)) {
            return EMBED_STORE_CACHE.get(key);
        }


        AiModelOptions modelOp = buildModelOptions(model);
        EmbeddingModel embeddingModel = AiModelFactory.createEmbeddingModel(modelOp);
        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                // Connection and table parameters
                .host(embedStoreConfigBean.getHost())
                .port(embedStoreConfigBean.getPort())
                .database(embedStoreConfigBean.getDatabase())
                .user(embedStoreConfigBean.getUser())
                .password(embedStoreConfigBean.getPassword())
                .table(embedStoreConfigBean.getTable())
                // Embedding dimension
                // Required: Must match the embedding model’s output dimension
                .dimension(embeddingModel.dimension())
                // Indexing and performance options
                // Enable IVFFlat index
                .useIndex(true)
                // Number of lists
                // for IVFFlat index
                .indexListSize(100)
                // Table creation options
                // Automatically create the table if it doesn’t exist
                .createTable(true)
                //Don’t drop the table first (set to true if you want a fresh start)
                .dropTableFirst(false)
                .build();
        EMBED_STORE_CACHE.put(key, embeddingStore);
        return embeddingStore;
    }

    /**
     * 构造ModelOptions
     *
     * @param model
     * @return
     * @author chenrui
     * @date 2025/3/11 17:45
     */
    public static AiModelOptions buildModelOptions(AiragModel model) {
        AiModelOptions.AiModelOptionsBuilder modelOpBuilder = AiModelOptions.builder()
                .provider(model.getProvider())
                .modelName(model.getModelName())
                .baseUrl(model.getBaseUrl());
        if (oConvertUtils.isObjectNotEmpty(model.getCredential())) {
            JSONObject modelCredential = JSONObject.parseObject(model.getCredential());
            modelOpBuilder.apiKey(oConvertUtils.getString(modelCredential.getString("apiKey"), null));
            modelOpBuilder.secretKey(oConvertUtils.getString(modelCredential.getString("secretKey"), null));
        }
        modelOpBuilder.topNumber(5);
        modelOpBuilder.similarity(0.75);
        return modelOpBuilder.build();
    }

    /**
     * 解析文件
     *
     * @param doc
     * @author chenrui
     * @date 2025/3/5 11:31
     */
    private String parseFile(AiragKnowledgeDoc doc) {
        String metadata = doc.getMetadata();
        AssertUtils.assertNotEmpty("请先上传文件", metadata);
        JSONObject metadataJson = JSONObject.parseObject(metadata);
        if (!metadataJson.containsKey(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH)) {
            throw new JeecgBootException("请先上传文件");
        }
        String filePath = metadataJson.getString(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH);
        AssertUtils.assertNotEmpty("请先上传文件", filePath);
        // 网络资源,先下载到临时目录
        filePath = ensureFile(filePath);
        // 提取文档内容
        File docFile = new File(filePath);
        if (docFile.exists()) {
            Document document = new TikaDocumentParser(AutoDetectParser::new, null, null, null).parse(docFile);
            if (null != document) {
                String content = document.text();
                // 判断是否md文档
                String fileType = FilenameUtils.getExtension(docFile.getName());
                if ("md".contains(fileType)) {
                    // 如果是md文件，查找所有图片语法，如果是本地图片，替换成网络图片
                    String baseUrl = doc.getBaseUrl() + "/sys/common/static/";
                    String sourcePath = metadataJson.getString(LLMConsts.KNOWLEDGE_DOC_METADATA_SOURCES_PATH);
                    if(oConvertUtils.isNotEmpty(sourcePath)) {
                        String escapedPath = uploadpath;
                        if (File.separator.equals("\\")){
                            escapedPath = uploadpath.replace("//", "\\\\");
                        }
                        sourcePath = sourcePath.replaceFirst("^" + escapedPath, "").replace("\\", "/");
                        baseUrl = baseUrl + sourcePath + "/";
                        StringBuffer sb = replaceImageUrl(content, baseUrl);
                        content = sb.toString();
                    }
                }
                return content;
            }
        }
        return null;
    }

    @NotNull
    private static StringBuffer replaceImageUrl(String content, String baseUrl) {
        // 正则表达式匹配md文件中的图片语法 ![alt text](image url)
        String mdImagePattern = "!\\[(.*?)]\\((.*?)(\\s*=\\d+)?\\)";
        Pattern pattern = Pattern.compile(mdImagePattern);
        Matcher matcher = pattern.matcher(content);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String imageUrl = matcher.group(2);
            // 检查是否是本地图片路径
            if (!imageUrl.startsWith("http")) {
                // 替换成网络图片路径
                String networkImageUrl = baseUrl + imageUrl;
                matcher.appendReplacement(sb, "![" + matcher.group(1) + "](" + networkImageUrl + ")");
            } else {
                matcher.appendReplacement(sb, "![" + matcher.group(1) + "](" + imageUrl + ")");
            }
        }
        matcher.appendTail(sb);
        return sb;
    }

    /**
     * 通过MinerU解析文件
     *
     * @param doc
     * @author chenrui
     * @date 2025/4/1 17:37
     */
    private void parseFileByMinerU(AiragKnowledgeDoc doc) {
        String metadata = doc.getMetadata();
        AssertUtils.assertNotEmpty("请先上传文件", metadata);
        JSONObject metadataJson = JSONObject.parseObject(metadata);
        if (!metadataJson.containsKey(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH)) {
            throw new JeecgBootException("请先上传文件");
        }
        String filePath = metadataJson.getString(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH);
        AssertUtils.assertNotEmpty("请先上传文件", filePath);
        filePath = ensureFile(filePath);

        File docFile = new File(filePath);
        String fileType = FilenameUtils.getExtension(filePath);
        if (!docFile.exists()
                || "txt".equalsIgnoreCase(fileType)
                || "md".equalsIgnoreCase(fileType)) {
            return ;
        }

        String command = "magic-pdf";
        if (oConvertUtils.isNotEmpty(knowConfigBean.getCondaEnv())) {
            command = "conda run -n " + knowConfigBean.getCondaEnv() + " " + command;
        }

        String outputPath = docFile.getParentFile().getAbsolutePath();
        String[] args = {
                "-p", docFile.getAbsolutePath(),
                "-o", outputPath,
        };

        try {
            String execLog = CommandExecUtil.execCommand(command, args);
            log.info("执行命令行:" + command + " args:" + Arrays.toString(args) + "\n log::" + execLog);
            // 如果成功,替换文件路径和静态资源路径
            String fileBaseName = FilenameUtils.getBaseName(docFile.getName());
            String newFileDir = outputPath + File.separator + fileBaseName + File.separator + "auto" + File.separator ;
            // 先检查文件是否存在,存在才替换
            File convertedFile = new File(newFileDir + fileBaseName + ".md");
            if (convertedFile.exists()) {
                log.info("文件转换成md成功,替换文件路径和静态资源路径");
                newFileDir = newFileDir.replaceFirst("^" + uploadpath, "");
                metadataJson.put(LLMConsts.KNOWLEDGE_DOC_METADATA_FILEPATH, newFileDir + fileBaseName + ".md");
                metadataJson.put(LLMConsts.KNOWLEDGE_DOC_METADATA_SOURCES_PATH, newFileDir);
                doc.setMetadata(metadataJson.toJSONString());
            }
        } catch (IOException e) {
            log.error("文件转换md失败,使用传统提取方案{}", e.getMessage(), e);
        }
    }

    /**
     * 确保文件存在
     * @param filePath
     * @return
     * @author chenrui
     * @date 2025/4/1 17:36
     */
    @NotNull
    private String ensureFile(String filePath) {
        // 网络资源,先下载到临时目录
        Matcher matcher = LLMConsts.WEB_PATTERN.matcher(filePath);
        if (matcher.matches()) {
            log.info("网络资源,下载到临时目录:" + filePath);
            // 准备文件
            String tempFilePath = uploadpath + File.separator + "tmp" + File.separator + UUIDGenerator.generate() + File.separator;
            String fileName = filePath;
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.indexOf("?"));
            }
            fileName = FilenameUtils.getName(fileName);
            tempFilePath = tempFilePath + fileName;
            FileDownloadUtils.download2DiskFromNet(filePath, tempFilePath);
            filePath = tempFilePath;
        } else {
            //本地文件
            filePath = uploadpath + File.separator + filePath;
        }
        return filePath;
    }


}
