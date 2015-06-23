package com.icat.miser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart3D;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.*;
import org.xclcharts.view.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.icat.miser.cache.ConsumptionDataManager;
import com.icat.miser.db.TConsumption;
import com.icat.miser.model.ConsumptionModel;
import com.icat.miser.model.ConsumptionMonthTotalModel;

public class MonthChartActivity extends SherlockActivity {

	private BarChart3D chart = new BarChart3D();
	private ChartView view = null;
	// 轴数据源
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	private List<ConsumptionMonthTotalModel> _ConsuptionsTotals = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.getMonthView();
		this.createMonthtotal();
		this.initView();

		FrameLayout content = new FrameLayout(this);

		// 缩放控件放置在FrameLayout的上层，用于放大缩小图表
		FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		/*
		 * //缩放控件放置在FrameLayout的上层，用于放大缩小图表 mZoomControls = new
		 * ZoomControls(this); mZoomControls.setIsZoomInEnabled(true);
		 * mZoomControls.setIsZoomOutEnabled(true);
		 * mZoomControls.setLayoutParams(frameParm);
		 */

		// 图表显示范围在占屏幕大小的90%的区域内
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels * 0.9);
		int scrHeight = (int) (dm.heightPixels * 0.9);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scrWidth, scrHeight);

		// 居中显示
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
		final RelativeLayout chartLayout = new RelativeLayout(this);

		chartLayout.addView(view, layoutParams);

		// 增加控件
		((ViewGroup) content).addView(chartLayout);
		// ((ViewGroup) content).addView(mZoomControls);
		setContentView(content);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// 当点击不同的menu item 是执行不同的操作
		Log.d("home", String.valueOf(id));
		switch (item.getItemId()) {

		case android.R.id.home:

			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();
	}

	protected int[] getBarLnDefaultSpadding() {
		int[] ltrb = new int[4];
		Context cxt = view.getContext();
		ltrb[0] = DensityUtil.dip2px(cxt, 40); // left
		ltrb[1] = DensityUtil.dip2px(cxt, 60); // top
		ltrb[2] = DensityUtil.dip2px(cxt, 20); // right
		ltrb[3] = DensityUtil.dip2px(cxt, 40); // bottom
		return ltrb;
	}

	private void chartRender() {
		try {
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(view.getContext(), 40),
					ltrb[1], ltrb[2], ltrb[3]);

			// 指定显示为横向3D柱形
			chart.setChartDirection(XEnum.Direction.HORIZONTAL);

			// 数据源
			chart.setDataSource(chartData);
			chart.setCategories(chartLabels);

			// 坐标系
			chart.getDataAxis().setAxisMax(20000);
			chart.getDataAxis().setAxisMin(1000);
			chart.getDataAxis().setAxisSteps(5000);
			chart.getCategoryAxis().setTickLabelRotateAngle(-45f);

			// 标题
			chart.setTitle("2015年度按月统计费用柱状图");
			// chart.addSubtitle("(XCL-Charts Demo)");

			// 轴标题
			chart.getAxisTitle().setLeftTitle("月份");
			chart.getAxisTitle().setLowerTitle("费用");

			// 背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			chart.getPlotGrid().showEvenRowBgColor();

			// 定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return (label + "元");
				}

			});

			// 定义柱形上标签显示格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df = new DecimalFormat("#0.00");
					String label = df.format(value).toString();
					return label;
				}
			});
			chart.getBar().getItemLabelPaint().setTextSize(22);

			// 定义基座颜色
			chart.setAxis3DBaseColor(Color.rgb(132, 162, 197));

			// 激活点击监听
			chart.ActiveListenItemClick();

			// 仅能纵向移动
			chart.setPlotPanMode(XEnum.PanMode.VERTICAL);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("chartdebug", e.toString());
		}
	}

	private void chartDataSet() {
		// 标签对应的柱形数据集
		List<Double> dataSeriesA = new LinkedList<Double>();
		// dataSeriesA.add(20d);
		// dataSeriesA.add(28d);
		// dataSeriesA.add(45d);

		List<Double> dataSeriesB = new LinkedList<Double>();
		// dataSeriesB.add(30d);
		// dataSeriesB.add(17d);
		// dataSeriesB.add(35d);
		ConsumptionMonthTotalModel monthtotal = null;
		int recordindex = 0;
		for (int i = 1; i < 13; i++) {
			if (this._ConsuptionsTotals.size() > recordindex) {
				monthtotal = this._ConsuptionsTotals.get(recordindex);
				String tmpi = (i < 10) ? "0" + String.valueOf(i) : String
						.valueOf(i);
				Boolean hasB = false;
				if (monthtotal.getMonthViewString().compareTo(tmpi) == 0) {
					if (!monthtotal.getIsConsumption()) {
						dataSeriesB.add(monthtotal.getMoney());
						recordindex++;
						if (this._ConsuptionsTotals.size() > recordindex)
							monthtotal = this._ConsuptionsTotals
									.get(recordindex);
						else
							monthtotal = null;

						hasB = true;
					}
					if (monthtotal != null
							&& monthtotal.getMonthViewString().compareTo(tmpi) == 0
							&& monthtotal.getIsConsumption()) {
						dataSeriesA.add(monthtotal.getMoney());
						recordindex++;

						if (!hasB)
							dataSeriesB.add(0d);
					} else
						dataSeriesA.add(0d);
				} else {
					dataSeriesA.add(0d);
					dataSeriesB.add(0d);
				}
			} else {
				dataSeriesA.add(0d);
				dataSeriesB.add(0d);
			}
		}

		chartData
				.add(new BarData("支出", dataSeriesA, android.graphics.Color.RED));
		chartData.add(new BarData("收入", dataSeriesB,
				android.graphics.Color.GREEN));
	}

	private void chartLabels() {
		for (int i = 1; i < 13; i++) {
			chartLabels.add(i + "月");
		}
	}

	private void createMonthtotal() {
		TConsumption tConsumption = new TConsumption();
		tConsumption.setDBHelper(ConsumptionDataManager._MiserDBHelper);
		this._ConsuptionsTotals = tConsumption.GetMonthTotal(2015);
	}

	private ChartView getMonthView() {
		view = new ChartView(this) {

			@Override
			protected void onSizeChanged(int w, int h, int oldw, int oldh) {
				super.onSizeChanged(w, h, oldw, oldh);
				// 图所占范围大小
				chart.setChartRange(w, h);
			}

			@Override
			public List<XChart> bindChart() {
				List<XChart> lst = new ArrayList<XChart>();
				lst.add(chart);
				return lst;
			}

			@Override
			public void render(Canvas canvas) {
				try {
					chart.render(canvas);
				} catch (Exception e) {
					Log.e("chartdebug", e.toString());
				}
			}

			@Override
			public boolean onTouchEvent(MotionEvent event) { //
				// TODO Auto-generated method stub super.onTouchEvent(event);
				return false;
				// if (event.getAction() == MotionEvent.ACTION_UP) {
				// triggerClick(event.getX(), event.getY());
				// }
				// return true;
			}

			// 触发监听
			/*
			 * 在图表上做链接 private void triggerClick(float x, float y) { BarPosition
			 * record = chart.getPositionRecord(x, y); if (null == record)
			 * return;
			 * 
			 * BarData bData = chartData.get(record.getDataID()); Double bValue
			 * = bData.getDataSet().get(record.getDataChildID());
			 * 
			 * Toast.makeText( this.getContext(), "info:" + record.getRectInfo()
			 * + " Key:" + bData.getKey() + " Current Value:" +
			 * Double.toString(bValue), Toast.LENGTH_SHORT) .show(); }
			 */
		};

		return view;
	}
}
