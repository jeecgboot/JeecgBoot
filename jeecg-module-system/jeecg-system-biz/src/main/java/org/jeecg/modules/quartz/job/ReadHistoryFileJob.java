package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取html文件中的分享链接信息，并保存到数据库中
 */
@Slf4j
@Component
public class ReadHistoryFileJob implements Job {

    private static final String dbKey = "alist";

    private static final boolean reAdd = false;

    private static final String[] nameFilter = new String[]{"(&lt;|&gt);","[,;，；。]\\s*希望大佬能转存一下","(链接|提取码)[:：]"};

    @Setter
    private String parameter;

    private JSONObject paramJson;

    private int totalCount = 0;

    private HttpURLConnection urlCon = null;

    private List<DictModel> resourceTypeList = new ArrayList<>();

    private Map<String,String> linkMap = new HashMap<>();

    @Autowired
    private ISysDictService sysDictService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(" Job Execution key："+jobExecutionContext.getJobDetail().getKey());
        JobUtil.startTime("ReadHistoryFileJob");
        try {
            List<String> result = DynamicDBUtil.findList(dbKey,"select distinct links from x_shares",String.class);
            for (String link : result) {
                linkMap.put(link,"");
            }
            resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
            paramJson = JSONObject.parseObject(parameter);
            String path = paramJson.getString("path");
            if (StringUtils.isNotBlank(path)) {
                readDirForHtml(paramJson.getString("channelType"),new File(path));
            }
            if (urlCon != null) {
                urlCon.disconnect();
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        JobUtil.endTime("ReadHistoryFileJob");
    }

    private void readDirForHtml(String channelType,File file) throws JobExecutionException, SQLException {
        log.info("param: {}; filePath: {}",channelType,file.getPath());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                if (f.isFile() && f.getName().endsWith(".html")) {
                    readFile(channelType,f);
                }
            }
        } else if (file.isFile()) {
            readFile(channelType,file);
        }
    }

    private void readFile(String channelType, File htmlFile) throws JobExecutionException {
        String line = "";
        try {
            List<Object[]> addParamList = new ArrayList<>(200);
            List<Object[]> delParamList = new ArrayList<>(200);
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            while ((line = reader.readLine())!=null){
                String name = "",remark = "",links = "",size = "",resourceType = "",password = "";
                if (channelType.equals("1") && (line.startsWith("名称：") || line.startsWith("Title:"))) { //夸克 115
                    for (String str : line.split("<br>")) {
                        if (StringUtils.isBlank(str)) {
                            continue;
                        }
                        if (StringUtils.isBlank(links)) {// 链接
                            links = JobUtil.getShareLink(str);
                        }
                        str = JobUtil.delHTMLTag(str);
                        if (StringUtils.isBlank(password)) {// 密码
                            password = getPassword(str);
                        }
                        if (str.startsWith("\uD83D\uDCC1 大小：")) {
                            size = str.substring(6);
                            continue;
                        } else if (str.startsWith("Size:")) {
                            size = str.substring(5);
                            continue;
                        }
                        if (str.startsWith("名称：")) {
                            name = str.substring(3);
                        } else if (str.startsWith("Title:")) {
                            name = str.substring(6);
                        } else if (str.startsWith("描述：")) {
                            remark = str.substring(3);
                        }
                    }
                } else if (channelType.equals("2") && line.startsWith("<a href=\"\" onclick=\"return ShowMentionName()\">")) { //谷歌
                    if (line.contains("@gdurl")) {
                        line = line.substring(line.indexOf("<br>")+4,line.indexOf("@gdurl")+10);
                    } else {
                        line = line.substring(line.indexOf("<br>")+4,line.lastIndexOf("</a>")+4);
                    }
                    String defaultName = JobUtil.getbracketContent(line);
                    for (String str : line.split("<br>")) {
                        if (StringUtils.isBlank(str)) {
                            continue;
                        }
                        str += " ";
                        // 链接
                        if (StringUtils.isBlank(links)) {
                            links = JobUtil.getShareLink(str);
                        }
                        str = JobUtil.delHTMLTag(str);
                        if ((str.startsWith("Title:") || str.startsWith("Title：")) && StringUtils.isBlank(name)) {
                            if (!JobUtil.isContains(str,"转存")) {
                                name = str.substring(str.indexOf("Title")+6);
                            }
                        } else if ((str.startsWith("Name:") || str.startsWith("Name：")) && StringUtils.isBlank(name)) {
                            name = str.substring(str.indexOf("Name")+5);
                        } else if ((str.startsWith("描述：") || str.startsWith("描述:")) && StringUtils.isBlank(remark)) {
                            remark = str.substring(str.indexOf("描述")+3);
                        } else if (JobUtil.validResourceName(str)) {
                            if (str.length() < 50) {
                                if (StringUtils.isBlank(name) && !str.contains("#") && !str.contains("https:")) {
                                    name = JobUtil.replaceAll(str,nameFilter);;
                                }
                            } else if (StringUtils.isBlank(remark)) {
                                remark = str;
                            }
                        }
                        str = JobUtil.replaceAll(str,"[()（）]","").trim();
                        // 密码
                        if (StringUtils.isBlank(password)) {
                            password = getPassword(str);
                        }
                        // 大小
                        if (StringUtils.isBlank(size)) {
                            size = getSize(str);
                        }
                    }
                    if (StringUtils.isNotBlank(links)) { //name默认为括号内容、备注、分享码
                        if (StringUtils.isBlank(name)) {
                            name = defaultName;
                        }
                        if (StringUtils.isBlank(name) && StringUtils.isNotBlank(remark)) {
                            name = JobUtil.replaceAll(remark.split(" ")[0],nameFilter);
                        }
                        if (StringUtils.isBlank(name)) {
                            name = links.substring(links.lastIndexOf("/")+1);
                        }
                    }
                } else if (channelType.equals("3") && line.startsWith("新投稿资源分享")) { //阿里云
                    for (String str : line.split("<br>")) {
                        if (StringUtils.isBlank(str)) {
                            continue;
                        }
                        links = JobUtil.getShareLink(str);
                        str = JobUtil.delHTMLTag(str);
                        if (!str.contains("新投稿资源分享")) {
                            name = str;
                        }
                    }
                } else if (channelType.equals("4") && line.contains("https://")) { //自定义 名称+链接+分享码(需要空格隔开)
                    if (!line.contains("链接")) {
                        log.warn(line);
                        continue;
                    }
                    line = line.replace("链接"," 链接").replace("分享码"," 分享码").replace("提取码"," 提取码")+" ";
                    links = JobUtil.getShareLink(line);
                    if (line.contains("链接:") || line.startsWith("链接：")) {
                        name = line.substring(0,line.indexOf("链接"));
                    }
                    password = getPassword(line);
                }
                if (StringUtils.isBlank(name) || StringUtils.isBlank(links)) {
                    continue;
                }
                name = name.trim().replaceAll("/","_");
                size = size.trim();
                password = password.trim();
                remark = remark.trim();
                totalCount++;
                if (linkMap.containsKey(links)) { //删除后重新添加
                    if (!reAdd) {
                        log.info("资源存在");
                        continue;
                    }
                    if (StringUtils.isNotBlank(linkMap.get(links))) {
                        log.info("资源重复");
                        continue;
                    }
                    delParamList.add(new Object[]{links});
                }
                String driverType = JobUtil.getDriverNameByUrl(links);
                if (!validDriveLink(links,driverType)) {
                    log.info("资源失效 {}",links);
                    continue;
                }
                log.info(String.format("%s -- %s -- %s -- %s",name,links,password,size));
                resourceType = JobUtil.getResourceTypeName(resourceTypeList,name);
                String mountPath = "/共享/"+(StringUtils.isBlank(resourceType)?driverType:resourceType)+"/"+name;
                addParamList.add(new Object[]{name,links,password,size,resourceType,driverType,mountPath,remark});
                linkMap.put(links,name);
            }
            DynamicDBUtil.batchUpdate(dbKey,"delete from x_shares where links=?",delParamList);
            DynamicDBUtil.batchUpdate(dbKey,"INSERT INTO x_shares (name, links, password, size, resource_type, driver,mount_path, remark) VALUES (?,?,?,?,?,?,?,?)",addParamList);
            log.info(String.format("文件名：%s\t成功%d；总数%d",htmlFile.getName(),addParamList.size(),totalCount));
        } catch (Exception e) {
            log.error(line);
            throw new JobExecutionException(e);
        }
    }

    private String getSize(String str) {
        str += " ";
        String size = "";
        if ((str.contains("Size:") || str.startsWith("Size：") || str.startsWith("Size ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("Size")+6)>0) {
                size = str.substring(str.indexOf("Size")+5,str.indexOf(" ",str.indexOf("Size")+6)).trim();
            }
        } else if ((str.contains("容量:") || str.startsWith("容量：") || str.startsWith("容量 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("容量")+4)>0) {
                size = str.substring(str.indexOf("容量")+3,str.indexOf(" ",str.indexOf("容量")+4)).trim();
            }
        } else if ((str.contains("大小:") || str.startsWith("大小：") || str.startsWith("大小 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("大小")+4)>0) {
                size = str.substring(str.indexOf("大小")+3,str.indexOf(" ",str.indexOf("大小")+4)).trim();
            }
        }
        if (!size.matches("^\\d+.*")) {
            size = "";
        }
        return size;
    }

    private String getPassword(String str) {
        str += " ";
        String password = "";
        if ((str.contains("分享码:") || str.contains("分享码：") || str.contains("分享码 ")) && StringUtils.isBlank(password)) {
            if (str.indexOf(" ",str.indexOf("分享码")+5)>0) {
                password = str.substring(str.indexOf("分享码")+4,str.indexOf(" ",str.indexOf("分享码")+5)).trim();
            }
        } else if ((str.contains("访问码:") || str.contains("访问码：") || str.contains("访问码 ")) && StringUtils.isBlank(password)) {
            if (str.indexOf(" ",str.indexOf("访问码")+5)>0) {
                password = str.substring(str.indexOf("访问码")+4,str.indexOf(" ",str.indexOf("访问码")+5)).trim();
            }
        } else if ((str.contains("提取码:") || str.contains("提取码：") || str.contains("提取码 ")) && StringUtils.isBlank(password)) {
            if (str.indexOf(" ",str.indexOf("提取码")+5)>0) {
                password = str.substring(str.indexOf("提取码")+4,str.indexOf(" ",str.indexOf("提取码")+5)).trim();
            }
        } else if ((str.contains("密码:") || str.contains("密码：") || str.contains("密码 ")) && StringUtils.isBlank(password)) {
            if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                password = str.substring(str.indexOf("密码")+3,str.indexOf(" ",str.indexOf("密码")+4)).trim();
            }
        } else if (str.contains("密码") && StringUtils.isBlank(password)) {
            if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                password = str.substring(str.indexOf("密码")+2,str.indexOf(" ",str.indexOf("密码")+3)).trim();
            }
        }
        if (!password.matches("^[a-zA-Z0-9].*")) {
            password = "";
        }
        return password;
    }

    private boolean validDriveLink(String link,String type) {
        if (type.equals("123云盘")) {
            return true;
        } else if (type.equals("谷歌硬盘")) {
            return false;
        }
        try {
            URL url = new URL(link);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("HEAD");
            urlCon.setConnectTimeout(3000);
            urlCon.setReadTimeout(3000);
            int responseCode = urlCon.getResponseCode();
            if (responseCode<200 || responseCode>399) {
                return false;
            }
            //链接中的内容，读到程序中
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String data = br.readLine();
            while (data != null) {
                data = br.readLine();
                sb.append(data);
            }
            br.close();
            isr.close();
            is.close();
            String finaldata = sb.toString();
            //看连接中是否包含失效文本
            if (finaldata == null || finaldata.contains("文件已经被取消") || finaldata.contains("此链接分享内容可能因为涉及侵权") || finaldata.contains("页面不存在") || finaldata.contains("没有分享的文件") || finaldata.contains("链接不存在")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String args[]) {
        ReadHistoryFileJob job = new ReadHistoryFileJob();
        System.out.println(job.getPassword(" 提取码: 5761 "));
    }
}