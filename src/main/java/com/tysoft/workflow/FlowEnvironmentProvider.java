package com.tysoft.workflow;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bstek.uflo.env.EnvironmentProvider;
import com.tysoft.entity.base.Unit;
import com.tysoft.entity.base.User;

public class FlowEnvironmentProvider implements EnvironmentProvider {
	private SessionFactory sessionFactory;
	private PlatformTransactionManager platformTransactionManager;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public PlatformTransactionManager getPlatformTransactionManager() {
		return platformTransactionManager;
	}

	public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
		this.platformTransactionManager = platformTransactionManager;
	}

	public String getCategoryId() {
		return null;
	}

	public String getLoginUser() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//HttpServletRequest req=RequestHolder.getThreadLocal();
		String username="";
		Unit unit = (Unit)request.getSession().getAttribute("SYS_UNIT");
		User user = (User)request.getSession().getAttribute("SYS_USER");
		if(user!=null) {
			if(unit==null) {
				username=user.getName();
			}else {
				username=unit.getUnitName()+"-"+user.getName();
			}
		}
		System.out.println("用户:"+username);
		return username;
	}
}