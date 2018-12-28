/**
* <p>Description: SEGS SEGS</p>
*
* <p>Copyright: Copyright (c) 2018</p>
*
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :Administrator
* @version 1.0
*/

package com.tysoft.entity.security;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户信息表 
 * 创建日期 2018-4-12 15:33:20
 */
@Entity
@Table(name="SS_user_info")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 167806838301729L;

    public  UserInfo(){
    }
    public  UserInfo(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 姓名
     */
    private java.lang.String name;

    /**
     * 身份证号
     */
    private java.lang.String idNumber;

    /**
     * 性别
     */
    private java.lang.Short sex;

    /**
     * 居住证号
     */
    private java.lang.String liveNumber;

    /**
     * 民族
     */
    private java.lang.String ethnic;

    /**
     * 联系电话
     */
    private java.lang.String telphone;

    /**
     * 婚姻状况
     */
    private java.lang.String marriage;

    /**
     * 教育水平
     */
    private java.lang.String eduLevel;

    /**
     * 户籍地址
     */
    private java.lang.String resAddress;

    /**
     * 家庭联系地址
     */
    private java.lang.String familyAddress;

    /**
     * 照片
     */
    private byte[] photo;

    private Boolean isUpdate;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
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
     *@return:java.lang.String 姓名
     */
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 姓名
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.String 身份证号
     */
    public java.lang.String getIdNumber(){
      return this.idNumber;
    }
    /**
     *@param:java.lang.String 身份证号
     */
    public void setIdNumber(java.lang.String idNumber){ 
      this.idNumber=idNumber;
    }

    /**
     *@return:java.lang.Short 性别
     */
    public java.lang.Short getSex(){
      return this.sex;
    }
    /**
     *@param:java.lang.Short 性别
     */
    public void setSex(java.lang.Short sex){ 
      this.sex=sex;
    }

    /**
     *@return:java.lang.String 居住证号
     */
    public java.lang.String getLiveNumber(){
      return this.liveNumber;
    }
    /**
     *@param:java.lang.String 居住证号
     */
    public void setLiveNumber(java.lang.String liveNumber){ 
      this.liveNumber=liveNumber;
    }

    /**
     *@return:java.lang.String 民族
     */
    public java.lang.String getEthnic(){
      return this.ethnic;
    }
    /**
     *@param:java.lang.String 民族
     */
    public void setEthnic(java.lang.String ethnic){ 
      this.ethnic=ethnic;
    }

    /**
     *@return:java.lang.String 联系电话
     */
    public java.lang.String getTelphone(){
      return this.telphone;
    }
    /**
     *@param:java.lang.String 联系电话
     */
    public void setTelphone(java.lang.String telphone){ 
      this.telphone=telphone;
    }

    /**
     *@return:java.lang.String 婚姻状况
     */
    public java.lang.String getMarriage(){
      return this.marriage;
    }
    /**
     *@param:java.lang.String 婚姻状况
     */
    public void setMarriage(java.lang.String marriage){ 
      this.marriage=marriage;
    }

    /**
     *@return:java.lang.String 教育水平
     */
    public java.lang.String getEduLevel(){
      return this.eduLevel;
    }
    /**
     *@param:java.lang.String 教育水平
     */
    public void setEduLevel(java.lang.String eduLevel){ 
      this.eduLevel=eduLevel;
    }

    /**
     *@return:java.lang.String 户籍地址
     */
    public java.lang.String getResAddress(){
      return this.resAddress;
    }
    /**
     *@param:java.lang.String 户籍地址
     */
    public void setResAddress(java.lang.String resAddress){ 
      this.resAddress=resAddress;
    }

    /**
     *@return:java.lang.String 家庭联系地址
     */
    public java.lang.String getFamilyAddress(){
      return this.familyAddress;
    }
    /**
     *@param:java.lang.String 家庭联系地址
     */
    public void setFamilyAddress(java.lang.String familyAddress){ 
      this.familyAddress=familyAddress;
    }

    /**
     *@return:java.sql.Blob 照片
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "photo", columnDefinition = "LONGBLOB",nullable=true)
    public byte[] getPhoto(){
      return this.photo;
    }
    /**
     *@param:java.sql.Blob 照片
     */
    public void setPhoto(byte[] photo){
      this.photo=photo;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean update) {
        isUpdate = update;
    }

    public UserInfo poToVo() {
    	UserInfo vo = new UserInfo();
       vo.setId(this.id);
       vo.setName(this.name);
       vo.setIdNumber(this.idNumber);
       vo.setSex(this.sex);
       vo.setLiveNumber(this.liveNumber);
       vo.setEthnic(this.ethnic);
       vo.setTelphone(this.telphone);
       vo.setMarriage(this.marriage);
       vo.setEduLevel(this.eduLevel);
       vo.setResAddress(this.resAddress);
       vo.setFamilyAddress(this.familyAddress);
       vo.setPhoto(this.photo);
       return vo;
    }
}
