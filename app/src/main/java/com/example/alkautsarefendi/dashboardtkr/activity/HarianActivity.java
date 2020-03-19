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

public class HarianActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = HarianActivity.class.getSimpleName();

    TextView txtPeriodeHarian;
    TextView txtTotalLembar;
    TextView txtTotalBiayaAir;
    TextView txtTotalDenda;
    TextView txtTotalMaterai;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harian);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Harian");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodeHarian = (TextView)findViewById(R.id.txtPeriodeHarian);
        txtTotalLembar = (TextView)findViewById(R.id.txtTotalLembar);
        txtTotalBiayaAir = (TextView)findViewById(R.id.txtTotalBiayaAir);
        txtTotalDenda = (TextView)findViewById(R.id.txtTotalDenda);
        txtTotalMaterai = (TextView)findViewById(R.id.txtTotalMatrai);

        new dataHarian().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataHarian extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(HarianActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] harian = null;
            String periodeHarian = "";
            String totalLembar = "";
            String totalBiayaAir = "";
            String TotalDenda = "";
            String TotalMaterai = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenerimaanHarianFirst";

            HttpHandler handler = new HttpHandler();

            String harianResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +harianResponse);

            System.out.print(harianResponse);

            harian = showData(harianResponse);

            totalLembar = harian[0].replace("Lembar : ","");
            totalBiayaAir = harian[1].replace("Biaya Air : ","");
            TotalDenda = harian[2].replace("Denda : ","");
            TotalMaterai = harian[3].replace("Materai : ","");
            periodeHarian = harian[4].replace("Periode : ","");


            System.out.print("Lembar Rek = " +totalLembar);
            System.out.print("Biaya Air = " +totalBiayaAir);
            System.out.print("Denda = " +TotalDenda);
            System.out.print("Materai = " +TotalMaterai);
            System.out.print("Periode = " +periodeHarian);

            final String finalTotalLembar = FormatAngka.Angko(Double.valueOf(String.valueOf(totalLembar)));
            final String finalTotalBiayaAir = FormatAngka.Angko(Double.valueOf(String.valueOf(totalBiayaAir)));
            final String finalTotalDenda = FormatAngka.Angko(Double.valueOf(String.valueOf(TotalDenda)));
            final String finalTotalMaterai= FormatAngka.Angko(Double.valueOf(String.valueOf(TotalMaterai)));
            final String finalPeriodeHarian = String.valueOf(periodeHarian);

            HarianActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodeHarian.setText(String.valueOf(finalPeriodeHarian));
                    txtTotalLembar.setText(String.valueOf(finalTotalLembar));
                    txtTotalBiayaAir.setText(String.valueOf(finalTotalBiayaAir));
                    txtTotalDenda.setText(String.valueOf(finalTotalDenda));
                    txtTotalMaterai.setText(String.valueOf(finalTotalMaterai));
                }
            });


            return String.valueOf(harian);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HarianActivity.this);

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
                            HarianActivity.super.onBackPressed();
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
