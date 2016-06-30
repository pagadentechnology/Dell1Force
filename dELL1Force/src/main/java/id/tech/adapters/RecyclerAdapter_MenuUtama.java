package id.tech.adapters;

import id.tech.dell1force.Activity_AddIssue;
import id.tech.dell1force.Activity_AddRemarks;
import id.tech.dell1force.Activity_InputSellout;
import id.tech.dell1force.Activity_ScanAbsen;
import id.tech.dell1force.Activity_Splashscreen;
import id.tech.dell1force.R;
import id.tech.dialogs.DialogAbsence;
import id.tech.dialogs.DialogLogout;
import id.tech.util.Parameter_Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerAdapter_MenuUtama extends
		RecyclerView.Adapter<RecyclerAdapter_MenuUtama.ViewHolder> {
	private Context context;
	private SharedPreferences sh;
	private Activity activity;
	private FragmentManager fm;
	private int lastPosition = -1;
	private DialogLogoutListener logoutListener;
	private boolean hakAkses_Merc_AM_CM, hakAkses_Promotor = false;
	private String level;

	public RecyclerAdapter_MenuUtama(Context context, Activity activity,
			FragmentManager fm) {
		// TODO Auto-generated constructor stub
		this.context = context;
		sh = this.context.getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		this.activity = activity;
		this.fm = fm;
		level = sh.getString(Parameter_Collections.SH_LEVEL,
				"Sales Presentative");

	}

	public interface DialogLogoutListener {
		public void openDialog();
	}

	public void setDialogListener(DialogLogoutListener listener) {
		logoutListener = listener;
	}

	private void showToast(String txt, Context ctx) {
		Toast.makeText(ctx, txt, Toast.LENGTH_SHORT).show();

	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {
		public TextView tv_label;
		public ImageView img;
		public View wrapper;
		private Activity activity;

		public ViewHolder(View v, Activity activity) {
			super(v);
			tv_label = (TextView) v.findViewById(R.id.txt_label);
			img = (ImageView) v.findViewById(R.id.img);
			wrapper = (View) v.findViewById(R.id.wrapper);
			wrapper.setOnClickListener(this);

			this.activity = activity;
		}

		private void cekHakAkses(String level) {
			if (level.equals("Merchandiser")) {
				hakAkses_Merc_AM_CM = true;
			} else if (level.equals("Area Manager")) {
				hakAkses_Merc_AM_CM = true;
			} else if (level.equals("Channel Manager")) {
				hakAkses_Merc_AM_CM = true;
			} else if (level.equals("Promoter")) {
				hakAkses_Merc_AM_CM = false;
			} else {
				hakAkses_Merc_AM_CM = false;
			}

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			cekHakAkses(level);

			switch (getAdapterPosition()) {
			case 0:
				// Intent load0 = new Intent(v.getContext(),
				// Activity_ScanAbsen.class);
				// v.getContext().startActivity(load0);

				Intent load0 = new Intent(v.getContext(), DialogAbsence.class);
				v.getContext().startActivity(load0);
				break;
			case 1:
//				if (!hakAkses_Merc_AM_CM) {
				if (true) {

//					if (!level.equals("Channel Manager")) {
					if (true) {
						Intent load = new Intent(v.getContext(),
								Activity_InputSellout.class);
						v.getContext().startActivity(load);

					}

				} else {
					showToast(
							"Only Promotor & Sales Presentative Have This Access",
							v.getContext());
				}
				break;
			case 2:
//				if (hakAkses_Merc_AM_CM) {
				if (sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {

					Intent load = new Intent(v.getContext(),
							Activity_AddRemarks.class);
					v.getContext().startActivity(load);

				} else {
					showToast("Please Absent First", v.getContext());
				}

				break;
			case 3:
//				if (hakAkses_Merc_AM_CM) {
				if (true) {
					Intent load = new Intent(v.getContext(),
							Activity_AddIssue.class);
					v.getContext().startActivity(load);

				} else {
					showToast("You Dont Have This Access", v.getContext());
				}
				break;
			case 4:

				DialogLogout pDialog = new DialogLogout(v.getContext());
				pDialog.show(fm, "");
				// Intent load = new Intent(v.getContext(),
				// Activity_Splashscreen.class);
				// v.getContext().startActivity(load);
				// activity.finish();
				break;

			case 5:
				v.getContext().startActivity(
						new Intent(Intent.ACTION_VIEW, Uri
								.parse(Parameter_Collections.URL_WEBSITE
										+ sh.getString(
												Parameter_Collections.SH_TOKEN,
												""))));
				Log.e("URL WEBSITE",
						Parameter_Collections.URL_WEBSITE
								+ sh.getString(Parameter_Collections.SH_TOKEN,
										""));
				break;

			default:
				break;
			}
		}

	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case 0:
			arg0.img.setImageResource(R.drawable.menu_wp_absen);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setText("Absen");
			setAnimation(arg0.wrapper, arg1);
			break;
		case 1:
			arg0.img.setImageResource(R.drawable.menu_wp_penjualan);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setText("Input Sell Out");
			setAnimation(arg0.wrapper, arg1);
			break;
		case 2:
			arg0.img.setImageResource(R.drawable.menu_wp_branding);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setText("Add Remarks");
			setAnimation(arg0.wrapper, arg1);
			break;
		case 3:
			arg0.img.setImageResource(R.drawable.menu_wp_issue);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setTextSize(15);
			arg0.tv_label.setText("Add Competitor Issue");
			setAnimation(arg0.wrapper, arg1);
			break;
		case 4:
			arg0.img.setImageResource(R.drawable.menu_wp_logout);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setText("Logout");
			setAnimation(arg0.wrapper, arg1);
			break;
		case 5:
			arg0.img.setImageResource(R.drawable.menu_wp_web);
			arg0.wrapper.setBackgroundColor(context.getResources().getColor(
					R.color.color_wp_darkblue));
			arg0.tv_label.setText("Sitemap");
			setAnimation(arg0.wrapper, arg1);
			break;

		default:
			break;
		}

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.item_menuutama, arg0, false);
		ViewHolder viewholder = new ViewHolder(v, this.activity);
		return viewholder;
	}

	private void setAnimation(View viewToAnimate, int position) {
		if (position > lastPosition) {
			Animation anim = AnimationUtils.loadAnimation(context,
					android.R.anim.slide_in_left);
			viewToAnimate.startAnimation(anim);
			lastPosition = position;
		}
	}

}
