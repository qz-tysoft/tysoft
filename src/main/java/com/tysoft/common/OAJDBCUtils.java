package com.tysoft.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 *	推送OA待办
 */
@Component
@ConfigurationProperties(prefix = "oaConfig")
public class OAJDBCUtils {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String driver;
	private String url;
	private String user;
	private String password;
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
    /**
     * 获取 Connetion
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
    	/**
         * 驱动注册
         */
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    	
        return DriverManager.getConnection(url, user, password);
    }
    
    /**
     * 释放资源
     * @param conn
     * @param st
     * @param rs
     */
    public void closeResource(Connection conn,Statement st,ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(conn);
    }
    
    /**
     * 释放连接 Connection
     * @param conn
     */
    public void closeConnection(Connection conn) {
        if(conn !=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        conn = null;
    }
    
    /**
     * 释放语句执行者 Statement
     * @param st
     */
    public void closeStatement(Statement st) {
        if(st !=null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        st = null;
    }
    
    /**
     * 释放结果集 ResultSet
     * @param rs
     */
    public void closeResultSet(ResultSet rs) {
        if(rs !=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        rs = null;
    }
    
    /**
     * 插入
     * @param syncToDoList 
     */
    public int insertSyncToDoList(SyncToDoList syncToDoList) {
    	int result = 1;
    	Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
			conn = this.getConnection();
			String sql = "INSERT INTO SyncToDoList(SyncToDoId,SystemCode,ToDoItemId,AppCode,AppName,ProcName,ProcId,TaskId,PartId,PartName,"
					+ "PasserId,PasserName,CurrentActi,ReceTime,FinishTime,URL,ToDoType,CreateTime,Status) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String syncToDoId = UUID.randomUUID().toString();
			syncToDoList.setSynctodoid(syncToDoId);
			logger.info(syncToDoList.toString());
			//ToDoItemId = "BR" + ProcId + "_" + TaskId
			st= conn.prepareStatement(sql);
			
			st.setString(1, syncToDoId);//主键
			st.setString(2, "BR");//SystemCode 系统编码
			st.setString(3, syncToDoList.getTodoitemid());//ToDoItemId 流程步骤Id
			st.setInt(4, syncToDoList.getAppcode());//AppCode 流程编号
			st.setString(5, syncToDoList.getAppname());//AppName
			st.setString(6, syncToDoList.getProcname());//流程标题
			st.setString(7, syncToDoList.getProcid());//流程实例编号
			st.setInt(8, syncToDoList.getTaskid());//流程任务编号
			st.setString(9, syncToDoList.getPartid());//当前步骤处理人ID
			st.setString(10, syncToDoList.getPartname());//当前步骤处理人名字
			st.setString(11, syncToDoList.getPasserid());//当前步骤发送人ID
			st.setString(12, syncToDoList.getPassername());//当前步骤发送人名字
			st.setString(13, syncToDoList.getCurrentacti());//当前任务步骤名称  填报or审批CurrentActi
			st.setString(14, syncToDoList.getRecetime() == null ? DateUtils.getTimeStr(new Date(), DateUtils.HYPHEN_DISPLAY_DATE)
					: DateUtils.getTimeStr(syncToDoList.getRecetime(), DateUtils.HYPHEN_DISPLAY_DATE));//步骤开始时间
			st.setString(15, syncToDoList.getTodotype().equals("1") ?
					DateUtils.getTimeStr(new Date(), DateUtils.HYPHEN_DISPLAY_DATE) : null);//步骤结束时间
			st.setString(16, syncToDoList.getUrl());//流程表单链接
			st.setString(17, syncToDoList.getTodotype());//0:待办; 1:已办; 2:删除
			st.setString(18, DateUtils.getTimeStr(new Date(), DateUtils.HYPHEN_DISPLAY_DATE));//记录创建时间
			st.setString(19, "0");//未推送
			
			result = st.executeUpdate();
		
		} catch (SQLException e) {
			result = 0;
			e.printStackTrace();
		} finally {
			this.closeResource(conn, st, rs);
		}
    	return result;
    }
    
    /**
     * 根据流程标题查询
     * @param procname 流程标题
     * @param todotype 类型
     * @return
     */
    public List<SyncToDoList> querySyncToDoListBy(String procname, String todotype){
    	Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SyncToDoList syncToDoList = new SyncToDoList();
        List<SyncToDoList> todoLidt = new ArrayList<SyncToDoList>();
        
        try {
			conn = this.getConnection();
			String sql = "select * from SyncToDoList where 1=1 ";
			
			List<Map<String,String>> params = new ArrayList<Map<String,String>>();
			if(StringUtils.isNotBlank(procname)) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", "ProcName");
				map.put("val", procname);
				params.add(map);
			}
			if(StringUtils.isNotBlank(todotype)) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", "ToDoType");
				map.put("val", todotype);
				params.add(map);
			}
			
			//设置查询条件
			for (int i = 0; i < params.size(); i++) {
				sql += "and SyncToDoList." + params.get(i).get("name") + " = ?";
			}
			pst = conn.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++) {
				pst.setString(i+1, params.get(i).get("val"));
			}
			
			rs = pst.executeQuery();
			while(rs.next()) {
				syncToDoList = new SyncToDoList();
				
				syncToDoList.setSynctodoid(rs.getString("SyncToDoId"));
				syncToDoList.setSystemcode(rs.getString("SystemCode"));
				syncToDoList.setTodoitemid(rs.getString("ToDoItemId"));
				syncToDoList.setAppcode(rs.getInt("AppCode"));
				syncToDoList.setAppname(rs.getString("AppName"));
				syncToDoList.setProcname(rs.getString("ProcName"));
				syncToDoList.setProcid(rs.getString("ProcId"));
				syncToDoList.setTaskid(rs.getInt("TaskId"));
				syncToDoList.setPartid(rs.getString("PartId"));
				syncToDoList.setPartname(rs.getString("PartName"));
				syncToDoList.setPasserid(rs.getString("PasserId"));
				syncToDoList.setPassername(rs.getString("PasserName"));
				syncToDoList.setCurrentacti(rs.getString("CurrentActi"));
				syncToDoList.setRecetime(rs.getDate("ReceTime"));
				syncToDoList.setUrl(rs.getString("URL"));
				syncToDoList.setTodotype(rs.getString("ToDoType"));
				syncToDoList.setStatus(rs.getString("Status"));
				
				todoLidt.add(syncToDoList);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource(conn, pst, rs);
		}
    	return todoLidt;
    }
    
    
    /**
     * 查询
     * @param synctodoid 主键
     * @param url 流程表单链接
     * @param partid 当前处理人id
     * @return
     */
    public List<SyncToDoList> querySyncToDoList(String synctodoid,String url,String partid){
    	Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SyncToDoList syncToDoList = new SyncToDoList();
        List<SyncToDoList> todoLidt = new ArrayList<SyncToDoList>();
        
        try {
			conn = this.getConnection();
			String sql = "select * from SyncToDoList where 1=1 ";
			
			List<Map<String,String>> params = new ArrayList<Map<String,String>>();
			if(StringUtils.isNotBlank(synctodoid)) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", "SyncToDoId");
				map.put("val", synctodoid);
				params.add(map);
			}
			if(StringUtils.isNotBlank(url)) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", "URL");
				map.put("val", "%"+url+"%");
				params.add(map);
			}
			if(StringUtils.isNotBlank(partid)) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", "PartId");
				map.put("val", partid);
				params.add(map);
			}
			
			//设置查询条件
			for (int i = 0; i < params.size(); i++) {
				if("URL".equals(params.get(i).get("name"))) {
					sql += "and SyncToDoList." + params.get(i).get("name") + " LIKE ? ";
				}else {
					sql += "and SyncToDoList." + params.get(i).get("name") + " = ? ";
				}
			}
			pst = conn.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++) {
				pst.setString(i+1, params.get(i).get("val"));
			}
			
			/*if(StringUtils.isNotBlank(synctodoid)) {
				//根据OA待办主键查询
				sql += "and SyncToDoList.SyncToDoId = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, synctodoid);
				
			}else if(StringUtils.isNotBlank(url)) {
				//根据表单链接URL查询
				sql += "and SyncToDoList.URL LIKE ? ";
				if(StringUtils.isNotBlank(partid)) {
					sql += "and PartId = ? ";
				}
				pst = conn.prepareStatement(sql);
				pst.setString(1, "%"+url+"%");
				
				if(StringUtils.isNotBlank(partid)) {
					pst.setString(2, partid);
				}
			}*/
			rs = pst.executeQuery();
			while(rs.next()) {
				syncToDoList = new SyncToDoList();
				
				syncToDoList.setSynctodoid(rs.getString("SyncToDoId"));
				syncToDoList.setSystemcode(rs.getString("SystemCode"));
				syncToDoList.setTodoitemid(rs.getString("ToDoItemId"));
				syncToDoList.setAppcode(rs.getInt("AppCode"));
				syncToDoList.setAppname(rs.getString("AppName"));
				syncToDoList.setProcname(rs.getString("ProcName"));
				syncToDoList.setProcid(rs.getString("ProcId"));
				syncToDoList.setTaskid(rs.getInt("TaskId"));
				syncToDoList.setPartid(rs.getString("PartId"));
				syncToDoList.setPartname(rs.getString("PartName"));
				syncToDoList.setPasserid(rs.getString("PasserId"));
				syncToDoList.setPassername(rs.getString("PasserName"));
				syncToDoList.setCurrentacti(rs.getString("CurrentActi"));
				syncToDoList.setRecetime(rs.getDate("ReceTime"));
				syncToDoList.setUrl(rs.getString("URL"));
				syncToDoList.setTodotype(rs.getString("ToDoType"));
				syncToDoList.setStatus(rs.getString("Status"));
				
				todoLidt.add(syncToDoList);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource(conn, pst, rs);
		}
    	return todoLidt;
    }
}
