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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticketrioapp.ticketrioapp.adapters.CompetenciaAdapter;
import com.ticketrioapp.ticketrioapp.wrapper.WrapperCompetencia;
import com.ticketrioapp.ticketrioapp.wrapper.WrapperCompraEntrada;
import com.ticketrioapp.ticketrioapp.wrapper.WrapperUsuario;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Juanma on 03/11/2015.
 */
public class CompraEntrada extends AppCompatActivity {

    private Context c;
    private Button botonComprar;
    private Button botonCerrarSesion;
    protected ImageView imageView;
    private Spinner mspin;
    private String deporte;
    private String sexo;
    private String disciplina;
    private Integer ronda;
    private Integer idCompetencia;
    private Integer cantEntradas;
    private Integer entradasVendidas;
    private float precio;
    private Long fecha;
    private Integer tenantID;
    private String estadio;
    private String item;


    public static SharedPreferences sharedpreferences;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprar_entrada);


        botonCerrarSesion = (Button)findViewById(R.id.buttonLogout);

        deporte = getIntent().getStringExtra("Deporte");
        sexo = getIntent().getStringExtra("Sexo");
        disciplina = getIntent().getStringExtra("Disciplina");
        ronda = getIntent().getIntExtra("Ronda", 0);

        idCompetencia  = getIntent().getIntExtra("idCompetencia", 0);
        cantEntradas  = getIntent().getIntExtra("cantEntradas", 0);
        entradasVendidas  = getIntent().getIntExtra("entradasVendidas", 0);
        precio  = getIntent().getFloatExtra("precio", 0);
        fecha = getIntent().getExtras().getLong("fecha");
        tenantID  = getIntent().getIntExtra("tenantID", 0);
        estadio = getIntent().getStringExtra("estadio");

        int entradasDisponibles = cantEntradas - entradasVendidas;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        Date fechaconvertida = new Date(fecha);
        String fechaFinal = df.format(fechaconvertida);

        sharedpreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);

        c = this;

        TextView nombreDep = (TextView) findViewById(R.id.ce_Deporte);
        TextView nombreDeiscip = (TextView) findViewById(R.id.ce_Disciplina);
        TextView sexovista = (TextView) findViewById(R.id.ce_Sexo);
        TextView rondavista = (TextView) findViewById(R.id.ce_Ronda);
        TextView entradasDisponiblresVista = (TextView) findViewById(R.id.ce_EntradasDisponibles);
        TextView precioEntrada = (TextView) findViewById(R.id.ce_Precio);
        TextView fechaVista = (TextView) findViewById(R.id.ce_Fecha);
        TextView estadioVista = (TextView) findViewById(R.id.ce_Estadio);
        imageView=(ImageView)findViewById(R.id.imageViewCompra);
        botonComprar = (Button) findViewById(R.id.botonComprar);


        nombreDep.setText(deporte);
        nombreDeiscip.setText(disciplina);
        sexovista.setText(sexo);
        rondavista.setText(String.valueOf(ronda));
        entradasDisponiblresVista.setText(String.valueOf(entradasDisponibles));
        precioEntrada.setText(String.valueOf(precio));
        fechaVista.setText(fechaFinal);
        estadioVista.setText(estadio);

        mspin=(Spinner) findViewById(R.id.spinner);
        Integer[] items = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        mspin.setAdapter(adapter);



        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {

                        item = mspin.getSelectedItem().toString();
                        new WebServiceCall().execute("https://sgem.com/rest/CompetenciaService/comprarEntradas");

                        String usuariosesion = sharedpreferences.getString("dataUsuario", "");
                        JsonObject dataUsuario = new JsonParser().parse(usuariosesion).getAsJsonObject();
                        String emailusuario = dataUsuario.get("email").getAsString();

                        Integer precioTotal = (Math.round(precio));

                        Integer precioCompra = precioTotal*Integer.parseInt(item);




                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
                        Date fechatransformada = new Date(fecha);
                        String fechaComp = df.format(fechatransformada);

                        String deporteqr = "Deporte:"+deporte+"\n";
                        String disciplinaqr = "Disciplina:"+disciplina+"\n";
                        String sexoqr = "Sexo:"+sexo+"\n";
                        String rondaqr = "Ronda:"+ronda.toString()+"\n";
                        String precioqr = "Precio Total:US$"+precioCompra.toString()+"\n";
                        String fechaqr = "Fecha:"+fechaComp+"\n";
                        String estadioqr = "Estadio:"+estadio+"\n";
                        String cantEntradasqr = "Entradas Copradas:"+item+"\n";
                        String usaurioqr = "Usuario comprador:"+emailusuario+"\n";


                        String Datos= usaurioqr+deporteqr+disciplinaqr+sexoqr+rondaqr+fechaqr+estadioqr+cantEntradasqr+precioqr;


                        new DownloadQR(imageView).execute(Datos);






                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
            } // cierra onclick
        });


        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(CompraEntrada.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } // cierra onclick
        });


    }


    public class WebServiceCall extends AsyncTask<String, Void,  Boolean> {

        private WrapperCompraEntrada wce;

        private boolean guardo = false;

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
        protected Boolean doInBackground(String... urls) {
            String usuario = sharedpreferences.getString("dataUsuario", "");
            JsonObject dataUsuario = new JsonParser().parse(usuario).getAsJsonObject();
            String email = dataUsuario.get("email").getAsString();

            String jwt = sharedpreferences.getString("token", "");
            JsonObject tokenJson = new JsonParser().parse(jwt).getAsJsonObject();
            String token = tokenJson.get("token").getAsString();

            wce = new WrapperCompraEntrada(tenantID, idCompetencia, Integer.parseInt(item), email);
            String url = urls[0];
            Boolean guardo = false;


            try {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                String compraJson = toJSONString(wce);
                HttpClient httpclient = getNewHttpClient();
                HttpPost httppost = new HttpPost(url);




                httppost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httppost.setHeader("Rol", "USUARIO_COMUN");
                httppost.setHeader("Authorization", token);
                httppost.setEntity(new StringEntity(compraJson));
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity entidad = response.getEntity();

                if(entidad.getContentLength() != 0) {
                    StringBuilder sb = new StringBuilder();
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(entidad.getContent()), 65728);
                        String line = null;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        guardo = Boolean.parseBoolean(sb.toString());
                    }
                    catch (IOException e) { e.printStackTrace(); }
                    catch (Exception e) { e.printStackTrace(); }
                }

            } catch (Exception e) {
                Log.d("Exception",e.getMessage());
                return false;
            }
            return guardo;
        }

        @Override
        protected void onPostExecute( Boolean guardo) {
            if(guardo){
                Toast.makeText(getApplicationContext(), "La Compra se realizado con exito", Toast.LENGTH_LONG).show();

                Intent i = new Intent(CompraEntrada.this, ListaDeportes.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Error al realizar la compra", Toast.LENGTH_LONG).show();
            }
        }

    }


    public String toJSONString(Object object) {	//	Funcion que convierte de objeto java a JSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }



}



