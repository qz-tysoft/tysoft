/**
* <p>Description: SEGS_COM SEGS_COM</p>
*
* <p>Copyright: Copyright (c) 2019</p>
*
* <p>Company: 厦门路桥信息股份有限公司</p>
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
import com.tysoft.entity.base.Power;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 菜单表 
 * 创建日期 2019-4-25 9:16:22
 */
@Entity
@Table(name="bs_menu")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  Menu(){
    }
    public  Menu(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 菜单名称
     */
    private java.lang.String menuName;

    /**
     * 图标名称
     */
    private java.lang.String icon;
    /**
     * pid
     */
    private java.lang.String pid;

    /**
     *  权限表
     */
    private Power power;


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
     *@return:java.lang.String 菜单名称
     */
    @Column(length=100)
    public java.lang.String getMenuName(){
      return this.menuName;
    }
    /**
     *@param:java.lang.String 菜单名称
     */
    public void setMenuName(java.lang.String menuName){ 
      this.menuName=menuName;
    }
    

    /**
     *@return:java.lang.String 图标名称
     */
    @Column(length=100)
    public java.lang.String getIcon(){
      return this.icon;
    }
    /**
     *@param:java.lang.String 图标名称
     */
    public void setIcon(java.lang.String icon){ 
      this.icon=icon;
    }

    /**
     *@return:java.lang.String pid
     */
    @Column(length=100)
    public java.lang.String getPid(){
      return this.pid;
    }
    /**
     *@param:java.lang.String pid
     */
    public void setPid(java.lang.String pid){ 
      this.pid=pid;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="power_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_power_menu"))
    public Power getPower() {
       return power;
    }
    public void setPower(Power power) {
       this.power = power;
    }

    /**PoToVo*/
    public Menu poToVo() {
        Menu vo = new Menu();
        vo.setId(this.id);
        vo.setMenuName(this.menuName);
        vo.setPid(this.pid);
        vo.setIcon(this.icon);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"menuName\":\"").append(this.getMenuName()).append("\"");
        sb.append(",");
        sb.append("\"pid\":\"").append(this.getPid()).append("\"");
        sb.append("\"icon\":\"").append(this.getIcon()).append("\"");
        sb.append(",");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("menuName",this.menuName);
        jsonMap.put("pid",this.pid);
        jsonMap.put("icon",this.icon);
        jsonMap.put("power", this.power==null?null:this.power.poToMap());
        return jsonMap;
    }
}