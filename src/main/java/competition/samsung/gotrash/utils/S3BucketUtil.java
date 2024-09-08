package competition.samsung.gotrash.utils;

import java.util.Objects;

public class S3BucketUtil {

    public static String createObjectKey(String serviceName, String id, String fileName){
        if(Objects.equals(fileName, "")){
            return serviceName + "/" + id;
        }
        return serviceName + "/" + id + "/" + fileName;
    }

}
