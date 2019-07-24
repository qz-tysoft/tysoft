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
import com.tysoft.entity.base.Annex;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 接收的附件表 接收的附件表
 * 创建日期 2019-7-22 8:57:00
 */
@Entity
@Table(name="im_receive_annex")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReceiveAnnex implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  ReceiveAnnex(){
    }
    public  ReceiveAnnex(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     *  附件表
     */
    private Annex annex;


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

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="annex_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_annex_receive_annex"))
    public Annex getAnnex() {
       return annex;
    }
    public void setAnnex(Annex annex) {
       this.annex = annex;
    }

    /**PoToVo*/
    public ReceiveAnnex poToVo() {
        ReceiveAnnex vo = new ReceiveAnnex();
        vo.setId(this.id);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("annex", this.annex==null?null:this.annex.poToMap());
        return jsonMap;
    }
}