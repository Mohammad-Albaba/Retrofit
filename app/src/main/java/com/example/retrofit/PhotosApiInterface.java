package com.example.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PhotosApiInterface {
     String LOGIN_BATH = "login.php";
     String DATA_BATH = "data.php";
     String PARAM_USERNAME = "username" ;
     String PARAM_PASSWORD = "password" ;
     String PARAM_PAGE = "page" ;
     @FormUrlEncoded
     @POST(LOGIN_BATH)
     Call<LoginResponse> login(@Field(PARAM_USERNAME) String username,@Field(PARAM_PASSWORD) String password);

     @GET(DATA_BATH)
     Call<ResponseBody> getPhotos(@Query(PARAM_PAGE) int page);
}
