package org.jeecg.modules.business.testuser.service.impl;

import org.jeecg.modules.business.testuser.entity.TestUser;
import org.jeecg.modules.business.testuser.mapper.TestUserMapper;
import org.jeecg.modules.business.testuser.service.ITestUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 测试用户
 * @Author: jeecg-boot
 * @Date:   2020-05-24
 * @Version: V1.0
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements ITestUserService {

}
