package com.ksyun.ks3.services.handler;

import com.google.gson.Gson;
import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.result.BucketQuota;
import com.ksyun.ks3.model.result.ReplicationRule;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public abstract class GetBucketQuotaResponceHandler extends Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, BucketQuota quota);


    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseaXML(responceHeaders, response));
    }

    @Override
    public final void onFailure(int statesCode, Header[] responceHeaders, byte[] response, Throwable throwable) {
        com.ksyun.ks3.exception.Ks3Error error = new Ks3Error(statesCode, response, throwable);
        this.onFailure(statesCode, error, responceHeaders, response == null ? "" : new String(response), throwable);
    }

    @Override
    public final void onProgress(long bytesWritten, long totalSize) {
    }

    @Override
    public final void onStart() {
    }

    @Override
    public final void onFinish() {
    }

    @Override
    public final void onCancel() {
    }

    private BucketQuota parseaXML(cz.msebera.android.httpclient.Header[] responceHeaders, byte[] response) {
        XmlPullParserFactory factory;
        BucketQuota quota = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            parse.setInput(new ByteArrayInputStream(response), "UTF-8");
            int eventType = parse.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType) {
                String nodeName = parse.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Quota".equalsIgnoreCase(nodeName)) {
                            quota = new BucketQuota();
                        }
                        if ("StorageQuota".equalsIgnoreCase(nodeName)) {
                            quota.setStorageQuota(Long.parseLong(parse.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (nodeName.equalsIgnoreCase("Quota")) {

                        }
                        break;
                    case XmlPullParser.TEXT:

                        break;
                    default:
                        break;
                }
                eventType = parse.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quota;
    }
}
