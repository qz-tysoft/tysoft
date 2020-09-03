/**
* <p>Description: SEGS_COM SEGS_COM</p>
*
* <p>Copyright: Copyright (c) 2019</p>
*
* <p>Company: tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.entity.base;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import com.tysoft.entity.base.Unit;
import java.util.List;
import java.util.ArrayList;
import com.tysoft.entity.base.Role;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;

/**
 * 用户表 
 * 创建日期 2019-3-7 9:17:52
 */
@Entity
@Table(name="bs_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable{

    private static final long serialVersionUID = 10663550610L;

    public  User(){
    }
    public  User(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 名字
     */
    private java.lang.String name;

    /**
     * 账号
     */
    private java.lang.String loginName;

    /**
     * 密码
     */
    private java.lang.String loginPsw;

    /**
     * 人员状态
     */
    private java.lang.Integer state;

    /**
     * 电话
     */
    private java.lang.String phone;

    /**
     * 使用单位 单位表
     */
    private Unit unit;

    /**
     *  角色表
     */
    private List<Role> roles = new ArrayList<Role>();

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    /**
     *@return:java.lang.String id
     */
    @Column(length=100)
    public java.lang.String getId(){
      return this.id;
    }
    /**
     *@param:java.lang.String id
     */
    public void setId(java.lang.String id){ 
      this.id=id;
    }

    /**
     *@return:java.lang.String 名字
     */
    @Column(length=100)
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 名字
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.String 账号
     */
    @Column(length=500)
    public java.lang.String getLoginName(){
      return this.loginName;
    }
    /**
     *@param:java.lang.String 账号
     */
    public void setLoginName(java.lang.String loginName){ 
      this.loginName=loginName;
    }

    /**
     *@return:java.lang.String 密码
     */
    @Column(length=500)
    public java.lang.String getLoginPsw(){
      return this.loginPsw;
    }
    /**
     *@param:java.lang.String 密码
     */
    public void setLoginPsw(java.lang.String loginPsw){ 
      this.loginPsw=loginPsw;
    }

    /**
     *@return:java.lang.Integer 人员状态
     */
    public java.lang.Integer getState(){
      return this.state;
    }
    /**
     *@param:java.lang.Integer 人员状态
     */
    public void setState(java.lang.Integer state){ 
      this.state=state;
    }

    /**
     *@return:java.lang.Integer 电话
     */
    public java.lang.String getPhone(){
      return this.phone;
    }
    /**
     *@param:java.lang.Integer 电话
     */
    public void setPhone(java.lang.String phone){ 
      this.phone=phone;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="unit_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_unit_user"))
    public Unit getUnit() {
       return unit;
    }
    public void setUnit(Unit unit) {
       this.unit = unit;
    }
    @ManyToMany 
    @JoinTable(name = "ss_user_role",  joinColumns = { @JoinColumn(name = "user_id") },  inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Role> getRoles() {
       return roles;
    }
    public void setRoles(List<Role> roles) {
       this.roles = roles;
    }

    /**PoToVo*/
    public User poToVo() {
        User vo = new User();
        vo.setId(this.id);
        vo.setName(this.name);
        vo.setLoginName(this.loginName);
        vo.setLoginPsw(this.loginPsw);
        vo.setState(this.state);
        vo.setPhone(this.phone);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"name\":\"").append(this.getName()).append("\"");
        sb.append(",");
        sb.append("\"loginName\":\"").append(this.getLoginName()).append("\"");
        sb.append(",");
        sb.append("\"loginPsw\":\"").append(this.getLoginPsw()).append("\"");
        sb.append(",");
        sb.append("\"state\":\"").append(this.getState()).append("\"");
        sb.append(",");
        sb.append("\"phone\":\"").append(this.getPhone()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("name",this.name);
        jsonMap.put("loginName",this.loginName);
        jsonMap.put("loginPsw",this.loginPsw);
        jsonMap.put("state",this.state);
        jsonMap.put("phone",this.phone);
        jsonMap.put("unit", this.unit==null?null:this.unit.poToMap());
        return jsonMap;
    }
}