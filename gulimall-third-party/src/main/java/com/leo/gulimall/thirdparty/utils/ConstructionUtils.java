package com.leo.gulimall.thirdparty.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConstructionUtils {

    @Value("${tencent.cos.file.secretid}")
    private String secretid;
    @Value("${tencent.cos.file.secretkey}")
    private String secretkey;
    @Value("${tencent.cos.file.region}")
    private String region;
    @Value("${tencent.cos.file.bucketname}")
    private String bucketname;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @PostConstruct
    public void init() {
        END_POINT = region;
        ACCESS_KEY_ID = secretid;
        ACCESS_KEY_SECRET = secretkey;
        BUCKET_NAME = bucketname;
    }
}
