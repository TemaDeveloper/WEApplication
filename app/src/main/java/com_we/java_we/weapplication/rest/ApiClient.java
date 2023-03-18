package com_we.java_we.weapplication.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String ROOT_URL = "http://192.168.2.245:8888/we_app/";

    public static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance(){
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        if(retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create(/*gson*/))
                    //.client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiService() {
        return getRetrofitInstance().create(ApiInterface.class);
    }

 /*   static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.v("Request ",String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.v("Request ",String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));


            final String responseString = new String(response.body().bytes());

            Log.v("Request ","Response: " + responseString);

            return  response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }*/
}
