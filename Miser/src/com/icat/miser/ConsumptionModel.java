package com.icat.miser;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import android.text.format.DateFormat;

public class ConsumptionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4410499064771485618L;

	private String _title = "";
	private Boolean _isConsumption = false;
	private double _money = 0;
	private Date _startDate = new Date();
	
	private static DateFormat _df = new DateFormat();

	public ConsumptionModel setTitle(String title) {
		this._title = title;
		return this;
	}

	public String getTitle() {
		return this._title;

	}

	public Boolean getIsConsumption() {
		return this._isConsumption;

	}

	public ConsumptionModel setIsConsumption(Boolean isConsumption) {
		this._isConsumption = isConsumption;
		return this;
	}

	public ConsumptionModel setMoney(double money) {
		this._money = money;
		return this;
	}

	public double getMoney() {
		return this._money;

	}
	
	public ConsumptionModel setStartDate(Date startDate) {
		this._startDate = startDate;
		return this;
	}

	public Date getStartDate() {
		return this._startDate;

	}
	
	public String getStartDateString() {
		return (String) _df.format("yyyy-MM-dd", this._startDate);

	}

	public String getMoneyString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String moneyunsigned = df.format(this._money);
		return _money == 0 ? "0" : _isConsumption 
				? "-" + moneyunsigned : "+" + moneyunsigned;

	}

}
