package com.ace.member.main.friends.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//todo 正式上线前修改此类
/**
 * /data/data/你的包名/databases/
 * 聊天记录数据库
 */
public class ChatSQLiteHelper extends SQLiteOpenHelper {
	private static int DB_VERSION_1_START_CODE = 1;
	private static int DB_VERSION_CODE = 8;

	/**
	 * create table
	 */
  private static final String CREATE_TABLE_CHAT="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_CHAT+" ("
    + "remote_id INTEGER(8) PRIMARY KEY NOT NULL, "//服务器上的tbl_chat.id
    + "time DATETIME NOT NULL, "
	  + "chat_type TINYINT(1) NOT NULL DEFAULT 0, "
    + "time_last_member_join DATETIME NOT NULL, "
    + "time_last_message DATETIME NOT NULL, "
    + "lmt DATETIME NOT NULL);";

  private static final String CREATE_TABLE_CHAT_CONTENT="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_CHAT_CONTENT+" ("
    + "id INTEGER PRIMARY KEY autoincrement,"
    + "time DATETIME NOT NULL, "
    + "remote_chat_id INTEGER(8) NOT NULL DEFAULT 0, "
    + "member_id INTEGER(8) NOT NULL DEFAULT 0, "
    + "type TINYINT(2) NOT NULL DEFAULT 0, "
    + "content VARCHAR(200) NOT NULL,"
    + "transfer_id INTEGER(8) NOT NULL DEFAULT 0,"
    + "status TINYINT(1) NOT NULL DEFAULT 0,"
    + "lmt DATETIME NOT NULL);";

  private static final String CREATE_TABLE_CHAT_MEMBER="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_CHAT_MEMBER+" ("
    + "remote_chat_id INTEGER(8) NOT NULL DEFAULT 0, "
    + "member_id INTEGER(8) NOT NULL DEFAULT 0, "
    + "time_join DATETIME NOT NULL, "
    + "flag_mute_notifications TINYINT(1) NOT NULL DEFAULT 0,"
	  + "time_last_read DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00', "
    + "lmt DATETIME NOT NULL," +
    "PRIMARY KEY (remote_chat_id, member_id));";

  private static final String CREATE_TABLE_FRIEND_REQUEST="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_FRIEND_REQUEST+" ("
    + "id INTEGER PRIMARY KEY autoincrement,"
    + "time DATETIME NOT NULL, "
    + "member_id INTEGER(8) NOT NULL DEFAULT 0, "
	  + "member_name VARCHAR(40) NOT NULL,"
    + "content VARCHAR(200) NOT NULL,"
	  + "type_add TINYINT(1) NOT NULL DEFAULT 0, "
    + "status TINYINT(1) NOT NULL DEFAULT 0, "
    + "time_complete DATETIME NOT NULL, "
    + "lmt DATETIME NOT NULL);";

  private static final String CREATE_TABLE_FRIENDS="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_FRIENDS+" ("
    + "friend_id INTEGER(8) NOT NULL DEFAULT 0, "
    + "name_remark VARCHAR(40) NOT NULL,"
    + "time_add DATETIME NOT NULL, "
    + "type_add TINYINT(1) NOT NULL DEFAULT 0, "
    + "lmt DATETIME NOT NULL," +
    "PRIMARY KEY (friend_id));";

  private static final String CREATE_TABLE_NOTIFICATION="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_NOTIFICATION+" ("
    + "id INTEGER PRIMARY KEY autoincrement,"
    + "time DATETIME NOT NULL, "
    + "type TINYINT(1) NOT NULL DEFAULT 0, "
    + "content VARCHAR(200) NOT NULL,"
    + "status TINYINT(1) NOT NULL DEFAULT 0, "
	  + "time_receive DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00', "
    + "lmt DATETIME NOT NULL);";

  private static final String CREATE_TABLE_MEMBER="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_MEMBER+" ("
	  + "remote_id INTEGER(8) PRIMARY KEY NOT NULL, "
    + "phone VARCHAR(20) NOT NULL UNIQUE,"
    + "email VARCHAR(64) NOT NULL,"
    + "portrait VARCHAR(50),"
    + "lmt DATETIME NOT NULL);";

	private static final String CREATE_TABLE_TRANSFER="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_TRANSFER+" ("
		+ "remote_id INTEGER(8) PRIMARY KEY NOT NULL DEFAULT 0, "
		+ "time DATETIME NOT NULL, "
		+ "user_type TINYINT(1) NOT NULL DEFAULT 0, "
		+ "user_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "currency VARCHAR(4) NOT NULL, "
		+ "amount DECIMAL(12,2) NOT NULL DEFAULT 0.00, "
		+ "fee DECIMAL(12,2) NOT NULL DEFAULT 0.00, "
		+ "to_user_type TINYINT(1) NOT NULL DEFAULT 0, "
		+ "to_user_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "status TINYINT(1) NOT NULL DEFAULT 0, "
		+ "remark VARCHAR(200) NOT NULL, "
		+ "lmt DATETIME NOT NULL);";

	private static final String CREATE_TABLE_MEMBER_TRANSACTION="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_MEMBER_TRANSACTION+" ("
		+ "remote_id INTEGER(8) PRIMARY KEY NOT NULL DEFAULT 0, "
		+ "time DATETIME NOT NULL, "
		+ "type TINYINT(2) NOT NULL DEFAULT 0, "
		+ "sub_type INTEGER(4) NOT NULL DEFAULT 0, "
		+ "currency VARCHAR(4) NOT NULL, "
		+ "amount DECIMAL(12,2) NOT NULL DEFAULT 0.00, "
		+ "remark VARCHAR(200) NOT NULL, "
		+ "lmt DATETIME NOT NULL);";

	private static final String CREATE_TABLE_GROUP="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_GROUP+" ("
		+ "id INTEGER PRIMARY KEY autoincrement,"
		+ "time DATETIME NOT NULL, "
		+ "member_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "chat_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "lmt DATETIME NOT NULL);";

	private static final String CREATE_TABLE_CHAT_EXTEND_GROUP="CREATE TABLE IF NOT EXISTS "+TableDB.TABLE_CHAT_EXTEND_GROUP+" ("
		+ "chat_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "group_name VARCHAR(40) NOT NULL,"
		+ "owner_id INTEGER(8) NOT NULL DEFAULT 0, "
		+ "flag_verify_invitation TINYINT(1) NOT NULL DEFAULT 0, "
		+ "group_notice VARCHAR(200) NOT NULL,"
		+ "lmt DATETIME NOT NULL," +
		"PRIMARY KEY (chat_id));";

	/**
	 * create index
	 */
	private static final String CREATE_TABLE_CHAT_INDEX1 = "CREATE INDEX time1 ON " + TableDB.TABLE_CHAT + " (time);";
	private static final String CREATE_TABLE_CHAT_INDEX2 = "CREATE INDEX time_last_message ON " + TableDB.TABLE_CHAT + " (time_last_message);";
	private static final String CREATE_TABLE_CHAT_CONTENT_INDEX = "CREATE INDEX remote_chat_id ON " + TableDB.TABLE_CHAT_CONTENT + " (remote_chat_id);";
	private static final String CREATE_TABLE_CHAT_MEMBER_INDEX = "CREATE INDEX remote_chat_id2 ON " + TableDB.TABLE_CHAT_MEMBER + " (remote_chat_id);";
	private static final String CREATE_TABLE_FRIEND_REQUEST_INDEX = "CREATE INDEX member_id ON " + TableDB.TABLE_FRIEND_REQUEST + " (member_id);";
	private static final String CREATE_TABLE_NOTIFICATION_INDEX = "CREATE INDEX time2 ON " + TableDB.TABLE_NOTIFICATION + " (time);";
	private static final String CREATE_TABLE_TRANSFER_INDEX = "CREATE INDEX time3 ON " + TableDB.TABLE_TRANSFER + " (time);";
	private static final String CREATE_TABLE_MEMBER_TRANSACTION_INDEX = "CREATE INDEX time4 ON " + TableDB.TABLE_MEMBER_TRANSACTION + " (time);";

	/**
	 * alter table
	 */
	private static final String ALTER_NOTIFICATION_ADD_TIME_RECEIVE="alter table "+TableDB.TABLE_NOTIFICATION+" add time_receive DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'";
	private static final String ALTER_CHAT_MEMBER_ADD_TIME_LAST_READ="alter table "+TableDB.TABLE_CHAT_MEMBER+" add time_last_read DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'";
	private static final String ALTER_NOTIFICATION_ADD_TRANSACTION_ID="alter table "+TableDB.TABLE_NOTIFICATION+" add transaction_id INTEGER(8) NOT NULL DEFAULT 0";

	/**
	 * alter_table_transfer
	 */
	private static final String CLONE_TRANSFER_TABLE="ALTER TABLE " + TableDB.TABLE_TRANSFER + " RENAME TO [temp_table_transfer];";
	private static final String INSERT_TO_NEW_TRANSFER_TABLE="INSERT INTO " + TableDB.TABLE_TRANSFER +"(rowid, remote_id, time, user_id, currency, amount, fee, to_user_id, status, remark, lmt) SELECT rowid, id, time, member_id, currency, amount, fee, to_member_id, status, remark, lmt FROM temp_table_transfer;";
	private static final String DROP_CLONE_TRANSFER_TABLE="DROP TABLE IF EXISTS temp_table_transfer;";

	public ChatSQLiteHelper(Context context, String DBName) {
		this(context, DBName, null, DB_VERSION_CODE);
	}

	private ChatSQLiteHelper(Context context, String DBName, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, DBName, factory, version);
	}

	/**
	 * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
	 * 重写onCreate方法，调用execSQL方法创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CHAT);
		db.execSQL(CREATE_TABLE_CHAT_INDEX1);
		db.execSQL(CREATE_TABLE_CHAT_INDEX2);

		db.execSQL(CREATE_TABLE_CHAT_CONTENT);
		db.execSQL(CREATE_TABLE_CHAT_CONTENT_INDEX);

		db.execSQL(CREATE_TABLE_CHAT_MEMBER);
		db.execSQL(CREATE_TABLE_CHAT_MEMBER_INDEX);

		db.execSQL(CREATE_TABLE_FRIEND_REQUEST);
		db.execSQL(CREATE_TABLE_FRIEND_REQUEST_INDEX);

		db.execSQL(CREATE_TABLE_FRIENDS);

		db.execSQL(CREATE_TABLE_NOTIFICATION);
		db.execSQL(CREATE_TABLE_NOTIFICATION_INDEX);

		db.execSQL(CREATE_TABLE_TRANSFER);
		db.execSQL(CREATE_TABLE_TRANSFER_INDEX);

		db.execSQL(CREATE_TABLE_MEMBER_TRANSACTION);
		db.execSQL(CREATE_TABLE_MEMBER_TRANSACTION_INDEX);

		db.execSQL(CREATE_TABLE_MEMBER);

		db.execSQL(CREATE_TABLE_GROUP);
		db.execSQL(CREATE_TABLE_CHAT_EXTEND_GROUP);
	}

	//当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (++oldVersion) {
			case 0:
				throw new RuntimeException("db onUpgrade error:update version");
			case 1:
				onCreate(db);
			case 2://version-2 :增加tbl_member表
				db.execSQL(CREATE_TABLE_MEMBER);
			case 3://version-3 :增加tbl_notification time_receive字段
				db.execSQL(ALTER_NOTIFICATION_ADD_TIME_RECEIVE);
			case 4://version-4 :增加tbl_chat_member time_last_read字段
				db.execSQL(ALTER_CHAT_MEMBER_ADD_TIME_LAST_READ);
			case 5://version-5 :增加 tbl_transfer 表
				db.execSQL(CREATE_TABLE_TRANSFER);
				db.execSQL(CREATE_TABLE_TRANSFER_INDEX);
			case 6://version-6 :调整 tbl_transfer 表
				if (oldVersion == 6){
					db.execSQL(CLONE_TRANSFER_TABLE);
					db.execSQL(CREATE_TABLE_TRANSFER);
					db.execSQL(INSERT_TO_NEW_TRANSFER_TABLE);
					db.execSQL(DROP_CLONE_TRANSFER_TABLE);
					db.execSQL(CREATE_TABLE_TRANSFER_INDEX);
				}
			case 7:
				db.execSQL(ALTER_NOTIFICATION_ADD_TRANSACTION_ID);
				db.execSQL(CREATE_TABLE_MEMBER_TRANSACTION);
				db.execSQL(CREATE_TABLE_MEMBER_TRANSACTION_INDEX);
			case 8://version-8: 增加tbl_group, tbl_chat_extend_group
				db.execSQL(CREATE_TABLE_GROUP);
				db.execSQL(CREATE_TABLE_CHAT_EXTEND_GROUP);
		}
	}
}