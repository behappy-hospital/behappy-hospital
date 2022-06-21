package org.xiaowu.behappy.oss.service;

import com.aliyun.oss.OSSClient;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.oss.config.AliOssProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 *
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class FileUploadService {

    private final AliOssProperties aliOssProperties;

    private final OSSClient ossClient;

    public String upload(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = aliOssProperties.getEndpoint();
        String bucketName = aliOssProperties.getBucket();
        try {
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //  2021/02/02/01.jpg
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;
            //调用方法实现上传
            ossClient.putObject(bucketName, fileName, inputStream);
            //上传之后文件路径
            // https://yygh-atguigu.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            //返回
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
