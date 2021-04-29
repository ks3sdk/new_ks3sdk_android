package com.ksyun.ks3.services.request;

import com.lzy.okgo.https.HttpsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class OkHttpUtils {

    /**
     * 构造函数初始化
     */
    private OkHttpUtils(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        builder.connectTimeout(10,TimeUnit.SECONDS);//连接超时
        builder.writeTimeout(60,TimeUnit.SECONDS);//写入超时

        //协议
        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTP_1_1);
        protocols.add(Protocol.HTTP_2);


        builder.protocols(protocols);

        //ssl
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        builder.hostnameVerifier(new HostnameVerifier() {
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        builder.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager);

        //cookie 自动存储
        builder.cookieJar(new CookieJar() {
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(),cookies);
            }

            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });

        mOkHttpClient = builder.build();
    }
}

