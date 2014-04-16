package sjsu.cs286.hw2.test;

import sjsu.cs286.hw2.AddActivity;
import android.test.*;
import android.widget.TextView;

public class AddActivityTest extends
		ActivityInstrumentationTestCase2<AddActivity> {
	private AddActivity mActivity;
	private TextView mView;
	private String resourceString;

	public AddActivityTest() {
		super("sjsu.cs286.hw2", AddActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		resourceString = mActivity.getString(sjsu.cs286.hw2.R.string.hello);
	}

	public void testPreconditions() {
		assertNotNull(resourceString);
		
	}

	public void testText() {
		assertEquals(resourceString, "Flash Card Activity!");
	}

	public void testWordLabel() {
		TextView mView1 = (TextView) mActivity.findViewById(sjsu.cs286.hw2.R.id.Word_Label);
		assertNotNull(mView1);
		String resourceString1 = mActivity.getString(sjsu.cs286.hw2.R.string.Word_Label);
		assertEquals(resourceString1, (String) mView1.getText());
		
	}
	
	public void testDescriptionLabel() {
		TextView mView1 = (TextView) mActivity.findViewById(sjsu.cs286.hw2.R.id.Description_Label);
		assertNotNull(mView1);
		String resourceString1 = mActivity.getString(sjsu.cs286.hw2.R.string.Description_Label);
		assertEquals(resourceString1, (String) mView1.getText());
		
	}
	
	public void testCancelButtonLabel() {
		TextView mView1 = (TextView) mActivity.findViewById(sjsu.cs286.hw2.R.id.cancel);
		assertNotNull(mView1);
		String resourceString1 = mActivity.getString(sjsu.cs286.hw2.R.string.Button_Cancel_Label);
		assertEquals(resourceString1, (String) mView1.getText());
		
	}

	public void testResetButtonLabel() {
		TextView mView1 = (TextView) mActivity.findViewById(sjsu.cs286.hw2.R.id.reset);
		assertNotNull(mView1);
		String resourceString1 = mActivity.getString(sjsu.cs286.hw2.R.string.Button_Reset_Label);
		assertEquals(resourceString1, (String) mView1.getText());
		
	}

	public void testSaveButtonLabel() {
		TextView mView1 = (TextView) mActivity.findViewById(sjsu.cs286.hw2.R.id.save);
		assertNotNull(mView1);
		String resourceString1 = mActivity.getString(sjsu.cs286.hw2.R.string.Button_Save_Label);
		assertEquals(resourceString1, (String) mView1.getText());
		
	}
}

