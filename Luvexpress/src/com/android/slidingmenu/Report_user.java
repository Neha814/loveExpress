package com.android.slidingmenu;

import android.app.Activity;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.appdupe.pairnofb.R;

public class Report_user extends Activity {
	RadioGroup options_radio;
	RadioButton no_reason, wrong_messages, wrong_pics, bad_behave, like_spam,
			other;
	String report_issue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_user);

		initialize();
		setDrawable();

		options_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.no_reason:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.tick_theme, 0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					report_issue = no_reason.getText().toString();
					break;
				case R.id.wrong_messages:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.tick_theme, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					report_issue = wrong_messages.getText().toString();
					break;
				case R.id.wrong_pics:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.tick_theme, 0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					report_issue = wrong_pics.getText().toString();
					break;
				case R.id.bad_behave:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.tick_theme, 0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					report_issue = bad_behave.getText().toString();
					break;
				case R.id.like_spam:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.tick_theme, 0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					report_issue = like_spam.getText().toString();
					break;
				case R.id.other:
					no_reason.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					wrong_messages.setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					wrong_pics.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					bad_behave.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					like_spam.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					other.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.tick_theme, 0);
					report_issue = other.getText().toString();
					break;
				default:
					break;
				}
			}
		});

		((ImageView) findViewById(R.id.back_image))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
	}

	public void initialize() {
		options_radio = (RadioGroup) findViewById(R.id.options_radio);
		no_reason = (RadioButton) findViewById(R.id.no_reason);
		wrong_messages = (RadioButton) findViewById(R.id.wrong_messages);
		wrong_pics = (RadioButton) findViewById(R.id.wrong_pics);
		bad_behave = (RadioButton) findViewById(R.id.bad_behave);
		like_spam = (RadioButton) findViewById(R.id.like_spam);
		other = (RadioButton) findViewById(R.id.other);
	}

	private void setDrawable() {
		StateListDrawable no_reasonDrawable = new StateListDrawable();
		no_reasonDrawable.addState(new int[] { android.R.attr.state_checked },
				getResources().getDrawable(R.drawable.no_reason_hover));
		no_reasonDrawable.addState(new int[] {},
				getResources().getDrawable(R.drawable.no_reason));
		no_reason.setButtonDrawable(no_reasonDrawable);

		// wrong_messages, wrong_pics, bad_behave, like_spam,other

		StateListDrawable wrong_messagesDrawable = new StateListDrawable();
		wrong_messagesDrawable.addState(
				new int[] { android.R.attr.state_checked }, getResources()
						.getDrawable(R.drawable.wrong_message_hover));
		wrong_messagesDrawable.addState(new int[] {}, getResources()
				.getDrawable(R.drawable.wrong_message));
		wrong_messages.setButtonDrawable(wrong_messagesDrawable);

		StateListDrawable wrong_picsDrawable = new StateListDrawable();
		wrong_picsDrawable.addState(new int[] { android.R.attr.state_checked },
				getResources().getDrawable(R.drawable.wrong_pics_hover));
		wrong_picsDrawable.addState(new int[] {},
				getResources().getDrawable(R.drawable.wrong_pics));
		wrong_pics.setButtonDrawable(wrong_picsDrawable);

		StateListDrawable bad_behaveDrawable = new StateListDrawable();
		bad_behaveDrawable.addState(new int[] { android.R.attr.state_checked },
				getResources().getDrawable(R.drawable.bad_behave_hover));
		bad_behaveDrawable.addState(new int[] {},
				getResources().getDrawable(R.drawable.bad_behave));
		bad_behave.setButtonDrawable(bad_behaveDrawable);

		StateListDrawable like_spamDrawable = new StateListDrawable();
		like_spamDrawable.addState(new int[] { android.R.attr.state_checked },
				getResources().getDrawable(R.drawable.like_spam_hover));
		like_spamDrawable.addState(new int[] {},
				getResources().getDrawable(R.drawable.like_spam));
		like_spam.setButtonDrawable(like_spamDrawable);

		StateListDrawable otherDrawable = new StateListDrawable();
		otherDrawable.addState(new int[] { android.R.attr.state_checked },
				getResources().getDrawable(R.drawable.other_reason_hover));
		otherDrawable.addState(new int[] {},
				getResources().getDrawable(R.drawable.other_reason));
		other.setButtonDrawable(otherDrawable);

	}
}