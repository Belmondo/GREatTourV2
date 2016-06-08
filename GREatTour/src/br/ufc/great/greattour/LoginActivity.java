package br.ufc.great.greattour;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.os.IBinder;
//import android.os.RemoteException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.ComponentName;
//import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
//import br.ufc.great.syssu.base.Pattern;
//import br.ufc.great.syssu.base.Tuple;
//import br.ufc.great.syssu.base.interfaces.ISysSUService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
//import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.ufc.great.JSON.JSONParser;
import br.ufc.great.greattour.utils.logger.Logger;
import br.ufc.great.loccamlib.LoccamListener;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class LoginActivity extends FragmentActivity implements LoccamListener {

	private LoccamActivator loccam;
	private SharedPreferences sharedPreferences;
	private NfcManager nfcManager;

	static Activity thisActivity = null;

	public static final String PREFS_NAME = "Preferences";



	public List<String> list;
	public List<String> list2;


	Intent intent;



	static EditText usernameEditText;
	static EditText passwordEditText;
	static TextView errorMessage;
	static TextView statusMessage;
	static Button btLogin; // botaoLogin
	Intent i;
	CheckBox rememberCheckbox;

	// diálogo de progresso para ser usado na validação
	private ProgressDialog pDialog;

	// criando um objeto JSON Parser
	JSONParser jParser = new JSONParser();

	// url do webservice vem aqui
	private static String url_login = "http://pesquisa.great.ufc.br/greattourv2/do_login.php";



	private String usuario = null;
	private String senha = null;

	// login JSONArray
	JSONArray products = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loginscreen);



		// LOGGER LOG ENTRY
		Logger.addEntry("Application Start");

		loccam = LoccamActivator.getInstance(getApplicationContext(), getPackageName());
		loccam.connect();

		thisActivity = this;

		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		rememberCheckbox = (CheckBox) findViewById(R.id.checkBox1);
		errorMessage = (TextView) findViewById(R.id.ErrorMenssage);
		statusMessage = (TextView) findViewById(R.id.OkConnectionMenssage);
		btLogin = (Button) findViewById(R.id.btLogin);

		checkStoredInformation();
		// checkNfcInformation();

		// /list = new ArrayList<String>();
		// /list.add(contextkey);

		// pegando clique do botao
		btLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				// insere nas seguintes variaveis para futura verifição
				usuario = usernameEditText.getText().toString();
				senha = passwordEditText.getText().toString();

				if (usuario == "" || senha == "") {

					Toast.makeText(LoginActivity.this, "Please, check your username and your password",
							Toast.LENGTH_LONG).show();
				} else {

					Log.d("Url Login ", url_login);

					new DoLogin().execute();
				}
			}
		});

	}

	/*
	 * public class NfcDialogFragment extends DialogFragment {
	 *
	 * @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
	 * AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 * builder.setMessage(
	 * "Este aplicativo pode uti	lizar as funções de NFC do seu dispositivo. Deseja ativar o NFC?"
	 * ); builder.setPositiveButton("Habilitar NFC", new
	 * DialogInterface.OnClickListener() {
	 *
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * startActivity(new Intent(
	 * android.provider.Settings.ACTION_WIRELESS_SETTINGS)); } });
	 * builder.setNegativeButton("Cancelar", new
	 * DialogInterface.OnClickListener() {
	 *
	 * @Override public void onClick(DialogInterface dialog, int which) { //
	 * Usuário clicou em "Cancelar" } }); return builder.create(); } }
	 *
	 * private void checkNfcInformation() { nfcManager = (NfcManager)
	 * this.getSystemService(Context.NFC_SERVICE); NfcAdapter adapter =
	 * nfcManager.getDefaultAdapter(); Log.d("NFC Check", "check 1"); if
	 * (adapter == null) { // Não há suporte para o NFC neste dispositivo Log.d(
	 * "NFC Check", "check 2, adapter null"); } else if (!adapter.isEnabled()) {
	 * // Há suporte, mas está desabilitado.
	 *
	 * DialogFragment frag = new NfcDialogFragment();
	 * frag.show(getFragmentManager(), "nfc"); Log.d("NFC Check",
	 * "check 3, adapter not enabled");
	 *
	 * } else { Log.d("NFC Check", "check 4, adapter enabled"); } }
	 */

	private void checkStoredInformation() {
		// Checa se existem dados salvos de login do usuário
		sharedPreferences = getSharedPreferences("GTprefs", Context.MODE_PRIVATE);
		if (sharedPreferences.contains("username")) {
			String tempUsername = sharedPreferences.getString("username", "");
			usernameEditText.setText(tempUsername);

		}

		if (sharedPreferences.contains("password")) {
			String tempPassword = sharedPreferences.getString("password", "");
			passwordEditText.setText(tempPassword);
		}
		if (sharedPreferences.contains("checkbox")) {
			if (sharedPreferences.getBoolean("checkbox", false)) {
				rememberCheckbox.setChecked(true);
			} else {
				rememberCheckbox.setChecked(false);
			}

		}

	}

	protected void onResume() {
		// initService();
		// id = ""+getPackageName();
		super.onResume();

		new LongOperation().execute();

	}

	public void onServiceConnected(ISysSUService arg0) {
		// TODO Auto-generated method stub
		loccam.putCACs();

	}

	public void onServiceDisconnected() {
		// TODO Auto-generated method stub
		loccam.disconnect();

	}

	public static void exibeconexao(int value) {

		final int valorrecebido = value;
		// mensagem no console para verificar se conseguiu entrar nesse método
		System.out.println("Entrou no método exibeConexao");

		// thread auxiliar para rodar o toast. Quaisquer alterações visuais
		// decorrentes da captura de contexto
		// deve ser feito dentro de um runnable
		thisActivity.runOnUiThread(new Runnable() {
			public void run() {

				if (valorrecebido == 1) {

					errorMessage.setText("");
					statusMessage.setTextColor(Color.parseColor("#007EC7"));
					statusMessage.setText("Connection Found.");
					btLogin.setEnabled(true); // reabilitando o botão

				} else {

					statusMessage.setText("");
					statusMessage.setTextColor(Color.RED);
					errorMessage.setText("No connection found."); // inserindo
																	// texto de
																	// erro
					btLogin.setEnabled(false); // desabilitando o botão

				}

			}
		});
	}

	protected void onDestroy() {
		// takeInterest();
		// releaseService();
		onServiceDisconnected();
		super.onDestroy();
	}

	protected void onStop() {
		// takeInterest();
		super.onStop();

	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(1000);

					// Jogando interesse no Loccam
					// putInterest();
					loccam.putLightLoCCAMInterest();
					// here
					loccam.putBatteryInterest();
					Log.d("putInterest", "Já inseriu interesse");

				} catch (Exception e) {
					Thread.interrupted();
				}

			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			loccam.readLightLoCCAM();
			// here
			loccam.readBattery();
			int valorConexao = Integer.parseInt(loccam.readLightLoCCAM());

			if (valorConexao == 1) {

				errorMessage.setText("");
				statusMessage.setTextColor(Color.parseColor("#007EC7"));
				statusMessage.setText("Connection Found.");
				btLogin.setEnabled(true); // reabilitando o botão

			} else {

				statusMessage.setText("");
				statusMessage.setTextColor(Color.RED);
				errorMessage.setText("No Connection Found !"); // inserindo
																// texto de erro
				btLogin.setEnabled(false); // desabilitando o botão

			}

			// loccam.readConnectivityLoCCAM();
			// tvLight.setText("O valor recebido do Contexto - light
			// é:"+loccam.readLightLoCCAM());
			// tvbateria.setText("O valor recebido do Contexto - Conectividade
			// é: "+
			// loccam.readConectividade());
			// OkConnection.setText("valor: "+loccam.readLightLoCCAM());
			// imprimir aqui o valor estático do contexto de conectidade, se
			// está on ou off na hora que a aplicação abre :D
			Log.d("ReadInterest", "Acabou de Ler Interesse");
			loccam.listenConditionRuleExample(true);
			// loccam.listenConditionConectividade(true);
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	class DoLogin extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			//LOGGER LOG ENTRY
			Logger.addEntry("Started Validating");
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Realizando validação. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			// Construindo os parâmetros necessários para que o php
			// consiga fazer a consulta
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", usuario));
			params.add(new BasicNameValuePair("password", senha));
			// pegando a string JSON pela URL
			JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);

			// checando no logcat
			Log.d("Deu Certo? Sucesso? ", json.toString());

			try {

				// verificando se retornou sucesso
				int success = json.getInt("success");

				String sucessString = Integer.toString(success);

				if (success == 1) {
					// usuario encontrado
					Log.d("Certo", sucessString);
					sharedPreferences = getSharedPreferences("GTprefs", Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					// Guarda as informações digitadas caso o usuário tenha
					// optado por isso
					if (rememberCheckbox.isChecked()) {
						String savedUsername = usernameEditText.getText().toString();
						String savedPassword = passwordEditText.getText().toString();
						editor.putString("username", savedUsername);
						editor.putString("password", savedPassword);
						editor.putBoolean("checkbox", true);
					} else {
						// Esquece informações gravadas
						editor.putString("username", "");
						editor.putString("password", "");
						editor.putBoolean("checkbox", false);
					}
					editor.apply();

					// nova intent

					// LOGGER LOG ENTRY
					Logger.addEntry("Login Successful");

					Intent intent = new Intent(LoginActivity.this, CurrentRoomActivity.class);
					// redirecionando
					startActivity(intent);

				} else {
					Log.d("Errado", sucessString);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// LOGGER LOG ENTRY
							Logger.addEntry("Login Failed");
							statusMessage.setText("Login failed.");
							statusMessage.setTextColor(Color.RED);
						}
					});

					// fazer algo se o login não retornar sucesso

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			// "apagando" o dialog depois de efetuar validação

			pDialog.dismiss();

		}

	}

}
