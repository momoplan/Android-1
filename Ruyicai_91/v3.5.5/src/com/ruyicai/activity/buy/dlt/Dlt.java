package com.ruyicai.activity.buy.dlt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.twentytwo.TwentyTwo;
import com.ruyicai.constant.Constants;



public class Dlt extends BuyActivityGroup{
	private String[] titles ={"自选","胆拖","12选2"};
	private String[] topTitles ={"大乐透","大乐透","大乐透"};
	private Class[] allId ={DltNormalSelect.class,DltDantuoSelect.class,DltTwoInDozenSelect.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_DLT);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_DLT);
	}
	public boolean getIsLuck(){
		return true;
	}
}
