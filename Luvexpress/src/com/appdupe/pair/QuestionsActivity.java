package com.appdupe.pair;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.pair.model.AnswerModel;
import com.appdupe.pair.model.KeyValuePair;
import com.appdupe.pair.model.QuestionModel;
import com.appdupe.pair.utility.AppLog;
import com.appdupe.pair.utility.BasicFunctions;
import com.appdupe.pair.utility.ConnectionDetector;
import com.appdupe.pair.utility.Constant;
import com.appdupe.pair.utility.HttpRequest;
import com.appdupe.pair.utility.JsonParser;
import com.appdupe.pair.utility.Ultilities;
import com.appdupe.pair.utility.Utility;
import com.appdupe.pairnofb.R;

public class QuestionsActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private static final String TAG = "QuestionsActivity";
	private ViewPager pager;
	private Button btnClose, btnDone;
	private ArrayList<QuestionModel> list;
	private ArrayList<AnswerModel> answerList;
	private QuestionAdapter adapter;
	private String deviceId = "", facebookId = "";
	private SharedPreferences preferences;
	private ConnectionDetector cd;
	Typeface Montserrat_Regular, Montserrat_Bold;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);

		Montserrat_Regular = BasicFunctions.Montserrat_Regular(getAssets());
		Montserrat_Bold = BasicFunctions.Montserrat_Bold(getAssets());

		initData();
		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
			return;
		} else {
			getQuestionData();
		}
		
		((ImageView) findViewById(R.id.back_image))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void initData() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		pager = (ViewPager) findViewById(R.id.pagerQuestions);
		btnClose = (Button) findViewById(R.id.btnQuestionClose);
		btnDone = (Button) findViewById(R.id.btnDone);
		list = new ArrayList<QuestionModel>();
		answerList = new ArrayList<AnswerModel>();
		btnClose.setOnClickListener(this);
		deviceId = Ultilities.getDeviceId(this);
		facebookId = preferences.getString(Constant.FACEBOOK_ID, "");
		System.out.println("FACEBOOK ID=" + facebookId);
		btnDone.setTypeface(Montserrat_Regular);
	}

	private void getQuestionData() {
		Utility.showProcess(this, "Getting Question...");
		final HttpRequest httpRequest = new HttpRequest();
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
				facebookId));
		new Thread(new Runnable() {
			public void run() {
				String json;
				try {
					json = httpRequest.postData(Constant.getQuestion_url,
							nameValuePairs);
					list = JsonParser.parseQuestionData(json);
					AppLog.Log(TAG, "QuestionSize ::-->" + list.size());
					runOnUiThread(new Runnable() {
						public void run() {
							Utility.closeprocess(QuestionsActivity.this);
							adapter = new QuestionAdapter(
									QuestionsActivity.this, list, answerList);
							pager.setAdapter(adapter);
							pager.setOnPageChangeListener(QuestionsActivity.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnQuestionClose:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	public void questionIndicatorLeft() {
		pager.setCurrentItem(pager.getCurrentItem() - 1);
	}

	public void questionIndicatorRight() {
		pager.setCurrentItem(pager.getCurrentItem() + 1);
	}
	
	@Override
	public void onBackPressed() {
		submitQuestion();
	}
	
	public void submitQuestion() {
		JSONArray jsonArray = getSelectedAnswerArray();
		AppLog.Log(TAG, "CHANGE IN ANSWER:" + jsonArray.length());
		System.out.println("arraylength=" + jsonArray.length());

		if (jsonArray.length() > 0) {
			Utility.showProcess(QuestionsActivity.this, "Submitting..");
			final HttpRequest httpRequest = new HttpRequest();
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new KeyValuePair(Constant.KEY_FACEBOOK_ID,
					facebookId));
			AppLog.Log(TAG, jsonArray.toString());
			nameValuePairs.add(new KeyValuePair(Constant.JSON, jsonArray
					.toString()));
			new Thread(new Runnable() {
				public void run() {
					String json;
					try {
						json = httpRequest.postData(Constant.updateAnswer_url,
								nameValuePairs);
						AppLog.Log(TAG, "Answer json:" + json);
					} catch (Exception e) {
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						public void run() {
							Utility.closeprocess(QuestionsActivity.this);
							finish();
						}
					});

				}
			}).start();
		} else {
			finish();
		}
	}

	private JSONArray getSelectedAnswerArray() {
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			QuestionModel model = list.get(i);
			if (model.getSelectedAnswerId() != -1) {
				JSONObject object = new JSONObject();
				try {
					object.put(Constant.ANSWER_ID, model.getSelectedAnswerId());
					object.put(Constant.QUESTION_ID, model.getQuestionId());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				AppLog.Log(TAG, "OBJECT" + object.toString());
				array.put(object);
			}
		}
		return array;
	}

	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	public void onPageSelected(int arg0) {
		if (arg0 == list.size() - 1) {
			adapter.manageIndicator(false, true);
		} else if (arg0 == 0) {
			adapter.manageIndicator(true, false);
		} else {
			adapter.manageIndicator(true, true);
		}
	}

	public void gotoNext() {
		if (list.size() > pager.getCurrentItem()) {
			pager.setCurrentItem(pager.getCurrentItem() + 1);
		}
	}

	public class QuestionAdapter extends PagerAdapter {
		private static final String TAG = "QuestionAdapter";
		private QuestionsActivity activity;
		private ArrayList<QuestionModel> list;
		public ArrayList<AnswerModel> selectedList;
		public int selectedRadioPosition = -1;
		private ConnectionDetector cd;
		private int questionPosition;

		public QuestionAdapter(QuestionsActivity activity,
				ArrayList<QuestionModel> list, ArrayList<AnswerModel> answerList) {
			this.activity = activity;
			this.list = list;
			this.selectedList = answerList;
			cd = new ConnectionDetector(activity);
		}

		public int getCount() {
			return list.size();
		}

		private View currentView;

		public Object instantiateItem(View collection, final int position) {
			selectedRadioPosition = -1;
			final Holder holder = new Holder();
			View v = collection;
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.question_item, null);
			holder.txtQuestion = (TextView) v
					.findViewById(R.id.txtQuestionItemQuestion);
			holder.txtQuestion.setTypeface(Montserrat_Regular);
			holder.radioGroup = (RadioGroup) v
					.findViewById(R.id.radioGroupQuestionItem);
			holder.submit = (Button) v.findViewById(R.id.btnQustionItemSubmit);
			holder.ivQuestionIndicatorLeft = (ImageView) v
					.findViewById(R.id.ivQuestionIndicatorLeft);
			holder.ivQuestionIndicatorRight = (ImageView) v
					.findViewById(R.id.ivQuestionIndicatorRight);
			final QuestionModel model = list.get(position);
			holder.txtQuestion.setText(model.getQuestion());
			final ArrayList<AnswerModel> answerList = model.getAnswer();
			final int answerListSize = answerList.size();
			AppLog.Log(TAG, "ANSWER SIZE:" + answerListSize);
			RadioButton answerButton[] = new RadioButton[answerListSize];
			for (int i = 0; i < answerListSize; i++) {
				final AnswerModel answerModel = answerList.get(i);
				answerButton[i] = (RadioButton) activity.getLayoutInflater()
						.inflate(R.layout.radiobutton, null);
				answerButton[i].setText(answerModel.getAnswer());
				answerButton[i].setTypeface(Montserrat_Regular);
				answerButton[i].setId(answerModel.getAnswerId());
				if (answerModel.getFlag() == 1) {
					answerButton[i].setChecked(true);
				}
				final int pos = i;
				answerButton[i].setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						selectedRadioPosition = pos;
						AppLog.Log(TAG, "selcted postion:"
								+ selectedRadioPosition);
					}
				});
				holder.radioGroup.addView(answerButton[i]);
			}
			
			holder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					for(int i=0;i<answerListSize;i++)
					{
						model.setSelectedAnswerId(checkedId);
						if(checkedId==answerList.get(i).getAnswerId())
						{
							AnswerModel as=answerList.get(i);
							as.setFlag(1);
							answerList.set(i, as);
						}
						else
						{
							AnswerModel as=answerList.get(i);
							as.setFlag(0);
							answerList.set(i, as);
						}
					}
					model.setAnswer(answerList);
					list.set(position, model);
				}
			});

			holder.ivQuestionIndicatorLeft
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							activity.questionIndicatorLeft();
						}
					});
			holder.ivQuestionIndicatorRight
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							activity.questionIndicatorRight();
						}
					});
			btnDone.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int selectedAnswerId;
					selectedAnswerId = holder.radioGroup
							.getCheckedRadioButtonId();
//					if (selectedAnswerId == -1) {
//						Toast.makeText(activity,
//								"You must select atleast one answer",
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
					if (selectedAnswerId != 0) {
						model.setSelectedAnswerId(selectedAnswerId);
						if (selectedRadioPosition != -1) {
							answerList.get(selectedRadioPosition).setFlag(1);
						}
						if (list.size()-1!=pager.getCurrentItem()) {
							activity.gotoNext();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									activity);
							builder.setTitle("Questions");
							builder.setMessage("Thank you for your answers. Are you sure want to proceed?");
							builder.setCancelable(false);
							builder.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
											if (!cd.isConnectingToInternet()) {
												Toast.makeText(activity,
														"No Internet",
														Toast.LENGTH_SHORT)
														.show();
												dialog.cancel();
											} else {
												activity.onBackPressed();
											}
										}
									});
							builder.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
						}
					}

				}
			});
			((ViewPager) collection).addView(v);
			collection.setTag(holder);
			currentView = v;
			return v;
		}

		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		public void manageIndicator(boolean right, boolean left) {
			if(!right && left)
			{
				btnDone.setVisibility(View.VISIBLE);
			}
			else
			{
				btnDone.setVisibility(View.INVISIBLE);
			}
			View rightView, leftView;
			rightView = currentView.findViewById(R.id.ivQuestionIndicatorRight);
			leftView = currentView.findViewById(R.id.ivQuestionIndicatorLeft);
			rightView.setVisibility(right ? View.VISIBLE : View.INVISIBLE);
			leftView.setVisibility(left ? View.VISIBLE : View.INVISIBLE);
		}

		class Holder {
			TextView txtQuestion;
			Button submit;
			ImageView ivQuestionIndicatorRight, ivQuestionIndicatorLeft;
			RadioGroup radioGroup;
		}
	}

}
