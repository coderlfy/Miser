package com.icat.miser;

import java.util.Calendar;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.*;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;

public class ConsumptionActivity extends SherlockActivity {
	private ConsumptionModel mConsumptionLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumption);

		Intent intent = this.getIntent();
		mConsumptionLog = (ConsumptionModel) intent
				.getSerializableExtra("consumptionlog");

		((EditText) findViewById(R.id.et_title)).setText(mConsumptionLog
				.getTitle());

		final EditText et1 = (EditText) findViewById(R.id.et_startDate);
		et1.setInputType(InputType.TYPE_NULL);

		final Calendar c = Calendar.getInstance();
		// 首次编辑时不可获取编辑值
		et1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(
						ConsumptionActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								c.set(year, monthOfYear, dayOfMonth);
								et1.setText(DateFormat.format("yyy-MM-dd", c));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});

		et1.setText(mConsumptionLog.getStartDateString());

		((EditText) findViewById(R.id.et_money)).setText(String
				.valueOf(mConsumptionLog.getMoney()));

		((ToggleButton) findViewById(R.id.tb_isConsumptioned))
				.setChecked(!mConsumptionLog.getIsConsumption());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		boolean isLight = false;

		menu.add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("返回").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		/*
		 * menu.add("Search").setShowAsAction( MenuItem.SHOW_AS_ACTION_IF_ROOM |
		 * MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		 * 
		 * menu.add("Refresh") .setIcon( isLight ? R.drawable.ic_refresh_inverse
		 * : R.drawable.ic_refresh) .setShowAsAction(
		 * MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		 */
		return true;
	}

	/*
	 * void getMainIntent() { Intent intent = getIntent(); style_id =
	 * intent.getIntExtra(ConsumptionlogsActivity.MAIN_THEME_STYLE, 0);
	 * 
	 * }
	 */
	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * 
	 * Toast.makeText(ConsumptionActivity.this, item.getItemId(),
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * switch (item.getItemId()) { case R.id.home: this.finish(); default:
	 * return super.onOptionsItemSelected(item); }
	 * 
	 * }
	 */
}
