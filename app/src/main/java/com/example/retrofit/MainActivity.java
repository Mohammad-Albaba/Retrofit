package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private RecyclerView photoRecyclerView;
    private ProgressBar progressBar;
    private List<Photo> photos;
    private PhotosAdapter photosAdapter;
    private NetworkUtils networkUtils;
    private Call<ResponseBody> photosApiCall;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkUtils = NetworkUtils.getInstance(this);
        gson = new Gson();
        photos = new ArrayList<>();
        progressBar = findViewById(R.id.progress_bar);
        photoRecyclerView = findViewById(R.id.recycler);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosAdapter = new PhotosAdapter(photos, new PhotosAdapter.OnLoadMoreLister() {
            @Override
            public void onLoadMore(int page) {
                loadData(page);
            }
        });
        photoRecyclerView.setAdapter(photosAdapter);
        loadData(0);
    }

    public void loadData(int page) {
        progressBar.setVisibility(View.VISIBLE);
        photosApiCall = networkUtils.getPhotosApiInterface().getPhotos(page);
        photosApiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                System.out.println("Photos Response: " + response);
                try {
                    String responseString = response.body().string();
                    Type photoListType = new TypeToken<List<Photo>>() {
                    }.getType();
                    try {
                        List<Photo> tempList = gson.fromJson(responseString, photoListType);
                        if (tempList != null) {
                            if (tempList.size() == 0) {
                                photosAdapter.setLastPage(true);
                            } else {
                                photos.addAll(tempList);
                                photosAdapter.notifyDataSetChanged();
                            }
                            photosAdapter.setLoading(false);
                        }
                    } catch (JsonSyntaxException e) {

                        ErrorResponse errorResponse = gson.fromJson(responseString, ErrorResponse.class);
                        Snackbar.make(findViewById(android.R.id.content), errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                }catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                  progressBar.setVisibility(View.GONE);
                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(findViewById(android.R.id.content) , t.getMessage(),Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        photosApiCall.cancel();
        super.onDestroy();
    }
}