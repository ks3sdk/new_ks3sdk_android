package com.ksyun.ks3.services.request.object;

import android.text.TextUtils;
import android.util.Log;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.ObjectMetadata;
import com.ksyun.ks3.model.acl.AccessControlList;
import com.ksyun.ks3.model.acl.CannedAccessControlList;
import com.ksyun.ks3.model.acl.Grant;
import com.ksyun.ks3.model.acl.Permission;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.services.request.tag.ObjectTagging;
import com.ksyun.ks3.util.Constants;
import com.ksyun.ks3.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.notNull;

public class PutObjectFetchRequest extends Ks3HttpObjectRequest{
    private static final long serialVersionUID = 8391263314427812457L;
    private ObjectMetadata objectMeta = new ObjectMetadata();
    private CannedAccessControlList cannedAcl;
    private AccessControlList acl = new AccessControlList();
    private String redirectLocation;
    private String callBackUrl;
    private String callBackBody;
    private Map<String, String> callBackHeaders;


    /**
     * 数据处理任务完成后通知的url
     */
    private String notifyURL;

    private String sourceUrl;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public PutObjectFetchRequest(String bucketname, String key, String sourceUrl) {
        this.setBucketname(bucketname);
        this.setObjectkey(key);
        this.setSourceUrl(sourceUrl);
    }
    public PutObjectFetchRequest(String bucketname, String key, String sourceUrl, ObjectTagging objectTagging) {
        this(bucketname, key, sourceUrl);
        if (objectTagging != null && objectTagging.getTagSet() != null && objectTagging.getTagSet().size() > 0) {
            this.setTagging(objectTagging);
        }
    }
    public PutObjectFetchRequest(String bucketname, String key, String sourceUrl, ObjectMetadata metadata) {
        this(bucketname, key, sourceUrl);
        this.setObjectMeta(metadata == null ? this.objectMeta : metadata);
        if (metadata.getTagging() != null && metadata.getTagging().getTagSet() != null && metadata.getTagging().getTagSet().size() > 0) {
            this.setTagging(metadata.getTagging());
        }
    }

    public PutObjectFetchRequest(String bucketname, String key, String sourceUrl, ObjectMetadata metadata, ObjectTagging objectTagging) {
        this(bucketname, key, sourceUrl);
        this.setObjectMeta(metadata == null ? this.objectMeta : metadata);
        if (objectTagging != null && objectTagging.getTagSet() != null && objectTagging.getTagSet().size() > 0) {
            this.setTagging(objectTagging);
        }
    }


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

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("fetch", "");
        this.addHeader(HttpHeaders.XKssSourceUrl, this.sourceUrl);
        if (!StringUtils.isBlank(this.callBackUrl) && !StringUtils.isBlank(this.callBackBody)) {
            this.addHeader(HttpHeaders.XKssCallBackUrl, this.callBackUrl);
            this.addHeader(HttpHeaders.XKssCallBackBody, this.callBackBody);

            if (this.callBackHeaders != null && this.callBackHeaders.size() > 0) {
                for (Map.Entry<String, String> entry : this.callBackHeaders.entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    if (!StringUtils.isBlank(key) && key.startsWith(Constants.CALL_BACK_CUSTOM_PREFIX) && !StringUtils.isBlank(val)) {
                        this.addHeader(key, val);
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
                this.addHeader(entry.getKey().toString(), entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : this.objectMeta.getUserMetadata()
                .entrySet()) {
            if (entry.getKey().startsWith(ObjectMetadata.userMetaPrefix))
                this.addHeader(entry.getKey(), entry.getValue());
        }
        if (this.cannedAcl != null) {
            this.addHeader(HttpHeaders.CannedAcl.toString(),
                    cannedAcl.toString());
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
                this.addHeader(HttpHeaders.GrantFullControl,
                        TextUtils.join(",", grants_fullcontrol));
            }
            if (grants_read.size() > 0) {
                this.addHeader(HttpHeaders.GrantRead,
                        TextUtils.join(",", grants_read));
            }
            if (grants_write.size() > 0) {
                this.addHeader(HttpHeaders.GrantWrite,
                        TextUtils.join(",", grants_write));
            }
        }
        if (this.redirectLocation != null) {
            this.addHeader(HttpHeaders.XKssWebsiteRedirectLocation,
                    this.redirectLocation);
        }
        this.setTagHeader();
    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        if (StringUtils.isBlank(this.getObjectkey()))
            throw new Ks3ClientException("object key can not be null");
        if (StringUtils.isBlank(this.sourceUrl)) {
            throw notNull("sourceUrl");
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

        return null;
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
}
