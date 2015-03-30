package com.example.registration;



import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * First Activity of registration application to fill user phone number
 * @author prisa damrongsiri
 *
 */
public class InformationPhoneActivity extends ActionBarActivity {

	/* user interface */
	private Button b_0;
	private Button b_1;
	private Button b_2;
	private Button b_3;
	private Button b_4;
	private Button b_5;
	private Button b_6;
	private Button b_7;
	private Button b_8;
	private Button b_9;
	private Button b_plus;
	private Button b_delete;
	private Button b_next;
	private TextView display;
    
	private String concat = "";
	private String phoneNumber = "";
	
	/**
	 * Set layout and any button for programming
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informationphonenumber);
		getSupportActionBar().hide();
		b_0 = (Button) findViewById(R.id.b_0);
		b_1 = (Button) findViewById(R.id.b_1);
		b_2 = (Button) findViewById(R.id.b_2);
		b_3 = (Button) findViewById(R.id.b_3);
		b_4 = (Button) findViewById(R.id.b_4);
		b_5 = (Button) findViewById(R.id.b_5);
		b_6 = (Button) findViewById(R.id.b_6);
		b_7 = (Button) findViewById(R.id.b_7);
		b_8 = (Button) findViewById(R.id.b_8);
		b_9 = (Button) findViewById(R.id.b_9);
		b_plus = (Button) findViewById(R.id.b_plus);
		b_delete = (Button) findViewById(R.id.b_delete);
		b_next = (Button)findViewById(R.id.b_next);
		display = (TextView)findViewById(R.id.display);
		
		
		final AlertDialog.Builder dDialog = new AlertDialog.Builder(InformationPhoneActivity.this);
		
		b_0.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				pressNumber("0");
			}
		});
		
		b_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("1");
			}
		});
		
		b_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("2");
			}
		});
		
		b_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("3");
			}
		});
		
		b_4.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				pressNumber("4");	
			}
		});
		
		b_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("5");
				
			}
		});
		
		b_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("6");
				
			}
		});
		
		b_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("7");
				
			}
		});
		
		b_8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressNumber("8");
				
			}
		});
		
		b_9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pressNumber("9");
				
			}
		});
		
		b_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(concat.length() == 0){
					pressNumber("+");
				}
				
			}
		});
		
		b_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteNumber();			
			}
		});
		
		final Intent intent = new Intent(this,CameraActivity.class);
	
		
		b_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = getPhoneNumber();
				if(phoneNumber.equals("")){
					dDialog.setTitle("Error");
					dDialog.setMessage("Please enter Phone Number");
					dDialog.setPositiveButton("Close", null);
					dDialog.show();
				}else if(phoneNumber.length() < 10){
					dDialog.setTitle("Error");
					dDialog.setMessage("Please enter New Phone Number");
					dDialog.setPositiveButton("Close", null);
					dDialog.show();
				}
				else{
					intent.putExtra("phoneNumber", phoneNumber);
					startActivityForResult(intent, 1);
				
				}
			}
		});
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	/* Get phone number */
	public String getPhoneNumber(){
		phoneNumber = concat.trim();
		return phoneNumber;
	}
	
	/* Delete the digit */
	public void deleteNumber(){
		if(concat.length() > 0 ){
			concat  = concat.substring( 0, concat.length() - 1 ); 
			display.setText(concat);
		}
	}
	
	/* Add the digit */
	public void pressNumber(String num){
		if(concat.length() <= 13){
			display.setText(concat+num);
			concat += num;	
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
