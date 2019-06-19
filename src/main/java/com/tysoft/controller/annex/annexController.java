package com.tysoft.controller.annex;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tysoft.controller.BaseController;
import com.tysoft.entity.base.Annex;

@Controller
@RequestMapping("/annex")
public class annexController extends BaseController {
    //定义界面
	private String annexView="annex/annexView";
	//载入文件的上传路径
	@Value("${web.upload-path}")
	protected String webUploadPath;
	
	//附件上传页面
	@RequestMapping("annexView")
	public String annexView(HttpServletRequest request){
		String fileNum=request.getParameter("fileNum");
		String fileType=request.getParameter("fileType");
		request.setAttribute("fileNum", fileNum);
		request.setAttribute("fileType", fileType);
		return annexView;
	}
	
	@RequestMapping("annexUpload")
	@ResponseBody
	public synchronized  Map<String,Object>  annexUpload(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		MultipartFile file =((MultipartHttpServletRequest)request).getFile("file"); 
		String	oldName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\")+1);
		String  fileType =file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		//精确到毫秒的时间戳
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String newName= time.format(new Date())+"."+fileType;  
		Annex annex=this.uploadFile(file,oldName,newName,request);
		map.put("annexId",annex.getId());
		map.put("code", 0);
		return map;
	}
	
	@RequestMapping("annexDel")
	@ResponseBody
	public Map<String,Object>  delAnnex(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		String annexId=request.getParameter("annexId");
        Annex annex=this.annexService.findAnnexById(annexId);
        String relativePath=annex.getRelativePath();
        String realPath=webUploadPath+relativePath;
        File file=new File(realPath);
        if(file.exists()&&file.isFile()) {
            file.delete();
            annexService.deleteAnnexByIds(annexId);
            map.put("state", 1);
        }
		return map;
	}
	
	
	@RequestMapping("annexDownload")
	public void  annexDownload(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
	   String annexId=request.getParameter("annexId");   
	   Annex annex=this.annexService.findAnnexById(annexId);
       String relativePath=annex.getRelativePath();
       String realPath=webUploadPath+relativePath;
       String fileName=annex.getName();
       File file=new File(realPath);	  
           //File file = new File(realPath , fileName);
           if (file.exists()) {
               response.setContentType("application/force-download");// 设置强制下载不打开
               response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
               byte[] buffer = new byte[1024];
               FileInputStream fis = null;
               BufferedInputStream bis = null;
               try {
                   fis = new FileInputStream(file);
                   bis = new BufferedInputStream(fis);
                   OutputStream os = response.getOutputStream();
                   int i = bis.read(buffer);
                   while (i != -1) {
                       os.write(buffer, 0, i);
                       i = bis.read(buffer);
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               } finally {
                   if (bis != null) {
                       try {
                           bis.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                   if (fis != null) {
                       try {
                           fis.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }
       }

}
