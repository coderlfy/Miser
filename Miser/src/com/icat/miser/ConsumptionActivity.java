package com.icat.miser;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.icat.miser.component.*;
import com.icat.miser.controller.ConsumptionlogsController;
import com.icat.miser.model.ConsumptionModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.*;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

@SuppressLint("SimpleDateFormat")
public class ConsumptionActivity extends SherlockActivity {
	private ConsumptionModel mConsumptionLog;
	private EditText mEtTitle = null;
	private EditText mEtStartDate = null;
	private EditText mEtMoney = null;
	private ToggleButton mTBisConsumptioned = null;
	private int mCurrentId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumption);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mEtTitle = (EditText) findViewById(R.id.et_title);
		mEtStartDate = (EditText) findViewById(R.id.et_startDate);
		mEtMoney = (EditText) findViewById(R.id.et_money);
		mTBisConsumptioned = (ToggleButton) findViewById(R.id.tb_isConsumptioned);
		mEtStartDate.setInputType(InputType.TYPE_NULL);

		/*
		 * final Calendar c = Calendar.getInstance(); // 首次编辑时不可获取编辑值
		 * mEtStartDate.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { DatePickerDialog dialog = new
		 * DatePickerDialog( ConsumptionActivity.this, new
		 * DatePickerDialog.OnDateSetListener() {
		 * 
		 * @Override public void onDateSet(DatePicker view, int year, int
		 * monthOfYear, int dayOfMonth) { c.set(year, monthOfYear, dayOfMonth);
		 * mEtStartDate.setText(DateFormat.format( "yyy-MM-dd", c)); } },
		 * c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
		 * .get(Calendar.DAY_OF_MONTH)); dialog.show(); } });
		 */
		mEtStartDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						ConsumptionActivity.this, mEtStartDate.getText()
								.toString());
				dateTimePicKDialog.dateTimePicKDialog(mEtStartDate);

			}
		});

		String initDateTime = "";
		Intent intent = this.getIntent();
		Serializable requestobj = intent.getSerializableExtra("consumptionlog");

		if (requestobj != null) {
			mConsumptionLog = (ConsumptionModel) requestobj;
			initDateTime = mConsumptionLog.getStartDateString();
			mEtTitle.setText(mConsumptionLog.getTitle());
			mEtStartDate.setText(initDateTime);
			mEtMoney.setText(String.valueOf(mConsumptionLog.getMoney()));
			mTBisConsumptioned.setChecked(!mConsumptionLog.getIsConsumption());
			mCurrentId = mConsumptionLog.getId();
		} else {
			initDateTime = (String) DateFormat.format("yyyy-MM-dd HH:mm",
					new Date());
			mEtStartDate.setText(initDateTime);
			mTBisConsumptioned.setChecked(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		menu.add(0, 1, 0, "保存")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0, 2, 0, "返回")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

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

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			ConsumptionModel consumption = new ConsumptionModel();
			try {
				consumption
						.setTitle(this.mEtTitle.getText().toString())
						.setMoney(
								Double.parseDouble(this.mEtMoney.getText()
										.toString()))
						.setIsConsumption(!this.mTBisConsumptioned.isChecked())
						.setStartDate(
								(new SimpleDateFormat("yyyy-MM-dd HH:mm"))
										.parse(this.mEtStartDate.getText()
												.toString()));

				if (this.mCurrentId == 0)
					consumption
							.setId(ConsumptionlogsController._ConsumptionDataManager
									.add(consumption));
				else {
					consumption.setId(this.mCurrentId);
					ConsumptionlogsController._ConsumptionDataManager
							.update(consumption);
				}

				ConsumptionlogsController.updateListView(consumption);

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.finish();
			break;
		case 2:
			this.finish();
			break;
		case android.R.id.home:

			this.finish();
			break;

		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
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
