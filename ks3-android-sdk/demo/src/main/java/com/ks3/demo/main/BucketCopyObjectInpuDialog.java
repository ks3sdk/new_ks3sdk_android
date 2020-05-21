package com.ks3.demo.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * 自定义Dialog
 * 
 */
public class BucketCopyObjectInpuDialog extends Dialog implements
		View.OnClickListener {

	private EditText sourceBucket;
	private EditText sourceKey;

	public interface OnBucketCopyObjectDialogListener {
		public void confirmBucketAndObject(String destinationBucket, String destinationObjectKey,String sourceBucketName,String sourceKey);
	}

	public OnBucketCopyObjectDialogListener listener;
	private EditText bucketField;
	private Button okBtn;
	private Button cancelBtn;
	private Context context;
	private EditText objectField;

	public BucketCopyObjectInpuDialog(Context context) {
		super(context);
		this.context = context;
	}

	public void setOnBucketCopyObjectDialogListener(
			OnBucketCopyObjectDialogListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_copy_object_input);
		setTitle("Input Bucket Name");
		setUpViews();
	}

	private void setUpViews() {
		bucketField = (EditText) findViewById(R.id.input);
		objectField = (EditText) findViewById(R.id.input_second);
		sourceBucket = ((EditText) findViewById(R.id.sourcebucket));
		sourceKey = ((EditText) findViewById(R.id.sourcekey));
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
				if (TextUtils.isEmpty(bucketField.getText().toString())
						|| TextUtils.isEmpty(objectField.getText().toString())
						||TextUtils.isEmpty(sourceBucket.getText().toString())
						||TextUtils.isEmpty(sourceKey.getText().toString())) {
					Toast.makeText(context,
							"Bucket name or Object key should not be null",
							Toast.LENGTH_SHORT).show();
				} else {
					String name = bucketField.getText().toString();
					String key = objectField.getText().toString();
					String sourceBucket = this.sourceBucket.getText().toString();
					String sourceKey = this.sourceKey.getText().toString();
					listener.confirmBucketAndObject(name, key,sourceBucket,sourceKey);
					BucketCopyObjectInpuDialog.this.dismiss();
				}
				break;
			case R.id.cancel:
				BucketCopyObjectInpuDialog.this.dismiss();
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
