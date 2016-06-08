package br.ufc.great.greattour;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufc.great.greattour.streamFiles.PlayImagesActivity;
import br.ufc.great.greattour.utils.logger.Logger;


/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class PhotosActivity extends Activity {

	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    public String resultServer = "";

    ArrayList<HashMap<String, Object>> MyArrList;


    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_photo_activity);

        // LOGGER LOG ENTRY
		Logger.addEntry("Displaying Images");

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

	    // Download JSON File
		new DownloadJSONFileAsync().execute();

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DOWNLOAD_JSON_PROGRESS:
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Downloading.....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            return mProgressDialog;
        default:
            return null;
        }
    }

    // Show All Content
    public void ShowAllContent()
    {
        // gridView1
        final GridView gridV = (GridView)findViewById(R.id.gridView1);
        gridV.setAdapter(new ImageAdapter(PhotosActivity.this,MyArrList));

        // OnClick
        gridV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				String Position = String.valueOf(position);

				Intent newActivity = new Intent(PhotosActivity.this,PlayImagesActivity.class);
				newActivity.putExtra("Position", Position);
				newActivity.putExtra("resultServer", resultServer);
				startActivity(newActivity);

			}
		});
    }



    public class ImageAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, Object>> MyArr = new ArrayList<HashMap<String, Object>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, Object>> myArrList)
        {
        	// TODO Auto-generated method stub
            context = c;
            MyArr = myArrList;
        }

        public int getCount() {
        	// TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
        	// TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_photo, null);
			}

			// ColPhoto
			ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPhoto);
			imageView.getLayoutParams().height = 80;
			imageView.getLayoutParams().width = 80;
			imageView.setPadding(10, 10, 10, 10);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        	 try
        	 {
        		 imageView.setImageBitmap((Bitmap)MyArr.get(position).get("ImageThumBitmap"));
        	 } catch (Exception e) {
        		 // When Error
        		 imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        	 }

			// ColID
			TextView txtID = (TextView) convertView.findViewById(R.id.ColID);
			txtID.setPadding(5, 0, 0, 0);
			txtID.setText("ID : " + MyArr.get(position).get("ImageID").toString());

			// ColName
			TextView txtName = (TextView) convertView.findViewById(R.id.ColName);
			txtName.setPadding(5, 0, 0, 0);
			txtName.setText("Name : " + MyArr.get(position).get("ImageName").toString());

			return convertView;

		}

    }



    // Download JSON in Background
    public class DownloadJSONFileAsync extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {
        	super.onPreExecute();
        	showDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub


        	String url = "http://pesquisa.great.ufc.br/greattourv2/teste.php";

        	JSONArray data;
			try {
				resultServer = getJSONUrl(url);
				data = new JSONArray(resultServer);

		    	MyArrList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> map;

				for(int i = 0; i < data.length(); i++){
	                JSONObject c = data.getJSONObject(i);
	    			map = new HashMap<String, Object>();
	    			map.put("ImageID", (String)c.getString("id_file"));
	    			map.put("ImageName", (String)c.getString("description_pt"));

	    			// Thumbnail Get ImageBitmap To Bitmap
	    			map.put("ImagePathThum", (String)c.getString("url"));
	    			map.put("ImageThumBitmap", (Bitmap)loadBitmap(c.getString("url")));

	    			// Full (for View Full)
	    			map.put("ImagePathFull", (String)c.getString("url"));

	    			MyArrList.add(map);
				}


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		return null;
        }

        protected void onPostExecute(Void unused) {
        	ShowAllContent(); // When Finish Show Content
            dismissDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
            removeDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
        }


    }


    /*** Get JSON Code from URL ***/
  	public String getJSONUrl(String url) {
  		StringBuilder str = new StringBuilder();
  		HttpClient client = new DefaultHttpClient();
  		HttpGet httpGet = new HttpGet(url);
  		try {
  			HttpResponse response = client.execute(httpGet);
  			StatusLine statusLine = response.getStatusLine();
  			int statusCode = statusLine.getStatusCode();
  			if (statusCode == 200) { // Download OK
  				HttpEntity entity = response.getEntity();
  				InputStream content = entity.getContent();
  				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
  				String line;
  				while ((line = reader.readLine()) != null) {
  					str.append(line);
  				}
  			} else {
  				Log.e("Log", "Failed to download file..");
  			}
  		} catch (ClientProtocolException e) {
  			e.printStackTrace();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		return str.toString();
  	}


  	/***** Get Image Resource from URL (Start) *****/
	private static final String TAG = "Image";
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	public static Bitmap loadBitmap(String url) {
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	        in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	        out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
	        copy(in, out);
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	        Log.e(TAG, "Could not load Bitmap from: " + url);
	    } finally {
	        closeStream(in);
	        closeStream(out);
	    }

	    return bitmap;
	}

	 private static void closeStream(Closeable stream) {
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                android.util.Log.e(TAG, "Could not close stream", e);
	            }
	        }
	    }

	 private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
	 /***** Get Image Resource from URL (End) *****/



}
