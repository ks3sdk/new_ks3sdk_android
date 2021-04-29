package com.ksyun.ks3.services;

import java.io.File;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ks3.demo.main.utils.DateUtils;
import com.ksyun.ks3.auth.AuthUtils;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.Bucket;
import com.ksyun.ks3.model.ObjectListing;
import com.ksyun.ks3.model.ObjectMetadata;
import com.ksyun.ks3.model.PartETag;
import com.ksyun.ks3.model.PostObjectFormFields;
import com.ksyun.ks3.model.PostPolicy;
import com.ksyun.ks3.model.PostPolicyCondition;
import com.ksyun.ks3.model.acl.AccessControlList;
import com.ksyun.ks3.model.acl.AccessControlPolicy;
import com.ksyun.ks3.model.acl.Authorization;
import com.ksyun.ks3.model.acl.CannedAccessControlList;
import com.ksyun.ks3.model.result.CompleteMultipartUploadResult;
import com.ksyun.ks3.model.result.CopyResult;
import com.ksyun.ks3.model.result.GetObjectResult;
import com.ksyun.ks3.model.result.HeadObjectResult;
import com.ksyun.ks3.model.result.InitiateMultipartUploadResult;
import com.ksyun.ks3.model.result.ListPartsResult;
import com.ksyun.ks3.services.handler.AbortMultipartUploadResponseHandler;
import com.ksyun.ks3.services.handler.CompleteMultipartUploadResponseHandler;
import com.ksyun.ks3.services.handler.CopyObjectResponseHandler;
import com.ksyun.ks3.services.handler.CreateBucketResponceHandler;
import com.ksyun.ks3.services.handler.DeleteBucketReplicationConfigResponceHandler;
import com.ksyun.ks3.services.handler.DeleteBucketResponceHandler;
import com.ksyun.ks3.services.handler.DeleteObjectRequestHandler;
import com.ksyun.ks3.services.handler.GetBucketACLResponceHandler;
import com.ksyun.ks3.services.handler.GetBucketReplicationConfigResponceHandler;
import com.ksyun.ks3.services.handler.GetObjectACLResponseHandler;
import com.ksyun.ks3.services.handler.GetObjectResponseHandler;
import com.ksyun.ks3.services.handler.GetObjectTaggingResponseHandler;
import com.ksyun.ks3.services.handler.HeadBucketResponseHandler;
import com.ksyun.ks3.services.handler.HeadObjectResponseHandler;
import com.ksyun.ks3.services.handler.InitiateMultipartUploadResponceHandler;
import com.ksyun.ks3.services.handler.Ks3HttpResponceHandler;
import com.ksyun.ks3.services.handler.ListBucketsResponceHandler;
import com.ksyun.ks3.services.handler.ListObjectsResponseHandler;
import com.ksyun.ks3.services.handler.ListPartsResponseHandler;
import com.ksyun.ks3.services.handler.PutBucketACLResponseHandler;
import com.ksyun.ks3.services.handler.PutBucketReplicationResponceHandler;
import com.ksyun.ks3.services.handler.PutObjectACLResponseHandler;
import com.ksyun.ks3.services.handler.PutObjectResponseHandler;
import com.ksyun.ks3.services.handler.UploadPartResponceHandler;
import com.ksyun.ks3.services.request.AbortMultipartUploadRequest;
import com.ksyun.ks3.services.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.services.request.CopyObjectRequest;
import com.ksyun.ks3.services.request.CreateBucketRequest;
import com.ksyun.ks3.services.request.DeleteBucketPolicyRequest;
import com.ksyun.ks3.services.request.DeleteBucketQuotaRequest;
import com.ksyun.ks3.services.request.DeleteBucketReplicationConfigRequest;
import com.ksyun.ks3.services.request.DeleteBucketRequest;
import com.ksyun.ks3.services.request.DeleteObjectRequest;
import com.ksyun.ks3.services.request.adp.GetAdpRequest;
import com.ksyun.ks3.services.request.GetBucketACLRequest;
import com.ksyun.ks3.services.request.GetBucketPolicyRequest;
import com.ksyun.ks3.services.request.GetBucketQuotaRequest;
import com.ksyun.ks3.services.request.GetBucketReplicationConfigRequest;
import com.ksyun.ks3.services.request.GetObjectACLRequest;
import com.ksyun.ks3.services.request.GetObjectRequest;
import com.ksyun.ks3.services.request.HeadBucketRequest;
import com.ksyun.ks3.services.request.HeadObjectRequest;
import com.ksyun.ks3.services.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.services.request.ListBucketsRequest;
import com.ksyun.ks3.services.request.ListObjectsRequest;
import com.ksyun.ks3.services.request.ListPartsRequest;
import com.ksyun.ks3.services.request.PutBuckePolicyRequest;
import com.ksyun.ks3.services.request.PutBuckeQuotaRequest;
import com.ksyun.ks3.services.request.PutBucketACLRequest;
import com.ksyun.ks3.services.request.PutBucketReplicationConfigRequest;
import com.ksyun.ks3.services.request.PutObjectACLRequest;
import com.ksyun.ks3.services.request.PutObjectRequest;
import com.ksyun.ks3.services.request.UploadPartRequest;
import com.ksyun.ks3.services.request.adp.PutAdpRequest;
import com.ksyun.ks3.services.request.object.PutObjectFetchRequest;
import com.ksyun.ks3.services.request.tag.DeleteObjectTaggingRequest;
import com.ksyun.ks3.services.request.tag.GetObjectTaggingRequest;
import com.ksyun.ks3.services.request.tag.PutObjectTaggingRequest;
import com.ksyun.ks3.util.Base64;
import com.ksyun.ks3.util.ClientIllegalArgumentException;
import com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator;
import com.ksyun.ks3.util.Constants;
import com.ksyun.ks3.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.HeadRequest;
import com.lzy.okgo.request.OptionsRequest;
import com.lzy.okgo.request.PatchRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.TraceRequest;
import com.lzy.okgo.utils.HttpUtils;

import org.joda.time.DateTime;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Ks3Client implements Ks3 {
    private Ks3ClientConfiguration clientConfiguration;
    private String endpoint;
    public Authorization auth;
    private Ks3HttpExector client = new Ks3HttpExector();
    private Context context = null;
    public AuthListener authListener = null;
    public static Ks3Client mInstance = null;

    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    /**
     * 在入口全局初始化
     *
     * @return
     */
    public static Ks3Client initKs3Client(String accesskeyid, String accesskeysecret, Application context) {
        if (mInstance == null) {
            synchronized (Ks3Client.class) {
                if (mInstance == null) {
                    mInstance = new Ks3Client(accesskeyid, accesskeysecret, context);
                    mInstance.initOkGo(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 单例实现
     *
     * @return
     */
    public static Ks3Client getInstance() {
        return mInstance;
    }


    public Ks3Client(String accesskeyid, String accesskeysecret, Context context) {
        this(accesskeyid, accesskeysecret, Ks3ClientConfiguration
                .getDefaultConfiguration(), context);
    }

    public Ks3Client(String accesskeyid, String accesskeysecret,
                     Ks3ClientConfiguration clientConfiguration, Context context) {
        this.auth = new Authorization(accesskeyid, accesskeysecret);
        this.clientConfiguration = clientConfiguration;
        this.context = context;
    }

    public Ks3Client(Authorization auth, Context context) {
        this(auth, Ks3ClientConfiguration.getDefaultConfiguration(), context);
    }

    public Ks3Client(Authorization auth,
                     Ks3ClientConfiguration clientConfiguration, Context context) {
        this.auth = auth;
        this.clientConfiguration = clientConfiguration;
        this.context = context;
    }

    public Ks3Client(AuthListener listener, Context context) {
        this(listener, Ks3ClientConfiguration.getDefaultConfiguration(),
                context);
    }

    public Ks3Client(AuthListener listener,  Ks3ClientConfiguration clientConfiguration, Context context) {
        this.authListener = listener;
        this.clientConfiguration = clientConfiguration;
        this.context = context;
    }

    public Authorization getAuth() {
        return auth;
    }

    public void setAuth(Authorization auth) {
        this.auth = auth;
    }


    public void setConfiguration(Ks3ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    /**
     * 创建完Ks3Client后应调用此方法设置endpoint;
     * endpoint 取值范围请参考：
     * http://ks3.ksyun.com/doc/api/index.html  --Region（区域）
     *
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public AuthListener getAuthListener() {
        return authListener;
    }

    public void setAuthListener(AuthListener authListener) {
        this.authListener = authListener;
    }

    /* Service */
    @Override
    public void listBuckets(ListBucketsResponceHandler resultHandler) {
        listBuckets(new ListBucketsRequest(), resultHandler);
    }

    @Override
    public void listBuckets(ListBucketsRequest request,
                            ListBucketsResponceHandler resultHandler) {
        this.listBuckets(request, resultHandler, true);
    }

    private void listBuckets(ListBucketsRequest request,
                             ListBucketsResponceHandler resultHandler, boolean isUseAsyncMode) {
        invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    /* Bucket ACL */
    @Override
    public void getBucketACL(String bucketName,
                             GetBucketACLResponceHandler resultHandler) {
        this.getBucketACL(new GetBucketACLRequest(bucketName), resultHandler);
    }

    @Override
    public void getBucketACL(GetBucketACLRequest request,
                             GetBucketACLResponceHandler resultHandler) {
        this.getBucketACL(request, resultHandler, true);
    }

    private void getBucketACL(GetBucketACLRequest request,
                              GetBucketACLResponceHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void putBucketACL(String bucketName,
                             AccessControlList accessControlList,
                             PutBucketACLResponseHandler resultHandler) {
        this.putBucketACL(
                new PutBucketACLRequest(bucketName, accessControlList),
                resultHandler);
    }

    @Override
    public void putBucketACL(String bucketName,
                             CannedAccessControlList CannedAcl,
                             PutBucketACLResponseHandler resultHandler) {
        this.putBucketACL(new PutBucketACLRequest(bucketName, CannedAcl),
                resultHandler);
    }

    @Override
    public void putBucketACL(PutBucketACLRequest request,
                             PutBucketACLResponseHandler resultHandler) {
        this.putBucketACL(request, resultHandler, true);
    }

    private void putBucketACL(PutBucketACLRequest request,
                              PutBucketACLResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    /* Object ACL */
    @Override
    public void putObjectACL(String bucketName, String objectName,
                             CannedAccessControlList accessControlList,
                             PutObjectACLResponseHandler resultHandler) {
        this.putObjectACL(new PutObjectACLRequest(bucketName, objectName,
                accessControlList), resultHandler);
    }

    @Override
    public void putObjectACL(String bucketName, String objectName,
                             AccessControlList accessControlList,
                             PutObjectACLResponseHandler resultHandler) {
        this.putObjectACL(new PutObjectACLRequest(bucketName, objectName,
                accessControlList), resultHandler);
    }

    @Override
    public void putObjectACL(PutObjectACLRequest request,
                             PutObjectACLResponseHandler resultHandler) {
        this.putObjectACL(request, resultHandler, true);
    }

    private void putObjectACL(PutObjectACLRequest request,
                              PutObjectACLResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void getObjectACL(String bucketName, String ObjectName,
                             GetObjectACLResponseHandler resultHandler) {
        this.getObjectACL(new GetObjectACLRequest(bucketName, ObjectName),
                resultHandler);
    }

    @Override
    public void getObjectACL(GetObjectACLRequest request,
                             GetObjectACLResponseHandler resultHandler) {
        this.getObjectACL(request, resultHandler, true);
    }

    private void getObjectACL(GetObjectACLRequest request,
                              GetObjectACLResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    /* Bucket */
    @Override
    public void headBucket(String bucketname,
                           HeadBucketResponseHandler resultHandler) {
        this.headBucket(new HeadBucketRequest(bucketname), resultHandler);
    }

    @Override
    public void headBucket(HeadBucketRequest request,
                           HeadBucketResponseHandler resultHandler) {
        this.headBucket(request, resultHandler, true);
    }

    private void headBucket(HeadBucketRequest request,
                            HeadBucketResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public boolean bucketExists(String bucketname) {
        return false;
    }

    @Override
    public void createBucket(String bucketname,
                             CreateBucketResponceHandler resultHandler) {
        this.createBucket(new CreateBucketRequest(bucketname), resultHandler);
    }

    @Override
    public void createBucket(String bucketname, AccessControlList list,
                             CreateBucketResponceHandler resultHandler) {
        this.createBucket(new CreateBucketRequest(bucketname, list),
                resultHandler);
    }

    @Override
    public void createBucket(String bucketname, CannedAccessControlList list,
                             CreateBucketResponceHandler resultHandler) {
        this.createBucket(new CreateBucketRequest(bucketname, list),
                resultHandler);
    }

    @Override
    public void createBucket(CreateBucketRequest request,
                             CreateBucketResponceHandler resultHandler) {
        this.createBucket(request, resultHandler, true);
    }

    private void createBucket(CreateBucketRequest request,
                              CreateBucketResponceHandler resultHandler, boolean isUseAsyncMode) {
        invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void deleteBucket(String bucketname,
                             DeleteBucketResponceHandler resultHandler) {
        this.deleteBucket(new DeleteBucketRequest(bucketname), resultHandler);
    }

    @Override
    public void deleteBucket(DeleteBucketRequest request,
                             DeleteBucketResponceHandler resultHandler) {
        this.deleteBucket(request, resultHandler, true);
    }

    private void deleteBucket(DeleteBucketRequest request,
                              DeleteBucketResponceHandler resultHandler, boolean isUseAsyncMode) {
        invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    /* Object */
    @Override
    public void listObjects(String bucketname,
                            ListObjectsResponseHandler resultHandler) {
        this.listObjects(new ListObjectsRequest(bucketname), resultHandler);
    }

    @Override
    public void listObjects(String bucketname, String prefix,
                            ListObjectsResponseHandler resultHandler) {
        this.listObjects(new ListObjectsRequest(bucketname, prefix),
                resultHandler);
    }

    @Override
    public void listObjects(ListObjectsRequest request,
                            ListObjectsResponseHandler resultHandler) {
        this.listObjects(request, resultHandler, true);
    }

    private void listObjects(ListObjectsRequest request,
                             ListObjectsResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void deleteObject(String bucketname, String key,
                             DeleteObjectRequestHandler handler) {
        this.deleteObject(new DeleteObjectRequest(bucketname, key), handler);
    }

    @Override
    public void deleteObject(DeleteObjectRequest request,
                             DeleteObjectRequestHandler handler) {
        this.deleteObject(request, handler, true);
    }

    private void deleteObject(DeleteObjectRequest request,
                              DeleteObjectRequestHandler handler, boolean isUseAsyncMode) {
        this.invoke(auth, request, handler, isUseAsyncMode);
    }

    @Override
    public Ks3HttpRequest getObject(Context context, String bucketname,
                                    String key, GetObjectResponseHandler handler) {
        this.context = context;
        return this.getObject(new GetObjectRequest(bucketname, key), handler);
    }

    @Override
    public Ks3HttpRequest getObject(GetObjectRequest request,
                                    GetObjectResponseHandler handler) {
        return this.getObject(request, handler, true);
    }

    private Ks3HttpRequest getObject(GetObjectRequest request,
                                     GetObjectResponseHandler handler, boolean isUseAsyncMode) {
        return this.invoke(auth, request, handler, isUseAsyncMode);
    }


    @Override
    public Ks3HttpRequest putObject(String bucketname, String objectkey,
                                    File file, PutObjectResponseHandler handler) {
        return this.putObject(
                new PutObjectRequest(bucketname, objectkey, file), handler);
    }

    @Override
    public Ks3HttpRequest putObject(String bucketname, String objectkey,
                                    File file, ObjectMetadata objectmeta,
                                    PutObjectResponseHandler handler) {
        return this.putObject(new PutObjectRequest(bucketname, objectkey, file,
                objectmeta), handler);
    }

    @Override
    public Ks3HttpRequest putObject(PutObjectRequest request,
                                    PutObjectResponseHandler handler) {
        return this.putObject(request, handler, true);
    }

    private Ks3HttpRequest putObject(PutObjectRequest request,
                                     PutObjectResponseHandler handler, boolean isUseAsyncMode) {
        return this.invoke(auth, request, handler, isUseAsyncMode);
    }

    @Override
    public void headObject(String bucketname, String objectkey,
                           HeadObjectResponseHandler resultHandler) {
        this.headObject(new HeadObjectRequest(bucketname, objectkey),
                resultHandler);
    }

    @Override
    public void headObject(HeadObjectRequest request,
                           HeadObjectResponseHandler resultHandler) {
        this.headObject(request, resultHandler, true);
    }

    private void headObject(HeadObjectRequest request,
                            HeadObjectResponseHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void copyObject(String destinationBucket, String destinationObject,
                           String sourceBucket, String sourceKey,
                           CopyObjectResponseHandler handler) {
        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey);
        this.copyObject(request, handler);

    }

    @Override
    public void copyObject(String destinationBucket, String destinationObject,
                           String sourceBucket, String sourceKey,
                           CannedAccessControlList cannedAcl, CopyObjectResponseHandler handler) {
        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey, cannedAcl);
        this.copyObject(request, handler);

    }

    @Override
    public void copyObject(String destinationBucket, String destinationObject,
                           String sourceBucket, String sourceKey,
                           AccessControlList accessControlList,
                           CopyObjectResponseHandler handler) {

        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey, accessControlList);
        this.copyObject(request, handler);

    }

    @Override
    public void copyObject(CopyObjectRequest request,
                           CopyObjectResponseHandler handler) {
        this.copyObject(request, handler, true);
    }

    private void copyObject(CopyObjectRequest request,
                            CopyObjectResponseHandler handler, boolean isUseAsyncMode) {
        this.invoke(auth, request, handler, isUseAsyncMode);
    }

    /* MultiUpload */
    @Override
    public void initiateMultipartUpload(String bucketname, String objectkey,
                                        InitiateMultipartUploadResponceHandler resultHandler) {
        this.initiateMultipartUpload(new InitiateMultipartUploadRequest(
                bucketname, objectkey), resultHandler);
    }

    @Override
    public void initiateMultipartUpload(InitiateMultipartUploadRequest request,
                                        InitiateMultipartUploadResponceHandler resultHandler) {
        this.initiateMultipartUpload(request, resultHandler, true);
    }

    private void initiateMultipartUpload(
            InitiateMultipartUploadRequest request,
            InitiateMultipartUploadResponceHandler resultHandler,
            boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void uploadPart(String bucketName, String key, String uploadId,
                           File file, long offset, int partNumber, long partSize,
                           UploadPartResponceHandler resultHandler) {
        this.uploadPart(new UploadPartRequest(bucketName, key, uploadId, file,
                offset, partNumber, partSize), resultHandler);
    }

    @Override
    public void uploadPart(UploadPartRequest request,
                           UploadPartResponceHandler resultHandler) {
        this.uploadPart(request, resultHandler, true);
    }

    private void uploadPart(UploadPartRequest request,
                            UploadPartResponceHandler resultHandler, boolean isUseAsyncMode) {
        this.invoke(auth, request, resultHandler, isUseAsyncMode);
    }

    @Override
    public void completeMultipartUpload(String bucketname, String objectkey,
                                        String uploadId, List<PartETag> partETags,
                                        CompleteMultipartUploadResponseHandler handler) {
        this.completeMultipartUpload(new CompleteMultipartUploadRequest(
                bucketname, objectkey, uploadId, partETags), handler);
    }

    @Override
    public void completeMultipartUpload(ListPartsResult result,
                                        CompleteMultipartUploadResponseHandler handler) {
        this.completeMultipartUpload(
                new CompleteMultipartUploadRequest(result), handler);
    }

    @Override
    public void completeMultipartUpload(CompleteMultipartUploadRequest request,
                                        CompleteMultipartUploadResponseHandler handler) {
        this.completeMultipartUpload(request, handler, true);
    }

    private void completeMultipartUpload(
            CompleteMultipartUploadRequest request,
            CompleteMultipartUploadResponseHandler handler,
            boolean isUseAsyncMode) {
        this.invoke(auth, request, handler, isUseAsyncMode);
    }

    @Override
    public void abortMultipartUpload(String bucketname, String objectkey,
                                     String uploadId, AbortMultipartUploadResponseHandler handler) {
        this.abortMultipartUpload(new AbortMultipartUploadRequest(bucketname,
                objectkey, uploadId), handler);
    }

    @Override
    public void abortMultipartUpload(AbortMultipartUploadRequest request,
                                     AbortMultipartUploadResponseHandler handler) {
        this.abortMultipartUpload(request, handler, true);
    }

    private void abortMultipartUpload(AbortMultipartUploadRequest request,
                                      AbortMultipartUploadResponseHandler handler, boolean isUseAsyncMode) {
        this.invoke(auth, request, handler, isUseAsyncMode);
    }

    @Override
    public void listParts(String bucketname, String objectkey, String uploadId,
                          ListPartsResponseHandler handler) {
        this.listParts(new ListPartsRequest(bucketname, objectkey, uploadId),
                handler);
    }

    @Override
    public void listParts(String bucketname, String objectkey, String uploadId,
                          int maxParts, ListPartsResponseHandler handler) {
        this.listParts(new ListPartsRequest(bucketname, objectkey, uploadId,
                maxParts), handler);
    }

    @Override
    public void listParts(String bucketname, String objectkey, String uploadId,
                          int maxParts, int partNumberMarker, ListPartsResponseHandler handler) {
        this.listParts(new ListPartsRequest(bucketname, objectkey, uploadId,
                maxParts, partNumberMarker), handler);
    }

    @Override
    public void listParts(ListPartsRequest request,
                          ListPartsResponseHandler handler) {
        this.listParts(request, handler, true);
    }

    @Override
    public void putBucketCrr(PutBucketReplicationConfigRequest request,
                             PutBucketReplicationResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    @Override
    public void getBucketCrr(GetBucketReplicationConfigRequest request,
                             GetBucketReplicationConfigResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    @Override
    public void deleteBucketCrr(DeleteBucketReplicationConfigRequest request,
                                DeleteBucketReplicationConfigResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void putBucketPolicy(PutBuckePolicyRequest request,
                                Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void getBucketPolicy(GetBucketPolicyRequest request,
                                Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void deleteBucketPolicy(DeleteBucketPolicyRequest request,
                                   Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void putBucketQuota(PutBuckeQuotaRequest request,
                               Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void getBucketQuota(GetBucketQuotaRequest request,
                               Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void deleteBucketQuota(DeleteBucketQuotaRequest request,
                                  Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void putAdpTask(PutAdpRequest request, Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void getAdpTask(GetAdpRequest request, Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void putObjectTag(PutObjectTaggingRequest request, Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void getObjectTag(GetObjectTaggingRequest request, GetObjectTaggingResponseHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void deleteObjectTag(DeleteObjectTaggingRequest request,
                                Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }

    public void putObjectFetch(PutObjectFetchRequest request, Ks3HttpResponceHandler handler) {
        this.invoke(auth, request, handler, true);
    }


    private void listParts(ListPartsRequest request,
                           ListPartsResponseHandler handler, boolean isUseAsyncMode) {
        this.invoke(auth, request, handler, isUseAsyncMode);
    }


    /* Invoke asnyc http client */
    private Ks3HttpRequest invoke(Authorization auth, Ks3HttpRequest request,
                                  AsyncHttpResponseHandler resultHandler, boolean isUseAsyncMode) {
        client.invoke(auth, request, resultHandler, clientConfiguration,
                context, endpoint, authListener, isUseAsyncMode);
        return request;
    }

    public PostObjectFormFields getObjectFormFields(String bucket, String filename,
                                                    Map<String, String> postFormData, Map<String, String> unknowValueFormFiled) throws Ks3ClientException {
        if (StringUtils.isBlank(bucket))
            throw ClientIllegalArgumentExceptionGenerator.notNull("bucket");
        if (postFormData == null)
            postFormData = new HashMap<String, String>();
        if (unknowValueFormFiled == null)
            unknowValueFormFiled = new HashMap<String, String>();
        postFormData.put("bucket", bucket);
        PostPolicy policy = new PostPolicy();
        //签名将在五小时后过期
        policy.setExpiration(DateUtils.convertDate2Str(new DateTime().plusHours(5).toDate(), DateUtils.DATETIME_PROTOCOL.ISO8861));

        for (Map.Entry<String, String> entry : postFormData.entrySet()) {
            if (!Constants.postFormIgnoreFields.contains(entry.getKey())) {
                PostPolicyCondition condition = new PostPolicyCondition();
                condition.setMatchingType(PostPolicyCondition.MatchingType.eq);
                condition.setParamA("$" + entry.getKey());
                condition.setParamB(entry.getValue().replace("${filename}", filename));
                policy.getConditions().add(condition);
            }
        }
        for (Map.Entry<String, String> entry : unknowValueFormFiled.entrySet()) {
            if (!Constants.postFormIgnoreFields.contains(entry.getKey())) {
                PostPolicyCondition condition = new PostPolicyCondition();
                condition.setMatchingType(PostPolicyCondition.MatchingType.startsWith);
                condition.setParamA("$" + entry.getKey());
                condition.setParamB(entry.getValue());
                policy.getConditions().add(condition);
            }
        }
        return postObject(policy);
    }

    public PostObjectFormFields postObject(PostPolicy policy)
            throws Ks3ClientException {
        Map<String, Object> policyMap = new HashMap<String, Object>();
        policyMap.put("expiration", policy.getExpiration());

        List<List<String>> conditions = new ArrayList<List<String>>();
        for (PostPolicyCondition condition : policy.getConditions()) {
            List<String> conditionList = new ArrayList<String>();
            if (condition.getMatchingType() != PostPolicyCondition.MatchingType.contentLengthRange) {
                if (!condition.getParamA().startsWith("$")) {
                    condition.setParamA("$" + condition.getParamA());
                }
            } else {
                if (!StringUtils.checkLong(condition.getParamA()) || !StringUtils.checkLong(condition.getParamB())) {
                    throw new ClientIllegalArgumentException("contentLengthRange匹配规则的参数A和参数B都应该是Long型");
                }
            }
            conditionList.add(condition.getMatchingType().toString());
            //表单中的项是忽略大小写的
            if (condition.getMatchingType() != PostPolicyCondition.MatchingType.contentLengthRange && !Constants.postFormUnIgnoreCase.contains(condition.getParamA().substring(1))) {
                conditionList.add(condition.getParamA().toLowerCase());
            } else {
                conditionList.add(condition.getParamA());
            }
            conditionList.add(condition.getParamB());
            conditions.add(conditionList);
        }
        policyMap.put("conditions", conditions);
        String policyJson = StringUtils.object2json(policyMap);
        String policyBase64 = "";
        policyBase64 = new String(Base64.encode(policyJson.getBytes()));
        PostObjectFormFields fields = new PostObjectFormFields();
        fields.setKssAccessKeyId(auth.getAccessKeyId());
        fields.setPolicy(policyBase64);
        try {
            fields.setSignature(AuthUtils.calcSignature(auth.getAccessKeySecret(), policyBase64));
        } catch (SignatureException e) {
            throw new Ks3ClientException("计算签名出错", e);
        }
        return fields;
    }

    @Override
    public void pause(Context context) {
        client.pause(context);
    }

    @Override
    public void cancel(Context context) {
        client.cancel(context);
    }


    @Override
    public ArrayList<Bucket> syncListBuckets() throws Throwable {
        final ArrayList<Bucket> list = new ArrayList<Bucket>();

        final Throwable error = new Throwable();
        this.listBuckets(new ListBucketsRequest(),
                new ListBucketsResponceHandler() {

                    @Override
                    public void onSuccess(int statesCode,
                                          Header[] responceHeaders,
                                          ArrayList<Bucket> resultList) {
                        list.addAll(resultList);
                    }

                    @Override
                    public void onFailure(int statesCode, Ks3Error ks3Error,
                                          Header[] responceHeaders, String response,
                                          Throwable paramThrowable) {
                        error.initCause(paramThrowable);
                    }
                }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return list;
    }

    @Override
    public AccessControlPolicy syncGetBucketACL(String bucketName)
            throws Throwable {
        GetBucketACLRequest request = new GetBucketACLRequest(bucketName);
        return this.syncGetBucketACL(request);
    }

    @Override
    public AccessControlPolicy syncGetBucketACL(GetBucketACLRequest request)
            throws Throwable {

        final AccessControlPolicy policy = new AccessControlPolicy();
        final Throwable error = new Throwable();
        this.getBucketACL(request, new GetBucketACLResponceHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  AccessControlPolicy accessControlPolicy) {
                policy.setAccessControlList(accessControlPolicy
                        .getAccessControlList());
                policy.setGrants(accessControlPolicy.getGrants());
                policy.setOwner(accessControlPolicy.getOwner());

            }

            @Override
            public void onFailure(int statesCode, Ks3Error kseError,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return policy;
    }

    @Override
    public void syncPutBucketACL(String bucketName,
                                 AccessControlList accessControlList) throws Throwable {
        PutBucketACLRequest request = new PutBucketACLRequest(bucketName,
                accessControlList);
        this.syncPutBucketACL(request);

    }

    @Override
    public void syncPutBucketACL(String bucketName,
                                 CannedAccessControlList accessControlList) throws Throwable {
        PutBucketACLRequest request = new PutBucketACLRequest(bucketName,
                accessControlList);
        this.syncPutBucketACL(request);
    }

    public void syncPutBucketACL(PutBucketACLRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.putBucketACL(request, new PutBucketACLResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public void syncPutObjectACL(String bucketName, String objectKey,
                                 CannedAccessControlList accessControlList) throws Throwable {
        PutObjectACLRequest request = new PutObjectACLRequest(bucketName,
                objectKey, accessControlList);
        this.syncPutObjectACL(request);
    }

    @Override
    public void syncPutObjectACL(String bucketName, String objectKey,
                                 AccessControlList accessControlList) throws Throwable {
        PutObjectACLRequest request = new PutObjectACLRequest(bucketName,
                objectKey, accessControlList);
        this.syncPutObjectACL(request);
    }

    @Override
    public void syncPutObjectACL(PutObjectACLRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.putObjectACL(request, new PutObjectACLResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public AccessControlPolicy syncGetObjectACL(String bucketName,
                                                String objectKey) throws Throwable {
        GetObjectACLRequest request = new GetObjectACLRequest(bucketName,
                objectKey);
        return this.syncGetObjectACL(request);
    }

    @Override
    public AccessControlPolicy syncGetObjectACL(GetObjectACLRequest request)
            throws Throwable {

        final AccessControlPolicy policy = new AccessControlPolicy();
        final Throwable error = new Throwable();
        this.getObjectACL(request, new GetObjectACLResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  AccessControlPolicy accessControlPolicy) {
                policy.setAccessControlList(accessControlPolicy
                        .getAccessControlList());
                policy.setGrants(accessControlPolicy.getGrants());
                policy.setOwner(accessControlPolicy.getOwner());

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return policy;
    }

    @Override
    public void syncHeadBucket(String bucketName) throws Throwable {
        HeadBucketRequest request = new HeadBucketRequest(bucketName);
        this.syncHeadBucket(request);
    }

    @Override
    public void syncHeadBucket(HeadBucketRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.headBucket(request, new HeadBucketResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public void syncCreateBucket(String bucketName) throws Throwable {
        CreateBucketRequest request = new CreateBucketRequest(bucketName);
        this.syncCreateBucket(request);
    }

    @Override
    public void syncCreateBucket(String bucketName,
                                 CannedAccessControlList accessControlList) throws Throwable {
        CreateBucketRequest request = new CreateBucketRequest(bucketName,
                accessControlList);
        this.syncCreateBucket(request);
    }

    @Override
    public void syncCreateBucket(String bucketName,
                                 AccessControlList accessControlList) throws Throwable {
        CreateBucketRequest request = new CreateBucketRequest(bucketName,
                accessControlList);
        this.syncCreateBucket(request);
    }

    @Override
    public void syncCreateBucket(CreateBucketRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.createBucket(request, new CreateBucketResponceHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public void syncDeleteBucket(String bucketName) throws Throwable {
        DeleteBucketRequest request = new DeleteBucketRequest(bucketName);
        this.syncDeleteBucket(request);
    }

    @Override
    public void syncDeleteBucket(DeleteBucketRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.deleteBucket(request, new DeleteBucketResponceHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);

            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public ObjectListing syncListObjects(String bucketName) throws Throwable {
        ListObjectsRequest request = new ListObjectsRequest(bucketName);
        return syncListObjects(request);
    }

    @Override
    public ObjectListing syncListObjects(String bucketName, String prefix)
            throws Throwable {
        ListObjectsRequest request = new ListObjectsRequest(bucketName, prefix);
        return syncListObjects(request);
    }

    @Override
    public ObjectListing syncListObjects(ListObjectsRequest request)
            throws Throwable {

        final ObjectListing listing = new ObjectListing();
        final Throwable error = new Throwable();
        this.listObjects(request, new ListObjectsResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  ObjectListing objectListing) {
                listing.setBucketName(objectListing.getBucketName());
                listing.setCommonPrefixes(objectListing.getCommonPrefixes());
                listing.setDelimiter(objectListing.getDelimiter());
                listing.setMarker(objectListing.getMarker());
                listing.setMaxKeys(objectListing.getMaxKeys());
                listing.setNextMarker(objectListing.getNextMarker());
                listing.setObjectSummaries(objectListing.getObjectSummaries());
                listing.setPrefix(objectListing.getPrefix());

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return listing;
    }

    @Override
    public void syncDeleteObject(String bucketName, String objectKey)
            throws Throwable {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName,
                objectKey);
        this.syncDeleteObject(request);
    }

    @Override
    public void syncDeleteObject(DeleteObjectRequest request) throws Throwable {

        final Throwable error = new Throwable();
        this.deleteObject(request, new DeleteObjectRequestHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public HeadObjectResult syncHeadObject(String bucketName, String objectKey)
            throws Throwable {
        HeadObjectRequest request = new HeadObjectRequest(bucketName, objectKey);
        return this.syncHeadObject(request);
    }

    @Override
    public HeadObjectResult syncHeadObject(HeadObjectRequest request)
            throws Throwable {

        final HeadObjectResult result = new HeadObjectResult();
        final Throwable error = new Throwable();
        this.headObject(request, new HeadObjectResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  HeadObjectResult headObjectResult) {
                result.setETag(headObjectResult.getETag());
                result.setLastmodified(headObjectResult.getLastmodified());
                result.setObjectMetadata(headObjectResult.getObjectMetadata());

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);

            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return result;
    }

    @Override
    public CopyResult syncCopyObject(String destinationBucket,
                                     String destinationObject, String sourceBucket, String sourceKey)
            throws Throwable {
        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey);
        return this.syncCopyObject(request);
    }

    @Override
    public CopyResult syncCopyObject(String destinationBucket,
                                     String destinationObject, String sourceBucket, String sourceKey,
                                     CannedAccessControlList accessControlList) throws Throwable {
        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey, accessControlList);
        return this.syncCopyObject(request);
    }

    @Override
    public CopyResult syncCopyObject(String destinationBucket,
                                     String destinationObject, String sourceBucket, String sourceKey,
                                     AccessControlList accessControlList) throws Throwable {
        CopyObjectRequest request = new CopyObjectRequest(destinationBucket,
                destinationObject, sourceBucket, sourceKey, accessControlList);
        return this.syncCopyObject(request);
    }

    @Override
    public CopyResult syncCopyObject(CopyObjectRequest request)
            throws Throwable {

        final CopyResult copyResult = new CopyResult();
        final Throwable error = new Throwable();
        this.copyObject(request, new CopyObjectResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  CopyResult result) {
                copyResult.setETag(result.getETag());
                copyResult.setLastModified(result.getLastModified());

            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return copyResult;
    }

    @Override
    public InitiateMultipartUploadResult syncInitiateMultipartUpload(
            String bucketName, String objectKey) throws Throwable {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
                bucketName, objectKey);
        return this.syncInitiateMultipartUpload(request);
    }

    @Override
    public InitiateMultipartUploadResult syncInitiateMultipartUpload(
            InitiateMultipartUploadRequest request) throws Throwable {

        final InitiateMultipartUploadResult initResult = new InitiateMultipartUploadResult();
        final Throwable error = new Throwable();
        this.initiateMultipartUpload(request,
                new InitiateMultipartUploadResponceHandler() {

                    @Override
                    public void onSuccess(int statesCode,
                                          Header[] responceHeaders,
                                          InitiateMultipartUploadResult result) {
                        initResult.setBucket(result.getBucket());
                        initResult.setKey(result.getKey());
                        initResult.setUploadId(result.getUploadId());

                    }

                    @Override
                    public void onFailure(int statesCode, Ks3Error ks3Error,
                                          Header[] responceHeaders, String response,
                                          Throwable paramThrowable) {
                        error.initCause(paramThrowable);
                    }
                }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return initResult;
    }

    @Override
    public CompleteMultipartUploadResult syncCompleteMultipartUpload(
            String bucketName, String objectKey, String uploadId,
            List<PartETag> partETags) throws Throwable {
        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
                bucketName, objectKey, uploadId, partETags);
        return this.syncCompleteMultipartUpload(request);
    }

    @Override
    public CompleteMultipartUploadResult syncCompleteMultipartUpload(
            ListPartsResult result) throws Throwable {
        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
                result);
        return this.syncCompleteMultipartUpload(request);
    }

    @Override
    public CompleteMultipartUploadResult syncCompleteMultipartUpload(
            CompleteMultipartUploadRequest request) throws Throwable {

        final CompleteMultipartUploadResult completeMultipartUploadResult = new CompleteMultipartUploadResult();
        final Throwable error = new Throwable();
        this.completeMultipartUpload(request,
                new CompleteMultipartUploadResponseHandler() {

                    @Override
                    public void onSuccess(int statesCode,
                                          Header[] responceHeaders,
                                          CompleteMultipartUploadResult result) {
                        completeMultipartUploadResult.setBucket(result
                                .getBucket());
                        completeMultipartUploadResult.setKey(result.getKey());
                        completeMultipartUploadResult.seteTag(result.geteTag());
                        completeMultipartUploadResult.setLocation(result
                                .getLocation());

                    }

                    @Override
                    public void onFailure(int statesCode, Ks3Error ks3Error,
                                          Header[] responceHeaders, String response,
                                          Throwable paramThrowable) {
                        error.initCause(paramThrowable);
                    }
                }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return completeMultipartUploadResult;
    }

    @Override
    public void syncAbortMultipartUpload(AbortMultipartUploadRequest request)
            throws Throwable {

        final Throwable error = new Throwable();
        this.abortMultipartUpload(request,
                new AbortMultipartUploadResponseHandler() {

                    @Override
                    public void onSuccess(int statesCode,
                                          Header[] responceHeaders) {

                    }

                    @Override
                    public void onFailure(int statesCode, Ks3Error ks3Error,
                                          Header[] responceHeaders, String response,
                                          Throwable paramThrowable) {
                        error.initCause(paramThrowable);

                    }
                }, false);
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public void syncAbortMultipartUpload(String bucketname, String objectKey,
                                         String uploadId) throws Throwable {
        AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(
                bucketname, objectKey, uploadId);
        this.syncAbortMultipartUpload(request);
    }

    @Override
    public ListPartsResult syncListParts(String bucketName, String objectKey,
                                         String uploadId) throws Throwable {
        ListPartsRequest request = new ListPartsRequest(bucketName, objectKey,
                uploadId);
        return this.syncListParts(request);
    }

    @Override
    public ListPartsResult syncListParts(String bucketName, String objectKey,
                                         String uploadId, int maxParts) throws Throwable {
        ListPartsRequest request = new ListPartsRequest(bucketName, objectKey,
                uploadId, maxParts);
        return this.syncListParts(request);
    }

    @Override
    public ListPartsResult syncListParts(String bucketName, String objectKey,
                                         String uploadId, int maxParts, int partNumberMarker)
            throws Throwable {
        ListPartsRequest request = new ListPartsRequest(bucketName, objectKey,
                uploadId, maxParts, partNumberMarker);
        return this.syncListParts(request);
    }

    @Override
    public ListPartsResult syncListParts(ListPartsRequest request)
            throws Throwable {
        final ListPartsResult listPartsResult = new ListPartsResult();
        final Throwable error = new Throwable();
        this.listParts(request, new ListPartsResponseHandler() {

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  ListPartsResult result) {
                listPartsResult.setBucketname(result.getBucketname());
                listPartsResult.setEncodingType(result.getEncodingType());
                listPartsResult.setInitiator(result.getInitiator());
                listPartsResult.setKey(result.getKey());
                listPartsResult.setMaxParts(result.getMaxParts());
                listPartsResult.setNextPartNumberMarker(result
                        .getNextPartNumberMarker());
                listPartsResult.setOwner(result.getOwner());
                listPartsResult.setPartNumberMarker(result
                        .getPartNumberMarker());
                listPartsResult.setParts(result.getParts());
                listPartsResult.setUploadId(result.getUploadId());
            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);

            }
        }, false);
        if (error.getCause() != null) {
            throw error;
        }
        return listPartsResult;
    }

    @Override
    public void syncGetObject(GetObjectRequest request, File file,
                              boolean append) throws Throwable {
        final Throwable error = new Throwable();
        this.getObject(request, new GetObjectResponseHandler(file, append) {

            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskProgress(double progress) {

            }

            @Override
            public void onTaskFinish() {

            }

            @Override
            public void onTaskFailure(int paramInt, Ks3Error ks3Error,
                                      Header[] paramArrayOfHeader, Throwable paramThrowable,
                                      File paramFile) {
                error.initCause(paramThrowable);
            }

            @Override
            public void onTaskCancel() {

            }

            @Override
            public void onTaskSuccess(int paramInt,
                                      Header[] paramArrayOfHeader, GetObjectResult getObjectResult) {
                // TODO Auto-generated method stub

            }
        });
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public void syncGetObject(Context context, File file, boolean append,
                              String bucketname, String key,
                              GetObjectResponseHandler getObjectResponceHandler) throws Throwable {
        GetObjectRequest request = new GetObjectRequest(bucketname, key);
        this.syncGetObject(request, file, append);
    }

    @Override
    public void syncPutObject(String bucketname, String objectkey, File file)
            throws Throwable {
        PutObjectRequest request = new PutObjectRequest(bucketname, objectkey,
                file);
        this.syncPutObject(request);
    }

    @Override
    public void syncPutObject(String bucketname, String objectkey, File file,
                              ObjectMetadata objectmeta) throws Throwable {
        PutObjectRequest request = new PutObjectRequest(bucketname, objectkey,
                file, objectmeta);
        this.syncPutObject(request);
    }

    @Override
    public void syncPutObject(PutObjectRequest request) throws Throwable {
        final Throwable error = new Throwable();
        this.putObject(request, new PutObjectResponseHandler() {

            @Override
            public void onTaskProgress(double progress) {

            }

            @Override
            public void onTaskSuccess(int statesCode, Header[] responceHeaders) {

            }

            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskFinish() {

            }

            @Override
            public void onTaskFailure(int statesCode, Ks3Error ks3Error,
                                      Header[] responceHeaders, String response,
                                      Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }

            @Override
            public void onTaskCancel() {

            }
        });
        if (error.getCause() != null) {
            throw error;
        }
    }

    @Override
    public PartETag syncUploadPart(String bucketName, String key,
                                   String uploadId, File file, long offset, int partNumber,
                                   long partSize) throws Throwable {
        UploadPartRequest request = new UploadPartRequest(bucketName, key,
                uploadId, file, offset, partNumber, partSize);
        return this.syncUploadPart(request);
    }

    @Override
    public PartETag syncUploadPart(UploadPartRequest request) throws Throwable {
        final Throwable error = new Throwable();
        final PartETag result = new PartETag();
        this.uploadPart(request, new UploadPartResponceHandler() {

            @Override
            public void onTaskProgress(double progress) {

            }

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders,
                                  PartETag response) {
                result.seteTag(response.geteTag());
                result.setPartNumber(response.getPartNumber());
            }

            @Override
            public void onFailure(int statesCode, Ks3Error ks3Error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                error.initCause(paramThrowable);
            }
        });
        if (error.getCause() != null) {
            throw error;
        }
        return result;
    }

    /**
     * get请求
     */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /**
     * post请求
     */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    /**
     * put请求
     */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /**
     * head请求
     */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /**
     * delete请求
     */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /**
     * options请求
     */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /**
     * patch请求
     */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /**
     * trace请求
     */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }
    /**
     * 获取全局上下文
     */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call OkGo.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call OkGo.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /**
     * 必须设置
     */
    public Ks3Client setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * 获取全局的cookie实例
     */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /**
     * 超时重试次数
     */
    public Ks3Client setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * 超时重试次数
     */
    public int getRetryCount() {
        return mRetryCount;
    }

    /**
     * 全局的缓存模式
     */
    public Ks3Client setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /**
     * 全局的缓存过期时间
     */
    public Ks3Client setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public long getCacheTime() {
        return mCacheTime;
    }

    /**
     * 获取全局公共请求参数
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 添加全局公共请求参数
     */
    public Ks3Client addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public Ks3Client addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    private void initOkGo(Application application) {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(application)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数

    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private static class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            return true;
        }
    }
}
