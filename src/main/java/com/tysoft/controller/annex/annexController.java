package com.tysoft.controller.annex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
    //定义下发界面
	private String annexView="annex/annexView";
	
	//附件上传页面
	@RequestMapping("annexView")
	public String annexView(HttpServletRequest request){
		return annexView;
	}
	
	@RequestMapping("annexUpload")
	@ResponseBody
	public Map<String,Object>  annexUpload(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		MultipartFile file =((MultipartHttpServletRequest)request).getFile("file"); 
		String	oldName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\")+1);
		String  fileType =file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String newName= time.format(new Date())+"."+fileType;  
		Annex annex=this.uploadFile(file,oldName,newName,request);
		map.put("code", 0);
		return map;
	}
	
}
