package com.ruyicai.dialog;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.HomeActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;

/**
 * 创建快捷方式
 * 
 * @author Administrator
 * 
 */
public class ShortcutDialog extends BaseDialog {
	Activity activity;

	public ShortcutDialog(Activity activity, String title, String message) {
		super(activity, title, message);
		this.activity = activity;
	}

	@Override
	public void onOkButton() {
		//addShortcut();
	}

	@Override
	public void onCancelButton() {

	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	private void addShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				activity.getString(R.string.app_name));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 快捷方式的动作
		Intent myIntent = new Intent(activity, HomeActivity.class);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				activity, R.drawable.icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		activity.sendBroadcast(shortcut);
	}

}
