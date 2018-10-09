package com.og.utils;


import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {

	public static EventBus getBus(){
		return EventBus.getDefault();
	}

	public static void register(Object o){
		getBus().register(o);
	}

	public static void unRegister(Object o){
		getBus().unregister(o);
	}

	public static void post(Object o){
		getBus().post(o);
	}
}
