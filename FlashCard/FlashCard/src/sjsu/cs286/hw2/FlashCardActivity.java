package sjsu.cs286.hw2;

import java.util.ArrayList;
import java.util.Collections;

import sjsu.cs286.hw2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class FlashCardActivity extends Activity {
	TextView selection;
	ListView list;
	DictionaryDBHelper dbHelper;
	public SQLiteDatabase db;
	public Cursor mCursor = null;
	Context context;
	public EditText word;
	public EditText description;
	ViewFlipper flipper;
	TextView tv1, tv2, tv3, tv4;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Integer ornt1 = getResources().getConfiguration().orientation;
		Integer ornt2 = Configuration.ORIENTATION_LANDSCAPE;
		Log.i("onCreate", " FlashCardActivity Portrait");
		Log.i("Orientation ", ornt1.toString() + " - " + ornt2);
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			setContentView(R.layout.main_portrait);
			// setRequestedOrientation(ActivityInfo.CONFIG_ORIENTATION);
			setRequestedOrientation(ActivityInfo.CONFIG_KEYBOARD_HIDDEN);

			list = (ListView) findViewById(R.id.list);

			if (list != null) {
				list.setFastScrollEnabled(true);
				// Listener
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {

						final String select = adapterView.getItemAtPosition(
								position).toString();
						String[] getData = select.split(" - ");

						Intent i = new Intent();
						i.setClass(FlashCardActivity.this, DetailDisplay.class);
						Bundle bundle = new Bundle();
						bundle.putString("word", getData[0]);
						bundle.putString("description", getData[1]);
						i.putExtras(bundle);
						startActivity(i);
						Log.i("onItemClick", "DetailDisplay Acitvity started");
						
					}
				});

				updateListView(); // Fetch data from database and display in
									// listview

			}
		} else {

			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
			Log.i("onCreate", "FlashCardActivity Landscape");
			setContentView(R.layout.main_landscape);
			
			flipper=(ViewFlipper)findViewById(R.id.flip);
			
			String str = getWord();
			String[] data = str.split(" - ");
			Log.i("Data ", str);
			
			tv1 =(TextView)findViewById(R.id.Word); 
			tv2 =(TextView)findViewById(R.id.Word2);
			Button btn = (Button)findViewById(R.id.next);
			
			Log.i("Definition ", data[1]);
			tv1.setText(data[0]);
			
			tv2.setText(data[1]);
			
			tv1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					flipper.setInAnimation(inFromRightAnimation());
					flipper.setOutAnimation(outToLeftAnimation());
					flipper.showNext();
				}
			});
			
			tv2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					flipper.setInAnimation(inFromLeftAnimation());
					flipper.setOutAnimation(outToRightAnimation());
					flipper.showPrevious();
				}
			});
			
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {

					String str = getWord();
					String[] data = str.split(" - ");
					Log.i("Word = ", str);

					tv1.setText(data[0]);
					tv2.setText(data[1]);
					if(flipper.getCurrentView() == tv2) {
						flipper.setInAnimation(inFromLeftAnimation());
						flipper.setOutAnimation(outToRightAnimation());
						flipper.showPrevious();
					}

				}
			});

		}

	}

	/*
	 * Source: http://mobileorchard.com/android-app-development-view-filpper-and-sliding-drawer/
	 */
    public void ClickHandler(View v)
    {

     //flip.showNext();
    	flipper=(ViewFlipper)findViewById(R.id.flip);
    	flipper.setInAnimation(inFromRightAnimation());
		flipper.setOutAnimation(outToLeftAnimation());
		flipper.showNext();

    }
    
    /*
	 * Source: http://mobileorchard.com/android-app-development-view-filpper-and-sliding-drawer/
	 */
	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	/*
	 * Source: http://mobileorchard.com/android-app-development-view-filpper-and-sliding-drawer/
	 */
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	/*
	 * Source: http://mobileorchard.com/android-app-development-view-filpper-and-sliding-drawer/
	 */
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	/*
	 * Source: http://mobileorchard.com/android-app-development-view-filpper-and-sliding-drawer/
	 */
	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("onStart", "FlashCardActivity");
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i("onStart", "FlashCardActivity Portrait");
			setContentView(R.layout.main_portrait);
			// setRequestedOrientation(ActivityInfo.CONFIG_ORIENTATION);
			setRequestedOrientation(ActivityInfo.CONFIG_KEYBOARD_HIDDEN);
			// selection=(TextView)findViewById(R.id.selection);
			list = (ListView) findViewById(R.id.list);

			if (list != null) {
				list.setFastScrollEnabled(true);
				// Listener
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						final String select = adapterView.getItemAtPosition(
								position).toString();
						String[] getData = select.split(" - ");

						Intent i = new Intent();
						i.setClass(FlashCardActivity.this, DetailDisplay.class);
						Bundle bundle = new Bundle();
						bundle.putString("word", getData[0]);
						
						db = dbHelper.getReadableDatabase();
						mCursor = db.rawQuery("select word,description from Dictionary where  word = '" + getData[0] + "'", null);
						mCursor.moveToFirst();
						String description = mCursor.getString(1).toString();
						
						Log.i("Description ", description);
						
						bundle.putString("description", description);
						i.putExtras(bundle);
						startActivity(i);
						Log.i("onItemClick", "DetailDisplay Acitvity started");
						
					}
				});

				updateListView(); // Fetch data from database and display in
									// listview

			}
		} else {
			Log.i("onStart", "FlashCardActivity Landscape");
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

		}
		// The activity is about to become visible.
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbHelper.close();
		db.close();
		mCursor.close();
		// The activity is about to be destroyed.
	}


	public Cursor queryDB() {
		db = dbHelper.getReadableDatabase();
		mCursor = db.rawQuery("select word,description from Dictionary", null);
		if (mCursor == null) {
			Log.e("SQLTest", "query failed");
		}
		return mCursor;
	}

	/**
	 * This method is responsible for fetching data from database and display in
	 * listview
	 */
	public void updateListView() {
		ListView listView = (ListView) findViewById(R.id.list);
		ArrayList<String> db_results = new ArrayList<String>();
		dbHelper = new DictionaryDBHelper(getBaseContext());
		Cursor cursor = queryDB();

		try {
			if (cursor.moveToFirst()) {
				do {
					String wordAndMeaning = cursor.getString(0) + " - "
							+ cursor.getString(1).split(" ")[0] + "..." ;
					db_results.add(wordAndMeaning);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("upateListView", "Fetch Data Failed");
			e.printStackTrace();
		}

		finally {
			dbHelper.close();
			cursor.close();
		}
		Collections.sort(db_results);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.row,
				R.id.label, db_results));
	}

	/**
	 * Change the view to add a word in the database
	 */
	public void onAdd(View view) {
		Toast.makeText(this, "Add Button clicked!", Toast.LENGTH_SHORT).show();
		Intent i = new Intent();
		i.setClass(FlashCardActivity.this, AddActivity.class);
		startActivity(i);

	}

	/**
	 * Delete all the words from the database
	 */
	public void onDelete(View view) {
		
		dbHelper = new DictionaryDBHelper(this);
		db = dbHelper.getWritableDatabase();

		Toast.makeText(this, "Delete Button clicked!", Toast.LENGTH_SHORT)
				.show();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to Delete All?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

									db.beginTransaction();
									try {
										dbHelper.getWritableDatabase().execSQL("delete from Dictionary");
										Toast.makeText(getApplicationContext(), "Entery Deleted successfully", 0).show();

										db.setTransactionSuccessful();
										queryDB();
										finish();
									}

									catch (Exception e) {
										Toast.makeText(getApplicationContext(), "Can't Delete", Toast.LENGTH_SHORT).show();
									} finally {
										db.endTransaction();
										dbHelper.close();
										db.close();
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
	 * Get word and its meaning from the database 
	 */
	public String getWord() {
		dbHelper = new DictionaryDBHelper(getBaseContext());
		Cursor cursor = queryDB();
		int number_of_records = cursor.getCount();
		Log.i("No of Records", " = " + number_of_records);
		int Max = number_of_records;
		int Min = 0;
		int random_position = Min + (int) (Math.random() * ((Max - Min)));

		Log.i("Position", " = " + random_position);

		cursor.moveToPosition(random_position);

		String randomWord = cursor.getString(0).toString();
		String definition = cursor.getString(1).toString();
		Log.i("word", randomWord);
		cursor.close();
		dbHelper.close();
		return randomWord + " - " + definition;
	}
}