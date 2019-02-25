package org.jeecg.modules.demo.test.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: auto/cgform 
 * @author： scott 
 * @date： 2019-01-30 
 * @version：V1.0
 */
@RestController
@RequestMapping("/auto/cgform")
@Slf4j
public class AutoController {

	@GetMapping(value = "/list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) {
		HttpSession session = request.getSession();
		log.info("  session id = " + session.getId());
		
		modelAndView.setViewName("demo3");
		List<String> userList = new ArrayList<String>();
		userList.add("admin");
		userList.add("user1");
		userList.add("user2");
		log.info("--------------test--------------");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("sessionid", session.getId());
		return modelAndView;
	}

}
