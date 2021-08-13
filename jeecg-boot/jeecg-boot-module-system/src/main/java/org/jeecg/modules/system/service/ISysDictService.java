package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.TreeSelectModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictService extends IService<SysDict> {

    public List<DictModel> queryDictItemsByCode(String code);

	/**
	 * 查询有效的数据字典项
	 * @param code
	 * @return
	 */
	List<DictModel> queryEnableDictItemsByCode(String code);

	/**
	 * 通过多个字典code获取字典数据
	 *
	 * @param dictCodeList
	 * @return key = 字典code，value=对应的字典选项
	 */
	Map<String, List<DictModel>> queryDictItemsByCodeList(List<String> dictCodeList);

    public Map<String,List<DictModel>> queryAllDictItems();

    @Deprecated
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    @Deprecated
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

    public String queryDictTextByKey(String code, String key);

	/**
	 * 可通过多个字典code查询翻译文本
	 * @param dictCodeList 多个字典code
	 * @param keys 数据列表
	 * @return
	 */
	Map<String, List<DictModel>> queryManyDictByKeys(List<String> dictCodeList, List<String> keys);

    @Deprecated
	String queryTableDictTextByKey(String table, String text, String code, String key);

	/**
	 * 通过查询指定table的 text code key 获取字典值，可批量查询
	 *
	 * @param table
	 * @param text
	 * @param code
	 * @param keys
	 * @return
	 */
	List<DictModel> queryTableDictTextByKeys(String table, String text, String code, List<String> keys);

	@Deprecated
	List<String> queryTableDictByKeys(String table, String text, String code, String keys);
	@Deprecated
	List<String> queryTableDictByKeys(String table, String text, String code, String keys,boolean delNotExist);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict
     * @return
     */
    boolean deleteByDictId(SysDict sysDict);

    /**
     * 添加一对多
     */
    public Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);

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
	 * 通过关键字查询字典表
	 * @param table
	 * @param text
	 * @param code
	 * @param keyword
	 * @return
	 */
	@Deprecated
	public List<DictModel> queryTableDictItems(String table, String text, String code,String keyword);

	/**
	 * 查询字典表数据 只查询前10条
	 * @param table
	 * @param text
	 * @param code
	 * @param keyword
	 * @return
	 */
	public List<DictModel> queryLittleTableDictItems(String table, String text, String code, String condition, String keyword, int pageSize);

	/**
	 * 查询字典表所有数据
	 * @param table
	 * @param text
	 * @param code
	 * @param condition
	 * @param keyword
	 * @return
	 */
	public List<DictModel> queryAllTableDictItems(String table, String text, String code, String condition, String keyword);
	/**
	  * 根据表名、显示字段名、存储字段名 查询树
	 * @param table
	 * @param text
	 * @param code
	 * @param pidField
	 * @param pid
	 * @param hasChildField
	 * @return
	 */
	@Deprecated
	List<TreeSelectModel> queryTreeList(Map<String, String> query,String table, String text, String code, String pidField,String pid,String hasChildField);

	/**
	 * 真实删除
	 * @param id
	 */
	public void deleteOneDictPhysically(String id);

	/**
	 * 修改delFlag
	 * @param delFlag
	 * @param id
	 */
	public void updateDictDelFlag(int delFlag,String id);

	/**
	 * 查询被逻辑删除的数据
	 * @return
	 */
	public List<SysDict> queryDeleteList();

	/**
	 * 分页查询
	 * @param query
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@Deprecated
	public List<DictModel> queryDictTablePageList(DictQuery query,int pageSize, int pageNo);

    /**
     * 获取字典数据
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    List<DictModel> getDictItems(String dictCode);

    /**
     * 【JSearchSelectTag下拉搜索组件专用接口】
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     *
     * @param dictCode 字典code格式：table,text,code
     * @return
     */
    List<DictModel> loadDict(String dictCode, String keyword, Integer pageSize);

}
