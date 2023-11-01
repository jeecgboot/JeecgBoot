package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.query_helper.QSecretAnswer;

/**
 * <p>
 * 密保答案 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
public interface SecretAnswerService extends IService<SecretAnswer> {
    IPage<SecretAnswer> pagination(MyPage<SecretAnswer> page, QSecretAnswer q);

    Result<Object> check(String account, String questions);
}
