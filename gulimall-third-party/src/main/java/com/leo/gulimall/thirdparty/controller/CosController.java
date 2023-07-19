package com.leo.gulimall.thirdparty.controller;

import com.alibaba.fastjson.JSON;
import com.leo.common.utils.R;
import com.leo.gulimall.thirdparty.utils.ConstructionUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("third_party/file")
public class CosController {

    @RequestMapping("/upload")
    public R upload(MultipartFile file) {

        String secretId = ConstructionUtils.ACCESS_KEY_ID;//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        String secretKey = ConstructionUtils.ACCESS_KEY_SECRET;//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(ConstructionUtils.END_POINT);
        ClientConfig clientConfig = new ClientConfig(region);

        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(cred, clientConfig);


//        File localFile = new File("C:\\Users\\user\\Desktop\\呆唯3.jpg");

        String bucketName = ConstructionUtils.BUCKET_NAME;
        //在文件名称的前面添加UUID,保证都是唯一的
        String key = UUID.randomUUID().toString().replaceAll("-","")+file.getName();
        //对上传文件分组
        String dateTime = new DateTime().toString("yyyy/MM/dd");
        key = dateTime+"/"+key;
        try {
            //获取上传文件的输入流
            InputStream inputStream = null;
            inputStream = file.getInputStream();


            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);

            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//            System.out.println(JSON.toJSONString(putObjectResult));
            //需要返回路径https://gulimall-1307858615.cos.ap-nanjing.myqcloud.com/KON.jpg
            String url = "https://"+bucketName+"."+"cos."+ConstructionUtils.END_POINT+".myqcloud.com/"+key;
            return R.ok().put("returnUrl",url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.error();
    }


}
