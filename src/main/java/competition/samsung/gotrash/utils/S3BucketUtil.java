package competition.samsung.gotrash.utils;

public class S3BucketUtil {

    public static String createObjectKey(String serviceName, String id, String fileName){
        return serviceName + "/" + id + "/" + fileName;
    }

}
