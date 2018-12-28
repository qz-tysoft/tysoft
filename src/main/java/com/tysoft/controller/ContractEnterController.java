package com.tysoft.controller;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tysoft.entity.security.Annex;
import com.tysoft.service.security.AnnexService;

@Controller
@RequestMapping("/contract")
public class ContractEnterController extends BaseController{
	@Autowired
	private AnnexService annexService;
		
		//合同签订界面
		@RequestMapping("test")
		public void signContractView(HttpServletRequest request,Map<String,Object> map) throws Exception {
			System.out.println("------------数据库连接测试------------");
			Annex annex=annexService.findAnnexById("402881e666be97330166be9a85310000");
			if(annex!=null) {
				String annexName=annex.getExtendName();
				System.out.println("数据库连接成功 查到的数据:"+annexName);
			}
		
		
		}
		
		
}
