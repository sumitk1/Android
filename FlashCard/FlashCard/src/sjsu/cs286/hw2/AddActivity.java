package sjsu.cs286.hw2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {

	public DictionaryDBHelper dbHelper;
	public Cursor mCursor = null;
	public SQLiteDatabase db;
	public EditText word;
	public EditText description;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.i("AddActivity", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		dbHelper = new DictionaryDBHelper(this);
		db = dbHelper.getWritableDatabase();

		word = (EditText) findViewById(R.id.Word);
		description = (EditText) findViewById(R.id.Description);
		
		InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(word.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart() This method will be responsible
	 * making connection with database
	 */
	public void onStart() {
		super.onStart();
		queryDB();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        db.close();
        mCursor.close();
        // The activity is about to be destroyed.
    }
    
    /**
	 * This method is responsible to check and display all the contents present
	 * in database.
	 */
	public void queryDB() {
		mCursor = db.rawQuery("select word, description from Dictionary order by word", null);
		if (mCursor == null) {
			Log.i("SQLTest", "query failed");
		}
	}

	/**
	 * This method will help user exit out of the application.
	 * 
	 * @param v
	 */
	public void onCancel(View v) {
		finish();
	}

	/**
	 * This method will clear all the fields entered by user.
	 * 
	 * @param v
	 */
	public void onReset(View v) {
		word.setText("");
		description.setText("");
		
	}

	/**
	 * This method is responsible for adding all the word details entered by
	 * user on click of Submit button.
	 * 
	 * @param v
	 */
	public void onSave(View v) {
		if((word.getText().toString() == "") || (description.getText().toString() == "")) {
			Toast.makeText(this, "Please enter a word and its description", Toast.LENGTH_LONG).show();
			
		} else {
			db.beginTransaction();
			try {
				String word = ((EditText) findViewById(R.id.Word)).getText().toString();
				String description = ((EditText) findViewById(R.id.Description)).getText().toString();
				Log.i("Word ", "[" + word + "] - ");
				Log.i("Description ", "[" + description + "] ");
				try {
					dbHelper.getWritableDatabase().execSQL("INSERT INTO Dictionary (word, description) "
									+ "VALUES " + "('" + word + "', '"+ description + "');");
					Toast.makeText(this, "Enteries Added successfully", Toast.LENGTH_SHORT).show();
					Log.i("onSave", "Enteries added successfully");
					//onRefresh();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Word not added! See log.", Toast.LENGTH_SHORT).show();
				}

				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
	
		}
		
	}

	public void onRefresh() {
		if (mCursor.moveToFirst()) {
			word.setText(mCursor.getString(0).toString());
			word.setEnabled(false);
			description.setText(mCursor.getString(1).toString());
			description.setEnabled(false);

		}
	}
}
