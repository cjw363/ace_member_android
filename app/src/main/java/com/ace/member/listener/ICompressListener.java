package com.ace.member.listener;


import java.io.File;
import java.util.Map;

public interface ICompressListener {
	void onStart(String path);

	void onCompressing(int current, int total);

	void onFinish(Map<String, File> map);

	void onError();

	public class SimpleCompressListener implements ICompressListener {

		@Override
		public void onStart(String path) {

		}

		@Override
		public void onCompressing(int current, int total) {

		}

		@Override
		public void onFinish(Map<String, File> map) {

		}

		@Override
		public void onError() {

		}
	}
}
