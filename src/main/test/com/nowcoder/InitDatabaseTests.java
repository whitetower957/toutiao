package com.nowcoder;


import com.nowcoder.dao.NewsDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Date;
import java.util.Random;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    NewsDao newsDao;

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

            News news = new News();
            news.setCommentCount(i);
            Date date  = new Date();
            date.setTime(date.getTime() + 1000*3600*3);
            news.setCreatedDate(date);
            news.setImage(String.format("www.baidu.com%d",random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("title{%d}",i));
            news.setLink(String.format("google.com%d",i));

            newsDao.addNews(news);
        }
//        测试用来判断是否相等
//        Assert.assertEquals("cola",userDao.selectById(1).getPassword());
//        userDao.deleteById(1);
//        Assert.assertEquals("cola",userDao.selectById(1).getPassword());
    }
}
