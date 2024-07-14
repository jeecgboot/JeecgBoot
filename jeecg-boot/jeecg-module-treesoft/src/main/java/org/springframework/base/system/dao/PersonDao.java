package org.springframework.base.system.dao;

import org.springframework.base.system.entity.Person;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.PasswordUtil;
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

        for (int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_user_role where id in (" + str3 + ")";
        return jdbcTemplate.update(sql) > 0;
    }

    public Map<String, Object> getPerson(String id) {
        String sql = " select u.username,u.realname,u.status,r.* from sys_user u left join treesoft_user_role r on u.id=r.user_id where u.del_flag=0 and r.id=?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
        Map<String, Object> map = new HashMap();
        if (list.size() > 0) {
            map = list.get(0);
        }
        return map;
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql = " select u.username,u.realname,u.status,r.* from sys_user u left join treesoft_user_role r on r.user_id=u.id where 1=1 ";
        if (!StringUtils.isEmpty(username)) {
            sql = sql + " and username like '%" + username + "%'";
        }

        if (!StringUtils.isEmpty(realname)) {
            sql = sql + " and realname like '%" + realname + "%'";
        }

        int rowCount = jdbcTemplate.queryForList(sql).size();
        sql = sql + "  limit " + limitFrom + "," + pageSize;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean personUpdate(Person person) throws Exception {
        String id = person.getId();
        String sql = "";
        boolean existsConfig = false;
        if (!StringUtils.isEmpty(id)) {
            Map<String, Object> map = this.getPerson(person.getId());
            if (map.size() > 1) {
                existsConfig = true;
            }
        }

        if (person.getExpiration() == null) {
            person.setExpiration("");
        }

        if (!StringUtils.isEmpty(id) && existsConfig) {
            sql = " update treesoft_user_role  set ";

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
            String userId = String.valueOf(System.currentTimeMillis());
            sql = " insert into treesoft_user_role (id, user_id,role,note,expiration,datascope,permission ) values ( '" + tempId + "','" + userId + "','" + person.getRole() + "','" + person.getNote() + "','" + person.getExpiration() + "','" + person.getDatascope() + "','" + person.getPermission() + "' ) ";
            String addPersonSql = "insert into sys_user(id, username, realname, password, salt, status, del_flag) values (?,?,?,?,?,?,?)";
            String salt = PasswordUtil.randomGen(8);
            String password = PasswordUtil.encrypt(person.getUsername(),person.getPassword(),salt);
            jdbcTemplate.update(addPersonSql,userId,person.getUsername(),person.getRealname(),password,salt,true,false);
        }
        return jdbcTemplate.update(sql) > 0;
    }

    public List<Map<String, Object>> selectUserById(String userId) {
        String sql = " select * from  treesoft_user_role where id='" + userId + "' ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " select * from  treesoft_user_role where id in (" + str3 + ") ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public boolean userNameIsExists(String userName) {
        String sql = " select username from  sys_user where LOWER(username) in ('" + userName + "') ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list.size() > 0;
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        String sql = "  update  treesoft_user_role set datascope='" + person.getDatascope() + "'   where id ='" + person.getId() + "'";
        jdbcTemplate.update(sql);
        return true;
    }

}
