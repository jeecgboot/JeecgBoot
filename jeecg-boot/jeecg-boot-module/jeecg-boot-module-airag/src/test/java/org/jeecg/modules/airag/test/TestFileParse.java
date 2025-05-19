package org.jeecg.modules.airag.test;

import dev.langchain4j.data.document.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.parser.AutoDetectParser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.llm.document.TikaDocumentParser;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.wildfly.common.Assert;

import java.io.File;
import java.io.IOException;

/**
 * @Description: 文件解析测试
 * @Author: chenrui
 * @Date: 2025/2/11 16:11
 */
@Slf4j
public class TestFileParse {

    @Test
    public void testParseTxt() {
        readFile("test.txt");
    }

    @Test
    public void testParsePdf() {
        readFile("test.pdf");
    }

    @Test
    public void testParseMd() {
        readFile("test.md");
    }

    @Test
    public void testParseDoc() {
        readFile("test.docx");
    }

    @Test
    public void testParseDoc2003() {
        readFile("test.doc");
    }

    @Test
    public void testParseExcel() {
        readFile("test.xlsx");
    }

    @Test
    public void testParseExcel2003() {
        readFile("test.xls");
    }

    @Test
    public void testParsePPT() {
        readFile("test.pptx");
    }
    @Test
    public void testParsePPT2003() {
        readFile("test.ppt");
    }

    private static void readFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            File file = resource.getFile();
            TikaDocumentParser parser = new TikaDocumentParser(AutoDetectParser::new, null, null, null);
            Document document = parser.parse(file);
            Assert.assertNotNull(document);
            System.out.println(filePath + "----" + document.text());
            Assert.assertTrue(oConvertUtils.isNotEmpty(document));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
