package com.example.retrofit;

public class Data {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Data.token = token;
    }
}