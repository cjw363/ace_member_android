package com.og.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import com.og.M;
import com.og.R;
import com.og.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class DialogFactory {

	public static ProgressDialog pDialog;
	public static int DLG_TYPE_1_CLOSE = 1; // Close / Cancel
	public static int DLG_TYPE_2_CONFIRM = 2; // Yes - No
	private static CustomDialog mCurrentDlg;
	public static String mCurrentMsg = "";
	public static Context mCurrentContext;
	private static int btnPositiveTitle = 0, btnNegativeTitle = 0;

	public static void setBtnTitle(int positive, int negative){
		btnPositiveTitle = positive;
		btnNegativeTitle = negative;
	}

	/**
	 * DialogFactory.ToastDialog(NewBillActivity.this, "Test","test ok",0,null);
	 * DialogFactory.ToastDialog(CustomerAcceptActivity.this, mTitle,"Customer Accepted",CLOSE_CUR_ACTIVITY);
	 *
	 * @param what 当有后续动作会导致该弹窗自动关闭时，需要 what和handle参数
	 */
	public static void ToastDialog(Context context, String msg, int what) {
		ToastDialogBase(context, msg, what, 0, 0,DLG_TYPE_1_CLOSE);
	}

	public static void ToastDialog2(Context context, String msg, int what, int layoutID,int textColor) {
		ToastDialogBase(context,msg, what, layoutID,textColor, DLG_TYPE_1_CLOSE);
	}

//	public static void ToastDialogConfirm(Context context, String title, String msg, int what, int layoutID) {
//		ToastDialogBase(context, title, msg, what, layoutID, DLG_TYPE_2_CONFIRM);
//	}

	public static void ToastDialogBase(final Context context, final String msg, final int what, int layoutID,int textColor,int dlgType) {
		try{
			if (context == null || ((Activity) context).isFinishing()) return;
			if (mCurrentDlg != null) {
				if (mCurrentContext != null && mCurrentContext != context || !mCurrentDlg.isShowing()) {
					close();
				} else if (msg.equals(mCurrentMsg)) {
					Log.e("mCurrentMsg", " same msg: " + msg);
					return;
				}
			}
			mCurrentMsg = msg;
			mCurrentContext = context;

			CustomDialog.Builder builder = new CustomDialog.Builder(context, layoutID);
			if(!TextUtils.isEmpty(mCurrentMsg)) builder.setMessage(mCurrentMsg);
			if(textColor>0) builder.setMessageColor(textColor);
			if (dlgType == DLG_TYPE_1_CLOSE) {
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mCurrentMsg = "";
						dialog.dismiss();
						if (what != 0) {
							EventBus.getDefault().post(new MessageEvent(what));
						}
					}
				});
			}
//			else if (dlgType == DLG_TYPE_2_CONFIRM) {
//				builder.setPositiveButton(btnPositiveTitle>0 ? btnPositiveTitle : R.string.btn_yes, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						if (Utils.isFastClick(context)) return;
//						mCurrentMsg = "";
//						dialog.dismiss();
//						if (what != 0) {
////							Message mg = Message.obtain();
////							mg.what = what;
////							Bundle b = new Bundle();
////							b.putInt("confirm", LibGlobal.YES);
////							mg.setData(b);
////							handler.sendMessage(mg);
//						}
//					}
//				});
//				builder.setNegativeButton(btnNegativeTitle>0 ? btnNegativeTitle : R.string.btn_no, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						mCurrentMsg = "";
//						dialog.dismiss();
//						if (what != 0) {
//							Message mg = Message.obtain();
//							mg.what = what;
//							Bundle b = new Bundle();
//							b.putInt("confirm", LibGlobal.NO);
//							mg.setData(b);
////							handler.sendMessage(mg);
//						}
//					}
//				});
//			}

			mCurrentDlg = builder.create();
			mCurrentDlg.setCancelable(false);
			mCurrentDlg.show();
//			setBtnTitle(0, 0);
		}catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static void close(){
		try{
			if(mCurrentDlg != null){
				mCurrentDlg.dismiss();
			}
		}catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static void block(Context context, String msg) {
		try{
			unblock();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(msg);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			setUnblockListener();
		} catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	public static void unblock() {
		try{
			if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
		}catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	/**
	 * 关闭超过5秒未能自动关闭的等待窗口(waiting..., checking...,)
	 */
	private static void setUnblockListener() {
		try{
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_441_CLOSE_BLOCKING_DIALOG));
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, 5000);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
