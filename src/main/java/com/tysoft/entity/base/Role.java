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
import java.util.List;
import java.util.ArrayList;
import com.tysoft.entity.base.Power;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;

/**
 * 角色表 
 * 创建日期 2019-3-6 15:11:13
 */
@Entity
@Table(name="bs_role")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable{

    private static final long serialVersionUID = 167806838301L;

    public  Role(){
    }
    public  Role(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 角色名
     */
    private java.lang.String roleName;

    /**
     *  权限表
     */
    private List<Power> powers = new ArrayList<Power>();

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
     *@return:java.lang.String 角色名
     */
    @Column(length=500)
    public java.lang.String getRoleName(){
      return this.roleName;
    }
    /**
     *@param:java.lang.String 角色名
     */
    public void setRoleName(java.lang.String roleName){ 
      this.roleName=roleName;
    }

    @ManyToMany 
    @JoinTable(name = "ss_role_power",  joinColumns = { @JoinColumn(name = "role_id") },  inverseJoinColumns = { @JoinColumn(name = "power_id") })
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Power> getPowers() {
       return powers;
    }
    public void setPowers(List<Power> powers) {
       this.powers = powers;
    }

    /**PoToVo*/
    public Role poToVo() {
        Role vo = new Role();
        vo.setId(this.id);
        vo.setRoleName(this.roleName);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"roleName\":\"").append(this.getRoleName()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("roleName",this.roleName);
        return jsonMap;
    }
}