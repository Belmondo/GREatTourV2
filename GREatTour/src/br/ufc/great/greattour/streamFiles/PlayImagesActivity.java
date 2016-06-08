package br.ufc.great.greattour.streamFiles;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import br.ufc.great.greattour.PhotosActivity;
import br.ufc.great.greattour.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */


public class PlayImagesActivity extends Activity {

	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    public int curPosition  = 0;
    public String resultServer;
    public ArrayList<HashMap<String, Object>> MyArrList;

    public Button back;
    public Button next;


    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent= getIntent();
        curPosition = Integer.parseInt(intent.getStringExtra("Position"));
        resultServer = String.valueOf(intent.getStringExtra("resultServer"));

        try {
			MyArrList = ConvertJSONtoArrayList(resultServer);

		} catch (JSONException e) {

			e.printStackTrace();
		}

        Log.d("ArrayList Size",String.valueOf(MyArrList.size()));

        // Show Image Full
        new DownloadFullPhotoFileAsync().execute();


        // Button Back
        back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	curPosition = curPosition - 1;
            	new DownloadFullPhotoFileAsync().execute();
            }
        });

        // Button Next
        next = (Button) findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	curPosition = curPosition + 1;
            	new DownloadFullPhotoFileAsync().execute();
            }
        });

    }


    // Show Image Full
    public void ShowImageFull(String imageName, Bitmap imgFull)
    {

        // Prepare Button (Back)
        if(curPosition <= 0)
        {
        	back.setEnabled(false);
        }
        else
        {
        	back.setEnabled(true);
        }

        // Prepare Button (Next)
        Log.d("curPosition",String.valueOf(curPosition));
        Log.d("MyArrList.size",String.valueOf(MyArrList.size()));
        if(curPosition >= MyArrList.size() - 1)
        {
        	next.setEnabled(false);
        }
        else
        {
        	next.setEnabled(true);
        }



         ImageView image = (ImageView) findViewById(R.id.fullimage);

     	 try
     	 {
     		image.setImageBitmap(imgFull);
     	 } catch (Exception e) {
     		 // When Error
     		image.setImageResource(android.R.drawable.ic_menu_report_image);
     	 }

     	 // Show Toast
     	Toast.makeText(PlayImagesActivity.this,imageName,Toast.LENGTH_LONG).show();

    }

    public ArrayList<HashMap<String, Object>> ConvertJSONtoArrayList(String json) throws JSONException
    {

    	JSONArray data = new JSONArray(resultServer);

		ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;

		for(int i = 0; i < data.length(); i++){
            JSONObject c = data.getJSONObject(i);
			map = new HashMap<String, Object>();
			map.put("ImageID", (String)c.getString("id_file"));
			map.put("ImageName", (String)c.getString("description_pt"));
			map.put("ImagePathThum", (String)c.getString("url"));
			map.put("ImagePathFull", (String)c.getString("url"));

			arr.add(map);
		}

		return arr;

    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS:
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


    public static final int DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS = 1;
    // Download Full Photo in Background
    public class DownloadFullPhotoFileAsync extends AsyncTask<String, Void, Void> {

    	String strImageName = "";
    	String ImageFullPhoto = "";

    	Bitmap ImageFullBitmap = null;

        protected void onPreExecute() {
        	super.onPreExecute();
        	showDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {
        	strImageName = MyArrList.get(curPosition).get("ImageName").toString();
            ImageFullPhoto = MyArrList.get(curPosition).get("ImagePathFull").toString();

            ImageFullBitmap = (Bitmap)loadBitmap(ImageFullPhoto);
    		return null;
        }

        protected void onPostExecute(Void unused) {
        	ShowImageFull(strImageName,ImageFullBitmap); // When Finish Show Images
            dismissDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
            removeDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
        }


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




}
