package com.example.labook2.apihelper;


import android.content.Context;

import com.example.labook2.Preferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL_API = "https://labook2.000webhostapp.com/";

    public static String getAPI(){
        return BASE_URL_API+ "api/";
    }



    public static BaseApiService getService(Context context){
        final Preferences sharedPrefManager = new Preferences(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request;
                        if (sharedPrefManager.getSPSudahLogin()){
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .addHeader("Authorization","Bearer "+sharedPrefManager.getSPToken())
                                    .build();
                        }else {
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                }).connectTimeout(5, TimeUnit.MINUTES).readTimeout(30, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getAPI())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)

                .build();

        return retrofit.create(BaseApiService.class);
    }
}
