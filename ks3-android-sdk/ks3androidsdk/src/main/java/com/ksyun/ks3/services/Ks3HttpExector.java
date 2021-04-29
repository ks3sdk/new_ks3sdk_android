package com.ksyun.ks3.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ksyun.ks3.auth.AuthEvent;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.acl.Authorization;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.util.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.lzy.okgo.OkGo;


public class Ks3HttpExector {


    public void invoke(Authorization auth, final Ks3HttpRequest request,
                       final AsyncHttpResponseHandler resultHandler,
                       Ks3ClientConfiguration clientConfiguration, final Context context,
                       String endpoint, AuthListener authListener, Boolean isUseAsyncMode) {
        if (TextUtils.isEmpty(endpoint)) {
            Log.e(Constants.LOG_TAG, "The endpoint is empty,do you call setEndpoint() after you create Ks3Client?");
            return;
        }
        request.setAuthorization(auth);
        //设置host
        if (request.getBucketname() == null || clientConfiguration.isPathStyleAccess()) {
            request.setEndpoint(endpoint);
        } else {
            request.setEndpoint(request.getBucketname() + "." + endpoint);
        }

        // 异步
        if (isUseAsyncMode) {
            // Token形式
            if (authListener != null) {
                request.setAuthListener(authListener);
                setUpRequsetInBackground(request, new Ks3AuthHandler() {

                    @Override
                    public void onSuccess(AuthEvent event) {
                        doRequset(request, context, resultHandler);
                    }

                    @Override
                    public void onFailure(AuthEvent event) {
                        resultHandler.onFailure(0, null, null,
                                new Ks3ClientException(event.getContent()));
                    }
                }, resultHandler);
            }
            // AK&SK形式
            else {
                try {
                    request.completeRequset(null, resultHandler);
                } catch (Ks3ClientException e) {
                    resultHandler.onFailure(0, null, null, e);
                    return;
                }
                doRequset(request, context, resultHandler);
            }
        }
        // 同步
        else {
            // Token形式
            if (authListener != null) {
                request.setAuthListener(authListener);
                Ks3AuthHandler ks3AuthHandler = new Ks3AuthHandler() {

                    @Override
                    public void onSuccess(AuthEvent event) {
                        doRequset(request, context, resultHandler);
                    }

                    @Override
                    public void onFailure(AuthEvent event) {
                        resultHandler.onFailure(0, null, null,
                                new Ks3ClientException(event.getContent()));
                    }
                };
                try {
                    request.completeRequset(ks3AuthHandler, resultHandler);
                } catch (Ks3ClientException e) {
                    ks3AuthHandler.isNeedCalculateAuth = false;
                    resultHandler.onFailure(0, null, null, e);
                    return;
                }
                // AK&SK形式
            } else {
                try {
                    request.completeRequset(null, resultHandler);
                } catch (Ks3ClientException e) {
                    resultHandler.onFailure(0, null, null, e);
                    return;
                }
                doRequset(request, context, resultHandler);
            }
        }

    }

    protected void doRequset(Ks3HttpRequest request, Context context,
                             AsyncHttpResponseHandler resultHandler) {
        // For test
        LogShow(request);
        RequestHandle handler = null;
        Log.d(Constants.LOG_TAG, "requset url = " + request.getUrl());
        switch (request.getHttpMethod()) {
            case GET:
                handler = client.get(context, request.getAsyncHttpRequestParam()
                                .getUrl(), request.getAsyncHttpRequestParam().getHeader(),
                        null, resultHandler);
                break;
            case POST:
                handler = client.post(context, request.getAsyncHttpRequestParam()
                                .getUrl(), request.getAsyncHttpRequestParam().getHeader(),
                        request.getEntity(), request.getContentType(),
                        resultHandler);
                break;
            case PUT:
                handler = client.put(context, request.getAsyncHttpRequestParam()
                                .getUrl(), request.getAsyncHttpRequestParam().getHeader(),
                        request.getEntity(), request.getContentType(),
                        resultHandler);
                break;
            case DELETE:
                handler = client.delete(context, request.getAsyncHttpRequestParam()
                                .getUrl(), request.getAsyncHttpRequestParam().getHeader(),
                        resultHandler);
                break;
            case HEAD:
                handler = client.head(context, request.getAsyncHttpRequestParam()
                                .getUrl(), request.getAsyncHttpRequestParam().getHeader(),
                        null, resultHandler);
                break;
            default:
                Log.e(Constants.LOG_TAG, "unsupport http method ! ");
                break;
        }
        request.setRequestHandler(handler);
    }

    public void get(Ks3HttpRequest request, Context context,
                    AsyncHttpResponseHandler resultHandler) {

        OkGo.<LzyResponse<ServerModel>>get(Urls.URL_JSONOBJECT)//
                .headers("aaa", "111")//
                .params("bbb", "222")//
                .converter(new JsonConvert<LzyResponse<ServerModel>>() {})//
                .adapt(new ObservableBody<LzyResponse<ServerModel>>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoading();
                    }
                })//
                .map(new Function<LzyResponse<ServerModel>, ServerModel>() {
                    @Override
                    public ServerModel apply(@NonNull LzyResponse<ServerModel> response) throws Exception {
                        return response.data;
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<ServerModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull ServerModel serverModel) {
                        handleResponse(serverModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();            //请求失败
                        showToast("请求失败");
                        handleError(null);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private void setUpRequsetInBackground(final Ks3HttpRequest request,
                                          final Ks3AuthHandler ks3AuthHandler,
                                          final AsyncHttpResponseHandler resultHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request.completeRequset(ks3AuthHandler, resultHandler);
                } catch (Ks3ClientException e) {
                    ks3AuthHandler.isNeedCalculateAuth = false;
                    resultHandler.onFailure(0, null, null, e);
                }
            }
        }).start();
    }

    private void LogShow(Ks3HttpRequest request) {
        request.getAsyncHttpRequestParam().getUrl();
        request.getAsyncHttpRequestParam().getHeader();
        request.getAsyncHttpRequestParam().getParams();
        StringBuffer sb = new StringBuffer();
        sb.append("**url** " + request.getAsyncHttpRequestParam().getUrl())
                .append("\n");
        sb.append("**heads**").append("\n");
        for (int i = 0; i < request.getAsyncHttpRequestParam().getHeader().length; i++) {
            sb.append(
                    request.getAsyncHttpRequestParam().getHeader()[i].getName())
                    .append("=>")
                    .append(request.getAsyncHttpRequestParam().getHeader()[i]
                            .getValue()).append("\n");
        }
        Log.e(Constants.LOG_TAG, sb.toString());
    }

    public void cancel(Context context) {
        client.cancelRequests(context, true);
    }

    public void pause(Context context) {
        client.cancelRequests(context, true);
    }

}
