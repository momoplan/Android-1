package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.secure.MobileSecurePayHelper;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import com.umpay.creditcard.android.UmpayActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 联动优势话费充值
 * 
 * @author win
 * 
 */
public class UmPayPhoneActivity extends Activity implements HandlerMsg {
	public ProgressDialog progressdialog;
//	private final String YINTYPE = "0900";
	Button secureOk, secureCancel;
	EditText accountnum;
	private TextView alipay_content = null;
//	private String sessionId = "";
//	private String userno = "";
//	private String message = "";
	private MyHandler handler = new MyHandler(this);
	private TextView accountTitleTextView = null;
	private final int REQUESTCODE = 1;
	private final int CREDIT_CARD_RECHARGE = 1; //信用卡充值
	private final int DEBIT_CARD_RECHARGE = 8; //借记卡充值
	private String orderId = "";
	private String money = "";
	public static final String UMPAY_RECHARGE_AMOUNT = "UMPAY_RECHARGE_AMOUNT";
	RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		initTextViewContent();
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText("话费充值");
		TextView title = (TextView)findViewById(R.id.recharge_title);
		title.setText(R.string.umpay_phone_text_moneny);
		LinearLayout layout = (LinearLayout)findViewById(R.id.umpay_phone_linear);
		layout.setVisibility(View.VISIBLE);
		radioGroup = (RadioGroup)findViewById(R.id.umpay_recharge_radiogroup);

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		secureCancel = (Button) findViewById(R.id.alipay_secure_cancel);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		accountnum.setVisibility(View.GONE);
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginUmpayRecharge();
			}
		});
		secureCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UmPayPhoneActivity.this.finish();
			}
		});
	}

	private void initTextViewContent() {
		alipay_content = (TextView) findViewById(R.id.alipay_content);
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = RechargeDescribeInterface.getInstance()
						.rechargeDescribe("umpayChargeDescription");
				try {
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
							alipay_content.setText(conten);
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	// 联动优势充值
	private void beginUmpayRecharge() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivity(intentSession);
		} else {
			// 检测安全支付服务是否被安装，
			MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
			boolean isInstall = mspHelper.detectMobile_sp(Constants.PAY_PLUGIN_NAME, Constants.UMPAY_PHONE_PACK_NAME);
			
			if (isInstall) {
				Log.i("yejc", "==========radioGroup.getCheckedRadioButtonId()="+radioGroup.getCheckedRadioButtonId()
						+" ================R.id.umpay_phone_three="+R.id.umpay_phone_three
						+" ================R.id.umpay_phone_thirty="+R.id.umpay_phone_thirty);
//				switch (radioGroup.getCheckedRadioButtonId()) {
//				case R.id.umpay_phone_three:
//					money = "3";
//					break;
//				case R.id.umpay_phone_thirty:
//					money = "30";
//					break;
//
//				default:
//					break;
//				}
				int id = radioGroup.getCheckedRadioButtonId();
				if (id == R.id.umpay_phone_thirty) {
					money = "30";
				} else {
					money = "3";
				}
				
				Intent intent = new Intent(this, UmPayPhonePopActivity.class);
				intent.putExtra(UMPAY_RECHARGE_AMOUNT, money);
				startActivity(intent);
			}
		}
	}

	// 充值
//	private void recharge(final RechargePojo rechargepojo) {
//		RWSharedPreferences pre = new RWSharedPreferences(UmPayPhoneActivity.this,
//				"addInfo");
//		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
//		userno = pre.getStringValue(ShellRWConstants.USERNO);
//		ConnectivityManager ConnMgr = (ConnectivityManager) this
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
//		if (info.getExtraInfo() != null
//				&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
//			Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
//					Toast.LENGTH_LONG).show();
//		}
//		progressdialog = UserCenterDialog.onCreateDialog(UmPayPhoneActivity.this);
//		progressdialog.show();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				String error_code = "00";
//				message = "";
//				try {
//					rechargepojo.setSessionid(sessionId);
//					rechargepojo.setUserno(userno);
//					String re = RechargeInterface.getInstance().recharge(
//							rechargepojo);
//					JSONObject obj = new JSONObject(re);
//					error_code = obj.getString("error_code");
//					message = obj.getString("message");
//					if (error_code.equals("0000")) {
//						orderId = obj.getString("orderId");
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				handler.handleMsg(error_code, message);
//				progressdialog.dismiss();
//			}
//		}).start();
//	}

	@Override
	public void errorCode_0000() {
		turnUMPayView();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * 银联充值跳转到插件
	 */
	public void turnUMPayView() {
		Intent intent = new Intent(this, UmpayActivity.class);
		intent.putExtra("tradNo", orderId);
        intent.putExtra("payType", CREDIT_CARD_RECHARGE + DEBIT_CARD_RECHARGE);
        startActivityForResult(intent, REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String message = data.getStringExtra("resultMessage");//支付结果描述
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
}