package com.example.flashcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R.id;


public class MainActivity extends Activity {
	private static final String TABLE = "card";
	private static final String DBFILENAME = Environment.getExternalStorageDirectory() + "/carddata.txt";
	private static final String DELIM = "---";
	private ArrayList <Question> questions;
	int question_size;
	int page_size;
	int current_q;
	int current_page = 0;

	class Question {
		String q;
		String a;
		
		Question(String q,String a) {
			this.q = q;
			this.a = a;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("current_page", current_page);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		current_page = savedInstanceState.getInt("current_page");
		TextView tv = (TextView) findViewById(id.textview1);
		current_q = current_page / 2;
		if (current_page % 2 == 0) {
			tv.setText(questions.get(current_q).q);
		} else {
			tv.setText(questions.get(current_q).q + "\n" + questions.get(current_q).a);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ContentValues values = new ContentValues();
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(TABLE,null,null);

		questions=new ArrayList<Question>();
		
		try {
			File file = new File(DBFILENAME);
			BufferedReader br = new BufferedReader(new FileReader(file));
			int data_size = 0;

			String line;
			String contents = "";
			String q = "";
			String a = "";
			while((line = br.readLine()) != null) {
				if (DELIM.equals(line)) {
					if (data_size % 2 == 0) {
						q = contents;
					} else {
						a = contents;
						
						values.put("q", q);
						values.put("a", a);
						db.insert(TABLE, null, values);
					}
					data_size++;
					contents = "";
					continue;
				}
				contents = contents + line + "\n";
			}
			br.close();
			if (data_size % 2 == 0) {
				q = contents;
			} else {
				a = contents;
				
				values.put("q", q);
				values.put("a", a);
				db.insert(TABLE, null, values);
			}

//			values.put("q", "店主がお店の資本金を引き出すときに使う勘定科目は？");
//			values.put("a", "引出金");
//			db.insert(TABLE, null, values);
//			values.put("q", "決算時、引出金勘定を振り替える勘定科目は？");
//			values.put("a", "資本金");
//			db.insert(TABLE, null, values);
		} catch (FileNotFoundException e) {
			Toast toast = Toast.makeText(MainActivity.this, getString(R.string.message_FileNotFound) + DBFILENAME, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		} catch (IOException e) {
			Toast toast = Toast.makeText(MainActivity.this, getString(R.string.message_IOException), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}

		Cursor c = db.query(TABLE, new String[] {"q","a"}, null, null, null, null, null);
		while(c.moveToNext()) {
			questions.add(new Question(c.getString(c.getColumnIndex("q")),c.getString(c.getColumnIndex("a"))));
//			str += c.getString(c.getColumnIndex("q"));
//			str += "\n";
//			str += c.getString(c.getColumnIndex("a"));
//			str += "\n";
//			str += "\n";
		}
		c.close();
		db.close();
		question_size = questions.size();
		page_size = question_size * 2;

		ScrollView sv = (ScrollView) findViewById(id.scrollview1);
		TextView tv = (TextView) findViewById(id.textview1);
		current_q = current_page / 2;
		if (current_page % 2 == 0) {
			tv.setText(questions.get(current_q).q);
		} else {
			tv.setText(questions.get(current_q).q + "\n" + questions.get(current_q).a);
		}
		

		sv.setOnTouchListener(new View.OnTouchListener() {
			private float lastTouchX;
			private float lastTouchY;
			private float currentX;
			private float currentY;
			private float deltaX;
			private float deltaY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自動生成されたメソッド・スタブ
				switch( event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					currentX = event.getX();
					currentY = event.getY();
					deltaX = currentX - lastTouchX;
					deltaY = currentY - lastTouchY;
					if ((0 < deltaX) && (Math.abs(deltaX) > Math.abs(deltaY))) {
						TextView tv = (TextView) findViewById(id.textview1);
						current_page = ((current_page - 1) + page_size) % page_size;
						current_q = current_page / 2;
						if (current_page % 2 == 0) {
							tv.setText(questions.get(current_q).q);
						} else {
							tv.setText(questions.get(current_q).q + "\n\n" + questions.get(current_q).a);
						}
					} else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
						TextView tv = (TextView) findViewById(id.textview1);
						current_page = ((current_page + 1) + page_size) % page_size;
						current_q = current_page / 2;
						if (current_page % 2 == 0) {
							tv.setText(questions.get(current_q).q);
						} else {
							tv.setText(questions.get(current_q).q + "\n\n" + questions.get(current_q).a);
						}
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					currentX = event.getX();
					currentY = event.getY();
					deltaX = currentX - lastTouchX;
					deltaY = currentY - lastTouchY;
					if ((0 < deltaX) && (Math.abs(deltaX) > Math.abs(deltaY))) {
						TextView tv = (TextView) findViewById(id.textview1);
						current_page = ((current_page - 1) + page_size) % page_size;
						current_q = current_page / 2;
						if (current_page % 2 == 0) {
							tv.setText(questions.get(current_q).q);
						} else {
							tv.setText(questions.get(current_q).q + "\n\n" + questions.get(current_q).a);
						}
					} else if ((deltaX < 0) && (Math.abs(deltaX) > Math.abs(deltaY))) {
						TextView tv = (TextView) findViewById(id.textview1);
						current_page = ((current_page + 1) + page_size) % page_size;
						current_q = current_page / 2;
						if (current_page % 2 == 0) {
							tv.setText(questions.get(current_q).q);
						} else {
							tv.setText(questions.get(current_q).q + "\n\n" + questions.get(current_q).a);
						}
					}
					break;
				}
//				return true;
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
