package com.icat.miser.adapter;

import java.util.List;

import com.icat.miser.R;
import com.icat.miser.model.ConsumptionModel;

import android.app.Activity;
import android.view.*;
import android.widget.*;

public class DocsAdapter extends BaseAdapter {
	private List<ConsumptionModel> _docs;
	private Activity _act;

	public DocsAdapter(Activity act, List<ConsumptionModel> docs) {
		this._docs = docs;
		this._act = act;
	}

	public void setSource(List<ConsumptionModel> docs) {
		this._docs = docs;
	}

	@Override
	public int getCount() {
		if (this._docs != null)
			return this._docs.size();

		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (this._docs != null)
			return this._docs.get(position);

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(this._act.getApplicationContext(),
					R.layout.consumption_item, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ConsumptionModel item = (ConsumptionModel) getItem(position);
		holder.mTitle.setText(item.getTitle());
		holder.mStartDate.setText(item.getStartDateView());

		int tmpcolor = android.graphics.Color.BLACK;
		if (item.getMoney() != 0) {
			tmpcolor = item.getIsConsumption() ? android.graphics.Color.RED
					: android.graphics.Color.GREEN;
		}
		holder.mMoney.setTextColor(tmpcolor);
		holder.mMoney.setText(item.getMoneyString());
		return convertView;
	}

	class ViewHolder {
		// ImageView iv_icon;
		TextView mTitle;
		TextView mStartDate;
		TextView mMoney;

		public ViewHolder(View view) {
			mTitle = (TextView) view.findViewById(R.id.tvDocTitle);
			mStartDate = (TextView) view.findViewById(R.id.tvStartDate);
			mMoney = (TextView) view.findViewById(R.id.tvMoney);
			view.setTag(this);
		}
	}
}
