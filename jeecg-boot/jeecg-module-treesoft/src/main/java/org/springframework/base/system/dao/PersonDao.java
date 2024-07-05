package org.springframework.base.system.dao;

import org.springframework.base.system.entity.Person;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.MD5Utils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedJdbcTemplate;

    public boolean deletePerson(String[] ids) throws Exception {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_users where id in (" + str3 + ")";
        return jdbcTemplate.update(sql)>0;
    }

    public Map<String, Object> getPerson(String id) {
        String sql = " select id, username,  realname ,role, status, note ,expiration , permission,datascope  from  treesoft_users where id='" + id + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String, Object> map = new HashMap();
        if (list.size() > 0) {
            map = (Map)list.get(0);
        }

        return (Map)map;
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql = " select * from  treesoft_users where 1=1 ";
        if (!StringUtils.isEmpty(username)) {
            sql = sql + " and username like '%" + username + "%'";
        }

        if (!StringUtils.isEmpty(realname)) {
            sql = sql + " and realname like '%" + realname + "%'";
        }

        int rowCount = jdbcTemplate.queryForList(sql).size();
        sql = sql + "  limit " + limitFrom + "," + pageSize;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        page.setTotalCount((long)rowCount);
        page.setResult(list);
        return page;
    }

    public boolean personUpdate(Person person) throws Exception {
        String id = person.getId();
        String sql = "";
        String username = person.getUsername();
        String password = person.getPassword();
        boolean existsConfig = false;
        if (!StringUtils.isEmpty(person.getId())) {
            Map<String, Object> map = this.getPerson(person.getId());
            if (map.size() > 1) {
                existsConfig = true;
            }
        }

        if (person.getExpiration() == null) {
            person.setExpiration("");
        }

        if (!StringUtils.isEmpty(password)) {
            password = MD5Utils.MD5Encode(password + "treesoft" + username.toLowerCase());
        }

        if (!StringUtils.isEmpty(id) && existsConfig) {
            sql = " update treesoft_users  set ";
            if (!StringUtils.isEmpty(person.getUsername())) {
                sql = sql + "username='" + person.getUsername() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getRealname())) {
                sql = sql + "realname='" + person.getRealname() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getExpiration())) {
                sql = sql + "expiration='" + person.getExpiration() + "' ,";
            }

            if (person.getPermission() == null) {
                sql = sql + "permission='' ,";
            } else {
                sql = sql + "permission='" + person.getPermission() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getDatascope())) {
                sql = sql + "datascope='" + person.getDatascope() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getStatus())) {
                sql = sql + "status='" + person.getStatus() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getRole())) {
                sql = sql + "role='" + person.getRole() + "' ,";
            }

            if (!StringUtils.isEmpty(person.getNote())) {
                sql = sql + "note='" + person.getNote() + "' ";
            }

            if (StringUtils.isEmpty(person.getNote())) {
                sql = sql + "note='' ";
            }

            sql = sql + "  where id='" + id + "'";
        } else {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_users (id, create_time ,username,password,realname , status,token,role,note,expiration,datascope,permission ) values ( '" + tempId + "','" + DateUtils.getDateTime() + "','" + person.getUsername() + "','" + password + "','" + person.getRealname() + "','" + person.getStatus() + "','" + MD5Utils.MD5Encode(password + "39") + "','" + person.getRole() + "','" + person.getNote() + "','" + person.getExpiration() + "','" + person.getDatascope() + "','" + person.getPermission() + "' ) ";
        }

        return jdbcTemplate.update(sql)>0;
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        String username = "";
        String password = "";
        String token = "";

        for(int i = 0; i < ids.length; ++i) {
            List<Map<String, Object>> list = this.selectUserById(ids[i]);
            username = (String)((Map)list.get(0)).get("username");
            password = MD5Utils.MD5Encode("123321treesoft" + username.toLowerCase());
            token = MD5Utils.MD5Encode(password + "39");
            String sql = "  update  treesoft_users set password='" + password + "' ,token ='" + token + "'  where id in ('" + ids[i] + "')";
            jdbcTemplate.update(sql);
        }
        return true;
    }

    public List<Map<String, Object>> selectUserById(String userId) {
        String sql = " select * from  treesoft_users where id='" + userId + "' ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " select * from  treesoft_users where id in (" + str3 + ") ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public boolean userNameIsExists(String userName) {
        String sql = " select username from  treesoft_users where LOWER(username) in ('" + userName + "') ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list.size() > 0;
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        String sql = "  update  treesoft_users set datascope='" + person.getDatascope() + "'   where id ='" + person.getId() + "'";
        jdbcTemplate.update(sql);
        return true;
    }

}
