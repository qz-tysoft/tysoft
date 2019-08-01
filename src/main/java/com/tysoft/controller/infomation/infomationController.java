package com.tysoft.controller.infomation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.controller.BaseController;
import com.tysoft.service.infomation.InfomationService;

@Controller
@RequestMapping("/infomationController")
public class infomationController  extends BaseController{
	@Autowired
	private InfomationService infomationService;
    
	private String infomationView = "infomation/infomationView";
  
	/**
    * 进入展示列表
    */
   @RequestMapping("infomationView")
   public String list(HttpServletRequest request) {
       String type=request.getParameter("type");
       request.setAttribute("type", type);
	   return infomationView;
   }
   
   /**
    * 查询列表
    */
   @RequestMapping("query-page")
   @ResponseBody
   public Map<String,Object> queryPage(HttpServletRequest request) {
       String pageNum = request.getParameter("pageNum");
       return null;
   }
   
   /**
    * 保存消息主表
    */
   @RequestMapping("save")
   @ResponseBody
   public String save(HttpServletRequest request) {
	   return Success;
    }
     
   /**
    * 获取消息主表
    */
   @RequestMapping("find")
   @ResponseBody
   public Map<String, Object> find(HttpServletRequest request) {
       String id = request.getParameter("id");
      
       return null;
   }
   
   /**
     * 删除消息主表
    */
   @RequestMapping("delete")
   @ResponseBody
   public String delete(HttpServletRequest request) {
	   return Success;
	}
	
	
}
