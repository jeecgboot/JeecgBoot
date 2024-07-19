//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.treesoft.system.web;

import com.alibaba.fastjson.JSONObject;
import com.treesoft.system.entity.IdsDto;
import com.treesoft.system.entity.Person;
import com.treesoft.system.persistence.Page;
import com.treesoft.system.service.ConfigService;
import com.treesoft.system.service.PersonService;
import com.treesoft.system.utils.LogUtil;
import com.treesoft.system.utils.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping({"system/person"})
public class PersonController extends BaseController {

    @Autowired
    private PersonService personService;
    @Autowired
    private ConfigService configService;

    public PersonController() {
    }

    @RequestMapping(
            value = {"i/person"},
            method = {RequestMethod.GET}
    )
    public String person(Model model) {
        return "system/personList";
    }

    @RequestMapping(
            value = {"i/personList"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Map<String, Object> personList(String username, String realname, HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = this.getPage(request);
        Map<String, Object> map2 = new HashMap();
        String mess = "";
        String status = "";
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        } else {
            try {
                page = this.personService.personList(page, username, realname);
                map2.put("rows", page.getResult());
                map2.put("total", page.getTotalCount());
                map2.put("columns", page.getColumns());
                map2.put("primaryKey", page.getPrimaryKey());
            } catch (Exception var11) {
                LogUtil.e("" + var11);
                mess = var11.getMessage();
                status = "fail";
                map2.put("mess", mess);
                map2.put("status", status);
                map2.put("rows", "");
                map2.put("total", "");
            }

            return map2;
        }
    }

    @RequestMapping(
            value = {"i/addPersonForm"},
            method = {RequestMethod.GET}
    )
    public String addPersonForm(Model model) throws Exception {
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/personForm";
    }

    @RequestMapping(
            value = {"i/editPersonForm/{id}"},
            method = {RequestMethod.GET}
    )
    public String editPersonForm(@PathVariable("id") String id, Model model) throws Exception {
        Map<String, Object> map = new HashMap();
        try {
            map = this.personService.getPerson(id);
        } catch (Exception var5) {
        }
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        log.info("id {},map {}", id, map);
        model.addAttribute("configList", configList);
        model.addAttribute("person", map);
        return "system/personForm";
    }

    @RequestMapping(
            value = {"i/personUpdate"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Map<String, Object> personUpdate(@RequestBody Person person, Model model, HttpServletRequest request) {
        String mess = "";
        String status = "";
        Map<String, Object> map = new HashMap();
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map.put("mess", "没有操作权限！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getRole())) {
            map.put("mess", "用户角色 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getDatascope())) {
            map.put("mess", "数据范围  必填！");
            map.put("status", "fail");
            return map;
        } else {
            try {
                boolean isAdd = StringUtils.isEmpty(person.getId());
                boolean bl = this.personService.userNameIsExists(person.getUsername().toLowerCase());
                if (isAdd && bl) {
                    map.put("mess", "该用户名已存在！");
                    map.put("status", "fail");
                    return map;
                }
                this.personService.personUpdate(person);
                mess = "操作成功";
                status = "success";
            } catch (Exception var14) {
                LogUtil.e("新增 修改用户出错，" + var14);
                mess = "新增或修改用户出错";
                status = "fail";
            }
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(
            value = {"i/deletePerson"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Map<String, Object> deletePerson(@RequestBody IdsDto tem, HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        String[] ids = tem.getIds();
        String mess = "";
        String status = "";
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map.put("mess", "没有操作权限！");
            map.put("status", "fail");
            return map;
        } else {
            try {
                if (StringUtils.isEmpty(tem.getIds())) {
                    map.put("mess", "ids 必填！");
                    map.put("status", "fail");
                    return map;
                }

                List<Map<String, Object>> list = this.personService.selectPersonByIds(ids);
                this.personService.deletePerson(ids);
                mess = "删除成功";
                status = "success";
                String username = (String) session.getAttribute("LOGIN_USER_NAME");
                String ip = NetworkUtil.getIpAddress(request);
                String userName = "";

                for (int i = 0; i < list.size(); ++i) {
                    userName = userName + list.get(i).get("username") + ",";
                }
            } catch (Exception var14) {
                LogUtil.e("删除用户出错，" + var14);
                mess = "删除用户出错";
                status = "fail";
            }

            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(
            value = {"configListSelect/{id}"},
            method = {RequestMethod.GET}
    )
    public String configListSelect(@PathVariable("id") String id, Model model) {
        new HashMap();

        try {
            Map<String, Object> map = this.personService.getPerson(id);
            String datascope = (String) map.get("datascope");
            model.addAttribute("personId", id);
            model.addAttribute("datascope", datascope);
            return "system/configListSelect";
        } catch (Exception var5) {
            LogUtil.e("打开 数据库配置页面 出错，" + var5);
            return "";
        }
    }

    @RequestMapping(
            value = {"saveSelectDatascope"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public String saveSelectDatascope(@RequestBody IdsDto tem, Model model) {
        JSONObject json = new JSONObject();

        try {
            Person person = new Person();
            person.setId(tem.getId());
            String datasource = "";
            if (tem.getIds().length > 0) {
                String[] var9;
                int var8 = (var9 = tem.getIds()).length;

                for (int var7 = 0; var7 < var8; ++var7) {
                    String idTemp = var9[var7];
                    datasource = datasource + idTemp + ",";
                }

                datasource = datasource.substring(0, datasource.length() - 1);
            }

            person.setDatascope(datasource);
            this.personService.personUpdateDatascope(person);
            json.put("mess", "操作成功！");
            json.put("status", "success");
        } catch (Exception var10) {
            LogUtil.e("保存用户的数据库配置 出错，" + var10);
            json.put("mess", "操作出错！" + var10.getMessage());
            json.put("status", "fail");
        }

        return json.toString();
    }
}
