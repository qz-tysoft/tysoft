package com.tysoft.entity.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ss_system_user_role")
public class UserRole {
	@Id
	@Column(name = "user_id", nullable = false, length = 36)
	private String  userId;
	@Column(name = "role_id", nullable = false, length = 36)
	private String  roleId;
	
	public UserRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRole(String userId, String roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	
}
