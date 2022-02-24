package org.jeecg.common.system.query;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.system.util.JeecgDataAutorUtils;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryGenerator {
	public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";

	private static final String BEGIN = "_begin";
	private static final String END = "_end";
	/**
	 * 数字类型字段，拼接此后缀 接受多值参数
	 */
	private static final String MULTI = "_MultiString";
	private static final String STAR = "*";
	private static final String COMMA = ",";
	/**
	 * 查询 逗号转义符 相当于一个逗号【作废】
	 */
	public static final String QUERY_COMMA_ESCAPE = "++";
	private static final String NOT_EQUAL = "!";
	/**页面带有规则值查询，空格作为分隔符*/
	private static final String QUERY_SEPARATE_KEYWORD = " ";
	/**高级查询前端传来的参数名*/
	private static final String SUPER_QUERY_PARAMS = "superQueryParams";
	/** 高级查询前端传来的拼接方式参数名 */
	private static final String SUPER_QUERY_MATCH_TYPE = "superQueryMatchType";
	/** 单引号 */
	public static final String SQL_SQ = "'";
	/**排序列*/
	private static final String ORDER_COLUMN = "column";
	/**排序方式*/
	private static final String ORDER_TYPE = "order";
	private static final String ORDER_TYPE_ASC = "ASC";

	/**mysql 模糊查询之特殊字符下划线 （_、\）*/
	public static final String LIKE_MYSQL_SPECIAL_STRS = "_,%";
	
	/**时间格式化 */
	private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();
	private static SimpleDateFormat getTime(){
		SimpleDateFormat time = local.get();
		if(time == null){
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			local.set(time);
		}
		return time;
	}
	
	/**
	 * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
	 * @param searchObj 查询实体
	 * @param parameterMap request.getParameterMap()
	 * @return QueryWrapper实例
	 */
	public static <T> QueryWrapper<T> initQueryWrapper(T searchObj,Map<String, String[]> parameterMap){
		long start = System.currentTimeMillis();
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		installMplus(queryWrapper, searchObj, parameterMap);
		log.debug("---查询条件构造器初始化完成,耗时:"+(System.currentTimeMillis()-start)+"毫秒----");
		return queryWrapper;
	}

	/**
	 * 组装Mybatis Plus 查询条件
	 * <p>使用此方法 需要有如下几点注意:   
	 * <br>1.使用QueryWrapper 而非LambdaQueryWrapper;
	 * <br>2.实例化QueryWrapper时不可将实体传入参数   
	 * <br>错误示例:如QueryWrapper<JeecgDemo> queryWrapper = new QueryWrapper<JeecgDemo>(jeecgDemo);
	 * <br>正确示例:QueryWrapper<JeecgDemo> queryWrapper = new QueryWrapper<JeecgDemo>();
	 * <br>3.也可以不使用这个方法直接调用 {@link #initQueryWrapper}直接获取实例
	 */
	private static void installMplus(QueryWrapper<?> queryWrapper,Object searchObj,Map<String, String[]> parameterMap) {
		
		/*
		 * 注意:权限查询由前端配置数据规则 当一个人有多个所属部门时候 可以在规则配置包含条件 orgCode 包含 #{sys_org_code}
		但是不支持在自定义SQL中写orgCode in #{sys_org_code} 
		当一个人只有一个部门 就直接配置等于条件: orgCode 等于 #{sys_org_code} 或者配置自定义SQL: orgCode = '#{sys_org_code}'
		*/
		
		//区间条件组装 模糊查询 高级查询组装 简单排序 权限查询
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);
		Map<String,SysPermissionDataRuleModel> ruleMap = getRuleMap();
		
		//权限规则自定义SQL表达式
		for (String c : ruleMap.keySet()) {
			if(oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
				queryWrapper.and(i ->i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
			}
		}
		
		String name, type, column;
		// update-begin--Author:taoyan  Date:20200923 for：issues/1671 如果字段加注解了@TableField(exist = false),不走DB查询-------
		//定义实体字段和数据库字段名称的映射 高级查询中 只能获取实体字段 如果设置TableField注解 那么查询条件会出问题
		Map<String,String> fieldColumnMap = new HashMap<String,String>();
		for (int i = 0; i < origDescriptors.length; i++) {
			//aliasName = origDescriptors[i].getName();  mybatis  不存在实体属性 不用处理别名的情况
			name = origDescriptors[i].getName();
			type = origDescriptors[i].getPropertyType().toString();
			try {
				if (judgedIsUselessField(name)|| !PropertyUtils.isReadable(searchObj, name)) {
					continue;
				}

				Object value = PropertyUtils.getSimpleProperty(searchObj, name);
				column = getTableFieldName(searchObj.getClass(), name);
				if(column==null){
					//column为null只有一种情况 那就是 添加了注解@TableField(exist = false) 后续都不用处理了
					continue;
				}
				fieldColumnMap.put(name,column);
				//数据权限查询
				if(ruleMap.containsKey(name)) {
					addRuleToQueryWrapper(ruleMap.get(name), column, origDescriptors[i].getPropertyType(), queryWrapper);
				}
				//区间查询
				doIntervalQuery(queryWrapper, parameterMap, type, name, column);
				//判断单值  参数带不同标识字符串 走不同的查询
				//TODO 这种前后带逗号的支持分割后模糊查询(多选字段查询生效) 示例：,1,3,
				if (null != value && value.toString().startsWith(COMMA) && value.toString().endsWith(COMMA)) {
					String multiLikeval = value.toString().replace(",,", COMMA);
					String[] vals = multiLikeval.substring(1, multiLikeval.length()).split(COMMA);
					final String field = oConvertUtils.camelToUnderline(column);
					if(vals.length>1) {
						queryWrapper.and(j -> {
                            log.info("---查询过滤器，Query规则---field:{}, rule:{}, value:{}", field, "like", vals[0]);
							j = j.like(field,vals[0]);
							for (int k=1;k<vals.length;k++) {
								j = j.or().like(field,vals[k]);
								log.info("---查询过滤器，Query规则 .or()---field:{}, rule:{}, value:{}", field, "like", vals[k]);
							}
							//return j;
						});
					}else {
						log.info("---查询过滤器，Query规则---field:{}, rule:{}, value:{}", field, "like", vals[0]);
						queryWrapper.and(j -> j.like(field,vals[0]));
					}
				}else {
					//根据参数值带什么关键字符串判断走什么类型的查询
					QueryRuleEnum rule = convert2Rule(value);
					value = replaceValue(rule,value);
					// add -begin 添加判断为字符串时设为全模糊查询
					//if( (rule==null || QueryRuleEnum.EQ.equals(rule)) && "class java.lang.String".equals(type)) {
						// 可以设置左右模糊或全模糊，因人而异
						//rule = QueryRuleEnum.LIKE;
					//}
					// add -end 添加判断为字符串时设为全模糊查询
					addEasyQuery(queryWrapper, column, rule, value);
				}
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		// 排序逻辑 处理 
		doMultiFieldsOrder(queryWrapper, parameterMap);
				
		//高级查询
		doSuperQuery(queryWrapper, parameterMap, fieldColumnMap);
		// update-end--Author:taoyan  Date:20200923 for：issues/1671 如果字段加注解了@TableField(exist = false),不走DB查询-------
		
	}


	/**
	 * 区间查询
	 * @param queryWrapper query对象
	 * @param parameterMap 参数map
	 * @param type         字段类型
	 * @param filedName    字段名称
	 * @param columnName   列名称
	 */
	private static void doIntervalQuery(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap, String type, String filedName, String columnName) throws ParseException {
		// 添加 判断是否有区间值
		String endValue = null,beginValue = null;
		if (parameterMap != null && parameterMap.containsKey(filedName + BEGIN)) {
			beginValue = parameterMap.get(filedName + BEGIN)[0].trim();
			addQueryByRule(queryWrapper, columnName, type, beginValue, QueryRuleEnum.GE);

		}
		if (parameterMap != null && parameterMap.containsKey(filedName + END)) {
			endValue = parameterMap.get(filedName + END)[0].trim();
			addQueryByRule(queryWrapper, columnName, type, endValue, QueryRuleEnum.LE);
		}
		//多值查询
		if (parameterMap != null && parameterMap.containsKey(filedName + MULTI)) {
			endValue = parameterMap.get(filedName + MULTI)[0].trim();
			addQueryByRule(queryWrapper, columnName.replace(MULTI,""), type, endValue, QueryRuleEnum.IN);
		}
	}
	
	//多字段排序 TODO 需要修改前端
	private static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
		String column=null,order=null;
		if(parameterMap!=null&& parameterMap.containsKey(ORDER_COLUMN)) {
			column = parameterMap.get(ORDER_COLUMN)[0];
		}
		if(parameterMap!=null&& parameterMap.containsKey(ORDER_TYPE)) {
			order = parameterMap.get(ORDER_TYPE)[0];
		}
        log.debug("排序规则>>列:" + column + ",排序方式:" + order);
		if (oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			//字典字段，去掉字典翻译文本后缀
			if(column.endsWith(CommonConstant.DICT_TEXT_SUFFIX)) {
				column = column.substring(0, column.lastIndexOf(CommonConstant.DICT_TEXT_SUFFIX));
			}
			//SQL注入check
			SqlInjectionUtil.filterContent(column);

			//update-begin--Author:scott  Date:20210531 for：36 多条件排序无效问题修正-------
			// 排序规则修改
			// 将现有排序 _ 前端传递排序条件{....,column: 'column1,column2',order: 'desc'} 翻译成sql "column1,column2 desc"
			// 修改为 _ 前端传递排序条件{....,column: 'column1,column2',order: 'desc'} 翻译成sql "column1 desc,column2 desc"
			if (order.toUpperCase().indexOf(ORDER_TYPE_ASC)>=0) {
				//queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
				String columnStr = oConvertUtils.camelToUnderline(column);
				String[] columnArray = columnStr.split(",");
				queryWrapper.orderByAsc(Arrays.asList(columnArray));
			} else {
				//queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
				String columnStr = oConvertUtils.camelToUnderline(column);
				String[] columnArray = columnStr.split(",");
				queryWrapper.orderByDesc(Arrays.asList(columnArray));
			}
			//update-end--Author:scott  Date:20210531 for：36 多条件排序无效问题修正-------
		}
	}
	
	/**
	 * 高级查询
	 * @param queryWrapper 查询对象
	 * @param parameterMap 参数对象
	 * @param fieldColumnMap 实体字段和数据库列对应的map
	 */
	private static void doSuperQuery(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap, Map<String,String> fieldColumnMap) {
		if(parameterMap!=null&& parameterMap.containsKey(SUPER_QUERY_PARAMS)){
			String superQueryParams = parameterMap.get(SUPER_QUERY_PARAMS)[0];
			String superQueryMatchType = parameterMap.get(SUPER_QUERY_MATCH_TYPE) != null ? parameterMap.get(SUPER_QUERY_MATCH_TYPE)[0] : MatchTypeEnum.AND.getValue();
            MatchTypeEnum matchType = MatchTypeEnum.getByValue(superQueryMatchType);
            // update-begin--Author:sunjianlei  Date:20200325 for：高级查询的条件要用括号括起来，防止和用户的其他条件冲突 -------
            try {
                superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
                List<QueryCondition> conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
                if (conditions == null || conditions.size() == 0) {
                    return;
                }
				// update-begin-author:sunjianlei date:20220119 for: 【JTC-573】 过滤空条件查询，防止 sql 拼接多余的 and
				List<QueryCondition> filterConditions = conditions.stream().filter(
						rule -> oConvertUtils.isNotEmpty(rule.getField())
								&& oConvertUtils.isNotEmpty(rule.getRule())
								&& oConvertUtils.isNotEmpty(rule.getVal())
				).collect(Collectors.toList());
				if (filterConditions.size() == 0) {
					return;
				}
				// update-end-author:sunjianlei date:20220119 for: 【JTC-573】 过滤空条件查询，防止 sql 拼接多余的 and
                log.info("---高级查询参数-->" + filterConditions);

                queryWrapper.and(andWrapper -> {
                    for (int i = 0; i < filterConditions.size(); i++) {
                        QueryCondition rule = filterConditions.get(i);
                        if (oConvertUtils.isNotEmpty(rule.getField())
                                && oConvertUtils.isNotEmpty(rule.getRule())
                                && oConvertUtils.isNotEmpty(rule.getVal())) {

                            log.debug("SuperQuery ==> " + rule.toString());

                            //update-begin-author:taoyan date:20201228 for: 【高级查询】 oracle 日期等于查询报错
							Object queryValue = rule.getVal();
                            if("date".equals(rule.getType())){
								queryValue = DateUtils.str2Date(rule.getVal(),DateUtils.date_sdf.get());
							}else if("datetime".equals(rule.getType())){
								queryValue = DateUtils.str2Date(rule.getVal(), DateUtils.datetimeFormat.get());
							}
							// update-begin--author:sunjianlei date:20210702 for：【/issues/I3VR8E】高级查询没有类型转换，查询参数都是字符串类型 ----
							String dbType = rule.getDbType();
							if (oConvertUtils.isNotEmpty(dbType)) {
								try {
									String valueStr = String.valueOf(queryValue);
									switch (dbType.toLowerCase().trim()) {
										case "int":
											queryValue = Integer.parseInt(valueStr);
											break;
										case "bigdecimal":
											queryValue = new BigDecimal(valueStr);
											break;
										case "short":
											queryValue = Short.parseShort(valueStr);
											break;
										case "long":
											queryValue = Long.parseLong(valueStr);
											break;
										case "float":
											queryValue = Float.parseFloat(valueStr);
											break;
										case "double":
											queryValue = Double.parseDouble(valueStr);
											break;
										case "boolean":
											queryValue = Boolean.parseBoolean(valueStr);
											break;
									}
								} catch (Exception e) {
									log.error("高级查询值转换失败：", e);
								}
							}
							// update-begin--author:sunjianlei date:20210702 for：【/issues/I3VR8E】高级查询没有类型转换，查询参数都是字符串类型 ----
                            addEasyQuery(andWrapper, fieldColumnMap.get(rule.getField()), QueryRuleEnum.getByValue(rule.getRule()), queryValue);
							//update-end-author:taoyan date:20201228 for: 【高级查询】 oracle 日期等于查询报错

                            // 如果拼接方式是OR，就拼接OR
                            if (MatchTypeEnum.OR == matchType && i < (filterConditions.size() - 1)) {
                                andWrapper.or();
                            }
                        }
                    }
                    //return andWrapper;
                });
            } catch (UnsupportedEncodingException e) {
                log.error("--高级查询参数转码失败：" + superQueryParams, e);
            } catch (Exception e) {
                log.error("--高级查询拼接失败：" + e.getMessage());
                e.printStackTrace();
            }
            // update-end--Author:sunjianlei  Date:20200325 for：高级查询的条件要用括号括起来，防止和用户的其他条件冲突 -------
		}
		//log.info(" superQuery getCustomSqlSegment: "+ queryWrapper.getCustomSqlSegment());
	}
	/**
	 * 根据所传的值 转化成对应的比较方式
	 * 支持><= like in !
	 * @param value
	 * @return
	 */
	public static QueryRuleEnum convert2Rule(Object value) {
		// 避免空数据
		// update-begin-author:taoyan date:20210629 for: 查询条件输入空格导致return null后续判断导致抛出null异常
		if (value == null) {
			return QueryRuleEnum.EQ;
		}
		String val = (value + "").toString().trim();
		if (val.length() == 0) {
			return QueryRuleEnum.EQ;
		}
		// update-end-author:taoyan date:20210629 for: 查询条件输入空格导致return null后续判断导致抛出null异常
		QueryRuleEnum rule =null;

		//update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
		//TODO 此处规则，只适用于 le lt ge gt
		// step 2 .>= =<
		if (rule == null && val.length() >= 3) {
			if(QUERY_SEPARATE_KEYWORD.equals(val.substring(2, 3))){
				rule = QueryRuleEnum.getByValue(val.substring(0, 2));
			}
		}
		// step 1 .> <
		if (rule == null && val.length() >= 2) {
			if(QUERY_SEPARATE_KEYWORD.equals(val.substring(1, 2))){
				rule = QueryRuleEnum.getByValue(val.substring(0, 1));
			}
		}
		//update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284---------------------

		// step 3 like
		if (rule == null && val.contains(STAR)) {
			if (val.startsWith(STAR) && val.endsWith(STAR)) {
				rule = QueryRuleEnum.LIKE;
			} else if (val.startsWith(STAR)) {
				rule = QueryRuleEnum.LEFT_LIKE;
			} else if(val.endsWith(STAR)){
				rule = QueryRuleEnum.RIGHT_LIKE;
			}
		}

		// step 4 in
		if (rule == null && val.contains(COMMA)) {
			//TODO in 查询这里应该有个bug  如果一字段本身就是多选 此时用in查询 未必能查询出来
			rule = QueryRuleEnum.IN;
		}
		// step 5 != 
		if(rule == null && val.startsWith(NOT_EQUAL)){
			rule = QueryRuleEnum.NE;
		}
		// step 6 xx+xx+xx 这种情况适用于如果想要用逗号作精确查询 但是系统默认逗号走in 所以可以用++替换【此逻辑作废】
		if(rule == null && val.indexOf(QUERY_COMMA_ESCAPE)>0){
			rule = QueryRuleEnum.EQ_WITH_ADD;
		}

		//update-begin--Author:taoyan  Date:20201229 for：initQueryWrapper组装sql查询条件错误 #284---------------------
		//特殊处理：Oracle的表达式to_date('xxx','yyyy-MM-dd')含有逗号，会被识别为in查询，转为等于查询
		if(rule == QueryRuleEnum.IN && val.indexOf("yyyy-MM-dd")>=0 && val.indexOf("to_date")>=0){
			rule = QueryRuleEnum.EQ;
		}
		//update-end--Author:taoyan  Date:20201229 for：initQueryWrapper组装sql查询条件错误 #284---------------------

		return rule != null ? rule : QueryRuleEnum.EQ;
	}
	
	/**
	 * 替换掉关键字字符
	 * 
	 * @param rule
	 * @param value
	 * @return
	 */
	private static Object replaceValue(QueryRuleEnum rule, Object value) {
		if (rule == null) {
			return null;
		}
		if (! (value instanceof String)){
			return value;
		}
		String val = (value + "").toString().trim();
		if (rule == QueryRuleEnum.LIKE) {
			value = val.substring(1, val.length() - 1);
			//mysql 模糊查询之特殊字符下划线 （_、\）
			value = specialStrConvert(value.toString());
		} else if (rule == QueryRuleEnum.LEFT_LIKE || rule == QueryRuleEnum.NE) {
			value = val.substring(1);
			//mysql 模糊查询之特殊字符下划线 （_、\）
			value = specialStrConvert(value.toString());
		} else if (rule == QueryRuleEnum.RIGHT_LIKE) {
			value = val.substring(0, val.length() - 1);
			//mysql 模糊查询之特殊字符下划线 （_、\）
			value = specialStrConvert(value.toString());
		} else if (rule == QueryRuleEnum.IN) {
			value = val.split(",");
		} else if (rule == QueryRuleEnum.EQ_WITH_ADD) {
			value = val.replaceAll("\\+\\+", COMMA);
		}else {
			//update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
			if(val.startsWith(rule.getValue())){
				//TODO 此处逻辑应该注释掉-> 如果查询内容中带有查询匹配规则符号，就会被截取的（比如：>=您好）
				value = val.replaceFirst(rule.getValue(),"");
			}else if(val.startsWith(rule.getCondition()+QUERY_SEPARATE_KEYWORD)){
				value = val.replaceFirst(rule.getCondition()+QUERY_SEPARATE_KEYWORD,"").trim();
			}
			//update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
		}
		return value;
	}
	
	private static void addQueryByRule(QueryWrapper<?> queryWrapper,String name,String type,String value,QueryRuleEnum rule) throws ParseException {
		if(oConvertUtils.isNotEmpty(value)) {
			//update-begin--Author:sunjianlei  Date:20220104 for：【JTC-409】修复逗号分割情况下没有转换类型，导致类型严格的数据库查询报错 -------------------
			// 针对数字类型字段，多值查询
			if(value.contains(COMMA)){
				Object[] temp = Arrays.stream(value.split(COMMA)).map(v -> {
					try {
						return QueryGenerator.parseByType(v, type, rule);
					} catch (ParseException e) {
						e.printStackTrace();
						return v;
					}
				}).toArray();
				addEasyQuery(queryWrapper, name, rule, temp);
				return;
			}
			Object temp = QueryGenerator.parseByType(value, type, rule);
			addEasyQuery(queryWrapper, name, rule, temp);
			//update-end--Author:sunjianlei  Date:20220104 for：【JTC-409】修复逗号分割情况下没有转换类型，导致类型严格的数据库查询报错 -------------------
		}
	}

	/**
	 * 根据类型转换给定的值
	 * @param value
	 * @param type
	 * @param rule
	 * @return
	 * @throws ParseException
	 */
	private static Object parseByType(String value, String type, QueryRuleEnum rule) throws ParseException {
		Object temp;
		switch (type) {
			case "class java.lang.Integer":
				temp =  Integer.parseInt(value);
				break;
			case "class java.math.BigDecimal":
				temp =  new BigDecimal(value);
				break;
			case "class java.lang.Short":
				temp =  Short.parseShort(value);
				break;
			case "class java.lang.Long":
				temp =  Long.parseLong(value);
				break;
			case "class java.lang.Float":
				temp =   Float.parseFloat(value);
				break;
			case "class java.lang.Double":
				temp =  Double.parseDouble(value);
				break;
			case "class java.util.Date":
				temp = getDateQueryByRule(value, rule);
				break;
			default:
				temp = value;
				break;
		}
		return temp;
	}
	
	/**
	 * 获取日期类型的值
	 * @param value
	 * @param rule
	 * @return
	 * @throws ParseException
	 */
	private static Date getDateQueryByRule(String value,QueryRuleEnum rule) throws ParseException {
		Date date = null;
		if(value.length()==10) {
			if(rule==QueryRuleEnum.GE) {
				//比较大于
				date = getTime().parse(value + " 00:00:00");
			}else if(rule==QueryRuleEnum.LE) {
				//比较小于
				date = getTime().parse(value + " 23:59:59");
			}
			//TODO 日期类型比较特殊 可能oracle下不一定好使
		}
		if(date==null) {
			date = getTime().parse(value);
		}
		return date;
	}
	
	/**
	  * 根据规则走不同的查询
	 * @param queryWrapper QueryWrapper
	 * @param name         字段名字
	 * @param rule         查询规则
	 * @param value        查询条件值
	 */
	public static void addEasyQuery(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
		if (value == null || rule == null || oConvertUtils.isEmpty(value)) {
			return;
		}
		name = oConvertUtils.camelToUnderline(name);
		log.info("---查询过滤器，Query规则---field:{}, rule:{}, value:{}",name,rule.getValue(),value);
		switch (rule) {
		case GT:
			queryWrapper.gt(name, value);
			break;
		case GE:
			queryWrapper.ge(name, value);
			break;
		case LT:
			queryWrapper.lt(name, value);
			break;
		case LE:
			queryWrapper.le(name, value);
			break;
		case EQ:
		case EQ_WITH_ADD:
			queryWrapper.eq(name, value);
			break;
		case NE:
			queryWrapper.ne(name, value);
			break;
		case IN:
			if(value instanceof String) {
				queryWrapper.in(name, (Object[])value.toString().split(COMMA));
			}else if(value instanceof String[]) {
				queryWrapper.in(name, (Object[]) value);
			}
			//update-begin-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
			else if(value.getClass().isArray()) {
				queryWrapper.in(name, (Object[])value);
			}else {
				queryWrapper.in(name, value);
			}
			//update-end-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
			break;
		case LIKE:
			queryWrapper.like(name, value);
			break;
		case LEFT_LIKE:
			queryWrapper.likeLeft(name, value);
			break;
		case RIGHT_LIKE:
			queryWrapper.likeRight(name, value);
			break;
		default:
			log.info("--查询规则未匹配到---");
			break;
		}
	}
	/**
	 * 
	 * @param name
	 * @return
	 */
	private static boolean judgedIsUselessField(String name) {
		return "class".equals(name) || "ids".equals(name)
				|| "page".equals(name) || "rows".equals(name)
				|| "sort".equals(name) || "order".equals(name);
	}

	

	/**
	 * 获取请求对应的数据权限规则 TODO 相同列权限多个 有问题
	 * @return
	 */
	public static Map<String, SysPermissionDataRuleModel> getRuleMap() {
		Map<String, SysPermissionDataRuleModel> ruleMap = new HashMap<String, SysPermissionDataRuleModel>();
		List<SysPermissionDataRuleModel> list =JeecgDataAutorUtils.loadDataSearchConditon();
		if(list != null&&list.size()>0){
			if(list.get(0)==null){
				return ruleMap;
			}
			for (SysPermissionDataRuleModel rule : list) {
				String column = rule.getRuleColumn();
				if(QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
					column = SQL_RULES_COLUMN+rule.getId();
				}
				ruleMap.put(column, rule);
			}
		}
		return ruleMap;
	}
	
	private static void addRuleToQueryWrapper(SysPermissionDataRuleModel dataRule, String name, Class propertyType, QueryWrapper<?> queryWrapper) {
		QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
		if(rule.equals(QueryRuleEnum.IN) && ! propertyType.equals(String.class)) {
			String[] values = dataRule.getRuleValue().split(",");
			Object[] objs = new Object[values.length];
			for (int i = 0; i < values.length; i++) {
				objs[i] = NumberUtils.parseNumber(values[i], propertyType);
			}
			addEasyQuery(queryWrapper, name, rule, objs);
		}else {
			if (propertyType.equals(String.class)) {
				addEasyQuery(queryWrapper, name, rule, converRuleValue(dataRule.getRuleValue()));
			}else if (propertyType.equals(Date.class)) {
				String dateStr =converRuleValue(dataRule.getRuleValue());
				if(dateStr.length()==10){
					addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr,DateUtils.date_sdf.get()));
				}else{
					addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr,DateUtils.datetimeFormat.get()));
				}
			}else {
				addEasyQuery(queryWrapper, name, rule, NumberUtils.parseNumber(dataRule.getRuleValue(), propertyType));
			}
		}
	}
	
	public static String converRuleValue(String ruleValue) {
		String value = JwtUtil.getUserSystemData(ruleValue,null);
		return value!= null ? value : ruleValue;
	}

	/**
	* @author: scott
	* @Description: 去掉值前后单引号
	* @date: 2020/3/19 21:26
	* @param ruleValue: 
	* @Return: java.lang.String
	*/
	public static String trimSingleQuote(String ruleValue) {
		if (oConvertUtils.isEmpty(ruleValue)) {
			return "";
		}
		if (ruleValue.startsWith(QueryGenerator.SQL_SQ)) {
			ruleValue = ruleValue.substring(1);
		}
		if (ruleValue.endsWith(QueryGenerator.SQL_SQ)) {
			ruleValue = ruleValue.substring(0, ruleValue.length() - 1);
		}
		return ruleValue;
	}
	
	public static String getSqlRuleValue(String sqlRule){
		try {
			Set<String> varParams = getSqlRuleParams(sqlRule);
			for(String var:varParams){
				String tempValue = converRuleValue(var);
				sqlRule = sqlRule.replace("#{"+var+"}",tempValue);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return sqlRule;
	}
	
	/**
	 * 获取sql中的#{key} 这个key组成的set
	 */
	public static Set<String> getSqlRuleParams(String sql) {
		if(oConvertUtils.isEmpty(sql)){
			return null;
		}
		Set<String> varParams = new HashSet<String>();
		String regex = "\\#\\{\\w+\\}";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		while(m.find()){
			String var = m.group();
			varParams.add(var.substring(var.indexOf("{")+1,var.indexOf("}")));
		}
		return varParams;
	}
	
	/**
	 * 获取查询条件 
	 * @param field
	 * @param alias
	 * @param value
	 * @param isString
	 * @return
	 */
	public static String getSingleQueryConditionSql(String field,String alias,Object value,boolean isString) {
		return getSingleQueryConditionSql(field, alias, value, isString,null);
	}

	/**
	 * 报表获取查询条件 支持多数据源
	 * @param field
	 * @param alias
	 * @param value
	 * @param isString
	 * @param dataBaseType
	 * @return
	 */
	public static String getSingleQueryConditionSql(String field,String alias,Object value,boolean isString, String dataBaseType) {
		if (value == null) {
			return "";
		}
		field =  alias+oConvertUtils.camelToUnderline(field);
		QueryRuleEnum rule = QueryGenerator.convert2Rule(value);
		return getSingleSqlByRule(rule, field, value, isString, dataBaseType);
	}

	/**
	 * 获取单个查询条件的值
	 * @param rule
	 * @param field
	 * @param value
	 * @param isString
	 * @param dataBaseType
	 * @return
	 */
	private static String getSingleSqlByRule(QueryRuleEnum rule,String field,Object value,boolean isString, String dataBaseType) {
		String res = "";
		switch (rule) {
		case GT:
			res =field+rule.getValue()+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case GE:
			res = field+rule.getValue()+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case LT:
			res = field+rule.getValue()+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case LE:
			res = field+rule.getValue()+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case EQ:
			res = field+rule.getValue()+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case EQ_WITH_ADD:
			res = field+" = "+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case NE:
			res = field+" <> "+getFieldConditionValue(value, isString, dataBaseType);
			break;
		case IN:
			res = field + " in "+getInConditionValue(value, isString);
			break;
		case LIKE:
			res = field + " like "+getLikeConditionValue(value);
			break;
		case LEFT_LIKE:
			res = field + " like "+getLikeConditionValue(value);
			break;
		case RIGHT_LIKE:
			res = field + " like "+getLikeConditionValue(value);
			break;
		default:
			res = field+" = "+getFieldConditionValue(value, isString, dataBaseType);
			break;
		}
		return res;
	}


	/**
	 * 获取单个查询条件的值
	 * @param rule
	 * @param field
	 * @param value
	 * @param isString
	 * @return
	 */
	private static String getSingleSqlByRule(QueryRuleEnum rule,String field,Object value,boolean isString) {
		return getSingleSqlByRule(rule, field, value, isString, null);
	}

	/**
	 * 获取查询条件的值
	 * @param value
	 * @param isString
	 * @param dataBaseType
	 * @return
	 */
	private static String getFieldConditionValue(Object value,boolean isString, String dataBaseType) {
		String str = value.toString().trim();
		if(str.startsWith("!")) {
			str = str.substring(1);
		}else if(str.startsWith(">=")) {
			str = str.substring(2);
		}else if(str.startsWith("<=")) {
			str = str.substring(2);
		}else if(str.startsWith(">")) {
			str = str.substring(1);
		}else if(str.startsWith("<")) {
			str = str.substring(1);
		}else if(str.indexOf(QUERY_COMMA_ESCAPE)>0) {
			str = str.replaceAll("\\+\\+", COMMA);
		}
		if(dataBaseType==null){
			dataBaseType = getDbType();
		}
		if(isString) {
			if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(dataBaseType)){
				return " N'"+str+"' ";
			}else{
				return " '"+str+"' ";
			}
		}else {
			// 如果不是字符串 有一种特殊情况 popup调用都走这个逻辑 参数传递的可能是“‘admin’”这种格式的
			if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(dataBaseType) && str.endsWith("'") && str.startsWith("'")){
				return " N"+str;
			}
			return value.toString();
		}
	}

	private static String getInConditionValue(Object value,boolean isString) {
		//update-begin-author:taoyan date:20210628 for: 查询条件如果输入,导致sql报错
		String[] temp = value.toString().split(",");
		if(temp.length==0){
			return "('')";
		}
		if(isString) {
			List<String> res = new ArrayList<>();
			for (String string : temp) {
				if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
					res.add("N'"+string+"'");
				}else{
					res.add("'"+string+"'");
				}
			}
			return "("+String.join("," ,res)+")";
		}else {
			return "("+value.toString()+")";
		}
		//update-end-author:taoyan date:20210628 for: 查询条件如果输入,导致sql报错
	}
	
	private static String getLikeConditionValue(Object value) {
		String str = value.toString().trim();
		if(str.startsWith("*") && str.endsWith("*")) {
			if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
				return "N'%"+str.substring(1,str.length()-1)+"%'";
			}else{
				return "'%"+str.substring(1,str.length()-1)+"%'";
			}
		}else if(str.startsWith("*")) {
			if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
				return "N'%"+str.substring(1)+"'";
			}else{
				return "'%"+str.substring(1)+"'";
			}
		}else if(str.endsWith("*")) {
			if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
				return "N'"+str.substring(0,str.length()-1)+"%'";
			}else{
				return "'"+str.substring(0,str.length()-1)+"%'";
			}
		}else {
			if(str.indexOf("%")>=0) {
				if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
					if(str.startsWith("'") && str.endsWith("'")){
						return "N"+str;
					}else{
						return "N"+"'"+str+"'";
					}
				}else{
					if(str.startsWith("'") && str.endsWith("'")){
						return str;
					}else{
						return "'"+str+"'";
					}
				}
			}else {
				if(DataBaseConstant.DB_TYPE_SQLSERVER.equals(getDbType())){
					return "N'%"+str+"%'";
				}else{
					return "'%"+str+"%'";
				}
			}
		}
	}
	
	/**
	 *   根据权限相关配置生成相关的SQL 语句
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String installAuthJdbc(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();
		//权限查询
		Map<String,SysPermissionDataRuleModel> ruleMap = getRuleMap();
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);
		String sql_and = " and ";
		for (String c : ruleMap.keySet()) {
			if(oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
				sb.append(sql_and+getSqlRuleValue(ruleMap.get(c).getRuleValue()));
			}
		}
		String name, column;
		for (int i = 0; i < origDescriptors.length; i++) {
			name = origDescriptors[i].getName();
			if (judgedIsUselessField(name)) {
				continue;
			}
			if(ruleMap.containsKey(name)) {
				column = getTableFieldName(clazz, name);
				if(column==null){
					continue;
				}
				SysPermissionDataRuleModel dataRule = ruleMap.get(name);
				QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
				Class propType = origDescriptors[i].getPropertyType();
				boolean isString = propType.equals(String.class);
				Object value;
				if(isString) {
					value = converRuleValue(dataRule.getRuleValue());
				}else {
					value = NumberUtils.parseNumber(dataRule.getRuleValue(),propType);
				}
				String filedSql = getSingleSqlByRule(rule, oConvertUtils.camelToUnderline(column), value,isString);
				sb.append(sql_and+filedSql);
			}
		}
		log.info("query auth sql is:"+sb.toString());
		return sb.toString();
	}
	
	/**
	  * 根据权限相关配置 组装mp需要的权限
	 * @param queryWrapper
	 * @param clazz
	 * @return
	 */
	public static void installAuthMplus(QueryWrapper<?> queryWrapper,Class<?> clazz) {
		//权限查询
		Map<String,SysPermissionDataRuleModel> ruleMap = getRuleMap();
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);
		for (String c : ruleMap.keySet()) {
			if(oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
				queryWrapper.and(i ->i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
			}
		}
		String name, column;
		for (int i = 0; i < origDescriptors.length; i++) {
			name = origDescriptors[i].getName();
			if (judgedIsUselessField(name)) {
				continue;
			}
			column = getTableFieldName(clazz, name);
			if(column==null){
				continue;
			}
			if(ruleMap.containsKey(name)) {
				addRuleToQueryWrapper(ruleMap.get(name), column, origDescriptors[i].getPropertyType(), queryWrapper);
			}
		}
	}

	/**
	 * 转换sql中的系统变量
	 * @param sql
	 * @return
	 */
	public static String convertSystemVariables(String sql){
		return getSqlRuleValue(sql);
	}

	/**
	 * 获取所有配置的权限 返回sql字符串 不受字段限制 配置什么就拿到什么
	 * @return
	 */
	public static String getAllConfigAuth() {
		StringBuffer sb = new StringBuffer();
		//权限查询
		Map<String,SysPermissionDataRuleModel> ruleMap = getRuleMap();
		String sql_and = " and ";
		for (String c : ruleMap.keySet()) {
			SysPermissionDataRuleModel dataRule = ruleMap.get(c);
			String ruleValue = dataRule.getRuleValue();
			if(oConvertUtils.isEmpty(ruleValue)){
				continue;
			}
			if(oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
				sb.append(sql_and+getSqlRuleValue(ruleValue));
			}else{
				boolean isString  = false;
				ruleValue = ruleValue.trim();
				if(ruleValue.startsWith("'") && ruleValue.endsWith("'")){
					isString = true;
					ruleValue = ruleValue.substring(1,ruleValue.length()-1);
				}
				QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
				String value = converRuleValue(ruleValue);
				String filedSql = getSingleSqlByRule(rule, c, value,isString);
				sb.append(sql_and+filedSql);
			}
		}
		log.info("query auth sql is = "+sb.toString());
		return sb.toString();
	}



	/**
	 * 获取系统数据库类型
	 */
	private static String getDbType(){
		return CommonUtils.getDatabaseType();
	}


	/**
	 * 获取class的 包括父类的
	 * @param clazz
	 * @return
	 */
	private static List<Field> getClassFields(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields;
		do{
			fields = clazz.getDeclaredFields();
			for(int i = 0;i<fields.length;i++){
				list.add(fields[i]);
			}
			clazz = clazz.getSuperclass();
		}while(clazz!= Object.class&&clazz!=null);
		return list;
	}

	/**
	 * 获取表字段名
	 * @param clazz
	 * @param name
	 * @return
	 */
	private static String getTableFieldName(Class<?> clazz, String name) {
		try {
			//如果字段加注解了@TableField(exist = false),不走DB查询
			Field field = null;
			try {
				field = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				//e.printStackTrace();
			}

			//如果为空，则去父类查找字段
			if (field == null) {
				List<Field> allFields = getClassFields(clazz);
				List<Field> searchFields = allFields.stream().filter(a -> a.getName().equals(name)).collect(Collectors.toList());
				if(searchFields!=null && searchFields.size()>0){
					field = searchFields.get(0);
				}
			}

			if (field != null) {
				TableField tableField = field.getAnnotation(TableField.class);
				if (tableField != null){
					if(tableField.exist() == false){
						//如果设置了TableField false 这个字段不需要处理
						return null;
					}else{
						String column = tableField.value();
						//如果设置了TableField value 这个字段是实体字段
						if(!"".equals(column)){
							return column;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * mysql 模糊查询之特殊字符下划线 （_、\）
	 *
	 * @param value:
	 * @Return: java.lang.String
	 */
	private static String specialStrConvert(String value) {
		if (DataBaseConstant.DB_TYPE_MYSQL.equals(getDbType()) || DataBaseConstant.DB_TYPE_MARIADB.equals(getDbType())) {
			String[] special_str = QueryGenerator.LIKE_MYSQL_SPECIAL_STRS.split(",");
			for (String str : special_str) {
				if (value.indexOf(str) !=-1) {
					value = value.replace(str, "\\" + str);
				}
			}
		}
		return value;
	}
}
