package com.appdupe.androidpushnotifications;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.appdupe.pair.pojo.LikeMatcheddataForListview;
import com.appdupe.pair.pojo.LikedMatcheData;
import com.appdupe.pair.pojo.Likes;
import com.appdupe.pair.utility.AlertDialogManager;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairchat.db.DatabaseHandler;
import com.appdupe.pairnofb.R;
import com.google.gson.Gson;

public class Chat_List extends SherlockFragmentActivity {
	private ListView matcheslistview;
	private MatchedDataAdapter adapter;
	private static final String TAG = "ChatActivity";
	private ArrayList<LikeMatcheddataForListview> arryList;
	private SharedPreferences preferences;
	private Dialog mdialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.rightmenu);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		matcheslistview = (ListView) findViewById(R.id.menu_right_ListView);
		arryList = new ArrayList<LikeMatcheddataForListview>();
		findLikedMatched();
		
		((ImageView) findViewById(R.id.back_image))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		matcheslistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LikeMatcheddataForListview matcheddataForListview = (LikeMatcheddataForListview) parent
						.getItemAtPosition(position);
				String faceboolid = matcheddataForListview.getFacebookid();
				// logDebug(" background setOnItemClickListener  onItemClick friend facebook id faceboolid "+faceboolid);
				// logDebug(" background setOnItemClickListener  onItemClick user facebook id  faceboolid"+new
				// SessionManager(MainActivity.this).getFacebookId());
				Bundle mBundle = new Bundle();
				mBundle.putString(Constant.FRIENDFACEBOOKID, faceboolid);
				mBundle.putString(Constant.CHECK_FOR_PUSH_OR_NOT, "1");

				Intent mIntent = new Intent(Chat_List.this, ChatActivity.class);
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
			}
		});
		initSerchData();
	}
	
	/*
	 * method responsible for initialize search function at right side friend
	 * list adapter of this activity
	 */
	private void initSerchData() {
		((EditText)findViewById(R.id.et_serch_right_side_menu)).addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(adapter!=null)
				adapter.getFilter().filter(s.toString().trim());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

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
	
	private void findLikedMatched() {
		AppLog.Log(TAG, "findLikedMatched for --"+preferences.getString(Constant.FACEBOOK_ID, ""));
		String params[] = { preferences.getString(Constant.FACEBOOK_ID, "") };
		new BackgroundTaskForFindLikeMatched().execute(params);
	}

	private class BackgroundTaskForFindLikeMatched extends
			AsyncTask<String, Void, Void> {
		private Ultilities mUltilities = new Ultilities();
		private List<NameValuePair> getuserparameter;
		private String likedmatchedata;
		private LikedMatcheData matcheData;
		private ArrayList<Likes> likesList;
		private LikeMatcheddataForListview matcheddataForListview;
		DatabaseHandler mDatabaseHandler = new DatabaseHandler(Chat_List.this);
		private boolean isResponseSuccess = true;

		@Override
		protected Void doInBackground(String... params) {
			try {

				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched   doInBackground appDirectory "
								+ appDirectory);
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.imagedirematchuserdirectory));

				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched doInBackground ");

				getuserparameter = mUltilities.getUserLikedParameter(params);

				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched doInBackground   getuserparameter "
								+ getuserparameter);
				
				HttpRequest httpRequest = new HttpRequest();
				likedmatchedata=httpRequest.postData(Constant.getliked_url,
						getuserparameter);

				/*likedmatchedata = mUltilities.makeHttpRequest(
						Constant.getliked_url, Constant.methodeName,
						getuserparameter);*/

				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched doInBackground   likedmatchedata "
								+ likedmatchedata);

				Gson gson = new Gson();
				matcheData = gson.fromJson(likedmatchedata,
						LikedMatcheData.class);

				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched doInBackground   matcheData "
								+ matcheData);

				if (matcheData.getErrFlag() == 0) {
					likesList = matcheData.getLikes();
					AppLog.Log(TAG,
							"BackgroundTaskForFindLikeMatched doInBackground   likesList "
									+ likesList);
					if (arryList != null) {
						arryList.clear();
					}

					AppLog.Log(TAG,
							"BackgroundTaskForFindLikeMatched doInBackground   likesList sized "
									+ likesList.size());
					for (int i = 0; i < likesList.size(); i++) {
						matcheddataForListview = new LikeMatcheddataForListview();
						String userName = likesList.get(i).getfName();
						String facebookid = likesList.get(i).getFbId();
						String picturl = likesList.get(i).getpPic();
						int falg = likesList.get(i).getFlag();
						String latd = likesList.get(i).getLadt();
						matcheddataForListview.setFacebookid(facebookid);
						matcheddataForListview.setUserName(userName);
						matcheddataForListview.setImageUrl(picturl);
						matcheddataForListview.setFlag("" + falg);
						matcheddataForListview.setladt(latd);
						// matcheddataForListview.setFilePath(filePath);
						File imageFile = mUltilities.createFileInSideDirectory(
								_picDir, userName + facebookid + ".jpg");
						// logDebug("BackGroundTaskForUserProfile doInBackground imageFile is profile "+imageFile.isFile());
						Utility.addBitmapToSdCardFromURL(likesList.get(i)
								.getpPic().replaceAll(" ", "%20"), imageFile);
						matcheddataForListview.setFilePath(imageFile
								.getAbsolutePath());
						if (!preferences.getString(Constant.FACEBOOK_ID, "")
								.equals(facebookid)) {
							arryList.add(matcheddataForListview);
						}

					}
					DatabaseHandler mDatabaseHandler = new DatabaseHandler(
							Chat_List.this);
					// SessionManager mSessionManager = new SessionManager(
					// MainActivity.this);
					String userFacebookid = preferences.getString(
							Constant.FACEBOOK_ID, "");

					//
					boolean isdataiserted = mDatabaseHandler.insertMatchList(
							arryList, userFacebookid);

					// what it is??
					// ////////////////////////////////////////////////////////////////////////////////

					ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
							.getUserFindMatch();
					AppLog.Log(TAG, "arryListtem  " + arryListtem);
					if (arryListtem != null && arryListtem.size() > 0) {
						AppLog.Log(TAG, "arryList  size " + arryListtem.size());
						arryList.clear();

						arryList.addAll(arryListtem);
					}

				} else if (matcheData.getErrFlag() == 1) {
					ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
							.getUserFindMatch();
					AppLog.Log(TAG, "arryListtem  " + arryListtem);
					if (arryListtem != null && arryListtem.size() > 0) {
						AppLog.Log(TAG, "arryList  size " + arryListtem.size());
						arryList.clear();

						arryList.addAll(arryListtem);
					}

				} else {
					// do nothing
				}

			} catch (Exception e) {
				AppLog.handleException(
						"BackgroundTaskForFindLikeMatched doInBackground Exception ",
						e);
				// some thing wrong happend
				isResponseSuccess = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			AppLog.Log(TAG, "BackgroundTaskForFindLikeMatched onPostExecute  ");
			try {
				mdialog.dismiss();
			} catch (Exception e) {
				AppLog.Log(TAG,
						"BackgroundTaskForFindLikeMatched   onPostExecute  Exception "
								+ e);
			}
			if (!isResponseSuccess) {
				AlertDialogManager.errorMessage(Chat_List.this, "Alert",
						"Request timeout");
			}
			Ultilities mUltilities = new Ultilities();
			int imageHeightAndWidht[] = mUltilities
					.getImageHeightAndWidthForAlubumListview(Chat_List.this);
			adapter = new MatchedDataAdapter(Chat_List.this, arryList, imageHeightAndWidht);
			System.out.println("Matched Adapter=" + adapter);
			matcheslistview.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
		}

		protected void onPreExecute() {
			super.onPreExecute();
			AppLog.Log(TAG, "BackgroundTaskForFindLikeMatched onPreExecute  ");
			try {
				mdialog = mUltilities.GetProcessDialog(Chat_List.this);
				mdialog.setCancelable(false);
				mdialog.show();
			} catch (Exception e) {
				AppLog.handleException(
						"BackgroundTaskForFindLikeMatched   onPreExecute  Exception ",
						e);
			}

		}

	}

}