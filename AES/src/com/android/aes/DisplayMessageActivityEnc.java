package com.android.aes;

import android.support.v4.app.NavUtils;
import com.code.aes.Encryption;
import com.code.aes.Functions;

public class DisplayMessageActivityEnc extends Activity {
	private Functions f = new Functions();
	private Encryption enc = new Encryption();
	
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message_enc);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Get the message form the intent
        Intent intent = getIntent();
        String editMsg = intent.getStringExtra(MainActivity.EXTRA_PAINTEXT);
        String editKey = intent.getStringExtra(MainActivity.EXTRA_KEY);
        
        // Encryption
        byte[] input = f.hexString2bytes(editMsg);
        byte[] key = f.hexString2bytes(editKey);
		byte[] cifrato = enc.Cipher(input, key);
		
		// Stampa su schermo questo
		String ciphertext = f.bytes2hexString(cifrato);
                
        // Create the text view
		TextView textCipher = (TextView)findViewById(R.id.ciphertext_result);
		TextView textPlain = (TextView)findViewById(R.id.plaintext_input);
		TextView textKey = (TextView)findViewById(R.id.key_input);
		textCipher.setText(ciphertext);
        textPlain.setText(editMsg);
        textKey.setText(editKey);
    }


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
