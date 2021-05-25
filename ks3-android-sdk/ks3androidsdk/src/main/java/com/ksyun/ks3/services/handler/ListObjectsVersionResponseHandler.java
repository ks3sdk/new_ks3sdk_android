package com.ksyun.ks3.services.handler;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.Ks3ObjectSummary;
import com.ksyun.ks3.model.ObjecVersiontListing;
import com.ksyun.ks3.model.ObjectListing;
import com.ksyun.ks3.model.Owner;
import com.ksyun.ks3.util.DateUtil;
import com.ksyun.ks3.util.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public abstract class ListObjectsVersionResponseHandler extends Ks3HttpResponceHandler {
    private boolean isCommonPrefixes = false;

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, ObjecVersiontListing objectListing);

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

    private ObjecVersiontListing parseXml(Header[] responceHeaders, byte[] response) {
        ObjecVersiontListing objectListing = null;
        List<Ks3ObjectSummary> objectSummarys = null;
        Ks3ObjectSummary objectSummary = null;
        Owner owner = null;
        List<String> commonPrefixes = null;
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            parse.setInput(new ByteArrayInputStream(response), "UTF-8");
            int eventType = parse.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType) {
                String nodeName = parse.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        objectListing = new ObjecVersiontListing();
                        objectSummarys = new ArrayList<Ks3ObjectSummary>();
                        commonPrefixes = new ArrayList<String>();
                        break;
                    case XmlPullParser.END_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if ("ListBucketResult".equalsIgnoreCase(nodeName)) {
                        }
                        if ("Name".equalsIgnoreCase(nodeName)) {
                            objectListing.setBucketName(parse.nextText());
                        }
                        if ("Prefix".equalsIgnoreCase(nodeName)) {
                            if (isCommonPrefixes)
                                commonPrefixes.add(parse.nextText());
                            else
                                objectListing.setPrefix(parse.nextText());
                        }
                        if ("KeyMarker".equalsIgnoreCase(nodeName)) {
                            objectListing.setKeymarker(parse.nextText());
                        }
                        if ("VersionIdMarker".equalsIgnoreCase(nodeName)) {
                            objectListing.setVersionIdMarker(parse.nextText());
                        }
                        if ("MaxKeys".equalsIgnoreCase(nodeName)) {
                            objectListing.setMaxKeys(Integer.valueOf(parse.nextText()));
                        }
                        if ("NextKeyMarker".equalsIgnoreCase(nodeName)) {
                            objectListing.setNextKeyMarker(parse.nextText());
                        }
                        if ("NextVersionIdMarker".equalsIgnoreCase(nodeName)) {
                            objectListing.setNextVersionIdMarker(parse.nextText());
                        }
                        if ("Delimiter".equalsIgnoreCase(nodeName)) {
                            objectListing.setDelimiter(parse.nextText());
                        }
                        if ("IsTruncated".equalsIgnoreCase(nodeName)) {
                            objectListing.setTruncated(Boolean.valueOf(parse.nextText()));
                        }
                        if ("Version".equalsIgnoreCase(nodeName)) {
                            objectSummary = new Ks3ObjectSummary();
                        }
                        if ("DeleteMarker".equalsIgnoreCase(nodeName)) {
                            objectSummary = new Ks3ObjectSummary();
                            objectSummary.setDeleteMarker(true);
                        }
                        if ("Key".equalsIgnoreCase(nodeName)) {
                            String keyString = parse.nextText();
                            objectSummary.setKey(keyString);
                        }
                        if ("ETag".equalsIgnoreCase(nodeName)) {
                            String etag = parse.nextText();
                            objectSummary.setETag(etag);
                        }
                        if ("Size".equalsIgnoreCase(nodeName)) {
                            objectSummary.setSize(Long.valueOf(parse.nextText()));
                        }
                        if ("Owner".equalsIgnoreCase(nodeName)) {
                            owner = new Owner();
                        }
                        if ("ID".equalsIgnoreCase(nodeName)) {
                            owner.setId(parse.nextText());
                        }
                        if ("DisplayName".equalsIgnoreCase(nodeName)) {
                            owner.setDisplayName(parse.nextText());
                        }
                        if ("StorageClass".equalsIgnoreCase(nodeName)) {
                            objectSummary.setStorageClass(parse.nextText());
                        }
                        if ("LastModified".equalsIgnoreCase(nodeName)) {
                            String dateStr = parse.nextText();
                            if (!StringUtils.isBlank(dateStr))
                                objectSummary.setLastModified(DateUtil.ConverToDate(dateStr));
                        }
                        if ("CommonPrefixes".equalsIgnoreCase(nodeName)) {
                            isCommonPrefixes = true;
                        }
                        if ("VersionId".equalsIgnoreCase(nodeName)) {
                            objectSummary.setVersionId(parse.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Version".equalsIgnoreCase(nodeName) || "DeleteMarker".equalsIgnoreCase(nodeName)) {
                            objectSummarys.add(objectSummary);
                        }
                        if ("Owner".equalsIgnoreCase(nodeName)) {
                            objectSummary.setOwner(owner);
                        }
                        if ("CommonPrefixes".equalsIgnoreCase(nodeName)) {
                            objectListing.setCommonPrefixes(commonPrefixes);
                        }
                        if ("ListVersionsResult".equalsIgnoreCase(nodeName)) {
                            objectListing.setObjectSummaries(objectSummarys);
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
        return objectListing;
    }

}
