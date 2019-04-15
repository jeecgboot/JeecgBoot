package org.jeecg.modules.online.cgreport.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.util.oConvertUtils;

/**
 * @Description: 根据不同的数据库，动态生成分页SQL
 * @author scott
 * @date 2019-03-20
 */
public class SqlUtil {

    public static final String DATABSE_TYPE_MYSQL ="mysql";
    public static final String DATABSE_TYPE_POSTGRE ="postgresql";
    public static final String DATABSE_TYPE_ORACLE ="oracle";
    public static final String DATABSE_TYPE_SQLSERVER ="sqlserver";

    /**
     * 分页SQL
     */
    public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
    public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";//postgresql
    public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; //oracle
    public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver

    /**
     * 获取所有表的SQL
     */
    public static final String MYSQL_ALLTABLES_SQL = "select distinct table_name from information_schema.columns where table_schema = {0}";         
    public static final String POSTGRE__ALLTABLES_SQL = "SELECT distinct c.relname AS  table_name FROM pg_class c";
    public static final String ORACLE__ALLTABLES_SQL = "select distinct colstable.table_name as  table_name from user_tab_cols colstable"; 
    public static final String SQLSERVER__ALLTABLES_SQL= "select distinct c.name as  table_name from sys.objects c";
    
    /**
     * 获取指定表的所有列名
     */
    public static final String MYSQL_ALLCOLUMNS_SQL = "select column_name from information_schema.columns where table_name = {0} and table_schema = {1}";
    public static final String POSTGRE_ALLCOLUMNS_SQL = "select table_name from information_schema.columns where table_name = {0}";
    public static final String ORACLE_ALLCOLUMNS_SQL = "select column_name from all_tab_columns where table_name ={0}";
    public static final String SQLSERVER_ALLCOLUMNS_SQL = "select name from syscolumns where id={0}";
    /**
     * 获取全sql
     * @param sql
     * @param params
     * @return
     */
    //TODO 规则要改
    public static String getFullSql(String sql,Map params){
        StringBuilder sqlB =  new StringBuilder();
        sqlB.append("SELECT t.* FROM ( ");
        sqlB.append(sql+" ");
        sqlB.append(") t ");
        if (params!=null&&params.size() >= 1) {
            sqlB.append("WHERE 1=1  ");
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                String value = String.valueOf(params.get(key));
                if (oConvertUtils.isNotEmpty(value)) {
                    sqlB.append(" AND ");
                    sqlB.append(" " + key +  value );
                }
            }
        }
        return sqlB.toString();
    }

    /**
     * 获取求数量sql
     * @param sql
     * @param params
     * @return
     */
    public static String getCountSql(String sql, Map params) {
        String querySql = getFullSql(sql,params);
        //若要兼容数据库,SQL中取别名一律用大写
        querySql = "SELECT COUNT(*) COUNT FROM ("+querySql+") t2";
        return querySql;
    }

    /**
     * 生成分页查询sql
     * @param sql
     * @param page
     * @param rows
     * @return
     */
    public static String jeecgCreatePageSql(String sql,Map params, int page, int rows){
    	//TODO 数据库链接URL，需要动态获取
    	String dbTypeUrl = "jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false";
        int beginNum = (page - 1) * rows;
        String[] sqlParam = new String[3];
        sqlParam[0] = sql;
        sqlParam[1] = beginNum+"";
        sqlParam[2] = rows+"";
        if(dbTypeUrl.indexOf(DATABSE_TYPE_MYSQL)!=-1){
            sql = MessageFormat.format(MYSQL_SQL, sqlParam);
        }else if(dbTypeUrl.indexOf(DATABSE_TYPE_POSTGRE)!=-1){
            sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        }else {
            int beginIndex = (page-1)*rows;
            int endIndex = beginIndex+rows;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);
            if(dbTypeUrl.indexOf(DATABSE_TYPE_ORACLE)!=-1) {
                sql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if(dbTypeUrl.indexOf(DATABSE_TYPE_SQLSERVER)!=-1) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return sql;
    }
    
    /**
     * 生成分页查询sql
     * @param sql
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String jeecgCreatePageSql(String dbTypeUrl,String dbKey,String sql,Map params, int page, int rows){
    	sql = getFullSql(sql,params);
        int beginNum = (page - 1) * rows;
        String[] sqlParam = new String[3];
        sqlParam[0] = sql;
        sqlParam[1] = beginNum+"";
        sqlParam[2] = rows+"";
        //TODO 动态数据源，获取数据库类型
        String databaseType = "";
        if(DATABSE_TYPE_MYSQL.equalsIgnoreCase(databaseType)){
            sql = MessageFormat.format(MYSQL_SQL, sqlParam);
        }else if(DATABSE_TYPE_POSTGRE.equalsIgnoreCase(databaseType)){
            sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        }else {
            int beginIndex = (page-1)*rows;
            int endIndex = beginIndex+rows;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);
            if(DATABSE_TYPE_ORACLE.equalsIgnoreCase(databaseType)) {
                sql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if(DATABSE_TYPE_SQLSERVER.equalsIgnoreCase(databaseType)) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return sql;
    }
    
    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }
    
    //add-begin--Author:luobaoli  Date:20150620 for：增加各个数据库获取表的SQL和获取指定表列的SQL
    public static String getAllTableSql(String dbType,String ... param){
    	if(oConvertUtils.isNotEmpty(dbType)){
	    	if(DATABSE_TYPE_MYSQL.equals(dbType)){
	    		return MessageFormat.format(MYSQL_ALLTABLES_SQL, param);
	    	}else if(DATABSE_TYPE_ORACLE.equals(dbType)){
	    		return ORACLE__ALLTABLES_SQL;
	    	}else if(DATABSE_TYPE_POSTGRE.equals(dbType)){
	    		return POSTGRE__ALLTABLES_SQL;
	    	}else if(DATABSE_TYPE_SQLSERVER.equals(dbType)){
	    		return SQLSERVER__ALLTABLES_SQL;
	    	}
    	}
    	return null;
    }
    
    public static String getAllCloumnSql(String dbType,String ... param){
    	if(oConvertUtils.isNotEmpty(dbType)){
	    	if(DATABSE_TYPE_MYSQL.equals(dbType)){
	    		return MessageFormat.format(MYSQL_ALLCOLUMNS_SQL, param);
	    	}else if(DATABSE_TYPE_ORACLE.equals(dbType)){
	    		return MessageFormat.format(ORACLE_ALLCOLUMNS_SQL, param);
	    	}else if(DATABSE_TYPE_POSTGRE.equals(dbType)){
	    		return MessageFormat.format(POSTGRE_ALLCOLUMNS_SQL, param);
	    	}else if(DATABSE_TYPE_SQLSERVER.equals(dbType)){
	    		return MessageFormat.format(SQLSERVER_ALLCOLUMNS_SQL, param);
	    	}
    	}
    	return null;
    }
    
    /**
	 * 【转换${}规则为万能SQL，方便SQL执行解析】
	 * @param sql
	 * @return
	 */
	public static String getSql(String sql){
		String regex = "\\$\\{\\w+\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		while(m.find()){
			String whereParam = m.group();
			//System.out.println(whereParam);
			sql = sql.replace(whereParam, "'' or 1=1 or 1=''");
			sql = sql.replace("'''", "''");
			//System.out.println(sql);
		}
		//兼容图表
		regex = "\\{\\w+\\}";
		p = Pattern.compile(regex);
		m = p.matcher(sql);
		while(m.find()){
			String whereParam = m.group();
			//System.out.println(whereParam);
			sql = sql.replace(whereParam, " 1=1 ");
			//System.out.println(sql);
		}
		return sql;
	}
	
	/**
	  * 获取页面查询参数
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map<?, ?> properties = request.getParameterMap();
		// 返回值Map 
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator<?> entries = properties.entrySet().iterator();
		
		Map.Entry<String, Object> entry;
		String name = "";
		String value = "";
		Object valueObj =null;
		while (entries.hasNext()) {
			entry = (Map.Entry<String, Object>) entries.next();
			name = (String) entry.getKey();
			valueObj = entry.getValue();
			if ("_t".equals(name) || null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		} 
		return returnMap;
	}
}
