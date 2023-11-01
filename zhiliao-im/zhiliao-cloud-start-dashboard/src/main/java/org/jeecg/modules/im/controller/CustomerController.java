package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QCustomer;
import org.jeecg.modules.im.service.CustomerService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/customer")
public class CustomerController extends BaseBackController {
    @Resource
    private CustomerService customerService;

    @RequestMapping("/pagination")
    public Result<Object> list(QCustomer q){
        return success(customerService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
