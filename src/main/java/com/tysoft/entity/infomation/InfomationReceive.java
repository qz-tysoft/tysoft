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
import com.tysoft.entity.infomation.Infomation;
import com.tysoft.entity.base.User;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 消息人员接收表 消息人员接收表
 * 创建日期 2019-7-22 8:57:00
 */
@Entity
@Table(name="im_infomation_receive")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfomationReceive implements Serializable{

    private static final long serialVersionUID = 167806838301L;

    public  InfomationReceive(){
    }
    public  InfomationReceive(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 阅读时间
     */
    private java.util.Date readTime;

    /**
     * 是否阅读
     */
    private java.lang.Integer isRead;

    /**
     *  消息主表
     */
    private Infomation infomation;

    /**
     *  用户表
     */
    private User user;

    /**
     *  用户表
     */
    private User sendUser;


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
     *@return:java.util.Date 阅读时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public java.util.Date getReadTime(){
      return this.readTime;
    }
    /**
     *@param:java.util.Date 阅读时间
     */
    public void setReadTime(java.util.Date readTime){ 
      this.readTime=readTime;
    }

    /**
     *@return:java.lang.Integer 是否阅读
     */
    public java.lang.Integer getIsRead(){
      return this.isRead;
    }
    /**
     *@param:java.lang.Integer 是否阅读
     */
    public void setIsRead(java.lang.Integer isRead){ 
      this.isRead=isRead;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="infomation_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_infomation_infomation_receive"))
    public Infomation getInfomation() {
       return infomation;
    }
    public void setInfomation(Infomation infomation) {
       this.infomation = infomation;
    }
    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="user_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_user_infomation_receive"))
    public User getUser() {
       return user;
    }
    public void setUser(User user) {
       this.user = user;
    }
    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="send_user_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_send_user_infomation_receive"))
    public User getSendUser() {
       return sendUser;
    }
    public void setSendUser(User sendUser) {
       this.sendUser = sendUser;
    }

    /**PoToVo*/
    public InfomationReceive poToVo() {
        InfomationReceive vo = new InfomationReceive();
        vo.setId(this.id);
        vo.setReadTime(this.readTime);
        vo.setIsRead(this.isRead);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"readTime\":\"").append(this.getReadTime() == null ? null : sdf.format(this.getReadTime())).append("\"");
        sb.append(",");
        sb.append("\"isRead\":\"").append(this.getIsRead()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("readTime",this.readTime == null ? null : sdf.format(this.readTime));
        jsonMap.put("isRead",this.isRead);
        jsonMap.put("infomation", this.infomation==null?null:this.infomation.poToMap());
        jsonMap.put("user", this.user==null?null:this.user.poToMap());
        jsonMap.put("sendUser", this.sendUser==null?null:this.sendUser.poToMap());
        return jsonMap;
    }
}