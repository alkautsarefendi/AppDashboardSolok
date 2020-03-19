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

public class GantiMeterActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = GantiMeterActivity.class.getSimpleName();

    TextView txtPeriodeGM;
    TextView txtTotalSPKGM;
    TextView txtTotalPasangGM;
    TextView txtTotalApproval;
    TextView txtTahunGMS;
    TextView txtTotalGMS;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_meter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ganti Meter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodeGM = (TextView)findViewById(R.id.txtPeriodeGM);
        txtTotalSPKGM = (TextView)findViewById(R.id.txtTotalSPKGM);
        txtTotalPasangGM = (TextView)findViewById(R.id.txtTotalPasangGM);
        txtTotalApproval = (TextView)findViewById(R.id.txtTotalApproval);
        txtTahunGMS = (TextView)findViewById(R.id.txtTahunGMS);
        txtTotalGMS = (TextView)findViewById(R.id.txtTotalGMS);

        new dataGantiMeter().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataGantiMeter extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = ProgressDialog.show(GantiMeterActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] gantiMeter = null;

            String periodeGM = "";
            String totalSPKGM = "";
            String totalPasangGM = "";
            String totalApproval = "";
            String tahunGMS = "";
            String totalGMS = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/GantiMeterFirst";

            HttpHandler handler = new HttpHandler();

            String gantiMeterResponse = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +gantiMeterResponse);

            System.out.print(gantiMeterResponse);

            gantiMeter = showData(gantiMeterResponse);

            periodeGM = gantiMeter[0].replace("Periode : ","");
            totalSPKGM = gantiMeter[1].replace("Total SPK : ","");
            totalPasangGM = gantiMeter[2].replace("Total Pasang : ","");
            totalApproval = gantiMeter[3].replace("Total Approval : ","");
            tahunGMS = gantiMeter[4].replace("Total Tahunan : ","");
            totalGMS = gantiMeter[5].replace("Tahun : ","");


            System.out.print("\nPeriode = " +periodeGM);
            System.out.print("\nTotal SPK = " +totalSPKGM);
            System.out.print("\nTotal Pasang = " +totalPasangGM);
            System.out.print("\nTotal Approval = " +totalApproval);
            System.out.print("\nTotal Tahunan = " +tahunGMS);
            System.out.print("\nTahun = " +totalGMS);

            final String finalPeriodeGM = String.valueOf(periodeGM);
            final String finalTotalSPKGM = FormatAngka.Angko(Double.valueOf(String.valueOf(totalSPKGM)));
            final String finalTotalPasangGM = FormatAngka.Angko(Double.valueOf(String.valueOf(totalPasangGM)));
            final String finalTotalApproval = FormatAngka.Angko(Double.valueOf(String.valueOf(totalApproval)));
            final String finalTahunGMS = FormatAngka.Angko(Double.valueOf(String.valueOf(tahunGMS)));
            final String finalTotalGMS = FormatAngka.Angko(Double.valueOf(String.valueOf(totalGMS)));

            GantiMeterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriodeGM.setText(String.valueOf(finalPeriodeGM));
                    txtTotalSPKGM.setText(String.valueOf(finalTotalSPKGM));
                    txtTotalPasangGM.setText(String.valueOf(finalTotalPasangGM));
                    txtTotalApproval.setText(String.valueOf(finalTotalApproval));
                    txtTahunGMS.setText(String.valueOf(finalTahunGMS));
                    txtTotalGMS.setText(String.valueOf(finalTotalGMS));
                }
            });


            return String.valueOf(gantiMeter);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GantiMeterActivity.this);

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
                            GantiMeterActivity.super.onBackPressed();
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
