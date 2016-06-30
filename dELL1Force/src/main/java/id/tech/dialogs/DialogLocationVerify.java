package id.tech.dialogs;

import id.tech.dell1force.Activity_MenuUtama;
import id.tech.dell1force.R;
import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DialogLocationVerify extends FragmentActivity {
	ImageView img;
	Button btn_OK, btn_Retry;
	String url_Loc_Now, cLat, cLong;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.dialog_location_verify);
		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		getAllView();

		new AsyncGetLoc().execute();
	}

	private class AsyncGetLoc extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (getLocationNow(getApplicationContext())) {
				if (!cLat.equals("") && !TextUtils.isEmpty(cLat)
						&& !cLat.equals("0.0") && !cLong.equals("0.0")) {
					String url = "http://maps.google.com/maps/api/staticmap?center="
							+ cLat
							+ ","
							+ cLong
							+ "&zoom=15&size=300x200&markers=color:blue|size:mid|"
							+ cLat + "," + cLong + "&sensor=false";
					Glide.with(getApplicationContext()).load(url).into(img);
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Can not load Locations now", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void getAllView() {
		img = (ImageView) findViewById(R.id.img);
		btn_OK = (Button) findViewById(R.id.btn_ok);
		btn_Retry = (Button) findViewById(R.id.btn_retry);

		btn_OK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						Activity_MenuUtama.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
		});

		btn_Retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncGetLoc().execute();
			}
		});
	}

	private boolean getLocationNow(Context context) {
		GPSTracker gps = new GPSTracker(context);
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			cLat = String.valueOf(latitude);
			cLong = String.valueOf(longitude);

			Log.e("Longitude", String.valueOf(longitude));
			Log.e("Latitude", String.valueOf(latitude));

			sh.edit()
					.putString(Parameter_Collections.TAG_LATITUDE,
							String.valueOf(latitude)).commit();
			sh.edit()
					.putString(Parameter_Collections.TAG_LONGITUDE,
							String.valueOf(longitude)).commit();

			return true;

		} else {
			return false;
		}
	}
}
