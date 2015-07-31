package com.android.aes;

public class MainActivity extends Activity {
	public static final String EXTRA_PAINTEXT = "com.example.aes.MESSAGE";
	public static final String EXTRA_KEY = "com.example.aes.KEY";
	public static final String DEEXTRA_PAINTEXT = "com.example.aes.DEMESSAGE";
	public static final String DEEXTRA_KEY = "com.example.aes.DEKEY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks Encrypt button */
	public void sendMessage(View view) {
		// mi preparo per andare su un'altra activity
		Intent intent = new Intent(this, DisplayMessageActivityEnc.class);
		
		// prendo attraverso l'id i form dove è stato inserito del testo
		EditText editPlainText = (EditText) findViewById(R.id.edit_plaintext);
		EditText editKey = (EditText) findViewById(R.id.edit_key);

		// controllo lunghezze
		if(editPlainText.getText().length() != 32) {
			editPlainText.setError("Text must be 32 characters long");
			return;
		} else {
			editPlainText.setError(null);
		}
		switch(editKey.getText().length()) {
			case 32: case 48: case 64: {
				editKey.setError(null);
				break;
			}
			default: {
				editKey.setError("Key must be 32, 48 or 64 characters long");
				return;
			}
		}
		
		// prendo i testi che sono stati inseriti e li faccio stringa
		String plaintext = editPlainText.getText().toString();
		String key = editKey.getText().toString();

		// passo all'intent il messaggio come chiave-valore (constant, msg)
		intent.putExtra(EXTRA_PAINTEXT, plaintext);
		intent.putExtra(EXTRA_KEY, key);
		startActivity(intent);
	}
	
	/** Called when the user clicks Decrypt button */
	public void deSendMessage(View view) {
		// mi preparo per andare su un'altra activity
		Intent intent = new Intent(this, DisplayMessageActivityDec.class);
		
		// prendo attraverso l'id i form dove è stato inserito del testo
		EditText deeditPlainText = (EditText) findViewById(R.id.deedit_plaintext);
		EditText deeditKey = (EditText) findViewById(R.id.deedit_key);

		// controllo lunghezze
		if(deeditPlainText.getText().length() != 32) {
			deeditPlainText.setError("Text must be 32 characters long");
			return;
		} else {
			deeditPlainText.setError(null);
		}
		switch(deeditKey.getText().length()) {
			case 32: case 48: case 64: {
				deeditKey.setError(null);
				break;
			}
			default: {
				deeditKey.setError("Key must be 32, 48 or 64 characters long");
				return;
			}
		}
		
		// prendo i testi che sono stati inseriti e li faccio stringa
		String deplaintext = deeditPlainText.getText().toString();
		String dekey = deeditKey.getText().toString();

		// passo all'intent il messaggio come chiave-valore (constant, msg)
		
		intent.putExtra(DEEXTRA_PAINTEXT, deplaintext);
		intent.putExtra(DEEXTRA_KEY, dekey);
		startActivity(intent);
	}

}
