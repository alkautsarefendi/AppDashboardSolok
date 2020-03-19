package com.example.alkautsarefendi.dashboardtkr.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alkautsarefendi.dashboardtkr.R;
import com.example.alkautsarefendi.dashboardtkr.format.FormatRupiah;
import com.example.alkautsarefendi.dashboardtkr.handler.HttpHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String TAG = DashboardActivity.class.getSimpleName();

    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> pieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

    TextView txtMingguIniDs;
    TextView txtBulanIniDs;
    TextView txtTahunInDs;
    TextView txtTotalTransaksiDs;
    TextView txtNominalTransaksiDs;
    TextView txtTotalRek;
    TextView txtRekAir;
    TextView txtPiutang;

    ProgressDialog pDialog;

    public static final String MINGGU_INI = "mingguIni";
    public static final String BULAN_INI = "bulanIni";
    public static final String TAHUN_INI = "tahunIni";
    public static final String TOTAL_TRANSAKSI = "totalTransaksi";
    public static final String NOMINAL_TRANSAKSI = "nominalTransaksi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtMingguIniDs = (TextView) findViewById(R.id.txtMingguIni);
        txtBulanIniDs = (TextView) findViewById(R.id.txtBulanIni);
        txtTahunInDs = (TextView) findViewById(R.id.txtTahunIni);
        txtTotalTransaksiDs = (TextView) findViewById(R.id.txtJumlahTransaksi);
        txtNominalTransaksiDs = (TextView) findViewById(R.id.txtNominalTransaksi);
        txtTotalRek = (TextView) findViewById(R.id.txtTotalRek);
        txtRekAir = (TextView) findViewById(R.id.txtRekAir);
        txtPiutang = (TextView) findViewById(R.id.txtPiutang);


        pieChart = (PieChart) findViewById(R.id.chart1);

        entries = new ArrayList<>();
        pieEntryLabels = new ArrayList<String>();

        AddValuesToPieEntry();
        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData(pieEntryLabels, pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(pieData);
        pieChart.animateY(1000);

        new saveInfo().execute();

        isInternetOn();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HalamanUtamaActivity.class));
    }

    public class saveInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(DashboardActivity.this,"Loading...","Mengambil data",true,false);

        }

        @Override
        protected String doInBackground(String... params) {

            String[] dashboard = null;
            String[] dashboard2 = null;
            String mingguIni = "";
            String bulanIni = "";
            String tahunIni = "";
            String tTransaksi = "";
            String nTransaksi = "";
            String totalRek = "";
            String rekAir = "";
            String piutang = "";

            String url = "http://183.91.67.198:45103/DashboardTKR/Rekapitulasi";
            String url2 = "http://183.91.67.198:45103/DashboardTKR/TagihanBulanIni";

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            String jsonStr2 = sh.makeServiceCall(url2);

            Log.e(TAG, "Response from url : " + jsonStr);
            Log.e(TAG, "Response from url2 : " + jsonStr2);

            System.out.print(jsonStr);
            System.out.print(jsonStr2);

            dashboard = showData(jsonStr);
            dashboard2 = showData(jsonStr2);


            mingguIni = dashboard[0].replace("Minggu ini : ", "");
            bulanIni = dashboard[1].replace("Bulan ini : ", "");
            tahunIni = dashboard[2].replace("Tahun ini : ", "");
            tTransaksi = dashboard[3].replace("Total Transaksi : ", "");
            nTransaksi = dashboard[4].replace("Nominal Transaksi : ", "");
            totalRek = dashboard2[0].replace("Total Rek : ", "");
            rekAir = dashboard2[1].replace("Rek Air : ", "");
            piutang = dashboard2[2].replace("Piutang : ", "");

            System.out.print("\nMinggu ini = " + mingguIni);
            System.out.print("\nBulan ini = " + bulanIni);
            System.out.print("\nTahun ini = " + tahunIni);
            System.out.print("\nTotal Transaksi = " + tTransaksi);
            System.out.print("\nNominal Transaksi = " + nTransaksi);
            System.out.print("\nTotal Rekening = " + totalRek);
            System.out.print("\nRekening Air = " + rekAir);
            System.out.print("\nPiutang = " + piutang);
            System.out.print(DashboardActivity.this.hashCode());

            final String finalMingguIni = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(mingguIni)));
            final String finalBulanIni = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(bulanIni)));
            final String finalTahunIni = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(tahunIni)));
            final String finalTTransaksi = tTransaksi;
            final String finalNTransaksi = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(nTransaksi)));
            final String finalTotalRek = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(totalRek)));
            final String finalRekAir = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(rekAir)));
            final String finalPiutang = FormatRupiah.Rupiah(Double.valueOf(String.valueOf(piutang)));

            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    txtMingguIniDs.setText(String.valueOf(finalMingguIni));
                    txtBulanIniDs.setText(String.valueOf(finalBulanIni));
                    txtTahunInDs.setText(String.valueOf(finalTahunIni));
                    txtTotalTransaksiDs.setText(String.valueOf(finalTTransaksi));
                    txtNominalTransaksiDs.setText(String.valueOf(finalNTransaksi));
                    txtTotalRek.setText(String.valueOf(finalTotalRek));
                    txtRekAir.setText(String.valueOf(finalRekAir));
                    txtPiutang.setText(String.valueOf(finalPiutang));

                }
            });

            return String.valueOf(dashboard);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

        }
    }


    public String[] showData(String resp) {
        //Method Split data
        String[] data = resp.split(Pattern.quote("|"));
        return data;
    }

    private void AddValuesToPieEntry() {
        entries.add(new BarEntry(20, 0));
        entries.add(new BarEntry(80, 1));

    }

    private void AddValuesToPieEntryLabels() {
        pieEntryLabels.add("Rek.Terbayar");
        pieEntryLabels.add("Sisa Rekening");
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
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
