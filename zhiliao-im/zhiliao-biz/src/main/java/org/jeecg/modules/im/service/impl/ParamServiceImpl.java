package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.query_helper.QParam;
import org.jeecg.modules.im.mapper.ParamMapper;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 参数 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class ParamServiceImpl extends BaseServiceImpl<ParamMapper, Param> implements ParamService {

    @Autowired
    private ParamMapper paramMapper;

    @Override
    public IPage<Param> pagination(MyPage<Param> page, QParam q) {
        return paramMapper.pagination(page,q);
    }

    @Override
    public List<Param> findAll() {
        return paramMapper.findAll();
    }

    @Override
    public String getByName(Param.Name name) {
        for (Param param : findAll()) {
            if(param.getName().equals(name.name())){
                return param.getValue();
            }
        }
        return null;
    }
    @Override
    public String getByName(Param.Name name,String defaultValue) {
        for (Param param : findAll()) {
            if(param.getName().equals(name.name())){
                return param.getValue();
            }
        }
        Param param = new Param();
        param.setName(name.name());
        param.setValue(defaultValue);
        param.setTsCreate(getTs());
        if(paramMapper.insert(param)==1){
            return defaultValue;
        }
        return defaultValue;
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        paramMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> createOrUpdate(Param param) {
        if(param.getId()==null){
            param.setTsCreate(getTs());
            if(!save(param)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(param)){
                return fail("更新失败");
            }
        }
        return success();
    }
}
