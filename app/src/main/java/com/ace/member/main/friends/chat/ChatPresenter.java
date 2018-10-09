package com.ace.member.main.friends.chat;

import android.content.Context;
import android.os.Environment;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ChatMsg;
import com.ace.member.bean.DBChatContentInfo;
import com.ace.member.bean.DBTransferInfo;
import com.ace.member.bean.FriendsDataWrapper;
import com.ace.member.main.friends.db.TableDB;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.utils.Session;
import com.ace.member.utils.StringUtil;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ChatPresenter extends BasePresenter implements ChatContract.ChatPresenter {
	private static final String CHAT_TYPE_1_FRIEND = "1";
	private static final String CHAT_TYPE_2_GROUP = "2";
	private static String CHAT_TYPE = "0";

	private boolean isFirstLoad = true;

	private final ChatContract.ChatView mChatView;
	private List<ChatMsg> mList = new ArrayList<>();

	@Inject
	public ChatPresenter(Context context, ChatContract.ChatView chatView) {
		super(context);
		mChatView = chatView;
	}

	public void clearData() {
		mList.clear();
	}

	@Override
	public void checkUpdateChatMsg(final int chatId, final int currentPage) {
		String lmt = StringUtil.checkStr(ChatDao.getInstance().queryChatRemoteIdLmt(chatId));
		String timeLastRead = StringUtil.checkStr(ChatDao.getInstance().getTimeLastRead(chatId));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("chat_id", chatId + "");
		map.put("lmt", lmt);
		map.put("time_last_read", timeLastRead);
		map.put("cmd", "getLastChatMsg");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					ChatDao.getInstance().insertTable(friendsDataWrapper);
					getChatMsg(chatId, currentPage);
					mChatView.setMute(friendsDataWrapper.getFlagMuteNotifications());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				getChatMsg(chatId, currentPage);//检查更新失败也要加载本地数据
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void updateChatMsg(int chatId) {
		ChatDao.getInstance().updateChatMsg(chatId);//更新状态
	}

	/**
	 * 因为数据库未插入chatId,从服务器下载会话所有的消息和更新联系人列表
	 */
	@Override
	public void getNewChatMsg(int memberId) {
		String chatMemberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_CHAT_MEMBER));
		String friendRequestLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_FRIEND_REQUEST));
		String friendsLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_FRIENDS));
		String memberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_MEMBER));
		String transferLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_TRANSFER));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("member_id", memberId + "");
		map.put("chat_member_lmt", chatMemberLmt);
		map.put("friend_request_lmt", friendRequestLmt);
		map.put("friends_lmt", friendsLmt);
		map.put("member_lmt", memberLmt);
		map.put("transfer_lmt", transferLmt);
		map.put("cmd", "getNewChatMsg");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					ChatDao.getInstance().insertTable(friendsDataWrapper);
					mChatView.getNewChatMsgResult();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void getChatMsg(int chatId, int currentPage) {
		++currentPage;
		List<ChatMsg> chatMsgList = ChatDao.getInstance().queryChatByMsg(chatId, currentPage);
		if (chatMsgList != null) {
			mList.addAll(0, chatMsgList);//加到头部
			mChatView.showChatMsg(mList, currentPage, chatMsgList.size());
		}
		if (currentPage >= ChatDao.getInstance().getChatTotalPages(chatId)) {//已经没有更多数据了
			mChatView.noMoreData(isFirstLoad);
		}
		updateChatMsg(chatId);//消息列表展示成功后，更新消息的status
		if (isFirstLoad) isFirstLoad = false;
	}

	@Override
	public void sendMsg(String content, int contentType, final int memberId) {
		int chatId = ChatDao.getInstance().getChatId(memberId);
		String timeLastRead = StringUtil.checkStr(ChatDao.getInstance().getTimeLastRead(chatId));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("member_id", String.valueOf(memberId));
		map.put("content_type", String.valueOf(contentType));
		map.put("content", content);
		map.put("time_last_read", timeLastRead);
		map.put("cmd", "sendChatMsg");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {

				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					assert friendsDataWrapper != null;
					ChatDao.getInstance().insertTable(friendsDataWrapper);//插入数据库，就算插入失败也没有关系，以服务器为准
					List<DBChatContentInfo> chatContentList = friendsDataWrapper.getChatContentList();
					for (int i = 0; i < chatContentList.size(); i++) {
						DBChatContentInfo contentInfo = chatContentList.get(i);
						ChatMsg chatMsg = new ChatMsg(contentInfo.getId(), contentInfo.getContent(), contentInfo.getMemberId(), contentInfo.getType(), contentInfo.getTime());
						mChatView.addLastChatMsg(chatMsg);
					}

					updateChatMsg(ChatDao.getInstance().getChatId(memberId));
					IMWebSocketService.sendFriendMsg(memberId, IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//数据成功插入才去发送websocket
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	@Override
	public void addTransferMsg(int transferID, final int friendID) {
		int chatId = ChatDao.getInstance().getChatId(friendID);
		String timeLastRead = StringUtil.checkStr(ChatDao.getInstance().getTimeLastRead(chatId));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("cmd", "addTransferMsg");
		map.put("transfer_id", String.valueOf(transferID));
		map.put("friend_id", String.valueOf(friendID));
		map.put("time_last_read", timeLastRead);
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					assert friendsDataWrapper != null;
					ChatDao.getInstance().insertTable(friendsDataWrapper);//插入数据库，就算插入失败也没有关系，以服务器为准
					updateChatMsg(ChatDao.getInstance().getChatId(friendID));
					IMWebSocketService.sendFriendMsg(friendID, IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//数据成功插入才去发送websocket
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	@Override
	public void receiveMsg(final int chatId) {
		String lmt = StringUtil.checkStr(ChatDao.getInstance().queryChatRemoteIdLmt(chatId));
		String timeLastRead = StringUtil.checkStr(ChatDao.getInstance().getTimeLastRead(chatId));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("chat_id", chatId + "");
		map.put("lmt", lmt);
		map.put("time_last_read", timeLastRead);
		map.put("cmd", "receiveMsg");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {

				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					assert friendsDataWrapper != null;
					ChatDao.getInstance().insertTable(friendsDataWrapper);
					List<DBChatContentInfo> chatContentList = friendsDataWrapper.getChatContentList();
					List<DBTransferInfo> chatTransferList = friendsDataWrapper.getTransferList();
					for (int i = 0; i < chatContentList.size(); i++) {
						DBChatContentInfo contentInfo = chatContentList.get(i);
						ChatMsg chatMsg = new ChatMsg(contentInfo.getId(), contentInfo.getContent(), contentInfo.getMemberId(), contentInfo.getType(), contentInfo.getTime());
						for (DBTransferInfo transferInfo : chatTransferList) {
							if (contentInfo.getTransferId() == transferInfo.getId()) {
								chatMsg.setCurrency(transferInfo.getCurrency());
								chatMsg.setAmount(transferInfo.getAmount());
							}
						}

						mChatView.addLastChatMsg(chatMsg);
					}
					updateChatMsg(chatId);//消息列表展示成功后，更新消息的status
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	//  @Override
	//  public void sendMediaMsg(ChatMsgEvent msgEvent, String contactID, String groupID) {
	//    String filePath = msgEvent.getFilePath();
	//    String messageType = msgEvent.getType();
	//    long time = msgEvent.getTime();
	//    String base64String = FileUtils.encode64File(filePath);
	//    long fileSize = FileUtils.getFileSize(filePath);
	//
	//    Map<String, String> map = new HashMap<>();
	//    map.put("_a", "friends");
	//    map.put("_b", "aj");
	//    map.put("_s", Session.sSid);
	//    map.put("chat_type", CHAT_TYPE);
	//    map.put("user_type", USER_TYPE_1_MEMBER);
	//    map.put("user_id", Session.uid);
	//    map.put("relate_type", USER_TYPE_1_MEMBER);
	//    map.put("relate_id", contactID);
	//    map.put("group_id", groupID);
	//    map.put("message_type", messageType);
	//    map.put("message", base64String);
	//    map.put("time", String.valueOf(time));
	//    map.put("size", String.valueOf(fileSize));
	//    map.put("cmd", "sendMediaMsg");
	//    submit(map, new IDataRequestListener() {
	//      @Override
	//      public List<Map<String, String>> loadSuccess(JSONObject object) {
	//        String msg = object.optString("msg");
	//        if (msg.equals("OK")) {
	//          List<ChatMsg> list = new ArrayList<>();
	//          JSONObject jsonObject = object.optJSONObject("data");
	//          String content = jsonObject.optString("message");
	//          String messageType = jsonObject.optString("message_type");
	//          String idFrom = jsonObject.optString("user_id");
	//          ChatMsg chatHistory = new ChatMsg(content, idFrom, messageType);
	//          list.add(chatHistory);
	//          mChatView.addLastChatMsg(list);
	//          mChatView.sendMsg();//数据成功插入才去发送websocket
	//        }
	//        return null;
	//      }
	//
	//      @Override
	//      public void loadFailure() {
	//        DialogFactory.ToastDialog(mContext, Utils.getString(R.string.title_error), Utils.getString(R.string.load_fail), 1);
	//      }
	//    });
	//  }

	private String mPath = Environment.getExternalStorageDirectory() + "/record/";//临时目录，跟录音目录是一样的

	@Override
	public void playVoice(String path) {
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("path", path);
		map.put("cmd", "loadFile");
		submit(map, new SimpleRequestListener() {
			//      @Override
			//      public List<Map<String, String>> loadSuccess(JSONObject object) {
			//        String msg = object.optString("msg");
			//        if (msg.equals("OK")) {
			//          String base64String = object.optString("data");
			//          String filePath = FileUtils.decode64File(base64String, mPath + TimeUtils.getCurrentTime() + ".amr");//应该用sp保存这路径，可以本地缓存播放
			//          mChatView.playVoice(filePath);
			//        }
			//        return null;
			//      }
			//
			//      @Override
			//      public void loadFailure() {
			//        DialogFactory.ToastDialog(mContext, Utils.getString(R.string.title_error), Utils.getString(R.string.load_fail), 1);
			//      }
		}, false);
	}

}
