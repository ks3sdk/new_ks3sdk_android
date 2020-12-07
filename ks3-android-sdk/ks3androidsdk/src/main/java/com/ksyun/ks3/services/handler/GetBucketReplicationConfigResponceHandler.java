package com.ksyun.ks3.services.handler;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.result.ReplicationRule;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public abstract class GetBucketReplicationConfigResponceHandler extends Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, ReplicationRule replicationRule);


    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseXml(responceHeaders, response));
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

    private ReplicationRule parseXml(cz.msebera.android.httpclient.Header[] responceHeaders, byte[] response) {
        XmlPullParserFactory factory;
        ReplicationRule replicationRule = null;
        List<String> prefixList = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            parse.setInput(new ByteArrayInputStream(response), "UTF-8");
            int eventType = parse.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType) {
                String nodeName = parse.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        prefixList = new ArrayList<>();
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("ns2:Replication".equalsIgnoreCase(nodeName)) {
                            replicationRule = new ReplicationRule();
                        }
                        if ("prefix".equalsIgnoreCase(nodeName)) {
                            prefixList.add(parse.nextText());
                            replicationRule.setPrefixList(prefixList);
                        }
                        if ("DeleteMarkerStatus".equalsIgnoreCase(nodeName)) {
                            if (parse.nextText().contentEquals("Enabled")) {
                                replicationRule.setDeleteMarkerStatus(true);
                            } else {
                                replicationRule.setDeleteMarkerStatus(false);
                            }
                        }
                        if ("targetBucket".equalsIgnoreCase(nodeName)) {
                            replicationRule.setTargetBucket(parse.nextText());
                        }
                        if ("region".equalsIgnoreCase(nodeName)) {
                            replicationRule.setRegion(parse.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (nodeName.equalsIgnoreCase("ns2:Replication")) {

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
        return replicationRule;
    }

}

