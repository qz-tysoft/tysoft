package com.tysoft.controller.faceRecognition;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tysoft.common.FaceUtils;
import com.tysoft.controller.BaseController;
@Controller
@RequestMapping("/faceRecognition")
public class faceRecognitionController extends BaseController{
    
	private String faceMainView = "faceRecognition/faceMainView";

	@RequestMapping("faceMainView")
	public String faceMainView(HttpServletRequest request){
		//比较两张图片相似度的
		return faceMainView;
	}
	
}
