package com.nowcoder.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

@Service
public class AliyunService {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    static String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    static String accessKeyId = "LTAIYjjuBz3pMUlJ";
    static String accessKeySecret = "FwbE4dfWRYSylnSBH0t22nHhJqcGGH";
    static String bucketName = "toutiaotest";
//    上传后的图片名字
    static String key = new Date() + ".jpg";

    private static final Logger logger = LoggerFactory.getLogger(AliyunService.class);

    public String uploadImgFile(MultipartFile file) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if (doPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
//        文件名
//        String fileName = UUID.randomUUID().toString().replaceAll("-","")
//                + "." + fileExt;
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
//        设置元信息
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image");
        metadata.setContentEncoding("utf-8");

        try{
            // 上传文件流。
            InputStream inputStream = new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + fileName));
            ossClient.putObject(bucketName,key,inputStream,metadata);
        }catch (Exception e){
            logger.error("上传失败" + e.getMessage());
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

}
