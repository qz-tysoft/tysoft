package com.tysoft.controller.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.tysoft.common.Criteria;
import com.tysoft.common.JsonUtils;
import com.tysoft.common.MD5Util;
import com.tysoft.common.Restrictions;
import com.tysoft.controller.BaseController;
import com.tysoft.entity.base.Power;
import com.tysoft.entity.base.Role;
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
		private String roleAddView="baseManage/role/roleAddView";
		private String userUnitSet="baseManage/user/user-unit-set";
		private String userPowerSet="baseManage/user/user-power-set";
	  	
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
	  		}else if(unitViewType.equals("unitEdit")) {
	  			String id=request.getParameter("id");
	  			Unit unit=this.unitService.findUnitById(id);
	  			request.setAttribute("unit",unit);
	  			view=unitAdd;
	  		}else if(unitViewType.equals("unitAddPeople")) {
	  		    String unitId=request.getParameter("unitId");
	  		    request.setAttribute("unitId",unitId);
	  		    view=userView;
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
	  	
	    //单位分页
	  	@RequestMapping("unitPage")
		@ResponseBody
		public Object unitPage(HttpServletRequest request) {
			String page=request.getParameter("page");
			String limit=request.getParameter("limit");
			String unitName=request.getParameter("unitName");
			String type=request.getParameter("type");
			Criteria<Unit> criteria=new Criteria<>();
	    
			if(StringUtil.isNotBlank(unitName)) {
			  criteria.add(Restrictions.like("unitName", unitName, false));
			}
			Order order = new Order(Direction.DESC, "id");// 根据id排序
			Sort sort = new Sort(order);
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			Page<Unit> pages=this.unitService.queryUnitByPage(criteria, sort, Integer.valueOf(page)-1, Integer.valueOf(limit));
			if (pages.getTotalPages() > 0) {
				Map<String, Object> map = null;
				Unit firstUnit=null;
				for (Unit unit : pages.getContent()) {
					map = new HashMap<String, Object>();
					String id=unit.getId();
					String name=unit.getUnitName();
					Integer unitNum=unit.getUnitNum();
					map.put("id", id);
					map.put("unitName", name);
					map.put("unitNum",unitNum);
					if(name.equals(this.firstUnit)) {
						if(!StringUtil.isNotBlank(type)) {
							listMap.add(0,map);
						}
						
				    }else {
				    	listMap.add(map);
				    }
				}
			}
			Map<String, Object> unitMap = new LinkedHashMap<String,Object>();
			unitMap.put("code", 0);
			unitMap.put("msg", "");
			unitMap.put("count",pages.getTotalElements());
			unitMap.put("data", listMap);
			return unitMap;
	  	}
	  	
	    //编辑单位
	  	@RequestMapping("unitEdit")
		@ResponseBody
		public String unitEdit(HttpServletRequest request) throws IllegalAccessException {
	  		String editUnit=request.getParameter("editUnit");
	  		Unit unit=(Unit)this.StringtoBean(editUnit,Unit.class);
	  		this.unitService.saveUnit(unit);
	  		return this.Success;
	  	}
	  	
	  	//解散单位
	  	@RequestMapping("unitRelease")
		@ResponseBody
		public String unitRelease(HttpServletRequest request) {
	  		String id=request.getParameter("id");
	  		Unit unit=this.unitService.findUnitById(id);
	  		int unitNum=unit.getUnitNum();
	  		if(unitNum==0) {
	  			this.unitService.deleteUnitByIds(id);	
	  		}else {
	  			System.out.println("当前单位存在人员");
	  		}
	  		return Success;
	  	}
		
	  	//添加单位-人员
	  	@RequestMapping("peopleAdd")
		@ResponseBody
		public String peopleAdd(HttpServletRequest request) {
	  		
	  		
	  		return null;
	  	}
	  	
		@RequestMapping("userPage")
		@ResponseBody
		public Object peoplePage(HttpServletRequest request) {
			Map<String, Object> resultMap = new LinkedHashMap<String,Object>();
			String unitId=request.getParameter("unitId");
			String page=request.getParameter("page");
			String limit=request.getParameter("limit");
			String name=request.getParameter("name");
			String state=request.getParameter("state");
			Criteria<User> criteria=new Criteria<>();
			criteria.add(Restrictions.eq("unit",this.unitService.findUnitById(unitId), false));
		    Unit unit=this.unitService.findUnitById(unitId);
		    String unitName=unit.getUnitName();
		    //默认不是未分配人员
		    int unitNameFlag=0;
		    if(unitName.equals(firstUnit)) {
		    	unitNameFlag=1;
		    }
			
			if(StringUtil.isNotBlank(name)) {
			  criteria.add(Restrictions.like("name", name, false));
			}
			
			if(StringUtil.isNotBlank(state)) {
				  criteria.add(Restrictions.eq("state", Integer.valueOf(state), false));
			}
			Order order = new Order(Direction.DESC, "id");// 根据id排序
			Sort sort = new Sort(order);
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			Page<User> pages=this.userService.queryUserByPage(criteria, sort, Integer.valueOf(page)-1, Integer.valueOf(limit));
			if (pages.getTotalPages() > 0) {
				Map<String, Object> map = null;
				for (User user : pages.getContent()) {
					map = new HashMap<String, Object>();
					String id=user.getId();
					String userName=user.getName();
					String phone=user.getPhone();
					map.put("id", id);
					map.put("state",user.getState());
					map.put("userName", userName);
					map.put("phone",phone);
					map.put("unitNameFlag",unitNameFlag);
				    listMap.add(map);
				}
			}
			Map<String, Object> peopleMap = new LinkedHashMap<String,Object>();
			peopleMap.put("code", 0);
			peopleMap.put("msg", "");
			peopleMap.put("count",pages.getTotalElements());
			peopleMap.put("data", listMap);
			return peopleMap;
		}
		
		 //人员管理界面
	  	@RequestMapping("userView")
	  	public String userView(HttpServletRequest request){
	  		return userView;
	  	}
	  	
	 	//新增人员界面
	  	@RequestMapping("userAddView")
		public String userAddView(HttpServletRequest request){
	  		String unitId=request.getParameter("unitId");
	  		String id=request.getParameter("id");
	  		User user=null;
	  		if(StringUtil.isNotBlank(id)) {
	  			user=this.userService.findUserById(id);
	  		}else {
	  			user=new User();
	  		}
	  		request.setAttribute("user",user);
	  		request.setAttribute("unitId",unitId);
	  		return userAdd;
	  	}
	  	
	    //人员增加
	  	@RequestMapping(value="userAdd",method=RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> userAdd(HttpServletRequest request,User user){
	  		String unitId=request.getParameter("unitId");
	  		String editFlag=request.getParameter("editFlag");
	  		Map<String,Object> tipMsg=new HashedMap<>();
	  		//编辑标记
	  		Unit unit=this.unitService.findUnitById(unitId);
	  		if(StringUtil.isNotBlank(editFlag)) {
	  		String beforeLoginName=request.getParameter("beforeLoginName");
	  		String nowLonginName=user.getLoginName();
	  		//编辑
	  		if(beforeLoginName.equals(nowLonginName)) {
	  			user.setUnit(unit);
	  			tipMsg.put("msg",1);
	  			this.userService.saveUser(user);
	  		}else {
	  			User isUser=this.userService.findIsUser(user);
	  			if(isUser!=null) {
		  			tipMsg.put("msg", 0);
		  		}else {
		  			user.setUnit(unit);
		  			tipMsg.put("msg",1);
		  			this.userService.saveUser(user);
		  		}
	  		}
	  		}else {
		  		user.setState(0);
		  		user.setLoginPsw(MD5Util.encode(this.initPsw));
		  		user.setUnit(unit);
		  		User isUser=this.userService.findIsUser(user);
		  		if(isUser!=null) {
		  			tipMsg.put("msg", 0);
		  		}else {
		  			int unitNum=unit.getUnitNum();
		  			unit.setUnitNum(unitNum+1);
		  			this.unitService.saveUnit(unit);
		  			tipMsg.put("msg",1);
		  			this.userService.saveUser(user);
		  		}
	  		}
	  		
	        return   tipMsg;
	  	}
	  	
		//删除单位-人员
	  	@RequestMapping("userDelet")
		@ResponseBody
		public String peopleDelet(HttpServletRequest request) {
	  		String id=request.getParameter("id");
	  		User user=this.userService.findUserById(id);
	  		Unit unit=user.getUnit();
	  		unit.setUnitNum(unit.getUnitNum()-1);
	  		this.unitService.saveUnit(unit);
	  		this.userService.deleteUserByIds(id);
	  		return Success;
	  	}
	  	
	    //人员状态
	  	@RequestMapping("userStateOpen")
		@ResponseBody
		public String userStateOpen(HttpServletRequest request) {
	  		String id=request.getParameter("id");
	  		User user=this.userService.findUserById(id);
	  		int chang=0;
	  		int state=user.getState();
	  		if(state==0) {
	  			chang=1;//禁用
	  		}else {
	  			chang=0;//启用
	  		}
	  		user.setState(chang);
	  		this.userService.saveUser(user);
	  		return Success;
	  	}
	  	
	  	//用户批量删除
	  	@RequestMapping("userBatchDelet")
		@ResponseBody
		public String userBatchDelet(HttpServletRequest request) {
	  		String ids=request.getParameter("ids");
	  		String idsArr[]=ids.split(",");
	  		User user=this.userService.findUserById(idsArr[0]);
	  		Unit unit=user.getUnit();
	  		unit.setUnitNum(unit.getUnitNum()-idsArr.length);
	  		this.unitService.saveUnit(unit);
	  		for(int i=0;i<idsArr.length;i++) {
	  		this.userService.deleteUserByIds(idsArr[i]);
	  		}
	  	    return Success;
	  	}
	  	
	  	//用户分配部门界面
	  	@RequestMapping("userUnitSetView")
	  	public String userUnitPowerSet(HttpServletRequest request) {
            String userId=request.getParameter("id");
            request.setAttribute("userId", userId);
	  		return userUnitSet;
	  	}
	  	
	  	
	     //分配功能
	  	@RequestMapping("userUnitSet")
	  	@ResponseBody
	  	public String userUnitSet(HttpServletRequest request) {
	  		String unitId=request.getParameter("unitId");
	  		String userId=request.getParameter("userId");
	  		User user=this.userService.findUserById(userId);
	  		Unit unit=this.unitService.findUnitById(unitId);
	  		//未分配人员单位减1
	  		Unit beforeUnit=user.getUnit();
	  		beforeUnit.setUnitNum(beforeUnit.getUnitNum()-1);
	  		this.unitService.saveUnit(beforeUnit);
	  		unit.setUnitNum(unit.getUnitNum()+1);
	  		Unit useUnit=this.unitService.saveUnit(unit);
	  		user.setUnit(useUnit);
	  		this.userService.saveUser(user);
	  		return Success;
	  	}
	  	//分配角色界面
		@RequestMapping("roleView")
		public String roleView(HttpServletRequest request) {
			String view="";
	  		String roleViewType=request.getParameter("roleViewType");
	  		if(roleViewType.equals("roleView")) {
	  			view=roleView;	
	  		}else if(roleViewType.equals("roleAddView")) {
	  			view=roleAddView;
	  		}else if(roleViewType.equals("roleEditView")) {
	  			String id=request.getParameter("id");
	  			Role role=this.roleService.findRoleById(id);
	  			view=roleAddView;
	  			request.setAttribute("role",role);
	  		}
	  		return view;
	  	}
		
		@RequestMapping("rolePage")
		@ResponseBody
		public Object rolePage(HttpServletRequest request) {
			String page=request.getParameter("page");
			String limit=request.getParameter("limit");
			String queryRoleName=request.getParameter("roleName");
			Criteria<Role> criteria=new Criteria<>();
			if(StringUtil.isNotBlank(queryRoleName)) {
			  criteria.add(Restrictions.like("roleName", queryRoleName, false));
			}
			Order order = new Order(Direction.DESC, "id");// 根据id排序
			Sort sort = new Sort(order);
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			Page<Role> pages=this.roleService.queryRoleByPage(criteria, sort, Integer.valueOf(page)-1, Integer.valueOf(limit));
			if (pages.getTotalPages() > 0) {
				Map<String, Object> map = null;
				for (Role role : pages.getContent()) {
					map = new HashMap<String, Object>();
					String id=role.getId();
					String roleName=role.getRoleName();
					map.put("id", id);
					map.put("roleName", roleName);
					listMap.add(map);
				}
			}
			Map<String, Object> roleMap = new LinkedHashMap<String,Object>();
			roleMap.put("code", 0);
			roleMap.put("msg", "");
			roleMap.put("count",pages.getTotalElements());
			roleMap.put("data", listMap);
			return roleMap;
		}
		
		//创建角色 
	 	@RequestMapping("creatRole")
	  	@ResponseBody
	  	public Map<String, Object> creatRole(HttpServletRequest request) {
	  		Map<String,Object> tipMsg=new HashedMap<>();
	 		String roleName=request.getParameter("roleName");
	 		Role role=this.roleService.isExistRole(roleName);
 	 		//当前没有此角色
	 		if(role==null) {
 	 			//创建角色
	 			Role newRole=new Role();
	 			newRole.setRoleName(roleName);
	 			tipMsg.put("msg", 0);
	 			this.roleService.saveRole(newRole);
 	 		}else {
 	 			tipMsg.put("msg", 1);
 	 		}
	 		return tipMsg;
	 	}
	 	//删除角色
		@RequestMapping("deletRole")
	  	@ResponseBody
	  	public String deletRole(HttpServletRequest request) {
	 		String id=request.getParameter("id");
	 		//进行角色删除
	 		this.roleService.deleteRoleByIds(id);
			return Success;
	 	}
		
		
		
		//编辑角色
		@RequestMapping("editRole")
	  	@ResponseBody
	  	public String editRole(HttpServletRequest request) {
	 		String editRole=request.getParameter("editRole");
	 		JSONObject obj=JSONObject.fromObject(editRole);
	  		Role role=(Role)JSONObject.toBean(obj, Role.class);
	  	    //进行保存
	  		this.roleService.saveRole(role);	
			return Success;
	 	}
		
		//角色-权限
		 @RequestMapping("rolePower")
	     @ResponseBody
		 public String rolePower(HttpServletRequest request) {
			 return Success;
		 }
	  	
}
