/**
 * 
 */
package com.example.flashcard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "card.db";
	private static final String TABLE = "card";
	private static final int DB_VERSION = 1;

	private static final String CREATE_TABLE = 
			"create table "+ TABLE + " ( "
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"q TEXT,"
			+"a TEXT);";

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DBHelper(Context context) {
//		super(context, name, factory, version);
		super(context, DB_NAME, null, DB_VERSION);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
//	public DBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//		super(context, name, factory, version, errorHandler);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}

	/* (非 Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ

		db.execSQL(CREATE_TABLE);
	}

	/* (非 Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
