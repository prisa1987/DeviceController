package com.example.usercontroller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * UserActivity manage about user interface,create data to the groups for expandable listview
 * @author prisa damrongsiri
 *
 */
public class UserActivity extends Activity implements OnRefreshListener{

	private SparseArray<Group> groups = new SparseArray<Group>();
	private static String SERVICE_URL = "http://127.0.0.1:9000/api/v1/";
	
	
	/** Data that convert form XML file, are in form object **/
	private List<Room> roomList;
	private List<Permission> permissionList;
	private List<Request> requestList;
	
	/** User interface **/
	private SwipeRefreshLayout swipeLayout;
	private CustomExpandableListAdapter adapter;
	private ExpandableListView listView;
	
	/** Encrpytion the user id **/
	public static String UUID = "9a884698-e182-487b-bf37-75b6c8b63aab";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		
		/** Separate version for the phone the running on ICE_CREAM_SANDWICH and above has actionbar**/
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			SpannableString s = new SpannableString("Device Controller");
			s.setSpan(new TypefaceSpan("fonts/existencestencillight.otf"), 0,
					s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			getActionBar().setDisplayShowHomeEnabled(false);
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.actionbar);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E5473C")));
			getActionBar().setTitle(s);
		} 
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.LinearLayout1);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		/** Download server public key */
		try {

			URL url = new URL(SERVICE_URL + "serverpublickey");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();

			File file = new File(getFilesDir(), "public.key");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream ops = new FileOutputStream(getFilesDir() + "public.key");
			InputStream ips = new BufferedInputStream(url.openStream());

			byte[] data = new byte[1024];
			int count;

			while ((count = ips.read(data)) != -1) {
				ops.write(data, 0, count);
			}
			
			ops.flush();
			ops.close();
			ips.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Connection con = new Connection(this, this.UUID);
		con.requestAllRoom();
		roomList = con.getModelRoom();

		con.requestPermission();
		permissionList = con.getPermissionUser();
		if(permissionList == null) permissionList = new ArrayList<Permission>();
		
		con.setRequest();
		requestList = con.checkRequest();
		if(requestList == null)  requestList = new ArrayList<Request>();

		loadData();
		listView = (ExpandableListView) findViewById(R.id.listView);
	
		adapter = new CustomExpandableListAdapter(this, groups, UUID, this.UUID);
		listView.setAdapter(adapter);
		
		/** expand listview */
		for (int i = 0; i < groups.size(); i++) {
			listView.expandGroup(i);
		}
	}
	
	/**
	 * Load room,permission,request  into group object which use for display in expandable listview
	 */
	public void loadData() {
		if (roomList == null)
			roomList = new ArrayList<Room>();
		for (int j = 0; j < roomList.size(); j++) {
			Log.d("room", "room : " + roomList.get(j).getDevices().getDevice());
			Group group = new Group(roomList.get(j).getName());
			group.setName(roomList.get(j).getName());

			for (int i = 0; i < roomList.get(j).getDevices().getDevice().size(); i++) {

				group.device.add(roomList.get(j).getDevices().getDevice()
						.get(i));

			}

			for (int i = 0; i < permissionList.size(); i++) {
				if (group.mapPermission.containsKey(permissionList.get(i).getDevice()
						.getId()) == false) {
					group.mapPermission.put(permissionList.get(i).getDevice().getId(),
							permissionList.get(i).getStatus());
					
					Log.d("id", "id : "
							+ permissionList.get(i).getDevice().getName() + " "
							+ permissionList.get(i).getStatus());
				}
			}

			for (int i = 0; i < requestList.size(); i++) {
				group.mapRequest.put(requestList.get(i).getDevice().getId(),
						requestList.get(i).getStatus());
				group.mapRequestID.put(requestList.get(i).getDevice().getId(),
						requestList.get(i));
			}

			groups.append(j, group);
		}

	}
	
	/**
	 * Refresh the new room,device,request,permssion from server then add into group and show it on expanable listview
	 */
	public void refreshData() {
		Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
		Connection con = new Connection(this, this.UUID);
		con.requestAllRoom();
		List<Room> roomList = con.getModelRoom();

		con.requestPermission(); // user ID
		List<Permission> permissionList = con.getPermissionUser();
		if(permissionList == null) permissionList = new ArrayList<Permission>();
		
		con.setRequest();
		List<Request> requestList = con.checkRequest();
		if(requestList == null)  requestList = new ArrayList<Request>();
		
		groups.clear();
		for (int j = 0; j < roomList.size(); j++) {
			Log.d("room", "room : " + roomList.get(j).getDevices().getDevice());
			Group group = new Group(roomList.get(j).getName());
			group.setName(roomList.get(j).getName());

			for (int i = 0; i < roomList.get(j).getDevices().getDevice().size(); i++) {

				group.device.add(roomList.get(j).getDevices()
						.getDevice().get(i));
			}
			if (permissionList != null) {
				for (int i = 0; i < permissionList.size(); i++) {
					if (group.mapPermission.containsKey(permissionList.get(i).getDevice()
							.getId()) == false) {
						group.mapPermission.put(
								permissionList.get(i).getDevice().getId(),
								permissionList.get(i).getStatus());

					}
				}
			}
			if (requestList != null) {
				for (int i = 0; i < requestList.size(); i++) {
					group.mapRequest.put(
							requestList.get(i).getDevice().getId(), requestList
									.get(i).getStatus());
					group.mapRequestID.put(requestList.get(i).getDevice()
							.getId(), requestList.get(i));
				}
			}

			groups.append(j, group);
		}
		adapter = new CustomExpandableListAdapter(this, groups, UUID, this.UUID);
		listView.setAdapter(adapter);

		for (int i = 0; i < groups.size(); i++) {
			listView.expandGroup(i);
		}
	}
	
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshData();
				swipeLayout.setRefreshing(false);

			}
		}, 5000);

	}
	
}
