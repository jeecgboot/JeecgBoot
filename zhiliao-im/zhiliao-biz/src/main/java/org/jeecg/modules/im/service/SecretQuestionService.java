package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.query_helper.QSecretQuestion;

import java.util.List;

/**
 * <p>
 * 密保问题 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
public interface SecretQuestionService extends IService<SecretQuestion> {

    List<SecretQuestion> findAll();
    IPage<SecretQuestion> pagination(MyPage<SecretQuestion> page, QSecretQuestion q);
    Result<Object> createOrUpdate(SecretQuestion item);
    Result<Object> del(String ids);
}
