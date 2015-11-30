package com.ticketrioapp.ticketrioapp.clases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Base64;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import com.ticketrioapp.ticketrioapp.wrapper.WrapperUsuario;


/**
 * Created by Juanma on 24/10/2015.
 */
public class Registro extends AppCompatActivity {

    private Button botonRegistrarme;
    private EditText mail;
    private EditText password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        mail = (EditText)findViewById(R.id.mailRegistro);
        password = (EditText)findViewById(R.id.passwordRegistro);
        botonRegistrarme = (Button) findViewById(R.id.botonRegistrarme);

        botonRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (prueba()) {

                    //  Toast.makeText(getApplicationContext(),"Antes llamada", Toast.LENGTH_LONG).show();
                    try {

//
                        new WebServiceCall().execute("https://sgem.com/rest/UsuarioService/usuarios");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "onCreate was called" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No deje campos vacios", Toast.LENGTH_LONG).show();
                }

            } // cierra onclick
        });


    }

    public class WebServiceCall extends AsyncTask<String, Void,  Boolean> {

        private WrapperUsuario u = new WrapperUsuario(mail.getText().toString(), getPassword(), 1);
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

            String url = urls[0];
            Boolean guardo = false;


            try {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                String userJson = toJSONString(u);
                HttpClient httpclient = getNewHttpClient();
                HttpPost httppost = new HttpPost(url);

                httppost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httppost.setHeader("Rol", "VISITANTE");
                httppost.setEntity(new StringEntity(userJson));
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
                Toast.makeText(getApplicationContext(), "El usuario se ha guardado con exito", Toast.LENGTH_LONG).show();

                Intent i = new Intent(Registro.this, Login.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Error al guardar usuario.", Toast.LENGTH_LONG).show();
            }
        }

        private String getPassword(){
            String base64 = "";
//            try {
                base64 = Base64.encodeToString(password.getText().toString().getBytes(), Base64.DEFAULT);
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            }
            return base64;
        }

        public WrapperUsuario getU() {
            return u;
        }
        public void setU(WrapperUsuario u) {
            this.u = u;
        }

        public boolean isGuardo() {   return guardo;   }
        public void setGuardo(boolean guardo) { this.guardo = guardo; }

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
