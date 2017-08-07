/*
 * 文件名：TenantAroundMgrController.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：yuanpeng
 * 修改时间：2017年8月6日
 */

package com.bonc.nerv.tioa.week.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.nerv.tioa.week.service.TenantAroundMgrService;

/**
 * 
 * 租户周边信息管理控制器
 * @author yuanpeng
 * @version 2017年8月6日
 * @see TenantAroundMgrController
 * @since
 */
@Controller
public class TenantAroundMgrController {
    @Autowired
    @Qualifier("tenantAroundMgrService")
    private TenantAroundMgrService tenantAroundMgrService;
    
    /**
     * 将id和name从接口导入到数据库
     * @return "" 
     * @see
     */
    @RequestMapping("/tenantAroundMgrController")
    @ResponseBody
    public String getTenantAroundMgr(){
        tenantAroundMgrService.saveIdAndNameFromHttp();
        return JSON.toJSONString("");
    }
    
    /**
     * 从数据库导出到Excel进行批量修改
     * @return ""
     * @see
     */
    @RequestMapping("/exportFromTenantAroundMgr")
    @ResponseBody
    public String exportFromTenantAroundMgr(){
        tenantAroundMgrService.exportFromTenantAroundMgr();
        return JSON.toJSONString("从数据库导出Excel成功");
    }
    
    /**
     * 对Excel进行批量修改后导入数据库
     * @return “”
     * @see
     */
    @RequestMapping("importToTenantAroundMgr")
    @ResponseBody
    public String importToTenantAroundMgr() {
        
        return JSON.toJSONString("导入Excel到数据库成功");
    }

}
