package com.sdu127.Util;

import com.sdu127.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FileUtil {
    @Value("${prefix-url}")
    private String PREFIX_URL;
    @Value("${pic.virtual-path}")
    private String picVirtualPath;
    @Value("${pic.local-file-path}")
    private String localFilePath;

    public String uploadImage(MultipartFile file, String folderName, String fileName) throws IOException {
        // 判断是否为图片
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileUploadException("请上传图片");
        }

        // 保存图片
        return uploadFile(file, folderName, fileName, picVirtualPath.substring(0, picVirtualPath.length() - 3));
    }


    /**
     * 上传文件到指定路径
     *
     * @param file 文件
     * @param folderName 文件夹名（服务名）
     * @param fileName 文件名
     * @throws IOException IO异常
     * @throws FileUploadException 文件异常
     */
    public String uploadFile(MultipartFile file, String folderName, String fileName, String virtualPath) throws IOException {
        // 获取文件内容
        byte[] fileBytes = file.getBytes();

        // 获取上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 从原始文件名中提取文件扩展名
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // 判断文件是否为空
        if (fileBytes.length == 0) {
            throw new FileUploadException("上传文件为空");
        }

        // 获取文件地址
        StringBuilder builder = new StringBuilder(localFilePath);
        int preLength = builder.length();

        // 以folderName创建文件地址
        File parentFile = new File(builder.append("/").append(folderName).toString());
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new FileUploadException("父文件夹创建失败");

        // 判断文件是否存在
        File specificFile = new File(builder.append("/").append(fileName).append(fileExtension).toString());

        // 将文件保存到指定目录
        Files.write(Paths.get(specificFile.getPath()), fileBytes);

        // 生成文件路径
        builder.replace(0, preLength, PREFIX_URL + virtualPath);

        return builder.toString();
    }


    /**
     * 删除图片
     */
    public void removeImage(String url) throws IOException {
        removeFile(url, "image", picVirtualPath.substring(0, picVirtualPath.length() - 3));
    }

    public void removeFile(String url, String folderName, String virtualPath) throws FileUploadException, IOException {
        String prefix = PREFIX_URL + virtualPath + "/" + folderName;
        // 校验URL格式是否正确
        if (!url.startsWith(prefix)) {
            throw new FileUploadException();
        }

        // 从URL提取文件的相对路径
        List<String> a = List.of(url.split("/"));
        String filePath = a.get(5);

        // 生成文件的完整路径
        String fullFilePath = localFilePath + folderName + "/" + filePath;

        // 创建File对象
        File file = new File(fullFilePath);

        // 检查文件是否存在
        if (!file.exists()) {
            return;
        }

        // 删除文件
        Files.delete(Paths.get(file.getPath()));
    }
}