package org.jeecg.modules.airag.wordtpl.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.wordtpl.consts.WordTitleEnum;
import org.jeecg.modules.airag.wordtpl.dto.*;
import org.jeecg.modules.airag.wordtpl.entity.EoaWordTemplate;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: Word模版工具类
 * @Author: chenrui
 * @Date: 2025/7/4 10:54
 */
@Component("jeecgWordTplUtils")
@Slf4j
public class WordTplUtils {


    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    /**
     * 本地：local minio：minio 阿里：alioss
     */
    @Value(value = "${jeecg.uploadType}")
    private String uploadType;


    /**
     * 根据设计数据生成word模版
     *
     * @param template
     * @param outputStream
     * @return
     * @author chenrui
     * @date 2025/7/9 11:14
     */
    public void generateWordTemplate(EoaWordTemplate template, ByteArrayOutputStream outputStream) {
        AssertUtils.assertNotEmpty("模版数据不能为空", template);
        XWPFDocument doc = new XWPFDocument();

        //添加自定义标题
        for (int i = 1; i <= 6; i++) {
            WordUtil.addCustomHeadingStyle(doc, "标题" + i, i);
        }

        // 设置纸张大小
        WordUtil.setPaperSize(doc, template.getHeight(), template.getWidth(), template.getPaperDirection());
        // 设置边距
        JSONArray margins = JSONArray.parseArray(template.getMargins());
        WordUtil.setPaperMargins(doc, margins);

        // TODO author: chenrui for:水印设置 date:2025/7/4

        // 渲染页眉和页脚
        renderHeaderAndFooter(template, doc);

        // 文档主体渲染 date:2025/7/4
        renderDocumentBody(doc, template);

        // 页码
//        addPageNumbers(doc, 0);

        try {
            doc.write(outputStream);
            outputStream.flush();
            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("生成Word模版失败: " + e.getMessage(), e);
        }
    }

    /**
     * 渲染页眉和页脚
     *
     * @param template
     * @param doc
     * @author chenrui
     * @date 2025/7/10 17:52
     */
    private static void renderHeaderAndFooter(EoaWordTemplate template, XWPFDocument doc) {
        //页眉
        JSONArray header = JSON.parseArray(template.getHeader());
        if (oConvertUtils.isObjectNotEmpty(header)) {
            XWPFHeader docHeader = doc.createHeader(HeaderFooterType.DEFAULT);
            XWPFParagraph paragraph = null;
            for (int i = 0; i < header.size(); i++) {
                String type = header.getJSONObject(i).getString("type") == null ? "" : header.getJSONObject(i).getString("type");
                switch (type) {
                    case "separator":
                        if (i == 0 || paragraph == null) {
                            paragraph = docHeader.createParagraph();
                        }
                        WordUtil.addSeparator(paragraph, header.getJSONObject(i));
                        if (i == 0) {
                            paragraph = null;
                        }
                        break;
                    default:
                        if (paragraph == null) {
                            paragraph = docHeader.createParagraph();
                        }
                        WordUtil.addParagraph(paragraph, header.getJSONObject(i), null, true);
                        break;
                }
            }
        }
        //页脚
        JSONArray footer = JSON.parseArray(template.getFooter());
        if (oConvertUtils.isObjectNotEmpty(footer)) {
            XWPFFooter docFooter = doc.createFooter(HeaderFooterType.DEFAULT);
            XWPFParagraph paragraph = null;
            for (int i = 0; i < footer.size(); i++) {
                String type = footer.getJSONObject(i).getString("type") == null ? "" : footer.getJSONObject(i).getString("type");
                switch (type) {
                    case "separator":
                        if (i == 0 || paragraph == null) {
                            paragraph = docFooter.createParagraph();
                        }
                        WordUtil.addSeparator(paragraph, footer.getJSONObject(i));
                        if (i == 0) {
                            paragraph = null;
                        }
                        break;
                    default:
                        if (paragraph == null) {
                            paragraph = docFooter.createParagraph();
                        }
                        WordUtil.addParagraph(paragraph, footer.getJSONObject(i), null, true);
                        break;
                }
            }
        }
    }

//    [poi5.x]
//    private static void addPageNumbers(XWPFDocument doc, int startingNum) {
//        CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr() ? doc.getDocument().getBody().getSectPr()
//                : doc.getDocument().getBody().addNewSectPr();
//        CTPageNumber pgNum = sectPr.isSetPgNumType() ? sectPr.getPgNumType() : sectPr.addNewPgNumType();
//        pgNum.setStart(BigInteger.valueOf(startingNum));
//        pgNum.setFmt(STNumberFormat.DECIMAL);
//    }

    /**
     * 渲染文档主体内容
     *
     * @param doc
     * @param template
     * @author chenrui
     * @date 2025/7/4 14:00
     */
    private void renderDocumentBody(XWPFDocument doc, EoaWordTemplate template) {

        // TODO author: chenrui for:整理图表???? date:2025/7/4

        // TODO author: chenrui for:整理条码 date:2025/7/4

        //文档主体内容
        JSONArray main = JSON.parseArray(template.getMain());
        int abstractNumID = 1;
        if (oConvertUtils.isObjectNotEmpty(main)) {
            XWPFParagraph paragraph = null;
            String lastType = "";
            for (int i = 0; i < main.size(); i++) {
                JSONObject content = main.getJSONObject(i);
                String type = content.getString("type") == null ? "" : content.getString("type");
                switch (type) {
                    case "":
                        if (content.getString("value").startsWith("\n") ||
                                (!type.equals(lastType) && !"tab".equals(lastType)
                                        && !"superscript".equals(lastType)
                                        && !"subscript".equals(lastType)
                                        && !"separator".equals(lastType)
                                        && !"hyperlink".equals(lastType))) {
                            content.put("value", content.getString("value").replaceFirst("\n", ""));
                            paragraph = doc.createParagraph();
                        }
                        if ("separator".equals(lastType)) {
                            String value = content.getString("value");
                            if (oConvertUtils.isObjectNotEmpty(value) && value.startsWith("\n")) {
                                content.put("value", value.replaceFirst("\n", ""));
                            }
                        }
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addParagraph(paragraph, content, null, false);
                        break;
                    case "title":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addTitleParagraph(paragraph, content);
                        break;
                    case "tab":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addTab(paragraph, null);
                        break;
                    case "table":
                        abstractNumID = WordUtil.addTable(doc, content, abstractNumID);
                        break;
                    case "superscript":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addSubSupScript(paragraph, content, "sup");
                        break;
                    case "subscript":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addSubSupScript(paragraph, content, "sub");
                        break;
                    case "separator":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addSeparator(paragraph, content);
                        break;
                    case "list":
                        abstractNumID = WordUtil.addList(doc, content, abstractNumID);
                        break;
                    case "hyperlink":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addHyperlink(paragraph, content);
                        break;
                    case "pageBreak":
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        WordUtil.addPageBreak(paragraph);
                        break;
                    case "image":
//                        String chartUrlPrefix = MessageUtil.getValue("chart.url.prefix");
                        String url = content.getString("value");
                        if (paragraph == null) {
                            paragraph = doc.createParagraph();
                        }
                        // TODO author: chenrui for:图表和条码 date:2025/7/4
//                        if (url.contains(chartUrlPrefix)) {
//                            //图表
//                            if (!StringUtil.isEmptyMap(docTplChartsMap)) {
//                                if (docTplChartsMap.containsKey(url)) {
//                                    DocTplCharts tplCharts = docTplChartsMap.get(url).get(0);
//                                    DocChartSettingDto docChartSettingDto = new DocChartSettingDto();
//                                    BeanUtils.copyProperties(tplCharts, docChartSettingDto);
//                                    WordUtil.addChart(doc, paragraph, content, dynamicData, isTemplate, docChartSettingDto);
//                                } else {
//                                    WordUtil.addImage(paragraph, content);
//                                }
//                            } else if (!StringUtil.isEmptyMap(docTplCodesMap)) {
//                                if (docTplCodesMap.containsKey(url) && !isTemplate) {
//                                    DocTplCodes tplCodes = docTplCodesMap.get(url).get(0);
//                                    Map<String, Object> data = null;
//                                    if (dynamicData.get(tplCodes.getDatasetName()) != null) {
//                                        Object obj = dynamicData.get(tplCodes.getDatasetName());
//                                        if (obj instanceof List) {
//                                            List<Map<String, Object>> datas = (List<Map<String, Object>>) obj;
//                                            data = datas.get(0);
//                                        } else {
//                                            data = (Map<String, Object>) dynamicData.get(tplCodes.getDatasetName());
//                                        }
//
//                                    }
//                                    int width = content.getIntValue("width");
//                                    int height = content.getIntValue("height");
//                                    if (data != null) {
//                                        Object value = data.get(tplCodes.getValueField());
//                                        if (value != null) {
//                                            byte[] codeByte = null;
//                                            if (tplCodes.getCodeType().intValue() == 1) {
//                                                codeByte = BarCodeUtil.generateBarcodeImage(String.valueOf(value), width, height);
//                                            } else {
//                                                codeByte = QRCodeUtil.generateQRCodeImage(String.valueOf(value), width, height);
//                                            }
//                                            WordUtil.addImage(paragraph, content, codeByte);
//                                        }
//                                    }
//                                } else {
//                                    WordUtil.addImage(paragraph, content);
//                                }
//                            } else {
//                                WordUtil.addImage(paragraph, content);
//                            }
//                        } else {
                        //图片
                        WordUtil.addImage(paragraph, content);
//                        }
                        break;
                    default:
                        if (content.getString("value").startsWith("\n") ||
                                (!type.equals(lastType) && !"tab".equals(lastType)
                                        && !"superscript".equals(lastType)
                                        && !"subscript".equals(lastType)
                                        && !"separator".equals(lastType))) {
                            content.put("value", content.getString("value").replaceFirst("\n", ""));
                            paragraph = doc.createParagraph();
                        }
                        if ("separator".equals(lastType)) {
                            String value = content.getString("value");
                            if (oConvertUtils.isObjectNotEmpty(value) && value.startsWith("\n")) {
                                content.put("value", value.replaceFirst("\n", ""));
                            }
                        }
                        WordUtil.addParagraph(paragraph, content, null, false);
                        break;
                }
                lastType = type;
            }
        }
    }

    public EoaWordTemplate parseWordFile(InputStream wordFileIs) throws Exception {
        AssertUtils.assertNotEmpty("请上传word文档", wordFileIs);
        EoaWordTemplate template = new EoaWordTemplate();
        XWPFDocument xwpfDocument = new XWPFDocument(wordFileIs);
        CTSectPr sectPr = xwpfDocument.getDocument().getBody().getSectPr();
        if (sectPr != null) {
            if (sectPr.getPgSz().getOrient() != null) {
                if ("landscape".equals(String.valueOf(sectPr.getPgSz().getOrient()))) {
                    template.setPaperDirection("horizontal");
                }
            }
            // [poi 5.x版本] getW() 和 getH() 返回 Object，需要强转为 BigInteger
            BigInteger w = (BigInteger) sectPr.getPgSz().getW();
            double width = Math.ceil((double) w.intValue() / 20 * 1.33445);
            BigInteger h = (BigInteger) sectPr.getPgSz().getH();
            double height = Math.ceil((double) h.intValue() / 20 * 1.33445);
            if ("horizontal".equals(template.getPaperDirection())) {
                template.setHeight((int) width);
                template.setWidth((int) height);
            } else {
                template.setHeight((int) height);
                template.setWidth((int) width);
            }
        }
        List<Object> headerElements = new ArrayList<>();
        List<XWPFHeader> headers = xwpfDocument.getHeaderList();
        if (oConvertUtils.isObjectNotEmpty(headers)) {
            for (int i = 0; i < headers.size(); i++) {
                XWPFHeader header = headers.get(i);
                List<XWPFParagraph> paragraphs = header.getParagraphs();
                if (oConvertUtils.isObjectNotEmpty(paragraphs)) {
                    for (int j = 0; j < paragraphs.size(); j++) {
                        parseTextParagraph(paragraphs.get(j), headerElements, j == 0);
                    }
                }
            }
        }
        List<Object> footerElements = new ArrayList<>();
        List<XWPFFooter> footers = xwpfDocument.getFooterList();
        if (oConvertUtils.isObjectNotEmpty(footers)) {
            for (int i = 0; i < footers.size(); i++) {
                XWPFFooter footer = footers.get(i);
                List<XWPFParagraph> paragraphs = footer.getParagraphs();
                if (oConvertUtils.isObjectNotEmpty(paragraphs)) {
                    for (int j = 0; j < paragraphs.size(); j++) {
                        parseTextParagraph(paragraphs.get(j), footerElements, j == 0);
                    }
                }
            }
        }

        List<Object> documentElements = new ArrayList<>();

        List<IBodyElement> bodyElements = xwpfDocument.getBodyElements();
        if (oConvertUtils.isObjectNotEmpty(bodyElements)) {
            Map<BigInteger, JSONObject> listMap = new HashMap<>();
            for (int i = 0; i < bodyElements.size(); i++) {
                IBodyElement iBodyElement = bodyElements.get(i);
                if (iBodyElement instanceof XWPFParagraph) {
                    XWPFParagraph paragraph = (XWPFParagraph) iBodyElement;
                    parseParagraph(paragraph, documentElements, listMap, i == 0);
                } else if (iBodyElement instanceof XWPFTable) {
                    parseTable((XWPFTable) iBodyElement, documentElements);
                }

            }
        }
        xwpfDocument.close();
        template.setMain(JSON.toJSONString(documentElements));
        template.setHeader(JSON.toJSONString(headerElements));
        template.setFooter(JSON.toJSONString(footerElements));
        return template;
    }

    private void parseParagraph(XWPFParagraph paragraph, List<Object> documentElements, Map<BigInteger, JSONObject> listMap, boolean isFirst) throws Exception {
        if (paragraph.getNumID() != null) {
            JSONObject listObj = null;
            if (listMap.containsKey(paragraph.getNumID())) {
                listObj = listMap.get(paragraph.getNumID());
            } else {
                String listStyle = "decimal";
                String listType = "ol";
                try {
                    if ("bullet".equals(paragraph.getNumFmt())) {
                        listStyle = "disc";
                        listType = "ul";
                    }
                } catch (Exception ignored) {
                }
                listObj = new JSONObject();
                listObj.put("value", "");
                listObj.put("type", "list");
                listObj.put("listType", listType);
                listObj.put("listStyle", listStyle);
                List<Object> valueList = new ArrayList<>();
                listObj.put("valueList", valueList);
                listMap.put(paragraph.getNumID(), listObj);
                documentElements.add(listObj);
            }
            parseListParagraph(paragraph, listObj);
        } else if (paragraph.getStyle() != null) {
            parseTitleParagraph(paragraph, documentElements);
        } else {
            parseTextParagraph(paragraph, documentElements, isFirst);
        }
    }

    private void parseTable(XWPFTable table, List<Object> documentElements) throws Exception {
        List<JSONObject> colgroup = new ArrayList<>();
        WordTableDTO wordTableDto = new WordTableDTO();
        int height = 0;
        List<XWPFTableRow> rows = table.getRows();
        Map<String, Object> mergeCells = new HashMap<>();
        List<WordTableRowDTO> wordTableRowDTOS = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List<XWPFTableCell> cells = rows.get(i).getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                if (cell == null) {
                    continue;
                }
                if (cell.getCTTc().getTcPr().getGridSpan() != null) {
                    int colspan = cell.getCTTc().getTcPr().getGridSpan().getVal().intValue();
                    for (int k = 1; k < colspan; k++) {
                        rows.get(i).getTableCells().add(j + k, null);
                    }
                }
            }
        }
        for (int i = 0; i < rows.size(); i++) {
            WordTableRowDTO wordTableRowDto = new WordTableRowDTO();
            height = height + rows.get(i).getHeight();
            wordTableRowDto.setHeight(rows.get(i).getHeight());
            List<WordTableCellDTO> wordTableCellDtos = new ArrayList<>();
            List<XWPFTableCell> cells = rows.get(i).getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                if (cell == null || mergeCells.containsKey(i + "_" + j)) {
                    continue;
                }
                WordTableCellDTO wordTableCellDto = new WordTableCellDTO();
                int colspan = 1;
                int rowspan = 1;
                wordTableCellDto.setColspan(colspan);
                wordTableCellDto.setRowspan(rowspan);
                if (oConvertUtils.isObjectNotEmpty(cell.getColor())) {
                    wordTableCellDto.setBackgroundColor("#" + cell.getColor());
                }
                if (cell.getCTTc().getTcPr().getGridSpan() != null) {
                    wordTableCellDto.setColspan(cell.getCTTc().getTcPr().getGridSpan().getVal().intValue());
                }
                if (i == 0) {
                    int width = 25;
                    try {
                        width = cell.getWidth() / 16;
                    } catch (Exception e) {
                        log.warn("WordTplUtils parseTable getWidth error: {}", e.getMessage());
                    }
                    width = width / wordTableCellDto.getColspan();
                    for (int k = 0; k < wordTableCellDto.getColspan(); k++) {
                        JSONObject col = new JSONObject();
                        col.put("width", width);
                        colgroup.add(col);
                    }
                }
                if (cell.getCTTc().getTcPr().getVMerge() != null && cell.getCTTc().getTcPr().getVMerge().getVal() != null && "restart".equals(cell.getCTTc().getTcPr().getVMerge().getVal().toString())) {
                    getRowSpan(i, j, wordTableCellDto, mergeCells, rows);
                }
                List<XWPFParagraph> cellParagraph = cell.getParagraphs();
                if (oConvertUtils.isObjectNotEmpty(cellParagraph)) {
                    List<Object> docTextDtos = new ArrayList<>();
                    for (int k = 0; k < cellParagraph.size(); k++) {
                        parseParagraph(cellParagraph.get(k), docTextDtos, new HashMap<>(), k == 0);
                    }
                    wordTableCellDto.setValue(docTextDtos);
                }
                if (cell.getVerticalAlignment() != null) {
                    if (cell.getVerticalAlignment() == XWPFTableCell.XWPFVertAlign.TOP) {
                        wordTableCellDto.setVerticalAlign("top");
                    } else if (cell.getVerticalAlignment() == XWPFTableCell.XWPFVertAlign.BOTTOM) {
                        wordTableCellDto.setVerticalAlign("bottom");
                    } else if (cell.getVerticalAlignment() == XWPFTableCell.XWPFVertAlign.CENTER) {
                        wordTableCellDto.setVerticalAlign("bottom");
                    }
                }
                wordTableCellDtos.add(wordTableCellDto);
            }
            wordTableRowDto.setTdList(wordTableCellDtos);
            wordTableRowDTOS.add(wordTableRowDto);
        }
        wordTableDto.setWidth(table.getWidth());
        wordTableDto.setHeight(height);
        wordTableDto.setTrList(wordTableRowDTOS);
        wordTableDto.setColgroup(colgroup);
        documentElements.add(wordTableDto);
    }

    private void parseListParagraph(XWPFParagraph paragraph, JSONObject listObj) {
        List<Object> valueList = (List<Object>) listObj.get("valueList");
        List<XWPFRun> runs = paragraph.getRuns();
        if (oConvertUtils.isObjectNotEmpty(runs)) {
            for (int i = 0; i < runs.size(); i++) {
                WordTextDTO wordTextDTO = new WordTextDTO();
                XWPFRun xwpfRun = runs.get(i);
                String text = String.valueOf(xwpfRun);
                if (text.equals("\t")) {
                    wordTextDTO.setType("tab");
                    if (i == 0) {
                        text = "\n" + text;
                    }
                    wordTextDTO.setValue(text);
                    valueList.add(wordTextDTO);
                    continue;
                }
                if (i == 0 && !text.startsWith("\n")) {
                    text = "\n" + text;
                }
                wordTextDTO.setValue(text);
                if (oConvertUtils.isObjectNotEmpty(xwpfRun.getColor())) {
                    wordTextDTO.setColor("#" + xwpfRun.getColor());
                }
                if (xwpfRun.isBold()) {
                    wordTextDTO.setBold(true);
                }
                if (xwpfRun.isItalic()) {
                    wordTextDTO.setItalic(true);
                }
                if (xwpfRun.isStrikeThrough()) {
                    wordTextDTO.setStrikeout(true);
                }
                if (xwpfRun.getUnderline().getValue() != UnderlinePatterns.NONE.getValue()) {
                    wordTextDTO.setUnderline(true);
                }
                wordTextDTO.setSize((int) (xwpfRun.getFontSize() == -1 ? 14 : xwpfRun.getFontSize() * 1.33445));
                if (oConvertUtils.isObjectNotEmpty(xwpfRun.getFontFamily())) {
                    wordTextDTO.setFont(xwpfRun.getFontFamily());
                }
                // [poi 5.x版本] 获取高亮颜色
                if (xwpfRun.isHighlighted()) {
                    String color = WordUtil.getHighlightByName(xwpfRun.getTextHighlightColor().toString());
                    if (oConvertUtils.isObjectNotEmpty(color)) {
                        wordTextDTO.setHighlight(color);
                    }
                }
                // [poi 4.x版本] 获取高亮颜色
//                if (xwpfRun.getCTR() != null && xwpfRun.getCTR().getRPr() != null && xwpfRun.getCTR().getRPr().isSetHighlight()) {
//                    String highlightVal = xwpfRun.getCTR().getRPr().getHighlight().getVal().toString();
//                    String color = WordUtil.getHighlightByName(highlightVal);
//                    if (oConvertUtils.isObjectNotEmpty(color)) {
//                        wordTextDTO.setHighlight(color);
//                    }
//                }
                valueList.add(wordTextDTO);
            }
        }
    }

    private void parseTitleParagraph(XWPFParagraph paragraph, List<Object> documentElements) {
        boolean isSeperator = isSeperator(paragraph);
        JSONObject titleParagraph = new JSONObject();
        titleParagraph.put("value", "");
        titleParagraph.put("type", "title");
        List<Object> valueList = new ArrayList<>();
        titleParagraph.put("valueList", valueList);
        int titleFontSize = 26;
        String level = WordTitleEnum.FIRST.getCode();
        if ("1".equals(paragraph.getStyle())) {
            level = WordTitleEnum.FIRST.getCode();
        } else if ("2".equals(paragraph.getStyle())) {
            titleFontSize = 24;
            level = WordTitleEnum.SECOND.getCode();
        } else if ("3".equals(paragraph.getStyle())) {
            titleFontSize = 22;
            level = WordTitleEnum.THIRD.getCode();
        } else if ("4".equals(paragraph.getStyle())) {
            titleFontSize = 20;
            level = WordTitleEnum.FOURTH.getCode();
        } else if ("5".equals(paragraph.getStyle())) {
            titleFontSize = 18;
            level = WordTitleEnum.FIFTH.getCode();
        } else if ("6".equals(paragraph.getStyle())) {
            titleFontSize = 16;
            level = WordTitleEnum.SIXTH.getCode();
        }
        titleParagraph.put("level", level);
        List<XWPFRun> runs = paragraph.getRuns();
        if (oConvertUtils.isObjectNotEmpty(runs)) {
            for (int i = 0; i < runs.size(); i++) {
                WordTextDTO wordTextDTO = new WordTextDTO();
                XWPFRun xwpfRun = runs.get(i);
                String text = String.valueOf(xwpfRun);
                wordTextDTO.setValue(text == null ? "" : text);
                wordTextDTO.setBold(true);
                wordTextDTO.setSize(titleFontSize);
                if (paragraph.getAlignment() != null) {
                    if (paragraph.getAlignment().getValue() == ParagraphAlignment.LEFT.getValue()) {
                        wordTextDTO.setRowFlex("left");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.RIGHT.getValue()) {
                        wordTextDTO.setRowFlex("right");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.CENTER.getValue()) {
                        wordTextDTO.setRowFlex("center");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.BOTH.getValue()) {
                        wordTextDTO.setRowFlex("alignment");
                    }
                }
                valueList.add(wordTextDTO);
            }
        }
        documentElements.add(titleParagraph);
        if (isSeperator) {
            WordTextDTO wordTextDTO = new WordTextDTO();
            wordTextDTO.setType("separator");
            wordTextDTO.setRowFlex("left");
            wordTextDTO.setValue("\n");
            CTP ctp = paragraph.getCTP();
            CTPPr pr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
            CTPBdr border = pr.isSetPBdr() ? pr.getPBdr() : pr.addNewPBdr();
            CTBorder ct = border.isSetBottom() ? border.getBottom() : border.addNewBottom();
            int seperatorType = ct.getVal().intValue();
            List<Object> dashArray = new ArrayList<>();
            switch (seperatorType) {
                case 3:
                    break;
                case 6:
                    dashArray.add(1);
                    dashArray.add(1);
                    wordTextDTO.setDashArray(dashArray);
                    break;
                case 7:
                    dashArray.add(4);
                    dashArray.add(4);
                    wordTextDTO.setDashArray(dashArray);
                    break;
                case 8:
                    dashArray.add(7);
                    dashArray.add(3);
                    dashArray.add(3);
                    dashArray.add(3);
                    wordTextDTO.setDashArray(dashArray);
                    break;
                case 9:
                    dashArray.add(6);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    wordTextDTO.setDashArray(dashArray);
                    break;
                case 22:
                    dashArray.add(3);
                    dashArray.add(1);
                    wordTextDTO.setDashArray(dashArray);
                    break;
                default:
                    break;
            }
            documentElements.add(wordTextDTO);
        }
    }

    private void parseTextParagraph(XWPFParagraph paragraph, List<Object> documentElements, boolean isFirst) throws Exception {
        List<XWPFRun> runs = paragraph.getRuns();
        boolean isSeperator = isSeperator(paragraph);
        if (oConvertUtils.isObjectNotEmpty(runs)) {
            for (int i = 0; i < runs.size(); i++) {
                WordTextDTO wordTextDto = new WordTextDTO();
                XWPFRun xwpfRun = runs.get(i);
                List<CTBr> brList = xwpfRun.getCTR().getBrList();
                if (oConvertUtils.isObjectNotEmpty(brList)) {
                    for (CTBr br : brList) {
                        if (br.getType() == STBrType.PAGE) {
                            WordTextDTO pageBreak = new WordTextDTO();
                            pageBreak.setType("pageBreak");
                            pageBreak.setValue("\n");
                            documentElements.add(pageBreak);
                        }
                    }
                }
                List<XWPFPicture> pictures = xwpfRun.getEmbeddedPictures();
                if (oConvertUtils.isObjectNotEmpty(pictures)) {
                    if (i == runs.size() - 1) {
                        WordTextDTO breakWordTextDto = new WordTextDTO();
                        breakWordTextDto.setValue("\n");
                        documentElements.add(breakWordTextDto);
                    }
                    for (int j = 0; j < pictures.size(); j++) {
                        WordImageDTO wordImageDto = new WordImageDTO();
                        XWPFPicture picture = pictures.get(j);
                        byte[] bytes = picture.getPictureData().getData();
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                        MultipartFile file = new CustomMultipartFile("file", picture.getPictureData().getFileName(), "application/octet-stream", bytes);
                        String url = this.uploadFile(file, "wordTplAssets");
                        wordImageDto.setValue(url);
                        wordImageDto.setWidth(image.getWidth());
                        wordImageDto.setHeight(image.getHeight());
                        if (paragraph.getAlignment() != null) {
                            if (paragraph.getAlignment().getValue() == ParagraphAlignment.LEFT.getValue()) {
                                wordImageDto.setRowFlex("left");
                            } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.RIGHT.getValue()) {
                                wordImageDto.setRowFlex("right");
                            } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.CENTER.getValue()) {
                                wordImageDto.setRowFlex("center");
                            } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.BOTH.getValue()) {
                                wordImageDto.setRowFlex("alignment");
                            }
                        }
                        documentElements.add(wordImageDto);
                    }
                    continue;
                }
                String text = String.valueOf(xwpfRun);
                if (text.equals("\t")) {
                    if (paragraph.getAlignment() != null) {
                        if (paragraph.getAlignment().getValue() == ParagraphAlignment.LEFT.getValue()) {
                            wordTextDto.setRowFlex("left");
                        } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.RIGHT.getValue()) {
                            wordTextDto.setRowFlex("right");
                        } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.CENTER.getValue()) {
                            wordTextDto.setRowFlex("center");
                        } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.BOTH.getValue()) {
                            wordTextDto.setRowFlex("alignment");
                        }
                    }
                    wordTextDto.setType("tab");
                    if (i == 0 && !isFirst) {
                        text = "\n" + text;
                    }
                    wordTextDto.setValue(text);
                    documentElements.add(wordTextDto);
                    continue;
                }
                if (i == 0 && !isFirst) {
                    text = "\n" + text;
                }
                String scriptType = getSupSubScriptType(xwpfRun);
                if (oConvertUtils.isObjectNotEmpty(scriptType)) {
                    wordTextDto.setType(scriptType);
                }
                wordTextDto.setValue(text);
                if (oConvertUtils.isObjectNotEmpty(xwpfRun.getColor())) {
                    wordTextDto.setColor("#" + xwpfRun.getColor());
                }
                if (xwpfRun.isBold()) {
                    wordTextDto.setBold(true);
                }
                if (xwpfRun.isItalic()) {
                    wordTextDto.setItalic(true);
                }
                if (xwpfRun.isStrikeThrough()) {
                    wordTextDto.setStrikeout(true);
                }
                if (xwpfRun.getUnderline().getValue() != UnderlinePatterns.NONE.getValue()) {
                    wordTextDto.setUnderline(true);
                }
                wordTextDto.setSize((int) (xwpfRun.getFontSize() == -1 ? 14 : xwpfRun.getFontSize() * 1.33445));
                if (oConvertUtils.isObjectNotEmpty(xwpfRun.getFontFamily())) {
                    wordTextDto.setFont(xwpfRun.getFontFamily());
                }
                // [poi 5.x版本] 获取高亮颜色
                if (xwpfRun.isHighlighted()) {
                    String color = WordUtil.getHighlightByName(xwpfRun.getTextHighlightColor().toString());
                    if (oConvertUtils.isObjectNotEmpty(color)) {
                        wordTextDto.setHighlight(color);
                    }
                }
                // [poi 4.x版本] 获取高亮颜色
//                if (xwpfRun.getCTR() != null && xwpfRun.getCTR().getRPr() != null && xwpfRun.getCTR().getRPr().isSetHighlight()) {
//                    String highlightVal = xwpfRun.getCTR().getRPr().getHighlight().getVal().toString();
//                    String color = WordUtil.getHighlightByName(highlightVal);
//                    if (oConvertUtils.isObjectNotEmpty(color)) {
//                        wordTextDto.setHighlight(color);
//                    }
//                }
                if (paragraph.getAlignment() != null) {
                    if (paragraph.getAlignment().getValue() == ParagraphAlignment.LEFT.getValue()) {
                        wordTextDto.setRowFlex("left");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.RIGHT.getValue()) {
                        wordTextDto.setRowFlex("right");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.CENTER.getValue()) {
                        wordTextDto.setRowFlex("center");
                    } else if (paragraph.getAlignment().getValue() == ParagraphAlignment.BOTH.getValue()) {
                        wordTextDto.setRowFlex("alignment");
                    }
                }
                documentElements.add(wordTextDto);
            }
        }
        if (isSeperator) {
            WordTextDTO wordTextDto = new WordTextDTO();
            wordTextDto.setType("separator");
            wordTextDto.setRowFlex("left");
            wordTextDto.setValue("\n");
            CTP ctp = paragraph.getCTP();
            CTPPr pr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
            CTPBdr border = pr.isSetPBdr() ? pr.getPBdr() : pr.addNewPBdr();
            CTBorder ct = border.isSetBottom() ? border.getBottom() : border.addNewBottom();
            int seperatorType = ct.getVal().intValue();
            List<Object> dashArray = new ArrayList<>();
            switch (seperatorType) {
                case 3:
                    break;
                case 6:
                    dashArray.add(1);
                    dashArray.add(1);
                    wordTextDto.setDashArray(dashArray);
                    break;
                case 7:
                    dashArray.add(4);
                    dashArray.add(4);
                    wordTextDto.setDashArray(dashArray);
                    break;
                case 8:
                    dashArray.add(7);
                    dashArray.add(3);
                    dashArray.add(3);
                    dashArray.add(3);
                    wordTextDto.setDashArray(dashArray);
                    break;
                case 9:
                    dashArray.add(6);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    dashArray.add(2);
                    wordTextDto.setDashArray(dashArray);
                    break;
                case 22:
                    dashArray.add(3);
                    dashArray.add(1);
                    wordTextDto.setDashArray(dashArray);
                    break;
                default:
                    break;
            }
            documentElements.add(wordTextDto);
        }
    }

    private void getRowSpan(int r, int c, WordTableCellDTO wordTableCellDto, Map<String, Object> mergeCells, List<XWPFTableRow> rows) {
        for (int i = (r + 1); i < rows.size(); i++) {
            List<XWPFTableCell> cells = rows.get(i).getTableCells();
            if (c <= (cells.size() - 1)) {
                XWPFTableCell cell = cells.get(c);
                if (cell != null && cell.getCTTc().getTcPr().getVMerge() != null && cell.getCTTc().getTcPr().getVMerge().getVal() == null) {
                    wordTableCellDto.setRowspan(wordTableCellDto.getRowspan() + 1);
                    mergeCells.put(i + "_" + c, "1");
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 是否分隔符
     *
     * @param paragraph
     * @return
     * @author chenrui
     * @date 2025/7/9 11:40
     */
    private boolean isSeperator(XWPFParagraph paragraph) {
        boolean result = true;
        CTP ctp = paragraph.getCTP();
        if (ctp == null) {
            return false;
        }
        CTPPr pr = ctp.getPPr();
        if (pr == null) {
            return false;
        }
        CTPBdr border = pr.getPBdr();
        if (border == null) {
            return false;
        }
        CTBorder ct = border.getBottom();
        if (ct == null) {
            return false;
        }
        return result;
    }

    /**
     * 获取上下标
     *
     * @param run
     * @return
     * @author chenrui
     * @date 2025/7/9 12:07
     */
    private String getSupSubScriptType(XWPFRun run) {
        String result = "";
        // [poi 5.x版本] 获取上下标
       if (run.getCTR().getRPr() != null && run.getCTR().getRPr().getVertAlignArray() != null && run.getCTR().getRPr().getVertAlignArray().length > 0) {
            CTVerticalAlignRun CTVerticalAlignRun = run.getCTR().getRPr().getVertAlignArray()[0];
            result = String.valueOf(CTVerticalAlignRun.getVal());
       }
        
        // [poi 4.x版本] 获取上下标
//        if (run.getCTR() != null) {
//            if (run.getCTR().getRPr() != null) {
//                if (run.getCTR().getRPr() != null && run.getCTR().getRPr().isSetVertAlign()) {
//                    CTVerticalAlignRun ctVerticalAlignRun = run.getCTR().getRPr().getVertAlign();
//                    result = String.valueOf(ctVerticalAlignRun.getVal());
//                }
//            }
//        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @author chenrui
     * @date 2025/7/7 18:40
     */
    private String uploadFile(MultipartFile file, String bizPath) {
        String savePath;
        if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
            savePath = CommonUtils.uploadLocal(file, bizPath, uploadpath);
        } else {
            savePath = CommonUtils.upload(file, bizPath, uploadType);
        }
        return savePath;
    }


    public static void main(String[] args) {
        EoaWordTemplate template = new EoaWordTemplate();
        template.setHeight(1123);
        template.setWidth(794);
        template.setPaperDirection("vertical");
        template.setMargins("[25.4,31.8,25.4,31.8]");
//        template.setMain("[{\"value\":\"\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"center\",\"dashArray\":[]},{\"value\":\"\",\"type\":\"title\",\"valueList\":[{\"value\":\"会\",\"font\":\"微软雅黑\",\"size\":26,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"center\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"微软雅黑\",\"size\":26,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"center\",\"dashArray\":[]},{\"value\":\"纪\",\"font\":\"微软雅黑\",\"size\":26,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"center\",\"dashArray\":[]},{\"value\":\"要\",\"font\":\"微软雅黑\",\"size\":26,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"center\",\"dashArray\":[]}],\"level\":\"first\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#FF0000\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"一\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"、\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"会\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"时\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"间\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"：\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"ml.meeting_time\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"二\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"、\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"会\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"地\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"点\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"：\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{{ml.location}}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"三\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"、\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"会\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"题\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"：\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{ml.agenda}}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"四\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"、\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"会\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"议\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"主\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"持\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"人\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"：\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{{ml.moderator}}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"五\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"、\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"参\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"会\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"人\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"员\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"：\",\"font\":\"宋体\",\"size\":16,\"bold\":true,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{{ml.attendees}}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\",\"dashArray\":[]},{\"value\":\"{{ml.content}}\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"left\"},{\"value\":\"\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"right\",\"dashArray\":[]},{\"value\":\"{{ml.formatted_date}}\\n\",\"font\":\"宋体\",\"size\":16,\"bold\":false,\"color\":\"#auto\",\"italic\":false,\"underline\":false,\"strikeout\":false,\"rowFlex\":\"right\"},{\"value\":\"http://localhost:9099/images/2025-07-03/1940720058774863873.png?t=1751538693508\",\"type\":\"image\",\"rowFlex\":\"right\",\"width\":137.64250695113952,\"height\":125.02599781096147,\"imgDisplay\":\"float-top\",\"imgFloatPosition\":{\"x\":551.6720123291016,\"y\":358.7252540588379,\"pageNo\":0}},{\"value\":\"     \",\"rowFlex\":\"right\"}]");
        template.setMain("[{\"value\":\"  \\n\\n222222222222\\n2222222222\"},{\"value\":\"http://localhost:9099/images/2025-07-08/1942502516700803074.png?t=1751963664591\",\"type\":\"image\",\"width\":200,\"height\":200,\"imgDisplay\":\"float-top\",\"imgFloatPosition\":{\"x\":232.421875,\"y\":111,\"pageNo\":0}},{\"value\":\"22222212312312312333333333333333333333333333333333\"}]");

        WordTplUtils utils = new WordTplUtils();
        ByteArrayOutputStream wordTemplateOut = new ByteArrayOutputStream();
        utils.generateWordTemplate(template, wordTemplateOut);
        try (FileOutputStream fos = new FileOutputStream("/Users/chenrui/work/temp/testWordTpl/test.docx")) {
            wordTemplateOut.writeTo(fos);
            fos.flush();
            //System.out.println("Word模版已保存到本地文件。");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 自定义的MultipartFile
     */
    private static class CustomMultipartFile implements MultipartFile {
        private final File file;
        private final byte[] content;
        private final String name;
        private final String originalFilename;
        private final String contentType;

        // 通过 File 构造
        public CustomMultipartFile(File file, String name, String contentType) {
            this.file = file;
            this.content = null;
            this.name = name;
            this.originalFilename = file.getName();
            this.contentType = contentType;
        }

        // 通过字节数组构造
        public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.file = null;
            this.content = content;
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            if (file != null) {
                return file.length() == 0;
            }
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            if (file != null) {
                return file.length();
            }
            return content == null ? 0 : content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            if (file != null) {
                try (FileInputStream fis = new FileInputStream(file);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    return baos.toByteArray();
                }
            }
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (file != null) {
                return Files.newInputStream(file.toPath());
            }
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            if (file != null) {
                try (InputStream in = getInputStream(); OutputStream out = Files.newOutputStream(dest.toPath())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else if (content != null) {
                try (OutputStream out = Files.newOutputStream(dest.toPath())) {
                    out.write(content);
                }
            }
        }
    }


}
