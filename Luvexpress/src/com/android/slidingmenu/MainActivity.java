package com.android.slidingmenu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.appdupe.androidpushnotifications.Chat_List;
import com.appdupe.pair.model.KeyValuePair;
import com.appdupe.pair.pojo.LikeMatcheddataForListview;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.CircleTransform;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.ScalingUtilities;
import com.appdupe.pair.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairnofb.R;
import com.facebook.Session;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener {// , OnOpenListener {
	private static final String TAG = "MainActivity";
	// private ListView matcheslistview;
	ImageButton btMenu, buttonRightMenu;
	TextView tvTitle;
	private Typeface topbartextviewFont;
	private SharedPreferences pref;
	// private EditText etSerchFriend;
	double mLatitude = 0;
	double mLongitude = 0;
	double dLatitude = 0;
	double dLongitude = 0;
	private Dialog mdialog;
	private ArrayList<LikeMatcheddataForListview> arryList;
	// private MatchedDataAdapter adapter;
	private ImageView profileimage;// ,backgroundimage;
	private LinearLayout profilelayout;
	public SlidingMenu menu;
	private boolean flagforHome = true, flagForProfile, flagForsetting,
			flagforappset;
	private ImageOptions options;
	private ConnectionDetector cd;
	TextView settingtextview, appsettextview, helptextview, sharetextview,
			user_name;
	private AQuery aQuery;
	Typeface Montserrat_Regular, Montserrat_Bold;
	String fb_id,payment_type = "";
	private static final int REQUEST_CODE_PAYMENT = 1;

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
	//private static final String CONFIG_CLIENT_ID = "Aae_2MqNqL1E-tKqquTc55tXoDKRtgNjfu6CCvgTn52jWMby5s3GYO9V6j4SSiT-Pemw7pN92Yvthsoj";
	private static final String CONFIG_CLIENT_ID = "AR-eHkF7FZt-AeSn1Ph1b5ofKNoKcCev0ryJNy8EeAhldBu8sSI8xyHamIF1NKa5y_WNJu17vv5HFLpy";
	
	private static PayPalConfiguration config = new PayPalConfiguration()
	.environment(CONFIG_ENVIRONMENT)
	.clientId(CONFIG_CLIENT_ID)
	// The following are only used in PayPalFuturePaymentActivity.
	.merchantName("Luvexpress")
	.merchantPrivacyPolicyUri(Uri.parse(Constant.Privacy_URL))
	.merchantUserAgreementUri(Uri.parse(Constant.Term_URL));
	Dialog dialog_payment_type;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cd = new ConnectionDetector(this);
		aQuery = new AQuery(this);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		fb_id = pref.getString(Constant.FACEBOOK_ID, "");

		options = new ImageOptions();
		options.fileCache = true;
		options.memCache = true;
		
		setContentView(R.layout.slidmenuxamplemainactivity);

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getAssets());
		Montserrat_Bold = BasicFunctions.Montserrat_Bold(getAssets());
		
		dialog_payment_type = new Dialog(
				MainActivity.this,
				android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog_payment_type.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog_payment_type
				.setContentView(R.layout.payment_type_popup);
		dialog_payment_type.setCancelable(false);
		dialog_payment_type.setCanceledOnTouchOutside(false);

		/*
		 * if(!pref.getBoolean(Constant.PREF_ISFIRST, true)) { Editor editor =
		 * pref.edit(); editor.putBoolean(Constant.PREF_ISFIRST, true);
		 * editor.commit(); //startActivity(new Intent(this,
		 * QuestionsActivity.class)); }
		 */

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		topbartextviewFont = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		tvTitle.setTypeface(topbartextviewFont);
		tvTitle.setTextColor(Color.rgb(255, 255, 255));
		tvTitle.setTextSize(20);
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		Log.d(TAG, "onCreate  before add menu ");
		menu.setMenu(R.layout.leftmenu);
		// menu.setSecondaryMenu(R.layout.rightmenu);
		// menu.setSecondaryMenu(R.layout.rightmenu);
		Log.d(TAG, "onCreate  add menu ");
		// menu.setSlidingEnabled(true);
		Log.d(TAG, "onCreate  finish");
		// etSerchFriend = (EditText) menu
		// .findViewById(R.id.et_serch_right_side_menu);
		// btnSerch = (Button) menu.findViewById(R.id.btn_serch_right_side);
		View leftmenuview = menu.getMenu();
		// View rightmenuview = menu.getSecondaryMenu();
		initLayoutComponent(leftmenuview);
		// menu.setSecondaryOnOpenListner(this);
		// lvMenuItems = getResources().getStringArray(R.array.menu_items);
		// lvMenu.setAdapter(new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
		// lvMenuItems));

		// matcheslistview.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // logDebug("setOnItemClickListener  onItemClick arg2 "+arg2);
		//
		// LikeMatcheddataForListview matcheddataForListview =
		// (LikeMatcheddataForListview) parent
		// .getItemAtPosition(position);
		// String faceboolid = matcheddataForListview.getFacebookid();
		// //
		// logDebug(" background setOnItemClickListener  onItemClick friend facebook id faceboolid "+faceboolid);
		// //
		// logDebug(" background setOnItemClickListener  onItemClick user facebook id  faceboolid"+new
		// // SessionManager(MainActivity.this).getFacebookId());
		// Bundle mBundle = new Bundle();
		// mBundle.putString(Constant.FRIENDFACEBOOKID, faceboolid);
		// mBundle.putString(Constant.CHECK_FOR_PUSH_OR_NOT, "1");
		//
		// Intent mIntent = new Intent(MainActivity.this,
		// ChatActivity.class);
		// mIntent.putExtras(mBundle);
		// startActivity(mIntent);
		// menu.toggle();
		// }
		// });

		buttonRightMenu = (ImageButton) findViewById(R.id.button_right_menu);

		btMenu = (ImageButton) findViewById(R.id.button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		try {
			profilelayout.setOnClickListener(this);
			settingtextview.setOnClickListener(this);
			appsettextview.setOnClickListener(this);
			helptextview.setOnClickListener(this);
			sharetextview.setOnClickListener(this);
		} catch (Exception e) {
			AppLog.handleException("oncreate   Exception  ", e);
		}

		// Bundle extras = getIntent().getExtras();
		System.out.println("Get Intent done");
		try {
			FragmentManager fm = MainActivity.this.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			FindMatches fragment = new FindMatches();
			ft.add(R.id.activity_main_content_fragment, fragment);
			tvTitle.setText(getResources().getString(R.string.app_name));

			ft.commit();
			setProfilePick(profileimage);
			// setProfilePick(backgroundimage);
			// String img =
			// preferences.getString(Constant.PREF_PROFILE_IMAGE_ONE,
			// "");
			// if (!img.equals("")) {
			// aQuery.id(backgroundimage).image(img, options);
			// }

		} catch (Exception e) {
			AppLog.handleException("onCreate Exception ", e);

		}

		// Ultilities mUltilities = new Ultilities();
		// int imageHeightAndWidht[] = mUltilities
		// .getImageHeightAndWidthForAlubumListview(this);
		// arryList = new ArrayList<LikeMatcheddataForListview>();
		// adapter = new MatchedDataAdapter(this, arryList,
		// imageHeightAndWidht);
		// System.out.println("Matched Adapter=" + adapter);
		// matcheslistview.setAdapter(adapter);

		// final SessionManager sessionManager = new SessionManager(this);

		buttonRightMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (isProfileclicked) {
				// Intent mIntent = new Intent(MainActivity.this,
				// EditProfileNew.class);
				// startActivity(mIntent);
				// } else {
				Intent mIntent = new Intent(MainActivity.this, Chat_List.class);
				startActivity(mIntent);
				// toggleRightMenu(v);
				// }
			}
		});

		// initSerchData();
		
		/*if(dialog_payment_type!=null && !dialog_payment_type.isShowing())
		{
		new Thread(new Runnable() {
			@Override
			public void run() {
				getPaymentstatus();
			}
		}).start();
		}*/
	}

	// /*
	// * method responsible for intialise search function at right side friend
	// * list adapter of this activity
	// */
	// private void initSerchData() {
	// etSerchFriend.addTextChangedListener(new TextWatcher() {
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// adapter.getFilter().filter(s.toString().trim());
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// }
	// });
	// }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PAYMENT) {
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
				if(dialog_payment_type!=null && !dialog_payment_type.isShowing())
				{
					dialog_payment_type.show();
				}
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				if(dialog_payment_type!=null && !dialog_payment_type.isShowing())
				{
					dialog_payment_type.show();
				}
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		} else {
			Session.getActiveSession().onActivityResult(MainActivity.this,
					requestCode, resultCode, data);
		}
	}

	// @Override
	// public void onOpen() {
	// AppLog.Log(TAG, "onOpen");
	// findLikedMatched();
	// }

	@Override
	protected void onResume() {
		super.onResume();
		AppLog.Log(TAG, " MainActivity   onResume  called");
		// SessionManager sessionManager = new SessionManager(this);
		// if (sessionManager.isIsProfileImageChanged()) {
		// sessionManager.setIsProfileImageChanged(false);
		setProfilePick(profileimage);
		// setProfilePick(backgroundimage);
		// } else {
		//
		// }
	}

	private void getPaymentstatus() {
		int status = 0;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.showProcess(MainActivity.this, "Getting Payment Details...");
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

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			status = -1;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
					Toast.makeText(MainActivity.this, "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		if (status == 1) {
			// Status OK
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
//					Toast.makeText(MainActivity.this, "Subscribed User",
//							Toast.LENGTH_SHORT).show();
				}
			});
			
			/*
			 * Latlong_Data pref_data = new Latlong_Data();
			 * pref_data.setLat(preLat); pref_data.setLong(preLong);
			 * pref_data.setCity_name("My Preferred Location");
			 * startActivityForResult( new Intent(MainActivity.this,
			 * PickLocation.class).putExtra( "latlong_data",
			 * latlong_data).putExtra("pref_data", pref_data), 101);
			 */
		} else if (status == 0) {
			runOnUiThread(new Runnable() {
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
							} else {
								Toast.makeText(MainActivity.this,
										"Please select any payment type first",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog_payment_type.dismiss();
							finish();
						}
					});
					dialog_payment_type.show();
				}
			});
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
					Toast.makeText(MainActivity.this, "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
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

		Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}
	
	private void sendPaymentToServer(String amount, String id) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.showProcess(MainActivity.this, "Saving Payment Details...");
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
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "Payment Saved",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(MainActivity.this);
					Toast.makeText(MainActivity.this, "Some error Occured",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public void setMenuTouchFullScreenEnable(boolean isEnable) {
		if (isEnable) {
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	private void setProfilePick(final ImageView userProfilImage) {
		final Ultilities mUltilities = new Ultilities();
		final int width_height[] = mUltilities
				.getImageHeightAndWidthForDrawer(MainActivity.this);
		Picasso.with(this).load(pref.getString(Constant.PREF_PROFILE_IMAGE_ONE, "/"))
		.transform(new CircleTransform()).fit()
		.into(userProfilImage);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width_height[1], width_height[0]);
		userProfilImage.setLayoutParams(layoutParams);
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("imageMain",
						pref.getString(Constant.PREF_PROFILE_IMAGE_ONE, "/"));
				final Bitmap bitmapimage = Utility.getBitmapFromURL(pref
						.getString(Constant.PREF_PROFILE_IMAGE_ONE, "/"));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						userProfilImage.setLayoutParams(new LayoutParams(
								width_height[1], width_height[0]));
						Bitmap cropedBitmap = null;
						ScalingUtilities mScalingUtilities = new ScalingUtilities();
						Bitmap mBitmap = null;
						if (bitmapimage != null) {
							cropedBitmap = mScalingUtilities
									.createScaledBitmap(bitmapimage,
											width_height[1], width_height[0],
											ScalingLogic.CROP);
							bitmapimage.recycle();
							mBitmap = mUltilities.getCircleBitmap(cropedBitmap,
									1);
							cropedBitmap.recycle();
							userProfilImage.setImageBitmap(mBitmap);
							((LinearLayout) findViewById(R.id.back_circle))
									.setBackgroundResource(R.drawable.profile_back_circle);
						} else {
						}
					}
				});

			}
		}).start();*/
	}

	private void initLayoutComponent(View leftmenu) {
		// menu_right=(LinearLayout)rightmenu.findViewById(R.id.menu_right);
		// menu_left=(LinearLayout)leftmenu.findViewById(R.id.menu_left);
		// matcheslistview = (ListView) rightmenu
		// .findViewById(R.id.menu_right_ListView);
		// lvMenu = (ListView) findViewById(R.id.menu_listview);

		profileimage = (ImageView) leftmenu.findViewById(R.id.profileimage);
		// backgroundimage = (ImageView) leftmenu.findViewById(R.id.backimage);
		// aQuery.id(profileimage).image(
		// preferences.getString(Constant.PREF_PROFILE_IMAGE_ONE, ""),
		// options);

		profilelayout = (LinearLayout) leftmenu
				.findViewById(R.id.profilelayout);
		settingtextview = (TextView) leftmenu
				.findViewById(R.id.settingtextview);
		appsettextview = (TextView) leftmenu.findViewById(R.id.appsettextview);
		helptextview = (TextView) leftmenu.findViewById(R.id.helptextview);
		sharetextview = (TextView) leftmenu.findViewById(R.id.sharetextview);
		user_name = (TextView) leftmenu.findViewById(R.id.user_name);

		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (pref.getString(Constant.PREF_FIRST_NAME, "") != null) {
			user_name.setText(pref.getString(Constant.PREF_FIRST_NAME, "")
					+ " " + pref.getString(Constant.PREF_LAST_NAME, ""));
		} else {
			user_name.setText("");
		}

		settingtextview.setTypeface(Montserrat_Regular);
		appsettextview.setTypeface(Montserrat_Regular);
		helptextview.setTypeface(Montserrat_Regular);
		sharetextview.setTypeface(Montserrat_Regular);
		user_name.setTypeface(Montserrat_Regular);

		((TextView) leftmenu.findViewById(R.id.view_profile))
				.setTypeface(Montserrat_Regular);

	}

	// private void findLikedMatched() {
	//
	// AppLog.Log(TAG, "findLikedMatched");
	//
	// // try {
	// // String deviceid = Ultilities.getDeviceId(this);
	// // AppLog.Log(TAG, "fineLikedMatched   deviceid" + deviceid);
	// // SessionManager mSessionManager = new SessionManager(this);
	//
	// // String sessionToken = mSessionManager.getUserToken();
	// // AppLog.Log(TAG, "fineLikedMatched   sessionToken" + sessionToken);
	// // Ultilities mUltilitie = new Ultilities();
	// // String currentdeviceTime = mSessionManager.getLastUpdatedTime();
	// // String curenttime = mUltilitie.getCurrentGmtTime();
	// // mSessionManager.setLastUpdate(curenttime);
	// Log.e("FACEBOOK_ID", pref.getString(Constant.FACEBOOK_ID, ""));
	// String params[] = { pref.getString(Constant.FACEBOOK_ID, "") };
	// new BackgroundTaskForFindLikeMatched().execute(params);
	//
	// }
	//
	// private class BackgroundTaskForFindLikeMatched extends
	// AsyncTask<String, Void, Void> {
	// private Ultilities mUltilities = new Ultilities();
	// private List<NameValuePair> getuserparameter;
	// private String likedmatchedata;
	// private LikedMatcheData matcheData;
	// private ArrayList<Likes> likesList;
	// private LikeMatcheddataForListview matcheddataForListview;
	// DatabaseHandler mDatabaseHandler = new DatabaseHandler(
	// MainActivity.this);
	// private boolean isResponseSuccess = true;
	//
	// @Override
	// protected Void doInBackground(String... params) {
	// try {
	//
	// File appDirectory = mUltilities
	// .createAppDirectoy(getResources().getString(
	// R.string.appdirectory));
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched   doInBackground appDirectory "
	// + appDirectory);
	// File _picDir = new File(appDirectory, getResources().getString(
	// R.string.imagedirematchuserdirectory));
	//
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground ");
	//
	// getuserparameter = mUltilities.getUserLikedParameter(params);
	//
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground   getuserparameter "
	// + getuserparameter);
	//
	// likedmatchedata = mUltilities.makeHttpRequest(
	// Constant.getliked_url, Constant.methodeName,
	// getuserparameter);
	//
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground   likedmatchedata "
	// + likedmatchedata);
	//
	// Gson gson = new Gson();
	// matcheData = gson.fromJson(likedmatchedata,
	// LikedMatcheData.class);
	//
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground   matcheData "
	// + matcheData);
	//
	// // "errNum": "51",
	// // "errFlag": "0",
	// // "errMsg": "Matches found!",
	//
	// if (matcheData.getErrFlag() == 0) {
	// likesList = matcheData.getLikes();
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground   likesList "
	// + likesList);
	// if (arryList != null) {
	// arryList.clear();
	// }
	//
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched doInBackground   likesList sized "
	// + likesList.size());
	// for (int i = 0; i < likesList.size(); i++) {
	// matcheddataForListview = new LikeMatcheddataForListview();
	// String userName = likesList.get(i).getfName();
	// String facebookid = likesList.get(i).getFbId();
	// // Log.i(TAG, "Background facebookid......"+facebookid);
	// String picturl = likesList.get(i).getpPic();
	// int falg = likesList.get(i).getFlag();
	// String latd = likesList.get(i).getLadt();
	// matcheddataForListview.setFacebookid(facebookid);
	// matcheddataForListview.setUserName(userName);
	// matcheddataForListview.setImageUrl(picturl);
	// matcheddataForListview.setFlag("" + falg);
	// matcheddataForListview.setladt(latd);
	// // matcheddataForListview.setFilePath(filePath);
	// File imageFile = mUltilities.createFileInSideDirectory(
	// _picDir, userName + facebookid + ".jpg");
	// //
	// logDebug("BackGroundTaskForUserProfile doInBackground imageFile is profile "+imageFile.isFile());
	// Utility.addBitmapToSdCardFromURL(likesList.get(i)
	// .getpPic().replaceAll(" ", "%20"), imageFile);
	// matcheddataForListview.setFilePath(imageFile
	// .getAbsolutePath());
	// if (!pref.getString(Constant.FACEBOOK_ID, "")
	// .equals(facebookid)) {
	// arryList.add(matcheddataForListview);
	// }
	//
	// }
	// DatabaseHandler mDatabaseHandler = new DatabaseHandler(
	// MainActivity.this);
	// // SessionManager mSessionManager = new SessionManager(
	// // MainActivity.this);
	// String userFacebookid = pref.getString(
	// Constant.FACEBOOK_ID, "");
	//
	// //
	// boolean isdataiserted = mDatabaseHandler.insertMatchList(
	// arryList, userFacebookid);
	//
	// // what it is??
	// //
	// ////////////////////////////////////////////////////////////////////////////////
	//
	// ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
	// .getUserFindMatch();
	// AppLog.Log(TAG, "arryListtem  " + arryListtem);
	// if (arryListtem != null && arryListtem.size() > 0) {
	// AppLog.Log(TAG, "arryList  size " + arryListtem.size());
	// arryList.clear();
	//
	// arryList.addAll(arryListtem);
	// // adapter.notifyDataSetChanged();
	//
	// // for (int i = 0; i < arryList.size(); i++)
	// // {
	// // Bitmap bitmapimage =
	// //
	// mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(arryList.get(i).getFilePath());
	// // logDebug("setProfilePick bitmapimage"+bitmapimage);
	// // ScalingUtilities mScalingUtilities =new
	// // ScalingUtilities();
	// // Bitmap cropedBitmap=
	// // mScalingUtilities.createScaledBitmap(bitmapimage,
	// // 100, 100, ScalingLogic.CROP);
	// // bitmapimage.recycle();
	// // Bitmap mBitmap=
	// // mUltilities.getCircleBitmap(cropedBitmap, 1);
	// // arryList.get(i).setmBitmap(mBitmap);
	// // cropedBitmap.recycle();
	// // // logDebug("setProfilePick  mBitmap"+mBitmap);
	// // // userProfilImage.setImageBitmap(mBitmap);
	// //
	// // }
	// }
	//
	// }
	// // "errNum": "50",
	// // "errFlag": "1",
	// // "errMsg": "Sorry, no matches found!"
	// else if (matcheData.getErrFlag() == 1) {
	// ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
	// .getUserFindMatch();
	// AppLog.Log(TAG, "arryListtem  " + arryListtem);
	// if (arryListtem != null && arryListtem.size() > 0) {
	// AppLog.Log(TAG, "arryList  size " + arryListtem.size());
	// arryList.clear();
	//
	// arryList.addAll(arryListtem);
	// }
	//
	// } else {
	// // do nothing
	// }
	//
	// } catch (Exception e) {
	// AppLog.handleException(
	// "BackgroundTaskForFindLikeMatched doInBackground Exception ",
	// e);
	// // some thing wrong happend
	// isResponseSuccess = false;
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// AppLog.Log(TAG, "BackgroundTaskForFindLikeMatched onPostExecute  ");
	// try {
	// mdialog.dismiss();
	// } catch (Exception e) {
	// AppLog.Log(TAG,
	// "BackgroundTaskForFindLikeMatched   onPostExecute  Exception "
	// + e);
	// }
	// if (!isResponseSuccess) {
	// AlertDialogManager.errorMessage(MainActivity.this, "Alert",
	// "Request timeout");
	// }
	// adapter.notifyDataSetChanged();
	// }
	//
	// protected void onPreExecute() {
	// super.onPreExecute();
	// AppLog.Log(TAG, "BackgroundTaskForFindLikeMatched onPreExecute  ");
	// try {
	// mdialog = mUltilities.GetProcessDialog(MainActivity.this);
	// mdialog.setCancelable(false);
	// mdialog.show();
	// } catch (Exception e) {
	// AppLog.handleException(
	// "BackgroundTaskForFindLikeMatched   onPreExecute  Exception ",
	// e);
	// }
	//
	// }
	//
	// }

	private class MatchedDataAdapter extends
			ArrayAdapter<LikeMatcheddataForListview> {
		private AQuery aQuery;
		private Activity mActivity;
		private LayoutInflater mInflater;
		private SessionManager sessionManager;

		public MatchedDataAdapter(Activity context,
				List<LikeMatcheddataForListview> objects,
				int imageHeigthAndWidth[]) {
			super(context, R.layout.matchedlistviewitem, objects);
			mActivity = context;
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// this.imageHeigthAndWidth=imageHeigthAndWidth;
			sessionManager = new SessionManager(context);
			aQuery = new AQuery(context);
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public LikeMatcheddataForListview getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.matchedlistviewitem,
						null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.userimage);
				holder.textview = (TextView) convertView
						.findViewById(R.id.userName);
				holder.lastMasage = (TextView) convertView
						.findViewById(R.id.lastmessage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textview.setId(position);
			holder.imageview.setId(position);
			holder.lastMasage.setId(position);
			holder.textview.setText(getItem(position).getUserName());
			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());
			try {
				holder.lastMasage.setText(sessionManager
						.getLastMessage(getItem(position).getFacebookid()));
			} catch (Exception e) {
				AppLog.handleException(TAG + " getView  Exception ", e);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView textview;
			TextView lastMasage;

		}
	}

	public void toggleMenu(View v) {
		menu.toggle();
	}

	public void toggleRightMenu(View v) {
		menu.showSecondaryMenu();
	}

	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else if (menu.isSecondaryMenuShowing()) {
			menu.showSecondaryMenu();
		} else {
			if (!flagforHome) {
				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new FindMatches();
				tvTitle.setText(getResources().getString(R.string.app_name));
				flagforHome = true;
				flagForProfile = false;
				flagForsetting = false;
				flagforappset = false;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					// tvTitle.setText(selectedItem);
				}
				/*new Thread(new Runnable() {
					@Override
					public void run() {
						getPaymentstatus();
					}
				}).start();*/
			} else {
				super.onBackPressed();
			}
		}
	}

	public void onStop() {
		super.onStop();
		if (mdialog != null) {
			mdialog.dismiss();
			mdialog = null;
		}
	}

	protected void onDestroy() {
		if (mdialog != null && mdialog.isShowing()) {
			mdialog.dismiss();
		}
		super.onDestroy();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void onClick(View v) {
		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;
		// if (v.getId() == R.id.homelayout)
		// {
		// if (!cd.isConnectingToInternet())
		// {
		// Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if (flagforHome)
		// {
		// menu.toggle();
		// return;
		// }
		// else
		// {
		// fragment = new FindMatches();
		// buttonRightMenu.setBackgroundResource(R.drawable.selector_for_message_button);
		// tvTitle.setText(getResources().getString(R.string.app_name));
		// flagforHome = true;
		// flagForProfile = false;
		// flagForsetting = false;
		// flagforappset = false;
		// isProfileclicked = false;
		// if (fragment != null)
		// {
		// ft.replace(R.id.activity_main_content_fragment, fragment);
		// ft.commit();
		// // tvTitle.setText(selectedItem);
		// }
		// menu.toggle();
		// }
		// }
		// else
		if (v.getId() == R.id.profilelayout) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			if (flagForProfile) {
				menu.toggle();
				return;
			} else {
				startActivity(new Intent(MainActivity.this, UserProfile.class));
				// fragment = new UserProfile();
				// tvTitle.setText(getResources().getString(R.string.myprofile));
				// flagforHome = false;
				// flagForProfile = true;
				// flagForsetting = false;
				// flagforappset = false;
				// if (fragment != null) {
				// ft.replace(R.id.activity_main_content_fragment, fragment);
				// ft.commit();
				// }
				menu.toggle();
			}
		} else if (v.getId() == R.id.settingtextview) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			if (flagForsetting) {
				menu.toggle();
				return;
			} else {
				// buttonRightMenu
				// .setBackgroundResource(R.drawable.selector_for_message_button);
				tvTitle.setText(getResources().getString(R.string.settings));
				fragment = new SettingActivity();
				flagforHome = false;
				flagForProfile = false;
				flagForsetting = true;
				flagforappset = false;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
				}
				menu.toggle();
			}
		} else if (v.getId() == R.id.appsettextview) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			if (flagforappset) {
				menu.toggle();
				return;
			} else {
				// buttonRightMenu
				// .setBackgroundResource(R.drawable.selector_for_message_button);
				// tvTitle.setText(getResources().getString(R.string.settings));
				fragment = new AppSetting();
				flagforHome = false;
				flagForProfile = false;
				flagForsetting = false;

				flagforappset = true;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					flagforappset = false;
				}
				menu.toggle();
			}
		}

		// else if (v.getId() == R.id.messages)
		// {
		// if (!cd.isConnectingToInternet())
		// {
		// Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// toggleRightMenu(v);
		//
		// }
		// else if (v.getId() == R.id.questionLayout)
		// {
		// if (!cd.isConnectingToInternet())
		// {
		// Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// menu.toggle();
		// Intent questionIntent = new Intent(this, QuestionsActivity.class);
		// startActivity(questionIntent);
		//
		// }
		// else if (v.getId() == R.id.invitelayout)
		// {
		// if (!cd.isConnectingToInternet())
		// {
		// Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// Intent sendIntent = new Intent();
		// sendIntent.setAction(Intent.ACTION_SEND);
		// sendIntent.putExtra(Intent.EXTRA_TEXT,"I am using Pair App ! Why don't you try it out...\nInstall Pair now !\nhttps://play.google.com/store/apps/details?id=com.appdupe.flamernofb");
		// sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT," Pair App !");
		// sendIntent.setType("message/rfc822"); //
		// sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {
		// "" });
		// startActivity(Intent.createChooser(sendIntent,
		// "Send mail using..."));
		// }

		else if (v.getId() == R.id.sharetextview) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"Check out Luvexpress and Start sharing your matches!");
			sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					" Share Luvexpress !");
			sendIntent.setType("message/plain");
			sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "" });
			startActivity(Intent
					.createChooser(sendIntent, "Send mail using..."));

			menu.toggle();
		}

		else if (v.getId() == R.id.helptextview) {
			if (!cd.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("mailto:")); // only email apps should
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "osvinandroid@gmail.com" });
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					" Feedback about Luvexpress !");
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			}
			// Intent sendIntent = new Intent();
			// sendIntent.setAction(Intent.ACTION_SENDTO);
			// sendIntent.putExtra(Intent.EXTRA_TEXT, "");
			// sendIntent.putExtra(Intent.EXTRA_EMAIL,
			// new String[] { "osvinandroid@gmail.com" });
			// sendIntent.setData(Uri.parse("mailto:osvinandroid@gmail.com"));
			// sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// " Feedback !");
			// sendIntent.setType("message/rfc822"); //
			// sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
			// new String[] { "" });
			// startActivity(Intent
			// .createChooser(sendIntent, "Send mail using..."));

			menu.toggle();
		}
	}
}
