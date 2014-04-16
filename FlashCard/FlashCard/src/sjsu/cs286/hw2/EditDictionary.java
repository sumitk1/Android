package sjsu.cs286.hw2;

import sjsu.cs286.hw2.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class EditContacts
 * 
 * This class is responsible for editing the word entered by user 
 * 
 * Methods in class provide functionality required to execute this program
 * 
 */
public class EditDictionary extends Activity {
	public DictionaryDBHelper dbHelper;
	public Cursor mCursor = null;
	public SQLiteDatabase db;

	public EditText word;
	public EditText description;

	String fieldWord;
	String fieldDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editdictionary);
		
		word = (EditText) findViewById(R.id.Word);
		description = (EditText) findViewById(R.id.Description);

		Bundle bundle = getIntent().getExtras();

		// Getting values of all fields from previous activity
		fieldWord = bundle.getString("word");
		fieldDescription = bundle.getString("description");

		// Setting value in field
		word.setText(fieldWord);
		word.setEnabled(false);
		description.setText(fieldDescription);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart() This method will be responsible
	 * making connection with database
	 */
	public void onStart() {
		super.onStart();
		//queryDB();
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
	public void onExit(View v) {
		finish();
	}

	/**
	 * This method will clear all the fields entered by user.
	 * 
	 * @param v
	 */
	public void onReset(View v) {
		description.setText("");

	}

	/**
	 * This method is responsible for adding all the contact details entered by
	 * user on click of Submit button.
	 * 
	 * @param v
	 */
	public void onSubmit(View v) {
		if((word.getText().toString() == "") || (description.getText().toString() == "")) {
			Toast.makeText(this, "Please enter a word and its description", Toast.LENGTH_LONG).show();
			
		} else {
			dbHelper = new DictionaryDBHelper(this);
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			
			try {
				String newDescription = ((EditText) findViewById(R.id.Description)).getText().toString();
				Log.i("New Description", newDescription);
	
				try {
					dbHelper.getWritableDatabase().execSQL("update Dictionary set description = '"
									+ newDescription + "' where word = '" + fieldWord + "'");
	
					Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
					Log.i("EditDictionary ", "Update successful for word - "+ fieldWord + "& description - "+ newDescription);
					
	
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Enteries not added", Toast.LENGTH_SHORT).show();
				}
				db.setTransactionSuccessful();
				Bundle bundle = getIntent().getExtras();

				// Getting values of all fields from previous activity
				bundle.putString("word", fieldWord);
				bundle.putString("description", newDescription);
				finish();
			} finally {
				db.endTransaction();
			}
		}
	
	}

}
