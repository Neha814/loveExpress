package com.appdupe.pair.utility;

public class Constant {
	// Update the server End point here
	
	// exact_path ==> https://www.luvexpress.com/process.php/login?

	// private static final String urlPath =
	// "http://elluminati.in/Flamer_nofacebook/";
	// private static final String urlPath =
	// "http://elluminati.in/Flamer_nofacebook_developer/";
	
	//http://phphosting.osvin.net/Pair_Dating/process.php/
	//private static final String urlPath = "http://www.appdupe.info/Flamer_nofacebook/";
    private static final String urlPath = "https://www.luvexpress.com/";
   //private static final String urlPath = "http://phphosting.osvin.net/luvExpress/";
//    private static final String urlPath = "http://phphosting.osvin.net/Pair_Dating/";
    public static String hostUrl = urlPath + "process.php/";
    public static final String login_url = hostUrl + "login";
	//public static final String login_url123 = http://phphosting.osvin.net/Pair_Dating/process.php/login;
	
	public static final String UpdatePrefrence_url = hostUrl + "updateEntity";
	public static final String uploadImage_url = hostUrl + "uploadImage";
	public static final String findMatch_url = hostUrl + "findMatches";
	public static final String getProfile_url = hostUrl + "getProfile";
	public static final String editUseProfile_url = hostUrl + "editProfile";
	public static final String getliked_url = hostUrl + "getProfileMatches";
	public static final String imagedelete_url = hostUrl + "deleteImage";
	public static final String UpdateToken_url = hostUrl + "updateSession";
	public static final String inviteaction_url = hostUrl + "inviteAction";
	public static final String updatePrefrence_url = hostUrl
			+ "updatePreferences";
	
	public static final String get_location=hostUrl+"get_user_locations";
	public static final String add_location=hostUrl+"add_user_locations";
	public static final String add_transaction=hostUrl+"add_user_transaction";
	public static final String get_transaction=hostUrl+"get_user_transactions";
	public static final String deleteLocation_url = hostUrl + "delete_user_locations";
	public static final String check_subscription_status=hostUrl+"check_subscription_status";
	
	public static final String user_exist=hostUrl+"fbUserExist";

	public static final String logout_url = hostUrl + "logout";
	public static final String deleteUserAccount_url = hostUrl
			+ "deleteAccount";
	public static final String uploadChunk_url = hostUrl + "uploadChunk";
	
	public static final String Privacy_URL=urlPath+"admin/index.php/privacy_policy";
	public static final String Term_URL=urlPath+"admin/index.php/terms_services";

	public static final String sendMessage_url = hostUrl + "sendMessage";
	public static final String getChatHistory_url = hostUrl + "getChatHistory";
	public static final String getChatMessage_url = hostUrl + "getChatMessage";

	public static final String getQuestion_url = hostUrl + "get_quetion";
	public static final String updateAnswer_url = hostUrl
			+ "get_question_ans_insert";
	public static final String preference_url = hostUrl + "getPreferences";
	
	public static final String get_user_pro_pic = hostUrl
			+ "get_user_profile_pic";
	public static final String upload_user_pic = hostUrl + "upload_user_image";
	public static final String delete_user_pic = hostUrl + "delete_user_Image";
	public static final String change_profile_pic = hostUrl
			+ "update_profile_pic";
	public static final String ImageHostUrl = urlPath + "pics/";
	public static final int ChunkSize = 524288;

	public static final String methodeName = "POST";

	// dilavar Addes For Status upadate;
	public static final String status_update_url = urlPath
			+ "process.php/update_status";

	// Update the Flurry Key Here:
	// http://support.flurry.com/index.php?title=Analytics/GettingStarted/TechnicalQuickStart/Android

	public static final String flurryKey = "6Z4Z9HMGHKBW3Q88WCFZ";

	public static final String facebooAuthenticationType = "1";
	public static final String deviceType = "2";
	public static final String FromLogin = "fromLogin";
	public static final String Fromsignup = "fromsignup";
	public static final String Fromsplash = "fromsplash";
	public static final String ALUBUMID = "lubumid";
	public static final String ALUBUMNAME = "alubumName";
	public static final String FRIENDFACEBOOKID = "friendfacebookid";
	public static final String CHECK_FOR_PUSH_OR_NOT = "check_for_push";
	public static boolean isMatchedFound = false;
	public static String isFromChatScreen = "isfromChateScreen";
	public static String isLikde = "1";
	public static String isDisliked = "2";
	public static final String FACEBOOK_ID = "facebookid";

	// kishan
	public static final String ERR_FLAG = "errFlag";
	public static final String QUESTION_DETAIL = "detail_que";
	public static final String QUESTION_ID = "q_id";
	public static final String ANSWER_ID = "ans_id";
	public static final String QUESTION = "question";
	public static final String OPTIONS = "options";
	public static final String OPTION = "option";
	public static final String FLAG = "flag";
	// login
	public static final String KEY_FB_ID = "ent_fbid";
	public static final String KEY_FIRST_NAME = "ent_first_name";
	public static final String KEY_LAST_NAME = "ent_last_name";
	public static final String KEY_SEX = "ent_sex";
	public static final String KEY_PUSH_TOKEN = "ent_push_token";
	public static final String KEY_CURRENT_LATITUDE = "ent_curr_lat";
	public static final String KEY_CURRENT_LONGITUDE = "ent_curr_long";
	public static final String KEY_BIRTHDAY = "ent_dob";
	public static final String KEY_PROFILE = "ent_profile_pic";
	public static final String KEY_DEVICE_TYPE = "ent_device_type";
	public static final String KEY_IMAGE_ID = "ent_image_id";
	public static final String KEY_NEW_INDEX_ID = "ent_new_prf_index_id";
	public static final String KEY_NEW_IMAGE_ID = "ent_new_image_id";
	public static final String KEY_STATUS = "ent_status";
	
	public static final String KEY_SHOW_MESSAGE="show_message";
	public static final String KEY_SHOW_MATCH="show_match";
	public static final String KEY_APP_VIB="show_app_vibration";
	

	public static final String KEY_DEVICE_ID = "ent_dev_id";
	public static final String KEY_FACEBOOK_ID = "ent_user_fbid";
	public static final String JSON = "ent_json";

	public static final String PREF_FIRST_NAME = "firstName";
	public static final String PREF_LAST_NAME = "lastName";
	public static final String PREF_USER_ABOUT = "userAbout";
	public static final String PREF_USER_AGE = "userAge";
	public static final String PREF_USER_LAT="userLat";
	public static final String PREF_USER_LONG="userLong";
	public static final String PREF_USER_STATUS = "userStatus";
	public static final String PREF_PROFILE_IMAGE_ONE = "imageOne";
	public static final String PREF_PROFILE_IMAGE_TWO = "imageTwo";
	public static final String PREF_PROFILE_IMAGE_THREE = "imageThree";
	public static final String PREF_PROFILE_IMAGE_FOUR = "imageFour";
	public static final String PREF_PROFILE_IMAGE_FIVE = "imageFive";
	public static final String PREF_PROFILE_IMAGE_SIX = "imageSix";
	public static final String PREF_ISFIRST = "isFirstTime";
//	public static final String PREFERENCE_NAME = "Not_to_Clear_pref";
}