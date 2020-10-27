



# 常见问题汇总


**1.谨慎设置  configuration.setDomainMode(true);** 


&ensp;&ensp;&ensp;&ensp;该方法只在 “**用户需要通过自己的域名上传，可以将Endpoint设置成自己域名**” 时使用，如果用户不是通过自己的域名上传，请勿调用次方法，会造成签名不正确。

2.**Token初始化Ks3Client的方式，如何在一个Activity里多次上传文件?**

&ensp;&ensp;&ensp;&ensp;Token方式初始化出一个在Activity里全局的client，并在AuthListener---onCalculateAuth()回调里向APP服务器获取签名。
      
      
&ensp;&ensp;&ensp;&ensp;每次上传文件都会回调到AuthListener---onCalculateAuth(),从而获取本次上传文件操作的签名。
      

```
      //Activity里全局的client
        client = new Ks3Client(new AuthListener() {
            @Override
            public String onCalculateAuth(String httpMethod,

                    String ContentType, String Date, String ContentMD5,

                    String Resource, String Headers) {
                // 此处应由APP端向业务服务器发送post请求返回Token(同步请求)。
                // 需要注意该回调方法运行在非主线程
	        	//Looper.prepare();
                String token = requsetToAppServer(httpMethod, ContentType,

                        Date, ContentMD5, Resource, Headers);

                return token;
            }

        }, DummyActivity.this);
	
        configuration = Ks3ClientConfiguration.getDefaultConfiguration();
        client.setConfiguration(configuration);
        client.setEndpoint(YOUR_END_POINT);
    
    //上传文件的方法(当文件过大时，需要使用分块上传的方式)
	private void doSingleUpload(final String bucketName, final UploadFile item) {
		final PutObjectRequest request = new PutObjectRequest(bucketName,
				item.file.getName(), item.file);

		client.putObject(request, new PutObjectResponseHandler() {
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
			public void onTaskCancel() {
			}

			@Override
			public void onTaskFailure(int statesCode, Ks3Error error,
					Header[] responceHeaders, String response,
					Throwable paramThrowable) {
			}
		});
	}
}
```
&ensp;&ensp;&ensp;&ensp;doSingleUpload方法详情请前往demo--UploadActivity查看

**3.问题2中这种方式，在一个Activity中多次上传文件时，会不会使得参数固定，导致签名不对？**


答：不会。

&ensp;&ensp;&ensp;&ensp;我们每次上传文件时都会调用方法：

```
	private void doSingleUpload(final String bucketName, final UploadFile item) {
		final PutObjectRequest request = new PutObjectRequest(bucketName,
				item.file.getName(), item.file);

		client.putObject(request, new PutObjectResponseHandler() {
			....
			....
		});
	}
}
```

&ensp;&ensp;&ensp;&ensp;每次该方法会为我们生成一个新的PutObjectRequest对象，签名需要的httpMethod,ContentType,Date,ContentMD5等参数，会存储在这个request对象里面，最终回调到AuthListener---onCalculateAuth(String httpMethod, String ContentType, String Date, String ContentMD5,String Resource, String Headers)  方法里。我们通过这些参数去请求APP服务器，获取正确签名。

&ensp;&ensp;&ensp;&ensp;**因此，我们每一次上传文件都需要一个新的PutObjectRequest对象，而client对象只需要一个全局的即可。**



**4.错误: 程序包cz.msebera.android.httpclient.XXX不存在**


&ensp;&ensp;&ensp;&ensp;API 23 以后 Apache 官方放弃了 HttpClient 对 Android 的支持;所以使用第三方包
```
dependencies {
    api 'cz.msebera.android:httpclient:4.5.8'
}
```



##### 5.Android Studio导入已存在的项目及存在的问题

在下载的是一个ZIP文件的情况下，如何将项目导入到Android Studio中，下面是介绍：
 **第一步** 将ZIP文件解压成文件夹，否则AS无法识别到安卓项目；
 **第二步** 打开AS，点击open an exsiting project，选择解压的项目，基本上如果项目前面是AS的图标，那么这就是那个安卓项目了。
 **第三步** 等待AS build项目，如果直接build成功，那么这个项目就已经导入完成，可以运行了。但是一般情况下，项目都会出现各种问题。对于第一次导入项目来说，AS爆出的问题更多，下面是汇总及解决办法。

**问题1：Cannot run git**

![image-20201014143746601](/Users/cqc/Library/Application Support/typora-user-images/image-20201014143746601.png)

参考文档：https://blog.csdn.net/weixin_44950987/article/details/102619708



**问题2：Error: no value has been specified for this provider**



先打开**Gradle Scripts > settings.gradle**，删除掉**include "xxxapp"**，然后再点击**Files>Sync project with gradle files**；

再次打开**Gradle Scripts > settings.gradle**，添加上**include "xxxapp"**，然后再点击**Files>Sync project with gradle files**。



**问题3：ERROR: Module 'demo': platform 'android-28' not found.**

Ctrl+shift+alt+s 或者File > Project Structure
手动选择JDK的位置。



**问题4：安装Android SDK失败**

**解决办法**
点击下方蓝色的Install missing SDK packages,将会进入下载界面，点击接受条约，即可下载。左侧会列出确实的SDK版本是28的。

![image-20201014144129266](/Users/cqc/Library/Application Support/typora-user-images/image-20201014144129266.png)