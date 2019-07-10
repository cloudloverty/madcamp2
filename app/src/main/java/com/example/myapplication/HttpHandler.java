package com.example.myapplication;



import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();
    public HttpHandler(){

    }
    public String makeServiceCall(String reqUrl){
        String response = null;
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDefaultUseCaches(false);
            conn.setReadTimeout(10000);
            Log.d("디버깅", "2222222222222222222222222");
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0");
            //connection success the problem wat not permit write or read external
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type","application/x-www-form-urlencoded");
//            conn.setRequestProperty("Accept","text/html");
            conn.setDoInput(true);
//            Log.d("디버깅", "onClick: e");
//            conn.connect(); // this is a problem..
//            ServerSocket serverSocket = new ServerSocket(ServerPort);
//            Socket client = serverSocket.accept();
            Log.d("디버깅", "onClick: c");
            response = convertStreamToString(conn.getInputStream());
            Log.d("디버깅", "onClick: b");
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());//http 안붙였을 때 이런 error 뜸 url 유효하지 않음
            Log.d("디버깅", "onClick: 01");
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            Log.d("디버깅", "onClick: 02");
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            Log.d("디버깅", "onClick: 03");
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            Log.d("디버깅", "onClick: 04");
        }
        return response;
    }
    private String convertStreamToString(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();

        String line="";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException ex){
            return"Network error!";
        }
        return sb.toString();

    }

}