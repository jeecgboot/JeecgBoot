package org.jeecg.modules.airag.wordtpl.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.wordtpl.consts.WordTitleEnum;
import org.jeecg.modules.airag.wordtpl.dto.MergeColDTO;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * word工具类
 *
 * @author chenrui
 * @date 2025/7/8 17:22
 */
@Slf4j
public class WordUtil {


    /**
     * 正则表达式:是否是网页
     */
    public static final Pattern WEB_PATTERN = Pattern.compile("^(http|https)://.*");

    /**
     * 添加自定义标题样式
     *
     * @param doc
     * @param strStyleId
     * @param headingLevel
     * @author chenrui
     * @date 2025/7/4 11:08
     */
    public static void addCustomHeadingStyle(XWPFDocument doc, String strStyleId, int headingLevel) {
        // 创建一个新的样式对象，并设置样式ID
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        // 设置样式名称
        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        // 设置样式优先级，数字越小越突出
        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));
        ctStyle.setUiPriority(indentNumber);

        // 设置样式在样式栏中可见
        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // [poi5.x]设置段落属性，指定大纲级别（即标题级别）
        CTPPrGeneral ctpPrGeneral = CTPPrGeneral.Factory.newInstance();
        ctpPrGeneral.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ctpPrGeneral);

//        // [poi4.x]设置段落属性，指定大纲级别（即标题级别）
//        CTPPr ctpPr = CTPPr.Factory.newInstance();
//        ctpPr.setOutlineLvl(indentNumber);
//        ctStyle.setPPr(ctpPr);

        // 创建 XWPFStyle 并添加到文档样式集合中
        XWPFStyle style = new XWPFStyle(ctStyle);
        XWPFStyles styles = doc.createStyles();
        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }

    /**
     * 设置纸张大小
     *
     * @param document
     * @param height
     * @param width
     * @param pagerDirection
     * @author chenrui
     * @date 2025/7/4 11:29
     */
    public static void setPaperSize(XWPFDocument document, int height, int width, String pagerDirection) {
        // 计算纸张实际宽高
        int pagerWidth = (int) (width * 20 / 1.33445);
        int pagerHeight = (int) (height * 20 / 1.33445);
        // 根据纸张方向设置纸张宽高
        CTBody body = document.getDocument().getBody();
        CTSectPr sectPr = body.isSetSectPr() ? body.getSectPr() : body.addNewSectPr();
        CTPageSz pageSize = sectPr.addNewPgSz();
        if ("horizontal".equals(pagerDirection)) {
            //横版
            pageSize.setW(BigInteger.valueOf(pagerHeight));
            pageSize.setH(BigInteger.valueOf(pagerWidth));
            pageSize.setOrient(STPageOrientation.LANDSCAPE);
        } else {
            pageSize.setW(BigInteger.valueOf(pagerWidth));
            pageSize.setH(BigInteger.valueOf(pagerHeight));
            pageSize.setOrient(STPageOrientation.PORTRAIT);
        }
    }

    /**
     * 设置页面边距
     *
     * @param document
     * @param margins  单位 mm
     * @author chenrui
     * @date 2025/7/4 11:32
     */
    public static void setPaperMargins(XWPFDocument document, JSONArray margins) {
        if (oConvertUtils.isNotEmpty(margins)) {
            // 获取CTSectPr，如果没有则创建
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            CTPageMar pageMar = sectPr.addNewPgMar();

            // 设置上页边距
            pageMar.setTop(BigInteger.valueOf((long) (margins.getLongValue(0)*14.4)));
            // 设置下页边距
            pageMar.setBottom(BigInteger.valueOf((long) (margins.getLongValue(2)*14.4)));
            // 设置左页边距
            pageMar.setLeft(BigInteger.valueOf((long) (margins.getLongValue(3)*14.4)));
            // 设置右页边距
            pageMar.setRight(BigInteger.valueOf((long) (margins.getLongValue(1)*14.4)));
        }
    }

    public static void addSeparator(XWPFParagraph paragraph, JSONObject content) {
        JSONArray dashArray = content.getJSONArray("dashArray");
        CTP ctp = paragraph.getCTP();
        CTPPr pr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTPBdr border = pr.isSetPBdr() ? pr.getPBdr() : pr.addNewPBdr();
        CTBorder ct = border.isSetBottom() ? border.getBottom() : border.addNewBottom();
        if (oConvertUtils.isEmpty(dashArray) || dashArray.isEmpty()) {
            ct.setVal(STBorder.Enum.forInt(3));
        } else {
            int value = dashArray.getIntValue(0);
            switch (value) {
                case 1:
                    ct.setVal(STBorder.Enum.forInt(6));
                    break;
                case 3:
                    ct.setVal(STBorder.Enum.forInt(22));
                    break;
                case 4:
                    ct.setVal(STBorder.Enum.forInt(7));
                    break;
                case 7:
                    ct.setVal(STBorder.Enum.forInt(8));
                    break;
                case 6:
                    ct.setVal(STBorder.Enum.forInt(9));
                    break;
                default:
                    ct.setVal(STBorder.Enum.forInt(3));
                    break;
            }
        }
        ct.setSz(BigInteger.valueOf(4));
        ct.setSpace(BigInteger.ZERO);
        ct.setColor("auto");
    }

    /**
     * 添加分页符
     *
     * @param paragraph
     * @author chenrui
     * @date 2025/7/4 15:03
     */
    public static void addPageBreak(XWPFParagraph paragraph) {
        paragraph.createRun().addBreak(BreakType.PAGE);
    }

//    /**
//     * 添加页码
//     * only poi 5.x
//     * @param doc
//     * @param startingNum
//     * @author chenrui
//     * @date 2025/7/4 15:18
//     */
//    private static void addPageNumbers(XWPFDocument doc, int startingNum) {
//        CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr() ? doc.getDocument().getBody().getSectPr()
//                : doc.getDocument().getBody().addNewSectPr();
//        CTPageNumber pgNum = sectPr.isSetPgNumType() ? sectPr.getPgNumType() : sectPr.addNewPgNumType();
//        pgNum.setStart(BigInteger.valueOf(startingNum));
//        pgNum.setFmt(STNumberFormat.DECIMAL);
//    }

    /**
     * 添加段落
     *
     * @param paragraph
     * @param content
     * @param titleStyle
     * @param ignoreStartn
     * @author chenrui
     * @date 2025/7/4 14:04
     */
    public static void addParagraph(XWPFParagraph paragraph, JSONObject content, String titleStyle, boolean ignoreStartn) {
        XWPFRun run = paragraph.createRun();
        setRunText(run, content, "text", ignoreStartn);
        // 对齐方式
        String rowFlex = content.getString("rowFlex");
        // 行间距
        Float rowMargin = content.getFloat("rowMargin");
        if (oConvertUtils.isNotEmpty(rowFlex)) {
            switch (rowFlex) {
                case "left":
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    break;
                case "center":
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    break;
                case "right":
                    paragraph.setAlignment(ParagraphAlignment.RIGHT);
                    break;
                case "alignment":
                    paragraph.setAlignment(ParagraphAlignment.BOTH);
                    break;
                default:
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    break;
            }
        } else {
            paragraph.setAlignment(ParagraphAlignment.LEFT);
        }
        if (rowMargin != null) {
            setSingleLineSpacing(paragraph, rowMargin);
        }
        if (oConvertUtils.isNotEmpty(titleStyle)) {
            paragraph.setStyle(titleStyle);
        }

    }


    /**
     * 添加标题段落
     *
     * @param paragraph
     * @param content
     * @author chenrui
     * @date 2025/7/4 14:42
     */
    public static void addTitleParagraph(XWPFParagraph paragraph, JSONObject content) {
        String level = content.getString("level");
        String headStyle = "";
        if (WordTitleEnum.FIRST.getCode().equals(level)) {
            headStyle = WordTitleEnum.FIRST.getName();
        } else if (WordTitleEnum.SECOND.getCode().equals(level)) {
            headStyle = WordTitleEnum.SECOND.getName();
        } else if (WordTitleEnum.THIRD.getCode().equals(level)) {
            headStyle = WordTitleEnum.THIRD.getName();
        } else if (WordTitleEnum.FOURTH.getCode().equals(level)) {
            headStyle = WordTitleEnum.FOURTH.getName();
        } else if (WordTitleEnum.FIFTH.getCode().equals(level)) {
            headStyle = WordTitleEnum.FIFTH.getName();
        } else if (WordTitleEnum.SIXTH.getCode().equals(level)) {
            headStyle = WordTitleEnum.SIXTH.getName();
        }
        JSONArray valueList = content.getJSONArray("valueList");
        if (oConvertUtils.isNotEmpty(valueList)) {
            for (int i = 0; i < valueList.size(); i++) {
                addParagraph(paragraph, valueList.getJSONObject(i), headStyle, false);
            }
        }
    }

    /**
     * 添加制表符
     *
     * @param paragraph
     * @param run
     * @author chenrui
     * @date 2025/7/4 14:44
     */
    public static void addTab(XWPFParagraph paragraph, XWPFRun run) {
        if (run == null) {
            run = paragraph.createRun();
        }
        run.setText("       ");
        CTText ctText = run.getCTR().getTArray(0);
        ctText.setSpace(SpaceAttribute.Space.PRESERVE);
    }

    /**
     * 添加单独的行间距
     *
     * @param paragraph
     * @param rowMargin
     * @author chenrui
     * @date 2025/7/4 14:32
     */
    public static void setSingleLineSpacing(XWPFParagraph paragraph, Float rowMargin) {
        CTP ctp = paragraph.getCTP();
        CTPPr ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
        spacing.setAfter(BigInteger.valueOf(0));
        spacing.setBefore(BigInteger.valueOf(0));
        //注意设置行距类型为 EXACT
        spacing.setLineRule(STLineSpacingRule.EXACT);
        //1磅数是20
        spacing.setLine(BigInteger.valueOf((long) (rowMargin * 350)));
    }

    /**
     * 设置Run文本内容
     *
     * @param run
     * @param content
     * @param type
     * @param ignoreStartEnter
     * @author chenrui
     * @date 2025/7/4 14:33
     */
    private static void setRunText(XWPFRun run, JSONObject content, String type, boolean ignoreStartEnter) {
        //内容
        String value = content.getString("value");
        //字体
        String font = content.getString("font");
        //字体大小
        Float fontSize = content.getFloat("size");
        //是否加粗
        Boolean bold = content.getBoolean("bold");
        //字体颜色
        String color = content.getString("color");
        //是否斜体
        Boolean italic = content.getBoolean("italic");
        //高亮
        String highlight = content.getString("highlight");
        //是否下划线
        Boolean underline = content.getBoolean("underline");
        //是否删除线
        Boolean strikeout = content.getBoolean("strikeout");
        // 忽视第一个换行
        if (ignoreStartEnter) {
            if (oConvertUtils.isNotEmpty(value) && value.startsWith("\n")) {
                value = value.replaceFirst("\n", "");
            }
        }
        if (value.contains("\n")) {
            if ("\n".equals(value)) {
                //update-begin---author:chenrui ---date:20250714  for：[QQYUN-13104]【word模版】设计器中有空格，导出到word空格没了------------
                run.addBreak();
                //update-end---author:chenrui ---date:20250714  for：[QQYUN-13104]【word模版】设计器中有空格，导出到word空格没了------------
            } else {
                String[] textes = (value + " ").split("\n");
                for (int i = 0; i < textes.length; i++) {
                    if (i == textes.length - 1) {
                        int lastIndex = textes[i].lastIndexOf(" ");
                        if (lastIndex != -1) {
                            textes[i] = textes[i].substring(0, lastIndex);
                        }
                    }
                    run.setText(textes[i]);
                    if (i != textes.length - 1) {
                        run.addBreak();
                    }
                }
            }

        } else {
            run.setText(value);
        }
        switch (type) {
            case "sup":
                run.setSubscript(VerticalAlign.SUPERSCRIPT);
                break;
            case "sub":
                run.setSubscript(VerticalAlign.SUBSCRIPT);
            default:
                break;
        }

        if (oConvertUtils.isNotEmpty(font)) {
            run.setFontFamily(font);
        } else {
            run.setFontFamily("微软雅黑");
        }
        // 字体大小
        if (null != fontSize) {
            // 字体大小转换为twips，1pt = 20 twips, 1px = 1/1.33445 pt
            run.setFontSize(Math.round(fontSize / 1.33445F));
        } else {
            // 默认字体大小
            run.setFontSize(Math.round(16F / 1.33445F));
        }
        if (bold != null) {
            run.setBold(bold);
        }
        if (oConvertUtils.isNotEmpty(color)) {
            if (color.startsWith("#")) {
                run.setColor(color.replaceAll("#", ""));
            } else if (color.startsWith("rgb")) {
                int[] rgbArray = rgbStringToRgb(color);
                color = rgb2Hex(rgbArray[0], rgbArray[1], rgbArray[2]);
                run.setColor(color);
            }
        }
        // 斜体
        if (italic != null) {
            run.setItalic(italic);
        }
        // 高亮
        if (oConvertUtils.isNotEmpty(highlight)) {
            run.setTextHighlightColor(getHighlightName(highlight));
        }
        // 下划线
        if (underline != null && underline) {
            JSONObject textDecoration = content.getJSONObject("textDecoration");
            if (textDecoration == null || textDecoration.isEmpty()) {
                run.setUnderline(UnderlinePatterns.SINGLE);
            } else {
                String style = textDecoration.getString("style");
                switch (style) {
                    case "solid":
                        run.setUnderline(UnderlinePatterns.SINGLE);
                        break;
                    case "double":
                        run.setUnderline(UnderlinePatterns.DOUBLE);
                        break;
                    case "dashed":
                        run.setUnderline(UnderlinePatterns.DASH);
                        break;
                    case "dotted":
                        run.setUnderline(UnderlinePatterns.DOT_DASH);
                        break;
                    case "wavy":
                        run.setUnderline(UnderlinePatterns.WAVE);
                        break;
                    default:
                        run.setUnderline(UnderlinePatterns.SINGLE);
                        break;
                }
            }
        } else {
            run.setUnderline(UnderlinePatterns.NONE);
        }
        if (strikeout != null) {
            run.setStrikeThrough(strikeout);
        }
    }

    /**
     * 添加上下标
     *
     * @param paragraph
     * @param content
     * @param type
     * @author chenrui
     * @date 2025/7/4 14:49
     */
    public static void addSubSupScript(XWPFParagraph paragraph, JSONObject content, String type) {
        XWPFRun run = paragraph.createRun();
        setRunText(run, content, type, false);
    }

    /**
     * 添加超链接
     *
     * @param paragraph
     * @param content
     * @author chenrui
     * @date 2025/7/4 14:50
     */
    public static void addHyperlink(XWPFParagraph paragraph, JSONObject content) {
        JSONArray valueList = content.getJSONArray("valueList");
        String url = content.getString("url");
        if (oConvertUtils.isNotEmpty(valueList)) {
            XWPFRun run = null;
            for (int i = 0; i < valueList.size(); i++) {
                JSONObject valueObj = valueList.getJSONObject(i);
                String value = valueObj.getString("value");
                url = url.replace("\b","");
                run = paragraph.createHyperlinkRun(url);
                valueObj.put("value", value);
                setRunText(run, valueObj, "text", false);
            }
        }
    }

    /**
     * 添加列表
     *
     * @param document
     * @param content
     * @param abstractNumID
     * @return
     * @author chenrui
     * @date 2025/7/4 15:03
     */
    public static int addList(XWPFDocument document, JSONObject content, int abstractNumID) {
        String listStyle = content.getString("listStyle");
        JSONArray valueList = content.getJSONArray("valueList");
        if (oConvertUtils.isNotEmpty(valueList)) {
            BigInteger numID = getNewDecimalNumberingId(document, BigInteger.valueOf(abstractNumID), listStyle);
            abstractNumID = abstractNumID + 1;
            XWPFParagraph paragraph = null;
            XWPFRun run;
            for (int i = 0; i < valueList.size(); i++) {
                JSONObject valueObj = valueList.getJSONObject(i);
                String value = valueObj.getString("value");
                String type = valueObj.getString("type") == null ? "" : valueObj.getString("type");
                if ("superscript".equals(type)) {
                    type = "sup";
                } else if ("subscript".equals(type)) {
                    type = "sub";
                } else {
                    type = "text";
                }
                if ("\n".equals(value)) {
                    paragraph = document.createParagraph();
                    paragraph.setNumID(numID);
                    paragraph.setNumILvl(BigInteger.valueOf(0));
                    run = paragraph.createRun();
                    valueObj.put("value", value);
                    setRunText(run, valueObj, type, false);
                } else {
                    String[] values = value.split("\n");
                    for (int j = 0; j < values.length; j++) {
                        if (oConvertUtils.isNotEmpty(values[j])) {
                            if (paragraph == null) {
                                paragraph = document.createParagraph();
                                paragraph.setNumID(numID);
                                paragraph.setNumILvl(BigInteger.valueOf(0));
                            } else if (j != 0) {
                                paragraph = document.createParagraph();
                                paragraph.setNumID(numID);
                                paragraph.setNumILvl(BigInteger.valueOf(0));
                            }
                            run = paragraph.createRun();
                            valueObj.put("value", values[j]);
                            setRunText(run, valueObj, type, false);
                        }
                    }
                }

            }
        }
        return abstractNumID;
    }

    /**
     * 添加表格列表
     *
     * @param document
     * @param content
     * @param abstractNumID
     * @param cell
     * @return
     * @author chenrui
     * @date 2025/7/4 14:52
     */
    public static int addCellList(XWPFDocument document, JSONObject content, int abstractNumID, XWPFTableCell cell) {
        String listStyle = content.getString("listStyle");
        JSONArray valueList = content.getJSONArray("valueList");
        if (oConvertUtils.isNotEmpty(valueList)) {
            BigInteger numID = getNewDecimalNumberingId(document, BigInteger.valueOf(abstractNumID), listStyle);
            abstractNumID = abstractNumID + 1;
            XWPFParagraph paragraph;
            XWPFRun run = null;
            for (int i = 0; i < valueList.size(); i++) {
                JSONObject valueObj = valueList.getJSONObject(i);
                String value = valueObj.getString("value");
                String type = valueObj.getString("type") == null ? "" : valueObj.getString("type");
                switch (type) {
                    case "superscript":
                        type = "sup";
                        break;
                    case "subscript":
                        type = "sub";
                        break;
                    default:
                        type = "text";
                        break;
                }
                if ("\n".equals(value)) {
                    if (oConvertUtils.isEmpty(cell.getParagraphs().get(0).getRuns())) {
                        paragraph = cell.getParagraphs().get(0);
                    } else {
                        paragraph = cell.addParagraph();
                    }
                    paragraph.setNumID(numID);
                    paragraph.setNumILvl(BigInteger.valueOf(0));
                    run = paragraph.createRun();
                    valueObj.put("value", value);
                    setRunText(run, valueObj, type, false);
                } else {
                    String[] values = value.split("\n");
                    for (int j = 0; j < values.length; j++) {
                        if (oConvertUtils.isNotEmpty(values[j])) {
                            if (oConvertUtils.isEmpty(cell.getParagraphs().get(0).getRuns())) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                            paragraph.setNumID(numID);
                            paragraph.setNumILvl(BigInteger.valueOf(0));
                            run = paragraph.createRun();
                            valueObj.put("value", values[j]);
                            setRunText(run, valueObj, type, false);
                        }
                    }
                }

            }
        }
        return abstractNumID;
    }

    /**
     * 获取新的编号ID
     *
     * @param document
     * @param abstractNumID
     * @param listStyle
     * @return
     * @author chenrui
     * @date 2025/7/4 14:51
     */
    private static BigInteger getNewDecimalNumberingId(XWPFDocument document, BigInteger abstractNumID, String listStyle) {
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
        cTAbstractNum.setAbstractNumId(abstractNumID);

        CTLvl cTLvl = cTAbstractNum.addNewLvl();
        cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
        switch (listStyle) {
            case "decimal":
                cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
                cTLvl.addNewLvlText().setVal("%1.");
                break;
            case "checkbox":
                cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
                cTLvl.addNewLvlText().setVal("■");
                break;
            case "disc":
                cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
                cTLvl.addNewLvlText().setVal("●");
                break;
            case "circle":
                cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
                cTLvl.addNewLvlText().setVal("○");
                break;
            case "square":
                cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
                cTLvl.addNewLvlText().setVal("□");
                break;
            default:
                break;
        }
        cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);

        XWPFNumbering numbering = document.createNumbering();

        abstractNumID = numbering.addAbstractNum(abstractNum);

        return numbering.addNum(abstractNumID);
    }

    /**
     * 添加图片
     *
     * @param paragraph
     * @param content
     * @author chenrui
     * @date 2025/7/4 14:53
     */
    public static void addImage(XWPFParagraph paragraph, JSONObject content) {
        InputStream in = null;
        try {
            String imageUrl = content.getString("value");
            Matcher matcher = WEB_PATTERN.matcher(imageUrl);
            if (matcher.matches()) {
                // 网络资源,先下载到临时目录
                log.info("[批量下载文件]网络资源,下载到临时目录:" + imageUrl);
                try {
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    imageUrl = imageUrl.replace(fileName, URLEncoder.encode(fileName, "UTF-8"));
                    URL url = new URL(imageUrl);
                    URLConnection conn = url.openConnection();
                    // 设置超时间为3秒
                    conn.setConnectTimeout(3 * 1000);
                    // 防止屏蔽程序
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    in = conn.getInputStream();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new JeecgBootException(e);
                }
            } else {
                String uploadPath = SpringContextUtils.getApplicationContext()
                        .getEnvironment()
                        .getProperty("jeecg.path.upload", "");
                // 将本地图片读取到 InputStream
                String filePath = uploadPath + File.separator + imageUrl;
                in = new FileInputStream(filePath);
            }
            XWPFRun run = paragraph.createRun();

            String display = content.getString("imgDisplay");

            if(oConvertUtils.isNotEmpty(display) && display.toLowerCase().startsWith("float-")){
                // 浮动图片
                String behindDoc = "0";
                if(!display.equalsIgnoreCase("float-bottom")){
                    behindDoc = "1";
                }

                int width = content.getIntValue("width");
                int height = content.getIntValue("height");

                int left =0;
                int top = 0;
                if(content.containsKey("imgFloatPosition")){
                    JSONObject imgFloatPosition = content.getJSONObject("imgFloatPosition");
                    if(imgFloatPosition.containsKey("x")){
                        left = imgFloatPosition.getIntValue("x");
                    }
                    if(imgFloatPosition.containsKey("y")){
                        top = imgFloatPosition.getIntValue("y");
                    }
                }
                byte[] imageBytes;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    imageBytes = baos.toByteArray();
                }
                addFloatingPicture(imageBytes, paragraph.getDocument(), run, width, height, left, top, behindDoc);
                return;
            }else{
                // 内嵌图片
                run.addPicture(in, Document.PICTURE_TYPE_PNG, "",
                        Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
            }
            String rowFlex = content.getString("rowFlex");//对齐方式
            if (oConvertUtils.isNotEmpty(rowFlex)) {
                switch (rowFlex) {
                    case "left":
                        paragraph.setAlignment(ParagraphAlignment.LEFT);
                        break;
                    case "center":
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        break;
                    case "right":
                        paragraph.setAlignment(ParagraphAlignment.RIGHT);
                        break;
                    case "alignment":
                        paragraph.setAlignment(ParagraphAlignment.BOTH);
                        break;
                    default:
                        paragraph.setAlignment(ParagraphAlignment.LEFT);
                        break;
                }
            } else {
                paragraph.setAlignment(ParagraphAlignment.LEFT);
            }
        } catch (Exception e) {
            log.error("添加图片异常", e);
            throw new RuntimeException("添加图片异常: " + e.getMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    // ignore
                    log.error("关闭图片流异常", e);
                }
            }
        }
    }

    /**
     * 添加图片
     *
     * @param imageData 图片流
     * @param doc   文档对象
     * @param run   文档运行对象
     * @param width     宽度 px
     * @param height    高度 px
     * @param left      左边距 px
     * @param top       上边距 px
     * @param behindDoc 环绕方式 0 处于文字上方 1 处于文字下方
     * @author chenrui
     * @date 2025-07-14 15:18
     */
    private static void addFloatingPicture(byte[] imageData, XWPFDocument doc, XWPFRun run, int width, int height, int left, int top, String behindDoc) {
        if(null == imageData){
            return;
        }
        // 生成 anchor XML
        int widthNum = Units.pixelToEMU(width);
        int heightNum = Units.pixelToEMU(height);
        int leftNum = Units.pixelToEMU(left);
        int topNum = Units.pixelToEMU(top);
        // 环绕方式 0 处于文字上方 1 处于文字下方
        behindDoc = oConvertUtils.getString(behindDoc, "0");
        try {
            // 添加图片数据
            String blipId = doc.addPictureData(imageData, Document.PICTURE_TYPE_JPEG);
                /*
                behindDoc：图片是否在文字之后。1 表示图片在文字后面，0 表示浮在文字上方。
                locked：是否锁定对象（防止移动/编辑）。0 表示未锁定。
                layoutInCell：如果插入在表格中，是否允许在单元格内部布局。1 表示允许。
                allowOverlap：是否允许重叠。1 表示允许。
                relativeHeight：相对层级高度，影响图像的叠放顺序。越大越靠前。
                 */
            String anchorXml =
                    "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" " +
                            "behindDoc=\"" + behindDoc + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\" relativeHeight=\"0\">" +
                            "<wp:simplePos x=\"0\" y=\"0\"/>" +
                            "<wp:positionH relativeFrom=\"page\">" +
                            "<wp:posOffset>" + leftNum + "</wp:posOffset>" +
                            "</wp:positionH>" +
                            "<wp:positionV relativeFrom=\"page\">" +
                            "<wp:posOffset>" + topNum + "</wp:posOffset>" +
                            "</wp:positionV>" +
                            "<wp:extent cx=\"" + widthNum + "\" cy=\"" + heightNum + "\"/>" +
                            "<wp:effectExtent l=\"0\" t=\"0\" r=\"9525\" b=\"9525\"/>" +
                            "<wp:wrapNone/>" +
                            "<wp:docPr id=\"1\" name=\"FloatingImage\"/>" +
                            "<wp:cNvGraphicFramePr/>" +
                            "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                            "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                            "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                            "<pic:nvPicPr>" +
                            "<pic:cNvPr id=\"0\" name=\"FloatingImage\"/>" +
                            "<pic:cNvPicPr/>" +
                            "</pic:nvPicPr>" +
                            "<pic:blipFill>" +
                            "<a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                            "<a:stretch><a:fillRect/></a:stretch>" +
                            "</pic:blipFill>" +
                            "<pic:spPr>" +
                            "<a:xfrm>" +
                            "<a:off x=\"0\" y=\"0\"/>" +
                            "<a:ext cx=\"" + widthNum + "\" cy=\"" + heightNum + "\"/>" +
                            "</a:xfrm>" +
                            "<a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>" +
                            "</pic:spPr>" +
                            "</pic:pic>" +
                            "</a:graphicData>" +
                            "</a:graphic>" +
                            "</wp:anchor>";
            // 注入 anchor 到 drawing
            XmlToken token = XmlToken.Factory.parse(anchorXml);
            CTDrawing drawing = run.getCTR().addNewDrawing();
            drawing.set(token);
        } catch (InvalidFormatException | XmlException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加表格
     *
     * @param doc
     * @param tableData
     * @param abstractNumID
     * @return
     * @throws Exception
     * @author chenrui
     * @date 2025/7/4 14:49
     */
    public static int addTable(XWPFDocument doc, JSONObject tableData, int abstractNumID) {
        //行数
        int rows = 1;
        //列数
        int cols = 1;
        JSONArray trList = tableData.getJSONArray("trList");
        rows = trList.size();
        JSONArray colgroup = tableData.getJSONArray("colgroup");
        cols = colgroup.size();
        String rowFlex = tableData.getString("rowFlex");
        XWPFTable xwpfTable = doc.createTable(rows, cols);
        String borderType = tableData.getString("borderType");
        if ("empty".equals(borderType)) {
            //无边框设置
            CTTblPr tblPr = xwpfTable.getCTTbl().getTblPr();
            if (tblPr == null) tblPr = xwpfTable.getCTTbl().addNewTblPr();
            CTTblBorders tblBorders = tblPr.addNewTblBorders();
            tblBorders.addNewTop().setVal(STBorder.NONE);
            tblBorders.addNewLeft().setVal(STBorder.NONE);
            tblBorders.addNewBottom().setVal(STBorder.NONE);
            tblBorders.addNewRight().setVal(STBorder.NONE);
            tblBorders.addNewInsideH().setVal(STBorder.NONE);
            tblBorders.addNewInsideV().setVal(STBorder.NONE);
        }
        if (oConvertUtils.isNotEmpty(rowFlex)) {
            switch (rowFlex) {
                case "left":
                    xwpfTable.setTableAlignment(TableRowAlign.LEFT);
                    break;
                case "center":
                    xwpfTable.setTableAlignment(TableRowAlign.CENTER);
                    break;
                case "right":
                    xwpfTable.setTableAlignment(TableRowAlign.RIGHT);
                    break;
                default:
                    xwpfTable.setTableAlignment(TableRowAlign.LEFT);
                    break;
            }
        }
        CTTbl ttbl = xwpfTable.getCTTbl();
        CTTblGrid tblGrid = ttbl.addNewTblGrid();

        setTableWidthFixed(xwpfTable, true);
        JSONObject mergeObj = new JSONObject();
        mergeObj.put("isMerge", true);
        for (int i = 0; i < rows; i++) {
            JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
            for (int j = 0; j < tdList.size(); j++) {
                JSONObject cellInfo = tdList.getJSONObject(j);
                boolean isMerge = cellInfo.getBooleanValue("isMerge");
                if (isMerge) {
                    continue;
                } else {
                    int rowspan = cellInfo.getIntValue("rowspan");
                    int colspan = cellInfo.getIntValue("colspan");
                    if (rowspan > 1 || colspan > 1) {
                        for (int k = 0; k < rowspan; k++) {
                            for (int k2 = 0; k2 < colspan; k2++) {
                                int r = i + k;
                                int c = j + k2;
                                if (r == i) {
                                    if (k2 != 0) {
                                        if (c >= tdList.size()) {
                                            tdList.add(mergeObj);
                                        } else {
                                            tdList.add(c, mergeObj);
                                        }
                                    }
                                } else {
                                    if (c >= trList.getJSONObject(r).getJSONArray("tdList").size()) {
                                        trList.getJSONObject(r).getJSONArray("tdList").add(mergeObj);
                                    } else {
                                        trList.getJSONObject(r).getJSONArray("tdList").add(c, mergeObj);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Map<Integer, Boolean> isDynamicRow = new HashMap<>();
        for (int i = 0; i < trList.size(); i++) {
            JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
            for (int j = 0; j < tdList.size(); j++) {
                JSONObject cellInfo = tdList.getJSONObject(j);
                boolean isMerge = cellInfo.getBooleanValue("isMerge");
                if (isMerge) {
                    continue;
                } else {
                    abstractNumID = setCellValue(xwpfTable.getRow(i).getCell(j), cellInfo, doc, abstractNumID);
                }
                if (!isDynamicRow.containsKey(i)) {
                    String plainText = getCellPlainText(cellInfo);
                    if (oConvertUtils.isNotEmpty(plainText)) {
                        if ((plainText.contains("{{") && plainText.contains("}}")) || (plainText.contains("[") && plainText.contains("]"))) {
                            isDynamicRow.put(i, true);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < cols; i++) {
            CTTblGridCol gridCol = tblGrid.addNewGridCol();
            gridCol.setW(new BigInteger(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue() * 16)));
            for (int j = 0; j < rows; j++) {
                xwpfTable.getRow(j).getCell(i).setWidth(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue() * 16));
            }
        }
        for (int i = 0; i < rows; i++) {
            int height = trList.getJSONObject(i).getIntValue("height");
            if (isDynamicRow.containsKey(i)) {
                xwpfTable.getRow(i).setHeight(42 * 15);
                xwpfTable.getRow(i).setHeight(42 * 15);
            } else {
                xwpfTable.getRow(i).setHeight(height * 15);
            }
        }
        for (int i = 0; i < trList.size(); i++) {
            JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
            for (int j = 0; j < tdList.size(); j++) {
                JSONObject cellInfo = tdList.getJSONObject(j);
                boolean isMerge = cellInfo.getBooleanValue("isMerge");
                if (isMerge) {
                    continue;
                }
                int rowspan = cellInfo.getIntValue("rowspan");
                int colspan = cellInfo.getIntValue("colspan");
                if (rowspan > 1) {
                    for (int k = 0; k < colspan; k++) {
                        TableTools.mergeCellsVertically(xwpfTable, j + k, i, i + rowspan - 1);
                    }
                }

            }
        }
        List<MergeColDTO> mergeCellsHorizonal = new ArrayList<MergeColDTO>();
        for (int i = trList.size() - 1; i >= 0; i--) {
            JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
            for (int j = tdList.size() - 1; j >= 0; j--) {
                JSONObject cellInfo = tdList.getJSONObject(j);
                boolean isMerge = cellInfo.getBooleanValue("isMerge");
                if (isMerge) {
                    continue;
                }
                int rowspan = cellInfo.getIntValue("rowspan");
                int colspan = cellInfo.getIntValue("colspan");
                if (colspan > 1) {
                    for (int k = 0; k < rowspan; k++) {
                        MergeColDTO mergeColDto = new MergeColDTO();
                        mergeColDto.setRow(i + k);
                        mergeColDto.setFrom(j);
                        mergeColDto.setTo(j + colspan - 1);
                        mergeCellsHorizonal.add(mergeColDto);
                    }
                }
            }
        }
        if (oConvertUtils.isNotEmpty(mergeCellsHorizonal)) {
            Map<Integer, List<MergeColDTO>> groupBy = mergeCellsHorizonal.stream().collect(Collectors.groupingBy(MergeColDTO::getRow));
            for (List<MergeColDTO> value : groupBy.values()) {
                value.sort(Comparator.comparing(MergeColDTO::getFrom).reversed());
                for (MergeColDTO mergeColDTO : value) {
                    TableTools.mergeCellsHorizonal(xwpfTable, mergeColDTO.getRow(), mergeColDTO.getFrom(), mergeColDTO.getTo());
                }
            }
        }
        return abstractNumID;
    }

    /**
     * 是否允许表格自动重调单元格宽度 对应【表格属性-表格-选项-自动重调尺寸以适应内容】
     *
     * @param table
     * @param isFixed
     * @author chenrui
     * @date 2025/7/4 14:48
     */
    public static void setTableWidthFixed(XWPFTable table, boolean isFixed) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTTblLayoutType tblLayout = tblPr.isSetTblLayout() ? tblPr.getTblLayout() : tblPr.addNewTblLayout();
        if (isFixed) {
            tblLayout.setType(STTblLayoutType.AUTOFIT);
        }
    }

    /**
     * 设置单元格的值
     *
     * @param cell
     * @param cellInfo
     * @param doc
     * @param abstractNumID
     * @return
     * @author chenrui
     * @date 2025/7/4 14:49
     */
    private static int setCellValue(XWPFTableCell cell, JSONObject cellInfo, XWPFDocument doc, int abstractNumID) {
        if (cell == null) {
            return abstractNumID;
        }
        String backgroundColor = cellInfo.getString("backgroundColor");
        if (oConvertUtils.isNotEmpty(backgroundColor)) {
            cell.setColor(backgroundColor.replaceFirst("#", ""));
        }
        JSONArray values = cellInfo.getJSONArray("value");
        String verticalAlign = cellInfo.getString("verticalAlign");
        if (oConvertUtils.isNotEmpty(values)) {
            XWPFParagraph paragraph = null;
            float pRowMargin = 1f;
            String lastType = "";
            for (int i = 0; i < values.size(); i++) {
                JSONObject content = values.getJSONObject(i);
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
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        if ("separator".equals(lastType)) {
                            String value = content.getString("value");
                            if (oConvertUtils.isNotEmpty(value) && value.startsWith("\n")) {
                                content.put("value", value.replaceFirst("\n", ""));
                            }
                        }
                        if (paragraph == null) {
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        WordUtil.addParagraph(paragraph, content, null, false);
                        break;
                    case "superscript":
                        if (paragraph == null) {
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        WordUtil.addSubSupScript(paragraph, content, "sup");
                        break;
                    case "subscript":
                        if (paragraph == null) {
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        WordUtil.addSubSupScript(paragraph, content, "sub");
                        break;
                    case "hyperlink":
                        if (paragraph == null) {
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        WordUtil.addHyperlink(paragraph, content);
                        break;
                    case "list":
                        abstractNumID = WordUtil.addCellList(doc, content, abstractNumID, cell);
                        break;
                    case "image":
                        if (paragraph == null) {
                            if (i == 0) {
                                paragraph = cell.getParagraphs().get(0);
                            } else {
                                paragraph = cell.addParagraph();
                            }
                        }
                        WordUtil.addImage(paragraph, content);
                        break;
                    default:
                        break;
                }
                lastType = type;
            }
            if (paragraph != null) {
                paragraph.setSpacingBetween(pRowMargin, LineSpacingRule.AUTO);
            }
            if (oConvertUtils.isNotEmpty(verticalAlign)) {
                switch (verticalAlign) {
                    case "top":
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
                        break;
                    case "middle":
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                        break;
                    case "bottom":
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
                        break;
                    default:
                        break;
                }
            }
        }
        return abstractNumID;
    }

    /**
     * 获取单元格的纯文本内容
     *
     * @param cellInfo
     * @return
     * @author chenrui
     * @date 2025/7/4 14:55
     */
    public static String getCellPlainText(JSONObject cellInfo) {
        StringBuilder sb = new StringBuilder();
        JSONArray values = cellInfo.getJSONArray("value");
        if (oConvertUtils.isNotEmpty(values)) {
            for (int i = 0; i < values.size(); i++) {
                JSONObject content = values.getJSONObject(i);
                String value = content.getString("value");//内容
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 将rgb字符串转换成rgb数组
     *
     * @param rgb
     * @return
     * @author chenrui
     * @date 2025/7/4 14:28
     */
    public static int[] rgbStringToRgb(String rgb) {
        int[] color = new int[3];
        rgb = rgb.replace("rgb", "");
        rgb = rgb.replace("(", "").replace(")", "").replaceAll(" ", "");
        String[] colors = rgb.split(",");
        //update-begin---author:chenrui ---date:20250813  for：[QQYUN-13384]word设计器，导出word失败------------
        // 只取前三个分量，忽略 alpha
        for (int i = 0; i < 3 && i < colors.length; i++) {
            color[i] = Integer.parseInt(colors[i].replaceAll("[^0-9]", ""));
        }
        //update-end---author:chenrui ---date:20250813  for：[QQYUN-13384]word设计器，导出word失败------------
        return color;
    }

    /**
     * 将RGB颜色值转换为十六进制字符串
     *
     * @param r
     * @param g
     * @param b
     * @return
     * @author chenrui
     * @date 2025/7/4 14:29
     */
    public static String rgb2Hex(int r, int g, int b) {
        return String.format("%02x%02x%02x", r, g, b);
    }

    /**
     * @param color
     * @return
     * @date 2020年4月7日 下午7:16:39
     */
    private static String getHighlightName(String color) {
        color = color.replaceAll("\\s+", "");
        if ("#FFFF00".equals(color)) {
            return "yellow";//1-黄色
        } else if ("#00FF00".equals(color)) {
            return "green";//2-绿色
        } else if ("#00FFFF".equals(color)) {
            return "cyan";//3-青色
        } else if ("#FF00FF".equals(color)) {
            return "magenta";//4-粉红色
        } else if ("#0000FF".equals(color)) {
            return "blue";//5-蓝色
        } else if ("#FF0000".equals(color)) {
            return "red";//6-红色
        } else if ("#000080".equals(color)) {
            return "darkBlue";//7-深蓝色
        } else if ("#008080".equals(color)) {
            return "darkCyan";//8-深青色
        } else if ("#008000".equals(color)) {
            return "darkGreen";//9-深绿色
        } else if ("#800080".equals(color)) {
            return "darkMagenta";//10-深粉红色，紫色
        } else if ("#800000".equals(color)) {
            return "darkRed";//11-深红色
        } else if ("#808000".equals(color)) {
            return "darkYellow";//12-深黄色
        } else if ("#808080".equals(color)) {
            return "darkGray";//13-深灰色
        } else if ("#C0C0C0".equals(color)) {
            return "lightGray";//14-浅灰色
        } else if ("#000000".equals(color)) {
            return "black";//15-黑色
        } else {
            return "none";//无色
        }
    }

    public static String getHighlightByName(String name) {
        if ("yellow".equals(name)) {
            return "#FFFF00";//1-黄色
        } else if ("green".equals(name)) {
            return "#00FF00";//2-绿色
        } else if ("cyan".equals(name)) {
            return "#00FFFF";//3-青色
        } else if ("magenta".equals(name)) {
            return "#FF00FF";//4-粉红色
        } else if ("blue".equals(name)) {
            return "#0000FF";//5-蓝色
        } else if ("red".equals(name)) {
            return "#FF0000";//6-红色
        } else if ("darkBlue".equals(name)) {
            return "#000080";//7-深蓝色
        } else if ("darkCyan".equals(name)) {
            return "#008080";//8-深青色
        } else if ("darkGreen".equals(name)) {
            return "#008000";//9-深绿色
        } else if ("darkMagenta".equals(name)) {
            return "#800080";//10-深粉红色，紫色
        } else if ("#800000".equals(name)) {
            return "darkRed";//11-深红色
        } else if ("darkYellow".equals(name)) {
            return "#808000";//12-深黄色
        } else if ("darkGray".equals(name)) {
            return "#808080";//13-深灰色
        } else if ("lightGray".equals(name)) {
            return "#C0C0C0";//14-浅灰色
        } else if ("black".equals(name)) {
            return "#000000";//15-黑色
        } else {
            return "";//无色
        }
    }

}
