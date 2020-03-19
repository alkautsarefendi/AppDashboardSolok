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

import com.example.alkautsarefendi.dashboardtkr.R;
import com.example.alkautsarefendi.dashboardtkr.format.FormatAngka;
import com.example.alkautsarefendi.dashboardtkr.handler.HttpHandler;

import java.util.regex.Pattern;

public class PemutusanActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView txtPeriodePemutusan, txtTotalSPKPemutusan,
            txtTotalPemutusan, txtTotalApprovalPutus, txtTahunPS, txtTotalPS;

    private String TAG = PemutusanActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemutusan);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pemutusan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodePemutusan = (TextView)findViewById(R.id.txtPeriodePemutusan);
        txtTotalPemutusan = (TextView)findViewById(R.id.txtTotalPemutusan);
        txtTahunPS = (TextView)findViewById(R.id.txtTahubPemutusanS);
        txtTotalPS = (TextView)findViewById(R.id.txtTotalPemutusanS);
        txtTotalSPKPemutusan = (TextView)findViewById(R.id.txtTotalSPKPemutusan);
        txtTotalApprovalPutus = (TextView)findViewById(R.id.txtTotalApprovalPemutusan);

        new dataPemutusan().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataPemutusan extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = ProgressDialog.show(PemutusanActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] pemutusan = null;

            String periodePutus = "";
            String totalSPKPutus = "";
            String totalPasangPutus = "";
            String totalApprovalPutus = "";
            String tahunPS = "";
            String totalPS = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/GantiMeterFirst";

            HttpHandler handler = new HttpHandler();

            String gantiMeterResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +gantiMeterResponse);

            System.out.print(gantiMeterResponse);

            pemutusan = showData(gantiMeterResponse);

            periodePutus = pemutusan[0].replace("Periode : ","");
            totalSPKPutus = pemutusan[1].replace("Total SPK : ","");
            totalPasangPutus = pemutusan[2].replace("Total Pasang : ","");
            totalApprovalPutus = pemutusan[3].replace("Total Approval : ","");
            tahunPS = pemutusan[4].replace("Total Tahunan : ","");
            totalPS = pemutusan[5].replace("Tahun : ","");


            System.out.print("\nPeriode = " +periodePutus);
            System.out.print("\nTotal SPK = " +totalSPKPutus);
            System.out.print("\nTotal Pasang = " +totalPasangPutus);
            System.out.print("\nTotal Approval = " +totalApprovalPutus);
            System.out.print("\nTotal Tahunan = " +tahunPS);
            System.out.print("\nTahun = " +totalPS);

            final String finalPeriodePutus = String.valueOf(periodePutus);
            final String finalTotalSPKPutus = FormatAngka.Angko(Double.valueOf(String.valueOf(totalSPKPutus)));
            final String finalTotalPasangPutus = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPasangPutus)));
            final String finalTotalApprovalPutus = FormatAngka.Angko(Double.valueOf(String.valueOf(totalApprovalPutus)));
            final String finalTahunPS = FormatAngka.Angko(Double.valueOf(String.valueOf(tahunPS)));
            final String finalTotalPS = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPS)));

            PemutusanActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodePemutusan.setText(String.valueOf(finalPeriodePutus));
                    txtTotalSPKPemutusan.setText(String.valueOf(finalTotalSPKPutus));
                    txtTotalPemutusan.setText(String.valueOf(finalTotalPasangPutus));
                    txtTotalApprovalPutus.setText(String.valueOf(finalTotalApprovalPutus));
                    txtTahunPS.setText(String.valueOf(finalTahunPS));
                    txtTotalPS.setText(String.valueOf(finalTotalPS));
                }
            });


            return String.valueOf(pemutusan);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PemutusanActivity.this);

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
                            PemutusanActivity.super.onBackPressed();
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
