
package competition.samsung.gotrash.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
public interface S3Service {

    String uploadFileAndGetUrl(MultipartFile file, String serviceName, String id) throws Exception;
    public String getPresignUrl(String ObjectKey) throws Exception;
    void deleteFile(String objectKey) throws Exception;
    void deleteFolder(String prefix) throws Exception;
}

