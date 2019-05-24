package com.estatesoft.ris.wx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class OASsoHttpReqUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(OASsoHttpReqUtils.class);

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     *
     * @param  head
     *            sso登录需要的_head
     * @param param
     *            请求参数，请求参数为json 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String head, String param) {

        LOGGER.info("请求url为===> {}, 参数为===> {}", url , param);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");

            if(!StringUtils.isEmpty(head)){
                conn.setRequestProperty("_head", head);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                result += line;
            }

            LOGGER.info("接受http响应：===> {}", result);
        } catch (Exception e) {

            LOGGER.error("发送 POST 请求出现异常！"+e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

       return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param  head
     *             sso登录需要的_head
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String cookie, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;

            if(!StringUtils.isEmpty(param)){
                urlNameString += "?" + param;
            }

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");

            if(!StringUtils.isEmpty(cookie)){
                connection.setRequestProperty("Cookie", cookie);
            }
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
