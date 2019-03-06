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
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 权限表 
 * 创建日期 2019-3-6 15:11:13
 */
@Entity
@Table(name="bs_power")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Power implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  Power(){
    }
    public  Power(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 权限名称
     */
    private java.lang.String powerName;

    /**
     * 地址
     */
    private java.lang.String url;

    /**
     * 图标名称
     */
    private java.lang.String icon;


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
     *@return:java.lang.String 权限名称
     */
    @Column(length=500)
    public java.lang.String getPowerName(){
      return this.powerName;
    }
    /**
     *@param:java.lang.String 权限名称
     */
    public void setPowerName(java.lang.String powerName){ 
      this.powerName=powerName;
    }

    /**
     *@return:java.lang.String 地址
     */
    @Column(length=500)
    public java.lang.String getUrl(){
      return this.url;
    }
    /**
     *@param:java.lang.String 地址
     */
    public void setUrl(java.lang.String url){ 
      this.url=url;
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


    /**PoToVo*/
    public Power poToVo() {
        Power vo = new Power();
        vo.setId(this.id);
        vo.setPowerName(this.powerName);
        vo.setUrl(this.url);
        vo.setIcon(this.icon);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"powerName\":\"").append(this.getPowerName()).append("\"");
        sb.append(",");
        sb.append("\"url\":\"").append(this.getUrl()).append("\"");
        sb.append(",");
        sb.append("\"icon\":\"").append(this.getIcon()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("powerName",this.powerName);
        jsonMap.put("url",this.url);
        jsonMap.put("icon",this.icon);
        return jsonMap;
    }
}