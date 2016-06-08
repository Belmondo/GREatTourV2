package br.ufc.great.greattour;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.JSON.JSONParser;
import br.ufc.great.example.requiredclassesHTTPrequest.*;
import br.ufc.great.greattour.utils.logger.Logger;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.ufc.great.returnpersons.ReturnPersonActivity;

/**
 * @author Rossana Maria
 * @author Ismayle Santos
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

/*
 *
 * Esta é a activity principal. Mostra a sala corrente para o usuário
 */

public class CurrentRoomActivity extends Activity {

	// imageView que aqui são os "botões"
	ImageView qrButton;
	static ImageView files;
	static ImageView persons;
	static ImageView legends;
	static ImageView greatMap;

	ImageView about;
	TextView contentTxt;

	/* bloco de teste */

	String contextkey = "context.device.locale";

	public List<String> list;
	private ISysSUService service;
	/* bloco de teste */

	static String roomCodeParameter;

	JSONParser jParser = new JSONParser();

	private static String CURRENT_ROOM_TAKE_INFORMATIONS = "http://pesquisa.great.ufc.br/greattourv2/current_room.php";
	private static final String TAG_SUCCESS = "success";
	private ProgressDialog pDialog;
	String idLocal = null;

	@Override
	public void onCreate(Bundle savedInstanceState) { // OK
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.currentroomscreen);

		roomCodeParameter = null;
		/*
		 * bloco de teste
		 */

		list = new ArrayList<String>();
		list.add(contextkey);

		/* bloco de teste */
		qrButton = (ImageView) findViewById(R.id.qrButton); // imageview
															// referente ao
															// qrCode
		files = (ImageView) findViewById(R.id.documents); // imageview referente
															// aos documentos
		persons = (ImageView) findViewById(R.id.persons); // imageview referente
															// as pessoas
		legends = (ImageView) findViewById(R.id.legends); // imageview referente
															// as legendas

		about = (ImageView) findViewById(R.id.about);

		greatMap = (ImageView) findViewById(R.id.greatMap);// imageview da
															// imagem central

		// formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView) findViewById(R.id.scan_content);

		qrButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				IntentIntegrator scanIntegrator = new IntentIntegrator(
						CurrentRoomActivity.this);

				// LOGGER LOG ENTRY
				Logger.addEntry("Starting to Read QRCode");
				scanIntegrator.initiateScan();

			}
		});

		// dependendo do mapa atual ele mostra diferentes informações acerca dos
		// documentos e das pessoas

		files.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Toast.makeText(CurrentRoomActivity.this, "Clicou nos Files",
				// Toast.LENGTH_LONG).show();
				Intent filesIntent = new Intent(CurrentRoomActivity.this,
						FilesActivity.class);
				// enviando parametro(codigo da sala corrente) para a proxima
				// pagina
				filesIntent.putExtra("currentRoomcode", roomCodeParameter);
				startActivity(filesIntent);
			}
		});

		// dependendo do mapa atual ele mostra diferentes informações acerca dos
		// documentos e das pessoas
		persons.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.v("clicou nos pesquisadores", "clicou");

				Intent intentPersons = new Intent(CurrentRoomActivity.this,
						ReturnPersonActivity.class);
							intentPersons.putExtra("currentRoomCode", roomCodeParameter);
				startActivity(intentPersons);
			}
		});

		legends.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // OK
				// Log.v(TAG, " click");

				Intent intent = new Intent(CurrentRoomActivity.this,
						LegendActivity.class);
				startActivity(intent);
			}
		});

		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intentAbout = new Intent(CurrentRoomActivity.this,
						AboutActivity.class);
				startActivity(intentAbout);

			}
		});

	}

	// Pra garantir que o cara só saia dessa tela se clicar 2 vezes e não apenas
	// uma
	private long lastbackclick = 0;
	private Toast toast = null;

	public void onBackPressed() {

		if (lastbackclick < System.currentTimeMillis() - 3000) {

			Toast.makeText(CurrentRoomActivity.this,
					"Press Return again to exit.", Toast.LENGTH_LONG).show();

			lastbackclick = System.currentTimeMillis();
		} else {
			if (toast != null)
				toast.cancel();
			finish();
			super.onBackPressed();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanningResult != null) {

			String scanContent = scanningResult.getContents();

			if (scanContent != null) {

				roomCodeParameter = scanContent;

				Toast.makeText(CurrentRoomActivity.this, roomCodeParameter, Toast.LENGTH_LONG).show();

				// Loader image - will be shown before loading image
				int loader = R.drawable.loader;
				// Imageview to show
				ImageView image = (ImageView) findViewById(R.id.greatMap);
				// Image url
				String image_url = "http://pesquisa.great.ufc.br/greattourv2/current_room.php?id_environment=";
				image_url = image_url.concat(scanContent);
				Log.d("Url da imagem encontrada", image_url);
				// ImageLoader class instance
				ImageLoader imgLoader = new ImageLoader(this);
				imgLoader.DisplayImage(image_url, loader, image);

			}

		}

	}

	public static void lessThenTenPercentBattery() {

		files.setVisibility(View.GONE);
	}

}
