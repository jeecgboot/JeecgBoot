package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.test.entity.ActivityUser;
import org.jeecg.modules.demo.test.mapper.ActivityUserMapper;
import org.jeecg.modules.demo.test.service.IActivityUserService;
import org.springframework.stereotype.Service;

@Service
public class ActivityUserServiceImpl extends ServiceImpl<ActivityUserMapper, ActivityUser>  implements IActivityUserService {
}
