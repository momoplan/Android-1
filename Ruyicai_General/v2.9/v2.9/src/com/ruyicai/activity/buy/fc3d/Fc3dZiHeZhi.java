
package com.ruyicai.activity.buy.fc3d;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.fc3d.F3dZiHeZhiCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;


/**
 * 福彩3d和值
 * @author Administrator
 *
 */
public class Fc3dZiHeZhi extends ZixuanActivity implements BuyImplement, OnCheckedChangeListener{
	private LinearLayout topLinear;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[]={"直选和值","组三和值","组六和值"};
	public static int iCurrentButton;//标签
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[1];// 1个选区
	private F3dZiHeZhiCode fc3dCode = new  F3dZiHeZhiCode();
	BallTable ballTable ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		topLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinear.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		initTopButton();
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}
	public void initTopButton(){
		for(int i=0;i<topTitle.length;i++){
			RadioButton radio = new RadioButton(this);
			radio.setText(topTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			topButton.addView(radio);
		}
	}
	/**
	 * 福彩3D和值直选
	 */
	private void create_FC3D_HEZHI_ZHIXUAN() {
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(28, 1, ballResId, 0, 0,Color.RED,title);
		createView(areaInfos, fc3dCode,this,true);
		ballTable = areaNums[0].table;
	}
	/**
	 * 福彩3D和值组3
	 */
	private void create_FC3D_HEZHI_ZU3() {
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(26, 1, ballResId, 0, 1,Color.RED,title);
		createView(areaInfos, fc3dCode,this,true);
		ballTable = areaNums[0].table;
	}
	/**
	 * 福彩3D和值组6
	 */
	private void create_FC3D_HEZHI_ZU6() {
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(22, 1, ballResId, 0, 3,Color.RED,title);
		createView(areaInfos, fc3dCode,this,true);
		ballTable = areaNums[0].table;
	}
	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0:// 和值直选
				iCurrentButton=PublicConst.BUY_FC3D_HEZHI_ZHIXUAN;
				create_FC3D_HEZHI_ZHIXUAN();
				break;
			case 1:// 和值组三
				iCurrentButton=PublicConst.BUY_FC3D_HEZHI_ZU3;
				create_FC3D_HEZHI_ZU3();
				break;
			case 2:// 和值组六
				iCurrentButton=PublicConst.BUY_FC3D_HEZHI_ZU6;
				create_FC3D_HEZHI_ZU6();
				break;
			}
		}
		
	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].info.areaNum;
			if(iBallId<0){
				  areaNums[i].table.clearAllHighlights();
				  areaNums[i].table.changeBallState(areaNums[i].info.chosenBallSum, nBallId);
				  break;
			}

	     }

	}
	/**
	 * 判断是否满足投注条件
	 */
	public void isTouzhu() {
		// TODO Auto-generated method stub
			if (ballTable.getHighlightBallNums() < 1) {
				alertInfo("请选择小球号码后再投注");
			} else if (ballTable.getHighlightBallNums() == 1) {
				// wangyl 7.13 配合陈晨投注时用
				int iZhuShu = getZhuShu();
				String fushiStr = PublicMethod.getZhuMa(ballTable.getHighlightBallNOs()[0])+"";
				if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {
					setZhuShu(iZhuShu);
					alert( "注码：" + fushiStr);
				}
			}
	}

	 /**
	    * 点击小球提示金额
	    * @param areaNum
	    * @param iProgressBeishu
	    * @return
	  */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		int iZhuShu = getZhuShu();
		if(iZhuShu==0){
			mTextSumMoney = "需要1个球";
		}else{
			mTextSumMoney = "共"+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
		}
		return mTextSumMoney;
	}
  /**
   * 获取注数
   * 
   */
	public int getZhuShu(){
		int zhushu = 0;
		switch(iCurrentButton){
		case PublicConst.BUY_FC3D_HEZHI_ZHIXUAN:
			zhushu= getFc3dZhixuanHezhiZhushu();
			break;
		case PublicConst.BUY_FC3D_HEZHI_ZU3:
			zhushu= getFc3dZu3HezhiZhushu() ;
			break;
		case PublicConst.BUY_FC3D_HEZHI_ZU6:
			zhushu= getFc3dZu6HezhiZhushu() ;
			break;
		}
		return zhushu* iProgressBeishu;
	}
	/**
	 * 获得福彩3D直选和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZhixuanHezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = ballTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得1）
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
	
				}
			}
		}
		return iZhuShu;
	}
	/**
	 * 获得福彩3D组3和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu3HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = ballTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5,
				5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组3，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组6和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu6HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = ballTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组6，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}
	 /**
	  * 投注联网
	  */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_FC3D);
	}

}
