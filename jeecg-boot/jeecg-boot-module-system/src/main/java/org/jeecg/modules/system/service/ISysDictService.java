package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.TreeSelectModel;

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

    public Map<String,List<DictModel>> queryAllDictItems();

    @Deprecated
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    @Deprecated
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

    public String queryDictTextByKey(String code, String key);

    @Deprecated
	String queryTableDictTextByKey(String table, String text, String code, String key);

	@Deprecated
	List<String> queryTableDictByKeys(String table, String text, String code, String keys);

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

}
