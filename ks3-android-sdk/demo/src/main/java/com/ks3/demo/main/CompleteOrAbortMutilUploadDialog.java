package com.ks3.demo.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 自定义Dialog，Bucket输入对话框
 * 
 */
public class CompleteOrAbortMutilUploadDialog extends Dialog implements
		View.OnClickListener {
	public interface OnCompelteOrAbort {
		public void complte();
		public void abort();
	}

	public OnCompelteOrAbort listener;
	private Button okBtn;
	private Button cancelBtn;
	private Context context;

	public CompleteOrAbortMutilUploadDialog(Context context) {
		super(context);
		this.context = context;
	}

	public void setOnBucketInputListener(OnCompelteOrAbort listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_multi_completeorabort);
		setUpViews();
	}

	private void setUpViews() {
		okBtn = (Button) findViewById(R.id.ok);
		cancelBtn = (Button) findViewById(R.id.cancel);
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			switch (v.getId()) {
			case R.id.ok:
//				if (TextUtils.isEmpty(bucketField.getText().toString())) {
//					Toast.makeText(context, "Bucket name should not be null",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					String result = bucketField.getText().toString();
//					listener.confirmBucket(result);
//
//				}
				listener.complte();
				CompleteOrAbortMutilUploadDialog.this.dismiss();
				break;
			case R.id.cancel:
				listener.abort();
				CompleteOrAbortMutilUploadDialog.this.dismiss();
				break;
			default:
				break;
			}
		} else {
			Toast.makeText(context, "Please set OnBucketInputListener first",
					Toast.LENGTH_SHORT).show();
		}

	}

}
