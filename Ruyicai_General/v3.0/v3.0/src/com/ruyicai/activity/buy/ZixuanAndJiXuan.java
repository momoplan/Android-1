package com.ruyicai.activity.buy;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView.BufferType;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.code.ssc.OneStarCode;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.RWSharedPreferences;

public abstract class ZixuanAndJiXuan extends Activity implements OnCheckedChangeListener,OnClickListener,SeekBar.OnSeekBarChangeListener,HandlerMsg{
	protected int BallResId[] = { R.drawable.grey, R.drawable.red };
	protected LayoutInflater inflater;
	protected LinearLayout buyview;
	protected BallTable   BallTable ;
	protected RadioGroup group;
	protected CodeInterface sscCode = new OneStarCode();
	protected String []  childtype=null;
	protected boolean isbigsmall=false;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	MyHandler handler = new MyHandler(this);//自定义handler
	public String phonenum,sessionId,userno;
	ProgressDialog progressdialog;
	public String codeStr;
	String lotno;
	public String highttype;
	public int type;
	public final static int NULL = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FIVE = 5;
	public final static int TWO_ZUXUAN = 6;
	public final static int TWO_HEZHI = 7;
	public final static int FIVE_TONGXUAN = 8;
	public final static int BIG_SMALL = 9;
	public final static String PAGE = "1";
	public final static String MAX = "5";
	public final static int TIME = 5*60000;//获取期号线程刷新时间单位是分
	int iZhuShu;
	AlertDialog touZhuDialog;
	TextView issueText;
	TextView alertText;
	TextView textZhuma;
	public View zhixuanview;
	public static String lotnoStr;
	public static boolean isTime = true;
	public static boolean isStart = true;
	public static JSONArray prizeInfos = null;
	public static TextView prizeText = null;
	public boolean isViewEnd = true;
	public boolean isViewStart = true;
	public static long startTime;
	long endTime ;
	public AddView addView;
	private boolean isJiXuan = false;
	private Button codeInfo;
	private TextView textTitleAlert;
	protected boolean isTen = true;
	private int MAX_ZHU = 10000;//每笔最多为1万注
	private final int All_ZHU = 99;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sscbuyview);
		childtype= new String[]{"直选","机选"};
	
	}
    /**
    * 点击小球提示金额
    * @param areaNum
    * @param iProgressBeishu
    * @return
    */
   public abstract String textSumMoney(AreaNum areaNum[],int iProgressBeishu);
   /**
    * 判断是否满足投注条件
    */
   public abstract String isTouzhu();
   /**
    * 返回注数
    */
   public abstract int getZhuShu();
   /**
    * 返回投注提示框提示信息
    */
   public abstract String getZhuma();
   /**
    * 返回机选投注注码
    */
   public abstract String getZhuma(Balls ball);
   /**
    * 投注联网信息
    */
   public abstract void touzhuNet();
    
	public void init(){
	LinearLayout childtypes= (LinearLayout)findViewById(R.id.sscchildtype);
	childtypes.setVisibility(View.VISIBLE);
	childtypes.removeAllViews();
	buyview=(LinearLayout)findViewById(R.id.buyview);
	inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	group = new RadioGroup(this); 
	group.setOrientation(RadioGroup.HORIZONTAL);
	// 设置屏幕宽度
     for(int i = 0 ; i < childtype.length ; i++){
         RadioButton radio = new RadioButton(this);
         radio.setText(childtype[i]);
         radio.setTextColor(Color.BLACK);
         radio.setId(i);
         radio.setButtonDrawable(R.drawable.radio_select);
		 radio.setPadding(Constants.PADDING-5, 0, 10, 0);
         group.addView(radio);
         group.setOnCheckedChangeListener(this);
 	     group.check(0);
        }
          //将单选按钮组添加到布局中
    childtypes.addView(group);
//	 if(isTime){
//		  isTime = false;
//		  isStart = true;
//	      prizeInfoNet();
//		}
//	 if(isViewStart){
//		 isViewStart = false;
//		 updateViewThread();
//	 }
	
	}
	/**
	 * 初始化选区
	 */
	public void initArea() {
		areaNums = new AreaNum[1];
        String title = "请选择投注号码" ;
		areaNums[0] = new AreaNum(10,10, 5, BallResId, 0, 0,Color.RED, title);
		
	}
	
	//单选框切换直选，机选
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){		
		case 0:
			iProgressBeishu = 1;iProgressQishu = 1;
			initArea();
			createView(areaNums, sscCode,type,false);
			BallTable=areaNums[0].table;
		break;
		case 1:
			iProgressBeishu = 1;iProgressQishu = 1;
            SscBalls onestar = new SscBalls(1);
			createviewmechine(onestar);
		break;	
		}
			
	}
	
	private TextView mTextSumMoney;
	private ImageButton zixuanTouzhu;
	protected EditText editZhuma;
	protected TextView textTitle;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	private TextView mTextBeishu, mTextQishu;
    public AreaNum areaNums[];
    public int iScreenWidth;
    private CodeInterface code;//注码接口类
    protected View view ;
	public Toast toast;
	private boolean toLogin = false;
	
	//创建直选页面
	public void createView(AreaNum areaNum[],CodeInterface code,int type,boolean isTen) {
	   isJiXuan = false;
	   buyview.removeAllViews();
	   zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
	   prizeText = (TextView) zhixuanview.findViewById(R.id.buy_zixuan_text_prize_num_title);
	   if(areaNums!=null){
		   for(int i=0;i<areaNums.length;i++){
			   if(areaNums[i].tableLayout!=null){
					areaNums[i].tableLayout.removeAllViews();
					areaNums[i].textTitle.setText("");
			   }
			}
		}
	    areaNums = areaNum;
	    this.code = code;
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		mTextSumMoney = (TextView)zhixuanview. findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) zhixuanview.findViewById(R.id.buy_zixuan_edit_zhuma);
		textTitle = (TextView) zhixuanview.findViewById(R.id.buy_zixuan_text_title);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
		int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five};
		int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five};
		int linearViewIds[]={R.id.buy_zixuan_linear_one,R.id.buy_zixuan_linear_two,R.id.buy_zixuan_linear_third,R.id.buy_zixuan_linear_four,R.id.buy_zixuan_linear_five};

		//初始化选区
		for(int i=0;i<areaNums.length;i++){
        	areaNums[i].initView(tableLayoutIds[i],textViewIds[i],linearViewIds[i],zhixuanview);
			AreaNum info = areaNums[i];
			if(i!=0){
				info.aIdStart=areaNums[i-1].areaNum+areaNums[i-1].aIdStart;
			}
			areaNums[i].table =  makeBallTable(areaNums[i].tableLayout, iScreenWidth, info.areaNum,info.ballResId, info.aIdStart, info.aBallViewText, this, this,isTen,"0");

			areaNums[i].init();
		}
		final TextView textNum = (TextView)zhixuanview.findViewById(R.id.buy_zixuan_add_text_num);
		Button add_dialog = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_add_delet);
		addView = new AddView(textNum,this,true);
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(addView.getSize()>0){
					showAddViewDialog();
				}else{
					 Toast.makeText(ZixuanAndJiXuan.this, ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_alert), Toast.LENGTH_SHORT).show();	
				}
			}
		});
		Button add = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertStr = isTouzhu();
				if(alertStr.equals("true")){
					addToCodes();
				}else if(alertStr.equals("false")){
					dialogExcessive();
				}else{
					alertInfo(alertStr);
				}
			}
		
		});
		Button zixuanTouzhu = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
//		ImageButton again = (ImageButton) zhixuanview.findViewById(R.id.buy_zixuan_img_again);
//		again.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			  again();
//			}
//		});
	   this.type = type;
	   showEditTitle(type);
	   buyview.addView(zhixuanview);
//	   if(prizeInfos!=null){
//		setPrizeInfo(prizeInfos);
//		}
	}
	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addToCodes() {
		if(getZhuShu()>MAX_ZHU){
			dialogExcessive();
		}else if(addView.getSize()>=All_ZHU){
			Toast.makeText(ZixuanAndJiXuan.this,ZixuanAndJiXuan.this.getString(R.string.buy_add_view_zhu_alert) , Toast.LENGTH_SHORT).show();
		}else{
			if(type == BIG_SMALL){
				getCodeInfoDX(addView);
			}else{
				getCodeInfo(addView);
			}
			addView.updateTextNum();
			again();
		}
	}
	private void showAddViewDialog() {
		addView.createDialog(ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_title));
		addView.showDialog();
	}
	public void getCodeInfo(AddView addView){
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		codeInfo.setTouZhuCode(getZhuma());
		for(AreaNum areaNum:areaNums){
			int[] codes = areaNum.table.getHighlightBallNOs();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				codeStr += PublicMethod.isTen(codes[i]);
				if (i != codes.length - 1) {
					codeStr += ",";
				}
			}
			codeInfo.addAreaCode(codeStr,areaNum.textColor);
		}   
		addView.addCodeInfo(codeInfo);
	}
	/**
	 * 添加大小
	 * @param addView
	 */
	public void getCodeInfoDX(AddView addView){
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		codeInfo.setTouZhuCode(getZhuma());
		for(AreaNum areaNum:areaNums){
			String[] codes = areaNum.table.getHighlightStr();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				codeStr += codes[i];
				if (i != codes.length - 1) {
					codeStr += ",";
				}
			}
			codeInfo.addAreaCode(codeStr,areaNum.textColor);
		}   
		addView.addCodeInfo(codeInfo);
	}
	public int getAmt(int zhuShu){
		if(betAndGift!=null){
			return zhuShu * betAndGift.getAmt();
		}else{
			return 0;
		}
		
	}
	public void showEditTitle(int type){
		textTitle.setTextSize(11);
		switch(type){
		case NULL:
			textTitle.setText("已选:");
			textTitle.setTextSize(15);
			break;
		case ONE:
			textTitle.setText("从个位选择1个或多个号码，选号与开奖号个位一致即中奖10元");
			break;
		case TWO:
			textTitle.setText("从后两位各选1个或多个号码，选号与开奖号后两位按位一致即中奖100元");
			break;
		case THREE:
			textTitle.setText("从三位各选1个或多个号码，选号与开奖号后三位按位一致即中奖1000元");
			break;
		case FIVE:
			textTitle.setText("从五位各选1个或多个号码，选号与开奖号按位一致即中奖10万元");
			break;
		case TWO_ZUXUAN:
			textTitle.setText("至少选择2个号码，开奖号码后两位包含在选号中即中奖50元");
			break;
		case TWO_HEZHI:
			textTitle.setText("开奖号码后面两位的数字相加之和,开奖号码为对子奖金100元；非对子奖金50元");
			break;
		case FIVE_TONGXUAN:
			textTitle.setText("从万、千、百、十、个位各选1个或多个号码，单注金额为2元，所选号码与开奖号码一一对应奖金为2万元；所选号码前三位或后三位与开奖号码一一对应，奖金为200元；" +
					"前二位或者后二位与开奖号码一一对应，奖金为20元");
			break;
		case BIG_SMALL:
			textTitle.setText("对十、个位进行大小单双投注，所选大小单双与开奖号码十、个位大小单双一致即中4元");
			break;
		}
		
	}
	//创建机选页面

	private Spinner jixuanZhu;
	private LinearLayout zhumaView;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	public Vector<Balls> balls = new Vector();
	protected Balls ballOne;
	public Toast toastjixuan;
	
	public void createviewmechine(Balls balles){
	   isJiXuan = true;
	   buyview.removeAllViews();
	   View jixuanview = inflater.inflate(R.layout.sscmechine, null);
	   buyview.addView(jixuanview);
	   sensor.startAction();
	   this.ballOne = balles;
	   zhumaView = (LinearLayout)jixuanview. findViewById(R.id.buy_danshi_jixuan_linear_zhuma);
	   zhumaView.removeAllViews();
	   toastjixuan = Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT);
	   toastjixuan.show();
	   balls = new Vector<Balls>();
			// 初始化spinner
			jixuanZhu = (Spinner) jixuanview.findViewById(R.id.buy_danshi_jixuan_spinner);
			if(isbigsmall){
			TextView textview = (TextView)jixuanview.findViewById(R.id.TextView01);
			textview.setText("注数:一注");
			jixuanZhu.setVisibility(View.GONE);	
			jixuanZhu.setSelection(0);
			}else{
			jixuanZhu.setSelection(4);
			jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = jixuanZhu.getSelectedItemPosition();
					if (isOnclik) {
						zhumaView.removeAllViews();
						balls = new Vector();
						for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
							Balls ball = ballOne.createBalls();
							balls.add(ball);
						}
						createTable(zhumaView);
					} else {
						isOnclik = true;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
		}
   
		int index = jixuanZhu.getSelectedItemPosition() + 1;
		for (int i = 0; i < index; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
		sensor.onVibrator();// 震动
		Button zixuanTouzhu = (Button)jixuanview. findViewById(R.id.buy_danshi_jixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		final TextView textNum = (TextView)findViewById(R.id.buy_zixuan_add_text_num);
		Button add_dialog = (Button) findViewById(R.id.buy_zixuan_img_add_delet);
		addView = new AddView(textNum,this,false);
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(addView.getSize()>0){
					showAddViewDialog();
				}else{
					 Toast.makeText(ZixuanAndJiXuan.this, ZixuanAndJiXuan.this.getString(R.string.buy_add_dialog_alert), Toast.LENGTH_SHORT).show();	
				}
			}
		});
		Button add = (Button) findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
               addJxToCodes();
			}
		
		});

	}
	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addJxToCodes() {
			if(addView.getSize()+balls.size()-1>=All_ZHU){
				Toast.makeText(ZixuanAndJiXuan.this,ZixuanAndJiXuan.this.getString(R.string.buy_add_view_zhu_alert) , Toast.LENGTH_SHORT).show();
			}else{
				if(type == BIG_SMALL){
					getJxCodeInfoDX(addView);
				}else{
					getJxCodeInfo(addView);
				}
				addView.updateTextNum();
				againJx();
			}
	}
	
	public void getJxCodeInfo(AddView addView){
		for(int i=0;i<balls.size();i++){
			Balls ball = balls.get(i);
			String codeStr = getAddZhuma(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			codeInfo.addAreaCode(codeStr,Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}   
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}
	public void getJxCodeInfoDX(AddView addView){
		for(int i=0;i<balls.size();i++){
			Balls ball = balls.get(i);
			String codeStr = getAddZhumaDX(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			codeInfo.addAreaCode(codeStr,Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}   
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}
	/**
	 * 重新选择方法
	 */
	private void againJx() {
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	/**
	 * 购彩篮显示注码
	 * @return
	 */
	private String getAddZhuma(int index){
		String zhumaString = "";
			for(int j=0;j<balls.get(index).getVZhuma().size();j++){
				if(isTen){
					zhumaString += balls.get(index).getTenShowZhuma(j);
				}else{
					zhumaString += balls.get(index).getShowZhuma(j);
				}
				
				if(j!=balls.get(index).getVZhuma().size()-1){
					zhumaString += "|";
				}
			}
		return zhumaString ;	
	}
	/**
	 * 购彩篮显示注码
	 * @return
	 */
	private String getAddZhumaDX(int index){
		String zhumaString = "";
			for(int j=0;j<balls.get(index).getVZhuma().size();j++){
				 String str =balls.get(index).getTenShowZhuma(j);
				if(str.equals("00")){
					zhumaString += "大";
				}else if(str.equals("01")){
					zhumaString += "小";
				}else if(str.equals("02")){
					zhumaString += "单";
				}else if(str.equals("03")){
					zhumaString += "双";
				}
				if(j!=balls.get(index).getVZhuma().size()-1){
					zhumaString += "|";
				}
			}
		return zhumaString ;	
	}
	//机选创建小球
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
			    String color = (String) balls.get(i).getVColor().get(j);
			    TableLayout table;
			    if(isbigsmall){
				table = PublicMethod.makeBallTableJiXuanbigsmall(null,iScreenWidth,BallResId, balls.get(i).getBalls(j), this); 	
			    }else
			    {
				table = PublicMethod.makeBallTableJiXuan(null,iScreenWidth,BallResId, balls.get(i).getBalls(j), this);
			    }
				lines.addView(table);
			}	
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhumaView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhumaView);
					} else {
						Toast.makeText(ZixuanAndJiXuan.this, getResources().getText(R.string.zhixuan_jixuan_toast),Toast.LENGTH_SHORT).show();

					}
   
				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(10, 5, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
            if(i%2==0){
				lines.setBackgroundResource(R.drawable.jixuan_list_bg);
			}
            lines.setPadding(0, 3, 0, 0);
			layout.addView(lines);
			
		}

	}
	/**
	 * 投注方法
	 */
	private void beginTouZhu() {
            if(isJiXuan){
            	touZhuJX();
            }else{
            	touZhuZX();
            }
	}
	public void touZhuJX(){
		if (balls.size() == 0) {
			alertInfo("请至少选择1注彩票");
		} else {
			if(addView.getSize()==0){
				addJxToCodes();
				alertJX(); 
			}else{
				showAddViewDialog();
			}
			
		}
	}
	public void touZhuZX(){
		String alertStr = isTouzhu();
		if(alertStr.equals("true")&&addView.getSize()==0){
			addToCodes();
			alertZX();
		}else if(alertStr.equals("true")&&addView.getSize()>0){
			addToCodes();
			showAddViewDialog();
		}else if(addView.getSize()>0){
			showAddViewDialog();
		}else if(alertStr.equals("false")){
			dialogExcessive();
		}else{
			alertInfo(alertStr);
		}
	}
	
	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
	private void setSeekWhenAddOrSub(int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu,View view) {
		ImageButton subtractBeishuBtn = (ImageButton) view.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}

	/**
	 * seekBar组件的监听器
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
//			changeTextSumMoney();
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
		alertText.setText(getTouzhuAlert());

	}

	/**
	 * 计算注数和金额的方法
	 * 
	 */
	public void changeTextSumMoney() {     
		String text = textSumMoney(areaNums,iProgressBeishu);
		if(toast == null){
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}else{
			toast.setText(text);
			toast.show();
		}

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
	 * @return BallTable
	 */
	public BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick,boolean isTen,String num) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart,context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 8; // 时时彩设置每列小球的个数为10
		if(isTen){
			 viewNumPerLine = 10;
		}
		
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine - 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);

			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
				TextView textView = new TextView(context);
				textView.setText(num);
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "";
				// PublicMethod.myOutput("-----------"+iBallViewNo);
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
				TextView textView = new TextView(context);
				textView.setText(""+iBallViewNo);
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int iBallId = v.getId();
		isBallTable(iBallId);
		showEditText(highttype);
		changeTextSumMoney();

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
			iBallId = iBallId - areaNums[i].areaNum;
			if(iBallId<0){
				  areaNums[i].table.changeBallState(areaNums[i].chosenBallSum, nBallId);
				  break;
			}

	     }

	}
	
	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText(String type){
         SpannableStringBuilder builder = new SpannableStringBuilder(); 
		 String zhumas = "";
		 int num = 0;//高亮小球数
		 int length = 0;
			for(int j=0;j<areaNums.length;j++){
				BallTable ballTable = areaNums[j].table;
				int[] zhuMa = ballTable.getHighlightBallNOs();
				if(j!=0){
				    zhumas +=" + ";
				}
				for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
					if(type.equals("SSC")){
					zhumas += (zhuMa[i])+"";
					}else if(type.equals("DLC")){
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
					}
					if (i != ballTable.getHighlightBallNOs().length - 1){
						zhumas += ",";
					}
				}
				num+=zhuMa.length;
			}
             if(num==0){
            	 editZhuma.setText("");
            	 showEditTitle(this.type);
             }else{
				builder.append(zhumas);
				String zhuma[]=zhumas.split("\\+");
				  for(int i=0;i<zhuma.length;i++){
					  if(i!=0){
							length += zhuma[i].length()+1;
						  }else{
							length += zhuma[i].length();
						  }
				     if(i!=zhuma.length-1){	
						  builder.setSpan(new ForegroundColorSpan(Color.BLACK), length, length+1, Spanned.SPAN_COMPOSING);  
					  }
					  builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor), length-zhuma[i].length(), length, Spanned.SPAN_COMPOSING);  

				  }
			    editZhuma.setText(builder, BufferType.EDITABLE);
			    showEditTitle(NULL);
             }
	}
	

	
	public void alertJX() {
		sensor.stopAction();
		if(touZhuDialog == null){
			initTouZhuDialog(getTouzhuAlert());
		}else{
		    initAlerDialog(getTouzhuAlert());
		}
		touZhuDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});

	}
	public void alertZX() {
		if(touZhuDialog == null){
			initTouZhuDialog(getTouzhuAlert());
		}else{
		    initAlerDialog(getTouzhuAlert());
		}
	}
	/**
	 * 初始化倍数和期数进度条
	 * @param view
	 */
    public void initImageView(View view){
		mSeekBarBeishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);
		mTextBeishu = (TextView) view.findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) view.findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		setProgressMax(2000);//设置倍数和期数最大值
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,mSeekBarBeishu, true,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,true,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,mSeekBarQishu, false,view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,false,view);
    }
	/**
	 * 第一次启动投注提示框
	 */
	public void initTouZhuDialog(String alert){
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu_new, null);
		RadioGroup group =(RadioGroup) v.findViewById(R.id.RadioGroup01);
		group.setVisibility(RadioGroup.GONE);
		touZhuDialog = new AlertDialog.Builder(this).setTitle("您选择的是").create();
		touZhuDialog.show();
		issueText =(TextView) v.findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		alertText =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		textZhuma =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitleAlert = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		issueText.setText(getBatchCode());
		alertText.setText(alert);
		textTitleAlert.setText("注码："+"共有"+addView.getSize()+"笔投注");
		addView.getCodeList().get(addView.getSize()-1).setTextCodeColor(textZhuma);
		codeInfo = (Button) v.findViewById(R.id.alert_dialog_touzhu_btn_look_code);
		isCodeText(codeInfo);
		codeInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addView.createCodeInfoDialog();
				addView.showDialog();
			}
		});
		initImageView(v);
		CheckBox checkPrize = (CheckBox) v.findViewById(R.id.alert_dialog_touzhu_check_prize);
		checkPrize.setChecked(true);
		checkPrize.setButtonDrawable(R.drawable.check_on_off);
		checkPrize.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					betAndGift.setPrizeend("1");
				}else{
					betAndGift.setPrizeend("0");
				}
			}
		});
		Button cancel = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				touZhuDialog.cancel();
				clearProgress();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(ZixuanAndJiXuan.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (sessionId.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(ZixuanAndJiXuan.this, UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					toLogin = false;
					touZhu();
				}
			}
	
		});
		
		touZhuDialog.setCancelable(false);
		touZhuDialog.getWindow().setContentView(v);
	}
	/**
	 * 用户投注
	 */
	private void touZhu() {
		initBet();
		touZhuDialog.cancel();
		touZhuNet();
		clearProgress();
	}
	private void isCodeText(Button codeInfo) {
		if(addView.getSize()>1){
			codeInfo.setVisibility(Button.VISIBLE);
		}else{
			codeInfo.setVisibility(Button.GONE);
		}
	}
	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu.setProgress(iProgressQishu);
	}
	/**
	 * 再次启动提示框
	 */
	public void initAlerDialog(String alert){
		clearProgress();
		issueText.setText(getBatchCode());
		alertText.setText(alert);
		textTitleAlert.setText("注码："+"共有"+addView.getSize()+"笔投注");
		addView.getCodeList().get(addView.getSize()-1).setTextCodeColor(textZhuma);
		isCodeText(codeInfo);
		touZhuDialog.show();
	}
	/**
	* 当前注数
	*/
	public void setZhuShu(int zhushu){
		iZhuShu = zhushu;
	}
	/**
	 * 自选投注提示框中的信息
	 */
	public String getTouzhuAlert(){
	return "注数：" + addView.getAllZhu() + "注    " + "金额：" + addView.getAllAmt()*iProgressBeishu+ "元" 
					+ "(冻结金额："
					+ (iProgressQishu - 1) * addView.getAllAmt()*iProgressBeishu
					+ "元)"; 
	}

	/**
	 * 返回当前期数
	 * @return
	 */
	public String getBatchCode(){
		String batchCode = "";
		if(highttype.equals("SSC")){
			batchCode = Ssc.batchCode;
		 }else if(highttype.equals("DLC")){
			batchCode = Dlc.batchCode;
		 }
		return batchCode += "期" ;
	}
	   /**
	    * 机选提醒框注码
	    * @return
	    */
	   public String getJxAlertZhuma(){
			String zhumaString = "";
			for (int i = 0; i < balls.size(); i++) {
				for(int j=0;j<balls.get(i).getVZhuma().size();j++){
					if(highttype.equals("SSC")){
						zhumaString += balls.get(i).getSpecialShowZhuma(j);
					 }else if(highttype.equals("DLC")){
						 zhumaString += balls.get(i).getTenSpecialShowZhuma(j);
					 }
					
					if(j!=balls.get(i).getVZhuma().size()-1){
						zhumaString += ",";
					}
				}
				if (i != balls.size() - 1) {
					zhumaString += "\n";
				}
			}
			codeStr = "注码：\n"+zhumaString;
			 return codeStr;	 
	   }

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}
	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
							}
						});
		dialog.show();

	}
	/**
	 * 单笔投注大于1万注时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ZixuanAndJiXuan.this);
		builder.setTitle(ZixuanAndJiXuan.this.getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于1万注！");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}
	
	/**
	 * 重新方法
	 * 
	 */
	public void again(){
		if(areaNums!=null){
		for(int i=0;i<areaNums.length;i++){
            areaNums[i].table.clearAllHighlights();					
		}
		showEditText(highttype);
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
//			touZhu();
			break;
		}
	}
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!toLogin){
			sensor.stopAction();
		}
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isTime = true;
		isStart = false;
		isViewEnd = false;
		prizeInfos = null;
		lotnoStr = "";
	}
	
	/**
	 * 重新初始化机选界面
	 */
	public void againView(){
	    sensor.startAction();
	    sensor.onVibrator();// 震动
	    toastjixuan.show();
	    jixuanZhu.setSelection(4);
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	/**
	 * 实现震动的类
	 * @author Administrator
	 *
	 */
	class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
				zhumaView.removeAllViews();
				balls = new Vector<Balls>();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = ballOne.createBalls();
					balls.add(ball);
				}
				createTable(zhumaView);
		}
	}
	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		touzhuNet();
		betAndGift.setAmount(""+addView.getAllAmt()*iProgressBeishu*100);
		betAndGift.setIsSellWays("1");
		betAndGift.setBet_code(addView.getTouzhuCode(iProgressBeishu,betAndGift.getAmt()*100));
		
	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);		
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error,msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}
   
//	/**
//	 * 开奖信息联网 获取信息
//	 *  time单位是分
//	 */
//	public static void prizeInfoNet(){
//		startTime = java.lang.System.currentTimeMillis();
//		Thread prizeThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//			while(isStart){
//				try {
//					prizeNet();
//					Thread.sleep(TIME);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			  }
//			}
//
//		});
//		prizeThread.start();
//	}
	
	/**
	 * 刷新界面
	 * 
	 */
//	public void updateViewThread(){
//		endTime = java.lang.System.currentTimeMillis()-startTime;
//		isViewEnd = true;
//		final Handler handler = new Handler();
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//			while(isViewEnd){
//				try {
//				if(prizeInfos ==null){
//					 prizeNet();
//			      }
//				
//			    handler.post(new Runnable() {
//					public void run() {
//						setPrizeInfo(prizeInfos);
//					}
//				}); 
//			    Long differenceTime = TIME - endTime;
//			    if(differenceTime>0){
//			    	Thread.sleep(differenceTime+2000);
//			    	endTime = TIME;
//			    }else{
//			    	Thread.sleep(TIME+2000);	
//			    }
//				
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			  }
//			}
//
//		});
//		thread.start();
//	}
//	static Handler handlers = new Handler();
//	/**
//	 * 获取最新开奖信息联网
//	 * @throws JSONException
//	 */
//	public static void prizeNet() throws JSONException{
//	
//		  handlers.post(new Runnable() {
//				public void run() {
//					 prizeText.setText("刷新开奖信息.....");
//					}
//				});
//		final JSONObject obj = PrizeInfoInterface.getInstance().getNoticePrizeInfo(lotnoStr,PAGE, MAX);
//			String msg = obj.getString("message");
//			String error = obj.getString("error_code");
//			String title ;
//			if(error.equals("0000")){//查询成功
//	          prizeInfos = obj.getJSONArray("result");
//	          handlers.post(new Runnable() {
//					public void run() {
//					 prizeText.setText("最新开奖");
//					}
//				});
//			}else{
//			  handlers.post(new Runnable() {
//					public void run() {
//					  prizeText.setText("开奖信息刷新失败");
//					}
//				});
//			}
//	}
//	/**
//	 * 设置开奖信息
//	 * @param result
//	 */
//	public void setPrizeInfo(JSONArray result){
//	try{
//		if(zhixuanview!=null&&result!=null){
//		LinearLayout layout = (LinearLayout)zhixuanview.findViewById(R.id.ssczhixuan_layout_prize_num);
//		layout.removeAllViews();
//		for(int i=0;i<result.length();i++){
//			TextView text = new TextView(this);
//			JSONObject info = result.getJSONObject(i);
//			text.setTextColor(Color.BLACK);
//			text.setTextSize(16);
//			text.setPadding(5, 5, 0, 0);
//			text.setText(info.getString("batchCode")+"期: "+formtPrizeInfo(info.getString("winCode")));
//			layout.addView(text);
//		 }
//	  }
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	}
	/**
	 * 格式化开奖号码
	 */
	public String formtPrizeInfo(String info){
		if(info.length()>5){
			return info;
		}else{
			String returnStr = "";
			int start = 0;
			while(start < info.length()){
				returnStr += info.substring(start,start+=1);
				if(start != info.length()){
					returnStr += ",";
				}
			}
			return returnStr;
		}
	
	}
	/**
	 * 设置倍数和期数最大值
	 * @param max
	 */
	public void setProgressMax(int max){
		mSeekBarBeishu.setMax(max);
		mSeekBarQishu.setMax(max);
	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String code= addView.getsharezhuma();
		clearArea();
		for(int i=0;i<areaNums.length;i++){
             areaNums[i].table.clearAllHighlights();
	     }
		editZhuma.setText("");
		PublicMethod.showDialog(ZixuanAndJiXuan.this,lotno+code);
	}
	public void clearArea(){
		if(addView != null){
			addView.clearInfo();
			addView.updateTextNum();
		}
	}
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}    
	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(string).setNeutralButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
                                    finish();
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
		dialog.show();

	}
	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			if(addView.getSize()!=0){
				alertExit(getString(R.string.buy_alert_exit));
			}else{
				finish();
			}
			break;
		}
		return false;
	}
}
