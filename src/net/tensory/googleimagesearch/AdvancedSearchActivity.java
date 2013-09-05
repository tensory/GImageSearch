package net.tensory.googleimagesearch;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

public class AdvancedSearchActivity extends Activity {
	HashMap<String, String> params = new HashMap<String, String>();
	Spinner colors, sizes, types;
	EditText site;
	public static final String GOOGLE_SITE = "as_sitesearch";
	public static final String GOOGLE_IMAGE_SIZE = "imgsz";
	public static final String GOOGLE_IMAGE_COLOR = "imgcolor";
	public static final String GOOGLE_IMAGE_TYPE = "imgtype";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_advanced_search);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    // Initialize spinner and input listeners
	    colors = (Spinner) findViewById(R.id.ddColors);
	    colors.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(GOOGLE_IMAGE_COLOR));
	    
	    sizes = (Spinner) findViewById(R.id.ddImageSizes);
	    sizes.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(GOOGLE_IMAGE_SIZE));
	    
	    types = (Spinner) findViewById(R.id.ddImageTypes);
	    types.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(GOOGLE_IMAGE_TYPE));
	    
	    site = (EditText) findViewById(R.id.etSite);
	    site.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				params.put(AdvancedSearchActivity.GOOGLE_SITE, s.toString());
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanced_search, menu);
		return true;
	}
	
	public class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
		public String key;
		public String value;
		
		public SpinnerOnItemSelectedListener(String tag) {
			super();
			key = tag;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			value = parent.getItemAtPosition(position).toString();
			params.put(key, value);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}	
	}
	
	public void onFilterSave(View v) {
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra("searchParams", makeUrlEncodedParamString());
		setResult(Activity.RESULT_OK, i);
		finish();
	}
	
	public String makeUrlEncodedParamString() {
		// From http://stackoverflow.com/questions/7671597/convert-map-to-querystring
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, String> entry : params.entrySet()) {
			try {
				if (sb.length() > 0) {
			          sb.append('&');
			    } 
				sb.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
		return sb.toString();	
	}
}
	