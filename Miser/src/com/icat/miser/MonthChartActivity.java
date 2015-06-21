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
import org.xclcharts.renderer.*;
import org.xclcharts.view.ChartView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;

public class MonthChartActivity extends SherlockActivity {

	private BarChart3D chart = new BarChart3D();
	private ChartView view = null;
	// ������Դ
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		
		FrameLayout content = new FrameLayout(this);

		// ���ſؼ�������FrameLayout���ϲ㣬���ڷŴ���Сͼ��
		FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		/*
		 * //���ſؼ�������FrameLayout���ϲ㣬���ڷŴ���Сͼ�� mZoomControls = new
		 * ZoomControls(this); mZoomControls.setIsZoomInEnabled(true);
		 * mZoomControls.setIsZoomOutEnabled(true);
		 * mZoomControls.setLayoutParams(frameParm);
		 */

		// ͼ����ʾ��Χ��ռ��Ļ��С��90%��������
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels * 0.9);
		int scrHeight = (int) (dm.heightPixels * 0.9);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scrWidth, scrHeight);

		// ������ʾ
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// ͼ��view���벼���У�Ҳ��ֱ�ӽ�ͼ��view����Activity��Ӧ��xml�ļ���
		final RelativeLayout chartLayout = new RelativeLayout(this);

		chartLayout.addView(getMonthView(), layoutParams);

		// ���ӿؼ�
		((ViewGroup) content).addView(chartLayout);
		// ((ViewGroup) content).addView(mZoomControls);
		setContentView(content);
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();
	}
	
	protected int[] getBarLnDefaultSpadding()
	{
		int [] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(view.getContext(), 40); //left	
		ltrb[1] = DensityUtil.dip2px(view.getContext(), 60); //top	
		ltrb[2] = DensityUtil.dip2px(view.getContext(), 20); //right	
		ltrb[3] = DensityUtil.dip2px(view.getContext(), 40); //bottom						
		return ltrb;
	}

	private void chartRender() {
		try {
			// ���û�ͼ��Ĭ������pxֵ,���ÿռ���ʾAxis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(view.getContext(), 40), ltrb[1],
					ltrb[2], ltrb[3]);

			// ָ����ʾΪ����3D����
			chart.setChartDirection(XEnum.Direction.HORIZONTAL);

			// ����Դ
			chart.setDataSource(chartData);
			chart.setCategories(chartLabels);

			// ����ϵ
			chart.getDataAxis().setAxisMax(50);
			chart.getDataAxis().setAxisMin(10);
			chart.getDataAxis().setAxisSteps(10);
			chart.getCategoryAxis().setTickLabelRotateAngle(-45f);

			// ����
			chart.setTitle("����ԭ�Ͻ������");
			chart.addSubtitle("(XCL-Charts Demo)");

			// �����
			chart.getAxisTitle().setLeftTitle("ԭ��");
			chart.getAxisTitle().setLowerTitle("������");

			// ��������
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			chart.getPlotGrid().showEvenRowBgColor();

			// �����������ǩ��ʾ��ʽ
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return (label + "��");
				}

			});

			// ���������ϱ�ǩ��ʾ��ʽ
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

			// ���������ɫ
			chart.setAxis3DBaseColor(Color.rgb(132, 162, 197));

			// ����������
			chart.ActiveListenItemClick();

			// ���������ƶ�
			chart.setPlotPanMode(XEnum.PanMode.VERTICAL);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("chartdebug", e.toString());
		}
	}

	private void chartDataSet() {
		// ��ǩ��Ӧ���������ݼ�
		List<Double> dataSeriesA = new LinkedList<Double>();
		dataSeriesA.add(20d);
		dataSeriesA.add(28d);
		dataSeriesA.add(45d);

		List<Double> dataSeriesB = new LinkedList<Double>();
		dataSeriesB.add(30d);
		dataSeriesB.add(17d);
		dataSeriesB.add(35d);

		chartData.add(new BarData("����", dataSeriesA, Color.rgb(224, 62, 54)));
		chartData.add(new BarData("����", dataSeriesB, Color.rgb(140, 71, 222)));
	}

	private void chartLabels() {
		chartLabels.add("֥��");
		chartLabels.add("��Ҷ");
		chartLabels.add("����");

	}

	private ChartView getMonthView() {
		view = new ChartView(this) {

			@Override
			public List<XChart> bindChart() {
				List<XChart> lst = new ArrayList<XChart>();
				lst.add(chart);
				return lst;
			}
		};

		return view;
	}
}
