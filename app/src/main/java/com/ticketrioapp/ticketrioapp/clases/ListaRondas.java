package com.ticketrioapp.ticketrioapp.clases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ticketrioapp.ticketrioapp.adapters.DisciplinaAdapter;
import com.ticketrioapp.ticketrioapp.adapters.RondaAdapter;

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
 * Created by Juanma on 01/11/2015.
 */
public class ListaRondas extends AppCompatActivity {

        private Context c;
        private ArrayList<Integer> rondas;
        private ListView listaRondas;
        private Button botonCerrarSesion;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rondas);
        listaRondas = (ListView) findViewById(R.id.listRondas);
        botonCerrarSesion = (Button)findViewById(R.id.buttonLogout);

        final String deporte = getIntent().getStringExtra("Deporte");
        final String sexo = getIntent().getStringExtra("Sexo");
        final String deciplina = getIntent().getStringExtra("Disciplina");
        c = this;


        try {
            new WebServiceCall().execute(("https://sgem.com/rest/EventoDeportivoService/listarRondas/1/"+sexo+"/"+deporte+"/"+deciplina.toString()).replace(" ", "%20"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        listaRondas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Integer ronda = rondas.get(pos);

                Intent intent = new Intent(ListaRondas.this, ListaCompetencias.class);
                intent.putExtra("Deporte", deporte);
                intent.putExtra("Disciplina", deciplina);
                intent.putExtra("Sexo", sexo);
                intent.putExtra("Ronda",(int) ronda);
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
                Intent intent = new Intent(ListaRondas.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } // cierra onclick
        });


    }

    public class WebServiceCall extends AsyncTask<String, Void, ArrayList<Integer>> {

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
        protected ArrayList<Integer> doInBackground(String... urls) {

            String url = urls[0];

            Log.d("TAGMIEQUIPO", "estoy en asynctask, la url es: " + url);
            ArrayList<Integer> lronda = new ArrayList<Integer>();


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

                for (JsonElement Integer : jArray) {
                    Integer r = gson.fromJson(Integer, Integer.class);
                    lronda.add(r);

                }


            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
            return lronda;
        }

        protected void onPostExecute(ArrayList<Integer> Rondas) {


            rondas = Rondas;

            RondaAdapter adapter = new RondaAdapter(c,rondas);

            if(adapter == null)
            {
                Log.d("ESTOY EN POSTEXECUTE","ES NULL");
            }else{
                Log.d("ESTOY EN POSTEXECUTE","NO ES NULL");
            }

            listaRondas.setAdapter(adapter);

        }
    }

}
