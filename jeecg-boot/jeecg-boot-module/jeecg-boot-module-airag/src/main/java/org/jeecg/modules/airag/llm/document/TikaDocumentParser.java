//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.jeecg.modules.airag.llm.document;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.internal.Utils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.Tika;
import org.apache.tika.exception.ZeroByteFileException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.jeecg.common.util.AssertUtils;
import org.xml.sax.ContentHandler;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * tika文档解析器,重写langchain4j的TikaDocumentParser <br/>
 * jeecgboot目前不支持poi5.x,所以langchain4j同的方法不能用,自己实现
 * @author chenrui
 * @date 2025/3/5 16:19
 */
public class TikaDocumentParser {
    private static final Tika tika = new Tika();
    private static final int NO_WRITE_LIMIT = -1;
    public static final Supplier<Parser> DEFAULT_PARSER_SUPPLIER = AutoDetectParser::new;
    public static final Supplier<Metadata> DEFAULT_METADATA_SUPPLIER = Metadata::new;
    public static final Supplier<ParseContext> DEFAULT_PARSE_CONTEXT_SUPPLIER = ParseContext::new;
    public static final Supplier<ContentHandler> DEFAULT_CONTENT_HANDLER_SUPPLIER = () -> new BodyContentHandler(-1);
    private final Supplier<Parser> parserSupplier;
    private final Supplier<ContentHandler> contentHandlerSupplier;
    private final Supplier<Metadata> metadataSupplier;
    private final Supplier<ParseContext> parseContextSupplier;

    public TikaDocumentParser() {
        this((Supplier) ((Supplier) null), (Supplier) null, (Supplier) null, (Supplier) null);
    }


    public TikaDocumentParser(Supplier<Parser> parserSupplier, Supplier<ContentHandler> contentHandlerSupplier, Supplier<Metadata> metadataSupplier, Supplier<ParseContext> parseContextSupplier) {
        this.parserSupplier = (Supplier) Utils.getOrDefault(parserSupplier, () -> DEFAULT_PARSER_SUPPLIER);
        this.contentHandlerSupplier = (Supplier) Utils.getOrDefault(contentHandlerSupplier, () -> DEFAULT_CONTENT_HANDLER_SUPPLIER);
        this.metadataSupplier = (Supplier) Utils.getOrDefault(metadataSupplier, () -> DEFAULT_METADATA_SUPPLIER);
        this.parseContextSupplier = (Supplier) Utils.getOrDefault(parseContextSupplier, () -> DEFAULT_PARSE_CONTEXT_SUPPLIER);
    }

    public Document parse(File file) {
        AssertUtils.assertNotEmpty("请选择文件", file);
        try {
            // 用于解析
            InputStream isForParsing = Files.newInputStream(file.toPath());
            // 使用 Tika 自动检测 MIME 类型
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".txt")
                    || fileName.endsWith(".md")
                    || fileName.endsWith(".pdf")) {
                return extractByTika(isForParsing);
            } else if (fileName.endsWith(".docx")) {
                return extractTextFromDocx(isForParsing);
            } else if (fileName.endsWith(".doc")) {
                return extractTextFromDoc(isForParsing);
            } else if (fileName.endsWith(".xlsx")) {
                return extractTextFromExcel(isForParsing);
            } else if (fileName.endsWith(".xls")) {
                return extractTextFromExcel(isForParsing);
            } else if (fileName.endsWith(".pptx")) {
                return extractTextFromPptx(isForParsing);
            } else if (fileName.endsWith(".ppt")) {
                return extractTextFromPpt(isForParsing);
            } else {
                throw new IllegalArgumentException("不支持的文件格式: " + FilenameUtils.getExtension(fileName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document tryExtractDocOrDocx(InputStream inputStream) throws IOException {
        try {
            // 先尝试 DOCX（基于 OPC XML 格式）
            return extractTextFromDocx(inputStream);
        } catch (Exception e1) {
            try {
                // 如果 DOCX 解析失败，则尝试 DOC（基于二进制格式）
                return extractTextFromDoc(inputStream);
            } catch (Exception e2) {
                throw new IOException("无法解析 DOC 或 DOCX 文件", e2);
            }
        }
    }

    /**
     * 使用tika提取文件内容 <br/>
     * pdf/text/md等文件使用tika提取
     *
     * @param inputStream
     * @return
     * @author chenrui
     * @date 2025/3/5 14:41
     */
    private Document extractByTika(InputStream inputStream) {
        try {
            Parser parser = (Parser) this.parserSupplier.get();
            ContentHandler contentHandler = (ContentHandler) this.contentHandlerSupplier.get();
            Metadata metadata = (Metadata) this.metadataSupplier.get();
            ParseContext parseContext = (ParseContext) this.parseContextSupplier.get();
            parser.parse(inputStream, contentHandler, metadata, parseContext);
            String text = contentHandler.toString();
            if (Utils.isNullOrBlank(text)) {
                throw new BlankDocumentException();
            } else {
                return Document.from(text);
            }
        } catch (BlankDocumentException e) {
            throw e;
        } catch (ZeroByteFileException var8) {
            throw new BlankDocumentException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提取docx文件内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/3/5 14:42
     * @deprecated 因为jeecg主项目目前不支持poi5.x, 自己实现提取功能.
     */
    @Deprecated
    private static Document extractTextFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder text = new StringBuilder();
            for (XWPFParagraph para : document.getParagraphs()) {
                text.append(para.getText()).append("\n");
            }
            return Document.from(text.toString());
        }
    }

    /**
     * 提取doc文件内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/3/5 14:42
     * @deprecated 因为jeecg主项目目前不支持poi5.x, 自己实现提取功能.
     */
    @Deprecated
    private static Document extractTextFromDoc(InputStream inputStream) throws IOException {
        try (HWPFDocument document = new HWPFDocument(inputStream);
             WordExtractor extractor = new WordExtractor(document)) {
            return Document.from(extractor.getText());
        }
    }

    /**
     * 提取excel文件内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/3/5 14:43
     * @deprecated 因为jeecg主项目目前不支持poi5.x, 自己实现提取功能.
     */
    @Deprecated
    private static Document extractTextFromExcel(InputStream inputStream) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            StringBuilder text = new StringBuilder();
            for (Sheet sheet : workbook) {
                text.append("Sheet: ").append(sheet.getSheetName()).append("\n");
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        text.append(cell.toString()).append("\t");
                    }
                    text.append("\n");
                }
                text.append("\n");
            }
            return Document.from(text.toString());
        }
    }

    /**
     * 提取pptx文件内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/3/5 14:43
     * @deprecated 因为jeecg主项目目前不支持poi5.x, 自己实现提取功能.
     */
    @Deprecated
    private static Document extractTextFromPptx(InputStream inputStream) throws IOException {
        try (XMLSlideShow ppt = new XMLSlideShow(inputStream)) {
            StringBuilder text = new StringBuilder();
            for (XSLFSlide slide : ppt.getSlides()) {
                text.append("Slide ").append(slide.getSlideNumber()).append(":\n");
                List<XSLFTextShape> shapes = slide.getShapes().stream()
                        .filter(s -> s instanceof XSLFTextShape)
                        .map(s -> (XSLFTextShape) s)
                        .collect(Collectors.toList());
                for (XSLFTextShape shape : shapes) {
                    text.append(shape.getText()).append("\n");
                }
                text.append("\n");
            }
            return Document.from(text.toString());
        }
    }

    /**
     * 提取ppt文件内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2025/3/5 14:43
     * @deprecated 因为jeecg主项目目前不支持poi5.x, 自己实现提取功能.
     */
    @Deprecated
    private static Document extractTextFromPpt(InputStream inputStream) throws IOException {
        try (org.apache.poi.hslf.usermodel.HSLFSlideShow ppt = new org.apache.poi.hslf.usermodel.HSLFSlideShow(inputStream)) {
            StringBuilder text = new StringBuilder();
            for (org.apache.poi.hslf.usermodel.HSLFSlide slide : ppt.getSlides()) {
                text.append("Slide ").append(slide.getSlideNumber()).append(":\n");
                for (List<HSLFTextParagraph> shapes : slide.getTextParagraphs()) {
                    text.append(HSLFTextParagraph.getText(shapes)).append("\n");
                }
                text.append("\n");
            }
            return Document.from(text.toString());
        }
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

}
