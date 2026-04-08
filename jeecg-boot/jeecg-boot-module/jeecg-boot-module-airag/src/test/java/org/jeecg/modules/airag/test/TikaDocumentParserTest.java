package org.jeecg.modules.airag.test;

import dev.langchain4j.data.document.Document;
import org.apache.tika.parser.AutoDetectParser;
import org.jeecg.modules.airag.llm.document.TikaDocumentParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TikaDocumentParserTest {

    @Test
    public void testParseZip() throws IOException {
        File zip = File.createTempFile("test_zip_", ".zip");
        try {
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zip.toPath()))) {
                zos.putNextEntry(new ZipEntry("hello.txt"));
                zos.write("Hello from zip".getBytes());
                zos.closeEntry();
            }
            TikaDocumentParser parser = new TikaDocumentParser(AutoDetectParser::new, null, null, null);
            Document doc = parser.parse(zip);
            assertNotNull(doc);
            assertTrue(doc.text().contains("Hello from zip"));
        } finally {
            zip.delete();
        }
    }

    @Test
    public void testParseZipWithDirectory() throws IOException {
        File zip = File.createTempFile("test_zip_dir_", ".zip");
        try {
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zip.toPath()))) {
                zos.putNextEntry(new ZipEntry("subdir/"));
                zos.closeEntry();
                zos.putNextEntry(new ZipEntry("subdir/notes.txt"));
                zos.write("Notes content".getBytes());
                zos.closeEntry();
            }
            TikaDocumentParser parser = new TikaDocumentParser(AutoDetectParser::new, null, null, null);
            Document doc = parser.parse(zip);
            assertNotNull(doc);
            assertTrue(doc.text().contains("Notes content"));
        } finally {
            zip.delete();
        }
    }
}
