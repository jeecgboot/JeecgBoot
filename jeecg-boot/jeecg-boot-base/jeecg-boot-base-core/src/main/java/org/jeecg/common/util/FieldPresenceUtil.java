package org.jeecg.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.dict.service.AutoPoiDictServiceI;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.jeecgframework.poi.excel.annotation.ExcelVerify;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelCollectionParams;
import org.jeecgframework.poi.excel.entity.params.ExcelImportEntity;
import org.jeecgframework.poi.excel.entity.params.ExcelVerifyEntity;
import org.jeecgframework.poi.exception.excel.ExcelImportException;
import org.jeecgframework.poi.exception.excel.enums.ExcelImportEnum;
import org.jeecgframework.poi.util.ExcelUtil;
import org.jeecgframework.poi.util.PoiPublicUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.*;

/**
 * 验证excel标题是否存在，当前默认有0.8（80%）即可通过验证
 */
public class FieldPresenceUtil {
    /**当有标题到达多少可以通过验证*/
    public static final Double NUM = 0.8;

    public static Boolean  fieldPresence(InputStream inputstream, Class<?> pojoClass, ImportParams params) {
        Workbook book = null;
        int errorNum = 0;
        int successNum = 0;
        if (!(inputstream.markSupported())) {
            inputstream = new PushbackInputStream(inputstream, 8);
        }
        try {
            if (POIFSFileSystem.hasPOIFSHeader(inputstream)) {
                book = new HSSFWorkbook(inputstream);
            } else if (POIXMLDocument.hasOOXMLHeader(inputstream)) {
                book = new XSSFWorkbook(OPCPackage.open(inputstream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < params.getSheetNum(); i++) {
            Row row = null;
            //跳过表头和标题行
            Iterator<Row> rows;
            try{
                rows=  book.getSheetAt(i).rowIterator();
            }catch (Exception e){
                //为空说明读取不到，故不是excel
                  throw new RuntimeException("请导入正确格式的excel文件！");
            }


            for (int j = 0; j < params.getTitleRows() + params.getHeadRows(); j++) {
                try{
                    row = rows.next();
                }catch (NoSuchElementException e){
                    //为空说明标题不出在，excel格式错误
                    throw new RuntimeException("请填写内容标题！");
                }
            }
            Sheet sheet = book.getSheetAt(i);
            Map<Integer, String> titlemap = null;
            try {
                titlemap = getTitleMap(sheet, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set<Integer> columnIndexSet = titlemap.keySet();
            Integer maxColumnIndex = Collections.max(columnIndexSet);
            Integer minColumnIndex = Collections.min(columnIndexSet);
            while (rows.hasNext() && (row == null || sheet.getLastRowNum() - row.getRowNum() > params.getLastOfInvalidRow())) {
                row = rows.next();
                Map<String, ExcelImportEntity> excelParams = new HashMap<String, ExcelImportEntity>();
                List<ExcelCollectionParams> excelCollection = new ArrayList<ExcelCollectionParams>();
                String targetId = null;
                if (!Map.class.equals(pojoClass)) {
                    Field fileds[] = PoiPublicUtil.getClassFields(pojoClass);
                    ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
                    if (etarget != null) {
                        targetId = etarget.value();
                    }
                    try {
                        getAllExcelField(targetId, fileds, excelParams, excelCollection, pojoClass, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    int firstCellNum = row.getFirstCellNum();
                    if (firstCellNum > minColumnIndex) {
                        firstCellNum = minColumnIndex;
                    }
                    int lastCellNum = row.getLastCellNum();
                    if (lastCellNum < maxColumnIndex + 1) {
                        lastCellNum = maxColumnIndex + 1;
                    }
                    for (int j = firstCellNum, le = lastCellNum; j < le; j++) {
                        String titleString = (String) titlemap.get(j);
                        if (excelParams.containsKey(titleString) || Map.class.equals(pojoClass)) {
                            successNum+=1;
                        }else{
                            if(excelCollection.size()>0){
                                Iterator var33 = excelCollection.iterator();
                                ExcelCollectionParams param = (ExcelCollectionParams)var33.next();
                                if (param.getExcelParams().containsKey(titleString)) {
                                    successNum+=1;
                                }else{
                                    errorNum+=1;
                                }
                            }else{
                                errorNum+=1;
                            }
                        }
                    }
                    if(successNum<errorNum){
                        return false;
                    }else if(successNum>errorNum){
                        if(errorNum>0){
                            double newNumber = (double) successNum / (successNum + errorNum);
                            BigDecimal bg = new BigDecimal(newNumber);
                            double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                            if(f1<NUM){
                                return false;
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    }else if(successNum==errorNum){
                        return false;
                    }else{
                        return false;
                    }
                } catch (ExcelImportException e) {
                    if (!e.getType().equals(ExcelImportEnum.VERIFY_ERROR)) {
                        throw new ExcelImportException(e.getType(), e);
                    }
                }

            }
        }
     return null;
    }

    /**
     * 获取文件名称标题
     * @Author JEECG
     * @date 20201023
     * @throws Exception
     */
    private static Map<Integer, String> getTitleMap(Sheet sheet, ImportParams params) throws Exception {
        Map<Integer, String> titlemap = new HashMap<Integer, String>();
        Iterator<Cell> cellTitle = null;
        String collectionName = null;
        Row headRow = null;
        int headBegin = params.getTitleRows();
        int allRowNum = sheet.getPhysicalNumberOfRows();
        while(headRow == null && headBegin < allRowNum){
            headRow = sheet.getRow(headBegin++);
        }
        if(headRow==null){
            throw new Exception("不识别该文件");
        }
        if (ExcelUtil.isMergedRegion(sheet, headRow.getRowNum(), 0)) {
            params.setHeadRows(2);
        }else{
            params.setHeadRows(1);
        }
        cellTitle = headRow.cellIterator();
        while (cellTitle.hasNext()) {
            Cell cell = cellTitle.next();
            String value = getKeyValue(cell);
            if (StringUtils.isNotEmpty(value)) {
                titlemap.put(cell.getColumnIndex(), value);//加入表头列表
            }
        }

        //多行表头
        for (int j = headBegin; j < headBegin + params.getHeadRows()-1; j++) {
            headRow = sheet.getRow(j);
            cellTitle = headRow.cellIterator();
            while (cellTitle.hasNext()) {
                Cell cell = cellTitle.next();
                String value = getKeyValue(cell);
                if (StringUtils.isNotEmpty(value)) {
                    int columnIndex = cell.getColumnIndex();
                    //当前cell的上一行是否为合并单元格
                    if(ExcelUtil.isMergedRegion(sheet, cell.getRowIndex()-1, columnIndex)){
                        collectionName = ExcelUtil.getMergedRegionValue(sheet, cell.getRowIndex()-1, columnIndex);
                        if(params.isIgnoreHeader(collectionName)){
                            titlemap.put(cell.getColumnIndex(), value);
                        }else{
                            titlemap.put(cell.getColumnIndex(), collectionName + "_" + value);
                        }
                    }else{
                        titlemap.put(cell.getColumnIndex(), value);
                    }
                }
            }
        }
        return titlemap;
    }
    /**
     * 获取key的值,针对不同类型获取不同的值
     *
     * @Author JEECG
     * @date 20201023
     * @param cell
     * @return
     */
    private static String getKeyValue(Cell cell) {
        if(cell==null){
            return null;
        }
        Object obj = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                obj = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                obj = cell.getCellFormula();
                break;
        }
        return obj == null ? null : obj.toString().trim();
    }

    /**
     * 获取需要导出的全部字段
     *
     *
     *
     * @param targetId
     *            目标ID
     * @param fields
     * @param excelCollection
     * @throws Exception
     */
    public static void getAllExcelField(String targetId, Field[] fields, Map<String, ExcelImportEntity> excelParams, List<ExcelCollectionParams> excelCollection, Class<?> pojoClass, List<Method> getMethods) throws Exception {
        ExcelImportEntity excelEntity = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (PoiPublicUtil.isNotUserExcelUserThis(null, field, targetId)) {
                continue;
            }
            if (PoiPublicUtil.isCollection(field.getType())) {
                // 集合对象设置属性
                ExcelCollectionParams collection = new ExcelCollectionParams();
                collection.setName(field.getName());
                Map<String, ExcelImportEntity> temp = new HashMap();
                ParameterizedType pt = (ParameterizedType)field.getGenericType();
                Class<?> clz = (Class)pt.getActualTypeArguments()[0];
                collection.setType(clz);
                getExcelFieldList(targetId, PoiPublicUtil.getClassFields(clz), clz, temp, (List)null);
                collection.setExcelParams(temp);
                collection.setExcelName(((ExcelCollection)field.getAnnotation(ExcelCollection.class)).name());
                additionalCollectionName(collection);
                excelCollection.add(collection);
            } else if (PoiPublicUtil.isJavaClass(field)) {
                addEntityToMap(targetId, field, (ExcelImportEntity)excelEntity, pojoClass, getMethods, excelParams);
            } else {
                List<Method> newMethods = new ArrayList<Method>();
                if (getMethods != null) {
                    newMethods.addAll(getMethods);
                }
                newMethods.add(PoiPublicUtil.getMethod(field.getName(), pojoClass));
                getAllExcelField(targetId, PoiPublicUtil.getClassFields(field.getType()), excelParams, excelCollection, field.getType(), newMethods);
            }
        }
    }
    public static void getExcelFieldList(String targetId, Field[] fields, Class<?> pojoClass, Map<String, ExcelImportEntity> temp, List<Method> getMethods) throws Exception {
        ExcelImportEntity excelEntity = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis((List)null, field, targetId)) {
                if (PoiPublicUtil.isJavaClass(field)) {
                    addEntityToMap(targetId, field, (ExcelImportEntity)excelEntity, pojoClass, getMethods, temp);
                } else {
                    List<Method> newMethods = new ArrayList();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }

                    newMethods.add(PoiPublicUtil.getMethod(field.getName(), pojoClass, field.getType()));
                    getExcelFieldList(targetId, PoiPublicUtil.getClassFields(field.getType()), field.getType(), temp, newMethods);
                }
            }
        }
    }
    /**
     * 追加集合名称到前面
     *
     * @param collection
     */
    private static void additionalCollectionName(ExcelCollectionParams collection) {
        Set<String> keys = new HashSet();
        keys.addAll(collection.getExcelParams().keySet());
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            collection.getExcelParams().put(collection.getExcelName() + "_" + key, collection.getExcelParams().get(key));
            collection.getExcelParams().remove(key);
        }
    }
    /**
     * 把这个注解解析放到类型对象中
     *
     * @param targetId
     * @param field
     * @param excelEntity
     * @param pojoClass
     * @param getMethods
     * @param temp
     * @throws Exception
     */
    public static void addEntityToMap(String targetId, Field field, ExcelImportEntity excelEntity, Class<?> pojoClass, List<Method> getMethods, Map<String, ExcelImportEntity> temp) throws Exception {
        Excel excel = field.getAnnotation(Excel.class);
        excelEntity = new ExcelImportEntity();
        excelEntity.setType(excel.type());
        excelEntity.setSaveUrl(excel.savePath());
        excelEntity.setSaveType(excel.imageType());
        excelEntity.setReplace(excel.replace());
        excelEntity.setDatabaseFormat(excel.databaseFormat());
        excelEntity.setVerify(getImportVerify(field));
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setNumFormat(excel.numFormat());
        excelEntity.setGroupName(excel.groupName());
        //update-begin-author:taoYan date:20180202 for:TASK #2067 【bug excel 问题】excel导入字典文本翻译问题
        excelEntity.setMultiReplace(excel.multiReplace());
        if(StringUtils.isNotEmpty(excel.dicCode())){
            AutoPoiDictServiceI jeecgDictService = null;
            try {
                jeecgDictService = ApplicationContextUtil.getContext().getBean(AutoPoiDictServiceI.class);
            } catch (Exception e) {
            }
            if(jeecgDictService!=null){
                String[] dictReplace = jeecgDictService.queryDict(excel.dictTable(), excel.dicCode(), excel.dicText());
                if(excelEntity.getReplace()!=null && dictReplace!=null && dictReplace.length!=0){
                    excelEntity.setReplace(dictReplace);
                }
            }
        }
        //update-end-author:taoYan date:20180202 for:TASK #2067 【bug excel 问题】excel导入字典文本翻译问题
        getExcelField(targetId, field, excelEntity, excel, pojoClass);
        if (getMethods != null) {
            List<Method> newMethods = new ArrayList<Method>();
            newMethods.addAll(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }
        temp.put(excelEntity.getName(), excelEntity);

    }
    public static void getExcelField(String targetId, Field field, ExcelImportEntity excelEntity, Excel excel, Class<?> pojoClass) throws Exception {
        excelEntity.setName(getExcelName(excel.name(), targetId));
        String fieldname = field.getName();
        //update-begin-author:taoyan for:TASK #2798 【例子】导入扩展方法，支持自定义导入字段转换规则
        excelEntity.setMethod(PoiPublicUtil.getMethod(fieldname, pojoClass, field.getType(),excel.importConvert()));
        //update-end-author:taoyan for:TASK #2798 【例子】导入扩展方法，支持自定义导入字段转换规则
        if (StringUtils.isNotEmpty(excel.importFormat())) {
            excelEntity.setFormat(excel.importFormat());
        } else {
            excelEntity.setFormat(excel.format());
        }
    }
    /**
     * 判断在这个单元格显示的名称
     *
     * @param exportName
     * @param targetId
     * @return
     */
    public static String getExcelName(String exportName, String targetId) {
        if (exportName.indexOf("_") < 0) {
            return exportName;
        }
        String[] arr = exportName.split(",");
        for (String str : arr) {
            if (str.indexOf(targetId) != -1) {
                return str.split("_")[0];
            }
        }
        return null;
    }
    /**
     * 获取导入校验参数
     *
     * @param field
     * @return
     */
    public static ExcelVerifyEntity getImportVerify(Field field) {
        ExcelVerify verify = field.getAnnotation(ExcelVerify.class);
        if (verify != null) {
            ExcelVerifyEntity entity = new ExcelVerifyEntity();
            entity.setEmail(verify.isEmail());
            entity.setInterHandler(verify.interHandler());
            entity.setMaxLength(verify.maxLength());
            entity.setMinLength(verify.minLength());
            entity.setMobile(verify.isMobile());
            entity.setNotNull(verify.notNull());
            entity.setRegex(verify.regex());
            entity.setRegexTip(verify.regexTip());
            entity.setTel(verify.isTel());
            return entity;
        }
        return null;
    }
}
