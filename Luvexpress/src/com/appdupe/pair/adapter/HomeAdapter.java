package com.appdupe.pair.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.pairnofb.R;
import com.appdupe.pair.utility.GalleryData;

public class HomeAdapter extends ArrayAdapter<GalleryData> {

	Activity activity;

	private ArrayList<GalleryData> data = new ArrayList<GalleryData>();

	/* FeaturedActivity fac; */
	public Drawable image_in_detailActivity;

	public static final int DIALOG_PROGRESS = 1;

	public HomeAdapter(Activity context, ArrayList<GalleryData> list) {
		super(context, R.layout.galleryitem, list);
		this.data = list;
		this.activity = context;
	}

	@Override
	public GalleryData getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	// public HomeAdapter(Activity c, ArrayList<GellaryData> itemListAds)
	// {
	// /*private final static String TAG="HomeAdapter";*/
	// // this.context = context;
	// /*this.user = username;
	// this.pass = password;*/
	// this.activity = c;
	// this.mContext = c;
	// this.sContext = c;
	// // TypedArray attr = mContext;
	// this.data = itemListAds;
	// /*TypedArray attr = mContext
	// .obtainStyledAttributes(R.styleable.HelloGallery);
	// mGalleryItemBackground = attr.getResourceId(
	// R.styleable.HelloGallery_android_galleryItemBackground, 0);
	// TypedArray attrs = sContext
	// .obtainStyledAttributes(R.styleable.HelloGallery_smallImage);
	// small_imageBackground = attrs.getResourceId(
	// R.styleable.HelloGallery_android_galleryItemBackground, 1);
	// attr.recycle();*/
	// }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("HomeAdapter getview");
		boolean flag = true;

		if (convertView == null) {
			LayoutInflater vi = activity.getLayoutInflater();
			convertView = vi.inflate(R.layout.galleryitem, null);
		}
		ImageView galleimageview = (ImageView) convertView
				.findViewById(R.id.gelleryimageview);
		galleimageview.setImageResource(getItem(position).getResourceId());
		TextView title=(TextView)convertView.findViewById(R.id.luvexpress);
		title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Montserrat-Bold.ttf"));
		title.setText(getItem(position).getTitle());

		return convertView;

	}
}
