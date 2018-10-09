package com.ace.member.main.me.system_update;


public interface SystemUpdateContract {
	interface View {
		void showCheckForDialog();

		void setUpdateLog(String log);

		void enableContent(boolean enable);

		void enableDownloadButton(boolean enable);

		void enableDownloadProgress(boolean enable);

		void setPbProgress(int progress);

		void setTvProgressText(String progress);

		void finish();
	}

	interface Presenter {
		void start();

		void stop();

		void download();
	}
}
