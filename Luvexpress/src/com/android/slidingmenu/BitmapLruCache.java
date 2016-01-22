package com.android.slidingmenu;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements
		ImageCache {

	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		return cacheSize;
	}

	public BitmapLruCache() {
		this(getDefaultLruCacheSize());
	}

	public BitmapLruCache(int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

}


/*<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
     android:gravity="center_vertical|center_horizontal"
    android:layout_marginTop="15dp" >

    <ImageView
        android:id="@+id/imageView1"
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:layout_alignParentLeft="true"
        android:layout_centerVertical="true"
        android:layout_marginLeft="16dp"
        android:gravity="center"
        android:src="@drawable/info" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_alignTop="@+id/imageView1"
        android:layout_marginLeft="5dp"
        android:layout_toRightOf="@+id/imageView1"
        android:gravity="center"
        android:text="@string/osvin_below_facebook"
        android:textSize="12sp" />
</RelativeLayout>*/