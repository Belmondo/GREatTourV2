package br.ufc.great.returnpersons;


import br.ufc.great.greattour.R;
import br.ufc.great.greattour.utils.logger.Logger;
import br.ufc.great.returnpersons.CustomListAdapter;
import br.ufc.great.returnpersons.AppController;
import br.ufc.great.returnpersons.Pesquisador;



import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;


 /**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */
public class ReturnPersonActivity extends Activity {
    // Log tag
    private static final String TAG = ReturnPersonActivity.class.getSimpleName();

    // Researchers json url
    private static final String url = "http://pesquisa.great.ufc.br/greattourv2/return_persons.php?id_environment=";
    private static String finalurl;
    private ProgressDialog pDialog;
    private List<Pesquisador> listaPesquisador = new ArrayList<Pesquisador>();

    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_return_persons);

        // LOGGER LOG ENTRY
		Logger.addEntry("Displaying Persons");

        Intent takeCurrentValue = getIntent();
		String currentRoomCode = takeCurrentValue.getStringExtra("currentRoomCode");
		//url.concat(currentRoomCode);
		//url.concat(type);

		finalurl = url+currentRoomCode;

		Toast.makeText(ReturnPersonActivity.this, finalurl, Toast.LENGTH_LONG).show();
        //recebendo o valor que vem da intent anterior
       // String empId = getIntent().getStringExtra("coLocal");
        //imprimindo no log, para confirmar o valor
        //Log.d("Passou o Valor", empId);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, listaPesquisador);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String linkVideoOnClicable = ((TextView) view.findViewById(R.id.lattes)).getText().toString();

				//Toast.makeText(MainActivity.this,linkVideoOnClicable , Toast.LENGTH_LONG).show();
				Uri uri = Uri.parse(linkVideoOnClicable);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);



			}
		});


        pDialog = new ProgressDialog(this);
        //mostrando o progressDialog antes de fazer a requisição http

        pDialog.setMessage("Loading...");
        pDialog.show();

        // Criando uma requisição ao obj pelo volley
        JsonArrayRequest movieReq = new JsonArrayRequest(finalurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // convertendo JSONObject
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);


                                Pesquisador pesquisador = new Pesquisador();


                                pesquisador.setNome(obj.getString("name"));

                                pesquisador.setLattes(obj.getString("lattes"));


                                pesquisador.setFotoUrl("http://pesquisa.great.ufc.br/greattourv2/".concat(obj.getString("photo")));

                                pesquisador.setAbout_en(obj.getString("about_en"));
                                pesquisador.setExpertise_Area(obj.getString("area_en"));

                                listaPesquisador.add(pesquisador);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();

                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
