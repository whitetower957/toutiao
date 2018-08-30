package com.nowcoder.service;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUser(int id){
        return userDao.selectById(id);
    }

    public Map<String,Object> register(String username, String password){
        Map<String,Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user = userDao.selectByName(username);
        if (user != null){
            map.put("msgname","用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);
        return map;
    }
}
