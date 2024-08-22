//package competition.samsung.gotrash.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.AwsCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//
//@Configuration
//public class S3Config {
//    @Value("${aws.access.key.id}")
//    private String accessKeyId;
//
//    @Value("${aws.secret.access.key}")
//    private String accessSecretAccessKey;
//    @Value("${aws.region}")
//    private String region;
//
//    @Bean
//    public S3Client s3Client() {
//        AwsCredentials credentials = AwsBasicCredentials.create(accessKeyId, accessSecretAccessKey);
//        return S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(credentials)).region(Region.of(region)).build();
//    }
//}
