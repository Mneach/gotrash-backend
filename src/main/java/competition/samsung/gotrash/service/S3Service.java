
package competition.samsung.gotrash.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
public interface S3Service {

    String uploadFile(File fileObj, String fileName);
    String getPresignUrl(String fileName);
    File convertMultiPartFileToFile(MultipartFile file);
}

