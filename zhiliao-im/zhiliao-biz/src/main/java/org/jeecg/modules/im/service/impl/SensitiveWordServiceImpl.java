package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.query_helper.QSensitiveWord;
import org.jeecg.modules.im.mapper.SensitiveWordMapper;
import org.jeecg.modules.im.service.SensitiveWordService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 敏感词 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-20
 */
@Service
public class SensitiveWordServiceImpl extends BaseServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @Override
    public IPage<SensitiveWord> pagination(MyPage<SensitiveWord> page, QSensitiveWord q) {
        return sensitiveWordMapper.pagination(page,q);
    }
    
    @Override
    public List<SensitiveWord> findAll() {
        return sensitiveWordMapper.findAll();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        sensitiveWordMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        redisUtil.removeAll(ConstantCache.SENSITIVE_WORD.replace("%s","")+"*");
        return success();
    }

    @Override
    public Result<Object> createOrUpdate(SensitiveWord sensitiveWord) {
        if(sensitiveWord.getId()==null){
            sensitiveWord.setTsCreate(getTs());
            if(!save(sensitiveWord)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(sensitiveWord)){
                return fail("更新失败");
            }
        }
        redisUtil.removeAll(ConstantCache.SENSITIVE_WORD.replace("%s","")+"*");
        return success();
    }
}
