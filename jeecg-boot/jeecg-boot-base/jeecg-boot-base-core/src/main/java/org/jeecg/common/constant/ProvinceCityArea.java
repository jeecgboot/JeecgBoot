package org.jeecg.common.constant;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.List;

/**
 * @Description: 省市区
 * @author: jeecg-boot
 */
@Component("pca")
public class ProvinceCityArea {
    List<Area> areaList;

    public String getText(String code){
        this.initAreaList();
        if(this.areaList!=null || this.areaList.size()>0){
            List<String> ls = new ArrayList<String>();
            getAreaByCode(code,ls);
            return String.join("/",ls);
        }
        return "";
    }

    public String getCode(String text){
        this.initAreaList();
        if(areaList!=null && areaList.size()>0){
            for(int i=areaList.size()-1;i>=0;i--){
                if(text.indexOf(areaList.get(i).getText())>=0){
                    return areaList.get(i).getId();
                }
            }
        }
        return null;
    }

    // update-begin-author:sunjianlei date:20220121 for:【JTC-704】数据导入错误 省市区组件，文件中为北京市，导入后，导为了山西省
    /**
     * 获取省市区code，精准匹配
     * @param texts 文本数组，省，市，区
     * @return 返回 省市区的code
     */
    public String[] getCode(String[] texts) {
        if (texts == null || texts.length == 0) {
            return null;
        }
        this.initAreaList();
        if (areaList == null || areaList.size() == 0) {
            return null;
        }
        String[] codes = new String[texts.length];
        String code = null;
        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            Area area;
            if (code == null) {
                area = getAreaByText(text);
            } else {
                area = getAreaByPidAndText(code, text);
            }
            if (area != null) {
                code = area.id;
                codes[i] = code;
            } else {
                return null;
            }
        }
        return codes;
    }

    /**
     * 根据text获取area
     * @param text
     * @return
     */
    public Area getAreaByText(String text) {
        for (Area area : areaList) {
            if (text.equals(area.getText())) {
                return area;
            }
        }
        return null;
    }

    /**
     * 通过pid获取 area 对象
     * @param pCode 父级编码
     * @param text
     * @return
     */
    public Area getAreaByPidAndText(String pCode, String text) {
        this.initAreaList();
        if (this.areaList != null && this.areaList.size() > 0) {
            for (Area area : this.areaList) {
                if (area.getPid().equals(pCode) && area.getText().equals(text)) {
                    return area;
                }
            }
        }
        return null;
    }
    // update-end-author:sunjianlei date:20220121 for:【JTC-704】数据导入错误 省市区组件，文件中为北京市，导入后，导为了山西省

    public void getAreaByCode(String code,List<String> ls){
        for(Area area: areaList){
            if(area.getId().equals(code)){
                String pid = area.getPid();
                ls.add(0,area.getText());
                getAreaByCode(pid,ls);
            }
        }
    }

    private void initAreaList(){
        //System.out.println("=====================");
        if(this.areaList==null || this.areaList.size()==0){
            this.areaList = new ArrayList<Area>();
            try {
                String jsonData = oConvertUtils.readStatic("classpath:static/pca.json");
                JSONObject baseJson = JSONObject.parseObject(jsonData);
                //第一层 省
                JSONObject provinceJson = baseJson.getJSONObject("86");
                for(String provinceKey: provinceJson.keySet()){
                    //System.out.println("===="+provinceKey);
                    Area province = new Area(provinceKey,provinceJson.getString(provinceKey),"86");
                    this.areaList.add(province);
                    //第二层 市
                    JSONObject cityJson = baseJson.getJSONObject(provinceKey);
                    for(String cityKey:cityJson.keySet()){
                        //System.out.println("-----"+cityKey);
                        Area city = new Area(cityKey,cityJson.getString(cityKey),provinceKey);
                        this.areaList.add(city);
                        //第三层 区
                        JSONObject areaJson =  baseJson.getJSONObject(cityKey);
                        if(areaJson!=null){
                            for(String areaKey:areaJson.keySet()){
                                //System.out.println("········"+areaKey);
                                Area area = new Area(areaKey,areaJson.getString(areaKey),cityKey);
                                this.areaList.add(area);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

    class Area{
        String id;
        String text;
        String pid;

        public Area(String id,String text,String pid){
            this.id = id;
            this.text = text;
            this.pid = pid;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getPid() {
            return pid;
        }
    }
}
