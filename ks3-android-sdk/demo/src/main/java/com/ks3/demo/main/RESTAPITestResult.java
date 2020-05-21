package com.ks3.demo.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class RESTAPITestResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rest_api_test_result);
		String result = getIntent().getExtras().getString("result");
		String api   = getIntent().getExtras().getString("api");
		TextView resulTextView = (TextView) findViewById(R.id.result);
		resulTextView.setText(result);
		TextView apiTextView = (TextView) findViewById(R.id.api);
		apiTextView.setText(api);
	}
}
