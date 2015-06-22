package com.icat.miser.model;

import java.io.Serializable;

public class ConsumptionMonthTotalModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7401983189530677297L;
	private double _money = 0;
	private String _monthViewString = "";
	private Boolean _isConsumption = false;

	public ConsumptionMonthTotalModel setMoney(double money) {
		this._money = money;
		return this;
	}

	public double getMoney() {
		return this._money;

	}

	public ConsumptionMonthTotalModel setMonthViewString(String monthViewString) {
		this._monthViewString = monthViewString;
		return this;
	}

	public String getMonthViewString() {
		return this._monthViewString;

	}

	public Boolean getIsConsumption() {
		return this._isConsumption;

	}

	public ConsumptionMonthTotalModel setIsConsumption(Boolean isConsumption) {
		this._isConsumption = isConsumption;
		return this;
	}

}
