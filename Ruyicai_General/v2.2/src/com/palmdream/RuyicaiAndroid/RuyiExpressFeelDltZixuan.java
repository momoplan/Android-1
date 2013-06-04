package com.palmdream.RuyicaiAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author wangyl 如意传情大乐透自选
 * 
 */
public class RuyiExpressFeelDltZixuan extends Activity implements
		OnClickListener, SeekBar.OnSeekBarChangeListener {

	// 小球图的宽高
	public static final int BALL_WIDTH = 32;
	private int iScreenWidth;

	BallTable DLTRedZhixuanBallTable = null;// 红球区
	BallTable DLTBlueZhixuanBallTable = null;// 篮球区

	public static final int DLT_RED_ZIXUAN_START = 0x90000010;
	public static final int DLT_BLUE_ZIXUAN_START = 0x90000050;
	// wangyl 7.12 赠送成功后返回处理
	public static final int DLT_INTENT = 1;

	private int DLTRedBallResId[] = { R.drawable.grey, R.drawable.red };
	private int DLTBlueBallResId[] = { R.drawable.grey, R.drawable.blue };

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	TextView mTextSumMoney;// 总金额
	// SeekBar默认为1
	int iProgressBeishu = 1;
	SeekBar mSeekBarBeishu;// 倍数
	TextView mTextBeishu;// 倍数
	ScrollView scrollView;
	Button sureBtn;
	Button newselected;
	int[] redBallNumbers = new int[35];
	int[] blueBallNumbers = new int[12];
	int iZhushu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.ruyichuanqing_dlt_zixuan);
		// 王艳玲7.6返回
		ImageView rtnview = (ImageView) findViewById(R.id.ruyipackage_btn_return_dlt);
		rtnview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		scrollView = (ScrollView) findViewById(R.id.ruyichuanqing_dlt_scrollview);

		LinearLayout iV = (LinearLayout) findViewById(R.id.ruyichuanqing_dlt_layout_id);

		{
			int redBallViewWidth = FC3DTest.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			int redBallViewNum = 35;
			DLTRedZhixuanBallTable = makeBallTable(iV,
					R.id.table_dlt_zixuan_red, iScreenWidth, redBallViewNum,
					redBallViewWidth, DLTRedBallResId, DLT_RED_ZIXUAN_START, 1);

			int blueBallViewNum = 12;
			DLTBlueZhixuanBallTable = makeBallTable(iV,
					R.id.table_dlt_zixuan_blue, iScreenWidth, blueBallViewNum,
					redBallViewWidth, DLTBlueBallResId, DLT_BLUE_ZIXUAN_START,
					1);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_dlt_zixuan);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV
					.findViewById(R.id.dlt_zixuan_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mTextBeishu = (TextView) iV
					.findViewById(R.id.dlt_zixuan_text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);

			ImageButton addBtn = (ImageButton) iV
					.findViewById(R.id.dlt_seekbar_add_zixuan);
			ImageButton subtractBtn = (ImageButton) iV
					.findViewById(R.id.dlt_seekbar_subtract_zixuan);
			addBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});
			subtractBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(--iProgressBeishu);
				}

			});

		}

		sureBtn = (Button) findViewById(R.id.dlt_sure_zixuan);
		sureBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				int redNums = DLTRedZhixuanBallTable.getHighlightBallNums();
				int blueNums = DLTBlueZhixuanBallTable.getHighlightBallNums();
				iZhushu = (int) getZhuShu(redBallNumbers.length,
						blueBallNumbers.length);
				if (redNums < 5 || blueNums < 2) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RuyiExpressFeelDltZixuan.this);
					builder.setTitle(getResources().getString(
							R.string.please_choose_number));
					builder.setMessage("前区至少选择5个小球后区至少选择2个小球");
					// 确定
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}

							});
					builder.show();
				} else if (iZhushu * 2 > 100000) {
					DialogExcessive();
				} else {
					// iZhushu = (int) getZhuShu(redBallNumbers.length,
					// blueBallNumbers.length);
					Intent intent = new Intent(RuyiExpressFeelDltZixuan.this,
							RuyiExpressFeelSuccess.class);
					Bundle dltZixuanBundle = new Bundle();
					dltZixuanBundle.putString("success", "dltZixuan");
					dltZixuanBundle.putIntArray("redBall", redBallNumbers);// 前区
					dltZixuanBundle.putIntArray("blueBall", blueBallNumbers);// 后区
					dltZixuanBundle.putInt("dltzixuanzhushu", iZhushu);
					dltZixuanBundle.putInt("dltzixuanbeishu", iProgressBeishu);
					intent.putExtras(dltZixuanBundle);
					// wangyl 7.12 赠送成功后返回处理
					startActivityForResult(intent, DLT_INTENT);
				}
			}

		});

		newselected = (Button) findViewById(R.id.dlt_newselectd_zixuan);
		newselected.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				DLTRedZhixuanBallTable.clearAllHighlights();
				DLTBlueZhixuanBallTable.clearAllHighlights();
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_number));
				mSeekBarBeishu.setProgress(1);
			}

		});

	}

	// wangyl 7.12 赠送成功后返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DLT_INTENT) {
			if (resultCode == RESULT_OK) {
				RuyiExpressFeelDltZixuan.this.finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		int iBallId = v.getId();
		if (iBallId >= DLT_RED_ZIXUAN_START && iBallId < DLT_BLUE_ZIXUAN_START) {
			int iBallViewId = v.getId() - DLT_RED_ZIXUAN_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(1, iBallViewId);// 点红球组
			}
		} else if (iBallId >= DLT_BLUE_ZIXUAN_START) {
			int iBallViewId = v.getId() - DLT_BLUE_ZIXUAN_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(2, iBallViewId);// 点蓝球组
			}
		}
		changeTextSumMoney();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		if (progress < 1) {
			seekBar.setProgress(1);
		}
		int iProgress = seekBar.getProgress();
		iProgressBeishu = iProgress;
		mTextBeishu.setText("" + iProgressBeishu);
		changeTextSumMoney();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * 创建BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @param int aBallViewText 0:小球从0开始显示,1:小球从1开始显示 ,3小球从3开始显示(福彩3D和值组6从3开始)
	 * @return BallTable
	 */
	private BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int aFieldWidth, int aBallNum, int aBallViewWidth, int[] aResId,
			int aIdStart, int aBallViewText) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);

		int iBallNum = aBallNum;
		int iBallViewWidth = aBallViewWidth;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;

		int viewNumPerLine = (iFieldWidth - scrollBarWidth)
				/ (iBallViewWidth + 2);
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (aBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(aParentView
						.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(this);

				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			iBallTable.tableLayout.addView(tableRow,
					new TableLayout.LayoutParams(FP, WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(this);
			for (int col = 0; col < lastLineViewNum; col++) {

				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(aParentView
						.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(this);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			iBallTable.tableLayout.addView(tableRow,
					new TableLayout.LayoutParams(FP, WC));
		}
		return iBallTable;
	}

	/**
	 * 显示各种玩法的注数与金额
	 * 
	 * @param aWhichGroupBall
	 *            被选中的BallTable（主要针对福彩3D和值玩法，0为其他玩法，10为福彩3D和值直选，11为福彩3D组3和值组3，12为福彩3D和值组6
	 *            ）
	 */
	public void changeTextSumMoney() {
		int iRedHighlights = DLTRedZhixuanBallTable.getHighlightBallNums();
		int iBlueHighlights = DLTBlueZhixuanBallTable.getHighlightBallNums();

		// 红球数 不足
		if (iRedHighlights < 5) {
			mTextSumMoney.setText("请选择前区小球");
		}
		// 红球数达到最低要求
		else if (iRedHighlights == 5) {
			if (iBlueHighlights < 2) {
				mTextSumMoney.setText("请选择后区小球");
			} else if (iBlueHighlights == 2) {
				long iZhuShu = getZhuShu(iRedHighlights, iBlueHighlights);
				String iTempString = "当前为单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
			} else {
				long iZhuShu = getZhuShu(iRedHighlights, iBlueHighlights);
				String iTempString = "当前为后区复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
			}
		}
		// 红球复式
		else {
			if (iBlueHighlights < 2) {
				mTextSumMoney.setText("请选择后区小球");
			} else if (iBlueHighlights == 2) {
				long iZhuShu = getZhuShu(iRedHighlights, iBlueHighlights);
				String iTempString = "当前为前区复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
			} else {
				long iZhuShu = getZhuShu(iRedHighlights, iBlueHighlights);
				String iTempString = "当前为双区复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
			}
		}
	}

	/**
	 * 获得各种玩法的注数
	 * 
	 * @return 返回注数
	 */
	private long getZhuShu(int aRedBalls, int aBlueBalls) {
		long dltZhuShu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			dltZhuShu += (PublicMethod.zuhe(5, aRedBalls)
					* PublicMethod.zuhe(2, aBlueBalls) * iProgressBeishu);
		}
		return dltZhuShu;
	}

	/**
	 * 根据玩法改变当前View 及响应
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球，如单式共两组小球，从0开始计数
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id+1
	 */
	private void changeBuyViewByRule(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 1) { // 前区
			int iChosenBallSum = 22;
			DLTRedZhixuanBallTable.changeBallState(iChosenBallSum, aBallViewId);
			redBallNumbers = DLTRedZhixuanBallTable.getHighlightBallNOs();
		}
		if (aWhichGroupBall == 2) { // 后区
			int iChosenBallSum = 12;
			DLTBlueZhixuanBallTable
					.changeBallState(iChosenBallSum, aBallViewId);
			blueBallNumbers = DLTBlueZhixuanBallTable.getHighlightBallNOs();
		}
	}

	/**
	 * 单笔投注大于10万元时的对话框
	 */
	private void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				RuyiExpressFeelDltZixuan.this);
		builder.setTitle("投注失败");
		builder.setMessage("单笔投注不能大于100000元");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}
}
