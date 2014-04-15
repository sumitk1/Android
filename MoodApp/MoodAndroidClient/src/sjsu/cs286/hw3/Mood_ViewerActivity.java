package sjsu.cs286.hw3;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Mood_ViewerActivity extends Activity {

	ViewFlipper flipper;
	TextView tv1, value, tv4;
	LinearLayout ViewMoodLayout;
	LinearLayout SettingsLayout;
	SeekBar seekbar;

	public EditText userName;
	public EditText serverURL;
	public EditText topic;

	public Cursor mCursor = null;
	public SQLiteDatabase db;
	public DBHelper dbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("onCreate", "Mood_ViewerActivity Portrait");
		setContentView(R.layout.main);

		dbHelper = new DBHelper(this);

		flipper = (ViewFlipper) findViewById(R.id.flip);

		String str = "qazwsx"; 
		Log.i("Data ", str);

		ViewMoodLayout = (LinearLayout) findViewById(R.id.ViewMoodLayout);
		SettingsLayout = (LinearLayout) findViewById(R.id.SettingsLayout);
		
		ImageButton btn = (ImageButton) findViewById(R.id.viewMood);
		ImageButton btn1 = (ImageButton) findViewById(R.id.viewSettings);

		userName 	= (EditText) findViewById(R.id.userName);
		serverURL 	= (EditText) findViewById(R.id.serverURL);
		topic 		= (EditText) findViewById(R.id.Topic);
		
		String userName_str = "";
		String serverURL_str = "";
		Cursor cursor = queryDB_server();
		queryDB_server().close();
		db.close();
		dbHelper.close();
		
		cursor.moveToFirst();
		int count = cursor.getCount();
		Log.i("count = ", "-" + count + "-");
		
		if (cursor != null && count !=0) {
			Log.i("Here", "--");
						
			cursor.moveToFirst();
			userName_str = cursor.getString(0); 
			serverURL_str = cursor.getString(1);
			
			Log.i("getUserName", "-" + userName_str + "-");
			Log.i("getServerURL", "-" + serverURL_str + "-");
			
			userName.setText(userName_str);
			serverURL.setText(serverURL_str);
			
			//get topic from server and put in edit box
			MoodClient client = new MoodClient();
			String pingServer = client.testConnect(serverURL_str);
			
			if (pingServer.equals("Mood Server\n")) {
				
				String responseTopic = client.getTopic(serverURL_str);
				
				if(responseTopic.equals("NoTopic")) {
					Log.i("no_responseTopic", "-" + responseTopic + "-");
					topic.setText("No topic set on server. Set your topic here!");
					
				} else {
					Log.i("responseTopic", "-" + responseTopic + "-");
					//Toast.makeText(this, "Response from server - " + response,Toast.LENGTH_SHORT).show();
					topic.setText(responseTopic);
					value = (TextView) findViewById(R.id.seekBarValues);
					value.setText("Set your Mood here and press set!");
					
					// update list view
					ListView listView = (ListView) findViewById(R.id.list);
					ArrayList<String> listItems = new ArrayList<String>();
					
					String moods = client.getMoods(serverURL_str);
					
					if (moods.equals("NoMoods\n")) {
						
						listItems.add("No Mood Set!");
						listView.setAdapter(new ArrayAdapter<String>(this, R.layout.row, R.id.label, listItems));
						
					} else {
						
						//Load moods with colors
						
					}
					
				}
			} else {
				
				topic.setText("No topic set on server. Set your topic here!");
				
			}

		} else {
			Log.i("Here1", "--");
			//set edit box text = no server to connect uneditable
			topic.setText("No server to connect to. Go to settings to add server!");
		}
		
		value = (TextView) findViewById(R.id.seekBarValues);
		seekbar = (SeekBar) findViewById(R.id.seekbar);

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				value.setText("Mood = " + progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Log.i("ImageButton", "Mood");
				if (flipper.getCurrentView() == SettingsLayout) {
					flipper.setInAnimation(inFromLeftAnimation());
					flipper.setOutAnimation(outToRightAnimation());
					flipper.showPrevious();
				}
				
				

			}
		});

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Log.i("ImageButton", "Settings");
				if (flipper.getCurrentView() == ViewMoodLayout) {
					flipper.setInAnimation(inFromRightAnimation());
					flipper.setOutAnimation(outToLeftAnimation());
					flipper.showPrevious();
				}

			}
		});

	}

	/*
	 * Source:
	 * http://mobileorchard.com/android-app-development-view-filpper-and-
	 * sliding-drawer/
	 */
	public void ClickHandler(View v) {

		// flip.showNext();
		flipper = (ViewFlipper) findViewById(R.id.flip);
		flipper.setInAnimation(inFromRightAnimation());
		flipper.setOutAnimation(outToLeftAnimation());
		flipper.showNext();

	}

	/*
	 * Source:
	 * http://mobileorchard.com/android-app-development-view-filpper-and-
	 * sliding-drawer/
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
	 * Source:
	 * http://mobileorchard.com/android-app-development-view-filpper-and-
	 * sliding-drawer/
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
	 * Source:
	 * http://mobileorchard.com/android-app-development-view-filpper-and-
	 * sliding-drawer/
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
	 * Source:
	 * http://mobileorchard.com/android-app-development-view-filpper-and-
	 * sliding-drawer/
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

	public Cursor queryDB_server() {
		db = dbHelper.getReadableDatabase();
		mCursor = db.rawQuery("select userName, serverURL from server where rowName='Settings'", null);
		if (mCursor == null) {
			Log.i("inQueryDB_server", "No data");
			Log.e("QueryDB", "No previous settings found");

		}
		Log.i("inQueryDB_server", "some data");
		
		return mCursor;
	}
	
	/**
	 * This method sets the topic
	 */
	public void onSet(View v) {
		
		Log.i("Button", "inside onSet()");
		
		String topic = ((EditText) findViewById(R.id.Topic)).getText().toString().trim();
		String noServer = "No server to connect to. Go to settings to add server!";
		String noTopic = "No topic set on server. Set your topic here!";
		String noMood = "Set your Mood here and press set!";
		
		String mood = ((TextView) findViewById(R.id.seekBarValues)).getText().toString().trim();
		if ( mood.equals("") || mood.equals(null) || mood.equals(noMood))
			mood = "0";
		
		Log.i("onSet_Mood", "--"+ mood +"--" );
		Log.i("onSet_Topic", "--"+ topic +"--" );
		
		if (  topic.equals("") || topic.equals(noServer) || topic.equals(noTopic)) {
			Toast.makeText(this, "No topic set!", Toast.LENGTH_LONG).show();

		} else {
			
			//set topic in server
			
		}
	}
	
	/**
	 * This method is responsible for adding the name & serverURL entered by
	 * user on click of Apply button.
	 * 
	 * @param v
	 */
	public void onApply(View v) {
		
		Log.i("here ", "[ inside onApply() ]");
		
		Log.i("userName1", "--"+((EditText) findViewById(R.id.userName)).getText().toString().trim()+"--" );
		Log.i("serverURL1", "--"+((EditText) findViewById(R.id.serverURL)).getText().toString().trim()+"--" );
		
		if (( ((EditText) findViewById(R.id.userName)).getText().toString().trim().equals("")) || (((EditText) findViewById(R.id.serverURL)).getText().toString().trim().equals(""))) {
			Toast.makeText(
					this,
					"Name & URL can't be empty. Please enter a Name and ServerURL!",
					Toast.LENGTH_LONG).show();

		} else {
			Cursor readDBForSettings = queryDB_server();
			queryDB_server().close();
			
			if (readDBForSettings == null || readDBForSettings.getCount() == 0) {
				db.beginTransaction();
				try {
					String rowName = "Settings";
					String userName = ((EditText) findViewById(R.id.userName))
							.getText().toString().trim();
					String serverURL = ((EditText) findViewById(R.id.serverURL))
							.getText().toString().trim();
					Log.i("userName ", "[" + userName + "] ");
					Log.i("serverURL ", "[" + serverURL + "] ");
					try {
						// Register
						MoodClient client = new MoodClient();
						String response = client.register(userName, serverURL);
						Log.i("ServerResponse", "-" + response + "-");
						//Toast.makeText(this, "Response from server - " + response,Toast.LENGTH_SHORT).show();
						
						if(response.equals("Okay")) {
							dbHelper.getWritableDatabase()
								.execSQL("INSERT INTO server (rowName, userName, serverURL) "
												+ "VALUES " + "('" + rowName
												+ "', '" + userName + "', '"
												+ serverURL + "');");
						
						// Register and get response
							Toast.makeText(this, "Enteries Added successfully",
									Toast.LENGTH_SHORT).show();
							Log.i("onApply Insert", "Enteries added successfully." + "rowName = '" + rowName
									+ "', userName = '" + userName + "', serverURL = '"
									+ serverURL + "'");
							
							// update Topic view
							String responseTopic = client.getTopic(serverURL);
							topic 		= (EditText) findViewById(R.id.Topic);
							
							if(responseTopic.equals("NoTopic")) {
								Log.i("no_responseTopic", "registerInsert-" + responseTopic + "-");
								topic.setText("No topic set on server. Set your topic here!");
								
							} else {
								Log.i("responseTopic", "registerInsert-" + responseTopic + "-");
								//Toast.makeText(this, "Response from server - " + response,Toast.LENGTH_SHORT).show();
								topic.setText(responseTopic);
								value = (TextView) findViewById(R.id.seekBarValues);
								value.setText("Set your Mood here and press set!");
								
								// update list view
								ListView listView = (ListView) findViewById(R.id.list);
								ArrayList<String> listItems = new ArrayList<String>();
								
								String moods = client.getMoods(serverURL);
								
								if (moods.equals("NoMoods\n")) {
									
									listItems.add("No Mood Set!");
									listView.setAdapter(new ArrayAdapter<String>(this, R.layout.row, R.id.label, listItems));
									
								} else {
									
									// Load moods with color
									
								}
								
							}
							
						} else if(response.equals("Sorry")){
							
							Toast.makeText(this, "Sorry, user can't be registered! Please try again!",Toast.LENGTH_LONG).show();
							((EditText) findViewById(R.id.userName)).setSelected(true);
							
						} else {
							
							Toast.makeText(this, "Sorry, some error. Please try again!",Toast.LENGTH_SHORT).show();
							((EditText) findViewById(R.id.userName)).setSelected(true);
						
						}
						// onRefresh();
						//finish();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(this, "Name or ServerURL not added! See log.", Toast.LENGTH_SHORT).show();
					}

					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			} else {
				db.beginTransaction();
				try {
					
					readDBForSettings.moveToFirst();
					
					readDBForSettings.moveToFirst();
					String userName_db = readDBForSettings.getString(0); 
					String serverURL_db = readDBForSettings.getString(1);
					
					Log.i("getUserNameDB", "-" + userName_db + "-");
					Log.i("getServerURLDB", "-" + serverURL_db + "-");
					
					String rowName = "Settings";
					String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
					String serverURL = ((EditText) findViewById(R.id.serverURL)).getText().toString();
					Log.i("userName ", "[" + userName + "] - ");
					Log.i("serverURL ", "[" + serverURL + "] ");
					
					if (userName_db.equals(userName) && serverURL_db.equals(serverURL)) {
						
					} else {
						try {
							
							// Register
							MoodClient client = new MoodClient();
							String response = client.register(userName, serverURL);
							Log.i("ServerResponse", "-" + response + "-");
							//Toast.makeText(this, "Response from server - " + response,Toast.LENGTH_SHORT).show();
							
							if(response.equals("Okay")) {
								dbHelper.getWritableDatabase()
								.execSQL("update server set userName = '"
											+ userName + "', serverURL = '" 
											+ serverURL + "' where rowName = '" 
											+ rowName + "'");
						
								Toast.makeText(this, "User registered successfully!!",Toast.LENGTH_SHORT).show();
						
								Log.i("onApply update", "Enteries added successfully." + "rowName = '" + rowName
												+ "', userName = '" + userName + "', serverURL = '"
												+ serverURL + "'");
								
								// update Topic view
								String responseTopic = client.getTopic(serverURL);
								topic 		= (EditText) findViewById(R.id.Topic);
								
								if(responseTopic.equals("NoTopic")) {
									Log.i("no_responseTopic", "registerInsert-" + responseTopic + "-");
									topic.setText("No topic set on server. Set your topic here!");
									
								} else {
									Log.i("responseTopic", "registerInsert-" + responseTopic + "-");
									//Toast.makeText(this, "Response from server - " + response,Toast.LENGTH_SHORT).show();
									topic.setText(responseTopic);
									value = (TextView) findViewById(R.id.seekBarValues);
									value.setText("Set your Mood here and press set!");
									
									// update list view
									ListView listView = (ListView) findViewById(R.id.list);
									ArrayList<String> listItems = new ArrayList<String>();
									
									String moods = client.getMoods(serverURL);
									
									if (moods.equals("NoMoods\n")) {
										
										listItems.add("No Mood Set!");
										listView.setAdapter(new ArrayAdapter<String>(this, R.layout.row, R.id.label, listItems));
										
									} else {
										
										// Load moods with color
										
									}
									
								}
						
							} else if(response.equals("Sorry")){
								Toast.makeText(this, "Sorry, user can't be registered! Please try again!",Toast.LENGTH_SHORT).show();
								((EditText) findViewById(R.id.userName)).setSelected(true);
								
							}else {
								Toast.makeText(this, "Sorry, some error. Please try again!",Toast.LENGTH_SHORT).show();
								((EditText) findViewById(R.id.userName)).setSelected(true);
							}
							
							// onRefresh();
							//finish();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(this,"Name or ServerURL not added! See log.",Toast.LENGTH_SHORT).show();
						}
					}
					
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				
				
			}

		}

	}
	

}