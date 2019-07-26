package com.tysoft.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.entity.base.Role;
import com.tysoft.entity.base.User;
import com.tysoft.entity.base.UserRole;
import com.tysoft.repository.base.UserRoleRespository;
import com.tysoft.service.base.RoleService;
import com.tysoft.service.base.UserService;
/**
 * @author 黄雄雄
 * @since 2019年7月26日
 */
@Component
public class RoleAssigneeProvider implements AssigneeProvider {
	@Autowired
	UserService userService;

    @Autowired
    RoleService roleService;
    
	@Autowired
	private UserRoleRespository userRoleRespository;

    
	public boolean isTree() {
		return true;
	}

	public String getName() {
		return "指定角色";
	}

	public void queryEntities(PageQuery<Entity> page, String parentId) {
		HttpServletRequest req = RequestHolder.getThreadLocal();

		int index = page.getPageIndex();
		int size = 10;
		List<Entity> entitys = new ArrayList<Entity>();
		Entity parameter = page.getQueryParameter();
		String id = null;
		if (parameter != null) {
			id = parameter.getId();
		}

        List<Role> allRoleList = roleService.queryAllRole();

		for (Role role : allRoleList) {
			if (id != null) {
				if (!role.getId().equals(id)) {
					continue;
				}
			}
			Entity entity = new Entity(role.getId(), role.getRoleName());
			entity.setChosen(true);
			entitys.add(entity);
		}
		page.setResult(entitys);
	}

	public Collection<String> getUsers(String entityId, Context context, ProcessInstance processInstance) {
		List<String> users = new ArrayList<String>();
		List<String> roleIdList = new ArrayList<>();
        roleIdList.add(entityId);
        HttpServletRequest req = RequestHolder.getThreadLocal();
        List<Role> roles=new ArrayList<>();
        //获取所有角色
        for(int i=0;i<roleIdList.size();i++) {
        	Role role=roleService.findRoleById(roleIdList.get(i));
        	if(role!=null) {
        		roles.add(role);
        	}
        }
		
        //获取所有属于该角色的用户
        List<User> systemUser=new ArrayList<>();
        for(int i=0;i<roles.size();i++) {
    		List<UserRole> userRoles=userRoleRespository.findByroleId(roles.get(i).getId());
            for(int j=0;j<userRoles.size();j++) {
            	UserRole userRole=userRoles.get(j);
            	User user=this.userService.findUserById(userRole.getUserId());
                if(user!=null) {
                	systemUser.add(user);
                }
            }
        }
      //进行数据填充
      		for (User user : systemUser) {
      			if(user.getUnit()==null) {
      				users.add(user.getName());
      			}else {
      				users.add(user.getUnit().getUnitName()+"-"+user.getName());
      			}
      		}
     
		return users;
	}

	public boolean disable() {
		return false;
	}

}