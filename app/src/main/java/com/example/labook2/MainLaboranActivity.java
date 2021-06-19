package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.BeritaAdapter;
import com.example.labook2.adapter.SewaAdapter;
import com.example.labook2.adapter.SewaLabLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BeritaItem;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainLaboranActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="MainLaboranActivity";
    private BottomNavigationView bottomNavigationView;
    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;
    Preferences sharedPrefManager;
    String nama, foto_user;
    int hak_akses, akses_laboran;
    TextView sapa, cal_bulan;
    ImageView foto_profil;
    String id_laboratorium;
    private RecyclerView recyclerView_sewa;
    List<SewaItem> sewaList = new ArrayList<>();
    SewaLabLaboranAdapter sewaLabLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_laboran, description, fcm;
    List<Event> events;
    MaterialCalendarView materialCalendarView;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    SimpleDateFormat dateFormatter;
    String tgl_pinjam, warna;
    Event event;
    SwipeRefreshLayout refreshLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    if (sharedPrefManager.getSPSudahLogin()){
                        Intent intent = new Intent(MainLaboranActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainLaboranActivity.this, NonLoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                case R.id.navigationMyCourses:
                    if (akses_laboran == 1) {
                        Intent intent2 = new Intent(MainLaboranActivity.this, ListSewaLaboranActivity.class);
                        startActivity(intent2);
                    }if (akses_laboran == 2) {
                        Intent intent2 = new Intent(MainLaboranActivity.this, ChartPenyewaanActivity.class);
                        startActivity(intent2);
                    }
                    return true;
                case R.id.navigationHome:
                    return true;
                case  R.id.navigationSearch:
                    if (id_laboratorium != null) {
                        Intent intent1 = new Intent(MainLaboranActivity.this, CrudLaboratoriumActivity.class);
                        startActivity(intent1);
                    }if (id_laboratorium == null) {
                    Intent intent1 = new Intent(MainLaboranActivity.this, TambahLaboratoriumActivity.class);
                    startActivity(intent1);
                    }
                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_laboran);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefManager = new Preferences(MainLaboranActivity.this.getApplicationContext());

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
                        Intent intent = new Intent(MainLaboranActivity.this, MainLaboranActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });


        //handling floating action menu
        sapa = (TextView) findViewById(R.id.textViewUser);
        nama = sharedPrefManager.getSPNama();
        foto_profil = (ImageView) findViewById(R.id.profileCircleImageView);
        foto_user = sharedPrefManager.getSPPict();
        fcm = sharedPrefManager.getFCMToken();

        if (sharedPrefManager.getSPSudahLogin()) {
            sapa.setText("Hello Laboran "+nama+" !");
            Picasso.get().load(foto_user).into(foto_profil);
        } else {
            sapa.setText("Hello !");
            foto_profil.setImageResource(R.drawable.ic_baseline_person_24);
        }

//        recyclerView_sewa = findViewById(R.id.recyclerView_sewa_lab_laboran);
        id_laboran = sharedPrefManager.getSPId();
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        getAkses_laboran();
        getId_laboratorium();

//        sewaLabLaboranAdapter = new SewaLabLaboranAdapter(mContext, sewaList);
//        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
//        recyclerView_sewa.setLayoutManager(mLayoutManager1);

//        getResultSewaLabLaboran();

//        //deklarasi widget yang ada di layout activity_main
//        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
//        materialCalendarView.state().edit()
//                .setFirstDayOfWeek(Calendar.SUNDAY)
//                .setMinimumDate(CalendarDay.from(1900, 1, 1))
//                .setMaximumDate(CalendarDay.from(2045, 12, 31))
//                // Maksud dari MONTHS adalah calender tersebut akan tampil berbentuk 4 minggu atau 1 bulan
//                // jika calendar mode tersebut di ganti menjadi WEEKS maka akan yang tampil akan 1 minggu
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();
//
//        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                //menampilkan toast jika berhasil di klik
//                Toast.makeText(MainLaboranActivity.this, "" + date, Toast.LENGTH_SHORT).show();
//            }
//        });
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
        cal_bulan = (TextView) findViewById(R.id.cal_bulan);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        getCal_sewa();
        Locale localeID = new Locale("in", "ID");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfBln1 = new SimpleDateFormat("MMMM yyyy");
        String formattedBln = dfBln1.format(c.getTime());
//        Date date = new Date(localeID.getCountry());
//        String bulan = DateFormat.getDateInstance().format(date);
//        SimpleDateFormat dfBln = new SimpleDateFormat("dd MMM yyyy");
//        Date dateBln = null; //YOUR DATE HERE
//        try {
//            dateBln = dfBln.parse(bulan);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        dfBln.applyPattern("MMMM yyyy");
//        String newDateBlnPinjam = dfBln.format(dateBln);
        cal_bulan.setText(formattedBln);

        getAkses_laboran();



//        compactCalendar.addEvents(events);
//        compactCalendar.addEvent((com.github.sundeepk.compactcalendarview.domain.Event) events);
//        Set an event for Teachers' Professional Day 2016 which is 21st of October

//        Event ev1 = new Event(Color.RED, 1616457600000L,"Teachers' Professional Day");
//        compactCalendar.addEvent(ev1);
//
//        Event ev2 = new Event(Color.GREEN, 1616640943000L, "laboratorium");
//        compactCalendar.addEvent(ev2);


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                Toast.makeText(MainLaboranActivity.this, "" + dateClicked, Toast.LENGTH_SHORT).show();
//                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
//                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
//                }
                SimpleDateFormat dfTglPinjam = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'z yyyy");
                Date dateTglPinjam = null; //YOUR DATE HERE
                try {
                    dateTglPinjam = dfTglPinjam.parse(String.valueOf(dateClicked));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dfTglPinjam.applyPattern("yyyy-MM-dd");
                String newDateTglPinjam = dfTglPinjam.format(dateTglPinjam);  //Output: newDate = "13/09/2014"
                String tgl = String.valueOf(dateClicked);
                Intent intent = new Intent(MainLaboranActivity.this, CalendarSewaActivity.class);
                intent.putExtra("tgl_pinjam", newDateTglPinjam);

                startActivity(intent);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
                cal_bulan.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

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
                        deleteToken(fcm,id_laboran);
                        sharedPrefManager.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, false);
                        Toast.makeText(MainLaboranActivity.this,"SIGN OUT SUKSES",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainLaboranActivity.this, LoginActivity.class)
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


    public void getCal_sewa(){
        mApiService.getCal_sewa(id_laboran)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            String myJsonResponse = null; // will contain the json response from the server
                            try {
                                JSONArray array = new JSONArray(response.body().string());

                                 events = new ArrayList<>(array.length());

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    long id = Long.parseLong(object.getString("id_peminjaman"));
                                    long epoctTime = Long.parseLong(object.getString("mili"));
                                    tgl_pinjam = object.getString("tgl_pinjam");
                                    int ket_sewa = Integer.parseInt(object.getString("keterangan"));
                                    int ket_pengerjaan = Integer.parseInt(object.getString("ket_pengerjaan"));

                                    if (ket_sewa == 1){
                                        description = "menunggu konfirmasi";
                                        event = new Event(Color.MAGENTA, epoctTime+'L', description);
                                    } if (ket_sewa == 2){
                                        description = "masih dalam pengerjaan";
                                        event = new Event(Color.GREEN, epoctTime+'L', description);
                                    } if (ket_pengerjaan == 2){
                                        description = "Pengerjaan selesai";
                                        event = new Event(Color.RED, epoctTime+'L', description);
                                    }

                                    events.add(event);
                                    compactCalendar.addEvent(event);
                                }

                            } catch (JSONException | IOException e){
                                e.printStackTrace();
                            }
//                            try {
//                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
//                                if (jsonRESULTS.getString("status").equals("true")) {
//
//                                    id_laboratorium = jsonRESULTS.getString("id_laboratorium");
//                                    Toast.makeText(mContext, "Data didapatkan "+id_laboratorium+" sukses", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    String error_message = jsonRESULTS.getString("error_msg");
//                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                        } else {
                            Toast.makeText(mContext, "Data tidak didapatkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

//    private void getResultSewaLabLaboran() {
//        mApiService.getSewaLaboran(id_laboran).enqueue(new Callback<ResponseSewa>() {
//            @Override
//            public void onResponse(Call<ResponseSewa> call, Response<ResponseSewa> response) {
//                if (response.isSuccessful()){
//
//                    final List<SewaItem> sewa = response.body().getSewa();
//
//                    recyclerView_sewa.setAdapter(new SewaLabLaboranAdapter(mContext, sewa));
//                    sewaLabLaboranAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(MainLaboranActivity.this,TentangAplikasiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showAlertDialogLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void getAkses_laboran(){
        mApiService.getAkses_laboran(id_laboran)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    akses_laboran = Integer.parseInt(jsonRESULTS.getString("hak_akses"));
                                    Toast.makeText(mContext, "Akses "+akses_laboran, Toast.LENGTH_SHORT).show();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Data tidak didapatkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    public void notif(View view) {
        Intent notif = new Intent(MainLaboranActivity.this, NotifikasiActivity.class);
        startActivity(notif);
    }

    public void getId_laboratorium(){
        mApiService.getId_laboratorium(id_laboran)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    id_laboratorium = jsonRESULTS.getString("id_laboratorium");
                                    Toast.makeText(mContext, "Data didapatkan "+id_laboratorium+" sukses", Toast.LENGTH_SHORT).show();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Data tidak didapatkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}