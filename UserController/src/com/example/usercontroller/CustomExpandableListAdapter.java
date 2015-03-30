package com.example.usercontroller;

import java.util.List;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Manage about data in expanable listview
 * @author prisa damrongsiri
 *
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
	
	/** User interface */
	public LayoutInflater inflater;
	public Activity activity;
	private Switch mySwitch;
	private ToggleButton swToggle;
	private Button action_btn;
	private ImageView notification;
	private Typeface face;

	/** Position of object in expandable listview */
	private int groupPosition;
	private int childPosition;
	
	private Connection con;
	private Device device;
	private String userId;
	private boolean isFirst;
	private SparseArray<Group> groups;
	
	private Object[][] layout = new Object[50][50];
	private static String UUID;
	
	public CustomExpandableListAdapter(Activity act, SparseArray<Group> groups,String userId, String UUID) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
		face = Typeface.createFromAsset(activity.getAssets(),"fonts/ralewayregular.otf");
		this.userId = userId;
		this.UUID = UUID;
	}
	
	/**
	 * Get parent or group position
	 * @return groupPosition
	 */
	public int getGroupPosition() {
		return groupPosition;
	}

	/**
	 * Set parent or group position
	 * @param groupPosition
	 */
	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}
	
	/**
	 * Get the child or member position
	 * @return childPosition
	 */
	public int getChildPosition() {
		return childPosition;
	}

	/**
	 * Set the child or member position
	 * @param childPosition
	 */
	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}
	
	/**
	 * Get child name
	 * @return name of that member such as Light A,Light B
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).device.get(childPosition).getName();
	}

	/**
	 * Get child position
	 * @return childPosition
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	
	/**
	 * Get a child view by the group position , child postion that you want to select
	 * @return convertView
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild,
			View convertView, ViewGroup parent) {
		
	
		final String children = (String) getChild(groupPosition, childPosition);
		final String device_id = groups.get(groupPosition).device.get(childPosition).getId();

		final TextView text;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
			layout[groupPosition][childPosition] = convertView;
		}
		isFirst = true;

		text = (TextView) convertView.findViewById(R.id.textView1);
		text.setText(children);
		text.setTypeface(face);

		/* Set current status */
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// for ICE_CREAM_SANDWICH and above versions
			mySwitch = (Switch) convertView.findViewById(R.id.mySwitch);
		} else {
			swToggle = (ToggleButton) convertView.findViewById(R.id.tgSwitch);
		}

		action_btn = (Button) convertView.findViewById(R.id.action_btn);
		notification = (ImageView) convertView.findViewById(R.id.noticImg);

		/** For firsttime, load the data into view */
		if (isFirst) {
			con = new Connection(activity, this.UUID);

			/* Set Permission */
			if (groups.get(groupPosition).mapPermission.get(device_id) != null) {

				if ((Boolean) groups.get(groupPosition).mapPermission.get(device_id)) {
					action_btn.setVisibility(View.INVISIBLE);
					notification.setVisibility(View.VISIBLE);
					if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						// for ICE_CREAM_SANDWICH and above versions
						mySwitch.setVisibility(View.VISIBLE);
					} else {
						swToggle.setVisibility(View.VISIBLE);

					}
		

					Log.d("Permission", "Permission : " + device_id + " allowed");
				} else {
					action_btn.setVisibility(View.VISIBLE);
					if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						// for ICE_CREAM_SANDWICH and above versions
						mySwitch.setVisibility(View.INVISIBLE);
					} else {
						swToggle.setVisibility(View.INVISIBLE);
					}
					
					Log.d("Permission", "Permission : " + device_id + " request");
				}
			} else {
				action_btn.setVisibility(View.VISIBLE);
				if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					// for ICE_CREAM_SANDWICH and above versions
					mySwitch.setVisibility(View.INVISIBLE);
				} else {
					swToggle.setVisibility(View.INVISIBLE);
				}
				// mySwitch.setVisibility(View.INVISIBLE);
				action_btn.setBackgroundResource(R.drawable.shaperequest);
			}

			/* Set Request */
			if (groups.get(groupPosition).mapRequest.get(device_id) == null) {
				groups.get(groupPosition).mapRequest.put(device_id, "NONE");
			}

			if (groups.get(groupPosition).mapRequest.get(device_id).equals("PENDING")) {
				action_btn.setText("Cancel");
				notification.setVisibility(View.INVISIBLE);
				action_btn.setBackgroundResource(R.drawable.shapecancel);
			} else if (groups.get(groupPosition).mapRequest.get(device_id).equals(
					"REJECTED")) {
				action_btn.setText("Reject");
				notification.setVisibility(View.VISIBLE);
				action_btn.setBackgroundResource(R.drawable.shapereject);
			} else if (groups.get(groupPosition).mapRequest.get(device_id).equals(
					"APPROVED")) {
				action_btn.setText("Approved");
				notification.setVisibility(View.VISIBLE);
				action_btn.setBackgroundResource(R.drawable.shapeapprove);
			} else {
				action_btn.setText("Request");
				notification.setVisibility(View.INVISIBLE);
				action_btn.setBackgroundResource(R.drawable.shaperequest);
			}
			action_btn.setTypeface(face);
			con.requestDevices(device_id);
			Device d = con.getDevice();
			if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				//for ICE_CREAM_SANDWICH and above versions
				mySwitch.setChecked(d.getStatus());
			} else {
				swToggle.setChecked(d.getStatus());
			}
		
		}

		action_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d("Posistion: ", "group : " + groupPosition + " child: "+ childPosition);
				View vi = (View) layout[groupPosition][childPosition];
				Button b = (Button) v.findViewById(R.id.action_btn);
				ImageView notificationImg = (ImageView) vi.findViewById(R.id.noticImg);
				if (groups.get(groupPosition).mapRequest.get(device_id) != null) {
					if (groups.get(groupPosition).mapRequest.get(device_id).equals("NONE")) {
						b.setText("Cancel");
						con.setAddNewRequest(device_id); // id device
						b.setBackgroundResource(R.drawable.shapecancel);
						groups.get(groupPosition).mapRequest.put(device_id, "PENDING");
					} else if (groups.get(groupPosition).mapRequest.get(device_id)
							.equals("APPROVED")) {
						b.setVisibility(v.INVISIBLE);
						
						int currentapiVersion = android.os.Build.VERSION.SDK_INT;
						if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
							// for ICE_CREAM_SANDWICH and above
							Switch sw = (Switch) vi.findViewById(R.id.mySwitch);
							sw.setVisibility(View.VISIBLE);
							
							Log.d("sw", (ImageView) vi.findViewById(R.id.noticImg) + "");
						} else {
							ToggleButton tg = (ToggleButton) vi.findViewById(R.id.tgSwitch);
							tg.setVisibility(View.VISIBLE);
							Log.d("sw", (ImageView) vi.findViewById(R.id.noticImg) + "");
						}
						notificationImg.setVisibility(View.INVISIBLE);
						Request re = (Request) groups.get(groupPosition).mapRequestID.get(device_id);
						String request_id = re.getId();
						b.setBackgroundResource(R.drawable.shaperequest);
						con.setConfirmApproved(request_id);
				
					} else {
						b.setText("Request");
						Request re = (Request) groups.get(groupPosition).mapRequestID.get(device_id);
						String request_id = re.getId();
						b.setBackgroundResource(R.drawable.shaperequest);
						con.deleteRequest(request_id);
						notificationImg.setVisibility(View.INVISIBLE);
					}
				} else {
					b.setText("Cancel");
					con.setAddNewRequest(device_id); //  id device
					b.setBackgroundResource(R.drawable.shapecancel);
					groups.get(groupPosition).mapRequest.put(device_id, "PENDING");
				}
				b.setTypeface(face);
				refresh();
				Log.d("Request List",
						groups.get(groupPosition).mapRequest.toString());

			}

		});

		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// for ICE_CREAM_SANDWICH and above versions
			mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (isFirst == false) {

						con.requestDevices(device_id);
						device = con.getDevice();
						
						Log.d("device","before ==>" + "id: " + device.getId() + " "
						+ device.getName() + " "+ device.getStatus());

						if (device.getStatus()) {
							device.setStatus(false);
						} else {
							device.setStatus(true);
						}
						con.setDeviceToggle(device_id+"",device.getStatus());
						
						Log.d("device", "after ==>" + device.getName() + " "+ device.getStatus());
						isFirst = false;

					} else {
						isFirst = false;
					}

				}
			});
		} else {
			swToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (isFirst == false) {

						con.requestDevices(device_id);
						device = con.getDevice();
						Log.d("device",
								"before ==>" + "id: " + device.getId() + " "
										+ device.getName() + " "
										+ device.getStatus());

						if (device.getStatus()) {
							device.setStatus(false);
						} else {
							device.setStatus(true);
						}
						con.setDeviceToggle(device_id+"",device.getStatus());
						Log.d("device", "after ==>" + device.getName() + " "
								+ device.getStatus());
						isFirst = false;

					} else {
						isFirst = false;
					}

				}
			});

		}

		isFirst = false;
		return convertView;
	}
	
	/**
	 * For update the room.permission,request when the user do something such as request,control device
	 */
	public void refresh() {
		Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show();
		con = new Connection(activity, this.UUID);
		con.requestAllRoom();
		List<Room> roomList = con.getModelRoom();

		con.requestPermission(); 
		List<Permission> permissionList = con.getPermissionUser();
		con.setRequest();
		List<Request> requestList = con.checkRequest();
		groups.clear();
		for (int j = 0; j < roomList.size(); j++) {
			Log.d("room", "room : " + roomList.get(j).getDevices().getDevice());
			Group group = new Group(roomList.get(j).getName());
			group.setName(roomList.get(j).getName());

			for (int i = 0; i < roomList.get(j).getDevices().getDevice().size(); i++) {
				group.device.add(roomList.get(j).getDevices().getDevice().get(i));
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

	}
	
	/**
	 * Display the number of children in that group
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).device.size();
	}

	/**
	 * Get parent or group object by groupPosition that you want
	 */
	@Override
	public Group getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/**
	 * Display the numbe parent or group of Expandablelistview
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/**
	 * Get parent or group position
	 * @return groupPosition
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Get group view  by groupPosition that you want
	 * @return convertView
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		Group group = (Group) getGroup(groupPosition);
		TextView title = (TextView) convertView
				.findViewById(R.id.accordionTitle);
		title.setText(group.string);
		title.setTypeface(face);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}
}
