package com.android.slidingmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.androidpushnotifications.ChatActivity;
import com.appdupe.pair.pojo.LikeMatcheddataForListview;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.CircleTransform;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.ScalingUtilities;
import com.appdupe.pair.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairchat.db.DatabaseHandler;
import com.appdupe.pairnofb.R;
import com.squareup.picasso.Picasso;

public class MatchFoundActivity extends Activity implements OnClickListener {

	private static String TAG = "MatchFoundActivity";
	private TextView bothUserTextview;
	private ImageView userImageview, friendImageview;
	private Button sendMessageButton, keepSwiping;
	private String strSenderFbId;
	private SharedPreferences preferences;
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.match_found_screen);
		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
			return;
		}
		
		((ImageView) findViewById(R.id.back_image))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		initComponent();

		Bundle bucket = getIntent().getExtras();
		if (bucket != null) {
			strSenderFbId = bucket.getString("SENDER_FB_ID");
			String strSenderuserName = bucket.getString("SENDER_USERNAME");
			bothUserTextview.setText("You and " + strSenderuserName
					+ " are a match.");
		}
		// setUserImage();
		setProfilePick(userImageview);
		setFriendImage();

		sendMessageButton.setOnClickListener(this);
		keepSwiping.setOnClickListener(this);
	}

	private void setFriendImage() {
		DatabaseHandler mDatabaseHandler = new DatabaseHandler(this);
		Ultilities mUltilities = new Ultilities();
		SessionManager manager = new SessionManager(this);
		String mfacebookid = manager.getFacebookId();
		LikeMatcheddataForListview matcheddataForListview = mDatabaseHandler
				.getSenderDetail(strSenderFbId);
		String imagePath = matcheddataForListview.getFilePath();
		
		Bitmap bitmapimage = mUltilities.showImage(imagePath);
		ScalingUtilities mScalingUtilities = new ScalingUtilities();
		Bitmap cropedBitmap = mScalingUtilities.createScaledBitmap(bitmapimage,
				200, 200, ScalingLogic.CROP);
		bitmapimage.recycle();
		Bitmap friendImage = mUltilities.getCircleBitmap(cropedBitmap, 1);
		friendImageview.setImageBitmap(friendImage);
		((RelativeLayout) findViewById(R.id.friend_imageview_layout))
		.setBackgroundResource(R.drawable.profile_back_circle);
		cropedBitmap.recycle();
		
//		Log.e("imagePath", imagePath);
//		Picasso.with(this).load(imagePath)
//		.transform(new CircleTransform()).fit()
//		.into(friendImageview);
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
//		friendImageview.setLayoutParams(layoutParams);
//		
//		((RelativeLayout) findViewById(R.id.friend_imageview_layout))
//		.setBackgroundResource(R.drawable.profile_back_circle);
	}

	private void setProfilePick(final ImageView userProfilImage) {
		final Ultilities mUltilities = new Ultilities();
		
		Picasso.with(this).load(preferences.getString(
				Constant.PREF_PROFILE_IMAGE_ONE, ""))
		.transform(new CircleTransform()).fit()
		.into(userProfilImage);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
		userProfilImage.setLayoutParams(layoutParams);
		
//		((RelativeLayout) findViewById(R.id.user_image_layout))
//		.setBackgroundResource(R.drawable.profile_back_circle);
		
		/*new Thread(new Runnable() {

			@Override
			public void run() {
				final Bitmap bitmapimage = Utility.getBitmapFromURL(preferences
						.getString(Constant.PREF_PROFILE_IMAGE_ONE, ""));
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						AppLog.Log(
								TAG,
								"Profile Image Url:"
										+ preferences
												.getString(
														Constant.PREF_PROFILE_IMAGE_ONE,
														""));
						Bitmap cropedBitmap = null;
						ScalingUtilities mScalingUtilities = new ScalingUtilities();
						Bitmap mBitmap = null;
						if (bitmapimage != null) {
							cropedBitmap = mScalingUtilities
									.createScaledBitmap(bitmapimage, 200, 200,
											ScalingLogic.CROP);
							bitmapimage.recycle();
							mBitmap = mUltilities.getCircleBitmap(cropedBitmap,
									1);
							cropedBitmap.recycle();
							userProfilImage.setImageBitmap(mBitmap);
							((RelativeLayout) findViewById(R.id.user_image_layout))
							.setBackgroundResource(R.drawable.profile_back_circle);
							// aQuery.id(userProfilImage).image(mBitmap);
						} else {
						}

					}
				});

			}
		}).start();*/
		// try {
		// // final Ultilities mUltilities = new Ultilities();
		//
		// // appDirectory =
		// //
		// mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
		// // _picDir = new File(appDirectory,
		// // getResources().getString(R.string.imagedirectory));
		// // myimgFile= new File(_picDir,
		// // getResources().getString(R.string.imagefilename)+"0.jpg");
		// DatabaseHandler mdaDatabaseHandler = new DatabaseHandler(this);
		// String imageOrderArray[] = { "1" };
		// ArrayList<ImageDetail> imagelist = mdaDatabaseHandler
		// .getImageDetailByImageOrder(imageOrderArray);
		// if (imagelist != null && imagelist.size() > 0) {
		// Bitmap bitmapimage = mUltilities.showImage/*
		// * setImageToImageViewBitmapFactory
		// * .decodeFiledecodeFile
		// */(imagelist.get(0)
		// .getSdcardpath());
		// Bitmap cropedBitmap = null;
		// ScalingUtilities mScalingUtilities = new ScalingUtilities();
		// Bitmap mBitmap = null;
		// if (bitmapimage != null) {
		// cropedBitmap = mScalingUtilities.createScaledBitmap(
		// bitmapimage, 200, 200, ScalingLogic.CROP);
		// bitmapimage.recycle();
		// mBitmap = mUltilities.getCircleBitmap(cropedBitmap, 1);
		// cropedBitmap.recycle();
		// userProfilImage.setImageBitmap(mBitmap);
		// } else {
		//
		// }
		//
		// } else {
		//
		// }
		//
		// } catch (Exception e) {
		// ImageView[] params = { userProfilImage };
		// // new
		// //
		// BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory().execute(params);
		// }

	}

	private void initComponent() {
		bothUserTextview = (TextView) findViewById(R.id.both_user_textview);
		userImageview = (ImageView) findViewById(R.id.user_imageview);
		friendImageview = (ImageView) findViewById(R.id.friend_imageview);
		sendMessageButton = (Button) findViewById(R.id.send_message_button);
		keepSwiping = (Button) findViewById(R.id.keep_swiping);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.send_message_button) {
			Bundle mBundle = new Bundle();
			mBundle.putString(Constant.FRIENDFACEBOOKID, strSenderFbId);
			mBundle.putString(Constant.CHECK_FOR_PUSH_OR_NOT, "1");

			Intent mIntent = new Intent(MatchFoundActivity.this,
					ChatActivity.class);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			finish();
		}
		if (v.getId() == R.id.keep_swiping) {
			finish();
		}
	}
	// private void setUserImage() {
	// File appDirectory;
	//
	// File _picDir;
	// File myimgFile;
	// try {
	// Ultilities mUltilities=new Ultilities();
	// appDirectory =
	// mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
	// _picDir = new File(appDirectory,
	// getResources().getString(R.string.imagedirectory));
	// myimgFile= new File(_picDir,
	// getResources().getString(R.string.imagefilename)+"0.jpg");
	// Bitmap bitmapimage =
	// mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(myimgFile.getAbsolutePath());
	// ScalingUtilities mScalingUtilities =new ScalingUtilities();
	// Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,
	// 200, 200, ScalingLogic.CROP);
	// bitmapimage.recycle();
	// Bitmap userImage= mUltilities.getCircleBitmap(cropedBitmap, 1);
	// userImageview.setImageBitmap(userImage);
	//
	// // logDebug("onCreate   userImage "+userImage);
	// cropedBitmap.recycle();
	//
	//
	//
	// } catch (Exception e)
	// {
	// //logError("onCreate  Exception "+e);
	//
	// }
	// }
}
