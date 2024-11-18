package com.sdu127.Controller;

import com.sdu127.Data.VO.Result;
import com.sdu127.Service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    @Resource
    FileService fileService;

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam MultipartFile file) {
        return fileService.uploadImage(file);
    }

    @DeleteMapping("/removeImage")
    public Result removeImage(@RequestParam String url) {
        return fileService.removeImage(url);
    }

}
