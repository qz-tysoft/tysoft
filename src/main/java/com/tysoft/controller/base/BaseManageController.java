package com.tysoft.controller.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.entity.base.Power;
import com.tysoft.service.base.PowerService;
import com.tysoft.service.base.RoleService;
import com.tysoft.service.base.UnitService;
import com.tysoft.service.base.UserService;

import jodd.util.StringUtil;

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
		private String powerLookChild="baseManage/power/power-look-child";
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
	  		tipMsg.put("msg", 1);
	        return   tipMsg;
	  	}
	  	
	    //权限界面
	  	@RequestMapping("powerView")
	  	public String powerView(HttpServletRequest request){
			String view="";
			String powerViewType=request.getParameter("powerViewType");
			//按顺序界面为权限模块界面-权限模块添加界面-子权限添加界面
			if(powerViewType.equals("powerView")) {
				view=powerView;
			}else if(powerViewType.equals("powerAddView")) {
				String pid=request.getParameter("pid");
				request.setAttribute("pid", pid);
				view=powerAdd;
			}else if(powerViewType.equals("powerLookChildView")) {
				String pid=request.getParameter("pid");
				request.setAttribute("pid", pid);
				view=powerLookChild;
			}
	  		return view;
	  	}
	  	
	    //权限模块界面
		@RequestMapping("powerPage")
		@ResponseBody
		public Object powerPage(HttpServletRequest request){
			String page=request.getParameter("page");
			String limit=request.getParameter("limit");
			String pid=request.getParameter("pid");
			String queryPowerName=request.getParameter("powerName");
			Criteria<Power> criteria=new Criteria<>();
	        if(StringUtil.isNotBlank(pid)) {
				criteria.add(Restrictions.eq("pid", pid, false));
			}else {
				criteria.add(Restrictions.eq("pid", "menu", false));
			}
			if(StringUtil.isNotBlank(queryPowerName)) {
			  criteria.add(Restrictions.like("powerName", queryPowerName, false));
			}
			Order order = new Order(Direction.DESC, "id");// 根据id排序
			Sort sort = new Sort(order);
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			Page<Power> pages=this.powerService.queryPowerByPage(criteria, sort, Integer.valueOf(page)-1, Integer.valueOf(limit));
			if (pages.getTotalPages() > 0) {
				Map<String, Object> map = null;
				for (Power power : pages.getContent()) {
					map = new HashMap<String, Object>();
					String id=power.getId();
					String powerName=power.getPowerName();
					String icon=power.getIcon();
					map.put("id", id);
					map.put("powerName", powerName);
					map.put("icon", icon);
					listMap.add(map);
				}
			}
			Map<String, Object> powerMap = new LinkedHashMap<String,Object>();
			powerMap.put("code", 0);
			powerMap.put("msg", "");
			powerMap.put("count",pages.getTotalElements());
			powerMap.put("data", listMap);
			return powerMap;
		}
		
		
		
		//权限增加界面
	  	@RequestMapping("powerAddView")
	  	public String powerAddView(HttpServletRequest request){
	  		return powerAdd;
	  	}
	  	 //人员增加
	  	@RequestMapping("powerAdd")
		@ResponseBody
		public Map<String, Object> powerAdd(HttpServletRequest request){
	  		Map<String,Object> tipMsg=new HashedMap<>();
            String powerName=request.getParameter("powerName");
            Power power=this.powerService.parentPower(powerName);
            if(power!=null) {
            	//该权限模块已存在
            	tipMsg.put("msg", 0);
            }else {
            	Power addPower=new Power();
            	addPower.setPowerName(powerName);
            	addPower.setPid("menu");
            	Power newPower=this.powerService.savePower(addPower);
            	if(newPower!=null) {
            		tipMsg.put("msg", 1);	
            	}
            }
	        return   tipMsg;
	  	}
	  	
}
