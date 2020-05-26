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
