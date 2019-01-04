package com.tysoft.controller.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
    
	private String mainView="index";
	
	
	@RequestMapping("validate")
	@ResponseBody
	public Map<String, Object> isLoginMsg(HttpServletRequest request){
		Map<String,Object> tipMsg=new HashedMap<>();
		String loginName=request.getParameter("userName");
		String loginPwd=request.getParameter("password");
		if(loginName.equals(loginPwd)) {
			tipMsg.put("msg", 0);
		}
		return tipMsg;
	}
	
	
	@RequestMapping("mainView")
	public String mainView(HttpServletRequest request){
		return mainView;
	}
}
