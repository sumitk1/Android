package sjsu.cs286.hw3;

import android.content.*;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {
	
	DBHelper(Context ctx) {
		super(ctx, "MoodDB", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE server ("
				+ "rowName VARCHAR(50) NOT NULL PRIMARY KEY,"
				+ "userName VARCHAR(50) NOT NULL UNIQUE,"
				+ "serverURL VARCHAR (1000)" + ");");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersiosn, int newVersion) {

	}

}