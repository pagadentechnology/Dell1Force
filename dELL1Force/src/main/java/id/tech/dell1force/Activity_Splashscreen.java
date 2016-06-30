package id.tech.dell1force;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.REST;
import id.tech.dell1force.R;
import id.tech.dialogs.DialogFragmentProgress;
import id.tech.dialogs.DialogLocationVerify;
import id.tech.model.PojoLogin;
import id.tech.util.ConnectFunctions;
import id.tech.util.GPSTracker;
import id.tech.util.JSONParser;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.ServiceHandler_JSON;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Activity_Splashscreen extends FragmentActivity {
	SharedPreferences sh;
	ImageView mImg_Logo;
	EditText mEd_Username, mEd_Password;
	Spinner mSpinner_Level;
	Button mBtn;
	View mWrapper;
	String cLat, cLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__splashscreen);
		getAllView();

		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		new Async_Splashscreen().execute();

	}

	private void getAllView() {
		mWrapper = (View) findViewById(R.id.wrapper);
		mImg_Logo = (ImageView) findViewById(R.id.img_logo);
		mImg_Logo.setVisibility(View.VISIBLE);

		mEd_Username = (EditText) findViewById(R.id.ed_username);
		mEd_Password = (EditText) findViewById(R.id.ed_password);
		mSpinner_Level = (Spinner) findViewById(R.id.spinner_level);
		mBtn = (Button) findViewById(R.id.btn_login);

		mBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Public_Functions
						.isNetworkAvailable(getApplicationContext())) {

					//Test
//					new Async_Login().execute();
					new Async_Login_Retrofit().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"No connections. Check your connections",
							Toast.LENGTH_LONG).show();
				}

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

	private class Async_Splashscreen extends AsyncTask<Void, Void, Void> {
		boolean isLogged;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			isLogged = sh.getBoolean(Parameter_Collections.SH_LOGGED, false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
				// getLocationNow(getApplicationContext());
			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (isLogged) {
				// startActivity(new Intent(getApplicationContext(),
				// Activity_MenuUtama.class));
				startActivity(new Intent(getApplicationContext(),
						DialogLocationVerify.class));
				finish();
			} else {
				mWrapper.setVisibility(View.VISIBLE);
			}

		}
	}
	private class Async_Login_Retrofit extends AsyncTask<Void, Void, Void> {
		private String cStatus, cMessage;
		private DialogFragmentProgress pDialog;
		private String cUsername, cPassword, cLevel;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			cUsername = mEd_Username.getText().toString();
			cPassword = mEd_Password.getText().toString();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!cUsername.isEmpty() && !cUsername.equals("")
					&& !TextUtils.isEmpty(cUsername) && !cPassword.isEmpty()
					&& !cPassword.equals("") && !TextUtils.isEmpty(cPassword)) {

				String md5_pass = Public_Functions.md5(cPassword);

				final REST adapter = Public_Functions.init_retrofit();

				Observable<PojoLogin> observable = adapter.login(cUsername, md5_pass);

				observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<PojoLogin>() {
							private boolean isSuksses= false;
							private String message= "Something Wrong";
							@Override
							public void onCompleted() {
								Log.e("LOG", "Complete");
								pDialog.dismiss();
								if(isSuksses){


										mWrapper.setVisibility(View.VISIBLE);
										// mImg_Logo.setVisibility(View.GONE);
										Toast.makeText(getApplicationContext(), cMessage,
												Toast.LENGTH_LONG).show();

										startActivity(new Intent(getApplicationContext(),
												DialogLocationVerify.class));
										finish();


								}else{
									Toast.makeText(getApplicationContext(), cMessage,
											Toast.LENGTH_LONG).show();
								}
							}

							@Override
							public void onError(Throwable e) {
								Log.e("LOG", "Error");
							}

							@Override
							public void onNext(PojoLogin pojoLogin) {
								Log.e("LOG", "Next");
								if(pojoLogin.getData() != null){
									isSuksses = true;
									sh.edit()
											.putString(Parameter_Collections.SH_USERNAME,
													cUsername).commit();
									sh.edit()
											.putBoolean(Parameter_Collections.SH_LOGGED,
													true).commit();
									sh.edit()
											.putString(Parameter_Collections.SH_TOKEN,
													pojoLogin.getData().getToken()).commit();
									sh.edit()
											.putString(Parameter_Collections.SH_LEVEL,
													pojoLogin.getData().getLevel()).commit();
								}
							}
						});

			}
				return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		}
	}

	private class Async_Login extends AsyncTask<Void, Void, Void> {
		private String cStatus, cMessage;
		private DialogFragmentProgress pDialog;
		private String cUsername, cPassword, cLevel;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			cUsername = mEd_Username.getText().toString();
			cPassword = mEd_Password.getText().toString();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConnectFunctions cf = new ConnectFunctions();
			// JSONObject jObj = cf.login("arif.dell", "arif1234");
			// JSONObject jObj = cf.login("srdell_bdg", "srdellbdg1234");
			// JSONObject jObj = cf.login("setyo.dell", "setyo1234");

			if (!cUsername.isEmpty() && !cUsername.equals("")
					&& !TextUtils.isEmpty(cUsername) && !cPassword.isEmpty()
					&& !cPassword.equals("") && !TextUtils.isEmpty(cPassword)) {

				// String md5_pass = Public_Functions.md5("arif1234");
				// JSONObject jObj = cf.login("arif.dell", md5_pass);
				String md5_pass = Public_Functions.md5(cPassword);
				JSONObject jObj = cf.login(cUsername, md5_pass);

				try {
					cStatus = jObj
							.getString(Parameter_Collections.TAG_RESULT_STATUS);
					cMessage = jObj
							.getString(Parameter_Collections.TAG_RESULT_MESSAGE);

					if (cStatus
							.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {
						JSONObject c = jObj
								.getJSONObject(Parameter_Collections.TAG_RESULT_DATA);

						String name = c
								.getString(Parameter_Collections.TAG_NAME);
						String token = c
								.getString(Parameter_Collections.TAG_TOKEN);
						String level = c
								.getString(Parameter_Collections.TAG_LEVEL);
						sh.edit()
								.putString(Parameter_Collections.SH_USERNAME,
										cUsername).commit();
						sh.edit()
								.putBoolean(Parameter_Collections.SH_LOGGED,
										true).commit();
						sh.edit()
								.putString(Parameter_Collections.SH_TOKEN,
										token).commit();
						sh.edit()
								.putString(Parameter_Collections.SH_LEVEL,
										level).commit();
					}else{
						cStatus = "empty";
						cMessage = "Error on Server, Please Contact Administrator";
					}
				} catch (JSONException e) {

				}

			} else {
				cStatus = "empty";
				cMessage = "Please Fill Username and Password";
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {
				mWrapper.setVisibility(View.VISIBLE);
				// mImg_Logo.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), cMessage,
						Toast.LENGTH_LONG).show();

				startActivity(new Intent(getApplicationContext(),
						DialogLocationVerify.class));
				finish();

			} else if (cStatus.equals("empty") || cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_ERROR)){
				Toast.makeText(getApplicationContext(), cMessage,
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
