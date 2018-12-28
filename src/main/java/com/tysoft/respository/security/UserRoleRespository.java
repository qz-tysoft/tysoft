package com.tysoft.respository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.tysoft.entity.security.UserRole;
/**
 * 
 * @author Administrator
 *用户角色表
 */
@Repository
public interface UserRoleRespository extends JpaRepository<UserRole,String>, JpaSpecificationExecutor<UserRole> {
	List<UserRole> findByroleId(String roleId);
	UserRole findByUserId(String userId);
}
