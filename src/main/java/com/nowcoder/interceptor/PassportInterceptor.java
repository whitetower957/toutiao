package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
//        当ticket不为空，但是不在数据库中或者为过期ticket，或状态不为0
        if (ticket != null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }

            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null){
//            modelAndView不为空，且用户是个登录用户
            modelAndView.addObject("user",hostHolder.getUser());

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
