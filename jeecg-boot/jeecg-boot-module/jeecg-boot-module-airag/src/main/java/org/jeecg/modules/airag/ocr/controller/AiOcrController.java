package org.jeecg.modules.airag.ocr.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.airag.ocr.entity.AiOcr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/airag/ocr")
public class AiOcrController  {

    @Autowired
    private RedisUtil redisUtil;
    
    private static final String AI_OCR_REDIS_KEY = "airag:ocr"; 
    
    @GetMapping("/list")
    public Result<?> list(){
        Object aiOcr = redisUtil.get(AI_OCR_REDIS_KEY);
        IPage<AiOcr> page = new Page<>(1,10);
        if(null != aiOcr){
            List<AiOcr> aiOcrList = JSONObject.parseArray(aiOcr.toString(), AiOcr.class);
            page.setRecords(aiOcrList);
            page.setTotal(aiOcrList.size());
            page.setPages(aiOcrList.size());
        }
        return Result.OK(page);
    }
    
    @PostMapping("/add")
    public Result<String> add(@RequestBody AiOcr aiOcr){
        Object aiOcrList = redisUtil.get(AI_OCR_REDIS_KEY);
        aiOcr.setId(UUID.randomUUID().toString().replace("-",""));
        if(null == aiOcrList){
            List<AiOcr> list = new ArrayList<>();
            list.add(aiOcr);
            redisUtil.set(AI_OCR_REDIS_KEY, JSONObject.toJSONString(list));
        }else{
            List<AiOcr> aiOcrs = JSONObject.parseArray(aiOcrList.toString(), AiOcr.class);
            aiOcrs.add(aiOcr);
            redisUtil.set(AI_OCR_REDIS_KEY,JSONObject.toJSONString(aiOcrs));
        }
        return Result.OK("添加成功");
    }

    @PutMapping("/edit")
    public Result<String> updateById(@RequestBody AiOcr aiOcr){
        Object aiOcrList = redisUtil.get(AI_OCR_REDIS_KEY);
        if(null != aiOcrList){
            List<AiOcr> aiOcrs = JSONObject.parseArray(aiOcrList.toString(), AiOcr.class);
            aiOcrs.forEach(item->{
                if(item.getId().equals(aiOcr.getId())){
                    BeanUtils.copyProperties(aiOcr,item);
                }
            });
            redisUtil.set(AI_OCR_REDIS_KEY,JSONObject.toJSONString(aiOcrs));
        }else{
            return Result.OK("编辑失败，未找到该数据");
        }
        return Result.OK("编辑成功");
    }

    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestBody AiOcr aiOcr){
        Object aiOcrObj = redisUtil.get(AI_OCR_REDIS_KEY);
        if(null != aiOcrObj){
            List<AiOcr> aiOcrs = JSONObject.parseArray(aiOcrObj.toString(), AiOcr.class);
            List<AiOcr> aiOcrList = new ArrayList<>();
            for(AiOcr ocr: aiOcrs){
                if(!ocr.getId().equals(aiOcr.getId())){
                    aiOcrList.add(ocr);
                }
            }
            if(CollectionUtils.isNotEmpty(aiOcrList)){
                redisUtil.set(AI_OCR_REDIS_KEY,JSONObject.toJSONString(aiOcrList));
            }else{
                redisUtil.removeAll(AI_OCR_REDIS_KEY);
            }
        }else{
            return Result.OK("删除失败，未找到该数据");
        }
        return Result.OK("删除成功");
    }
}
