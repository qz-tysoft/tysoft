package com.tysoft.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.UserService;
/**
 * @author 黄雄雄
 * @since 2019年7月26日
 */
@Component
public class UserAssigneeProvider implements AssigneeProvider {
	@Autowired
	UserService userService;
	
	public boolean isTree() {
		return false;
	}

	public String getName() {
		return "指定用户";
	}

	public void queryEntities(PageQuery<Entity> page, String parentId) {
		//HttpServletRequest req = RequestHolder.getThreadLocal();

		int index = page.getPageIndex();
		int size = 10;
		List<Entity> entitys = new ArrayList<Entity>();
		Entity parameter = page.getQueryParameter();
		String id = null;
		if (parameter != null) {
			id = parameter.getId();
		}

		//User userinfo = (User) req.getSession().getAttribute("SYS_USER");
		
		Criteria<User> criteria = new Criteria<User>();
		criteria.add(Restrictions.eq("state",0, true));
		Page<User> userlist = this.userService.queryUserByPage(criteria, null, index-1,size);
		page.setRecordCount(Long.valueOf(userlist.getTotalElements()).intValue());
		page.setPageSize(size);
		for (User user : userlist.getContent()) {
			if (id != null) {
				if (!user.getId().equals(id)) {
					continue;
				}
			}
			String userData="";
			if(user.getUnit()==null) {
				userData=user.getName();
			}else {
				userData=user.getUnit().getUnitName()+"-"+user.getName();	
			}
			Entity entity = new Entity(user.getId(),userData);
			entity.setChosen(true);
			entitys.add(entity);
		}
		page.setResult(entitys);
	}

	public Collection<String> getUsers(String entityId, Context context, ProcessInstance processInstance) {
		
		List<String> users = new ArrayList<String>();
		User userinfo = userService.findUserById(entityId);
		String userData="";
		if(userinfo.getUnit()==null) {
			userData=userinfo.getName();
		}else {
			userData=userinfo.getUnit().getUnitName()+"-"+userinfo.getName();	
		}
		users.add(userData);
		
		return users;
	}

	public boolean disable() {
		return false;
	}

}
