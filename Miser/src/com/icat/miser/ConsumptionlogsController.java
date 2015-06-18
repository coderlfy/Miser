package com.icat.miser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.icatcontrol.icatlistview.extras.SoundPullEventListener;
import com.icatcontrol.icatlistview.pulltorefresh.*;
import com.icatcontrol.icatlistview.pulltorefresh.PullToRefreshBase.*;
import com.icatcontrol.icatlistview.startup.*;
import com.icatcontrol.icatlistview.swipe.*;
import com.icatcontrol.icatlistview.swipe.SwipeMenuListView.*;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;

public class ConsumptionlogsController {
	private String[] mStrings = { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler",
			"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
			"Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
			"Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

	private String[][] consumptionlogsData = {
			{ "早上吃饭，油条包子", "true", "12.9", "2015-2-3" },
			{ "中午车费", "true", "2", "2015-2-3" },
			{ "下午麦丹劳套餐", "true", "0", "2015-2-3" },
			{ "卓展孩子玩耍", "true", "120", "2015-2-5" },
			{ "本月工资", "false", "17760", "2015-2-10" },
			{ "借给华俊房租填补", "true", "500", "2015-3-13" },
			{ "回老家火车", "true", "120", "2015-3-25" },
			{ "车站到家费用", "true", "25", "2015-3-25" },
			{ "回家请客吃饭", "true", "240", "2015-3-26" },
			{ "带孩子玩百家乐", "true", "120", "2015-3-28" },
			{ "去太原旅游总费用", "true", "340", "2015-3-29" },
			{ "太原买中午餐", "true", "12.9", "2015-3-30" } };

	private CatListView mPullRefreshListView;
	private static SwipeMenuListView _ActualListView;
	private ConsumptionlogsActivity mActivity;
	private Boolean mEnableRefreshSound = false;
	public static MiserDBHelper _MiserDBHelper = null;
	private static DocsAdapter _Docsadapter = null;
	private static List<ConsumptionModel> _Consuptions = null;
	private TConsumption mTConsumption = null;

	public ConsumptionlogsController(ConsumptionlogsActivity activity) {
		this.mActivity = activity;
		
		if(_MiserDBHelper == null)
			_MiserDBHelper = new MiserDBHelper(this.mActivity);
		
		
	}
	
	public static void updateListView(ConsumptionModel consumption){
		Boolean isfind = false;
		for(int i = 0; i < _Consuptions.size(); i++)  
		{  
			ConsumptionModel item = _Consuptions.get(i);
			if(item.getId() == consumption.getId()){
				isfind = true;
				item.setIsConsumption(consumption.getIsConsumption());
				item.setMoney(consumption.getMoney());
				item.setStartDate(consumption.getStartDate());
				item.setTitle(consumption.getTitle());
			}  
		}
		if(!isfind)
			_Consuptions.add(consumption);
		
		_Docsadapter.setSource(_Consuptions);
		_Docsadapter.notifyDataSetChanged();
	}

	public void BindRefreshListView(CatListView mPullRefreshListView) {
		this.mPullRefreshListView = mPullRefreshListView;

		this.bindDownRefresh();

		this.bindUpAttach();

		this.bindUpDownRefreshSound();

		this.bindContentListView();
	}

	private void bindDownRefresh() {
		this.mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<SwipeMenuListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<SwipeMenuListView> refreshView) {
						String label = DateUtils.formatDateTime(
								mActivity.getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});

	}

	private void bindUpAttach() {

		// Add an end-of-list listener
		this.mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {

						Toast.makeText(mActivity, "End of List!",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void bindUpDownRefreshSound() {
		if (!mEnableRefreshSound)
			return;

		SoundPullEventListener<SwipeMenuListView> soundListener = new SoundPullEventListener<SwipeMenuListView>(
				this.mActivity);

		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		mPullRefreshListView.setOnPullEventListener(soundListener);
	}

	private void bindContentListView() {
		_ActualListView = mPullRefreshListView.getRefreshableView();

		this.mActivity.registerForContextMenu(_ActualListView);

		_Consuptions = this.createData();
		_Docsadapter = new DocsAdapter(this.mActivity,
				_Consuptions);

		_ActualListView.setAdapter(_Docsadapter);

		this.addSwipeMenu();

		this.bindSwipeMenuItemHandler();

		this.bindSwipeMenuHandler();

		this.bindContentItemClickHandler();

		this.bindContentItemLongClickHandler();
	}

	private void addSwipeMenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				/*
				 * SwipeMenuItem openItem = new SwipeMenuItem(
				 * getApplicationContext()); // set item background
				 * openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
				 * 0xC9, 0xCE))); // set item width
				 * openItem.setWidth(dp2px(90)); // set item title
				 * openItem.setTitle("Open"); // set item title fontsize
				 * openItem.setTitleSize(18); // set item title font color
				 * openItem.setTitleColor(Color.WHITE); // add to menu
				 * menu.addMenuItem(openItem);
				 */

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						mActivity.getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		_ActualListView.setMenuCreator(creator);
	}

	private void bindSwipeMenuItemHandler() {

		_ActualListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						
						ConsumptionModel item = _Consuptions.get(position);
						switch (index) {
						case 0:
							mTConsumption.Delete(item.getId());
							_Consuptions.remove(item);
							_Docsadapter.setSource(_Consuptions);
							_Docsadapter.notifyDataSetChanged();
							break;
						case 1:
							break;
						}
						return false;
					}
				});
	}

	private void bindSwipeMenuHandler() {
		_ActualListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
			}

			@Override
			public void onSwipeEnd(int position) {
			}
		});
	}

	private void bindContentItemClickHandler() {
		_ActualListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, ConsumptionActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable("consumptionlog",
						(ConsumptionModel) _ActualListView
								.getItemAtPosition(position));
				intent.putExtras(bundle);
				mActivity.startActivity(intent);
			}
		});
	}

	private void bindContentItemLongClickHandler() {
		_ActualListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Toast.makeText(mActivity.getApplicationContext(),
								position + " long click", 0).show();
						return false;
					}
				});
	}

	private List<ConsumptionModel> createData() {
		this.mTConsumption = new TConsumption();
		this.mTConsumption.setDBHelper(_MiserDBHelper);
		
		return this.mTConsumption.Get();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				this.mActivity.getResources().getDisplayMetrics());
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mListItems.addFirst("Added after refresh...");
			// mAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

}
