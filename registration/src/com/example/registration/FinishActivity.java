package com.example.registration;



import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The last step in the registration display register success and show phonenumbe that the applcation
 * will send sms to it.
 * @author prisa damrongsiri
 *
 */
public class FinishActivity extends ActionBarActivity {

	private TextView phoneNumber;
	private Button b_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		getSupportActionBar().hide();

		phoneNumber = (TextView) findViewById(R.id.r_phonenumber);
		phoneNumber.setText(getIntent().getStringExtra("phoneNumber"));

		b_home = (Button) findViewById(R.id.home_button);
		b_home.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(),
						InformationPhoneActivity.class);
				startActivity(in);

			}
		});

	}

}
