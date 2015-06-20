package com.icat.miser;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.icat.miser.controller.ConsumptionlogsController;
import com.icatcontrol.icatlistview.startup.CatListView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.EditText;

public class ConsumptionlogsActivity extends SherlockListActivity {

	static final int MENU_Add_Consumption = 1;
	static final int MENU_search_titleKey = 2;
	static final int MENU_search_startDate = 3;
	static final int MENU_search_money = 4;
	static final int MENU_set_option = 5;
	static final int MENU_View_ALL = 6;

	private CatListView mPullRefreshListView;
	private ConsumptionlogsController mConsumptionlogsController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumptionlogs);

		mPullRefreshListView = (CatListView) findViewById(R.id.consumption_logs_list);

		if (mConsumptionlogsController == null)
			mConsumptionlogsController = new ConsumptionlogsController(this);

		mConsumptionlogsController.BindRefreshListView(mPullRefreshListView);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		/* ������ϽǵĹ����� */
		menu.add(0, MENU_View_ALL, 0, "�鿴ȫ��");
		menu.add(0, MENU_search_titleKey, 0, "�����ƹ���");
		menu.add(0, MENU_search_startDate, 0, "��ʱ�����");
		menu.add(0, MENU_search_money, 0, "��������");
		menu.add(0, MENU_set_option, 0, "�������");
		
		menu.add(0, MENU_Add_Consumption, 0, "New")
        .setIcon(R.drawable.ic_compose)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		//����ͳ�Ʋ˵�
		//menu.add("Search").setShowAsAction( MenuItem.SHOW_AS_ACTION_IF_ROOM |
		//		 MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		/*
		 * menu.add( 0, MENU_DISABLE_SCROLL, 1,
		 * mPullRefreshListView.isScrollingWhileRefreshingEnabled() ?
		 * "Disable Scrolling while Refreshing" :
		 * "Enable Scrolling while Refreshing"); menu.add( 0, MENU_SET_MODE, 0,
		 * mPullRefreshListView.getMode() == Mode.BOTH ?
		 * "Change to MODE_PULL_DOWN" : "Change to MODE_PULL_BOTH"); menu.add(0,
		 * MENU_DEMO, 0, "Demo");
		 */
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		/*
		 * ����Ҽ��˵� AdapterContextMenuInfo info = (AdapterContextMenuInfo)
		 * menuInfo;
		 * 
		 * menu.setHeaderTitle("Item: " +
		 * getListView().getItemAtPosition(info.position)); menu.add("Item 1");
		 * menu.add("Item 2"); menu.add("Item 3"); menu.add("Item 4");
		 */
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		/*
		 * δ֪ MenuItem disableItem = menu.findItem(MENU_DISABLE_SCROLL);
		 * disableItem .setTitle(mPullRefreshListView
		 * .isScrollingWhileRefreshingEnabled() ?
		 * "Disable Scrolling while Refreshing" :
		 * "Enable Scrolling while Refreshing");
		 * 
		 * MenuItem setModeItem = menu.findItem(MENU_SET_MODE); setModeItem
		 * .setTitle(mPullRefreshListView.getMode() == Mode.BOTH ?
		 * "Change to MODE_FROM_START" : "Change to MODE_PULL_BOTH");
		 */
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		/* ���Ͻǵ��¼����� */
		switch (item.getItemId()) {
		case MENU_Add_Consumption:
			Intent intent = new Intent(ConsumptionlogsActivity.this,
					ConsumptionActivity.class);

			Bundle bundle = new Bundle();
			bundle.putSerializable("consumptionlog", null);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case MENU_search_titleKey:
			EditText titlekey = new EditText(ConsumptionlogsActivity.this);
			titlekey.setBackgroundResource(R.drawable.et_bg);
			
			new AlertDialog.Builder(ConsumptionlogsActivity.this)  
			.setTitle("���������ؼ���")  
			.setIcon(android.R.drawable.ic_search_category_default)  
			.setView(titlekey)  
			.setPositiveButton("����", null)  
			.setNegativeButton("ȡ��", null) 
			.show();  

			break;
		/*
		 * case MENU_MANUAL_REFRESH: new GetDataTask().execute();
		 * mPullRefreshListView.setRefreshing(false); break;
		 * 
		 * case MENU_DISABLE_SCROLL: mPullRefreshListView
		 * .setScrollingWhileRefreshingEnabled(!mPullRefreshListView
		 * .isScrollingWhileRefreshingEnabled()); break; case MENU_SET_MODE:
		 * mPullRefreshListView .setMode(mPullRefreshListView.getMode() ==
		 * Mode.BOTH ? Mode.PULL_FROM_START : Mode.BOTH); break; case MENU_DEMO:
		 * mPullRefreshListView.demo(); break;
		 */
		}

		return super.onOptionsItemSelected(item);
	}

}
