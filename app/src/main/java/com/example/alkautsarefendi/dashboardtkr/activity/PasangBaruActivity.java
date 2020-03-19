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

public class PasangBaruActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = PasangBaruActivity.class.getSimpleName();

    TextView txtPeriodePasangBaru;
    TextView txtJumlahPermintaan;
    TextView txtJumlahTerpasang;
    TextView txtJumlahApproval;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasang_baru);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pasang Baru");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodePasangBaru = (TextView)findViewById(R.id.txtPeriodePasangBaru);
        txtJumlahPermintaan = (TextView)findViewById(R.id.txtJumlahPermintaan);
        txtJumlahTerpasang = (TextView)findViewById(R.id.txtJumlahTerpasang);
        txtJumlahApproval = (TextView)findViewById(R.id.txtJumlahApproval);

        new dataPasangBaru().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataPasangBaru extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(PasangBaruActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] pasangBaru = null;
            String periodePasangBaru = "";
            String jumlahPermintaan = "";
            String jumlahTerpasang="";
            String jumlahApproval = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenerimaanPasangBaruFirst";

            HttpHandler handler = new HttpHandler();

            String PasangBaruResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +PasangBaruResponse);

            System.out.print(PasangBaruResponse);

            pasangBaru = showData(PasangBaruResponse);

            periodePasangBaru = pasangBaru[0].replace("Periode : ","");
            jumlahPermintaan = pasangBaru[1].replace("Jumlah Permintaan : ","");
            jumlahTerpasang = pasangBaru[2].replace("Jumlah Terpasang : ","");
            jumlahApproval = pasangBaru[3].replace("Jumlah Aktif : ","");

            System.out.print("Periode = " +periodePasangBaru);
            System.out.print("Jumlah Permintaan = " +jumlahPermintaan);
            System.out.print("Jumlah Terpasang = " +jumlahTerpasang);
            System.out.print("Jumlah Aktif = " +jumlahApproval);

            final String finalPeriodePSB = String.valueOf(periodePasangBaru);
            final String finalJumlahPermintaan = String.valueOf(jumlahPermintaan);
            final String finalJumlahTerpasang = String.valueOf(jumlahTerpasang);
            final String finalJumlahAproval = String.valueOf(jumlahApproval);

            PasangBaruActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodePasangBaru.setText(String.valueOf(finalPeriodePSB));
                    txtJumlahPermintaan.setText(String.valueOf(finalJumlahPermintaan));
                    txtJumlahTerpasang.setText(String.valueOf(finalJumlahTerpasang));
                    txtJumlahApproval.setText(String.valueOf(finalJumlahAproval));
                }
            });


            return String.valueOf(pasangBaru);
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

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED){
            Log.e(TAG,"Connected");
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasangBaruActivity.this);

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
                            PasangBaruActivity.super.onBackPressed();
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
