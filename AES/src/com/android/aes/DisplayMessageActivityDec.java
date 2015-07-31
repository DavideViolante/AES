package com.android.aes;

import com.code.aes.Decryption;
import com.code.aes.Functions;

import android.support.v4.app.NavUtils;

public class DisplayMessageActivityDec extends Activity {
	private Functions f = new Functions();
	private Decryption dec = new Decryption();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message_dec);
		
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Get the message form the intent
        Intent intent = getIntent();
        String deeditMsg = intent.getStringExtra(MainActivity.DEEXTRA_PAINTEXT);
        String deeditKey = intent.getStringExtra(MainActivity.DEEXTRA_KEY);
        
        // Encryption
        byte[] input = f.hexString2bytes(deeditMsg);
        byte[] key = f.hexString2bytes(deeditKey);
		byte[] decifrato = dec.InvCipher(input, key);
		
		// Stampa su schermo questo
		String plaintext = f.bytes2hexString(decifrato);
                
        // Create the text view
		TextView textDeCipher = (TextView)findViewById(R.id.plaintext_result);
		TextView textPlain = (TextView)findViewById(R.id.ciphertext_input);
		TextView textKey = (TextView)findViewById(R.id.key_input);
		textPlain.setText(deeditMsg);
		textKey.setText(deeditKey);
		textDeCipher.setText(plaintext);
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
		getMenuInflater().inflate(R.menu.display_message_activity_dec, menu);
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
