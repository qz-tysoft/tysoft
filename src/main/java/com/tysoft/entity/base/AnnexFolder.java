/**
* Description: SEGS SEGS
*
* Copyright: Copyright (c) 2018
*
* Company: 厦门路桥信息股份有限公司
*
* @author :Administrator
* @version 1.0
*/

package com.tysoft.entity.base;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 附件目录 
 * 创建日期 2018-3-9 17:23:07
 */
@Entity
@Table(name="SS_annex_folder")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnnexFolder implements Serializable{

    private static final long serialVersionUID = 167806838301729L;
    
    public static final Integer TYPE_BUSINESS_REPORT = 11;		//经营快报附件类型
    
    public static final Integer TYPE_PROJECT = 1;		//项目信息附件类型
    
    public static final Integer TYPE_PROJECT_STATUS = 2;//项目状态附件类型
    
    public static final Integer TYPE_PROJECT_TEAM = 3;	//项目班组附件类型
    
    public static final Integer TYPE_PERSON_ADMIN = 4;	//现场管理人员附件类型
    
    public static final Integer TYPE_PERSON_WORKER = 5;	//务工人员附件类型
    
    public static final Integer TYPE_ATTE_INFO = 6;		//考勤信息附件类型
    
    public static final Integer TYPE_PERSON_IN = 7;		//人员入场附件类型
    
    public static final Integer TYPE_PERSON_OUT = 8;	//人员出场附件类型
    
    public static final Integer TYPE_PROJECT_GROUP = 9;		//项目组附件类型
    
    public static final Integer TYPE_PROJECT_GROUP_STATUS = 10;	//项目组状态附件类型

    public static final Integer TYPE_WORK_TABLE = 13;	//工作用表附件类型

    public static final Integer TYPE_INTERNAL_CONTROL = 14;	//内控管理附件类型

    public  AnnexFolder(){
    }
    public  AnnexFolder(Integer id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.Integer id;

    /**
     * 编号
     */
    private java.lang.String no;

    /**
     * 材料目录名称
     */
    private java.lang.String folderName;

    /**
     * 材料说明
     */
    private java.lang.String remark;

    /**
     * 材料类型
     */
    private java.lang.Integer type;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    /**
     *@return:java.lang.Integer id
     */
    public java.lang.Integer getId(){
      return this.id;
    }
    /**
     *@param:java.lang.Integer id
     */
    public void setId(java.lang.Integer id){ 
      this.id=id;
    }

    /**
     *@return:java.lang.String 编号
     */
    public java.lang.String getNo(){
      return this.no;
    }
    /**
     *@param:java.lang.String 编号
     */
    public void setNo(java.lang.String no){ 
      this.no=no;
    }

    /**
     *@return:java.lang.String 材料目录名称
     */
    public java.lang.String getFolderName(){
      return this.folderName;
    }
    /**
     *@param:java.lang.String 材料目录名称
     */
    public void setFolderName(java.lang.String folderName){ 
      this.folderName=folderName;
    }

    /**
     *@return:java.lang.String 材料说明
     */
    public java.lang.String getRemark(){
      return this.remark;
    }
    /**
     *@param:java.lang.String 材料说明
     */
    public void setRemark(java.lang.String remark){ 
      this.remark=remark;
    }

    /**
     *@return:java.lang.Integer 材料类型
     */
    public java.lang.Integer getType(){
      return this.type;
    }
    /**
     *@param:java.lang.Integer 材料类型
     */
    public void setType(java.lang.Integer type){ 
      this.type=type;
    }

    public AnnexFolder poToVo() {
    AnnexFolder vo = new AnnexFolder();
       vo.setId(this.id);
       vo.setNo(this.no);
       vo.setFolderName(this.folderName);
       vo.setRemark(this.remark);
       vo.setType(this.type);
       return vo;
    }
}
