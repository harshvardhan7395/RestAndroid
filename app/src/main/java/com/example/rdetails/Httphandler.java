package com.example.rdetails;

import android.util.Log;
import java.io.*;
import java.net.*;


public class Httphandler {

    private static final String TAG = Httphandler.class.getSimpleName();

    public Httphandler(){}

    public String makeServiceCall(String reqUrl){

        String response = null;
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        }catch (Exception e){
            Log.e(TAG,"Exception: "+e.getMessage());
        }

        return response;
    }

    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try{
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
           try{
               is.close();
           }catch(IOException e){
               e.printStackTrace();
           }
        }
        return  sb.toString();
    }

}
