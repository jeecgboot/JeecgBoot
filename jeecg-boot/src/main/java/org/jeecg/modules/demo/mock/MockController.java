package org.jeecg.modules.demo.mock;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MockController {

	@GetMapping(value = "/user")
	public String user() {
		return readJson("classpath:org/jeecg/modules/demo/mock/json/user.json");
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
	//-------------------------------------------------------------------------------------------
	
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
			json = IOUtils.toString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
