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
import com.example.alkautsarefendi.dashboardtkr.format.FormatRupiah;
import com.example.alkautsarefendi.dashboardtkr.handler.HttpHandler;

import java.util.regex.Pattern;

public class LoketActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = LoketActivity.class.getSimpleName();

    TextView txtPeriode;
    TextView txtLembarRek;
    TextView txtNominalRek;
    TextView txtTgl;
    TextView txtLembarRekHariIni;
    TextView txtRekAirHariIni;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loket);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Loket");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriode = (TextView)findViewById(R.id.txtPeriodePembayaran);
        txtLembarRek = (TextView)findViewById(R.id.txtLembarRekening);
        txtNominalRek = (TextView)findViewById(R.id.txtNominalRekening);
        txtTgl = (TextView)findViewById(R.id.txtTglHariIni);
        txtLembarRekHariIni = (TextView)findViewById(R.id.txtLembarRekeningHariIni);
        txtRekAirHariIni = (TextView)findViewById(R.id.txtRekeningAirHariIni);

        new dataLoket().execute();
        isInternetOn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataLoket extends AsyncTask<String ,String ,String > {



        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(LoketActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] penerimaanLoket = null;
            String periode = "";
            String lembarRekening = "";
            String nominalRekening = "";
            String tgl = "";
            String lembarRekeningHariIni = "";
            String rekAirHariIni = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenerimaanLoket";

            HttpHandler handler = new HttpHandler();

            String kudo = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +kudo);

            System.out.print(kudo);

            penerimaanLoket = showData(kudo);

            periode = penerimaanLoket[0].replace("Periode Pembayaran : ","");
            lembarRekening = penerimaanLoket[1].replace("Lembar Rekening : ","");
            nominalRekening = penerimaanLoket[2].replace("Nominal Rek : ","");
            lembarRekeningHariIni = penerimaanLoket[3].replace("Lembar Tunggakan Sampai Hari ini : ","");
            rekAirHariIni = penerimaanLoket[4].replace("Rekening Air s.d Hari ini : ","");
            tgl = penerimaanLoket[5].replace("Tanggal Hari Ini : ","");

            System.out.print("Periode = " +periode);
            System.out.print("Lembar Rek = " +lembarRekening);
            System.out.print("Nominal Rek = " +nominalRekening);
            System.out.print("Tanggal hari ini = " +tgl);
            System.out.print("Lembar Rek SD = " +lembarRekeningHariIni);
            System.out.print("Rek Air SD = " +rekAirHariIni);

            final String finalPeriode = periode;
            final String finalLembarRek = FormatAngka.Angko(Double.valueOf(String.valueOf(lembarRekening)));
            final String finalNominalRek = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(nominalRekening)));
            final String finalTgl = tgl;
            final String finalLembarRekSD = FormatAngka.Angko(Double.valueOf(String.valueOf(lembarRekeningHariIni)));
            final String finalRekAirSD = FormatRupiah.Rupiah(Double.valueOf((rekAirHariIni)));

            LoketActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPeriode.setText(String.valueOf(finalPeriode));
                    txtLembarRek.setText(String.valueOf(finalLembarRek));
                    txtNominalRek.setText(String.valueOf(finalNominalRek));
                    txtTgl.setText(String.valueOf(finalTgl));
                    txtLembarRekHariIni.setText(String.valueOf(finalLembarRekSD));
                    txtRekAirHariIni.setText(String.valueOf(finalRekAirSD));
                }
            });


            return String.valueOf(penerimaanLoket);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoketActivity.this);

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
                            LoketActivity.super.onBackPressed();
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
