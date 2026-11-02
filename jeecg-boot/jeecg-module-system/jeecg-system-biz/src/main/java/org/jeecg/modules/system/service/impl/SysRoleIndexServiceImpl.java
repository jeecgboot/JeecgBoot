package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.constant.DefIndexConst;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysRoleIndexMapper;
import org.jeecg.modules.system.mapper.SysRoleMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysRoleIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @Description: 角色首页配置
 * @Author: jeecg-boot
 * @Date: 2022-03-25
 * @Version: V1.0
 */
@Service("sysRoleIndexServiceImpl")
public class SysRoleIndexServiceImpl extends ServiceImpl<SysRoleIndexMapper, SysRoleIndex> implements ISysRoleIndexService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    @Cacheable(cacheNames = DefIndexConst.CACHE_KEY + "#3600", key = "'" + DefIndexConst.DEF_INDEX_ALL + "'")
    public SysRoleIndex queryDefaultIndex() {
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, DefIndexConst.DEF_INDEX_ALL);
        queryWrapper.eq(SysRoleIndex::getStatus, CommonConstant.STATUS_1);
        SysRoleIndex entity = super.getOne(queryWrapper);
        // 保证不为空
        if (entity == null) {
            entity = this.initDefaultIndex();
        }
        return entity;
    }

    @Override
    public boolean updateDefaultIndex(String url, String component, boolean isRoute) {
        // 1. 先查询出配置信息
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, DefIndexConst.DEF_INDEX_ALL);
        SysRoleIndex entity = super.getOne(queryWrapper);
        boolean success = false;
        // 2. 如果不存在则新增
        if (entity == null) {
            entity = this.newDefIndexConfig(url, component, isRoute);
            success = super.save(entity);
        } else {
            // 3. 如果存在则更新
            entity.setUrl(url);
            entity.setComponent(component);
            entity.setRoute(isRoute);
            entity.setRelationType(CommonConstant.HOME_RELATION_DEFAULT);
            success = super.updateById(entity);
        }
        // 4. 清理缓存
        if (success) {
            this.cleanDefaultIndexCache();
        }
        return success;
    }

    @Override
    public SysRoleIndex initDefaultIndex() {
        return this.newDefIndexConfig(DefIndexConst.DEF_INDEX_URL, DefIndexConst.DEF_INDEX_COMPONENT, true);
    }

    /**
     * 创建默认首页配置
     *
     * @param indexComponent
     * @return
     */
    private SysRoleIndex newDefIndexConfig(String indexUrl, String indexComponent, boolean isRoute) {
        SysRoleIndex entity = new SysRoleIndex();
        entity.setRoleCode(DefIndexConst.DEF_INDEX_ALL);
        entity.setUrl(indexUrl);
        entity.setComponent(indexComponent);
        entity.setRoute(isRoute);
        entity.setStatus(CommonConstant.STATUS_1);
        entity.setRelationType(CommonConstant.HOME_RELATION_DEFAULT);
        return entity;
    }

    @Override
    public void cleanDefaultIndexCache() {
        redisUtil.del(DefIndexConst.CACHE_KEY + "::" + DefIndexConst.DEF_INDEX_ALL);
    }

    /**
     * 切换默认门户
     * @param sysRoleIndex
     */
    @Override
    public void changeDefHome(SysRoleIndex sysRoleIndex) {
        // 1. 先查询出配置信息
        String username = sysRoleIndex.getRoleCode();
        //当前状态(1:工作台/门户 0：菜单默认)
        String status = sysRoleIndex.getStatus();
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, username);
        queryWrapper.eq(SysRoleIndex::getRelationType,CommonConstant.HOME_RELATION_USER);
        queryWrapper.orderByAsc(SysRoleIndex::getPriority);
        List<SysRoleIndex> list = super.list(queryWrapper);
        boolean success = false;
        if(CommonConstant.STATUS_1.equalsIgnoreCase(status)){
            // 2. 如果存在则编辑
            if (!CollectionUtils.isEmpty(list)) {
                sysRoleIndex.setId(list.get(0).getId());
                sysRoleIndex.setStatus(CommonConstant.STATUS_1);
                sysRoleIndex.setRoute(true);
                success = super.updateById(sysRoleIndex);
            } else {
                // 3. 如果不存在则新增
                sysRoleIndex.setRelationType(CommonConstant.HOME_RELATION_USER);
                sysRoleIndex.setStatus(CommonConstant.STATUS_1);
                sysRoleIndex.setRoute(true);
                success = super.save(sysRoleIndex);
            }
        }else {
            // 0：菜单默认，则是菜单默认首页
            if (!CollectionUtils.isEmpty(list)) {
                //将用户级别的首页配置状态设置成0
                for (int i = 0; i < list.size(); i++) {
                    SysRoleIndex roleIndex = list.get(i);
                    roleIndex.setStatus(CommonConstant.STATUS_0);
                    success = super.updateById(roleIndex) || success;
                }
            } else {
                //update-begin---author:lvdandan ---date:20260901  for：【LHZP-1145】【vue3】首页配置，用户和角色应该需支持多选
                // 保存个人禁用配置，避免继续命中包含该用户的共享配置
                sysRoleIndex.setRelationType(CommonConstant.HOME_RELATION_USER);
                sysRoleIndex.setStatus(CommonConstant.STATUS_0);
                sysRoleIndex.setRoute(true);
                success = super.save(sysRoleIndex);
                //update-end---author:lvdandan ---date:20260901  for：【LHZP-1145】【vue3】首页配置，用户和角色应该需支持多选
            }
        }
        // 4. 清理缓存
        if (success) {
            this.cleanDefaultIndexCache();
            redisUtil.del(DefIndexConst.CACHE_TYPE + username);
        }
        // 5. 缓存类型
        //当前地址
        String url = sysRoleIndex.getUrl();
        //首页类型(默认首页)
        String type = DefIndexConst.HOME_TYPE_MENU;
        if(oConvertUtils.isNotEmpty(url) && CommonConstant.STATUS_1.equalsIgnoreCase(status)){
           type = url.contains(DefIndexConst.HOME_TYPE_SYSTEM) ? DefIndexConst.HOME_TYPE_SYSTEM : DefIndexConst.HOME_TYPE_PERSONAL;
        }
        redisUtil.set(DefIndexConst.CACHE_TYPE + username,type);
    }

    /**
     * 更新其他全局默认的状态值
     *
     * @param roleCode
     * @param status
     * @param id
     */
    @Override
    public void updateOtherDefaultStatus(String roleCode, String status, String id) {
        //roleCode是全局默认
        if(oConvertUtils.isNotEmpty(roleCode) && DefIndexConst.DEF_INDEX_ALL.equals(roleCode)){
            //状态为开启状态
            if(oConvertUtils.isNotEmpty(status) && CommonConstant.STATUS_1.equals(status)){
                LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysRoleIndex::getRoleCode,roleCode);
                queryWrapper.eq(SysRoleIndex::getStatus,CommonConstant.STATUS_1);
                queryWrapper.ne(SysRoleIndex::getId,id);
                queryWrapper.select(SysRoleIndex::getId);
                List<SysRoleIndex> list = this.list(queryWrapper);
                if(CollectionUtil.isNotEmpty(list)){
                    list.forEach(sysRoleIndex -> {
                        sysRoleIndex.setStatus(CommonConstant.STATUS_0);
                    });
                    this.updateBatchById(list);
                }
            }
        }
    }

    /**
     * 翻译关联的用户/角色名称，写入relationName
     * 用户显示真实姓名（逻辑删除的用户也显示），角色显示角色名称，已被物理删除的显示原编码并标注已删除
     *
     * @param records 首页配置列表
     */
    @Override
    public void fillRelationName(List<SysRoleIndex> records) {
        // 1. 按关联类型收集需要翻译的编码
        //同户名
        Set<String> usernames = new HashSet<>();
        //角色编码
        Set<String> roleCodes = new HashSet<>();
        for (SysRoleIndex record : records) {
            //用户
            if (CommonConstant.HOME_RELATION_USER.equals(record.getRelationType())) {
                //获取编码集合，里面可能是逗号分割的
                List<String> usernameList = splitRelationCodes(record.getRoleCode());
                usernames.addAll(usernameList);
            //角色
            } else if (CommonConstant.HOME_RELATION_ROLE.equals(record.getRelationType())) {
                //获取编码集合，里面可能是逗号分割的
                List<String> roleCodeList = splitRelationCodes(record.getRoleCode());
                roleCodes.addAll(roleCodeList);
            }
        }
        // 2. 批量查询名称
        //查询用户名字 key username value realname
        Map<String, String> realnameMap = queryRealnameMap(usernames);
        //查询角色名字 key roleCode value roleName
        Map<String, String> roleNameMap = queryRoleNameMap(roleCodes);
        // 3. 逐个编码翻译后逗号拼接
        for (SysRoleIndex record : records) {
            //用户
            if (CommonConstant.HOME_RELATION_USER.equals(record.getRelationType())) {
                String relationName = joinRelationNames(record.getRoleCode(), realnameMap, "用户已删除");
                record.setRelationName(relationName);
            //角色
            } else if (CommonConstant.HOME_RELATION_ROLE.equals(record.getRelationType())) {
                String relationName = joinRelationNames(record.getRoleCode(), roleNameMap, "角色已删除");
                record.setRelationName(relationName);
            }
        }
    }

    /**
     * 查询用户名对应的真实姓名（包含逻辑删除的用户）
     *
     * @param usernames 用户名
     * @return key:用户名 value:真实姓名
     */
    private Map<String, String> queryRealnameMap(Set<String> usernames) {
        Map<String, String> realnameMap = new HashMap<>();
        if (usernames.isEmpty()) {
            return realnameMap;
        }
        // 查询正常用户
        LambdaQueryWrapper<SysUser> userQuery = new LambdaQueryWrapper<>();
        userQuery.in(SysUser::getUsername, usernames);
        List<SysUser> userList = sysUserMapper.selectList(userQuery);
        for (SysUser user : userList) {
            realnameMap.put(user.getUsername(), user.getRealname());
        }
        // 正常用户中查不到的，再查逻辑删除的用户
        List<String> missingUsernames = new ArrayList<>();
        for (String username : usernames) {
            if (!realnameMap.containsKey(username)) {
                missingUsernames.add(username);
            }
        }
        if (!missingUsernames.isEmpty()) {
            //查询逻辑删除的用户，普通的查询会带着 del_flag = 0，所以要单独查询
            LambdaQueryWrapper<SysUser> deletedUserQuery = new LambdaQueryWrapper<>();
            deletedUserQuery.in(SysUser::getUsername, missingUsernames);
            deletedUserQuery.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_1);
            List<SysUser> deletedUserList = sysUserMapper.selectLogicDeleted(deletedUserQuery);
            for (SysUser user : deletedUserList) {
                realnameMap.put(user.getUsername(), user.getRealname());
            }
        }
        return realnameMap;
    }

    /**
     * 查询角色编码对应的角色名称
     *
     * @param roleCodes 角色编码
     * @return key:角色编码 value:角色名称
     */
    private Map<String, String> queryRoleNameMap(Set<String> roleCodes) {
        Map<String, String> roleNameMap = new HashMap<>();
        if (roleCodes.isEmpty()) {
            return roleNameMap;
        }
        LambdaQueryWrapper<SysRole> roleQuery = new LambdaQueryWrapper<>();
        roleQuery.in(SysRole::getRoleCode, roleCodes);
        List<SysRole> roleList = sysRoleMapper.selectList(roleQuery);
        for (SysRole role : roleList) {
            roleNameMap.put(role.getRoleCode(), role.getRoleName());
        }
        return roleNameMap;
    }

    /**
     * 逐个编码翻译成名称：名称为空时显示原编码，查不到（已被物理删除）时显示原编码并标注
     *
     * @param relationCodes 逗号分隔的用户名或角色编码
     * @param nameMap       编码与名称的对应关系
     * @param deletedTip    已删除的标注
     * @return 逗号分隔的名称
     */
    private String joinRelationNames(String relationCodes, Map<String, String> nameMap, String deletedTip) {
        //获取编码集合，里面可能是逗号分割的
        List<String> codeList = splitRelationCodes(relationCodes);
        List<String> nameList = new ArrayList<>();
        for (String code : codeList) {
            String name = nameMap.get(code);
            if (!nameMap.containsKey(code)) {
                // 查不到说明已被物理删除，显示原编码并标注
                nameList.add(code + "(" + deletedTip + ")");
            } else if (oConvertUtils.isEmpty(name)) {
                // 名称为空时显示原编码
                nameList.add(code);
            } else {
                nameList.add(name);
            }
        }
        return String.join(SymbolConstant.COMMA, nameList);
    }

    /**
     * 拆分逗号分隔的用户名或角色编码
     *
     * @param relationCodes 逗号分隔的用户名或角色编码
     * @return 编码列表
     */
    private List<String> splitRelationCodes(String relationCodes) {
        List<String> codeList = new ArrayList<>();
        if (oConvertUtils.isEmpty(relationCodes)) {
            return codeList;
        }
        String[] codeArray = relationCodes.split(SymbolConstant.COMMA);
        for (String code : codeArray) {
            String trimCode = code.trim();
            if (oConvertUtils.isNotEmpty(trimCode)) {
                codeList.add(trimCode);
            }
        }
        return codeList;
    }

}
