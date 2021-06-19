package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartPenyewaanActivity extends AppCompatActivity {
    ArrayList<Entry> x;
    ArrayList<String> y;
    private LineChart mChart;
    Preferences sharedPrefManager;
    String id_laboran, new_tahun;
    BaseApiService mApiService;
    Context mContext;
    TextView tv_tahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_penyewaan);

        sharedPrefManager = new Preferences(ChartPenyewaanActivity.this.getApplicationContext());
        id_laboran = sharedPrefManager.getSPId();
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        tv_tahun = (TextView) findViewById(R.id.tahun);
        String tahun = DateFormat.getDateInstance().format(new Date());
        SimpleDateFormat dfThn = new SimpleDateFormat("MMM dd, yyyy");
        Date dateThn = null; //YOUR DATE HERE
        try {
            dateThn = dfThn.parse(String.valueOf(tahun));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dfThn.applyPattern("yyyy");
        String newDateThnPinjam = dfThn.format(dateThn);
        tv_tahun.setText("Tahun "+newDateThnPinjam);
        new_tahun = newDateThnPinjam;

        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
//        mChart.setMarkerView(MarkerView);
        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setInverted(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        getChart();
    }

    public void getChart(){
        mApiService.getChart(id_laboran, new_tahun)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            String myJsonResponse = null; // will contain the json response from the server
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonRESULTS.getJSONArray("sewa2");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int value = object.getInt("value");
                                    String date = object.getString("tgl_pinjam");
                                    SimpleDateFormat dfBln = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateBln = null; //YOUR DATE HERE
                                    try {
                                        dateBln = dfBln.parse(String.valueOf(date));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    dfBln.applyPattern("MMMM");
                                    String newDateBlnPinjam = dfBln.format(dateBln);
                                    x.add(new Entry(value, i));
                                    y.add(newDateBlnPinjam);
                                }

                                LineDataSet set1 = new LineDataSet(x, "Banyak Peminjaman");
                                set1.setLineWidth(2f);
//                                set1.setCircleRadius(4f);
                                LineData data = new LineData(y, set1);
                                mChart.setData(data);
                                mChart.invalidate();

                            } catch (JSONException | IOException e){
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

    public void history(View view) {
        Intent intent = new Intent(ChartPenyewaanActivity.this, ListSewaLaboranActivity.class);
        startActivity(intent);
    }
}