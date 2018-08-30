package com.nowcoder.controller;



import com.nowcoder.model.User;
import com.nowcoder.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by nowcoder on 2016/6/26.
 */
//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path = {"/","index"})
    @ResponseBody
    public String index(HttpSession session){

       return "hello coco" + session.getAttribute("msg") +"<br>"
               + toutiaoService.say();
   }

   @RequestMapping(value = "/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "coco") String key){
        return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
   }
   @RequestMapping("vm")
    public String news(Model model){
        model.addAttribute("value1","vle");
       List<String> list = Arrays.asList(new String[]{"pink","blue","orange"});
       Map<String,String> map = new HashMap<String, String>();
       for (int i = 0; i < 4; i++){
           map.put(String.valueOf(i),String.valueOf(i*i));
       }
       model.addAttribute("colors",list);
       model.addAttribute("map",map);

       model.addAttribute("user",new User("kk"));
        return "news";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");

        }

        for (Cookie cookie:
             request.getCookies()) {
            sb.append("cookie:" + cookie.getName() + ":"
                    + cookie.getValue() + "<br>");
        }
        sb.append("getMethod:" + request.getMethod()+"<br>");
        sb.append("getPathInfo:" + request.getPathInfo()+"<br>");
        sb.append("getQueryString:" + request.getQueryString()+"<br>");
        sb.append("getRequestURI:" + request.getRequestURI()+"<br>");
        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid",defaultValue = "b") String nowcoderid,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowCoderId From Cookie :" + nowcoderid;
    }
    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session){
        RedirectView re = new RedirectView("/",true);
//        判断code是不是301，是永久跳转，否则临时跳转，默认是临时
        if (code == 301){
            re.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg","jump from redirect");
        return re;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false) String key){
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("key 错误");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}
