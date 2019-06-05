package com.tysoft.controller.login;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.common.JsonUtils;
import com.tysoft.common.MD5Util;
import com.tysoft.controller.BaseController;
import com.tysoft.entity.base.Menu;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.MenuService;
import com.tysoft.service.base.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
    
	//后台主界面
	private String mainView="index";
	//404界面
	private String noFindView="template/tips/404";
	//500界面
	private String errorView="template/tips/error";
	
	@Autowired
	UserService userService;
	
	@Autowired
    MenuService menuService;
	
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
			   List<Menu> haveMenuList =this.menuService.findUserAllMenu(user,1); 			    
			   List<Menu> allMenu=this.menuService.allMenuByChildMenu(haveMenuList);
			   List<Menu> firstMenuList=(List<Menu>) this.userService.findUserMenu(user);
			   request.getSession().setAttribute("firstMenuList", firstMenuList);
			   request.getSession().setAttribute("allMenu", allMenu);
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
	
	//查询子菜单
	@RequestMapping("findChildMenu")
	@ResponseBody
	public Map<String, Object> findChildMenu(HttpServletRequest request) throws Exception{
		Map<String,Object> tipMsg=new HashedMap<>();   
		List<Menu> firstMenuList=(List<Menu>)request.getSession().getAttribute("firstMenuList");
		List<Menu> allMenu=(List<Menu>)request.getSession().getAttribute("allMenu");
		tipMsg.put("firstMenuList",firstMenuList);
		tipMsg.put("allMenu",allMenu);
		return tipMsg;
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
