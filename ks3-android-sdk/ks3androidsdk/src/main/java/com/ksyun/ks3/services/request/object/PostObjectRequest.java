package com.ksyun.ks3.services.request.object;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.ksyun.ks3.auth.AuthUtils;
import com.ksyun.ks3.auth.DefaultSigner;
import com.ksyun.ks3.auth.MD5CalculateAble;
import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.Mimetypes;
import com.ksyun.ks3.model.ObjectMetadata;
import com.ksyun.ks3.model.PostObjectFormFields;
import com.ksyun.ks3.model.acl.AccessControlList;
import com.ksyun.ks3.model.acl.Authorization;
import com.ksyun.ks3.model.acl.CannedAccessControlList;
import com.ksyun.ks3.model.acl.Grant;
import com.ksyun.ks3.model.acl.Permission;
import com.ksyun.ks3.model.transfer.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.model.transfer.RepeatableFileInputStream;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.services.request.tag.ObjectTag;
import com.ksyun.ks3.services.request.tag.ObjectTagging;
import com.ksyun.ks3.util.Constants;
import com.ksyun.ks3.util.LengthCheckInputStream;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.StringUtils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostObjectRequest extends Ks3HttpObjectRequest implements
        MD5CalculateAble {
    private static final long serialVersionUID = 8398633676278496457L;
    private File file;
    private ObjectMetadata objectMeta = new ObjectMetadata();
    private CannedAccessControlList cannedAcl;
    private AccessControlList acl = new AccessControlList();
    private String redirectLocation;
    private String callBackUrl;
    private String callBackBody;
    private Map<String, String> callBackHeaders;

    public Authorization auth;

    public PostObjectFormFields getFields() {
        return fields;
    }

    public void setFields(PostObjectFormFields fields) {
        this.fields = fields;
    }

    public PostObjectFormFields fields;
    /**
     * 数据处理任务完成后通知的url
     */
    private String notifyURL;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private InputStream inputStream;

    public PostObjectRequest(String bucketname, String key, File file, PostObjectFormFields fields) {
        this.setBucketname(bucketname);
        this.setObjectkey(key);
        this.setFile(file);
        this.setFields(fields);
    }

//    public PostObjectRequest(String bucketname, String key, File file, ObjectMetadata metadata) {
//        this(bucketname, key, file,null);
//        this.setObjectMeta(metadata == null ? this.objectMeta : metadata);
//        if (metadata.getTagging() != null && metadata.getTagging().getTagSet() != null && metadata.getTagging().getTagSet().size() > 0) {
//            this.setTagging(metadata.getTagging());
//        }
//    }

    public void setCallBack(String callBackUrl, String callBackBody, Map<String, String> callBackHeaders, String notifyURL) {
        this.callBackUrl = callBackUrl;
        this.callBackBody = callBackBody;
        this.callBackHeaders = callBackHeaders;
        this.notifyURL = notifyURL;
    }

    public void setCallBack(String callBackUrl, String callBackBody, Map<String, String> callBackHeaders) {
        this.callBackUrl = callBackUrl;
        this.callBackBody = callBackBody;
        this.callBackHeaders = callBackHeaders;
    }


    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.POST);
        this.addHeader("Content-Type", "multipart/form-data");
        this.addParams("key", this.getObjectkey());

        if (!StringUtils.isBlank(this.callBackUrl) && !StringUtils.isBlank(this.callBackBody)) {
            this.addParams(HttpHeaders.XKssCallBackUrl.toString(), this.callBackUrl);
            this.addParams(HttpHeaders.XKssCallBackBody.toString(), this.callBackBody);

            if (this.callBackHeaders != null && this.callBackHeaders.size() > 0) {
                for (Map.Entry<String, String> entry : this.callBackHeaders.entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    if (!StringUtils.isBlank(key) && key.startsWith(Constants.CALL_BACK_CUSTOM_PREFIX) && !StringUtils.isBlank(val)) {
                        this.addParams(key, val);
                    } else {
                        Log.e(Constants.LOG_TAG, "the header:" + key + "-" + val + " is not correct ,this head will be ignored");
                    }
                }
            } else {
                Log.d(Constants.LOG_TAG, "the callbackheaders is null");
            }
        } else {
            Log.d(Constants.LOG_TAG, "the callbacurl or callbackbody is null , ignore set the callback");
        }

        for (Map.Entry<ObjectMetadata.Meta, String> entry : this.objectMeta.getMetadata()
                .entrySet()) {
            if (!entry.getKey().equals(ObjectMetadata.Meta.ContentLength.toString())) {
                this.addParams(entry.getKey().toString(), entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : this.objectMeta.getUserMetadata()
                .entrySet()) {
            if (entry.getKey().startsWith(ObjectMetadata.userMetaPrefix))
                this.addParams(entry.getKey(), entry.getValue());
        }
        if (this.cannedAcl != null) {
            this.addParams(HttpHeaders.CannedAcl.toString(), cannedAcl.toString());
        }
        if (this.acl != null) {
            List<String> grants_fullcontrol = new ArrayList<String>();
            List<String> grants_read = new ArrayList<String>();
            List<String> grants_write = new ArrayList<String>();
            for (Grant grant : acl.getGrants()) {
                if (grant.getPermission().equals(Permission.FullControl)) {
                    grants_fullcontrol.add("id=\""
                            + grant.getGrantee().getIdentifier() + "\"");
                } else if (grant.getPermission().equals(Permission.Read)) {
                    grants_read.add("id=\""
                            + grant.getGrantee().getIdentifier() + "\"");
                } else if (grant.getPermission().equals(Permission.Write)) {
                    grants_write.add("id=\""
                            + grant.getGrantee().getIdentifier() + "\"");
                }
            }
            if (grants_fullcontrol.size() > 0) {
                this.addParams(HttpHeaders.GrantFullControl.toString(), TextUtils.join(",", grants_fullcontrol));
            }
            if (grants_read.size() > 0) {
                this.addParams(HttpHeaders.GrantRead.toString(), TextUtils.join(",", grants_read));
            }
            if (grants_write.size() > 0) {
                this.addParams(HttpHeaders.GrantWrite.toString(), TextUtils.join(",", grants_write));
            }
        }
        if (this.redirectLocation != null) {
            this.addParams(HttpHeaders.XKssWebsiteRedirectLocation.toString(), this.redirectLocation);
        }
        if (this.getTagging() != null && this.getTagging().getTagSet() != null && this.getTagging().getTagSet().size() > 0) {
            XmlWriter writer = new XmlWriter();
            writer.start("Tagging");
            writer.start("TagSet");
            List<ObjectTag> tags = this.getTagging().getTagSet();
            StringBuffer stringBuffer = new StringBuffer();
            for (ObjectTag tag : tags) {
                writer.start("Tag");
                writer.start("Key").value(tag.getKey()).end();
                if (tag.getValue() != null) {
                    writer.start("Value").value(tag.getValue()).end();
                    stringBuffer.append(tag.getKey() + "=" + tag.getValue() + "&");
                }
            }
            if (stringBuffer.length() > 0) {
                String xKssObjectTagStr = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
                this.getParams().put(HttpHeaders.XKssObjectTag.toString(), xKssObjectTagStr);
            }
        }
        this.addParams(HttpHeaders.Authorization.toString(),
                new DefaultSigner().calculate(this.auth, this).trim());
    }

    @Override
    protected void validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        if (StringUtils.isBlank(this.getObjectkey()))
            throw new Ks3ClientException("object key can not be null");
        if (file == null && inputStream == null) {
            throw new Ks3ClientException("upload object can not be null");
        }

        if (this.acl != null && this.acl.getGrants() != null) {
            for (Grant grant : this.acl.getGrants()) {
                if (grant.getPermission() == null)
                    throw new Ks3ClientException("grant :" + grant.getGrantee()
                            + ",permission can not be null");
            }
        }
        if (this.redirectLocation != null) {
            if (!this.redirectLocation.startsWith("/")
                    && !this.redirectLocation.startsWith("http://")
                    && !this.redirectLocation.startsWith("https://"))
                throw new Ks3ClientException(
                        "redirectLocation should start with / http:// or https://");
        }

    }

    public File getFile() {
        return file;
    }

    private void setFile(File file) {
        this.file = file;
    }

    public ObjectMetadata getObjectMeta() {
        return objectMeta;
    }

    public void setObjectMeta(ObjectMetadata objectMeta) {
        this.objectMeta = objectMeta;
    }

    public CannedAccessControlList getCannedAcl() {
        return cannedAcl;
    }

    public void setCannedAcl(CannedAccessControlList cannedAcl) {
        this.cannedAcl = cannedAcl;
    }

    public AccessControlList getAcl() {
        return acl;
    }

    public void setAcl(AccessControlList acl) {
        this.acl = acl;
    }

    public String getRedirectLocation() {
        return redirectLocation;
    }

    public void setRedirectLocation(String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackBody() {
        return callBackBody;
    }

    public void setCallBackBody(String callBackBody) {
        this.callBackBody = callBackBody;
    }

    public Map<String, String> getCallBackHeaders() {
        return callBackHeaders;
    }

    public void setCallBackHeaders(Map<String, String> callBackHeaders) {
        this.callBackHeaders = callBackHeaders;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getMd5() {
        return Base64
                .encodeToString(
                        ((MD5DigestCalculatingInputStream) super.getRequestBody())
                                .getMd5Digest(), Base64.DEFAULT).trim();
    }

}
