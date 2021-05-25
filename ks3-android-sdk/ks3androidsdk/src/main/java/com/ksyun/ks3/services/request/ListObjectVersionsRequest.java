package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.StringUtils;

import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.notNull;

public class ListObjectVersionsRequest extends Ks3HttpObjectRequest {
    private static final long serialVersionUID = 7624709560043939375L;

    private String prefix;

    private String delimiter;

    private Integer maxKeys;

    private String encodingType;

    /**
     * 起始 key(不包含)
     */
    private String KeyMarker;
    /**
     * 起始版本号（不包含）
     */
    private String VersionIdMarker;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Integer getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(Integer maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getKeyMarker() {
        return KeyMarker;
    }

    public void setKeyMarker(String keyMarker) {
        KeyMarker = keyMarker;
    }

    public String getVersionIdMarker() {
        return VersionIdMarker;
    }

    public void setVersionIdMarker(String versionIdMarker) {
        VersionIdMarker = versionIdMarker;
    }

    /**
     * @param bucketName
     */
    public ListObjectVersionsRequest(String bucketName) {
        this(bucketName, null, null, null, null,null);
    }

    /**
     *
     * @param bucketName
     * @param prefix
     */
    public ListObjectVersionsRequest(String bucketName, String prefix) {
        this(bucketName, prefix, null, null, null,null);
    }

    /**
     *
     * @param bucket
     * @param prefix
     * @param delimiter
     * @param maxKeys
     * @param keyMarker
     * @param versionIdMarker
     */
    public ListObjectVersionsRequest(String bucket, String prefix, String keyMarker, String versionIdMarker, String delimiter, Integer maxKeys) {
        setBucketname(bucket);
        this.prefix = prefix;
        this.KeyMarker = keyMarker;
        this.VersionIdMarker = versionIdMarker;
        this.delimiter = delimiter;
        this.maxKeys = maxKeys;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("versions", "");
        this.addParams("prefix", prefix);
        this.addParams("delimiter", delimiter);
        this.addParams("key-marker", KeyMarker );
        this.addParams("version-id-marker", VersionIdMarker);
        if (maxKeys != null)
            this.addParams("max-keys", maxKeys.toString());
        if (!StringUtils.isBlank(this.encodingType))
            this.addParams("encoding-type", this.encodingType);
        this.addHeader(HttpHeaders.ContentType, "text/plain");
    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        if (this.maxKeys != null && (this.maxKeys > 1000 || this.maxKeys < 1))
            throw new Ks3ClientException(
                    "maxKeys should between 1 and 1000");
        if (this.VersionIdMarker != null && this.KeyMarker == null) {
            throw notNull("KeyMarker");
        }
        return "";
    }
    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

}
