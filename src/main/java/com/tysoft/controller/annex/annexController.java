package com.tysoft.controller.annex;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import jodd.util.StringUtil;

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
		String exts=request.getParameter("exts");
		String isSingle=request.getParameter("isSingle");
		
		//单独打开此界面默认条件
		if(!StringUtil.isNotBlank(fileNum)&&!StringUtil.isNotBlank(fileType)&&!StringUtil.isNotBlank(exts)){
			fileNum="5";
			fileType="file";
			exts="";
		}
		if(!StringUtil.isNotBlank(isSingle)) {
			isSingle="fasle";
		}
		if(StringUtil.isNotBlank(exts)&&StringUtil.isNotBlank(fileNum)) {
			if(isSingle.equals("true")) {
			request.setAttribute("tipMsg", "当前可上传的文件格式:"+exts+" 文件最多上传:"+fileNum+"个");
			}else {
				request.setAttribute("tipMsg", "当前可上传的文件格式:"+exts);
			}
	    }
		
		if(!StringUtil.isNotBlank(exts)) {
			if(isSingle.equals("true")) {
		 	request.setAttribute("tipMsg", "当前文件最多上传:"+fileNum+"个");
		    }
		}
		request.setAttribute("isSingle",isSingle);
		request.setAttribute("fileNum", fileNum);
		request.setAttribute("fileType", fileType);
		request.setAttribute("exts", exts);
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
	
	@RequestMapping("batchAnnexDel")
	@ResponseBody
	public String  batchAnnexDel(HttpServletRequest request){
		String annexIds=request.getParameter("annexIds");
        String delAnnexIds[]=annexIds.split(",");
        //开始批量删除
        for(int i=0;i<delAnnexIds.length;i++) {
        	String annexId=delAnnexIds[i].replaceAll(" ", "");
        	if(StringUtil.isNotBlank(annexId)) {
        		//开始进行删除
                Annex annex=this.annexService.findAnnexById(annexId);
                String relativePath=annex.getRelativePath();
                String realPath=webUploadPath+relativePath;
                File file=new File(realPath);
                if(file.exists()&&file.isFile()) {
                    file.delete();
                    annexService.deleteAnnexByIds(annexId);
                }
        	}
        }
		return Success;
	}
	
	
	
	@RequestMapping("downloadAnnex")
	public String  downloadAnnex(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
	   String annexId=request.getParameter("annexId");   
	   Annex annex=this.annexService.findAnnexById(annexId);
       String relativePath=annex.getRelativePath();
       String realPath=webUploadPath+relativePath;
       String fileName=annex.getName();
       String fileType=annex.getExtendName();
       File file=new File(realPath);	  
           //File file = new File(realPath , fileName);
           if (file.exists()) {
        	   // 配置文件下载
               response.setHeader("content-type", "application/octet-stream");
               response.setContentType("application/octet-stream");
               // 下载文件能正常显示中文
               response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+"."+fileType, "UTF-8"));
               // 实现文件下载
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
                  // System.out.println("Download the song successfully!");
               }
               catch (Exception e) {
                 //  System.out.println("Download the song failed!");
               }
               finally {
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
           return null;
       }

}
