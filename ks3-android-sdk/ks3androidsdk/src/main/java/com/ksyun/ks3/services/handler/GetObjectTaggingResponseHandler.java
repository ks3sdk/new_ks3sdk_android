package com.ksyun.ks3.services.handler;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.services.request.tag.ObjectTag;
import com.ksyun.ks3.services.request.tag.ObjectTagging;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class GetObjectTaggingResponseHandler extends Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders,ObjectTagging tagging);

    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders,byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseXml(response));
    }

    @Override
    public final void onFailure(int statesCode, Header[] responceHeaders,byte[] response, Throwable throwable) {
        Ks3Error error = new Ks3Error(statesCode, response, throwable);
        onFailure(statesCode, error,responceHeaders, response == null ?"":new String(response), throwable);
    }

    @Override
    public final void onProgress(long bytesWritten, long totalSize) {}

    @Override
    public final void onStart() {}

    @Override
    public final void onFinish() {}

    @Override
    public final void onCancel() {}

    private ObjectTagging parseXml(byte[] response) {
        XmlPullParserFactory factory;
        ObjectTagging objectTagging = null;
        ArrayList<ObjectTag> tagList = null;
        ObjectTag tagging = null;
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
                        if ("Tagging".equalsIgnoreCase(nodeName)) {
                            objectTagging = new ObjectTagging();
                        }
                        if ("TagSet".equalsIgnoreCase(nodeName)) {
                            tagList = new ArrayList<>();
                            objectTagging.setTagSet(tagList);
                        }
                        if ("Tag".equalsIgnoreCase(nodeName)) {
                            tagging = new ObjectTag();
                            tagList.add(tagging);
                        }
                        if ("Key".equalsIgnoreCase(nodeName)) {
                            tagging.setKey(parse.nextText());
                        }
                        if ("Value".equalsIgnoreCase(nodeName)) {
                            tagging.setValue(parse.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (nodeName.equalsIgnoreCase("Tagging")) {

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
        return objectTagging;
    }
}
