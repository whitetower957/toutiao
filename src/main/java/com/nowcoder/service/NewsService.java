package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getLatestNews(int user,int offset, int limit){
        return newsDao.selectByUserIdAndOffset(user,offset,limit);
    }
}
