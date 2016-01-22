package com.android.slidingmenu;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.appdupe.pair.model.KeyValuePair;
import com.appdupe.pair.model.ProfileImageModel;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.CircleTransform;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.JsonParser;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairnofb.R;
import com.squareup.picasso.Picasso;

public class EditProfileNew extends Activity implements OnClickListener {
	private ImageView firstImage, secondImage, thirdImage, fourthImage,
			fifthImage, sixImage, addImage1, addImage2, addImage3, addImage4,
			addImage5, deleteImage1, deleteImage2, deleteImage3, deleteImage4,
			deleteImage5, update_profile_pic;
	private LinearLayout deleteLayout;
	private int selectedImage;
	private String firstImageUrl, secondImageUrl, thirdImageUrl,
			fourthImageUrl, fifthImageUrl, sixthImageUrl, imagePath;
	private TextView editprofileTextivew;
	private EditText edtStatus;
	private SharedPreferences preferences;
	private Editor editor;
	private AQuery aQuery;
	private Button btnEdit, btnDelete, btnSetProfile, btnSubmit;
	private File imageFile;
	private Uri imageUri;

	private static final int CAMERA_REQUEST_CODE = 100;
	private static final int RESULT_LOAD_IMAGE = 101;
	private static final String TAG = "EditProfileNew";
	private int mobile_width = 480;
	private Bitmap photoBitmap;
	int errorFlag = 1, gender = 0;
	private ConnectionDetector detector;
	private ImageOptions options;
	RadioGroup choose_group;
	RadioButton male, female;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile_new);
		initData();
	}

	private void initData() {
		aQuery = new AQuery(this);
		detector = new ConnectionDetector(this);
		options = new ImageOptions();
		options.fileCache = true;
		options.memCache = true;
		options.animation = AQuery.FADE_IN;
		firstImage = (ImageView) findViewById(R.id.userprofilpicture);
		secondImage = (ImageView) findViewById(R.id.firtstImage);
		thirdImage = (ImageView) findViewById(R.id.secondImage);
		fourthImage = (ImageView) findViewById(R.id.thirdimageview);
		fifthImage = (ImageView) findViewById(R.id.fourtthimageview);
		sixImage = (ImageView) findViewById(R.id.fifthImageview);
		update_profile_pic = (ImageView) findViewById(R.id.update_profile_pic);

		addImage1 = (ImageView) findViewById(R.id.addImage1);
		addImage2 = (ImageView) findViewById(R.id.addImage2);
		addImage3 = (ImageView) findViewById(R.id.addImage3);
		addImage4 = (ImageView) findViewById(R.id.addImage4);
		addImage5 = (ImageView) findViewById(R.id.addImage5);

		deleteImage1 = (ImageView) findViewById(R.id.deleteImage1);
		deleteImage2 = (ImageView) findViewById(R.id.deleteImage2);
		deleteImage3 = (ImageView) findViewById(R.id.deleteImage3);
		deleteImage4 = (ImageView) findViewById(R.id.deleteImage4);
		deleteImage5 = (ImageView) findViewById(R.id.deleteImage5);

		choose_group = (RadioGroup) findViewById(R.id.choose_group);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);

		choose_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.male:
					male.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.male, 0, R.drawable.tick, 0);
					female.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.female, 0, 0, 0);
					break;
				case R.id.female:
					male.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.male, 0, 0, 0);
					female.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.female, 0, R.drawable.tick, 0);
					break;
				default:
					break;
				}
			}
		});

		// editprofileTextivew = (TextView)
		// findViewById(R.id.editprofileTextivew);
		edtStatus = (EditText) findViewById(R.id.edtStatusS);
		deleteLayout = (LinearLayout) findViewById(R.id.deleteandeditbuttonlayout);
		btnEdit = (Button) findViewById(R.id.editbuton);
		btnDelete = (Button) findViewById(R.id.deletebutton);
		btnSetProfile = (Button) findViewById(R.id.setProfileButton);
		btnSubmit = (Button) findViewById(R.id.btnEditProfileSubmit);
		btnSubmit.setOnClickListener(this);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnSetProfile.setOnClickListener(this);
		firstImage.setOnClickListener(this);
		secondImage.setOnClickListener(this);
		thirdImage.setOnClickListener(this);
		fourthImage.setOnClickListener(this);
		fifthImage.setOnClickListener(this);
		sixImage.setOnClickListener(this);
		deleteImage1.setOnClickListener(this);
		deleteImage2.setOnClickListener(this);
		deleteImage3.setOnClickListener(this);
		deleteImage4.setOnClickListener(this);
		deleteImage5.setOnClickListener(this);
		addImage1.setOnClickListener(this);
		addImage2.setOnClickListener(this);
		addImage3.setOnClickListener(this);
		addImage4.setOnClickListener(this);
		addImage5.setOnClickListener(this);
		update_profile_pic.setOnClickListener(this);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		gender = preferences.getInt(Constant.KEY_SEX, 0);
		if (gender == 1) {
			male.setChecked(true);
		} else if (gender == 2) {
			female.setChecked(true);
		}

		/*
		 * editprofileTextivew.setTypeface(topbartextviewFont);
		 * editprofileTextivew.setTextColor(Color.rgb(255, 255, 255));
		 * editprofileTextivew.setTextSize(20);
		 */
		edtStatus.setText(preferences.getString(Constant.PREF_USER_STATUS, ""));
		((TextView) findViewById(R.id.txtStatus)).setText("About "
				+ preferences.getString(Constant.PREF_FIRST_NAME, ""));
		if (detector.isConnectingToInternet()) {
			getProfileImages();
		} else {
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

	}

	private void getDataFromPref() {
		firstImageUrl = preferences.getString(Constant.PREF_PROFILE_IMAGE_ONE,
				"");
		secondImageUrl = preferences.getString(Constant.PREF_PROFILE_IMAGE_TWO,
				"");
		thirdImageUrl = preferences.getString(
				Constant.PREF_PROFILE_IMAGE_THREE, "");
		fourthImageUrl = preferences.getString(
				Constant.PREF_PROFILE_IMAGE_FOUR, "");
		fifthImageUrl = preferences.getString(Constant.PREF_PROFILE_IMAGE_FIVE,
				"");
		sixthImageUrl = preferences.getString(Constant.PREF_PROFILE_IMAGE_SIX,
				"");

	}

	private void showImage() {
		if (!firstImageUrl.equals("")) {
			
			Picasso.with(this).load(firstImageUrl)
			.fit()
			.into(firstImage);
			
//			aQuery.id(firstImage).image(firstImageUrl, options)
//					.progress(R.id.progressProfile);
		}
		if (!secondImageUrl.equals("")) {
			findViewById(R.id.progressFirst).setVisibility(View.VISIBLE);
			
			Picasso.with(this).load(secondImageUrl)
			.fit()
			.into(secondImage);
			
//			aQuery.id(secondImage).image(secondImageUrl, options);
			deleteImage1.setVisibility(View.VISIBLE);
		}

		if (!thirdImageUrl.equals("")) {
			findViewById(R.id.progressSecond).setVisibility(View.VISIBLE);
			
			Picasso.with(this).load(thirdImageUrl)
			.fit()
			.into(thirdImage);
			
//			aQuery.id(thirdImage).image(thirdImageUrl, options);
			deleteImage2.setVisibility(View.VISIBLE);
			// addImage2.setVisibility(View.GONE);
		}
		if (!fourthImageUrl.equals("")) {
			findViewById(R.id.progressThird).setVisibility(View.VISIBLE);
			
			Picasso.with(this).load(fourthImageUrl)
			.fit()
			.into(fourthImage);
			
//			aQuery.id(fourthImage).image(fourthImageUrl, options);
			deleteImage3.setVisibility(View.VISIBLE);
			// addImage3.setVisibility(View.GONE);
		}
		if (!fifthImageUrl.equals("")) {
			findViewById(R.id.progressFour).setVisibility(View.VISIBLE);
			
			Picasso.with(this).load(fifthImageUrl)
			.fit()
			.into(fifthImage);
			
//			aQuery.id(fifthImage).image(fifthImageUrl, options);
			deleteImage4.setVisibility(View.VISIBLE);
			// addImage4.setVisibility(View.GONE);
		}
		if (!sixthImageUrl.equals("")) {
			findViewById(R.id.progressFive).setVisibility(View.VISIBLE);
			
			Picasso.with(this).load(sixthImageUrl)
			.fit()
			.into(sixImage);
			
//			aQuery.id(sixImage).image(sixthImageUrl, options);
			deleteImage5.setVisibility(View.VISIBLE);
			// addImage5.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		getDataFromPref();
		switch (v.getId()) {
		case R.id.update_profile_pic:
			selectedImage = 1;
			ShowDialogForChoose();
			break;
		/*
		 * case R.id.firtstImage: selectedImage = 2; onImageClick(2,
		 * secondImageUrl, false); break; case R.id.secondImage: selectedImage =
		 * 3; onImageClick(3, thirdImageUrl, false); break; case
		 * R.id.thirdimageview: selectedImage = 4; onImageClick(4,
		 * fourthImageUrl, false); break; case R.id.fourtthimageview:
		 * selectedImage = 5; onImageClick(5, fifthImageUrl, false); break; case
		 * R.id.fifthImageview: selectedImage = 6; onImageClick(6,
		 * sixthImageUrl, false); break;
		 */
		case R.id.editbuton:
			onImageClick(selectedImage, "", true);
			break;
		case R.id.addImage1:
			selectedImage = 2;
			addImage(2, secondImageUrl, false);
			break;
		case R.id.addImage2:
			selectedImage = 3;
			addImage(3, thirdImageUrl, false);
			break;
		case R.id.addImage3:
			selectedImage = 4;
			addImage(4, fourthImageUrl, false);
			break;
		case R.id.addImage4:
			selectedImage = 5;
			addImage(5, fifthImageUrl, false);
			break;
		case R.id.addImage5:
			selectedImage = 6;
			addImage(6, sixthImageUrl, false);
			break;
		case R.id.deleteImage1:
			Alertdialog(2);
			break;
		case R.id.deleteImage2:
			Alertdialog(3);
			break;
		case R.id.deleteImage3:
			Alertdialog(4);
			break;
		case R.id.deleteImage4:
			Alertdialog(5);
			break;
		case R.id.deleteImage5:
			Alertdialog(6);
			break;
		case R.id.deletebutton:

			Alertdialog();

			break;
		case R.id.setProfileButton:
			deleteLayout.setVisibility(View.GONE);
			setprofile(selectedImage);
			break;
		case R.id.btnEditProfileSubmit:
			if (!detector.isConnectingToInternet()) {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			Utility.showProcess(EditProfileNew.this, "Updating status..");
			new Thread(new Runnable() {
				@Override
				public void run() {
					updateStatus();
				}
			}).start();
			break;
		default:
			break;
		}
	}

	private void Alertdialog(final int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Image");
		builder.setMessage("Do you want to delete this image?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				deleteLayout.setVisibility(View.GONE);
				deleteImage(i);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				deleteLayout.setVisibility(View.GONE);
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	private void Alertdialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Image");
		builder.setMessage("Do you want to delete this image?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				deleteLayout.setVisibility(View.GONE);
				deleteImage(selectedImage);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				deleteLayout.setVisibility(View.GONE);
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	private void updateStatus() {
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				preferences.getString(Constant.FACEBOOK_ID, "")));
		nameValuePairs.add(new KeyValuePair(Constant.KEY_STATUS, edtStatus
				.getText().toString().trim()));
		if (male.isChecked()) {
			gender = 1;
		} else if (female.isChecked()) {
			gender = 2;
		}

		nameValuePairs
				.add(new BasicNameValuePair(Constant.KEY_SEX, gender + ""));
		try {
			for (int i = 0; i < nameValuePairs.size(); i++)
				Log.e(nameValuePairs.get(i).getName(), nameValuePairs.get(i)
						.getValue());

			Log.e("url", Constant.status_update_url);

			String statusResponse = httpRequest.postData(
					Constant.status_update_url, nameValuePairs);
			Log.i(TAG, "status submitted");
			final String string = JsonParser
					.parseStatusResponse(statusResponse);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utility.closeprocess(EditProfileNew.this);
					if (string != null) {
						Toast.makeText(EditProfileNew.this, string,
								Toast.LENGTH_SHORT).show();
						editor.putString(Constant.PREF_USER_STATUS, edtStatus
								.getText().toString().trim());
						editor.putInt(Constant.KEY_SEX, gender);

						editor.commit();
						SessionManager mSessionManager = new SessionManager(
								EditProfileNew.this);
						mSessionManager.setUserSex(gender + "");
						finish();
					} else {
						Toast.makeText(EditProfileNew.this, "Update failed",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setprofile(int i) {
		if (!detector.isConnectingToInternet()) {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
			return;
		}

		String imageId = null;
		switch (i) {
		case 2:
			imageId = (String) secondImage.getTag();
			break;
		case 3:
			imageId = (String) thirdImage.getTag();
			break;
		case 4:
			imageId = (String) fourthImage.getTag();
			break;
		case 5:
			imageId = (String) fifthImage.getTag();
			break;
		case 6:
			imageId = (String) sixImage.getTag();
			break;

		default:
			break;
		}

		Utility.showProcess(EditProfileNew.this, "Changing Image..");
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				preferences.getString(Constant.FACEBOOK_ID, "")));
		nameValuePairs.add(new KeyValuePair(Constant.KEY_NEW_INDEX_ID, (i - 1)
				+ ""));
		nameValuePairs
				.add(new KeyValuePair(Constant.KEY_NEW_IMAGE_ID, imageId));
		AppLog.Log(
				TAG,
				"FaceBook Id::"
						+ preferences.getString(Constant.FACEBOOK_ID, "")
						+ " index Id::" + (i - 1) + " image id" + imageId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json;
				try {
					json = httpRequest.postData(Constant.change_profile_pic,
							nameValuePairs);
					AppLog.Log(TAG, "Response after changing profile:" + json);
					ArrayList<ProfileImageModel> listProfile = JsonParser
							.parseProfileImageData(json);
					SetListIntoPref(listProfile);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getDataFromPref();
							showImage();
							Utility.closeprocess(EditProfileNew.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Utility.closeprocess(EditProfileNew.this);
						}
					});
				}
			}

		}).start();

	}

	private void deleteImage(int selectedImage) {
		switch (selectedImage) {
		case 2:
			if (detector.isConnectingToInternet()) {
				String Tag = (String) secondImage.getTag();
				delete_picture(Integer.parseInt(Tag));
			} else {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			editor.putString(Constant.PREF_PROFILE_IMAGE_TWO, null);
			editor.commit();
			secondImage.setImageResource(R.drawable.add_image);
			deleteImage1.setVisibility(View.GONE);
			findViewById(R.id.progressFirst).setVisibility(View.GONE);
			break;
		case 3:
			if (detector.isConnectingToInternet()) {
				String Tag = (String) thirdImage.getTag();
				delete_picture(Integer.parseInt(Tag));
			} else {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			editor.putString(Constant.PREF_PROFILE_IMAGE_THREE, null);
			editor.commit();
			thirdImage.setImageResource(R.drawable.add_image);
			deleteImage2.setVisibility(View.GONE);
			findViewById(R.id.progressSecond).setVisibility(View.GONE);
			break;
		case 4:
			if (detector.isConnectingToInternet()) {
				String Tag = (String) fourthImage.getTag();
				delete_picture(Integer.parseInt(Tag));
			} else {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}

			editor.putString(Constant.PREF_PROFILE_IMAGE_FOUR, null);
			editor.commit();
			fourthImage.setImageResource(R.drawable.add_image);
			deleteImage3.setVisibility(View.GONE);
			findViewById(R.id.progressThird).setVisibility(View.GONE);
			break;
		case 5:
			if (detector.isConnectingToInternet()) {
				String Tag = (String) fifthImage.getTag();
				delete_picture(Integer.parseInt(Tag));
			} else {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;

			}
			editor.putString(Constant.PREF_PROFILE_IMAGE_FIVE, null);
			editor.commit();
			fifthImage.setImageResource(R.drawable.add_image);
			deleteImage4.setVisibility(View.GONE);
			findViewById(R.id.progressFour).setVisibility(View.GONE);
			break;
		case 6:
			if (detector.isConnectingToInternet()) {
				String Tag = (String) sixImage.getTag();
				delete_picture(Integer.parseInt(Tag));
			} else {
				Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
				return;
			}
			editor.putString(Constant.PREF_PROFILE_IMAGE_SIX, null);
			editor.commit();
			sixImage.setImageResource(R.drawable.add_image);
			deleteImage5.setVisibility(View.GONE);
			findViewById(R.id.progressFive).setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void onImageClick(int selection, String imageUrl, boolean edit) {
		selectedImage = selection;
		AppLog.Log(TAG, "ImageUrl" + imageUrl);
		if (edit) {
			deleteLayout.setVisibility(View.GONE);
			ShowDialogForChoose();
		} else {
			if (imageUrl.equals("")) {
				deleteLayout.setVisibility(View.GONE);
				ShowDialogForChoose();
			} else {
				deleteLayout.setVisibility(View.VISIBLE);
				if (selectedImage == 1) {
					btnDelete.setVisibility(View.GONE);
					btnSetProfile.setVisibility(View.GONE);
				} else {
					btnDelete.setVisibility(View.VISIBLE);
					btnSetProfile.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void addImage(int selection, String imageUrl, boolean edit) {
		selectedImage = selection;
		AppLog.Log(TAG, "ImageUrl" + imageUrl);
		if (edit) {
			deleteLayout.setVisibility(View.GONE);
			ShowDialogForChoose();
		} else {
			if (imageUrl.equals("")) {
				deleteLayout.setVisibility(View.GONE);
				ShowDialogForChoose();
			} else {
				deleteLayout.setVisibility(View.VISIBLE);
				if (selectedImage == 1) {
					btnDelete.setVisibility(View.GONE);
					btnSetProfile.setVisibility(View.GONE);
				} else {
					btnDelete.setVisibility(View.VISIBLE);
					btnSetProfile.setVisibility(View.VISIBLE);
				}

			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bitmap bitmap = null;
		if (CAMERA_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
			bitmap = resizeBitmap(imagePath);
		} else if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK && null != data) {
			if (data != null) {
				Uri contentURI = data.getData();
				imagePath = getRealPathFromURI(contentURI);
				bitmap = resizeBitmap(imagePath);
			}
		}
		if (resultCode == RESULT_OK) {
			String base64String = "";
			if (bitmap != null) {
				base64String = Utility.getBase64String(bitmap);
			} else {
				Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			switch (selectedImage) {
			case 1:

				if (detector.isConnectingToInternet()) {
					uploadImage(0, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				aQuery.id(firstImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_ONE, imagePath);
				editor.commit();
				break;
			case 2:

				if (detector.isConnectingToInternet()) {
					uploadImage(1, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				deleteImage1.setVisibility(View.VISIBLE);
				aQuery.id(secondImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_TWO, imagePath);
				editor.commit();

				break;
			case 3:
				if (detector.isConnectingToInternet()) {
					uploadImage(2, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				deleteImage2.setVisibility(View.VISIBLE);
				aQuery.id(thirdImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_THREE, imagePath);
				editor.commit();
				break;
			case 4:
				if (detector.isConnectingToInternet()) {
					uploadImage(3, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				deleteImage3.setVisibility(View.VISIBLE);
				aQuery.id(fourthImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_FOUR, imagePath);
				editor.commit();
				break;
			case 5:
				if (detector.isConnectingToInternet()) {
					uploadImage(4, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;

				}
				deleteImage4.setVisibility(View.VISIBLE);
				aQuery.id(fifthImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_FIVE, imagePath);
				editor.commit();
				break;
			case 6:
				if (detector.isConnectingToInternet()) {
					uploadImage(5, base64String);
				} else {
					Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT)
							.show();
					return;

				}
				deleteImage5.setVisibility(View.VISIBLE);
				aQuery.id(sixImage).image(bitmap);
				editor.putString(Constant.PREF_PROFILE_IMAGE_SIX, imagePath);
				editor.commit();
				break;

			default:
				break;
			}
		}
	}

	private Bitmap resizeBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		int outWidth = 0, outHeight = 0;
		if (path != null) {
			BitmapFactory.decodeFile(path, options);
			outWidth = options.outWidth;
			outHeight = options.outHeight;
		} else {
			if (photoBitmap != null) {
				outWidth = photoBitmap.getWidth();
				outHeight = photoBitmap.getHeight();
			} else {
				return null;
			}
		}
		int ratio = (int) ((((float) outWidth) / mobile_width) + 0.5f);
		if (ratio == 0) {
			ratio = 1;
		}
//		if (path != null) {
//			Log.e("here if", "if");
			options.inJustDecodeBounds = false;
			options.inSampleSize = ratio;
			// Applog.Log(TAG, "from gallery  " + ratio);
			photoBitmap = BitmapFactory.decodeFile(path, options);
			outWidth = photoBitmap.getWidth();
			outHeight = photoBitmap.getHeight();
			// ivItemPhoto.setImageBitmap(photoBitmap);
//			return photoBitmap;
//		} else {
//			Log.e("here else", "else");
//			outWidth = outWidth / ratio;
//			outHeight = outHeight / ratio;
//			photoBitmap = Bitmap.createScaledBitmap(photoBitmap, outWidth,
//					outHeight, true);
			
			try {
				ExifInterface exif = new ExifInterface(path);
				String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
				Log.e("orientString", orientString);
				int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
				int rotationAngle = 0;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
				
				Matrix matrix = new Matrix();
				matrix.setRotate(rotationAngle, (float) outWidth, (float) outHeight);
				photoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, outWidth, outHeight, matrix, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return photoBitmap;
//		}
//		Log.e("outWidth/outHeight", outWidth+"/"+outHeight);
//		
//		Bitmap rotatedBitmap = null;
//		try {
//		// Create and configure BitmapFactory
//		BitmapFactory.Options bounds = new BitmapFactory.Options();
//		bounds.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(path, bounds);
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		Bitmap bm = BitmapFactory.decodeFile(path, opts);
//		// Read EXIF Data
//		ExifInterface exif = new ExifInterface(path);
//		
//		String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//		int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
//		int rotationAngle = 0;
//		if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
//		if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
//		if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
//		// Rotate Bitmap
//		Matrix matrix = new Matrix();
//		matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//		rotatedBitmap=Bitmap.createScaledBitmap(photoBitmap, 100,
//				100, true);
//		
////		rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,outWidth, outHeight, matrix, true);
//		
//		// Return result
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return rotatedBitmap;
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	private void delete_picture(int image_id) {
		Utility.showProcess(EditProfileNew.this, "Deleting Image..");
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				preferences.getString(Constant.FACEBOOK_ID, "")));
		nameValuePairs.add(new KeyValuePair(Constant.KEY_IMAGE_ID, image_id
				+ ""));
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json;
				try {
					json = httpRequest.postData(Constant.delete_user_pic,
							nameValuePairs);
					JSONObject object = new JSONObject(json);
					errorFlag = object.getInt(Constant.ERR_FLAG);
					AppLog.Log(TAG, "Delete json:" + json);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Utility.closeprocess(EditProfileNew.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	private void uploadImage(final int id, String base64) {
		Utility.showProcess(EditProfileNew.this, "Uploding Image..");
		AppLog.Log(TAG, "Index ID::" + id + " AND base64" + base64);
		// final int errorFlag = 1;
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				preferences.getString(Constant.FACEBOOK_ID, "")));
		nameValuePairs.add(new KeyValuePair("ent_index_id", id + ""));
		nameValuePairs.add(new KeyValuePair("ent_userimage", base64));
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json;
				try {
					json = httpRequest.postData(Constant.upload_user_pic,
							nameValuePairs);
					AppLog.Log(TAG, "Upload image response:" + json);
					JSONObject object = new JSONObject(json);

					errorFlag = object.getInt(Constant.ERR_FLAG);
					if (errorFlag == 0) {

						setImageTagAndUrlToPref(id,
								object.getInt("ent_image_id"),
								object.getString("picURL"));
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EditProfileNew.this,
										"Some Error Occured Please try again.",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
					AppLog.Log(TAG, "Answer json:" + json);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Utility.closeprocess(EditProfileNew.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}).start();
	}

	private void getProfileImages() {
		Utility.showProcess(EditProfileNew.this, "Getting Images..");
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				preferences.getString(Constant.FACEBOOK_ID, "")));
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json;
				try {
					json = httpRequest.postData(Constant.get_user_pro_pic,
							nameValuePairs);
					AppLog.Log(TAG, "Image json:" + json);
					ArrayList<ProfileImageModel> imageList = JsonParser
							.parseProfileImageData(json);
					AppLog.Log(TAG, "Return Image Size:" + imageList.size());
					SetListIntoPref(imageList);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getDataFromPref();
							showImage();
							Utility.closeprocess(EditProfileNew.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void SetListIntoPref(ArrayList<ProfileImageModel> imageList) {
		if (imageList.size() > 0) {
			for (int i = 0; i < imageList.size(); i++) {
				final ProfileImageModel imageModel = imageList.get(i);
				int imageIndex = imageModel.getIndexId();
				switch (imageIndex) {
				case 0:
					setImageTagAndUrlToPref(0, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				case 1:
					setImageTagAndUrlToPref(1, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				case 2:
					setImageTagAndUrlToPref(2, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				case 3:
					setImageTagAndUrlToPref(3, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				case 4:
					setImageTagAndUrlToPref(4, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				case 5:
					setImageTagAndUrlToPref(5, imageModel.getImageId(),
							imageModel.getImageUrl());
					break;
				default:
					break;
				}
			}

		}

	}

	protected void setImageTagAndUrlToPref(final int id, final int i,
			final String url) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (id) {
				case 0:
					editor.putString(Constant.PREF_PROFILE_IMAGE_ONE, url);
					editor.commit();
					firstImage.setTag(i + "");
					break;
				case 1:
					editor.putString(Constant.PREF_PROFILE_IMAGE_TWO, url);
					editor.commit();
					secondImage.setTag(i + "");
					break;
				case 2:
					editor.putString(Constant.PREF_PROFILE_IMAGE_THREE, url);
					editor.commit();
					thirdImage.setTag(i + "");
					break;
				case 3:
					editor.putString(Constant.PREF_PROFILE_IMAGE_FOUR, url);
					editor.commit();
					fourthImage.setTag(i + "");
					break;
				case 4:
					editor.putString(Constant.PREF_PROFILE_IMAGE_FIVE, url);
					editor.commit();
					fifthImage.setTag(i + "");
					break;
				case 5:
					editor.putString(Constant.PREF_PROFILE_IMAGE_SIX, url);
					editor.commit();
					sixImage.setTag(i + "");
					break;

				default:
					break;
				}
			}
		});

	}

	private Uri getTempUri() {
		// Create an image file name
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String dt = sdf.format(new Date());
		imageFile = null;
		imageFile = new File(Environment.getExternalStorageDirectory()
				+ "/Flamer/", "Camera_" + dt + ".jpg");
		AppLog.Log(
				TAG,
				"New Camera Image Path:- "
						+ Environment.getExternalStorageDirectory()
						+ "/Flamer/" + "Camera_" + dt + ".jpg");
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Flamer");
		if (!file.exists()) {
			file.mkdir();
		}
		if (!imageFile.exists()) {
			try {
				imageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		imagePath = Environment.getExternalStorageDirectory() + "/Flamer/"
				+ "Camera_" + dt + ".jpg";
		imageUri = Uri.fromFile(imageFile);
		return imageUri;
	}

	private void ShowDialogForChoose() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, new String[] {
								"Camera", "Gallery" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									Intent cameraIntent = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

									cameraIntent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											getTempUri());
									startActivityForResult(cameraIntent,
											CAMERA_REQUEST_CODE);
								} else if (which == 1) {
									Intent intent = new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent,
													"Select File"),
											RESULT_LOAD_IMAGE);

									// Intent i = new Intent(
									// Intent.ACTION_PICK,
									// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

									// Intent intent = new
									// Intent(Intent.ACTION_VIEW,
									// Uri.parse("content://media/internal/images/media"));
									// startActivityForResult(intent,
									// RESULT_LOAD_IMAGE);

									// TODO set gallery path
									// Intent i = new Intent(
									// Intent.ACTION_PICK,
									// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT
									// _URI);
									// startActivityForResult(i,
									// RESULT_LOAD_IMAGE);
								}
							}
						});
		builder.create().show();
	}
}
