package com.android.slidingmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.appdupe.pair.model.KeyValuePair;
import com.appdupe.pair.model.Latlong_Data;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.LocationFinder;
import com.appdupe.pair.utility.LocationFinder.LocationResult;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairnofb.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paypal.android.sdk.payments.PayPalService;

public class PickLocation extends SherlockFragmentActivity implements
		OnMarkerClickListener {

	// Google Map
	private GoogleMap googleMap;
	ArrayList<LatLng> latlong = new ArrayList<LatLng>();
	int color;
	double latitude_, longitude_;
	boolean firstOpen = false;
	Context ctx;
	// bus stops info variables
	String errors;
	String new_loc = "";
	Dialog dialog_location;
	Latlong_Data data_latlong;
	private SharedPreferences preferences;
	private static final String TAG = "PickLocation";
	private ArrayList<Latlong_Data> latlong_data = new ArrayList<Latlong_Data>();
	private String fb_id;
	private LocationFinder newLocationFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ctx = this;

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		latitude_ = Double.parseDouble(preferences.getString(
				Constant.PREF_USER_LAT, ""));
		longitude_ = Double.parseDouble(preferences.getString(
				Constant.PREF_USER_LONG, ""));
		fb_id = preferences.getString(Constant.FACEBOOK_ID, "");

		dialog_location = new Dialog(ctx, android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog_location.setContentView(R.layout.confirm_location_popup);
		dialog_location.setCancelable(false);
		dialog_location.setCanceledOnTouchOutside(false);

		latlong_data = (ArrayList<Latlong_Data>) getIntent()
				.getSerializableExtra("latlong_data");

		((ImageView) findViewById(R.id.back_image))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);

			// final MarkerOptions marker_current = new
			// MarkerOptions().position(
			// new LatLng(latitude_, longitude_))
			// .title("Current Location");
			//
			// marker_current.icon(BitmapDescriptorFactory
			// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
			// googleMap.addMarker(marker_current);

			googleMap.setOnMarkerClickListener(this);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude_, longitude_)).zoom(10).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			googleMap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng latlong) {
					try {
						setMarkers();
						Log.e("onMapClick", "onMapClick");
						new_loc="";
						Boolean show_popup = true;
						for (int i = 0; i < latlong_data.size(); i++) {
							Location loc1 = new Location("");
							loc1.setLatitude(Double.parseDouble(latlong_data
									.get(i).getLat()));
							loc1.setLongitude(Double.parseDouble(latlong_data
									.get(i).getLong()));

							Location loc2 = new Location("");
							loc2.setLatitude(latlong.latitude);
							loc2.setLongitude(latlong.longitude);

							double distanceInMiles = loc1.distanceTo(loc2) * 0.00062137;
							if (distanceInMiles < 100) {
								show_popup = false;
							}
						}
						if (!show_popup) {
							Toast.makeText(PickLocation.this,
									"Already in Range", Toast.LENGTH_SHORT)
									.show();
							googleMap.clear();
						} else {
							googleMap.clear();
							Geocoder geoCoder = new Geocoder(PickLocation.this,
									Locale.getDefault());

							List<Address> address = geoCoder.getFromLocation(
									latlong.latitude, latlong.longitude, 1);
							if(address.size()>0)
							{
								for (int i = 0; i < address.get(0)
										.getMaxAddressLineIndex(); i++) {
									new_loc = new_loc
											+ address.get(0).getAddressLine(i);
									if (new_loc.length() > 0)
										new_loc = new_loc + " ";
									Log.e("here", address.get(0).getAddressLine(i));
								}
							}
							
							if (new_loc.equals(null))
								new_loc = "";

							MarkerOptions marker = new MarkerOptions()
									.position(new LatLng(latlong.latitude,
													latlong.longitude)).title(
											new_loc);
							marker.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
							googleMap.addMarker(marker);

							if (dialog_location != null && !dialog_location.isShowing()) {
								Location_popup(latlong);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	LocationResult mLocationResult = new LocationResult() {
		public void gotLocation(final double latitude, final double longitude) {
			System.out.println("Got Location...Lat:" + String.valueOf(latitude)
					+ "Long:" + String.valueOf(longitude));
			if (latitude == 0.0 || longitude == 0.0) {
				return;
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						latitude_ = latitude;
						longitude_ = longitude;
					}
				});
			}
		}
	};

	private void Location_popup(final LatLng latlong) {
		final EditText location_name = (EditText) dialog_location
				.findViewById(R.id.location_name);
		location_name.setText(new_loc);
		Button ok = (Button) dialog_location.findViewById(R.id.ok);
		Button Cancel = (Button) dialog_location.findViewById(R.id.cancel);

		location_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					location_name.setError(null);
				}
			}
		});

		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String city_name = location_name.getText().toString().trim();
				Log.e("city_name", city_name);
				if (city_name.length() <= 0) {
					location_name.setError("Please enter city name first");
				} else {
					Boolean save = true;
					for (int i = 0; i < latlong_data.size(); i++) {
						if (city_name.equalsIgnoreCase(latlong_data.get(i)
								.getCity_name()))
							save = false;
					}
					if (save) {
						location_name.setError(null);
						data_latlong = new Latlong_Data();
						data_latlong.setLat(latlong.latitude + "");
						data_latlong.setLong(latlong.longitude + "");
						data_latlong.setCity_name(city_name);
						new Thread(new Runnable() {
							@Override
							public void run() {
								sendDataToServer();
							}
						}).start();
					} else {
						location_name.setError("Location already added");
					}
				}
			}
		});
		Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_location.dismiss();
			}
		});
		dialog_location.show();
	}

	@Override
	protected void onResume() {
		super.onResume();

		LocationManager locationManagerresume = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locationManagerresume
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			newLocationFinder = new LocationFinder();
			newLocationFinder.getLocation(PickLocation.this, mLocationResult);

			// MarkerOptions marker_current = new MarkerOptions().position(
			// new LatLng(latitude_, longitude_))
			// .title("Current Location");
			//
			// marker_current.icon(BitmapDescriptorFactory
			// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
			// googleMap.addMarker(marker_current);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude_, longitude_)).zoom(10).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			setMarkers();
		} else {
			BasicFunctions.showGPSDisabledAlertToUser(this);
		}
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		if (dialog_location != null && !dialog_location.isShowing()) {
			Boolean show_popup = true;
			for (int i = 0; i < latlong_data.size(); i++) {
				LatLng lat_long = arg0.getPosition();
				Double lat = Double.parseDouble(latlong_data.get(i).getLat());
				Double long_ = Double
						.parseDouble(latlong_data.get(i).getLong());
				if (lat_long.latitude == lat && lat_long.longitude == long_) {
					show_popup = false;
				}
			}

			if (show_popup)
				Location_popup(arg0.getPosition());
		}
		return false;
	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	private void sendDataToServer() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.showProcess(PickLocation.this, "Adding Location...");
			}
		});
		try {
			ArrayList<NameValuePair> keyValuePairs = new ArrayList<NameValuePair>();
			String city = data_latlong.getCity_name();
			if (city == null)
				city = "";
			keyValuePairs.add(new KeyValuePair("name", city));
			keyValuePairs.add(new KeyValuePair("latitude", data_latlong
					.getLat()));
			keyValuePairs.add(new KeyValuePair("longitude", data_latlong
					.getLong()));
			keyValuePairs.add(new KeyValuePair("fb_id", fb_id));
			String paramString = URLEncodedUtils.format(keyValuePairs, "utf-8");
			Log.e("Orignal URL", keyValuePairs + "");
			Log.e("paramString", paramString);

			HttpRequest httpRequest = new HttpRequest();
			String response = httpRequest.postData(Constant.add_location,
					keyValuePairs);
			Log.e("parseLoginResponse", response);
			JSONObject loc_data = new JSONObject(response);
			if (loc_data.getInt("errNum") == 77) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Utility.closeprocess(PickLocation.this);
						Toast.makeText(PickLocation.this,
								"Location already added", Toast.LENGTH_SHORT)
								.show();
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (dialog_location != null)
							dialog_location.dismiss();
						Utility.closeprocess(PickLocation.this);
						Toast.makeText(PickLocation.this, "Location Added",
								Toast.LENGTH_SHORT).show();
						Intent i = new Intent();
						setResult(RESULT_OK, i);
						finish();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(PickLocation.this);
					Toast.makeText(PickLocation.this, "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});

		}
	}

	private void setMarkers() {
		Latlong_Data data = (Latlong_Data) getIntent().getSerializableExtra(
				"pref_data");
		for (int i = 0; i < latlong_data.size(); i++) {
			Latlong_Data data1 = latlong_data.get(i);
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(Double.parseDouble(data1.getLat()), Double
							.parseDouble(data1.getLong()))).title(
					data1.getCity_name());
			if (data.getLat().equals(data1.getLat())
					&& data.getLong().equals(data1.getLong())) {
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			} else {
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			}
			googleMap.addMarker(marker);
		}
	}
}
