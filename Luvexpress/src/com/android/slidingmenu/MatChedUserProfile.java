package com.android.slidingmenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.appdupe.pair.ExtendedGallery;
import com.appdupe.pair.pojo.GellaryData;
import com.appdupe.pair.pojo.InviteActionData;
import com.appdupe.pair.pojo.MatchesData;
import com.appdupe.pair.pojo.userProFileData;
import com.appdupe.pair.utility.AlertDialogManager;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pairnofb.R;
import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.Settings;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MatChedUserProfile extends Activity implements OnClickListener {
	private static final String TAG = "MatChedUserProfile";
	private static boolean mDebugLog = true;
	private static String mDebugTag = "MatChedUserProfile";
	private ExtendedGallery imageExtendedGallery;
	private LinearLayout image_count;
	private TextView usernametextivew, ueragetextviw, distancetextview,
			activetimetextview, abouttextview, abouttextviewvalues,
			viewMatchedProfiletextview, statusTextView;
	private RelativeLayout Aboutuseragelayout, likedislikebuttonlayout,
			matcheduserProfileToplayout;
	private Button likeButton, dislikebutton;
	private ArrayList<GellaryData> imageList;
	private ImageAdapterForGellary mAdapterForGellary;
	private ProgressDialog mDialog;
	private int[] imageHeightandWIdth;
	private ConnectionDetector cd;
	private RelativeLayout.LayoutParams layoutParams;
	private int count;
	private ImageView[] page_text;
	private SharedPreferences preferences;
	MatchesData data;
	Typeface Montserrat_Regular;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matcheduserprofile);
		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
			return;
		}
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initLayoutResource();

		imageList = new ArrayList<GellaryData>();
		mAdapterForGellary = new ImageAdapterForGellary(this, imageList);
		imageExtendedGallery.setAdapter(mAdapterForGellary);
		Ultilities ultilities = new Ultilities();

		imageHeightandWIdth = ultilities
				.getImageHeightAndWidthForProfileGellary(this);
		cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectingToInternet()) {

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				if (bundle.getBoolean(Constant.isFromChatScreen)) {
					matcheduserProfileToplayout.setVisibility(View.GONE);
					getUserProfile(true);
				} else {
					data = (MatchesData) getIntent().getSerializableExtra(
							"match_data");
					getUserProfile(false);
				}
			}
		} else {
			AlertDialogManager
					.internetConnetionErrorAlertDialog(MatChedUserProfile.this);
		}

		Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);

		try {
			likeButton.setOnClickListener(this);
			dislikebutton.setOnClickListener(this);
		} catch (Exception e) {
		}

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				imageHeightandWIdth[1], imageHeightandWIdth[0]);
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 5, 0, 0);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 5, 0, 0);

		imageExtendedGallery
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						for (int i = 0; i < count; i++) {
							page_text[i]
									.setImageResource(R.drawable.white_dot_light);
						}
						page_text[pos]
								.setImageResource(R.drawable.white_dot_dark);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		((ImageView) findViewById(R.id.back_image))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		((ImageView) findViewById(R.id.options))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final Dialog options_popup = BasicFunctions
								.options_popup(MatChedUserProfile.this);

						TextView unmatch = (TextView) options_popup
								.findViewById(R.id.unmatch);
						TextView report = (TextView) options_popup
								.findViewById(R.id.report);
						TextView show_profile = (TextView) options_popup
								.findViewById(R.id.show_profile);
						TextView hide_moments = (TextView) options_popup
								.findViewById(R.id.hide_moments);
						String name = data.getFirstName();
						unmatch.setText("Unmatch " + name);
						report.setText("Report " + name);
						show_profile.setText("Show " + name + "'s Profile");
						hide_moments.setText("Hide  " + name + "'s Moments");

						Button Cancel = (Button) options_popup
								.findViewById(R.id.cancel);
						unmatch.setTypeface(Montserrat_Regular);
						report.setTypeface(Montserrat_Regular);
						show_profile.setTypeface(Montserrat_Regular);
						hide_moments.setTypeface(Montserrat_Regular);
						Cancel.setTypeface(Montserrat_Regular);

						unmatch.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								options_popup.dismiss();
							}
						});
						report.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								options_popup.dismiss();
								startActivity(new Intent(
										MatChedUserProfile.this,
										Report_user.class));
							}
						});
						show_profile.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								options_popup.dismiss();
							}
						});
						hide_moments.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								options_popup.dismiss();
							}
						});

						Cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								options_popup.dismiss();
							}
						});
					}
				});
	}

	private void initLayoutResource() {
		viewMatchedProfiletextview = (TextView) findViewById(R.id.viewMatchedProfiletextview);
		likeButton = (Button) findViewById(R.id.likeButton);
		dislikebutton = (Button) findViewById(R.id.dislikebutton);
		imageExtendedGallery = (ExtendedGallery) findViewById(R.id.imageExtendedGallery);
		image_count = (LinearLayout) findViewById(R.id.image_count);
		usernametextivew = (TextView) findViewById(R.id.usernametextivew);
		ueragetextviw = (TextView) findViewById(R.id.ueragetextviw);
		distancetextview = (TextView) findViewById(R.id.distancetextview);
		activetimetextview = (TextView) findViewById(R.id.activetimetextview);
		statusTextView = (TextView) findViewById(R.id.txtMatchedUserStatus);
		abouttextview = (TextView) findViewById(R.id.abouttextview);
		Aboutuseragelayout = (RelativeLayout) findViewById(R.id.Aboutuseragelayout);
		Aboutuseragelayout.setVisibility(View.GONE);
		likedislikebuttonlayout = (RelativeLayout) findViewById(R.id.likedislikebuttonlayout);
		matcheduserProfileToplayout = (RelativeLayout) findViewById(R.id.matcheduserProfileToplayout);
		abouttextviewvalues = (TextView) findViewById(R.id.abouttextviewvalues);

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getAssets());
		viewMatchedProfiletextview.setTypeface(Montserrat_Regular);

		usernametextivew.setTypeface(Montserrat_Regular);
		statusTextView.setTypeface(Montserrat_Regular);

		ueragetextviw.setTypeface(Montserrat_Regular);

		abouttextview.setTypeface(Montserrat_Regular);

		abouttextviewvalues.setTypeface(Montserrat_Regular);

		distancetextview.setTypeface(Montserrat_Regular);

		activetimetextview.setTypeface(Montserrat_Regular);
	}

	private void getUserProfile(Boolean value) {
		SessionManager mSessionManager = new SessionManager(this);
		String macheduserFacebookid = mSessionManager
				.getMatchedUserFacebookId();
		AppLog.Log(TAG, "Matched UserFacebook ID:" + macheduserFacebookid);
		// String userSessionToken = mSessionManager.getUserToken();
		// String userDeviceId = Ultilities.getDeviceId(this);
		if (macheduserFacebookid != null && macheduserFacebookid.length() > 0) {
			if (value) {
				String[] params = { macheduserFacebookid };
				new BackGroundTaskForUserProfile().execute(params);
			} else {
				String[] images = data.getImage();
				for (int i = 0; i < images.length; i++) {
					GellaryData mGellaryData = new GellaryData();
					mGellaryData.setImageUrl(images[i]);
					imageList.add(mGellaryData);

				}

				page_text = new ImageView[imageList.size()];
				count = imageList.size();
				image_count.removeAllViews();
				for (int i = 0; i < imageList.size(); i++) {
					page_text[i] = new ImageView(MatChedUserProfile.this);
					page_text[i].setPadding(2, 2, 2, 2);
					page_text[i].setImageResource(R.drawable.white_dot_dark);
					image_count.addView(page_text[i]);

				}

				mAdapterForGellary.notifyDataSetChanged();
				if (data.getStatus() != null && !data.getStatus().equals("")) {
					statusTextView.setText(data.getStatus());
				} else {
					statusTextView.setText("");
				}
				ueragetextviw.setText(data.getAge() + "yrs");
				usernametextivew.setText("" + data.getFirstName());
				viewMatchedProfiletextview.setText(data.getFirstName());

				Location loc1 = new Location("");
				loc1.setLatitude(Double.parseDouble(preferences.getString(
						Constant.PREF_USER_LAT, "" + 0)));
				loc1.setLongitude(Double.parseDouble(preferences.getString(
						Constant.PREF_USER_LONG, "" + 0)));

				Location loc2 = new Location("");
				loc2.setLatitude(Double.parseDouble(data.getLat()));
				loc2.setLongitude(Double.parseDouble(data.getLong()));

				double distanceInMiles = loc1.distanceTo(loc2) * 0.00062137;

				distancetextview.setText(String.format("%.0f", distanceInMiles)
						+ " miles away");

				String gmtTime = data.getLastActive();
				try {
					Calendar c = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String s = sdf.format(c.getTime());
					Date current_date = sdf.parse(s);
					Date last_active_date = sdf.parse(gmtTime);

					Log.e("date", current_date.toString() + "/"
							+ last_active_date);

					activetimetextview.setText(BasicFunctions
							.getDateDiffString(last_active_date, current_date));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (data.getPersDesc() != null
						&& data.getPersDesc().length() > 0) {
					Aboutuseragelayout.setVisibility(View.VISIBLE);
					abouttextview.setText("About  " + "" + data.getFirstName());
					abouttextviewvalues.setText("" + data.getPersDesc());

				} else {
					Aboutuseragelayout.setVisibility(View.GONE);
				}
			}
		} else {
			ErrorMessageMandetoryFiledMissing(
					getResources().getString(R.string.alert), getResources()
							.getString(R.string.retriedmessage));
		}
	}

	private class BackGroundTaskForUserProfile extends
			AsyncTask<String, Void, Void> {
		Ultilities mUltilities = new Ultilities();
		private String getProfileResponse;
		private List<NameValuePair> userProfileNameValuePairList;
		private userProFileData mUserProFileData;
		private GellaryData mGellaryData;

		// private SessionManager sessionManager = new SessionManager(
		// MatChedUserProfile.this);

		@Override
		protected Void doInBackground(String... params) {
			try {
				userProfileNameValuePairList = mUltilities
						.getUserProfileParameter(params);
				
				HttpRequest httpRequest = new HttpRequest();
				getProfileResponse=httpRequest.postData(Constant.getProfile_url,
						userProfileNameValuePairList);
				
				
				/*getProfileResponse = mUltilities.makeHttpRequest(
						Constant.getProfile_url, Constant.methodeName,
						userProfileNameValuePairList);*/
				AppLog.Log(TAG, "Matched User Profile response-------->"
						+ getProfileResponse);
				// logDebug("BackGroundTaskForUserProfile  getProfileResponse "+getProfileResponse);
				Gson gson = new Gson();
				mUserProFileData = gson.fromJson(getProfileResponse,
						userProFileData.class);

				String[] images = mUserProFileData.getImages();

				for (int i = 0; i < images.length; i++) {
					mGellaryData = new GellaryData();
					mGellaryData.setImageUrl(images[i]);
					imageList.add(mGellaryData);
				}

				runOnUiThread(new Runnable() {
					public void run() {

						if (mDialog != null) {
							mDialog.dismiss();
						}

						page_text = new ImageView[imageList.size()];
						count = imageList.size();
						image_count.removeAllViews();
						for (int i = 0; i < imageList.size(); i++) {
							page_text[i] = new ImageView(
									MatChedUserProfile.this);
							page_text[i].setPadding(2, 2, 2, 2);
							page_text[i]
									.setImageResource(R.drawable.white_dot_dark);
							image_count.addView(page_text[i]);

						}

						mAdapterForGellary.notifyDataSetChanged();
						if (mUserProFileData.getStatus() != null
								&& !mUserProFileData.getStatus().equals("")) {
							statusTextView
									.setText(mUserProFileData.getStatus());
						} else {
							statusTextView.setText("");
						}
						ueragetextviw
								.setText(mUserProFileData.getAge() + "yrs");
						usernametextivew.setText(""
								+ mUserProFileData.getFirstName());
						viewMatchedProfiletextview.setText(mUserProFileData
								.getFirstName());

						Location loc1 = new Location("");
						loc1.setLatitude(Double.parseDouble(preferences
								.getString(Constant.PREF_USER_LAT, "" + 0)));
						loc1.setLongitude(Double.parseDouble(preferences
								.getString(Constant.PREF_USER_LONG, "" + 0)));

						Location loc2 = new Location("");
						loc2.setLatitude(Double.parseDouble(mUserProFileData
								.getLati()));
						loc2.setLongitude(Double.parseDouble(mUserProFileData
								.getLong()));

						double distanceInMiles = loc1.distanceTo(loc2) * 0.00062137;

						distancetextview.setText(String.format("%.0f",
								distanceInMiles) + " miles away");

						String gmtTime = mUserProFileData.getLastActive();
						try {
							Calendar c = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							String s = sdf.format(c.getTime());
							Date current_date = sdf.parse(s);
							Date last_active_date = sdf.parse(gmtTime);

							Log.e("date", current_date.toString() + "/"
									+ last_active_date);

							activetimetextview.setText(BasicFunctions
									.getDateDiffString(last_active_date,
											current_date));
						} catch (ParseException e) {
							e.printStackTrace();
						}

						if (mUserProFileData.getPersDesc() != null
								&& mUserProFileData.getPersDesc().length() > 0) {
							Aboutuseragelayout.setVisibility(View.VISIBLE);
							abouttextview.setText("About  " + ""
									+ mUserProFileData.getFirstName());
							abouttextviewvalues.setText(""
									+ mUserProFileData.getPersDesc());

						} else {
							Aboutuseragelayout.setVisibility(View.GONE);
						}

						// page_text = new ImageView[imageList.size()];
						// count = imageList.size();
						// image_count.removeAllViews();
						// for (int i = 0; i < imageList.size(); i++) {
						// page_text[i] = new ImageView(
						// MatChedUserProfile.this);
						// page_text[i].setPadding(2, 2, 2, 2);
						// page_text[i]
						// .setImageResource(R.drawable.white_dot_dark);
						// image_count.addView(page_text[i]);
						// }
						//
						// mAdapterForGellary.notifyDataSetChanged();
						// if (mUserProFileData.getStatus() != null
						// && !mUserProFileData.getStatus().equals("")) {
						// statusTextView
						// .setText(mUserProFileData.getStatus());
						// } else {
						// statusTextView.setText("N/A");
						// }
						// ueragetextviw.setText("" +
						// mUserProFileData.getAge());
						// usernametextivew.setText(""
						// + mUserProFileData.getFirstName());
						// viewMatchedProfiletextview.setText(""
						// + mUserProFileData.getFirstName());
						// SessionManager sessionManager = new SessionManager(
						// MatChedUserProfile.this);
						// String DistanceUinit = null;
						// if (sessionManager.getDistaceUnit().equals("Km")) {
						// DistanceUinit = "Km.";
						// } else {
						// DistanceUinit = "miles.";
						// }
						//
						// distancetextview.setText("Less then "
						// + mUserProFileData.getDistance() + " "
						// + DistanceUinit + " away");
						// String gmtTime = mUserProFileData.getLastActive();
						// // gmtTime=gmtTime.replaceAll("-", " ");
						//
						// String localTime =
						// UltilitiesDate.getLocalTime(gmtTime);
						// // String
						// // curentTime=ultilities.getCurrentDateYYYYMMdd();
						// String dataString = UltilitiesDate
						// .datesString(localTime);
						// UltilitiesDate ultilitiesDate = new UltilitiesDate();
						// int days = ultilitiesDate.getDays();
						// int hours = ultilitiesDate.getHours();
						//
						// activetimetextview.setText("active " + days + " -d  "
						// + hours + "- Hour ago");
						// if (mUserProFileData.getPersDesc() != null
						// && mUserProFileData.getPersDesc().length() > 0) {
						// Aboutuseragelayout.setVisibility(View.VISIBLE);
						// abouttextview.setText("About  " + ""
						// + mUserProFileData.getFirstName());
						// abouttextviewvalues.setText(""
						// + mUserProFileData.getPersDesc());
						//
						// } else {
						// Aboutuseragelayout.setVisibility(View.GONE);
						// }
					}
				});

			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackGroundTaskForUserProfile   doInBackground Exception"
								+ e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {

				if (mDialog != null) {
					mDialog.dismiss();
				}

			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackGroundTaskForUserProfile   onPostExecute Exception  "
								+ e);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(MatChedUserProfile.this);
			mDialog.setMessage("Please Wait..");
			mDialog.setCancelable(false);
			mDialog.show();
		}

	}

	private class ImageAdapterForGellary extends ArrayAdapter<GellaryData> {
		Activity mActivity;
		AQuery aQuery;
		private LayoutInflater mInflater;
		private ImageOptions options;

		public ImageAdapterForGellary(Activity context,
				List<GellaryData> objects) {
			super(context, R.layout.galleritem, objects);
			mActivity = context;
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			options = new ImageOptions();
			options.memCache = true;
			options.fileCache = true;
			options.animation = AQuery.FADE_IN;
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleritem, null);
				aQuery = new AQuery(convertView);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);
				// holder.mProgressBar = (ProgressBar) convertView
				// .findViewById(R.id.pbGalleryItemImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// holder.mProgressBar.setId(position);
			holder.imageview.setId(position);
			
//			aQuery.id(holder.imageview).image(getItem(position).getImageUrl(), options)
//			.progress(R.id.pbGalleryItemImage);
//
			Picasso.with(MatChedUserProfile.this)
					.load(getItem(position).getImageUrl())
					.error(R.drawable.error)
					.resize(imageHeightandWIdth[1], imageHeightandWIdth[0])
					.into(holder.imageview);

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			// ProgressBar mProgressBar;

		}
	}

	public void onStart() {
		super.onStart();

		// Session.getActiveSession().addCallback(statusCallback);
		// FlurryAgent.onStartSession(this, Constant.flurryKey);

	}

	@Override
	public void onStop() {
		super.onStop();
		// Session.getActiveSession().removeCallback(statusCallback);
		// FlurryAgent.onEndSession(this);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.likeButton) {
			likeMatchedUser("1");
		}
		if (v.getId() == R.id.dislikebutton) {
			likeMatchedUser("2");
		}
	}

	private void likeMatchedUser(String action) {
		SessionManager mSessionManager = new SessionManager(
				MatChedUserProfile.this);
		String sessionToke = mSessionManager.getUserToken();
		String devieceId = Ultilities.getDeviceId(MatChedUserProfile.this);
		String MatchedUserFacebookId = mSessionManager
				.getMatchedUserFacebookId();
		String userAction = action;
		// String[] params = { sessionToke, devieceId, MatchedUserFacebookId,
		// userAction };
		String[] params = { preferences.getString(Constant.FACEBOOK_ID, ""),
				MatchedUserFacebookId, userAction };

		new BackGroundTaskForInviteAction().execute(params);

	}

	private class BackGroundTaskForInviteAction extends
			AsyncTask<String, Void, Void> {

		private String inviteActionResponse;
		private List<NameValuePair> inviteactionparamlist;
		private InviteActionData mActionData;
		private Ultilities mUltilities = new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			try {
				inviteactionparamlist = mUltilities
						.getInviteActionParameter(params);

				HttpRequest httpRequest = new HttpRequest();
				inviteActionResponse=httpRequest.postData(Constant.inviteaction_url,
						inviteactionparamlist);
				
				/*inviteActionResponse = mUltilities.makeHttpRequest(
						Constant.inviteaction_url, Constant.methodeName,
						inviteactionparamlist);*/

				Gson gson = new Gson();
				mActionData = gson.fromJson(inviteActionResponse,
						InviteActionData.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(MatChedUserProfile.this);
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				mDialog.dismiss();
				
				try
				{
					if (mActionData.getErrNum() == 29
							&& mActionData.getErrFlag() == 0) {
						SessionManager mSessionManager = new SessionManager(
								MatChedUserProfile.this);
						mSessionManager.isInviteActionSucess(true);

						finish();
					} else if (mActionData.getErrNum() == 37
							&& mActionData.getErrFlag() == 1) {
						ErrorMessage("alrte", mActionData.getErrMsg());
					} else if (mActionData.getErrNum() == 55) {
						Intent matchIntent = new Intent(MatChedUserProfile.this,
								MatchFoundActivity.class);
						matchIntent
								.putExtra("SENDER_FB_ID", mActionData.getuFbId());
						matchIntent.putExtra("SENDER_USERNAME",
								mActionData.getuName());
						startActivity(matchIntent);
					} else {
						ErrorMessage("alrte",
								"sorry Server Error! Please try again after sometime!");
					}
					finish();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MatChedUserProfile.this);
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

	private void ErrorMessageMandetoryFiledMissing(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MatChedUserProfile.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

}
