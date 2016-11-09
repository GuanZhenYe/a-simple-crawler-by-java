package com.crawler.download;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guanzhenye on 2016/11/8.
 */
public class FileDownlaoder {

    //确定下载的文件名
    private static String getFileNameByUrl(String url, String contentType) {
        // 移除 "http://"
        url = url.substring(7);
        // 确认抓取到的页面为 text/html 类型
        if (contentType.indexOf("html") != -1) {
            // 把所有的url中的特殊符号转化成下划线
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
        } else {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + "."
                    + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
        System.out.println("fileName="+url);
        return url;
    }

    //保存文件
    private static void saveFileToLocal(String fileName,byte[] bytes) throws IOException {

        File dir=new File("G:\\crawler_temp");
        if(!dir.exists()){
            dir.mkdir();
        }

        File file=new File(dir,fileName);
        if(!file.exists()){
            file.createNewFile();
        }

        DataOutputStream dataOutputStream=new DataOutputStream(new FileOutputStream(file));
        for (int i=0;i<bytes.length;i++){
            dataOutputStream.write(bytes[i]);

        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    //下载文件
    public static void downloadFile(String url){

        CloseableHttpClient httpClient= HttpClients.createDefault();
        //get 请求
        HttpGet getMethod=new HttpGet(url);
        //请求超时时间
        RequestConfig requestConfig=RequestConfig.custom().setConnectTimeout(5000).build();
        getMethod.setConfig(requestConfig);
        byte[] bytes=new byte[1024];
        try {
            CloseableHttpResponse response=httpClient.execute(getMethod);
            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                Header[] headers= response.getAllHeaders();
                String contentType=null;
                for(int i=0;i<headers.length;i++){
                    //获取相应头的contentType
                    if("Content-Type".equals(headers[i].getName())){
                        contentType=headers[i].getValue();
                    }
                    System.out.println(headers[i].getName()+"  "+headers[i].getValue());
                }
                System.out.println("--------------------------------------");

                HttpEntity resEntity=response.getEntity();
                bytes=EntityUtils.toByteArray(resEntity);

                //文件名
                String fileName=getFileNameByUrl(url,contentType);

                //保存在本地
                saveFileToLocal(fileName,bytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            getMethod.releaseConnection();
        }
    }
}
