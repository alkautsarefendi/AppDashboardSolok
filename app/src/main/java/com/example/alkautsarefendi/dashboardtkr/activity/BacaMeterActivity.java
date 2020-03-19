package com.example.alkautsarefendi.dashboardtkr.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alkautsarefendi.dashboardtkr.R;
import com.example.alkautsarefendi.dashboardtkr.format.FormatAngka;
import com.example.alkautsarefendi.dashboardtkr.handler.HttpHandler;

import java.util.regex.Pattern;

public class BacaMeterActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = BacaMeterActivity.class.getSimpleName();

    TextView txtPeriodeBM;
    TextView txtTotalPelanggan;
    TextView txtJumlahPembacaan;
    TextView txtPersentase;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca_meter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Baca Meter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodeBM = (TextView)findViewById(R.id.txtPeriodePembacaan);
        txtTotalPelanggan = (TextView)findViewById(R.id.txtTotalPelanggan);
        txtJumlahPembacaan = (TextView)findViewById(R.id.txtJumlahPembacaan);
        txtPersentase = (TextView)findViewById(R.id.txtPersentaseBacaMeter);
        new dataBacaMeter().execute();

        isInternetOn();
    }

    private class dataBacaMeter extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(BacaMeterActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] bacaMeter = null;
            String periodeBM = "";
            String totalPelanggan = "";
            String jumlahPembacaan = "";
            String persentaseBM = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/BacaMeterFirst";

            HttpHandler handler = new HttpHandler();

            String bacaMeterResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +bacaMeterResponse);

            System.out.print(bacaMeterResponse);

            bacaMeter = showData(bacaMeterResponse);

            periodeBM = bacaMeter[0].replace("Periode : ","");
            totalPelanggan = bacaMeter[1].replace("Total Pelanggan : ","");
            jumlahPembacaan = bacaMeter[2].replace("Jumlah Pembacaan : ","");
            persentaseBM = bacaMeter[3].replace("Persentase : ","");


            System.out.print("Periode = " +periodeBM);
            System.out.print("Total Pelanggan = " +totalPelanggan);
            System.out.print("Jumlah Pembacaan = " +jumlahPembacaan);
            System.out.print("Persentase = " +persentaseBM);

            final String finalPeriodeBM = String.valueOf(periodeBM);
            final String finalTotalPelanggan = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPelanggan)));
            final String finalJumlahPembacaan = FormatAngka.Angko(Double.valueOf(String.valueOf(jumlahPembacaan)));
            final String finalPersentaseBM = String.valueOf(persentaseBM);

            BacaMeterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodeBM.setText(String.valueOf(finalPeriodeBM));
                    txtTotalPelanggan.setText(String.valueOf(finalTotalPelanggan));
                    txtJumlahPembacaan.setText(String.valueOf(finalJumlahPembacaan));
                    txtPersentase.setText(String.valueOf(finalPersentaseBM));
                }
            });


            return String.valueOf(bacaMeter);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();

        }
    }

    private String[] showData(String response) {
        //Method Split data
        String[] data = response.split(Pattern.quote("|"));
        return data;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));

    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED){
            Log.e(TAG,"Connected");
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BacaMeterActivity.this);

            alertDialog.setMessage("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BacaMeterActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(intent);
                            finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }
}
