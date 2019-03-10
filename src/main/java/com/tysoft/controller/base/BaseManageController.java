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

import com.google.gson.JsonObject;
import com.tysoft.common.Criteria;
import com.tysoft.common.JsonUtils;
import com.tysoft.common.MD5Util;
import com.tysoft.common.Restrictions;
import com.tysoft.controller.BaseController;
import com.tysoft.entity.base.Power;
import com.tysoft.entity.base.Unit;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.PowerService;
import com.tysoft.service.base.RoleService;
import com.tysoft.service.base.UnitService;
import com.tysoft.service.base.UserService;

import jodd.util.StringUtil;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/baseManage")
public class BaseManageController extends BaseController{
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
		private String powerView="baseManage/power/powerView";
		private String powerAdd="baseManage/power/power-add";
		private String powerEdit="baseManage/power/power-edit"; 
		private String powerLookChild="baseManage/power/power-look-child";
		private String unitView="baseManage/unit/unitView";
		private String unitAdd="baseManage/unit/unit-add"; 
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
			}else if(powerViewType.equals("powerEdit")) {
				String id=request.getParameter("id");
				Power  power=this.powerService.findPowerById(id);
				request.setAttribute("power",power);
				view=powerEdit;
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
					map.put("url", power.getUrl());
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
	  	
	  	//权限增加
	  	@RequestMapping("powerAdd")
		@ResponseBody
		public Map<String, Object> powerAdd(HttpServletRequest request){
	  		Map<String,Object> tipMsg=new HashedMap<>();
	  		//权限json字符串
	  		String childPower=request.getParameter("childPower");
            String powerName=request.getParameter("powerName");
            if(StringUtil.isNotBlank(childPower)) {
             //获得对象
    	  	 JSONObject obj=JSONObject.fromObject(childPower);
    	  	 Power power=new Power();
    	  	 power.setPid((String)obj.get("pid"));
    	  	 power.setPowerName((String)obj.get("powerName"));
    	  	 power.setUrl((String)obj.get("url"));
    	  	 Power savePower=this.powerService.savePower(power);
    	  	 if(savePower!=null) {
    	  		tipMsg.put("msg",1);
    	  	 }
            }else {
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
            }
           
	        return   tipMsg;
	  	}
	  	
	  	//权限修改
		@RequestMapping("powerEdit")
		@ResponseBody
	  	public Map<String, Object> powerEdit(HttpServletRequest request){
	  		Map<String,Object> tipMsg=new HashedMap<>();
	  		String editPower=request.getParameter("editPower");
	  		JSONObject obj=JSONObject.fromObject(editPower);
	  		Power power=(Power)JSONObject.toBean(obj, Power.class);
	  		this.powerService.savePower(power);
	  		return tipMsg;
	  	}
	  	
	  	//权限删除
	  	@RequestMapping("powerDel")
		@ResponseBody
	  	public Map<String,Object> powerDel(HttpServletRequest request){
	  	  Map<String,Object> resultMap=new HashedMap<>();
	  	  String id=request.getParameter("id");
	  	  Power power=this.powerService.findPowerById(id);
	  	  //删除子权限
	  	  if(!power.getPid().equals("menu")) {
	  		  this.powerService.deletePowerByIds(id);
	  	  }else {
	  	  //批量删除
	  		  //主权限id
	  		  String pid=power.getId();
	  		  Criteria<Power> criteria=new Criteria<>();
	  		  criteria.add(Restrictions.eq("pid", pid, false));	
	  		  List<Power> powerList=this.powerService.queryPowerByCondition(criteria, null);
	  		  powerList.add(power);
	  		  this.powerService.batchDeletPower(powerList);
	  	  }
	  	  
	  	  return resultMap;
	  	}

        //验证删除密码
	  	@RequestMapping("validatePsw")
		@ResponseBody
		public Map<String,Object> validatePsw(HttpServletRequest request) throws Exception{
	  		 Map<String,Object> resultMap=new HashedMap<>();
	  		 String psw=request.getParameter("psw");
	  		 User user=this.getCurrentSystemUser(request);
	  		 if(MD5Util.encode(psw).equals(user.getLoginPsw())) {
	  			resultMap.put("msg", 0);
	  		 }else {
	  			resultMap.put("msg", 1);
	  		 }
	  		 return resultMap;
	  	}

	  	 //单位界面
	  	@RequestMapping("unitView")
	  	public String unitView(HttpServletRequest request){
	  		String view="";
	  		String unitViewType=request.getParameter("unitViewType");
	  		if(unitViewType.equals("unitView")) {
	  			view=unitView;
	  		}else if(unitViewType.equals("unitAdd")) {
	  			view=unitAdd;
	  		}
	  		return view;
	  	}

	  	
	  	
	    //添加单位
	  	@RequestMapping("unitAdd")
		@ResponseBody
		public Map<String,Object> unitAdd(HttpServletRequest request) throws Exception{
	  		 Map<String,Object> resultMap=new HashedMap<>();
	  		 String unitName=request.getParameter("unitName");
             Unit unit=this.unitService.findUnitByName(unitName);
             if(unit!=null) {
    	  		 resultMap.put("msg", 0);
             }else {
 	  			 Unit newUnit=new Unit();
 	  			 newUnit.setUnitName(unitName);
 	  			 newUnit.setUnitNum(0);
 	  			 this.unitService.saveUnit(newUnit);
            	 resultMap.put("msg", 1);
             }
	  		 return resultMap;
	  	}
}
