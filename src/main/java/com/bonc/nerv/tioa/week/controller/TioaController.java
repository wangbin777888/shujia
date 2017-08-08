package com.bonc.nerv.tioa.week.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 
 * 调用外部restful接口的服务类
 * @author yuanpeng
 * @version 2017年7月31日
 * @see TioaController
 * @since
 */
@Controller
public class TioaController {    
    /**
     * 
     * Description: <br>
     * 进入实施自动化展示页面
     * @return 
     * @see
     */
    @RequestMapping("/view")
    public String inter(){
        return "view/data_view";
    }
    
   
}