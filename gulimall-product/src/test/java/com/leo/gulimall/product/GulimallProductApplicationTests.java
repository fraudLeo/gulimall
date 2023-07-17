package com.leo.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leo.gulimall.product.entity.BrandEntity;
import com.leo.gulimall.product.service.BrandService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Configuration
@SpringBootTest
@ComponentScan(basePackages = {"com.leo.gulimall.product.service"})
public class GulimallProductApplicationTests {


    @Autowired
    BrandService brandService;
    @Test
    public void testUpload() {
        TreeMap<String, Object> config = new TreeMap<String, Object>();


        try {
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            String secretId = System.getenv("AKID582sgSBufS8elpvwz24iO1UtOuQqGYff");//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String secretKey = System.getenv("2zwngbaSDrLoBiLtHWk6Zu4ze64zxx59");//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            // 替换为您的云 api 密钥 SecretId
            config.put("secretId", secretId);
            // 替换为您的云 api 密钥 SecretKey
            config.put("secretKey", secretKey);


            // 设置域名:
            // 如果您使用了腾讯云 cvm，可以设置内部域名
            //config.put("host", "sts.internal.tencentcloudapi.com");


            // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
            config.put("durationSeconds", 1800);


            // 换成您的 bucket
            config.put("bucket", "examplebucket-1250000000");
            // 换成 bucket 所在地区
            config.put("region", "ap-guangzhou");


            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
            // 列举几种典型的前缀授权场景：
            // 1、允许访问所有对象："*"
            // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
            // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefixes", new String[] {
                    "exampleobject",
                    "exampleobject2"
            });


            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请参见 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分块上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);


            JSONObject credential = CosStsClient.getCredential(config);
            System.out.println(credential.toString());
//            System.out.println(credential.getJSONObject("tmpSecretId"));
//            System.out.println(credential.getJSONObject("tmpSecretKey"));
//            System.out.println(credential.getJSONObject("credentials.sessionToken"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }


    }
    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("魅族");
//        brandService.save(brandEntity);
//        log.info("保存成功，{}",brandEntity.toString());

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",9L));
        list.forEach((item)->{log.info("品牌id查询，{}",item);
            System.out.println(item);});
    }

}
