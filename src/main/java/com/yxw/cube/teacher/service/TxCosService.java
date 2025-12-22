package com.yxw.cube.teacher.service;

import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;

import java.util.ArrayList;
import java.util.List;

public class TxCosService {

    private static final String TEMP_SECRET_ID = "AKIDW5ZGzwn49lsriTx_4JTc5W57stjq6yDbb0eUV3lLESz-9ag9drOts0CduhB25gQX";
    private static final String TEMP_SECRET_KEY = "6tmamOx2ri238rP1+DVugvKlNFSVVZwLz3W3A7ZwG0s=";
    private static final String SESSION_TOKEN = "trSRfZuk2wWmVmS3mEwq6dd1GwbxOFfa8797d827c714ecc2d1cafad695bcd931-wyXd995PnxK7eQuJoCVFlM0WLIfDyQhnZdpmeRZZPWpzInrkVT-_9EUqnXSbFfS_Xzt-czNl-lZ7jRBIHZOqOwwYj4KltbdeUs0ZwykvjrJsyiyjvvXvTZCRYwJE4FMiVA2atSUH6RHkfbHpd_qF-ujTKKzbhKizf6hv5DSK4-psVJ59hmxX0hAoqeaRoT7g1xcWbpDjK24mhC5uPBM1MOFbylCfyjfWIRgGzFIVw-LXfllAkfvspoZqGQVr93B3dP5XPJVYhsUh9iEVRhkZMlVMbxX0IGR6QRT5TxXmSHHNh1gzBp9gGk983URDRur41XDl6cdiqrDiQg3m5FOsZ7-gspO1CvqA_3wQNYJSxH7fpsLaGEOXMZANkyy54visb5iH0OcAqJa1SWUuyALOQ";
    private static final String BUCKET_NAME = "boway-spark-1258344699";
    private static final String REGION_NAME = "ap-guangzhou";
    private static final String PREFIX_PATH = "100720251103181645009601/100820251103183902001048/";


    public static void main(String[] args) {
        COSClient cosClient = null;
        try {
            // 1. 初始化凭证对象
            BasicSessionCredentials cred = new BasicSessionCredentials(
                    TEMP_SECRET_ID,
                    TEMP_SECRET_KEY,
                    SESSION_TOKEN
            );

            // 2. 配置客户端 (设置地域)
            ClientConfig clientConfig = new ClientConfig(new Region(REGION_NAME));

            // 3. 创建 COSClient 实例
            cosClient = new COSClient(cred, clientConfig);

            System.out.println("COSClient 初始化成功。开始 ListObjects 测试...");

            // 4. 执行 ListObjects 请求 (重点测试权限)
            ListObjectsRequest listRequest = new ListObjectsRequest();
            listRequest.setBucketName(BUCKET_NAME);
            listRequest.setPrefix(PREFIX_PATH);

            String customPrefix = "100720251103181645009601/100820251103183902001048/20251216/sql/sqlData/828/";
            listRequest.setPrefix(customPrefix);

            // 只列出前 10 个以快速测试
            listRequest.setMaxKeys(100);

            ObjectListing objectListing;
            List<COSObjectSummary> objects = new ArrayList<>();

            //do {
            //    objectListing = cosClient.listObjects(listRequest);
            //    objectListing.getObjectSummaries().forEach(data -> {
            //        if (data.getKey().endsWith(".csv") && data.getSize() > 0) {
            //            objects.add(data);
            //        }
            //    });
            //    listRequest.setMarker(objectListing.getNextMarker());
            //} while (objectListing.isTruncated());

            while (true){
                objectListing = cosClient.listObjects(listRequest);
                objectListing.getObjectSummaries().forEach(data -> {
                    if (data.getKey().endsWith(".csv") && data.getSize() > 0) {
                        objects.add(data);
                    }
                });
                if (!objectListing.isTruncated()) {
                    break;
                }
                listRequest.setMarker(objectListing.getNextMarker());
            }

            System.out.println(JSONUtil.toJsonPrettyStr(objects));

            // 5. 成功输出
            System.out.println("✅ 测试成功！成功列出对象。");
            System.out.printf("本次返回对象数: %d\n", objects.size());
            if (!objects.isEmpty()) {
                System.out.printf("首个对象 Key: %s\n", objects.get(0).getKey());
            }

        } catch (CosServiceException e) {
            // 捕获服务错误 (如 403 AccessDenied)
            System.err.println("❌ COS Service Error: 签名或权限问题");
            System.err.printf("Code: %s, Message: %s\n", e.getErrorCode(), e.getErrorMessage());
            System.err.printf("Request ID: %s\n", e.getRequestId());
            if (e.getErrorCode().equalsIgnoreCase("AccessDenied")) {
                System.err.println("【核心问题】本地测试确认 AccessDenied，问题一定出在 STS 策略上！");
            }
        } catch (CosClientException e) {
            // 捕获客户端错误 (如网络、配置错误)
            System.err.println("❌ COS Client Error: 客户端或网络问题");
            System.err.println("Message: " + e.getMessage());
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
                System.out.println("COSClient 关闭。");
            }
        }
    }
}
