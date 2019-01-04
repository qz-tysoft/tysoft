package com.tysoft.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private String companyView="user/administrators/list"; 
    
    //公司主界面
  	@RequestMapping("companyView")
  	public String companyView(HttpServletRequest request){
  		return companyView;
  	}
	
	
}
