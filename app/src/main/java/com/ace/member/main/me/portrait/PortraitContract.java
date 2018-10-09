package com.ace.member.main.me.portrait;

import com.ace.member.bean.PortraitBean;
import com.ace.member.main.image_detail.Image;

import java.util.List;

public interface PortraitContract {

	interface View {
		void setPortrait(PortraitBean portrait);

		void showSuccess();

		void enableSubmit(boolean enable);
	}

	interface Presenter {
		void start();

		void upload(List<Image> list);
	}
}
