package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QBlockIp;
import org.jeecg.modules.im.mapper.BlockIpMapper;
import org.jeecg.modules.im.service.BlockIpService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前台ip黑名单 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class BlockIpServiceImpl extends BaseServiceImpl<BlockIpMapper, BlockIp> implements BlockIpService {

    @Autowired
    private BlockIpMapper blockIpMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public IPage<BlockIp> pagination(MyPage<BlockIp> page, QBlockIp q) {
        return blockIpMapper.pagination(page, q);
    }

    @Override
    public Result<Object> checkIp(String ip) {
        boolean isOk = true;
        //根据ip查
        if (findByIp(ip)!=null) {
            isOk = false;
        }
        //根据区间查
        if (isOk&&!CollectionUtils.isEmpty(findByIp2(IPUtil.ipToLong(ip)))) {
            isOk = false;
        }
        //根据地址段查
        if (isOk&&!CollectionUtils.isEmpty(findByIp3(IPUtil.address(ip)))) {
            isOk = false;
        }
        //根据国家查
        if (isOk&&findByIp4(ip)!=null) {
            isOk = false;
        }
        if (!isOk) {
            return fail("error",Kv.by("ip", ip));
        }
        return success();
    }

    //根据ip查
    public BlockIp findByIp(String ip) {
        String cacheKey = String.format(ConstantCache.BLOCK_IPS,BlockIp.Type.单个,ip);
        BlockIp blockIp = (BlockIp) redisUtil.get(cacheKey);
        if(blockIp==null){
            blockIp = blockIpMapper.findByIp(ip);
            redisUtil.set(cacheKey,blockIp);
        }
        return blockIp;
    }

    //根据区间查
    public List<BlockIp> findByIp2(long ip) {
        String cacheKey = String.format(ConstantCache.BLOCK_IPS,BlockIp.Type.区间,ip);
        List<BlockIp> blockIps = (List<BlockIp>) redisUtil.get(cacheKey);
        if(blockIps==null){
            blockIps = blockIpMapper.findByIp2(ip);
            redisUtil.set(cacheKey,blockIps);
        }
        return blockIps;
    }

    //根据地址段查
    public List<BlockIp> findByIp3(String ip) {
        String cacheKey = String.format(ConstantCache.BLOCK_IPS,BlockIp.Type.地址段,ip);
        List<BlockIp> blockIps = (List<BlockIp>) redisUtil.get(cacheKey);
        if(blockIps==null){
            blockIps = blockIpMapper.findByIp3(ip);
            redisUtil.set(cacheKey,blockIps);
        }
        return blockIps;
    }
    //根据国家查
    public BlockIp findByIp4(String ip) {
        String cacheKey = String.format(ConstantCache.BLOCK_IPS,BlockIp.Type.国家,ip);
        BlockIp blockIp = (BlockIp) redisUtil.get(cacheKey);
        if(blockIp==null){
            String country = IPUtil.getCityInfo(ip);
            if(StringUtils.isNotBlank(country)){
                country = country.split("\\|")[0];
            }
            blockIp = blockIpMapper.findByIp4(country);
            redisUtil.set(cacheKey,blockIp);
        }
        return blockIp;
    }

    @Override
    public Result<Object> createOrUpdate(BlockIp blockIp) {
        if(blockIp.getId()==null){
            blockIp.setTsCreate(getTs());
            if(blockIp.getType().equals(BlockIp.Type.单个.toString())){
                blockIp.setInfo(IPUtil.getCityInfo(blockIp.getIp()));
            }else if(blockIp.getType().equals(BlockIp.Type.区间.toString())){
                blockIp.setNum1(IPUtil.ipToLong(blockIp.getIp1()));
                blockIp.setNum2(IPUtil.ipToLong(blockIp.getIp2()));
                blockIp.setIp(blockIp.getIp1()+"~"+blockIp.getIp2());
            }
            if(!save(blockIp)){
                return fail("添加失败");
            }
        }else{
            if(blockIp.getType().equals(BlockIp.Type.单个.toString())){
                blockIp.setInfo(IPUtil.getCityInfo(blockIp.getIp()));
            }else if(blockIp.getType().equals(BlockIp.Type.区间.toString())){
                blockIp.setNum1(IPUtil.ipToLong(blockIp.getIp1()));
                blockIp.setNum2(IPUtil.ipToLong(blockIp.getIp2()));
                blockIp.setIp(blockIp.getIp1()+"~"+blockIp.getIp2());
            }
            if(!updateById(blockIp)){
                return fail("更新失败");
            }
        }
        redisUtil.removeAll(ConstantCache.BLOCK_IPS.replace("%s_%s",""));
        return success();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        blockIpMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        redisUtil.removeAll(ConstantCache.BLOCK_IPS.replace("%s_%s",""));
        return success();
    }
}
