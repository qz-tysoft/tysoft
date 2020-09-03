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
 * 单位表 单位表

 * 创建日期 2019-3-6 15:11:13
 */
@Entity
@Table(name="bs_unit")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit implements Serializable{

    private static final long serialVersionUID = 233931232148L;

    public  Unit(){
    }
    public  Unit(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 单位名
     */
    private java.lang.String unitName;

    /**
     * 单位人数
     */
    private java.lang.Integer unitNum;


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
     *@return:java.lang.String 单位名
     */
    @Column(length=500)
    public java.lang.String getUnitName(){
      return this.unitName;
    }
    /**
     *@param:java.lang.String 单位名
     */
    public void setUnitName(java.lang.String unitName){ 
      this.unitName=unitName;
    }

    /**
     *@return:java.lang.Integer 单位人数
     */
    public java.lang.Integer getUnitNum(){
      return this.unitNum;
    }
    /**
     *@param:java.lang.Integer 单位人数
     */
    public void setUnitNum(java.lang.Integer unitNum){ 
      this.unitNum=unitNum;
    }


    /**PoToVo*/
    public Unit poToVo() {
        Unit vo = new Unit();
        vo.setId(this.id);
        vo.setUnitName(this.unitName);
        vo.setUnitNum(this.unitNum);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"unitName\":\"").append(this.getUnitName()).append("\"");
        sb.append(",");
        sb.append("\"unitNum\":\"").append(this.getUnitNum()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("unitName",this.unitName);
        jsonMap.put("unitNum",this.unitNum);
        return jsonMap;
    }
}