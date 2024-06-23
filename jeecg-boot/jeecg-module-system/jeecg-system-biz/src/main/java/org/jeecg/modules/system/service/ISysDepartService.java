package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.vo.SysDepartExportVo;
import org.jeecg.modules.system.vo.lowapp.ExportDepartVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysDepartService extends IService<SysDepart>{

    /**
     * 查询我的部门信息,并分节点进行显示
     * @param departIds 部门id
     * @return
     */
    List<SysDepartTreeModel> queryMyDeptTreeList(String departIds);

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeModel> queryTreeList();


    /**
     * 查询所有部门信息,并分节点进行显示
     * @param ids 多个部门id
     * @return
     */
    List<SysDepartTreeModel> queryTreeList(String ids);

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     * @return
     */
    public List<DepartIdModel> queryDepartIdTreeList();

    /**
     * 保存部门数据
     * @param sysDepart
     * @param username 用户名
     */
    void saveDepartData(SysDepart sysDepart,String username);

    /**
     * 更新depart数据
     * @param sysDepart
     * @param username 用户名
     * @return
     */
    Boolean updateDepartDataById(SysDepart sysDepart,String username);
    
    /**
     * 删除depart数据
     * @param id
     * @return
     */
	/* boolean removeDepartDataById(String id); */
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @param myDeptSearch
     * @param departIds 多个部门id
     * @return
     */
    List<SysDepartTreeModel> searchByKeyWord(String keyWord,String myDeptSearch,String departIds);
    
    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);
    
    /**
     * 查询SysDepart集合
     * @param userId
     * @return
     */
	public List<SysDepart> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepart> queryDepartsByUsername(String username);
    
    /**
     * 根据用户ID查询部门
     *
     * @param userId
     * @return
     */
    List<String> queryDepartsByUserId(String userId);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param ids 多个部门id
     * @return
     */
	void deleteBatchWithChildren(List<String> ids);

    /**
     *  根据部门Id查询,当前和下级所有部门IDS
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDepId(String departId);

    /**
     * 获取我的部门下级所有部门IDS
     * @param departIds 多个部门id
     * @return
     */
    List<String> getMySubDepIdsByDepId(String departIds);
    /**
     * 根据关键字获取部门信息（通讯录）
     * @param keyWord 搜索词
     * @return
     */
    List<SysDepartTreeModel> queryTreeByKeyWord(String keyWord);
    /**
     * 获取我的部门下级所有部门
     * @param parentId 父id
     * @param ids 多个部门id
     * @param primaryKey 主键字段（id或者orgCode）
     * @return
     */
    List<SysDepartTreeModel> queryTreeListByPid(String parentId,String ids, String primaryKey);

    /**
     * 获取某个部门的所有父级部门的ID
     *
     * @param departId 根据departId查
     * @return JSONObject
     */
    JSONObject queryAllParentIdByDepartId(String departId);

    /**
     * 获取某个部门的所有父级部门的ID
     *
     * @param orgCode 根据orgCode查
     * @return JSONObject
     */
    JSONObject queryAllParentIdByOrgCode(String orgCode);
    /**
     * 获取公司信息
     * @param orgCode 部门编码
     * @return
     */
    SysDepart queryCompByOrgCode(String orgCode);
    /**
     * 获取下级部门
     * @param pid
     * @return
     */
    List<SysDepart> queryDeptByPid(String pid);

    /**
     * 获取我的部门已加入的公司
     * @return
     */
    List<SysDepart> getMyDepartList();

    /**
     * 删除部门
     * @param id
     */
    void deleteDepart(String id);

    /**
     * 通讯录通过租户id查询部门数据
     * @param parentId
     * @param tenantId
     * @param departName
     * @return
     */
    List<SysDepartTreeModel> queryBookDepTreeSync(String parentId, Integer tenantId, String departName);

    /**
     * 根据id查询部门信息
     * @param parentId
     * @return
     */
    SysDepart getDepartById(String parentId);

    /**
     * 根据id查询部门信息
     * @param parentId
     * @return
     */
    IPage<SysDepart> getMaxCodeDepart(Page<SysDepart> page, String parentId);

    /**
     * 更新叶子节点
     * @param id
     * @param izLeaf
     */
    void updateIzLeaf(String id, Integer izLeaf);

    /**
     * 获取导出部门的数据
     * @param tenantId
     * @return
     */
    List<ExportDepartVo> getExcelDepart(int tenantId);

    void importExcel(List<ExportDepartVo> listSysDeparts, List<String> errorMessageList);

    /**
     * 根据租户id导出部门
     * @param tenantId
     * @return
     */
    List<SysDepartExportVo> getExportDepart(Integer tenantId);

    /**
     * 导出系统部门excel
     * @param listSysDeparts
     * @param errorMessageList
     */
    void importSysDepart(List<SysDepartExportVo> listSysDeparts, List<String> errorMessageList);
}
