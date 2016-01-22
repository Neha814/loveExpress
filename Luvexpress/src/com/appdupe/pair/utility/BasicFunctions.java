package com.appdupe.pair.utility;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.appdupe.pairnofb.R;

public class BasicFunctions {
	
	public static Typeface Montserrat_Regular(AssetManager mgr)
	{
		return Typeface.createFromAsset(mgr, "fonts/Montserrat-Regular.ttf");
		
	}
	
	public static Typeface Montserrat_Bold(AssetManager mgr)
	{
		return Typeface.createFromAsset(mgr, "fonts/Montserrat-Bold.ttf");
		
	}
	
	/**
	 * Returns a string that describes the number of days between dateOne and
	 * dateTwo.
	 * 
	 */

	public static String getDateDiffString(Date dateOne, Date dateTwo) {
		long timeOne = dateOne.getTime();
		long timeTwo = dateTwo.getTime();
		long diff = timeTwo - timeOne;

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffDays > 0) {
			int year = (int) (diffDays / 365);
			int rest = (int) (diffDays % 365);
			int month = rest / 30;
			rest = rest % 30;
			int weeks = rest / 7;
			return "Active "+diffDays + "d ago";
		} else {
			diffDays *= -1;
			return "Active "+diffHours + "h " + diffMinutes + "m ago";
		}
	}
	
	public static Dialog options_popup(Context ctx) {
		final Dialog dialog2 = new Dialog(ctx,
				android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog2.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog2.setContentView(R.layout.options_popup);
		dialog2.setCancelable(true);
		dialog2.setCanceledOnTouchOutside(true);
		
		dialog2.show();
		return dialog2;
	}
	
	
	public static void showGPSDisabledAlertToUser(final Context ctx) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								((SherlockFragmentActivity) ctx).startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						((SherlockFragmentActivity) ctx).finish();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

}
