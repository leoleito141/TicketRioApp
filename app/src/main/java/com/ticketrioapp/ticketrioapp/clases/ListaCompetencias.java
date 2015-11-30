package com.ticketrioapp.ticketrioapp.clases;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticketrioapp.ticketrioapp.adapters.CompetenciaAdapter;
import com.ticketrioapp.ticketrioapp.wrapper.WrapperCompetencia;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juanma on 01/11/2015.
 */
public class ListaCompetencias extends AppCompatActivity {

    private Context c;
    private ArrayList<WrapperCompetencia> competencias;
    private ListView listaCompetencias;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_competencia);
        listaCompetencias = (ListView) findViewById(R.id.listCompetencias);

        final String deporte = getIntent().getStringExtra("Deporte");
        final String sexo = getIntent().getStringExtra("Sexo");
        final String deciplina = getIntent().getStringExtra("Disciplina");
        final Integer ronda = getIntent().getIntExtra("Ronda",0);
        c = this;


        try {
            new WebServiceCall().execute(("https://sgem.com/rest/CompetenciaService/listarCompetenciasPorRonda/1/"+sexo+"/"+deporte+"/"+deciplina.toString()).replace(" ", "%20")+"/"+ronda);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        listaCompetencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                WrapperCompetencia comp = competencias.get(pos);

                Intent intent = new Intent(ListaCompetencias.this, CompraEntrada.class);
                intent.putExtra("Deporte", deporte);
                intent.putExtra("Disciplina", deciplina);
                intent.putExtra("Sexo", sexo);
                intent.putExtra("Ronda", ronda);
                intent.putExtra("idCompetencia",comp.getCompetenciaId());
                intent.putExtra("cantEntradas",comp.getCantEntradas());
                intent.putExtra("entradasVendidas",comp.getEntradasVendidas());
                intent.putExtra("precio",comp.getPrecioEntrada());
                Date d = comp.getFecha();
                long newFecha = d.getTime();

                intent.putExtra("fecha",newFecha);
               // intent.putExtra("fecha",comp.getFecha());
                intent.putExtra("estadio",comp.getEstadio());
                intent.putExtra("tenantID",comp.getTenantID());
                startActivity(intent);
            }

        });



    }

    public class WebServiceCall extends AsyncTask<String, Void, ArrayList<WrapperCompetencia>> {

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
        protected ArrayList<WrapperCompetencia> doInBackground(String... urls) {

            String url = urls[0];

            Log.d("TAGMIEQUIPO", "estoy en asynctask, la url es: " + url);
            ArrayList<WrapperCompetencia> lcompetencia = new ArrayList<WrapperCompetencia>();


            HttpClient httpclient = getNewHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response;

            get.setHeader(HTTP.CONTENT_TYPE, "application/json");
            get.setHeader("Rol", "VISITANTE");

            try {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = gsonBuilder.create();

                response = httpclient.execute(get);

                JsonParser parser = new JsonParser();
                JsonArray jArray = parser.parse(EntityUtils.toString(response.getEntity())).getAsJsonArray();



                for(int i = 0; i< jArray.size(); i++){

                    WrapperCompetencia wc = new WrapperCompetencia();
                    JsonElement competencia = jArray.get(i);

                    JsonObject comp =competencia.getAsJsonObject();

                    wc.setCantEntradas(comp.get("cantEntradas").getAsInt());
                    wc.setEntradasVendidas(comp.get("entradasVendidas").getAsInt());
                    wc.setCompetenciaId(comp.get("idCompetencia").getAsInt());
                    wc.setEstadio(comp.get("estadio").getAsString());
                    wc.setPrecioEntrada(comp.get("precioEntrada").getAsFloat());
                    wc.setTenantID(comp.get("tenantId").getAsInt());





                    String longV = comp.get("fecha").getAsString();
                    long millisecond = Long.parseLong(longV);
              //      String dateString= DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
                    Date d = new Date(millisecond);

                    wc.setFecha(d);




                    lcompetencia.add(wc);
                }



            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
            return lcompetencia;
        }

        protected void onPostExecute(ArrayList<WrapperCompetencia> Competencias) {

            competencias = Competencias;

            CompetenciaAdapter adapter = new CompetenciaAdapter(c,competencias);

            listaCompetencias.setAdapter(adapter);

        }

    }


}
