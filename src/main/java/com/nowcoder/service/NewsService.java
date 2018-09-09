package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getLatestNews(int user,int offset, int limit){
        return newsDao.selectByUserIdAndOffset(user,offset,limit);
    }

    public String saveImage(MultipartFile file) throws IOException{
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if (doPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
//        文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-","")
                + "." + fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath());
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

//    添加资讯
    public int addNews(News news){
        newsDao.addNews(news);
        return news.getId();
    }

    public News getById(int newsId){
        return newsDao.getById(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDao.updateCommentCount(id, count);
    }
}
