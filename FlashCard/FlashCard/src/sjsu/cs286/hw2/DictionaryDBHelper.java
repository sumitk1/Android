package sjsu.cs286.hw2;

import android.content.*;
import android.database.sqlite.*;

public class DictionaryDBHelper extends SQLiteOpenHelper {
	
	DictionaryDBHelper(Context ctx) {
		super(ctx, "DictionaryDB", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE Dictionary ("
				+ "word VARCHAR(128) NOT NULL PRIMARY KEY,"
				+ "description VARCHAR (1000)" + ");");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersiosn, int newVersion) {

	}

}