package com.android.slidingmenu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appdupe.pair.LoginNew;
import com.appdupe.pair.model.KeyValuePair;
import com.appdupe.pair.model.Latlong_Data;
import com.appdupe.pair.model.PreferenceModel;
import com.appdupe.pair.pojo.DeleteUserAccountData;
import com.appdupe.pair.pojo.UpdatePrefrence;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.JsonParser;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairchat.db.DatabaseHandler;
import com.appdupe.pairnofb.R;
import com.edmodo.rangebar.RangeBar;
import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class SettingActivity extends Fragment implements OnClickListener {
	// private static boolean mDebugLog = true;
	// private static final String mDebugTag = "SettingActivity";
	private static final String TAG = "SettingActivity";
	private SharedPreferences preferences;
	private RangeBar rangebar;
	private SeekBar seeklitmitosearch;
	private int minAge, maxAge;
	private TextView// maleTextview,
			// imTextview,ganderNameTextview,femaleTextview,
			// showmemaletextview, showmefemaltextview,
			limitotsearchvalue,
			showagetextview,
			maxage,
			agedeshtextview,
			showdistancetextview,
			distancevalue, MiTextview, kmTextview, minagevalue, show_value,
			// showmetextivew, maleFemaleTextview,
			limitsearchtextview, distanceform;
	// private CheckBox maleChekbox, femaleChekbox;
	// private ToggleButton maletoggle, femaletoggle;
	private Button // contactButton, logoutButton,
			deleteaccount,
			update;
	private int usersexprefrence, radious, distanceunilt = 1;// usersex,
	private boolean isDistanceUinitKm;// ismaleselected, isfemalselected;
	private ProgressDialog mDialog;
	private String distanceunit, userSex, dform, preLat = "", preLong = "",
			fb_id, payment_type = "";
	private RelativeLayout // maletextviewparentlayout,femaletextviewparentlayout,
			MiTextviewparentlayout,
			kmTextviewparentLayout, limittoshowtextlayout;
	LinearLayout layer1, layer2;
	RadioGroup prefrenceradio;
	Spinner radioGroupLocations;
	RadioButton only_men, only_women, both;
	// ImageView back_image;
	// ListView show_values;
	SessionManager mSessionManager;
	private ConnectionDetector cd;
	ToggleButton toggleButtonSearch;
	Typeface Montserrat_Regular;
	ArrayList<Latlong_Data> latlong_data = new ArrayList<Latlong_Data>();
	String city_names[];
	ArrayList<String> city_nameswithoutCurrent = new ArrayList<String>();

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	/**
	 * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
	 * credentials from https://developer.paypal.com
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "AR-eHkF7FZt-AeSn1Ph1b5ofKNoKcCev0ryJNy8EeAhldBu8sSI8xyHamIF1NKa5y_WNJu17vv5HFLpy";

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Luvexpress")
			.merchantPrivacyPolicyUri(Uri.parse(Constant.Privacy_URL))
			.merchantUserAgreementUri(Uri.parse(Constant.Term_URL));
	Dialog dialog_payment_type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settingactivity, null);

		dialog_payment_type = new Dialog(getActivity(),
				android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog_payment_type.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog_payment_type.setContentView(R.layout.payment_type_popup);
		dialog_payment_type.setCancelable(true);
		dialog_payment_type.setCanceledOnTouchOutside(true);

		cd = new ConnectionDetector(getActivity());
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
					.show();
			return view;
		}

		/*
		 * Bundle bundle = this.getArguments(); dform =
		 * bundle.getString("distform"); if (dform == null) dform = "Km";
		 */

		initDataList(view);
		Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getActivity(), null,
						statusCallback, savedInstanceState);

			}
			if (session == null) {
				Dialog dialog_payment_type = new Dialog(getActivity(),
						android.R.style.Theme_Holo_Dialog_NoActionBar);
				dialog_payment_type.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog_payment_type.setContentView(R.layout.payment_type_popup);
				dialog_payment_type.setCancelable(true);
				dialog_payment_type.setCanceledOnTouchOutside(true);
				session = new Session(getActivity());

			}
			Session.setActiveSession(session);

			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

			}
		}

		rangebar.setTickCount(83);
		rangebar.setTickHeight(0);

		rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
			@Override
			public void onIndexChangeListener(RangeBar rangeBar,
					int leftThumbIndex, int rightThumbIndex) {

				/*
				 * if (leftThumbIndex < 18) { minAge = leftThumbIndex + 18; }
				 * else { minAge = leftThumbIndex; }
				 * 
				 * maxAge = rightThumbIndex;
				 * 
				 * minagevalue.setText("" + minAge); maxage.setText("" +
				 * maxAge);
				 * 
				 * /*
				 */
				minagevalue.setText((leftThumbIndex + 18) + "");
				maxage.setText((rightThumbIndex + 18) + "");
				minAge = leftThumbIndex + 18;
				maxAge = rightThumbIndex + 18;
				// leftIndexValue.setText("" + leftThumbIndex);
				// rightIndexValue.setText("" + rightThumbIndex);
			}
		});

		update.setOnClickListener(this);
		MiTextviewparentlayout.setOnClickListener(this);
		kmTextviewparentLayout.setOnClickListener(this);
		limittoshowtextlayout.setOnClickListener(this);
		deleteaccount.setOnClickListener(this);

		getPref();

		((TextView) view.findViewById(R.id.add_location))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								getPaymentstatus();
							}
						}).start();
					}
				});

		((TextView) view.findViewById(R.id.delete_location))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// onCreateDialog();
						// delete_locations_popup();
						dialog();
					}
				});

		radioGroupLocations
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						preLat = latlong_data.get(position).getLat();
						preLong = latlong_data.get(position).getLong();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		return view;
	}

	private void getPref() {
		try {
			mSessionManager = new SessionManager(getActivity());
			Utility.showProcess(getActivity(), "Getting Data...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					latlong_data.clear();
					ArrayList<NameValuePair> keyValuePairs = new ArrayList<NameValuePair>();
					keyValuePairs.add(new KeyValuePair(
							Constant.KEY_FACEBOOK_ID, fb_id));
					HttpRequest httpRequest = new HttpRequest();
					try {
						String response = httpRequest.postData(
								Constant.preference_url, keyValuePairs);
						AppLog.Log(TAG, "preference Response:" + response);
						PreferenceModel prefData = JsonParser
								.parsePreferenceResponse(response);
						if (prefData.getErrFlag() == 0) {
							userSex = prefData.getSex() + "";
							minAge = prefData.getPrefLowerAge();
							maxAge = prefData.getPrefUpperAge();
							usersexprefrence = prefData.getPrefSex();
							preLat = prefData.getPrLat();
							preLong = prefData.getPreLong();
							latlong_data = prefData.getData();
							Latlong_Data lat_long = new Latlong_Data();
							lat_long.setCity_name("My Current Location");
							lat_long.setLat(prefData.getLoginLat());
							lat_long.setLong(prefData.getLoginLong());
							lat_long.setLocationId(0);
							latlong_data.add(0, lat_long);

							Log.e("data", userSex + "/" + minAge + "/" + maxAge
									+ "/" + usersexprefrence + "/" + preLat
									+ "/" + preLong);

							mSessionManager.setUserHeigherAge("" + maxAge);
							mSessionManager.setUserLowerAge("" + minAge);
							mSessionManager.setDistaceUnit("mi");
							mSessionManager.setDistance(prefData
									.getPrefRadius());
							mSessionManager.setUserPrefSex(usersexprefrence
									+ "");
							mSessionManager.setUserSex(userSex);
							mSessionManager.setUsersearchon(prefData
									.getSearchon());

							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Utility.closeprocess(getActivity());
									setData();
								}
							});
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			AppLog.Log(TAG, "onCreate   Exception  " + e);
		}
	}

	private void initDataList(View view) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		fb_id = preferences.getString(Constant.FACEBOOK_ID, "");
		System.out.println("PREFRENCES=" + preferences);
		// imTextview = (TextView) view.findViewById(R.id.imTextview);
		// maleTextview = (TextView) view.findViewById(R.id.maleTextview);
		rangebar = (RangeBar) view.findViewById(R.id.rangebar1);
		// contactButton = (Button) view.findViewById(R.id.contactButton);
		// logoutButton = (Button) view.findViewById(R.id.logoutButton);
		update = (Button) view.findViewById(R.id.update);
		maxage = (TextView) view.findViewById(R.id.maxage);
		minagevalue = (TextView) view.findViewById(R.id.minagevalue);
		agedeshtextview = (TextView) view.findViewById(R.id.agedeshtextview);
		show_value = (TextView) view.findViewById(R.id.show_value);
		toggleButtonSearch = (ToggleButton) view
				.findViewById(R.id.toggleButtonSearch);

		limitotsearchvalue = (TextView) view
				.findViewById(R.id.limitotsearchvalue);

		radioGroupLocations = (Spinner) view
				.findViewById(R.id.radioGroupLocations);

		layer1 = (LinearLayout) view.findViewById(R.id.layer1);
		layer2 = (LinearLayout) view.findViewById(R.id.layer2);
		prefrenceradio = (RadioGroup) view.findViewById(R.id.prefrenceradio);
		only_men = (RadioButton) view.findViewById(R.id.only_men);
		only_women = (RadioButton) view.findViewById(R.id.only_women);
		both = (RadioButton) view.findViewById(R.id.both);
		// back_image = (ImageView) view.findViewById(R.id.back_image);

		// show_values=(ListView)view.findViewById(R.id.show_values);
		//
		// show_values.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// if(usersexprefrence)

		// show_values.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// show_values.setItemChecked(position, true);
		// }
		// });

		// ganderNameTextview = (TextView) view
		// .findViewById(R.id.ganderNameTextview);
		// femaleTextview = (TextView) view.findViewById(R.id.femaleTextview);
		// ganderNameTextview = (TextView) view
		// .findViewById(R.id.ganderNameTextview);
		//
		// showmetextivew = (TextView) view.findViewById(R.id.showmetextivew);
		// maleFemaleTextview = (TextView) view
		// .findViewById(R.id.maleFemaleTextview);
		// showmemaletextview = (TextView) view
		// .findViewById(R.id.showmemaletextview);
		// showmefemaltextview = (TextView) view
		// .findViewById(R.id.showmefemaltextview);
		// maleChekbox = (CheckBox) view.findViewById(R.id.maleChekbox);
		// femaleChekbox = (CheckBox) view.findViewById(R.id.femaleChekbox);
		//
		// maletoggle =(ToggleButton)view.findViewById(R.id.maletoggleButton);
		// femaletoggle
		// =(ToggleButton)view.findViewById(R.id.femaletoggleButton);

		limitsearchtextview = (TextView) view
				.findViewById(R.id.limitsearchtextview);
		seeklitmitosearch = (SeekBar) view.findViewById(R.id.seeklitmitosearch);
		showdistancetextview = (TextView) view
				.findViewById(R.id.showdistancetextview);
		distancevalue = (TextView) view.findViewById(R.id.distancevalue);
		distanceform = (TextView) view.findViewById(R.id.distanceform);
		MiTextview = (TextView) view.findViewById(R.id.MiTextview);
		kmTextview = (TextView) view.findViewById(R.id.kmTextview);

		// maletextviewparentlayout = (RelativeLayout) view
		// .findViewById(R.id.maletextviewparentlayout);
		// femaletextviewparentlayout = (RelativeLayout) view
		// .findViewById(R.id.femaletextviewparentlayout);
		MiTextviewparentlayout = (RelativeLayout) view
				.findViewById(R.id.MiTextviewparentlayout);
		kmTextviewparentLayout = (RelativeLayout) view
				.findViewById(R.id.kmTextviewparentLayout);
		limittoshowtextlayout = (RelativeLayout) view
				.findViewById(R.id.limittoshowtextlayout);

		showagetextview = (TextView) view.findViewById(R.id.showagetextview);

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getActivity()
				.getAssets());
		// imTextview.setTypeface(HelveticaLTStd_Light);
		// imTextview.setTextColor(Color.rgb(0, 0, 0));
		// ganderNameTextview.setTypeface(HelveticaInseratLTStd_Roman);
		// ganderNameTextview.setTextColor(Color.rgb(0, 0, 0));
		//
		// maleTextview.setTypeface(HelveticaLTStd_Light);
		//
		// showmetextivew.setTypeface(HelveticaLTStd_Light);
		// showmetextivew.setTextColor(Color.rgb(0, 0, 0));
		//
		// maleFemaleTextview.setTypeface(HelveticaInseratLTStd_Roman);
		// maleFemaleTextview.setTextColor(Color.rgb(0, 0, 0));
		//
		// showmemaletextview.setTypeface(HelveticaInseratLTStd_Roman);
		// showmemaletextview.setTextColor(Color.rgb(153, 153, 153));
		//
		// showmefemaltextview.setTypeface(HelveticaInseratLTStd_Roman);
		// showmefemaltextview.setTextColor(Color.rgb(153, 153, 153));

		((TextView) view.findViewById(R.id.search))
				.setTypeface(Montserrat_Regular);
		((TextView) view.findViewById(R.id.show_text))
				.setTypeface(Montserrat_Regular);
		((TextView) view.findViewById(R.id.show_view))
				.setTypeface(Montserrat_Regular);
		only_men.setTypeface(Montserrat_Regular);
		only_women.setTypeface(Montserrat_Regular);
		both.setTypeface(Montserrat_Regular);

		limitsearchtextview.setTypeface(Montserrat_Regular);
		limitotsearchvalue.setTypeface(Montserrat_Regular);
		showagetextview.setTypeface(Montserrat_Regular);
		minagevalue.setTypeface(Montserrat_Regular);
		maxage.setTypeface(Montserrat_Regular);
		agedeshtextview.setTypeface(Montserrat_Regular);
		showdistancetextview.setTypeface(Montserrat_Regular);
		distancevalue.setTypeface(Montserrat_Regular);
		MiTextview.setTypeface(Montserrat_Regular);
		kmTextview.setTypeface(Montserrat_Regular);

		deleteaccount = (Button) view.findViewById(R.id.deleteaccount);
		deleteaccount.setVisibility(View.GONE);
	}

	private void setData() {
		if (minAge != 0 || maxAge != 0)
			rangebar.setThumbIndices(minAge - 18, maxAge - 18);
		minagevalue.setText("" + minAge);
		maxage.setText("" + maxAge);
		if (mSessionManager.getUsersearchon() == 1) {
			toggleButtonSearch.setChecked(true);
		} else {
			toggleButtonSearch.setChecked(false);
		}

		seeklitmitosearch
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progressChanged = 0;

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						radious = progress;
						AppLog.Log(TAG, "onProgressChanged  radious");
						limitotsearchvalue.setText("" + radious);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {

					}
				});
		if (mSessionManager.getDistaceUnit().equals("Km")) {
			kmTextviewparentLayout
					.setBackgroundResource(R.drawable.selector_on);
			MiTextviewparentlayout
					.setBackgroundResource(R.drawable.selector_off);

			isDistanceUinitKm = true;
			radious = mSessionManager.getDistance();
			distanceunit = "Km";
			distancevalue.setText("Km");
			distanceunilt = 1;

			kmTextview.setTextColor(Color.rgb(255, 255, 255));
			MiTextview.setTextColor(Color.rgb(153, 153, 153));
		} else {
			MiTextviewparentlayout
					.setBackgroundResource(R.drawable.selector_on);
			kmTextviewparentLayout
					.setBackgroundResource(R.drawable.selector_off);
			isDistanceUinitKm = false;
			radious = mSessionManager.getDistance();
			distanceunit = "Mi";
			distancevalue.setText("Mi");
			distanceunilt = 2;
			MiTextview.setTextColor(Color.rgb(255, 255, 255));
			kmTextview.setTextColor(Color.rgb(153, 153, 153));
		}

		AppLog.Log(TAG, "oncreate radious " + radious);
		seeklitmitosearch.setProgress(radious);

		limitotsearchvalue.setText("" + radious);

		Log.i("TAG", "user sex pref" + usersexprefrence);
		if (usersexprefrence == 3) {
			// TODO set preference
			both.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.tick_white, 0);
			show_value.setText(both.getText());
		} else if (usersexprefrence == 2) {
			only_women.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.tick_white, 0);
			show_value.setText(only_women.getText());
		} else if (usersexprefrence == 1) {
			only_men.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.tick_white, 0);
			show_value.setText(only_men.getText());
		}
		only_men.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				only_men.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.tick_white, 0);
				only_women.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				both.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				usersexprefrence = 1;
				show_value.setText(only_men.getText());
				layer1.setVisibility(View.VISIBLE);
				layer2.setVisibility(View.GONE);
				update.setVisibility(View.VISIBLE);
			}
		});

		only_women.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				only_men.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				only_women.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.tick_white, 0);
				both.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				usersexprefrence = 2;
				show_value.setText(only_women.getText());
				layer1.setVisibility(View.VISIBLE);
				layer2.setVisibility(View.GONE);
				update.setVisibility(View.VISIBLE);
			}
		});
		both.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				only_men.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				only_women.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				both.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.tick_white, 0);
				usersexprefrence = 3;
				show_value.setText(both.getText());
				layer1.setVisibility(View.VISIBLE);
				layer2.setVisibility(View.GONE);
				update.setVisibility(View.VISIBLE);
			}
		});

		prefrenceradio
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.only_men:
							only_men.setCompoundDrawablesWithIntrinsicBounds(0,
									0, R.drawable.tick_white, 0);
							only_women.setCompoundDrawablesWithIntrinsicBounds(
									0, 0, 0, 0);
							both.setCompoundDrawablesWithIntrinsicBounds(0, 0,
									0, 0);
							usersexprefrence = 1;
							show_value.setText(only_men.getText());
							break;
						case R.id.only_women:
							only_men.setCompoundDrawablesWithIntrinsicBounds(0,
									0, 0, 0);
							only_women.setCompoundDrawablesWithIntrinsicBounds(
									0, 0, R.drawable.tick_white, 0);
							both.setCompoundDrawablesWithIntrinsicBounds(0, 0,
									0, 0);
							usersexprefrence = 2;
							show_value.setText(only_women.getText());
							break;
						case R.id.both:
							only_men.setCompoundDrawablesWithIntrinsicBounds(0,
									0, 0, 0);
							only_women.setCompoundDrawablesWithIntrinsicBounds(
									0, 0, 0, 0);
							both.setCompoundDrawablesWithIntrinsicBounds(0, 0,
									R.drawable.tick_white, 0);
							usersexprefrence = 3;
							show_value.setText(both.getText());
							break;
						default:
							break;
						}

						layer1.setVisibility(View.VISIBLE);
						layer2.setVisibility(View.GONE);
						update.setVisibility(View.VISIBLE);
					}
				});

		set_locations();
	}

	private void dialog() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
		alt_bld.setTitle("Select to Delete:");

		String array[] = new String[city_nameswithoutCurrent.size()];
		for (int i = 0; i < city_nameswithoutCurrent.size(); i++) {
			array[i] = city_nameswithoutCurrent.get(i);
		}

		alt_bld.setMultiChoiceItems(array, new boolean[array.length],
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton, boolean isChecked) {

					}
				});
		alt_bld.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ListView list = ((AlertDialog) dialog).getListView();
						ArrayList<String> selectedArray = new ArrayList<String>();
						for (int i = 0; i < list.getCount(); i++) {
							boolean checked = list.isItemChecked(i);
							if (checked) {
								selectedArray.add(list.getItemAtPosition(i)
										.toString());
							}
						}
						/*
						 * Toast.makeText(getActivity(), "Selected digit: " +
						 * sb.toString(), Toast.LENGTH_SHORT).show();
						 */
						String ids = "";
						for (int i = 0; i < latlong_data.size(); i++) {
							for (int j = 0; j < selectedArray.size(); j++) {
								if (selectedArray.get(j).equalsIgnoreCase(
										latlong_data.get(i).getCity_name())) {
									ids = ids
											+ latlong_data.get(i)
													.getLocationId() + ",";
								}
							}
						}
						if (!ids.equals(null) && ids.length() > 0) {
							ids = ids.substring(0, ids.length() - 1);

							if (ids.length() > 0) {
								Log.e("ids==>",""+ids);
								Delete_Location(ids, ((AlertDialog) dialog));
							}
						}

					}
				});
		alt_bld.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		if (city_nameswithoutCurrent.size() > 0) {
			AlertDialog alert = alt_bld.create();
			alert.show();
		} else {
			Toast.makeText(getActivity(), "No Added Location",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void Delete_Location(final String ids, final AlertDialog builder) {
		try {
			Utility.showProcess(getActivity(), "Deleting Location...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					ArrayList<NameValuePair> keyValuePairs = new ArrayList<NameValuePair>();
					keyValuePairs.add(new KeyValuePair("id", ids));
					keyValuePairs.add(new KeyValuePair("fb_id", fb_id));
					HttpRequest httpRequest = new HttpRequest();
					try {
						String response = httpRequest.postData(
								Constant.deleteLocation_url, keyValuePairs);
						AppLog.Log(TAG, "preference Response:" + response);
						final JSONObject json = new JSONObject(response);

						Utility.closeprocess(getActivity());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									Toast.makeText(getActivity(),
											json.getString("errMsg"),
											Toast.LENGTH_SHORT).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

						if (json.getInt("errFlag") == 0) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									builder.dismiss();
									getPref();
								}
							});
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			AppLog.Log(TAG, "onCreate   Exception  " + e);
		}
	}

	private void set_locations() {
		radioGroupLocations.setPopupBackgroundDrawable(new ColorDrawable(
				getActivity().getResources()
						.getColor(R.color.theme_color_light)));
		city_names = new String[latlong_data.size()];
		city_nameswithoutCurrent.clear();
		for (int i = 0; i < latlong_data.size(); i++) {
			city_names[i] = latlong_data.get(i).getCity_name();
			if (!latlong_data.get(i).getCity_name()
					.equalsIgnoreCase("My Current Location")) {
				city_nameswithoutCurrent
						.add(latlong_data.get(i).getCity_name());
			}
		}

		ArrayAdapter<String> Loc_adap = new ArrayAdapter<String>(getActivity(),
				R.layout.city_data, city_names);
		Loc_adap.setDropDownViewResource(R.layout.dropdown_city_popup);
		radioGroupLocations.setAdapter(Loc_adap);

		if (preLat != null && preLong != null) {
			for (int i = 0; i < latlong_data.size(); i++) {
				if (preLat.equals(latlong_data.get(i).getLat())
						&& preLong.equals(latlong_data.get(i).getLong())) {
					radioGroupLocations.setSelection(i);
				}
			}
		}
		// RadioButton answerButton[] = new RadioButton[2];
		// LayoutParams params= new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// for (int i = 0; i < 2; i++) {
		// answerButton[i] = (RadioButton) getActivity().getLayoutInflater()
		// .inflate(R.layout.location_radiobutton, null);
		// answerButton[i].setText("My Current Location");
		// answerButton[i].setTypeface(Montserrat_Regular);
		// answerButton[i].setLayoutParams(params);
		// // answerButton[i].setCompoundDrawablesWithIntrinsicBounds(0, 0,
		// // R.drawable.tick_theme, 0);
		// answerButton[i].setId(i);
		// final int pos = i;
		// radioGroupLocations.addView(answerButton[i]);
		// }
		// radioGroupLocations.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// int count=radioGroupLocations.getChildCount();
		// for(int i=0;i<count;i++)
		// {
		// RadioButton rb=(RadioButton) radioGroupLocations.getChildAt(i);
		// if(i==checkedId)
		// rb.setCompoundDrawablesWithIntrinsicBounds(0, 0,
		// R.drawable.tick_theme, 0);
		// else
		// rb.setCompoundDrawablesWithIntrinsicBounds(0, 0,
		// 0, 0);
		// }
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.MiTextviewparentlayout) {
			if (isDistanceUinitKm) {
				isDistanceUinitKm = false;
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_off);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				distanceunit = "Mi";
				distancevalue.setText(distanceunit);
				MiTextview.setTextColor(Color.rgb(255, 255, 255));
				kmTextview.setTextColor(Color.rgb(153, 153, 153));
			} else {

			}
		}
		if (v.getId() == R.id.limittoshowtextlayout) {
			layer1.setVisibility(View.GONE);
			layer2.setVisibility(View.VISIBLE);
			update.setVisibility(View.GONE);
		}
		// if (v.getId() == R.id.back_image) {
		// layer1.setVisibility(View.VISIBLE);
		// layer2.setVisibility(View.GONE);
		// }
		if (v.getId() == R.id.kmTextviewparentLayout) {
			if (isDistanceUinitKm) {

			} else {
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_on);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				distanceunit = "Km";
				distancevalue.setText(distanceunit);
				kmTextview.setTextColor(Color.rgb(255, 255, 255));
				MiTextview.setTextColor(Color.rgb(153, 153, 153));
				isDistanceUinitKm = true;
			}
		}
		// if (v.getId() == R.id.maletextviewparentlayout) {
		// if (ismaleselected) {
		//
		// } else {
		// ganderNameTextview.setText(getResources().getString(
		// R.string.maletextview));
		// usersex = 1;
		// maletextviewparentlayout
		// .setBackgroundResource(R.drawable.selector_on);
		// femaletextviewparentlayout
		// .setBackgroundResource(R.drawable.selector_off);
		// maleTextview.setTextColor(Color.rgb(255, 255, 255));
		// femaleTextview.setTextColor(Color.rgb(153, 153, 153));
		// ismaleselected = true;
		// }
		// }
		// if (v.getId() == R.id.femaletextviewparentlayout) {
		// if (ismaleselected) {
		// ganderNameTextview.setText(getResources().getString(
		// R.string.femaletextview));
		// usersex = 2;
		// maletextviewparentlayout
		// .setBackgroundResource(R.drawable.selector_off);
		// ;
		// femaletextviewparentlayout
		// .setBackgroundResource(R.drawable.selector_on);
		// femaleTextview.setTextColor(Color.rgb(255, 255, 255));
		// maleTextview.setTextColor(Color.rgb(153, 153, 153));
		// ismaleselected = false;
		//
		// } else {
		//
		// }
		// }
		if (v.getId() == R.id.update) {
			updateUserPrefrence();
		}
		if (v.getId() == R.id.logoutButton) {
			logoutCurrentUser();
		}
		// if (v.getId() == R.id.contactButton) {
		// Intent email = new Intent(Intent.ACTION_SEND/* Intent.ACTION_VIEW
		// */);
		// email.setType("message/rfc822");
		// // email.putExtra(Intent.EXTRA_EMAIL,emailStrArray);
		// email.putExtra(Intent.EXTRA_SUBJECT, "Pair app");
		// email.putExtra(Intent.EXTRA_TEXT, "");
		// email.putExtra(android.content.Intent.EXTRA_EMAIL,
		// new String[] { "info@appdupe.com" });
		// startActivity(Intent.createChooser(email,
		// "Choose an Email client :"));
		// }
		if (v.getId() == R.id.deleteaccount) {
			delettCurrentUser();
		}
	}

	private void logoutCurrentUser() {
		// SessionManager mSessionManager = new SessionManager(getActivity());
		// String sessionToke = mSessionManager.getUserToken();
		// String deviceid = Ultilities.getDeviceId(getActivity());
		// String[] params = { sessionToke, deviceid };
		// new BackGroundTaskForLogout().execute(params);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Logout");
		builder.setMessage("Are you sure you want to logout from this application?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				DatabaseHandler handler = new DatabaseHandler(getActivity());
				handler.clearAllData();
				getActivity().finish();
				startActivity(new Intent(getActivity(), LoginNew.class));
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		//
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void delettCurrentUser() {
		// SessionManager mSessionManager = new SessionManager(getActivity());
		// String sessionToke = mSessionManager.getUserToken();
		// String deviceid = Ultilities.getDeviceId(getActivity());
		String[] params = { preferences.getString(Constant.FACEBOOK_ID, "") };
		new BackGroundTaskForDeleteAccount().execute(params);
	}

	private class BackGroundTaskForDeleteAccount extends
			AsyncTask<String, Void, Void> {
		private String response;
		private boolean responseSuccess;
		private List<NameValuePair> deletuserAccountParameterList;
		private DeleteUserAccountData deleteUserAccountData;
		private Ultilities mUltilities = new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			deletuserAccountParameterList = mUltilities
					.getDeleteUserAccountParameter(params);
			AppLog.Log(TAG,
					"BackGroundTaskForDeleteAccount  deletuserAccountParameterList "
							+ deletuserAccountParameterList);

			try {
				HttpRequest httpRequest = new HttpRequest();
				response = httpRequest.postData(Constant.deleteUserAccount_url,
						deletuserAccountParameterList);

				/*
				 * response = mUltilities.makeHttpRequest(
				 * Constant.deleteUserAccount_url, Constant.methodeName,
				 * deletuserAccountParameterList);
				 */
				AppLog.Log(TAG, "BackGroundTaskForDeleteAccount  response "
						+ response);
				Gson gson = new Gson();
				deleteUserAccountData = gson.fromJson(response,
						DeleteUserAccountData.class);
				if (deleteUserAccountData.getErrFlag() == 0
						&& deleteUserAccountData.getErrNum() == 61) {
					Session session = Session.getActiveSession();
					AppLog.Log(TAG, "BackGroundTaskForLogout  session "
							+ session);
					if (!session.isClosed()) {
						AppLog.Log(TAG,
								"BackGroundTaskForLogout  session not close need to cleaar  "
										+ session.getState());
						session.closeAndClearTokenInformation();
					} else {
						// nothing session already close no need to clear
					}

					// DatabaseHandler databaseHandler=new
					// DatabaseHandler(getActivity());
					// databaseHandler.deleteUserData();
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 31) {
					Session session = Session.getActiveSession();
					AppLog.Log(TAG, "BackGroundTaskForLogout  session "
							+ session);
					if (!session.isClosed()) {
						AppLog.Log(TAG,
								"BackGroundTaskForLogout  session not close need to cleaar  "
										+ session.getState());
						session.closeAndClearTokenInformation();

					} else {
						// nothing session already close no need to clear
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				mDialog = mUltilities.GetProcessDialog(getActivity());
				mDialog.setMessage("Please wait..");
				mDialog.setCancelable(true);
				mDialog.show();
			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackGroundTaskForLogout onPreExecute Exception " + e);
			}
		}

		@Override
		protected void onPostExecute(java.lang.Void result) {
			super.onPostExecute(result);
			try {
				mDialog.dismiss();
				if (deleteUserAccountData.getErrFlag() == 0
						&& deleteUserAccountData.getErrNum() == 61) {
					// Intent intent = new Intent(Intent.ACTION_MAIN);
					// intent.addCategory(Intent.CATEGORY_HOME);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(intent);
					// getActivity().finish();
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager.logoutUser();
					Intent intent = new Intent(getActivity(), LoginNew.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().finish();
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 62) {
					ErrorMessage("Alert", deleteUserAccountData.getErrMsg());
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 31) {
					ErrorMessageInvalidSessionTOken("Alert",
							deleteUserAccountData.getErrMsg());
				} else {
					// some thing wrong is happend
				}

			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackGroundTaskForLogout onPostExecute Exception " + e);

			}
		}
	}

	// private class BackGroundTaskForLogout extends AsyncTask<String, Void,
	// Void> {
	//
	// private String response;
	// private boolean responseSuccess;
	// private List<NameValuePair> logOutParameter;
	// private LogOutData logOutData;
	// private Ultilities mUltilities = new Ultilities();
	//
	// @Override
	// protected Void doInBackground(String... params) {
	// logDebug("BackGroundTaskForLogout");
	// try {
	// logOutParameter = mUltilities.getLogOutParameter(params);
	// logDebug("BackGroundTaskForLogout  logOutParameter"
	// + logOutParameter);
	// response = mUltilities.makeHttpRequest(Constant.logout_url,
	// Constant.methodeName, logOutParameter);
	// logDebug("BackGroundTaskForLogout  response" + response);
	// Gson gson = new Gson();
	// logOutData = gson.fromJson(response, LogOutData.class);
	// logDebug("BackGroundTaskForLogout  errornumber is "
	// + logOutData.getErrNum());
	// logDebug("BackGroundTaskForLogout  errorFlag is "
	// + logOutData.getErrFlag());
	// if (logOutData.getErrFlag() == 0
	// && logOutData.getErrNum() == 41) {
	// Session session = Session.getActiveSession();
	// logDebug("BackGroundTaskForLogout  session " + session);
	// if (!session.isClosed()) {
	// logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
	// + session.getState());
	// session.closeAndClearTokenInformation();
	// } else {
	// // nothing session already close no need to clear
	// }
	//
	// // DatabaseHandler databaseHandler=new
	// // DatabaseHandler(getActivity());
	// // databaseHandler.deleteUserData();
	// } else if (logOutData.getErrFlag() == 1
	// && logOutData.getErrNum() == 31) {
	// Session session = Session.getActiveSession();
	// logDebug("BackGroundTaskForLogout  session " + session);
	// if (!session.isClosed()) {
	// logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
	// + session.getState());
	// session.closeAndClearTokenInformation();
	//
	// } else {
	// // nothing session already close no need to clear
	// }
	// }
	//
	// } catch (Exception e) {
	// logError("BackGroundTaskForLogout  doInBackground Exception  "
	// + e);
	// }
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// try {
	// mDialog.dismiss();
	// if (logOutData.getErrFlag() == 0
	// && logOutData.getErrNum() == 41) {
	// SessionManager mSessionManager = new SessionManager(
	// getActivity());
	// mSessionManager.logoutUser();
	// Intent intent = new Intent(getActivity(),
	// LoginUsingFacebook.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(intent);
	// getActivity().finish();
	// } else if (logOutData.getErrFlag() == 1
	// && logOutData.getErrNum() == 37) {
	// ErrorMessage("Alert", logOutData.getErrMsg());
	// } else if (logOutData.getErrFlag() == 1
	// && logOutData.getErrNum() == 31) {
	// ErrorMessageInvalidSessionTOken("Alert",
	// logOutData.getErrMsg());
	// }
	//
	// } catch (Exception e) {
	// logError("BackGroundTaskForLogout onPostExecute Exception " + e);
	// }
	//
	// /*
	// * Error: { "errNum": "37", "errFlag": "1", "errMsg":
	// * "Server Error! Please try again after sometime!" }
	// *
	// *
	// * Error: { "errNum": "31", "errFlag": "1", "errMsg":
	// * "invalid session token!" } Success: Error: { "errNum": "41",
	// * "errFlag": "0", "errMsg": "Logged out successfully" }
	// */
	//
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	//
	// try {
	// mDialog = mUltilities.GetProcessDialog(getActivity());
	// mDialog.setMessage("Please wait..");
	// mDialog.setCancelable(true);
	// mDialog.show();
	// } catch (Exception e) {
	// logError("BackGroundTaskForLogout onPreExecute Exception " + e);
	// }
	// }
	// }

	private void updateUserPrefrence() {
		SessionManager mSessionManager = new SessionManager(getActivity());
		String sessionToke = mSessionManager.getUserToken();
		String deviceid = Ultilities.getDeviceId(getActivity());
		String loweragePrefrence = "" + minAge;
		String heigherAge = "" + maxAge;
		String sexPrefrence = "" + usersexprefrence;
		String selectedusersex = "" + preferences.getInt(Constant.KEY_SEX, 0);
		String userSelectedRadius = "" + radious;
		String seachon = "";
		if (toggleButtonSearch.isChecked())
			seachon = "1";
		else
			seachon = "0";
		String[] params = { sessionToke, deviceid, selectedusersex,
				sexPrefrence, loweragePrefrence, heigherAge,
				userSelectedRadius, "" + distanceunilt,
				preferences.getString(Constant.FACEBOOK_ID, ""), seachon,
				preLat, preLong };
		new BackgroundTaskForUpdatePrefrence().execute(params);
	}

	private class BackgroundTaskForUpdatePrefrence extends
			AsyncTask<String, Void, Void> {
		private String response;
		private boolean responseSuccess;
		private List<NameValuePair> prefrenceUpdateParameter;
		private UpdatePrefrence mUpdatePrefrence;
		private Ultilities mUltilities = new Ultilities();
		private String loweragePrefrence, heigherAge, sexPrefrence,
				selectedusersex, userSelectedRadius;

		@Override
		protected Void doInBackground(String... params) {
			try {
				selectedusersex = params[2];
				sexPrefrence = params[3];
				loweragePrefrence = params[4];
				heigherAge = params[5];
				userSelectedRadius = params[6];

				prefrenceUpdateParameter = mUltilities
						.getUserPrefrenceParameter(params);
				// response = mUltilities.makeHttpRequest(
				// Constant.updatePrefrence_url, Constant.methodeName,
				// prefrenceUpdateParameter);

				HttpRequest httpRequest = new HttpRequest();
				response = httpRequest.postData(Constant.updatePrefrence_url,
						prefrenceUpdateParameter);
				AppLog.Log(TAG,
						"BackgroundTaskForUpdatePrefrence doInBackground  response "
								+ response);
				Gson gson = new Gson();
				mUpdatePrefrence = gson.fromJson(response,
						UpdatePrefrence.class);
			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackgroundTaskForUpdatePrefrence doInBackground  Exception "
								+ e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				mDialog.dismiss();
				if (mUpdatePrefrence.getErrFlag() == 0
						&& mUpdatePrefrence.getErrNum() == 13) {
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager.setDistance(Integer
							.parseInt(userSelectedRadius));
					mSessionManager.setUserHeigherAge(heigherAge);
					mSessionManager.setUserLowerAge(loweragePrefrence);

					mSessionManager.setUserSex(selectedusersex);
					mSessionManager.setUserPrefSex(sexPrefrence);
					mSessionManager.setDistaceUnit(distanceunit);

					Toast.makeText(getActivity(), mUpdatePrefrence.getErrMsg(),
							Toast.LENGTH_SHORT).show();
					// "Preferences updated successfully."

				} else if (mUpdatePrefrence.getErrFlag() == 1
						&& mUpdatePrefrence.getErrNum() == 14) {
					// Change any preferences to update
					mDialog.dismiss();
					ErrorMessage("Alert", mUpdatePrefrence.getErrMsg());
				} else if (mUpdatePrefrence.getErrFlag() == 1
						&& mUpdatePrefrence.getErrNum() == 15) {
					// Change any preferences to update
					mDialog.dismiss();
					ErrorMessage("Alert", mUpdatePrefrence.getErrMsg());
				} else {
					mDialog.dismiss();
					ErrorMessage("Alert",
							"Sorry! Server not able to process your request please  try again");
				}

			} catch (Exception e) {
				AppLog.Log(TAG,
						" BackgroundTaskForUpdatePrefrence onPostExecute Exception "
								+ e);
			}

			/*
			 * Error: { "errNum": "14", "errFlag": "1", "errMsg":
			 * "Change any preferences to update." }
			 * 
			 * Success: { "errNum": "13", "errFlag": "0", "errMsg":
			 * "Preferences updated successfully." }
			 */

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(getActivity());
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}

	}

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageInvalidSessionTOken(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SessionManager mSessionManager = new SessionManager(
								getActivity());
						mSessionManager.logoutUser();
						Intent intent = new Intent(getActivity(),
								LoginNew.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						getActivity().finish();
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();
		}
	}

	private void Buy_change_location_package(String value) {
		/*
		 * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
		 * Change PAYMENT_INTENT_SALE to - PAYMENT_INTENT_AUTHORIZE to only
		 * authorize payment and capture funds later. - PAYMENT_INTENT_ORDER to
		 * create a payment for authorization and capture later via calls from
		 * your server.
		 * 
		 * Also, to include additional payment details and an item list, see
		 * getStuffToBuy() below.
		 */
		String message = "";
		if (value.equalsIgnoreCase("1")) {
			message = "One day subscription charges!!";
		} else {
			message = "One month subscription charges!!";
		}
		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
				"USD", message, PayPalPayment.PAYMENT_INTENT_SALE);

		/*
		 * See getStuffToBuy(..) for examples of some available payment options.
		 */

		Intent intent = new Intent(getActivity(), PaymentActivity.class);
		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(getActivity(), Constant.flurryKey);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 101) {
			if (resultCode == Activity.RESULT_OK)
				getPref();
		} else if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(TAG, confirm.toJSONObject().toString(4));
						Log.i(TAG, confirm.getPayment().toJSONObject()
								.toString(4));
						final String amount = confirm.getPayment()
								.toJSONObject().getString("amount");
						final String transactionid = confirm.toJSONObject()
								.getJSONObject("response").getString("id");

						new Thread(new Runnable() {
							@Override
							public void run() {
								sendPaymentToServer(amount, transactionid);
							}
						}).start();
						/**
						 * TODO: send 'confirm' and possibly
						 * confirm.getPayment() to your server for verification
						 * or consent completion. See
						 * https://developer.paypal.com
						 * /webapps/developer/docs/integration
						 * /mobile/verify-mobile-payment/ for more details.
						 * 
						 * For sample mobile backend interactions, see
						 * https://github
						 * .com/paypal/rest-api-sdk-python/tree/master
						 * /samples/mobile_backend
						 */
						// Toast.makeText(
						// getApplicationContext(),
						// "PaymentConfirmation info received from PayPal",
						// Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ",
								e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				if (dialog_payment_type != null
						&& !dialog_payment_type.isShowing()) {
					dialog_payment_type.show();
				}
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				if (dialog_payment_type != null
						&& !dialog_payment_type.isShowing()) {
					dialog_payment_type.show();
				}
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		} else {
			Session.getActiveSession().onActivityResult(getActivity(),
					requestCode, resultCode, data);
		}
	}

	private void sendPaymentToServer(String amount, String id) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.showProcess(getActivity(), "Saving Payment Details...");
			}
		});
		try {
			ArrayList<NameValuePair> keyValuePairs = new ArrayList<NameValuePair>();
			keyValuePairs.add(new KeyValuePair("transaction_id", id));
			keyValuePairs.add(new KeyValuePair("type", payment_type));
			keyValuePairs.add(new KeyValuePair("amount", amount));
			keyValuePairs.add(new KeyValuePair("fb_id", fb_id));
			String paramString = URLEncodedUtils.format(keyValuePairs, "utf-8");
			Log.e("Orignal URL", keyValuePairs + "");
			Log.e("paramString", paramString);

			HttpRequest httpRequest = new HttpRequest();
			String response = httpRequest.postData(Constant.add_transaction,
					keyValuePairs);
			Log.e("parseLoginResponse", response);
			JSONObject loc_data = new JSONObject(response);
			if (loc_data.getInt("errFlag") == 0) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "Payment Saved",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(getActivity());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(getActivity());
					Toast.makeText(getActivity(), "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void getPaymentstatus() {
		int status = 0;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.showProcess(getActivity(), "Getting Status...");
			}
		});
		try {
			ArrayList<NameValuePair> keyValuePairs = new ArrayList<NameValuePair>();
			keyValuePairs.add(new KeyValuePair("fb_id", fb_id));
			HttpRequest httpRequest = new HttpRequest();
			String response = httpRequest.postData(
					Constant.check_subscription_status, keyValuePairs);
			Log.e("parseLoginResponse", response);
			JSONObject pay_data = new JSONObject(response);
			if (pay_data.getInt("errFlag") == 0) {
				status = 1;
			} else if (pay_data.getInt("errFlag") == 1) {
				status = 0;
			}

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(getActivity());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			status = -1;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(getActivity());
					Toast.makeText(getActivity(), "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		if (status == 1) {
			Latlong_Data pref_data = new Latlong_Data();
			pref_data.setLat(preLat);
			pref_data.setLong(preLong);
			pref_data.setCity_name("My Preferred Location");
			startActivityForResult(
					new Intent(getActivity(), PickLocation.class).putExtra(
							"latlong_data", latlong_data).putExtra("pref_data",
							pref_data), 101);
		} else if (status == 0) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					final RadioButton one_time = (RadioButton) dialog_payment_type
							.findViewById(R.id.one_time);
					final RadioButton one_month = (RadioButton) dialog_payment_type
							.findViewById(R.id.one_month);
					Button continue_ = (Button) dialog_payment_type
							.findViewById(R.id.continue_);
					Button cancel = (Button) dialog_payment_type
							.findViewById(R.id.cancel);

					continue_.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (one_time.isChecked()) {
								payment_type = "0";
								Buy_change_location_package("1");
								dialog_payment_type.dismiss();
							} else if (one_month.isChecked()) {
								payment_type = "1";
								Buy_change_location_package("19.99");
								dialog_payment_type.dismiss();
							} else
								Toast.makeText(getActivity(),
										"Please select any payment type first",
										Toast.LENGTH_SHORT).show();
						}
					});
					cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog_payment_type.dismiss();
						}
					});
					dialog_payment_type.show();
				}
			});
		} else {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(getActivity());
					Toast.makeText(getActivity(), "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/*
	 * @Override protected void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState); Session session =
	 * Session.getActiveSession(); Session.saveSession(session, outState); }
	 */
}