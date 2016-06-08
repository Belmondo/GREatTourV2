package br.ufc.great.greattour;

import br.ufc.great.greattour.utils.logger.Logger;
import br.ufc.great.pageslider.PageSlider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class FilesActivity extends Activity {

	static ImageView images;
	static ImageView videos;
	static ImageView texts;
	static ImageView audios;
	ImageView iconHouse;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.filesscreen);

        // LOGGER LOG ENTRY
		Logger.addEntry("Displaying Media");

		Intent takeCurrentValue = getIntent();
		final String currentRoomCode = takeCurrentValue.getStringExtra("currentRoomcode");

		Toast.makeText(FilesActivity.this,currentRoomCode , Toast.LENGTH_LONG).show();


		texts = (ImageView) findViewById(R.id.imageViewTEXTS);
		images = (ImageView) findViewById(R.id.imageViewIMAGES);
		videos = (ImageView) findViewById(R.id.imageViewVIDEOS);
		audios = (ImageView) findViewById(R.id.imageViewAudio);

		iconHouse = (ImageView) findViewById(R.id.iconHouse);

		texts.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentDocumentos = new Intent(FilesActivity.this,
						DocumentsActivity.class);

				intentDocumentos.putExtra("currentRoomCode",currentRoomCode);
				startActivity(intentDocumentos);


			}
		});

		images.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {


				Intent intentImages = new Intent(FilesActivity.this,
						PageSlider.class);

				startActivity(intentImages);


			}
		});

		videos.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentVideos = new Intent(FilesActivity.this,
						VideoActivity.class);
				intentVideos.putExtra("currentRoomCode",currentRoomCode);
				startActivity(intentVideos);

			}
		});

		audios.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentSound = new Intent(FilesActivity.this,
						SoundActivity.class);
				intentSound.putExtra("currentRoomCode",currentRoomCode);
				startActivity(intentSound);

			}
		});

		iconHouse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentSound = new Intent(FilesActivity.this,
						CurrentRoomActivity.class);
				startActivity(intentSound);

			}
		});

	}

	public static void between20e30() {

		videos.setVisibility(View.GONE);
		audios.setVisibility(View.GONE);

	}

	public static void between10e20() {

		videos.setVisibility(View.GONE);
		images.setVisibility(View.GONE);
		audios.setVisibility(View.GONE);
	}

	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
