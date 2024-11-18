package com.sdu127.Service;

import com.sdu127.Data.VO.Result;
import com.sdu127.Util.FileUtil;
import com.sdu127.exception.FileUploadException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    @Resource
    FileUtil fileUtil;

    public Result uploadImage(MultipartFile image) {
        try {
            UUID uuid = UUID.randomUUID();
            String imagePath = fileUtil.uploadImage(image, "image", uuid.toString());
            return Result.success("图片上传成功", imagePath);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    public Result removeImage(String url) {
        try {
            fileUtil.removeImage(url);
            return Result.ok();
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}
