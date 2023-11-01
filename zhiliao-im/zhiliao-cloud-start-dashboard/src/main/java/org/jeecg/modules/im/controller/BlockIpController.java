package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.query_helper.QBlockIp;
import org.jeecg.modules.im.service.BlockIpService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/im/blockIp")
public class BlockIpController extends BaseBackController {
    @Resource
    BlockIpService blockIpService;

    @RequestMapping("/pagination")
    public Result<Object> list(QBlockIp q){
        return success(blockIpService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated BlockIp blockIp, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return blockIpService.createOrUpdate(blockIp);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        BlockIp ip = blockIpService.getById(id);
        if(ip.getType().equals(BlockIp.Type.区间.name())){
            ip.setIp1(IPUtil.longToIP(ip.getNum1()));
            ip.setIp2(IPUtil.longToIP(ip.getNum2()));
        }
        return success(ip);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return blockIpService.del(ids);
    }
}
