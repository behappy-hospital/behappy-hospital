package org.xiaowu.behappy.oss.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.oss.service.FileUploadService;

@RestController
@RequestMapping("/api/oss/file")
@AllArgsConstructor
public class FileApiController {

    private final FileUploadService fileUploadService;

    /**
     * 上传文件到阿里云oss
     * @apiNote
     * @author xiaowu
     * @param file
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.String>
     */
    @PostMapping("/fileUpload")
    public Result<String> fileUpload(MultipartFile file) {
        //获取上传文件
        String url = fileUploadService.upload(file);
        return Result.ok(url);
    }
}
