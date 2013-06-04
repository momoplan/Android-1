package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.activity.buy.jc.oddsprize.JCPrizePermutationandCombination;
import com.ruyicai.activity.buy.jc.zq.view.BFView.JcInfoAdapter;
import com.ruyicai.activity.buy.jc.zq.view.BFView.JcInfoAdapter.ViewHolder;
import com.ruyicai.code.jc.zq.FootBF;
import com.ruyicai.code.jc.zq.FootBQC;
import com.ruyicai.code.jc.zq.FootHun;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

/**
 * 混合投注
 * 
 * @author win
 * 
 */
public class HunHeZqView extends JcMainView {

	protected int CHECK_TEAM = 8;// 最多串几场比赛
	JcInfoAdapter adapter;
	FootHun footHunCode;

	public HunHeZqView(Context context, BetAndGiftPojo betAndGift,
			Handler handler, LinearLayout layout, String type,
			boolean isDanguan, List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isDanguan, checkTeam);
		// TODO Auto-generated constructor stub
		footHunCode = new FootHun(context);
	}

	public boolean isHunHe() {
		return true;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jczq_hunhe_guoguan_title).toString();
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		itemInfo.setLevel(jsonItem.getString("v1"));
		itemInfo.vStrs = new String[51];
		int spf_lenght = 3;
		int bqc_lenght = 9;
		int zjq_lenght = 8;
		int bf_lenght = 31;
		for (int j = 0; j < bqc_lenght; j++) {
			itemInfo.getVStrs()[j + spf_lenght] = jsonItem.getString("half_v"
					+ FootHun.bqcType[j + spf_lenght]);
		}
		for (int j = 0; j < zjq_lenght; j++) {
			itemInfo.getVStrs()[j + spf_lenght + bqc_lenght] = jsonItem
					.getString("goal_v" + j);
		}
		for (int j = 0; j < bf_lenght; j++) {
			itemInfo.getVStrs()[j + spf_lenght + bqc_lenght + zjq_lenght] = jsonItem
					.getString("score_v"
							+ FootHun.bqcType[j + spf_lenght + bqc_lenght
									+ zjq_lenght]);
		}
		initTitles(itemInfo);
	}

	private void initTitles(final Info info) {

		info.vStrs[0] = info.getWin();
		info.vStrs[1] = info.getLevel();
		info.vStrs[2] = info.getFail();

	}

	/**
	 * 获取单关投注的中奖金额最大金额和最小金额的字符串
	 * 
	 * @return
	 */
	public String getDanPrizeString(int muti) {

		return JCPrizePermutationandCombination.getNewDanGuanPrize(
				getOddsArraysValue(), muti);
	}

	@Override
	public int getTeamNum() {
		// TODO Auto-generated method stub
		return CHECK_TEAM;
	}

	@Override
	public void setTeamNum(int checkTeam) {
		// TODO Auto-generated method stub
		CHECK_TEAM = checkTeam;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}

	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCZQ_HUN;
	}

	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jczq_dialog_sfc_guoguan_title)
				.toString();
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {

		return footHunCode.getCode(key, listInfo);
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += info.getHome() + " vs " + info.getAway() + "：";
				for (int j = 0; j < info.check.length; j++) {
					if (info.check[j].isChecked()) {
						codeStr += info.check[j].getChcekTitle() + " ";
					}
				}
				if (info.isDan()) {
					codeStr += "(胆)";
				}
				codeStr += "\n\n";
			}
		}
		return codeStr;
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		// TODO Auto-generated method stub
		adapter = new JcInfoAdapter(context, listInfo);
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;

		public JcInfoAdapter(Context context, List<List> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		int index;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
			final ArrayList<Info> list = (ArrayList<Info>) mList.get(position);
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_view_list_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			if (list.size() == 0) {
				holder.btn.setVisibility(Button.GONE);
			} else {
				isOpen(list, holder);
				holder.btn.setText(list.get(0).getTime() + "  " + list.size()
						+ context.getString(R.string.jc_main_btn_text));
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						list.get(0).isOpen = !list.get(0).isOpen;
						isOpen(list, holder);
					}
				});
				for (Info info : list) {
					holder.layout.addView(addView(info)/*addLayout(info)*/);
				}
			}

			return convertView;
		}

		private void isOpen(final ArrayList<Info> list, final ViewHolder holder) {
			if (list.get(0).isOpen) {
				holder.layout.setVisibility(LinearLayout.VISIBLE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_open);
			} else {
				holder.layout.setVisibility(LinearLayout.GONE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			}
		}
		
		// add by yejc 20130402
		private View addView(final Info info) {
			View convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_others, null);
			TextView gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			TextView gameDate = (TextView) convertView
					.findViewById(R.id.game_date);

			final TextView homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
//			homeTeam.getPaint().setFakeBoldText(true);
			final TextView textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			if (!"".equals(info.getLetPoint()) && !"0".equals(info.getLetPoint())) {
				textVS.setText(info.getLetPoint());
			}
			final TextView guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
//			guestTeam.getPaint().setFakeBoldText(true);

			TextView btn = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button);

			TextView analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.game_dan);

			gameName.setText(info.getTeam());
			String date = getWeek(info.getWeeks()) + " " + info.getTeamId() + "\n"
					+ getEndTime(info.getTimeEnd()) + " " + "(截)";
			gameDate.setText(date);
			homeTeam.setText(info.getHome());

			gameName.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (context instanceof JcMainActivity) {
						JcMainActivity activity = (JcMainActivity) context;
						activity.createTeamDialog();
					}
				}
			});

			// textVS.setText(info.getWin());
			guestTeam.setText(info.getAway());

			if (!info.getBtnStr().equals("")) {
				btn.setText(info.getBtnStr());
			}
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.createDialog(FootHun.titleStrs, true,
								info.getHome() + " VS " + info.getAway());
					}
					isNoDan(info, btnDan);
				}
			});
			if (isDanguan || isHunHe()) {
				btnDan.setVisibility(Button.GONE);
			} else {
				btnDan.setVisibility(Button.VISIBLE);
				btnDan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (info.isDan()) {
							info.setDan(false);
							btnDan.setBackgroundResource(R.drawable.jc_btn);
						} else if (info.onclikNum > 0 && isDanCheckTeam()
								&& isDanCheck()) {
							info.setDan(true);
							btnDan.setBackgroundResource(R.drawable.jc_btn_b);
						}
					}
				});
			}
			analysis.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trunExplain(getEvent(Constants.JCFOOT, info),
							info.getHome(), info.getAway());
				}
			});
			return convertView;
		}
		// end

		private View addLayout(final Info info) {
			View convertView;
			convertView = mInflater.inflate(R.layout.buy_jc_main_listview_item,
					null);
			LinearLayout layout1 = (LinearLayout) convertView
					.findViewById(R.id.lq_sf_layout_bottom1);
			LinearLayout layout2 = (LinearLayout) convertView
					.findViewById(R.id.lq_sf_layout_bottom2);
			layout1.setVisibility(LinearLayout.GONE);
			layout2.setVisibility(LinearLayout.VISIBLE);
			TextView time = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_time);
			TextView team = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team);
			TextView home = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name1);
			TextView away = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name2);
			TextView timeEnd = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_time_end);
			TextView score = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_score);
			TextView btn = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.jc_main_list_item_btn_dan);
			final Button btnXi = (Button) convertView
					.findViewById(R.id.buy_jc_main_list_item_btn_xi);

			score.setVisibility(TextView.GONE);
			time.setText(info.getTime() + "  "
					+ context.getString(R.string.jc_main_team_id_title)
					+ info.getTeamId());
			team.setText(info.getTeam());
			home.setText(info.getHome() + "(主)");
			away.setText(info.getAway() + "(客)");
			timeEnd.setText(info.getTimeEnd());

			if (!info.getBtnStr().equals("")) {
				btn.setText(info.getBtnStr());
			}
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.createDialog(FootHun.titleStrs, true,
								info.getHome() + " VS " + info.getAway());
					}
					isNoDan(info, btnDan);
				}
			});
			btnDan.setVisibility(Button.GONE);
			btnXi.setVisibility(Button.VISIBLE);
			btnXi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trunExplain(getEvent(Constants.JCFOOT, info),
							info.getHome(), info.getAway());
				}
			});
			return convertView;
		}

		class ViewHolder {
			Button btn;
			LinearLayout layout;

		}
	}

	@Override
	public String getPlayType() {
		// TODO Auto-generated method stub
		if (isDanguan) {
			return "J00002_0";
		} else {
			return "J00002_1";
		}
	}

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return footHunCode.getOddsList(listInfo);
	}

	public List<double[]> getMinOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return footHunCode.getMinOddsList(listInfo);
	}

	/**
	 * 最多可以设胆7场比赛
	 */
	public boolean isDanCheckTeam() {
		return false;
	}

}
