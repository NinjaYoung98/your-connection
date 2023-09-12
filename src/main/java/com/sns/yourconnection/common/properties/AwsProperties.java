package com.sns.yourconnection.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProperties {

    private final Credentials credentials = new Credentials();
    private final S3 s3 = new S3();
    private final Region region = new Region();

    @Getter
    @Setter
    public static class Credentials {

        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class S3 {

        private String image;
        private String storage;
    }

    @Getter
    @Setter
    public static class Region {

        private String statics;
    }

    public String getAccessKey() {
        return credentials.getAccessKey();
    }

    public String getSecretKey() {
        return credentials.getSecretKey();
    }

    public String getImageBucket() {
        return s3.getImage();
    }

    public String getStorageBucket() {
        return s3.getStorage();
    }

    public String getRegionStatic() {
        return region.getStatics();
    }
}
