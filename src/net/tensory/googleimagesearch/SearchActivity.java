package net.tensory.googleimagesearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.GridView;
import android.widget.Toast;
import net.tensory.googleimagesearch.ImageResult;

public class SearchActivity extends Activity {
	EditText etQuery;
	Button btnSearch;
	Button btnMoreResults;
	GridView gvResults;
	TextView txtAdvancedSearchButton;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	String searchParams;
	
	public static final int ADVANCED_SEARCH_ACTIVITY_ID = 9; // I just pulled this number out of a hat, what should I have used?
	public static final int RESULT_COUNT = 8; 
	
    List<String> listFilterDataHeader;
    HashMap<String, List<String>> listFilterDataChild;
	
	private String searchUriBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		searchUriBase = getResources().getString(R.string.txtSearchUriBase) + "rsz=" + RESULT_COUNT + "&v=1.0&";
		searchParams = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
		txtAdvancedSearchButton = (TextView) findViewById(R.id.txtAdvancedSearchLabel);
		btnMoreResults = (Button) findViewById(R.id.btnMoreResults);
	}
	
	public void setupFilters() {
		listFilterDataHeader = new ArrayList<String>();
		listFilterDataChild = new HashMap<String, List<String>>();
		listFilterDataHeader.add(getResources().getString(R.string.txtFilterGroupLabel));
		
		List<String> filters = new ArrayList<String>();
		filters.add(getResources().getString(R.string.txtFilterNameColor));
		listFilterDataChild.put(listFilterDataHeader.get(0), filters);
	}
	
	public void onImageSearch(View v) {
		doSearch();
		
	}
	
	public void doSearch() {
		String query = etQuery.getText().toString();

		AsyncHttpClient searchClient = new AsyncHttpClient();
		String queryString = searchUriBase + "start=" + 0 + "&q=" + Uri.encode(query);
		
		if (query.length() == 0) {
			Toast.makeText(this, getResources().getString(R.string.txtSearchError), Toast.LENGTH_LONG).show();
			return;
		}
		
		if (searchParams.length() > 0) {
			queryString += "&" + searchParams;
		}
		
		searchClient.get(
				queryString,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject(
									"responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							
							showMoreResultsInterface();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
		}); 
		
	}
	
	public void onAdvancedSearchClick(View v) {
		startActivityForResult(new Intent(getApplicationContext(), AdvancedSearchActivity.class), SearchActivity.ADVANCED_SEARCH_ACTIVITY_ID);
	}
		
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
	      if (requestCode == ADVANCED_SEARCH_ACTIVITY_ID) {
	          if (resultCode == RESULT_OK) {
	            String paramString = data.getStringExtra("searchParams");
	            if (paramString.length() > 0) {
	            	this.searchParams = paramString;
	            	this.doSearch();
	            }
	          }
	      }
	}
	
	public void showMoreResultsInterface() {
		btnMoreResults.setVisibility(View.VISIBLE);
	}
}
