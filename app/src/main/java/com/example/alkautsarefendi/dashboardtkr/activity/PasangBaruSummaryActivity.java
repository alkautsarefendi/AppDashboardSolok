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

public class PasangBaruSummaryActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = PasangBaruSummaryActivity.class.getSimpleName();

    TextView txtPeriodePBS;
    TextView txtTotalPelangganPBS;
    TextView txtTotalPembayaranPBS;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasang_baru_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pasang Baru Summary");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodePBS = (TextView)findViewById(R.id.txtPeriodePasangBaruSummary);
        txtTotalPelangganPBS = (TextView)findViewById(R.id.txtTotalPelangganS);
        txtTotalPembayaranPBS = (TextView)findViewById(R.id.txtTotalPembayaranS);

        new dataPBS().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataPBS extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(PasangBaruSummaryActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] pbs = null;
            String periodePBS = "";
            String totalPelangganPBS = "";
            String totalPembayaranPBS="";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenerimaanPembayaranPasangBaru";

            HttpHandler handler = new HttpHandler();

            String pbsResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +pbsResponse);

            System.out.print(pbsResponse);

            pbs = showData(pbsResponse);

            periodePBS = pbs[0].replace("Periode : ","");
            totalPelangganPBS = pbs[1].replace("Total Pelanggan : ","");
            totalPembayaranPBS = pbs[2].replace("Total Pembayaran : ","");

            System.out.print("Periode = " +periodePBS);
            System.out.print("Total Pelanggan = " +totalPelangganPBS);
            System.out.print("Total Pembayaran = " +totalPembayaranPBS);

            final String finalPeriodePBS = String.valueOf(periodePBS);
            final String finalTotalPelanggan = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPelangganPBS)));
            final String finalTotalPembayaran = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPembayaranPBS)));

            PasangBaruSummaryActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodePBS.setText(String.valueOf(finalPeriodePBS));
                    txtTotalPelangganPBS.setText(String.valueOf(finalTotalPelanggan));
                    txtTotalPembayaranPBS.setText(String.valueOf(finalTotalPembayaran));
                }
            });


            return String.valueOf(pbs);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasangBaruSummaryActivity.this);

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
                            PasangBaruSummaryActivity.super.onBackPressed();
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
