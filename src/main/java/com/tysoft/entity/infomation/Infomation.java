/**
* <p>Description: TYOA TYOA</p>
*
* <p>Copyright: Copyright (c) 2019</p>
*
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :admin
* @version 1.0
*/

package com.tysoft.entity.infomation;

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
import com.tysoft.entity.base.User;
import com.tysoft.entity.infomation.ReceiveAnnex;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 消息主表 消息主表
 * 创建日期 2019-7-22 8:57:00
 */
@Entity
@Table(name="im_infomation")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Infomation implements Serializable{

    private static final long serialVersionUID = 233931232148L;

    public  Infomation(){
    }
    public  Infomation(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 消息类型
     */
    private java.lang.Integer type;

    /**
     * 消息等级
     */
    private java.lang.Integer grade;

    /**
     * 消息标题
     */
    private java.lang.String title;

    /**
     * 消息内容
     */
    private java.lang.String content;

    /**
     * 创建时间
     */
    private java.util.Date creatTime;

    /**
     * 发送时间
     */
    private java.util.Date sendTime;

    /**
     *  用户表
     */
    private User user;

    /**
     *  接收的附件表
     */
    private ReceiveAnnex receiveAnnex;


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
     *@return:java.lang.Integer 消息类型
     */
    public java.lang.Integer getType(){
      return this.type;
    }
    /**
     *@param:java.lang.Integer 消息类型
     */
    public void setType(java.lang.Integer type){ 
      this.type=type;
    }

    /**
     *@return:java.lang.Integer 消息等级
     */
    public java.lang.Integer getGrade(){
      return this.grade;
    }
    /**
     *@param:java.lang.Integer 消息等级
     */
    public void setGrade(java.lang.Integer grade){ 
      this.grade=grade;
    }

    /**
     *@return:java.lang.String 消息标题
     */
    @Column(length=1024)
    public java.lang.String getTitle(){
      return this.title;
    }
    /**
     *@param:java.lang.String 消息标题
     */
    public void setTitle(java.lang.String title){ 
      this.title=title;
    }

    /**
     *@return:java.lang.String 消息内容
     */
    @Column(length=0)
    public java.lang.String getContent(){
      return this.content;
    }
    /**
     *@param:java.lang.String 消息内容
     */
    public void setContent(java.lang.String content){ 
      this.content=content;
    }

    /**
     *@return:java.util.Date 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public java.util.Date getCreatTime(){
      return this.creatTime;
    }
    /**
     *@param:java.util.Date 创建时间
     */
    public void setCreatTime(java.util.Date creatTime){ 
      this.creatTime=creatTime;
    }

    /**
     *@return:java.util.Date 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public java.util.Date getSendTime(){
      return this.sendTime;
    }
    /**
     *@param:java.util.Date 发送时间
     */
    public void setSendTime(java.util.Date sendTime){ 
      this.sendTime=sendTime;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="user_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_user_infomation"))
    public User getUser() {
       return user;
    }
    public void setUser(User user) {
       this.user = user;
    }
    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="receive_annex_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_receive_annex_infomation"))
    public ReceiveAnnex getReceiveAnnex() {
       return receiveAnnex;
    }
    public void setReceiveAnnex(ReceiveAnnex receiveAnnex) {
       this.receiveAnnex = receiveAnnex;
    }

    /**PoToVo*/
    public Infomation poToVo() {
        Infomation vo = new Infomation();
        vo.setId(this.id);
        vo.setType(this.type);
        vo.setGrade(this.grade);
        vo.setTitle(this.title);
        vo.setContent(this.content);
        vo.setCreatTime(this.creatTime);
        vo.setSendTime(this.sendTime);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"type\":\"").append(this.getType()).append("\"");
        sb.append(",");
        sb.append("\"grade\":\"").append(this.getGrade()).append("\"");
        sb.append(",");
        sb.append("\"title\":\"").append(this.getTitle()).append("\"");
        sb.append(",");
        sb.append("\"content\":\"").append(this.getContent()).append("\"");
        sb.append(",");
        sb.append("\"creatTime\":\"").append(this.getCreatTime() == null ? null : sdf.format(this.getCreatTime())).append("\"");
        sb.append(",");
        sb.append("\"sendTime\":\"").append(this.getSendTime() == null ? null : sdf.format(this.getSendTime())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("type",this.type);
        jsonMap.put("grade",this.grade);
        jsonMap.put("title",this.title);
        jsonMap.put("content",this.content);
        jsonMap.put("creatTime",this.creatTime == null ? null : sdf.format(this.creatTime));
        jsonMap.put("sendTime",this.sendTime == null ? null : sdf.format(this.sendTime));
        jsonMap.put("user", this.user==null?null:this.user.poToMap());
        jsonMap.put("receiveAnnex", this.receiveAnnex==null?null:this.receiveAnnex.poToMap());
        return jsonMap;
    }
}