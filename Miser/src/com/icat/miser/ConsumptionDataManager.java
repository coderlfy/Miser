package com.icat.miser;

import java.util.ArrayList;
import java.util.List;

import com.icat.miser.db.MiserDBHelper;
import com.icat.miser.db.TConsumption;
import com.icat.miser.model.ConsumptionModel;

import android.app.Activity;

public class ConsumptionDataManager {
	private Boolean mIsFinishLoaded = false;
	private int mHasViewRecords = 0;
	private int mTotalRecords = 0;
	private int mCurrentPageIndex = 0;
	private int mPageSize = 2;
	private List<ConsumptionModel> _Consuptions = null;
	private List<Integer> _newIds = null;

	private TConsumption mTConsumption = null;
	public static MiserDBHelper _MiserDBHelper = null;
	private Activity mActivity;

	public ConsumptionDataManager(Activity activity) {
		this.mActivity = activity;
		// ？？？取当前库的记录数。
		if (_MiserDBHelper == null)
			_MiserDBHelper = new MiserDBHelper(this.mActivity);
		
		this.mTConsumption = new TConsumption();
		this.mTConsumption.setDBHelper(_MiserDBHelper);

		this.mTotalRecords = this.mTConsumption.GetCount();
	}

	public List<ConsumptionModel> getData() {
		return _Consuptions;
	}

	public Boolean getmIsFinishLoaded() {
		return mIsFinishLoaded;
	}

	private void setmIsFinishLoaded(Boolean mIsFinishLoaded) {
		this.mIsFinishLoaded = mIsFinishLoaded;
	}

	public List<ConsumptionModel> updateData(ConsumptionModel newConsumption) {
		Boolean isfind = false;
		for (int i = 0; i < _Consuptions.size(); i++) {
			ConsumptionModel item = _Consuptions.get(i);
			if (item.getId() == newConsumption.getId()) {
				isfind = true;

				_Consuptions.set(i, newConsumption);
			}
		}
		if (!isfind)
			_Consuptions.add(newConsumption);

		return _Consuptions;
	}

	public void delete(int id, ConsumptionModel item) {
		mTConsumption.Delete(id);
		this._Consuptions.remove(item);
		this.mHasViewRecords--;
		this.mTotalRecords--;
	}
	
	public int add(ConsumptionModel entity){
		
		int newid = this.mTConsumption.Add(entity);
		this.mHasViewRecords++;
		this.mTotalRecords++;
		if(_newIds == null)
			_newIds = new ArrayList<Integer>();
		_newIds.add(newid);
		
		return newid;
	}
	
	public void update(ConsumptionModel entity){
		this.mTConsumption.Update(entity);
	}

	public List<ConsumptionModel> createData() {
		mCurrentPageIndex = 0;

		this._Consuptions = this.mTConsumption.Get(mCurrentPageIndex + 1,
				this.mPageSize, null);
		mHasViewRecords = this._Consuptions.size();

		this.setmIsFinishLoaded((mHasViewRecords == mTotalRecords));
		mCurrentPageIndex++;

		return this._Consuptions;
	}

	public void attachData() {
		if(this.mIsFinishLoaded)
			return;
		
		List<ConsumptionModel> tmp = this.mTConsumption.Get(
				mCurrentPageIndex + 1, this.mPageSize, this._newIds);
		int c = tmp.size();
		if (c > 0) {
			this._Consuptions.addAll(tmp);

			mHasViewRecords += c;
			this.setmIsFinishLoaded((mHasViewRecords == mTotalRecords));
			mCurrentPageIndex++;
		}
	}
	
	public int getCount(){
		return this.mTotalRecords;
	}
}
