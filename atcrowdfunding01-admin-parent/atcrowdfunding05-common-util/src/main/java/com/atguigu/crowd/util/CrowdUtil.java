package com.atguigu.crowd.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrowdUtil {


    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param endpoint        OSS 参数
     * @param accessKeyId     OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream     要上传的文件的输入流
     * @param bucketName      OSS 参数
     * @param bucketDomain    OSS 参数
     * @param originalName    要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(

        String endpoint, String accessKeyId, String accessKeySecret,

        InputStream inputStream, String bucketName, String bucketDomain, String originalName) {
            // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");

        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));

        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);

            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {

                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;

                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();

                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();

                // 当前方法返回失败
                return ResultEntity.failed(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 = "+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());

        } finally {
            if (ossClient != null) {

                // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }
    }

    /**
     * 给第三方短信接口传递参数，发送验证码
     *
     * @param phoneNum 接受验证手机号
     * @param appCode  Api身份验证码
     * @param sign     签名
     * @param skin     模板
     * @return
     */

    public static ResultEntity<String> sendCodeByShortMessage(

            // 接受验证手机号
            String phoneNum,

            // Api身份验证码
            String appCode,

            // 请求地址
            String host,

            // 请求方式
            String method,

            // 请求路径
            String path,

            // 签名
            String sign,

            // 模板
            String skin
    ) {
//        String host = "https://cdcxdxjk.market.alicloudapi.com";
//        String path = "/chuangxin/dxjk";
//        String method = "POST";
        //String appcode = "bf1e3c537cdc41558143747de9073058";
//        String appcode = "appCode";

        // 生成验证码
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {

            int random = (int) (Math.random() * 10);

            stringBuilder.append(random);
        }
        String code = stringBuilder.toString();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("phone", phoneNum);
        querys.put("templateId", sign);
        querys.put("variables", code);
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            String reasonPhrase = statusLine.getReasonPhrase();

            if (statusCode == 200) {

                return ResultEntity.successWithData(code);
            } else
                return ResultEntity.failed(reasonPhrase);


            //System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }

    }

    public static boolean judgeRequestType(HttpServletRequest request) {
        /**
         * 判断普通页面请求和ajax请求的是通过请求来判断
         */
        String acceptHeader = request.getHeader("Accept");
        String xRequested = request.getHeader("X-Requested-With");
        /**
         * 判断字符串的值是否为空
         * true 当前请求时Ajax请求
         * false  则不是
         */
        return (
                (acceptHeader != null && acceptHeader.contains("application/json"))
                        ||
                        (xRequested != null && xRequested.equals("XMLHttpRequest"))
        );
    }

    /**
     * 对明文进行md5加密
     *
     * @param source 传入的明文字符串
     * @return 加密好的数据
     */
    public static String md5(String source) {
        // 1:判断source是不是有效的字符串
        if (source == null || source.length() == 0) {

            // 2.不是有效的字符串抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATA);
        }
        // 3.获取messageDigest对象
        String algorithm = "md5";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 4.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            // 5.执行加密
            byte[] output = messageDigest.digest(input);

            // 6.创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);

            // 7.按照16进制将BigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
