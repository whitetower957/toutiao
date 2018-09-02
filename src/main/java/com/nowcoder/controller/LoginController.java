package com.nowcoder.controller;

import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String index(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember",defaultValue = "0") int rember,
                        HttpServletResponse response) {
            try {
                Map<String,Object> map = userService.register(username,password);
                if (map.containsKey("ticket")){
                    Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
    //                设置ticket全段有效
                    cookie.setPath("/");
    //                如果用户设置rememberme,则设置cookie时间长一点
                    if (rember > 0){
                        cookie.setMaxAge(3600*24*5);
                    }
                    response.addCookie(cookie);
                    return ToutiaoUtil.getJSONString(0,"注册成功");
                } else
                    return ToutiaoUtil.getJSONString(1,map);
            }catch (Exception e){
                logger.error("注册异常" + e.getMessage());
                return ToutiaoUtil.getJSONString(1,"注册异常");
            }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember",defaultValue = "0") int rember,
                        HttpServletResponse response) {
        try {
            Map<String,Object> map = userService.register(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
//                设置ticket全段有效
                cookie.setPath("/");
//                如果用户设置rememberme,则设置cookie时间长一点
                if (rember > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"注册成功");
            } else
                return ToutiaoUtil.getJSONString(1,map);
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }




}
