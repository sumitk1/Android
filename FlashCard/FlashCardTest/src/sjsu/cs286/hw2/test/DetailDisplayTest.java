package sjsu.cs286.hw2.test;

import sjsu.cs286.hw2.DetailDisplay;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.widget.TextView;

public class DetailDisplayTest extends
		ActivityInstrumentationTestCase2<DetailDisplay> {
	private DetailDisplay mActivity;
	private TextView mView;
	private String resourceString;
	private RenamingDelegatingContext context = null;

	public DetailDisplayTest() {
		super("sjsu.cs286.hw2", DetailDisplay.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent addEvent = new Intent();
	    addEvent.setClassName("sjsu.cs286.hw2", "DetailDisplay.class");
	    addEvent.putExtra("word", "Some Word");
	    addEvent.putExtra("description", "Some Description");
	    setActivityIntent(addEvent);
	    context = new RenamingDelegatingContext(getActivity(), "test_");
		mActivity = this.getActivity();
		resourceString = mActivity.getString(sjsu.cs286.hw2.R.string.hello);
		
		//TODO: Setup Mock for database objects to mock database calls
	}

	public void testPreconditions() {
		assertNotNull(resourceString);

	}

	public void testText() {
		assertEquals(resourceString, "Flash Card Activity!");
	}

	public void testWordLabel() {
		TextView mView1 = (TextView) mActivity
				.findViewById(sjsu.cs286.hw2.R.id.Word_Label_Display);
		assertNotNull(mView1);
		String resourceString1 = mActivity
				.getString(sjsu.cs286.hw2.R.string.Word_Label);
		assertEquals(resourceString1, (String) mView1.getText());

	}

	public void testDescriptionLabel() {
		TextView mView1 = (TextView) mActivity
				.findViewById(sjsu.cs286.hw2.R.id.Description_Label_Display);
		assertNotNull(mView1);
		String resourceString1 = mActivity
				.getString(sjsu.cs286.hw2.R.string.Description_Label);
		assertEquals(resourceString1, (String) mView1.getText());

	}

	public void testEditButtonLabel() {
		TextView mView1 = (TextView) mActivity
				.findViewById(sjsu.cs286.hw2.R.id.edit);
		assertNotNull(mView1);
		String resourceString1 = mActivity
				.getString(sjsu.cs286.hw2.R.string.Button_Edit_Label);
		assertEquals(resourceString1, (String) mView1.getText());

	}

	public void testResetButtonLabel() {
		TextView mView1 = (TextView) mActivity
				.findViewById(sjsu.cs286.hw2.R.id.delete);
		assertNotNull(mView1);
		String resourceString1 = mActivity
				.getString(sjsu.cs286.hw2.R.string.Button_Delete_Label);
		assertEquals(resourceString1, (String) mView1.getText());

	}

	public void testSaveButtonLabel() {
		TextView mView1 = (TextView) mActivity
				.findViewById(sjsu.cs286.hw2.R.id.exit);
		assertNotNull(mView1);
		String resourceString1 = mActivity
				.getString(sjsu.cs286.hw2.R.string.Button_Exit_Label);
		assertEquals(resourceString1, (String) mView1.getText());

	}
}
