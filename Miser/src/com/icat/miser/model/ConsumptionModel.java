package com.icat.miser.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import android.text.format.DateFormat;

public class ConsumptionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4410499064771485618L;

	private int _id = 0;
	private String _title = "";
	private Boolean _isConsumption = false;
	private double _money = 0;
	private Date _startDate = new Date();
	//private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public ConsumptionModel setTitle(String title) {
		this._title = title;
		return this;
	}

	public String getTitle() {
		return this._title;

	}

	public ConsumptionModel setId(int id) {
		this._id = id;
		return this;
	}

	public int getId() {
		return this._id;

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
		return (String) DateFormat.format("yyyy-MM-dd HH:mm", this._startDate);

	}

	public String getStartDateView() {
		String formatstr = "yyyy-MM-dd HH:mm";
		String tmp = (String) DateFormat.format("yyyy-MM-dd", this._startDate);
		String now = (String) DateFormat.format("yyyy-MM-dd", new Date());
		if (tmp.compareTo(now) == 0) {
			formatstr = "���� HH:mm";
		}
		else if(tmp.compareTo(this.subtract(1)) == 0){
			formatstr = "���� HH:mm";
		}

		return (String) DateFormat.format(formatstr, this._startDate);

	}

	private String subtract(int day) {

		return (String) DateFormat.format("yyyy-MM-dd",
				new Date((new Date()).getTime() - day * 24 * 60 * 60 * 1000));
	}

	public String getMoneyString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String moneyunsigned = df.format(this._money);
		return _money == 0 ? "0" : _isConsumption ? "-" + moneyunsigned : "+"
				+ moneyunsigned;

	}

}
