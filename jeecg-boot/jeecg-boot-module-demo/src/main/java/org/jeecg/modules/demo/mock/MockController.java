package org.jeecg.modules.demo.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class MockController {

	private final String JSON_PATH = "classpath:org/jeecg/modules/demo/mock/json";
	
	/**
	 * 通用json访问接口
	 * 格式： http://localhost:8080/jeecg-boot/api/json/{filename}
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/json/{filename}", method = RequestMethod.GET)
	public String getJsonData(@PathVariable String filename) {
		String jsonpath = "classpath:org/jeecg/modules/demo/mock/json/"+filename+".json";
		return readJson(jsonpath);
	}
	
	@GetMapping(value = "/asynTreeList")
	public String asynTreeList(String id) {
		return readJson(JSON_PATH + "/asyn_tree_list_" + id + ".json");
	}
	
	@GetMapping(value = "/user")
	public String user() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/user.json");
	}
	
	/**
	 * 老的登录获取用户信息接口
	 * @return
	 */
	@GetMapping(value = "/user/info")
	public String userInfo() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/user_info.json");
	}

	@GetMapping(value = "/role")
	public String role() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/role.json");
	}

	@GetMapping(value = "/service")
	public String service() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/service.json");
	}

	@GetMapping(value = "/permission")
	public String permission() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/permission.json");
	}

	@GetMapping(value = "/permission/no-pager")
	public String permission_no_page() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/permission_no_page.json");
	}
	
	/**
	 * 省市县
	 */
	@GetMapping(value = "/area")
	public String area() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/area.json");
	}
	
	/**
	  * 测试报表数据
	 */
	@GetMapping(value = "/report/getYearCountInfo")
	public String getYearCountInfo() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/getCntrNoCountInfo.json");
	}
	@GetMapping(value = "/report/getMonthCountInfo")
	public String getMonthCountInfo() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/getCntrNoCountInfo.json");
	}
	@GetMapping(value = "/report/getCntrNoCountInfo")
	public String getCntrNoCountInfo() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/getCntrNoCountInfo.json");
	}
	@GetMapping(value = "/report/getCabinetCountInfo")
	public String getCabinetCountInfo() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/getCntrNoCountInfo.json");
	}
	
	/**
	   * 实时磁盘监控
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
	        	map.put("restPPT", fs[i].getFreeSpace()*100/fs[i].getTotalSpace());
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
	
	//-------------------------------------------------------------------------------------------
	/**
	 * 工作台首页的数据
	 * @return
	 */
	@GetMapping(value = "/list/search/projects")
	public String projects() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/workplace_projects.json");
	}

	@GetMapping(value = "/workplace/activity")
	public String activity() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/workplace_activity.json");
	}
	
	@GetMapping(value = "/workplace/teams")
	public String teams() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/workplace_teams.json");
	}
	
	@GetMapping(value = "/workplace/radar")
	public String radar() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/workplace_radar.json");
	}
	
	@GetMapping(value = "/task/process")
	public String taskProcess() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/task_process.json");
	}
	//-------------------------------------------------------------------------------------------
	
	//author:lvdandan-----date：20190315---for:添加数据日志json----
	public String sysDataLogJson() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/sysdatalog.json");
	}
	//author:lvdandan-----date：20190315---for:添加数据日志json----
	
	/**
	 * 读取json格式文件
	 * @param jsonSrc
	 * @return
	 */
	private String readJson(String jsonSrc) {
		String json = "";
		try {
			//File jsonFile = ResourceUtils.getFile(jsonSrc);
			//json = FileUtils.re.readFileToString(jsonFile);
			//换个写法，解决springboot读取jar包中文件的问题
			InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonSrc.replace("classpath:", ""));
			json = IOUtils.toString(stream,"UTF-8");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return json;
	}

}
