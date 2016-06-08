package br.ufc.great.greattour;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import br.ufc.great.greattour.streamFiles.StreamDocument;
import br.ufc.great.greattour.utils.logger.Logger;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */
public class DocumentsActivity extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://pesquisa.great.ufc.br/greattourv2/return_files.php?id_environment=";
	private static String finalurl;


	private static String type = "&id_type=1";

	// JSON Node names
	private static final String TAG_CONTACTS = "Files";
	private static final String TAG_DESCRIPTION = "description_pt";
	private static final String TAG_URL = "url";


	// TextView linkVideo;

	// contacts JSONArray
	JSONArray contacts = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_lists);

        // LOGGER LOG ENTRY
		Logger.addEntry("Displaying Texts");

		Intent takeCurrentValue = getIntent();
		String currentRoomCode = takeCurrentValue.getStringExtra("currentRoomCode");


		finalurl = url+currentRoomCode+type;


		contactList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String linkVideoOnClicable = ((TextView) view
						.findViewById(R.id.tvLinkDocumento)).getText()
						.toString();


				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						StreamDocument.class);
				in.putExtra(TAG_URL, linkVideoOnClicable);

				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetContacts().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(DocumentsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			//
			String jsonStr = sh.makeServiceCall(finalurl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					contacts = jsonObj.getJSONArray(TAG_CONTACTS);

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);

						// String id = c.getString("description_pt");
						String urlLink = c.getString("url");
						String description_pt = c.getString("description_pt");

						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						// contact.put(TAG_ID, id);
						contact.put(TAG_DESCRIPTION, description_pt);
						contact.put(TAG_URL, urlLink);



						// adding contact to contact list
						contactList.add(contact);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(DocumentsActivity.this,
					contactList, R.layout.list_item_documents_screen,
					new String[] { TAG_DESCRIPTION, TAG_URL }, new int[] {
							R.id.tvNameDocumento, R.id.tvLinkDocumento });

			setListAdapter(adapter);

			// linkVideo.setVisibility(View.GONE);

		}

	}

}
