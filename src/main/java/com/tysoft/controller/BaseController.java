package com.tysoft.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.tysoft.entity.base.Annex;
import com.tysoft.entity.base.AnnexFolder;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.AnnexService;

import net.sf.json.JSONObject;

/**     
 * @Title: BaseController.java   
 * @Description: TODO(用一句话描述该文件做什么)   
 * @author 黄雄雄   
 * @date 2018年12月28日 上午11:21:44   
 * @version V2.0     
 */
public abstract class BaseController {
	public static  String Success="0";
	public static  String Fail="1";
	//未分配人员
	public static  String firstUnit="未分配人员";
	public static  String initPsw="6";
	@Value("${web.upload-path}")
	protected String webUploadPath;

    @Autowired
	protected AnnexService annexService;

	/**
	 * 获取当前用户
	 * @param request
	 * @return
	 * @throws Exception
	 */
	 protected User getCurrentSystemUser(HttpServletRequest request) throws Exception{
		User user = (User)request.getSession().getAttribute("SYS_USER");
		return user;
	}

	
	/**
	 * 文件上传通用方法
	 * @param file
	 * @param annexFolder
	 * @param request
	 * @return
	 */
	protected Annex uploadFile(MultipartFile file,String oldName,String newName,HttpServletRequest request){
		// 数据库保存的目录
		String datdDirectory = null;
		
		try {
			if (!file.isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String temp = "annex" + File.separator + "upload" + File.separator + sdf.format(new Date()) + File.separator;  
				// 获取图片的文件名
				String fileName = file.getOriginalFilename();
				//获取文件大小
				long fileSize = file.getSize();//字节
				//获取文件类型
				String contentType = file.getContentType();
				// 获取图片的扩展名
				String extensionName = StringUtils.substringAfter(oldName, ".");
				// 文件路径
				String filePath = webUploadPath.concat(temp);
				File dest = new File(filePath, oldName);
				if (!dest.getParentFile().exists()) {
				   dest.getParentFile().mkdirs();
				}
				// 上传到指定目录
				FileOutputStream fos = new FileOutputStream(dest);
				fos.write(file.getBytes());
				fos.flush();
				fos.close();
				datdDirectory = temp.concat(newName).replaceAll("\\\\", "/");
				User userinfo = this.getCurrentSystemUser(request);
				//保存附件记录
				Annex annex = new Annex();
				annex.setContextType(contentType);
				annex.setExtendName(extensionName);
				annex.setFileSize(new BigDecimal(fileSize));
				annex.setName(StringUtils.substringBefore(oldName, "."));
				annex.setCreator(userinfo!=null?userinfo.getName():null);
				annex.setRelativePath(datdDirectory);
				annex.setUploadTime(new Date());
				annex = this.annexService.saveAnnex(annex);
				//保存成功进行文件的重命名
				File oldFile=new File(webUploadPath+temp.concat(oldName).replaceAll("\\\\", "/"));
				File newFile = new File(webUploadPath+annex.getRelativePath());
				oldFile.renameTo(newFile);
				return annex;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 图片文件转base64
	 * @param file
	 * @return
	 * @throws Exception
	 */
	protected String encodeBase64File(MultipartFile file) throws Exception {
		if(!file.isEmpty()) {
			String base64 = Base64.getEncoder().encodeToString(file.getBytes());
			return base64;
		}
		return null;
	}
	/**
	 * base64转图片
	 * @param base64
	 * @param annexFolder
	 * @return
	 * @throws Exception
	 */
//	protected Annex decodeBase64File(String base64,AnnexFolder annexFolder) throws Exception{
//		if(!StringUtils.isEmpty(base64)) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			String temp = "annex" + File.separator + "upload" + File.separator + sdf.format(new Date()) + File.separator;
//			byte[] buffer = Base64.getDecoder().decode(base64);
//			// 文件路径
//			String filePath = webUploadPath.concat(temp);
//			if(!new File(filePath).exists()) {
//				new File(filePath).mkdirs();
//			}
//			String fileName = System.currentTimeMillis()+".jpg";
//			filePath = filePath + File.separator + fileName;
//			OutputStream out = new FileOutputStream(filePath);  
//            out.write(buffer);  
//            out.flush();  
//            out.close();
//            File file = new File(filePath);
//            //保存附件记录
//			Annex annex = new Annex();
//			annex.setAnnexFolder(annexFolder);
//			annex.setContextType("image/jpeg");
//			annex.setExtendName("jpg");
//			annex.setFileSize(new BigDecimal(file.length()));
//			annex.setName(StringUtils.substringBefore(fileName, "."));
//			annex.setRelativePath(temp.concat(fileName).replaceAll("\\\\", "/"));
//			annex.setUploadTime(new Date());
//			annex = this.annexService.saveAnnex(annex);
//			return annex;
//		}
//		return null;
//	}
	
	/**
	 * http请求公共方法
	 * @param requestUrl
	 * @param outputStr
	 * @param requestMethod
	 * @return
	 */
	protected JSONObject httpRequest(String requestUrl,String jsonStr,String requestMethod){
		try{
			URL url = new URL(requestUrl);
			// 设定连接的相关参数
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);  
            connection.setUseCaches(false);  
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式 
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			out.append(jsonStr); 
			out.flush();
			out.close();
			// 获取服务端的反馈
			// 将返回的输入流转换成字符串
		 	InputStream inputStream = connection.getInputStream();
		 	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		 	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		 	StringBuffer buffer = new StringBuffer();
		 	String str = null;
		 	while ((str = bufferedReader.readLine()) != null) {
		 		buffer.append(str);
		 	}
		 	bufferedReader.close();
		 	inputStreamReader.close();
		 	// 释放资源
		 	inputStream.close();
		 	inputStream = null;
		 	connection.disconnect();
			return JSONObject.fromObject(buffer.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取UUID
	 * @return
	 */
	protected String getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}
	
	/**
	 * 获得统计时间区间
	 * @param type
	 * @return
	 */
	protected Map<String, Date> getCountTimeFrame(Integer type){
		Map<String, Date> timeMap = new HashMap<String,Date>();
		Date btime = null,etime = null;
		switch(type) {
			//当天
			case 1:
				btime = getTimesmorning();
				etime = getTimesnight();
				break;
			//本	周
			case 2:
				btime = getTimesWeekmorning();
				etime = getWeekEnd();
				break;
			//本月
			case 3:
				btime = getTimesMonthmorning();
				etime = getTimesMonthnight();
				break;
			//本季度
			case 4:
				btime = getCurrentQuarterStartTime();
				etime = getCurrentQuarterEndTime();
				break;
			//今年
			case 5:
				btime = getCurrentYearStartTime();
				etime = getCurrentYearEndTime();
				break;
			//截至当前24小时前
			case 24:
				btime = get24Before();
				etime = new Date();
				break;
			//默认当天
			default:
				btime = getTimesmorning();
				etime = getTimesnight();
				break;
		}
		timeMap.put("btime", btime);
		timeMap.put("etime", etime);
		return timeMap;
	}
	
	/**
	 * 获取
	 */
	public static Date get24Before() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)-24);
		return cal.getTime();
	}
	
	/**
	 * 获得当天0点时间
	 * @return
	 */
	public static Date getTimesmorning() {
		Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	/**
	 * 获得当天24点
	 * @return
	 */
	public static Date getTimesnight() {
		Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	/**
	 * 本周开始时间
	 * @return
	 */
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
        return cal.getTime(); 
	}
	
	/**
	 * 本周结束时间
	 * @return
	 */
	public static Date getWeekEnd() {
		Calendar cal = Calendar.getInstance();  
        cal.setTime(getTimesWeekmorning());  
        cal.add(Calendar.DAY_OF_WEEK, 7);  
        return cal.getTime();  
	}
	
	/**
	 * 获得本月第一天0点时间  
	 * @return
	 */
    public static Date getTimesMonthmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return cal.getTime();  
    }
    
    /**
     * 获得本月最后一天24点时间  
     * @return
     */
    public static Date getTimesMonthnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        return cal.getTime();  
    }  
	
	/**
	 * 本季度开始时间
	 * @return
	 */
	public static Date getCurrentQuarterStartTime() {  
        Calendar c = Calendar.getInstance();  
        int currentMonth = c.get(Calendar.MONTH) + 1;  
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date now = null;  
        try {  
            if (currentMonth >= 1 && currentMonth <= 3)  
                c.set(Calendar.MONTH, 0);  
            else if (currentMonth >= 4 && currentMonth <= 6)  
                c.set(Calendar.MONTH, 3);  
            else if (currentMonth >= 7 && currentMonth <= 9)  
                c.set(Calendar.MONTH, 4);  
            else if (currentMonth >= 10 && currentMonth <= 12)  
                c.set(Calendar.MONTH, 9);  
            c.set(Calendar.DATE, 1);  
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return now;  
    }
	/** 
     * 当前季度的结束时间，即xxxx-xx-xx 23:59:59 
     * @return 
     */  
    public static Date getCurrentQuarterEndTime() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getCurrentQuarterStartTime());  
        cal.add(Calendar.MONTH, 3);  
        return cal.getTime();  
    }
    /**
     * 今年开始时间
     * @return
     */
    public static Date getCurrentYearStartTime() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));  
        return cal.getTime();  
    }  
    /**
     * 今年结束时间
     * @return
     */
    public static Date getCurrentYearEndTime() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getCurrentYearStartTime());  
        cal.add(Calendar.YEAR, 1);  
        return cal.getTime();  
    }  
    
    /**
     * 字符串转Bean
     * @return
     */
   public Object StringtoBean(String str,Class beanClass) {
	   JSONObject objStr=JSONObject.fromObject(str);
	   Object obj=JSONObject.toBean(objStr, beanClass);
	   return obj;
   }
    
  
}
