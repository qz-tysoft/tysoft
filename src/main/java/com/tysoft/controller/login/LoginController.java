package com.tysoft.controller.login;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.common.JsonUtils;
import com.tysoft.common.MD5Util;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/login")
public class LoginController {
    
	//后台主界面
	private String mainView="index";
	//404界面
	private String noFindView="template/tips/404";
	//500界面
	private String errorView="template/tips/error";
	
	@Autowired
	UserService userService;
	
	@RequestMapping("validate")
	@ResponseBody
	public Map<String, Object> isLoginMsg(HttpServletRequest request){
		Map<String,Object> tipMsg=new HashedMap<>();
		String loginName=request.getParameter("userName");
		String loginPwd=request.getParameter("password");
		User user=this.userService.findUser(loginName, loginPwd);
		if(user!=null) {
			if(user.getState()==0) {
				tipMsg.put("msg", 0);
				//加入session
				request.getSession().setAttribute("SYS_USER", user);
			}else if(user.getState()==1) {
				//用户被禁用
				tipMsg.put("msg", 1);
			}
		}else {
			tipMsg.put("msg", 2);
		}
		return tipMsg;
	}
	
	//主界面
	@RequestMapping("mainView")
	public String mainView(HttpServletRequest request){
		User user=(User) request.getSession().getAttribute("SYS_USER");
		request.setAttribute("user", user);
		return mainView;
	}
	
	//查询所有用户
	@RequestMapping("findAllUser")
	@ResponseBody
	public Object findAllUser(HttpServletRequest request){
		Map<String, Object> resultMap = new LinkedHashMap<String,Object>();
		List<User> users=this.userService.queryAllUser();
		List<Object> userJson=new ArrayList<>();
		if(users.size()>0) {
			for(int i=0;i<users.size();i++) {
				userJson.add(JsonUtils.objectToJson(users.get(i)));
			}
		}
		resultMap.put("code", 0);
		resultMap.put("msg", "");
		resultMap.put("count", users.size());
		resultMap.put("data", users);
		return resultMap;
	}
	
	//404界面
	@RequestMapping("noFindView")
	public String noFindView(HttpServletRequest request){
		return noFindView;
	}
	
	//错误界面
	@RequestMapping("errorView")
	public String errorPage(HttpServletRequest request){
			return errorView;
	}
}
