package com.ticketrioapp.ticketrioapp.clases;

import android.support.v7.app.AppCompatActivity;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Button;
import android.widget.AdapterView;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ticketrioapp.ticketrioapp.adapters.DeportesAdapter;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.security.KeyStore;
import java.util.ArrayList;


/**
 * Created by Juanma on 25/10/2015.
 */
public class ListaDeportes extends AppCompatActivity {

    private ListView listaDeportes;
    private ListView listViewDeportes;
    private Context c;
    private ArrayList<String> deportes;
    private String sexo;
    private Button buttonMasculino;
    private Button buttonFemenino;
    private Button botonCerrarSesion;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_deportes);

        c = this;
        buttonFemenino = (Button)findViewById(R.id.bottonFemenino);
        buttonMasculino = (Button)findViewById(R.id.buttonMasculino);
        buttonMasculino.setFocusable(true);
        buttonMasculino.setFocusableInTouchMode(true);///add this line
        buttonMasculino.requestFocus();
        botonCerrarSesion = (Button)findViewById(R.id.buttonLogout);
        this.sexo = "Masculino";

        listViewDeportes = (ListView) findViewById(R.id.listDeportes);

        listaDeportes = (ListView) findViewById(R.id.listDeportes);

        try {
            new WebServiceCall3().execute("https://sgem.com/rest/EventoDeportivoService/listarDeportes/1/Masculino");
            //   new WebServiceCall3().execute(("http://"+ip+":8080/JatrickAppServer/rest/EquipoService/jugadores/" + equipo.toString()).replace(" ", "%20"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        buttonMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexo = "Masculino";

                try {
                    new WebServiceCall3().execute("https://sgem.com/rest/EventoDeportivoService/listarDeportes/1/Masculino");
                    //   new WebServiceCall3().execute(("http://"+ip+":8080/JatrickAppServer/rest/EquipoService/jugadores/" + equipo.toString()).replace(" ", "%20"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            } // cierra onclick
        });


        buttonFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexo = "Femenino";

                try {
                    new WebServiceCall3().execute("https://sgem.com/rest/EventoDeportivoService/listarDeportes/1/Femenino");
                    //   new WebServiceCall3().execute(("http://"+ip+":8080/JatrickAppServer/rest/EquipoService/jugadores/" + equipo.toString()).replace(" ", "%20"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            } // cierra onclick
        });


            listViewDeportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String deporte = deportes.get(pos);

                    Intent intent = new Intent(ListaDeportes.this, ListaDisciplinas.class);
                    intent.putExtra("Deporte", deporte);
                    intent.putExtra("Sexo", sexo);
                    startActivity(intent);
                }

            });



        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
//                finish();

                Intent intent = new Intent(ListaDeportes.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } // cierra onclick
        });

    }






    public class WebServiceCall3 extends AsyncTask<String, Void, ArrayList<String>> {

        public HttpClient getNewHttpClient() {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                return new DefaultHttpClient();
            }
        }


        @Override
        protected ArrayList<String> doInBackground(String... urls) {

            String url = urls[0];

            Log.d("TAGMIEQUIPO", "estoy en asynctask, la url es: " + url);
            ArrayList<String> ld = new ArrayList<String>();


            HttpClient httpclient = getNewHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response;

            get.setHeader(HTTP.CONTENT_TYPE, "application/json");
            get.setHeader("Rol", "VISITANTE");

            try {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                response = httpclient.execute(get);

                JsonParser parser = new JsonParser();
                JsonArray jArray = parser.parse(EntityUtils.toString(response.getEntity())).getAsJsonArray();

                for (JsonElement String : jArray) {
                    String d = gson.fromJson(String, String.class);
                    ld.add(d);

                }


            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
            return ld;
        }

        protected void onPostExecute(ArrayList<String> Deportes) {


            deportes = Deportes;

            DeportesAdapter adapter = new DeportesAdapter(c,deportes);

            if(adapter == null)
            {
                Log.d("ESTOY EN POSTEXECUTE","ES NULL");
            }else{
                Log.d("ESTOY EN POSTEXECUTE","NO ES NULL");
            }

            listaDeportes.setAdapter(adapter);

        }
    }



}




