package com.tysoft.controller.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.controller.BaseController;
import com.tysoft.service.base.*;


@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController{
	//自动装载相关服务
	@Autowired
	protected  CompanyPowerService companyPowerService;
	@Autowired
	protected  CompanyService companyService;
	@Autowired
	protected PowerService powerService;
	@Autowired
	protected  RoleService roleService;
	@Autowired
	protected  UnitService unitService;
	@Autowired
	protected  UserService userService;
	
    private String companyView="user/administrators/list"; 
    
    //公司主界面
  	@RequestMapping("companyView")
  	public String companyView(HttpServletRequest request){
  		return companyView;
  	}
	
  	//新增公司
  	@RequestMapping("addCompany")
	@ResponseBody
	public Map<String, Object> addCompany(HttpServletRequest request){
  		Map<String, Object> tipMap=new HashMap<>();
  		
  		return tipMap;
  	}
}
