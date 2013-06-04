package com.ruyicai.activity.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.eleven.Eleven;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.gdeleven.GdEleven;
import com.ruyicai.activity.buy.jc.lq.LqMainActivity;
import com.ruyicai.activity.buy.jc.zq.ZqMainActivity;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.pl5.PL5;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.ten.TenActivity;
import com.ruyicai.activity.buy.twentytwo.TwentyTwo;
import com.ruyicai.activity.buy.zc.FootballLottery;
import com.ruyicai.activity.expert.ExpertActivity;
import com.ruyicai.activity.info.LotInfoActivity;
import com.ruyicai.activity.join.JoinInfoActivity;
import com.ruyicai.activity.more.ActionActivity;
import com.ruyicai.activity.more.LuckChoose;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.custom.gallery.Lights;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.dialog.MessageDialog;
import com.ruyicai.dialog.ShortcutDialog;
import com.ruyicai.net.newtransaction.PrizeRankInterface;
import com.ruyicai.net.newtransaction.TopNewsInformationInterface;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
/**
 * 购彩大厅界面
 * 
 * @author Administrator
 * 
 */
public class BuyActivity extends Activity implements OnClickListener {
	String messagetitle;
	String messagedetail;
	String messageerrorcode;
	String userno,phonenum,sessionid;
	private String messageidflag = null;
	private JSONObject obj;
	private int SCREENMAX = 9;// 屏幕最大图标数
	private int SCREENUM = 4;// 屏幕最大数
	private int SCREEALL = 0;// 屏幕总图标数
	private int PRIZERANKSCREEN = 1;//新加中獎排行
	private int HEIGHT = 0;
	ProgressDialog progressdialog;
	String newstitle="";
	String newscontent="";
	private int top = 20;
	private List<String> mLabelArray = new ArrayList<String>();
	private int[] imageViews = { R.drawable.ico_buy, R.drawable.ico_double,R.drawable.ico_super,
			R.drawable.ico_3d,R.drawable.ico_115, R.drawable.ico_timec,
			R.drawable.icon_jc,R.drawable.gd_eleven, R.drawable.ico_eleven,R.drawable.ico_expert,
			R.drawable.ico_three,R.drawable.ico_seven, R.drawable.twentyfive,R.drawable.icon_pl5,
			R.drawable.icon_qxc ,R.drawable.ico_goalin,R.drawable.ico_basketball};
	private String[] imageTitle = { "合买大厅", "双色球", "大乐透", "福彩3D", "江西11选5","时时彩", "竞彩足球", "广东11选5","11运夺金","专家荐号","排列三", "七乐彩","22选5","排列五","七星彩","足彩",
			 "竞彩篮球"};
	private final Class[] cla = { JoinInfoActivity.class, Ssq.class,Dlt.class,
			Fc3d.class,Dlc.class, Ssc.class,  ZqMainActivity.class,GdEleven.class,Eleven.class,ExpertActivity.class,PL3.class,
			Qlc.class,TwentyTwo.class,PL5.class, QXC.class,FootballLottery.class,LqMainActivity.class};
	private int[] imgViewsId = { R.id.mainpage_hemai_sign,
			R.id.mainpage_ssq_sign, R.id.mainpage_fc3d_sign,
			R.id.mainpage_11x5_sign, R.id.mainpage_dlt_sign,
			R.id.mainpage_ssc_sign, R.id.mainpage_qlc_sign,
			R.id.mainpage_pl3_sign, R.id.mainpage_zucai_sign ,
			R.id.mainpage_10_sign, R.id.mainpage_11_sign,
			R.id.mainpage_12_sign,R.id.mainpage_13_sign,
			R.id.mainpage_14_sign,R.id.mainpage_15_sign};
	private int[] textViewId = { R.id.mainpage_hemai_text,
			R.id.mainpage_ssq_text, R.id.mainpage_fc3d_text,
			R.id.mainpage_11x5_text, R.id.mainpage_dlt_text,
			R.id.mainpage_ssc_text, R.id.mainpage_qlc_text,
			R.id.mainpage_pl3_text, R.id.mainpage_zucai_text,
			R.id.mainpage_10_text, R.id.mainpage_11_text,
			R.id.mainpage_12_text, R.id.mainpage_13_text,
			R.id.mainpage_14_text, R.id.mainpage_15_text
			};

	
	
	
	
	// 其中ViewPager为多页显示控件，PagerTitleStrip用于显示当前页面的标题
	private ViewPager viewPagerContainer;
	// 缓存需要左右滑动的视图群的列表容器
	private List<View> viewsBufList;
	private Lights lights;
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressdialog.dismiss();
				MessageDialog newsdialog = new MessageDialog(BuyActivity.this,newstitle,newscontent);
				newsdialog.showDialog();
				newsdialog.createMyDialog();
				break;
			case 2:
				progressdialog.dismiss();
				break;
			case 4:
				MessageDialog dialog = new MessageDialog(BuyActivity.this,messagetitle,messagedetail);
				dialog.showDialog();
				dialog.createMyDialog();
				break;
			}
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buy_activity);
		progressdialog = UserCenterDialog.onCreateDialog(this);
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight();//屏幕的高度
		viewPagerContainer = (ViewPager) findViewById(R.id.viewpager);
		initNumber();
		initLights();
		initGallery();
		initImgView();
		initRollingText();
		isShortcut();
	}
	/**
	 * 根据不同手机分辨率，初始化屏幕图标数据
	 */
	public void initNumber() {
		SCREEALL = imageViews.length;
		switch (HEIGHT) {
		case 320:
			SCREENMAX = 6;
			top = 10;
			break;
		case 480:
			SCREENMAX = 9;
			top = 5;
			break;
		case 854:
			SCREENMAX = 9;
			top = 30;
			break;
		case 800:
			SCREENMAX = 9;
			top = 25;
			break;
		case 960:
			SCREENMAX = 9;
			top = 10;
			break;
		default:
			SCREENMAX = 9;
			top = 50;
			break;
		}
		if(SCREEALL%SCREENMAX==0){
			SCREENUM =  SCREEALL/SCREENMAX;
		}else{
			SCREENUM =  SCREEALL/SCREENMAX+1;
		}
		SCREENUM += PRIZERANKSCREEN;//彩种屏幕数加上新加的非购彩页面的数量 
		for (int i = 0; i < SCREENUM; i++) {
			mLabelArray.add("" + i);
		}
	}
	/**
	 * 初始化滑动
	 */
	public void initGallery() {
		// 添加需要左右划屏效果的视图到缓存容器中
		viewsBufList = new ArrayList<View>();
		for(int i=0;i<SCREENUM;i++){
			viewsBufList.add(new GalleryViewItem(getApplicationContext(), i));
		}
		// 设置 ViewPager 的 Adapter
		viewPagerContainer.setAdapter(new MainViewPagerAdapter());
		// 设置第一显示页面是 第2个View
		viewPagerContainer.setCurrentItem(1);
		// 设置 ViewPager页面切换监听对象
		viewPagerContainer.setOnPageChangeListener(viewPagerPageChangeListener);
	}
	/**
	 * 初始化翻幕灯
	 */
	public void initLights() {
		lights = new Lights(this);
		lights.setIsAnim(false);
		LinearLayout layout = (LinearLayout) findViewById(R.id.buy_activity_light_layout);
		int[] images = { R.drawable.buy_radio, R.drawable.buy_radio_b };
		lights.createViews(SCREENUM, images, layout);
		lights.isDefault(1);// 20120709 tangzhihua 默认显示第二页
	}
	/**
	 * 初始化imageView按钮
	 */
	public void initImgView() {
		int[] buttons = { R.id.mainpage_usercenter, R.id.mainpage_luck_sign,
				R.id.mainpage_help };
		for (int j = 0; j < buttons.length; j++) {
			Button button = (Button) findViewById(buttons[j]);
			button.setBackgroundResource(R.drawable.join_info_btn_selecter);
			button.setOnClickListener(this);
		}
	}
	/**
	 * 联网方法
	 */
	public void informationNet(){
		progressdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonStr;
				jsonStr = TopNewsInformationInterface.informationQuery();
				try {
					Message msg = new Message();
					JSONObject obj = new JSONObject(jsonStr);
					newstitle = obj.getString("title");
					newscontent = obj.getString("content");
					msg.what=1;
					handler.sendMessage(msg);
					
				} catch (JSONException e) {
					Message msg = new Message();
					msg.what=2;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * 拆分滚动信息字符串 格式为逗号隔开
	 */
	public String[] splitStr(String str, int with) {
		String strss[] = str.split(",");
		if (strss.length == 0) {
			int indexs = str.length() / with + 1;
			String strs[] = new String[indexs];
			for (int i = 0; i < indexs; i++) {
				if (i == indexs - 1) {
					strs[i] = str.substring(i * with, str.length());
				} else {
					strs[i] = str.substring(i * with, with * (i + 1));
				}
			}
			return strs;
		}
		return strss;
	}

	public View addTextByText(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setGravity(1);
		tv.setTextSize(15);
		tv.setTextColor(Color.BLACK);
		return tv;
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
		Constants.MEMUTYPE = 0;
	}

	protected void onPause() {
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainpage_usercenter:
			tendToUserCenter();
			break;
		case R.id.mainpage_luck_sign:
			tendToLuckCenter();
			break;
		case R.id.mainpage_help:
			tendToActionCenter();
			break;
		}
	}
	 /**
	  * 彩票资讯
	  */
	public void tendToUserCenter() {
	        Intent intent = new Intent(BuyActivity.this, LotInfoActivity.class);
		    startActivity(intent);
	}

	public void tendToActionCenter() {
		Intent intent = new Intent(BuyActivity.this, ActionActivity.class);
		startActivity(intent);
	}
	
	public void tendToLuckCenter() {
		Intent intent = new Intent(BuyActivity.this, LuckChoose.class);
		startActivity(intent);
	}

	private void dialogMsg() {
		RWSharedPreferences shellRW = new RWSharedPreferences(BuyActivity.this, "UserMessage");
		messageidflag = shellRW.getStringValue(ShellRWConstants.ID);
		if (!PublicConst.MESSAGE.equals("")) {
			try {
				obj = new JSONObject(PublicConst.MESSAGE);
				String id = obj.getString("id");
				messagetitle = obj.getString("title");
				messagedetail = obj.getString("message");
				if (messageidflag == null) {
					shellRW.putStringValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (!messageidflag.equals(id)) {
					shellRW.putStringValue(ShellRWConstants.ID, id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}

			} catch (JSONException e) {

			}
		}
	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			ExitDialogFactory.createExitDialog(this);
			break;
		}
		return false;
	}
	
	private class GalleryViewItem extends TableLayout {
		/*
		 *中奖排行中的一些参数
		 */
		private final String NAME = "name";
		private final String PRIZEAMT = "prizeAmt";
		TabHost prizeRankTab ;
		String[] prizerankTitles = {getString(R.string.total),getString(R.string.month),getString(R.string.week)};
		ListView monthView,weekView,totalView;
		int[] listViewId = {R.id.prizeRank_total,R.id.prizeRank_month,R.id.prizeRank_week};
		View view ;//主View
		LayoutInflater mInflater = null;
		final Handler handler2 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Constants.prizeRankJSON = msg.obj.toString();
					prizeRankTab.setCurrentTab(0);
					break;
				}
			}   

		};
		
		/**
		 * 初始化数据
		 * @param rankType  中奖排行类型
		 */
		private List<Map<String,Object>> initPrizeRankListData(String rankType){
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			try{
				JSONObject aa = new JSONObject(Constants.prizeRankJSON );
				String rankStr  = aa.getString(rankType);
				JSONArray  prizeRankArray = new JSONArray(rankStr);
				
				for(int i = 0;i<prizeRankArray.length();i++ ){
					try{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(NAME,prizeRankArray.getJSONObject(i).getString("name"));
						map.put(PRIZEAMT,prizeRankArray.getJSONObject(i).getString("prizeAmt"));
						list.add(map);
					}catch(Exception e){
					}
				}
			
			}catch (Exception e) {
			}
			
			return list;
		}
		
		private void getPrizeRankData(){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String prizeRankData = PrizeRankInterface.getInstance().prizeRankQuery();
					if(prizeRankData!= ""&&prizeRankData!=null){
							msg.what = 0;
							msg.obj = prizeRankData;
							handler2.sendMessage(msg);
					};
				}
			}).start();
		}
		public GalleryViewItem(Context context, int position) {
			super(context);
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(position == 0){
				view = (LinearLayout) inflate.inflate( R.layout.buy_activity_btn, null);
				prizeRankTab = (TabHost)view.findViewById(R.id.buyactivity_tab_host);
				prizeRankTab.setVisibility(TabHost.VISIBLE);
				initPrizeRank();
				if(Constants.prizeRankJSON  == ""||	Constants.prizeRankJSON  == null){
					getPrizeRankData();   
				}
			}else{
				view = (LinearLayout) inflate.inflate( R.layout.buy_activity_btn1, null);
				prizeRankTab = (TabHost)view.findViewById(R.id.buyactivity_tab_host);
				initBtn(view, position);
			}
			this.addView(view);
		}
		/**
		 * 初始化中奖排行的一些参数（TAB）
		 * @param view
		 */
		private void initPrizeRank() {
			prizeRankTab.setup();
			mInflater = LayoutInflater.from(BuyActivity.this);
			for(int i=0;i<prizerankTitles.length;i++){
					addTab(i); 
			}
			prizeRankTab.setOnTabChangedListener(accountTabChangedListener);
			prizeRankTab.setCurrentTab(1);
		}
		 /**
		  * 为TabHost添加TabSpec
		  * @param index
		  */
		public void addTab(int index){
			View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
			ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
			TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
			img.setBackgroundResource(R.drawable.tab_buy_selector);
			title.setText(prizerankTitles[index]);
			TabHost.TabSpec tabSpec = prizeRankTab.newTabSpec(prizerankTitles[index]).setIndicator(indicatorTab).setContent(listViewId[index]);
			prizeRankTab.addTab(tabSpec);
		}
	
		TabHost.OnTabChangeListener accountTabChangedListener = new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {	
				for(int i=0;i<prizerankTitles.length;i++){
					if(tabId.equals(prizerankTitles[0])){
						initPrizeList(totalView,listViewId[0],initPrizeRankListData(Constants.TOTAL));
					}else if(tabId.equals(prizerankTitles[1])){
						initPrizeList(monthView,listViewId[1],initPrizeRankListData(Constants.MONTH));
					}else if(tabId.equals(prizerankTitles[2])){
						initPrizeList(weekView,listViewId[2],initPrizeRankListData(Constants.WEEK));
					}
				}
			}
		};
		/**
		 * 初始化中奖排行界面
		 * @param listview  listView
		 * @param listviewid listView的ID
		 * @param list    中奖排行的数据  
		 */
		protected void initPrizeList( ListView listview,int listviewid,List<Map<String,Object>> list) {
			listview = (ListView)view.findViewById(listviewid);
			switch (HEIGHT) {
			case 320:
				listview.setPadding(0, 0, 0, 130);
				break;
			case 480:
				listview.setPadding(0, 0, 0, 110);
				break;
			case 800:
				listview.setPadding(0, 0, 0, 54);
				break;
			default:
				listview.setPadding(0, 0, 0, 0);
				break;
			}
			listview.setSelector(R.color.transparent);
			BuyActivityAdapter adapter = new BuyActivityAdapter(BuyActivity.this,list);
			listview.setSelected(false);
			listview.setAdapter(adapter);
			if(listview.isFocused()){
				listview.setItemsCanFocus(false);
			}else{
				listview.setItemsCanFocus(true);
			}
		}
		/**
		 * 中奖排行ListViewAdapter
		 * @author miao
		 */
		private class BuyActivityAdapter extends BaseAdapter{
			
			LayoutInflater mAdapterInflater = null;
			List<Map<String,Object>> mList = null;
			
			public  BuyActivityAdapter(Context context, List<Map<String,Object>> list) {
				mAdapterInflater = LayoutInflater.from(context);
				mList = list;
			}

			public int getCount() {
				return mList.size();
			}

			@Override
			public Object getItem(int position) {
				return mList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position,  View convertView, ViewGroup parent) {
				final String prizeName =  (String) mList.get(position).get(NAME);
				final String prizeAmt = (String) mList.get(position).get(PRIZEAMT);
				PrizeViewHolder holder = new PrizeViewHolder();
				if (convertView == null) {
					convertView = mAdapterInflater.inflate(R.layout.prizerank_listitem,null);
					holder = new PrizeViewHolder();
					holder.prizeId = (TextView) convertView.findViewById(R.id.prizeRank_id);
					holder.prizerName= (TextView) convertView.findViewById(R.id.prizeRank_name);
					holder.prizeNumber = (TextView) convertView.findViewById(R.id.prizeRank_number);
				} else {
					holder = (PrizeViewHolder) convertView.getTag();
				}
				int rank =  position+1;
				setPrizeRankIDBg(holder.prizeId,rank);
				holder.prizeId.setText(rank+"");
				holder.prizerName.setText(prizeName);
				String prizeAmtHtml = "<font color=\"#ff0000\"><B>"+PublicMethod.toYuan(prizeAmt)+"</B></font>"+"元";//以Html格式设置颜色
				holder.prizeNumber.setText(Html.fromHtml(prizeAmtHtml));
				convertView.setTag(holder);
				return convertView;
			}
			class PrizeViewHolder {
				TextView prizeId;
				TextView prizerName;
				TextView prizeNumber;
			}
			private void setPrizeRankIDBg(TextView btn,int ID){
				switch (ID) {
				case 1:
					btn.setBackgroundResource(R.drawable.prizerank_1);
					break;
				case 2:
					btn.setBackgroundResource(R.drawable.prizerank_2);
					break;

				case 3:
					btn.setBackgroundResource(R.drawable.prizerank_3);
					break;

				default:
					btn.setBackgroundResource(R.drawable.prizerank_other);
					break;
				}
			}
		}
		public void initBtn(View view, int position) {
			int length = SCREENMAX;
			int imgpostion = position -1;//设定初始化img数
			if(position<SCREENUM){
				if(SCREENUM>position+1){
					length = SCREENMAX;
				}else{
					length = SCREEALL - SCREENMAX * (imgpostion);
				}
			}
			RelativeLayout top1 = (RelativeLayout) view .findViewById(R.id.layout1_top);
			RelativeLayout top2 = (RelativeLayout) view .findViewById(R.id.layout2_top);
			RelativeLayout top3 = (RelativeLayout) view .findViewById(R.id.layout3_top);
			top1.setPadding(0, top, 0, 0);
			top2.setPadding(0, top, 0, 0);
			top3.setPadding(0, top, 0, 0);
			for (int i = 0; i < length; i++) {
				final int index = i + SCREENMAX * (imgpostion);
				ImageView imgView = (ImageView) view .findViewById(imgViewsId[i]);
				imgView.setVisibility(ImageView.VISIBLE);
				imgView.setImageResource(imageViews[index]);
				imgView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(BuyActivity.this, cla[index]);
						startActivity(intent);
					}
				});
				TextView textTitle = (TextView) view.findViewById(textViewId[i]);
				textTitle.setTextSize(14);
				textTitle.setVisibility(TextView.VISIBLE);
				textTitle.setText(imageTitle[index]);
			}
		}
	}
	  /**
     * 首次启动软件是否创建快捷方式
     * 
     */
	public void isShortcut(){
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		boolean isShortcut = shellRW.getBooleanValue("isShortcut");
		if(!isShortcut){//如果是false的话代表首次启动
		  shellRW.putBooleanValue("isShortcut", true);
		  ShortcutDialog dialog = new ShortcutDialog(this, getString(R.string.shortcut_dialog_title), getString(R.string.shortcut_dialog_message));
		  dialog.showDialog();
		  dialog.createMyDialog();
		}else{
		  dialogMsg();
		}
	}
	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
       switch(resultCode){
       case RESULT_OK:
    	   tendToUserCenter();
    	   break;
       }
	}
	private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			// activity从1到2滑动，2被加载后掉用此方法
			lights.isLight(arg0);// 在这里实现ViewPager控件滑动时, 下方的当前页面标志图标
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 从1到2滑动，在1滑动前调用
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private class MainViewPagerAdapter extends PagerAdapter {
		public MainViewPagerAdapter() {
		}
		@Override
		public int getCount() {
			/*
			 * 返回提供给ViewPager的视图总数. 一般我们会把View群先插入一个List<View>中缓存,
			 * 然后在这里就返回这个List<View>.size()即可.
			 */
			return viewsBufList.size();
		}
		@Override
		public void startUpdate(ViewGroup container) {
			// Log.d(TAG, "PagerAdapter:startUpdate");
		}
		@Override
		public int getItemPosition(Object object) {
			// Log.d(TAG, "PagerAdapter:getItemPosition");
			return POSITION_UNCHANGED;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewsBufList.get(position));
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewsBufList.get(position), 0);
			return viewsBufList.get(position);
		}
		@Override
		public void finishUpdate(ViewGroup container) {
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void notifyDataSetChanged() {
		}
		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
		}
	};
	private View creatGridView(Context context  , int[] imageViews , String[] imageTitle , final Class[] cla){
		 // 初始化参数  
		GridView gridView = new GridView(context) ;
		gridView.setPadding(0,top, 0, 0);
		//设置背景透明
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		        gridView.setNumColumns(3);
		        // 设置数据匹配器  
		        gridView.setAdapter(getAdapter(imageViews , imageTitle));  
		        // 只是显示是没用的，这里我们在添加单击item时的监听事件  
		       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		            @Override  
		           public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {  
		            	Intent intent = new Intent(BuyActivity.this, cla[index]);
						startActivity(intent);
		            }  
		        });
			return gridView;  
		}
	 /** 
	    *  获取数据匹配器 
	    */  
	   private ListAdapter getAdapter( int[] imageViews , String[] imageTitle) {  
	       // 该list用来存放每一个item对应的文字和图片  
	       List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();  
	       for (int i = 0; i < imageTitle.length; i++) {  
	           HashMap<String, Object> map = new HashMap<String, Object>();  
	           map.put("itemText", imageTitle[i]);  
	            map.put("itemImage", imageViews[i]);  
	          list.add(map);  
	        }  
	       SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.buy_gridview_item, new String[] { "itemText", "itemImage" },  
	                new int[] { R.id.imageText, R.id.imageView });  
	        return simpleAdapter;  
	    }  
		public void initRollingText() {
			ViewFlipper mFlipper = ((ViewFlipper) this .findViewById(R.id.notice_other_flipper));
			String str[] = splitStr(Constants.NEWS, 23);
			for (int i = 0; i < str.length; i++) {
				mFlipper.addView(addTextByText(str[i]));
			}
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			mFlipper.startFlipping();
			mFlipper.setOnClickListener(filterclick);
		}
		 OnClickListener  filterclick = new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			          informationNet();
				}
			};
}