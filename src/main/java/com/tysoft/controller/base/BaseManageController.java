package com.tysoft.controller.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.service.base.PowerService;
import com.tysoft.service.base.RoleService;
import com.tysoft.service.base.UnitService;
import com.tysoft.service.base.UserService;

@Controller
@RequestMapping("/baseManage")
public class BaseManageController {
	//自动装载相关服务
		@Autowired
		protected PowerService powerService;
		@Autowired
		protected  RoleService roleService;
		@Autowired
		protected  UnitService unitService;
		@Autowired
		protected  UserService userService;
		private String userView="baseManage/user/userView"; 
		private String userAdd="baseManage/user/user-add"; 
		private String unitView="baseManage/unit/unitView";
		private String unitAdd="baseManage/unit/unit-add"; 
		private String powerView="baseManage/power/powerView";
		private String powerAdd="baseManage/power/power-add"; 
		private String roleView="baseManage/role/roleView";
		private String roleAdd="baseManage/power/role-add"; 
		   
	    //人员管理界面
	  	@RequestMapping("userView")
	  	public String userView(HttpServletRequest request){
	  		return userView;
	  	}
	  	
	 	//新增人员界面
	  	@RequestMapping("userAddView")
		public String companyAddView(HttpServletRequest request){
	  		return userAdd;
	  	}
	  	
	  	
	    //人员增加
	  	@RequestMapping("userAdd")
		@ResponseBody
		public Map<String, Object> companyAdd(HttpServletRequest request){
	  		Map<String,Object> tipMsg=new HashedMap<>();
	  		String companyName=request.getParameter("companyName");
	  		String loginName=request.getParameter("loginName");
	  		tipMsg.put("msg", 1);
	  		String loginPsw=request.getParameter("loginPsw");
	        return   tipMsg;
	  	}
	  	
	    //权限界面
	  	@RequestMapping("powerView")
	  	public String powerView(HttpServletRequest request){
	  		return powerView;
	  	}
	  	
	  	
	  	 //权限增加界面
	  	@RequestMapping("powerAddView")
	  	public String powerAddView(HttpServletRequest request){
	  		return userAdd;
	  	}
	  	 //人员增加
	  	@RequestMapping("powerAdd")
		@ResponseBody
		public Map<String, Object> powerAdd(HttpServletRequest request){
	  		Map<String,Object> tipMsg=new HashedMap<>();
	  		String companyName=request.getParameter("companyName");
	  		String loginName=request.getParameter("loginName");
	  		tipMsg.put("msg", 1);
	  		String loginPsw=request.getParameter("loginPsw");
	        return   tipMsg;
	  	}
	  	
	  	
}
