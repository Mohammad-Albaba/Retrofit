package com.example.retrofit;

import android.content.Context;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private final String BASE_URL = "https://omaralbelbaisy.com/api/";
   private final String PARAM_TOKEN = "Token" ;



    private PhotosApiInterface photosApiInterface;
    private Context context;

    private static NetworkUtils instance;

    public static NetworkUtils getInstance(Context context){
        if (instance == null){
            instance = new NetworkUtils(context.getApplicationContext());
        }
        return instance;
    }

    private NetworkUtils(Context context){
        this.context = context;
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        Interceptor interceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (!TextUtils.isEmpty(Data.getToken())) {
                     builder.addHeader(PARAM_TOKEN, Data.getToken());
                }
                return chain.proceed(builder.build());
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        photosApiInterface = retrofit.create(PhotosApiInterface.class);

    }

    public PhotosApiInterface getPhotosApiInterface(){
       return photosApiInterface;
    }


//    public String parseVolleyErrors(VolleyError volleyError) {
//        if (volleyError != null ) {
//            NetworkResponse networkResponse = volleyError.networkResponse;
//            if (networkResponse != null){
//                System.out.println("Status Code: " + networkResponse.statusCode);
//            }
//            if (volleyError instanceof NoConnectionError){
//                return context.getString(R.string.error_no_connection);
//            }else if (volleyError instanceof NetworkError){
//                return context.getString(R.string.error_no_network);
//            }else if (volleyError instanceof AuthFailureError){
//                return context.getString(R.string.error_auth_fail);
//            }else if (volleyError instanceof TimeoutError){
//                return context.getString(R.string.error_timeout);
//            }else if (volleyError instanceof ServerError){
//                return context.getString(R.string.error_server);
//            }else{
//                return context.getResources().getString(R.string.general_error);
//            }
//        } else {
//            return context.getResources().getString(R.string.general_error);
//        }
//    }

}