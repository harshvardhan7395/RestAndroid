package com.example.rdetails;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class createrest extends AppCompatActivity {

    EditText rname,add;
    String restname,address;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createrest);

        rname = (EditText) findViewById(R.id.restname);
        add = (EditText) findViewById(R.id.address2);

        Button btn = (Button) findViewById(R.id.create);

        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                try{

                    // CALL GetText method to make post method call
                    SendData();
                }
                catch(Exception ex)
                {
                    Log.e(TAG,"URL exception!");
                }
            }
        });


    }

    public void SendData(){

        restname = rname.getText().toString();
        address = add.getText().toString();

        JSONObject jobj = new JSONObject();

        try{
            jobj.put("name",restname);
            jobj.put("add",address);
            }catch (JSONException e) {
                e.printStackTrace();
            }

            if(jobj.length()>0) {
            new SendJsonToServer().execute(String.valueOf(jobj));
            }
        }

        class SendJsonToServer extends AsyncTask<String,String,String>{

        protected String doInBackground(String...params){
            String JsonResponse;
            String JsonData = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://192.168.0.107:8000/api/createnew/");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept","application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonData);

                writer.close();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                Log.i(TAG,JsonResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }


            return null;
        }
        }



    }
