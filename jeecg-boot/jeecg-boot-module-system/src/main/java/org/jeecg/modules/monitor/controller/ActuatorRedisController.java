package org.jeecg.modules.monitor.controller;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.monitor.domain.RedisInfo;
import org.jeecg.modules.monitor.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sys/actuator/redis")
public class ActuatorRedisController {

    @Autowired
    private RedisService redisService;

    /**
     * Redis详细信息
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    public Result<?> getRedisInfo() throws Exception {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        log.info(infoList.toString());
        return Result.ok(infoList);
    }

    @GetMapping("/keysSize")
    public Map<String, Object> getKeysSize() throws Exception {
        return redisService.getKeysSize();
    }

    /**
     * 获取redis key数量 for 报表
     * @return
     * @throws Exception
     */
    @GetMapping("/keysSizeForReport")
    public Map<String, JSONArray> getKeysSizeReport() throws Exception {
		return redisService.getMapForReport("1");
    }
    /**
     * 获取redis 内存 for 报表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/memoryForReport")
    public Map<String, JSONArray> memoryForReport() throws Exception {
		return redisService.getMapForReport("2");
    }
    /**
     * 获取redis 全部信息 for 报表
     * @return
     * @throws Exception
     */
    @GetMapping("/infoForReport")
    public Map<String, JSONArray> infoForReport() throws Exception {
		return redisService.getMapForReport("3");
    }

    @GetMapping("/memoryInfo")
    public Map<String, Object> getMemoryInfo() throws Exception {
        return redisService.getMemoryInfo();
    }
    
  //update-begin--Author:zhangweijian  Date:20190425 for：获取磁盘信息
  	/**
  	 * @功能：获取磁盘信息
  	 * @param request
  	 * @param response
  	 * @return
  	 */
  	@GetMapping("/queryDiskInfo")
  	public Result<List<Map<String,Object>>> queryDiskInfo(HttpServletRequest request, HttpServletResponse response){
  		Result<List<Map<String,Object>>> res = new Result<>();
  		try {
  			// 当前文件系统类
  	        FileSystemView fsv = FileSystemView.getFileSystemView();
  	        // 列出所有windows 磁盘
  	        File[] fs = File.listRoots();
  	        log.info("查询磁盘信息:"+fs.length+"个");
  	        List<Map<String,Object>> list = new ArrayList<>();
  	        
  	        for (int i = 0; i < fs.length; i++) {
  	        	if(fs[i].getTotalSpace()==0) {
  	        		continue;
  	        	}
  	        	Map<String,Object> map = new HashMap<>();
  	        	map.put("name", fsv.getSystemDisplayName(fs[i]));
  	        	map.put("max", fs[i].getTotalSpace());
  	        	map.put("rest", fs[i].getFreeSpace());
  	        	map.put("restPPT", (fs[i].getTotalSpace()-fs[i].getFreeSpace())*100/fs[i].getTotalSpace());
  	        	list.add(map);
  	        	log.info(map.toString());
  	        }
  	        res.setResult(list);
  	        res.success("查询成功");
  		} catch (Exception e) {
  			res.error500("查询失败"+e.getMessage());
  		}
  		return res;
  	}
  	//update-end--Author:zhangweijian  Date:20190425 for：获取磁盘信息
}
