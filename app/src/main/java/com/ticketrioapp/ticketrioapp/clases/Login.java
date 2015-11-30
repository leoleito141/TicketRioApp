package com.ticketrioapp.ticketrioapp.clases;

import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Button;
import android.view.View;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
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

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;

import com.ticketrioapp.ticketrioapp.wrapper.WrapperUsuario;


/**
 * Created by Juanma on 24/10/2015.
 */




public class Login extends AppCompatActivity {

    public static final String MyPREFERENCES = "sesion" ;
    private Button botonRegistro;
    private Button botonLogin;
    private EditText mail;
    private EditText password;

    public static SharedPreferences sharedpreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mail = (EditText) findViewById(R.id.mailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        botonRegistro = (Button) findViewById(R.id.botonRegistro);
        botonLogin = (Button) findViewById(R.id.botonLogin);


        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//                    Intent i = new Intent(Login.this, ListaDeportes.class);
//                    startActivity(i);

                if(prueba()) {

                    try {
                        new WebServiceCall().execute("https://sgem.com/rest/UsuarioService/loginAndroid");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "No deje campos vacios", Toast.LENGTH_LONG).show();
                }

            } // cierra onclick */

        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
            }
        });

    }

    public class WebServiceCall extends AsyncTask<String, Void,  ArrayList<String>> {

        private WrapperUsuario u = new WrapperUsuario(mail.getText().toString(),getPassword(), 1);

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

            HttpClient httpclient = getNewHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response;

            ArrayList<String> respuesta = new ArrayList<String>();

            try {

                String userJson = toJSONString(u);

                httppost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httppost.setHeader("Rol", "VISITANTE");
                httppost.setEntity(new StringEntity(userJson));
                response = httpclient.execute(httppost);

                StatusLine statusLine = response.getStatusLine();
                Log.d("Tag", statusLine.toString());
                statusLine.getStatusCode();
                respuesta.add(0, String.valueOf(statusLine.getStatusCode()));
                respuesta.add(1,EntityUtils.toString(response.getEntity()));

            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }

            return respuesta;
        }


        @Override
        protected void onPostExecute( ArrayList<String> respuesta) {

            String status = respuesta.get(0);
            String datos = respuesta.get(1);

            if(status.equals("200"))
            {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                String[] partes = datos.split("\\.");
                byte[] payloadB64= Base64.decode(partes[1], Base64.DEFAULT);
                String payload = "";
                try {
                    payload = new String(payloadB64, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JsonObject payloadJson= new JsonParser().parse(payload).getAsJsonObject();

                editor.putString("dataUsuario", payloadJson.get("dataUsuario").toString());
                editor.putString("token", datos);
                editor.commit();

                // limpio historial de activities y redirecciono
                Intent i = new Intent(Login.this, ListaDeportes.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }else if(status.equals("404")){
                Toast.makeText(getApplicationContext(), "Credenciales incorrectas.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Error en el servidor.", Toast.LENGTH_LONG).show();
            }
        }

        private String getPassword(){
            return Base64.encodeToString(password.getText().toString().getBytes(), Base64.DEFAULT);
        }

    }

    private boolean prueba(){

        boolean bandera = false;

        if (String.valueOf(this.mail.getText()).equals("")) {

            bandera = false;

        } else if (String.valueOf(this.password.getText()).equals("")) {

            bandera = false;
        } else {
            bandera = true;
        }
        return bandera;

    }

    public String toJSONString(Object object) {	//	Funcion que convierte de objeto java a JSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }



}



