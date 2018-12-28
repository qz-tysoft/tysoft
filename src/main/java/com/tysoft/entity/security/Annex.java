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

package com.tysoft.entity.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import com.tysoft.entity.security.AnnexFolder;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 附件表 
 * 创建日期 2018-3-9 17:23:07
 */
@Entity
@Table(name="SS_annex")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Annex implements Serializable{

    private static final long serialVersionUID = 233931232148002L;

    public  Annex(){
    }
    public  Annex(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 附件名称
     */
    private java.lang.String name;

    /**
     * 附件扩展名
     */
    private java.lang.String extendName;

    /**
     * 相对的路径
     */
    private java.lang.String relativePath;

    /**
     * 文件大小
     */
    private java.math.BigDecimal fileSize;

    /**
     * 上传人
     */
    private java.lang.String creator;

    /**
     * 上传时间
     */
    private java.util.Date uploadTime;

    /**
     * 上下文类型
     */
    private java.lang.String contextType;

    /**
     * 说明
     */
    private java.lang.String remark;

    /**
     * 根目录
     */
    private java.lang.String rootPath;

    /**
     *  附件目录
     */
    private AnnexFolder annexFolder;
    
    /**
     * 是否更新
     */
    private Boolean isUpdate;


    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    /**
     *@return:java.lang.String id
     */
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
     *@return:java.lang.String 附件名称
     */
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 附件名称
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.String 附件扩展名
     */
    public java.lang.String getExtendName(){
      return this.extendName;
    }
    /**
     *@param:java.lang.String 附件扩展名
     */
    public void setExtendName(java.lang.String extendName){ 
      this.extendName=extendName;
    }

    /**
     *@return:java.lang.String 相对的路径
     */
    public java.lang.String getRelativePath(){
      return this.relativePath;
    }
    /**
     *@param:java.lang.String 相对的路径
     */
    public void setRelativePath(java.lang.String relativePath){ 
      this.relativePath=relativePath;
    }

    /**
     *@return:java.math.BigDecimal 文件大小
     */
    public java.math.BigDecimal getFileSize(){
      return this.fileSize;
    }
    /**
     *@param:java.math.BigDecimal 文件大小
     */
    public void setFileSize(java.math.BigDecimal fileSize){ 
      this.fileSize=fileSize;
    }

    /**
     *@return:java.lang.String 上传人
     */
    public java.lang.String getCreator(){
      return this.creator;
    }
    /**
     *@param:java.lang.String 上传人
     */
    public void setCreator(java.lang.String creator){ 
      this.creator=creator;
    }

    /**
     *@return:java.util.Date 上传时间
     */
    public java.util.Date getUploadTime(){
      return this.uploadTime;
    }
    /**
     *@param:java.util.Date 上传时间
     */
    public void setUploadTime(java.util.Date uploadTime){ 
      this.uploadTime=uploadTime;
    }

    /**
     *@return:java.lang.String 上下文类型
     */
    public java.lang.String getContextType(){
      return this.contextType;
    }
    /**
     *@param:java.lang.String 上下文类型
     */
    public void setContextType(java.lang.String contextType){ 
      this.contextType=contextType;
    }

    /**
     *@return:java.lang.String 说明
     */
    public java.lang.String getRemark(){
      return this.remark;
    }
    /**
     *@param:java.lang.String 说明
     */
    public void setRemark(java.lang.String remark){ 
      this.remark=remark;
    }

    /**
     *@return:java.lang.String 根目录
     */
    public java.lang.String getRootPath(){
      return this.rootPath;
    }
    /**
     *@param:java.lang.String 根目录
     */
    public void setRootPath(java.lang.String rootPath){ 
      this.rootPath=rootPath;
    }

    @ManyToOne
    @JoinColumn(name="annex_folder_id",nullable = true)
    public AnnexFolder getAnnexFolder() {
       return annexFolder;
    }
    public void setAnnexFolder(AnnexFolder annexFolder) {
       this.annexFolder = annexFolder;
    }

    public Boolean getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public Annex poToVo() {
    Annex vo = new Annex();
       vo.setId(this.id);
       vo.setName(this.name);
       vo.setExtendName(this.extendName);
       vo.setRelativePath(this.relativePath);
       vo.setFileSize(this.fileSize);
       vo.setCreator(this.creator);
       vo.setUploadTime(this.uploadTime);
       vo.setContextType(this.contextType);
       vo.setRemark(this.remark);
       vo.setRootPath(this.rootPath);
       vo.setAnnexFolder(this.annexFolder!=null?this.annexFolder.poToVo():null);
       return vo;
    }
    
    public static List<Annex> poToVos(List<Annex> annexes){
    	List<Annex> vos = null;
    	if(annexes!=null) {
    		vos = new ArrayList<Annex>();
    		for(Annex annex : annexes) {
    			vos.add(annex.poToVo());
    		}
    	}
    	return vos;
    }
}
