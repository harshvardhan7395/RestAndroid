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
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class createitem extends AppCompatActivity {


    EditText iname,p;
    private int pos;
    String itemname,price;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createitem);

        iname = (EditText) findViewById(R.id.itemname);
        p = (EditText) findViewById(R.id.price);

        Button btn = (Button) findViewById(R.id.create);

        Bundle b = getIntent().getExtras();
        pos = b.getInt("posi");

        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                try{

                    // CALL GetText method to make post method call
                    AddItem();
                }
                catch(Exception ex)
                {
                    Log.e(TAG,"URL exception!");
                }
            }
        });


    }


    public void AddItem(){

        itemname = iname.getText().toString();
        price = p.getText().toString();

        JSONObject jobj = new JSONObject();

        try{
            jobj.put("name1",itemname);
            jobj.put("price",price);
            jobj.put("pos",pos);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        if(jobj.length()>0) {
            new SendItemToServer().execute(String.valueOf(jobj));
        }

    }

    class SendItemToServer extends AsyncTask<String,String,String>{

        protected String doInBackground(String...params){

            String JsonResponse;
            String JsonData = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://192.168.0.107:8000/api/"+pos+"/createnewitems/");
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




            return  null;
        }
    }
}
