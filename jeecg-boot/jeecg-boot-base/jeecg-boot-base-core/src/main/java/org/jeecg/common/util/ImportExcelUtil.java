package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 导出返回信息
 */
@Slf4j
public class ImportExcelUtil {

    public static Result<?> imporReturnRes(int errorLines,int successLines,List<String> errorMessage) throws IOException {
        if (errorLines == 0) {
            return Result.ok("共" + successLines + "行数据全部导入成功！");
        } else {
            JSONObject result = new JSONObject(5);
            int totalCount = successLines + errorLines;
            result.put("totalCount", totalCount);
            result.put("errorCount", errorLines);
            result.put("successCount", successLines);
            result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + successLines + "，错误行数：" + errorLines);
            String fileUrl = PmsUtil.saveErrorTxtByList(errorMessage, "userImportExcelErrorLog");
            int lastIndex = fileUrl.lastIndexOf(File.separator);
            String fileName = fileUrl.substring(lastIndex + 1);
            result.put("fileUrl", "/sys/common/static/" + fileUrl);
            result.put("fileName", fileName);
            Result res = Result.ok(result);
            res.setCode(201);
            res.setMessage("文件导入成功，但有错误。");
            return res;
        }
    }

    public static List<String> importDateSave(List<Object> list, Class serviceClass,List<String> errorMessage,String errorFlag)  {
        IService bean =(IService) SpringContextUtils.getBean(serviceClass);
        for (int i = 0; i < list.size(); i++) {
            try {
                boolean save = bean.save(list.get(i));
                if(!save){
                    throw new Exception(errorFlag);
                }
            } catch (Exception e) {
                String message = e.getMessage().toLowerCase();
                int lineNumber = i + 1;
                // 通过索引名判断出错信息
                if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_ROLE_CODE)) {
                    errorMessage.add("第 " + lineNumber + " 行：角色编码已经存在，忽略导入。");
                } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_JOB_CLASS_NAME)) {
                    errorMessage.add("第 " + lineNumber + " 行：任务类名已经存在，忽略导入。");
                }else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_CODE)) {
                    errorMessage.add("第 " + lineNumber + " 行：职务编码已经存在，忽略导入。");
                }else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE)) {
                    errorMessage.add("第 " + lineNumber + " 行：部门编码已经存在，忽略导入。");
                }else {
                    errorMessage.add("第 " + lineNumber + " 行：未知错误，忽略导入");
                    log.error(e.getMessage(), e);
                }
            }
        }
        return errorMessage;
    }

    public static List<String> importDateSaveOne(Object obj, Class serviceClass,List<String> errorMessage,int i,String errorFlag)  {
        IService bean =(IService) SpringContextUtils.getBean(serviceClass);
        try {
            boolean save = bean.save(obj);
            if(!save){
                throw new Exception(errorFlag);
            }
        } catch (Exception e) {
            String message = e.getMessage().toLowerCase();
            int lineNumber = i + 1;
            // 通过索引名判断出错信息
            if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_ROLE_CODE)) {
                errorMessage.add("第 " + lineNumber + " 行：角色编码已经存在，忽略导入。");
            } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_JOB_CLASS_NAME)) {
                errorMessage.add("第 " + lineNumber + " 行：任务类名已经存在，忽略导入。");
            }else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_CODE)) {
                errorMessage.add("第 " + lineNumber + " 行：职务编码已经存在，忽略导入。");
            }else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE)) {
                errorMessage.add("第 " + lineNumber + " 行：部门编码已经存在，忽略导入。");
            }else {
                errorMessage.add("第 " + lineNumber + " 行：未知错误，忽略导入");
                log.error(e.getMessage(), e);
            }
        }
        return errorMessage;
    }
}
