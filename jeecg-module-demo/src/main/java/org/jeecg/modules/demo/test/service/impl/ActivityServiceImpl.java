package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.test.entity.Activity;
import org.jeecg.modules.demo.test.mapper.ActivityMapper;
import org.jeecg.modules.demo.test.service.IActivityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>  implements IActivityService {
}
