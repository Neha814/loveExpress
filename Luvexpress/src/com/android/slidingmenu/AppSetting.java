package com.android.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appdupe.pair.LoginNew;
import com.appdupe.pair.pojo.DeleteUserAccountData;
import com.appdupe.pair.pojo.LikeMatcheddataForListview;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pairchat.db.DatabaseHandler;
import com.appdupe.pairnofb.PrivacyTermWebActivity;
import com.appdupe.pairnofb.R;
import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class AppSetting extends Fragment implements OnClickListener {
	// private static boolean mDebugLog = true;
	// private static final String mDebugTag = "SettingActivity";
	private static final String TAG = "AppSettingActivity";
	private SharedPreferences preferences;
	private TextView distanceform, MiTextview, kmTextview;
	private Button privacyButton, logoutButton, deleteaccount, termsButton;
	private ImageView chat, setting, sharepair;
	private boolean isDistanceUinitKm, ismileselected, iskmselected;
	private String distanceunit;
	private RelativeLayout MiTextviewparentlayout, kmTextviewparentLayout;
	SessionManager mSessionManager;
	private ListView matcheslistview;
	public SlidingMenu menu;
	private ConnectionDetector cd;
	private ArrayList<LikeMatcheddataForListview> arryList;
	ToggleButton toggleButton_matches, toggleButton_messages,
			toggleButton_app_vib;
	Typeface Montserrat_Regular;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.appsetting_layout, null);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		System.out.println("PREFRENCES=" + preferences);
		cd = new ConnectionDetector(getActivity());
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
					.show();
			return view;
		}

		initDataList(view);
		Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getActivity(), null,
						statusCallback, savedInstanceState);

			}
			if (session == null) {
				session = new Session(getActivity());

			}
			Session.setActiveSession(session);

			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

			}
		}

		logoutButton.setOnClickListener(this);
		deleteaccount.setOnClickListener(this);
		privacyButton.setOnClickListener(this);
		termsButton.setOnClickListener(this);

		MiTextviewparentlayout.setOnClickListener(this);
		kmTextviewparentLayout.setOnClickListener(this);
		chat.setOnClickListener(this);
		setting.setOnClickListener(this);
		sharepair.setOnClickListener(this);

		mSessionManager = new SessionManager(getActivity());

		toggleButton_matches
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						Editor editor = preferences.edit();
						if (toggleButton_matches.isChecked())
							editor.putInt(Constant.KEY_SHOW_MATCH, 1);
						else
							editor.putInt(Constant.KEY_SHOW_MATCH, 0);

						editor.commit();
					}
				});

		toggleButton_messages
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						Editor editor = preferences.edit();
						if (toggleButton_messages.isChecked())
							editor.putInt(Constant.KEY_SHOW_MESSAGE, 1);
						else
							editor.putInt(Constant.KEY_SHOW_MESSAGE, 0);

						editor.commit();
					}
				});

		toggleButton_app_vib
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						Editor editor = preferences.edit();
						if (toggleButton_app_vib.isChecked())
							editor.putInt(Constant.KEY_APP_VIB, 1);
						else
							editor.putInt(Constant.KEY_APP_VIB, 0);

						editor.commit();
					}
				});

		return view;
	}

	private void initDataList(View view) {
		// TODO Auto-generated method stub

		// Typeface HelveticaLTStd_Light =
		// Typeface.createFromAsset(getActivity()
		// .getAssets(), "fonts/HelveticaLTStd-Light.otf");
		// Typeface HelveticaInseratLTStd_Roman = Typeface.createFromAsset(
		// getActivity().getAssets(),
		// "fonts/HelveticaInseratLTStd-Roman.otf");

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getActivity()
				.getAssets());

		privacyButton = (Button) view.findViewById(R.id.policyButton);
		logoutButton = (Button) view.findViewById(R.id.logoutButton);
		deleteaccount = (Button) view.findViewById(R.id.deleteaccount);
		termsButton = (Button) view.findViewById(R.id.termsButton);

		toggleButton_matches = (ToggleButton) view
				.findViewById(R.id.toggleButton_matches);
		toggleButton_messages = (ToggleButton) view
				.findViewById(R.id.toggleButton_messages);
		toggleButton_app_vib = (ToggleButton) view
				.findViewById(R.id.toggleButton_app_vib);

		if (preferences.getInt(Constant.KEY_SHOW_MATCH, 1) == 1) {
			toggleButton_matches.setChecked(true);
		} else {
			toggleButton_matches.setChecked(false);
		}
		if (preferences.getInt(Constant.KEY_APP_VIB, 1) == 1) {
			toggleButton_app_vib.setChecked(true);
		} else {
			toggleButton_app_vib.setChecked(false);
		}
		if (preferences.getInt(Constant.KEY_SHOW_MESSAGE, 1) == 1) {
			toggleButton_messages.setChecked(true);
		} else {
			toggleButton_messages.setChecked(false);
		}

		chat = (ImageView) view.findViewById(R.id.chat);
		setting = (ImageView) view.findViewById(R.id.setting);
		sharepair = (ImageView) view.findViewById(R.id.sharepair);

		distanceform = (TextView) view.findViewById(R.id.distancevalue);
		MiTextview = (TextView) view.findViewById(R.id.MiTextview);
		kmTextview = (TextView) view.findViewById(R.id.kmTextview);
		MiTextviewparentlayout = (RelativeLayout) view
				.findViewById(R.id.MiTextviewparentlayout);
		kmTextviewparentLayout = (RelativeLayout) view
				.findViewById(R.id.kmTextviewparentLayout);

		((TextView) view.findViewById(R.id.newmsgtextview))
				.setTypeface(Montserrat_Regular);
		((TextView) view.findViewById(R.id.msgtextview))
				.setTypeface(Montserrat_Regular);
		((TextView) view.findViewById(R.id.vibtextview))
				.setTypeface(Montserrat_Regular);
		((TextView) view.findViewById(R.id.showmetextivew))
				.setTypeface(Montserrat_Regular);
		privacyButton.setTypeface(Montserrat_Regular);
		logoutButton.setTypeface(Montserrat_Regular);
		termsButton.setTypeface(Montserrat_Regular);
		deleteaccount.setTypeface(Montserrat_Regular);
		distanceform.setTypeface(Montserrat_Regular);
		MiTextview.setTypeface(Montserrat_Regular);
		kmTextview.setTypeface(Montserrat_Regular);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.MiTextviewparentlayout) {
			if (isDistanceUinitKm) {
				isDistanceUinitKm = false;
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_off);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				distanceunit = "Mi";
				distanceform.setText(distanceunit);
				MiTextview.setTextColor(Color.rgb(255, 255, 255));
				kmTextview.setTextColor(Color.rgb(153, 153, 153));
			} else {

			}
		}
		if (v.getId() == R.id.kmTextviewparentLayout) {
			if (isDistanceUinitKm) {

			} else {
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_on);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				distanceunit = "Km";
				distanceform.setText(distanceunit);
				kmTextview.setTextColor(Color.rgb(255, 255, 255));
				MiTextview.setTextColor(Color.rgb(153, 153, 153));
				isDistanceUinitKm = true;
			}
		}
		if (v.getId() == R.id.logoutButton) {
			logoutCurrentUser();
		}
		if (v.getId() == R.id.chat) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			((MainActivity) getActivity()).toggleRightMenu(v);
		}
		if (v.getId() == R.id.setting) {

			Fragment fragment = new SettingActivity();
			FragmentManager fm = getActivity().getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Bundle bundle = new Bundle();

			if (fragment != null) {
				bundle.putString("distform", distanceunit);
				fragment.setArguments(bundle);
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();
			}
		}

		if (v.getId() == R.id.policyButton) {
			Intent mIntent = new Intent(getActivity(),
					PrivacyTermWebActivity.class);
			mIntent.putExtra("URL", Constant.Privacy_URL);
			startActivity(mIntent);
			// menu.toggle();
		}
		if (v.getId() == R.id.termsButton) {

			// Intent mIntent = new Intent(getActivity(), TermsActivity.class);
			Intent mIntent = new Intent(getActivity(),
					PrivacyTermWebActivity.class);
			mIntent.putExtra("URL", Constant.Term_URL);
			startActivity(mIntent);
			// menu.toggle();
		}
		if (v.getId() == R.id.sharepair) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"Check out PikShare and Start sharing your piks!");
			sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					" Share Pair !");
			sendIntent.setType("message/rfc822"); //
			sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "" });
			startActivity(Intent
					.createChooser(sendIntent, "Send mail using..."));
		}
		if (v.getId() == R.id.deleteaccount) {
			delettCurrentUser();
		}

	}

	// private void confirm_dialog_logout(Context ctx, String str) {
	// final Dialog dialog2 = new
	// Dialog(ctx,android.R.style.Theme_Holo_Dialog_NoActionBar);
	// dialog2.setContentView(R.layout.logout_popup);
	// dialog2.setCancelable(true);
	// dialog2.setCanceledOnTouchOutside(true);
	// TextView message = (TextView) dialog2.findViewById(R.id.message);
	// Button logout = (Button) dialog2.findViewById(R.id.logout);
	// Button Cancel = (Button) dialog2.findViewById(R.id.cancel);
	// message.setTypeface(Montserrat_Regular);
	// logout.setTypeface(Montserrat_Regular);
	// Cancel.setTypeface(Montserrat_Regular);
	// if (str != null) {
	// message.setText(str);
	// }
	// logout.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialog2.dismiss();
	// Editor editor = preferences.edit();
	// editor.clear();
	// editor.commit();
	// DatabaseHandler handler = new DatabaseHandler(getActivity());
	// handler.clearAllData();
	// getActivity().finish();
	// startActivity(new Intent(getActivity(), LoginNew.class));
	// }
	// });
	// Cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialog2.dismiss();
	// }
	// });
	// dialog2.show();
	// }

	private void logoutCurrentUser() {
		final Dialog dialog2 = new Dialog(getActivity(),
				android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog2.setContentView(R.layout.logout_popup);
		dialog2.setCancelable(true);
		dialog2.setCanceledOnTouchOutside(true);
		TextView message = (TextView) dialog2.findViewById(R.id.message);
		Button logout = (Button) dialog2.findViewById(R.id.logout);
		Button Cancel = (Button) dialog2.findViewById(R.id.cancel);
		message.setTypeface(Montserrat_Regular);
		logout.setTypeface(Montserrat_Regular);
		Cancel.setTypeface(Montserrat_Regular);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();

				Session session = Session.getActiveSession();
				AppLog.Log(TAG, "BackGroundTaskForLogout  session " + session);
				if (!session.isClosed()) {
					AppLog.Log(TAG,
							"BackGroundTaskForLogout  session not close need to cleaar  "
									+ session.getState());
					session.closeAndClearTokenInformation();
				} else {
					// nothing session already close no need to clear
				}

				Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				DatabaseHandler handler = new DatabaseHandler(getActivity());
				handler.clearAllData();
				getActivity().finish();
				startActivity(new Intent(getActivity(), LoginNew.class));
			}
		});
		Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();
			}
		});
		dialog2.show();

		// SessionManager mSessionManager = new SessionManager(getActivity());
		// String sessionToke = mSessionManager.getUserToken();
		// String deviceid = Ultilities.getDeviceId(getActivity());
		// String[] params = { sessionToke, deviceid };
		// new BackGroundTaskForLogout().execute(params);
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// builder.setTitle("Logout");
		// builder.setMessage("Are you sure you want to logout from this application?");
		// builder.setCancelable(false);
		// builder.setPositiveButton("Yes", new
		// DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.cancel();
		// Editor editor = preferences.edit();
		// editor.clear();
		// editor.commit();
		// DatabaseHandler handler = new DatabaseHandler(getActivity());
		// handler.clearAllData();
		// getActivity().finish();
		// startActivity(new Intent(getActivity(), LoginNew.class));
		// }
		// });
		// builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.cancel();
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();

	}

	private void delettCurrentUser() {
		// SessionManager mSessionManager = new SessionManager(getActivity());
		// String sessionToke = mSessionManager.getUserToken();
		// String deviceid = Ultilities.getDeviceId(getActivity());

		final Dialog dialog2 = new Dialog(getActivity(),
				android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog2.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog2.setContentView(R.layout.delete_account_popup);
		dialog2.setCancelable(true);
		dialog2.setCanceledOnTouchOutside(true);
		TextView message = (TextView) dialog2.findViewById(R.id.message);
		Button delete = (Button) dialog2.findViewById(R.id.delete);
		Button Cancel = (Button) dialog2.findViewById(R.id.cancel);
		message.setTypeface(Montserrat_Regular);
		delete.setTypeface(Montserrat_Regular);
		Cancel.setTypeface(Montserrat_Regular);
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();
				String[] params = { preferences.getString(Constant.FACEBOOK_ID,
						"") };
				new BackGroundTaskForDeleteAccount().execute(params);
			}
		});
		Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();
			}
		});
		dialog2.show();
	}

	private class BackGroundTaskForDeleteAccount extends
			AsyncTask<String, Void, Void> {
		private String response;
		private List<NameValuePair> deletuserAccountParameterList;
		private DeleteUserAccountData deleteUserAccountData;
		private Ultilities mUltilities = new Ultilities();
		private ProgressDialog mDialog;

		@Override
		protected Void doInBackground(String... params) {
			try
			{
				deletuserAccountParameterList = mUltilities
						.getDeleteUserAccountParameter(params);
				
				HttpRequest httpRequest = new HttpRequest();
				response=httpRequest.postData(Constant.deleteUserAccount_url,
						deletuserAccountParameterList);
				
				/*response = mUltilities.makeHttpRequest(
						Constant.deleteUserAccount_url, Constant.methodeName,
						deletuserAccountParameterList);*/
				Log.e("delete account response", response);
				Gson gson = new Gson();
				deleteUserAccountData = gson.fromJson(response,
						DeleteUserAccountData.class);
				if (deleteUserAccountData.getErrFlag() == 0
						&& deleteUserAccountData.getErrNum() == 61) {
					Session session = Session.getActiveSession();
					AppLog.Log(TAG, "BackGroundTaskForLogout  session " + session);
					if (!session.isClosed()) {
						AppLog.Log(TAG,
								"BackGroundTaskForLogout  session not close need to cleaar  "
										+ session.getState());
						session.closeAndClearTokenInformation();
					} else {
						// nothing session already close no need to clear
					}
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 31) {
					Session session = Session.getActiveSession();
					AppLog.Log(TAG, "BackGroundTaskForLogout  session " + session);
					if (!session.isClosed()) {
						AppLog.Log(TAG,
								"BackGroundTaskForLogout  session not close need to cleaar  "
										+ session.getState());
						session.closeAndClearTokenInformation();

					} else {
						// nothing session already close no need to clear
					}
				}
			}
			catch (Exception e) {
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
					Intent intent = new Intent(getActivity(),
							LoginNew.class);
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
				} else if (deleteUserAccountData.getErrFlag() == 0
						&& deleteUserAccountData.getErrNum() == 64) {
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

					Editor editor = preferences.edit();
					editor.clear();
					editor.commit();
					DatabaseHandler handler = new DatabaseHandler(getActivity());
					handler.clearAllData();
					getActivity().finish();
					startActivity(new Intent(getActivity(), LoginNew.class));
				}
				Toast.makeText(getActivity(),
						deleteUserAccountData.getErrMsg(), Toast.LENGTH_SHORT)
						.show();
			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackGroundTaskForLogout onPostExecute Exception " + e);

			}
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
		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

}