package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.query_helper.QSecretQuestion;
import org.jeecg.modules.im.mapper.SecretQuestionMapper;
import org.jeecg.modules.im.service.SecretQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 密保问题 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
@Service
public class SecretQuestionServiceImpl extends BaseServiceImpl<SecretQuestionMapper, SecretQuestion> implements SecretQuestionService {

    @Autowired
    private SecretQuestionMapper mapper;
    @Override
    public List<SecretQuestion> findAll() {
        return mapper.findAll();
    }

    @Override
    public IPage<SecretQuestion> pagination(MyPage<SecretQuestion> page, QSecretQuestion q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(SecretQuestion item) {
        if(item.getId()==null){
            if(!save(item)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(item)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        for (String id : StringUtils.split(ids, ",")) {
            mapper.deleteById(id);
        }
        return success();
    }
}
