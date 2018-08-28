package com.nowcoder;


import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;

    @Test
    public void initDate(){
        Random random = new Random();
        User user = new User();
        for (int i = 0; i < 11; i++){
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            user.setPassword("cola");
            userDao.updatePassword(user);
        }
//        测试用来判断是否相等
        Assert.assertEquals("cola",userDao.selectById(1).getPassword());
        userDao.deleteById(1);
        Assert.assertEquals("cola",userDao.selectById(1).getPassword());
    }
}
