package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.service.AliyunService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(Aspect.class);

    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    AliyunService aliyunService;
    @Autowired
    UserService userService;

//    图片上传
    @RequestMapping(path = {"/uploadImage/"}, method = { RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try {
          String fileUrl = newsService.saveImage(file);
//            String fileUrl = aliyunService.uploadImgFile(file);
                if (fileUrl == null){
                    return ToutiaoUtil.getJSONString(1,"上传图片失败");
                }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("图片上传失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }
    }
//图片下载
    @RequestMapping(path = {"/image/"}, method = { RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(
                    new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误" + e.getMessage());
        }
    }
//    添加资讯
    @RequestMapping(path = {"/user/addNews/"}, method = { RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try {
            News news = new News();
            if (hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
//                匿名用户id
                news.setUserId(3);
            }
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加资讯错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = "/news/{newsId}",method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if (news != null){
//            评论
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }
}
