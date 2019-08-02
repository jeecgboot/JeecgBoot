package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.entity.Test;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.mapper.TestMapper;
import org.jeecg.modules.system.service.ITest;
import org.springframework.stereotype.Service;

/***
 *@author yinxucun
 *@date 2019-07-11 15:55 
 */
@Service
public class TestService  extends ServiceImpl<TestMapper, Test> implements ITest {
}
