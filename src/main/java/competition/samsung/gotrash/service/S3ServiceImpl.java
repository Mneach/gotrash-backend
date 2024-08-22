package competition.samsung.gotrash.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3ServiceImpl {

    public String uploadFile(MultipartFile file);
}
