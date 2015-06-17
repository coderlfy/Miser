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
	private SwipeMenuListView mActualListView;
	private ConsumptionlogsActivity mActivity;
	private Boolean mEnableRefreshSound = false;

	public ConsumptionlogsController(ConsumptionlogsActivity activity) {
		this.mActivity = activity;
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
		mActualListView = mPullRefreshListView.getRefreshableView();

		this.mActivity.registerForContextMenu(mActualListView);

		DocsAdapter docsadapter = new DocsAdapter(this.mActivity,
				this.createData());

		mActualListView.setAdapter(docsadapter);

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

		mActualListView.setMenuCreator(creator);
	}

	private void bindSwipeMenuItemHandler() {

		mActualListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						// ApplicationInfo item = mAppList.get(position);
						switch (index) {
						case 0:
							// open
							// open(item);
							break;
						case 1:
							// mAppList.remove(position);
							// mAdapter.notifyDataSetChanged();
							break;
						}
						return false;
					}
				});
	}

	private void bindSwipeMenuHandler() {
		mActualListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
			}

			@Override
			public void onSwipeEnd(int position) {
			}
		});
	}

	private void bindContentItemClickHandler() {
		mActualListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, ConsumptionActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable("consumptionlog",
						(ConsumptionModel) mActualListView
								.getItemAtPosition(position));
				intent.putExtras(bundle);
				mActivity.startActivity(intent);
			}
		});
	}

	private void bindContentItemLongClickHandler() {
		mActualListView
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
		List<ConsumptionModel> docs = new ArrayList<ConsumptionModel>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < consumptionlogsData.length; i++) {
			ConsumptionModel doc = new ConsumptionModel();
			try {
				doc.setTitle(consumptionlogsData[i][0])
						.setIsConsumption(
								Boolean.parseBoolean(consumptionlogsData[i][1]))
						.setMoney(Double.parseDouble(consumptionlogsData[i][2]))
						.setStartDate(sdf.parse(consumptionlogsData[i][3]));

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			docs.add(doc);
		}
		return docs;
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
