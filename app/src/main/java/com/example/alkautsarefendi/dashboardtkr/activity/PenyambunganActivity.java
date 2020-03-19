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
import com.example.alkautsarefendi.dashboardtkr.format.FormatRupiah;
import com.example.alkautsarefendi.dashboardtkr.handler.HttpHandler;

import java.util.regex.Pattern;

public class PenyambunganActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView txtPeriodePenyambungan, txtJumlahPermintaanP, txtJumlahTerpasangP, txtJumlahApprovalP,
            txtPeriodePembayaranPS, txtTotalPelangganPS, txtTotalPembayaranPS;

    private String TAG = PenyambunganActivity.class.getSimpleName();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyambungan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Penyambungan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriodePenyambungan = (TextView)findViewById(R.id.txtPeriodePenyambunganKembali);
        txtJumlahPermintaanP = (TextView)findViewById(R.id.txtJumlahPermintaanPenyambunganKembali);
        txtJumlahTerpasangP = (TextView)findViewById(R.id.txtJumlahTerpasangPenyambunganKembali);
        txtJumlahApprovalP = (TextView)findViewById(R.id.txtJumlahApprovalPenyambunganKembali);
        txtPeriodePembayaranPS = (TextView)findViewById(R.id.txtPeriodePembayaranPenyambunganS);
        txtTotalPelangganPS = (TextView)findViewById(R.id.txtTotalPelangganPembayaranPenyambunganS);
        txtTotalPembayaranPS = (TextView)findViewById(R.id.txtTotalPembayaranPenyambunganS);

        new dataPenyambungan().execute();
        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    private class dataPenyambungan extends AsyncTask<String ,String ,String > {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = ProgressDialog.show(PenyambunganActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] penyambungan = null;
            String[] pembayaran = null;
            String PeriodePenyambungan = "";
            String JumlahPermintaanP = "";
            String JumlahTerpasangP = "";
            String JumlahApprovalP = "";
            String PeriodePembayaranPS = "";
            String TotalPelangganPS = "";
            String TotalPembayaranPS = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/PenyambunganKembaliFirst";
            String url2 = "http://183.91.67.198:45103/DashboardTKR/PembayaranPenyambunganKembaliSummary";

            HttpHandler handler = new HttpHandler();

            String kudo = handler.makeServiceCall(url);
            String joni = handler.makeServiceCall(url2);


            Log.e(TAG, "Response from url: " +kudo);
            Log.e(TAG, "Response from url2: " +joni);

            System.out.print(kudo);
            System.out.print(joni);

            penyambungan = showData(kudo);
            pembayaran = showData(joni);

            PeriodePenyambungan = penyambungan[0].replace("Periode : ","");
            JumlahPermintaanP = penyambungan[1].replace("Jumlah Permintaan : ","");
            JumlahTerpasangP = penyambungan[2].replace("Jumlah Terpasang : ","");
            JumlahApprovalP = penyambungan[3].replace("Jumlah Aktif : ","");
            PeriodePembayaranPS = pembayaran[0].replace("Periode : ","");
            TotalPelangganPS = pembayaran[1].replace("Total Pelanggan : ","");
            TotalPembayaranPS = pembayaran[2].replace("Total Pembayaran : ","");

            System.out.print("Periode = " +PeriodePenyambungan);
            System.out.print("Jumlah Permintaan = " +JumlahPermintaanP);
            System.out.print("Jumlah Terpasang  = " +JumlahTerpasangP);
            System.out.print("Jumlah Aktif = " +JumlahApprovalP);
            System.out.print("Periode = " +PeriodePembayaranPS);
            System.out.print("Total Pelanggan = " +TotalPelangganPS);
            System.out.print("Total Pembayaran = " +TotalPembayaranPS);

            final String finalPeriodePenyambungan = PeriodePenyambungan;
            final String finalJumlahPermintaanP = FormatAngka.Angko(Double.valueOf(String.valueOf(JumlahPermintaanP)));
            final String finalJumlahTerpasangP = FormatAngka.Angko(Double.valueOf(String.valueOf(JumlahTerpasangP)));
            final String finalJumlahApprovalP = FormatAngka.Angko(Double.valueOf(String.valueOf(JumlahApprovalP)));
            final String finalPeriodePembayaranPS = PeriodePembayaranPS;
            final String finalTotalPelangganPS = FormatAngka.Angko(Double.valueOf((TotalPelangganPS)));
            final String finalTotalPembayaranPS = FormatAngka.Angko(Double.valueOf((TotalPembayaranPS)));

            PenyambunganActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    txtPeriodePenyambungan.setText(String.valueOf(finalPeriodePenyambungan));
                    txtJumlahPermintaanP.setText(String.valueOf(finalJumlahPermintaanP));
                    txtJumlahTerpasangP.setText(String.valueOf(finalJumlahTerpasangP));
                    txtJumlahApprovalP.setText(String.valueOf(finalJumlahApprovalP));
                    txtPeriodePembayaranPS.setText(String.valueOf(finalPeriodePembayaranPS));
                    txtTotalPelangganPS.setText(String.valueOf(finalTotalPelangganPS));
                    txtTotalPembayaranPS .setText(String.valueOf(finalTotalPembayaranPS));
                }
            });


            return String.valueOf(penyambungan);
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PenyambunganActivity.this);

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
                            PenyambunganActivity.super.onBackPressed();
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
