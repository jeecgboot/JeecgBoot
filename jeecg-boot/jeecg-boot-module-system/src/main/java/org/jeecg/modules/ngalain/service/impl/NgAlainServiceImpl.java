package org.jeecg.modules.ngalain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ngalain.mapper.NgAlainMapper;
import org.jeecg.modules.ngalain.service.NgAlainService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service("ngAlainService")
@Transactional
public class NgAlainServiceImpl implements NgAlainService {
    @Autowired
    private ISysPermissionService sysPermissionService;
    @Autowired
    private NgAlainMapper mapper;
    @Override
    public JSONArray getMenu(String id) throws Exception {
        return getJeecgMenu(id);
    }
    @Override
    public JSONArray getJeecgMenu(String id) throws Exception {
        List<SysPermission> metaList = sysPermissionService.queryByUser(id);
        JSONArray jsonArray = new JSONArray();
        getPermissionJsonArray(jsonArray, metaList, null);
        JSONArray menulist= parseNgAlain(jsonArray);
        JSONObject jeecgMenu = new JSONObject();
        jeecgMenu.put("text", "jeecg菜单");
        jeecgMenu.put("group",true);
        jeecgMenu.put("children", menulist);
        JSONArray jeecgMenuList=new JSONArray();
        jeecgMenuList.add(jeecgMenu);
        return jeecgMenuList;
    }

    @Override
    public List<Map<String, String>> getDictByTable(String table, String key, String value) {
        return this.mapper.getDictByTable(table,key,value);
    }

    private JSONArray parseNgAlain(JSONArray jsonArray) {
        JSONArray menulist=new JSONArray();
        for (Object object : jsonArray) {
            JSONObject jsonObject= (JSONObject) object;
            String path= (String) jsonObject.get("path");
            JSONObject meta= (JSONObject) jsonObject.get("meta");
            JSONObject menu=new JSONObject();
            menu.put("text",meta.get("title"));
            menu.put("reuse",true);
            if (jsonObject.get("children")!=null){
                JSONArray child=  parseNgAlain((JSONArray) jsonObject.get("children"));
                menu.put("children",child);
                JSONObject icon=new JSONObject();
                icon.put("type", "icon");
                icon.put("value", meta.get("icon"));
                menu.put("icon",icon);
            }else {
                menu.put("link",path);
            }
            menulist.add(menu);
        }
        return menulist;
    }

    /**
     *  获取菜单JSON数组
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getPermissionJsonArray(JSONArray jsonArray,List<SysPermission> metaList,JSONObject parentJson) {
        for (SysPermission permission : metaList) {
            if(permission.getMenuType()==null) {
                continue;
            }
            String tempPid = permission.getParentId();
            JSONObject json = getPermissionJsonObject(permission);
            if(parentJson==null && oConvertUtils.isEmpty(tempPid)) {
                jsonArray.add(json);
                if(!permission.isLeaf()) {
                    getPermissionJsonArray(jsonArray, metaList, json);
                }
            }else if(parentJson!=null && oConvertUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))){
                if(permission.getMenuType()==0) {
                    JSONObject metaJson = parentJson.getJSONObject("meta");
                    if(metaJson.containsKey("permissionList")) {
                        metaJson.getJSONArray("permissionList").add(json);
                    }else {
                        JSONArray permissionList = new JSONArray();
                        permissionList.add(json);
                        metaJson.put("permissionList", permissionList);
                    }

                }else if(permission.getMenuType()==1) {
                    if(parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(json);
                    }else {
                        JSONArray children = new JSONArray();
                        children.add(json);
                        parentJson.put("children", children);
                    }

                    if(!permission.isLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, json);
                    }
                }
            }


        }
    }
    private JSONObject getPermissionJsonObject(SysPermission permission) {
        JSONObject json = new JSONObject();
        //类型(0：一级菜单 1：子菜单  2：按钮)
        if(permission.getMenuType()==2) {
            json.put("action", permission.getPerms());
            json.put("describe", permission.getName());
        }else if(permission.getMenuType()==0||permission.getMenuType()==1) {
            json.put("id", permission.getId());
            if(permission.getUrl()!=null&&(permission.getUrl().startsWith("http://")||permission.getUrl().startsWith("https://"))) {
                String url= new String(Base64.getUrlEncoder().encode(permission.getUrl().getBytes()));
                json.put("path", "/sys/link/" +url.replaceAll("=",""));
            }else {
                json.put("path", permission.getUrl());
            }

            //重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
            json.put("name", urlToRouteName(permission.getUrl()));

            //是否隐藏路由，默认都是显示的
            if(permission.isHidden()) {
                json.put("hidden",true);
            }
            //聚合路由
            if(permission.isAlwaysShow()) {
                json.put("alwaysShow",true);
            }
            json.put("component", permission.getComponent());
            JSONObject meta = new JSONObject();
            meta.put("title", permission.getName());
            if(oConvertUtils.isEmpty(permission.getParentId())) {
                //一级菜单跳转地址
                json.put("redirect",permission.getRedirect());
                meta.put("icon", oConvertUtils.getString(permission.getIcon(), ""));
            }else {
                meta.put("icon", oConvertUtils.getString(permission.getIcon(), ""));
            }
            if(permission.getUrl()!=null&&(permission.getUrl().startsWith("http://")||permission.getUrl().startsWith("https://"))) {
                meta.put("url", permission.getUrl());
            }
            json.put("meta", meta);
        }

        return json;
    }
    /**
     * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-）
     * 举例： URL = /isystem/role
     *     RouteName = isystem-role
     * @return
     */
    private String urlToRouteName(String url) {
        if(oConvertUtils.isNotEmpty(url)) {
            if(url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");
            return url;
        }else {
            return null;
        }
    }
}
