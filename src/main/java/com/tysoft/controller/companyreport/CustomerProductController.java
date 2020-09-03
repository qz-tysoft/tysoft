/**
 * <p>Description: TYOA TYOA Controller</p>
 * <p>Copyright: Copyright (c) 2020</p>
 * <p>Company: 厦门路桥信息股份有限公司</p>
 *
 * @author :admin
 * @version 1.0
 */

package com.tysoft.controller.companyreport;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.tysoft.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;
import com.tysoft.entity.companyreport.CustomerProduct;
import com.tysoft.service.companyreport.CustomerProductService;


/**
 * 客户产品表管理
 */
@Controller
@RequestMapping("/customer-product")
public class CustomerProductController extends BaseController {
    @Autowired
    private CustomerProductService customerProductService;


    /**
     * 进入展示列表
     */
    @RequestMapping("list")
    public String list(HttpServletRequest request, Model model) {
        return "companyreport/customer-product";
    }

    /**
     * 查询列表
     */
    @RequestMapping("query-page")
    @ResponseBody
    public Map<String, Object> queryPage(HttpServletRequest request) {

        return null;
    }

    /**
     * 保存客户产品表
     */
    @RequestMapping("save")
    @ResponseBody
    public String save(CustomerProduct customerProduct, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isBlank(id)) {
        }
        CustomerProduct newCustomerProduct = customerProductService.saveCustomerProduct(customerProduct);
        request.setAttribute("customerProduct", customerProduct);
        request.setAttribute("id", id);
        if (newCustomerProduct != null) {
            return "{\"success\":true,\"msg\":\"保存成功!\"}";
        } else {
            return "{\"success\":false,\"msg\":\"保存失败!\"}";
        }
    }

    /**
     * 获取客户产品表
     */
    @RequestMapping("find")
    @ResponseBody
    public Map<String, Object> find(HttpServletRequest request) {
        String id = request.getParameter("id");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id)) {
            CustomerProduct customerProduct = this.customerProductService.findCustomerProductById(id);
            resultMap.put("customerProduct", customerProduct.poToMap());
        }
        return resultMap;
    }

    /**
     * 删除客户产品表
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        try {
            this.customerProductService.deleteCustomerProductByIds(ids);
        } catch (Exception e) {
            return "{\"success\":false,\"msg\":\"删除失败!\"}";
        }
        return "{\"success\":true,\"msg\":\"删除成功!\"}";
    }
}