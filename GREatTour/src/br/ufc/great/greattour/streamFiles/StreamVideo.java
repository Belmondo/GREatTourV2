package br.ufc.great.greattour.streamFiles;

import br.ufc.great.greattour.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class StreamVideo extends Activity {

	//Declarado o videoView
	VideoView vidView;
	//declarando o controlador do video
	MediaController vidControl;

	String value;

	// vale lembrar que o android só aceita os seguintes formatos:
	//3GP, MP4, WEBM, and MKV,

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_video_stream);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("url");
        }
        //instanciando o VidView
        vidView = (VideoView)findViewById(R.id.myVideo);

        //link do video à fazer o stream
        String vidAddress = value;
        //"convertendo à um objeto URI"
        Uri vidUri = Uri.parse(vidAddress);

        //passando o objeto URI para a VideoView
        vidView.setVideoURI(vidUri);

        //instanciando o controlador do video
        vidControl = new MediaController(this);
        //colocando para ele usar o videoView como ancora, ou seja, onde aparecer
        vidControl.setAnchorView(vidView);
        //setando o mediaControl para o objeto videoView
        vidView.setMediaController(vidControl);

        //startando o video
        vidView.start();
}

}
