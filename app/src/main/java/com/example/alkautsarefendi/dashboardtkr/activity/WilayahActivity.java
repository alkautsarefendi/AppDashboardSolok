package com.example.alkautsarefendi.dashboardtkr.activity;

import android.annotation.SuppressLint;
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

public class WilayahActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = WilayahActivity.class.getSimpleName();

    TextView txtPeriodeWilayah;
    TextView txtLembarRekWilayah;
    TextView txtNominalRekWilayah;
    TextView txtLembarRekTunggakan;
    TextView txtNominalsdHariini;
    TextView txtPersentase;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilayah);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Wilayah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodeWilayah = (TextView)findViewById(R.id.txtPeriodeWilayah);
        txtLembarRekWilayah = (TextView)findViewById(R.id.txtLembarRekeningWilayah);
        txtNominalRekWilayah = (TextView)findViewById(R.id.txtNominalRekeningWilayah);
        txtLembarRekTunggakan = (TextView)findViewById(R.id.txtLembarRekeningTunggakan);
        txtNominalsdHariini = (TextView)findViewById(R.id.txtRekeningAirHariIniwlyh);
        txtPersentase = (TextView)findViewById(R.id.txtPersentaseWilayah);

        new dataWilayah().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));

    }

    private class dataWilayah extends AsyncTask<String,String ,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(WilayahActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] penerimaanWilayah = null;
            String periodeWilayah = "";
            String lembarRekeningWilayah = "";
            String nominalRekeningWilayah = "";
            String lembarRekTunggakan = "";
            String nominalsdHariini = "";
            String persentaseWilayah = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenerimaanWilayahFirst";

            HttpHandler handler = new HttpHandler();

            String kudo = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " +kudo);

            System.out.print(kudo);

            penerimaanWilayah = showData(kudo);

            periodeWilayah = penerimaanWilayah[0].replace("Periode Pembayaran : ","");
            lembarRekeningWilayah = penerimaanWilayah[1].replace("Lembar Rekening : ","");
            nominalRekeningWilayah = penerimaanWilayah[2].replace("Nominal Rek : ","");
            lembarRekTunggakan = penerimaanWilayah[3].replace("lembar Rekening dan tunggakan : ","");
            nominalsdHariini = penerimaanWilayah[4].replace("Nominal Sd hari ini : ","");
            persentaseWilayah = penerimaanWilayah[5].replace("Persentase : ","");

            System.out.print("Periode = " +periodeWilayah);
            System.out.print("Lembar Rek = " +lembarRekeningWilayah);
            System.out.print("Nominal Rek = " +nominalRekeningWilayah);
            System.out.print("Lembar Rekening dan tunggakan = " +lembarRekTunggakan);
            System.out.print("Nominal Sd hari ini = " +nominalsdHariini);
            System.out.print("Persentase = " +persentaseWilayah);

            final String finalPeriodeWilayah = periodeWilayah;
            final String finalLembarRekWilayah = FormatAngka.Angko(Double.valueOf(String.valueOf(lembarRekeningWilayah)));
            final String finalNominalRekWilayah = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(nominalRekeningWilayah)));
            final String finalLembarRekTunggakan = FormatAngka.Angko(Double.valueOf(String.valueOf(lembarRekTunggakan)));
            final String finalNominalsdHariini = FormatAngka.Angko(Double.valueOf(String.valueOf(nominalsdHariini)));
            final String finalPersentaseWilayah = FormatAngka.Angko(Double.valueOf((persentaseWilayah)));

            WilayahActivity.this.runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    txtPeriodeWilayah.setText(String.valueOf(finalPeriodeWilayah));
                    txtLembarRekWilayah.setText(String.valueOf(finalLembarRekWilayah));
                    txtNominalRekWilayah.setText(String.valueOf(finalNominalRekWilayah));
                    txtLembarRekTunggakan.setText(String.valueOf(finalLembarRekTunggakan));
                    txtNominalsdHariini.setText(String.valueOf(finalNominalsdHariini));
                    txtPersentase.setText(String.valueOf(finalPersentaseWilayah)+"%");
                }
            });



            return String.valueOf(penerimaanWilayah);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(WilayahActivity.this);

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
                            WilayahActivity.super.onBackPressed();
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
