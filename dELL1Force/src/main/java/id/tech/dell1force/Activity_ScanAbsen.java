package id.tech.dell1force;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.jwetherell.quick_response_code.DecoderActivity;
import com.jwetherell.quick_response_code.DecoderActivityHandler;
import com.jwetherell.quick_response_code.IDecoderActivity;
import com.jwetherell.quick_response_code.ViewfinderView;
import com.jwetherell.quick_response_code.camera.CameraManager;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;

import id.tech.REST;
import id.tech.dialogs.DialogFragmentProgress;
import id.tech.model.PojoAbsence;
import id.tech.util.ConnectFunctions;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_ScanAbsen extends FragmentActivity implements
		IDecoderActivity, SurfaceHolder.Callback {
	private static final String TAG = DecoderActivity.class.getSimpleName();

	protected DecoderActivityHandler handler = null;
	protected ViewfinderView viewfinderView = null;
	protected CameraManager cameraManager = null;
	protected boolean hasSurface = false;
	protected Collection<BarcodeFormat> decodeFormats = null;
	protected String characterSet = null;
	byte[] dataX;
	long totalSize = 0;
	String code, cUrlImage;
	
	SharedPreferences sh;
	String cType_Absence, cRemarks, cUsername, cStoreID, cLat, cLong;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_scan);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		
		cType_Absence = getIntent().getStringExtra("tipe");
		cRemarks = getIntent().getStringExtra("remarks");
		
		cUsername = sh.getString(Parameter_Collections.SH_USERNAME,"");
		
		cLat = sh.getString(Parameter_Collections.TAG_LATITUDE, "");
		cLong = sh.getString(Parameter_Collections.TAG_LONGITUDE, "");
		
		handler = null;
		hasSurface = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (cameraManager == null)
			cameraManager = new CameraManager(getApplication());

		if (viewfinderView == null) {
			viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
			viewfinderView.setCameraManager(cameraManager);
		}

		showScanner();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}

		cameraManager.closeDriver();

		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (holder == null)
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		hasSurface = false;
	}

	@Override
	public ViewfinderView getViewfinder() {
		// TODO Auto-generated method stub
		return viewfinderView;
	}

	@Override
	public Handler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}

	@Override
	public CameraManager getCameraManager() {
		// TODO Auto-generated method stub
		return cameraManager;
	}

	protected void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);

			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	protected static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	protected void showScanner() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	protected void initCamera(SurfaceHolder surfaceHolder) {
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null)
				handler = new DecoderActivityHandler(this, decodeFormats,
						characterSet, cameraManager);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
		}
	}

	@Override
	public void finish() {
		if (code != null) {
			Intent data = new Intent();
			// Return some hard-coded values
			data.putExtra("code", code);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {
		// TODO Auto-generated method stub
		drawResultPoints(barcode, rawResult);
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);
		code = resultHandler.getDisplayContents().toString();
		Log.d("code", code);

		// Intent load = new Intent(getApplicationContext(), Hasil.class);
		// Bitmap b = barcode;
		// ByteArrayOutputStream bs = new ByteArrayOutputStream();
		// b.compress(Bitmap.CompressFormat.PNG, 50, bs);
		// load.putExtra("byte", bs.toByteArray());
		// startActivity(load);

		cStoreID = code;
		Toast.makeText(getApplicationContext(), "Kode Toko = " + code,
				Toast.LENGTH_SHORT).show();

		if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
//			new Async_Absen().execute();
			new Async_Submit_Retrofit().execute();
		}else{
			Toast.makeText(getApplicationContext(),
					"No connections. Check your connections",
					Toast.LENGTH_LONG).show();
		}
		
	}

	private class Async_Submit_Retrofit extends AsyncTask<Void,Void,Void>{
		DialogFragmentProgress pDialog;
		private String cStatus, cMessage;
		private boolean isSukses = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
		}

		@Override
		protected Void doInBackground(Void... params) {
			final REST adapter = Public_Functions.init_retrofit();

			Observable<PojoAbsence> observable = adapter.absence_inout(cUsername, cStoreID, cType_Absence, cLat, cLong);

			observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<PojoAbsence>() {

						@Override
						public void onCompleted() {
							Log.e("LOG", "Completed");
							if (isSukses) {
								pDialog.dismiss();
								Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
										.show();

								sh.edit().putBoolean(Parameter_Collections.SH_ABSENTED, true).commit();
								sh.edit().putString(Parameter_Collections.SH_STOREID, cStoreID).commit();

								finish();
								finish();
							} else {
								Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
										.show();
								finish();
							}
						}

						@Override
						public void onError(Throwable e) {
							Log.e("LOG", "Error");
						}

						@Override
						public void onNext(PojoAbsence pojoAbsence) {
							Log.e("LOG", "Completed");
							if (pojoAbsence.getStatus().equals("Ok")) {
								isSukses = true;
							}
							cMessage = pojoAbsence.getMessage();
						}
					});
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

		}
	}

	private class Async_Absen extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		private String cStatus, cMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConnectFunctions cf = new ConnectFunctions();
			JSONObject jObj = cf.absence(cUsername, cStoreID, cType_Absence,
					cLat,cLong);

			try {
				cStatus = jObj
						.getString(Parameter_Collections.TAG_RESULT_STATUS);
				cMessage = jObj
						.getString(Parameter_Collections.TAG_RESULT_MESSAGE);

				if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {

				}

			} catch (JSONException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();			
			
			if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
				.show();
				
				sh.edit().putBoolean(Parameter_Collections.SH_ABSENTED, true).commit();
				sh.edit().putString(Parameter_Collections.SH_STOREID, cStoreID).commit();
				
				finish();
			}else{
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
				.show();
			}
		}
	}
}
