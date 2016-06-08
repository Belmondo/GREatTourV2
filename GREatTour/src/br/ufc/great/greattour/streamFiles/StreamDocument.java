package br.ufc.great.greattour.streamFiles;

import java.io.File;
import java.io.IOException;




import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class StreamDocument extends Activity {

	String value;

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	Bundle extras = getIntent().getExtras();
    if (extras != null) {
        value = extras.getString("url");
    }

    	new DownloadFile().execute(value, "dissertacao.pdf");

	    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/pdfGreatTour/" + "dissertacao.pdf");  // -> filename
	    Uri path = Uri.fromFile(pdfFile);
	    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
	    pdfIntent.setDataAndType(path, "application/pdf");
	    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	    try{
	        startActivity(pdfIntent);
	    }catch(ActivityNotFoundException e){
	        Toast.makeText(StreamDocument.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
	    }



	}


	 private class DownloadFile extends AsyncTask<String, Void, Void>{

	        @Override
	        protected Void doInBackground(String... strings) {
	            String fileUrl = strings[0];   // -> Local de onde ele tem que ser baixado
	            String fileName = strings[1];  // -> nome do arquivo
	            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	            File folder = new File(extStorageDirectory, "pdfGreatTour");
	            folder.mkdir();

	            File pdfFile = new File(folder, fileName);

	            try{
	                pdfFile.createNewFile();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	            DocumentDownload.downloadFile(fileUrl, pdfFile);
	            return null;
	        }
	    }


	}
