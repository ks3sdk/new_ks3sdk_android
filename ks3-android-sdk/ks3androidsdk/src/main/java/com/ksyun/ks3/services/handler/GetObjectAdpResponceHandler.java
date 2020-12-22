package com.ksyun.ks3.services.handler;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.services.request.adp.AdpTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public abstract class GetObjectAdpResponceHandler extends Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, AdpTask adpTask);


    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseaXML(response));
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

    private AdpTask  parseaXML(byte[] response) {
        XmlPullParserFactory factory;
        AdpTask adpTask = null;
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
                        if ("Task".equalsIgnoreCase(nodeName)) {
                            adpTask = new AdpTask();
                        }
                        if ("taskid".equalsIgnoreCase(nodeName)) {
                            adpTask.setTaskId(parse.nextText());
                        }
                        if ("processstatus".equalsIgnoreCase(nodeName)) {
                            adpTask.setProcessstatus(parse.nextText());
                        }
                        if ("processdesc".equalsIgnoreCase(nodeName)) {
                            adpTask.setProcessdesc(parse.nextText());
                        }
                        if ("notifystatus".equalsIgnoreCase(nodeName)) {
                            adpTask.setNotifystatus(parse.nextText());
                        }
                        if ("notifydesc".equalsIgnoreCase(nodeName)) {
                            adpTask.setNotifydesc(parse.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (nodeName.equalsIgnoreCase("Task")) {

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
        return adpTask;
    }
}
