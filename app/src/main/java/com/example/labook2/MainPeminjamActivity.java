package com.example.labook2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;

import androidx.appcompat.widget.Toolbar;

import com.example.labook2.adapter.AwalLabAdapter;
import com.example.labook2.adapter.BeritaAdapter;
import com.example.labook2.adapter.BidangAdapter;
import com.example.labook2.adapter.LaboratoriumAdapter;
import com.example.labook2.adapter.SewaAdapter;
import com.example.labook2.adapter.SewaHistoryAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BeritaItem;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.LaboratoriumItem;
import com.example.labook2.model.ResponseBerita;
import com.example.labook2.model.ResponseBidang;
import com.example.labook2.model.ResponseLaboratorium;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPeminjamActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;
    Preferences sharedPrefManager;
    String nama, foto_user;
    int hak_akses;
    TextView sapa;
    ImageView foto_profil;
//    private RecyclerView recyclerView;
    private RecyclerView recyclerView_berita;
    private RecyclerView recyclerView_lab;
    List<LaboratoriumItem> laboratoriumList = new ArrayList<>();
    List<SewaItem> sewaList = new ArrayList<>();
    List<BeritaItem> beritaList = new ArrayList<>();
    SewaAdapter sewaAdapter;
    BeritaAdapter beritaAdapter;
    AwalLabAdapter awalLabAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_peminjam, fcm;
    String layanan, keterangan;
    SwipeRefreshLayout refreshLayout;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    if (sharedPrefManager.getSPSudahLogin()){
                        Intent intent = new Intent(MainPeminjamActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainPeminjamActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                case R.id.navigationMyCourses:
                    if (sharedPrefManager.getSPSudahLogin()){
                        Intent intent = new Intent(MainPeminjamActivity.this, ListSewaActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent2 = new Intent(MainPeminjamActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    }
                    return true;
                case R.id.navigationHome:
                    return true;
                case  R.id.navigationSearch:
                    Intent intent1 = new Intent(MainPeminjamActivity.this,CariLaboratoriumActivity.class);
                    startActivity(intent1);
                    return true;
                case  R.id.navigationMenu:
                    if (sharedPrefManager.getSPSudahLogin()){
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    }else{
                        Intent intent2 = new Intent(MainPeminjamActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setDarkMode(getWindow());

        setContentView(R.layout.activity_main_peminjam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefManager = new Preferences(MainPeminjamActivity.this.getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_green_dark, android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainPeminjamActivity.this, MainPeminjamActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });

        //handling floating action menu
        sapa = (TextView) findViewById(R.id.textViewUser);
        nama = sharedPrefManager.getSPNama();
        foto_profil = (ImageView) findViewById(R.id.profileImageView);
        foto_user = sharedPrefManager.getSPPict();


        if (sharedPrefManager.getSPSudahLogin()) {
            sapa.setText("Hello "+nama+" !");
//            Toast.makeText(mContext, "test "+foto_user, Toast.LENGTH_SHORT).show();
            foto_profil.setImageResource(R.drawable.ic_baseline_person_24);
            if (foto_user != null){
                Picasso.get().load(foto_user).into(foto_profil);
            }

        } else {
            sapa.setText("Hello !");
            foto_profil.setImageResource(R.drawable.ic_baseline_person_24);
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                layanan = query;
                getSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                layanan = newText;
                getSearch();
                return false;
            }
        });

//        recyclerView = findViewById(R.id.recyclerView);
        recyclerView_berita = findViewById(R.id.recyclerView_berita);
        recyclerView_lab = findViewById(R.id.recyclerView_lab);
        id_peminjam = sharedPrefManager.getSPId();
        fcm = sharedPrefManager.getFCMToken();
        keterangan = String.valueOf('1');
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

//        sewaAdapter = new SewaAdapter(mContext, sewaList);
        beritaAdapter = new BeritaAdapter(mContext, beritaList);
//        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
//        recyclerView.setLayoutManager(mLayoutManager);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        recyclerView_berita.setLayoutManager(mLayoutManager1);

        awalLabAdapter = new AwalLabAdapter(mContext, laboratoriumList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        recyclerView_lab.setLayoutManager(mLayoutManager2);

//        getResultSewa();
        getResultBerita();
        getResultLab();
    }

    private void getSearch() {
        mApiService.getSearchLab(layanan).enqueue(new Callback<ResponseLaboratorium>() {
            @Override
            public void onResponse(Call<ResponseLaboratorium> call, Response<ResponseLaboratorium> response) {
                if (response.isSuccessful()){

                    final List<LaboratoriumItem> laboratorium = response.body().getLaboratorium();

                    recyclerView_lab.setAdapter(new AwalLabAdapter(mContext, laboratorium));
                    awalLabAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaboratorium> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getResultLab() {
        mApiService.getLaboratorium().enqueue(new Callback<ResponseLaboratorium>() {
            @Override
            public void onResponse(Call<ResponseLaboratorium> call, Response<ResponseLaboratorium> response) {
                if (response.isSuccessful()){

                    final List<LaboratoriumItem> laboratorium = response.body().getLaboratorium();

                    recyclerView_lab.setAdapter(new AwalLabAdapter(mContext, laboratorium));
                    awalLabAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaboratorium> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getResultSewa() {
//        mApiService.getSewa(id_peminjam,keterangan).enqueue(new Callback<ResponseSewa>() {
//            @Override
//            public void onResponse(Call<ResponseSewa> call, Response<ResponseSewa> response) {
//                if (response.isSuccessful()){
//
//                    final List<SewaItem> sewa = response.body().getSewa();
//
//                    recyclerView.setAdapter(new SewaAdapter(mContext, sewa));
//                    sewaAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseSewa> call, Throwable t) {
//                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void getResultBerita() {
        mApiService.getBerita().enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if (response.isSuccessful()) {

                    final List<BeritaItem> berita = response.body().getBerita();

                    recyclerView_berita.setAdapter(new BeritaAdapter(mContext, berita));
                    beritaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tentang) {
            Intent intent = new Intent(MainPeminjamActivity.this,TentangAplikasiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showAlertDialogLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAlertDialogLogout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));

        // set title dialog
        alertDialogBuilder.setTitle("Log Out Aplikasi");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        deleteToken(fcm,id_peminjam);
                        sharedPrefManager.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, false);
                        Toast.makeText(MainPeminjamActivity.this,"SIGN OUT SUKSES",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainPeminjamActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void deleteToken(String fcm_token,String user_id){
        mApiService.deleteToken(fcm_token,user_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.e("DEBUG", "DELETED");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DEBUG", "Error " +t.getMessage());
            }
        });
    }

    //create a seperate class file, if required in multiple activities
//    public void setDarkMode(Window window){
//        if(new DarkModePrefManager(this).isNightMode()){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            changeStatusBar(MODE_DARK,window);
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            changeStatusBar(MODE_LIGHT,window);
//        }
//    }
//    public void changeStatusBar(int mode, Window window){
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatusBar));
//            //Light mode
//            if(mode==MODE_LIGHT){
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//        }
//    }

    public void lihat_berita(View view) {
        Intent berita = new Intent(MainPeminjamActivity.this, AllBeritaActivity.class);
        startActivity(berita);
    }

    public void notif(View view) {
        Intent notif = new Intent(MainPeminjamActivity.this, NotifikasiActivity.class);
        startActivity(notif);
    }
}
