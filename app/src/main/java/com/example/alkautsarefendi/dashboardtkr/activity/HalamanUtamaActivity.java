package com.example.alkautsarefendi.dashboardtkr.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alkautsarefendi.dashboardtkr.adapter.AndroidDataAdapter;
import com.example.alkautsarefendi.dashboardtkr.view.AndroidVersion;
import com.example.alkautsarefendi.dashboardtkr.R;
import com.example.alkautsarefendi.dashboardtkr.view.RecyclerItemClickListener;

import java.util.ArrayList;

public class HalamanUtamaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView txtUserHeader;
    LoginActivity loginActivity;
    private String TAG = HalamanUtamaActivity.class.getSimpleName();

    private final String recyclerViewTitleText[] = {
            "DASHBOARD",
            "LOKET",
            "WILAYAH",
            "HARIAN",
            "BACA METER",
            "GANTI METER",
            "PASANG BARU",
            "PASANG BARU SUMMARY",
            "PENYAMBUNGAN KEMBALI",
            "PEMUTUSAN"

    };
    private final int recyclerViewImages[] = {
            R.drawable.dashboard,
            R.drawable.loket,
            R.drawable.wilayah,
            R.drawable.harian,
            R.drawable.bacameter,
            R.drawable.gantimeter,
            R.drawable.pasangbaru,
            R.drawable.pasangbarusummary,
            R.drawable.harian,
            R.drawable.loket
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginActivity = new LoginActivity();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(HalamanUtamaActivity.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        AndroidDataAdapter mAdapter = new AndroidDataAdapter(HalamanUtamaActivity.this, av);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        switch (i){
                            case 0:
                                Intent intent = new Intent(HalamanUtamaActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(HalamanUtamaActivity.this, LoketActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:
                                Intent intent2 = new Intent(HalamanUtamaActivity.this, WilayahActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(HalamanUtamaActivity.this, HarianActivity.class);
                                startActivity(intent3);
                                break;
                            case 4:
                                Intent intent4 = new Intent(HalamanUtamaActivity.this, BacaMeterActivity.class);
                                startActivity(intent4);
                                break;
                            case 5:
                                Intent intent5 = new Intent(HalamanUtamaActivity.this, GantiMeterActivity.class);
                                startActivity(intent5);
                                break;
                            case 6:
                                Intent intent6 = new Intent(HalamanUtamaActivity.this, PasangBaruActivity.class);
                                startActivity(intent6);
                                break;
                            case 7:
                                Intent intent7 = new Intent(HalamanUtamaActivity.this, PasangBaruSummaryActivity.class);
                                startActivity(intent7);
                                break;
                            case 8:
                                Intent intent8 = new Intent(HalamanUtamaActivity.this, PenyambunganActivity.class);
                                startActivity(intent8);
                                break;
                            case 9:
                                Intent intent9 = new Intent(HalamanUtamaActivity.this, PemutusanActivity.class);
                                startActivity(intent9);
                        }
                    }
                })
        );

        initInstances();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        txtUserHeader = (TextView)navigationView.findViewById(R.id.txtUser);

        navigationView.setNavigationItemSelectedListener(this);

        isInternetOn();
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

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
                            HalamanUtamaActivity.super.onBackPressed();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

        alertDialog.setMessage("Keluar dari Aplikasi?");
        alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HalamanUtamaActivity.super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });

        alertDialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Apakah anda yakin ingin Logout?");
            alertDialog.setIcon(R.drawable.exit);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.commit();

                    startActivity(new Intent(HalamanUtamaActivity.this,LoginActivity.class));
                    finish();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();


            /*finish();
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            //starting login activity
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HalamanUtamaActivity.this, HalamanUtamaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(HalamanUtamaActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_loket) {
            Intent intent = new Intent(HalamanUtamaActivity.this, LoketActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wilayah) {
            Intent intent = new Intent(HalamanUtamaActivity.this, WilayahActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_harian) {
            Intent intent = new Intent(HalamanUtamaActivity.this, HarianActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_baca_meter) {
            Intent intent = new Intent(HalamanUtamaActivity.this, BacaMeterActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_ganti_meter) {
            Intent intent = new Intent(HalamanUtamaActivity.this, GantiMeterActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_pasang_baru) {
            Intent intent = new Intent(HalamanUtamaActivity.this, PasangBaruActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_pasang_baru_sumary) {
            Intent intent = new Intent(HalamanUtamaActivity.this, PasangBaruSummaryActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Apakah anda yakin ingin Logout?");
            alertDialog.setIcon(R.drawable.exit);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.commit();

                    startActivity(new Intent(HalamanUtamaActivity.this,LoginActivity.class));
                    finish();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
    }

    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(recyclerViewTitleText[i]);
            mAndroidVersion.setrecyclerViewImage(recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }
}
