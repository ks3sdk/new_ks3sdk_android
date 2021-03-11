package com.ks3.demo.main;

import android.util.Log;

import com.ks3.demo.main.utils.DateUtils;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.ObjectMetadata;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ObjectMetadataBuilder {

    public static ObjectMetadata build(Map<String, List<String>> headers) {
        if (headers.isEmpty()) {
            Log.e("tag", "headers is empty");
            return null;
        } else if (headers.containsKey((Object) null)
                && !((List) headers.get((Object) null)).isEmpty()
                && !((String) ((List) headers.get((Object) null)).get(0)).matches("HTTP/\\d.\\d 2\\d\\d.*")) {
            Log.e("tag", "http request failed, " + (String) ((List) headers.get((Object) null)).get(0));
            return null;
        } else {
            ObjectMetadata meta = new ObjectMetadata();
//            if (headers.containsKey(ObjectMetadata.Meta.XKssObjectTagCount.name())
////                    && !((List) headers.get(ObjectMetadata.Meta.XKssObjectTagCount.name())).isEmpty()) {
////                meta.setObjectTagCount(Long.valueOf((String) ((List) headers.get(ObjectMetadata.Meta.XKssObjectTagCount.name())).get(0)).longValue());
////            }
            if (headers.containsKey("Content-Length")
                    && !((List) headers.get("Content-Length")).isEmpty()) {
                meta.setContentLength(Long.valueOf((String) ((List) headers.get("Content-Length")).get(0)).longValue());
            }

            if (headers.containsKey("Content-Type")
                    && !((List) headers.get("Content-Type")).isEmpty()) {
                String contentType = (String) ((List) headers.get("Content-Type")).get(0);
                meta.setContentType(modifyContentType(contentType));
            }

            if (headers.containsKey("Content-Encoding")
                    && !((List) headers.get("Content-Encoding")).isEmpty()) {
                meta.setContentEncoding((String) ((List) headers.get("Content-Encoding")).get(0));
            }

            if (headers.containsKey("Content-Disposition")
                    && !((List) headers.get("Content-Disposition")).isEmpty()) {
                meta.setContentDisposition((String) ((List) headers.get("Content-Disposition")).get(0));
            }

            if (headers.containsKey("Content-Language")
                    && !((List) headers.get("Content-Language")).isEmpty()) {
                meta.addOrEditMeta(ObjectMetadata.Meta.ContentLanguage, (String) ((List) headers.get("Content-Language")).get(0));
            }

            if (headers.containsKey("ETag")) {
                meta.addOrEditMeta(ObjectMetadata.Meta.Etag, (String) ((List) headers.get("ETag")).get(0));
            }

            if (headers.containsKey("Cache-Control")
                    && !((List) headers.get("Cache-Control")).isEmpty()) {
                meta.setCacheControl((String) ((List) headers.get("Cache-Control")).get(0));
            }

            if (headers.containsKey("Expires")
                    && !((List) headers.get("Expires")).isEmpty()) {
                meta.addOrEditMeta(ObjectMetadata.Meta.Expires, (String) ((List) headers.get("Expires")).get(0));
            }

            if (headers.containsKey("Last-Modified")
                    && !((List) headers.get("Last-Modified")).isEmpty()) {
                try {
                    Date e = DateUtils.parseRfc822Date((String) ((List) headers.get("Last-Modified")).get(0));
                    meta.addOrEditMeta(ObjectMetadata.Meta.LastModified, e.toString());
                } catch (Exception var3) {
                    Log.e("tag", "Unknow date format:" + (String) ((List) headers.get("Last-Modified")).get(0));
                }
            } else {
                meta.addOrEditMeta(ObjectMetadata.Meta.LastModified, new Date().toString());
            }

            return meta;
        }
    }

    private static String modifyContentType(String contentType) {
        String realConentType = contentType;
        if (contentType.indexOf("charset=") != -1) {
            realConentType = contentType.substring(0,
                    contentType.indexOf("charset=") + "charset=".length())
                    + contentType.substring(contentType.indexOf("charset=") + "charset=".length()).toUpperCase();
            Log.i("tag", "ContentType contains charset, transfer to uppercase {}" + realConentType);
        }
        return realConentType;
    }

}
