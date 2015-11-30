package com.ticketrioapp.ticketrioapp.clases;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    public static final int segundos = 8;//8 va lento, 2 va rapido
    public static final int milisegundos = segundos*1000;
    public static final int delay = 2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        empezaranimacion();
        progressBar.setMax(maxProgreso());
    }

    public void empezaranimacion(){

        new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                progressBar.setProgress(definirProgreso(millisUntilFinished));

            }

            @Override
            public void onFinish() {

                Intent nuevo = new Intent(MainActivity.this,Login.class);
                startActivity(nuevo);
                finish();
            }
        }.start();
    }


    public int definirProgreso(long miliseconds){

        return (int)((milisegundos-miliseconds)/1000);

    }


    public int maxProgreso(){

        return segundos-delay;

    }
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
