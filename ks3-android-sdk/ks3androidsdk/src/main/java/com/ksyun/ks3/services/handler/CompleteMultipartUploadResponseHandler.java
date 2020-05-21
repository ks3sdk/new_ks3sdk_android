package com.ksyun.ks3.services.handler;


import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.result.CompleteMultipartUploadResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public abstract class CompleteMultipartUploadResponseHandler extends
        Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, CompleteMultipartUploadResult result);


    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseXml(responceHeaders, response));
    }

    @Override
    public final void onFailure(int statesCode, Header[] responceHeaders, byte[] response, Throwable throwable) {
        Ks3Error error = new Ks3Error(statesCode, response, throwable);
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

    private CompleteMultipartUploadResult parseXml(Header[] responceHeaders, byte[] response) {
        XmlPullParserFactory factory;
        CompleteMultipartUploadResult result = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            parse.setInput(new ByteArrayInputStream(response), "UTF-8");
            int eventType = parse.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType) {
                String nodeName = parse.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        result = new CompleteMultipartUploadResult();
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Bucket".equalsIgnoreCase(nodeName)) {
                            result.setBucket(parse.nextText());
                        }
                        if ("Location".equalsIgnoreCase(nodeName)) {
                            result.setLocation(parse.nextText());
                        }
                        if ("Key".equalsIgnoreCase(nodeName)) {
                            result.setKey(parse.nextText());
                        }
                        if ("ETag".equalsIgnoreCase(nodeName)) {
                            result.seteTag(parse.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
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
        return result;
    }

}
