package br.ufc.great.greattour.streamFiles;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import br.ufc.great.greattour.R;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class StreamAudio extends Activity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener{

	public String value;

	private ImageButton buttonPlayPause;
	private SeekBar seekBarProgress;


	private MediaPlayer mediaPlayer;
	private int mediaFileLengthInMilliseconds;


	private final Handler handler = new Handler();


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_file_audio_stream);


		Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("url");
        }


        buttonPlayPause = (ImageButton)findViewById(R.id.ButtonTestPlayPause);
		buttonPlayPause.setOnClickListener(this);

		seekBarProgress = (SeekBar)findViewById(R.id.SeekBarTestPlay);
		seekBarProgress.setOnTouchListener(this);


		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);

	}

	private void primarySeekBarProgressUpdater() {
    	seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100));

		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}
    }

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		/** Method which updates the SeekBar secondary progress by current song loading from URL position*/
		seekBarProgress.setSecondaryProgress(percent);

	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		 /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
		buttonPlayPause.setImageResource(R.drawable.button_play);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.SeekBarTestPlay){
			/** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
			if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ButtonTestPlayPause){
			 /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
			try {
				mediaPlayer.setDataSource(value);
				mediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}

			mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				buttonPlayPause.setImageResource(R.drawable.button_pause);
			}else {
				mediaPlayer.pause();
				buttonPlayPause.setImageResource(R.drawable.button_play);
			}

			primarySeekBarProgressUpdater();
		}

	}





}
