

package com.tysoft.controller.companyreport;

import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.tysoft.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tysoft.entity.companyreport.CustomerCompany;
import com.tysoft.service.companyreport.CustomerCompanyService;


/**
 * 客户公司设置表管理
 */
@Controller
@RequestMapping("/customer-company")
public class CustomerCompanyController extends BaseController {
	@Autowired
	private CustomerCompanyService customerCompanyService;


   /**
    * 进入展示列表
    */
   @RequestMapping("list")
   public String list(HttpServletRequest request,Model model) {
       System.out.println("1111111");
       return "companyreport/customer-company-project";
   }
   
   /**
    * 查询列表
    */
   @RequestMapping("query-page")
   @ResponseBody
   public Map<String,Object> queryPage(HttpServletRequest request) {

       return null;
   }
   
   /**
    * 保存客户公司设置表
    */
   @RequestMapping("save")
   @ResponseBody
   public String save(CustomerCompany customerCompany, HttpServletRequest request) {
		  String id = request.getParameter("id");
		  if(StringUtils.isBlank(id)) {
		  }
		  CustomerCompany newCustomerCompany = customerCompanyService.saveCustomerCompany(customerCompany);
		  request.setAttribute("customerCompany", customerCompany);
		  request.setAttribute("id", id);
		  if(newCustomerCompany != null){
              return "{\"success\":true,\"msg\":\"保存成功!\"}";
          }else{
              return "{\"success\":false,\"msg\":\"保存失败!\"}";
          }

    }
     
   /**
    * 获取客户公司设置表
    */
   @RequestMapping("find")
   @ResponseBody
   public Map<String, Object> find(HttpServletRequest request) {
       String id = request.getParameter("id");
       Map<String, Object> resultMap = new HashMap<String,Object>();
       if(StringUtils.isNotBlank(id)){
           CustomerCompany customerCompany = this.customerCompanyService.findCustomerCompanyById(id);
           resultMap.put("customerCompany",customerCompany.poToMap());
       }
       return resultMap;
   }
   
   /**
     * 删除客户公司设置表
    */
   @RequestMapping("delete")
   @ResponseBody
   public String delete(HttpServletRequest request) {
	    String ids = request.getParameter("ids");
	    try{
	        this.customerCompanyService.deleteCustomerCompanyByIds(ids);
	    }catch(Exception e){
		    return "{\"success\":false,\"msg\":\"删除失败!\"}";
	    }
	    return "{\"success\":true,\"msg\":\"删除成功!\"}";
	}
}