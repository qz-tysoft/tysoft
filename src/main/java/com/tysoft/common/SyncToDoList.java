package com.tysoft.common;

import java.sql.Date;

/**
 *	OA待办中间类 
 */
public class SyncToDoList {
	
	/**
     * 待办
     */
    public static final String TYPE_INWORK = "0";
    /**
     * 已办
     */
    public static final String TYPE_FINISH = "1";
    
	/**
     * 主键
     */
	private String synctodoid;

	/**
	 * 系统编码 
	 */
    private String systemcode;

    /**
	 * 流程步骤Id 
	 */
    private String todoitemid;

    /**
	 * 流程编号 
	 */
    private Integer appcode;

    /**
	 * 流程名称 
	 */
    private String appname;

    /**
	 * 流程标题
	 */
    private String procname;

    /**
	 * 流程实例编号
	 */
    private String procid;

    /**
	 * 流程任务编号
	 */
    private Integer taskid;

    /**
	 * 当前步骤处理人ID
	 */
    private String partid;

    /**
	 * 当前步骤处理人名字
	 */
    private String partname;

    /**
	 * 当前步骤发送人ID
	 */
    private String passerid;

    /**
	 * 当前步骤发送人名字
	 */
    private String passername;

    /**
	 * 当前任务步骤名称
	 */
    private String currentacti;

    /**
	 * 步骤开始时间
	 */
    private Date recetime;


    /**
	 * 流程表单链接
	 */
    private String url;

    /**
	 * 类型，待办or已办 (0 or 1) 
	 */
    private String todotype;


    /**
	 * 状态，未推or成功or失败 "0"
	 */
    private String status;

    /**
     * @return synctodoid 主键
     */
	public String getSynctodoid() {
		return synctodoid;
	}

	/**
	 * @param synctodoid 主键
	 */
	public void setSynctodoid(String synctodoid) {
		this.synctodoid = synctodoid;
	}

	/**
	 * @return systemcode 系统编码
	 */
	public String getSystemcode() {
		return systemcode;
	}

	/**
	 * @param systemcode 系统编码
	 */
	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode;
	}

	/**
	 * @return 流程步骤Id
	 */
	public String getTodoitemid() {
		return todoitemid;
	}

	/**
	 * @param todoitemid 流程步骤Id
	 */
	public void setTodoitemid(String todoitemid) {
		this.todoitemid = todoitemid;
	}

	/**
	 * @return 流程编号
	 */
	public Integer getAppcode() {
		return appcode;
	}

	/**
	 * @param appcode 流程编号
	 */
	public void setAppcode(Integer appcode) {
		this.appcode = appcode;
	}

	/**
	 * @return 流程名称
	 */
	public String getAppname() {
		return appname;
	}

	/**
	 * @param appname 流程名称
	 */
	public void setAppname(String appname) {
		this.appname = appname;
	}

	/**
	 * @return 流程标题
	 */
	public String getProcname() {
		return procname;
	}

	/**
	 * @param procname 流程标题
	 */
	public void setProcname(String procname) {
		this.procname = procname;
	}

	/**
	 * @return 流程实例编号
	 */
	public String getProcid() {
		return procid;
	}

	/**
	 * @param procid 流程实例编号
	 */
	public void setProcid(String procid) {
		this.procid = procid;
	}

	/**
	 * @return 流程任务编号
	 */
	public Integer getTaskid() {
		return taskid;
	}

	/**
	 * @param taskid 流程任务编号
	 */
	public void setTaskid(Integer taskid) {
		this.taskid = taskid;
	}

	/**
	 * @return 当前步骤处理人ID
	 */
	public String getPartid() {
		return partid;
	}

	/**
	 * @param partid 当前步骤处理人ID
	 */
	public void setPartid(String partid) {
		this.partid = partid;
	}

	/**
	 * @return 当前步骤处理人名字
	 */
	public String getPartname() {
		return partname;
	}

	/**
	 * @param partname 当前步骤处理人名字
	 */
	public void setPartname(String partname) {
		this.partname = partname;
	}

	/**
	 * @return 当前步骤发送人ID
	 */
	public String getPasserid() {
		return passerid;
	}

	/**
	 * @param passerid 当前步骤发送人ID
	 */
	public void setPasserid(String passerid) {
		this.passerid = passerid;
	}

	/**
	 * @return 当前步骤发送人名字
	 */
	public String getPassername() {
		return passername;
	}

	/**
	 * @param passername 当前步骤发送人名字
	 */
	public void setPassername(String passername) {
		this.passername = passername;
	}

	/**
	 * @return 当前任务步骤名称
	 */
	public String getCurrentacti() {
		return currentacti;
	}

	/**
	 * @param currentacti 当前任务步骤名称
	 */
	public void setCurrentacti(String currentacti) {
		this.currentacti = currentacti;
	}

	/**
	 * @return 步骤开始时间
	 */
	public Date getRecetime() {
		return recetime;
	}

	/**
	 * @param recetime 步骤开始时间
	 */
	public void setRecetime(Date recetime) {
		this.recetime = recetime;
	}

	/**
	 * @return 流程表单链接
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url 流程表单链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return 类型
	 */
	public String getTodotype() {
		return todotype;
	}

	/**
	 * @param todotype 类型
	 */
	public void setTodotype(String todotype) {
		this.todotype = todotype;
	}

	/**
	 * @return 状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SyncToDoList [synctodoid=" + synctodoid + ", systemcode=" + systemcode + ", todoitemid=" + todoitemid
				+ ", appcode=" + appcode + ", appname=" + appname + ", procname=" + procname + ", procid=" + procid
				+ ", taskid=" + taskid + ", partid=" + partid + ", partname=" + partname + ", passerid=" + passerid
				+ ", passername=" + passername + ", currentacti=" + currentacti + ", recetime=" + recetime + ", url="
				+ url + ", todotype=" + todotype + ", status=" + status + "]";
	}

}
