package com.ucenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hjedu.customer.entity.BbsUser;

@Controller
@RequestMapping("/th")
public class HomeController {
    
    //入口
    @RequestMapping(value = "/home.do")
    public String home(Model model) {
    	BbsUser user = new BbsUser();
        model.addAttribute("user",user);
        return "aa";
    }

    //提交表单后进行数据读取，并将数据传出
    @RequestMapping(value = "/bb.do",method = RequestMethod.POST)
    public String bb(BbsUser user,Model model) {
        model.addAttribute("user", user);
        model.addAttribute("message", ",welcome");
        return "bb";
    }
}