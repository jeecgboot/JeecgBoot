package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictModelMany;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.jeecg.modules.system.model.TreeSelectModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
	
	/**
	 * 重复检查SQL
     * @param duplicateCheckVo
	 * @return
	 */
	@Deprecated
	public Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);

    /**
     * 重复校验 sql语句
     * @param duplicateCheckVo
     * @return
     */
	@Deprecated
	public Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);

    /**
     * 通过字典code获取字典数据
     * @param code 字典code
     * @return  List<DictModel>
     */
	public List<DictModel> queryDictItemsByCode(@Param("code") String code);

	/**
	 * 查询有效的数据字典项
	 * @param code
	 * @return
	 */
	List<DictModel> queryEnableDictItemsByCode(@Param("code") String code);


	/**
	 * 通过多个字典code获取字典数据
	 *
	 * @param dictCodeList
	 * @return
	 */
	public List<DictModelMany> queryDictItemsByCodeList(@Param("dictCodeList") List<String> dictCodeList);

    /**
     * 通过字典code获取字典数据
     * @param code
     * @param key
     * @return
     */
	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

	/**
	 * 可通过多个字典code查询翻译文本
	 * @param dictCodeList 多个字典code
	 * @param keys 数据列表
	 * @return
	 */
	List<DictModelMany> queryManyDictByKeys(@Param("dictCodeList") List<String> dictCodeList, @Param("keys") List<String> keys);

	/**
	 * 查询系统所有字典项
	 * @return
	 */
	public List<DictModelMany> queryAllDictItems(List<Integer> tenantIdList);
	
	/**
	 * 查询所有部门 作为字典信息 id -->value,departName -->text
	 * @return
	 */
	public List<DictModel> queryAllDepartBackDictModel();
	
	/**
	 * 查询所有用户  作为字典信息 username -->value,realname -->text
	 * @return
	 */
	public List<DictModel> queryAllUserBackDictModel();
	
	/**
	  * 根据表名、显示字段名、存储字段名 查询树
	 * @param table
	 * @param text
	 * @param code
	 * @param pid
	 * @param hasChildField
     * @param query
     * @param pidField
	 * @return
	 */
	@Deprecated
	List<TreeSelectModel> queryTreeList(@Param("query") Map<String, String> query, @Param("table") String table, @Param("text") String text, @Param("code") String code,
										@Param("pidField") String pidField, @Param("pid") String pid, @Param("hasChildField") String hasChildField,
										@Param("converIsLeafVal") int converIsLeafVal);

	/**
	 * 删除
	 * @param id
	 */
	@Select("delete from sys_dict where id = #{id}")
	public void deleteOneById(@Param("id") String id);

	/**
	 * 查询被逻辑删除的数据
	 * @return
	 */
	@Select("select * from sys_dict where del_flag = 1")
	public List<SysDict> queryDeleteList();

	/**
	 * 修改状态值
	 * @param delFlag
	 * @param id
	 */
	@Update("update sys_dict set del_flag = #{flag,jdbcType=INTEGER} where id = #{id,jdbcType=VARCHAR}")
	public void updateDictDelFlag(@Param("flag") int delFlag, @Param("id") String id);


	/**
	 * 分页查询字典表数据
	 * @param page
	 * @param query
	 * @return
	 */
	@Deprecated
	public Page<DictModel> queryDictTablePageList(Page page, @Param("query") DictQuery query);


	/**
	 * 查询 字典表数据 支持查询条件 分页
	 * @param page
	 * @param table
	 * @param text
	 * @param code
	 * @param filterSql
	 * @return
	 */
	@Deprecated
	IPage<DictModel> queryPageTableDictWithFilter(Page<DictModel> page, @Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);

	/**
	 * 查询 字典表数据 支持查询条件 查询所有
	 * @param table
	 * @param text
	 * @param code
	 * @param filterSql
	 * @return
	 */
	@Deprecated
	List<DictModel> queryTableDictWithFilter(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);

	/**
	 * 查询字典表的数据
	 * @param table 表名
	 * @param text   显示字段名
	 * @param code   存储字段名
	 * @param filterSql 条件sql
	 * @param codeValues 存储字段值 作为查询条件in
	 * @return
	 */
	@Deprecated
	List<DictModel> queryTableDictByKeysAndFilterSql(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql,  
													 @Param("codeValues") List<String> codeValues);

	/**
	 * 根据应用id获取字典列表和详情
	 * @param lowAppId
	 * @param tenantId
	 * @return
	 */
	@InterceptorIgnore(tenantLine = "true")
    List<SysDict> getDictListByLowAppId(@Param("lowAppId") String lowAppId, @Param("tenantId") Integer tenantId);

	/**
	 * 查询被逻辑删除的数据（根据租户id）
	 * @return
	 */
	@Select("select * from sys_dict where del_flag = 1 and tenant_id = #{tenantId}")
	List<SysDict> queryDeleteListBtTenantId(@Param("tenantId") Integer tenantId);
}
