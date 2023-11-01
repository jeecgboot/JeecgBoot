package org.jeecg.modules.im.controller;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.query_helper.QMuc;
import org.jeecg.modules.im.service.MucService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 群组
 */
@RestController
@RequestMapping("/im/muc")
public class MucController extends BaseBackController {
    @Resource
    private MucService mucService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMuc q){
        if(!isEmpty(q.getIds())) {
            StringBuilder s = new StringBuilder();
            for (String id : StringUtils.split(q.getIds(), ",")) {
                if(isEmpty(id)){
                    continue;
                }
                s.append("'").append(id).append("',");
            }
            q.setIds(s.substring(0,s.toString().length()-1));
        }
        return success(mucService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建群组
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody Muc muc){
        return mucService.createOrUpdate(muc);
    }

    /**
     * 解散群组
     */
    @RequestMapping("/destroy")
    public Result<Object> destroy(@RequestParam Integer id){
        return mucService.consoleDestroy(id);
    }

    /**
     * 群组详情
     */
    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(mucService.getById(id));
    }
}
