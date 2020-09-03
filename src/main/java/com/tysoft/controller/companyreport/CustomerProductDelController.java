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
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.tysoft.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;
import com.tysoft.entity.companyreport.CustomerProductDel;
import com.tysoft.service.companyreport.CustomerProductDelService;


/**
 * 客户产品详情表管理
 */
@Controller
@RequestMapping("/customer-product-del")
public class CustomerProductDelController extends BaseController {
    @Autowired
    private CustomerProductDelService customerProductDelService;


    /**
     * 进入展示列表
     */
    @RequestMapping("list")
    public String list(HttpServletRequest request, Model model) {
        return "companyreport/customer-product-del";
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
     * 保存客户产品详情表
     */
    @RequestMapping("save")
    @ResponseBody
    public String save(CustomerProductDel customerProductDel, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isBlank(id)) {
        }
        CustomerProductDel newCustomerProductDel = customerProductDelService.saveCustomerProductDel(customerProductDel);
        request.setAttribute("customerProductDel", customerProductDel);
        request.setAttribute("id", id);
        if (newCustomerProductDel != null) {
            return "{\"success\":true,\"msg\":\"保存成功!\"}";
        } else {
            return "{\"success\":false,\"msg\":\"保存失败!\"}";
        }
    }

    /**
     * 获取客户产品详情表
     */
    @RequestMapping("find")
    @ResponseBody
    public Map<String, Object> find(HttpServletRequest request) {
        String id = request.getParameter("id");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id)) {
            CustomerProductDel customerProductDel = this.customerProductDelService.findCustomerProductDelById(id);
            resultMap.put("customerProductDel", customerProductDel.poToMap());
        }
        return resultMap;
    }

    /**
     * 删除客户产品详情表
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        try {
            this.customerProductDelService.deleteCustomerProductDelByIds(ids);
        } catch (Exception e) {
            return "{\"success\":false,\"msg\":\"删除失败!\"}";
        }
        return "{\"success\":true,\"msg\":\"删除成功!\"}";
    }
}