package sjsu.cs286.hw2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailDisplay extends Activity {
	//DictionaryDBHelper dbHelper;
	public TextView word;
	public TextView description;
	public DictionaryDBHelper dbHelper;
	public Cursor mCursor = null;
	public SQLiteDatabase db;
	AlertDialog.Builder alertbox;
	String fieldWord;
	String fieldDescription;
	Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		dbHelper = new DictionaryDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		word = (TextView) findViewById(R.id.Word);
		//word.setEnabled(false);
		description = (TextView) findViewById(R.id.Description);
		//description.setEnabled(false);
		
		bundle = getIntent().getExtras();
		// Getting values of all fields from previous activity
		fieldWord = bundle.getString("word");
		fieldDescription = bundle.getString("description");
		
		// Setting value in field
		word.setText(fieldWord);
		description.setText(fieldDescription);
	}

	public void onStart() {
		super.onStart();
				
		word = (TextView) findViewById(R.id.Word);
		//word.setEnabled(false);
		description = (TextView) findViewById(R.id.Description);
		//description.setEnabled(false);
		
		Bundle bundle = getIntent().getExtras();
		// Getting values of all fields from previous activity
		fieldWord = bundle.getString("word");
		//fieldDescription = bundle.getString("description");
		
		db = dbHelper.getReadableDatabase();
		mCursor = db.rawQuery("select word,description from Dictionary where  word = '" + fieldWord + "'", null);
		mCursor.moveToFirst();
		fieldDescription = mCursor.getString(1).toString();
		
		// Setting value in field
		word.setText(fieldWord);
		description.setText(fieldDescription);
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

	public void onExit(View v) {
		finish();
	}

	public void onEdit(View v) {

		Intent i = new Intent();
		i.setClass(this, EditDictionary.class);
		Bundle bundle = new Bundle();
		bundle.putString("word", fieldWord);
		bundle.putString("description", fieldDescription);
		i.putExtras(bundle);
		startActivity(i);

	}

	public void onDelete(View v) {
		dbHelper = new DictionaryDBHelper(this);
		db = dbHelper.getWritableDatabase();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to Delete this record?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								if (word != null) {
									db.beginTransaction();
									try {
										dbHelper.getWritableDatabase().execSQL("delete from Dictionary where word = '" + fieldWord + "'");
										Toast.makeText(getApplicationContext(), "Entery Deleted successfully", 0).show();

										db.setTransactionSuccessful();
										queryDB();
										onRefresh();
										
										finish();
									}

									catch (Exception e) {
										Toast.makeText(getApplicationContext(), "Can't Delete", Toast.LENGTH_SHORT).show();
									} finally {
										db.endTransaction();
										dbHelper.close();
										db.close();
									}
								}
								// Delete from DB
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
		
		
	}

	/**
	 * This method will move the cursor to first record in database
	 */
	public void onRefresh() {
		if (mCursor.moveToFirst()) {
			word.setText(mCursor.getString(0).toString());
			word.setEnabled(false);
			description.setText(mCursor.getString(1).toString());
			description.setEnabled(false);

		}
	}
}
