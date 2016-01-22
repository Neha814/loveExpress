package com.android.slidingmenu;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.androidpushnotifications.ChatActivity;
import com.appdupe.pair.pojo.FindMatchData;
import com.appdupe.pair.pojo.InviteActionData;
import com.appdupe.pair.pojo.LikeMatcheddataForListview;
import com.appdupe.pair.pojo.MatchesData;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.CircleTransform;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.ScalingUtilities;
import com.appdupe.pair.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.pair.utility.ScreenSize;
import com.appdupe.pair.utility.SessionManager;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairchat.db.DatabaseHandler;
import com.appdupe.pairnofb.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class FindMatches extends Fragment implements // OnDragListener,
		OnClickListener, com.squareup.picasso.Callback {
	private static final String TAG = "Layout1 Fragment";
	private Animation anime;
	static int xd, yd;
	private RelativeLayout swipeviewlayout;
	private RelativeLayout noMatchFound;
	private ImageView userProfilImage, amimagetedview, likeButton,
			dislikeButton, matchedUserInfoButton;
	private TextView messagetextview;
	private ArrayList<MatchesData> MachedataList;
	private int[] matchUserHeightAndWidth;
	private int[] profileImageHeightAndWidth;
	private SharedPreferences preferences;
	private String machedUserFaceBookid;
	private Button inviteButton;
	private LinearLayout likedislikelayout;
	private RelativeLayout invitebuttonlayout;
	private int imageindex = 0;
	private int MatchCount;
	private int numberOfImageDownload = 3;
	private int mutualLikedId = 5000;
	private int mulikediconId = 2000;
	private int mutulFriendiconId = 1000;
	private int userNameid = 3000;
	private int imageDownloadingCount = 0;
	private MatchesData matchesData;
	private int _xDelta;
	private int _yDelta;
	private int windowwidth;
	private int screenCenter;
	private int x_cord, y_cord;
	private int Likes = 0;
	private float alphaValue = 0;
	private ConnectionDetector cd;

	Typeface Montserrat_Regular, Montserrat_Bold;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_layout1, null);
		cd = new ConnectionDetector(getActivity());
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
					.show();
			return view;
		}

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getActivity()
				.getAssets());
		Montserrat_Bold = BasicFunctions.Montserrat_Bold(getActivity()
				.getAssets());

		preferences = PreferenceManager.getDefaultSharedPreferences(inflater
				.getContext());
		invitebuttonlayout = (RelativeLayout) view
				.findViewById(R.id.invitebuttonlayout);
		inviteButton = (Button) view.findViewById(R.id.inviteButton);
		invitebuttonlayout.setVisibility(View.GONE);
		swipeviewlayout = (RelativeLayout) view
				.findViewById(R.id.swipeviewlayout);
		userProfilImage = (ImageView) view.findViewById(R.id.userProfilImage);
		messagetextview = (TextView) view.findViewById(R.id.messagetextview);
		amimagetedview = (ImageView) view.findViewById(R.id.circleimageview);
		noMatchFound = (RelativeLayout) view.findViewById(R.id.noMatchFound);
		matchedUserInfoButton = (ImageView) view
				.findViewById(R.id.matchedUserInfoButton);
		likeButton = (ImageView) view.findViewById(R.id.likeButton);
		dislikeButton = (ImageView) view.findViewById(R.id.dislikeButton);
		likedislikelayout = (LinearLayout) view
				.findViewById(R.id.likedislikelayout);

		((TextView) view.findViewById(R.id.search_love))
				.setTypeface(Montserrat_Regular);
		messagetextview.setTypeface(Montserrat_Regular);
		inviteButton.setTypeface(Montserrat_Regular);

		matchedUserInfoButton.setOnClickListener(this);
		likeButton.setOnClickListener(this);
		dislikeButton.setOnClickListener(this);
		inviteButton.setOnClickListener(this);
		Ultilities mUltilities = new Ultilities();
		matchUserHeightAndWidth = mUltilities
				.getImageHeightAndWidthForMatchedUser(getActivity());
		profileImageHeightAndWidth = mUltilities
				.getImageHeightAndWidthForProFileImageHomsecreen(getActivity());
		// RelativeLayout.LayoutParams findPeoplelayoutParams = mUltilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// findPeoplelayoutParams.addRule(RelativeLayout.BELOW,
		// R.id.imageviewlayout);
		// findPeoplelayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// findPeoplelayoutParams.setMargins(0,
		// topMarginForInvitelayoutAndText[0], 0, 0);
		// findingpeopletextlayout.setLayoutParams(findPeoplelayoutParams);
		// RelativeLayout.LayoutParams invitlayoutParams = mUltilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// invitlayoutParams.addRule(RelativeLayout.BELOW,
		// R.id.findingpeopletextlayout);
		// invitlayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// invitlayoutParams.setMargins(0, topMarginForInvitelayoutAndText[1],
		// 0,
		// 0);
		// invitebuttonlayout.setLayoutParams(invitlayoutParams);
		ScreenSize screenSize = new ScreenSize(getActivity());
		windowwidth = (int) screenSize.getScreenWidthPixel();
		screenCenter = windowwidth / 2;
		// RelativeLayout.LayoutParams likedislikeparam = new
		// RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// likedislikeparam.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// likedislikeparam.setMargins(0, imageLayoutHeightandWidth[3], 0, 0);
		// likedislikelayout.setLayoutParams(likedislikeparam);

		try {
			setProfilePick(userProfilImage, profileImageHeightAndWidth[0],
					profileImageHeightAndWidth[1]);
		} catch (Exception e) {
			AppLog.handleException(TAG + " onCreateView  Exception ", e);
		}
		anime = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
		amimagetedview.startAnimation(anime);

		anime.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

		ConnectionDetector connectionDetector = new ConnectionDetector(
				getActivity());

		if (connectionDetector.isConnectingToInternet()) {
			// if (isFristTimeLogin()) {
			// Editor editor = preferences.edit();
			// editor.putBoolean(Constant.PREF_IS_FIRST_TIME, false);
			// editor.commit();
			// showJEWISHDialog();
			// } else {
			findMatch();
			// }
		} else {
			Ultilities ultilities = new Ultilities();
			ultilities.displayMessageAndExit(getActivity(), "Alert",
					" working internet connection required");

		}

		// addView(MachedataList);

		return view;

	}

	private ImageView setImageOnImgeView(String imagaeUrl) {
		ImageView imageView = new ImageView(getActivity());
		// imageView.setLayoutParams(new
		// LayoutParams(matchUserHeightAndWidth[1],
		// matchUserHeightAndWidth[0]));

		Picasso.with(getActivity()) //
				.load(imagaeUrl) //
				.error(R.drawable.error) //
				.resize(matchUserHeightAndWidth[1], matchUserHeightAndWidth[0]) //
				.into(imageView, this);

		return imageView;
	}

	@SuppressWarnings("unused")
	private RelativeLayout setMutualLikeAndFrien(MatchesData matchesData,
			Ultilities ultilities, int index) {
		RelativeLayout mutuallikeandfriend = new RelativeLayout(getActivity());
		mutuallikeandfriend.setId(mutualLikedId + index);
		// android.widget.RelativeLayout.LayoutParams paramsmutualikeandDislike
		// = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsmutualikeandDislike.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// paramsmutualikeandDislike.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// mutuallikeandfriend.setLayoutParams(paramsmutualikeandDislike);

		// / mutulFriends
		RelativeLayout mutuafriendlayout = new RelativeLayout(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsmutuafriendlayout =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsmutuafriendlayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// mutuafriendlayout.setLayoutParams(paramsmutuafriendlayout);
		mutuafriendlayout.setBackgroundResource(R.drawable.pink_bar_right);

		mutuallikeandfriend.addView(mutuafriendlayout);

		RelativeLayout mutuafriendlayoutChile = new RelativeLayout(
				getActivity());
		// android.widget.RelativeLayout.LayoutParams
		// paramsmutuafriendlayoutChile = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsmutuafriendlayoutChile.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// paramsmutuafriendlayoutChile.addRule(RelativeLayout.CENTER_VERTICAL);
		// mutuafriendlayoutChile.setLayoutParams(paramsmutuafriendlayoutChile);
		mutuafriendlayout.addView(mutuafriendlayoutChile);

		ImageView follower_icon = new ImageView(getActivity());
		follower_icon.setId(mutulFriendiconId + index);
		// android.widget.RelativeLayout.LayoutParams paramsfollower_icon =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsfollower_icon.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsfollower_icon.setMargins(15, 0, 0, 0);
		// follower_icon.setLayoutParams(paramsfollower_icon);
		mutuafriendlayoutChile.addView(follower_icon);
		follower_icon.setBackgroundResource(R.drawable.follower_icon);

		TextView numberOfmutualFriend = new TextView(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsnumberOfmutual =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsnumberOfmutual.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsnumberOfmutual.addRule(RelativeLayout.RIGHT_OF,
		// follower_icon.getId());
		// paramsnumberOfmutual.setMargins(10, 0, 0, 0);
		// numberOfmutualFriend.setLayoutParams(paramsnumberOfmutual);
		mutuafriendlayoutChile.addView(numberOfmutualFriend);
		numberOfmutualFriend.setText("4");
		numberOfmutualFriend.setTextColor(android.graphics.Color.rgb(255, 255,
				255));

		// mutual likes

		RelativeLayout mutualikedayout = new RelativeLayout(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsmutualikedayout =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsmutualikedayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// paramsmutualikedayout.addRule(RelativeLayout.CENTER_IN_PARENT);
		// mutualikedayout.setLayoutParams(paramsmutualikedayout);
		mutualikedayout.setBackgroundResource(R.drawable.pink_bar_left);
		mutuallikeandfriend.addView(mutualikedayout);

		RelativeLayout mutualikedayoutChiled = new RelativeLayout(getActivity());
		// android.widget.RelativeLayout.LayoutParams
		// paramsmutualikedayoutChiled = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsmutualikedayoutChiled.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// paramsmutualikedayoutChiled.addRule(RelativeLayout.CENTER_VERTICAL);
		// mutualikedayoutChiled.setLayoutParams(paramsmutualikedayoutChiled);
		mutualikedayout.addView(mutualikedayoutChiled);

		ImageView book_icon = new ImageView(getActivity());
		book_icon.setId(mulikediconId + 1);
		android.widget.RelativeLayout.LayoutParams paramsbook_icon = ultilities
				.getRelativelayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsbook_icon.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsbook_icon.setMargins(15, 0, 0, 0);
		// book_icon.setLayoutParams(paramsfollower_icon);
		mutualikedayoutChiled.addView(book_icon);
		book_icon.setBackgroundResource(R.drawable.book_icon);

		TextView numberOfmutuallikes = new TextView(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsnumberOfmutuallikes
		// = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsnumberOfmutuallikes.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsnumberOfmutuallikes.addRule(RelativeLayout.RIGHT_OF,
		// book_icon.getId());
		// paramsnumberOfmutuallikes.setMargins(10, 0, 0, 0);
		// numberOfmutuallikes.setLayoutParams(paramsnumberOfmutuallikes);
		mutualikedayoutChiled.addView(numberOfmutuallikes);
		numberOfmutuallikes.setText(" " + matchesData.getImage().length);
		numberOfmutuallikes.setTextColor(android.graphics.Color.rgb(255, 255,
				255));

		return mutuallikeandfriend;
	}

	@SuppressWarnings("unused")
	private RelativeLayout setUserNameAndAgeLayout(
			RelativeLayout relativeLayout, MatchesData matchesData,
			Ultilities ultilities, int index) {
		RelativeLayout UserNameAndAgeLayout = new RelativeLayout(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsUserNameAndAgeLayout
		// = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUserNameAndAgeLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// paramsUserNameAndAgeLayout.addRule(RelativeLayout.ABOVE,
		// relativeLayout.getId());
		// UserNameAndAgeLayout.setBackgroundResource(R.drawable.gray_bar);
		// UserNameAndAgeLayout.setLayoutParams(paramsUserNameAndAgeLayout);

		RelativeLayout UserNameAndAgeLayoutChild = new RelativeLayout(
				getActivity());
		// android.widget.RelativeLayout.LayoutParams
		// paramsUserNameAndAgeLayoutChild = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUserNameAndAgeLayoutChild.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsUserNameAndAgeLayoutChild.setMargins(50, 0, 0, 0);
		// UserNameAndAgeLayoutChild
		// .setLayoutParams(paramsUserNameAndAgeLayoutChild);
		UserNameAndAgeLayout.addView(UserNameAndAgeLayoutChild);

		TextView UserName = new TextView(getActivity());
		UserName.setId(userNameid + index);
		// android.widget.RelativeLayout.LayoutParams paramsUserName =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUserName.addRule(RelativeLayout.CENTER_VERTICAL);
		// UserName.setLayoutParams(paramsUserName);
		UserName.setText(matchesData.getFirstName());
		UserNameAndAgeLayoutChild.addView(UserName);

		TextView UserAge = new TextView(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsUserAge = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUserAge.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsUserAge.addRule(RelativeLayout.RIGHT_OF, UserName.getId());
		// paramsUserAge.setMargins(10, 0, 0, 0);
		// UserAge.setLayoutParams(paramsUserAge);
		UserAge.setText("" + matchesData.getAge());
		UserNameAndAgeLayoutChild.addView(UserAge);

		RelativeLayout UserNameAndAgeLayoutChild1 = new RelativeLayout(
				getActivity());
		// android.widget.RelativeLayout.LayoutParams
		// paramsUserNameAndAgeLayoutChild1 = ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUserNameAndAgeLayoutChild1
		// .addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsUserNameAndAgeLayoutChild1
		// .addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// paramsUserNameAndAgeLayoutChild1.setMargins(280, 0, 50, 0);
		// UserNameAndAgeLayoutChild1
		// .setLayoutParams(paramsUserNameAndAgeLayoutChild1);
		UserNameAndAgeLayout.addView(UserNameAndAgeLayoutChild1);

		ImageView cameraIcon = new ImageView(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramscameraIcon =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramscameraIcon.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramscameraIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// cameraIcon.setBackgroundResource(R.drawable.camera_icon);
		// cameraIcon.setLayoutParams(paramscameraIcon);
		UserNameAndAgeLayoutChild1.addView(cameraIcon);

		TextView nuberofphoto = new TextView(getActivity());
		// android.widget.RelativeLayout.LayoutParams paramsnuberofphoto =
		// ultilities
		// .getRelativelayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsnuberofphoto.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsnuberofphoto.addRule(RelativeLayout.LEFT_OF,
		// cameraIcon.getId());
		// nuberofphoto.setLayoutParams(paramsnuberofphoto);
		nuberofphoto.setText("" + matchesData.getImage().length);
		UserNameAndAgeLayoutChild1.addView(nuberofphoto);

		return UserNameAndAgeLayout;
	}

	/**
	 * 
	 * 
	 * <b>This method is responsible for enabling menu fullscreen mode</b>
	 * 
	 * @param b
	 *            if true -->Enable Fullscreen <br />
	 *            else it will disable fullscreen menu mode
	 */
	private void setFullScreenMenuTouchEnable(boolean b) {
		MainActivity activity = (MainActivity) getActivity();
		if (activity != null) {
			activity.setMenuTouchFullScreenEnable(b);
		}
	}

	/**
	 * Add Matches Views
	 * 
	 * @param MachedataList
	 */
	private void addView(final ArrayList<MatchesData> MachedataList) {
		MatchCount = MachedataList.size();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		for (int i = 0; i < MatchCount; i++) {
			final int position = i;
			final RelativeLayout myRelView = (RelativeLayout) inflater.inflate(
					R.layout.match_user_layout_try, null);
			matchesData = this.MachedataList.get(i);
			machedUserFaceBookid = this.MachedataList.get(i).getFbId();
			myRelView.setTag(i);
			LinearLayout lay = (LinearLayout) myRelView
					.findViewById(R.id.camera_line);
			lay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int viewCount = swipeviewlayout.getChildCount();
					MatchesData matchesData = MachedataList.get(viewCount - 1);
					int selectedImageIndex = viewCount - 1;
					AppLog.Log(TAG, "Selected Index:---->" + selectedImageIndex);
					machedUserFaceBookid = matchesData.getFbId();
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager
							.setMatchedUserFacebookId(machedUserFaceBookid);
					mSessionManager
							.setImageIndexForLikeDislike(selectedImageIndex);
					mSessionManager
							.setLastDownloadImageIndex(numberOfImageDownload);
					Intent mIntent = new Intent(getActivity(),
							MatChedUserProfile.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constant.isFromChatScreen, false);
					mIntent.putExtra("match_data", matchesData);
					mIntent.putExtras(bundle);
					startActivity(mIntent);
				}
			});
			ImageView imageView = (ImageView) myRelView
					.findViewById(R.id.iv_user_image_user_matches);
			TextView txtMatchPercent = (TextView) myRelView
					.findViewById(R.id.txtMatchPerCent);
			txtMatchPercent.setText(MachedataList.get(i).getMatchPercentage()
					+ "%");
			Picasso.with(getActivity()) //
					.load(MachedataList.get(i).getPPic()) //
					/* .placeholder(R.drawable.placeholder) *///
					.error(R.drawable.error) //
					.resize(matchUserHeightAndWidth[1],
							matchUserHeightAndWidth[0]) //
					.into(imageView, this);

			txtMatchPercent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int viewCount = swipeviewlayout.getChildCount();
					MatchesData matchesData = MachedataList.get(viewCount - 1);
					int selectedImageIndex = viewCount - 1;
					AppLog.Log(TAG, "Selected Index:---->" + selectedImageIndex);
					machedUserFaceBookid = matchesData.getFbId();
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager
							.setMatchedUserFacebookId(machedUserFaceBookid);
					mSessionManager
							.setImageIndexForLikeDislike(selectedImageIndex);
					mSessionManager
							.setLastDownloadImageIndex(numberOfImageDownload);
					Intent mIntent = new Intent(getActivity(),
							MatChedUserProfile.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constant.isFromChatScreen, false);
					mIntent.putExtra("match_data", matchesData);
					mIntent.putExtras(bundle);
					startActivity(mIntent);
				}
			});

			TextView tvMatual = (TextView) myRelView
					.findViewById(R.id.tv_follower_count_user_matches);
			tvMatual.setText("04");
			TextView tvAge = (TextView) myRelView
					.findViewById(R.id.tv_name_age);
			tvAge.setText(MachedataList.get(i).getFirstName() + " , "
					+ MachedataList.get(i).getAge() + "");

			TextView tv_name_active = (TextView) myRelView
					.findViewById(R.id.tv_name_active);

			String gmtTime = matchesData.getLastActive();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String s = sdf.format(Calendar.getInstance().getTime());
				Date current_date = sdf.parse(s);
				Date last_active_date = sdf.parse(gmtTime);

				tv_name_active.setText(BasicFunctions.getDateDiffString(
						last_active_date, current_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Location loc1 = new Location("");
			loc1.setLatitude(Double.parseDouble(preferences.getString(
					Constant.PREF_USER_LAT, "" + 0)));
			loc1.setLongitude(Double.parseDouble(preferences.getString(
					Constant.PREF_USER_LONG, "" + 0)));

			String lat = matchesData.getLat();
			String long_ = matchesData.getLong();
			if (lat.equals(""))
				lat = "0.0";
			if (long_.equals(""))
				long_ = "0.0";
			Location loc2 = new Location("");
			loc2.setLatitude(Double.parseDouble(lat));
			loc2.setLongitude(Double.parseDouble(long_));

			double distanceInMiles = loc1.distanceTo(loc2) * 0.00062137;

			TextView tv_name_away = (TextView) myRelView
					.findViewById(R.id.tv_name_away);
			tv_name_away.setText(String.format("%.0f", distanceInMiles)
					+ " miles away");

			TextView tvImageCount = (TextView) myRelView
					.findViewById(R.id.tv_pic_count_user_matches);
			tvImageCount.setText(MachedataList.get(i).getImage().length + "");

			GradientDrawable sd_like = (GradientDrawable) getActivity()
					.getResources().getDrawable(R.drawable.like_nops_back);
			// sd_like.setColor(android.graphics.Color.GREEN);
			sd_like.setStroke(2, android.graphics.Color.GREEN);
			sd_like.setCornerRadius(5);

			final Button imageLike = new Button(getActivity());
			// imageLike.setLayoutParams(new LayoutParams(100, 50));
			imageLike.setText("Liked");
			imageLike.setTextColor(android.graphics.Color.GREEN);
			imageLike.setTextSize(18);
			imageLike.setTypeface(Montserrat_Bold);
			imageLike.setBackgroundDrawable(sd_like);
			imageLike.setX(30);
			imageLike.setY(80);
			imageLike.setAlpha(alphaValue);
			imageLike.setPadding(5, 5, 5, 5);
			myRelView.addView(imageLike);// 3rd view

			GradientDrawable sd_nops = (GradientDrawable) getActivity()
					.getResources().getDrawable(R.drawable.like_nops_back);
			// sd_nops.setColor(android.graphics.Color.RED);
			sd_nops.setStroke(2, android.graphics.Color.RED);
			sd_nops.setCornerRadius(5);

			final Button imagePass = new Button(getActivity());
			imagePass.setBackgroundColor(android.graphics.Color.TRANSPARENT);
			// imagePass.setLayoutParams(new LayoutParams(100, 50));
			imagePass.setTextSize(18);
			imagePass.setText("Nops");
			imagePass.setTextColor(android.graphics.Color.RED);
			imagePass.setBackgroundDrawable(sd_nops);
			imagePass.setTypeface(Montserrat_Bold);
			imagePass.setX((windowwidth - 150));
			imagePass.setY(80);
			imagePass.setRotation(45);
			imagePass.setAlpha(alphaValue);
			imagePass.setPadding(5, 5, 5, 5);
			myRelView.addView(imagePass);// 4 th view

			imageView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					x_cord = (int) event.getRawX();
					y_cord = (int) event.getRawY();

					final int X = (int) event.getRawX();
					final int Y = (int) event.getRawY();

					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						xd = X;
						yd = Y;
						RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) myRelView
								.getLayoutParams();
						_xDelta = X - lParams.leftMargin;
						_yDelta = Y - lParams.topMargin;
						break;
					case MotionEvent.ACTION_MOVE:
						x_cord = (int) event.getRawX();
						y_cord = (int) event.getRawY();

						myRelView.setX(X - _xDelta);
						myRelView.setY(Y - _yDelta);

						if (x_cord >= screenCenter) {
							myRelView
									.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
							if (x_cord > (screenCenter + (screenCenter / 2))) {
								imageLike.setAlpha(1);
								if (x_cord > (windowwidth - (screenCenter / 4))) {
									Likes = 2;
								} else {
									Likes = 0;
								}
							} else {
								Likes = 0;
								imageLike.setAlpha(0);
							}
							imagePass.setAlpha(0);
						} else {
							// rotate
							myRelView
									.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
							if (x_cord < (screenCenter / 2)) {
								imagePass.setAlpha(1);
								if (x_cord < screenCenter / 4) {

									Likes = 1;

								} else {
									Likes = 0;
								}
							} else {
								Likes = 0;
								imagePass.setAlpha(0);
							}

							imageLike.setAlpha(0);
						}

						break;
					case MotionEvent.ACTION_UP:

						x_cord = (int) event.getRawX();
						y_cord = (int) event.getRawY();

						imagePass.setAlpha(0);
						imageLike.setAlpha(0);

						if (Likes == 0) {
							myRelView.setX(0);
							myRelView.setY(0);
							myRelView.setRotation(0);
						} else if (Likes == 1) {
							imageindex = imageindex + 1;

							if (imageindex == MatchCount) {
								hideSwipeLayout();
							}

							matchesData = FindMatches.this.MachedataList
									.get(position);
							machedUserFaceBookid = matchesData.getFbId();
							AppLog.Log(TAG,
									"Event Status   machedUserFaceBookid  "
											+ machedUserFaceBookid);
							swipeviewlayout.removeView(myRelView);
							likeMatchedUser(Constant.isDisliked);
						} else if (Likes == 2) {
							imageindex = imageindex + 1;
							if (imageindex == MatchCount) {
								hideSwipeLayout();
							}
							int viewCount = swipeviewlayout.getChildCount();
							MatchesData matchesData = MachedataList
									.get(viewCount - 1);
							machedUserFaceBookid = matchesData.getFbId();
							swipeviewlayout.removeView(myRelView);
							likeMatchedUser(Constant.isLikde);
						}
						break;
					default:
						break;
					}
					return true;
				}
			});
			swipeviewlayout.addView(myRelView);
		}

		// set visible true if match user count is more than one
		if (MatchCount > 0) {
			// likedislikelayout.setVisibility(View.VISIBLE);
			setFullScreenMenuTouchEnable(false);
		}
	}

	private void setProfilePick(final ImageView userProfilImage,
			final int height, final int width) {
		final Ultilities mUltilities = new Ultilities();
		Picasso.with(getActivity()).load(preferences.getString(
				Constant.PREF_PROFILE_IMAGE_ONE, ""))
		.transform(new CircleTransform()).fit()
		.into(userProfilImage);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(profileImageHeightAndWidth[1], profileImageHeightAndWidth[0]);
		userProfilImage.setLayoutParams(layoutParams);

		/*new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("imageFind", preferences.getString(
						Constant.PREF_PROFILE_IMAGE_ONE, ""));
				final Bitmap bitmapimage = Utility.getBitmapFromURL(preferences
						.getString(Constant.PREF_PROFILE_IMAGE_ONE, ""));
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Bitmap cropedBitmap = null;
							ScalingUtilities mScalingUtilities = new ScalingUtilities();
							Bitmap mBitmap = null;
							if (bitmapimage != null) {
								cropedBitmap = mScalingUtilities
										.createScaledBitmap(bitmapimage, width,
												height, ScalingLogic.CROP);
								bitmapimage.recycle();
								mBitmap = mUltilities.getCircleBitmap(
										cropedBitmap, 1);
								cropedBitmap.recycle();
								userProfilImage.setImageBitmap(mBitmap);
							} else {

							}

						}
					});
				}

			}
		}).start();*/
	}

	/**
	 * responsible to find maches user profile from server
	 */
	private void findMatch() {
		Log.i(TAG, "*** Find Matches Called ***");
		new BackGroundTaskForFindMatch().execute();
	}

	private class BackGroundTaskForFindMatch extends
			AsyncTask<String, Void, Void> {
		private Ultilities mUltilities = new Ultilities();
		// private String sessionToken;

		// private SessionManager mSessionManager = new SessionManager(
		// getActivity());
		private String findMathchResponse;
		private List<NameValuePair> findMatchNameValuePairList;
		// private String deviceid;
		private String fbId;
		private FindMatchData mFindMatchData;
		private boolean success = true;

		@Override
		protected Void doInBackground(String... params) {
			try {
				// deviceid = /* "defoutlfortestin" */Ultilities
				// .getDeviceId(getActivity());
				// sessionToken = mSessionManager.getUserToken();
				fbId = preferences.getString(Constant.FACEBOOK_ID, "");
				String[] findMatchParamere = { fbId };
				findMatchNameValuePairList = mUltilities
						.getFindMatchParameter(findMatchParamere);
				// findMatchNameValuePairList
				// .add(new BasicNameValuePair("jewish",
				// preferences.getBoolean(Constant.PREF_JEWISH,
				// false) ? "yes" : "no"));
				String paramString = URLEncodedUtils.format(
						findMatchNameValuePairList, "utf-8");
				Log.e("Orignal URL", paramString + "");
				Log.e("url", Constant.findMatch_url);
				HttpRequest httpRequest = new HttpRequest();
				findMathchResponse=httpRequest.postData(Constant.findMatch_url,
						findMatchNameValuePairList);
				/*findMathchResponse = mUltilities.makeHttpRequest(
						Constant.findMatch_url, Constant.methodeName,
						findMatchNameValuePairList);*/
				AppLog.Log(TAG,
						"BackGroundTaskForFindMatch   doInBackground findMathchResponse "
								+ findMathchResponse);
				Gson gson = new Gson();
				mFindMatchData = gson.fromJson(findMathchResponse,
						FindMatchData.class);
				
				Log.i(TAG, "*** FIND RESPONSE :: " + findMathchResponse);
				Log.e("size", mFindMatchData.getMatches().size() + "//");
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				// mdialog.dismiss();
				if (success) {

					if (mFindMatchData.getErrNum().equals("21")
							&& mFindMatchData.getErrFlag().equals("1")) {

						// no match found

						Log.i(TAG, "**** no match found ****");

						swipeviewlayout.setVisibility(View.GONE);
						likedislikelayout.setVisibility(View.GONE);
						setFullScreenMenuTouchEnable(true);
						messagetextview.setText(mFindMatchData
								.getErorrMassage());
						Constant.isMatchedFound = false;

					} else if (mFindMatchData.getErrNum().equals("9")
							&& mFindMatchData.getErrFlag().equals("1")) {
						// invalid session token
						Log.i(TAG, "**** invalid session token ****");
						ErrorMessage("Alert", mFindMatchData.getErorrMassage());
						Constant.isMatchedFound = false;
					} else if (mFindMatchData.getErrNum().equals("19")
							&& mFindMatchData.getErrFlag().equals("1")) {
						// invalid session token
						Log.i(TAG, "**** invalid session token ****");
						Toast.makeText(getActivity(),
								mFindMatchData.getErorrMassage(),
								Toast.LENGTH_SHORT).show();
						ErrorMessage("Alert", mFindMatchData.getErorrMassage());
						Constant.isMatchedFound = false;
					} else if (mFindMatchData.getErrNum().equals("31")
							&& mFindMatchData.getErrFlag().equals("1")) {
						// your session is expire need update session
						// getUpdateSessionToken();
					} else {

						MachedataList = mFindMatchData.getMatches();

						Log.i(TAG, "**** Marches Found MachedataList ****");

						int pos = -1;
						for (int i = 0; i < MachedataList.size(); i++) {
							MatchesData data = MachedataList.get(i);
							if (data.getFbId().equals(
									preferences.getString(Constant.FACEBOOK_ID,
											""))) {
								pos = i;
								break;
							}
						}
						// if (pos >= 0) {
						// MachedataList.remove(pos);
						// }

						addView(MachedataList);
						// dwonLoadImage(numberOfImageDownload);

					}
				} else {

					messagetextview
							.setText("No new potential pairs near you right now");
					// ErrorMessageRequesTimeOut("Alert ", "Request timeout");
					Constant.isMatchedFound = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AppLog.handleException(
						TAG
								+ " BackGroundTaskForFindMatch  onPostExecute Exception ",
						e);
				messagetextview
						.setText("No new potential pairs near you right now");
				// ErrorMessageRequesTimeOut("Alert ", "Request timeout");
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mdialog=mUltilities.GetProcessDialog(getActivity());
			// mdialog.setCancelable(false);
			// mdialog.show();
		}

	}

	// private void dwonLoadImage(int numberOfImageDownload) {
	// // logDebug("dwonLoadImage");
	// try {
	// Constant.isMatchedFound = true;
	// if (MachedataList != null && MachedataList.size() > 0) {
	// String imageDownload[] = { "" + numberOfImageDownload };
	// new BackGroundTaskForDownLoadMatcheduserImage()
	// .execute(imageDownload);
	// } else {
	// swipeviewlayout.setVisibility(View.GONE);
	// likedislikelayout.setVisibility(View.GONE);
	// setFullScreenMenuTouchEnable(true);
	// noMatchFound.setVisibility(View.VISIBLE);
	// amimagetedview.setVisibility(View.VISIBLE);
	// messagetextview
	// .setText("No new potential pairs near you right now");
	// amimagetedview.startAnimation(anime);
	// // invitebuttonlayout.setVisibility(View.VISIBLE);
	// }
	// } catch (Exception e) {
	// AppLog.handleException(TAG + " dwonLoadImage   Exception ", e);
	// }
	// }

	// private class BackGroundTaskForDownLoadMatcheduserImage extends
	// AsyncTask<String, Void, Void> {
	// private Bitmap mBitmap1;
	// private Bitmap mBitmap2;
	// private ScalingUtilities mScalingUtilities = new ScalingUtilities();
	// private int index;
	//
	// protected Void doInBackground(String... params) {
	//
	// int numberOfImageDownload = Integer.parseInt(params[0]);
	//
	// try {
	// if (downloadcallfirsttime) {
	// //
	// // if (numberOfImageDownload<MachedataList.size())
	// // {
	// // for (int i = numberOfImageDownload-3; i
	// // <numberOfImageDownload ; i++)
	// // {
	// // index=i;
	// //
	// // mBitmap1
	// // =Utility.getBitmap(MachedataList.get(i).getpPic().replaceAll(" ",
	// // "%20"));
	// // final Bitmap scaleBitmap=
	// // Bitmap.createScaledBitmap(mBitmap1,
	// // matchUserHeightAndWidth[1], matchUserHeightAndWidth[0],
	// // true);
	// // MachedataList.get(i).setmBitmap(scaleBitmap);
	// //
	// // mBitmap1.recycle();
	// //
	// //
	// // }
	// // }
	// // else if (numberOfImageDownload-3<MachedataList.size())
	// // {
	// // for (int i = numberOfImageDownload-3; i
	// // <MachedataList.size(); i++)
	// // {
	// // index=i;
	// //
	// // mBitmap1
	// // =Utility.getBitmap(MachedataList.get(i).getpPic().replaceAll(" ",
	// // "%20"));
	// // final Bitmap scaleBitmap=
	// // Bitmap.createScaledBitmap(mBitmap1,
	// // matchUserHeightAndWidth[1], matchUserHeightAndWidth[0],
	// // true);
	// // MachedataList.get(i).setmBitmap(scaleBitmap);
	// //
	// // mBitmap1.recycle();
	// //
	// //
	// // }
	// // }
	// // else
	// // {
	// //
	// // }
	// //
	// } else {
	// //
	// //
	// // if (numberOfImageDownload<MachedataList.size())
	// // {
	// // for (int i = numberOfImageDownload-1; i
	// // <numberOfImageDownload ; i++)
	// // {
	// // index=i;
	// //
	// // mBitmap1
	// // =Utility.getBitmap(MachedataList.get(i).getpPic().replaceAll(" ",
	// // "%20"));
	// // if (mBitmap1!=null)
	// // {
	// // final Bitmap scaleBitmap=
	// // Bitmap.createScaledBitmap(mBitmap1,
	// // matchUserHeightAndWidth[1], matchUserHeightAndWidth[0],
	// // true);
	// // MachedataList.get(i).setmBitmap(scaleBitmap);
	// //
	// // mBitmap1.recycle();
	// // }
	// //
	// //
	// //
	// // }
	// // }
	// // else if (numberOfImageDownload-1<MachedataList.size())
	// // {
	// // for (int i = numberOfImageDownload-1; i
	// // <MachedataList.size(); i++)
	// // {
	// // index=i;
	// // mBitmap1
	// // =Utility.getBitmap(MachedataList.get(i).getpPic().replaceAll(" ",
	// // "%20"));
	// // if (mBitmap1!=null)
	// // {
	// // final Bitmap scaleBitmap=
	// // Bitmap.createScaledBitmap(mBitmap1,
	// // matchUserHeightAndWidth[1], matchUserHeightAndWidth[0],
	// // true);
	// // MachedataList.get(i).setmBitmap(scaleBitmap);
	// //
	// // mBitmap1.recycle();
	// // }
	// //
	// //
	// //
	// //
	// // }
	// // }
	// // else
	// // {
	// //
	// // }
	// //
	//
	// }
	//
	// } catch (OutOfMemoryError e) {
	// AppLog.handleException(
	// TAG
	// +
	// " BackGroundTaskForDoanLoadMatcheduserImage   OutOfMemoryError  doInBackground + "
	// + e.getMessage(), null);
	// } catch (Exception e) {
	// AppLog.handleException(
	// TAG
	// +
	// " BackGroundTaskForDoanLoadMatcheduserImage   Exception  doInBackground",
	// e);
	// }
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// // logDebug("BackGroundTaskForDoanLoadMatcheduserImage  onPostExecute");
	// if (downloadcallfirsttime) {
	// addView(MachedataList);
	// } else {
	// Constant.isMatchedFound = false;
	// try {
	// // if
	// // (MachedataList.get(newdownloadedImageIndex).getmBitmap()
	// // != null) {
	// // //
	// //
	// swipeimageviewsecond.setImageBitmap(MachedataList.get(newdownloadedImageIndex).getmBitmap());
	// // //
	// // firstImageviewsecondprogressbar.setVisibility(View.GONE);
	// // //
	// //
	// swipeimageview.setImageBitmap(MachedataList.get(newdownloadedImageIndex).getmBitmap());
	// // // firstImageviewprogressbar.setVisibility(View.GONE);
	// // }
	//
	// } catch (Exception e) {
	// }
	//
	// }
	//
	// }
	//
	// }

	private void showSwipeView() {
		anime.cancel();
		anime.reset();

		amimagetedview.clearAnimation();
		swipeviewlayout.setVisibility(View.VISIBLE);
		likedislikelayout.setVisibility(View.VISIBLE);
		noMatchFound.setVisibility(View.GONE);
		setFullScreenMenuTouchEnable(false);
		amimagetedview.setVisibility(View.GONE);
		Constant.isMatchedFound = false;
	}

	// @Override
	// public boolean onTouch(View view, MotionEvent event) {
	// x_cord = (int) event.getRawX();
	// y_cord = (int) event.getRawY();
	//
	// //view.setX(x_cord - screenCenter + 40);
	// //view.setY(y_cord - 150);
	// switch (event.getAction())
	// {
	// case MotionEvent.ACTION_DOWN:
	// break;
	// case MotionEvent.ACTION_MOVE:
	// x_cord = (int) event.getRawX();
	// y_cord = (int) event.getRawY();
	// // view.setX(x_cord - screenCenter + 40);
	// // view.setY(y_cord - 150);
	// if (x_cord >= screenCenter)
	// {
	// view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
	// if (x_cord > (screenCenter + (screenCenter / 2)))
	// {
	// //imageLike.setAlpha(1);
	// if (x_cord > (windowwidth - (screenCenter / 4)))
	// {
	// Likes = 2;
	// } else {
	// Likes = 0;
	// }
	// }
	// else
	// {
	// Likes = 0;
	// // imageLike.setAlpha(0);
	// }
	// //imagePass.setAlpha(0);
	// } else
	// {
	// // rotate
	// view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
	// if (x_cord < (screenCenter / 2))
	// {
	// //imagePass.setAlpha(1);
	// if (x_cord < screenCenter / 4)
	// {
	// Likes = 1;
	// } else {
	// Likes = 0;
	// }
	// } else
	// {
	// Likes = 0;
	// //imagePass.setAlpha(0);
	// }
	//
	// //imageLike.setAlpha(0);
	// }
	//
	// break;
	// case MotionEvent.ACTION_UP:
	// x_cord = (int) event.getRawX();
	// y_cord = (int) event.getRawY();
	//
	// Log.e("X Point", "" + x_cord + " , Y " + y_cord);
	// //imagePass.setAlpha(0);
	// //imageLike.setAlpha(0);
	//
	// if (Likes == 0)
	// {
	// Log.e("Event Status", "Nothing");
	// view.setX(40);
	// view.setY(40);
	// view.setRotation(0);
	// } else if (Likes == 1)
	// {
	// Log.e("Event Status", "Passed");
	// swipeviewlayout.removeView(view);
	// } else if (Likes == 2)
	// {
	//
	// Log.e("Event Status", "Liked");
	// swipeviewlayout.removeView(view);
	// }
	// break;
	// default:
	// break;
	// }
	// return true;
	// }

	// private void moveView(float x, float y) {
	// int viewCurrentlocation[] = new int[2];
	// // imageviealayout.getLocationInWindow(viewCurrentlocation);
	//
	// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// params.leftMargin = Math.round(x);
	// params.topMargin = Math.round(y);
	// // imageviealayout.setLayoutParams(params);
	// // swipeviewlayout.invalidate();
	//
	// }

	// @Override
	// public boolean onDrag(View layoutview, DragEvent dragevent) {
	// int action = dragevent.getAction();
	// switch (action) {
	// case DragEvent.ACTION_DRAG_STARTED:
	//
	// // Log.d(LOGCAT, "Drag event started");
	// break;
	// case DragEvent.ACTION_DRAG_ENTERED:
	// // Log.d(LOGCAT, "Drag event entered into "+layoutview.toString());
	// break;
	// case DragEvent.ACTION_DRAG_EXITED:
	// // Log.d(LOGCAT, "Drag event exited from "+layoutview.toString());
	// break;
	// case DragEvent.ACTION_DROP:
	// // Log.d(LOGCAT, "Dropped");
	// View view = (View) dragevent.getLocalState();
	//
	// ViewGroup owner = (ViewGroup) view.getParent();
	//
	// // owner.removeView(view);
	// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// // lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
	// // lp.addRule(RelativeLayout.CENTER_VERTICAL);
	// // RelativeLayout container = (RelativeLayout) layoutview;
	// swipeviewlayout.addView(view, lp);
	// view.setVisibility(View.VISIBLE);
	// break;
	// case DragEvent.ACTION_DRAG_ENDED:
	// // Log.d(LOGCAT, "Drag ended");
	// break;
	// default:
	// break;
	// }
	// return true;
	// }

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// SessionManager mSessionManager = new SessionManager(
						// getActivity());
						// mSessionManager.logoutUser();
						// Intent intent = new Intent(getActivity(),
						// LoginUsingFacebook.class);
						// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// startActivity(intent);
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	// private void ErrorMessageRequesTimeOut(String title, String message) {
	// try {
	// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	// builder.setTitle(title);
	// builder.setMessage(message);
	//
	// builder.setPositiveButton(
	// getResources().getString(R.string.okbuttontext),
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// dialog.dismiss();
	// }
	// });
	//
	// AlertDialog alert = builder.create();
	// alert.setCancelable(false);
	// alert.show();
	// } catch (Exception e) {
	// AppLog.handleException(TAG
	// + " ErrorMessageRequesTimeOut  Exception ", e);
	// }
	// }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.matchedUserInfoButton) {

			int viewCount = swipeviewlayout.getChildCount();
			MatchesData matchesData = MachedataList.get(viewCount - 1);
			int selectedImageIndex = viewCount - 1;
			AppLog.Log(TAG, "Selected Index:---->" + selectedImageIndex);
			machedUserFaceBookid = matchesData.getFbId();
			SessionManager mSessionManager = new SessionManager(getActivity());
			mSessionManager.setMatchedUserFacebookId(machedUserFaceBookid);
			mSessionManager.setImageIndexForLikeDislike(selectedImageIndex);
			mSessionManager.setLastDownloadImageIndex(numberOfImageDownload);
			Intent mIntent = new Intent(getActivity(), MatChedUserProfile.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constant.isFromChatScreen, false);
			mIntent.putExtra("match_data", matchesData);
			mIntent.putExtras(bundle);
			startActivity(mIntent);

			// int viewCount = swipeviewlayout.getChildCount();
			// MatchesData matchesData = MachedataList.get(viewCount - 1);
			// int selectedImageIndex = viewCount - 1;
			// AppLog.Log(TAG, "Selected Index:---->" + selectedImageIndex);
			// machedUserFaceBookid = matchesData.getFbId();
			// SessionManager mSessionManager = new
			// SessionManager(getActivity());
			// mSessionManager.setMatchedUserFacebookId(machedUserFaceBookid);
			// mSessionManager.setImageIndexForLikeDislike(selectedImageIndex);
			// mSessionManager.setLastDownloadImageIndex(numberOfImageDownload);
			// Intent mIntent = new Intent(getActivity(),
			// MatChedUserProfile.class);
			// Bundle bundle = new Bundle();
			// bundle.putBoolean(Constant.isFromChatScreen, false);
			// mIntent.putExtras(bundle);
			// startActivity(mIntent);
		}
		if (v.getId() == R.id.imageviealayout) {
			// logDebug("onClick  imageviewlayout");
			SessionManager mSessionManager = new SessionManager(getActivity());
			mSessionManager.setMatchedUserFacebookId(machedUserFaceBookid);
			mSessionManager.setImageIndexForLikeDislike(imageindex);
			Intent mIntent = new Intent(getActivity(), MatChedUserProfile.class);
			startActivity(mIntent);
		}
		if (v.getId() == R.id.likeButton) {
			likeButton.setEnabled(false);
			int viewCount = swipeviewlayout.getChildCount();
			if (viewCount != 0) {
				MatchesData matchesData = MachedataList.get(viewCount - 1);
				machedUserFaceBookid = matchesData.getFbId();
				likeMatchedUser(Constant.isLikde);

				int removeViewindex = viewCount - 1;
				RelativeLayout animatedview = (RelativeLayout) swipeviewlayout
						.getChildAt(removeViewindex);

				if (animatedview != null) {
					AppLog.Log(TAG, "animatedview ");
				} else {
					AppLog.Log(TAG, "animatedview is null ");
				}

				rotedandRansletAnimation(true, removeViewindex, animatedview);
				imageindex = imageindex + 1;
			} else {
				// invitebuttonlayout.setVisibility(View.VISIBLE);
				swipeviewlayout.setVisibility(View.GONE);
				likedislikelayout.setVisibility(View.GONE);
				setFullScreenMenuTouchEnable(true);
				noMatchFound.setVisibility(View.VISIBLE);
				amimagetedview.setVisibility(View.VISIBLE);
				messagetextview
						.setText("No new potential pairs near you right now");
				amimagetedview.startAnimation(anime);
				// invitebuttonlayout.setVisibility(View.VISIBLE);

			}
			// rotedandRansletAnimation(true);
		}

		if (v.getId() == R.id.dislikeButton) {
			dislikeButton.setEnabled(false);

			// numberOfImageDownload = numberOfImageDownload + 1;
			int viewCount = swipeviewlayout.getChildCount();

			if (viewCount != 0) {
				MatchesData matchesData = MachedataList.get(viewCount - 1);
				machedUserFaceBookid = matchesData.getFbId();
				likeMatchedUser(Constant.isDisliked);

				int removeViewindex = viewCount - 1;
				RelativeLayout animatedview = (RelativeLayout) swipeviewlayout
						.getChildAt(removeViewindex);

				if (animatedview != null) {
					AppLog.Log(TAG, "animatedview ");
				} else {
					AppLog.Log(TAG, "animatedview is null ");
				}

				imageindex = imageindex + 1;
				rotedandRansletAnimation(false, removeViewindex, animatedview);
			} else {
				swipeviewlayout.setVisibility(View.GONE);
				likedislikelayout.setVisibility(View.GONE);
				setFullScreenMenuTouchEnable(true);
				noMatchFound.setVisibility(View.VISIBLE);
				amimagetedview.setVisibility(View.VISIBLE);
				messagetextview
						.setText("No new potential pairs near you right now");
				amimagetedview.startAnimation(anime);
			}
		}
		if (v.getId() == R.id.inviteButton) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}

	}

	private void rotedandRansletAnimation(boolean isLiked, final int viewindex,
			RelativeLayout relativeLayout) {
		AnimationSet snowMov1 = null;
		// logDebug("rotedandRansletAnimation  isLiked "+isLiked);
		if (isLiked) {

			snowMov1 = new AnimationSet(true);
			RotateAnimation rotate1 = new RotateAnimation(0, 20,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			rotate1.setStartOffset(50);
			rotate1.setDuration(1000);
			snowMov1.addAnimation(rotate1);
			TranslateAnimation trans1 = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.5f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			trans1.setDuration(1000);
			snowMov1.addAnimation(trans1);
		} else {
			snowMov1 = new AnimationSet(true);
			RotateAnimation rotate1 = new RotateAnimation(0, -20,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			rotate1.setStartOffset(50);
			rotate1.setDuration(1000);
			snowMov1.addAnimation(rotate1);
			TranslateAnimation trans1 = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.5f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			trans1.setDuration(1000);
			// snowMov1.setFillAfter(true);
			snowMov1.addAnimation(trans1);
		}

		// AnimationSet s = new AnimationSet(false);//false mean dont share
		// interpolators
		// s.addAnimation(traintween);
		// s.addAnimation(trainfad);
		// if (relativeLayout == null) {
		// return;
		// }
		relativeLayout.startAnimation(snowMov1);
		snowMov1.setAnimationListener(new AnimationListener() {

			int secondIndex = imageindex + 1;

			@Override
			public void onAnimationStart(Animation animation) {
				// changeImageFromOnstart(imageindex,secondIndex);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dislikeButton.setEnabled(true);
				likeButton.setEnabled(true);
				swipeviewlayout.removeViewAt(viewindex);
				if (viewindex == 0) {
					hideSwipeLayout();
				} else {

				}
				// logDebug("setAnimationListener  onAnimationEnd");
				// imageviealayout.setVisibility(View.INVISIBLE);
				// if (imageindex<MachedataList.size())
				// {
				// MachedataList.get(imageindex-1).getmBitmap().recycle();
				// }

				// changeImage(imageindex,secondIndex++);

			}
		});

	}

	// private void changeImageFromOnstart(int firesIndex, int secondindex) {
	// // logDebug("changeImageFromOnstart  firesIndex  "+firesIndex);
	// // logDebug("changeImageFromOnstart  secondindex  "+secondindex);
	// //
	// logDebug("changeImageFromOnstart  list size is   "+MachedataList.size());
	// newdownloadedImageIndex = firesIndex;
	// if (firesIndex < MachedataList.size()) {
	//
	// // firstImageviewsecondprogressbar.setVisibility(View.VISIBLE);
	//
	// // Picasso.with(getActivity()) //
	// // .load(MachedataList.get(firesIndex).getpPic()) //
	// // /*.placeholder(R.drawable.placeholder)*/ //
	// // .error(R.drawable.error) //
	// // .resize(matchUserHeightAndWidth[1], matchUserHeightAndWidth[0])
	// // //
	// // .into(swipeimageviewsecond,this);
	// //
	// // imagevewsecondlayout.setVisibility(View.VISIBLE);
	// // if (MachedataList.get(firesIndex).getmBitmap()!=null)
	// // {
	// //
	// swipeimageviewsecond.setImageBitmap(MachedataList.get(firesIndex).getmBitmap());
	// // firstImageviewsecondprogressbar.setVisibility(View.GONE);
	// // }
	// // else
	// // {
	// // swipeimageviewsecond.setImageBitmap(null);
	// // firstImageviewsecondprogressbar.setVisibility(View.VISIBLE);
	// // }
	//
	// //
	// matcheduserNamesecond.setText(""+MachedataList.get(firesIndex).getFirstName());
	// //
	// matcheduserAgesecond.setText(""+MachedataList.get(firesIndex).getAge());
	// //
	// nubrtogmutullikessecond.setText(""+MachedataList.get(firesIndex).getSharedLikes());
	// //
	// nuberofphotosecond.setText(""+MachedataList.get(firesIndex).getImgCnt());
	// // nuberofphoto.setText(""+MachedataList.get(firesIndex).getImgCnt());
	//
	// } else if (firesIndex == MachedataList.size()) {
	//
	// // imagevewsecondlayout.setVisibility(View.GONE);
	//
	// } else if (firesIndex > MachedataList.size()) {
	// hideSwipeLayout();
	// /*
	// * swipeviewlayout.setVisibility(View.GONE);
	// * noMatchFound.setVisibility(View.VISIBLE);
	// * amimagetedview.setVisibility(View.VISIBLE);
	// * amimagetedview.startAnimation(anime);
	// * messagetextview.setText("No new potential pairs near you right now"
	// * ); invitebuttonlayout.setVisibility(View.VISIBLE); //
	// * anime.start(); // anime.startNow(); // anime.reset();
	// * //circleimageview.startAnimation(anime); imageindex=0;
	// */
	// }
	//
	// }

	// /*
	// * private interface CallBack { public void imgageLoadFinish(); }
	// *
	// * CallBack mCallBack=new CallBack()
	// */
	// private void changeImage(int firesIndex, int secondindex) {
	// // logDebug("changeImage  firesIndex  "+firesIndex);
	// // logDebug("changeImage  secondindex  "+secondindex);
	// // logDebug("changeImage  list size is   "+MachedataList.size());
	// if (firesIndex < MachedataList.size()
	// && secondindex < MachedataList.size()) {
	// try {
	// // swipeimageview.setImageDrawable(null);
	// // MachedataList.get(firesIndex-1).getmBitmap().recycle();
	// } catch (Exception e) {
	// AppLog.handleException(TAG + " changeImage  Exception  ", e);
	// }
	//
	// if (firesIndex == 0) {
	//
	// //
	// swipeimageview.setImageBitmap(MachedataList.get(firesIndex).getmBitmap());
	// // firstImageviewprogressbar.setVisibility(View.VISIBLE);
	// //
	// matcheduserName.setText(""+MachedataList.get(firesIndex).getFirstName());;
	// // matcheduserAge.setText(""+MachedataList.get(firesIndex).getAge());
	// //
	// nubrtogmutullikes.setText(""+MachedataList.get(firesIndex).getSharedLikes());
	// // nuberofphoto.setText(""+MachedataList.get(firesIndex).getImgCnt());
	// machedUserFaceBookid = MachedataList.get(firesIndex).getFbId();
	//
	// // Trigger the download of the URL asynchronously into the image
	// // view.
	//
	// // Picasso.with(getActivity()) //
	// // .load(MachedataList.get(firesIndex).getpPic()) //
	// // /* .placeholder(R.drawable.placeholder)*/ //
	// // .error(R.drawable.error) //
	// // .resize(matchUserHeightAndWidth[1],
	// // matchUserHeightAndWidth[0]) //
	// // .into(swipeimageview,this);
	//
	// // imagevewsecondlayout.setVisibility(View.VISIBLE);
	// // //
	// //
	// swipeimageviewsecond.setImageBitmap(MachedataList.get(secondindex).getmBitmap());
	// // Picasso.with(getActivity()) //
	// // .load(MachedataList.get(secondindex).getpPic()) //
	// // /*.placeholder(R.drawable.placeholder)*/ //
	// // .error(R.drawable.error) //
	// // .resize(matchUserHeightAndWidth[1],
	// // matchUserHeightAndWidth[0]) //
	// // .into(swipeimageviewsecond,this);
	//
	// // firstImageviewsecondprogressbar.setVisibility(View.VISIBLE);
	// //
	// matcheduserNamesecond.setText(""+MachedataList.get(secondindex).getFirstName());
	// //
	// matcheduserAgesecond.setText(""+MachedataList.get(secondindex).getAge());
	// //
	// nubrtogmutullikessecond.setText(""+MachedataList.get(secondindex).getSharedLikes());
	// //
	// nuberofphotosecond.setText(""+MachedataList.get(secondindex).getImgCnt());
	// // nuberofphoto.setText(""+MachedataList.get(secondindex).getImgCnt());
	//
	// } else {
	// //
	// swipeimageview.setImageBitmap(MachedataList.get(firesIndex).getmBitmap());
	// // Picasso.with(getActivity()) //
	// // .load(MachedataList.get(firesIndex).getpPic()) //
	// // /*.placeholder(R.drawable.placeholder)*/ //
	// // .error(R.drawable.error) //
	// // .resize(matchUserHeightAndWidth[1],
	// // matchUserHeightAndWidth[0]) //
	// // .into(swipeimageview,this);
	// // //firstImageviewprogressbar.setVisibility(View.VISIBLE);
	// //
	// matcheduserName.setText(""+MachedataList.get(firesIndex).getFirstName());;
	// // matcheduserAge.setText(""+MachedataList.get(firesIndex).getAge());
	// //
	// nubrtogmutullikes.setText(""+MachedataList.get(firesIndex).getSharedLikes());
	// // nuberofphoto.setText(""+MachedataList.get(firesIndex).getImgCnt());
	// // machedUserFaceBookid=MachedataList.get(firesIndex).getFbId();
	// }
	//
	// } else if (firesIndex < MachedataList.size()) {
	//
	// // Picasso.with(getActivity()) //
	// // .load(MachedataList.get(firesIndex).getpPic()) //
	// // /*.placeholder(R.drawable.placeholder) *///
	// // .error(R.drawable.error) //
	// // .resize(matchUserHeightAndWidth[1], matchUserHeightAndWidth[0])
	// // //
	// // .into(swipeimageview,this);
	//
	// // newdownloadedImageIndex=firesIndex;
	// // //imagevewsecondlayout.setVisibility(View.GONE);
	// // if (MachedataList.get(firesIndex).getmBitmap()!=null)
	// // {
	// //
	// swipeimageview.setImageBitmap(MachedataList.get(firesIndex).getmBitmap());
	// // firstImageviewprogressbar.setVisibility(View.GONE);
	// // }
	// // else
	// // {
	// // swipeimageview.setImageBitmap(null);
	// // firstImageviewprogressbar.setVisibility(View.VISIBLE);
	// // }
	//
	// //
	// matcheduserName.setText(""+MachedataList.get(firesIndex).getFirstName());;
	// // matcheduserAge.setText(""+MachedataList.get(firesIndex).getAge());
	// //
	// nubrtogmutullikes.setText(""+MachedataList.get(firesIndex).getSharedLikes());
	// // nuberofphoto.setText(""+MachedataList.get(firesIndex).getImgCnt());
	// // machedUserFaceBookid=MachedataList.get(firesIndex).getFbId();
	// } else {
	// hideSwipeLayout();
	// }
	//
	// }

	private void hideSwipeLayout() {
		swipeviewlayout.setVisibility(View.GONE);
		likedislikelayout.setVisibility(View.GONE);
		setFullScreenMenuTouchEnable(true);
		noMatchFound.setVisibility(View.VISIBLE);
		amimagetedview.setVisibility(View.VISIBLE);
		amimagetedview.startAnimation(anime);
		messagetextview.setText("No new potential pairs near you right now");
		// invitebuttonlayout.setVisibility(View.VISIBLE);
		// anime.start();
		// anime.startNow();
		// circleimageview.startAnimation(anime);
		// anime.cancel();
		imageindex = 0;
		MatchCount = 0;
		ConnectionDetector connectionDetector = new ConnectionDetector(
				getActivity());

		if (connectionDetector.isConnectingToInternet()) {
			// if (isFristTimeLogin()) {
			// Editor editor = preferences.edit();
			// editor.putBoolean(Constant.PREF_IS_FIRST_TIME, false);
			// editor.commit();
			// showJEWISHDialog();
			// } else {
			findMatch();
			// }
		} else {
			Ultilities ultilities = new Ultilities();
			ultilities.displayMessageAndExit(getActivity(), "Alert",
					" working internet connection required");

		}
	}

	@Override
	public void onError() {
		imageDownloadingCount = imageDownloadingCount + 1;
		AppLog.Log(TAG, "call back  onError  imageDownloadingCount "
				+ imageDownloadingCount);
		AppLog.Log(TAG, "call back onError    MatcheCount " + MatchCount);
		if (imageDownloadingCount == MatchCount) {
			showSwipeView();
			AppLog.Log(TAG, "call back onError   show swipes view ");
		} else {
			// downloading images please waiting..............
			AppLog.Log(TAG,
					"call back downloading images please waiting..............");
		}
	}

	@Override
	public void onSuccess() {
		imageDownloadingCount = imageDownloadingCount + 1;
		AppLog.Log(TAG, "call back   onSuccess   imageDownloadingCount "
				+ imageDownloadingCount);
		AppLog.Log(TAG, "call back   onSuccess   MatcheCount " + MatchCount);

		if (imageDownloadingCount == MatchCount) {
			showSwipeView();
			AppLog.Log(TAG, "call back   onSuccess  show image  ");
		} else {
			// downloading images please waiting..............
			AppLog.Log(TAG,
					"call back   downloading images please waiting..............");
		}
		// firstImageviewsecondprogressbar.setVisibility(View.GONE);
		// firstImageviewprogressbar.setVisibility(View.GONE);

	}

	private void likeMatchedUser(String action) {
		Log.e(TAG, "likeMatchedUser");

		// SessionManager mSessionManager = new SessionManager(getActivity());
		// String sessionToke = mSessionManager.getUserToken();
		// String devieceId = Ultilities.getDeviceId(getActivity());
		String MatchedUserFacebookId = machedUserFaceBookid;
		String fbId = preferences.getString(Constant.FACEBOOK_ID, "");
		String userAction = action;

		if (MatchedUserFacebookId != null && MatchedUserFacebookId.length() > 0) {
			String[] params = { fbId, MatchedUserFacebookId, userAction };
			Log.e("params", fbId+"-"+MatchedUserFacebookId+"-"+ userAction );
			// logDebug("likeMatchedUser MatchedUserFacebookId  "+MatchedUserFacebookId);

			new BackGroundTaskForInviteAction().execute(params);
		} else {
			// logDebug("likeMatchedUser MatchedUserFacebookId  "+MatchedUserFacebookId);
		}
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
				// Log.e("url", Constant.inviteaction_url);
				inviteactionparamlist = mUltilities
						.getInviteActionParameter(params);

				// for (int i = 0; i < inviteactionparamlist.size(); i++)
				// Log.e(inviteactionparamlist.get(i).getName(),
				// inviteactionparamlist.get(i).getValue());

				// logDebug("likeMatchedUser BackGroundTaskForInviteAction doInBackground inviteactionparamlist"+inviteactionparamlist);
				/*inviteActionResponse = mUltilities.makeHttpRequest(
						Constant.inviteaction_url, Constant.methodeName,
						inviteactionparamlist);*/
				
				HttpRequest httpRequest = new HttpRequest();
				inviteActionResponse=httpRequest.postData(Constant.inviteaction_url,
						inviteactionparamlist);

				// ent_user_fbid=869660346462629&ent_invitee_fbid=1649310368649687&ent_user_action=1
				// {"errNum":"37","errFlag":"1","errMsg":"Server Error! Please try again after sometime!"}

				Log.e("response", inviteActionResponse);
				// logDebug("likeMatchedUser BackGroundTaskForInviteAction   doInBackground inviteActionResponse "+inviteActionResponse);
				Gson gson = new Gson();
				mActionData = gson.fromJson(inviteActionResponse,
						InviteActionData.class);
				// logDebug("likeMatchedUser BackGroundTaskForInviteAction   doInBackground mActionData "+mActionData);
				if (mActionData != null) {
					// logDebug("BackGroundTaskForInviteAction   doInBackground   error no"+mActionData.getErrNum());
					if (mActionData.getErrNum() == 55) {
						File appDirectory = mUltilities
								.createAppDirectoy(getResources().getString(
										R.string.appdirectory));
						// logDebug("BackgroundTaskForFindLikeMatched   doInBackground appDirectory "+appDirectory);
						File _picDir = new File(appDirectory,
								getResources().getString(
										R.string.imagedirematchuserdirectory));

						File imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								mActionData.getuName() + mActionData.getuFbId()
										+ ".jpg");
						// logDebug("BackGroundTaskForUserProfile doInBackground imageFile is profile "+imageFile.isFile());
						Utility.addBitmapToSdCardFromURL(mActionData.getpPic()
								.replaceAll(" ", "%20"), imageFile);
						ArrayList<LikeMatcheddataForListview> matchlist = new ArrayList<LikeMatcheddataForListview>();
						LikeMatcheddataForListview objMatchData = new LikeMatcheddataForListview();
						objMatchData.setFacebookid(mActionData.getuFbId());
						objMatchData.setFilePath(imageFile.getAbsolutePath());
						objMatchData.setUserName(mActionData.getuName());
						objMatchData.setFlag("3");
						objMatchData.setImageUrl(mActionData.getpPic());
						// objMatchData.setmBitmap(mActionData.getuFbId());
						Ultilities mUltilitie = new Ultilities();

						String curenttime = mUltilitie.getCurrentGmtTime();
						objMatchData.setladt(curenttime);
						matchlist.add(objMatchData);
						DatabaseHandler objDatabaseHandler = new DatabaseHandler(
								getActivity());
						SessionManager mSessionManager = new SessionManager(
								getActivity());
						String userFaceBookid = mSessionManager.getFacebookId();
						boolean flagSuccess = objDatabaseHandler
								.insertMatchList(matchlist, userFaceBookid);
						Log.i("",
								"BackGroundTaskForInviteAction flagSuccess....."
										+ flagSuccess);
						// matcheddataForListview.setFilePath(imageFile.getAbsolutePath());
					} else {

					}
				}

			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mDialog=mUltilities.GetProcessDialog(getActivity());
			// mDialog.setMessage("Please wait..");
			// mDialog.setCancelable(true);
			// mDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// mDialog.dismiss();
			try {
				if (mActionData != null) {
					if (mActionData.getErrNum() == 29
							&& mActionData.getErrFlag() == 0) {
						SessionManager mSessionManager = new SessionManager(
								getActivity());
						mSessionManager.isInviteActionSucess(true);
					} else if (mActionData.getErrNum() == 37
							&& mActionData.getErrFlag() == 1) {
						ErrorMessage("alrte", mActionData.getErrMsg());
					} else if (mActionData.getErrNum() == 55) {
						Intent matchIntent = new Intent(getActivity(),
								MatchFoundActivity.class);
						matchIntent.putExtra("SENDER_FB_ID",
								mActionData.getuFbId());
						matchIntent.putExtra("SENDER_USERNAME",
								mActionData.getuName());
						startActivity(matchIntent);
					} else {
						ErrorMessage("alrte",
								"sorry Server Error! Please try again after sometime!");
					}
					// if (mActionData.getErrNum() == 55) {
					// Intent matchIntent = new Intent(getActivity(),
					// MatchFoundActivity.class);
					// matchIntent
					// .putExtra("SENDER_FB_ID", mActionData.getuFbId());
					// matchIntent.putExtra("SENDER_USERNAME",
					// mActionData.getuName());
					// startActivity(matchIntent);
					// } else {
					//
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			// logDebug("onResume");
			if (cd.isConnectingToInternet()) {
				setProfilePick(userProfilImage, profileImageHeightAndWidth[0],
						profileImageHeightAndWidth[1]);
			} else {
				Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			SessionManager mSessionManager = new SessionManager(getActivity());
			if (mSessionManager.getIsInviteActionSucess()) {
				mSessionManager.isInviteActionSucess(false);
				imageindex = mSessionManager.getImageIndexForLikeDislike();
				swipeviewlayout.removeViewAt(imageindex);
			} else {
				// do nothing no like dislike selected
			}

			// in order to announce me
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
